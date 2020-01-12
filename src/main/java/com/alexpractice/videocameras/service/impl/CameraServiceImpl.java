package com.alexpractice.videocameras.service.impl;

import com.alexpractice.videocameras.model.CameraInformation;
import com.alexpractice.videocameras.model.OutputModel;
import com.alexpractice.videocameras.model.SourceData;
import com.alexpractice.videocameras.model.TokenData;
import com.alexpractice.videocameras.service.CameraService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CameraServiceImpl implements CameraService {

    private final RestTemplate restTemplate;
    private final Executor executor;

    private static String infoUrl = "http://www.mocky.io/v2/5c51b9dd3400003252129fb5";


    @Override
    public List<OutputModel> getInfo() {
        val info = restTemplate.getForObject(infoUrl, CameraInformation[].class);
        val infoStream = Optional.ofNullable(info)
                .map(Arrays::stream)
                .orElse(Stream.empty());
        return infoStream.parallel()
                .map(this::mapInfoToOutput)
                .collect(Collectors.toList());
    }

    private OutputModel mapInfoToOutput(CameraInformation info) {
        val result = new OutputModel();
        val sourceDataFuture = CompletableFuture
                .supplyAsync(() -> restTemplate.getForObject(info.getSourceDataUrl(), SourceData.class), executor)
                .exceptionally(throwable -> new SourceData())
                .thenAccept(it -> {
                    result.setUrlType(it.getUrlType());
                    result.setVideoUrl(it.getVideoUrl());
                });
        val tokenDataFuture = CompletableFuture
                .supplyAsync(() -> restTemplate.getForObject(info.getTokenDataUrl(), TokenData.class), executor)
                .exceptionally(throwable -> new TokenData())
                .thenAccept(it -> {
                    result.setTtl(it.getTtl());
                    result.setValue(it.getValue());
                });
        result.setId(info.getId());
        val commonFuture = CompletableFuture.allOf(sourceDataFuture, tokenDataFuture);
        commonFuture.join();
        return result;
    }

}

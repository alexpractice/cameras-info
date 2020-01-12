package com.alexpractice.videocameras.service.impl;

import com.alexpractice.videocameras.model.CameraInformation;
import com.alexpractice.videocameras.model.SourceData;
import com.alexpractice.videocameras.model.TokenData;
import com.alexpractice.videocameras.model.enums.UrlType;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CameraServiceImplTest {

    @InjectMocks
    private CameraServiceImpl cameraService;

    @Mock
    private RestTemplate restTemplate;

    private Executor executor = Executors.newFixedThreadPool(5);

    private CameraInformation[] infoArray;
    private SourceData src;
    private TokenData tokenData;

    private Long cameraId = 112L;
    private String srcUrl = "SRC";
    private String tokenUrl = "TOKEN";
    private String videoUrl = "vvvv";
    private UrlType urlType = UrlType.LIVE;
    private Integer ttl = 15;
    private String value = "fdggggjuj";


    @BeforeEach
    public void beforeTest() {
        ReflectionTestUtils.setField(cameraService, "executor", executor);

        val info = new CameraInformation();
        info.setId(cameraId);
        info.setSourceDataUrl(srcUrl);
        info.setTokenDataUrl(tokenUrl);

        infoArray = new CameraInformation[1];
        infoArray[0] = info;

        src = new SourceData();
        src.setUrlType(urlType);
        src.setVideoUrl(videoUrl);
        tokenData = new TokenData();
        tokenData.setTtl(ttl);
        tokenData.setValue(value);
    }

    @Test
    public void shouldHandleNullAnswer() {
        when(restTemplate.getForObject(anyString(), eq(CameraInformation[].class))).thenReturn(null);
        val result = cameraService.getInfo();
        verify(restTemplate, atLeastOnce()).getForObject(anyString(), eq(CameraInformation[].class));
        Assertions.assertTrue(CollectionUtils.isEmpty(result));
        Assertions.assertTrue(Objects.nonNull(result));
    }

    @Test
    public void shouldGetInfo() {
        //when
        when(restTemplate.getForObject(anyString(), eq(CameraInformation[].class))).thenReturn(infoArray);
        when(restTemplate.getForObject(eq(srcUrl), eq(SourceData.class))).thenReturn(src);
        when(restTemplate.getForObject(eq(tokenUrl), eq(TokenData.class))).thenReturn(tokenData);
        //test
        val result = cameraService.getInfo();
        //then
        verify(restTemplate, atLeastOnce()).getForObject(anyString(), eq(CameraInformation[].class));
        verify(restTemplate, atLeastOnce()).getForObject(eq(srcUrl), eq(SourceData.class));
        verify(restTemplate, atLeastOnce()).getForObject(eq(tokenUrl), eq(TokenData.class));
        Assertions.assertEquals(1, result.size());
        val instance = result.get(0);
        Assertions.assertEquals(cameraId, instance.getId());
        Assertions.assertEquals(urlType, instance.getUrlType());
        Assertions.assertEquals(videoUrl, instance.getVideoUrl());
        Assertions.assertEquals(value, instance.getValue());
        Assertions.assertEquals(ttl, instance.getTtl());
    }

    @Test
    public void shouldHandleException() {
        //then
        when(restTemplate.getForObject(anyString(), eq(CameraInformation[].class))).thenReturn(infoArray);
        when(restTemplate.getForObject(eq(srcUrl), eq(SourceData.class))).thenThrow(new RuntimeException());
        when(restTemplate.getForObject(eq(tokenUrl), eq(TokenData.class))).thenReturn(tokenData);
        val result = cameraService.getInfo();
        verify(restTemplate, atLeastOnce()).getForObject(anyString(), eq(CameraInformation[].class));
        verify(restTemplate, atLeastOnce()).getForObject(eq(srcUrl), eq(SourceData.class));
        verify(restTemplate, atLeastOnce()).getForObject(eq(tokenUrl), eq(TokenData.class));
        Assertions.assertEquals(1, result.size());
        val instance = result.get(0);
        Assertions.assertEquals(cameraId, instance.getId());
        Assertions.assertNull(instance.getUrlType());
        Assertions.assertNull(instance.getVideoUrl());
        Assertions.assertEquals(value, instance.getValue());
        Assertions.assertEquals(ttl, instance.getTtl());
    }
}
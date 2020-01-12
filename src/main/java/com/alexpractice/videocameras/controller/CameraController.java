package com.alexpractice.videocameras.controller;

import com.alexpractice.videocameras.model.OutputModel;
import com.alexpractice.videocameras.service.CameraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CameraController {

    private final CameraService cameraService;

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OutputModel> getInfo() {
        return cameraService.getInfo();
    }
}

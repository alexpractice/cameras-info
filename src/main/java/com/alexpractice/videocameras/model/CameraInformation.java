package com.alexpractice.videocameras.model;

import lombok.Data;

@Data
public class CameraInformation {
    private Long id;
    private String sourceDataUrl; // строка, ссылка для получения данных источника.
    private String tokenDataUrl; // строка, ссылка для получения токенов безопасности по камере.
}

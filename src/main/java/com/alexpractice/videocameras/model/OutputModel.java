package com.alexpractice.videocameras.model;

import com.alexpractice.videocameras.model.enums.UrlType;
import lombok.Data;

@Data
public class OutputModel {
    private Long id;
    private UrlType urlType;
    private String videoUrl;
    private String value;
    private Integer ttl;
}

package com.alexpractice.videocameras.model;

import lombok.Data;

@Data
public class TokenData {
    private String value;
    private Integer ttl;
}
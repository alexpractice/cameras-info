package com.alexpractice.videocameras.model;

import com.alexpractice.videocameras.model.enums.UrlType;
import lombok.Data;

@Data
public class SourceData {
    private UrlType urlType; //- строка, тип ссылки на видеопоток. Возможные значения: "LIVE","ARCHIVE"
    private String videoUrl; //- строка, ссылка на видеопоток
}

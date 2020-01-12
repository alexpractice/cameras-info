package com.alexpractice.videocameras;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VideoCamerasApplication {

    public static void main(String[] args) {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
        SpringApplication.run(VideoCamerasApplication.class, args);
    }

}

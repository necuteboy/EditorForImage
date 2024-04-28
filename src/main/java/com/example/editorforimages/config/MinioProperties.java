package com.example.editorforimages.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    private String url;

    private String accessKey;

    private String secretKey;

    private String bucket;

    private Long imageSize;

}
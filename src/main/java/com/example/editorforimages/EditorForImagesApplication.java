package com.example.editorforimages;

import com.example.editorforimages.config.JwtProperties;
import com.example.editorforimages.config.MinioProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties({MinioProperties.class, JwtProperties.class})
@Slf4j
public class EditorForImagesApplication {

    public static void main(final String[] args) {
        SpringApplication.run(EditorForImagesApplication.class, args);
    }

}

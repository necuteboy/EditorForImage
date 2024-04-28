package com.example.editorforimages.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private final String secret;
    private final Duration expirationMs;

    public JwtProperties(final String secret, final Duration expirationMs) {
        this.secret = secret;
        this.expirationMs = expirationMs;
    }
}

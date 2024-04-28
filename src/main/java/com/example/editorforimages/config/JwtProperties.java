package com.example.editorforimages.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
@SuppressWarnings("FinalParameters")
public class JwtProperties {
    private String secret;
    private Duration expirationMs;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Duration getExpirationMs() {
        return expirationMs;
    }

    public void setExpirationMs(Duration expirationMs) {
        this.expirationMs = expirationMs;
    }
}

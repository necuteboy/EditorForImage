package com.example.editorforimages.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageResponse {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    public Image[] images;
}

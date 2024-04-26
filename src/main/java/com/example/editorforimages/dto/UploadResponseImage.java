package com.example.editorforimages.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class UploadResponseImage {
    @Schema(description = "ИД файла", requiredMode = Schema.RequiredMode.REQUIRED)
    public UUID imageId;
}

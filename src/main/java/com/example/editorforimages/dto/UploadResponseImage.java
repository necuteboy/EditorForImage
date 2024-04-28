package com.example.editorforimages.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class UploadResponseImage {
    @Schema(description = "ИД файла", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID imageId;
}

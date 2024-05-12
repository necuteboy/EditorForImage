package com.example.editorforimages.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ImageWipMessage {

    private UUID imageId;

    private UUID requestId;

    private FilterType[] filters;

}

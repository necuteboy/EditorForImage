package com.example.editorforimages.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class AppError {
    private final int status;
    private final String message;
    private Date timestamp;

    public AppError(final int status, final String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}

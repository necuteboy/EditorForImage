package com.example.editorforimages.exceptions;

public class UnprocessableEntityException extends RuntimeException {
    public UnprocessableEntityException(final String message) {
        super(message);
    }
}

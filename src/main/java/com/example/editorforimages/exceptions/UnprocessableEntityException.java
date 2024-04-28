package com.example.editorforimages.exceptions;

public class UnprocessableEntityException extends RuntimeException{
    public UnprocessableEntityException(String message) {
        super(message);
    }
}

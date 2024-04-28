package com.example.editorforimages.exceptions;

public class FileWriteException extends RuntimeException{
    public FileWriteException() {
    }

    public FileWriteException(String message) {
        super(message);
    }

    public FileWriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileWriteException(Throwable cause) {
        super(cause);
    }
}

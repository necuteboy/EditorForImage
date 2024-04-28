package com.example.editorforimages.exceptions;

public class FileWriteException extends RuntimeException {
    public FileWriteException() {
    }

    public FileWriteException(final String message) {
        super(message);
    }

    public FileWriteException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FileWriteException(final Throwable cause) {
        super(cause);
    }
}

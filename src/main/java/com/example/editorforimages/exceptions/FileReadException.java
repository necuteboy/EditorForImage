package com.example.editorforimages.exceptions;

public class FileReadException extends RuntimeException {
    public FileReadException() {
    }

    public FileReadException(final String message) {
        super(message);
    }

    public FileReadException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FileReadException(final Throwable cause) {
        super(cause);
    }
}

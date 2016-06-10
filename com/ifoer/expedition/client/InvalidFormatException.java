package com.ifoer.expedition.client;

public class InvalidFormatException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public InvalidFormatException(String message) {
        super(message);
    }

    public InvalidFormatException(Throwable cause) {
        super(cause);
    }

    public InvalidFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}

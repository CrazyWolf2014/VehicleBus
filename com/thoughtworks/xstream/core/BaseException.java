package com.thoughtworks.xstream.core;

public abstract class BaseException extends RuntimeException {
    public abstract Throwable getCause();

    protected BaseException(String message) {
        super(message);
    }
}

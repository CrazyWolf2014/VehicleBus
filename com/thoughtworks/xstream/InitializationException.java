package com.thoughtworks.xstream;

public class InitializationException extends com.thoughtworks.xstream.XStream.InitializationException {
    public InitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitializationException(String message) {
        super(message);
    }
}

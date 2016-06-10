package com.cnlaunch.framework.network.http;

public class HttpException extends Exception {
    private static final long serialVersionUID = 4010634120321127684L;

    public HttpException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public HttpException(String detailMessage) {
        super(detailMessage);
    }

    public HttpException(Throwable throwable) {
        super(throwable);
    }
}

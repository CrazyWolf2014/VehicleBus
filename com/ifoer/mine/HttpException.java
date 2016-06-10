package com.ifoer.mine;

public class HttpException extends Exception {
    public static final int CODE_NO_RESPONSE = 0;
    public static final int CODE_TIME_OUT = -1;
    private int code;

    public HttpException(int code) {
        this.code = code;
    }

    public HttpException(String detailMessage, int code) {
        super(detailMessage);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}

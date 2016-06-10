package com.ifoer.mine;

public class DecodeException extends Exception {
    public static final int CODE_NO_RESPONSE = 0;
    public static final int CODE_NULL_POINT = 100;
    private int code;

    public DecodeException(int code) {
        this.code = code;
    }

    public DecodeException(String detailMessage, int code) {
        super(detailMessage);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}

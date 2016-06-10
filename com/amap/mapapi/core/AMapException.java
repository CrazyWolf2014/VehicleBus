package com.amap.mapapi.core;

public class AMapException extends Exception {
    public static final String ERROR_CONNECTION = "http\u8fde\u63a5\u5931\u8d25 - ConnectionException";
    public static final String ERROR_INVALID_PARAMETER = "\u65e0\u6548\u7684\u53c2\u6570 - IllegalArgumentException";
    public static final String ERROR_IO = "IO \u64cd\u4f5c\u5f02\u5e38 - IOException";
    public static final String ERROR_NULL_PARAMETER = "\u7a7a\u6307\u9488\u5f02\u5e38 - NullPointException";
    public static final String ERROR_PROTOCOL = "\u534f\u8bae\u89e3\u6790\u9519\u8bef - ProtocolException";
    public static final String ERROR_SOCKET = "socket \u8fde\u63a5\u5f02\u5e38 - SocketException";
    public static final String ERROR_SOCKE_TIME_OUT = "socket \u8fde\u63a5\u8d85\u65f6 - SocketTimeoutException";
    public static final String ERROR_UNKNOWN = "\u672a\u77e5\u7684\u9519\u8bef";
    public static final String ERROR_UNKNOW_HOST = "\u672a\u77e5\u4e3b\u673a - UnKnowHostException";
    public static final String ERROR_UNKNOW_SERVICE = "\u670d\u52a1\u5668\u8fde\u63a5\u5931\u8d25 - UnknownServiceException";
    public static final String ERROR_URL = "url\u5f02\u5e38 - MalformedURLException";
    private String f299a;

    public AMapException(String str) {
        this.f299a = ERROR_UNKNOWN;
        this.f299a = str;
    }

    public AMapException() {
        this.f299a = ERROR_UNKNOWN;
    }

    public String getErrorMessage() {
        return this.f299a;
    }
}

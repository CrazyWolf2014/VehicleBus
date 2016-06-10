package com.kenai.jbosh;

public class BOSHException extends Exception {
    private static final long serialVersionUID = 1;

    public BOSHException(String str) {
        super(str);
    }

    public BOSHException(String str, Throwable th) {
        super(str, th);
    }
}

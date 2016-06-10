package org.jivesoftware.smack.util;

public class Base64Encoder implements StringEncoder {
    private static Base64Encoder instance;

    private Base64Encoder() {
    }

    public static Base64Encoder getInstance() {
        if (instance == null) {
            instance = new Base64Encoder();
        }
        return instance;
    }

    public String encode(String str) {
        return Base64.encodeBytes(str.getBytes());
    }

    public String decode(String str) {
        return new String(Base64.decode(str));
    }
}

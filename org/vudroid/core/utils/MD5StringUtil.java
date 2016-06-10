package org.vudroid.core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.xbill.DNS.KEYRecord;

public class MD5StringUtil {
    private static final MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5StringFor(String s) {
        byte[] hash = digest.digest(s.getBytes());
        StringBuilder builder = new StringBuilder();
        for (byte b : hash) {
            builder.append(Integer.toString(b & KEYRecord.PROTOCOL_ANY, 16));
        }
        return builder.toString();
    }
}

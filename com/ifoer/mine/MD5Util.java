package com.ifoer.mine;

import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public class MD5Util {
    public static String MD5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            try {
                digest.update(s.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] messageDigest = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (byte b : messageDigest) {
                String h = Integer.toHexString(b & KEYRecord.PROTOCOL_ANY);
                while (h.length() < 2) {
                    h = new StringBuilder(Contact.RELATION_ASK).append(h).toString();
                }
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
            return XmlPullParser.NO_NAMESPACE;
        }
    }
}

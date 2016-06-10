package com.ifoer.md5;

import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.ifoer.mine.Contact;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.xbill.DNS.KEYRecord;

public class MD5 {
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(byteArray[i] & KEYRecord.PROTOCOL_ANY).length() == 1) {
                md5StrBuff.append(Contact.RELATION_ASK).append(Integer.toHexString(byteArray[i] & KEYRecord.PROTOCOL_ANY));
            } else {
                md5StrBuff.append(Integer.toHexString(byteArray[i] & KEYRecord.PROTOCOL_ANY));
            }
        }
        return md5StrBuff.toString();
    }

    public static String encrypt(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(byteArray[i] & KEYRecord.PROTOCOL_ANY).length() == 1) {
                md5StrBuff.append(Contact.RELATION_ASK).append(Integer.toHexString(byteArray[i] & KEYRecord.PROTOCOL_ANY));
            } else {
                md5StrBuff.append(Integer.toHexString(byteArray[i] & KEYRecord.PROTOCOL_ANY));
            }
        }
        return md5StrBuff.toString();
    }

    public static String signByMD5(String source) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(source.getBytes());
        return toHexString(md5.digest());
    }

    private static String toHexString(byte[] source) {
        char[] hexChars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] hexResult = new char[(source.length * 2)];
        int index = 0;
        for (byte b : source) {
            int i = index + 1;
            hexResult[index] = hexChars[(b >>> 4) & 15];
            index = i + 1;
            hexResult[i] = hexChars[b & 15];
        }
        return new String(hexResult);
    }
}

package com.cnlaunch.x431pro.utils;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DESUtils {
    public static byte[] desCrypto(byte[] datasource, String password) {
        try {
            SecureRandom random = new SecureRandom();
            SecretKey securekey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(password.getBytes()));
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(1, securekey, random);
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] decrypt(byte[] src, String password) throws Exception {
        SecureRandom random = new SecureRandom();
        SecretKey securekey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(password.getBytes()));
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(2, securekey, random);
        return cipher.doFinal(src);
    }
}

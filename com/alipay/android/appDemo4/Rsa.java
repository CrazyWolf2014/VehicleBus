package com.alipay.android.appDemo4;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Rsa {
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    public static String sign(String content, String privateKey) {
        String charset = "utf-8";
        try {
            PrivateKey priKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(privateKey)));
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(charset));
            return Base64.encode(signature.sign());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean doCheck(String content, String sign, String publicKey) {
        try {
            PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(publicKey)));
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes("utf-8"));
            return signature.verify(Base64.decode(sign));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

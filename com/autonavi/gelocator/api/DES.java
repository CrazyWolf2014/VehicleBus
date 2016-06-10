package com.autonavi.gelocator.api;

import com.ifoer.mine.Contact;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord;

public class DES {
    private static String f748a;
    private Cipher f749b;
    private Cipher f750c;

    static {
        f748a = "DESede";
    }

    public DES(String str) {
        this.f749b = null;
        this.f750c = null;
        try {
            SecureRandom secureRandom = new SecureRandom();
            Key generateSecret = SecretKeyFactory.getInstance(f748a).generateSecret(new DESedeKeySpec(str.getBytes("utf-8")));
            this.f749b = Cipher.getInstance(f748a);
            this.f749b.init(1, generateSecret, secureRandom);
            this.f750c = Cipher.getInstance(f748a);
            this.f750c.init(2, generateSecret, secureRandom);
        } catch (Exception e) {
        }
    }

    public static void main(String[] strArr) {
        String str = "d57e33aede82131489f5cf911cdc0d47e8da3c5d1caf30260f70531a9a08abc9978f33f2d3022b4e9f0d4f451c6ff1c6976149b1b4daf8ab9283721d90f1a9b9cacdca301eec7afc060c9a67fde2cef409fb8a8464f1ebd37134daab2b091708cc3eaf3f520c7e51da73e234ddee6cd5ded3922fc69adabf5429aca6a1f2b2eff7bc30e248ba6e02bef54705a820ad055627fac174ec5421754fdb24974ca9907dadaa75f36e457102c3aa75c068ee658ac8f0434ff7d5a2519598f13782cebbbf84106b8fb3305b499d82db19477e26f7c740dfcf322a20d54d263ef47679b8f5ac1fa55443255512c254ca3202f26f88f626164b0133b5269e3fc0e27d9106e423cf904620f458b4c2db7f5dfd2ca20beedb4850303d32";
        String decrypt = new DES("autonavi00spas$#@!666666").decrypt(str, "GBK");
        System.out.println("\u89e3\u5bc6\u5bc6\u540e===>" + decrypt + SpecilApiUtil.LINE_SEP + str.length() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + decrypt.length());
    }

    public String byte2hex(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArr) {
            String toHexString = Integer.toHexString(b & KEYRecord.PROTOCOL_ANY);
            if (toHexString.length() == 1) {
                stringBuffer.append(new StringBuilder(Contact.RELATION_ASK).append(toHexString).toString());
            } else {
                stringBuffer.append(toHexString);
            }
        }
        return stringBuffer.toString();
    }

    public String decrypt(String str, String str2) {
        return new String(this.f750c.doFinal(hex2byte(str)), str2);
    }

    public String encrypt(String str) {
        return byte2hex(this.f749b.doFinal(str.getBytes("utf-8")));
    }

    public byte[] hex2byte(String str) {
        if (str == null) {
            return null;
        }
        String trim = str.trim();
        int length = trim.length();
        if (length == 0 || length % 2 == 1) {
            return null;
        }
        byte[] bArr = new byte[(length / 2)];
        int i = 0;
        while (i < trim.length()) {
            try {
                bArr[i / 2] = (byte) Integer.decode("0X" + trim.substring(i, i + 2)).intValue();
                i += 2;
            } catch (Exception e) {
                return null;
            }
        }
        return bArr;
    }
}

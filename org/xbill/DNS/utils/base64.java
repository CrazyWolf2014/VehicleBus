package org.xbill.DNS.utils;

import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.xbill.DNS.KEYRecord;

public class base64 {
    private static final String Base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    private base64() {
    }

    public static String toString(byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i < (bArr.length + 2) / 3; i++) {
            int i2;
            short[] sArr = new short[3];
            short[] sArr2 = new short[4];
            for (i2 = 0; i2 < 3; i2++) {
                if ((i * 3) + i2 < bArr.length) {
                    sArr[i2] = (short) (bArr[(i * 3) + i2] & KEYRecord.PROTOCOL_ANY);
                } else {
                    sArr[i2] = (short) -1;
                }
            }
            sArr2[0] = (short) (sArr[0] >> 2);
            if (sArr[1] == (short) -1) {
                sArr2[1] = (short) ((sArr[0] & 3) << 4);
            } else {
                sArr2[1] = (short) (((sArr[0] & 3) << 4) + (sArr[1] >> 4));
            }
            if (sArr[1] == (short) -1) {
                sArr2[3] = (short) 64;
                sArr2[2] = (short) 64;
            } else if (sArr[2] == (short) -1) {
                sArr2[2] = (short) ((sArr[1] & 15) << 2);
                sArr2[3] = (short) 64;
            } else {
                sArr2[2] = (short) (((sArr[1] & 15) << 2) + (sArr[2] >> 6));
                sArr2[3] = (short) (sArr[2] & 63);
            }
            for (i2 = 0; i2 < 4; i2++) {
                byteArrayOutputStream.write(Base64.charAt(sArr2[i2]));
            }
        }
        return new String(byteArrayOutputStream.toByteArray());
    }

    public static String formatString(byte[] bArr, int i, String str, boolean z) {
        String org_xbill_DNS_utils_base64 = toString(bArr);
        StringBuffer stringBuffer = new StringBuffer();
        int i2 = 0;
        while (i2 < org_xbill_DNS_utils_base64.length()) {
            stringBuffer.append(str);
            if (i2 + i >= org_xbill_DNS_utils_base64.length()) {
                stringBuffer.append(org_xbill_DNS_utils_base64.substring(i2));
                if (z) {
                    stringBuffer.append(" )");
                }
            } else {
                stringBuffer.append(org_xbill_DNS_utils_base64.substring(i2, i2 + i));
                stringBuffer.append(SpecilApiUtil.LINE_SEP);
            }
            i2 += i;
        }
        return stringBuffer.toString();
    }

    public static byte[] fromString(String str) {
        int i;
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = str.getBytes();
        for (i = 0; i < bytes.length; i++) {
            if (!Character.isWhitespace((char) bytes[i])) {
                byteArrayOutputStream.write(bytes[i]);
            }
        }
        byte[] toByteArray = byteArrayOutputStream.toByteArray();
        if (toByteArray.length % 4 != 0) {
            return null;
        }
        byteArrayOutputStream.reset();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        for (i = 0; i < (toByteArray.length + 3) / 4; i++) {
            int i2;
            short[] sArr = new short[4];
            short[] sArr2 = new short[3];
            for (i2 = 0; i2 < 4; i2++) {
                sArr[i2] = (short) Base64.indexOf(toByteArray[(i * 4) + i2]);
            }
            sArr2[0] = (short) ((sArr[0] << 2) + (sArr[1] >> 4));
            if (sArr[2] == (short) 64) {
                sArr2[2] = (short) -1;
                sArr2[1] = (short) -1;
                if ((sArr[1] & 15) != 0) {
                    return null;
                }
            } else if (sArr[3] == (short) 64) {
                sArr2[1] = (short) (((sArr[1] << 4) + (sArr[2] >> 2)) & KEYRecord.PROTOCOL_ANY);
                sArr2[2] = (short) -1;
                if ((sArr[2] & 3) != 0) {
                    return null;
                }
            } else {
                sArr2[1] = (short) (((sArr[1] << 4) + (sArr[2] >> 2)) & KEYRecord.PROTOCOL_ANY);
                sArr2[2] = (short) (((sArr[2] << 6) + sArr[3]) & KEYRecord.PROTOCOL_ANY);
            }
            i2 = 0;
            while (i2 < 3) {
                try {
                    if (sArr2[i2] >= (short) 0) {
                        dataOutputStream.writeByte(sArr2[i2]);
                    }
                    i2++;
                } catch (IOException e) {
                }
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
}

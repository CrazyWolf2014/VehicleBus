package cn.sharesdk.framework.utils;

import android.util.Base64;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.net.URLEncoder;
import java.security.Key;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: cn.sharesdk.framework.utils.a */
public class C0052a {
    private static C0055d f100a;

    static {
        f100a = new C0055d();
    }

    public static String m165a(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bArr.length; i++) {
            stringBuffer.append(String.format("%02x", new Object[]{Byte.valueOf(bArr[i])}));
        }
        return stringBuffer.toString();
    }

    public static byte[] m166a(String str) {
        byte[] bytes = str.getBytes("utf-8");
        MessageDigest instance = MessageDigest.getInstance("SHA-1");
        instance.update(bytes);
        return instance.digest();
    }

    public static byte[] m167a(String str, String str2) {
        Object bytes = str.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET);
        Object obj = new byte[16];
        System.arraycopy(bytes, 0, obj, 0, Math.min(bytes.length, 16));
        byte[] bytes2 = str2.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET);
        Key secretKeySpec = new SecretKeySpec(obj, "AES");
        Cipher instance = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        instance.init(1, secretKeySpec);
        byte[] bArr = new byte[instance.getOutputSize(bytes2.length)];
        instance.doFinal(bArr, instance.update(bytes2, 0, bytes2.length, bArr, 0));
        return bArr;
    }

    public static byte[] m168a(byte[] bArr, String str) {
        byte[] bytes = str.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET);
        Key secretKeySpec = new SecretKeySpec(bArr, "AES");
        Cipher instance = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        instance.init(1, secretKeySpec);
        byte[] bArr2 = new byte[instance.getOutputSize(bytes.length)];
        instance.doFinal(bArr2, instance.update(bytes, 0, bytes.length, bArr2, 0));
        return bArr2;
    }

    public static byte[] m169a(byte[] bArr, byte[] bArr2) {
        Object obj = new byte[16];
        System.arraycopy(bArr, 0, obj, 0, Math.min(bArr.length, 16));
        Key secretKeySpec = new SecretKeySpec(obj, "AES");
        Cipher instance = Cipher.getInstance("AES/ECB/NoPadding", "BC");
        instance.init(2, secretKeySpec);
        byte[] bArr3 = new byte[instance.getOutputSize(bArr2.length)];
        int update = instance.update(bArr2, 0, bArr2.length, bArr3, 0);
        int doFinal = instance.doFinal(bArr3, update) + update;
        return bArr3;
    }

    public static String m170b(String str) {
        if (str == null) {
            return null;
        }
        byte[] c = C0052a.m173c(str);
        return c != null ? C0054c.m204a(c) : null;
    }

    public static String m171b(String str, String str2) {
        String encodeToString;
        Throwable th;
        try {
            encodeToString = Base64.encodeToString(C0052a.m167a(str2, str), 0);
            try {
                if (encodeToString.contains(SpecilApiUtil.LINE_SEP)) {
                    encodeToString = encodeToString.replace(SpecilApiUtil.LINE_SEP, XmlPullParser.NO_NAMESPACE);
                }
            } catch (Throwable th2) {
                th = th2;
                C0058e.m220b(th);
                return encodeToString;
            }
        } catch (Throwable th3) {
            Throwable th4 = th3;
            encodeToString = null;
            th = th4;
            C0058e.m220b(th);
            return encodeToString;
        }
        return encodeToString;
    }

    public static String m172c(String str, String str2) {
        return URLEncoder.encode(str, str2).replace("\\+", "%20");
    }

    public static byte[] m173c(String str) {
        byte[] bArr = null;
        if (str != null) {
            try {
                MessageDigest instance = MessageDigest.getInstance("MD5");
                instance.update(str.getBytes("utf-8"));
                bArr = instance.digest();
            } catch (Throwable e) {
                C0058e.m220b(e);
            }
        }
        return bArr;
    }

    public static String m174d(String str) {
        return URLEncoder.encode(str).replace("\\+", "%20");
    }
}

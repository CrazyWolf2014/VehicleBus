package com.iflytek.msc.p013f;

import com.ifoer.mine.Contact;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.msc.f.f */
public class C0277f {
    public static synchronized String m1225a(String str) {
        String stringBuffer;
        int i = 0;
        synchronized (C0277f.class) {
            try {
                MessageDigest instance = MessageDigest.getInstance("MD5");
                char[] toCharArray = str.toCharArray();
                byte[] bArr = new byte[toCharArray.length];
                for (int i2 = 0; i2 < toCharArray.length; i2++) {
                    bArr[i2] = (byte) toCharArray[i2];
                }
                byte[] digest = instance.digest(bArr);
                StringBuffer stringBuffer2 = new StringBuffer();
                while (i < digest.length) {
                    int i3 = digest[i] & KEYRecord.PROTOCOL_ANY;
                    if (i3 < 16) {
                        stringBuffer2.append(Contact.RELATION_ASK);
                    }
                    stringBuffer2.append(Integer.toHexString(i3));
                    i++;
                }
                stringBuffer = stringBuffer2.toString();
            } catch (Exception e) {
                e.printStackTrace();
                stringBuffer = XmlPullParser.NO_NAMESPACE;
            }
        }
        return stringBuffer;
    }

    public static byte[] m1226a(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (bArr[i] ^ 5);
        }
        return bArr;
    }

    public static byte[] m1227b(byte[] bArr) {
        GZIPOutputStream gZIPOutputStream;
        IOException e;
        Throwable th;
        byte[] bArr2 = null;
        if (bArr != null) {
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                try {
                    gZIPOutputStream.write(bArr);
                    gZIPOutputStream.finish();
                    gZIPOutputStream.flush();
                    bArr2 = byteArrayOutputStream.toByteArray();
                    for (int i = 0; i < bArr2.length; i++) {
                        bArr2[i] = (byte) (bArr2[i] ^ 5);
                    }
                    if (gZIPOutputStream != null) {
                        try {
                            gZIPOutputStream.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                } catch (IOException e3) {
                    e2 = e3;
                    try {
                        e2.printStackTrace();
                        if (gZIPOutputStream != null) {
                            try {
                                gZIPOutputStream.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                        if (byteArrayOutputStream != null) {
                            byteArrayOutputStream.close();
                        }
                        return bArr2;
                    } catch (Throwable th2) {
                        th = th2;
                        if (gZIPOutputStream != null) {
                            try {
                                gZIPOutputStream.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                                throw th;
                            }
                        }
                        if (byteArrayOutputStream != null) {
                            byteArrayOutputStream.close();
                        }
                        throw th;
                    }
                }
            } catch (IOException e4) {
                e222 = e4;
                gZIPOutputStream = null;
                e222.printStackTrace();
                if (gZIPOutputStream != null) {
                    gZIPOutputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                return bArr2;
            } catch (Throwable th3) {
                gZIPOutputStream = null;
                th = th3;
                if (gZIPOutputStream != null) {
                    gZIPOutputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                throw th;
            }
        }
        return bArr2;
    }

    public static byte[] m1228c(byte[] bArr) {
        GZIPInputStream gZIPInputStream;
        byte[] bArr2;
        IOException iOException;
        IOException iOException2;
        Throwable th;
        if (bArr == null) {
            return null;
        }
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (bArr[i] ^ 5);
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
            try {
                bArr2 = new byte[Flags.FLAG5];
                while (true) {
                    int read = gZIPInputStream.read(bArr2, 0, bArr2.length);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr2, 0, read);
                }
                bArr2 = byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                iOException = e;
                bArr2 = null;
                iOException2 = iOException;
                try {
                    iOException2.printStackTrace();
                    if (gZIPInputStream != null) {
                        try {
                            gZIPInputStream.close();
                        } catch (IOException iOException22) {
                            iOException22.printStackTrace();
                            return bArr2;
                        }
                    }
                    if (byteArrayInputStream != null) {
                        byteArrayInputStream.close();
                    }
                    if (byteArrayOutputStream != null) {
                        return bArr2;
                    }
                    byteArrayOutputStream.close();
                    return bArr2;
                } catch (Throwable th2) {
                    th = th2;
                    if (gZIPInputStream != null) {
                        try {
                            gZIPInputStream.close();
                        } catch (IOException iOException222) {
                            iOException222.printStackTrace();
                            throw th;
                        }
                    }
                    if (byteArrayInputStream != null) {
                        byteArrayInputStream.close();
                    }
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                    throw th;
                }
            }
            try {
                byteArrayOutputStream.flush();
                if (gZIPInputStream != null) {
                    try {
                        gZIPInputStream.close();
                    } catch (IOException iOException2222) {
                        iOException2222.printStackTrace();
                        return bArr2;
                    }
                }
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
                if (byteArrayOutputStream == null) {
                    return bArr2;
                }
                byteArrayOutputStream.close();
                return bArr2;
            } catch (IOException e2) {
                iOException2222 = e2;
                iOException2222.printStackTrace();
                if (gZIPInputStream != null) {
                    gZIPInputStream.close();
                }
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    return bArr2;
                }
                byteArrayOutputStream.close();
                return bArr2;
            }
        } catch (IOException e3) {
            gZIPInputStream = null;
            iOException = e3;
            bArr2 = null;
            iOException2222 = iOException;
            iOException2222.printStackTrace();
            if (gZIPInputStream != null) {
                gZIPInputStream.close();
            }
            if (byteArrayInputStream != null) {
                byteArrayInputStream.close();
            }
            if (byteArrayOutputStream != null) {
                return bArr2;
            }
            byteArrayOutputStream.close();
            return bArr2;
        } catch (Throwable th3) {
            th = th3;
            gZIPInputStream = null;
            if (gZIPInputStream != null) {
                gZIPInputStream.close();
            }
            if (byteArrayInputStream != null) {
                byteArrayInputStream.close();
            }
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            throw th;
        }
    }
}

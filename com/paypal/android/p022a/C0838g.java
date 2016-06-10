package com.paypal.android.p022a;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.xbill.DNS.KEYRecord.Flags;

/* renamed from: com.paypal.android.a.g */
public final class C0838g {
    private static byte[] f1566a;
    private static C0838g f1567b;

    static {
        f1567b = null;
    }

    public static void m1562a() {
        if (f1567b == null) {
            f1567b = new C0838g();
        }
        f1566a = f1567b.m1567b("com/paypal/android/utils/data/data.bin");
    }

    public static byte[] m1563a(int i, int i2) {
        return C0838g.m1564a(i, i2, f1566a);
    }

    public static byte[] m1564a(int i, int i2, byte[] bArr) {
        Object obj = new byte[i2];
        System.arraycopy(bArr, i, obj, 0, i2);
        return obj;
    }

    public static byte[] m1565a(String str) {
        return f1567b.m1567b(str);
    }

    public static void m1566b() {
        f1566a = null;
        f1567b = null;
    }

    private byte[] m1567b(String str) {
        InputStream resourceAsStream;
        ByteArrayOutputStream byteArrayOutputStream;
        byte[] toByteArray;
        InputStream inputStream;
        ByteArrayOutputStream byteArrayOutputStream2;
        Throwable th;
        Throwable th2;
        Object obj;
        InputStream inputStream2 = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            if (classLoader != null) {
                resourceAsStream = classLoader.getResourceAsStream(str);
                if (resourceAsStream != null) {
                    try {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        try {
                            byte[] bArr = new byte[Flags.FLAG5];
                            while (true) {
                                int read = resourceAsStream.read(bArr, 0, bArr.length);
                                if (read == -1) {
                                    break;
                                }
                                byteArrayOutputStream.write(bArr, 0, read);
                                Thread.yield();
                            }
                            toByteArray = byteArrayOutputStream.toByteArray();
                            try {
                                byteArrayOutputStream.close();
                            } catch (Throwable th3) {
                            }
                            if (resourceAsStream != null) {
                                try {
                                    resourceAsStream.close();
                                } catch (Throwable th4) {
                                }
                            }
                        } catch (IOException e) {
                            ByteArrayOutputStream byteArrayOutputStream3 = byteArrayOutputStream;
                            inputStream = resourceAsStream;
                            byteArrayOutputStream2 = byteArrayOutputStream3;
                            if (byteArrayOutputStream2 != null) {
                                try {
                                    byteArrayOutputStream2.close();
                                } catch (Throwable th5) {
                                }
                            }
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (Throwable th6) {
                                }
                            }
                            return toByteArray;
                        } catch (Throwable th7) {
                            th = th7;
                            inputStream2 = resourceAsStream;
                            th2 = th;
                            if (byteArrayOutputStream != null) {
                                try {
                                    byteArrayOutputStream.close();
                                } catch (Throwable th8) {
                                }
                            }
                            if (inputStream2 != null) {
                                try {
                                    inputStream2.close();
                                } catch (Throwable th9) {
                                }
                            }
                            throw th2;
                        }
                    } catch (IOException e2) {
                        inputStream = resourceAsStream;
                        obj = inputStream2;
                        if (byteArrayOutputStream2 != null) {
                            byteArrayOutputStream2.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        return toByteArray;
                    } catch (Throwable th10) {
                        th = th10;
                        Object obj2 = inputStream2;
                        inputStream2 = resourceAsStream;
                        th2 = th;
                        if (byteArrayOutputStream != null) {
                            byteArrayOutputStream.close();
                        }
                        if (inputStream2 != null) {
                            inputStream2.close();
                        }
                        throw th2;
                    }
                    return toByteArray;
                }
            }
            resourceAsStream = inputStream2;
            if (resourceAsStream != null) {
                try {
                    resourceAsStream.close();
                } catch (Throwable th11) {
                }
            }
        } catch (IOException e3) {
            obj = inputStream2;
            inputStream = inputStream2;
            if (byteArrayOutputStream2 != null) {
                byteArrayOutputStream2.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            return toByteArray;
        } catch (Throwable th12) {
            th2 = th12;
            byteArrayOutputStream = inputStream2;
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            if (inputStream2 != null) {
                inputStream2.close();
            }
            throw th2;
        }
        return toByteArray;
    }
}

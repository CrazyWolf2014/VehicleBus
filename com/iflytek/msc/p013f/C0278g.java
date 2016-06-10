package com.iflytek.msc.p013f;

import android.content.Context;
import android.os.MemoryFile;
import android.text.TextUtils;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.msc.f.g */
public class C0278g {
    private static String f1041a;

    static {
        f1041a = XmlPullParser.NO_NAMESPACE;
    }

    public static String m1229a(Context context) {
        if (!TextUtils.isEmpty(f1041a)) {
            return f1041a;
        }
        String absolutePath = context.getFilesDir().getAbsolutePath();
        if (!absolutePath.endsWith(FilePathGenerator.ANDROID_DIR_SEP)) {
            absolutePath = absolutePath + FilePathGenerator.ANDROID_DIR_SEP;
        }
        absolutePath = absolutePath + "msclib/";
        File file = new File(absolutePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        f1041a = absolutePath;
        return f1041a;
    }

    public static void m1230a(String str) {
        File file = new File(str);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void m1231a(ConcurrentLinkedQueue<byte[]> concurrentLinkedQueue, String str) {
        Exception e;
        Throwable th;
        FileOutputStream fileOutputStream;
        try {
            C0278g.m1236c(str);
            fileOutputStream = new FileOutputStream(str);
            try {
                Iterator it = concurrentLinkedQueue.iterator();
                while (it.hasNext()) {
                    fileOutputStream.write((byte[]) it.next());
                }
                fileOutputStream.close();
                FileOutputStream fileOutputStream2 = null;
                if (null != null) {
                    try {
                        fileOutputStream2.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (Exception e3) {
                e2 = e3;
                try {
                    e2.printStackTrace();
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e22) {
                            e22.printStackTrace();
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e4) {
                            e4.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
        } catch (Exception e5) {
            e22 = e5;
            fileOutputStream = null;
            e22.printStackTrace();
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (Throwable th3) {
            th = th3;
            fileOutputStream = null;
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            throw th;
        }
    }

    public static boolean m1232a(MemoryFile memoryFile, long j, String str) {
        FileOutputStream fileOutputStream;
        Exception e;
        Throwable th;
        boolean z = false;
        if (!(memoryFile == null || TextUtils.isEmpty(str))) {
            try {
                C0278g.m1230a(str);
                C0278g.m1236c(str);
                fileOutputStream = new FileOutputStream(str);
                try {
                    byte[] bArr = new byte[Flags.FLAG5];
                    int i = 0;
                    while (((long) i) < j) {
                        int i2 = (int) (j - ((long) i) > 1024 ? 1024 : j - ((long) i));
                        memoryFile.readBytes(bArr, i, 0, i2);
                        fileOutputStream.write(bArr, 0, i2);
                        i += i2;
                    }
                    z = true;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e2) {
                        }
                    }
                } catch (Exception e3) {
                    e = e3;
                    try {
                        e.printStackTrace();
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (Exception e4) {
                            }
                        }
                        return z;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (Exception e5) {
                            }
                        }
                        throw th;
                    }
                }
            } catch (Exception e6) {
                e = e6;
                fileOutputStream = null;
                e.printStackTrace();
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                return z;
            } catch (Throwable th3) {
                th = th3;
                fileOutputStream = null;
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                throw th;
            }
        }
        return z;
    }

    public static boolean m1233a(byte[] bArr, String str, boolean z, int i) {
        Exception e;
        boolean z2 = false;
        RandomAccessFile randomAccessFile = null;
        if (!z) {
            try {
                C0278g.m1230a(str);
            } catch (Exception e2) {
                e = e2;
                try {
                    e.printStackTrace();
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                        } catch (Exception e3) {
                        }
                    }
                    return z2;
                } catch (Throwable th) {
                    Throwable th2 = th;
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                        } catch (Exception e4) {
                        }
                    }
                    throw th2;
                }
            }
        }
        C0278g.m1236c(str);
        RandomAccessFile randomAccessFile2 = new RandomAccessFile(str, "rw");
        if (!z) {
            try {
                randomAccessFile2.setLength(0);
            } catch (Exception e5) {
                e = e5;
                randomAccessFile = randomAccessFile2;
                e.printStackTrace();
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
                return z2;
            } catch (Throwable th3) {
                th2 = th3;
                randomAccessFile = randomAccessFile2;
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
                throw th2;
            }
        } else if (i < 0) {
            randomAccessFile2.seek(randomAccessFile2.length());
        } else {
            randomAccessFile2.seek((long) i);
        }
        randomAccessFile2.write(bArr);
        z2 = true;
        if (randomAccessFile2 != null) {
            try {
                randomAccessFile2.close();
            } catch (Exception e6) {
            }
        }
        return z2;
    }

    public static byte[] m1234a(Context context, String str) {
        InputStream open;
        byte[] bArr;
        Exception e;
        Throwable th;
        Exception exception;
        try {
            open = context.getAssets().open(str);
            try {
                bArr = new byte[open.available()];
                try {
                    open.read(bArr);
                    if (open != null) {
                        try {
                            open.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                } catch (Exception e3) {
                    e = e3;
                    try {
                        e.printStackTrace();
                        if (open != null) {
                            try {
                                open.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                        return bArr;
                    } catch (Throwable th2) {
                        th = th2;
                        if (open != null) {
                            try {
                                open.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                }
            } catch (Exception e4) {
                exception = e4;
                bArr = null;
                e = exception;
                e.printStackTrace();
                if (open != null) {
                    open.close();
                }
                return bArr;
            }
        } catch (Exception e42) {
            open = null;
            exception = e42;
            bArr = null;
            e = exception;
            e.printStackTrace();
            if (open != null) {
                open.close();
            }
            return bArr;
        } catch (Throwable th3) {
            th = th3;
            open = null;
            if (open != null) {
                open.close();
            }
            throw th;
        }
        return bArr;
    }

    public static byte[] m1235b(String str) {
        FileInputStream fileInputStream;
        Exception e;
        Throwable th;
        byte[] bArr = null;
        FileInputStream fileInputStream2 = null;
        try {
            File file = new File(str);
            if (file.exists()) {
                fileInputStream = new FileInputStream(file);
                try {
                    bArr = new byte[fileInputStream.available()];
                    fileInputStream.read(bArr);
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                } catch (Exception e3) {
                    e = e3;
                    try {
                        e.printStackTrace();
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                        return bArr;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                }
            } else if (null != null) {
                try {
                    fileInputStream2.close();
                } catch (IOException e2222) {
                    e2222.printStackTrace();
                }
            }
        } catch (Exception e4) {
            e = e4;
            fileInputStream = null;
            e.printStackTrace();
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            return bArr;
        } catch (Throwable th3) {
            fileInputStream = null;
            th = th3;
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            throw th;
        }
        return bArr;
    }

    public static void m1236c(String str) {
        if (!TextUtils.isEmpty(str)) {
            File file = new File(str);
            if (!str.endsWith(FilePathGenerator.ANDROID_DIR_SEP)) {
                file = file.getParentFile();
            }
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }
}

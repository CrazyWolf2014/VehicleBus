package com.iflytek.util.p017a;

import com.iflytek.msc.p013f.C0276e;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/* renamed from: com.iflytek.util.a.b */
public class C0325b {
    public static int m1454a(String str, String str2, boolean z) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                file.createNewFile();
            } else if (z) {
                file.delete();
                file.createNewFile();
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(randomAccessFile.length());
            randomAccessFile.write(str2.getBytes("utf-8"));
            int length = (int) randomAccessFile.length();
            randomAccessFile.close();
            return length;
        } catch (IOException e) {
            C0276e.m1221a("iFly_ContactManager", "save file failed. " + str);
            return 0;
        }
    }

    public static String m1455a(String str) {
        String str2;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(str));
            byte[] bArr = new byte[fileInputStream.available()];
            fileInputStream.read(bArr);
            str2 = new String(bArr, "utf-8");
            try {
                fileInputStream.close();
            } catch (IOException e) {
                C0276e.m1221a("iFly_ContactManager", "load file failed. " + str);
                return str2;
            }
        } catch (IOException e2) {
            str2 = null;
            C0276e.m1221a("iFly_ContactManager", "load file failed. " + str);
            return str2;
        }
        return str2;
    }
}

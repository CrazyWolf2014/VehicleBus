package com.ifoer.util;

import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import org.xbill.DNS.KEYRecord.Flags;

public class Copy_File {
    private static boolean f1305D;
    public static ArrayList<String> list;

    static {
        list = new ArrayList();
        f1305D = false;
    }

    public static void findAllSoFile(String path) {
        File root = new File(path);
        if (root.exists()) {
            File[] currentFiles = root.listFiles();
            for (int i = 0; i < currentFiles.length; i++) {
                if (currentFiles[i].isDirectory()) {
                    findAllSoFile(new StringBuilder(String.valueOf(path)).append(FilePathGenerator.ANDROID_DIR_SEP).append(currentFiles[i].getName()).append(FilePathGenerator.ANDROID_DIR_SEP).toString());
                } else {
                    String filePath = currentFiles[i].getPath();
                    String fileName = currentFiles[i].getName();
                    if (fileName.contains("lib") && fileName.contains(".so")) {
                        list.add(filePath);
                    }
                }
            }
        }
    }

    public static void findAllDownloadFile(String path) {
        File root = new File(path);
        if (root.exists()) {
            File[] currentFiles = root.listFiles();
            for (int i = 0; i < currentFiles.length; i++) {
                if (currentFiles[i].isDirectory()) {
                    findAllSoFile(new StringBuilder(String.valueOf(path)).append(FilePathGenerator.ANDROID_DIR_SEP).append(currentFiles[i].getName()).append(FilePathGenerator.ANDROID_DIR_SEP).toString());
                } else {
                    list.add(currentFiles[i].getName());
                }
            }
        }
    }

    public static int copy(String fromFile, String toFile) {
        File root = new File(fromFile);
        if (!root.exists()) {
            return -1;
        }
        File[] currentFiles = root.listFiles();
        File targetDir = new File(toFile);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        for (int i = 0; i < currentFiles.length; i++) {
            if (currentFiles[i].isDirectory()) {
                copy(new StringBuilder(String.valueOf(currentFiles[i].getPath())).append(FilePathGenerator.ANDROID_DIR_SEP).toString(), new StringBuilder(String.valueOf(toFile)).append(FilePathGenerator.ANDROID_DIR_SEP).append(currentFiles[i].getName()).append(FilePathGenerator.ANDROID_DIR_SEP).toString());
            } else {
                CopySdcardFile(currentFiles[i].getPath(), new StringBuilder(String.valueOf(toFile)).append(FilePathGenerator.ANDROID_DIR_SEP).append(currentFiles[i].getName()).toString());
            }
        }
        return 0;
    }

    public static int CopySdcardFile(String fromFile, String toFile) {
        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte[] bt = new byte[Flags.FLAG5];
            while (true) {
                int c = fosfrom.read(bt);
                if (c <= 0) {
                    fosfrom.close();
                    fosto.close();
                    return 0;
                }
                fosto.write(bt, 0, c);
            }
        } catch (Exception e) {
            return -1;
        }
    }

    public static synchronized int delectFile(String filePath) {
        int i;
        synchronized (Copy_File.class) {
            File root = new File(filePath);
            if (root.exists()) {
                File[] currentFiles = root.listFiles();
                for (int i2 = 0; i2 < currentFiles.length; i2++) {
                    if (currentFiles[i2].isDirectory()) {
                        delectFile(new StringBuilder(String.valueOf(currentFiles[i2].getPath())).append(FilePathGenerator.ANDROID_DIR_SEP).toString());
                    } else {
                        File file = new File(currentFiles[i2].getPath());
                        if (file.exists() && file.delete()) {
                        }
                    }
                }
                i = 0;
            } else {
                i = -1;
            }
        }
        return i;
    }
}

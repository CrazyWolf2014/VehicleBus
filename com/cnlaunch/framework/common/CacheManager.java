package com.cnlaunch.framework.common;

import android.os.Environment;
import android.text.TextUtils;
import com.cnlaunch.framework.utils.NLog;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

public class CacheManager {
    private static String sysCachePath;
    private static final String tag;

    static {
        tag = CacheManager.class.getSimpleName();
    }

    public static String getSysCachePath() {
        return sysCachePath;
    }

    public static void setSysCachePath(String sysCachePath) {
        sysCachePath = sysCachePath;
    }

    public static <T> void saveTestData(String xmlResult, String fileName) {
        try {
            if ("mounted".equals(Environment.getExternalStorageState()) && NLog.isDebug) {
                StringBuilder path = new StringBuilder(Environment.getExternalStorageDirectory().getPath()).append(File.separator).append("testData");
                File file = new File(path.toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
                BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(new File(path.append(File.separator).append(fileName).toString())));
                os.write(xmlResult.getBytes());
                os.flush();
                os.close();
                NLog.m917e(tag, "saveTestData success: " + path);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static <T> boolean writeObject(Object object, String key) {
        try {
            String path = getCachePath(key);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(object);
            oos.flush();
            oos.close();
            File file = new File(path);
            if (file.exists()) {
                file.setLastModified(System.currentTimeMillis());
                NLog.m917e(tag, "writeObject object success : " + path);
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return false;
    }

    public static <T> T readObject(String key) {
        try {
            String cachePath = getCachePath(key);
            if (!new File(cachePath).exists()) {
                return null;
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(cachePath));
            T obj = ois.readObject();
            ois.close();
            return obj;
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
            return null;
        } catch (OptionalDataException e2) {
            e2.printStackTrace();
            return null;
        } catch (FileNotFoundException e3) {
            e3.printStackTrace();
            return null;
        } catch (IOException e4) {
            e4.printStackTrace();
            return null;
        } catch (ClassNotFoundException e5) {
            e5.printStackTrace();
            return null;
        }
    }

    public static <T> boolean isInvalidCache(String key, long timeout) {
        File file = new File(getCachePath(key));
        if (file.exists()) {
            if (System.currentTimeMillis() - file.lastModified() < 1000 * timeout) {
                NLog.m917e(tag, "the cahce is effect : " + path);
                return true;
            }
        }
        NLog.m917e(tag, "the cahce is invalid : " + path);
        return false;
    }

    public static <T> String getCachePath(String key) {
        if (TextUtils.isEmpty(sysCachePath)) {
            throw new IllegalArgumentException("CacheManager sysCachePath is not null.");
        }
        StringBuilder path = new StringBuilder();
        path.append(sysCachePath);
        path.append(File.separator);
        path.append(key);
        return path.toString();
    }

    public static <T> boolean clearCache(String key) {
        File file = new File(getCachePath(key));
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static boolean clearAll() {
        if (TextUtils.isEmpty(sysCachePath)) {
            NLog.m917e(tag, "sysCachePath is null");
            return false;
        }
        File file = new File(sysCachePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}

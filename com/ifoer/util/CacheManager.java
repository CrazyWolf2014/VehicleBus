package com.ifoer.util;

import android.util.Log;
import com.ifoer.image.GDApplication;
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
    private static final String TAG;

    static {
        TAG = CacheManager.class.getSimpleName();
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
                Log.e(TAG, "writeObject object success : " + path);
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

    public static <T> boolean isInvalidObject(String key, long timeout) {
        String path = getCachePath(key);
        File file = new File(path);
        if (file.exists()) {
            if (System.currentTimeMillis() - file.lastModified() < 1000 * timeout) {
                Log.e(TAG, "the cahce is effect : " + path);
                return true;
            }
        }
        Log.e(TAG, "the cahce is invalid : " + path);
        return false;
    }

    public static <T> String getCachePath(String key) {
        StringBuilder path = new StringBuilder();
        path.append(GDApplication.SYS_CACHE_PATH);
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

    public static boolean clearCache() {
        File file = new File(GDApplication.SYS_CACHE_PATH);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}

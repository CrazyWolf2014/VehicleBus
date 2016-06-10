package com.ifoer.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

public class DataCleanManager {
    public static boolean cleanInternalCache(Context context) {
        return deleteFilesByDirectory(context.getCacheDir());
    }

    public static boolean cleanDatabases(Context context) {
        return deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
    }

    public static boolean cleanSharedPreference(Context context) {
        return deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }

    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    public static boolean cleanFiles(Context context) {
        return deleteFilesByDirectory(context.getFilesDir());
    }

    public static boolean cleanLibs(Context context) {
        PackageManager pm = context.getPackageManager();
        String dataDir = XmlPullParser.NO_NAMESPACE;
        try {
            dataDir = pm.getApplicationInfo(context.getPackageName(), 0).dataDir;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return deleteGeneralFile(new StringBuilder(String.valueOf(dataDir)).append("/libs/cnlaunch/car/").toString());
    }

    public static boolean deleteGeneralFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            flag = deleteDirectory(file.getAbsolutePath());
        } else if (file.isFile()) {
            flag = deleteFile(file);
        }
        if (flag) {
        }
        return flag;
    }

    private static boolean deleteFile(File file) {
        Log.i("SrorageManager", "\u5220\u9664\u6587\u4ef6*************");
        return file.delete();
    }

    private static boolean deleteDirectory(String path) {
        Log.i("MainActivity", "\u5f00\u59cb\u5220\u9664\u76ee\u5f55 path" + path);
        boolean flag = true;
        File dirFile = new File(path);
        if (!dirFile.isDirectory()) {
            return 1;
        }
        for (File file : dirFile.listFiles()) {
            if (file.isFile()) {
                flag = deleteFile(file);
                Log.i("SrorageMager", "\u5220\u9664\u6587\u4ef6" + file.getName() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + flag);
            } else if (file.isDirectory()) {
                flag = deleteDirectory(file.getAbsolutePath());
            }
            if (!flag) {
                break;
            }
        }
        return dirFile.delete();
    }

    public static boolean cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return deleteFilesByDirectory(context.getExternalCacheDir());
        }
        return false;
    }

    public static boolean cleanCustomCache(String filePath) {
        return deleteFilesByDirectory(new File(filePath));
    }

    public static void cleanApplicationData(Context context, String... filepath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }

    private static boolean deleteFilesByDirectory(File directory) {
        boolean hasClean = true;
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                hasClean = item.delete();
                if (!hasClean) {
                    return hasClean;
                }
            }
        }
        return hasClean;
    }
}

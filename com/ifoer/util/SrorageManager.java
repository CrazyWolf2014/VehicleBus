package com.ifoer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.ifoer.entity.Constant;
import com.ifoer.expeditionphone.MainActivity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class SrorageManager {
    public static final String getDefaultExternalStoragePath() {
        String externPath = Environment.getExternalStorageDirectory().getPath();
        String phonePath = XmlPullParser.NO_NAMESPACE;
        String state = Environment.getExternalStorageState();
        if (!Environment.isExternalStorageRemovable()) {
            return externPath;
        }
        String[] paths = new StorageList(MainActivity.contexts).getVolumePaths();
        if (paths.length > 1) {
            phonePath = paths[1];
        }
        return phonePath;
    }

    @SuppressLint({"NewApi"})
    public static final String getDefaultExternalStoragePath(Context context) {
        String externPath = Environment.getExternalStorageDirectory().getPath();
        String phonePath = XmlPullParser.NO_NAMESPACE;
        String state = Environment.getExternalStorageState();
        if (!Environment.isExternalStorageRemovable()) {
            return externPath;
        }
        String[] paths = new StorageList(context).getVolumePaths();
        if (paths.length > 1) {
            phonePath = paths[1];
        }
        return phonePath;
    }

    public static final String getCnlaunchDirPath() {
        return getDefaultExternalStoragePath() + "/cnlaunch/" + Constant.PRODUCT_TYPE;
    }

    public static final String getDownloadBinZipPath() {
        String path = getDefaultExternalStoragePath() + "/cnlaunch/" + Constant.PRODUCT_TYPE + "downloadZip/";
        Log.d("weizewei", "\u9ed8\u8ba4\u8def\u5f84cnlaunch\u6587\u4ef6\u5939\u76ee\u5f55 " + path);
        return path;
    }

    public static final String getDownloadBinUnzipPath() {
        String path = getDefaultExternalStoragePath() + "/cnlaunch/" + Constant.PRODUCT_TYPE + "newestDownload/";
        Log.d("weizewei", "\u9ed8\u8ba4\u8def\u5f84cnlaunch\u6587\u4ef6\u5939\u76ee\u5f55 " + path);
        return path;
    }

    public static final String getDiagsoftZipPath() {
        return getDefaultExternalStoragePath() + "/cnlaunch/" + Constant.PRODUCT_TYPE + "zipFile/";
    }

    public static final String getDiagsoftUnZipPaths() {
        return getDefaultExternalStoragePath() + "/cnlaunch/" + Constant.PRODUCT_TYPE;
    }

    public static final String getCarDiagsoftPath() {
        return getDefaultExternalStoragePath() + "/cnlaunch/" + Constant.PRODUCT_TYPE + "DIAGNOSTIC/VEHICLES/";
    }

    @SuppressLint({"SdCardPath"})
    public static final boolean checkSd(boolean isSanMu) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            File file;
            Log.i("SrorageManager", "sd \u5361\u8def\u5f84" + Environment.getExternalStorageDirectory());
            String pathHeq = "/mnt/extsd/cnlaunch";
            String pathSanMu = "/storage/sdcard1/cnlaunch";
            Log.e("SrorageManager", "pathSanMu" + pathSanMu);
            if (isSanMu) {
                file = new File(pathSanMu);
            } else {
                file = new File(pathHeq);
            }
            if (file.exists()) {
                Log.e("SrorageManager", "*****\u6709\u9700\u8981\u5347\u7ea7\u7684\u6587\u4ef6");
                return false;
            }
            Log.e("SrorageManager", "****\u4e0d\u9700\u8981\u5347\u7ea7");
            return true;
        }
        Log.e("SrorageManager", "sd\u5361\u4e0d\u5b58\u5728");
        return false;
    }

    public static void forJava(String name, File f2, String FROMPATH, Context mContexts) {
        File file = new File(FROMPATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            InputStream fosfrom = mContexts.getAssets().open(name);
            OutputStream fosto = new FileOutputStream(new StringBuilder(String.valueOf(FROMPATH)).append(name).toString());
            byte[] bt = new byte[Flags.FLAG5];
            while (true) {
                int c = fosfrom.read(bt);
                if (c <= 0) {
                    fosfrom.close();
                    fosto.close();
                    return;
                }
                fosto.write(bt, 0, c);
            }
        } catch (Exception e) {
        }
    }

    public static void moveFile(String source, String destination) {
        new File(source).renameTo(new File(destination));
    }

    public static void copy(String src, String des, Context mContexts) {
        boolean hasCopy;
        File file = new File(des);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            InputStream fosfrom = mContexts.getAssets().open(src);
            OutputStream fosto = new FileOutputStream(des);
            byte[] bt = new byte[Flags.FLAG5];
            while (true) {
                int c = fosfrom.read(bt);
                if (c <= 0) {
                    break;
                }
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            hasCopy = true;
        } catch (Exception e) {
            hasCopy = false;
        }
        if (new File(src).exists() && hasCopy) {
            deleteGeneralFile(src);
        }
    }

    public static void moveFile(File source, File destination) {
        source.renameTo(destination);
    }

    public static boolean cutGeneralFile(String srcPath, String destDir) {
        if (copyGeneralFile(srcPath, destDir) && deleteGeneralFile(srcPath)) {
            return true;
        }
        return false;
    }

    public static boolean deleteGeneralFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                flag = deleteDirectory(file.getAbsolutePath());
            } else if (file.isFile()) {
                flag = deleteFile(file);
            }
            if (flag) {
            }
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

    public static boolean copyGeneralFile(String srcPath, String destDir) {
        boolean flag = false;
        File file = new File(srcPath);
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            flag = copyFile(srcPath, destDir);
        } else if (file.isDirectory()) {
            flag = copyDirectory(srcPath, destDir);
        }
        return flag;
    }

    private static boolean copyFile(String srcPath, String destDir) {
        boolean flag = false;
        if (!new File(srcPath).exists()) {
            return false;
        }
        String destPath = new StringBuilder(String.valueOf(destDir)).append(srcPath.substring(srcPath.lastIndexOf(File.separator))).toString();
        if (destPath.equals(srcPath)) {
            return false;
        }
        File destFile = new File(destPath);
        if (destFile.exists() && destFile.isFile()) {
            destFile.delete();
        }
        new File(destDir).mkdirs();
        try {
            FileInputStream fis = new FileInputStream(srcPath);
            FileOutputStream fos = new FileOutputStream(destFile);
            byte[] buf = new byte[Flags.FLAG5];
            while (true) {
                int c = fis.read(buf);
                if (c == -1) {
                    break;
                }
                fos.write(buf, 0, c);
            }
            fis.close();
            fos.close();
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (flag) {
        }
        return flag;
    }

    private static boolean copyDirectory(String srcPath, String destDir) {
        int i = 0;
        boolean flag = false;
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) {
            return false;
        }
        String destPath = new StringBuilder(String.valueOf(destDir)).append(File.separator).append(getDirName(srcPath)).toString();
        if (destPath.equals(srcPath)) {
            return false;
        }
        File destDirFile = new File(destPath);
        if (destDirFile.exists()) {
            destDirFile.delete();
        }
        destDirFile.mkdirs();
        File[] fileList = srcFile.listFiles();
        if (fileList.length != 0) {
            int length = fileList.length;
            while (i < length) {
                File temp = fileList[i];
                if (temp.isFile()) {
                    flag = copyFile(temp.getAbsolutePath(), destPath);
                } else if (temp.isDirectory()) {
                    flag = copyDirectory(temp.getAbsolutePath(), destPath);
                }
                if (!flag) {
                    break;
                }
                i++;
            }
        } else {
            flag = true;
        }
        if (flag) {
        }
        return flag;
    }

    private static String getDirName(String dir) {
        if (dir.endsWith(File.separator)) {
            dir = dir.substring(0, dir.lastIndexOf(File.separator));
        }
        return dir.substring(dir.lastIndexOf(File.separator) + 1);
    }
}

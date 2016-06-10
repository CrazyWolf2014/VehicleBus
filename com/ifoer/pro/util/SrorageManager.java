package com.ifoer.pro.util;

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
    public static final void checkSd() {
        String path = "/mnt/extsd/cnlaunch";
        String path2 = "/mnt/sdcard/cnlaunch";
        if (new File(path).exists()) {
            cutGeneralFile(path, path2);
            Log.e("SrorageManager", "*****\u6709\u9700\u8981\u5347\u7ea7\u7684\u6587\u4ef6");
            return;
        }
        Log.e("SrorageManager", "****\u4e0d\u9700\u8981\u5347\u7ea7");
    }

    public static void moveFile(String source, String destination) {
        new File(source).renameTo(new File(destination));
    }

    public static void moveFile(File source, File destination) {
        source.renameTo(destination);
    }

    public static boolean cutGeneralFile(String srcPath, String destDir) {
        if (!copyGeneralFile(srcPath, destDir)) {
            System.out.println("\u590d\u5236\u5931\u8d25\u5bfc\u81f4\u526a\u5207\u5931\u8d25!");
            return false;
        } else if (deleteGeneralFile(srcPath)) {
            System.out.println("\u526a\u5207\u6210\u529f!");
            return true;
        } else {
            System.out.println("\u5220\u9664\u6e90\u6587\u4ef6(\u6587\u4ef6\u5939)\u5931\u8d25\u5bfc\u81f4\u526a\u5207\u5931\u8d25!");
            return false;
        }
    }

    public static boolean deleteGeneralFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("\u8981\u5220\u9664\u7684\u6587\u4ef6\u4e0d\u5b58\u5728\uff01");
        }
        if (file.isDirectory()) {
            flag = deleteDirectory(file.getAbsolutePath());
        } else if (file.isFile()) {
            flag = deleteFile(file);
        }
        if (flag) {
            System.out.println("\u5220\u9664\u6587\u4ef6\u6216\u6587\u4ef6\u5939\u6210\u529f!");
        }
        return flag;
    }

    private static boolean deleteFile(File file) {
        return file.delete();
    }

    private static boolean deleteDirectory(String path) {
        boolean flag = true;
        File dirFile = new File(path);
        if (!dirFile.isDirectory()) {
            return 1;
        }
        for (File file : dirFile.listFiles()) {
            if (file.isFile()) {
                flag = deleteFile(file);
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
        if (file.exists()) {
            if (file.isFile()) {
                System.out.println("\u4e0b\u9762\u8fdb\u884c\u6587\u4ef6\u590d\u5236!");
                flag = copyFile(srcPath, destDir);
            } else if (file.isDirectory()) {
                System.out.println("\u4e0b\u9762\u8fdb\u884c\u6587\u4ef6\u5939\u590d\u5236!");
                flag = copyDirectory(srcPath, destDir);
            }
            return flag;
        }
        System.out.println("\u6e90\u6587\u4ef6\u6216\u6e90\u6587\u4ef6\u5939\u4e0d\u5b58\u5728!");
        return false;
    }

    private static boolean copyFile(String srcPath, String destDir) {
        boolean flag = false;
        if (new File(srcPath).exists()) {
            String destPath = new StringBuilder(String.valueOf(destDir)).append(srcPath.substring(srcPath.lastIndexOf(File.separator))).toString();
            if (destPath.equals(srcPath)) {
                System.out.println("\u6e90\u6587\u4ef6\u8def\u5f84\u548c\u76ee\u6807\u6587\u4ef6\u8def\u5f84\u91cd\u590d!");
                return false;
            }
            File destFile = new File(destPath);
            if (destFile.exists() && destFile.isFile()) {
                System.out.println("\u76ee\u6807\u76ee\u5f55\u4e0b\u5df2\u6709\u540c\u540d\u6587\u4ef6!");
                return false;
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
            }
            if (flag) {
                System.out.println("\u590d\u5236\u6587\u4ef6\u6210\u529f!");
            }
            return flag;
        }
        System.out.println("\u6e90\u6587\u4ef6\u4e0d\u5b58\u5728");
        return false;
    }

    private static boolean copyDirectory(String srcPath, String destDir) {
        int i = 0;
        System.out.println("\u590d\u5236\u6587\u4ef6\u5939\u5f00\u59cb!");
        boolean flag = false;
        Log.i("SrorageManager", "\u6587\u4ef6\u8def\u5f84" + srcPath + "\u76ee\u6807\u8def\u5f84" + destDir);
        File srcFile = new File(srcPath);
        if (srcFile.exists()) {
            String destPath = new StringBuilder(String.valueOf(destDir)).append(File.separator).append(getDirName(srcPath)).toString();
            if (destPath.equals(srcPath)) {
                System.out.println("\u76ee\u6807\u6587\u4ef6\u5939\u4e0e\u6e90\u6587\u4ef6\u5939\u91cd\u590d");
                return false;
            }
            File destDirFile = new File(destPath);
            if (destDirFile.exists()) {
                System.out.println("\u76ee\u6807\u4f4d\u7f6e\u5df2\u6709\u540c\u540d\u6587\u4ef6\u5939!");
                return false;
            }
            destDirFile.mkdirs();
            File[] fileList = srcFile.listFiles();
            if (fileList.length != 0) {
                int length = fileList.length;
                while (i < length) {
                    File temp = fileList[i];
                    Log.i("SrorageManager", "fileList.length" + fileList.length);
                    if (temp.isFile()) {
                        flag = copyFile(temp.getAbsolutePath(), destPath);
                    } else if (temp.isDirectory()) {
                        flag = copyDirectory(temp.getAbsolutePath(), destPath);
                    }
                    Log.i("SrorageManager", "flag" + flag);
                    if (!flag) {
                        break;
                    }
                    i++;
                }
            } else {
                flag = true;
            }
            if (flag) {
                System.out.println("\u590d\u5236\u6587\u4ef6\u5939\u6210\u529f!");
            }
            return flag;
        }
        System.out.println("\u6e90\u6587\u4ef6\u5939\u4e0d\u5b58\u5728");
        return false;
    }

    private static String getDirName(String dir) {
        if (dir.endsWith(File.separator)) {
            dir = dir.substring(0, dir.lastIndexOf(File.separator));
        }
        return dir.substring(dir.lastIndexOf(File.separator) + 1);
    }
}

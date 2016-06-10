package CRP.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431pro.common.Constants;
import com.ifoer.entity.ApkVersionInfo;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.Tools;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.util.EncodingUtils;
import org.xmlpull.v1.XmlPullParser;

public class CRPTools {
    public static final int CRP_BOTELV = 115200;
    public static final String CRP_HEQIANG_DEVICEINFO = "/dev/ttyS2";
    public static final String CRP_HEQIANG_VERSION = "/usr/ver.txt";
    public static final String CRP_SANMU_DEVICEINFO = "/dev/ttyMT0";
    public static final String CRP_SERIALPORTINFO_VERSION = "/cnlaunch/DeviceInfo.txt";
    public static final String CRP_SERIALPORT_LIB = "serial_port";

    public static boolean readSnTxt(Context mContexts) {
        String path = Environment.getExternalStorageDirectory() + CRP_SERIALPORTINFO_VERSION;
        String res = XmlPullParser.NO_NAMESPACE;
        if (new File(path).exists()) {
            try {
                FileInputStream fin = new FileInputStream(path);
                byte[] buffer = new byte[fin.available()];
                fin.read(buffer);
                res = EncodingUtils.getString(buffer, AsyncHttpResponseHandler.DEFAULT_CHARSET);
                fin.close();
                System.out.println("res--->" + res);
                String[] sn = res.split("_");
                String serialPortID = sn[0];
                String serialPortNO = sn[1];
                MySharedPreferences.setString(mContexts, Constants.SERIALID, serialPortID);
                MySharedPreferences.setString(mContexts, Constants.SERIALNO, serialPortNO);
                for (int i = 0; i < sn[2].length(); i++) {
                    if (Tools.isNumeric(new StringBuilder(String.valueOf(sn[2].charAt(i))).toString())) {
                        int a = i;
                        break;
                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        System.out.println("\u6587\u4ef6\u4e0d\u5b58\u5728");
        return false;
    }

    public static String readVersionInfo() {
        String path = Environment.getExternalStorageDirectory() + CRP_HEQIANG_VERSION;
        String res = XmlPullParser.NO_NAMESPACE;
        if (!new File(path).exists()) {
            return res;
        }
        try {
            FileInputStream fin = new FileInputStream(path);
            byte[] buffer = new byte[fin.available()];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, AsyncHttpResponseHandler.DEFAULT_CHARSET);
            fin.close();
            System.out.println("res--->" + res);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
    }

    public static String getSnNo(Context mContexts) {
        String path = Environment.getExternalStorageDirectory() + CRP_SERIALPORTINFO_VERSION;
        String res = XmlPullParser.NO_NAMESPACE;
        String serialPortNO = XmlPullParser.NO_NAMESPACE;
        if (new File(path).exists()) {
            try {
                FileInputStream fin = new FileInputStream(path);
                byte[] buffer = new byte[fin.available()];
                fin.read(buffer);
                res = EncodingUtils.getString(buffer, AsyncHttpResponseHandler.DEFAULT_CHARSET);
                fin.close();
                System.out.println("res--->" + res);
                String[] sn = res.split("_");
                String serialPortID = sn[0];
                serialPortNO = sn[1];
                MySharedPreferences.setString(mContexts, Constants.SERIALID, serialPortID);
                MySharedPreferences.setString(mContexts, Constants.SERIALNO, serialPortNO);
                MySharedPreferences.setString(mContexts, MySharedPreferences.serialNoKey, serialPortNO);
                Log.i(" CRP2 MainActivity", new StringBuilder(Constants.SERIALID).append(serialPortID).toString());
                Log.i(" CRP2 MainActivity", new StringBuilder(Constants.SERIALNO).append(serialPortNO).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i("MainActivity", "\u8bf7\u8fd0\u884c\u8bbe\u5907\u4fe1\u606f\u8fd9\u4e2a\u5de5\u5177\uff0c\u521d\u59cb\u5316\u8f6f\u4ef6");
        }
        return serialPortNO;
    }

    public static long getUsableSDCardSize() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize());
    }

    public static ArrayList<ApkVersionInfo> readSdFile(String path, Context context, int currentVer) {
        ArrayList<ApkVersionInfo> list = new ArrayList();
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (getFileType(files[i].getName()).equals("apk")) {
                    ApkVersionInfo apk = getUninstallAPKInfo(context, files[i].getAbsolutePath(), currentVer);
                    if (apk != null) {
                        list.add(apk);
                    }
                }
            }
        }
        return list;
    }

    public static ApkVersionInfo getUninstallAPKInfo(Context ctx, String archiveFilePath, int currentVer) {
        PackageManager pm = ctx.getPackageManager();
        PackageInfo pakinfo = pm.getPackageArchiveInfo(archiveFilePath, 1);
        if (pakinfo == null) {
            return null;
        }
        ApkVersionInfo apk = new ApkVersionInfo();
        apk.setVersion(pakinfo.versionName);
        ApplicationInfo appInfo = pakinfo.applicationInfo;
        apk.setPackageName(appInfo.packageName);
        apk.setAppName(pm.getApplicationLabel(appInfo).toString());
        apk.setPath(archiveFilePath);
        int notInstalkVer = Integer.parseInt(apk.getVersion().replace(".", XmlPullParser.NO_NAMESPACE));
        if (apk.getAppName().equals("CRP229") && apk.getPackageName().equals("com.ifoer.expedition.crp229") && notInstalkVer > currentVer) {
            return apk;
        }
        File file = new File(archiveFilePath);
        if (!file.exists()) {
            return null;
        }
        file.delete();
        return null;
    }

    public static String getFileType(String fileName) {
        if (fileName != null) {
            int typeIndex = fileName.lastIndexOf(".");
            if (typeIndex != -1) {
                return fileName.substring(typeIndex + 1).toLowerCase();
            }
        }
        return XmlPullParser.NO_NAMESPACE;
    }

    public static boolean writeFileData(String userName, String psw, String email) {
        File file = new File(Environment.getExternalStorageDirectory() + "/cnlaunch/profile.txt");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(new StringBuilder(String.valueOf(userName)).append(AlixDefine.split).append(psw).append(AlixDefine.split).append(email).toString().getBytes());
            fout.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

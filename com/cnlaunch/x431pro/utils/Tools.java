package com.cnlaunch.x431pro.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import com.cnlaunch.mycar.jni.JniX431File;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.ifoer.mine.Contact;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import org.achartengine.ChartFactory;
import org.achartengine.renderer.DefaultRenderer;
import org.vudroid.pdfdroid.PDFManager;
import org.xmlpull.v1.XmlPullParser;

public class Tools {
    private static final String TAG = "Tools";

    public static void openPdf(Context context, String filePath) {
        if (new File(filePath).exists()) {
            PDFManager.open(context, filePath);
        }
    }

    public static boolean isPackageName_Exist(Context context, String packagename) {
        List<PackageInfo> pkgList = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < pkgList.size(); i++) {
            if (((PackageInfo) pkgList.get(i)).packageName.equalsIgnoreCase(packagename)) {
                return true;
            }
        }
        return false;
    }

    public static int hasShortcutByAppName(Context context, String appName) {
        if (appName == null) {
            return -1;
        }
        Cursor c = null;
        try {
            ContentResolver cr = context.getContentResolver();
            Integer sdkInt = Integer.valueOf(VERSION.SDK_INT);
            String l2 = "content://com.android.launcher2.settings/favorites?notify=true";
            String l = "content://com.android.launcher.settings/favorites?notify=true";
            String str = XmlPullParser.NO_NAMESPACE;
            if (sdkInt.intValue() > 7) {
                str = l2;
            } else {
                str = l;
            }
            c = cr.query(Uri.parse(str), new String[]{ChartFactory.TITLE, "iconResource"}, "title=?", new String[]{appName}, null);
            if (c == null) {
                if (sdkInt.intValue() > 7) {
                    str = l;
                } else {
                    str = l2;
                }
                c = cr.query(Uri.parse(str), new String[]{ChartFactory.TITLE, "iconResource"}, "title=?", new String[]{appName}, null);
            }
            int count = -1;
            if (c != null && c.getCount() > 0) {
                count = c.getCount();
            }
            if (c == null) {
                return count;
            }
            c.close();
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            if (c != null) {
                c.close();
            }
            return -1;
        }
    }

    public static String getCurrentPackageName(Context context) {
        String packageNames = XmlPullParser.NO_NAMESPACE;
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return packageNames;
        } catch (Exception e2) {
            e2.printStackTrace();
            return packageNames;
        }
    }

    public static boolean isServiceRunning(Context context, String serviceName) {
        for (RunningServiceInfo service : ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAppRunning(Context context, String packageName) {
        for (RunningAppProcessInfo rapi : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            if (rapi.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static Bitmap Create2DCode(String str) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, JniX431File.MAX_DS_COLNUMBER, JniX431File.MAX_DS_COLNUMBER);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[(width * height)];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[(y * width) + x] = DefaultRenderer.BACKGROUND_COLOR;
                    } else {
                        pixels[(y * width) + x] = -1;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean checkSDCard() {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    public static String getLocalPath(String sysPath, String fileName) {
        return fileName + ".png";
    }

    public static boolean saveBitmapToFile(Bitmap bitmap, String sysPath, String fileName) {
        try {
            File file = new File(getLocalPath(sysPath, fileName));
            file.createNewFile();
            FileOutputStream fileOut = new FileOutputStream(file);
            bitmap.compress(CompressFormat.PNG, 100, fileOut);
            fileOut.flush();
            fileOut.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static String getFormatResult(String str) {
        if (TextUtils.isEmpty(str)) {
            return XmlPullParser.NO_NAMESPACE;
        }
        return str.substring(0, str.length() - 1);
    }

    public static float convertDpToPixel(float dp, Context context) {
        return (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f) * dp;
    }

    public static float convertPixelsToDp(float px, Context context) {
        return px / (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }

    public static String getUninstallAPKInfo(Context ctx, String archiveFilePath) {
        String versionName = Contact.RELATION_ASK;
        try {
            PackageInfo pakinfo = ctx.getPackageManager().getPackageArchiveInfo(archiveFilePath, 1);
            if (pakinfo != null) {
                return pakinfo.versionName;
            }
            return versionName;
        } catch (Exception e) {
            return Contact.RELATION_ASK;
        }
    }

    public static String getInstallAPKInfo(Context ctx, String pak) {
        String versionName = Contact.RELATION_ASK;
        try {
            PackageInfo pakinfo = ctx.getPackageManager().getPackageInfo(pak, 1);
            if (pakinfo != null) {
                return pakinfo.versionName;
            }
            return versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return versionName;
        }
    }

    public static long getApkLength(Context ctx, String filePath, boolean type) {
        if (!type) {
            return getFileSize(new File(filePath));
        }
        try {
            InputStream in = ctx.getResources().getAssets().open("plug/" + filePath);
            long length = (long) in.available();
            in.close();
            return length;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static long getFileSize(File file) throws Exception {
        if (file.exists()) {
            return (long) new FileInputStream(file).available();
        }
        file.createNewFile();
        Log.e("\u83b7\u53d6\u6587\u4ef6\u5927\u5c0f", "\u6587\u4ef6\u4e0d\u5b58\u5728!");
        return 0;
    }

    public static boolean isExpert(String roles) {
        if ((Integer.parseInt(roles) & 16) == 16) {
            return true;
        }
        return false;
    }

    public static boolean isTechnician(String roles) {
        if ((Integer.parseInt(roles) & 1) == 1) {
            return true;
        }
        return false;
    }

    public static int dipTopx(Context context, float dipValue) {
        return (int) ((dipValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static Drawable getCurrentDrawable(Context context) {
        ShapeDrawable bgdrawable = new ShapeDrawable(new OvalShape());
        bgdrawable.getPaint().setColor(context.getResources().getColor(17170445));
        return bgdrawable;
    }

    public static int dip2px(Context context, float dipValue) {
        return (int) ((dipValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static Boolean isCorrectFormat(String str) {
        Boolean bl = Boolean.valueOf(false);
        if (Pattern.compile("^[a-zA-Z][0-9a-zA-Z_]+$").matcher(str).matches()) {
            return Boolean.valueOf(true);
        }
        return bl;
    }

    public static String getTime(long time) {
        String result = XmlPullParser.NO_NAMESPACE;
        if (time == 0) {
            return result;
        }
        return new SimpleDateFormat("HH:mm").format(new Date(time));
    }

    public static String getDate(long time) {
        String result = XmlPullParser.NO_NAMESPACE;
        if (time == 0) {
            return result;
        }
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(time));
    }

    public static String getDate(long time, String format) {
        String result = XmlPullParser.NO_NAMESPACE;
        if (time == 0) {
            return result;
        }
        return new SimpleDateFormat(format).format(new Date(time));
    }

    public static boolean isNotNull(String txt) {
        return (TextUtils.isEmpty(txt) || "null".equalsIgnoreCase(txt)) ? false : true;
    }

    public static boolean isUSAProject() {
        return true;
    }

    public static boolean checkIpSuccess(String ip) {
        String regx = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        if (TextUtils.isEmpty(ip)) {
            return false;
        }
        return ip.matches(regx);
    }
}

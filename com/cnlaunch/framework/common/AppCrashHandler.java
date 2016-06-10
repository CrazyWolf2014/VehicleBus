package com.cnlaunch.framework.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class AppCrashHandler implements UncaughtExceptionHandler {
    private static AppCrashHandler instance;
    private final String EXCEPTION;
    private final String PATTERN;
    private final String PREFIX;
    private final String SUFFIX;
    private final String TRACE;
    private final String VERSIONCODE;
    private final String VERSIONNAME;
    private Properties crashReport;
    private AppCrashCallback mCallback;
    private Context mContext;
    private UncaughtExceptionHandler mDefaultHandler;
    private final String tag;

    /* renamed from: com.cnlaunch.framework.common.AppCrashHandler.1 */
    class C01101 extends Thread {
        private final /* synthetic */ Throwable val$ex;

        C01101(Throwable th) {
            this.val$ex = th;
        }

        public void run() {
            Looper.prepare();
            AppCrashHandler.this.saveCrashInfo(AppCrashHandler.this.mContext, this.val$ex);
            AppCrashHandler.this.saveCrashInfo2(AppCrashHandler.this.mContext, this.val$ex);
            AppCrashHandler.this.sendCrashReport();
            Toast.makeText(AppCrashHandler.this.mContext, C0136R.string.crash_hint, 0).show();
            Looper.loop();
        }
    }

    /* renamed from: com.cnlaunch.framework.common.AppCrashHandler.2 */
    class C01112 implements FilenameFilter {
        C01112() {
        }

        public boolean accept(File dir, String filename) {
            return filename.endsWith(".cr");
        }
    }

    public interface AppCrashCallback {
        boolean upload(File file);
    }

    public AppCrashHandler() {
        this.tag = AppCrashHandler.class.getSimpleName();
        this.crashReport = new Properties();
        this.TRACE = "trace";
        this.EXCEPTION = "exception";
        this.VERSIONNAME = "versionName";
        this.VERSIONCODE = "versionCode";
        this.PREFIX = "crash_";
        this.PATTERN = "yyyy-MM-dd hh:mm:ss";
        this.SUFFIX = ".cr";
    }

    public static AppCrashHandler getInstance() {
        if (instance == null) {
            instance = new AppCrashHandler();
        }
        return instance;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void setCallback(AppCrashCallback callback) {
        this.mCallback = callback;
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        if (handlerException(ex) || this.mDefaultHandler == null) {
            try {
                Thread.sleep(5000);
                ActivityPageManager.getInstance().exit(this.mContext);
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
        this.mDefaultHandler.uncaughtException(thread, ex);
    }

    private boolean handlerException(Throwable ex) {
        if (ex == null) {
            return true;
        }
        if (TextUtils.isEmpty(ex.getLocalizedMessage())) {
            return false;
        }
        new C01101(ex).start();
        try {
            Thread.sleep(5000);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return true;
        }
    }

    private void conllectCrashDeviceInfo(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 1);
            if (pi != null) {
                this.crashReport.put("versionName", String.valueOf(pi.versionName));
                this.crashReport.put("versionCode", String.valueOf(pi.versionCode));
            }
            Field[] fieldList = Build.class.getDeclaredFields();
            if (fieldList != null) {
                for (Field device : fieldList) {
                    device.setAccessible(true);
                    this.crashReport.put(device.getName(), String.valueOf(device.get(null)));
                }
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
    }

    private void saveCrashInfo(Context context, Throwable ex) {
        try {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            for (Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
                cause.printStackTrace(printWriter);
            }
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 1);
            StringBuilder builder = new StringBuilder();
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd,HH-mm-ss");
            builder.append(String.format("Devices Model: %s\n", new Object[]{Build.MODEL}));
            builder.append(String.format("Devices SDK Version: %s\n", new Object[]{Integer.valueOf(VERSION.SDK_INT)}));
            if (pi != null) {
                builder.append(String.format("Software Version Name: %s\n", new Object[]{pi.versionName}));
                builder.append(String.format("Software Version Code: %s\n", new Object[]{Integer.valueOf(pi.versionCode)}));
            }
            builder.append(String.format("Software Type: %s\n", new Object[]{"X431PADII"}));
            builder.append(String.format("Crash Time: %s\n", new Object[]{format.format(date)}));
            builder.append(writer.toString());
            printWriter.close();
            FileOutputStream fos = this.mContext.openFileOutput(getCrashFileName(), 0);
            fos.write(builder.toString().getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveCrashInfo2(Context context, Throwable ex) {
        try {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            for (Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
                cause.printStackTrace(printWriter);
            }
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 1);
            StringBuilder builder = new StringBuilder();
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd,HH-mm-ss");
            builder.append(String.format("Devices Model: %s\n", new Object[]{Build.MODEL}));
            builder.append(String.format("Devices SDK Version: %s\n", new Object[]{Integer.valueOf(VERSION.SDK_INT)}));
            if (pi != null) {
                Object[] objArr = new Object[1];
                objArr[0] = pi.versionName;
                builder.append(String.format("Software Version Name: %s\n", objArr));
                objArr = new Object[1];
                objArr[0] = Integer.valueOf(pi.versionCode);
                builder.append(String.format("Software Version Code: %s\n", objArr));
            }
            builder.append(String.format("Software Type: %s\n", new Object[]{"X431PADII"}));
            builder.append(String.format("Crash Time: %s\n", new Object[]{format.format(date)}));
            builder.append(writer.toString());
            printWriter.close();
            String fileName = getCrashFileName();
            if (Environment.getExternalStorageState().equals("mounted")) {
                String path = Environment.getExternalStorageDirectory() + "/cnlaunch/" + "LOG/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos1 = new FileOutputStream(new StringBuilder(String.valueOf(path)).append(fileName).toString());
                fos1.write(builder.toString().getBytes());
                fos1.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCrashFileName() {
        StringBuilder fileName = new StringBuilder("crash_");
        fileName.append(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        fileName.append(".cr");
        return fileName.toString();
    }

    public void sendCrashReport() {
        String[] list = this.mContext.getFilesDir().list(new C01112());
        if (list != null && list.length > 0) {
            for (String fileName : list) {
                File file = new File(this.mContext.getFilesDir(), fileName);
                if (file.exists() && this.mCallback != null && this.mCallback.upload(file)) {
                    file.delete();
                }
            }
        }
    }
}

package com.ifoer.image;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CrashHandler implements UncaughtExceptionHandler {
    private static CrashHandler INSTANCE = null;
    public static final String TAG = "CrashHandler";
    private DateFormat formatter;
    private Map<String, String> infos;
    private Context mContext;
    private UncaughtExceptionHandler mDefaultHandler;

    /* renamed from: com.ifoer.image.CrashHandler.1 */
    class C06731 extends Thread {
        private final /* synthetic */ Throwable val$ex;

        C06731(Throwable th) {
            this.val$ex = th;
        }

        public void run() {
            Looper.prepare();
            Toast.makeText(CrashHandler.this.mContext, "Sorry\u3002Error is :" + this.val$ex.getMessage(), 1).show();
            Looper.loop();
        }
    }

    static {
        INSTANCE = new CrashHandler();
    }

    private CrashHandler() {
        this.infos = new HashMap();
        this.formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        if (handleException(ex) || this.mDefaultHandler == null) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            Process.killProcess(Process.myPid());
            System.exit(1);
            return;
        }
        this.mDefaultHandler.uncaughtException(thread, ex);
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        new C06731(ex).start();
        collectDeviceInfo(this.mContext);
        saveCrashInfo2File(ex);
        return true;
    }

    public void collectDeviceInfo(Context ctx) {
        try {
            PackageInfo pi = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 1);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = new StringBuilder(String.valueOf(pi.versionCode)).toString();
                this.infos.put("versionName", versionName);
                this.infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        for (Field field : Build.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                this.infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e2) {
                Log.e(TAG, "an error occured when collect crash info", e2);
            }
        }
    }

    public String saveCreashInfo2File(String mesage, Throwable ex) {
        StringBuffer sb = new StringBuffer();
        if (mesage != null) {
            sb.append("Message :" + mesage);
        } else {
            sb.append("Message is null");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        for (Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
            cause.printStackTrace(printWriter);
        }
        printWriter.close();
        sb.append(writer.toString());
        try {
            String fileName = "crash-" + this.formatter.format(new Date()) + "-" + System.currentTimeMillis() + ".log";
            if (!Environment.getExternalStorageState().equals("mounted")) {
                return fileName;
            }
            String path = "/sdcard/iGV/crash/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(new StringBuilder(String.valueOf(path)).append(fileName).toString());
            fos.write(sb.toString().getBytes());
            fos.close();
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
            return null;
        }
    }

    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Entry<String, String> entry : this.infos.entrySet()) {
            String value = (String) entry.getValue();
            sb.append(new StringBuilder(String.valueOf((String) entry.getKey())).append("=").append(value).append(SpecilApiUtil.LINE_SEP).toString());
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        for (Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
            cause.printStackTrace(printWriter);
        }
        printWriter.close();
        sb.append(writer.toString());
        try {
            long timestamp = System.currentTimeMillis();
            String time = this.formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            if (!Environment.getExternalStorageState().equals("mounted")) {
                return fileName;
            }
            String path = "/sdcard/iGV/crash/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(new StringBuilder(String.valueOf(path)).append(fileName).toString());
            fos.write(sb.toString().getBytes());
            fos.close();
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
            return null;
        }
    }
}

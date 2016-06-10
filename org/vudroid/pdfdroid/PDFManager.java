package org.vudroid.pdfdroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.vudroid.core.PDFPreferences;
import org.xbill.DNS.KEYRecord.Flags;

public class PDFManager {
    private static final String tag;

    static {
        tag = PDFManager.class.getSimpleName();
    }

    public static boolean initAssetsFontLib(Context context, String ttfName) {
        PDFPreferences sharePref = new PDFPreferences(context);
        if (TextUtils.isEmpty(ttfName)) {
            Log.e(tag, "initFontLib ttfName is not null.");
            return false;
        }
        if ("mounted".equals(Environment.getExternalStorageState())) {
            try {
                File dir = new File(Environment.getExternalStorageDirectory().getPath());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, ttfName);
                if (file.exists()) {
                    sharePref.setFontPath(file.getAbsolutePath());
                    return true;
                } else if (getAssetFileToFileDir(dir, context, ttfName) == null) {
                    return false;
                } else {
                    sharePref.setFontPath(file.getAbsolutePath());
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        Log.e(tag, "initFontLib SD card Unavailable.");
        return false;
    }

    private static File getAssetFileToFileDir(File dir, Context context, String fileName) {
        try {
            String filePath = dir.getAbsolutePath() + File.separator + fileName;
            InputStream is = context.getAssets().open(fileName);
            if (is != null) {
                File file = new File(filePath);
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] temp = new byte[Flags.FLAG5];
                while (true) {
                    int i = is.read(temp);
                    if (i <= 0) {
                        fos.close();
                        is.close();
                        return file;
                    }
                    fos.write(temp, 0, i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFontPath(Context context) {
        return new PDFPreferences(context).getFontPath();
    }

    public static void open(Context context, String pdfPath) {
        if (TextUtils.isEmpty(pdfPath)) {
            Log.e(tag, "open pdfPath is not null.");
            return;
        }
        File file = new File(pdfPath);
        if (file.exists()) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.fromFile(file));
            intent.setClass(context, PdfViewerActivity.class);
            context.startActivity(intent);
            return;
        }
        Log.e(tag, "open file is not exists.");
    }
}

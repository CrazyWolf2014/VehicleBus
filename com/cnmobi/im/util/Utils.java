package com.cnmobi.im.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.cropImage.LoadingDialog;
import com.ifoer.mine.Contact;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class Utils {
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), Flags.FLAG8);
            if (ai != null) {
                metaData = ai.metaData;
            }
            if (metaData != null) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {
        }
        return apiKey;
    }

    public static String pwd2MD5(String pwd) {
        String md5 = XmlPullParser.NO_NAMESPACE;
        try {
            md5 = MD5(URLEncoder.encode(pwd, "utf-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return md5;
    }

    public static String MD5(String str) {
        try {
            int i;
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            char[] charArray = str.toCharArray();
            byte[] byteArray = new byte[charArray.length];
            for (i = 0; i < charArray.length; i++) {
                byteArray[i] = (byte) charArray[i];
            }
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (byte b : md5Bytes) {
                int val = b & KEYRecord.PROTOCOL_ANY;
                if (val < 16) {
                    hexValue.append(Contact.RELATION_ASK);
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return XmlPullParser.NO_NAMESPACE;
        }
    }

    private static String toUpCase(String str) {
        StringBuffer newstr = new StringBuffer();
        newstr.append(str.substring(0, 1).toUpperCase()).append(str.substring(1, str.length()));
        return newstr.toString();
    }

    public static LoadingDialog getLoadingDialog(Context context) {
        LoadingDialog dialog = new LoadingDialog(context, C0136R.style.LoadingDialog);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            for (File deleteFile : childFiles) {
                deleteFile(deleteFile);
            }
        }
    }

    public static String getDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String getTime() {
        return new StringBuilder(String.valueOf(System.currentTimeMillis())).toString();
    }

    public static boolean checkEmail(String email) {
        if (email.matches("[a-zA-Z0-9_-]{2,}[@][a-z0-9]{2,}[.]\\p{Lower}{2,}")) {
            return true;
        }
        return false;
    }

    public static Drawable loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), "image.gif");
        } catch (IOException e) {
        }
        return drawable;
    }
}

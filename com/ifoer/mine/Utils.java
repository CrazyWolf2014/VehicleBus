package com.ifoer.mine;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
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

    public static Drawable loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), "image.gif");
        } catch (IOException e) {
        }
        return drawable;
    }

    public static void hiddenSoftKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService("input_method");
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showSoftKeyboard(Activity activity, EditText editText) {
        editText.requestFocus();
        ((InputMethodManager) editText.getContext().getSystemService("input_method")).showSoftInput(editText, 0);
    }

    public static void displaySoftKeyboard(Activity activity, EditText et) {
        ((InputMethodManager) activity.getSystemService("input_method")).showSoftInput(et, 0);
    }

    public static SpannableString analyzeFaaceFromStr(String str, Context context, int width) {
        SpannableString spanStr = null;
        if (str != null) {
            spanStr = new SpannableString(str);
            int cutLen = 0;
            while (true) {
                if (str.indexOf("[@id:") == -1) {
                    break;
                }
                int temp = str.indexOf("[@id:");
                int start = str.indexOf("[@id:") + 5;
                int end = str.indexOf("]");
                if (!(start == -1 || end == -1)) {
                    Drawable d = context.getResources().getDrawable(Integer.valueOf(str.substring(start, end)).intValue());
                    d.setBounds(0, 0, (int) (((double) width) * 1.6d), (int) (((double) width) * 1.6d));
                    spanStr.setSpan(new ImageSpan(d, 1), cutLen + temp, (cutLen + end) + 1, 18);
                    String str2 = str;
                    str = str2.replaceFirst("\\[" + str.substring(temp + 1, end + 1), XmlPullParser.NO_NAMESPACE);
                    cutLen += (end - temp) + 1;
                }
            }
        }
        return spanStr;
    }

    public static Bitmap getBitmap(Context context, int resId, String path, int width, int height) {
        int scale;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap bm;
        if (resId != -1) {
            bm = BitmapFactory.decodeResource(context.getResources(), resId, options);
        } else {
            bm = BitmapFactory.decodeFile(path, options);
        }
        int xScale = options.outWidth / width;
        int yScale = options.outHeight / height;
        if (xScale > yScale) {
            scale = xScale;
        } else {
            scale = yScale;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;
        if (resId != -1) {
            return BitmapFactory.decodeResource(context.getResources(), resId, options);
        }
        return BitmapFactory.decodeFile(path, options);
    }

    public static boolean isNetworkAccessiable(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info != null) {
            return info.isConnected();
        }
        return false;
    }

    public static boolean checkEmail(String email) {
        try {
            return Pattern.compile("^([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+.[a-zA-Z]{2,4}$").matcher(email).matches();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isMobileNO(String mobiles) {
        try {
            return mobiles.length() <= 16;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isMobileNO2Contact(String mobiles) {
        return Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$").matcher(mobiles).matches();
    }

    public static int getStrlength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uffe5]";
        for (int i = 0; i < value.length(); i++) {
            if (value.substring(i, i + 1).matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength++;
            }
        }
        return valueLength;
    }
}

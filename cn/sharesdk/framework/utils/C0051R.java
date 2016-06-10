package cn.sharesdk.framework.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import cn.sharesdk.framework.p000a.C0023d;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: cn.sharesdk.framework.utils.R */
public class C0051R {
    private static float f99a;

    private static Bitmap m164a(InputStream inputStream, int i) {
        Options options = new Options();
        options.inPreferredConfig = Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = i;
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    public static long dateStrToLong(String str) {
        return new SimpleDateFormat("yyyy-MM-dd").parse(str, new ParsePosition(0)).getTime();
    }

    public static Bundle decodeUrl(String str) {
        Bundle bundle = new Bundle();
        if (str != null) {
            for (String split : str.split(AlixDefine.split)) {
                String[] split2 = split.split("=");
                if (split2.length < 2 || split2[1] == null) {
                    bundle.putString(URLDecoder.decode(split2[0]), XmlPullParser.NO_NAMESPACE);
                } else {
                    bundle.putString(URLDecoder.decode(split2[0]), URLDecoder.decode(split2[1]));
                }
            }
        }
        return bundle;
    }

    public static int dipToPx(Context context, int i) {
        if (f99a <= 0.0f) {
            f99a = context.getResources().getDisplayMetrics().density;
        }
        return (int) ((((float) i) * f99a) + 0.5f);
    }

    public static String encodeUrl(Bundle bundle) {
        if (bundle == null) {
            return XmlPullParser.NO_NAMESPACE;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Object obj = 1;
        for (String str : bundle.keySet()) {
            Object obj2 = bundle.get(str);
            if (obj2 == null) {
                obj2 = XmlPullParser.NO_NAMESPACE;
            }
            if (obj != null) {
                obj = null;
            } else {
                stringBuilder.append(AlixDefine.split);
            }
            stringBuilder.append(C0052a.m174d(str) + "=" + C0052a.m174d(String.valueOf(obj2)));
        }
        return stringBuilder.toString();
    }

    public static String encodeUrl(ArrayList<C0023d<String>> arrayList) {
        if (arrayList == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Iterator it = arrayList.iterator();
        int i = 0;
        while (it.hasNext()) {
            C0023d c0023d = (C0023d) it.next();
            if (i > 0) {
                stringBuilder.append('&');
            }
            String str = c0023d.f9a;
            String str2 = (String) c0023d.f10b;
            if (str != null) {
                if (str2 == null) {
                    str2 = XmlPullParser.NO_NAMESPACE;
                }
                stringBuilder.append(C0052a.m174d(str) + "=" + C0052a.m174d(str2));
                i++;
            }
        }
        return stringBuilder.toString();
    }

    public static Bitmap getBitmap(File file, int i) {
        InputStream fileInputStream = new FileInputStream(file);
        Bitmap a = C0051R.m164a(fileInputStream, i);
        fileInputStream.close();
        return a;
    }

    public static Bitmap getBitmap(String str) {
        return C0051R.getBitmap(str, 1);
    }

    public static Bitmap getBitmap(String str, int i) {
        return C0051R.getBitmap(new File(str), i);
    }

    public static int getBitmapRes(Context context, String str) {
        try {
            return C0051R.getResId(Class.forName(context.getPackageName() + ".R$drawable"), str);
        } catch (Throwable th) {
            C0058e.m220b(th);
            return 0;
        }
    }

    public static String getCachePath(Context context, String str) {
        String str2 = context.getFilesDir().getAbsolutePath() + "/ShareSDK/cache/";
        C0053b c0053b = new C0053b(context);
        if (c0053b.m202v()) {
            str2 = c0053b.m203w() + "/ShareSDK/" + c0053b.m196p() + "/cache/";
        }
        if (!TextUtils.isEmpty(str)) {
            str2 = str2 + str + FilePathGenerator.ANDROID_DIR_SEP;
        }
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdirs();
        }
        return str2;
    }

    public static int getResId(Class<?> cls, String str) {
        int intValue;
        if (str != null) {
            try {
                Field field = cls.getField(str.toLowerCase());
                field.setAccessible(true);
                intValue = ((Integer) field.get(null)).intValue();
            } catch (Throwable th) {
                C0058e.m220b(th);
                intValue = 0;
            }
        } else {
            intValue = 0;
        }
        if (intValue <= 0) {
            System.err.println("resource " + cls.getName() + "." + str + " not found!");
        }
        return intValue;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getStringRes(Context context, String str) {
        try {
            return C0051R.getResId(Class.forName(context.getPackageName() + ".R$string"), str);
        } catch (Throwable th) {
            C0058e.m220b(th);
            return 0;
        }
    }

    public static long parseTwitterDate(String str) {
        String[] split;
        int parseInt;
        int parseInt2;
        int i = 2;
        int i2 = 1;
        int i3 = 0;
        try {
            split = str.split(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            String toUpperCase = split[1].toUpperCase();
            if (toUpperCase.startsWith("FEB")) {
                i = i2;
            } else if (!toUpperCase.startsWith("MAR")) {
                i = toUpperCase.startsWith("APR") ? 3 : toUpperCase.startsWith("MAY") ? 4 : toUpperCase.startsWith("JUN") ? 5 : toUpperCase.startsWith("JUL") ? 6 : toUpperCase.startsWith("AUG") ? 7 : toUpperCase.startsWith("SEP") ? 8 : toUpperCase.startsWith("OCT") ? 9 : toUpperCase.startsWith("NOV") ? 10 : toUpperCase.startsWith("DEC") ? 11 : i3;
            }
            parseInt = Integer.parseInt(split[2]);
        } catch (Throwable th) {
            C0058e.m220b(th);
            return 0;
        }
        if (split[4].startsWith("+")) {
            split[4] = split[4].substring(1);
        }
        try {
            i2 = Integer.parseInt(split[4]);
        } catch (Throwable th2) {
            C0058e.m220b(th2);
            i2 = i3;
        }
        int i4 = 1970;
        try {
            i4 = Integer.parseInt(split[5]);
        } catch (Throwable th3) {
            C0058e.m220b(th3);
        }
        split = split[3].split(":");
        try {
            parseInt2 = Integer.parseInt(split[0]);
        } catch (Throwable th32) {
            C0058e.m220b(th32);
            parseInt2 = i3;
        }
        int i5 = (parseInt2 - i2) + 8;
        try {
            i2 = Integer.parseInt(split[1]);
        } catch (Throwable th22) {
            C0058e.m220b(th22);
            i2 = i3;
        }
        try {
            i3 = Integer.parseInt(split[2]);
        } catch (Throwable th322) {
            C0058e.m220b(th322);
        }
        Calendar instance = Calendar.getInstance();
        instance.set(1, i4);
        instance.set(2, i);
        instance.set(5, parseInt);
        instance.set(11, i5);
        instance.set(12, i2);
        instance.set(13, i3);
        C0058e.m219b("date: " + str + ", parsed date: " + instance.toString(), new Object[0]);
        return instance.getTimeInMillis();
    }

    public static Uri pathToContentUri(Context context, String str) {
        Cursor query = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, "_data=? ", new String[]{str}, null);
        if (query != null && query.moveToFirst()) {
            return Uri.withAppendedPath(Uri.parse("content://media/external/images/media"), XmlPullParser.NO_NAMESPACE + query.getInt(query.getColumnIndex("_id")));
        } else if (!new File(str).exists()) {
            return null;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_data", str);
            return context.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
        }
    }

    public static int pxToDip(Context context, int i) {
        if (f99a <= 0.0f) {
            f99a = context.getResources().getDisplayMetrics().density;
        }
        return (int) ((((float) i) / f99a) + 0.5f);
    }

    public static long strToDate(String str) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str, new ParsePosition(0)).getTime();
    }

    public static String toWordText(String str, int i) {
        char[] toCharArray = str.toCharArray();
        int i2 = i * 2;
        StringBuilder stringBuilder = new StringBuilder();
        int i3 = i2;
        for (char c : toCharArray) {
            i3 -= c < '\u0100' ? 1 : 2;
            if (i3 < 0) {
                return stringBuilder.toString();
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public static Bundle urlToBundle(String str) {
        int indexOf = str.indexOf("://");
        try {
            URL url = new URL(indexOf >= 0 ? "http://" + str.substring(indexOf + 1) : "http://" + str);
            Bundle decodeUrl = C0051R.decodeUrl(url.getQuery());
            decodeUrl.putAll(C0051R.decodeUrl(url.getRef()));
            return decodeUrl;
        } catch (Throwable th) {
            C0058e.m220b(th);
            return new Bundle();
        }
    }
}

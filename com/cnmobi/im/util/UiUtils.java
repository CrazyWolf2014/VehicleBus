package com.cnmobi.im.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.mapabc.minimap.map.vmap.VMapProjection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;

public class UiUtils {

    /* renamed from: com.cnmobi.im.util.UiUtils.1 */
    class C02071 implements OnClickListener {
        private final /* synthetic */ Activity val$activity;

        C02071(Activity activity) {
            this.val$activity = activity;
        }

        public void onClick(View arg0) {
            this.val$activity.finish();
        }
    }

    public static void enabledBackButton(Activity activity) {
        ImageButton back = (ImageButton) activity.findViewById(C0136R.id.backBtn);
        back.setVisibility(0);
        back.setOnClickListener(new C02071(activity));
    }

    public static String getCCNumber(String jid) {
        return jid.split(XmppConnection.JID_SEPARATOR)[0];
    }

    public static boolean isNull(TextView tv) {
        boolean result = false;
        if (tv == null || tv.getText() == null) {
            result = true;
        }
        if (XmlPullParser.NO_NAMESPACE.equals(tv.getText().toString().trim())) {
            return true;
        }
        return result;
    }

    public static Date parse(String dateStr, String format) {
        Date result = null;
        try {
            result = new SimpleDateFormat(format).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static boolean isPhone(String phone) {
        return Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$").matcher(phone).matches();
    }

    public static String inputStream2String(InputStream is) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String str = XmlPullParser.NO_NAMESPACE;
        while (true) {
            str = in.readLine();
            if (str == null) {
                return buffer.toString();
            }
            buffer.append(str);
        }
    }

    public static double twoPointangle(Point p1, Point p2) {
        double result = Math.atan2((double) (p2.y - p1.y), (double) (p2.x - p1.x));
        Log.i("Math.atan2", String.valueOf(result));
        return (VMapProjection.MaxLongitude * result) / 3.141592653589793d;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}

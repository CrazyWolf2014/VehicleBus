package com.alipay.android.appDemo4;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import com.cnlaunch.x431frame.C0136R;
import java.io.IOException;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.json.JSONObject;

public class BaseHelper {
    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String convertStreamToString(java.io.InputStream r5) {
        /*
        r2 = new java.io.BufferedReader;
        r4 = new java.io.InputStreamReader;
        r4.<init>(r5);
        r2.<init>(r4);
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r1 = 0;
    L_0x0010:
        r1 = r2.readLine();	 Catch:{ IOException -> 0x0022 }
        if (r1 != 0) goto L_0x001e;
    L_0x0016:
        r5.close();	 Catch:{ IOException -> 0x0039 }
    L_0x0019:
        r4 = r3.toString();
        return r4;
    L_0x001e:
        r3.append(r1);	 Catch:{ IOException -> 0x0022 }
        goto L_0x0010;
    L_0x0022:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x002f }
        r5.close();	 Catch:{ IOException -> 0x002a }
        goto L_0x0019;
    L_0x002a:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0019;
    L_0x002f:
        r4 = move-exception;
        r5.close();	 Catch:{ IOException -> 0x0034 }
    L_0x0033:
        throw r4;
    L_0x0034:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0033;
    L_0x0039:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0019;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.android.appDemo4.BaseHelper.convertStreamToString(java.io.InputStream):java.lang.String");
    }

    public static void showDialog(Activity context, String strTitle, String strText, int icon) {
        Builder tDialog = new Builder(context);
        tDialog.setIcon(icon);
        tDialog.setTitle(strTitle);
        tDialog.setMessage(strText);
        tDialog.setPositiveButton(C0136R.string.Ensure, null);
        tDialog.show();
    }

    public static void log(String tag, String info) {
    }

    public static void chmod(String permission, String path) {
        try {
            Runtime.getRuntime().exec("chmod " + permission + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ProgressDialog showProgress(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(false);
        dialog.setOnCancelListener(new AlixOnCancelListener((Activity) context));
        dialog.show();
        return dialog;
    }

    public static JSONObject string2JSON(String str, String split) {
        JSONObject json = new JSONObject();
        try {
            String[] arrStr = str.split(split);
            for (int i = 0; i < arrStr.length; i++) {
                String[] arrKeyValue = arrStr[i].split("=");
                json.put(arrKeyValue[0], arrStr[i].substring(arrKeyValue[0].length() + 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}

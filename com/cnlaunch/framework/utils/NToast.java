package com.cnlaunch.framework.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;

public class NToast {
    public static void shortToast(Context context, int resId) {
        showToast(context, context.getString(resId), 0);
    }

    public static void shortToast(Context context, String text) {
        showToast(context, text, 0);
    }

    public static void longToast(Context context, int resId) {
        showToast(context, context.getString(resId), 1);
    }

    public static void longToast(Context context, String text) {
        showToast(context, text, 1);
    }

    public static void shortToast(Context context, int resId, int gravity) {
        showToast(context, context.getString(resId), 0, gravity);
    }

    public static void shortToast(Context context, String text, int gravity) {
        showToast(context, text, 0, gravity);
    }

    public static void longToast(Context context, int resId, int gravity) {
        showToast(context, context.getString(resId), 1, gravity);
    }

    public static void longToast(Context context, String text, int gravity) {
        showToast(context, text, 1, gravity);
    }

    public static void showToast(Context context, String text, int duration) {
        if (!TextUtils.isEmpty(text)) {
            Toast toast = new Toast(context);
            View v = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C0136R.layout.layout_toast, null);
            ((TextView) v.findViewById(C0136R.id.toast_message)).setText(text);
            toast.setView(v);
            toast.setDuration(duration);
            toast.show();
        }
    }

    public static void showToast(Context context, String text, int duration, int gravity) {
        if (!TextUtils.isEmpty(text)) {
            Toast toast = new Toast(context);
            View v = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C0136R.layout.layout_toast, null);
            ((TextView) v.findViewById(C0136R.id.toast_message)).setText(text);
            toast.setGravity(gravity, 0, 0);
            toast.setView(v);
            toast.setDuration(duration);
            toast.show();
        }
    }
}

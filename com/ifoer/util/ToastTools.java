package com.ifoer.util;

import android.content.Context;
import android.widget.Toast;

public class ToastTools {
    public static void showToast(Context context, String toastContent) {
        Toast.makeText(context, toastContent, 1).show();
    }
}

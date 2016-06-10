package com.ifoer.util;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class DialogUtil {
    public static void setDialogSize(Activity activity) {
        int screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
        Window window = activity.getWindow();
        LayoutParams layoutParams = window.getAttributes();
        if (screenWidth < screenHeight) {
            layoutParams.width = (int) (((double) screenWidth) * 0.9d);
            layoutParams.height = (int) (((double) screenHeight) * 0.7d);
        } else {
            layoutParams.width = (int) (((double) screenWidth) * 0.8d);
            layoutParams.height = (int) (((double) screenHeight) * 0.75d);
        }
        window.setAttributes(layoutParams);
    }
}

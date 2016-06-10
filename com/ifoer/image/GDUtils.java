package com.ifoer.image;

import android.content.Context;
import java.util.concurrent.ExecutorService;

public class GDUtils {
    private GDUtils() {
    }

    public static GDApplication getGDApplication(Context context) {
        return (GDApplication) context.getApplicationContext();
    }

    public static ImageCache getImageCache(Context context) {
        return getGDApplication(context).getImageCache();
    }

    public static ExecutorService getExecutor(Context context) {
        return getGDApplication(context).getExecutor();
    }
}

package com.cnlaunch.framework.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.Iterator;
import java.util.Stack;

public class ActivityPageManager {
    private static Stack<Activity> activityStack;
    private static ActivityPageManager instance;

    public static ActivityPageManager getInstance() {
        if (instance == null) {
            instance = new ActivityPageManager();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack();
        }
        activityStack.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack();
        }
        activityStack.remove(activity);
    }

    public Activity currentActivity() {
        if (activityStack == null || activityStack.size() <= 0) {
            return null;
        }
        return (Activity) activityStack.lastElement();
    }

    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
        }
    }

    public void popAllActivityExceptOne(Class cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity != null && !activity.getClass().equals(cls)) {
                popActivity(activity);
            } else {
                return;
            }
        }
    }

    public void finishActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            finishActivity((Activity) activityStack.lastElement());
        }
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    public void finishActivity(Class<?> cls) {
        Iterator it = activityStack.iterator();
        while (it.hasNext()) {
            Activity activity = (Activity) it.next();
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    public void finishAllActivity() {
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            if (activityStack.get(i) != null) {
                ((Activity) activityStack.get(i)).finish();
            }
        }
        activityStack.clear();
    }

    public static void unbindReferences(View view) {
        if (view != null) {
            try {
                view.destroyDrawingCache();
                unbindViewReferences(view);
                if (view instanceof ViewGroup) {
                    unbindViewGroupReferences((ViewGroup) view);
                }
            } catch (Throwable th) {
            }
        }
    }

    private static void unbindViewGroupReferences(ViewGroup viewGroup) {
        int nrOfChildren = viewGroup.getChildCount();
        for (int i = 0; i < nrOfChildren; i++) {
            View view = viewGroup.getChildAt(i);
            unbindViewReferences(view);
            if (view instanceof ViewGroup) {
                unbindViewGroupReferences((ViewGroup) view);
            }
        }
        try {
            viewGroup.removeAllViews();
        } catch (Throwable th) {
        }
    }

    private static void unbindViewReferences(View view) {
        try {
            view.setOnClickListener(null);
            view.setOnCreateContextMenuListener(null);
            view.setOnFocusChangeListener(null);
            view.setOnKeyListener(null);
            view.setOnLongClickListener(null);
            view.setOnClickListener(null);
        } catch (Throwable th) {
        }
        Drawable d = view.getBackground();
        if (d != null) {
            d.setCallback(null);
        }
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            d = imageView.getDrawable();
            if (d != null) {
                d.setCallback(null);
            }
            imageView.setImageDrawable(null);
            imageView.setBackgroundDrawable(null);
        }
        if (view instanceof WebView) {
            WebView webview = (WebView) view;
            webview.stopLoading();
            webview.clearFormData();
            webview.clearDisappearingChildren();
            webview.setWebChromeClient(null);
            webview.setWebViewClient(null);
            webview.destroyDrawingCache();
            webview.destroy();
        }
        if (view instanceof ListView) {
            try {
                ((ListView) view).removeAllViewsInLayout();
            } catch (Throwable th2) {
            }
            ((ListView) view).destroyDrawingCache();
        }
    }

    public void exit(Context context) {
        try {
            finishAllActivity();
            ((ActivityManager) context.getSystemService("activity")).restartPackage(context.getPackageName());
            LruCacheManager.getInstance().evictAll();
            CacheManager.clearAll();
            System.exit(0);
            Process.killProcess(Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.tencent.mm.sdk.platformtools;

import android.content.Context;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.PluginIntent;

public final class MMApplicationContext {
    private static String am;
    private static Context f1659q;

    static {
        f1659q = null;
        am = PluginIntent.APP_PACKAGE_PATTERN;
    }

    private MMApplicationContext() {
    }

    public static Context getContext() {
        return f1659q;
    }

    public static String getDefaultPreferencePath() {
        return am + "_preferences";
    }

    public static String getPackageName() {
        return am;
    }

    public static void setContext(Context context) {
        f1659q = context;
        am = context.getPackageName();
        Log.m1655d("MicroMsg.MMApplicationContext", "setup application context for package: " + am);
    }
}

package com.tencent.mm.sdk.openapi;

import android.content.Context;

public class WXAPIFactory {
    private static WXAPIFactory f1625p;

    static {
        f1625p = null;
    }

    private WXAPIFactory() {
    }

    public static IWXAPI createWXAPI(Context context, String str) {
        if (f1625p == null) {
            f1625p = new WXAPIFactory();
        }
        return new WXApiImplV10(context, str);
    }

    public static IWXAPI createWXAPI(Context context, String str, boolean z) {
        if (f1625p == null) {
            f1625p = new WXAPIFactory();
        }
        return new WXApiImplV10(context, str, z);
    }
}

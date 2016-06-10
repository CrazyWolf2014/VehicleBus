package com.amap.mapapi.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.DisplayMetrics;
import java.io.InputStream;
import java.lang.reflect.Field;
import org.codehaus.jackson.smile.SmileConstants;
import org.ksoap2.SoapEnvelope;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.amap.mapapi.core.m */
public class PublicResManager {
    private Context f364a;
    private String[] f365b;
    private String[] f366c;
    private String[] f367d;
    private Bitmap[] f368e;

    public PublicResManager(Context context) {
        this.f364a = null;
        this.f365b = new String[]{"nomap.png", "beta.png", "poi_1.png", "compass_bg.png", "compass_pointer.png", "loc1.png", "loc2.png", "zoom_in_true_HVGA.9.png", "zoom_out_true_HVGA.9.png", "zoom_in_disabled_HVGA.9.png", "zoom_out_disabled_HVGA.9.png", "zoom_in_selected_HVGA.9.png", "zoom_out_selected_HVGA.9.png"};
        this.f366c = new String[]{"nomap.png", "beta.png", "poi_1_WVGA.png", "compass_bg.png", "compass_pointer.png", "loc1.png", "loc2.png", "zoom_in_true_WVGA.9.png", "zoom_out_true_WVGA.9.png", "zoom_in_disabled_WVGA.9.png", "zoom_out_disabled_WVGA.9.png", "zoom_in_selected_WVGA.9.png", "zoom_out_selected_WVGA.9.png"};
        this.f367d = new String[]{"nomap.png", "beta.png", "poi_1_QVGA.png", "compass_bg__QVGA.png", "compass_pointer_QVGA.png", "loc1_QVGA.png", "loc2_QVGA.png", "zoom_in_true_QVGA.9.png", "zoom_out_true_QVGA.9.png", "zoom_in_disabled_QVGA.9.png", "zoom_out_disabled_QVGA.9.png", "zoom_in_selected_QVGA.9.png", "zoom_out_selected_QVGA.9.png"};
        this.f368e = null;
        this.f364a = context;
    }

    public void m536a() {
        if (this.f368e != null) {
            int length = this.f368e.length;
            for (int i = 0; i < length; i++) {
                if (this.f368e[i] != null) {
                    this.f368e[i].recycle();
                    this.f368e[i] = null;
                }
            }
            this.f368e = null;
        }
    }

    public final Bitmap m533a(int i) {
        if (this.f368e == null) {
            this.f368e = new Bitmap[this.f365b.length];
        }
        if (this.f368e[i] != null && !this.f368e[i].isRecycled()) {
            return this.f368e[i];
        }
        String str = XmlPullParser.NO_NAMESPACE;
        if (ConfigableConst.f335e == 2) {
            str = this.f366c[i];
        } else if (ConfigableConst.f335e == 1) {
            str = this.f367d[i];
        } else if (ConfigableConst.f335e == 3) {
            str = this.f365b[i];
        }
        Bitmap a = m534a(this.f364a, str);
        if (a != null) {
            this.f368e[i] = a;
        }
        return this.f368e[i];
    }

    public final Bitmap m534a(Context context, String str) {
        Bitmap decodeStream;
        Exception e;
        try {
            InputStream open = context.getAssets().open(str);
            decodeStream = BitmapFactory.decodeStream(open);
            try {
                open.close();
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                return decodeStream;
            }
        } catch (Exception e3) {
            Exception exception = e3;
            decodeStream = null;
            e = exception;
            e.printStackTrace();
            return decodeStream;
        }
        return decodeStream;
    }

    public final Drawable m537b(Context context, String str) {
        Drawable bitmapDrawable = new BitmapDrawable(m534a(context, str));
        bitmapDrawable.setBounds(0, 0, bitmapDrawable.getIntrinsicWidth(), bitmapDrawable.getIntrinsicHeight());
        return bitmapDrawable;
    }

    public final NinePatchDrawable m535a(Context context, String str, byte[] bArr, Rect rect) {
        return new NinePatchDrawable(m534a(context, str), bArr, rect, null);
    }

    public void m538b() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        DisplayMetrics displayMetrics2 = this.f364a.getApplicationContext().getResources().getDisplayMetrics();
        Field field = null;
        try {
            field = displayMetrics2.getClass().getField("densityDpi");
        } catch (SecurityException e) {
        } catch (NoSuchFieldException e2) {
        }
        if (field != null) {
            int i;
            long j = (long) (displayMetrics2.widthPixels * displayMetrics2.heightPixels);
            try {
                i = field.getInt(displayMetrics2);
            } catch (IllegalArgumentException e3) {
                e3.printStackTrace();
                i = SmileConstants.TOKEN_PREFIX_SHORT_UNICODE;
            } catch (IllegalAccessException e4) {
                e4.printStackTrace();
                i = SmileConstants.TOKEN_PREFIX_SHORT_UNICODE;
            }
            if (i <= SoapEnvelope.VER12) {
                ConfigableConst.f335e = 1;
                return;
            } else if (i <= SmileConstants.TOKEN_PREFIX_SHORT_UNICODE) {
                ConfigableConst.f335e = 3;
                return;
            } else if (i <= 240) {
                ConfigableConst.f335e = 2;
                return;
            } else if (j > 153600) {
                ConfigableConst.f335e = 2;
                return;
            } else if (j < 153600) {
                ConfigableConst.f335e = 1;
                return;
            } else {
                ConfigableConst.f335e = 3;
                return;
            }
        }
        long j2 = (long) (displayMetrics2.widthPixels * displayMetrics2.heightPixels);
        if (j2 > 153600) {
            ConfigableConst.f335e = 2;
        } else if (j2 < 153600) {
            ConfigableConst.f335e = 1;
        } else {
            ConfigableConst.f335e = 3;
        }
    }
}

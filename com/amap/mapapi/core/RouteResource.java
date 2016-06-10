package com.amap.mapapi.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* renamed from: com.amap.mapapi.core.q */
public class RouteResource {
    public static Drawable f373a;
    public static Drawable f374b;
    public static Drawable f375c;
    public static Drawable f376d;
    public static Drawable f377e;
    public static Drawable f378f;
    public static Drawable f379g;
    public static Drawable f380h;
    public static Drawable f381i;
    public static Drawable f382j;
    public static Paint f383k;
    public static Paint f384l;
    public static Paint f385m;
    public static boolean f386n;

    static {
        f386n = false;
    }

    public static void m541a(Context context) {
        if (!f386n) {
            f384l = new Paint();
            f384l.setStyle(Style.STROKE);
            f384l.setColor(Color.rgb(54, Opcodes.FREM, 227));
            f384l.setAlpha(Opcodes.GETFIELD);
            f384l.setStrokeWidth(5.5f);
            f384l.setStrokeJoin(Join.ROUND);
            f384l.setStrokeCap(Cap.ROUND);
            f384l.setAntiAlias(true);
            f383k = new Paint();
            f383k.setStyle(Style.STROKE);
            f383k.setColor(Color.rgb(54, Opcodes.FREM, 227));
            f383k.setAlpha(Opcodes.FCMPG);
            f383k.setStrokeWidth(5.5f);
            f383k.setStrokeJoin(Join.ROUND);
            f383k.setStrokeCap(Cap.ROUND);
            f383k.setAntiAlias(true);
            f385m = new Paint();
            f385m.setStyle(Style.STROKE);
            f385m.setColor(Color.rgb(54, Opcodes.FREM, 227));
            f385m.setAlpha(Opcodes.GETFIELD);
            f385m.setStrokeWidth(5.5f);
            f385m.setStrokeJoin(Join.ROUND);
            f385m.setStrokeCap(Cap.ROUND);
            f385m.setAntiAlias(true);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            displayMetrics = context.getResources().getDisplayMetrics();
            long j = (long) (displayMetrics.heightPixels * displayMetrics.widthPixels);
            if (j > 153600) {
                RouteResource.m542a(context, 1);
            } else if (j < 153600) {
                RouteResource.m542a(context, 3);
            } else {
                RouteResource.m542a(context, 2);
            }
            Rect rect = new Rect(8, 4, 16, 14);
            byte[] bArr = new byte[]{(byte) 1, (byte) 2, (byte) 2, (byte) 9, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 8, (byte) 0, (byte) 0, (byte) 0, (byte) 16, (byte) 0, (byte) 0, (byte) 0, (byte) 4, (byte) 0, (byte) 0, (byte) 0, (byte) 14, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 8, (byte) 0, (byte) 0, (byte) 0, (byte) 18, (byte) 0, (byte) 0, (byte) 0, (byte) 4, (byte) 0, (byte) 0, (byte) 0, (byte) 17, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0};
            Rect rect2 = new Rect(17, 5, 8, 12);
            byte[] bArr2 = new byte[]{(byte) 1, (byte) 2, (byte) 2, (byte) 9, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 17, (byte) 0, (byte) 0, (byte) 0, (byte) 8, (byte) 0, (byte) 0, (byte) 0, (byte) 5, (byte) 0, (byte) 0, (byte) 0, (byte) 12, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 14, (byte) 0, (byte) 0, (byte) 0, (byte) 26, (byte) 0, (byte) 0, (byte) 0, (byte) 5, (byte) 0, (byte) 0, (byte) 0, (byte) 19, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0};
            f380h = ConfigableConst.f337g.m535a(context, "left_back.png", bArr, rect);
            f381i = ConfigableConst.f337g.m535a(context, "right_back.png", bArr2, rect2);
            f386n = true;
        }
    }

    public static Drawable m543b(Context context) {
        return ConfigableConst.f337g.m535a(context, "left_back.png", new byte[]{(byte) 1, (byte) 2, (byte) 2, (byte) 9, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 8, (byte) 0, (byte) 0, (byte) 0, (byte) 16, (byte) 0, (byte) 0, (byte) 0, (byte) 4, (byte) 0, (byte) 0, (byte) 0, (byte) 14, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 8, (byte) 0, (byte) 0, (byte) 0, (byte) 18, (byte) 0, (byte) 0, (byte) 0, (byte) 4, (byte) 0, (byte) 0, (byte) 0, (byte) 17, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0}, new Rect(8, 4, 16, 14));
    }

    public static void m540a() {
        Bitmap bitmap;
        f386n = false;
        if (f373a != null) {
            bitmap = ((BitmapDrawable) f373a).getBitmap();
            if (!(bitmap == null || bitmap.isRecycled())) {
                bitmap.recycle();
                f373a = null;
            }
        }
        if (f374b != null) {
            bitmap = ((BitmapDrawable) f374b).getBitmap();
            if (!(bitmap == null || bitmap.isRecycled())) {
                bitmap.recycle();
                f374b = null;
            }
        }
        if (f375c != null) {
            bitmap = ((BitmapDrawable) f375c).getBitmap();
            if (!(bitmap == null || bitmap.isRecycled())) {
                bitmap.recycle();
                f375c = null;
            }
        }
        if (f376d != null) {
            bitmap = ((BitmapDrawable) f376d).getBitmap();
            if (!(bitmap == null || bitmap.isRecycled())) {
                bitmap.recycle();
                f376d = null;
            }
        }
        if (f377e != null) {
            bitmap = ((BitmapDrawable) f377e).getBitmap();
            if (!(bitmap == null || bitmap.isRecycled())) {
                bitmap.recycle();
                f377e = null;
            }
        }
        if (f378f != null) {
            bitmap = ((BitmapDrawable) f378f).getBitmap();
            if (!(bitmap == null || bitmap.isRecycled())) {
                bitmap.recycle();
                f378f = null;
            }
        }
        if (f379g != null) {
            bitmap = ((BitmapDrawable) f379g).getBitmap();
            if (!(bitmap == null || bitmap.isRecycled())) {
                bitmap.recycle();
                f379g = null;
            }
        }
        if (f380h != null) {
            f380h = null;
        }
        if (f381i != null) {
            f381i = null;
        }
        if (f382j != null) {
            bitmap = ((BitmapDrawable) f382j).getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                f382j = null;
            }
        }
    }

    static void m542a(Context context, int i) {
        switch (i) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                try {
                    f373a = ConfigableConst.f337g.m537b(context, "start_w.png");
                    f374b = ConfigableConst.f337g.m537b(context, "end_w.png");
                    f375c = ConfigableConst.f337g.m537b(context, "foot_w.png");
                    f377e = ConfigableConst.f337g.m537b(context, "bus_w.png");
                    f376d = ConfigableConst.f337g.m537b(context, "car_w.png");
                    f378f = ConfigableConst.f337g.m537b(context, "starticon_w.png");
                    f379g = ConfigableConst.f337g.m537b(context, "endicon_w.png");
                    f382j = ConfigableConst.f337g.m537b(context, "route_coner_w.png");
                } catch (Exception e) {
                }
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                try {
                    f373a = ConfigableConst.f337g.m537b(context, "start.png");
                    f374b = ConfigableConst.f337g.m537b(context, "end.png");
                    f375c = ConfigableConst.f337g.m537b(context, "foot.png");
                    f377e = ConfigableConst.f337g.m537b(context, "bus.png");
                    f376d = ConfigableConst.f337g.m537b(context, "car.png");
                    f378f = ConfigableConst.f337g.m537b(context, "starticon.png");
                    f379g = ConfigableConst.f337g.m537b(context, "endicon.png");
                    f382j = ConfigableConst.f337g.m537b(context, "route_coner.png");
                } catch (Exception e2) {
                }
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                try {
                    f373a = ConfigableConst.f337g.m537b(context, "start.png");
                    f374b = ConfigableConst.f337g.m537b(context, "end.png");
                    f375c = ConfigableConst.f337g.m537b(context, "foot.png");
                    f377e = ConfigableConst.f337g.m537b(context, "bus.png");
                    f376d = ConfigableConst.f337g.m537b(context, "car.png");
                    f378f = ConfigableConst.f337g.m537b(context, "starticon.png");
                    f379g = ConfigableConst.f337g.m537b(context, "endicon.png");
                    f382j = ConfigableConst.f337g.m537b(context, "route_coner_q.png");
                } catch (Exception e3) {
                }
            default:
                try {
                    f373a = ConfigableConst.f337g.m537b(context, "start.png");
                    f374b = ConfigableConst.f337g.m537b(context, "end.png");
                    f375c = ConfigableConst.f337g.m537b(context, "foot.png");
                    f377e = ConfigableConst.f337g.m537b(context, "bus_w.png");
                    f376d = ConfigableConst.f337g.m537b(context, "car.png");
                    f378f = ConfigableConst.f337g.m537b(context, "starticon.png");
                    f379g = ConfigableConst.f337g.m537b(context, "endicon.png");
                    f382j = ConfigableConst.f337g.m537b(context, "route_coner.png");
                } catch (Exception e4) {
                }
        }
    }
}

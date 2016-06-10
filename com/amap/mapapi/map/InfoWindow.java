package com.amap.mapapi.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import com.amap.mapapi.core.ConfigableConst;
import com.amap.mapapi.core.CoreUtil;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapView.LayoutParams;
import org.xbill.DNS.KEYRecord;

/* renamed from: com.amap.mapapi.map.u */
class InfoWindow implements OnGestureListener {
    public static InfoWindow f697a;
    public static Drawable f698b;
    public static Bitmap f699c;
    protected MapView f700d;
    protected View f701e;
    protected GeoPoint f702f;
    protected long f703g;
    protected LayoutParams f704h;

    static {
        f697a = null;
        f698b = null;
        f699c = null;
    }

    public InfoWindow(MapView mapView, View view, GeoPoint geoPoint, Drawable drawable, LayoutParams layoutParams) {
        this.f703g = -1;
        this.f700d = mapView;
        this.f701e = view;
        this.f702f = geoPoint;
        this.f704h = layoutParams;
        m852a(drawable);
    }

    private void m852a(Drawable drawable) {
        if (drawable == null) {
            if (f698b == null) {
                InfoWindow.m851a(this.f700d.getContext());
            }
            drawable = f698b;
            drawable.setAlpha(KEYRecord.PROTOCOL_ANY);
        }
        this.f701e.setBackgroundDrawable(drawable);
    }

    private static void m851a(Context context) {
        byte[] bArr = new byte[]{(byte) 1, (byte) 2, (byte) 2, (byte) 9, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 20, (byte) 0, (byte) 0, (byte) 0, (byte) 19, (byte) 0, (byte) 0, (byte) 0, (byte) 15, (byte) 0, (byte) 0, (byte) 0, (byte) 36, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 20, (byte) 0, (byte) 0, (byte) 0, (byte) -117, (byte) 0, (byte) 0, (byte) 0, (byte) 15, (byte) 0, (byte) 0, (byte) 0, (byte) 29, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) -1, (byte) -1, (byte) -1, (byte) -14, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0};
        Rect rect = new Rect(20, 15, 19, 36);
        f699c = ConfigableConst.f337g.m534a(context, "popup_bg.9.png");
        f698b = new NinePatchDrawable(f699c, bArr, rect, null);
    }

    public void m854a() {
        if (f699c != null && !f699c.isRecycled()) {
            f699c.recycle();
            f699c = null;
        }
    }

    public void m855b() {
        if (!m853d()) {
            if (f697a != null) {
                f697a.m856c();
            }
            f697a = this;
            this.f700d.m648b().m1903a((OnGestureListener) this);
            if (this.f704h == null) {
                this.f704h = new LayoutParams(-2, -2, this.f702f, 25, 5, 85);
            }
            this.f700d.addView(this.f701e, this.f704h);
            this.f703g = CoreUtil.m483a();
            this.f700d.m648b().m1902a(this.f703g);
        }
    }

    public void m856c() {
        if (m853d() && this.f700d != null) {
            f697a = null;
            this.f700d.removeView(this.f701e);
            this.f700d.m648b().m1910b((OnGestureListener) this);
        }
    }

    private boolean m853d() {
        return f697a == this;
    }

    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    public void onLongPress(MotionEvent motionEvent) {
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }
}

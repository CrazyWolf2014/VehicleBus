package com.amap.mapapi.map;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.view.MotionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* compiled from: MultiTouchGestureDetector */
abstract class ah {
    static float f599j;
    private static Method f600o;
    private static Method f601p;
    private static boolean f602q;
    private static boolean f603r;
    MultiTouchGestureDetector f604a;
    int f605b;
    Matrix f606c;
    Matrix f607d;
    PointF f608e;
    PointF f609f;
    PointF f610g;
    float f611h;
    float f612i;
    boolean f613k;
    boolean f614l;
    boolean f615m;
    public int f616n;

    /* renamed from: com.amap.mapapi.map.ah.b */
    public interface MultiTouchGestureDetector {
        boolean m759a(float f, float f2);

        boolean m760a(float f, PointF pointF);

        boolean m761a(Matrix matrix);

        boolean m762a(PointF pointF);

        boolean m763b(float f);

        boolean m764b(Matrix matrix);
    }

    /* renamed from: com.amap.mapapi.map.ah.a */
    private static class MultiTouchGestureDetector extends ah {
        float f1862o;
        float f1863p;
        float f1864q;
        float f1865r;

        private MultiTouchGestureDetector() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean m1938a(android.view.MotionEvent r8) {
            /*
            r7 = this;
            r4 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
            r3 = 2;
            r6 = 1;
            r1 = 0;
            com.amap.mapapi.map.ah.m770c(r8);
            r0 = com.amap.mapapi.map.ah.f602q;
            if (r0 != 0) goto L_0x000f;
        L_0x000e:
            return r1;
        L_0x000f:
            r0 = r8.getAction();
            r0 = r0 & 255;
            switch(r0) {
                case 0: goto L_0x001b;
                case 1: goto L_0x0087;
                case 2: goto L_0x00bd;
                case 3: goto L_0x0018;
                case 4: goto L_0x0018;
                case 5: goto L_0x003b;
                case 6: goto L_0x008d;
                default: goto L_0x0018;
            };
        L_0x0018:
            r0 = r1;
        L_0x0019:
            r1 = r0;
            goto L_0x000e;
        L_0x001b:
            r0 = r8.getX();
            r7.f1862o = r0;
            r0 = r8.getY();
            r7.f1863p = r0;
            r0 = r7.d;
            r2 = r7.c;
            r0.set(r2);
            r0 = r7.e;
            r2 = r7.f1862o;
            r3 = r7.f1863p;
            r0.set(r2, r3);
            r7.b = r6;
            r0 = r1;
            goto L_0x0019;
        L_0x003b:
            r0 = r7.n;
            r0 = r0 + 1;
            r7.n = r0;
            r0 = r7.n;
            if (r0 != r6) goto L_0x0018;
        L_0x0045:
            r7.m = r6;
            r0 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            j = r0;
            r0 = r7.m1937c(r8);
            r7.h = r0;
            r0 = r7.h;
            r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
            if (r0 <= 0) goto L_0x0018;
        L_0x0057:
            r0 = r7.c;
            r0.reset();
            r0 = r7.d;
            r0.reset();
            r0 = r7.d;
            r2 = r7.c;
            r0.set(r2);
            r0 = r7.f;
            r7.m1936a(r0, r8);
            r7.b = r3;
            r7.k = r6;
            r0 = r7.a;
            r2 = r7.e;
            r0 = r0.m762a(r2);
            r0 = r0 | r1;
            r1 = r7.f;
            r1 = r1.x;
            r7.f1864q = r1;
            r1 = r7.f;
            r1 = r1.y;
            r7.f1865r = r1;
            goto L_0x0019;
        L_0x0087:
            r7.k = r1;
            r7.b = r1;
            r0 = r1;
            goto L_0x0019;
        L_0x008d:
            r0 = r7.n;
            r0 = r0 + -1;
            r7.n = r0;
            r0 = r7.n;
            if (r0 != r6) goto L_0x009b;
        L_0x0097:
            r7.m = r6;
            r7.b = r3;
        L_0x009b:
            r0 = r7.n;
            if (r0 != 0) goto L_0x0018;
        L_0x009f:
            r0 = r7.f;
            r7.m1936a(r0, r8);
            r7.l = r1;
            r7.m = r1;
            r0 = r7.k;
            if (r0 == 0) goto L_0x0018;
        L_0x00ac:
            r7.k = r1;
            r0 = r7.a;
            r2 = r7.i;
            r3 = r7.f;
            r0 = r0.m760a(r2, r3);
            r0 = r0 | r1;
            r7.b = r1;
            goto L_0x0019;
        L_0x00bd:
            r0 = r7.b;
            if (r0 != r6) goto L_0x0105;
        L_0x00c1:
            r0 = r8.getX();
            r2 = r8.getY();
            r3 = r7.c;
            r4 = r7.d;
            r3.set(r4);
            r3 = r7.c;
            r4 = r8.getX();
            r5 = r7.e;
            r5 = r5.x;
            r4 = r4 - r5;
            r5 = r8.getY();
            r6 = r7.e;
            r6 = r6.y;
            r5 = r5 - r6;
            r3.postTranslate(r4, r5);
            r3 = r7.a;
            r4 = r7.f1862o;
            r4 = r0 - r4;
            r5 = r7.f1863p;
            r5 = r2 - r5;
            r3 = r3.m759a(r4, r5);
            r1 = r1 | r3;
            r7.f1862o = r0;
            r7.f1863p = r2;
            r0 = r7.a;
            r2 = r7.c;
            r0 = r0.m761a(r2);
            r0 = r0 | r1;
            goto L_0x0019;
        L_0x0105:
            r0 = r7.b;
            if (r0 != r3) goto L_0x0018;
        L_0x0109:
            r2 = r7.m1937c(r8);
            r0 = 0;
            r7.i = r0;
            r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r0 <= 0) goto L_0x0018;
        L_0x0114:
            r0 = r7.h;
            r0 = r2 - r0;
            r0 = java.lang.Math.abs(r0);
            r3 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
            r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1));
            if (r0 <= 0) goto L_0x0018;
        L_0x0122:
            r0 = r7.c;
            r3 = r7.d;
            r0.set(r3);
            r0 = r7.h;
            r0 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1));
            if (r0 <= 0) goto L_0x0196;
        L_0x012f:
            r0 = r7.h;
            r0 = r2 / r0;
        L_0x0133:
            r7.i = r0;
            r0 = r7.h;
            r0 = r2 / r0;
            j = r0;
            r0 = r7.h;
            r0 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1));
            if (r0 >= 0) goto L_0x0146;
        L_0x0141:
            r0 = r7.i;
            r0 = -r0;
            r7.i = r0;
        L_0x0146:
            r0 = r7.g;
            r7.m1936a(r0, r8);
            r0 = r7.a;
            r3 = r7.g;
            r3 = r3.x;
            r4 = r7.f1864q;
            r3 = r3 - r4;
            r4 = r7.g;
            r4 = r4.y;
            r5 = r7.f1865r;
            r4 = r4 - r5;
            r0 = r0.m759a(r3, r4);
            r0 = r0 | r1;
            r1 = r7.g;
            r1 = r1.x;
            r7.f1864q = r1;
            r1 = r7.g;
            r1 = r1.y;
            r7.f1865r = r1;
            r1 = r7.c;
            r3 = r7.h;
            r3 = r2 / r3;
            r4 = r7.h;
            r2 = r2 / r4;
            r4 = r7.f;
            r4 = r4.x;
            r5 = r7.f;
            r5 = r5.y;
            r1.postScale(r3, r2, r4, r5);
            r1 = r7.a;
            r2 = r7.i;
            r1 = r1.m763b(r2);
            r0 = r0 | r1;
            r1 = r7.a;
            r2 = r7.c;
            r1 = r1.m764b(r2);
            r0 = r0 | r1;
            r7.l = r6;
            goto L_0x0019;
        L_0x0196:
            r0 = r7.h;
            r0 = r0 / r2;
            goto L_0x0133;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.amap.mapapi.map.ah.a.a(android.view.MotionEvent):boolean");
        }

        private float m1937c(MotionEvent motionEvent) {
            float floatValue;
            float f = 0.0f;
            try {
                floatValue = ((Float) ah.f600o.invoke(motionEvent, new Object[]{Integer.valueOf(0)})).floatValue() - ((Float) ah.f600o.invoke(motionEvent, new Object[]{Integer.valueOf(1)})).floatValue();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                floatValue = 0.0f;
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
                floatValue = 0.0f;
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
                floatValue = 0.0f;
            }
            try {
                f = ((Float) ah.f601p.invoke(motionEvent, new Object[]{Integer.valueOf(0)})).floatValue() - ((Float) ah.f601p.invoke(motionEvent, new Object[]{Integer.valueOf(1)})).floatValue();
            } catch (IllegalArgumentException e4) {
                e4.printStackTrace();
            } catch (IllegalAccessException e22) {
                e22.printStackTrace();
            } catch (InvocationTargetException e32) {
                e32.printStackTrace();
            }
            return FloatMath.sqrt((floatValue * floatValue) + (f * f));
        }

        private void m1936a(PointF pointF, MotionEvent motionEvent) {
            float floatValue;
            float f = 0.0f;
            try {
                floatValue = ((Float) ah.f600o.invoke(motionEvent, new Object[]{Integer.valueOf(1)})).floatValue() + ((Float) ah.f600o.invoke(motionEvent, new Object[]{Integer.valueOf(0)})).floatValue();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                floatValue = 0.0f;
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
                floatValue = 0.0f;
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
                floatValue = 0.0f;
            }
            try {
                f = ((Float) ah.f601p.invoke(motionEvent, new Object[]{Integer.valueOf(0)})).floatValue() + ((Float) ah.f601p.invoke(motionEvent, new Object[]{Integer.valueOf(1)})).floatValue();
            } catch (IllegalArgumentException e4) {
                e4.printStackTrace();
            } catch (IllegalAccessException e22) {
                e22.printStackTrace();
            } catch (InvocationTargetException e32) {
                e32.printStackTrace();
            }
            pointF.set(floatValue / 2.0f, f / 2.0f);
        }
    }

    public abstract boolean m771a(MotionEvent motionEvent);

    ah() {
        this.f605b = 0;
        this.f606c = new Matrix();
        this.f607d = new Matrix();
        this.f608e = new PointF();
        this.f609f = new PointF();
        this.f610g = new PointF();
        this.f611h = 1.0f;
        this.f612i = 1.0f;
        this.f613k = false;
        this.f614l = false;
        this.f615m = false;
        this.f616n = 0;
    }

    static {
        f599j = 1.0f;
        f602q = false;
        f603r = false;
    }

    public static ah m765a(Context context, MultiTouchGestureDetector multiTouchGestureDetector) {
        ah multiTouchGestureDetector2 = new MultiTouchGestureDetector();
        multiTouchGestureDetector2.f604a = multiTouchGestureDetector;
        return multiTouchGestureDetector2;
    }

    private static void m770c(MotionEvent motionEvent) {
        if (!f603r) {
            f603r = true;
            try {
                f600o = motionEvent.getClass().getMethod("getX", new Class[]{Integer.TYPE});
                f601p = motionEvent.getClass().getMethod("getY", new Class[]{Integer.TYPE});
                if (f600o != null && f601p != null) {
                    f602q = true;
                }
            } catch (Exception e) {
            }
        }
    }
}

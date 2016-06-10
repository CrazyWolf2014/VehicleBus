package com.amap.mapapi.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;
import com.amap.mapapi.core.ConfigableConst;
import com.amap.mapapi.core.CoreUtil;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.MapServerUrl;
import com.amap.mapapi.core.ServerUrlSetting;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.amap.mapapi.map.ZoomButtonsController.OnZoomListener;
import com.amap.mapapi.map.ah.MultiTouchGestureDetector;
import com.amap.mapapi.map.aw.TrackballGestureDetector;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.cnlaunch.x431pro.common.RequestCode;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.entity.Constant;
import com.ifoer.expeditionphone.WelcomeActivity;
import com.ifoer.util.MyHttpException;
import com.mapabc.minimap.map.vmap.NativeMap;
import com.mapabc.minimap.map.vmap.NativeMapEngine;
import com.mapabc.minimap.map.vmap.VMapProjection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.achartengine.renderer.DefaultRenderer;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class MapView extends ViewGroup {
    public boolean VMapMode;
    public boolean VisInited;
    NativeMap f484a;
    Bitmap f485b;
    protected boolean bfirstDrawed;
    int[] f486c;
    public int centerX;
    public int centerY;
    ZoomButtonsController f487d;
    boolean f488e;
    NativeMapEngine f489f;
    ByteBuffer f490g;
    Bitmap f491h;
    public int height;
    ConnectionManager f492i;
    public boolean isInited;
    ak f493j;
    private MapActivity f494k;
    private ag f495l;
    private C1036c f496m;
    public C0094e mRouteCtrl;
    public int mapAngle;
    public int mapLevel;
    private MapController f497n;
    private C0096g f498o;
    private boolean f499p;
    private LayerCtrlManager f500q;
    private final int[] f501r;
    private boolean f502s;
    private aw f503t;
    public at tileDownloadCtrl;
    private TrackballGestureDetector f504u;
    private boolean f505v;
    private int f506w;
    public int width;
    private C0093d f507x;
    private Context f508y;

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        public static final int BOTTOM = 80;
        public static final int BOTTOM_CENTER = 81;
        public static final int CENTER = 17;
        public static final int CENTER_HORIZONTAL = 1;
        public static final int CENTER_VERTICAL = 16;
        public static final int LEFT = 3;
        public static final int MODE_MAP = 0;
        public static final int MODE_VIEW = 1;
        public static final int RIGHT = 5;
        public static final int TOP = 48;
        public static final int TOP_LEFT = 51;
        public int alignment;
        public int mode;
        public GeoPoint point;
        public int f447x;
        public int f448y;

        public LayoutParams(int i, int i2, GeoPoint geoPoint, int i3) {
            this(i, i2, geoPoint, MODE_MAP, MODE_MAP, i3);
        }

        public LayoutParams(int i, int i2, GeoPoint geoPoint, int i3, int i4, int i5) {
            super(i, i2);
            this.mode = MODE_VIEW;
            this.point = null;
            this.f447x = MODE_MAP;
            this.f448y = MODE_MAP;
            this.alignment = TOP_LEFT;
            this.mode = MODE_MAP;
            this.point = geoPoint;
            this.f447x = i3;
            this.f448y = i4;
            this.alignment = i5;
        }

        public LayoutParams(int i, int i2, int i3, int i4, int i5) {
            super(i, i2);
            this.mode = MODE_VIEW;
            this.point = null;
            this.f447x = MODE_MAP;
            this.f448y = MODE_MAP;
            this.alignment = TOP_LEFT;
            this.f447x = i3;
            this.f448y = i4;
            this.alignment = i5;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.mode = MODE_VIEW;
            this.point = null;
            this.f447x = MODE_MAP;
            this.f448y = MODE_MAP;
            this.alignment = TOP_LEFT;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.mode = MODE_VIEW;
            this.point = null;
            this.f447x = MODE_MAP;
            this.f448y = MODE_MAP;
            this.alignment = TOP_LEFT;
        }
    }

    public enum ReticleDrawMode {
        DRAW_RETICLE_NEVER,
        DRAW_RETICLE_OVER,
        DRAW_RETICLE_UNDER
    }

    /* renamed from: com.amap.mapapi.map.MapView.a */
    class C0091a implements Comparator {
        int f450a;
        int f451b;
        final /* synthetic */ MapView f452c;

        public C0091a(MapView mapView, int i, int i2) {
            this.f452c = mapView;
            this.f450a = i;
            this.f451b = i2;
        }

        public int compare(Object obj, Object obj2) {
            bf bfVar = (bf) obj;
            bf bfVar2 = (bf) obj2;
            int i = bfVar.f665a - this.f450a;
            int i2 = bfVar.f666b - this.f451b;
            int i3 = bfVar2.f665a - this.f450a;
            int i4 = bfVar2.f666b - this.f451b;
            i = (i * i) + (i2 * i2);
            i2 = (i3 * i3) + (i4 * i4);
            if (i > i2) {
                return 1;
            }
            if (i < i2) {
                return -1;
            }
            return 0;
        }
    }

    /* renamed from: com.amap.mapapi.map.MapView.b */
    public interface C0092b {
        void m595a(int i);
    }

    /* renamed from: com.amap.mapapi.map.MapView.d */
    public static abstract class C0093d {
        public abstract void m596a(int i, int i2, int i3, int i4);
    }

    /* renamed from: com.amap.mapapi.map.MapView.e */
    public class C0094e {
        public boolean f453a;
        final /* synthetic */ MapView f454b;
        private ImageView[] f455c;
        private Drawable[] f456d;
        private C0092b f457e;
        private boolean f458f;
        private int f459g;
        private int f460h;
        private int f461i;
        private int f462j;
        private int f463k;
        private int f464l;
        private OnClickListener f465m;

        public C0094e(MapView mapView) {
            this.f454b = mapView;
            this.f455c = null;
            this.f456d = null;
            this.f457e = null;
            this.f453a = false;
            this.f458f = false;
            this.f459g = Service.CISCO_FNA;
            this.f460h = 85;
            this.f461i = 50;
            this.f462j = 35;
            this.f463k = 30;
            this.f464l = 25;
            this.f465m = new ae(this);
        }

        private void m600b(int i, int i2) {
            int i3 = 0;
            String[] strArr = new String[]{"overview.png", "detail.png", "prev.png", "next.png", "overview_disable.png", "detail_disable.png", "prev_disable.png", "next_disable.png"};
            for (int i4 = 0; i4 < 8; i4++) {
                this.f456d[i4] = ConfigableConst.f337g.m537b(this.f454b.f494k, strArr[i4]);
            }
            while (i3 < 4) {
                this.f455c[i3] = new ImageView(this.f454b.f494k);
                this.f455c[i3].setImageDrawable(this.f456d[i3]);
                this.f454b.addView(this.f455c[i3], this.f454b.generateDefaultLayoutParams());
                this.f455c[i3].setVisibility(4);
                this.f455c[i3].setOnClickListener(this.f465m);
                i3++;
            }
        }

        public void m601a() {
            int i;
            if (this.f456d != null) {
                int length = this.f456d.length;
                for (i = 0; i < length; i++) {
                    if (this.f456d[i] != null) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.f456d[i];
                        if (bitmapDrawable != null) {
                            Bitmap bitmap = bitmapDrawable.getBitmap();
                            if (!(bitmap == null || bitmap.isRecycled())) {
                                bitmap.recycle();
                            }
                        }
                    }
                }
            }
            this.f456d = null;
            if (this.f455c != null) {
                i = this.f455c.length;
                for (int i2 = 0; i2 < i; i2++) {
                    this.f455c[i2] = null;
                }
                this.f455c = null;
            }
        }

        public void m602a(int i, int i2) {
            if (this.f455c != null && this.f456d != null) {
                int i3;
                int intrinsicWidth = this.f455c[0].getDrawable().getIntrinsicWidth();
                int i4 = 0;
                if (this.f454b.f494k.getResources().getConfiguration().orientation == 1) {
                    i4 = (i / 2) - (intrinsicWidth + 8);
                    i3 = (i / 2) + (intrinsicWidth + 8);
                } else if (this.f454b.f494k.getResources().getConfiguration().orientation == 2) {
                    i4 = (i / 2) - (intrinsicWidth + 15);
                    i3 = (i / 2) + (intrinsicWidth + 15);
                } else {
                    i3 = 0;
                }
                android.view.ViewGroup.LayoutParams layoutParams = new LayoutParams(-2, -2, i4, i2, 85);
                android.view.ViewGroup.LayoutParams layoutParams2 = new LayoutParams(-2, -2, i4, i2, 83);
                android.view.ViewGroup.LayoutParams layoutParams3 = new LayoutParams(-2, -2, i3, i2, 85);
                android.view.ViewGroup.LayoutParams layoutParams4 = new LayoutParams(-2, -2, i3, i2, 83);
                this.f454b.updateViewLayout(this.f455c[0], layoutParams);
                this.f454b.updateViewLayout(this.f455c[1], layoutParams2);
                this.f454b.updateViewLayout(this.f455c[2], layoutParams3);
                this.f454b.updateViewLayout(this.f455c[3], layoutParams4);
            }
        }

        public void m604a(boolean z, C0092b c0092b) {
            boolean z2 = false;
            if (this.f455c == null || this.f456d == null) {
                this.f455c = new ImageView[4];
                this.f456d = new Drawable[8];
                if (ConfigableConst.f335e == 2) {
                    m600b(this.f459g, this.f460h);
                } else if (ConfigableConst.f335e == 1) {
                    m600b(this.f463k, this.f464l);
                } else {
                    m600b(this.f461i, this.f462j);
                }
                m602a(this.f454b.f495l.f594b.m750c(), this.f454b.f495l.f594b.m752d());
            }
            this.f457e = c0092b;
            for (int i = 0; i < 4; i++) {
                int i2;
                ImageView imageView = this.f455c[i];
                if (z) {
                    i2 = 0;
                } else {
                    i2 = 4;
                }
                imageView.setVisibility(i2);
                if (z) {
                    this.f455c[i].bringToFront();
                }
            }
            C0096g c = this.f454b.f498o;
            if (!z) {
                z2 = true;
            }
            c.m617a(z2);
            this.f458f = z;
        }

        public void m603a(boolean z) {
            m597a(2, z);
        }

        public void m605b(boolean z) {
            m597a(3, z);
        }

        public void m607c(boolean z) {
            m597a(1, z);
        }

        public void m608d(boolean z) {
            m597a(0, z);
        }

        public boolean m606b() {
            return this.f458f;
        }

        private void m597a(int i, boolean z) {
            this.f455c[i].setImageDrawable(this.f456d[z ? i : i + 4]);
        }
    }

    /* renamed from: com.amap.mapapi.map.MapView.f */
    class C0095f extends ImageView implements OnClickListener {
        final /* synthetic */ MapView f466a;
        private int f467b;

        public C0095f(MapView mapView, int i) {
            this.f466a = mapView;
            super(mapView.f494k);
            this.f467b = i;
            setClickable(true);
            setOnClickListener(this);
        }

        public void onClick(View view) {
            if (ConfigableConst.f343m) {
                MapView mapView;
                MapView.m633f(this.f466a);
                if (FragmentTransaction.TRANSIT_FRAGMENT_OPEN == this.f467b) {
                    if (this.f466a.f495l.f594b.m755g().VMapMode && this.f466a.f495l.f594b.m753e() < this.f466a.mapLevel && this.f466a.mapLevel < this.f466a.f495l.f594b.m741a()) {
                        mapView = this.f466a;
                        mapView.mapLevel += this.f466a.f506w;
                    }
                    this.f466a.f497n.m593a(this.f466a.f506w);
                    if (!this.f466a.f497n.m592a()) {
                        this.f466a.f506w = 0;
                    }
                }
                if (4098 == this.f467b) {
                    if (this.f466a.f495l.f594b.m755g().VMapMode && this.f466a.f495l.f594b.m753e() < this.f466a.mapLevel && this.f466a.mapLevel > this.f466a.f495l.f594b.m747b()) {
                        mapView = this.f466a;
                        mapView.mapLevel -= this.f466a.f506w;
                    }
                    this.f466a.f497n.m594b(this.f466a.f506w);
                    if (!this.f466a.f497n.m592a()) {
                        this.f466a.f506w = 0;
                    }
                }
            }
        }

        public void m609a(boolean z) {
            int i = z ? 0 : 4;
            if (getVisibility() != i) {
                setVisibility(i);
            }
        }

        public void m610b(boolean z) {
            setFocusable(z);
        }
    }

    /* renamed from: com.amap.mapapi.map.MapView.g */
    public class C0096g {
        Bitmap f468a;
        Bitmap f469b;
        Bitmap f470c;
        Bitmap f471d;
        Bitmap f472e;
        Bitmap f473f;
        StateListDrawable f474g;
        StateListDrawable f475h;
        final /* synthetic */ MapView f476i;
        private C0095f f477j;
        private C0095f f478k;
        private FixViewOverlay f479l;
        private boolean f480m;
        private boolean f481n;
        private Handler f482o;
        private Runnable f483p;

        public void m617a(boolean z) {
            if (!this.f480m) {
                return;
            }
            if (this.f476i.mRouteCtrl == null || !this.f476i.mRouteCtrl.m606b()) {
                if (z) {
                    this.f482o.removeCallbacks(this.f483p);
                    this.f482o.postDelayed(this.f483p, 10000);
                }
                m622d();
                this.f478k.m609a(z);
                this.f477j.m609a(z);
                if (this.f476i.f487d != null && this.f476i.f487d.getOnZoomListener() != null && this.f481n != z) {
                    this.f476i.f487d.getOnZoomListener().onVisibilityChanged(z);
                    this.f481n = z;
                }
            }
        }

        public void m616a() {
            m617a(true);
        }

        public C0096g(MapView mapView, Context context) {
            this.f476i = mapView;
            this.f480m = true;
            this.f481n = true;
            this.f482o = new Handler();
            this.f483p = new af(this);
            this.f468a = null;
            this.f469b = null;
            this.f470c = null;
            this.f471d = null;
            this.f472e = null;
            this.f473f = null;
            this.f474g = null;
            this.f475h = null;
            m613h();
            m618b(false);
        }

        private void m613h() {
            this.f478k = new C0095f(this.f476i, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            this.f477j = new C0095f(this.f476i, 4098);
            this.f476i.addView(this.f478k, this.f476i.generateDefaultLayoutParams());
            this.f476i.addView(this.f477j, this.f476i.generateDefaultLayoutParams());
            this.f479l = new FixViewOverlay(new Rect(0, 0, 0, 0));
            this.f476i.f495l.f596d.m708a(this.f479l, true);
            this.f474g = m614i();
            this.f475h = m615j();
            this.f477j.setBackgroundDrawable(this.f475h);
            this.f478k.setBackgroundDrawable(this.f474g);
            m622d();
        }

        public void m618b(boolean z) {
            this.f480m = true;
            m617a(z);
            this.f480m = z;
            this.f476i.f495l.f596d.m708a(this.f479l, z);
        }

        public boolean m619b() {
            return this.f478k.isShown();
        }

        public void m621c(boolean z) {
            this.f478k.m610b(z);
            this.f477j.m610b(z);
        }

        public void m620c() {
            if (this.f468a != null) {
                this.f468a = null;
            }
            if (this.f469b != null) {
                this.f469b = null;
            }
            if (this.f470c != null) {
                this.f470c = null;
            }
            if (this.f471d != null) {
                this.f471d = null;
            }
            if (this.f472e != null) {
                this.f472e = null;
            }
            if (this.f473f != null) {
                this.f473f = null;
            }
            if (this.f474g != null) {
                this.f474g = null;
            }
            if (this.f475h != null) {
                this.f475h = null;
            }
        }

        private StateListDrawable m614i() {
            StateListDrawable stateListDrawable = new StateListDrawable();
            if (this.f468a == null || this.f468a.isRecycled()) {
                this.f468a = ConfigableConst.f337g.m533a(ConfigableConst.ConfigableConst.ezoomin.ordinal());
            }
            if (this.f469b == null || this.f469b.isRecycled()) {
                this.f469b = ConfigableConst.f337g.m533a(ConfigableConst.ConfigableConst.ezoomindisable.ordinal());
            }
            if (this.f470c == null || this.f470c.isRecycled()) {
                this.f470c = ConfigableConst.f337g.m533a(ConfigableConst.ConfigableConst.ezoominselected.ordinal());
            }
            Drawable bitmapDrawable = new BitmapDrawable(this.f468a);
            Drawable bitmapDrawable2 = new BitmapDrawable(this.f470c);
            Drawable bitmapDrawable3 = new BitmapDrawable(this.f469b);
            stateListDrawable.addState(MapView.PRESSED_ENABLED_STATE_SET, bitmapDrawable2);
            stateListDrawable.addState(MapView.ENABLED_STATE_SET, bitmapDrawable);
            stateListDrawable.addState(MapView.EMPTY_STATE_SET, bitmapDrawable3);
            return stateListDrawable;
        }

        private StateListDrawable m615j() {
            StateListDrawable stateListDrawable = new StateListDrawable();
            if (this.f471d == null || this.f471d.isRecycled()) {
                this.f471d = ConfigableConst.f337g.m533a(ConfigableConst.ConfigableConst.ezoomout.ordinal());
            }
            if (this.f472e == null || this.f472e.isRecycled()) {
                this.f472e = ConfigableConst.f337g.m533a(ConfigableConst.ConfigableConst.ezoomoutdisable.ordinal());
            }
            if (this.f473f == null || this.f473f.isRecycled()) {
                this.f473f = ConfigableConst.f337g.m533a(ConfigableConst.ConfigableConst.ezoomoutselected.ordinal());
            }
            Drawable bitmapDrawable = new BitmapDrawable(this.f471d);
            Drawable bitmapDrawable2 = new BitmapDrawable(this.f473f);
            Drawable bitmapDrawable3 = new BitmapDrawable(this.f472e);
            stateListDrawable.addState(MapView.PRESSED_ENABLED_STATE_SET, bitmapDrawable2);
            stateListDrawable.addState(MapView.ENABLED_STATE_SET, bitmapDrawable);
            stateListDrawable.addState(MapView.EMPTY_STATE_SET, bitmapDrawable3);
            return stateListDrawable;
        }

        public void m622d() {
            if (this.f476i.f495l.f594b.m753e() == this.f476i.f495l.f594b.m747b()) {
                this.f477j.setPressed(false);
                this.f477j.setEnabled(false);
            } else {
                this.f477j.setEnabled(true);
            }
            if (this.f476i.f495l.f594b.m753e() == this.f476i.f495l.f594b.m741a()) {
                this.f478k.setPressed(false);
                this.f478k.setEnabled(false);
                return;
            }
            this.f478k.setEnabled(true);
        }

        private void m611a(int i, int i2) {
            android.view.ViewGroup.LayoutParams layoutParams = new LayoutParams(-2, -2, (i / 2) + 1, i2 - 8, 83);
            android.view.ViewGroup.LayoutParams layoutParams2 = new LayoutParams(-2, -2, (i / 2) - 1, i2 - 8, 85);
            if (-1 == this.f476i.indexOfChild(this.f478k)) {
                this.f476i.addView(this.f478k, layoutParams);
            } else {
                this.f476i.updateViewLayout(this.f478k, layoutParams);
            }
            if (-1 == this.f476i.indexOfChild(this.f477j)) {
                this.f476i.addView(this.f477j, layoutParams2);
            } else {
                this.f476i.updateViewLayout(this.f477j, layoutParams2);
            }
        }

        public void m623e() {
            this.f478k.bringToFront();
            this.f477j.bringToFront();
        }

        public C0095f m624f() {
            return this.f478k;
        }

        public C0095f m625g() {
            return this.f477j;
        }
    }

    /* renamed from: com.amap.mapapi.map.MapView.1 */
    class C10341 extends be {
        final /* synthetic */ MapView f1811a;

        C10341(MapView mapView) {
            this.f1811a = mapView;
        }

        public String m1890a(int i, int i2, int i3) {
            return MapServerUrl.m503a().m511e() + "/appmaptile?z=" + i3 + "&x=" + i + "&y=" + i2 + "&lang=zh_cn&size=1&scale=1&style=6";
        }
    }

    /* renamed from: com.amap.mapapi.map.MapView.2 */
    class C10352 extends be {
        final /* synthetic */ MapView f1812a;

        C10352(MapView mapView) {
            this.f1812a = mapView;
        }

        public String m1891a(int i, int i2, int i3) {
            return MapServerUrl.m503a().m507c() + "/trafficengine/mapabc/traffictile?v=w2.61&zoom=" + (17 - i3) + "&x=" + i + "&y=" + i2;
        }
    }

    /* renamed from: com.amap.mapapi.map.MapView.c */
    public class C1036c extends ImageView implements OnDoubleTapListener, OnGestureListener, MultiTouchGestureDetector {
        final /* synthetic */ MapView f1813a;
        private Point f1814b;
        private GestureDetector f1815c;
        private ah f1816d;
        private boolean f1817e;
        private ArrayList<OnGestureListener> f1818f;
        private ArrayList<MultiTouchGestureDetector> f1819g;
        private Scroller f1820h;
        private int f1821i;
        private int f1822j;
        private Matrix f1823k;
        private float f1824l;
        private boolean f1825m;
        private float f1826n;
        private float f1827o;
        private int f1828p;
        private int f1829q;
        private long f1830r;
        private int f1831s;
        private int f1832t;
        private final long f1833u;

        public C1036c(MapView mapView, Context context) {
            this.f1813a = mapView;
            super(context);
            this.f1817e = false;
            this.f1818f = new ArrayList();
            this.f1819g = new ArrayList();
            this.f1821i = 0;
            this.f1822j = 0;
            this.f1823k = new Matrix();
            this.f1824l = 1.0f;
            this.f1825m = false;
            this.f1830r = 0;
            this.f1831s = 0;
            this.f1832t = 0;
            this.f1833u = 300;
            this.f1814b = null;
            this.f1815c = new GestureDetector(this);
            this.f1816d = ah.m765a(context, this);
            this.f1820h = new Scroller(context);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            displayMetrics = context.getApplicationContext().getResources().getDisplayMetrics();
            this.f1828p = displayMetrics.widthPixels;
            this.f1829q = displayMetrics.heightPixels;
            this.f1821i = displayMetrics.widthPixels / 2;
            this.f1822j = displayMetrics.heightPixels / 2;
        }

        public ah m1900a() {
            return this.f1816d;
        }

        protected void onDraw(Canvas canvas) {
            this.f1813a.f495l.f596d.m705a(canvas, this.f1823k, this.f1826n, this.f1827o);
        }

        public void m1901a(float f) {
            this.f1824l = f;
        }

        public float m1909b() {
            return this.f1824l;
        }

        public void m1914c() {
            this.f1826n = 0.0f;
            this.f1827o = 0.0f;
        }

        public boolean m1908a(MotionEvent motionEvent) {
            this.f1813a.f498o.m616a();
            int action = motionEvent.getAction();
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            this.f1814b = null;
            switch (action) {
                case KEYRecord.OWNER_USER /*0*/:
                    this.f1814b = new Point(x, y);
                    break;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    this.f1813a.f497n.scrollBy((int) (motionEvent.getX() * 25.0f), (int) (motionEvent.getY() * 25.0f));
                    break;
            }
            return false;
        }

        private void m1899e() {
            if (this.f1814b != null) {
                int i = this.f1814b.x - this.f1831s;
                int i2 = this.f1814b.y - this.f1832t;
                this.f1814b.x = this.f1831s;
                this.f1814b.y = this.f1832t;
                this.f1813a.f497n.scrollBy(i, i2);
            }
        }

        private void m1895a(int i, int i2) {
            if (this.f1814b != null) {
                this.f1831s = i;
                this.f1832t = i2;
                m1899e();
            }
        }

        public boolean m1913b(MotionEvent motionEvent) {
            this.f1813a.f498o.m616a();
            boolean a = this.f1816d.m771a(motionEvent);
            if (a) {
                return a;
            }
            return this.f1815c.onTouchEvent(motionEvent);
        }

        public boolean onDown(MotionEvent motionEvent) {
            if (this.f1814b == null) {
                this.f1814b = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
            } else {
                this.f1814b.set((int) motionEvent.getX(), (int) motionEvent.getY());
            }
            return true;
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            this.f1820h.fling(this.f1821i, this.f1822j, (((int) (-f)) * 3) / 5, (((int) (-f2)) * 3) / 5, -this.f1828p, this.f1828p, -this.f1829q, this.f1829q);
            return true;
        }

        public void onLongPress(MotionEvent motionEvent) {
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            m1895a((int) motionEvent2.getX(), (int) motionEvent2.getY());
            postInvalidate();
            return true;
        }

        public void onShowPress(MotionEvent motionEvent) {
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            this.f1813a.f495l.f596d.m720c(motionEvent);
            Iterator it = this.f1818f.iterator();
            while (it.hasNext()) {
                ((OnGestureListener) it.next()).onSingleTapUp(motionEvent);
            }
            return false;
        }

        public void m1903a(OnGestureListener onGestureListener) {
            this.f1818f.add(onGestureListener);
        }

        public void m1910b(OnGestureListener onGestureListener) {
            this.f1818f.remove(onGestureListener);
        }

        public boolean m1904a(float f, float f2) {
            this.f1813a.f497n.stopAnimation(true);
            if (this.f1825m) {
                this.f1826n += f;
                this.f1827o += f2;
            }
            invalidate();
            return this.f1825m;
        }

        public boolean m1911b(float f) {
            m1901a(f);
            return false;
        }

        public boolean m1906a(Matrix matrix) {
            return false;
        }

        public boolean m1912b(Matrix matrix) {
            this.f1823k.set(matrix);
            postInvalidate();
            return true;
        }

        public boolean m1905a(float f, PointF pointF) {
            this.f1813a.f495l.f596d.f569f = false;
            m1894a(f, pointF, this.f1826n, this.f1827o);
            this.f1825m = false;
            postInvalidateDelayed(8);
            return true;
        }

        public boolean m1907a(PointF pointF) {
            this.f1813a.f495l.f596d.m709a(true);
            this.f1813a.f495l.f596d.f569f = true;
            this.f1825m = true;
            return true;
        }

        public boolean onDoubleTap(MotionEvent motionEvent) {
            if (this.f1813a.f502s) {
                this.f1813a.f497n.zoomInFixing((int) motionEvent.getX(), (int) motionEvent.getY());
            }
            return true;
        }

        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            return false;
        }

        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            if (InfoWindow.f697a != null && CoreUtil.m483a() - m1915d() > 1000) {
                InfoWindow.f697a.m856c();
            }
            return false;
        }

        public void m1902a(long j) {
            this.f1830r = j;
        }

        public long m1915d() {
            return this.f1830r;
        }

        private void m1894a(float f, PointF pointF, float f2, float f3) {
            int floor;
            boolean z;
            int c = this.f1813a.f495l.f594b.m750c() / 2;
            int d = this.f1813a.f495l.f594b.m752d() / 2;
            if (f > 0.0f) {
                floor = (int) Math.floor((double) f);
                z = true;
            } else if (f < 0.0f) {
                floor = (int) Math.floor((double) Math.abs(f));
                z = false;
            } else {
                return;
            }
            OnZoomListener onZoomListener = this.f1813a.getZoomButtonsController().getOnZoomListener();
            floor = this.f1813a.m647b(z ? floor + this.f1813a.f495l.f594b.m753e() : this.f1813a.f495l.f594b.m753e() - floor);
            if (floor != this.f1813a.f495l.f594b.m753e()) {
                this.f1813a.f486c[0] = this.f1813a.f486c[1];
                this.f1813a.f486c[1] = floor;
                if (this.f1813a.f486c[0] != this.f1813a.f486c[1]) {
                    if (onZoomListener != null) {
                        onZoomListener.onZoom(z);
                    }
                    GeoPoint fromPixels = this.f1813a.f495l.f593a.fromPixels(c, d);
                    this.f1813a.f495l.f594b.m742a(floor);
                    this.f1813a.f495l.f594b.m744a(fromPixels);
                }
            }
        }
    }

    static /* synthetic */ int m633f(MapView mapView) {
        int i = mapView.f506w + 1;
        mapView.f506w = i;
        return i;
    }

    public ZoomButtonsController getZoomButtonsController() {
        if (this.f487d == null) {
            this.f487d = new ZoomButtonsController(this);
        }
        return this.f487d;
    }

    public MapView(Context context) {
        super(context);
        this.f499p = false;
        this.f501r = new int[]{3000000, 1500000, 800000, 400000, 200000, 100000, RequestCode.REQ_CHECK_FIRST_RUN_WITH_CACHE, 25000, 12000, 6000, 3000, 1500, Constant.TIME_OUT, MyHttpException.ERROR_PARAMETER, AsyncTaskManager.REQUEST_SUCCESS_CODE, 100, 50, 25};
        this.f502s = true;
        this.VMapMode = false;
        this.VisInited = false;
        this.f486c = new int[2];
        this.f505v = false;
        this.f488e = false;
        this.f506w = 0;
        this.f507x = null;
        this.f489f = null;
        this.f490g = null;
        this.f491h = null;
        this.f492i = null;
        this.f493j = null;
        this.tileDownloadCtrl = null;
        this.mapLevel = 12;
        this.mapAngle = 0;
        this.isInited = false;
        if (VERSION.SDK_INT > 10) {
            setLayerType(1, null);
        }
        setClickable(true);
        if (context instanceof MapActivity) {
            ((MapActivity) context).m582a(this, context, null);
            return;
        }
        throw new IllegalArgumentException("MapViews can only be created inside instances of MapActivity.");
    }

    public MapView(Context context, String str) {
        super(context);
        this.f499p = false;
        this.f501r = new int[]{3000000, 1500000, 800000, 400000, 200000, 100000, RequestCode.REQ_CHECK_FIRST_RUN_WITH_CACHE, 25000, 12000, 6000, 3000, 1500, Constant.TIME_OUT, MyHttpException.ERROR_PARAMETER, AsyncTaskManager.REQUEST_SUCCESS_CODE, 100, 50, 25};
        this.f502s = true;
        this.VMapMode = false;
        this.VisInited = false;
        this.f486c = new int[2];
        this.f505v = false;
        this.f488e = false;
        this.f506w = 0;
        this.f507x = null;
        this.f489f = null;
        this.f490g = null;
        this.f491h = null;
        this.f492i = null;
        this.f493j = null;
        this.tileDownloadCtrl = null;
        this.mapLevel = 12;
        this.mapAngle = 0;
        this.isInited = false;
        if (VERSION.SDK_INT > 10) {
            setLayerType(1, null);
        }
        setClickable(true);
        if (context instanceof MapActivity) {
            ((MapActivity) context).m582a(this, context, str);
            return;
        }
        throw new IllegalArgumentException("MapViews can only be created inside instances of MapActivity.");
    }

    public MapView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MapView(Context context, AttributeSet attributeSet, int i) {
        String str = null;
        super(context, attributeSet, i);
        this.f499p = false;
        this.f501r = new int[]{3000000, 1500000, 800000, 400000, 200000, 100000, RequestCode.REQ_CHECK_FIRST_RUN_WITH_CACHE, 25000, 12000, 6000, 3000, 1500, Constant.TIME_OUT, MyHttpException.ERROR_PARAMETER, AsyncTaskManager.REQUEST_SUCCESS_CODE, 100, 50, 25};
        this.f502s = true;
        this.VMapMode = false;
        this.VisInited = false;
        this.f486c = new int[2];
        this.f505v = false;
        this.f488e = false;
        this.f506w = 0;
        this.f507x = null;
        this.f489f = null;
        this.f490g = null;
        this.f491h = null;
        this.f492i = null;
        this.f493j = null;
        this.tileDownloadCtrl = null;
        this.mapLevel = 12;
        this.mapAngle = 0;
        this.isInited = false;
        if (VERSION.SDK_INT > 10) {
            setLayerType(1, null);
        }
        this.f508y = context;
        int attributeCount = attributeSet.getAttributeCount();
        String str2 = XmlPullParser.NO_NAMESPACE;
        int i2 = 0;
        while (i2 < attributeCount) {
            String toLowerCase = attributeSet.getAttributeName(i2).toLowerCase();
            if (toLowerCase.equals("amapapikey")) {
                String str3 = str;
                str = attributeSet.getAttributeValue(i2);
                toLowerCase = str3;
            } else if (toLowerCase.equals("useragent")) {
                toLowerCase = attributeSet.getAttributeValue(i2);
                str = str2;
            } else {
                if (toLowerCase.equals("clickable")) {
                    this.f499p = attributeSet.getAttributeValue(i2).equals("true");
                }
                toLowerCase = str;
                str = str2;
            }
            i2++;
            str2 = str;
            str = toLowerCase;
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{16843281});
        if (str2.length() < 15) {
            obtainStyledAttributes.getString(0);
        }
        if (context instanceof MapActivity) {
            ((MapActivity) context).m582a(this, context, str);
            return;
        }
        throw new IllegalArgumentException("MapViews can only be created inside instances of MapActivity.");
    }

    void m644a(Context context, String str) {
        this.f508y = context;
        try {
            this.f494k = (MapActivity) context;
            m641m();
            this.f488e = this.f494k.f434a;
            if (this.f494k.getMapMode() == MapActivity.MAP_MODE_VECTOR) {
                this.VMapMode = true;
            }
            setBackgroundColor(Color.rgb(222, 215, 214));
            this.f495l = new ag(this.f494k, this, str);
            this.f498o = new C0096g(this, this.f508y);
            this.mRouteCtrl = new C0094e(this);
            this.f497n = new MapController(this.f495l);
            setEnabled(true);
        } catch (Exception e) {
            throw new IllegalArgumentException("can only be created inside instances of MapActivity");
        }
    }

    public void setMapProjectSetting(ac acVar) {
        this.f495l.f598f.m684a(acVar);
    }

    public void setScreenHotPoint(Point point) {
        this.f495l.f598f.m682a(point);
        this.f495l.f594b.m746a(false, false);
    }

    public void setMapMoveEnable(boolean z) {
        ConfigableConst.f344n = z;
    }

    public void setServerUrl(ServerUrlSetting serverUrlSetting) {
        if (serverUrlSetting != null) {
            if (!(serverUrlSetting.f390d == null || serverUrlSetting.f390d.equals(XmlPullParser.NO_NAMESPACE))) {
                MapServerUrl.m503a().m504a(serverUrlSetting.f390d);
            }
            if (!(serverUrlSetting.f391e == null || serverUrlSetting.f391e.equals(XmlPullParser.NO_NAMESPACE))) {
                MapServerUrl.m503a().m506b(serverUrlSetting.f391e);
            }
            if (!(serverUrlSetting.f389c == null || serverUrlSetting.f389c.equals(XmlPullParser.NO_NAMESPACE))) {
                MapServerUrl.m503a().m512e(serverUrlSetting.f389c);
            }
            if (!(serverUrlSetting.f387a == null || serverUrlSetting.f387a.equals(XmlPullParser.NO_NAMESPACE))) {
                MapServerUrl.m503a().m508c(serverUrlSetting.f387a);
            }
            if (!(serverUrlSetting.f388b == null || serverUrlSetting.f388b.equals(XmlPullParser.NO_NAMESPACE))) {
                MapServerUrl.m503a().m510d(serverUrlSetting.f388b);
            }
            if (serverUrlSetting.f392f != null && !serverUrlSetting.f392f.equals(XmlPullParser.NO_NAMESPACE)) {
                MapServerUrl.m503a().m514f(serverUrlSetting.f392f);
            }
        }
    }

    public boolean isDoubleClickZooming() {
        return this.f502s;
    }

    public void setDoubleClickZooming(boolean z) {
        this.f502s = z;
    }

    public void registerTrackballListener(TrackballGestureDetector trackballGestureDetector) {
        this.f504u = trackballGestureDetector;
    }

    public void setEnabled(boolean z) {
        ConfigableConst.f343m = z;
        super.setEnabled(z);
    }

    public C0096g getZoomMgr() {
        return this.f498o;
    }

    public LayerCtrlManager getLayerMgr() {
        if (isVectorMap()) {
            return null;
        }
        if (this.f500q == null) {
            this.f500q = new LayerCtrlManager();
            this.f500q.m857a(this);
        }
        return this.f500q;
    }

    ag m642a() {
        return this.f495l;
    }

    C1036c m648b() {
        return this.f496m;
    }

    void m643a(int i) {
        this.f506w = i;
    }

    public void setReticleDrawMode(ReticleDrawMode reticleDrawMode) {
        this.f495l.f596d.m707a(reticleDrawMode);
    }

    public GeoPoint getMapCenter() {
        return this.f495l.f594b.m754f();
    }

    public MapController getController() {
        return this.f497n;
    }

    public final List<Overlay> getOverlays() {
        return this.f495l.f596d.m719c();
    }

    public int getLatitudeSpan() {
        return this.f495l.f593a.m1934a();
    }

    public int getLongitudeSpan() {
        return this.f495l.f593a.m1935b();
    }

    public int getZoomLevel() {
        return this.f495l.f594b.m753e();
    }

    public int getMaxZoomLevel() {
        return this.f495l.f594b.m741a();
    }

    public int getMinZoomLevel() {
        return this.f495l.f594b.m747b();
    }

    public Projection getProjection() {
        return this.f495l.f593a;
    }

    public String getDebugVersion() {
        return ConfigableConst.f341k;
    }

    public String getReleaseVersion() {
        return ConfigableConst.f342l;
    }

    void m649c() {
        this.f498o.m623e();
    }

    public boolean canCoverCenter() {
        return this.f495l.f596d.m712a(getMapCenter());
    }

    public void setBuiltInZoomControls(boolean z) {
        this.f498o.m618b(z);
    }

    public void displayZoomControls(boolean z) {
        this.f498o.m621c(z);
    }

    public void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
    }

    private void m641m() {
        this.f496m = new C1036c(this, this.f494k);
        addView(this.f496m, 0, new android.view.ViewGroup.LayoutParams(-1, -1));
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2, 0, 0, 51);
    }

    public void preLoad() {
    }

    public void setVectorMap(boolean z) {
        if (this.VMapMode != z) {
            GeoPoint mapCenter = getMapCenter();
            int e = m642a().f594b.m753e();
            this.VMapMode = z;
            if (z) {
                m642a().f594b.m748b(ConfigableConst.f333c);
                m642a().f594b.m751c(ConfigableConst.f334d);
                m642a().f598f.m681a();
                au auVar = (au) m642a().f597e.m732a(0);
                auVar.m691f();
                auVar.a_();
                auVar.m2529a();
                m642a().f597e.m734a(null, 0);
                if (((bg) m642a().f597e.m732a(1)) == null) {
                    m642a().f597e.m734a(new bg(m642a(), getContext()), 1);
                }
                this.width = getWidth();
                this.height = getHeight();
                Vinit();
            } else {
                int i;
                if (e <= ConfigableConst.f332b) {
                    i = ConfigableConst.f332b;
                } else {
                    i = e;
                }
                if (i >= ConfigableConst.f331a) {
                    e = ConfigableConst.f331a;
                } else {
                    e = i;
                }
                m642a().f594b.m748b(ConfigableConst.f331a);
                m642a().f594b.m751c(ConfigableConst.f332b);
                m642a().f598f.m681a();
                bg bgVar = (bg) m642a().f597e.m732a(1);
                if (bgVar != null) {
                    bgVar.m691f();
                    bgVar.a_();
                    bgVar.m689a();
                    m642a().f597e.m734a(null, 1);
                    VdestoryMap();
                }
                if (((au) m642a().f597e.m732a(0)) == null) {
                    ad auVar2 = new au(m642a(), getContext());
                    auVar2.b_();
                    auVar2.m690e();
                    m642a().f597e.m734a(auVar2, 0);
                }
            }
            this.f497n.setCenter(mapCenter);
            m642a().f594b.m742a(e);
            m642a().f594b.m746a(false, false);
            getZoomMgr().m622d();
        }
    }

    public boolean isVectorMap() {
        return this.VMapMode;
    }

    public void setMapviewSizeChangedListener(C0093d c0093d) {
        this.f507x = c0093d;
    }

    public void setSatellite(boolean z) {
        if (!isVectorMap() && isSatellite() != z) {
            boolean isTraffic = isTraffic();
            setTraffic(false);
            if (!z) {
                m642a().f596d.m714a(m642a().f596d.f571h, false);
                m642a().f596d.m714a(m642a().f596d.f570g, true);
                if (isTraffic) {
                    setTraffic(true);
                }
                m642a().f594b.m746a(false, false);
            } else if (m642a().f596d.m702a(m642a().f596d.f571h) != null) {
                m642a().f596d.m714a(m642a().f596d.f571h, true);
                if (isTraffic) {
                    setTraffic(true);
                }
                m642a().f594b.m746a(false, false);
            } else {
                LayerPropertys layerPropertys = new LayerPropertys();
                layerPropertys.f1927j = new C10341(this);
                layerPropertys.f1918a = m642a().f596d.f571h;
                layerPropertys.f1922e = true;
                layerPropertys.f1921d = true;
                layerPropertys.f1923f = true;
                layerPropertys.f1924g = true;
                layerPropertys.f1919b = 17;
                layerPropertys.f1920c = 4;
                m642a().f596d.m713a(layerPropertys, getContext());
                m642a().f596d.m714a(m642a().f596d.f571h, true);
                if (isTraffic) {
                    setTraffic(true);
                }
                m642a().f594b.m746a(false, false);
            }
        }
    }

    public boolean isSatellite() {
        LayerPropertys a = m642a().f596d.m702a(m642a().f596d.f571h);
        if (a != null) {
            return a.f1923f;
        }
        return false;
    }

    public void setTraffic(boolean z) {
        if (!isVectorMap() && z != isTraffic()) {
            boolean isSatellite = isSatellite();
            String str = XmlPullParser.NO_NAMESPACE;
            if (isSatellite) {
                str = m642a().f596d.f573j;
            } else {
                str = m642a().f596d.f572i;
            }
            if (!z) {
                m642a().f596d.m714a(str, false);
                m642a().f594b.m746a(false, false);
            } else if (m642a().f596d.m702a(str) != null) {
                m642a().f596d.m714a(str, true);
                m642a().f594b.m746a(false, false);
            } else {
                LayerPropertys layerPropertys;
                if (isSatellite) {
                    layerPropertys = new LayerPropertys();
                    layerPropertys.f1925h = true;
                    layerPropertys.f1926i = 120000;
                    layerPropertys.f1918a = str;
                    layerPropertys.f1922e = false;
                    layerPropertys.f1921d = true;
                    layerPropertys.f1923f = true;
                    layerPropertys.f1924g = false;
                    layerPropertys.f1919b = 18;
                    layerPropertys.f1920c = 9;
                    m642a().f596d.m713a(layerPropertys, getContext());
                } else {
                    layerPropertys = new LayerPropertys();
                    layerPropertys.f1925h = true;
                    layerPropertys.f1926i = 120000;
                    layerPropertys.f1927j = new C10352(this);
                    layerPropertys.f1918a = str;
                    layerPropertys.f1922e = false;
                    layerPropertys.f1921d = true;
                    layerPropertys.f1923f = true;
                    layerPropertys.f1924g = false;
                    layerPropertys.f1919b = 18;
                    layerPropertys.f1920c = 9;
                    m642a().f596d.m713a(layerPropertys, getContext());
                }
                m642a().f596d.m714a(str, true);
                m642a().f594b.m746a(false, false);
            }
        }
    }

    public boolean isTraffic() {
        if (isVectorMap()) {
            return false;
        }
        String str;
        boolean isSatellite = isSatellite();
        String str2 = XmlPullParser.NO_NAMESPACE;
        if (isSatellite) {
            str = m642a().f596d.f573j;
        } else {
            str = m642a().f596d.f572i;
        }
        LayerPropertys a = m642a().f596d.m702a(str);
        if (a != null) {
            return a.f1923f;
        }
        return false;
    }

    public void setStreetView(boolean z) {
    }

    public boolean isStreetView() {
        return false;
    }

    void m650d() {
        if (InfoWindow.f697a != null) {
            InfoWindow.f697a.m856c();
        }
        VdestoryMap();
        this.f495l.m758a();
        if (this.f497n != null) {
            this.f497n.stopAnimation(true);
            this.f497n.stopPanning();
        }
        this.f497n = null;
        this.f496m = null;
        this.f495l = null;
        if (this.mRouteCtrl != null) {
            this.mRouteCtrl.m601a();
            this.mRouteCtrl = null;
        }
        if (this.f498o != null) {
            this.f498o.m620c();
            this.f498o = null;
        }
    }

    int m647b(int i) {
        if (i < this.f495l.f594b.m747b()) {
            i = this.f495l.f594b.m747b();
        }
        if (i > this.f495l.f594b.m741a()) {
            return this.f495l.f594b.m741a();
        }
        return i;
    }

    protected final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    protected void dispatchDraw(Canvas canvas) {
        if (-1 == indexOfChild(this.f496m)) {
            addView(this.f496m, 0, new android.view.ViewGroup.LayoutParams(-1, -1));
        }
        super.dispatchDraw(canvas);
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.f495l != null) {
            this.f495l.f594b.m756h();
        }
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.f494k, attributeSet);
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        super.onRestoreInstanceState(parcelable);
    }

    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    protected void onDetachedFromWindow() {
        if (this.VMapMode && this.isInited) {
            VdestoryMap();
        }
        super.onDetachedFromWindow();
    }

    public void computeScroll() {
        if (this.f496m.f1820h.computeScrollOffset()) {
            int currX = this.f496m.f1820h.getCurrX() - this.f496m.f1821i;
            int currY = this.f496m.f1820h.getCurrY() - this.f496m.f1822j;
            this.f496m.f1821i = this.f496m.f1820h.getCurrX();
            this.f496m.f1822j = this.f496m.f1820h.getCurrY();
            GeoPoint fromPixels = this.f495l.f593a.fromPixels(currX + this.f495l.f598f.f544k.x, currY + this.f495l.f598f.f544k.y);
            if (this.f496m.f1820h.isFinished()) {
                this.f495l.f594b.m746a(false, false);
                return;
            } else {
                this.f495l.f594b.m749b(fromPixels);
                return;
            }
        }
        super.computeScroll();
    }

    public void setClickable(boolean z) {
        this.f499p = z;
        super.setClickable(z);
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (this.f495l == null) {
            return true;
        }
        if (!this.f499p) {
            return false;
        }
        if (this.f495l.f596d.m710a(i, keyEvent) || this.f497n.onKey(this, i, keyEvent)) {
            return true;
        }
        return false;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (this.f495l == null) {
            return true;
        }
        if (!this.f499p) {
            return false;
        }
        if (this.f495l.f596d.m717b(i, keyEvent) || this.f497n.onKey(this, i, keyEvent)) {
            return true;
        }
        return false;
    }

    public boolean onTrackballEvent(MotionEvent motionEvent) {
        if (this.f495l == null) {
            return true;
        }
        if (!this.f499p || this.f504u == null) {
            return false;
        }
        this.f503t = aw.m805a();
        this.f503t.m807a(motionEvent);
        this.f504u.m804a(this.f503t);
        if (this.f495l.f596d.m711a(motionEvent)) {
            return true;
        }
        return this.f496m.m1908a(motionEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!ConfigableConst.f343m || this.f495l == null) {
            return true;
        }
        if (!this.f499p) {
            return false;
        }
        if (this.f495l.f596d.m718b(motionEvent)) {
            return true;
        }
        this.f496m.m1913b(motionEvent);
        return super.onTouchEvent(motionEvent);
    }

    protected final void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.f498o.m622d();
        if (this.VMapMode) {
            if (this.isInited) {
                VdestoryMap();
            }
            this.width = getWidth();
            this.height = getHeight();
            Vinit();
            this.isInited = true;
        }
        this.f495l.f598f.m682a(new Point(i / 2, i2 / 2));
        this.f498o.m611a(i, i2);
        this.mRouteCtrl.m602a(i, i2);
        this.f495l.f594b.m743a(i, i2);
        if (!(this.f497n.getReqLatSpan() == 0 || this.f497n.getReqLngSpan() == 0)) {
            this.f497n.zoomToSpan(this.f497n.getReqLatSpan(), this.f497n.getReqLngSpan());
            this.f497n.setReqLatSpan(0);
            this.f497n.setReqLngSpan(0);
        }
        if (this.f507x != null) {
            this.f507x.m596a(i, i2, i3, i4);
        }
    }

    public int getScale(int i) {
        if (i < getMinZoomLevel() || i > getMaxZoomLevel() || i < 0 || i > this.f501r.length) {
            return -1;
        }
        return this.f501r[i - 1];
    }

    public double getMetersPerPixel(int i) {
        if (i < getMinZoomLevel() || i > getMaxZoomLevel()) {
            return 0.0d;
        }
        return m642a().f598f.f541h[i];
    }

    protected void Vinit() {
        if (!this.isInited) {
            this.f490g = ByteBuffer.allocate(Opcodes.ACC_DEPRECATED);
            this.f491h = Bitmap.createBitmap(KEYRecord.OWNER_ZONE, KEYRecord.OWNER_ZONE, Config.RGB_565);
            this.f489f = new NativeMapEngine(getContext());
            this.f489f.initIconData(this.f508y);
            this.f489f.initStyleData(this.f508y);
            this.f492i = new ConnectionManager();
            this.tileDownloadCtrl = new at(this);
            this.f492i.f696e = this;
            this.f492i.start();
            this.tileDownloadCtrl.start();
            this.f493j = new ak(this);
            this.f493j.start();
            this.isInited = true;
        }
    }

    protected void VdestoryMap() {
        if (this.f492i != null) {
            this.f492i.m850b();
        }
        if (this.tileDownloadCtrl != null) {
            this.tileDownloadCtrl.m802c();
        }
        if (this.f493j != null) {
            this.f493j.m775a();
            boolean z = true;
            while (z) {
                try {
                    this.f493j.join();
                    z = false;
                } catch (InterruptedException e) {
                }
            }
        }
        if (this.f491h != null) {
            this.f491h.recycle();
            this.f491h = null;
            this.f490g = null;
        }
        this.isInited = false;
        if (this.f489f != null) {
            this.f489f.destory();
            this.f489f = null;
        }
    }

    protected boolean isGridInScreen(String str) {
        bf bfVar = new bf();
        bf bfVar2 = new bf();
        int c = m629c(this.mapLevel);
        int i = this.mapLevel - c;
        m645a(bfVar, bfVar2);
        bfVar = VMapProjection.PixelsToTile(bfVar.f665a >> i, bfVar.f666b >> i);
        bfVar2 = VMapProjection.PixelsToTile(bfVar2.f665a >> i, bfVar2.f666b >> i);
        Point QuadKeyToTile = VMapProjection.QuadKeyToTile(str);
        if (str.length() == c && QuadKeyToTile.x >= bfVar.f665a && QuadKeyToTile.x <= bfVar2.f665a && QuadKeyToTile.y >= bfVar.f666b && QuadKeyToTile.y <= bfVar2.f666b) {
            return true;
        }
        return false;
    }

    protected boolean isAGridsInScreen(ArrayList<String> arrayList) {
        bf bfVar = new bf();
        bf bfVar2 = new bf();
        int c = m629c(this.mapLevel);
        int i = this.mapLevel - c;
        m645a(bfVar, bfVar2);
        bf PixelsToTile = VMapProjection.PixelsToTile(bfVar.f665a >> i, bfVar.f666b >> i);
        bf PixelsToTile2 = VMapProjection.PixelsToTile(bfVar2.f665a >> i, bfVar2.f666b >> i);
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            if (((String) arrayList.get(i2)).length() == c) {
                Point QuadKeyToTile = VMapProjection.QuadKeyToTile((String) arrayList.get(i2));
                if (QuadKeyToTile.x >= PixelsToTile.f665a && QuadKeyToTile.x <= PixelsToTile2.f665a && QuadKeyToTile.y >= PixelsToTile.f666b && QuadKeyToTile.y <= PixelsToTile2.f666b) {
                    return true;
                }
            }
        }
        return false;
    }

    protected synchronized void loadBMtilesData2(ArrayList arrayList, boolean z) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            Object obj;
            String str = (String) arrayList.get(i);
            if (this.f489f == null || !this.f489f.hasGridData(str)) {
                obj = null;
            } else {
                obj = 1;
            }
            if (obj == null && !this.tileDownloadCtrl.m801b(str)) {
                arrayList2.add(str);
            }
        }
        if (arrayList2.size() <= 0) {
            this.tileDownloadCtrl.f639a = 0;
        } else if (z) {
            this.tileDownloadCtrl.f639a = 0;
            this.tileDownloadCtrl.m800b();
            sendMapDataRequest(arrayList2);
        } else {
            at atVar = this.tileDownloadCtrl;
            atVar.f639a += arrayList2.size();
            this.tileDownloadCtrl.m800b();
        }
    }

    protected void sendMapDataRequest(ArrayList arrayList) {
        if (arrayList.size() != 0) {
            aa aaVar = new aa(this);
            aaVar.f524e = this.mapLevel;
            this.f492i.m848a();
            for (int i = 0; i < arrayList.size(); i++) {
                String str = (String) arrayList.get(i);
                this.tileDownloadCtrl.m803c(str);
                aaVar.m672b(str);
            }
            this.f492i.m849a(aaVar);
        }
    }

    public void paintVectorMap(Canvas canvas) {
        int i;
        Point point;
        if (!this.f505v) {
            setBackgroundColor(DefaultRenderer.BACKGROUND_COLOR | this.f489f.getBKColor(this.mapLevel));
            this.f505v = true;
        }
        loadBMtilesData2(m651e(), false);
        ArrayList f = m652f();
        canvas.save();
        Matrix matrix = canvas.getMatrix();
        matrix.preRotate((float) (-this.mapAngle), (float) (this.width / 2), (float) (this.height / 2));
        canvas.setMatrix(matrix);
        ArrayList arrayList = new ArrayList();
        Hashtable hashtable = new Hashtable();
        for (i = 0; i < f.size(); i++) {
            String str = (String) f.get(i);
            if (this.f489f.hasBitMapData(str)) {
                arrayList.add(str);
            } else {
                if (!this.f493j.m777a(str)) {
                    al alVar = new al();
                    alVar.f622a = str;
                    this.f493j.m776a(alVar);
                }
                String str2 = XmlPullParser.NO_NAMESPACE;
                if (str.length() != 0) {
                    str = str.substring(0, str.length() - 1);
                } else {
                    str = str2;
                }
                if (this.f489f.hasBitMapData(str)) {
                    hashtable.put(str, str);
                }
            }
        }
        if (hashtable.size() > 0) {
            Enumeration elements = hashtable.elements();
            while (elements.hasMoreElements()) {
                str = (String) elements.nextElement();
                if (this.f489f.hasBitMapData(str)) {
                    bf QuadKeyToTitle = VMapProjection.QuadKeyToTitle(str);
                    int length = (QuadKeyToTitle.f665a * KEYRecord.OWNER_ZONE) << (20 - str.length());
                    int length2 = (QuadKeyToTitle.f666b * KEYRecord.OWNER_ZONE) << (20 - str.length());
                    point = new Point();
                    getScreenPntBy20Pixel(length, length2, this.mapLevel - 1, point);
                    canvas.save();
                    Matrix matrix2 = canvas.getMatrix();
                    matrix2.preScale(2.0f, 2.0f, (float) (this.width / 2), (float) (this.height / 2));
                    canvas.setMatrix(matrix2);
                    try {
                        this.f489f.fillBitmapBufferData(str, this.f490g.array());
                        this.f491h.copyPixelsFromBuffer(this.f490g);
                        canvas.drawBitmap(this.f491h, (float) point.x, (float) point.y, null);
                    } catch (Exception e) {
                    }
                    canvas.restore();
                }
            }
        }
        for (i = 0; i < arrayList.size(); i++) {
            str = (String) arrayList.get(i);
            if (this.f489f.hasBitMapData(str)) {
                QuadKeyToTitle = VMapProjection.QuadKeyToTitle(str);
                length = (QuadKeyToTitle.f665a * KEYRecord.OWNER_ZONE) << (20 - str.length());
                length2 = (QuadKeyToTitle.f666b * KEYRecord.OWNER_ZONE) << (20 - str.length());
                point = new Point();
                getScreenPntBy20Pixel(length, length2, this.mapLevel, point);
                try {
                    this.f489f.fillBitmapBufferData(str, this.f490g.array());
                    this.f491h.copyPixelsFromBuffer(this.f490g);
                    canvas.drawBitmap(this.f491h, (float) point.x, (float) point.y, null);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        canvas.restore();
        NativeMap nativeMap = new NativeMap();
        nativeMap.initMap(null, this.width, this.height);
        nativeMap.setMapParameter(this.centerX, this.centerY, this.mapLevel, this.mapAngle);
        nativeMap.paintMap(this.f489f, 0);
        nativeMap.paintLables(this.f489f, canvas, 2);
    }

    protected int getGridLevelOff(int i) {
        switch (i) {
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                return 4;
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                return 2;
            case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                return 2;
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                return 7;
            default:
                return 0;
        }
    }

    private int m629c(int i) {
        switch (i) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                return 2;
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                return 6;
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                return 6;
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                return 6;
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                return 6;
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                return 10;
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                return 10;
            case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                return 12;
            case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                return 12;
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                return 14;
            case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                return 14;
            case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                return 14;
            case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
                return 14;
            case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                return 14;
            case WelcomeActivity.GPIO_IOCQDATAOUT /*19*/:
                return 14;
            case FileOptions.JAVA_GENERATE_EQUALS_AND_HASH_FIELD_NUMBER /*20*/:
                return 14;
            default:
                return 0;
        }
    }

    protected void getScreenPnt(int i, int i2, Point point) {
        getScreenPntBy20Pixel(i << (20 - this.mapLevel), i2 << (20 - this.mapLevel), point);
    }

    protected void getScreenPntBy20Pixel(int i, int i2, int i3, Point point) {
        int i4 = 20 - i3;
        point.x = i >> i4;
        point.y = i2 >> i4;
        point.x = ((this.width >> 1) - (this.centerX >> i4)) + point.x;
        point.y = ((this.height >> 1) - (this.centerY >> i4)) + point.y;
    }

    protected void getScreenPntBy20Pixel(int i, int i2, Point point) {
        int i3 = 20 - this.mapLevel;
        point.x = i >> i3;
        point.y = i2 >> i3;
        bf bfVar = new bf();
        point.x = ((this.width >> 1) - (this.centerX >> i3)) + point.x;
        point.y = ((this.height >> 1) - (this.centerY >> i3)) + point.y;
    }

    protected void getPixelPnt(Point point, bf bfVar) {
        getPixel20Pnt(point, bfVar, this.centerX, this.centerY);
        bfVar.f665a >>= 20 - this.mapLevel;
        bfVar.f666b >>= 20 - this.mapLevel;
    }

    protected void getPixel20Pnt(Point point, bf bfVar, int i, int i2) {
        int i3 = 20 - this.mapLevel;
        bfVar.f665a = ((point.x - (this.width >> 1)) << i3) + i;
        bfVar.f666b = ((point.y - (this.height >> 1)) << i3) + i2;
    }

    void m645a(bf bfVar, bf bfVar2) {
        bf bfVar3 = new bf();
        getPixelPnt(new Point(0, 0), bfVar3);
        int min = Math.min(Integer.MAX_VALUE, bfVar3.f665a);
        int min2 = Math.min(Integer.MAX_VALUE, bfVar3.f666b);
        int max = Math.max(Integer.MIN_VALUE, bfVar3.f665a);
        int max2 = Math.max(Integer.MIN_VALUE, bfVar3.f666b);
        getPixelPnt(new Point(this.width, 0), bfVar3);
        min = Math.min(min, bfVar3.f665a);
        min2 = Math.min(min2, bfVar3.f666b);
        max = Math.max(max, bfVar3.f665a);
        max2 = Math.max(max2, bfVar3.f666b);
        getPixelPnt(new Point(this.width, this.height), bfVar3);
        min = Math.min(min, bfVar3.f665a);
        min2 = Math.min(min2, bfVar3.f666b);
        max = Math.max(max, bfVar3.f665a);
        max2 = Math.max(max2, bfVar3.f666b);
        getPixelPnt(new Point(0, this.height), bfVar3);
        min = Math.min(min, bfVar3.f665a);
        min2 = Math.min(min2, bfVar3.f666b);
        max = Math.max(max, bfVar3.f665a);
        int max3 = Math.max(max2, bfVar3.f666b);
        bfVar.f665a = min;
        bfVar.f666b = min2;
        bfVar2.f665a = max;
        bfVar2.f666b = max3;
    }

    ArrayList<String> m651e() {
        bf bfVar = new bf();
        bf bfVar2 = new bf();
        ArrayList<String> arrayList = new ArrayList();
        int c = m629c(this.mapLevel);
        int i = this.mapLevel - c;
        m645a(bfVar, bfVar2);
        bf PixelsToTile = VMapProjection.PixelsToTile(bfVar.f665a >> i, bfVar.f666b >> i);
        bfVar = VMapProjection.PixelsToTile(bfVar2.f665a >> i, bfVar2.f666b >> i);
        i = bfVar.f665a - PixelsToTile.f665a;
        int i2 = bfVar.f666b - PixelsToTile.f666b;
        arrayList.clear();
        for (int i3 = 0; i3 <= i2; i3++) {
            for (int i4 = 0; i4 <= i; i4++) {
                arrayList.add(VMapProjection.TileToQuadKey(PixelsToTile.f665a + i4, PixelsToTile.f666b + i3, c));
            }
        }
        return arrayList;
    }

    boolean m646a(String str) {
        bf bfVar = new bf();
        bf bfVar2 = new bf();
        if (this.mapLevel != str.length()) {
            return false;
        }
        m645a(bfVar, bfVar2);
        bfVar = VMapProjection.PixelsToTile(bfVar.f665a, bfVar.f666b);
        bfVar2 = VMapProjection.PixelsToTile(bfVar2.f665a, bfVar2.f666b);
        Point QuadKeyToTile = VMapProjection.QuadKeyToTile(str);
        if (QuadKeyToTile.x < bfVar.f665a || QuadKeyToTile.x > bfVar2.f665a || QuadKeyToTile.y < bfVar.f666b || QuadKeyToTile.y > bfVar2.f666b) {
            return false;
        }
        return true;
    }

    ArrayList<String> m652f() {
        int i;
        ArrayList arrayList = new ArrayList();
        ArrayList<String> arrayList2 = new ArrayList();
        bf bfVar = new bf();
        bf bfVar2 = new bf();
        bf bfVar3 = new bf();
        m645a(bfVar, bfVar2);
        getPixelPnt(new Point(this.width / 2, this.height / 2), bfVar3);
        bf PixelsToTile = VMapProjection.PixelsToTile(bfVar.f665a, bfVar.f666b);
        bfVar = VMapProjection.PixelsToTile(bfVar2.f665a, bfVar2.f666b);
        bfVar2 = VMapProjection.PixelsToTile(bfVar3.f665a, bfVar3.f666b);
        int i2 = bfVar.f665a - PixelsToTile.f665a;
        int i3 = bfVar.f666b - PixelsToTile.f666b;
        Comparator c0091a = new C0091a(this, bfVar2.f665a, bfVar2.f666b);
        for (int i4 = 0; i4 <= i3; i4++) {
            for (i = 0; i <= i2; i++) {
                arrayList.add(new bf(PixelsToTile.f665a + i, PixelsToTile.f666b + i4));
            }
        }
        Collections.sort(arrayList, c0091a);
        for (i = 0; i < arrayList.size(); i++) {
            bf bfVar4 = (bf) arrayList.get(i);
            arrayList2.add(VMapProjection.TileToQuadKey(bfVar4.f665a, bfVar4.f666b, this.mapLevel));
        }
        return arrayList2;
    }

    protected void setMapCenterScreen(int i, int i2) {
        bf bfVar = new bf();
        getPixel20Pnt(new Point(i, i2), bfVar, this.centerX, this.centerY);
        setMapCenter(bfVar.f665a, bfVar.f666b);
    }

    protected void setMapCenter(int i, int i2) {
        if (i >= 0 && i <= 268435455 && i2 >= 20 && i2 <= 268435431) {
            this.centerX = i;
            this.centerY = i2;
        }
    }

    protected HttpURLConnection getConnection(String str) {
        Proxy proxy;
        URL url;
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.f508y.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.getType() == 1) {
                android.net.Proxy.getHost(this.f508y);
                android.net.Proxy.getPort(this.f508y);
                proxy = null;
            } else {
                String defaultHost = android.net.Proxy.getDefaultHost();
                int defaultPort = android.net.Proxy.getDefaultPort();
                if (defaultHost != null) {
                    proxy = new Proxy(Type.HTTP, new InetSocketAddress(defaultHost, defaultPort));
                }
            }
            url = new URL(MapServerUrl.m503a().m515g() + "/bmserver/VMMV2?" + str);
            if (proxy == null) {
                return (HttpURLConnection) url.openConnection(proxy);
            }
            return (HttpURLConnection) url.openConnection();
        }
        proxy = null;
        try {
            url = new URL(MapServerUrl.m503a().m515g() + "/bmserver/VMMV2?" + str);
            if (proxy == null) {
                return (HttpURLConnection) url.openConnection();
            }
            return (HttpURLConnection) url.openConnection(proxy);
        } catch (IOException e) {
            return null;
        }
    }

    protected void onWindowVisibilityChanged(int i) {
        if (this.VMapMode) {
            if (i == 8) {
                if (this.isInited) {
                    VdestoryMap();
                }
            } else if (i == 0) {
                this.bfirstDrawed = false;
                int width = getWidth();
                int height = getHeight();
                if (width > 0 && height > 0) {
                    this.width = getWidth();
                    this.height = getHeight();
                    Vinit();
                    this.isInited = true;
                }
            }
            super.onWindowVisibilityChanged(i);
            return;
        }
        super.onWindowVisibilityChanged(i);
    }
}

package com.amap.mapapi.map;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.amap.mapapi.core.ConfigableConst;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.GeoPoint.C0085a;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.Overlay.Snappable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public abstract class ItemizedOverlay<Item extends OverlayItem> extends Overlay implements Snappable {
    private static int f1799d;
    private boolean f1800a;
    private Drawable f1801b;
    private Drawable f1802c;
    private C0089b f1803e;
    private OnFocusChangeListener f1804f;
    private int f1805g;
    private int f1806h;

    public interface OnFocusChangeListener {
        void onFocusChanged(ItemizedOverlay<?> itemizedOverlay, OverlayItem overlayItem);
    }

    /* renamed from: com.amap.mapapi.map.ItemizedOverlay.a */
    enum C0088a {
        Normal,
        Center,
        CenterBottom
    }

    /* renamed from: com.amap.mapapi.map.ItemizedOverlay.b */
    class C0089b implements Comparator<Integer> {
        final /* synthetic */ ItemizedOverlay f431a;
        private ArrayList<Item> f432b;
        private ArrayList<Integer> f433c;

        public /* synthetic */ int compare(Object obj, Object obj2) {
            return m577a((Integer) obj, (Integer) obj2);
        }

        public C0089b(ItemizedOverlay itemizedOverlay) {
            this.f431a = itemizedOverlay;
            int size = itemizedOverlay.size();
            this.f432b = new ArrayList(size);
            this.f433c = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                this.f433c.add(Integer.valueOf(i));
                this.f432b.add(itemizedOverlay.createItem(i));
            }
            Collections.sort(this.f433c, this);
        }

        public int m575a() {
            return this.f432b.size();
        }

        public int m577a(Integer num, Integer num2) {
            GeoPoint point = ((OverlayItem) this.f432b.get(num.intValue())).getPoint();
            GeoPoint point2 = ((OverlayItem) this.f432b.get(num2.intValue())).getPoint();
            if (point.getLatitudeE6() > point2.getLatitudeE6()) {
                return -1;
            }
            if (point.getLatitudeE6() < point2.getLatitudeE6()) {
                return 1;
            }
            if (point.getLongitudeE6() < point2.getLongitudeE6()) {
                return -1;
            }
            if (point.getLongitudeE6() > point2.getLongitudeE6()) {
                return 1;
            }
            return 0;
        }

        public int m576a(Item item) {
            if (item != null) {
                for (int i = 0; i < m575a(); i++) {
                    if (item.equals(this.f432b.get(i))) {
                        return i;
                    }
                }
            }
            return -1;
        }

        public Item m579a(int i) {
            return (OverlayItem) this.f432b.get(i);
        }

        public int m581b(int i) {
            return ((Integer) this.f433c.get(i)).intValue();
        }

        public int m578a(boolean z) {
            if (this.f432b.size() == 0) {
                return 0;
            }
            Iterator it = this.f432b.iterator();
            int i = Integer.MIN_VALUE;
            int i2 = Integer.MAX_VALUE;
            while (it.hasNext()) {
                GeoPoint point = ((OverlayItem) it.next()).getPoint();
                int latitudeE6 = z ? point.getLatitudeE6() : point.getLongitudeE6();
                if (latitudeE6 > i) {
                    i = latitudeE6;
                }
                if (latitudeE6 >= i2) {
                    latitudeE6 = i2;
                }
                i2 = latitudeE6;
            }
            return i - i2;
        }

        public boolean m580a(GeoPoint geoPoint, MapView mapView) {
            boolean onTap;
            Projection projection = mapView.getProjection();
            Point toPixels = projection.toPixels(geoPoint, null);
            int i = -1;
            int i2 = 0;
            double d = Double.MAX_VALUE;
            int i3 = Integer.MAX_VALUE;
            while (i2 < m575a()) {
                double a = m572a((OverlayItem) this.f432b.get(i2), projection, toPixels, i2);
                if (a >= 0.0d && a < d) {
                    i3 = m581b(i2);
                    d = a;
                    i = i2;
                } else if (a == d && m581b(i2) > r3) {
                    i = i2;
                }
                i2++;
            }
            if (-1 != i) {
                onTap = this.f431a.onTap(i);
            } else {
                this.f431a.setFocus(null);
                onTap = false;
            }
            mapView.m642a().f596d.m721d();
            return onTap;
        }

        private double m572a(Item item, Projection projection, Point point, int i) {
            if (!m574b(item, projection, point, i)) {
                return -1.0d;
            }
            C0085a a = m573a(item, projection, point);
            return (double) ((a.f301b * a.f301b) + (a.f300a * a.f300a));
        }

        private C0085a m573a(Item item, Projection projection, Point point) {
            Point toPixels = projection.toPixels(item.getPoint(), null);
            return new C0085a(point.x - toPixels.x, point.y - toPixels.y);
        }

        private boolean m574b(Item item, Projection projection, Point point, int i) {
            C0085a a = m573a(item, projection, point);
            Drawable drawable = item.getmMarker();
            if (drawable == null) {
                drawable = this.f431a.f1801b;
            }
            return this.f431a.hitTest(item, drawable, a.f300a, a.f301b);
        }
    }

    protected abstract Item createItem(int i);

    public abstract int size();

    static {
        f1799d = -1;
    }

    public ItemizedOverlay(Drawable drawable) {
        this.f1800a = true;
        this.f1803e = null;
        this.f1804f = null;
        this.f1805g = -1;
        this.f1806h = -1;
        this.f1801b = drawable;
        if (this.f1801b == null) {
            this.f1801b = new BitmapDrawable(ConfigableConst.f337g.m533a(ConfigableConst.ConfigableConst.emarker.ordinal()));
        }
        this.f1801b.setBounds(0, 0, this.f1801b.getIntrinsicWidth(), this.f1801b.getIntrinsicHeight());
        this.f1802c = new ao().m1945a(this.f1801b);
        if (1 == f1799d) {
            boundCenterBottom(this.f1801b);
        } else if (2 == f1799d) {
            boundCenter(this.f1801b);
        } else {
            boundCenterBottom(this.f1801b);
        }
    }

    public static Drawable boundCenterBottom(Drawable drawable) {
        f1799d = 1;
        return m1879a(drawable, C0088a.CenterBottom);
    }

    public static Drawable boundCenter(Drawable drawable) {
        f1799d = 2;
        return m1879a(drawable, C0088a.Center);
    }

    private static Drawable m1879a(Drawable drawable, C0088a c0088a) {
        int i = 0;
        if (drawable == null || C0088a.Normal == c0088a) {
            return null;
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        Rect bounds = drawable.getBounds();
        int width = bounds.width() / 2;
        int i2 = -bounds.height();
        if (c0088a == C0088a.Center) {
            i2 /= 2;
            i = -i2;
        }
        drawable.setBounds(-width, i2, width, i);
        return drawable;
    }

    protected final void populate() {
        this.f1803e = new C0089b(this);
        this.f1805g = -1;
        this.f1806h = -1;
    }

    public GeoPoint getCenter() {
        return getItem(getIndexToDraw(0)).getPoint();
    }

    protected int getIndexToDraw(int i) {
        return this.f1803e.m581b(i);
    }

    public final Item getItem(int i) {
        return this.f1803e.m579a(i);
    }

    public int getLatSpanE6() {
        return this.f1803e.m578a(true);
    }

    public int getLonSpanE6() {
        return this.f1803e.m578a(false);
    }

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.f1804f = onFocusChangeListener;
    }

    public void setDrawFocusedItem(boolean z) {
        this.f1800a = z;
    }

    public final int getLastFocusedIndex() {
        return this.f1805g;
    }

    protected void setLastFocusedIndex(int i) {
        this.f1805g = i;
    }

    public Item getFocus() {
        if (this.f1806h != -1) {
            return this.f1803e.m579a(this.f1806h);
        }
        return null;
    }

    public void setFocus(Item item) {
        if (item != null && this.f1806h == this.f1803e.m576a((OverlayItem) item)) {
            return;
        }
        if (item != null || this.f1806h == -1) {
            this.f1806h = this.f1803e.m576a((OverlayItem) item);
            if (this.f1806h != -1) {
                setLastFocusedIndex(this.f1806h);
                if (this.f1804f != null) {
                    this.f1804f.onFocusChanged(this, item);
                    return;
                }
                return;
            }
            return;
        }
        if (this.f1804f != null) {
            this.f1804f.onFocusChanged(this, item);
        }
        this.f1806h = -1;
    }

    public Item nextFocus(boolean z) {
        if (this.f1803e.m575a() == 0) {
            return null;
        }
        if (this.f1805g != -1) {
            int i = this.f1806h == -1 ? this.f1805g : this.f1806h;
            return z ? m1883b(i) : m1881a(i);
        } else if (z) {
            return this.f1803e.m579a(0);
        } else {
            return null;
        }
    }

    private Item m1881a(int i) {
        if (i == 0) {
            return null;
        }
        return this.f1803e.m579a(i - 1);
    }

    private Item m1883b(int i) {
        if (i == this.f1803e.m575a() - 1) {
            return null;
        }
        return this.f1803e.m579a(i + 1);
    }

    public boolean onTap(GeoPoint geoPoint, MapView mapView) {
        return this.f1803e.m580a(geoPoint, mapView);
    }

    protected boolean hitTest(Item item, Drawable drawable, int i, int i2) {
        return drawable.getBounds().contains(i, i2);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent, MapView mapView) {
        return false;
    }

    public boolean onTrackballEvent(MotionEvent motionEvent, MapView mapView) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        return false;
    }

    protected boolean onTap(int i) {
        if (i != this.f1806h) {
            setFocus(getItem(i));
        }
        return false;
    }

    public boolean onSnapToItem(int i, int i2, Point point, MapView mapView) {
        for (int i3 = 0; i3 < this.f1803e.m575a(); i3++) {
            Point toPixels = mapView.getProjection().toPixels(this.f1803e.m579a(i3).getPoint(), null);
            point.x = toPixels.x;
            point.y = toPixels.y;
            double d = (double) (i - toPixels.x);
            double d2 = (double) (i2 - toPixels.y);
            boolean z = (d * d) + (d2 * d2) < 64.0d;
            if (z) {
                return z;
            }
        }
        return false;
    }

    public void draw(Canvas canvas, MapView mapView, boolean z) {
        for (int i = 0; i < this.f1803e.m575a(); i++) {
            int indexToDraw = getIndexToDraw(i);
            if (indexToDraw != this.f1806h) {
                m1882a(canvas, mapView, z, getItem(indexToDraw), 0);
            }
        }
        OverlayItem focus = getFocus();
        if (this.f1800a && focus != null) {
            m1882a(canvas, mapView, true, focus, 4);
            m1882a(canvas, mapView, false, focus, 4);
        }
    }

    private void m1882a(Canvas canvas, MapView mapView, boolean z, Item item, int i) {
        Drawable marker = item.getMarker(i);
        boolean z2 = marker == null;
        if (marker != null) {
            z2 = marker.equals(this.f1801b);
        }
        if (z2) {
            if (z) {
                marker = this.f1802c;
                this.f1802c.setBounds(this.f1801b.copyBounds());
                ao.m1944a(this.f1802c, this.f1801b);
            } else {
                marker = this.f1801b;
            }
        }
        Point toPixels = mapView.getProjection().toPixels(item.getPoint(), null);
        if (z2) {
            Overlay.m653a(canvas, marker, toPixels.x, toPixels.y);
        } else {
            Overlay.drawAt(canvas, marker, toPixels.x, toPixels.y, z);
        }
    }

    protected Drawable getDefaultMarker() {
        Drawable drawable = null;
        if (null == null) {
            drawable = new BitmapDrawable(ConfigableConst.f337g.m533a(ConfigableConst.ConfigableConst.emarker.ordinal()));
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return drawable;
    }
}

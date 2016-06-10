package com.amap.mapapi.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.amap.mapapi.core.CoreUtil;
import com.amap.mapapi.core.PoiItem;
import com.amap.mapapi.map.MapView.LayoutParams;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class PoiOverlay extends ItemizedOverlay<PoiItem> {
    private InfoWindow f2355a;
    private ArrayList<PoiItem> f2356b;
    private boolean f2357c;
    private MapView f2358d;
    private boolean f2359e;

    public void closePopupWindow() {
        if (this.f2355a != null) {
            this.f2355a.m856c();
        }
        this.f2355a = null;
    }

    public PoiOverlay(Drawable drawable, List<PoiItem> list) {
        this(drawable, list, XmlPullParser.NO_NAMESPACE);
    }

    public PoiOverlay(Drawable drawable, List<PoiItem> list, String str) {
        super(drawable);
        this.f2355a = null;
        this.f2356b = new ArrayList();
        this.f2357c = true;
        this.f2359e = false;
        String trim = str.trim();
        for (PoiItem poiItem : list) {
            if (trim.equals(XmlPullParser.NO_NAMESPACE)) {
                this.f2356b.add(poiItem);
            } else if (poiItem.getTypeCode() != null) {
                if (trim.length() <= 2 && poiItem.getTypeCode().startsWith(trim)) {
                    this.f2356b.add(poiItem);
                } else if (trim.equals(poiItem.getTypeCode())) {
                    this.f2356b.add(poiItem);
                }
            }
        }
        populate();
    }

    protected PoiItem createItem(int i) {
        return (PoiItem) this.f2356b.get(i);
    }

    public int size() {
        return this.f2356b.size();
    }

    public void addToMap(MapView mapView) {
        this.f2358d = mapView;
        mapView.getOverlays().add(this);
        this.f2359e = true;
    }

    public boolean removeFromMap() {
        if (this.f2358d == null) {
            throw new UnsupportedOperationException("poioverlay must be added to map frist!");
        } else if (!this.f2359e) {
            return false;
        } else {
            boolean remove = this.f2358d.getOverlays().remove(this);
            if (remove) {
                this.f2355a.m854a();
                closePopupWindow();
                this.f2359e = false;
            }
            return remove;
        }
    }

    protected Drawable getPopupMarker(PoiItem poiItem) {
        Drawable marker = poiItem.getMarker(0);
        if (marker == null) {
            marker = getDefaultMarker();
        }
        return m2518a(marker, 24, 18);
    }

    public void enablePopup(boolean z) {
        this.f2357c = z;
        if (!this.f2357c) {
            closePopupWindow();
        }
    }

    public boolean showPopupWindow(int i) {
        if (!this.f2357c) {
            return false;
        }
        if (this.f2358d == null) {
            throw new UnsupportedOperationException("poioverlay must be added to map first!");
        }
        PoiItem poiItem = (PoiItem) this.f2356b.get(i);
        this.f2355a = new InfoWindow(this.f2358d, getPopupView((PoiItem) this.f2356b.get(i)), poiItem.getPoint(), getPopupBackground(), getLayoutParam(i));
        this.f2358d.m642a().f594b.m744a(poiItem.getPoint());
        this.f2355a.m855b();
        return true;
    }

    protected boolean onTap(int i) {
        super.onTap(i);
        return showPopupWindow(i);
    }

    protected Drawable getPopupBackground() {
        return null;
    }

    protected LayoutParams getLayoutParam(int i) {
        return null;
    }

    protected LayoutParams getLayoutParam() {
        return null;
    }

    protected View getPopupView(PoiItem poiItem) {
        Context context = this.f2358d.getContext();
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(5, 10, 5, 20);
        View linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(0);
        linearLayout2.setGravity(51);
        View imageView = new ImageView(context);
        imageView.setBackgroundColor(-1);
        imageView.setImageDrawable(getPopupMarker(poiItem));
        View textView = new TextView(context);
        textView.setBackgroundColor(-1);
        textView.setText(CoreUtil.m493c(CoreUtil.m486a(poiItem.getTitle(), "#000000")));
        linearLayout2.addView(imageView, new LinearLayout.LayoutParams(-2, -2));
        linearLayout2.addView(textView, new LinearLayout.LayoutParams(-2, -2));
        linearLayout.addView(linearLayout2);
        if (m2521b(poiItem) != null) {
            linearLayout2 = new TextView(context);
            linearLayout2.setBackgroundColor(-1);
            linearLayout2.setText(m2521b(poiItem));
            linearLayout.addView(linearLayout2, new LinearLayout.LayoutParams(-1, -2));
        }
        linearLayout2 = new TextView(context);
        linearLayout2.setBackgroundColor(-1);
        linearLayout2.setText(m2519a(poiItem));
        linearLayout.addView(linearLayout2, new LinearLayout.LayoutParams(-1, -2));
        m2520a(linearLayout, poiItem, context);
        linearLayout2 = new TextView(context);
        linearLayout2.setText(XmlPullParser.NO_NAMESPACE);
        linearLayout2.setHeight(5);
        linearLayout2.setWidth(1);
        linearLayout.addView(linearLayout2);
        return linearLayout;
    }

    private void m2520a(LinearLayout linearLayout, PoiItem poiItem, Context context) {
        String tel = poiItem.getTel();
        if (!CoreUtil.m488a(tel)) {
            View textView = new TextView(context);
            tel = CoreUtil.m486a("Tel:  " + tel, "#000000");
            textView.setBackgroundColor(-1);
            textView.setText(CoreUtil.m493c(tel));
            textView.setLinksClickable(true);
            Linkify.addLinks(textView, 4);
            linearLayout.addView(textView, new LinearLayout.LayoutParams(-1, -2));
        }
    }

    private Spanned m2519a(PoiItem poiItem) {
        String str = "\u7c7b\u522b: ";
        String str2 = XmlPullParser.NO_NAMESPACE;
        try {
            str2 = CoreUtil.m486a(str + poiItem.getTypeDes().split(PoiItem.DesSplit)[1], "#000000");
        } catch (Exception e) {
        }
        return CoreUtil.m493c(str2);
    }

    private Spanned m2521b(PoiItem poiItem) {
        String snippet = poiItem.getSnippet();
        if (CoreUtil.m488a(snippet)) {
            return null;
        }
        return CoreUtil.m493c(CoreUtil.m486a("\u5730\u5740: " + snippet, "#000000"));
    }

    static Bitmap m2517a(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, drawable.getOpacity() != -1 ? Config.ARGB_4444 : Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return createBitmap;
    }

    static Drawable m2518a(Drawable drawable, int i, int i2) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap a = m2517a(drawable);
        Matrix matrix = new Matrix();
        matrix.postScale(((float) i) / ((float) intrinsicWidth), ((float) i2) / ((float) intrinsicHeight));
        Bitmap createBitmap = Bitmap.createBitmap(a, 0, 0, intrinsicWidth, intrinsicHeight, matrix, true);
        if (!(a == null || a.isRecycled())) {
            a.recycle();
        }
        return new BitmapDrawable(createBitmap);
    }
}

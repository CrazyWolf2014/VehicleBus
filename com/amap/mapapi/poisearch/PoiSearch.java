package com.amap.mapapi.poisearch;

import android.app.Activity;
import android.content.Context;
import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.ClientInfoUtil;
import com.amap.mapapi.core.ConfigableConst;
import com.amap.mapapi.core.CoreUtil;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapView;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;

public class PoiSearch {
    private SearchBound f717a;
    private Query f718b;
    private Context f719c;
    private int f720d;

    public static class Query {
        private String f709a;
        private String f710b;
        private String f711c;

        public Query(String str, String str2) {
            this(str, str2, null);
        }

        public Query(String str, String str2, String str3) {
            this.f709a = str;
            this.f710b = str2;
            this.f711c = str3;
            if (!m862b()) {
                throw new IllegalArgumentException("Empty  query and catagory");
            }
        }

        private boolean m862b() {
            return (CoreUtil.m488a(this.f709a) && CoreUtil.m488a(this.f710b)) ? false : true;
        }

        public String getQueryString() {
            return this.f709a;
        }

        public String getCategory() {
            if (this.f710b == null || this.f710b.equals("00") || this.f710b.equals("00|")) {
                return m863a();
            }
            return this.f710b;
        }

        String m863a() {
            return XmlPullParser.NO_NAMESPACE;
        }

        public String getCity() {
            return this.f711c;
        }
    }

    public static class SearchBound {
        public static final String BOUND_SHAPE = "bound";
        public static final String ELLIPSE_SHAPE = "Ellipse";
        public static final String POLYGON_SHAPE = "Polygon";
        public static final String RECTANGLE_SHAPE = "Rectangle";
        private GeoPoint f712a;
        private GeoPoint f713b;
        private int f714c;
        private GeoPoint f715d;
        private String f716e;

        public SearchBound(GeoPoint geoPoint, int i) {
            this.f716e = BOUND_SHAPE;
            this.f714c = i;
            this.f715d = geoPoint;
            m864a(geoPoint, CoreUtil.m489b(i), CoreUtil.m489b(i));
        }

        public SearchBound(GeoPoint geoPoint, GeoPoint geoPoint2) {
            this.f716e = RECTANGLE_SHAPE;
            m865a(geoPoint, geoPoint2);
        }

        public SearchBound(MapView mapView) {
            this.f716e = RECTANGLE_SHAPE;
            m865a(mapView.getProjection().fromPixels(0, ConfigableConst.f340j), mapView.getProjection().fromPixels(ConfigableConst.f339i, 0));
        }

        private void m865a(GeoPoint geoPoint, GeoPoint geoPoint2) {
            this.f712a = geoPoint;
            this.f713b = geoPoint2;
            if (this.f712a.m464b() >= this.f713b.m464b() || this.f712a.m462a() >= this.f713b.m462a()) {
                throw new IllegalArgumentException("invalid rect ");
            }
            this.f715d = new GeoPoint((this.f712a.m464b() + this.f713b.m464b()) / 2, (this.f712a.m462a() + this.f713b.m462a()) / 2);
        }

        private void m864a(GeoPoint geoPoint, int i, int i2) {
            int i3 = i / 2;
            int i4 = i2 / 2;
            long b = geoPoint.m464b();
            long a = geoPoint.m462a();
            m865a(new GeoPoint(b - ((long) i3), a - ((long) i4)), new GeoPoint(b + ((long) i3), ((long) i4) + a));
        }

        public GeoPoint getLowerLeft() {
            return this.f712a;
        }

        public GeoPoint getUpperRight() {
            return this.f713b;
        }

        public GeoPoint getCenter() {
            return this.f715d;
        }

        public int getLonSpanInMeter() {
            return CoreUtil.m482a(this.f713b.getLongitudeE6() - this.f712a.getLongitudeE6());
        }

        public int getLatSpanInMeter() {
            return CoreUtil.m482a(this.f713b.getLatitudeE6() - this.f712a.getLatitudeE6());
        }

        public int getRange() {
            return this.f714c;
        }

        public String getShape() {
            return this.f716e;
        }
    }

    public PoiSearch(Activity activity, Query query) {
        this.f720d = 20;
        ClientInfoUtil.m471a(activity);
        this.f719c = activity;
        setQuery(query);
    }

    public PoiSearch(Context context, String str, Query query) {
        this.f720d = 20;
        ClientInfoUtil.m471a(context);
        this.f719c = context;
        setQuery(query);
    }

    public PoiPagedResult searchPOI() throws AMapException {
        PoiSearchServerHandler poiSearchServerHandler = new PoiSearchServerHandler(new QueryInternal(this.f718b, this.f717a), CoreUtil.m491b(this.f719c), CoreUtil.m485a(this.f719c), null);
        poiSearchServerHandler.m2655a(1);
        poiSearchServerHandler.m2660b(this.f720d);
        return PoiPagedResult.m859a(poiSearchServerHandler, (ArrayList) poiSearchServerHandler.m531j());
    }

    public void setPageSize(int i) {
        this.f720d = i;
    }

    @Deprecated
    public void setPoiNumber(int i) {
        setPageSize(i);
    }

    public void setQuery(Query query) {
        this.f718b = query;
    }

    public void setBound(SearchBound searchBound) {
        this.f717a = searchBound;
    }

    public Query getQuery() {
        return this.f718b;
    }

    public SearchBound getBound() {
        return this.f717a;
    }
}

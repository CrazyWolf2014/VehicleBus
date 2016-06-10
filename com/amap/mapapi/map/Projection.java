package com.amap.mapapi.map;

import android.graphics.Point;
import com.amap.mapapi.core.GeoPoint;

public interface Projection {
    GeoPoint fromPixels(int i, int i2);

    float metersToEquatorPixels(float f);

    Point toPixels(GeoPoint geoPoint, Point point);
}

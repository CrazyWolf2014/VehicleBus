package com.amap.mapapi.map;

import com.amap.mapapi.core.GeoPoint;

public interface RouteMessageHandler {
    void onDrag(MapView mapView, RouteOverlay routeOverlay, int i, GeoPoint geoPoint);

    void onDragBegin(MapView mapView, RouteOverlay routeOverlay, int i, GeoPoint geoPoint);

    void onDragEnd(MapView mapView, RouteOverlay routeOverlay, int i, GeoPoint geoPoint);

    boolean onRouteEvent(MapView mapView, RouteOverlay routeOverlay, int i, int i2);
}

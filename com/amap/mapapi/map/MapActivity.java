package com.amap.mapapi.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import com.amap.mapapi.core.ClientInfoUtil;
import com.amap.mapapi.core.ConfigableConst;
import java.util.ArrayList;

public abstract class MapActivity extends Activity {
    public static int MAP_MODE_BITMAP;
    public static int MAP_MODE_VECTOR;
    boolean f434a;
    private ArrayList<MapView> f435b;
    private int f436c;

    public MapActivity() {
        this.f435b = new ArrayList();
        this.f436c = MAP_MODE_BITMAP;
        this.f434a = false;
    }

    public void setMapMode(int i) {
        this.f436c = i;
    }

    public void setCachInInstalledPackaget(boolean z) {
        this.f434a = z;
    }

    public int getMapMode() {
        return this.f436c;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ClientInfoUtil.m471a(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        int size = this.f435b.size();
        for (int i = 0; i < size; i++) {
            MapView mapView = (MapView) this.f435b.get(0);
            if (mapView != null) {
                int childCount = mapView.getChildCount();
                for (int i2 = 0; i2 < childCount; i2++) {
                    if (mapView.getChildAt(i2) != null) {
                        if (mapView.getChildAt(i2).getBackground() != null) {
                            mapView.getChildAt(i2).getBackground().setCallback(null);
                        }
                        mapView.getChildAt(i2).setBackgroundDrawable(null);
                    }
                }
                ag a = mapView.m642a();
                if (a != null) {
                    a.f595c.m723b();
                }
                mapView.m650d();
                if (mapView.f484a != null) {
                    try {
                        if (!(mapView.f485b == null || mapView.f485b.isRecycled())) {
                            mapView.f485b.recycle();
                        }
                        mapView.f485b = null;
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
                this.f435b.remove(0);
                ConfigableConst.f336f--;
            }
        }
        System.gc();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    protected void onPause() {
        super.onPause();
        int size = this.f435b.size();
        for (int i = 0; i < size; i++) {
            MapView mapView = (MapView) this.f435b.get(i);
            if (mapView != null) {
                ag a = mapView.m642a();
                if (a != null) {
                    a.f595c.m725d();
                }
            }
        }
    }

    protected void onResume() {
        super.onResume();
        int size = this.f435b.size();
        for (int i = 0; i < size; i++) {
            MapView mapView = (MapView) this.f435b.get(i);
            if (mapView != null) {
                ag a = mapView.m642a();
                if (a != null) {
                    a.f595c.m724c();
                }
            }
        }
    }

    protected void onRestart() {
        super.onPause();
        int size = this.f435b.size();
        for (int i = 0; i < size; i++) {
            MapView mapView = (MapView) this.f435b.get(i);
            if (mapView != null) {
                ag a = mapView.m642a();
                if (a != null) {
                    a.f595c.m726e();
                }
            }
        }
    }

    protected void onStop() {
        super.onStop();
        int size = this.f435b.size();
        for (int i = 0; i < size; i++) {
            MapView mapView = (MapView) this.f435b.get(i);
            if (mapView != null) {
                ag a = mapView.m642a();
                if (a != null) {
                    a.f595c.m722a();
                }
            }
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    void m582a(MapView mapView, Context context, String str) {
        ConfigableConst.f336f++;
        mapView.m644a(context, str);
        this.f435b.add(mapView);
    }

    protected boolean isLocationDisplayed() {
        return false;
    }

    protected boolean isRouteDisplayed() {
        return false;
    }

    static {
        MAP_MODE_VECTOR = 1;
        MAP_MODE_BITMAP = 2;
    }
}

package com.amap.mapapi.map;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/* renamed from: com.amap.mapapi.map.n */
class CompassServer extends ad {
    private SensorManager f1911a;
    private Sensor f1912b;
    private SensorEventListener f1913c;

    public CompassServer(ag agVar, Context context) {
        super(agVar, context);
        this.f1913c = null;
        this.f1911a = (SensorManager) context.getSystemService("sensor");
        this.f1912b = this.f1911a.getDefaultSensor(3);
    }

    public boolean m1996a(SensorEventListener sensorEventListener) {
        m1994g();
        this.f1913c = sensorEventListener;
        return m1995h();
    }

    public void m1997b() {
        m1994g();
        this.f1913c = null;
    }

    public void b_() {
        m1995h();
    }

    public void a_() {
        m1994g();
    }

    private void m1994g() {
        if (this.f1913c != null) {
            try {
                this.f1911a.unregisterListener(this.f1913c);
            } catch (Exception e) {
            }
        }
    }

    private boolean m1995h() {
        boolean z = false;
        if (this.f1913c != null) {
            try {
                z = this.f1911a.registerListener(this.f1913c, this.f1912b, 1);
            } catch (Exception e) {
            }
        }
        return z;
    }
}

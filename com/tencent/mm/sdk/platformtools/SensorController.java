package com.tencent.mm.sdk.platformtools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.google.protobuf.DescriptorProtos.FileOptions;

public class SensorController extends BroadcastReceiver implements SensorEventListener {
    private static float aX;
    private static float ba;
    private SensorManager aY;
    private float aZ;
    private SensorEventCallBack bb;
    private Sensor bc;
    private final boolean bd;
    private boolean be;
    private boolean bf;

    public interface SensorEventCallBack {
        void onSensorEvent(boolean z);
    }

    static {
        aX = 4.2949673E9f;
        ba = 0.5f;
    }

    public SensorController(Context context) {
        this.be = false;
        this.bf = false;
        this.aY = (SensorManager) context.getSystemService("sensor");
        this.bc = this.aY.getDefaultSensor(8);
        this.bd = this.bc != null;
        this.aZ = ba + 1.0f;
    }

    public boolean isSensorEnable() {
        return this.bd;
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.HEADSET_PLUG")) {
            int intExtra = intent.getIntExtra("state", 0);
            if (intExtra == 1) {
                this.be = true;
            }
            if (intExtra == 0) {
                this.be = false;
            }
        }
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (!this.be) {
            float f = sensorEvent.values[0];
            switch (sensorEvent.sensor.getType()) {
                case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                    if (f < aX) {
                        aX = f;
                        ba = 0.5f + f;
                    }
                    if (this.aZ < ba || f >= ba) {
                        if (this.aZ <= ba && f > ba && this.bb != null) {
                            Log.m1663v("MicroMsg.SensorController", "sensor event true");
                            this.bb.onSensorEvent(true);
                        }
                    } else if (this.bb != null) {
                        Log.m1663v("MicroMsg.SensorController", "sensor event false");
                        this.bb.onSensorEvent(false);
                    }
                    this.aZ = f;
                default:
            }
        }
    }

    public void removeSensorCallBack() {
        Log.m1663v("MicroMsg.SensorController", "sensor callback removed");
        this.aY.unregisterListener(this, this.bc);
        this.aY.unregisterListener(this);
        this.bf = false;
        this.bb = null;
    }

    public void setSensorCallBack(SensorEventCallBack sensorEventCallBack) {
        Log.m1663v("MicroMsg.SensorController", "sensor callback set");
        if (!this.bf) {
            this.aY.registerListener(this, this.bc, 2);
            this.bf = true;
        }
        this.bb = sensorEventCallBack;
    }
}

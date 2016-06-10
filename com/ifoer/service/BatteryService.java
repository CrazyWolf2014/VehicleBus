package com.ifoer.service;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.GpioManager;
import android.hardware.GpioPort;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import com.example.gpiomanager.MainActivity;
import java.io.IOException;

public class BatteryService extends Service {
    public static final String BUILD;
    public static final String BUILDA23 = "A23";
    public static final String BUILDSTART;
    public static final String BUILDV04 = "V04";
    public static final String BUILDV05 = "V05";
    public static final String BUILDVER;
    public static final String DEVICE_NAME = "/dev/mtgpio";
    private IntentFilter batteryLevelFilter;
    private BatteryReceiver batteryReceiver;
    private boolean isExistDialog;
    public final Handler mHandler;
    Runnable runnable;
    private int timeLimit;

    /* renamed from: com.ifoer.service.BatteryService.1 */
    class C06851 implements Runnable {
        C06851() {
        }

        public void run() {
            if (BatteryService.this.timeLimit < 30) {
                BatteryService batteryService = BatteryService.this;
                batteryService.timeLimit = batteryService.timeLimit + 1;
                BatteryService.this.mHandler.postDelayed(this, 1000);
                if (BatteryService.BUILD.startsWith("A23_V")) {
                    if (BatteryService.BUILDVER.equalsIgnoreCase(BatteryService.BUILDV04)) {
                        BatteryService.this.controlLights();
                        return;
                    } else if (BatteryService.BUILDVER.equalsIgnoreCase(BatteryService.BUILDV05)) {
                        BatteryService.this.controlLights();
                        return;
                    } else {
                        return;
                    }
                } else if (BatteryService.BUILD.startsWith("MTK")) {
                    try {
                        GpioPort mGpioPort = ((GpioManager) BatteryService.this.getSystemService("gpio")).openGpioPort(BatteryService.DEVICE_NAME);
                        mGpioPort.setRedLightOn();
                        SystemClock.sleep(200);
                        mGpioPort.setRedLightOff();
                        SystemClock.sleep(200);
                        mGpioPort.close();
                        return;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        return;
                    }
                } else {
                    return;
                }
            }
            BatteryService.this.mHandler.removeCallbacks(this);
        }
    }

    private class BatteryReceiver extends BroadcastReceiver {

        /* renamed from: com.ifoer.service.BatteryService.BatteryReceiver.1 */
        class C06861 implements OnClickListener {
            C06861() {
            }

            public void onClick(DialogInterface dialog, int which) {
                Intent bService = new Intent();
                bService.setAction("com.ifoer.service.BatteryService");
                BatteryService.this.stopService(bService);
                dialog.dismiss();
            }
        }

        private BatteryReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                float batteryPct = ((float) (intent.getIntExtra("level", 0) * 100)) / ((float) intent.getIntExtra("scale", 100));
                if (batteryPct <= 10.0f && BatteryService.this.isExistDialog) {
                    Dialog mDialog = new Builder(context).setTitle("low battery alert").setMessage("The current battery for\uff1a" + String.valueOf(batteryPct) + "%").setCancelable(false).setNegativeButton("close", new C06861()).create();
                    mDialog.getWindow().setType(2003);
                    mDialog.show();
                    BatteryService.this.isExistDialog = false;
                    new RedLightThread(null).start();
                }
            }
        }
    }

    private class RedLightThread extends Thread {
        private RedLightThread() {
        }

        public void run() {
            BatteryService.this.mHandler.postDelayed(BatteryService.this.runnable, 1000);
        }
    }

    public BatteryService() {
        this.isExistDialog = true;
        this.timeLimit = 0;
        this.mHandler = new Handler();
        this.runnable = new C06851();
    }

    static {
        BUILD = Build.DISPLAY;
        BUILDSTART = BUILD.substring(0, 3);
        BUILDVER = BUILD.substring(BUILD.indexOf("V"), BUILD.indexOf("V") + 3);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        registerBroadcastReceiver();
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void registerBroadcastReceiver() {
        this.batteryReceiver = new BatteryReceiver();
        this.batteryLevelFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        registerReceiver(this.batteryReceiver, this.batteryLevelFilter);
    }

    private void controlLights() {
        RedOn(BUILDVER);
        SystemClock.sleep(300);
        RedOff(BUILDVER);
        SystemClock.sleep(300);
    }

    public void RedOn(String version) {
        if (version.equalsIgnoreCase(BUILDV04)) {
            MainActivity.setDirection("PB", 3, "out");
            MainActivity.setValue("PB", 3, 1);
        } else if (version.equalsIgnoreCase(BUILDV05)) {
            MainActivity.setDirection("PL", 11, "out");
            MainActivity.setValue("PL", 11, 1);
        }
    }

    public void RedOff(String version) {
        if (version.equalsIgnoreCase(BUILDV04)) {
            MainActivity.setDirection("PB", 3, "out");
            MainActivity.setValue("PB", 3, 0);
        } else if (version.equalsIgnoreCase(BUILDV05)) {
            MainActivity.setDirection("PL", 11, "out");
            MainActivity.setValue("Pl", 11, 0);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.batteryReceiver != null) {
            unregisterReceiver(this.batteryReceiver);
        }
    }
}

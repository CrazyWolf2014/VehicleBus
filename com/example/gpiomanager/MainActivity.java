package com.example.gpiomanager;

import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import com.cnlaunch.x431pro.common.Constants;
import com.ifoer.serialport.SanMu;
import com.ifoer.util.MySharedPreferences;
import org.xbill.DNS.WKSRecord.Service;

public class MainActivity {
    public static final String DEVICE_NAME = "/dev/mtgpio";
    public static final int GPIO_IOCQDATAIN = 18;
    public static final int GPIO_IOCQDATAOUT = 19;
    public static final int GPIO_IOCSDATAHIGH = 21;
    public static final int GPIO_IOCSDATALOW = 20;
    public static final int GPIO_IOCSDIRIN = 7;
    public static final int GPIO_IOCSDIROUT = 8;
    public static final int GPIO_IOCTMODE0 = 2;
    public static final String TAG = "GPIO";
    public static String V04;
    public static String V05;
    public static String version;

    private static native int native_gpio_getValue(String str, int i);

    private static native void native_gpio_setDirection(String str, int i, String str2);

    private static native int native_gpio_setValue(String str, int i, int i2);

    static {
        version = Build.DISPLAY;
        V04 = "A23_V04_CRP229";
        V05 = "A23_V05_CRP229";
        System.loadLibrary("gpio_jni");
    }

    public static void reSet(Context context) {
        if (version.equalsIgnoreCase(V04)) {
            setDirection("PG", 4, "out");
            setValue("PG", 4, 1);
            SystemClock.sleep(100);
            setValue("PG", 4, 0);
            SystemClock.sleep(50);
            setValue("PG", 4, 1);
            SystemClock.sleep(1000);
        } else if (version.equalsIgnoreCase(V05)) {
            setDirection("PG", 10, "out");
            setValue("PG", 10, 1);
            SystemClock.sleep(100);
            setValue("PG", 10, 0);
            SystemClock.sleep(50);
            setValue("PG", 10, 1);
            SystemClock.sleep(1000);
        } else if (version.contains("MTK")) {
            SanmuReset(context);
        }
    }

    public static void SanMusetHigh(Context context) {
        SanMu.IO(Service.STATSRV, 1, context);
        SystemClock.sleep(500);
    }

    public static void SanMusetLow(Context context) {
        SanMu.IO(Service.STATSRV, 0, context);
    }

    public static int SanmuGetV(Context context) {
        return SanMu.IO(Service.CISCO_SYS, -1, context);
    }

    public static void SanmuReset(Context context) {
        SanMu.IO(Service.PROFILE, 1, context);
        SystemClock.sleep(100);
        SanMu.IO(Service.PROFILE, 0, context);
        SystemClock.sleep(400);
        SanMu.IO(Service.PROFILE, 1, context);
        SystemClock.sleep(2000);
    }

    public static void setGoloBlick(String version) {
        if (version.equalsIgnoreCase(V04)) {
            setDirection("PL", GPIO_IOCSDIRIN, "out");
            setValue("PL", GPIO_IOCSDIRIN, 1);
            SystemClock.sleep(300);
            setValue("PL", GPIO_IOCSDIRIN, 0);
            SystemClock.sleep(200);
        } else if (version.equalsIgnoreCase(V05)) {
            setDirection("PL", 5, "out");
            setValue("PL", 5, 1);
            SystemClock.sleep(300);
            setValue("PL", 5, 0);
            SystemClock.sleep(200);
        }
    }

    public static void GoloOn(String version) {
        if (version.equalsIgnoreCase(V04)) {
            setDirection("PL", GPIO_IOCSDIRIN, "out");
            setValue("PL", GPIO_IOCSDIRIN, 1);
        } else if (version.equalsIgnoreCase(V05)) {
            setDirection("PL", 5, "out");
            setValue("PL", 5, 1);
        }
    }

    public static void GoloOff(String version) {
        if (version.equalsIgnoreCase(V04)) {
            setDirection("PL", GPIO_IOCSDIRIN, "out");
            setValue("PL", GPIO_IOCSDIRIN, 0);
        } else if (version.equalsIgnoreCase(V05)) {
            setDirection("PL", 5, "out");
            setValue("PL", 5, 0);
        }
    }

    public static void setGolo() {
        setDirection("PL", 5, "out");
        setValue("PL", 5, 0);
    }

    public static void goloFlicker() {
        setValue("PL", 5, 1);
    }

    public static void goloColse() {
        setValue("PL", 5, 0);
    }

    public void setLH(String version) {
        if (version.equalsIgnoreCase(V04)) {
            setDirection("PG", 1, "out");
            setValue("PG", 1, 0);
            SystemClock.sleep(100);
            setValue("PG", 1, 1);
            SystemClock.sleep(50);
            setValue("PG", 1, 0);
            SystemClock.sleep(1000);
        } else if (version.equalsIgnoreCase(V05)) {
            setDirection("PG", 12, "out");
            setValue("PG", 12, 0);
            SystemClock.sleep(100);
            setValue("PG", 12, 1);
            SystemClock.sleep(50);
            setValue("PG", 12, 0);
            SystemClock.sleep(1000);
        }
    }

    public static void setLow_OFF() {
        if (version.equalsIgnoreCase(V04)) {
            setDirection("PG", 1, "out");
            setValue("PG", 1, 0);
        } else if (version.equalsIgnoreCase(V05)) {
            setDirection("PG", 12, "out");
            setValue("PG", 12, 0);
        }
    }

    public static void setHigh_ON() {
        if (version.equalsIgnoreCase(V04)) {
            setDirection("PG", 1, "out");
            setValue("PG", 1, 1);
        } else if (version.equalsIgnoreCase(V05)) {
            setDirection("PG", 12, "out");
            setValue("PG", 12, 1);
        }
    }

    public static void setHigh_ON1() {
        setDirection("PG", 12, "out");
        setValue("PG", 12, 1);
    }

    public static int getV() {
        if (version.equalsIgnoreCase(V04)) {
            setDirection("PG", GPIO_IOCTMODE0, "in");
            return getValue("PG", GPIO_IOCTMODE0);
        } else if (!version.equalsIgnoreCase(V05)) {
            return 0;
        } else {
            setDirection("PG", 11, "in");
            return getValue("PG", 11);
        }
    }

    public static int ODB_ISOK(boolean hardFlag) {
        if (!hardFlag) {
            return 0;
        }
        if (getV() == 0) {
            return -1;
        }
        return 1;
    }

    public int DPU431_OpenV(boolean hardFlag, MainActivity m) {
        if (!hardFlag) {
            return 0;
        }
        if (getV() != 0) {
            return 1;
        }
        setHigh_ON();
        SystemClock.sleep(500);
        return GPIO_IOCTMODE0;
    }

    void DPU431_CloseV(MainActivity m) {
        setLow_OFF();
    }

    public static void isHavODB(Context context) {
        if (Integer.parseInt(Build.DISPLAY.substring(6, GPIO_IOCSDIRIN)) >= 5) {
            MySharedPreferences.setBoolean(context, Constants.hasODB, true);
        } else {
            MySharedPreferences.setBoolean(context, Constants.hasODB, false);
        }
    }

    public static void setDirection(String port, int num, String inout) {
        native_gpio_setDirection(port.toUpperCase(), num, inout.toLowerCase());
    }

    public static int setValue(String port, int num, int value) {
        return native_gpio_setValue(port.toUpperCase(), num, value);
    }

    public static int getValue(String port, int num) {
        return native_gpio_getValue(port.toUpperCase(), num);
    }
}

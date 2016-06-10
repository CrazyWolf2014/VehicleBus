package com.ifoer.serialport;

import android.content.Context;
import android.hardware.GpioManager;
import android.hardware.GpioPort;
import java.io.IOException;

public class SanMu {
    public static final String DEVICE_NAME = "/dev/mtgpio";
    public static final int GPIO_IOCQDATAIN = 18;
    public static final int GPIO_IOCQDATAOUT = 19;
    public static final int GPIO_IOCSDATAHIGH = 21;
    public static final int GPIO_IOCSDATALOW = 20;
    public static final int GPIO_IOCSDIRIN = 7;
    public static final int GPIO_IOCSDIROUT = 8;
    public static final int GPIO_IOCTMODE0 = 2;

    public static int IO(int port, int level, Context context) {
        int ret = 0;
        try {
            GpioPort mGpioPort = ((GpioManager) context.getSystemService("gpio")).openGpioPort(DEVICE_NAME);
            if (level >= 0) {
                mGpioPort.GpioIoctl(GPIO_IOCTMODE0, port);
                mGpioPort.GpioIoctl(GPIO_IOCSDIROUT, port);
                if (level > 0) {
                    mGpioPort.GpioIoctl(GPIO_IOCSDATAHIGH, port);
                } else {
                    mGpioPort.GpioIoctl(GPIO_IOCSDATALOW, port);
                }
            } else {
                mGpioPort.GpioIoctl(GPIO_IOCTMODE0, port);
                mGpioPort.GpioIoctl(GPIO_IOCSDIRIN, port);
                ret = mGpioPort.GpioIoctl(GPIO_IOCQDATAIN, port);
            }
            mGpioPort.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}

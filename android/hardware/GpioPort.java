package android.hardware;

import android.os.ParcelFileDescriptor;
import java.io.FileDescriptor;
import java.io.IOException;

public class GpioPort {
    public static final int GPIO_BUZZER_OFF = 23;
    public static final int GPIO_BUZZER_ON = 22;
    public static final int GPIO_GET_ADC_CHANNEL_ONE_DATA = 48;
    public static final int GPIO_IOCQDATAIN = 18;
    public static final int GPIO_IOCQDATAOUT = 19;
    public static final int GPIO_IOCQDIR = 6;
    public static final int GPIO_IOCQINV = 15;
    public static final int GPIO_IOCQMODE = 1;
    public static final int GPIO_IOCQPULL = 12;
    public static final int GPIO_IOCQPULLEN = 9;
    public static final int GPIO_IOCSDATAHIGH = 21;
    public static final int GPIO_IOCSDATALOW = 20;
    public static final int GPIO_IOCSDIRIN = 7;
    public static final int GPIO_IOCSDIROUT = 8;
    public static final int GPIO_IOCSINVDISABLE = 17;
    public static final int GPIO_IOCSINVENABLE = 16;
    public static final int GPIO_IOCSPULLDISABLE = 11;
    public static final int GPIO_IOCSPULLDOWN = 13;
    public static final int GPIO_IOCSPULLENABLE = 10;
    public static final int GPIO_IOCSPULLUP = 14;
    public static final int GPIO_IOCTMODE0 = 2;
    public static final int GPIO_IOCTMODE1 = 3;
    public static final int GPIO_IOCTMODE2 = 4;
    public static final int GPIO_IOCTMODE3 = 5;
    public static final int GPIO_LED_BLUE_BLINK = 34;
    public static final int GPIO_LED_BLUE_OFF = 35;
    public static final int GPIO_LED_BLUE_ON = 33;
    public static final int GPIO_LED_GREEN_BLINK = 40;
    public static final int GPIO_LED_GREEN_OFF = 41;
    public static final int GPIO_LED_GREEN_ON = 39;
    public static final int GPIO_LED_RED_BLINK = 25;
    public static final int GPIO_LED_RED_OFF = 32;
    public static final int GPIO_LED_RED_ON = 24;
    public static final int GPIO_LED_YELLOW_BLINK = 37;
    public static final int GPIO_LED_YELLOW_OFF = 38;
    public static final int GPIO_LED_YELLOW_ON = 36;
    private ParcelFileDescriptor mFileDescriptor;
    private final String mName;
    private int mNativeContext;

    private native int native_GpioIoctl(int i, int i2);

    private native void native_close();

    private native int[] native_getvoltage();

    private native void native_open(FileDescriptor fileDescriptor) throws IOException;

    private native int native_setBlueLightOff();

    private native int native_setBlueLightOn();

    private native int native_setBlueLightblick();

    private native int native_setBuzzerOff();

    private native int native_setBuzzerOn();

    private native int native_setGreenLightOff();

    private native int native_setGreenLightOn();

    private native int native_setGreenLightblick();

    private native int native_setRedLightOff();

    private native int native_setRedLightOn();

    private native int native_setRedLightblick();

    private native int native_setYellowLightOff();

    private native int native_setYellowLightOn();

    private native int native_setYellowLightblick();

    public GpioPort(String name) {
        this.mName = name;
    }

    public void open(ParcelFileDescriptor pfd) throws IOException {
        native_open(pfd.getFileDescriptor());
        this.mFileDescriptor = pfd;
    }

    public void close() throws IOException {
        if (this.mFileDescriptor != null) {
            this.mFileDescriptor.close();
            this.mFileDescriptor = null;
        }
        native_close();
    }

    public int[] getVoltage() throws IOException {
        if (this.mFileDescriptor != null) {
            return native_getvoltage();
        }
        return null;
    }

    public int setBuzzerOn() {
        return native_setBuzzerOn();
    }

    public int setBuzzerOff() {
        return native_setBuzzerOff();
    }

    public int setRedLightOn() {
        return native_setRedLightOn();
    }

    public int setRedLightblick() {
        return native_setRedLightblick();
    }

    public int setRedLightOff() {
        return native_setRedLightOff();
    }

    public int setBlueLightOn() {
        return native_setBlueLightOn();
    }

    public int setBlueLightblick() {
        return native_setBlueLightblick();
    }

    public int setBlueLightOff() {
        return native_setBlueLightOff();
    }

    public int setYellowLightOn() {
        return native_setYellowLightOn();
    }

    public int setYellowLightblick() {
        return native_setYellowLightblick();
    }

    public int setYellowLightOff() {
        return native_setYellowLightOff();
    }

    public int setGreenLightOn() {
        return native_setGreenLightOn();
    }

    public int setGreenLightblick() {
        return native_setGreenLightblick();
    }

    public int setGreenLightOff() {
        return native_setGreenLightOff();
    }

    public int GpioIoctl(int cmd, int pin) {
        return native_GpioIoctl(cmd, pin);
    }
}

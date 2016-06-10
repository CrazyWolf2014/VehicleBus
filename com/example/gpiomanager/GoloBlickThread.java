package com.example.gpiomanager;

import android.content.Context;
import android.hardware.GpioManager;
import android.hardware.GpioPort;
import com.ifoer.expeditionphone.WelcomeActivity;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;

public class GoloBlickThread extends Thread {
    private static String V04;
    private static String V05;
    private Context context;
    private boolean isSanmu;
    private String version;

    static {
        V04 = "A23_V04_CRP229";
        V05 = "A23_V05_CRP229";
    }

    public GoloBlickThread(Context context, boolean isSanmu, String version) {
        this.isSanmu = false;
        this.version = XmlPullParser.NO_NAMESPACE;
        this.context = context;
        this.isSanmu = isSanmu;
        this.version = version;
    }

    public void run() {
        int i;
        if (this.isSanmu) {
            try {
                GpioPort mGpioPort = ((GpioManager) this.context.getSystemService("gpio")).openGpioPort(WelcomeActivity.DEVICE_NAME);
                for (i = 0; i < 10; i++) {
                    mGpioPort.setYellowLightOn();
                    Thread.sleep(200);
                    mGpioPort.setYellowLightOff();
                    Thread.sleep(200);
                }
                mGpioPort.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        } else if (this.version.equalsIgnoreCase(V04)) {
            i = 0;
            while (i < 10) {
                try {
                    MainActivity.setDirection("PL", 7, "out");
                    MainActivity.setValue("PL", 7, 1);
                    Thread.sleep(300);
                    MainActivity.setValue("PL", 7, 0);
                    Thread.sleep(200);
                    i++;
                } catch (InterruptedException e22) {
                    e22.printStackTrace();
                    return;
                }
            }
        } else if (this.version.equalsIgnoreCase(V05)) {
            i = 0;
            while (i < 10) {
                try {
                    MainActivity.setDirection("PL", 5, "out");
                    MainActivity.setValue("PL", 5, 1);
                    Thread.sleep(300);
                    MainActivity.setValue("PL", 5, 0);
                    Thread.sleep(200);
                    i++;
                } catch (InterruptedException e222) {
                    e222.printStackTrace();
                    return;
                }
            }
        }
    }
}

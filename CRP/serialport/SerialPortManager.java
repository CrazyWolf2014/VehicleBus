package CRP.serialport;

import CRP.utils.CRPTools;
import android.os.Build;
import com.ifoer.serialport.SerialPort;
import com.ifoer.serialport.SerialPortFinder;

public class SerialPortManager {
    public static final String TAG = "SerialPortManager";
    private SerialPort mSerialPort;
    public SerialPortFinder mSerialPortFinder;

    public SerialPortManager() {
        this.mSerialPort = null;
        this.mSerialPortFinder = new SerialPortFinder();
    }

    public SerialPort getSerialPort() {
        String builder = Build.DISPLAY;
        if (this.mSerialPort != null) {
            this.mSerialPort.close();
        }
        this.mSerialPort = null;
        try {
            if (builder.contains("A23_V0")) {
                this.mSerialPort = SerialPort.getInsance(CRPTools.CRP_HEQIANG_DEVICEINFO, CRPTools.CRP_BOTELV, 0);
            } else {
                this.mSerialPort = SerialPort.getInsance(CRPTools.CRP_SANMU_DEVICEINFO, CRPTools.CRP_BOTELV, 0);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return this.mSerialPort;
    }

    public void closeSerialPort() {
        if (this.mSerialPort != null) {
            this.mSerialPort.close();
            this.mSerialPort = null;
        }
    }
}

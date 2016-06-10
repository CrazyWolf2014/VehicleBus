package android.hardware;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import java.io.IOException;

public class GpioManager {
    private static final String TAG = "GpioManager";
    private final Context mContext;
    private final IGpioManager mGpioService;

    public GpioManager(Context context, IGpioManager service) {
        this.mContext = context;
        this.mGpioService = service;
    }

    public GpioPort openGpioPort(String name) throws IOException {
        try {
            ParcelFileDescriptor pfd = this.mGpioService.openGpioPort(name);
            if (pfd != null) {
                GpioPort port = new GpioPort(name);
                port.open(pfd);
                return port;
            }
            throw new IOException("Could not open serial port " + name);
        } catch (RemoteException e) {
            Log.e(TAG, "exception in UsbManager.openDevice", e);
            return null;
        }
    }
}

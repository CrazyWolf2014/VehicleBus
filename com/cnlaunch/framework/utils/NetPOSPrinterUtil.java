package com.cnlaunch.framework.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.format.Formatter;
import com.cnlaunch.x431frame.C0136R;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import org.achartengine.renderer.DefaultRenderer;
import org.codehaus.jackson.smile.SmileConstants;
import org.xbill.DNS.KEYRecord;

public class NetPOSPrinterUtil {
    public static final int ERROR_PRINT_ACTUATOR_FAULT = 8;
    public static final int ERROR_PRINT_HEAD_OVERHEATING = 128;
    public static final int ERROR_PRINT_JAM = 1;
    public static final int ERROR_PRINT_NO_PAPER = 4;
    public static final int ERROR_PRINT_WILL_NO_PAPER = 2;
    public static final int PRINT_NOT_CONNECT = 4095;
    public static final int PRINT_PORT = 7001;
    public static final int PRINT_WIDTH = 384;
    public static final int SUCCESS_PRINT = 0;
    private static DataOutputStream dos;
    private static DataInputStream in;
    private static Bitmap nBitmapFirst;
    private static Bitmap nBitmapSecond;
    private static int result;
    private static int serverAddress;
    private static TextPaint textPaint;
    private static Socket wifiSocket;

    /* renamed from: com.cnlaunch.framework.utils.NetPOSPrinterUtil.1 */
    class C01351 implements Runnable {
        private final /* synthetic */ Context val$context;
        private final /* synthetic */ int val$result;

        C01351(int i, Context context) {
            this.val$result = i;
            this.val$context = context;
        }

        public void run() {
            switch (this.val$result) {
                case KEYRecord.OWNER_USER /*0*/:
                    NToast.longToast(this.val$context, C0136R.string.print_success);
                case NetPOSPrinterUtil.ERROR_PRINT_JAM /*1*/:
                    NToast.longToast(this.val$context, C0136R.string.print_jam);
                case NetPOSPrinterUtil.ERROR_PRINT_WILL_NO_PAPER /*2*/:
                    NToast.longToast(this.val$context, C0136R.string.print_will_nopaper);
                case NetPOSPrinterUtil.ERROR_PRINT_NO_PAPER /*4*/:
                    NToast.longToast(this.val$context, C0136R.string.print_no_paper);
                case NetPOSPrinterUtil.ERROR_PRINT_ACTUATOR_FAULT /*8*/:
                    NToast.longToast(this.val$context, C0136R.string.print_actuator_fault);
                case NetPOSPrinterUtil.ERROR_PRINT_HEAD_OVERHEATING /*128*/:
                    NToast.longToast(this.val$context, C0136R.string.print_head_overheating);
                default:
            }
        }
    }

    static {
        result = 0;
        dos = null;
        in = null;
    }

    public static int printPic(Context context, Bitmap bmp) {
        WifiManager wifi_service = (WifiManager) context.getSystemService("wifi");
        DhcpInfo dhcpinfo = wifi_service.getDhcpInfo();
        WifiInfo wifi_info = wifi_service.getConnectionInfo();
        if (wifi_info != null && wifi_info.getSSID() != null && !wifi_info.getSSID().startsWith("X-431PRINTER") && !wifi_info.getSSID().startsWith("\"X-431PRINTER")) {
            return PRINT_NOT_CONNECT;
        }
        serverAddress = dhcpinfo.serverAddress;
        try {
            wifiSocket = new Socket(Formatter.formatIpAddress(serverAddress), PRINT_PORT);
            dos = new DataOutputStream(wifiSocket.getOutputStream());
            in = new DataInputStream(wifiSocket.getInputStream());
            if (dos == null || in == null) {
                return PRINT_NOT_CONNECT;
            }
            byte[] data = new byte[3];
            data[0] = (byte) 27;
            data[ERROR_PRINT_JAM] = (byte) 51;
            try {
                dos.write(data, 0, data.length);
                data[0] = (byte) 0;
                data[ERROR_PRINT_JAM] = (byte) 0;
                data[ERROR_PRINT_WILL_NO_PAPER] = (byte) 0;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            byte[] escj = new byte[3];
            escj[0] = (byte) 27;
            escj[ERROR_PRINT_JAM] = (byte) 74;
            int i = 3;
            byte[] esccheck = new byte[]{(byte) 29, (byte) 114, (byte) 73};
            byte[] escBmp = new byte[5];
            escBmp[0] = (byte) 27;
            escBmp[ERROR_PRINT_JAM] = (byte) 42;
            escBmp[ERROR_PRINT_WILL_NO_PAPER] = SmileConstants.TOKEN_LITERAL_NULL;
            escBmp[3] = (byte) (bmp.getWidth() % KEYRecord.OWNER_ZONE);
            escBmp[ERROR_PRINT_NO_PAPER] = (byte) (bmp.getWidth() / KEYRecord.OWNER_ZONE);
            for (int i2 = 0; i2 < (bmp.getHeight() / 24) + ERROR_PRINT_JAM; i2 += ERROR_PRINT_JAM) {
                try {
                    dos.write(escBmp, 0, escBmp.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < bmp.getWidth(); j += ERROR_PRINT_JAM) {
                    for (int k = 0; k < 24; k += ERROR_PRINT_JAM) {
                        if ((i2 * 24) + k < bmp.getHeight()) {
                            if (Color.red(bmp.getPixel(j, (i2 * 24) + k)) == 0) {
                                int i3 = k / ERROR_PRINT_ACTUATOR_FAULT;
                                data[i3] = (byte) (data[i3] + ((byte) (ERROR_PRINT_HEAD_OVERHEATING >> (k % ERROR_PRINT_ACTUATOR_FAULT))));
                            }
                        }
                    }
                    try {
                        dos.write(data, 0, data.length);
                        data[0] = (byte) 0;
                        data[ERROR_PRINT_JAM] = (byte) 0;
                        data[ERROR_PRINT_WILL_NO_PAPER] = (byte) 0;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                try {
                    if (i2 % 10 == 0) {
                        dos.write(esccheck);
                        if (in.readByte() == null) {
                            dos.write(escj, 0, escj.length);
                        }
                    } else {
                        dos.write(escj, 0, escj.length);
                    }
                } catch (IOException e22) {
                    e22.printStackTrace();
                }
            }
            i = 3;
            byte[] escf = new byte[]{(byte) 29, (byte) 122, (byte) 49};
            i = 3;
            byte[] esck = new byte[]{(byte) 27, (byte) 74, (byte) 64};
            try {
                dos.write(escf);
                dos.write(esck);
                result = in.readByte();
                try {
                    dos.close();
                    in.close();
                } catch (IOException e222) {
                    e222.printStackTrace();
                }
                return result;
            } catch (IOException e3) {
                return PRINT_NOT_CONNECT;
            }
        } catch (UnknownHostException e12) {
            e12.printStackTrace();
            return PRINT_NOT_CONNECT;
        } catch (IOException e13) {
            e13.printStackTrace();
            return PRINT_NOT_CONNECT;
        }
    }

    public static int printPic(Context context, Bitmap bmp, String ip) {
        if (TextUtils.isEmpty(ip)) {
            return PRINT_NOT_CONNECT;
        }
        try {
            wifiSocket = new Socket(ip, PRINT_PORT);
            dos = new DataOutputStream(wifiSocket.getOutputStream());
            in = new DataInputStream(wifiSocket.getInputStream());
            if (dos == null || in == null) {
                return PRINT_NOT_CONNECT;
            }
            byte[] data = new byte[3];
            data[0] = (byte) 27;
            data[ERROR_PRINT_JAM] = (byte) 51;
            try {
                dos.write(data, 0, data.length);
                data[0] = (byte) 0;
                data[ERROR_PRINT_JAM] = (byte) 0;
                data[ERROR_PRINT_WILL_NO_PAPER] = (byte) 0;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            byte[] escj = new byte[3];
            escj[0] = (byte) 27;
            escj[ERROR_PRINT_JAM] = (byte) 74;
            byte[] esccheck = new byte[]{(byte) 29, (byte) 114, (byte) 73};
            byte[] escBmp = new byte[]{(byte) 27, (byte) 42, SmileConstants.TOKEN_LITERAL_NULL, (byte) (bmp.getWidth() % KEYRecord.OWNER_ZONE), (byte) (bmp.getWidth() / KEYRecord.OWNER_ZONE)};
            for (int i = 0; i < (bmp.getHeight() / 24) + ERROR_PRINT_JAM; i += ERROR_PRINT_JAM) {
                try {
                    dos.write(escBmp, 0, escBmp.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < bmp.getWidth(); j += ERROR_PRINT_JAM) {
                    for (int k = 0; k < 24; k += ERROR_PRINT_JAM) {
                        if ((i * 24) + k < bmp.getHeight()) {
                            if (Color.red(bmp.getPixel(j, (i * 24) + k)) == 0) {
                                int i2 = k / ERROR_PRINT_ACTUATOR_FAULT;
                                data[i2] = (byte) (data[i2] + ((byte) (ERROR_PRINT_HEAD_OVERHEATING >> (k % ERROR_PRINT_ACTUATOR_FAULT))));
                            }
                        }
                    }
                    try {
                        dos.write(data, 0, data.length);
                        data[0] = (byte) 0;
                        data[ERROR_PRINT_JAM] = (byte) 0;
                        data[ERROR_PRINT_WILL_NO_PAPER] = (byte) 0;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                try {
                    if (i % 10 == 0) {
                        dos.write(esccheck);
                        if (in.readByte() == null) {
                            dos.write(escj, 0, escj.length);
                        }
                    } else {
                        dos.write(escj, 0, escj.length);
                    }
                } catch (IOException e22) {
                    e22.printStackTrace();
                }
            }
            byte[] esck = new byte[]{(byte) 27, (byte) 74, (byte) 64};
            try {
                dos.write(new byte[]{(byte) 29, (byte) 122, (byte) 49});
                dos.write(esck);
                result = in.readByte();
                try {
                    dos.close();
                    in.close();
                } catch (IOException e222) {
                    e222.printStackTrace();
                }
                return result;
            } catch (IOException e3) {
                return PRINT_NOT_CONNECT;
            }
        } catch (UnknownHostException e4) {
            e4.printStackTrace();
            return PRINT_NOT_CONNECT;
        } catch (IOException e12) {
            e12.printStackTrace();
            return PRINT_NOT_CONNECT;
        }
    }

    public static Bitmap drawBitFirst(Context context) {
        nBitmapFirst = Bitmap.createBitmap(PRINT_WIDTH, 85, Config.RGB_565);
        Canvas canvas = new Canvas(nBitmapFirst);
        canvas.drawColor(-1);
        Paint p = new Paint();
        p.setColor(DefaultRenderer.BACKGROUND_COLOR);
        p.setTextSize(20.0f);
        canvas.drawText(context.getResources().getString(C0136R.string.print_launch), 0.0f, 20.0f, p);
        canvas.drawLine(0.0f, 40.0f, 384.0f, 40.0f, p);
        canvas.drawText(context.getResources().getString(C0136R.string.print_automobile_fault_diagnosis_test_report), 20.0f, 70.0f, p);
        canvas.drawLine(0.0f, 80.0f, 384.0f, 80.0f, p);
        return nBitmapFirst;
    }

    public static Bitmap drawBitSecond(Context context, String s) {
        textPaint = new TextPaint();
        textPaint.setColor(DefaultRenderer.BACKGROUND_COLOR);
        textPaint.setTextSize(20.0f);
        StaticLayout layout = new StaticLayout(s, textPaint, PRINT_WIDTH, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        nBitmapSecond = Bitmap.createBitmap(PRINT_WIDTH, layout.getHeight(), Config.RGB_565);
        Canvas canvas = new Canvas(nBitmapSecond);
        canvas.drawColor(-1);
        layout.draw(canvas);
        return nBitmapSecond;
    }

    public static Bitmap mixtureBitmap(Bitmap first, Bitmap second) {
        if (first == null || second == null) {
            return null;
        }
        Bitmap newBitmap = Bitmap.createBitmap(PRINT_WIDTH, first.getHeight() + second.getHeight(), Config.RGB_565);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, 0.0f, 0.0f, null);
        cv.drawBitmap(second, 0.0f, (float) first.getHeight(), null);
        return newBitmap;
    }

    public static void resultToast(Context context, int result) {
        Handler handler = new Handler(Looper.getMainLooper());
        if (context != null) {
            handler.post(new C01351(result, context));
        }
    }
}

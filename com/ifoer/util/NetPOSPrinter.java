package com.ifoer.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.MotionEventCompat;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.format.Formatter;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.achartengine.renderer.DefaultRenderer;
import org.codehaus.jackson.smile.SmileConstants;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public class NetPOSPrinter {
    public static final int ERROR_PRINT_ACTUATOR_FAULT = 8;
    public static final int ERROR_PRINT_HEAD_OVERHEATING = 128;
    public static final int ERROR_PRINT_JAM = 1;
    public static final int ERROR_PRINT_NO_PAPER = 4;
    public static final int ERROR_PRINT_WILL_NO_PAPER = 2;
    public static final int PRINT_NOT_CONNECT = 4095;
    public static final int PRINT_PORT = 7001;
    public static final int PRINT_WIDTH = 384;
    public static final int SUCCESS_PRINT = 0;
    String company_address;
    String company_phone;
    Date curDate;
    DataOutputStream dos;
    SimpleDateFormat formatter;
    int huanhangSum;
    DataInputStream in;
    String license_plate_number;
    Context mContext;
    Bitmap nBitmap;
    Bitmap nBitmapFirst;
    Bitmap nBitmapSecond;
    int result;
    String serialNum;
    private int serverAddress;
    String str;
    TextPaint textPaint;
    Socket wifiSocket;

    /* renamed from: com.ifoer.util.NetPOSPrinter.1 */
    class C07551 implements Runnable {
        private final /* synthetic */ int val$result;

        C07551(int i) {
            this.val$result = i;
        }

        public void run() {
            switch (this.val$result) {
                case KEYRecord.OWNER_USER /*0*/:
                    Toast.makeText(NetPOSPrinter.this.mContext.getApplicationContext(), C0136R.string.print_success, NetPOSPrinter.ERROR_PRINT_JAM).show();
                case NetPOSPrinter.ERROR_PRINT_JAM /*1*/:
                    Toast.makeText(NetPOSPrinter.this.mContext.getApplicationContext(), C0136R.string.print_jam, NetPOSPrinter.ERROR_PRINT_JAM).show();
                case NetPOSPrinter.ERROR_PRINT_WILL_NO_PAPER /*2*/:
                    Toast.makeText(NetPOSPrinter.this.mContext.getApplicationContext(), C0136R.string.print_will_nopaper, NetPOSPrinter.ERROR_PRINT_JAM).show();
                case NetPOSPrinter.ERROR_PRINT_NO_PAPER /*4*/:
                    Toast.makeText(NetPOSPrinter.this.mContext.getApplicationContext(), C0136R.string.print_no_paper, NetPOSPrinter.ERROR_PRINT_JAM).show();
                case NetPOSPrinter.ERROR_PRINT_ACTUATOR_FAULT /*8*/:
                    Toast.makeText(NetPOSPrinter.this.mContext.getApplicationContext(), C0136R.string.print_Actuator_fault, NetPOSPrinter.ERROR_PRINT_JAM).show();
                case NetPOSPrinter.ERROR_PRINT_HEAD_OVERHEATING /*128*/:
                    Toast.makeText(NetPOSPrinter.this.mContext.getApplicationContext(), C0136R.string.print_head_overheating, NetPOSPrinter.ERROR_PRINT_JAM).show();
                case NetPOSPrinter.PRINT_NOT_CONNECT /*4095*/:
                    Toast.makeText(NetPOSPrinter.this.mContext.getApplicationContext(), C0136R.string.print_connect_printer, NetPOSPrinter.ERROR_PRINT_JAM).show();
                default:
            }
        }
    }

    public NetPOSPrinter() {
        this.result = 0;
        this.dos = null;
        this.in = null;
        this.huanhangSum = 0;
        this.textPaint = new TextPaint();
        this.formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.curDate = new Date(System.currentTimeMillis());
        this.str = this.formatter.format(this.curDate);
        this.company_address = MySharedPreferences.getStringValue(this.mContext, Constants.COMPANYADDRESS);
        this.company_phone = MySharedPreferences.getStringValue(this.mContext, Constants.COMPANYPHONENUMBER);
        this.license_plate_number = MySharedPreferences.getStringValue(this.mContext, Constants.LICENSEPLATENUMBERDIAG);
        this.serialNum = MySharedPreferences.getStringValue(this.mContext, MySharedPreferences.serialNoKey);
    }

    public NetPOSPrinter(Context context) {
        this.result = 0;
        this.dos = null;
        this.in = null;
        this.huanhangSum = 0;
        this.textPaint = new TextPaint();
        this.formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.curDate = new Date(System.currentTimeMillis());
        this.str = this.formatter.format(this.curDate);
        this.company_address = MySharedPreferences.getStringValue(this.mContext, Constants.COMPANYADDRESS);
        this.company_phone = MySharedPreferences.getStringValue(this.mContext, Constants.COMPANYPHONENUMBER);
        this.license_plate_number = MySharedPreferences.getStringValue(this.mContext, Constants.LICENSEPLATENUMBERDIAG);
        this.serialNum = MySharedPreferences.getStringValue(this.mContext, MySharedPreferences.serialNoKey);
        this.mContext = context;
    }

    public int printPic(Bitmap bmp) {
        WifiManager wifi_service = (WifiManager) this.mContext.getSystemService("wifi");
        DhcpInfo dhcpinfo = wifi_service.getDhcpInfo();
        WifiInfo wifi_info = wifi_service.getConnectionInfo();
        if (wifi_info != null && wifi_info.getSSID() != null && !wifi_info.getSSID().startsWith("X-431PRINTER")) {
            return PRINT_NOT_CONNECT;
        }
        this.serverAddress = dhcpinfo.serverAddress;
        try {
            this.wifiSocket = new Socket(Formatter.formatIpAddress(this.serverAddress), PRINT_PORT);
            this.dos = new DataOutputStream(this.wifiSocket.getOutputStream());
            this.in = new DataInputStream(this.wifiSocket.getInputStream());
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e12) {
            e12.printStackTrace();
        }
        if (this.dos == null || this.in == null) {
            return PRINT_NOT_CONNECT;
        }
        byte[] data = new byte[3];
        data[0] = (byte) 27;
        data[ERROR_PRINT_JAM] = (byte) 51;
        try {
            this.dos.write(data, 0, data.length);
            data[0] = (byte) 0;
            data[ERROR_PRINT_JAM] = (byte) 0;
            data[ERROR_PRINT_WILL_NO_PAPER] = (byte) 0;
        } catch (IOException e122) {
            e122.printStackTrace();
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
                this.dos.write(escBmp, 0, escBmp.length);
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
                    this.dos.write(data, 0, data.length);
                    data[0] = (byte) 0;
                    data[ERROR_PRINT_JAM] = (byte) 0;
                    data[ERROR_PRINT_WILL_NO_PAPER] = (byte) 0;
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            try {
                if (i2 % 10 == 0) {
                    this.dos.write(esccheck);
                    if (this.in.readByte() == null) {
                        this.dos.write(escj, 0, escj.length);
                    }
                } else {
                    this.dos.write(escj, 0, escj.length);
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
            this.dos.write(escf);
            this.dos.write(esck);
            this.result = this.in.readByte();
            try {
                this.dos.close();
                this.in.close();
            } catch (IOException e222) {
                e222.printStackTrace();
            }
            return this.result;
        } catch (IOException e3) {
            return PRINT_NOT_CONNECT;
        }
    }

    public Bitmap bitmapFanzhuan(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        bitmap.getPixels(new int[(width * height)], 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i += ERROR_PRINT_JAM) {
            for (int j = 0; j < width; j += ERROR_PRINT_JAM) {
                int grey = bitmap.getPixel(j, i);
                if (((((16711680 & grey) >> 16) + ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & grey) >> ERROR_PRINT_ACTUATOR_FAULT)) + (grey & KEYRecord.PROTOCOL_ANY)) / 3 < 100) {
                    bitmap.setPixel(j, i, DefaultRenderer.BACKGROUND_COLOR);
                } else {
                    bitmap.setPixel(j, i, -1);
                }
            }
        }
        return bitmap;
    }

    public Bitmap bitmapDuiBi(Bitmap bp) {
        ColorMatrix cm = new ColorMatrix();
        Bitmap bmp = Bitmap.createBitmap(bp.getWidth(), bp.getHeight(), Config.ARGB_8888);
        cm.set(new float[]{2.0f, 0.0f, 0.0f, 0.0f, 50.0f, 0.0f, 2.0f, 0.0f, 0.0f, 50.0f, 0.0f, 0.0f, 2.0f, 0.0f, 50.0f, 0.0f, 0.0f, 0.0f, 2.0f, 0.0f});
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        new Canvas(bmp).drawBitmap(bp, 0.0f, 0.0f, paint);
        return bmp;
    }

    public Bitmap zoomBitmap(Bitmap target) {
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(384.0f / ((float) width), ((float) ((height * PRINT_WIDTH) / width)) / ((float) height));
        return Bitmap.createBitmap(target, 0, 0, width, height, matrix, true);
    }

    public Bitmap drawBitFirst() {
        this.nBitmapFirst = Bitmap.createBitmap(PRINT_WIDTH, 85, Config.RGB_565);
        Canvas canvas = new Canvas(this.nBitmapFirst);
        canvas.drawColor(-1);
        Paint p = new Paint();
        p.setColor(DefaultRenderer.BACKGROUND_COLOR);
        p.setTextSize(20.0f);
        canvas.drawText(this.mContext.getResources().getString(C0136R.string.print_launch), 0.0f, 20.0f, p);
        canvas.drawLine(0.0f, 40.0f, 384.0f, 40.0f, p);
        canvas.drawText(this.mContext.getResources().getString(C0136R.string.print_automobile_fault_diagnosis_test_report), 20.0f, 70.0f, p);
        canvas.drawLine(0.0f, 80.0f, 384.0f, 80.0f, p);
        return this.nBitmapFirst;
    }

    public Bitmap drawBitSecond(String s) {
        if (this.company_address == null) {
            this.company_address = XmlPullParser.NO_NAMESPACE;
        }
        if (this.company_phone == null) {
            this.company_phone = XmlPullParser.NO_NAMESPACE;
        }
        if (this.license_plate_number == null) {
            this.license_plate_number = XmlPullParser.NO_NAMESPACE;
        }
        StringBuffer ss = new StringBuffer();
        ss.append(new StringBuilder(String.valueOf(this.mContext.getResources().getString(C0136R.string.print_test_time))).append(this.str).append(SpecilApiUtil.LINE_SEP).toString());
        ss.append(new StringBuilder(String.valueOf(this.mContext.getResources().getString(C0136R.string.print_serial_number))).append(this.serialNum).append(SpecilApiUtil.LINE_SEP).toString());
        ss.append(new StringBuilder(String.valueOf(this.mContext.getResources().getString(C0136R.string.print_test_company_address))).append(this.company_address).append(SpecilApiUtil.LINE_SEP).toString());
        ss.append(new StringBuilder(String.valueOf(this.mContext.getResources().getString(C0136R.string.print_test_company_phone))).append(this.company_phone).append(SpecilApiUtil.LINE_SEP).toString());
        ss.append(new StringBuilder(String.valueOf(this.mContext.getResources().getString(C0136R.string.print_test_license_plate_number))).append(this.license_plate_number).append(SpecilApiUtil.LINE_SEP).toString());
        ss.append(s);
        this.textPaint.setColor(DefaultRenderer.BACKGROUND_COLOR);
        this.textPaint.setTextSize(20.0f);
        StaticLayout layout = new StaticLayout(ss, this.textPaint, PRINT_WIDTH, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        this.nBitmapSecond = Bitmap.createBitmap(PRINT_WIDTH, layout.getHeight(), Config.RGB_565);
        Canvas canvas = new Canvas(this.nBitmapSecond);
        canvas.drawColor(-1);
        layout.draw(canvas);
        return this.nBitmapSecond;
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

    public void resultToast(int result) {
        new Handler(Looper.getMainLooper()).post(new C07551(result));
    }
}

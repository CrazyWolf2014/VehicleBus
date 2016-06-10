package com.ifoer.expedition.BluetoothChat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.cnlaunch.x431pro.common.Constants;
import com.iflytek.speech.SpeechConfig;
import com.ifoer.entity.AnalysisData;
import com.ifoer.entity.Constant;
import com.ifoer.expedition.BluetoothOrder.Analysis;
import com.ifoer.expedition.BluetoothOrder.ByteHexHelper;
import com.ifoer.expedition.BluetoothOrder.DpuOrderUtils;
import com.ifoer.expedition.BluetoothOrder.OrderMontage;
import com.ifoer.serialport.SerialPort;
import com.ifoer.util.Bridge;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.WriteByteData;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

@SuppressLint({"NewApi"})
public class BluetoothChatService {
    private static final boolean f1233D = true;
    public static long DELAY = 0;
    public static boolean HardFlag = false;
    public static final int INPUTSTREAM = 101010;
    public static final UUID MY_UUID;
    public static final int OUTPUTSTEAM = 101011;
    public static final int STATE_CONNECTED = 3;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_NONE = 0;
    private static final String TAG = "BluetoothChatService";
    public static Bridge bridge;
    private static CheckConnectorTimeTask checkConnectorTimeTask;
    public static Context context;
    public static int flag;
    public static InputStream inputStream;
    public static boolean isExistDiag;
    public static boolean isTimeOut;
    public static int length;
    public static ConnectedThread mConnectedThread;
    public static Handler mHandler;
    public static OutputStream outputStream;
    public static String readData;
    public static boolean readEnd;
    public static SerialPort remoteDevice;
    public static byte[] send;
    public static DataInputStream socketReader;
    public static DataOutputStream socketWriter;
    public static Timer timeCheck;
    public static Timer timer;
    public static int totalLength;
    private byte[] buffer;
    private ConnectAsynTask mConnectAsyntask;
    private ConnectThread mConnectThread;
    public boolean mIsFirtConnect;
    private int mState;

    private class ConnectThread extends Thread {
        private final boolean isLoadLib;
        private final SerialPort mmDevice;

        public ConnectThread(SerialPort device, boolean isLoadLib) {
            this.mmDevice = device;
            this.isLoadLib = isLoadLib;
            BluetoothChatService.this.mConnectAsyntask = new ConnectAsynTask(this.mmDevice, BluetoothChatService.this, this.isLoadLib);
            BluetoothChatService.this.setState(BluetoothChatService.STATE_CONNECTED);
        }

        public void run() {
            setName("ConnectThread");
            BluetoothChatService.this.mConnectAsyntask.execute(new Object[BluetoothChatService.STATE_NONE]);
            synchronized (BluetoothChatService.this) {
                BluetoothChatService.this.mConnectThread = null;
            }
        }

        public void cancel() {
            this.mmDevice.close();
        }
    }

    public class ConnectedThread extends Thread {
        private InputStream mmInStream;
        private OutputStream mmOutStream;
        private SerialPort mmSerial;

        public ConnectedThread(SerialPort serial) {
            InputStream tmpIn = serial.getInputStream();
            OutputStream tmpOut = serial.getOutputStream();
            this.mmInStream = tmpIn;
            this.mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[Flags.FLAG5];
            byte[] fullBuffer = new byte[BluetoothChatService.STATE_NONE];
            while (true) {
                try {
                    byte[] ontBuffer = BluetoothChatService.cutOutByte(buffer, this.mmInStream.read(buffer));
                    if (fullBuffer == null || fullBuffer.length <= 0) {
                        fullBuffer = ontBuffer;
                    } else {
                        fullBuffer = BluetoothChatService.getMergeBytes(fullBuffer, ontBuffer);
                    }
                    String order = ByteHexHelper.bytesToHexString(fullBuffer);
                    int sites = order.indexOf("55aaf8f0");
                    if (order.length() > 8 && sites > 0) {
                        order = order.substring(sites);
                        fullBuffer = ByteHexHelper.hexStringToBytes(order);
                    }
                    BluetoothChatService.flag = order.length() / BluetoothChatService.STATE_CONNECTING;
                    if (BluetoothChatService.flag >= BluetoothChatService.STATE_LISTEN) {
                        if (!order.substring(BluetoothChatService.STATE_NONE, BluetoothChatService.STATE_CONNECTING).equalsIgnoreCase("55")) {
                            BluetoothChatService.flag = BluetoothChatService.STATE_NONE;
                            buffer = new byte[Flags.FLAG5];
                        } else if (BluetoothChatService.flag >= BluetoothChatService.STATE_CONNECTING && order.substring(BluetoothChatService.STATE_CONNECTING, 4).equalsIgnoreCase("aa") && BluetoothChatService.flag >= 6) {
                            BluetoothChatService.length = ByteHexHelper.intPackLength(order.substring(8, 12));
                            if (BluetoothChatService.totalLength == 7) {
                                BluetoothChatService.totalLength += BluetoothChatService.length;
                            }
                            if (BluetoothChatService.flag >= BluetoothChatService.totalLength) {
                                BluetoothChatService.readData = ByteHexHelper.bytesToHexString(fullBuffer).substring(BluetoothChatService.STATE_NONE, BluetoothChatService.totalLength * BluetoothChatService.STATE_CONNECTING);
                                BluetoothChatService.totalLength = 7;
                                BluetoothChatService.length = BluetoothChatService.STATE_NONE;
                                BluetoothChatService.flag = BluetoothChatService.STATE_NONE;
                                order = XmlPullParser.NO_NAMESPACE;
                                fullBuffer = new byte[BluetoothChatService.STATE_NONE];
                                BluetoothChatService.readEnd = BluetoothChatService.f1233D;
                                String eRRCode = BluetoothChatService.readData.substring(18, 20);
                                if (!(eRRCode.equals("00") || eRRCode.equals("01") || eRRCode.equals("02") || eRRCode.equals("03") || eRRCode.equals("04") || eRRCode.equals("=05") || eRRCode.equals("06") || eRRCode.equals("07") || !eRRCode.equals(XmlPullParser.NO_NAMESPACE))) {
                                }
                                if (BluetoothChatService.timer != null) {
                                    BluetoothChatService.timer.cancel();
                                }
                                if (BluetoothChatService.bridge != null) {
                                    BluetoothChatService.bridge.putData();
                                }
                            }
                        }
                    }
                    buffer = new byte[Flags.FLAG5];
                } catch (Exception e) {
                    BluetoothChatService.timer.cancel();
                    e.printStackTrace();
                    terminate();
                    return;
                }
            }
        }

        public void terminate() {
            interrupt();
            cancel();
        }

        public void write(byte[] buffer) {
            try {
                BluetoothChatService.send = null;
                BluetoothChatService.send = buffer;
                this.mmOutStream.write(buffer);
            } catch (IOException e) {
            }
        }

        public void cancel() {
            try {
                if (this.mmSerial != null) {
                    this.mmSerial.close();
                    this.mmSerial = null;
                }
                if (this.mmInStream != null) {
                    this.mmInStream.close();
                    this.mmInStream = null;
                }
                if (this.mmOutStream != null) {
                    this.mmOutStream.close();
                    this.mmOutStream = null;
                }
            } catch (IOException e) {
            }
        }
    }

    class MyTimerTask extends TimerTask {
        MyTimerTask() {
        }

        public void run() {
            BluetoothChatService.readEnd = BluetoothChatService.f1233D;
            BluetoothChatService.totalLength = 7;
            BluetoothChatService.length = BluetoothChatService.STATE_NONE;
            BluetoothChatService.flag = BluetoothChatService.STATE_NONE;
            BluetoothChatService.this.buffer = new byte[Flags.FLAG5];
        }
    }

    class MyTimerTasks extends TimerTask {
        Bridge bridge;

        public MyTimerTasks(Bridge bridge) {
            this.bridge = bridge;
        }

        public void run() {
            BluetoothChatService.readEnd = BluetoothChatService.f1233D;
            BluetoothChatService.totalLength = 7;
            BluetoothChatService.length = BluetoothChatService.STATE_NONE;
            BluetoothChatService.flag = BluetoothChatService.STATE_NONE;
            BluetoothChatService.this.buffer = new byte[Flags.FLAG5];
            this.bridge.getData();
        }
    }

    static {
        checkConnectorTimeTask = null;
        MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        mHandler = null;
        HardFlag = false;
        timer = new Timer();
        timeCheck = new Timer();
        readData = XmlPullParser.NO_NAMESPACE;
        DELAY = 8000;
        readEnd = false;
        flag = STATE_NONE;
        totalLength = 7;
        length = STATE_NONE;
        isTimeOut = false;
    }

    public BluetoothChatService(Context context, Handler handler, boolean isDiga) {
        this.buffer = new byte[Flags.FLAG5];
        this.mIsFirtConnect = false;
        context = context;
        this.mState = STATE_NONE;
        mHandler = handler;
        HardFlag = MySharedPreferences.getBooleanValue(context, Constants.hasODB, false);
        checkConnectorTimeTask = new CheckConnectorTimeTask();
        if (isDiga) {
            timeCheck.schedule(checkConnectorTimeTask, 2000, 2000);
        }
    }

    public static void setIsexistDiag(boolean isExist) {
        isExistDiag = isExist;
    }

    public static boolean IsexistDiag() {
        return isExistDiag;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static void setHandler(Handler handler) {
        mHandler = handler;
    }

    public synchronized void setState(int state) {
        this.mState = state;
        mHandler.obtainMessage(STATE_LISTEN, state, -1).sendToTarget();
    }

    public synchronized int getState() {
        return this.mState;
    }

    public synchronized void start() {
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        setState(STATE_NONE);
    }

    public void connect(SerialPort device, boolean type, boolean isLoadLib) {
        this.mIsFirtConnect = type;
        connect(device, isLoadLib);
    }

    public synchronized void connect(SerialPort device, boolean isLoadLib) {
        if (this.mState == STATE_CONNECTING && this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }
        this.mConnectThread = new ConnectThread(device, isLoadLib);
        this.mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    public synchronized void connected(SerialPort device, boolean isLoadLib) {
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        mConnectedThread = new ConnectedThread(device);
        mConnectedThread.start();
        setState(STATE_CONNECTED);
        if (getState() == STATE_CONNECTED || getState() == STATE_CONNECTING) {
            if (isLoadLib) {
                mHandler.sendMessage(mHandler.obtainMessage(4));
            }
            setState(STATE_CONNECTED);
        }
    }

    public synchronized void stop() {
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        setState(STATE_NONE);
    }

    public void write(byte[] out) {
        synchronized (this) {
            if (this.mState != STATE_CONNECTED) {
                return;
            }
            ConnectedThread r = mConnectedThread;
        }
    }

    public void connectionFailed() {
        setState(STATE_LISTEN);
    }

    public void firstConnectionFailed() {
        setState(STATE_LISTEN);
        mHandler.sendMessage(mHandler.obtainMessage(8));
    }

    public synchronized String writeBack(byte[] buffer) {
        return XmlPullParser.NO_NAMESPACE;
    }

    public boolean download2505() {
        byte[] sendOrder = OrderMontage.resetConnector2505();
        for (int flag = STATE_NONE; flag < STATE_CONNECTED; flag += STATE_LISTEN) {
            if (Analysis.analysis2505(Analysis.analysis(sendOrder, ByteHexHelper.hexStringToBytes(writeBack(sendOrder)))).booleanValue()) {
                return f1233D;
            }
        }
        return false;
    }

    public boolean download2504() {
        byte[] sendOrder = OrderMontage.disconnected2504();
        for (int flag = STATE_NONE; flag < STATE_CONNECTED; flag += STATE_LISTEN) {
            if (Analysis.analysis2504(Analysis.analysis(sendOrder, ByteHexHelper.hexStringToBytes(writeBack(sendOrder)))).booleanValue()) {
                return f1233D;
            }
        }
        return false;
    }

    public boolean download2111() {
        byte[] sendOrder = OrderMontage.download2111();
        for (int flag = STATE_NONE; flag < STATE_CONNECTED; flag += STATE_LISTEN) {
            if (Analysis.analysis2111(Analysis.analysis(sendOrder, ByteHexHelper.hexStringToBytes(writeBack(sendOrder)))).booleanValue()) {
                return f1233D;
            }
        }
        return false;
    }

    public boolean provingPassword2110() {
        byte[] sendOrder = OrderMontage.resumePw2110("000000");
        for (int flag = STATE_NONE; flag < STATE_CONNECTED; flag += STATE_LISTEN) {
            if (Analysis.analysis2110(Analysis.analysis(sendOrder, ByteHexHelper.hexStringToBytes(writeBack(sendOrder)))).booleanValue()) {
                return f1233D;
            }
        }
        return false;
    }

    public String requestConnect2114() {
        String succeed = XmlPullParser.NO_NAMESPACE;
        String receiveOrderStr = XmlPullParser.NO_NAMESPACE;
        byte[] sendOrder = OrderMontage.requestConnect2114();
        Constant.mChatService.setWaitTime(1000);
        if (sendOrder.length > 0) {
            Runnable dynamic = new WriteByteData(new Bridge(), sendOrder);
            readData = XmlPullParser.NO_NAMESPACE;
            Thread t = new Thread(dynamic);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            receiveOrderStr = readData;
            System.out.println("backOrder==" + receiveOrderStr);
        }
        return Analysis.analysis2114(Analysis.analysis(sendOrder, ByteHexHelper.hexStringToBytes(receiveOrderStr)));
    }

    public String requestConnect2502() {
        String succeed = XmlPullParser.NO_NAMESPACE;
        String receiveOrderStr = XmlPullParser.NO_NAMESPACE;
        byte[] sendOrder = OrderMontage.requestConnect2502();
        Constant.mChatService.setWaitTime(MyHttpException.ERROR_SERVER);
        if (sendOrder.length > 0) {
            Runnable dynamic = new WriteByteData(new Bridge(), sendOrder);
            readData = XmlPullParser.NO_NAMESPACE;
            Thread t = new Thread(dynamic);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            receiveOrderStr = readData;
        }
        return Analysis.analysis2502(Analysis.analysis(sendOrder, ByteHexHelper.hexStringToBytes(receiveOrderStr)));
    }

    public boolean passwordVerify2503(String verify) {
        String receiveOrderStr = XmlPullParser.NO_NAMESPACE;
        byte[] sendOrder = OrderMontage.securityCheck2503(verify);
        Constant.mChatService.setWaitTime(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
        if (sendOrder.length > 0) {
            Runnable dynamic = new WriteByteData(new Bridge(), sendOrder);
            readData = XmlPullParser.NO_NAMESPACE;
            Thread t = new Thread(dynamic);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            receiveOrderStr = readData;
        }
        return Analysis.analysis2503(Analysis.analysis(sendOrder, ByteHexHelper.hexStringToBytes(receiveOrderStr))).booleanValue();
    }

    public boolean smartBox2109(int model) {
        byte[] sendOrder = OrderMontage.transferDPUMode2109(model);
        int flag = STATE_NONE;
        String backOrder = XmlPullParser.NO_NAMESPACE;
        while (flag < STATE_CONNECTED) {
            if (sendOrder.length > 0) {
                Runnable dynamic = new WriteByteData(new Bridge(), sendOrder);
                readData = XmlPullParser.NO_NAMESPACE;
                Thread t = new Thread(dynamic);
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Analysis.analysis2109(Analysis.analysis(sendOrder, ByteHexHelper.hexStringToBytes(readData))).booleanValue()) {
                    return f1233D;
                }
                flag += STATE_LISTEN;
            }
        }
        return false;
    }

    public boolean smartBox210900() {
        String orderStr = writeBack(OrderMontage.transferDPUMode2109(STATE_NONE));
        int iReceiveBufferLength = ByteHexHelper.intPackLength(orderStr.substring(8, 12)) - 1;
        byte[] pReceiveBuffer = new byte[iReceiveBufferLength];
        byte[] allOrder = ByteHexHelper.hexStringToBytes(orderStr);
        int flag = STATE_NONE;
        for (int i = 7; i < iReceiveBufferLength + 7; i += STATE_LISTEN) {
            pReceiveBuffer[flag] = allOrder[i];
            Log.i("smartBox210900", "pReceiveBuffer[" + flag + "]" + pReceiveBuffer[flag]);
            flag += STATE_LISTEN;
        }
        if (ByteHexHelper.byteToHexString(pReceiveBuffer[STATE_CONNECTING]).equals("01") && ByteHexHelper.byteToHexString(pReceiveBuffer[STATE_CONNECTED]).equals("00")) {
            return f1233D;
        }
        return false;
    }

    public boolean reset2407() {
        return f1233D;
    }

    public boolean reset2505() {
        byte[] sendOrder = OrderMontage.resetConnector2505();
        String backOrder = XmlPullParser.NO_NAMESPACE;
        BluetoothChatService mChatService = Constant.mChatService;
        if (sendOrder.length > 0) {
            Runnable dynamic = new WriteByteData(new Bridge(), sendOrder);
            readData = XmlPullParser.NO_NAMESPACE;
            Thread t = new Thread(dynamic);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            backOrder = readData;
        }
        return false;
    }

    public String readDPUVersionInfo2105() {
        String downloadBinVer = XmlPullParser.NO_NAMESPACE;
        String backOrder = XmlPullParser.NO_NAMESPACE;
        byte[] sendOrder = OrderMontage.DPUVer2105();
        for (int flag = STATE_NONE; flag < STATE_CONNECTED; flag += STATE_LISTEN) {
            Constant.mChatService.setWaitTime(SpeechConfig.Rate8K);
            if (sendOrder.length > 0) {
                Runnable dynamic = new WriteByteData(new Bridge(), sendOrder);
                readData = XmlPullParser.NO_NAMESPACE;
                Thread t = new Thread(dynamic);
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                backOrder = readData;
            }
            AnalysisData analysisData = Analysis.analysis(sendOrder, ByteHexHelper.hexStringToBytes(backOrder));
            if (analysisData.getState().booleanValue()) {
                return Analysis.analysis2105(analysisData);
            }
        }
        return downloadBinVer;
    }

    public String currentState2114() {
        byte[] sendOrder = OrderMontage.currentStatus2114();
        String backOrder = XmlPullParser.NO_NAMESPACE;
        String runnningmode = XmlPullParser.NO_NAMESPACE;
        for (int flag = STATE_NONE; flag < STATE_CONNECTED; flag += STATE_LISTEN) {
            Constant.mChatService.setWaitTime(SpeechConfig.Rate8K);
            if (sendOrder.length > 0) {
                Runnable dynamic = new WriteByteData(new Bridge(), sendOrder);
                readData = XmlPullParser.NO_NAMESPACE;
                Thread t = new Thread(dynamic);
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                backOrder = readData;
            }
            AnalysisData analysisData = Analysis.analysis(sendOrder, ByteHexHelper.hexStringToBytes(backOrder));
            if (analysisData.getState().booleanValue()) {
                return Analysis.analysis2114(analysisData);
            }
        }
        return runnningmode;
    }

    public boolean SwitchtoBootMode() {
        byte[] sendOrder = OrderMontage.updateFirmware2407();
        String backOrder = XmlPullParser.NO_NAMESPACE;
        for (int flag = STATE_NONE; flag < STATE_CONNECTED; flag += STATE_LISTEN) {
            Constant.mChatService.setWaitTime(SpeechConfig.Rate8K);
            if (sendOrder.length > 0) {
                Runnable dynamic = new WriteByteData(new Bridge(), sendOrder);
                readData = XmlPullParser.NO_NAMESPACE;
                Thread t = new Thread(dynamic);
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                backOrder = readData;
            }
            if (Analysis.analysis2407(Analysis.analysis(sendOrder, ByteHexHelper.hexStringToBytes(backOrder))).booleanValue()) {
                return f1233D;
            }
        }
        return false;
    }

    public boolean SendFileNameAndLength(File donwloadbin) {
        byte[] sendOrder = OrderMontage.sendFileNameAndLength2402(donwloadbin);
        String backOrder = XmlPullParser.NO_NAMESPACE;
        Constant.mChatService.setWaitTime(SpeechConfig.Rate8K);
        if (sendOrder.length > 0) {
            Runnable dynamic = new WriteByteData(new Bridge(), sendOrder);
            readData = XmlPullParser.NO_NAMESPACE;
            Thread t = new Thread(dynamic);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            backOrder = readData;
        }
        return Analysis.analysis2402(Analysis.analysis(sendOrder, ByteHexHelper.hexStringToBytes(backOrder))).booleanValue();
    }

    public boolean sendUpdateFileMd5(String md5) {
        byte[] sendOrder = OrderMontage.sendUpdateFileMd52404(md5);
        String backOrder = XmlPullParser.NO_NAMESPACE;
        for (int flag = STATE_NONE; flag < STATE_CONNECTED; flag += STATE_LISTEN) {
            Constant.mChatService.setWaitTime(SpeechConfig.Rate8K);
            if (sendOrder.length > 0) {
                Runnable dynamic = new WriteByteData(new Bridge(), sendOrder);
                readData = XmlPullParser.NO_NAMESPACE;
                Thread t = new Thread(dynamic);
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                backOrder = readData;
            }
            if (Analysis.analysis2404(Analysis.analysis(sendOrder, ByteHexHelper.hexStringToBytes(backOrder))).booleanValue()) {
                return f1233D;
            }
        }
        return false;
    }

    public boolean ValidateAllFilesMd5(HashMap<String, String> md5info) {
        String backOrder = XmlPullParser.NO_NAMESPACE;
        byte[] sendOrder = OrderMontage.ValidateAllFilesMd52408();
        while (flag < STATE_CONNECTED) {
            Constant.mChatService.setWaitTime(SpeechConfig.Rate8K);
            if (sendOrder.length > 0) {
                Runnable dynamic = new WriteByteData(new Bridge(), sendOrder);
                readData = XmlPullParser.NO_NAMESPACE;
                Thread thread = new Thread(dynamic);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                backOrder = readData;
            }
            byte[] receiveOrder = ByteHexHelper.hexStringToBytes(backOrder);
            if (Analysis.analysis(sendOrder, receiveOrder).getState().booleanValue()) {
                if (DpuOrderUtils.filterOutCmdParameters(receiveOrder)[STATE_NONE] == null) {
                    byte[] temp = DpuOrderUtils.filterOutCmdParameters(receiveOrder);
                    int fileNum = (temp[STATE_NONE] << 8) | temp[STATE_LISTEN];
                    int offset = STATE_CONNECTING;
                    HashMap<String, String> md5info1 = new HashMap();
                    for (int i = STATE_NONE; i < fileNum; i += STATE_LISTEN) {
                        int j;
                        int fileNameLen = (temp[offset] << 8) | temp[offset + STATE_LISTEN];
                        byte[] filename_bytes = new byte[(fileNameLen - 1)];
                        for (j = STATE_NONE; j < fileNameLen - 1; j += STATE_LISTEN) {
                            filename_bytes[j] = temp[(offset + STATE_CONNECTING) + j];
                        }
                        byte[] md5bytes = new byte[32];
                        for (j = STATE_NONE; j < 32; j += STATE_LISTEN) {
                            md5bytes[j] = temp[((offset + STATE_CONNECTING) + fileNameLen) + j];
                        }
                        md5info.put(new String(filename_bytes), new String(md5bytes));
                        offset += (fileNameLen + STATE_CONNECTING) + 32;
                    }
                    for (Entry<String, String> e2 : md5info1.entrySet()) {
                        if (!((String) md5info.get((String) e2.getKey())).equals((String) e2.getValue())) {
                            return false;
                        }
                    }
                }
                return f1233D;
            }
            flag += STATE_LISTEN;
        }
        return false;
    }

    public boolean ValidateUpdateFinished() {
        byte[] sendOrder = OrderMontage.ValidateUpdateFinished2405();
        String backOrder = XmlPullParser.NO_NAMESPACE;
        for (int flag = STATE_NONE; flag < STATE_CONNECTED; flag += STATE_LISTEN) {
            Constant.mChatService.setWaitTime(SpeechConfig.Rate8K);
            if (sendOrder.length > 0) {
                Runnable dynamic = new WriteByteData(new Bridge(), sendOrder);
                readData = XmlPullParser.NO_NAMESPACE;
                Thread t = new Thread(dynamic);
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                backOrder = readData;
            }
            if (Analysis.analysis2405(Analysis.analysis(sendOrder, ByteHexHelper.hexStringToBytes(backOrder))).booleanValue()) {
                return f1233D;
            }
        }
        return false;
    }

    public void setWaitTime(int outtime) {
        DELAY = (long) outtime;
    }

    public static byte[] getMergeBytes(byte[] pByteA, byte[] pByteB) {
        int i;
        int aCount = pByteA.length;
        int bCount = pByteB.length;
        byte[] b = new byte[(aCount + bCount)];
        for (i = STATE_NONE; i < aCount; i += STATE_LISTEN) {
            b[i] = pByteA[i];
        }
        for (i = STATE_NONE; i < bCount; i += STATE_LISTEN) {
            b[aCount + i] = pByteB[i];
        }
        return b;
    }

    public static byte[] cutOutByte(byte[] b, int j) {
        if (b.length == 0 || j == 0) {
            return null;
        }
        byte[] bjq = new byte[j];
        for (int i = STATE_NONE; i < j; i += STATE_LISTEN) {
            bjq[i] = b[i];
        }
        return bjq;
    }
}

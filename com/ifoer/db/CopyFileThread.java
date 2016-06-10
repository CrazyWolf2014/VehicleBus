package com.ifoer.db;

import CRP.serialport.SerialPortManager;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.ifoer.entity.Constant;
import com.ifoer.expedition.BluetoothChat.BluetoothChatService;
import com.ifoer.expedition.BluetoothOrder.ByteHexHelper;
import com.ifoer.expedition.client.DbscarUpgradeUtil;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.expeditionphone.WelcomeActivity;
import com.ifoer.util.ApkDownLoad;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SrorageManager;
import java.io.File;
import java.io.FileInputStream;
import org.apache.http.util.EncodingUtils;
import org.xmlpull.v1.XmlPullParser;

public class CopyFileThread extends Thread {
    public static Handler mHandler;
    private boolean isSanmu;
    private Context mContexts;

    /* renamed from: com.ifoer.db.CopyFileThread.1 */
    class C03431 implements OnClickListener {
        C03431() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            CopyFileThread.this.openSerial();
        }
    }

    /* renamed from: com.ifoer.db.CopyFileThread.2 */
    class C03442 implements OnClickListener {
        C03442() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            CopyFileThread.mHandler.sendEmptyMessage(ApkDownLoad.DOWNlOAD_NO_SD);
        }
    }

    public CopyFileThread(Context context, Handler handler, boolean isSanmu) {
        this.mContexts = context;
        mHandler = handler;
        this.isSanmu = isSanmu;
    }

    public void run() {
        super.run();
        String path = XmlPullParser.NO_NAMESPACE;
        String path2 = XmlPullParser.NO_NAMESPACE;
        if (this.isSanmu) {
            path = "/storage/sdcard1/cnlaunch";
            path2 = "/storage/sdcard0";
        } else {
            path = "/mnt/extsd/cnlaunch";
            path2 = "/mnt/sdcard";
        }
        if (SrorageManager.cutGeneralFile(path, path2)) {
            mHandler.obtainMessage(4).sendToTarget();
            if (MainActivity.database == null) {
                MainActivity.database = DBDao.getInstance(this.mContexts).getConnection();
            }
            checkBinFile();
            return;
        }
        mHandler.obtainMessage(3).sendToTarget();
        WelcomeActivity.thread4 = true;
        if (WelcomeActivity.thread1 && WelcomeActivity.thread2 && WelcomeActivity.thread3 && WelcomeActivity.thread4) {
            mHandler.obtainMessage(MainActivity.MESSAGE_SHOW_MENU).sendToTarget();
        }
    }

    private void checkBinFile() {
        String path = Constant.DOWBIN_UPGRADE_PATH;
        if (!new File(path).exists()) {
            mHandler.obtainMessage(7).sendToTarget();
        } else if (DbscarUpgradeUtil.inspectIntegrity(path).booleanValue()) {
            String[] v = readFileSdcard(new StringBuilder(String.valueOf(path)).append("DOWNLOAD.ini").toString()).split("=");
            String oldVersion = MySharedPreferences.getStringValue(this.mContexts, Constants.DOWNLOADBIN);
            DBDao.getInstance(this.mContexts).setDownloadVersion(MainActivity.database, ByteHexHelper.replaceBlank(v[1]));
            String v2 = ByteHexHelper.replaceBlank(v[1]);
            if (Double.valueOf(v2).doubleValue() > Double.valueOf(oldVersion).doubleValue()) {
                firmUpdate();
            } else if (SrorageManager.deleteGeneralFile(Constant.DOWBIN_UPGRADE_PATH)) {
                mHandler.obtainMessage(7).sendToTarget();
            }
        }
    }

    public String readFileSdcard(String fileName) {
        String res = XmlPullParser.NO_NAMESPACE;
        try {
            FileInputStream fin = new FileInputStream(fileName);
            byte[] buffer = new byte[fin.available()];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, AsyncHttpResponseHandler.DEFAULT_CHARSET);
            fin.close();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
    }

    public boolean checkConnector() {
        if (!MySharedPreferences.getBooleanValue(this.mContexts, Constants.isSanMu, false)) {
            boolean HardFlag = MySharedPreferences.getBooleanValue(this.mContexts, Constants.hasODB, false);
            if (!HardFlag) {
                return false;
            }
            if (com.example.gpiomanager.MainActivity.ODB_ISOK(HardFlag) != 1) {
                com.example.gpiomanager.MainActivity.setHigh_ON();
                SystemClock.sleep(500);
            }
        } else if (com.example.gpiomanager.MainActivity.SanmuGetV(this.mContexts) == 0) {
            com.example.gpiomanager.MainActivity.SanMusetHigh(this.mContexts);
            Log.i("checkConnector", "\u4e09\u6728\u7f6e\u9ad8\u5b8c\u6bd5 ");
        }
        return true;
    }

    public void checkConectiorBefore(Context context, String title, String message) {
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getText(C0136R.string.Ok), new C03431());
        builder.setNegativeButton(context.getResources().getText(C0136R.string.no), new C03442());
        builder.create().show();
    }

    public void firmUpdate() {
        if (checkConnector()) {
            Log.i("checkConnector", "\u68c0\u6d4b\u63a5\u5934\u5b8c\u6bd5 ");
            openSerial();
            return;
        }
        checkConectiorBefore(this.mContexts, this.mContexts.getString(C0136R.string.initializeTilte), this.mContexts.getString(C0136R.string.check_connector));
    }

    private void openSerial() {
        MainActivity.serialPort = new SerialPortManager().getSerialPort();
        if (MainActivity.serialPort != null) {
            if (Constant.mChatService == null) {
                Constant.mChatService = new BluetoothChatService(this.mContexts, mHandler, false);
            }
            if (BluetoothChatService.mConnectedThread != null) {
                BluetoothChatService.mConnectedThread = null;
            }
            Constant.mChatService.connect(MainActivity.serialPort, false);
            mHandler.obtainMessage(6).sendToTarget();
            return;
        }
        Log.i("nxy", "\u6253\u5f00\u4e32\u53e3\u5931\u8d25");
    }
}

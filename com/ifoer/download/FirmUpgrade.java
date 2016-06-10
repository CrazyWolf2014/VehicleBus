package com.ifoer.download;

import CRP.serialport.SerialPortManager;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.SystemClock;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.example.gpiomanager.MainActivity;
import com.ifoer.entity.Constant;
import com.ifoer.expedition.BluetoothChat.BluetoothChatService;
import com.ifoer.util.ApkDownLoad;
import com.ifoer.util.MySharedPreferences;

public class FirmUpgrade {
    private Context context;
    private Handler mHandler;

    /* renamed from: com.ifoer.download.FirmUpgrade.1 */
    class C03461 implements OnClickListener {
        C03461() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            if (FirmUpgrade.this.openSerial()) {
                FirmUpgrade.this.showUpdateDig();
            }
        }
    }

    /* renamed from: com.ifoer.download.FirmUpgrade.2 */
    class C03472 implements OnClickListener {
        C03472() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            FirmUpgrade.this.mHandler.sendEmptyMessage(ApkDownLoad.DOWNLOAD_FINISHED);
        }
    }

    /* renamed from: com.ifoer.download.FirmUpgrade.3 */
    class C03483 implements OnClickListener {
        C03483() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            new DownloadBinUpdate(FirmUpgrade.this.context, Constant.mChatService, FirmUpgrade.this.mHandler, true).checkUpdateAsync();
        }
    }

    /* renamed from: com.ifoer.download.FirmUpgrade.4 */
    class C03494 implements OnClickListener {
        C03494() {
        }

        public void onClick(DialogInterface dialog, int arg1) {
            dialog.dismiss();
            FirmUpgrade.this.mHandler.sendEmptyMessage(ApkDownLoad.DOWNlOAD_NO_SD);
        }
    }

    public FirmUpgrade(Context context, Handler handler) {
        this.context = context;
        this.mHandler = handler;
    }

    public void upgrade() {
        if (!checkConnector()) {
            checkConectiorBefore(this.context, this.context.getString(C0136R.string.initializeTilte), this.context.getString(C0136R.string.check_connector));
        } else if (openSerial()) {
            showUpdateDig();
        }
    }

    public boolean checkConnector() {
        if (!MySharedPreferences.getBooleanValue(this.context, Constants.isSanMu, false)) {
            boolean HardFlag = MySharedPreferences.getBooleanValue(this.context, Constants.hasODB, false);
            if (!HardFlag) {
                return false;
            }
            if (MainActivity.ODB_ISOK(HardFlag) != 1) {
                MainActivity.setHigh_ON();
                SystemClock.sleep(500);
            }
        } else if (MainActivity.SanmuGetV(this.context) == 0) {
            MainActivity.SanMusetHigh(this.context);
        }
        return true;
    }

    public void checkConectiorBefore(Context context, String title, String message) {
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getText(C0136R.string.Ok), new C03461());
        builder.setNegativeButton(context.getResources().getText(C0136R.string.no), new C03472());
        builder.create().show();
    }

    private boolean openSerial() {
        com.ifoer.expeditionphone.MainActivity.serialPort = new SerialPortManager().getSerialPort();
        if (com.ifoer.expeditionphone.MainActivity.serialPort == null) {
            return false;
        }
        if (Constant.mChatService == null) {
            Constant.mChatService = new BluetoothChatService(this.context, this.mHandler, false);
        }
        if (BluetoothChatService.mConnectedThread != null) {
            BluetoothChatService.mConnectedThread = null;
        }
        Constant.mChatService.connect(com.ifoer.expeditionphone.MainActivity.serialPort, false);
        return true;
    }

    private void showUpdateDig() {
        Builder builder = new Builder(this.context);
        builder.setMessage(this.context.getResources().getString(C0136R.string.FirmwareUpgradeMessage));
        builder.setCancelable(false);
        builder.setPositiveButton(this.context.getResources().getString(C0136R.string.enter), new C03483());
        builder.setNegativeButton(this.context.getResources().getString(C0136R.string.cancel), new C03494());
        builder.create().show();
    }
}

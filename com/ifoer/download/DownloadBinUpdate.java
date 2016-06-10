package com.ifoer.download;

import CRP.utils.CRPTools;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.iflytek.speech.SpeechConfig;
import com.ifoer.db.CopyFileThread;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.expedition.BluetoothChat.BluetoothChatService;
import com.ifoer.expedition.BluetoothOrder.Analysis;
import com.ifoer.expedition.BluetoothOrder.ByteHexHelper;
import com.ifoer.expedition.BluetoothOrder.DpuOrderUtils;
import com.ifoer.expedition.BluetoothOrder.OrderMontage;
import com.ifoer.expedition.BluetoothOrder.StatisticHelper;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.expeditionphone.ProgressInfo;
import com.ifoer.mine.Contact;
import com.ifoer.util.ApkDownLoad;
import com.ifoer.util.Bridge;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.WriteByteData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.util.EncodingUtils;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class DownloadBinUpdate {
    private static final String TAG = "DownloadBinUpdate";
    private final int PKG_SIZE;
    private ProgressDialog downloadDialog;
    Handler downloadHandler;
    private boolean isKeyToUpgade;
    private BluetoothChatService mChatService;
    private Context mContext;
    private Handler mHandler;
    private UpdaterAPP mWork;
    private String unzipPath;

    /* renamed from: com.ifoer.download.DownloadBinUpdate.1 */
    class C03451 extends Handler {
        C03451() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    DownloadBinUpdate.this.downloadDialog.setProgress(0);
                    DownloadBinUpdate.this.downloadDialog.setMessage(msg.obj.toString());
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    int downloadSize = msg.arg1;
                    int fileSize = msg.arg2;
                    DownloadBinUpdate.this.downloadDialog.setTitle(DownloadBinUpdate.this.mContext.getResources().getString(C0136R.string.DownloadUpgradeNow));
                    DownloadBinUpdate.this.downloadDialog.setProgress((downloadSize * 100) / fileSize);
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    String version = DownloadBinUpdate.this.mChatService.readDPUVersionInfo2105();
                    DownloadBinUpdate.this.downloadDialog.setTitle(DownloadBinUpdate.this.mContext.getResources().getString(C0136R.string.DownloadUpgradeFinish));
                    DownloadBinUpdate.this.downloadDialog.dismiss();
                    DownloadBinUpdate.this.writeFileData(version);
                    MySharedPreferences.setString(DownloadBinUpdate.this.mContext, Constants.DOWNLOADBIN, version.substring(1));
                    DBDao.getInstance(DownloadBinUpdate.this.mContext).setDownloadVersion(MainActivity.database, version.substring(1));
                    Toast.makeText(DownloadBinUpdate.this.mContext, C0136R.string.DownloadUpgradeSuccess, 0).show();
                    com.example.gpiomanager.MainActivity.setLow_OFF();
                    if (DownloadBinUpdate.this.isKeyToUpgade) {
                        DownloadBinUpdate.this.mHandler.sendEmptyMessage(ApkDownLoad.DOWNlOAD_SD_LOW);
                    } else if (CopyFileThread.mHandler != null) {
                        CopyFileThread.mHandler.obtainMessage(7).sendToTarget();
                    }
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    if (DownloadBinUpdate.this.downloadDialog != null && DownloadBinUpdate.this.downloadDialog.isShowing()) {
                        DownloadBinUpdate.this.downloadDialog.dismiss();
                    }
                    if (DownloadBinUpdate.this.isKeyToUpgade) {
                        DownloadBinUpdate.this.mHandler.sendEmptyMessage(ApkDownLoad.DOWNlOAD_NO_SD);
                    } else if (CopyFileThread.mHandler != null) {
                        CopyFileThread.mHandler.obtainMessage(7).sendToTarget();
                    }
                default:
            }
        }
    }

    class UpdaterAPP extends Thread {
        UpdaterAPP() {
        }

        public void run() {
            Looper.prepare();
            DownloadBinUpdate.this.downloadAndInstall();
        }
    }

    public DownloadBinUpdate(Context context, BluetoothChatService mChatService, Handler handler, boolean isKeyToUpgare) {
        this.PKG_SIZE = Flags.EXTEND;
        this.downloadDialog = null;
        this.unzipPath = Constant.UNZIP_PATH;
        this.mChatService = null;
        this.downloadHandler = new C03451();
        this.mContext = context;
        this.mChatService = mChatService;
        this.mHandler = handler;
        this.isKeyToUpgade = isKeyToUpgare;
        this.downloadDialog = new ProgressDialog(context);
        this.downloadDialog.setProgressStyle(1);
        this.downloadDialog.setMax(100);
        this.downloadDialog.setProgress(0);
        this.downloadDialog.setTitle(context.getResources().getString(C0136R.string.DownloadUpgradeInit));
        this.downloadDialog.setCancelable(false);
        this.downloadDialog.show();
    }

    public void checkUpdateAsync() {
        if (this.mWork == null) {
            this.mWork = new UpdaterAPP();
            this.mWork.start();
        }
    }

    public void downloadAndInstall() {
        try {
            String success = updateDownloadBin();
            Log.i("DownloadBin", "success" + success);
            if (this.downloadDialog != null && this.downloadDialog.isShowing()) {
                this.downloadDialog.dismiss();
            }
            if (success.equals(Contact.RELATION_ASK)) {
                Toast.makeText(this.mContext, C0136R.string.DownloadFileMissing, 0).show();
            } else if (success.equals(Contact.RELATION_FRIEND)) {
                Toast.makeText(this.mContext, C0136R.string.DownloadUpgradeFail, 0).show();
                this.mHandler.sendEmptyMessage(ApkDownLoad.DOWNlOAD_NO_SD);
            } else if (success.equals(Contact.RELATION_BACKNAME)) {
                Toast.makeText(this.mContext, C0136R.string.DownloadUpgradeSuccess, 0).show();
            }
        } catch (Exception e) {
            Toast.makeText(this.mContext, C0136R.string.DownloadUpgradeFail, 0).show();
            e.printStackTrace();
        }
    }

    public String updateDownloadBin() throws Exception {
        StatisticHelper helper = new StatisticHelper();
        String mode = XmlPullParser.NO_NAMESPACE;
        boolean iSuccess = false;
        File donwloadbin = new File(this.unzipPath + "/Diagnostic/Configure/Download", "/DOWNLOAD.bin");
        if (!donwloadbin.exists()) {
            return Contact.RELATION_ASK;
        }
        int flag1 = 0;
        while (flag1 < 3) {
            this.mChatService.reset2407();
            SystemClock.sleep(500);
            com.example.gpiomanager.MainActivity.reSet(this.mContext);
            SystemClock.sleep(500);
            String runningmode = this.mChatService.requestConnect2114();
            if (this.mChatService.passwordVerify2503(this.mChatService.requestConnect2502())) {
                if (this.mChatService.SendFileNameAndLength(donwloadbin)) {
                    break;
                }
                flag1++;
            } else {
                flag1++;
            }
        }
        if (flag1 >= 3) {
            return Contact.RELATION_FRIEND;
        }
        long totalLen = donwloadbin.length();
        byte[] buff = new byte[Flags.EXTEND];
        int writePos = 0;
        try {
            InputStream fileInputStream = new FileInputStream(donwloadbin);
            ProgressInfo progress = new ProgressInfo();
            int counter = 0;
            while (true) {
                int count = fileInputStream.read(buff);
                if (count <= 0) {
                    break;
                }
                counter++;
                long start = System.currentTimeMillis();
                byte[] sendOrder;
                String backOrder;
                int flag;
                Runnable writeByteData;
                Thread thread;
                if (count < Flags.EXTEND) {
                    Object rest = new byte[count];
                    System.arraycopy(buff, 0, rest, 0, count);
                    sendOrder = OrderMontage.sendUpdateFilesContent2403(DpuOrderUtils.dataChunkParams(writePos, rest, count));
                    backOrder = XmlPullParser.NO_NAMESPACE;
                    for (flag = 0; flag < 3; flag++) {
                        Constant.mChatService.setWaitTime(SpeechConfig.Rate8K);
                        if (sendOrder.length > 0) {
                            writeByteData = new WriteByteData(new Bridge(), sendOrder);
                            BluetoothChatService.readData = XmlPullParser.NO_NAMESPACE;
                            thread = new Thread(writeByteData);
                            thread.start();
                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            backOrder = BluetoothChatService.readData;
                        }
                        if (Analysis.analysis2403(Analysis.analysis(sendOrder, ByteHexHelper.hexStringToBytes(backOrder))).booleanValue()) {
                            iSuccess = true;
                            break;
                        }
                        iSuccess = false;
                    }
                } else {
                    sendOrder = OrderMontage.sendUpdateFilesContent2403(DpuOrderUtils.dataChunkParams(writePos, buff, count));
                    backOrder = XmlPullParser.NO_NAMESPACE;
                    for (flag = 0; flag < 3; flag++) {
                        Constant.mChatService.setWaitTime(SpeechConfig.Rate8K);
                        if (sendOrder.length > 0) {
                            writeByteData = new WriteByteData(new Bridge(), sendOrder);
                            BluetoothChatService.readData = XmlPullParser.NO_NAMESPACE;
                            thread = new Thread(writeByteData);
                            thread.start();
                            try {
                                thread.join();
                            } catch (InterruptedException e2) {
                                e2.printStackTrace();
                            }
                            backOrder = BluetoothChatService.readData;
                        }
                        if (Analysis.analysis2403(Analysis.analysis(sendOrder, ByteHexHelper.hexStringToBytes(backOrder))).booleanValue()) {
                            iSuccess = true;
                            break;
                        }
                        iSuccess = false;
                    }
                }
                writePos += count;
                helper.calcResults(totalLen - ((long) writePos), count, start, System.currentTimeMillis());
                progress.setFile(donwloadbin).setCurrent(1).setFileSum(1).setTotalBytes((int) totalLen).setSentBytes(writePos).setLeftHours(helper.getRestHours()).setLeftMinites(helper.getRestMinutes()).setLeftSeconds(helper.getRestSeconds()).setPercent((int) (((long) (writePos * 100)) / totalLen));
                this.downloadHandler.obtainMessage(1, writePos, (int) totalLen).sendToTarget();
            }
            fileInputStream.close();
        } catch (FileNotFoundException e3) {
            e3.printStackTrace();
        } catch (IOException e4) {
            e4.printStackTrace();
        }
        try {
            if (!this.mChatService.sendUpdateFileMd5(ByteHexHelper.calculateSingleFileMD5sum(donwloadbin))) {
                return Contact.RELATION_FRIEND;
            }
        } catch (Exception e5) {
            e5.printStackTrace();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e22) {
            e22.printStackTrace();
        }
        if (iSuccess) {
            this.downloadHandler.obtainMessage(2).sendToTarget();
            return Contact.RELATION_BACKNAME;
        }
        Message msg = this.downloadHandler.obtainMessage();
        msg.what = 3;
        this.downloadHandler.sendMessage(msg);
        return Contact.RELATION_FRIEND;
    }

    public void writeFileData(String version) {
        String filePath = Environment.getExternalStorageDirectory() + CRPTools.CRP_SERIALPORTINFO_VERSION;
        File file = new File(filePath);
        String res = XmlPullParser.NO_NAMESPACE;
        try {
            FileInputStream fin = new FileInputStream(filePath);
            byte[] buffer = new byte[fin.available()];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, AsyncHttpResponseHandler.DEFAULT_CHARSET);
            fin.close();
            res = res.replace(res.split("_")[2], version);
            file.delete();
            file.createNewFile();
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(res.getBytes());
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

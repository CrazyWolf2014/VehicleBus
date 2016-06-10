package com.ifoer.expeditionphone;

import CRP.serialport.SerialPortManager;
import CRP.utils.CRPTools;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.GpioPort;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.example.gpiomanager.GoloBlickThread;
import com.example.gpiomanager.MainActivity;
import com.ifoer.db.CopyFileThread;
import com.ifoer.db.DBDao;
import com.ifoer.db.InsertToCarIcon;
import com.ifoer.db.SaveDataAfterStartThread;
import com.ifoer.db.SaveSerialNoThread;
import com.ifoer.download.DownloadBinUpdate;
import com.ifoer.entity.ApkVersionInfo;
import com.ifoer.entity.Constant;
import com.ifoer.entity.PayKeyAll;
import com.ifoer.expeditionphone.inteface.IWelcomeActivityInterface;
import com.ifoer.mine.Contact;
import com.ifoer.serialport.SerialPort;
import com.ifoer.serialport.SerialPortNoReadThread;
import com.ifoer.service.BatteryService;
import com.ifoer.ui.MainMenuActivity;
import com.ifoer.util.ApkDownLoad;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.util.SrorageManager;
import com.ifoer.util.Tools;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.util.EncodingUtils;
import org.codehaus.jackson.smile.SmileConstants;
import org.xmlpull.v1.XmlPullParser;

public class WelcomeActivity extends BaseActivity implements IWelcomeActivityInterface {
    public static final String DEVICE_NAME = "/dev/mtgpio";
    public static final int GPIO_IOCQDATAIN = 18;
    public static final int GPIO_IOCQDATAOUT = 19;
    public static final int GPIO_IOCSDATAHIGH = 21;
    public static final int GPIO_IOCSDATALOW = 20;
    public static final int GPIO_IOCSDIRIN = 7;
    public static final int GPIO_IOCSDIROUT = 8;
    public static final int GPIO_IOCTMODE0 = 2;
    private static String LOCAL_APK_PATH = null;
    private static final String TAG = "WelcomeActivity";
    static String builder;
    public static SQLiteDatabase database;
    public static Context mContexts;
    public static int recvCount;
    public static int recvLoop;
    public static int[] recvSize;
    static SerialPort serialPort;
    public static boolean thread1;
    public static boolean thread2;
    public static boolean thread3;
    public static boolean thread4;
    private boolean f2138D;
    private ProgressDialog binProgressDialogs;
    private int currentVersion;
    String downloadBin;
    Handler handler;
    private boolean hasReadSn;
    List<PayKeyAll> listpaykey;
    MainActivity f2139m;
    GpioPort mGpioPort;
    private int notInstallVersion;
    private String paths;
    private String paypalpath;
    StringBuffer sb;
    public String serialNo;
    String serialPortID;
    String serialPortNO;
    private SerialPortNoReadThread serialPortReadThread;
    private int times;

    /* renamed from: com.ifoer.expeditionphone.WelcomeActivity.1 */
    class C06481 extends Handler {
        C06481() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MainActivity.MESSAGE_SHOW_MENU && WelcomeActivity.this.hasReadSn) {
                WelcomeActivity.this.jump();
            }
            int i = msg.what;
            if (msg.what == WelcomeActivity.GPIO_IOCTMODE0) {
                Toast.makeText(WelcomeActivity.mContexts, C0136R.string.get_data_fail, 1).show();
            }
            if (msg.what == 3) {
                WelcomeActivity.this.binProgressDialogs.dismiss();
                Toast.makeText(WelcomeActivity.mContexts, WelcomeActivity.mContexts.getString(C0136R.string.copy_failed), 1).show();
            }
            if (msg.what == 4) {
                WelcomeActivity.this.binProgressDialogs.dismiss();
                Toast.makeText(WelcomeActivity.mContexts, WelcomeActivity.mContexts.getString(C0136R.string.copy_success), 1).show();
            }
            if (msg.what == 5) {
                SimpleDialog.reStart(WelcomeActivity.mContexts);
            }
            if (msg.what == 6) {
                WelcomeActivity.this.updateDownloadBinDialog();
            }
            if (msg.what == WelcomeActivity.GPIO_IOCSDIRIN) {
                WelcomeActivity.this.checkApkUpdate();
            }
            if (msg.what == WelcomeActivity.GPIO_IOCSDIROUT) {
                WelcomeActivity.this.check();
            }
            if (msg.what == 9) {
                WelcomeActivity welcomeActivity = WelcomeActivity.this;
                welcomeActivity.times = welcomeActivity.times + 1;
                if (WelcomeActivity.this.times == WelcomeActivity.GPIO_IOCTMODE0) {
                    SimpleDialog.FailedRestart(WelcomeActivity.mContexts);
                } else {
                    WelcomeActivity.this.check();
                }
            }
            if (msg.what == ApkDownLoad.DOWNlOAD_NO_SD) {
                Toast.makeText(WelcomeActivity.mContexts, WelcomeActivity.this.getString(C0136R.string.DownloadUpgradeFail), 0).show();
                WelcomeActivity.this.handler.sendEmptyMessage(WelcomeActivity.GPIO_IOCSDIRIN);
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.WelcomeActivity.2 */
    class C06492 extends TimerTask {
        C06492() {
        }

        public void run() {
            MySharedPreferences.setString(WelcomeActivity.this.getApplicationContext(), "showPurchase", Contact.RELATION_FRIEND);
            MySharedPreferences.setString(WelcomeActivity.this.getApplicationContext(), "showResponsibility", Contact.RELATION_FRIEND);
            MySharedPreferences.setString(WelcomeActivity.this.getApplicationContext(), "firstLogin", Contact.RELATION_FRIEND);
            MySharedPreferences.setString(WelcomeActivity.this.getApplicationContext(), "showNoticKey", Contact.RELATION_FRIEND);
            MySharedPreferences.setString(WelcomeActivity.this.getApplicationContext(), "getSerialNo", Contact.RELATION_FRIEND);
            MySharedPreferences.getSharedPref(WelcomeActivity.this.getApplicationContext()).edit().putString(MySharedPreferences.CCKey, XmlPullParser.NO_NAMESPACE).commit();
            MySharedPreferences.getSharedPref(WelcomeActivity.this.getApplicationContext()).edit().putString(MySharedPreferences.TokenKey, XmlPullParser.NO_NAMESPACE).commit();
            MySharedPreferences.getSharedPref(WelcomeActivity.this.getApplicationContext()).edit().putString("usernames", XmlPullParser.NO_NAMESPACE).commit();
            MySharedPreferences.getSharedPref(WelcomeActivity.this.getApplicationContext()).edit().putString("BluetoothDeviceAddress", XmlPullParser.NO_NAMESPACE).commit();
            MySharedPreferences.getSharedPref(WelcomeActivity.this.getApplicationContext()).edit().putString("CurrentPosition", XmlPullParser.NO_NAMESPACE).commit();
            Intent intent = new Intent(WelcomeActivity.this, MainMenuActivity.class);
            intent.putExtra("fromWelcome", true);
            intent.putExtra(MainMenuActivity.IfShowDialog, WelcomeActivity.GPIO_IOCTMODE0);
            WelcomeActivity.this.startActivity(intent);
            WelcomeActivity.this.overridePendingTransition(0, 0);
            WelcomeActivity.this.finish();
            WelcomeActivity.this.overridePendingTransition(0, 0);
        }
    }

    /* renamed from: com.ifoer.expeditionphone.WelcomeActivity.3 */
    class C06503 implements OnClickListener {
        C06503() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            WelcomeActivity.this.readSn();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.WelcomeActivity.4 */
    class C06514 implements OnClickListener {
        C06514() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            WelcomeActivity.this.finish();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.WelcomeActivity.5 */
    class C06525 implements OnClickListener {
        private final /* synthetic */ Context val$context;
        private final /* synthetic */ Handler val$handler;
        private final /* synthetic */ boolean val$isSamu;

        C06525(Context context, Handler handler, boolean z) {
            this.val$context = context;
            this.val$handler = handler;
            this.val$isSamu = z;
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            new CopyFileThread(this.val$context, this.val$handler, this.val$isSamu).start();
            WelcomeActivity.this.binProgressDialogs = new ProgressDialog(this.val$context);
            WelcomeActivity.this.binProgressDialogs.setCancelable(false);
            WelcomeActivity.this.binProgressDialogs.setMessage(this.val$context.getResources().getText(C0136R.string.copying));
            WelcomeActivity.this.binProgressDialogs.show();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.WelcomeActivity.6 */
    class C06536 implements OnClickListener {
        private final /* synthetic */ Handler val$handler;

        C06536(Handler handler) {
            this.val$handler = handler;
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            WelcomeActivity.thread4 = true;
            if (WelcomeActivity.thread1 && WelcomeActivity.thread2 && WelcomeActivity.thread3 && WelcomeActivity.thread4) {
                this.val$handler.obtainMessage(MainActivity.MESSAGE_SHOW_MENU).sendToTarget();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.WelcomeActivity.7 */
    class C06547 implements OnClickListener {
        C06547() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            new DownloadBinUpdate(WelcomeActivity.mContexts, Constant.mChatService, WelcomeActivity.this.handler, false).checkUpdateAsync();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.WelcomeActivity.8 */
    class C06558 implements OnClickListener {
        C06558() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            WelcomeActivity.this.handler.sendEmptyMessage(WelcomeActivity.GPIO_IOCSDIRIN);
        }
    }

    /* renamed from: com.ifoer.expeditionphone.WelcomeActivity.9 */
    class C06569 implements OnClickListener {
        C06569() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            WelcomeActivity.this.finish();
        }
    }

    class SavePaykey implements Runnable {
        SavePaykey() {
        }

        public void run() {
            WelcomeActivity.this.sb = new StringBuffer();
            if (WelcomeActivity.this.listpaykey.size() > 0) {
                for (int i = 0; i < WelcomeActivity.this.listpaykey.size(); i++) {
                    PayKeyAll payKeyAll = (PayKeyAll) WelcomeActivity.this.listpaykey.get(i);
                    String cc = payKeyAll.getCc();
                    String serialNo = payKeyAll.getSerialNo();
                    String orderSN = payKeyAll.getOrderSN();
                    String paykey = payKeyAll.getPaykey();
                    WelcomeActivity.this.sb.append("\n\n" + cc + ";" + serialNo + ";" + orderSN + ";" + paykey + ";" + payKeyAll.getStatus() + "=");
                }
                WelcomeActivity.this.SaveTxt(WelcomeActivity.this.sb.toString(), true);
            }
        }
    }

    public WelcomeActivity() {
        this.f2138D = true;
        this.listpaykey = new ArrayList();
        this.times = 0;
        this.mGpioPort = null;
        this.f2139m = new MainActivity();
        this.hasReadSn = false;
        this.handler = new C06481();
    }

    static {
        thread1 = false;
        thread2 = false;
        thread3 = false;
        thread4 = false;
        LOCAL_APK_PATH = XmlPullParser.NO_NAMESPACE;
        builder = Build.DISPLAY;
        serialPort = null;
        recvCount = 0;
        recvSize = new int[GPIO_IOCSDIROUT];
        recvLoop = 0;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.main);
        Constant.exsitApp = false;
        mContexts = this;
    }

    public void initView() {
        this.paypalpath = Constant.PAYPAL_PATH;
        this.paths = this.paypalpath + "paypalpaykey.txt";
        LOCAL_APK_PATH = Environment.getExternalStorageDirectory() + "/cnlaunch/CRP229/";
        database = DBDao.getInstance(this).getConnection();
        MyApplication.getInstance().addActivity(this);
        this.listpaykey = DBDao.getInstance(this).queryPayKeyAll(database);
        String loginfirst = MySharedPreferences.getStringValue(this, MySharedPreferences.loginfirst);
        new Thread(new SavePaykey()).start();
        if (builder.contains("MTK")) {
            MySharedPreferences.setBoolean(mContexts, Constants.isSanMu, true);
        } else {
            MySharedPreferences.setBoolean(mContexts, Constants.isSanMu, false);
            if (builder.contains("04")) {
                MySharedPreferences.setBoolean(mContexts, Constants.hasODB, false);
            } else {
                MySharedPreferences.setBoolean(mContexts, Constants.hasODB, true);
            }
        }
        if (loginfirst == null || loginfirst.equals(XmlPullParser.NO_NAMESPACE)) {
            DBDao.getInstance(this).deleteAllTable(database);
            MySharedPreferences.setString(this, MySharedPreferences.loginfirst, Contact.RELATION_FRIEND);
            MySharedPreferences.setString(this, MySharedPreferences.first, Contact.RELATION_ASK);
            initOBD();
            if (!SerialPortNoReadThread.interrupted()) {
                SystemClock.sleep(200);
                SerialPortNoReadThread.interrupted();
            }
        } else if (loginfirst.equals(Contact.RELATION_FRIEND)) {
            MySharedPreferences.setString(this, MySharedPreferences.loginfirst, Contact.RELATION_FRIEND);
            MySharedPreferences.setString(this, MySharedPreferences.first, Contact.RELATION_FRIEND);
            writeDeviceInfo(false);
        }
        startBatteryService();
        if (DBDao.getInstance(this).isThereData(database)) {
            thread1 = true;
        } else {
            new InsertToCarIcon(this, this.handler).start();
        }
        String serailNo = getSnNo();
        if (serailNo == null || serailNo.equalsIgnoreCase(XmlPullParser.NO_NAMESPACE)) {
            thread2 = true;
        } else {
            new SaveDataAfterStartThread(this, this.handler, serailNo).start();
        }
        if (DBDao.getInstance(this).isClearSerialNo(database)) {
            new SaveSerialNoThread(this, this.handler).start();
        } else {
            thread3 = true;
        }
        if (SrorageManager.checkSd(builder.contains("MTK"))) {
            thread4 = true;
        } else {
            Update(mContexts, this.handler);
        }
        if (thread1 && thread2 && thread3 && thread4) {
            this.handler.obtainMessage(MainActivity.MESSAGE_SHOW_MENU).sendToTarget();
        }
    }

    private void startBatteryService() {
        startService(new Intent(this, BatteryService.class));
    }

    public void jump() {
        database.close();
        new Timer().schedule(new C06492(), 500);
    }

    public void SaveTxt(String s, boolean ss) {
        try {
            File paypal = new File(this.paypalpath);
            if (!paypal.exists()) {
                paypal.mkdirs();
            }
            File files = new File(this.paths);
            FileOutputStream outStream;
            OutputStreamWriter writer;
            if (files.exists()) {
                files.delete();
                outStream = new FileOutputStream(this.paths, ss);
                writer = new OutputStreamWriter(outStream, AsyncHttpResponseHandler.DEFAULT_CHARSET);
                writer.write(s);
                writer.flush();
                writer.close();
                outStream.close();
                return;
            }
            outStream = new FileOutputStream(this.paths, ss);
            writer = new OutputStreamWriter(outStream, AsyncHttpResponseHandler.DEFAULT_CHARSET);
            writer.write(s);
            writer.flush();
            writer.close();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFolderFile(String filePath, boolean deleteThisPath) throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File absolutePath : files) {
                    deleteFolderFile(absolutePath.getAbsolutePath(), true);
                }
            }
            if (!deleteThisPath) {
                return;
            }
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.listFiles().length == 0) {
                file.delete();
            }
        }
    }

    public void initOBD() {
        if (builder.contains("MTK")) {
            MySharedPreferences.setBoolean(mContexts, Constants.isSanMu, true);
            initOBDMTK();
            readSn();
        } else if (builder.contains(BatteryService.BUILDA23)) {
            MySharedPreferences.setBoolean(mContexts, Constants.isSanMu, false);
            new GoloBlickThread(mContexts, false, builder).start();
            MainActivity.setLow_OFF();
            if (MainActivity.getV() == 0) {
                MainActivity.setHigh_ON();
                SystemClock.sleep(1000);
            }
            MainActivity.reSet(mContexts);
            if (MySharedPreferences.getBooleanValue(mContexts, Constants.hasODB, false)) {
                readSn();
            } else {
                checkConectiorBefore(mContexts, mContexts.getString(C0136R.string.initializeTilte), mContexts.getString(C0136R.string.check_connector));
            }
        }
    }

    public void readSn() {
        serialPort = new SerialPortManager().getSerialPort();
        if (serialPort != null) {
            this.serialPortReadThread = new SerialPortNoReadThread(serialPort, mContexts);
            this.serialPortReadThread.start();
            byte[] cmd33 = new byte[10];
            cmd33[0] = (byte) 85;
            cmd33[1] = (byte) -86;
            cmd33[GPIO_IOCTMODE0] = (byte) -16;
            cmd33[3] = (byte) -8;
            cmd33[5] = (byte) 3;
            cmd33[6] = (byte) -88;
            cmd33[GPIO_IOCSDIRIN] = SmileConstants.TOKEN_LITERAL_NULL;
            cmd33[GPIO_IOCSDIROUT] = (byte) 3;
            cmd33[9] = (byte) -127;
            byte[] cmd44 = new byte[10];
            cmd44[0] = (byte) 85;
            cmd44[1] = (byte) -86;
            cmd44[GPIO_IOCTMODE0] = (byte) -16;
            cmd44[3] = (byte) -8;
            cmd44[5] = (byte) 3;
            cmd44[6] = (byte) -90;
            cmd44[GPIO_IOCSDIRIN] = SmileConstants.TOKEN_LITERAL_NULL;
            cmd44[GPIO_IOCSDIROUT] = (byte) 5;
            cmd44[9] = (byte) -119;
            try {
                recvCount = 0;
                int[] iArr = recvSize;
                recvSize[1] = 0;
                iArr[0] = 0;
                recvLoop = 0;
                OutputStream mOutputStream = serialPort.getOutputStream();
                mOutputStream.flush();
                SystemClock.sleep(50);
                mOutputStream.write(cmd33);
                SystemClock.sleep(50);
                mOutputStream.write(cmd44);
                SystemClock.sleep(500);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (builder.contains("MTK")) {
                MainActivity.SanMusetLow(mContexts);
            } else if (builder.contains(BatteryService.BUILDA23)) {
                MainActivity.setLow_OFF();
            }
            writeDeviceInfo(true);
        }
    }

    public void initOBDMTK() {
        new GoloBlickThread(mContexts, true, builder).start();
        if (MainActivity.SanmuGetV(mContexts) == 0) {
            MainActivity.SanMusetHigh(mContexts);
        }
        MainActivity.SanmuReset(mContexts);
    }

    public String getSnNo() {
        String path = Environment.getExternalStorageDirectory() + CRPTools.CRP_SERIALPORTINFO_VERSION;
        String res = XmlPullParser.NO_NAMESPACE;
        if (new File(path).exists()) {
            try {
                FileInputStream fin = new FileInputStream(path);
                byte[] buffer = new byte[fin.available()];
                fin.read(buffer);
                res = EncodingUtils.getString(buffer, AsyncHttpResponseHandler.DEFAULT_CHARSET);
                fin.close();
                String[] sn = res.split("_");
                String serialPortID = sn[0];
                String serialPortNO = sn[1];
                int a = 0;
                for (int i = 0; i < sn[GPIO_IOCTMODE0].length(); i++) {
                    if (Tools.isNumeric(new StringBuilder(String.valueOf(sn[GPIO_IOCTMODE0].charAt(i))).toString())) {
                        a = i;
                        break;
                    }
                }
                String downloadbinVersion = sn[GPIO_IOCTMODE0].substring(a);
                String oldVersion = MySharedPreferences.getStringValue(this, Constants.DOWNLOADBIN);
                MySharedPreferences.setString(this, Constants.DOWNLOADBIN, downloadbinVersion);
                MySharedPreferences.setString(this, Constants.SERIALID, serialPortID);
                MySharedPreferences.setString(this, Constants.SERIALNO, serialPortNO);
                MySharedPreferences.setString(this, MySharedPreferences.serialNoKey, serialPortNO);
                this.serialNo = serialPortNO;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mContexts, "Serial number does not exist.Please check whether the file has been deleted,then run \u201cDeviceInfo\u201dapk", 0).show();
        }
        return this.serialNo;
    }

    public void checkConectiorBefore(Context context, String title, String message) {
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getText(C0136R.string.Ok), new C06503());
        builder.setNegativeButton(context.getResources().getText(C0136R.string.no), new C06514());
        builder.create().show();
    }

    public void Update(Context context, Handler handler) {
        boolean isSamu = builder.contains("MTK");
        Builder builder = new Builder(context);
        builder.setTitle(context.getResources().getText(C0136R.string.initializeTilte));
        builder.setMessage(context.getResources().getText(C0136R.string.hasFile_update));
        builder.setPositiveButton(context.getResources().getString(C0136R.string.Ok), new C06525(context, handler, isSamu));
        builder.setNegativeButton(context.getResources().getString(C0136R.string.no), new C06536(handler));
        builder.show();
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

    public void updateDownloadBinDialog() {
        Builder builder = new Builder(mContexts);
        builder.setMessage(mContexts.getResources().getString(C0136R.string.FirmwareUpgradeMessage));
        builder.setCancelable(false);
        builder.setPositiveButton(mContexts.getResources().getString(C0136R.string.enter), new C06547());
        builder.setNegativeButton(mContexts.getResources().getString(C0136R.string.cancel), new C06558());
        builder.create().show();
    }

    public void cleanData(boolean isFirst) {
        Builder builder = new Builder(mContexts);
        if (isFirst) {
            builder.setMessage(mContexts.getResources().getString(C0136R.string.error_device));
        } else {
            builder.setMessage(mContexts.getResources().getString(C0136R.string.library_not_exist1));
        }
        builder.setCancelable(false);
        builder.setPositiveButton(mContexts.getResources().getString(C0136R.string.enter), new C06569());
        builder.setNegativeButton(mContexts.getResources().getString(C0136R.string.cancel), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                WelcomeActivity.this.handler.sendEmptyMessage(WelcomeActivity.GPIO_IOCSDIRIN);
            }
        });
        builder.create().show();
    }

    public void checkApkUpdate() {
        BufferedInputStream fin = null;
        try {
            this.currentVersion = Integer.parseInt(mContexts.getPackageManager().getPackageInfo(mContexts.getPackageName(), 0).versionName.replace(".", XmlPullParser.NO_NAMESPACE));
            if (new File(LOCAL_APK_PATH).isDirectory()) {
                ArrayList<ApkVersionInfo> list = CRPTools.readSdFile(LOCAL_APK_PATH, mContexts, this.currentVersion);
                if (list.size() > 0) {
                    String path = ((ApkVersionInfo) list.get(list.size() - 1)).getPath();
                    this.handler.obtainMessage(GPIO_IOCSDIROUT).sendToTarget();
                    Intent i = new Intent("android.intent.action.VIEW");
                    i.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
                    sendBroadcast(i, "needRestart");
                    mContexts.startActivity(i);
                } else {
                    this.handler.obtainMessage(5).sendToTarget();
                }
            } else {
                this.handler.obtainMessage(5).sendToTarget();
            }
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e32) {
                    e32.printStackTrace();
                }
            }
        }
    }

    private String getUninstallAPKInfo(Context ctx, String archiveFilePath) {
        PackageInfo pakinfo = ctx.getPackageManager().getPackageArchiveInfo(archiveFilePath, 1);
        File file = new File(archiveFilePath);
        if (pakinfo != null) {
            return pakinfo.versionName;
        }
        return null;
    }

    public void check() {
        new Timer().schedule(new TimerTask() {
            public void run() {
                try {
                    if (Integer.parseInt(WelcomeActivity.mContexts.getPackageManager().getPackageInfo(WelcomeActivity.mContexts.getPackageName(), 0).versionName.replace(".", XmlPullParser.NO_NAMESPACE)) == WelcomeActivity.this.currentVersion && WelcomeActivity.this.times < WelcomeActivity.GPIO_IOCTMODE0) {
                        WelcomeActivity.this.handler.obtainMessage(9).sendToTarget();
                    }
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }, 15000);
    }

    protected void onDestroy() {
        super.onDestroy();
        MainActivity.GoloOff(builder);
    }

    private void writeprofile(String id, String sn, String downloadbin) {
        String filePath = Environment.getExternalStorageDirectory() + CRPTools.CRP_SERIALPORTINFO_VERSION;
        String message = new StringBuilder(String.valueOf(id)).append("_").append(sn).append("_").append(downloadbin).toString();
        File fileD = new File(Environment.getExternalStorageDirectory() + "/cnlaunch");
        if (!fileD.exists()) {
            fileD.mkdirs();
        }
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(message.getBytes());
            fout.close();
            if (thread1 && thread2 && thread3 && thread4) {
                this.handler.obtainMessage(MainActivity.MESSAGE_SHOW_MENU).sendToTarget();
            }
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }
    }

    private void writeDeviceInfo(boolean isFirst) {
        this.serialPortID = MySharedPreferences.getStringValue(this, Constants.SERIALID);
        this.serialPortNO = MySharedPreferences.getStringValue(this, Constants.SERIALNO);
        this.downloadBin = MySharedPreferences.getStringValue(this, Constants.DOWNLOADBIN);
        if (TextUtils.isEmpty(this.downloadBin)) {
            MySharedPreferences.setString(mContexts, Constants.DOWNLOADBIN, "1.00");
            this.downloadBin = "1.00";
        }
        if (TextUtils.isEmpty(this.serialPortID) || TextUtils.isEmpty(this.serialPortNO)) {
            Log.i("nxy", "\u8bfb\u53d6\u5e8f\u5217\u53f7\u51fa\u95ee\u9898");
            this.hasReadSn = false;
            cleanData(isFirst);
            return;
        }
        this.hasReadSn = true;
        writeprofile(this.serialPortID, this.serialPortNO, this.downloadBin);
    }
}

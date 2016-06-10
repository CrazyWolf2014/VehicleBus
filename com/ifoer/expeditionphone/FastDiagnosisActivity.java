package com.ifoer.expeditionphone;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431frame.C0136R.string;
import com.cnlaunch.x431pro.common.Constants;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.db.DBDao;
import com.ifoer.dbentity.CarVersionInfo;
import com.ifoer.download.DownloadTask;
import com.ifoer.download.DownloadTaskManager;
import com.ifoer.download.DownloadTaskManagerThread;
import com.ifoer.entity.Constant;
import com.ifoer.expedition.BluetoothChat.DeviceListActivity;
import com.ifoer.expedition.ndk.VINDynamicDepot;
import com.ifoer.expedition.ndk.VINStdJni;
import com.ifoer.expeditionphone.vin.VinCameraActivity;
import com.ifoer.expeditionphone.vin.VinVoiceActivity;
import com.ifoer.util.AndroidToLan;
import com.ifoer.util.Copy_File;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.webservice.X431PadDiagSoftService;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class FastDiagnosisActivity extends RelativeLayout implements OnClickListener {
    public static final int CAMERA_RECOGNITION_REQUEST_CODE = 10000;
    public static final int PHOTO_ALBUM_REQUEST_CODE = 10001;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    public static final int REQUEST_ENABLE_BT = 2;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 10002;
    public static EditText etVin;
    public static String mDataDir;
    private static FastDiagnosisActivity mFastDiagnosisActivity;
    public static String mVin;
    private Button btnOk;
    private Context context;
    private String dataDir;
    private Handler downHandler;
    private View fastDiagnosisView;
    private boolean flagLoadOK;
    private LayoutInflater inflater;
    private boolean isInitDiag;
    private String lanName;
    private DialogInterface.OnClickListener listener;
    private LinearLayout llCarVoice;
    private LinearLayout llCarWindow;
    private LinearLayout llPhotoAlbum;
    private BluetoothAdapter mBluetoothAdapter;
    private String mCarName;
    private String mConnectedDeviceName;
    private String mCountry;
    private SQLiteDatabase mDatabase;
    private VINDynamicDepot mDynamicDepotJni;
    private IntentFilter mIntentFilter;
    private String mJumpPaths;
    private String mJumpSDPaths;
    private String mPaths;
    private ProgressDialog mProgress;
    private ProgressDialog mProgressDiaglog;
    private mBroadcastReceiver mReceiver;
    private String mSerialNo;
    private String mStrVin;
    private String[] mVinArray;
    private String paths;
    private String sdPaths;
    private int timeFlag;

    /* renamed from: com.ifoer.expeditionphone.FastDiagnosisActivity.1 */
    class C05611 extends Handler {

        /* renamed from: com.ifoer.expeditionphone.FastDiagnosisActivity.1.1 */
        class C05591 extends Thread {
            C05591() {
            }

            public void run() {
                try {
                    C05591.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /* renamed from: com.ifoer.expeditionphone.FastDiagnosisActivity.1.2 */
        class C05602 extends Thread {
            C05602() {
            }

            public void run() {
                try {
                    C05602.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        C05611() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    FastDiagnosisActivity.this.mPaths = new StringBuilder(String.valueOf(FastDiagnosisActivity.this.sdPaths)).append(MySharedPreferences.getStringValue(FastDiagnosisActivity.this.context, MySharedPreferences.vinAutoPaths)).toString();
                    Log.d("weige", "bb:  " + FastDiagnosisActivity.this.mPaths);
                    try {
                        FastDiagnosisActivity.this.copyFileFromSdcardToDataLib();
                        FastDiagnosisActivity.this.mProgressDiaglog.dismiss();
                        new C05591().start();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                case Service.METAGRAM /*99*/:
                    FastDiagnosisActivity.this.mProgressDiaglog.dismiss();
                    Toast.makeText(FastDiagnosisActivity.this.context, new StringBuilder(String.valueOf(FastDiagnosisActivity.this.context.getString(C0136R.string.obdGain))).append(FastDiagnosisActivity.this.context.getString(C0136R.string.get_data_fail)).append("\u8bf7\u4f7f\u7528\u5176\u4ed6\u65b9\u5f0f\u8f93\u5165VIN\u7801.").toString(), FastDiagnosisActivity.REQUEST_CONNECT_DEVICE).show();
                case ParseCharStream.HISTORY_LENGTH /*100*/:
                    Log.d("weige", "100=  " + FastDiagnosisActivity.this.mPaths);
                    new C05602().start();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.FastDiagnosisActivity.2 */
    class C05622 implements DialogInterface.OnClickListener {
        C05622() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ArrayList<CarVersionInfo> list = FastDiagnosisActivity.this.getCarInfoList(which);
            if (list.size() <= 0) {
                Toast.makeText(FastDiagnosisActivity.this.context, new StringBuilder(String.valueOf(FastDiagnosisActivity.this.context.getString(C0136R.string.left_btn_zhenduan))).append(FastDiagnosisActivity.this.context.getString(C0136R.string.carType)).append(FastDiagnosisActivity.this.context.getString(C0136R.string.download_software_status)).toString(), FastDiagnosisActivity.REQUEST_CONNECT_DEVICE).show();
            } else {
                FastDiagnosisActivity.this.chooseCarCheck(list, which);
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.FastDiagnosisActivity.3 */
    class C05633 implements Runnable {
        C05633() {
        }

        public void run() {
            String serialNo = MySharedPreferences.getStringValue(FastDiagnosisActivity.this.context, MySharedPreferences.serialNoKey);
            X431PadDiagSoftService Service = new X431PadDiagSoftService();
            List<X431PadDtoSoft> dtoList = new ArrayList();
            try {
                int lanId = AndroidToLan.getLanId(FastDiagnosisActivity.this.mCountry);
                String cc = MySharedPreferences.getStringValue(FastDiagnosisActivity.this.context, MySharedPreferences.CCKey);
                String token = MySharedPreferences.getStringValue(FastDiagnosisActivity.this.context, MySharedPreferences.TokenKey);
                Service.setCc(cc);
                Service.setToken(token);
                X431PadDtoSoft softDto = Service.getSpecifiedDiagSoftLatestInfo1(serialNo, "AutoSearch", Integer.valueOf(lanId), Integer.valueOf(XStream.NO_REFERENCES), FastDiagnosisActivity.this.downHandler);
                dtoList.add(softDto);
                String paths = Constant.LOCAL_SERIALNO_PATH + FastDiagnosisActivity.this.mSerialNo + "/DIAGNOSTIC/VEHICLES/AutoSearch/";
                double maxOld = FastDiagnosisActivity.this.getMaxVersion(paths);
                double version = Double.parseDouble(softDto.getVersionNo().split("V")[FastDiagnosisActivity.REQUEST_CONNECT_DEVICE]);
                if (!Environment.getExternalStorageState().equals("mounted") || maxOld >= version) {
                    FastDiagnosisActivity.this.mPaths = new StringBuilder(String.valueOf(FastDiagnosisActivity.this.sdPaths)).append(paths).append("V").append(maxOld).append(FilePathGenerator.ANDROID_DIR_SEP).toString();
                    FastDiagnosisActivity.this.copyFileFromSdcardToDataLib();
                    Log.d("weige", "mPaths=  " + FastDiagnosisActivity.this.mPaths);
                    VINStdJni vINStdJni = new VINStdJni();
                    stdJni.setVinCallbackEnv(FastDiagnosisActivity.this.mPaths);
                    return;
                }
                DownloadTaskManager.getInstance();
                new Thread(new DownloadTaskManagerThread()).start();
                DownloadTaskManager.getInstance().addDownloadTask(new DownloadTask(FastDiagnosisActivity.this.context, FastDiagnosisActivity.this.downHandler, softDto, serialNo));
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            } catch (NullPointerException e2) {
                e2.printStackTrace();
                FastDiagnosisActivity.this.downHandler.sendEmptyMessage(0);
            } catch (Throwable e3) {
                e3.printStackTrace();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.FastDiagnosisActivity.4 */
    class C05644 implements Runnable {
        C05644() {
        }

        public void run() {
            try {
                String serialNo = MySharedPreferences.getStringValue(FastDiagnosisActivity.this.context, MySharedPreferences.serialNoKey);
                String paths = Constant.LOCAL_SERIALNO_PATH + FastDiagnosisActivity.this.mSerialNo + "/DIAGNOSTIC/VEHICLES/AutoSearch/";
                if (new File(FastDiagnosisActivity.this.mPaths).exists()) {
                    double maxOld = FastDiagnosisActivity.this.getMaxVersion(FastDiagnosisActivity.this.mPaths);
                    if (Environment.getExternalStorageState().equals("mounted")) {
                        FastDiagnosisActivity.this.mPaths = new StringBuilder(String.valueOf(FastDiagnosisActivity.this.sdPaths)).append(paths).append("V").append(maxOld).append(FilePathGenerator.ANDROID_DIR_SEP).toString();
                        Log.e("weizewei", "mPaths=" + FastDiagnosisActivity.this.mPaths);
                        FastDiagnosisActivity.this.copyFileFromSdcardToDataLib();
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    class MyThread extends Thread {
        MyThread() {
        }

        public void run() {
            while (!FastDiagnosisActivity.this.flagLoadOK) {
                try {
                    sleep(1000);
                    FastDiagnosisActivity fastDiagnosisActivity = FastDiagnosisActivity.this;
                    fastDiagnosisActivity.timeFlag = fastDiagnosisActivity.timeFlag + FastDiagnosisActivity.REQUEST_CONNECT_DEVICE;
                    if (FastDiagnosisActivity.this.timeFlag >= 15) {
                        FastDiagnosisActivity.this.downHandler.sendEmptyMessage(99);
                        return;
                    }
                } catch (Exception e) {
                    FastDiagnosisActivity.this.downHandler.sendEmptyMessage(99);
                    return;
                }
            }
            Log.e("weige", "flagLoadOK= " + FastDiagnosisActivity.this.flagLoadOK);
            FastDiagnosisActivity.this.timeFlag = 0;
            FastDiagnosisActivity.this.downHandler.sendEmptyMessage(100);
        }
    }

    class PublicCircleAsy extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        PublicCircleAsy() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(FastDiagnosisActivity.this.context);
            this.progressDialog.setMessage(FastDiagnosisActivity.this.getResources().getText(C0136R.string.initializeMessage));
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        protected String doInBackground(String... params) {
            if (Copy_File.copy(FastDiagnosisActivity.this.sdPaths, FastDiagnosisActivity.this.paths) == 0) {
                Copy_File.list = new ArrayList();
                Copy_File.findAllSoFile(FastDiagnosisActivity.this.paths);
                if (Copy_File.list.size() > 0) {
                    return null;
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            this.progressDialog.cancel();
            if (Copy_File.list.size() > 0) {
                ArrayList<CarVersionInfo> list = DBDao.getInstance(FastDiagnosisActivity.this.context).queryCarVersion(FastDiagnosisActivity.this.mCarName, FastDiagnosisActivity.this.lanName, FastDiagnosisActivity.this.mSerialNo, FastDiagnosisActivity.this.mDatabase);
                int softID = DBDao.getInstance(FastDiagnosisActivity.this.context).querySoftId(FastDiagnosisActivity.this.mCarName, FastDiagnosisActivity.this.mDatabase);
                Intent intent = new Intent(FastDiagnosisActivity.this.context, LoadDynamicLibsActivity.class);
                intent.putExtra("list", list);
                intent.putExtra("carId", new StringBuilder(String.valueOf(softID)).toString());
                intent.putExtra("lanName", FastDiagnosisActivity.this.lanName);
                FastDiagnosisActivity.this.context.startActivity(intent);
                return;
            }
            Toast.makeText(FastDiagnosisActivity.this.context, FastDiagnosisActivity.this.getResources().getString(C0136R.string.so_error), FastDiagnosisActivity.REQUEST_CONNECT_DEVICE).show();
        }
    }

    class RecommendAsy extends AsyncTask<Integer, Integer, String> {
        Context context;
        int iret;

        public RecommendAsy(Context context) {
            this.iret = -1;
            this.context = context;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(Integer... params) {
            FastDiagnosisActivity.this.mDynamicDepotJni.VINDiagnoseMain();
            FastDiagnosisActivity.this.isInitDiag = false;
            this.iret = FastDiagnosisActivity.this.mDynamicDepotJni.OBDReadVIN();
            return null;
        }

        protected void onPostExecute(String result) {
            if (this.iret == 0) {
                FastDiagnosisActivity.this.mVinArray = null;
                FastDiagnosisActivity.this.mVinArray = FastDiagnosisActivity.this.mDynamicDepotJni.AutoSearchVehByVIN(FastDiagnosisActivity.this.mStrVin, 17).split(",");
                Toast.makeText(this.context, new StringBuilder(String.valueOf(this.context.getString(C0136R.string.obdGain))).append(this.context.getString(C0136R.string.log_succcess)).append(":").append(FastDiagnosisActivity.this.mStrVin).toString(), FastDiagnosisActivity.REQUEST_CONNECT_DEVICE).show();
                FastDiagnosisActivity.this.mProgress.dismiss();
            } else {
                Toast.makeText(this.context, new StringBuilder(String.valueOf(this.context.getString(C0136R.string.obdGain))).append(this.context.getString(C0136R.string.get_data_fail)).append("\u8bf7\u4f7f\u7528\u5176\u4ed6\u65b9\u5f0f\u8f93\u5165VIN\u7801.").toString(), FastDiagnosisActivity.REQUEST_CONNECT_DEVICE).show();
                FastDiagnosisActivity.this.mProgress.dismiss();
            }
            FastDiagnosisActivity.this.mProgressDiaglog.dismiss();
            FastDiagnosisActivity.this.mBluetoothAdapter = null;
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {

        /* renamed from: com.ifoer.expeditionphone.FastDiagnosisActivity.mBroadcastReceiver.1 */
        class C05651 extends Thread {
            C05651() {
            }

            public void run() {
                try {
                    C05651.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new RecommendAsy(FastDiagnosisActivity.this.context).execute(new Integer[0]);
            }
        }

        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("SPT_SET_VIN")) {
                FastDiagnosisActivity.this.mStrVin = intent.getStringExtra("SPT_SET_VIN");
                String vinCode = MySharedPreferences.getStringValue(context, "VIN_CODE");
                if (vinCode.equals(XmlPullParser.NO_NAMESPACE)) {
                    FastDiagnosisActivity.etVin.setText(FastDiagnosisActivity.this.mStrVin);
                    MySharedPreferences.setString(context, "VIN_CODE", FastDiagnosisActivity.this.mStrVin);
                }
                Log.d(Constants.DEBUG_KEY, new StringBuilder(String.valueOf(vinCode)).append("\u63a5\u6536\u5e7f\u64ad===Activity===").append(FastDiagnosisActivity.this.mStrVin).toString());
            } else if (intent.getAction().equals("android.bluetooth.device.action.ACL_CONNECTED")) {
                FastDiagnosisActivity.this.mProgressDiaglog.show();
                new MyThread().start();
                new VINStdJni().setVinCallbackEnv(FastDiagnosisActivity.this.mPaths);
                new C05651().start();
            }
        }
    }

    static {
        mDataDir = null;
        mFastDiagnosisActivity = null;
    }

    public static FastDiagnosisActivity getInstense(Context context) {
        if (mFastDiagnosisActivity == null) {
            mFastDiagnosisActivity = new FastDiagnosisActivity(context);
        }
        return mFastDiagnosisActivity;
    }

    public FastDiagnosisActivity(Context context) {
        super(context);
        this.inflater = null;
        this.mStrVin = XmlPullParser.NO_NAMESPACE;
        this.mConnectedDeviceName = null;
        this.mBluetoothAdapter = null;
        this.mPaths = XmlPullParser.NO_NAMESPACE;
        this.mSerialNo = XmlPullParser.NO_NAMESPACE;
        this.dataDir = XmlPullParser.NO_NAMESPACE;
        this.paths = XmlPullParser.NO_NAMESPACE;
        this.sdPaths = XmlPullParser.NO_NAMESPACE;
        this.mDynamicDepotJni = new VINDynamicDepot();
        this.isInitDiag = false;
        this.flagLoadOK = false;
        this.timeFlag = 0;
        this.downHandler = new C05611();
        this.listener = new C05622();
        this.context = context;
        this.mProgressDiaglog = new ProgressDialog(context);
        this.mProgressDiaglog.setMessage(context.getString(C0136R.string.obdGain) + "VIN," + context.getString(C0136R.string.imWait));
        this.mCountry = Locale.getDefault().getCountry();
        this.lanName = AndroidToLan.toLan(this.mCountry);
        try {
            mDataDir = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).dataDir;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        initView();
        this.mSerialNo = MySharedPreferences.getStringValue(context, MySharedPreferences.serialNoKey);
        try {
            Log.e("weige", "FastDiagnosisActivity try");
            registerBoradcastReceiver();
            this.mDatabase = MainActivity.database;
            this.mPaths = Constant.LOCAL_SERIALNO_PATH + this.mSerialNo + "/DIAGNOSTIC/VEHICLES/AutoSearch/";
            String cc = MySharedPreferences.getStringValue(context, MySharedPreferences.CCKey);
            File filePaths = new File(this.mPaths);
            if ((cc == null || cc.equals(XmlPullParser.NO_NAMESPACE)) && !filePaths.exists()) {
                SimpleDialog.ToastToLogin(context);
                if (this.mSerialNo != null || XmlPullParser.NO_NAMESPACE.equals(this.mSerialNo)) {
                    context.startActivity(new Intent(context, SerialNumberActivity.class));
                }
                return;
            }
            if (cc != null) {
                if (!cc.equals(XmlPullParser.NO_NAMESPACE)) {
                    checkVersionAndDownLoad();
                    if (this.mSerialNo != null) {
                    }
                    context.startActivity(new Intent(context, SerialNumberActivity.class));
                }
            }
            Log.e("weizewei", "\u5982\u679c\u6ca1\u6709\u767b\u5f55\u5e76\u4e14sdcard\u5df2\u7ecf\u6709\u8bca\u65ad\u6587\u4ef6\u7684\u8bdd\uff0c\u76f4\u63a5\u68c0\u6d4b\u8fdb\u884c\u8bca\u65ad");
            checkSDcarVersion();
            if (this.mSerialNo != null) {
            }
            context.startActivity(new Intent(context, SerialNumberActivity.class));
        } catch (Throwable e2) {
            e2.printStackTrace();
        }
    }

    public void registerBoradcastReceiver() {
        this.mReceiver = new mBroadcastReceiver();
        this.mIntentFilter = new IntentFilter();
        this.mIntentFilter.addAction("SPT_SET_VIN");
        this.mIntentFilter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
        this.mIntentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        this.context.registerReceiver(this.mReceiver, this.mIntentFilter);
    }

    private void initView() {
        this.inflater = LayoutInflater.from(this.context);
        this.fastDiagnosisView = this.inflater.inflate(C0136R.layout.fast_diagnosis, null);
        this.fastDiagnosisView.setBackgroundDrawable(MySharedPreferences.getBgId(this.context));
        addView(this.fastDiagnosisView, new LayoutParams(-1, -1));
        this.llPhotoAlbum = (LinearLayout) this.fastDiagnosisView.findViewById(C0136R.id.ll_fast_diagnosis_photo_album);
        this.llCarWindow = (LinearLayout) this.fastDiagnosisView.findViewById(C0136R.id.ll_fast_diagnosis_car_window);
        this.llCarVoice = (LinearLayout) this.fastDiagnosisView.findViewById(C0136R.id.ll_fast_diagnosis_car_voice);
        etVin = (EditText) this.fastDiagnosisView.findViewById(C0136R.id.et_fast_dignosis);
        this.btnOk = (Button) this.fastDiagnosisView.findViewById(C0136R.id.btn_fast_diagnosis);
        MySharedPreferences.setString(this.context, "VIN_CODE", XmlPullParser.NO_NAMESPACE);
        this.llPhotoAlbum.setOnClickListener(this);
        this.llCarWindow.setOnClickListener(this);
        this.llCarVoice.setOnClickListener(this);
        this.btnOk.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.ll_fast_diagnosis_car_window) {
            ((Activity) this.context).startActivityForResult(new Intent(this.context, VinCameraActivity.class), CAMERA_RECOGNITION_REQUEST_CODE);
        } else if (v.getId() == C0136R.id.ll_fast_diagnosis_photo_album) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction("android.intent.action.GET_CONTENT");
            ((Activity) this.context).startActivityForResult(intent, PHOTO_ALBUM_REQUEST_CODE);
        } else if (v.getId() == C0136R.id.ll_fast_diagnosis_car_voice) {
            ((Activity) this.context).startActivityForResult(new Intent(this.context, VinVoiceActivity.class), VOICE_RECOGNITION_REQUEST_CODE);
        } else if (v.getId() == C0136R.id.btn_fast_diagnosis) {
            this.mStrVin = etVin.getText().toString();
            if (this.mStrVin.length() <= 0) {
                MySharedPreferences.setString(this.context, "VIN_CODE", XmlPullParser.NO_NAMESPACE);
                Toast.makeText(this.context, this.context.getString(C0136R.string.vin_hint), 0).show();
            } else {
                MySharedPreferences.setString(this.context, "VIN_CODE", this.mStrVin);
                Log.e("weizewei", "mStrVin=" + this.mStrVin);
            }
            if (Constant.mChatService.getState() != 3) {
                Toast.makeText(this.context, this.context.getString(C0136R.string.not_connected), REQUEST_CONNECT_DEVICE).show();
                ((Activity) MainActivity.contexts).startActivityForResult(new Intent(MainActivity.contexts, DeviceListActivity.class), REQUEST_CONNECT_DEVICE);
                return;
            }
            if (!this.isInitDiag) {
                this.mDynamicDepotJni.VINDiagnoseMain();
                this.isInitDiag = true;
            }
            if (Constant.mChatService.getState() == 3) {
                this.mStrVin = etVin.getText().toString();
                if (this.mStrVin.length() <= 0) {
                    Toast.makeText(this.context, this.context.getString(C0136R.string.vin_hint), 0).show();
                    return;
                }
                this.mVinArray = null;
                if (this.mStrVin.length() < 17) {
                    Toast.makeText(this.context, this.context.getString(C0136R.string.vin_erro), 0).show();
                    return;
                }
                this.mVinArray = this.mDynamicDepotJni.AutoSearchVehByVIN(this.mStrVin, 17).split(",");
                if (this.mVinArray != null) {
                    viewCarChooseDialog();
                }
            }
        }
    }

    private void viewCarChooseDialog() {
        if (this.mVinArray.length == REQUEST_CONNECT_DEVICE) {
            ArrayList<CarVersionInfo> list = getCarInfoList(0);
            if (list.size() <= 0) {
                Toast.makeText(this.context, new StringBuilder(String.valueOf(this.mVinArray[0])).append(this.context.getString(C0136R.string.carType)).append(this.context.getString(C0136R.string.download_software_status)).toString(), REQUEST_CONNECT_DEVICE).show();
                return;
            } else {
                chooseCarCheck(list, 0);
                return;
            }
        }
        Builder builder = new Builder(this.context);
        builder.setTitle(getResources().getText(C0136R.string.selectCar));
        Class<string> cls = string.class;
        String[] tempVinArray = new String[this.mVinArray.length];
        for (int i = 0; i < this.mVinArray.length; i += REQUEST_CONNECT_DEVICE) {
            String tempStr = DBDao.getInstance(this.context).queryCarName(this.mDatabase, this.mVinArray[i], MainActivity.country);
            if (tempStr.equals(XmlPullParser.NO_NAMESPACE)) {
                tempVinArray[i] = this.mVinArray[i];
            } else {
                try {
                    tempVinArray[i] = this.context.getResources().getText(cls.getDeclaredField(tempStr).getInt(null)) + "(" + this.mVinArray[i] + ")";
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e2) {
                    e2.printStackTrace();
                } catch (IllegalAccessException e3) {
                    e3.printStackTrace();
                } catch (NoSuchFieldException e4) {
                    e4.printStackTrace();
                }
            }
        }
        builder.setItems(tempVinArray, this.listener);
        builder.show();
    }

    private void chooseCarCheck(ArrayList<CarVersionInfo> list, int index) {
        CarVersionInfo carVerInfo = (CarVersionInfo) list.get(0);
        String carSoftWare = carVerInfo.getVersionDir();
        this.mCarName = carVerInfo.getCarId();
        File file = new File(carSoftWare);
        if (!file.exists()) {
            String[] tempPaths = Constant.getDefaultExternalStoragePathList();
            if (tempPaths.length > REQUEST_CONNECT_DEVICE) {
                carSoftWare = carSoftWare.replaceAll(tempPaths[0], tempPaths[REQUEST_CONNECT_DEVICE]);
                file = new File(carSoftWare);
            }
        }
        if (file.exists()) {
            String[] paths = carSoftWare.split(Constants.des_password);
            this.mJumpSDPaths = new StringBuilder(String.valueOf(carSoftWare)).append(FilePathGenerator.ANDROID_DIR_SEP).toString();
            this.mJumpPaths = paths[REQUEST_CONNECT_DEVICE];
            loadSo(new StringBuilder(String.valueOf(carSoftWare)).append(FilePathGenerator.ANDROID_DIR_SEP).toString(), paths[REQUEST_CONNECT_DEVICE]);
            return;
        }
        Builder builder = new Builder(this.context);
        builder.setTitle(getResources().getText(C0136R.string.initializeTilte));
        builder.setMessage(getResources().getText(C0136R.string.library_not_exist1));
        builder.setPositiveButton(getResources().getText(C0136R.string.sure), null);
        builder.show();
    }

    private ArrayList<CarVersionInfo> getCarInfoList(int index) {
        String tempLanName = Locale.getDefault().getCountry().toUpperCase();
        if (tempLanName.equals(Constants.DEFAULT_LANGUAGE) || tempLanName.equals("ZH") || tempLanName.equals("CN") || tempLanName.equals("TW") || tempLanName.equals("HK")) {
            tempLanName = this.lanName;
            return DBDao.getInstance(this.context).queryCarVersion(this.mVinArray[index], this.lanName, this.mSerialNo, this.mDatabase);
        }
        ArrayList<CarVersionInfo> list = DBDao.getInstance(this.context).queryCarVersion(this.mVinArray[index], tempLanName, this.mSerialNo, this.mDatabase);
        if (list.size() > 0) {
            return list;
        }
        this.mCountry = Constants.DEFAULT_LANGUAGE;
        return DBDao.getInstance(this.context).queryCarVersion(this.mVinArray[index], Constants.DEFAULT_LANGUAGE, this.mSerialNo, this.mDatabase);
    }

    private void loadSo(String dataDirs, String sdPath) {
        this.paths = mDataDir + "/libs/cnlaunch" + sdPath + FilePathGenerator.ANDROID_DIR_SEP;
        this.sdPaths = dataDirs;
        ArrayList<CarVersionInfo> list = DBDao.getInstance(this.context).queryCarVersion(this.mCarName, this.lanName, this.mSerialNo, this.mDatabase);
        int softID = DBDao.getInstance(this.context).querySoftId(this.mCarName, this.mDatabase);
        Intent intent = new Intent(this.context, LoadDynamicLibsActivity.class);
        intent.putExtra("list", list);
        intent.putExtra("carId", new StringBuilder(String.valueOf(softID)).toString());
        intent.putExtra("lanName", this.lanName);
        this.context.startActivity(intent);
    }

    public static String str2NumbersOrLetters(String str) {
        char[] temC = str.toCharArray();
        for (int i = 0; i < temC.length; i += REQUEST_CONNECT_DEVICE) {
            int mid = temC[i];
            if ((mid < 48 || mid > 57) && ((mid < 65 || mid > 90) && (mid < 97 || mid > Opcodes.ISHR))) {
                temC[i] = ' ';
            }
        }
        return new String(temC).replaceAll(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE);
    }

    private void connectBluetooth() {
        if (this.mProgress != null) {
            this.mProgress.dismiss();
        }
        Log.e("weizewei", "\u8fde\u63a5\u84dd\u7259");
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.mBluetoothAdapter == null) {
            Toast.makeText(this.context, getResources().getString(C0136R.string.main_blue_null), REQUEST_CONNECT_DEVICE).show();
        } else if (this.mBluetoothAdapter == null || !this.mBluetoothAdapter.isEnabled()) {
            ((Activity) this.context).startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), REQUEST_ENABLE_BT);
        } else if (Constant.mChatService.getState() != 3) {
            ((Activity) MainActivity.contexts).startActivityForResult(new Intent(MainActivity.contexts, DeviceListActivity.class), REQUEST_CONNECT_DEVICE);
        } else if (Constant.mChatService.getState() == 3) {
            Log.e("weizewei", "STATE_CONNECTED");
            this.mProgressDiaglog.show();
            Log.e("weizewei", "\u8fde\u63a5\u84dd\u7259,\u52a0\u8f7d");
            new VINStdJni().setVinCallbackEnv(this.mPaths);
            new RecommendAsy(this.context).execute(new Integer[0]);
        }
    }

    private void copyFileFromAssetsToDataLib() throws Throwable {
        try {
            mDataDir = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 0).dataDir;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        String paths = mDataDir + "/libs/";
        if (!new File(new StringBuilder(String.valueOf(paths)).append("libAutoVinSTD.so").toString()).exists()) {
            File filePaths = new File(paths);
            if (!filePaths.exists()) {
                filePaths.mkdirs();
            }
            try {
                InputStream fosfrom = getResources().getAssets().open("OBD_VIN.zip");
                OutputStream fosto = new FileOutputStream(new StringBuilder(String.valueOf(paths)).append("OBD_VIN.zip").toString());
                byte[] bt = new byte[Flags.FLAG5];
                while (true) {
                    int c = fosfrom.read(bt);
                    if (c <= 0) {
                        break;
                    }
                    fosto.write(bt, 0, c);
                }
                fosfrom.close();
                fosto.close();
            } catch (Exception e2) {
            }
            upZipFile(new StringBuilder(String.valueOf(paths)).append("OBD_VIN.zip").toString(), paths);
            new File(new StringBuilder(String.valueOf(paths)).append("OBD_VIN.zip").toString()).delete();
        }
        loadLibary();
    }

    private void loadLibary() {
        Log.e("weige", "loadLibary");
        this.paths = mDataDir + "/libs/";
        System.load(this.paths + "libAutoVinCOMM_ABSTRACT_LAYER.so");
        System.load(this.paths + "libAutoVinSTD.so");
        System.load(this.paths + "libAutoVinDIAG.so");
        this.flagLoadOK = true;
        connectBluetooth();
        this.mProgress.dismiss();
    }

    private void copyFileFromAssetsToSDCard() throws Throwable {
        if (!new File(this.mPaths + "AUTO.GGP").exists()) {
            try {
                InputStream fosfrom = getResources().getAssets().open("OBD_GGP.zip");
                OutputStream fosto = new FileOutputStream(this.mPaths + "OBD_GGP.zip");
                byte[] bt = new byte[Flags.FLAG5];
                while (true) {
                    int c = fosfrom.read(bt);
                    if (c <= 0) {
                        break;
                    }
                    fosto.write(bt, 0, c);
                }
                fosfrom.close();
                fosto.close();
            } catch (Exception e) {
            }
            upZipFile(this.mPaths + "OBD_GGP.zip", this.mPaths);
            File file = new File(this.mPaths);
            new File(this.mPaths + "OBD_GGP.zip").delete();
        }
    }

    private void upZipFile(String zipFiles, String folderPath) throws ZipException, IOException {
        File zipFile = new File(zipFiles);
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        ZipFile zf = new ZipFile(zipFile);
        Enumeration<?> entries = zf.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            InputStream in = zf.getInputStream(entry);
            File desFile = new File(new String(new StringBuilder(String.valueOf(folderPath)).append(File.separator).append(entry.getName()).toString().getBytes("8859_1"), "GB2312"));
            if (!desFile.exists()) {
                File fileParentDir = desFile.getParentFile();
                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }
                desFile.createNewFile();
            }
            OutputStream out = new FileOutputStream(desFile);
            byte[] buffer = new byte[Flags.FLAG5];
            while (true) {
                int realLength = in.read(buffer);
                if (realLength <= 0) {
                    break;
                }
                out.write(buffer, 0, realLength);
            }
            in.close();
            out.close();
        }
    }

    private void checkVersionAndDownLoad() {
        Log.e("weige", "checkVersionAndDownLoad");
        this.mProgress = new ProgressDialog(this.context);
        this.mProgress.setMessage(this.context.getString(C0136R.string.imWait));
        this.mProgress.show();
        new Thread(new C05633()).start();
    }

    private void checkSDcarVersion() {
        Log.e("weizewei", "checkSDcarVersion");
        this.mProgress = new ProgressDialog(this.context);
        this.mProgress.setMessage(this.context.getString(C0136R.string.imWait));
        this.mProgress.show();
        new Thread(new C05644()).start();
    }

    private double getMaxVersion(String Path) {
        File[] files = new File(Path).listFiles();
        if (files == null) {
            return 0.0d;
        }
        ArrayList<String> paths = new ArrayList();
        for (int i = 0; i < files.length; i += REQUEST_CONNECT_DEVICE) {
            File f = files[i];
            if (f.getName().substring(0, REQUEST_CONNECT_DEVICE).equalsIgnoreCase("V")) {
                paths.add(f.getName());
            }
        }
        if (paths.size() == 0 || paths.size() != REQUEST_CONNECT_DEVICE) {
            return 0.0d;
        }
        return Double.parseDouble(((String) paths.get(0)).split("V")[REQUEST_CONNECT_DEVICE]);
    }

    private void copyFileFromSdcardToDataLib() throws Throwable {
        try {
            mDataDir = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 0).dataDir;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        String paths = mDataDir + "/libs/";
        if (!new File(new StringBuilder(String.valueOf(paths)).append("libAutoVinSTD.so").toString()).exists()) {
            File filePaths = new File(paths);
            if (!filePaths.exists()) {
                filePaths.mkdirs();
            }
            try {
                Copy_File.list = new ArrayList();
                Copy_File.findAllSoFile(this.mPaths);
                for (int i = 0; i < Copy_File.list.size(); i += REQUEST_CONNECT_DEVICE) {
                    InputStream fosfrom = new FileInputStream(new File((String) Copy_File.list.get(i)));
                    int lastIndexOf = ((String) Copy_File.list.get(i)).lastIndexOf(FilePathGenerator.ANDROID_DIR_SEP) + REQUEST_CONNECT_DEVICE;
                    OutputStream fosto = new FileOutputStream(new StringBuilder(String.valueOf(paths)).append(((String) Copy_File.list.get(i)).substring(r16, ((String) Copy_File.list.get(i)).length())).toString());
                    byte[] bt = new byte[Flags.FLAG5];
                    while (true) {
                        int c = fosfrom.read(bt);
                        if (c <= 0) {
                            break;
                        }
                        fosto.write(bt, 0, c);
                    }
                    fosfrom.close();
                    fosto.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        loadLibary();
    }
}

package com.ifoer.ui;

import CRP.serialport.SerialPortManager;
import CRP.utils.CRPTools;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.car.result.DownloadBinResult;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.cnmobi.im.dto.Msg;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.db.DBDao;
import com.ifoer.download.DownloadBinUpdate;
import com.ifoer.entity.ApkVersionInfo;
import com.ifoer.entity.Constant;
import com.ifoer.entity.SoftMaxVersion;
import com.ifoer.expedition.BluetoothChat.BluetoothChatService;
import com.ifoer.expedition.BluetoothOrder.ByteHexHelper;
import com.ifoer.expedition.client.DbscarUpgradeUtil;
import com.ifoer.expeditionphone.DownloadBinActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.expeditionphone.RegRuleActivity;
import com.ifoer.expeditionphone.SerialNumberActivity;
import com.ifoer.util.Common;
import com.ifoer.util.DataCleanManager;
import com.ifoer.util.Files;
import com.ifoer.util.MyApkUpdate;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.webservice.UpdateSoftware;
import com.ifoer.webservice.X431PadDiagSoftServiceCodes;
import com.launch.service.BundleBuilder;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import org.apache.http.util.EncodingUtils;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.vudroid.pdfdroid.PDFManager;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class MoreActivity extends Activity implements OnClickListener {
    public static final String DEVICE_NAME = "/dev/mtgpio";
    public static String DOWNLOAD_BIN_BASE_VERSION = null;
    private static final int ISNEW = 100;
    private static String LOCAL_APK_PATH = null;
    private static final int NOUPGRADE = 101;
    public static ProgressDialog binProgressDialogs;
    public static boolean hasDownLoad;
    boolean F1IsDown;
    AlertDialog aboutDialog;
    private View aboutDialogView;
    private Button aboutKnowBtn;
    private RelativeLayout about_btn;
    private RelativeLayout apk_upgrade;
    private RelativeLayout cleanData;
    private long clickTime;
    private TextView concerSionText;
    private Context context;
    private RelativeLayout conversion;
    private RelativeLayout devtest;
    private double downloadBinVersion;
    private RelativeLayout download_btn;
    private RelativeLayout firmware_upgrade;
    private RelativeLayout guide_btn;
    private int height;
    private PackageInfo info;
    private Intent intent;
    private boolean isUpdate;
    private String language;
    private long lastTime;
    private final Handler mHandler;
    private mBroadcastReceiver mReceiver;
    private RelativeLayout manual_btn;
    private RelativeLayout model;
    private TextView modelText;
    private IntentFilter myIntentFilter;
    private TextView repu_notic;
    public AlertDialog reputPopupWindow;
    private Button reput_btn;
    private CheckBox reput_check;
    private ImageView reput_close;
    private RelativeLayout reput_lay;
    boolean rightIsDown;
    private RelativeLayout rlPop;
    private RelativeLayout statment;
    private View tipsView;
    private RelativeLayout user_rules;
    private String verLocal;
    private TextView version;
    private int width;

    /* renamed from: com.ifoer.ui.MoreActivity.1 */
    class C07081 extends Handler {
        C07081() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    if (MoreActivity.binProgressDialogs != null && MoreActivity.binProgressDialogs.isShowing()) {
                        MoreActivity.binProgressDialogs.dismiss();
                    }
                    Toast.makeText(MoreActivity.this.context, C0136R.string.timeout, 0).show();
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    if (MoreActivity.binProgressDialogs != null && MoreActivity.binProgressDialogs.isShowing()) {
                        MoreActivity.binProgressDialogs.dismiss();
                    }
                    Toast.makeText(MoreActivity.this.context, C0136R.string.get_data_fail, 1).show();
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    MoreActivity.this.apk_upgrade.setEnabled(true);
                    Toast.makeText(MoreActivity.this.context, MoreActivity.this.getResources().getString(C0136R.string.downloadbin_not_exist), 0).show();
                case MoreActivity.ISNEW /*100*/:
                    if (MoreActivity.binProgressDialogs != null && MoreActivity.binProgressDialogs.isShowing()) {
                        MoreActivity.binProgressDialogs.dismiss();
                    }
                    MoreActivity.this.apk_upgrade.setEnabled(true);
                    Toast.makeText(MoreActivity.this.context, C0136R.string.isnew, 0).show();
                case MoreActivity.NOUPGRADE /*101*/:
                    MoreActivity.this.apk_upgrade.setEnabled(true);
                case Constant.TIME_OUT /*800*/:
                    if (MoreActivity.binProgressDialogs != null && MoreActivity.binProgressDialogs.isShowing()) {
                        MoreActivity.binProgressDialogs.dismiss();
                    }
                    Toast.makeText(MoreActivity.this.context, C0136R.string.timeout, 1).show();
                case Constant.SETVICE_ABNORMAL /*801*/:
                    if (MoreActivity.binProgressDialogs != null && MoreActivity.binProgressDialogs.isShowing()) {
                        MoreActivity.binProgressDialogs.dismiss();
                    }
                    Toast.makeText(MoreActivity.this.context, C0136R.string.the_service_side_abnormal, 1).show();
                case Constant.LOGIN_SUCCESS /*802*/:
                    String version = MySharedPreferences.getStringValue(MoreActivity.this.context, Constants.DOWNLOADBIN);
                    if (version.length() > 0) {
                        MoreActivity.DOWNLOAD_BIN_BASE_VERSION = version;
                    }
                    new DownloadSoftware().execute(new String[0]);
                case Constant.ERROR_NETWORK /*803*/:
                    Toast.makeText(MoreActivity.this.context, C0136R.string.ERROR_NETWORK, 1).show();
                case Constant.ERROR_SERVER /*804*/:
                    MoreActivity.this.apk_upgrade.setEnabled(true);
                    if (msg.getData() != null) {
                        Toast.makeText(MoreActivity.this.context, msg.getData().getString(BundleBuilder.AskFromMessage), 1).show();
                    } else {
                        Toast.makeText(MoreActivity.this.context, MoreActivity.this.context.getString(C0136R.string.ERROR_SERVER), 1).show();
                    }
                case Constant.ERROR_CODE /*805*/:
                    if (msg.getData() == null || msg.getData().getString(BundleBuilder.AskFromMessage) == null || msg.getData().getString(BundleBuilder.AskFromMessage).equals(XmlPullParser.NO_NAMESPACE)) {
                        Toast.makeText(MoreActivity.this.context, MoreActivity.this.getString(C0136R.string.error_server), 0).show();
                    } else {
                        Toast.makeText(MoreActivity.this.context, msg.getData().getString(BundleBuilder.AskFromMessage), 0).show();
                    }
                default:
            }
        }
    }

    /* renamed from: com.ifoer.ui.MoreActivity.2 */
    class C07092 implements OnClickListener {
        C07092() {
        }

        public void onClick(View arg0) {
            if (!DataCleanManager.cleanInternalCache(MoreActivity.this.context)) {
                Toast.makeText(MoreActivity.this.context, MoreActivity.this.context.getString(C0136R.string.clean_failed), 0).show();
            } else if (DataCleanManager.cleanLibs(MoreActivity.this.context)) {
                Toast.makeText(MoreActivity.this.context, MoreActivity.this.context.getString(C0136R.string.clean_success), 0).show();
            } else {
                Toast.makeText(MoreActivity.this.context, MoreActivity.this.context.getString(C0136R.string.clean_failed), 0).show();
            }
        }
    }

    /* renamed from: com.ifoer.ui.MoreActivity.3 */
    class C07103 implements OnKeyListener {
        C07103() {
        }

        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode != 4) {
                return false;
            }
            MoreActivity.this.reputPopupWindow.dismiss();
            return true;
        }
    }

    /* renamed from: com.ifoer.ui.MoreActivity.4 */
    class C07114 implements OnClickListener {
        C07114() {
        }

        public void onClick(View arg0) {
            MySharedPreferences.setBoolean(MoreActivity.this.context, MySharedPreferences.IfShowResponsibilityKey, false);
            MoreActivity.this.reputPopupWindow.dismiss();
        }
    }

    /* renamed from: com.ifoer.ui.MoreActivity.5 */
    class C07125 implements OnClickListener {
        C07125() {
        }

        public void onClick(View arg0) {
            MoreActivity.this.reputPopupWindow.dismiss();
        }
    }

    /* renamed from: com.ifoer.ui.MoreActivity.6 */
    class C07136 implements OnClickListener {
        C07136() {
        }

        public void onClick(View v) {
            MoreActivity.this.aboutDialog.dismiss();
        }
    }

    /* renamed from: com.ifoer.ui.MoreActivity.7 */
    class C07147 implements OnClickListener {
        C07147() {
        }

        public void onClick(View v) {
            MoreActivity.this.aboutDialog.dismiss();
        }
    }

    /* renamed from: com.ifoer.ui.MoreActivity.8 */
    class C07158 implements DialogInterface.OnClickListener {
        C07158() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            MoreActivity.this.isUpdate = false;
            new DownloadBinUpdate(MoreActivity.this, Constant.mChatService, MoreActivity.this.mHandler, false).checkUpdateAsync();
        }
    }

    /* renamed from: com.ifoer.ui.MoreActivity.9 */
    class C07169 implements DialogInterface.OnClickListener {
        C07169() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            MoreActivity.this.isUpdate = false;
        }
    }

    class DownloadSoftware extends AsyncTask<String, String, DownloadBinResult> {
        DownloadSoftware() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            MoreActivity.binProgressDialogs = new ProgressDialog(MoreActivity.this.context);
            MoreActivity.binProgressDialogs.setMessage(MoreActivity.this.getResources().getString(C0136R.string.shopping_wait));
            MoreActivity.binProgressDialogs.setCancelable(false);
            if (MoreActivity.binProgressDialogs != null && !MoreActivity.binProgressDialogs.isShowing()) {
                MoreActivity.binProgressDialogs.show();
            }
        }

        protected DownloadBinResult doInBackground(String... params) {
            String cc = MySharedPreferences.getStringValue(MoreActivity.this.context, MySharedPreferences.CCKey);
            String productSerialNo = MySharedPreferences.getStringValue(MoreActivity.this.context, MySharedPreferences.serialNoKey);
            DownloadBinResult result = null;
            try {
                result = new UpdateSoftware().getBinFileMaxVersion(MoreActivity.this.context, cc, productSerialNo, MoreActivity.DOWNLOAD_BIN_BASE_VERSION, Constants.DEFAULT_LANGUAGE);
            } catch (SocketTimeoutException e) {
                MoreActivity.this.mHandler.obtainMessage(0).sendToTarget();
            } catch (NullPointerException e2) {
                MoreActivity.this.mHandler.obtainMessage(2).sendToTarget();
            } catch (Exception e3) {
                MoreActivity.this.mHandler.obtainMessage(2).sendToTarget();
                e3.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(DownloadBinResult result) {
            super.onPostExecute(result);
            if (result != null) {
                if (MoreActivity.binProgressDialogs != null && MoreActivity.binProgressDialogs.isShowing()) {
                    MoreActivity.binProgressDialogs.dismiss();
                }
                if (result.getCode() == 0) {
                    if (result.getVersioninfo() != null) {
                        SoftMaxVersion rs = result.getVersioninfo();
                        if (rs.getVersionNo() != null) {
                            if (Double.valueOf(MoreActivity.DOWNLOAD_BIN_BASE_VERSION).doubleValue() < Double.valueOf(rs.getVersionNo()).doubleValue()) {
                                Intent intent = new Intent(MoreActivity.this.context, DownloadBinActivity.class);
                                intent.putExtra("downloadBinInfo", rs);
                                MoreActivity.hasDownLoad = true;
                                MoreActivity.this.startActivity(intent);
                                return;
                            }
                            MoreActivity.this.mHandler.sendEmptyMessage(MoreActivity.ISNEW);
                        } else if (rs.getMessage() == null || rs.getMessage().equals(XmlPullParser.NO_NAMESPACE)) {
                            Toast.makeText(MoreActivity.this.context, C0136R.string.isnew, 0).show();
                        } else {
                            Bundle data = new Bundle();
                            data.putString(BundleBuilder.AskFromMessage, rs.getMessage());
                            Message msg = new Message();
                            msg.what = Constant.ERROR_SERVER;
                            msg.setData(data);
                            MoreActivity.this.mHandler.sendMessage(msg);
                        }
                    }
                } else if (result.getCode() == MyHttpException.ERROR_RESULT_NOT_EXIST) {
                    Toast.makeText(MoreActivity.this.context, C0136R.string.isnew, 0).show();
                } else {
                    Toast.makeText(MoreActivity.this.context, new X431PadDiagSoftServiceCodes().queryLatestDiagSoftsCode(result.getCode(), MoreActivity.this), 0).show();
                }
            } else if (MoreActivity.binProgressDialogs != null && MoreActivity.binProgressDialogs.isShowing()) {
                MoreActivity.binProgressDialogs.dismiss();
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                MoreActivity.this.finish();
                MoreActivity.this.overridePendingTransition(0, 0);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - MoreActivity.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
                if (MoreActivity.hasDownLoad) {
                    MyApplication.getInstance().exitActivity("com.ifoer.expeditionphone.DownloadBinActivity");
                }
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                MoreActivity.this.lastTime = System.currentTimeMillis();
            }
            if (intent.getAction().equals("upgrade")) {
                MoreActivity.this.firmUpdate();
            }
        }
    }

    public MoreActivity() {
        this.width = 0;
        this.height = 0;
        this.isUpdate = false;
        this.mHandler = new C07081();
        this.F1IsDown = false;
        this.rightIsDown = false;
    }

    static {
        DOWNLOAD_BIN_BASE_VERSION = "00.00";
        LOCAL_APK_PATH = XmlPullParser.NO_NAMESPACE;
        hasDownLoad = false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.more_popa);
        this.context = this;
        init(this.context);
        registerBoradcastReceiver();
    }

    private void init(Context context) {
        LOCAL_APK_PATH = Environment.getExternalStorageDirectory() + "/cnlaunch/CRP229";
        this.language = Files.getLanguage();
        try {
            this.info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            this.verLocal = "Ver: " + this.info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        initView();
    }

    private void initView() {
        this.aboutDialog = new Builder(this.context).create();
        this.devtest = (RelativeLayout) findViewById(C0136R.id.gm_devtest);
        this.devtest.setOnClickListener(this);
        this.guide_btn = (RelativeLayout) findViewById(C0136R.id.content_lay1);
        this.guide_btn.setOnClickListener(this);
        this.guide_btn.setFocusable(true);
        this.about_btn = (RelativeLayout) findViewById(C0136R.id.about);
        this.about_btn.setOnClickListener(this);
        this.download_btn = (RelativeLayout) findViewById(C0136R.id.download);
        this.download_btn.setOnClickListener(this);
        this.manual_btn = (RelativeLayout) findViewById(C0136R.id.manual);
        this.manual_btn.setOnClickListener(this);
        this.apk_upgrade = (RelativeLayout) findViewById(C0136R.id.check_version);
        this.apk_upgrade.setOnClickListener(this);
        this.user_rules = (RelativeLayout) findViewById(C0136R.id.more_rulesss);
        this.user_rules.setOnClickListener(this);
        this.statment = (RelativeLayout) findViewById(C0136R.id.rule_statment);
        this.statment.setOnClickListener(this);
        this.firmware_upgrade = (RelativeLayout) findViewById(C0136R.id.firmware_upgrade);
        this.firmware_upgrade.setOnClickListener(this);
        this.conversion = (RelativeLayout) findViewById(C0136R.id.conversion);
        this.conversion.setOnClickListener(this);
        this.concerSionText = (TextView) findViewById(C0136R.id.text_conversion);
        if (MySharedPreferences.getStringValue(this.context, MySharedPreferences.conversion) == null || MySharedPreferences.getStringValue(this.context, MySharedPreferences.conversion).equals(XmlPullParser.NO_NAMESPACE)) {
            this.concerSionText.setText(C0136R.string.conversion_Metric);
            MySharedPreferences.setString(this.context, MySharedPreferences.conversion, "Metric");
        } else if (MySharedPreferences.getStringValue(this.context, MySharedPreferences.conversion).equals("Imperial")) {
            this.concerSionText.setText(C0136R.string.conversion_Imperial);
            MySharedPreferences.setString(this.context, MySharedPreferences.conversion, "Imperial");
        } else {
            this.concerSionText.setText(C0136R.string.conversion_Metric);
            MySharedPreferences.setString(this.context, MySharedPreferences.conversion, "Metric");
        }
        this.model = (RelativeLayout) findViewById(C0136R.id.model);
        this.modelText = (TextView) findViewById(C0136R.id.text_model);
        this.model.setOnClickListener(this);
        if (MySharedPreferences.getIntValue(this.context, MySharedPreferences.model) == 0) {
            this.modelText.setText(C0136R.string.model_normal);
        } else if (MySharedPreferences.getIntValue(this.context, MySharedPreferences.model) == 1) {
            this.modelText.setText(C0136R.string.model_vin);
        }
        this.cleanData = (RelativeLayout) findViewById(C0136R.id.clean);
        this.cleanData.setOnClickListener(new C07092());
    }

    public void initPopupWindow() {
        this.reputPopupWindow = new Builder(this.context).create();
        this.reputPopupWindow.setCanceledOnTouchOutside(false);
        this.reputPopupWindow.setView(reputDialogView());
        this.reputPopupWindow.show();
        this.reputPopupWindow.setOnKeyListener(new C07103());
        this.reputPopupWindow.setContentView(reputDialogView(), new LayoutParams(-1, -1));
    }

    private View reputDialogView() {
        this.reput_lay = (RelativeLayout) LayoutInflater.from(this.context).inflate(C0136R.layout.reputation_pop, null);
        this.rlPop = (RelativeLayout) this.reput_lay.findViewById(C0136R.id.rl_popdialog);
        if (getResources().getConfiguration().orientation == 2) {
            this.rlPop.setPadding(0, 60, 0, 60);
        } else {
            this.rlPop.setPadding(30, Opcodes.FCMPG, 30, Opcodes.FCMPG);
        }
        this.reput_close = (ImageView) this.reput_lay.findViewById(C0136R.id.repu_close);
        this.reput_check = (CheckBox) this.reput_lay.findViewById(C0136R.id.repu_select);
        this.repu_notic = (TextView) this.reput_lay.findViewById(C0136R.id.repu_notic);
        this.reput_check.setVisibility(8);
        this.repu_notic.setVisibility(8);
        this.reput_btn = (Button) this.reput_lay.findViewById(C0136R.id.repu_btn);
        this.reput_btn.setOnClickListener(new C07114());
        this.reput_close.setOnClickListener(new C07125());
        return this.reput_lay;
    }

    private View AboutDialog() {
        Bundle bundleSN = getDeviceSN();
        Bundle bundleTime = getBuildTime();
        WindowManager wm = (WindowManager) this.context.getSystemService("window");
        this.width = wm.getDefaultDisplay().getWidth();
        this.height = wm.getDefaultDisplay().getHeight();
        this.aboutDialogView = LayoutInflater.from(this.context).inflate(C0136R.layout.about_pop, null, false);
        this.version = (TextView) this.aboutDialogView.findViewById(C0136R.id.about_text);
        String info = XmlPullParser.NO_NAMESPACE;
        if (bundleSN != null) {
            info = new StringBuilder(String.valueOf(this.context.getResources().getString(C0136R.string.about_pop_text))).append("SN: ").append(bundleSN.getString("sn")).append(SpecilApiUtil.LINE_SEP).append("ID: ").append(bundleSN.getString(LocaleUtil.INDONESIAN)).append("\n\n").append(this.verLocal).append(SpecilApiUtil.LINE_SEP).append(Constant.buildStr.substring(0, 6)).append(bundleTime.getString(Msg.TIME_REDIO)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(this.context.getResources().getString(C0136R.string.about_web)).toString();
        } else {
            info = new StringBuilder(String.valueOf(this.context.getResources().getString(C0136R.string.about_pop_text))).append(SpecilApiUtil.LINE_SEP).append(this.verLocal).append(SpecilApiUtil.LINE_SEP).append(Constant.buildStr.substring(0, 6)).append(bundleTime.getString(Msg.TIME_REDIO)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(this.context.getResources().getString(C0136R.string.about_web)).toString();
        }
        this.version.setText(info);
        this.aboutKnowBtn = (Button) this.aboutDialogView.findViewById(C0136R.id.ikonwBtn);
        this.aboutKnowBtn.setOnClickListener(new C07136());
        return this.aboutDialogView;
    }

    public Bundle getDeviceSN() {
        String dataPath = Environment.getExternalStorageDirectory() + CRPTools.CRP_SERIALPORTINFO_VERSION;
        File deviceInfo = new File(dataPath);
        String res = XmlPullParser.NO_NAMESPACE;
        Bundle bundle_SNInfo = new Bundle();
        if (deviceInfo.exists()) {
            try {
                FileInputStream fin = new FileInputStream(dataPath);
                byte[] buffer = new byte[fin.available()];
                fin.read(buffer);
                res = EncodingUtils.getString(buffer, AsyncHttpResponseHandler.DEFAULT_CHARSET);
                fin.close();
                String[] sn = res.split("_");
                String serialPortID = sn[0];
                String serialPortNO = sn[1];
                bundle_SNInfo.putString(LocaleUtil.INDONESIAN, serialPortID);
                bundle_SNInfo.putString("sn", serialPortNO);
                return bundle_SNInfo;
            } catch (Exception e) {
                e.printStackTrace();
                return bundle_SNInfo;
            }
        }
        Toast.makeText(getApplicationContext(), "Serial number does not exist.Please check whether the file has been deleted,then run \u201cDeviceInfo\u201dapk", 0).show();
        return null;
    }

    private void checkFirmUpdate() {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            Toast.makeText(this.context, getResources().getString(C0136R.string.sdcard), 0).show();
        } else if (Common.isNetworkAvailable(this.context)) {
            if (TextUtils.isEmpty(MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey))) {
                SimpleDialog.ToastToLogin2(this.context, this.mHandler, binProgressDialogs);
            } else if (TextUtils.isEmpty(MySharedPreferences.getStringValue(this.context, MySharedPreferences.serialNoKey))) {
                this.context.startActivity(new Intent(this.context, SerialNumberActivity.class));
            } else {
                version = MySharedPreferences.getStringValue(this.context, Constants.DOWNLOADBIN);
                if (version.length() > 0) {
                    DOWNLOAD_BIN_BASE_VERSION = version;
                }
                new DownloadSoftware().execute(new String[0]);
            }
        } else if (checkBinFile()) {
            version = DBDao.getInstance(this.context).getDownloadVersion(MainActivity.database);
            this.downloadBinVersion = Double.valueOf(MySharedPreferences.getStringValue(this.context, Constants.DOWNLOADBIN)).doubleValue();
            if (Double.valueOf(version).doubleValue() > this.downloadBinVersion) {
                firmUpdate();
            } else {
                Toast.makeText(this.context, getResources().getString(C0136R.string.downloadbin_not_exist), 0).show();
            }
        } else {
            Toast.makeText(this.context, getResources().getString(C0136R.string.downloadbin_not_exist), 0).show();
        }
    }

    public Bundle getBuildTime() {
        String encoding = "utf-8";
        String res = XmlPullParser.NO_NAMESPACE;
        Bundle bundle_SNInfo = new Bundle();
        try {
            InputStreamReader read = new InputStreamReader(getApplicationContext().getAssets().open("ver.h"), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = XmlPullParser.NO_NAMESPACE;
            for (int i = 0; i < 4; i++) {
                lineTxt = bufferedReader.readLine();
            }
            bundle_SNInfo.putString(Msg.TIME_REDIO, lineTxt.substring(2, lineTxt.length()));
            bufferedReader.close();
            read.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bundle_SNInfo;
    }

    private View GuideView() {
        View view = LayoutInflater.from(this.context).inflate(C0136R.layout.information_info, null, false);
        WebView mWebView = (WebView) view.findViewById(C0136R.id.wv);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.requestFocus();
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + "http://www8.cao.go.jp/okinawa/8/2012/0409-1-1.pdf");
        return view;
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.firmware_upgrade && !this.isUpdate) {
            checkFirmUpdate();
        }
        if (v.getId() == C0136R.id.gm_devtest) {
            return;
        }
        if (v.getId() == C0136R.id.about) {
            this.aboutDialog = new Builder(this.context).create();
            this.aboutDialog.show();
            this.aboutDialog.setCancelable(true);
            if (getResources().getConfiguration().orientation == 2) {
                this.aboutDialog.setContentView(AboutDialog(), new LayoutParams(this.width - 100, this.height - 70));
            } else if (getResources().getConfiguration().orientation == 1) {
                this.aboutDialog.setContentView(AboutDialog(), new LayoutParams(-1, -1));
            }
        } else if (v.getId() == C0136R.id.content_lay1) {
            if (new File(Constant.assestsPath + MySharedPreferences.getStringValue(this.context, "CRP229_GUIDE_EN")).exists()) {
                openPdf(this.context, Constant.assestsPath + MySharedPreferences.getStringValue(this.context, "CRP229_GUIDE_EN"));
            } else {
                Toast.makeText(this.context, this.context.getResources().getString(C0136R.string.main_file_null), 0).show();
            }
        } else if (v.getId() == C0136R.id.download) {
            if (this.language.equals("zh")) {
                this.intent = getPdfFileIntent(Constant.assestsPath + MySharedPreferences.getStringValue(this.context, "DOWNLOADINFO_CN"));
            } else if (this.language.equalsIgnoreCase("JP")) {
                this.intent = getPdfFileIntent(Constant.assestsPath + MySharedPreferences.getStringValue(this.context, "DOWNLOADINFO_JA"));
            } else if (this.language.equalsIgnoreCase("DE")) {
                this.intent = getPdfFileIntent(Constant.assestsPath + MySharedPreferences.getStringValue(this.context, "DOWNLOADINFO_DE"));
            } else if (this.language.equalsIgnoreCase("FR")) {
                this.intent = getPdfFileIntent(Constant.assestsPath + MySharedPreferences.getStringValue(this.context, "DOWNLOADINFO_FR"));
            } else if (this.language.equalsIgnoreCase("RU")) {
                this.intent = getPdfFileIntent(Constant.assestsPath + MySharedPreferences.getStringValue(this.context, "DOWNLOADINFO_RU"));
            } else if (this.language.equalsIgnoreCase("IT")) {
                this.intent = getPdfFileIntent(Constant.assestsPath + MySharedPreferences.getStringValue(this.context, "DOWNLOADINFO_IT"));
            } else {
                this.intent = getPdfFileIntent(Constant.assestsPath + MySharedPreferences.getStringValue(this.context, "DOWNLOADINFO_EN"));
            }
            try {
                this.context.startActivity(this.intent);
            } catch (ActivityNotFoundException e) {
                tipDiaglog();
            }
        } else if (v.getId() == C0136R.id.manual) {
            if (new File(Constant.assestsPath + MySharedPreferences.getStringValue(this.context, "CRP229_MANUAL_EN")).exists() || new File(Constant.assestsPath + MySharedPreferences.getStringValue(this.context, "CRP229_MANUAL_EN")).exists()) {
                openPdf(this.context, Constant.assestsPath + MySharedPreferences.getStringValue(this.context, "CRP229_MANUAL_EN"));
            } else {
                Toast.makeText(this.context, this.context.getResources().getString(C0136R.string.main_file_null), 0).show();
            }
        } else if (v.getId() == C0136R.id.check_version) {
            this.apk_upgrade.setEnabled(false);
            if (Common.isNetworkAvailable(this.context)) {
                binProgressDialogs = new ProgressDialog(this.context);
                binProgressDialogs.setMessage(this.context.getResources().getText(C0136R.string.getting_version_info));
                binProgressDialogs.show();
                MyApkUpdate.getMyApkUpdate(this.context, this.mHandler, false).checkUpdateAsync();
                return;
            }
            checkApkUpdate();
        } else if (v.getId() == C0136R.id.more_rulesss) {
            this.context.startActivity(new Intent(this.context, RegRuleActivity.class).putExtra("isShow", false));
            ((Activity) this.context).overridePendingTransition(0, 0);
        } else if (v.getId() == C0136R.id.rule_statment) {
            initPopupWindow();
        } else if (v.getId() == C0136R.id.conversion) {
            if (MySharedPreferences.getStringValue(this.context, MySharedPreferences.conversion).equals("Metric")) {
                ((TextView) this.conversion.findViewById(C0136R.id.text_conversion)).setText(C0136R.string.conversion_Imperial);
                MySharedPreferences.setString(this.context, MySharedPreferences.conversion, "Imperial");
            } else if (MySharedPreferences.getStringValue(this.context, MySharedPreferences.conversion).equals("Imperial")) {
                ((TextView) this.conversion.findViewById(C0136R.id.text_conversion)).setText(C0136R.string.conversion_Metric);
                MySharedPreferences.setString(this.context, MySharedPreferences.conversion, "Metric");
            }
        } else if (v.getId() != C0136R.id.model) {
        } else {
            if (MySharedPreferences.getIntValue(this.context, MySharedPreferences.model) == 0) {
                this.modelText.setText(C0136R.string.model_vin);
                MySharedPreferences.setInt(this.context, MySharedPreferences.model, 1);
            } else if (MySharedPreferences.getIntValue(this.context, MySharedPreferences.model) == 1) {
                this.modelText.setText(C0136R.string.model_normal);
                MySharedPreferences.setInt(this.context, MySharedPreferences.model, 0);
            }
        }
    }

    public Intent getPdfFileIntent(String path) {
        Intent i = new Intent("android.intent.action.VIEW");
        i.addCategory("android.intent.category.DEFAULT");
        i.addFlags(268435456);
        i.setDataAndType(Uri.fromFile(new File(path)), "application/pdf");
        return i;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.aboutDialog == null) {
            this.aboutDialog = new Builder(this.context).create();
        }
        if (getResources().getConfiguration().orientation == 2) {
            this.aboutDialog.setContentView(AboutDialog(), new LayoutParams((this.width * 58) / ISNEW, (this.height * 84) / ISNEW));
        } else if (getResources().getConfiguration().orientation == 1) {
            this.aboutDialog.setContentView(AboutDialog(), new LayoutParams(-1, -1));
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == Service.CISCO_TNA || keyCode == 21) {
            this.clickTime = System.currentTimeMillis();
            this.F1IsDown = true;
        }
        if (keyCode == 22 && this.F1IsDown) {
            if ((System.currentTimeMillis() - this.clickTime) / 1000 < 3) {
                runDeviceInfo();
            } else {
                this.clickTime = 0;
                this.F1IsDown = false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void runDeviceInfo() {
        Intent myintent = new Intent();
        myintent.addFlags(268435456);
        myintent.setComponent(new ComponentName("android_serialport_api.com", "android_serialport_api.com.ManualDetection"));
        myintent.setAction("android.intent.action.VIEW");
        try {
            startActivity(myintent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this.context, getResources().getString(C0136R.string.deviceinfotip), 1).show();
        }
    }

    private View downLoadAdobeReader() {
        WindowManager wm = (WindowManager) this.context.getSystemService("window");
        this.width = wm.getDefaultDisplay().getWidth();
        this.height = wm.getDefaultDisplay().getHeight();
        this.tipsView = LayoutInflater.from(this.context).inflate(C0136R.layout.download_adobe, null, false);
        this.version = (TextView) this.tipsView.findViewById(C0136R.id.about_text);
        this.version.setText(new StringBuilder(String.valueOf(this.context.getResources().getString(C0136R.string.main_tools_pdf))).append(":").append(SpecilApiUtil.LINE_SEP).append("http://get.adobe.com/reader/otherversions/").toString());
        this.aboutKnowBtn = (Button) this.tipsView.findViewById(C0136R.id.ikonwBtn);
        this.aboutKnowBtn.setOnClickListener(new C07147());
        return this.tipsView;
    }

    private void tipDiaglog() {
        this.aboutDialog = new Builder(this.context).create();
        this.aboutDialog.show();
        this.aboutDialog.setCancelable(true);
        if (getResources().getConfiguration().orientation == 2) {
            this.aboutDialog.setContentView(downLoadAdobeReader(), new LayoutParams(this.width - 150, this.height - 100));
        } else if (getResources().getConfiguration().orientation == 1) {
            this.aboutDialog.setContentView(downLoadAdobeReader(), new LayoutParams(-1, -1));
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mReceiver != null) {
            unregisterReceiver(this.mReceiver);
        }
    }

    private void registerBoradcastReceiver() {
        this.mReceiver = new mBroadcastReceiver();
        this.myIntentFilter = new IntentFilter();
        this.myIntentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        this.myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        this.myIntentFilter.addAction("upgrade");
        registerReceiver(this.mReceiver, this.myIntentFilter);
    }

    public void firmUpdate() {
        checkConnector();
        this.isUpdate = true;
        if (MainActivity.serialPort == null) {
            try {
                MainActivity.serialPort = new SerialPortManager().getSerialPort();
            } catch (InvalidParameterException e) {
                e.printStackTrace();
            } catch (SecurityException e2) {
                e2.printStackTrace();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        if (Constant.mChatService == null) {
            Constant.mChatService = new BluetoothChatService(this, this.mHandler, false);
        }
        if (BluetoothChatService.mConnectedThread != null) {
            BluetoothChatService.mConnectedThread = null;
        }
        Constant.mChatService.connect(MainActivity.serialPort, false);
        Builder builder = new Builder(this);
        builder.setMessage(getResources().getString(C0136R.string.FirmwareUpgradeMessage));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(C0136R.string.enter), new C07158());
        builder.setNegativeButton(getResources().getString(C0136R.string.cancel), new C07169());
        builder.create().show();
    }

    private boolean checkBinFile() {
        String path = Constant.DOWBIN_UPGRADE_PATH;
        Log.e("MoreActivity", path);
        if (!new File(path).exists()) {
            return false;
        }
        boolean flag = DbscarUpgradeUtil.inspectIntegrity(path).booleanValue();
        if (flag) {
            DBDao.getInstance(this).setDownloadVersion(MainActivity.database, ByteHexHelper.replaceBlank(readFileSdcard(new StringBuilder(String.valueOf(path)).append("DOWNLOAD.ini").toString()).split("=")[1]));
            return true;
        }
        Log.e("MoreActivity", "flag" + flag);
        return false;
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
        if (!MySharedPreferences.getBooleanValue(this.context, Constants.isSanMu, false)) {
            boolean HardFlag = MySharedPreferences.getBooleanValue(this.context, Constants.hasODB, false);
            if (HardFlag && com.example.gpiomanager.MainActivity.ODB_ISOK(HardFlag) != 1) {
                com.example.gpiomanager.MainActivity.setHigh_ON();
                SystemClock.sleep(500);
            }
        } else if (com.example.gpiomanager.MainActivity.SanmuGetV(this.context) == 0) {
            com.example.gpiomanager.MainActivity.SanMusetHigh(this.context);
        }
        return true;
    }

    public void checkApkUpdate() {
        BufferedInputStream fin = null;
        try {
            int currentVer = Integer.parseInt(getPackageManager().getPackageInfo(getPackageName(), 0).versionName.replace(".", XmlPullParser.NO_NAMESPACE));
            if (new File(LOCAL_APK_PATH).isDirectory()) {
                ArrayList<ApkVersionInfo> list = CRPTools.readSdFile(LOCAL_APK_PATH, this.context, currentVer);
                if (list.size() > 0) {
                    ApkVersionInfo apkInfo = (ApkVersionInfo) list.get(list.size() - 1);
                    if (binProgressDialogs != null && binProgressDialogs.isShowing()) {
                        binProgressDialogs.dismiss();
                    }
                    String path = apkInfo.getPath();
                    Intent i1 = new Intent("android.intent.action.VIEW");
                    i1.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
                    startActivity(i1);
                } else {
                    this.mHandler.obtainMessage(5).sendToTarget();
                }
            } else {
                this.mHandler.obtainMessage(5).sendToTarget();
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

    public static void openPdf(Context context, String filePath) {
        if (new File(filePath).exists()) {
            PDFManager.open(context, filePath);
        }
    }
}

package com.ifoer.expeditionphone;

import CRP.serialport.SerialPortManager;
import android.annotation.SuppressLint;
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
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.car.result.DiagSoftIdListResult;
import com.car.result.ProductDTOResult;
import com.car.result.X431PadSoftListResult;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.cnmobi.im.util.XmppConnection;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.adapter.BaseDiagAdapter;
import com.ifoer.adapter.SpecialFunctionActivityAdapter;
import com.ifoer.db.DBDao;
import com.ifoer.db.PortThread;
import com.ifoer.db.SaveSoftId;
import com.ifoer.db.UpgradeRunnable;
import com.ifoer.dbentity.CarVersionInfo;
import com.ifoer.download.DownloadBinUpdate;
import com.ifoer.download.DownloadTask;
import com.ifoer.download.DownloadTaskManager;
import com.ifoer.download.DownloadTaskManagerThread;
import com.ifoer.entity.Constant;
import com.ifoer.entity.DiagSoftIdDTO;
import com.ifoer.entity.ProductDTO;
import com.ifoer.entity.SptActiveTest;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptInputNumric;
import com.ifoer.entity.SptInputStringEx;
import com.ifoer.entity.SptMessageBoxText;
import com.ifoer.entity.SptStreamSelectIdItem;
import com.ifoer.entity.SptTroubleTest;
import com.ifoer.entity.SptVwDataStreamIdItem;
import com.ifoer.entity.Spt_Nobuttonbox_Text;
import com.ifoer.entity.X431PadSoftDTO;
import com.ifoer.event.OnScrollCompleteListener;
import com.ifoer.event.ScrollEvent;
import com.ifoer.expedition.BluetoothChat.ActiveTestActivity;
import com.ifoer.expedition.BluetoothChat.BluetoothChatService;
import com.ifoer.expedition.BluetoothChat.CarDiagnoseActivity;
import com.ifoer.expedition.BluetoothChat.DataStreamActivity;
import com.ifoer.expedition.BluetoothChat.DeviceListActivity;
import com.ifoer.expedition.BluetoothChat.FaultCodeActivity;
import com.ifoer.expedition.BluetoothChat.FaultCodeFrozenActivity;
import com.ifoer.expedition.BluetoothChat.StreamSelectActivity;
import com.ifoer.expedition.BluetoothChat.VWDataStreamActivity;
import com.ifoer.expedition.client.ServiceManager;
import com.ifoer.expeditionphone.inteface.IMainActivityInterface;
import com.ifoer.expeditionphone.vin.VinPhotoActivity;
import com.ifoer.freememory.FreeMemory;
import com.ifoer.mine.Contact;
import com.ifoer.serialport.SerialPort;
import com.ifoer.ui.FastDiagnosisActivity;
import com.ifoer.ui.MainMenuActivity;
import com.ifoer.util.AndroidToLan;
import com.ifoer.util.Common;
import com.ifoer.util.Copy_File;
import com.ifoer.util.DataCleanManager;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.MySoftUpdate;
import com.ifoer.util.SimpleDialog;
import com.ifoer.view.MenuSelectPicPopupWindow;
import com.ifoer.view.Panel;
import com.ifoer.view.Panel.PanelClosedEvent;
import com.ifoer.view.Panel.PanelOpenedEvent;
import com.ifoer.view.SlidePointView;
import com.ifoer.view.WorkSpace;
import com.ifoer.webservice.KeyToLogin;
import com.ifoer.webservice.ProductService;
import com.ifoer.webservice.X431PadDiagSoftService;
import com.launch.rcu.socket.SocketCall;
import com.launch.service.BundleBuilder;
import com.launch.thread.SocketSendThread;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import com.thoughtworks.xstream.XStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.SocketTimeoutException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimerTask;
import org.apache.harmony.javax.security.auth.callback.ConfirmationCallback;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smackx.bytestreams.ibb.packet.DataPacketExtension;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

@SuppressLint({"NewApi"})
public class MainActivity extends BaseActivity implements OnClickListener, OnItemClickListener, OnScrollCompleteListener, IMainActivityInterface {
    private static final boolean f2129D = true;
    public static final String DEVICE_NAME = "device_name";
    public static int ENLARGE_WIDTH = 0;
    private static String FROMPATH = null;
    public static final String IfShowDialog = "IfShowDialog";
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_FIRST_TOAST = 8;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_SHOW_MENU = 1010;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_WRITE = 3;
    public static final int REFRESH_UI = 7;
    public static final int REQUEST_CIRCLE_ADD_IMAGE = 3;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    public static final int REQUEST_ENABLE_BT = 2;
    public static final int REQUEST_USER_LOGIN_OUT = 25;
    private static final String TAG = "MainAcitivty";
    public static final String TOAST = "toast";
    public static Context contexts;
    public static String country;
    public static String dataDir;
    public static SQLiteDatabase database;
    public static boolean hasLoadLib;
    public static String imageStr;
    public static Intent mKeyToUpgradeIntent;
    public static Intent mMainActivityIntent;
    public static int mSlidePoint;
    public static View mSpaceOrderLayoutView;
    public static MenuSelectPicPopupWindow menuWindow;
    public static Panel panel;
    public static int resetConnectTimes;
    public static SerialPort serialPort;
    public static ServiceManager serviceManager;
    public static boolean showNewFlag;
    public static SocketCall socketCall;
    public static SocketSendThread socketSendThread;
    private GridView SpecialGridView;
    private AlertDialog aboutDialog;
    private Button aboutKnowBtn;
    private List<HashMap<String, Object>> americaLists;
    private List<HashMap<String, Object>> asiaList;
    private Button btn_login;
    public List<HashMap<String, Object>> carList;
    private String carTypeSearch;
    private String cc;
    private List<HashMap<String, Object>> chinaList;
    private String clickedViewItemName;
    private int clickedViewItemSoftId;
    private int connectTimes;
    private ProgressDialog connextProgressDialog;
    private LinearLayout container;
    private RelativeLayout contentsss;
    private LinearLayout derma;
    public Builder dialog;
    Handler downHandler;
    private int downLoadCount;
    private List<HashMap<String, Object>> euroList;
    private String getSerialNoProductType;
    private int height1;
    private int initSlidingFlag;
    private Intent intent;
    private Boolean isExecuteM;
    public boolean isShowExist;
    int lanId;
    String lanName;
    public String language;
    private long lastTime;
    public View leftMenu;
    List<String> listText;
    private boolean loadSpecail;
    private BaseDiagAdapter mAmericaBaseDiagAdapter;
    private GridView mAmericaCarGridView;
    private BaseDiagAdapter mAsiaBaseDiagAdapter;
    private GridView mAsiaCarGridView;
    private BaseDiagAdapter mChinaBaseDiagAdapter;
    private GridView mChinaCarGridView;
    private String mConnectedDeviceName;
    private BaseDiagAdapter mEuroBaseDiagAdapter;
    private GridView mEuroCarGridView;
    final Handler mHandler;
    private LinearLayout mRegCloseLinearLayout;
    private AlertDialog mRegDialog;
    private ProgressBar mRegDownProgressBar;
    private TextView mRegDownTextView;
    private SlidePointView mSlidePoinView;
    private SpecialFunctionActivityAdapter mSpecilaBaseDiagAdapeter;
    private WorkSpace mWorkSpace;
    private RelativeLayout mainLayout;
    private RelativeLayout main_head;
    private ImageView menuBg;
    public LinearLayout menuBtn;
    public IntentFilter myIntentFilter;
    private Button mzCar;
    private TextView name;
    private Button ozCar;
    private String packgeId;
    PanelClosedEvent panelClosedEvent;
    PanelOpenedEvent panelOpenedEvent;
    private PopupWindow portPopupWindow;
    private List<ProductDTO> productDTOs;
    private String productType;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialogs;
    public mBroadcastReceiver receiver;
    public AlertDialog reputPopupWindow;
    private Button reput_btn;
    private CheckBox reput_check;
    private ImageView reput_close;
    private RelativeLayout reput_lay;
    private ProductDTOResult res;
    private RelativeLayout rlPop;
    private int screenHeight;
    private EditText searchEdit;
    private String serialNo;
    private Intent service;
    private int showDialogFlag;
    private TextView show_name;
    private TextView show_new;
    private View sligdingView;
    private DiagSoftIdListResult softIdListRes;
    private X431PadSoftListResult softListRexult;
    private List<HashMap<String, Object>> specialList;
    private int statusBarHeight;
    private View tipsView;
    private Button toSearch;
    public AlertDialog toastPopupWindow;
    private Button toast_btn;
    private CheckBox toast_check;
    private RelativeLayout toast_lay;
    private String token;
    private TextView version;
    private int width1;
    private Button yzCar;
    private Button zgCar;

    /* renamed from: com.ifoer.expeditionphone.MainActivity.3 */
    class C05893 extends Handler {
        C05893() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    MainActivity.this.mRegCloseLinearLayout.setVisibility(0);
                    Toast toast2 = new Toast(MainActivity.this);
                    toast2.setGravity(17, 0, 0);
                    toast2.setDuration(100);
                    View s = MainActivity.this.getLayoutInflater().inflate(C0136R.layout.cust_toast, null);
                    toast2.setView(s);
                    ((TextView) s.findViewById(C0136R.id.sucess)).setText(C0136R.string.download_fail);
                    toast2.show();
                case MainActivity.REQUEST_CONNECT_DEVICE /*1*/:
                    MainActivity.this.mRegDownProgressBar.setProgress(msg.arg1);
                case MainActivity.REQUEST_ENABLE_BT /*2*/:
                    MainActivity.this.mRegDialog.dismiss();
                case MainActivity.MESSAGE_DEVICE_NAME /*4*/:
                    Toast toast = new Toast(MainActivity.this);
                    toast.setGravity(17, 0, 0);
                    toast.setDuration(100);
                    toast.setView(MainActivity.this.getLayoutInflater().inflate(C0136R.layout.cust_toast, null));
                    toast.show();
                case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                    Toast.makeText(MainActivity.this, C0136R.string.the_service_side_abnormal, MainActivity.REQUEST_CONNECT_DEVICE).show();
                case MainActivity.REFRESH_UI /*7*/:
                    MainActivity.this.mRegDownProgressBar.setMax(msg.arg1);
                    MainActivity.this.mRegDownTextView.setText(new StringBuilder(String.valueOf(MainActivity.this.getString(C0136R.string.updateApk_now))).append(msg.obj).append(MainActivity.this.getString(C0136R.string.diagnostic_software)).toString());
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.MainActivity.4 */
    class C05904 extends Handler {
        C05904() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MainActivity.REQUEST_CONNECT_DEVICE /*1*/:
                    switch (msg.arg1) {
                        case MainActivity.REQUEST_CONNECT_DEVICE /*1*/:
                            if (MainActivity.this.connextProgressDialog != null && MainActivity.this.connextProgressDialog.isShowing() && MainActivity.resetConnectTimes == MainActivity.REQUEST_CONNECT_DEVICE) {
                                MainActivity.this.connextProgressDialog.dismiss();
                            }
                            MainActivity.this.sendBroadcast(new Intent("STATE_LISTEN"));
                        case MainActivity.REQUEST_ENABLE_BT /*2*/:
                        case MainActivity.REQUEST_CIRCLE_ADD_IMAGE /*3*/:
                        default:
                    }
                case MainActivity.MESSAGE_DEVICE_NAME /*4*/:
                    MainActivity.this.mConnectedDeviceName = msg.getData().getString(MainActivity.DEVICE_NAME);
                    MainActivity.this.sendBroadcast(new Intent("Connected"));
                    if (MainActivity.this.connextProgressDialog != null && MainActivity.this.connextProgressDialog.isShowing()) {
                        MainActivity.this.connextProgressDialog.dismiss();
                    }
                case MainActivity.MESSAGE_TOAST /*5*/:
                    SimpleDialog.closeProgressDialog(MainActivity.this.progressDialog);
                    Toast.makeText(MainActivity.this, msg.getData().getString(MainActivity.TOAST), 0).show();
                    MainActivity.this.sendBroadcast(new Intent("NotConnected"));
                case MainActivity.REFRESH_UI /*7*/:
                    MainActivity.this.refreshUi();
                case MainActivity.MESSAGE_FIRST_TOAST /*8*/:
                    SimpleDialog.closeProgressDialog(MainActivity.this.progressDialog);
                    MainActivity.this.sendBroadcast(new Intent("FirstNotConnected"));
                case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                    if (MainActivity.this.mChinaBaseDiagAdapter == null) {
                        MainActivity.this.mChinaBaseDiagAdapter = MainActivity.this.createBaseDiagAdapter(MainActivity.this.chinaList);
                        MainActivity.this.mChinaCarGridView.setAdapter(MainActivity.this.mChinaBaseDiagAdapter);
                        return;
                    }
                    MainActivity.this.mChinaBaseDiagAdapter.refresh(MainActivity.this.chinaList);
                case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                    if (MainActivity.this.mAsiaBaseDiagAdapter == null) {
                        MainActivity.this.mAsiaBaseDiagAdapter = MainActivity.this.createBaseDiagAdapter(MainActivity.this.asiaList);
                        MainActivity.this.mAsiaCarGridView.setAdapter(MainActivity.this.mAsiaBaseDiagAdapter);
                        return;
                    }
                    MainActivity.this.mAsiaBaseDiagAdapter.refresh(MainActivity.this.asiaList);
                case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                    if (MainActivity.this.mEuroBaseDiagAdapter == null) {
                        MainActivity.this.mEuroBaseDiagAdapter = MainActivity.this.createBaseDiagAdapter(MainActivity.this.euroList);
                        MainActivity.this.mEuroCarGridView.setAdapter(MainActivity.this.mEuroBaseDiagAdapter);
                        return;
                    }
                    MainActivity.this.mEuroBaseDiagAdapter.refresh(MainActivity.this.euroList);
                case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                    if (MainActivity.this.mAmericaBaseDiagAdapter == null) {
                        MainActivity.this.mAmericaBaseDiagAdapter = MainActivity.this.createBaseDiagAdapter(MainActivity.this.americaLists);
                        MainActivity.this.mAmericaCarGridView.setAdapter(MainActivity.this.mAmericaBaseDiagAdapter);
                        return;
                    }
                    MainActivity.this.mAmericaBaseDiagAdapter.refresh(MainActivity.this.americaLists);
                case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                    MainActivity.this.progressDialog.dismiss();
                    Toast.makeText(MainActivity.contexts, C0136R.string.timeout, MainActivity.REQUEST_CONNECT_DEVICE).show();
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    if (MainActivity.this.progressDialog != null && MainActivity.this.progressDialog.isShowing()) {
                        MainActivity.this.progressDialog.dismiss();
                    }
                    Toast.makeText(MainActivity.contexts, C0136R.string.get_data_fail, MainActivity.REQUEST_CONNECT_DEVICE).show();
                case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
                    if (MainActivity.this.mAsiaBaseDiagAdapter == null) {
                        MainActivity.this.mAsiaBaseDiagAdapter = MainActivity.this.createBaseDiagAdapter(MainActivity.this.asiaList);
                        MainActivity.this.mAsiaCarGridView.setAdapter(MainActivity.this.mAsiaBaseDiagAdapter);
                    } else {
                        MainActivity.this.mAsiaBaseDiagAdapter.refresh(MainActivity.this.asiaList);
                    }
                    if (MainActivity.this.mAsiaBaseDiagAdapter == null) {
                        MainActivity.this.mAsiaBaseDiagAdapter = MainActivity.this.createBaseDiagAdapter(MainActivity.this.asiaList);
                        MainActivity.this.mAsiaCarGridView.setAdapter(MainActivity.this.mAsiaBaseDiagAdapter);
                    } else {
                        MainActivity.this.mAsiaBaseDiagAdapter.refresh(MainActivity.this.asiaList);
                    }
                    if (MainActivity.this.mEuroBaseDiagAdapter == null) {
                        MainActivity.this.mEuroBaseDiagAdapter = MainActivity.this.createBaseDiagAdapter(MainActivity.this.euroList);
                        MainActivity.this.mEuroCarGridView.setAdapter(MainActivity.this.mEuroBaseDiagAdapter);
                    } else {
                        MainActivity.this.mEuroBaseDiagAdapter.refresh(MainActivity.this.euroList);
                    }
                    if (MainActivity.this.mAmericaBaseDiagAdapter == null) {
                        MainActivity.this.mAmericaBaseDiagAdapter = MainActivity.this.createBaseDiagAdapter(MainActivity.this.americaLists);
                        MainActivity.this.mAmericaCarGridView.setAdapter(MainActivity.this.mAmericaBaseDiagAdapter);
                        return;
                    }
                    MainActivity.this.mAmericaBaseDiagAdapter.refresh(MainActivity.this.americaLists);
                case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                    if (MainActivity.this.mSpecilaBaseDiagAdapeter == null) {
                        MainActivity.this.mSpecilaBaseDiagAdapeter = MainActivity.this.createBaseDiagAdapter2(MainActivity.this.specialList);
                        MainActivity.this.SpecialGridView.setAdapter(MainActivity.this.mSpecilaBaseDiagAdapeter);
                        return;
                    }
                    MainActivity.this.mSpecilaBaseDiagAdapeter.refresh(MainActivity.this.specialList);
                case Constant.TIME_OUT /*800*/:
                    if (MainActivity.this.progressDialogs != null && MainActivity.this.progressDialogs.isShowing()) {
                        MainActivity.this.progressDialogs.dismiss();
                    }
                    Toast.makeText(MainActivity.mContexts, C0136R.string.timeout, MainActivity.REQUEST_CONNECT_DEVICE).show();
                case Constant.SETVICE_ABNORMAL /*801*/:
                    if (MainActivity.this.progressDialogs != null && MainActivity.this.progressDialogs.isShowing()) {
                        MainActivity.this.progressDialogs.dismiss();
                    }
                    Toast.makeText(MainActivity.mContexts, C0136R.string.the_service_side_abnormal, MainActivity.REQUEST_CONNECT_DEVICE).show();
                case Constant.ERROR_NETWORK /*803*/:
                    Toast.makeText(MainActivity.mContexts, C0136R.string.ERROR_NETWORK, MainActivity.REQUEST_CONNECT_DEVICE).show();
                case Constant.ERROR_SERVER /*804*/:
                    Toast.makeText(MainActivity.mContexts, MainActivity.mContexts.getString(C0136R.string.ERROR_SERVER), MainActivity.REQUEST_CONNECT_DEVICE).show();
                case Constant.ERROR_CODE /*805*/:
                    if (msg.getData() == null || msg.getData().getString(BundleBuilder.AskFromMessage) == null || msg.getData().getString(BundleBuilder.AskFromMessage).equals(XmlPullParser.NO_NAMESPACE)) {
                        Toast.makeText(MainActivity.mContexts, MainActivity.this.getString(C0136R.string.error_server), 0).show();
                    } else {
                        Toast.makeText(MainActivity.mContexts, msg.getData().getString(BundleBuilder.AskFromMessage), 0).show();
                    }
                case MainActivity.MESSAGE_SHOW_MENU /*1010*/:
                    MainActivity.this.showMenu(MainActivity.contexts);
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.MainActivity.5 */
    class C05915 implements OnClickListener {
        C05915() {
        }

        public void onClick(View v) {
            new KeyToLogin(MainActivity.mContexts, MainActivity.this.mHandler, MainActivity.this.progressDialogs).login();
            MainActivity.this.menuBtn.setSelected(false);
            MainActivity.this.btn_login.setSelected(MainActivity.f2129D);
        }
    }

    /* renamed from: com.ifoer.expeditionphone.MainActivity.6 */
    class C05926 implements OnClickListener {
        C05926() {
        }

        public void onClick(View v) {
            MainActivity.this.menuBtn.setSelected(MainActivity.f2129D);
            MainActivity.this.btn_login.setSelected(false);
            MainActivity.this.finish();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.MainActivity.7 */
    class C05937 implements TextWatcher {
        C05937() {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                MainActivity.this.carTypeSearch = XmlPullParser.NO_NAMESPACE;
                MainActivity.this.refreshUi();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.MainActivity.8 */
    class C05948 implements OnClickListener {
        C05948() {
        }

        public void onClick(View v) {
            MainActivity.this.carTypeSearch = MainActivity.this.searchEdit.getText().toString();
            MainActivity.this.refreshUi();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.MainActivity.9 */
    class C05959 implements OnKeyListener {
        C05959() {
        }

        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode != MainActivity.MESSAGE_DEVICE_NAME) {
                return false;
            }
            MySharedPreferences.share.getBoolean(MySharedPreferences.IfShowNoticKey, MainActivity.f2129D);
            MySharedPreferences.setString(MainActivity.contexts, "showNoticKey", Contact.RELATION_FRIEND);
            MainActivity.this.toastPopupWindow.dismiss();
            return MainActivity.f2129D;
        }
    }

    class GetAmerica extends Thread {
        GetAmerica() {
        }

        public void run() {
            boolean isopen;
            String serialNo = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.serialNoKey);
            String ifshow = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.IfShow);
            if (ifshow == null || !ifshow.equals(Contact.RELATION_FRIEND)) {
                isopen = false;
            } else {
                isopen = MainActivity.f2129D;
            }
            MainActivity.this.americaLists = MainActivity.this.GetAmericaLists(serialNo, Boolean.valueOf(isopen), MainActivity.this.carTypeSearch);
            MainActivity.this.mHandler.obtainMessage(13).sendToTarget();
        }
    }

    class GetAsia extends Thread {
        GetAsia() {
        }

        public void run() {
            boolean isopen;
            String serialNo = MySharedPreferences.getStringValue(MainActivity.this, MySharedPreferences.serialNoKey);
            String ifshow = MySharedPreferences.getStringValue(MainActivity.this, MySharedPreferences.IfShow);
            if (ifshow == null || !ifshow.equals(Contact.RELATION_FRIEND)) {
                isopen = false;
            } else {
                isopen = MainActivity.f2129D;
            }
            MainActivity.this.asiaList = MainActivity.this.GetAsiaList(serialNo, Boolean.valueOf(isopen), MainActivity.this.carTypeSearch);
            MainActivity.this.mHandler.obtainMessage(11).sendToTarget();
        }
    }

    class GetChina extends Thread {
        GetChina() {
        }

        public void run() {
            boolean isopen;
            String serialNo = MySharedPreferences.getStringValue(MainActivity.this, MySharedPreferences.serialNoKey);
            String ifshow = MySharedPreferences.getStringValue(MainActivity.this, MySharedPreferences.IfShow);
            if (ifshow == null || !ifshow.equals(Contact.RELATION_FRIEND)) {
                isopen = false;
            } else {
                isopen = MainActivity.f2129D;
            }
            MainActivity.this.chinaList = MainActivity.this.GetChinaList(serialNo, Boolean.valueOf(isopen), MainActivity.this.carTypeSearch);
            MainActivity.this.mHandler.obtainMessage(10).sendToTarget();
        }
    }

    class GetEuro extends Thread {
        GetEuro() {
        }

        public void run() {
            boolean isopen;
            String serialNo = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.serialNoKey);
            String ifshow = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.IfShow);
            if (ifshow == null || !ifshow.equals(Contact.RELATION_FRIEND)) {
                isopen = false;
            } else {
                isopen = MainActivity.f2129D;
            }
            MainActivity.this.euroList = MainActivity.this.GetEuroList(serialNo, Boolean.valueOf(isopen), MainActivity.this.carTypeSearch);
            MainActivity.this.mHandler.obtainMessage(12).sendToTarget();
        }
    }

    class GetSoftIdTask extends AsyncTask<String, String, String> {
        DiagSoftIdListResult result;

        GetSoftIdTask() {
        }

        protected String doInBackground(String... params) {
            X431PadDiagSoftService softService = new X431PadDiagSoftService();
            MainActivity.this.cc = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.CCKey);
            MainActivity.this.token = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.TokenKey);
            softService.setCc(MainActivity.this.cc);
            softService.setToken(MainActivity.this.token);
            try {
                this.result = softService.getDiagSoftIdList(MainActivity.this.productType);
                if (this.result != null && this.result.getCode() == 0 && this.result.getDiagSoftIdList() != null && this.result.getDiagSoftIdList().size() > 0) {
                    new SaveSoftId(MainActivity.this, this.result).start();
                }
            } catch (SocketTimeoutException e) {
            } catch (NullPointerException e2) {
            }
            return null;
        }
    }

    class GetSpecial extends Thread {
        GetSpecial() {
        }

        public void run() {
            boolean isopen;
            String serialNo = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.serialNoKey);
            String ifshow = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.IfShow);
            if (ifshow == null || !ifshow.equals(Contact.RELATION_FRIEND)) {
                isopen = false;
            } else {
                isopen = MainActivity.f2129D;
            }
            MainActivity.this.specialList = MainActivity.this.GetSpecialList(serialNo, Boolean.valueOf(isopen), XmlPullParser.NO_NAMESPACE);
            MainActivity.this.mHandler.obtainMessage(18).sendToTarget();
        }
    }

    class MyTimerTask extends TimerTask {
        MyTimerTask() {
        }

        public void run() {
            if (MainActivity.this.connectTimes == 0 && MainActivity.this.connextProgressDialog != null && MainActivity.this.connextProgressDialog.isShowing()) {
                MainActivity.this.connextProgressDialog.dismiss();
            }
        }
    }

    class PortNumAsy extends AsyncTask<String, String, String> {
        private int which;

        public PortNumAsy(int which) {
            this.which = which;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            ProductService productClient = new ProductService();
            MainActivity.this.cc = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.CCKey);
            MainActivity.this.token = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.TokenKey);
            productClient.setCc(MainActivity.this.cc);
            productClient.setToken(MainActivity.this.token);
            try {
                MainActivity.this.res = productClient.getRegisteredProductsForPad(MainActivity.this.getSerialNoProductType);
            } catch (SocketTimeoutException e) {
                MainActivity.this.mHandler.obtainMessage(14).sendToTarget();
            } catch (Exception e2) {
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (MainActivity.this.res != null) {
                switch (MainActivity.this.res.getCode()) {
                    case KEYRecord.OWNER_USER /*0*/:
                        MySharedPreferences.setString(MainActivity.contexts, "getSerialNo", Contact.RELATION_BACKNAME);
                        String cckey = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.CCKey);
                        if (MainActivity.this.res.getProductDTOs() != null && MainActivity.this.res.getProductDTOs().size() > 0) {
                            MainActivity.this.productDTOs = MainActivity.this.res.getProductDTOs();
                            List<HashMap<String, String>> list = new ArrayList();
                            for (ProductDTO dto : MainActivity.this.productDTOs) {
                                HashMap<String, String> map = new HashMap();
                                map.put(MultipleAddresses.CC, cckey);
                                map.put(Constants.serialNo, dto.getSerialNo());
                                list.add(map);
                            }
                            new PortThread(MainActivity.contexts, list).start();
                        }
                    default:
                }
            }
        }
    }

    class SoftIdListTask extends AsyncTask<String, String, String> {
        String icon;
        ProgressDialog progressDialog;
        int softId;
        String softName;
        String softPackageId;

        SoftIdListTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(MainActivity.contexts);
            this.progressDialog.setMessage(MainActivity.this.getString(C0136R.string.find_wait));
            this.progressDialog.setCancelable(false);
            if (!this.progressDialog.isShowing()) {
                this.progressDialog.show();
            }
        }

        protected String doInBackground(String... params) {
            X431PadDiagSoftService softService = new X431PadDiagSoftService();
            MainActivity.this.cc = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.CCKey);
            MainActivity.this.token = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.TokenKey);
            softService.setCc(MainActivity.this.cc);
            softService.setToken(MainActivity.this.token);
            this.softId = Integer.parseInt(params[0]);
            this.softPackageId = params[MainActivity.REQUEST_CONNECT_DEVICE];
            this.softName = params[MainActivity.REQUEST_ENABLE_BT];
            this.icon = params[MainActivity.REQUEST_CIRCLE_ADD_IMAGE];
            if (MainActivity.country == null || MainActivity.country.length() <= 0) {
                MainActivity.country = Locale.getDefault().getCountry();
                MainActivity.this.lanId = AndroidToLan.getLanId(MainActivity.country);
            } else {
                MainActivity.this.lanId = AndroidToLan.getLanId(MainActivity.country);
            }
            try {
                String serialNo = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.serialNoKey);
                if (this.softId == 0) {
                    MainActivity.this.softIdListRes = softService.getDiagSoftIdList(MainActivity.this.productType);
                    if (MainActivity.this.softIdListRes != null && MainActivity.this.softIdListRes.getCode() == 0 && MainActivity.this.softIdListRes.getDiagSoftIdList() != null && MainActivity.this.softIdListRes.getDiagSoftIdList().size() > 0) {
                        new SaveSoftId(MainActivity.this, MainActivity.this.softIdListRes).start();
                        MainActivity.this.clickedViewItemSoftId = 0;
                        for (DiagSoftIdDTO dto : MainActivity.this.softIdListRes.getDiagSoftIdList()) {
                            if (dto.getSoftPackageId().equalsIgnoreCase(this.softPackageId)) {
                                this.softId = dto.getSoftId().intValue();
                            }
                        }
                        MainActivity.this.softListRexult = softService.getDiagSoftInfoBySoftId(serialNo, Integer.valueOf(this.softId), Integer.valueOf(MainActivity.this.lanId), Integer.valueOf(XStream.NO_REFERENCES), MainActivity.this.cc, MainActivity.this.token);
                    }
                    return null;
                }
                MainActivity.this.softListRexult = softService.getDiagSoftInfoBySoftId(serialNo, Integer.valueOf(this.softId), Integer.valueOf(MainActivity.this.lanId), Integer.valueOf(XStream.NO_REFERENCES), MainActivity.this.cc, MainActivity.this.token);
                return null;
            } catch (SocketTimeoutException e) {
                MainActivity.this.mHandler.obtainMessage(14).sendToTarget();
            } catch (NullPointerException e2) {
                MainActivity.this.mHandler.obtainMessage(15).sendToTarget();
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (MainActivity.this.softIdListRes != null) {
                if (this.progressDialog != null && this.progressDialog.isShowing()) {
                    this.progressDialog.dismiss();
                }
                switch (MainActivity.this.softIdListRes.getCode()) {
                    case ConfirmationCallback.UNSPECIFIED_OPTION /*-1*/:
                        SimpleDialog.validTokenDialog(MainActivity.this);
                        break;
                    case MyHttpException.ERROR_PARAMETER_EMPTY /*401*/:
                        Toast.makeText(MainActivity.contexts, MainActivity.this.getResources().getString(C0136R.string.notic_null), 0).show();
                        break;
                    case MyHttpException.ERROR_SERVER /*500*/:
                        Toast.makeText(MainActivity.contexts, MainActivity.this.getResources().getString(C0136R.string.notic_serv), 0).show();
                        break;
                    case MyHttpException.ERROR_EMPTY_SOFTLIST /*607*/:
                        Toast.makeText(MainActivity.contexts, MainActivity.this.getResources().getString(C0136R.string.main_softid_null), 0).show();
                        break;
                }
            } else if (this.progressDialog != null && this.progressDialog.isShowing()) {
                this.progressDialog.dismiss();
            }
            if (MainActivity.this.softListRexult != null && MainActivity.this.softListRexult.getCode() == -1) {
                SimpleDialog.validTokenDialog(MainActivity.this);
            } else if (MainActivity.this.softListRexult == null || MainActivity.this.softListRexult.getCode() != 0 || MainActivity.this.softListRexult.getDtoList().size() <= 0) {
                Toast.makeText(MainActivity.contexts, MainActivity.this.getResources().getString(C0136R.string.main_soft_null), 0).show();
            } else {
                X431PadSoftDTO dto = (X431PadSoftDTO) MainActivity.this.softListRexult.getDtoList().get(0);
                if (dto == null || Integer.parseInt(dto.getLanId()) != AndroidToLan.getLanId(MainActivity.this.lanName)) {
                    Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(C0136R.string.main_lanuage_null), 0).show();
                } else {
                    dto.getPurchased();
                }
            }
            if (this.progressDialog != null && this.progressDialog.isShowing()) {
                this.progressDialog.dismiss();
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {

        /* renamed from: com.ifoer.expeditionphone.MainActivity.mBroadcastReceiver.1 */
        class C05961 implements OnClickListener {
            C05961() {
            }

            public void onClick(View v) {
                MainActivity.this.mRegDialog.dismiss();
            }
        }

        /* renamed from: com.ifoer.expeditionphone.MainActivity.mBroadcastReceiver.2 */
        class C05972 implements Runnable {
            C05972() {
            }

            public void run() {
                String serialNo = MySharedPreferences.getStringValue(MainActivity.this, MySharedPreferences.serialNoKey);
                X431PadDiagSoftService Service = new X431PadDiagSoftService();
                List<X431PadDtoSoft> dtoList = new ArrayList();
                try {
                    MainActivity.this.lanId = AndroidToLan.getLanId(MainActivity.country);
                    MainActivity.this.cc = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.CCKey);
                    MainActivity.this.token = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.TokenKey);
                    Service.setCc(MainActivity.this.cc);
                    Service.setToken(MainActivity.this.token);
                    X431PadDtoSoft softDto = Service.getSpecifiedDiagSoftLatestInfo1(serialNo, "DEMO", Integer.valueOf(MainActivity.this.lanId), Integer.valueOf(XStream.NO_REFERENCES), MainActivity.this.downHandler);
                    X431PadDtoSoft softDto2 = Service.getSpecifiedDiagSoftLatestInfo1(serialNo, "EOBD2", Integer.valueOf(MainActivity.this.lanId), Integer.valueOf(XStream.NO_REFERENCES), MainActivity.this.downHandler);
                    dtoList.add(softDto);
                    dtoList.add(softDto2);
                    if (Environment.getExternalStorageState().equals("mounted")) {
                        DownloadTaskManager.getInstance();
                        new Thread(new DownloadTaskManagerThread()).start();
                        for (int i = 0; i < dtoList.size(); i += MainActivity.REQUEST_CONNECT_DEVICE) {
                            DownloadTaskManager.getInstance().addDownloadTask(new DownloadTask(MainActivity.this, MainActivity.this.downHandler, (X431PadDtoSoft) dtoList.get(i), serialNo));
                        }
                        return;
                    }
                    Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(C0136R.string.sd_not_use), 0).show();
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                } catch (NullPointerException e2) {
                    e2.printStackTrace();
                    MainActivity.this.downHandler.sendEmptyMessage(0);
                }
            }
        }

        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                if ((System.currentTimeMillis() - MainActivity.this.lastTime) / 1000 > 30) {
                    SimpleDialog.ExitDialog(context);
                    if (MainActivity.hasLoadLib) {
                        MyApplication.getInstance().exitActivity("com.ifoer.expeditionphone.LoadDynamicLibsActivity");
                    }
                }
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                MainActivity.this.lastTime = System.currentTimeMillis();
            }
            if (intent.getAction().equals("SHOW_DOWN_DIALOG")) {
                View view = LayoutInflater.from(context).inflate(C0136R.layout.show_down_dialog, null);
                MainActivity mainActivity = MainActivity.this;
                mainActivity.dialog = new Builder(MainActivity.this);
                MainActivity.this.dialog.setView(view);
                MainActivity.this.mRegDialog = MainActivity.this.dialog.create();
                MainActivity.this.mRegDialog.show();
                MainActivity.this.mRegDownProgressBar = (ProgressBar) view.findViewById(C0136R.id.progressBar);
                MainActivity.this.mRegCloseLinearLayout = (LinearLayout) view.findViewById(C0136R.id.repu_close);
                MainActivity.this.mRegCloseLinearLayout.setVisibility(MainActivity.MESSAGE_DEVICE_NAME);
                MainActivity.this.mRegDownTextView = (TextView) view.findViewById(C0136R.id.down_status);
                MainActivity.this.dialog.setCancelable(false);
                MainActivity.this.mRegCloseLinearLayout.setOnClickListener(new C05961());
                new Thread(new C05972()).start();
            }
            if (intent.getAction().equals("Show_names")) {
                String name = MySharedPreferences.getStringValue(context, MySharedPreferences.UserNameKey);
                if (name == null) {
                    name = XmlPullParser.NO_NAMESPACE;
                }
                MainActivity.this.btn_login.setVisibility(MainActivity.MESSAGE_FIRST_TOAST);
                MainActivity.this.show_name.setText(name);
            } else if (intent.getAction().equals("Nushow_names")) {
                MainActivity.this.show_name.setText(XmlPullParser.NO_NAMESPACE);
                MainActivity.this.btn_login.setVisibility(0);
                MainActivity.this.btn_login.setClickable(MainActivity.f2129D);
            } else if (intent.getAction().equals(DeviceListActivity.EXTRA_DEVICE_ADDRESS)) {
                Constant.mChatService.connect(MainActivity.serialPort, intent.getExtras().getBoolean("FIRSTCONNECT"));
            } else if (intent.getAction().equals("refreshUi")) {
                MainActivity.this.mHandler.obtainMessage(MainActivity.REFRESH_UI).sendToTarget();
            } else if (intent.getAction().equals("show")) {
                mainActivity = MainActivity.this;
                mainActivity.downLoadCount = mainActivity.downLoadCount + MainActivity.REQUEST_CONNECT_DEVICE;
                if (MySharedPreferences.share == null) {
                    MySharedPreferences.getSharedPref(MainActivity.mContexts);
                }
                if (!MySharedPreferences.share.contains(MySharedPreferences.isDownLoadTaskOver)) {
                    MySharedPreferences.setInt(MainActivity.mContexts, MySharedPreferences.isDownLoadTaskOver, MainActivity.REQUEST_CONNECT_DEVICE);
                }
                if (MainActivity.this.downLoadCount == MySharedPreferences.getIntValue(context, MySharedPreferences.isDownLoadTaskOver)) {
                    MySharedPreferences.setInt(MainActivity.mContexts, MySharedPreferences.isDownLoadTaskOver, MainActivity.REQUEST_CONNECT_DEVICE);
                    MainActivity.this.downLoadCount = 0;
                    new MySoftUpdate(MainActivity.mContexts).checkUpdateAsync();
                }
            } else if (intent.getAction().equals("android.intent.action.LOCALE_CHANGED")) {
                MainActivity.database.close();
                if (Constant.mChatService != null) {
                    Constant.mChatService.stop();
                }
                if (MainActivity.serviceManager != null) {
                    MainActivity.serviceManager.stopService();
                }
                MainActivity.deleteFiles(MainActivity.dataDir + "/libs/cnlaunch/");
                MySharedPreferences.getSharedPref(MainActivity.this).edit().putString(MySharedPreferences.CCKey, XmlPullParser.NO_NAMESPACE).commit();
                MySharedPreferences.getSharedPref(MainActivity.this).edit().putString(MySharedPreferences.TokenKey, XmlPullParser.NO_NAMESPACE).commit();
                Presence presence = new Presence(Type.unavailable);
                try {
                    XmppConnection.closeConnection();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                MyApplication.getInstance().exit();
                Process.killProcess(Process.myPid());
                System.exit(0);
            }
            if (MainActivity.this.isExecuteM.booleanValue()) {
                if (intent.getAction().equals("Connected")) {
                    MainActivity.this.loadDymicLid(MainActivity.this.serialNo, MainActivity.this.packgeId);
                }
                if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    MainActivity.this.connectTimes = 0;
                    MainActivity.this.onWindowsChangeSet();
                    MainActivity.this.connectBluetoothAddress();
                } else if (intent.getAction().equals("android.bluetooth.device.action.ACL_CONNECTED")) {
                    if (Constant.mChatService == null) {
                        MainActivity.this.setupChat();
                    }
                } else if (intent.getAction().equals("Display_models") || intent.getAction().equals("Show_models") || intent.getAction().equals("Show_serial")) {
                    if (MainActivity.this.loadSpecail) {
                        new GetSpecial().start();
                        return;
                    }
                    new GetChina().start();
                    new GetAsia().start();
                    new GetEuro().start();
                    new GetAmerica().start();
                } else if (intent.getAction().equals("feedbackMeauData")) {
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("menuData");
                    menu = new Intent(MainActivity.this, CarDiagnoseActivity.class);
                    menu.putExtra("menuData", menuDataList);
                    menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(MainActivity.this.progressDialog);
                    MainActivity.this.startActivity(menu);
                    MainActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_DS_MENU_ID")) {
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("SPT_DS_MENU_ID");
                    menu = new Intent(MainActivity.this, CarDiagnoseActivity.class);
                    menu.putExtra("menuData", menuDataList);
                    menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(MainActivity.this.progressDialog);
                    MainActivity.this.startActivity(menu);
                    MainActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_ACTIVE_TEST")) {
                    SptActiveTest sptActiveTest = (SptActiveTest) intent.getExtras().getSerializable("ACTIVE_TEST");
                    Intent active = new Intent(MainActivity.this, ActiveTestActivity.class);
                    active.putExtra("ACTIVE_TEST", sptActiveTest);
                    active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(MainActivity.this.progressDialog);
                    MainActivity.this.startActivity(active);
                    MainActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_NOBUTTONBOX_TEXT")) {
                    if (MainActivity.this.progressDialog == null) {
                        MainActivity.this.progressDialog = new ProgressDialog(MainActivity.contexts);
                        MainActivity.this.progressDialog.setCancelable(false);
                    } else {
                        MainActivity.this.progressDialog.dismiss();
                        MainActivity.this.progressDialog = null;
                        MainActivity.this.progressDialog = new ProgressDialog(MainActivity.contexts);
                        MainActivity.this.progressDialog.setCancelable(false);
                    }
                    Spt_Nobuttonbox_Text nobuttonbox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("Nobuttonbox");
                    SimpleDialog.openProgressDialog(MainActivity.contexts, nobuttonbox.getTitle(), nobuttonbox.getContent(), MainActivity.this.progressDialog);
                } else if (intent.getAction().equals("closeNobuttonBox")) {
                    SimpleDialog.closeProgressDialog(MainActivity.this.progressDialog);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE")) {
                    ArrayList<SptTroubleTest> troubleTestList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE");
                    r0 = new Intent(MainActivity.this, FaultCodeActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE", troubleTestList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(MainActivity.this.progressDialog);
                    MainActivity.this.startActivity(r0);
                    MainActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_STREAM_SELECT_ID_EX")) {
                    ArrayList<SptStreamSelectIdItem> streamSelectIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_STREAM_SELECT_ID_EX");
                    r0 = new Intent(MainActivity.this, StreamSelectActivity.class);
                    r0.putExtra("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(MainActivity.this.progressDialog);
                    MainActivity.this.startActivity(r0);
                    MainActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                    ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                    r0 = new Intent(MainActivity.this, DataStreamActivity.class);
                    r0.putExtra("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(MainActivity.this.progressDialog);
                    MainActivity.this.startActivity(r0);
                    MainActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_MESSAGEBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(MainActivity.this.progressDialog);
                    SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) intent.getExtras().getSerializable("SPT_MESSAGEBOX_TEXT");
                    switch (sptMessageBoxText.getDialogType()) {
                        case MainActivity.REQUEST_CONNECT_DEVICE /*1*/:
                            SimpleDialog.okDialog(MainActivity.contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
                        case MainActivity.REQUEST_ENABLE_BT /*2*/:
                            SimpleDialog.okCancelDialog(MainActivity.contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
                        case MainActivity.REQUEST_CIRCLE_ADD_IMAGE /*3*/:
                            SimpleDialog.yesNoDialog(MainActivity.contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
                        case MainActivity.MESSAGE_DEVICE_NAME /*4*/:
                            SimpleDialog.retryCancelDialog(MainActivity.contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
                        case MainActivity.MESSAGE_TOAST /*5*/:
                            SimpleDialog.noButtonDialog(MainActivity.contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
                        case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                            SimpleDialog.okPrintDialog(MainActivity.contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
                        default:
                    }
                } else if (intent.getAction().equals("SPT_INPUTSTRING_EX")) {
                    SimpleDialog.closeProgressDialog(MainActivity.this.progressDialog);
                    SptInputStringEx inputString = (SptInputStringEx) intent.getExtras().getSerializable("SPT_INPUTSTRING_EX");
                    SimpleDialog.sptInputStringExDiagnose(MainActivity.contexts, inputString.getDialogTitle(), inputString.getDialogContent(), inputString.getInputHint());
                } else if (intent.getAction().equals("SPT_INPUT_NUMERIC")) {
                    SimpleDialog.closeProgressDialog(MainActivity.this.progressDialog);
                    SptInputNumric inputNumric = (SptInputNumric) intent.getExtras().getSerializable("SPT_INPUT_NUMERIC");
                    SimpleDialog.sptInputNumericDiagnose(MainActivity.contexts, inputNumric.getDialogTitle(), inputNumric.getDialogContent(), inputNumric.getInputHint(), inputNumric.getDigit());
                } else if (intent.getAction().equals("SPT_INPUTBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(MainActivity.this.progressDialog);
                    Spt_Nobuttonbox_Text inputBox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTBOX_TEXT");
                    SimpleDialog.sptInputBoxTextDiagnose(MainActivity.contexts, inputBox.getTitle(), inputBox.getContent());
                } else if (intent.getAction().equals("SPT_INPUTSTRING")) {
                    SimpleDialog.closeProgressDialog(MainActivity.this.progressDialog);
                    Spt_Nobuttonbox_Text inputStr = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTSTRING");
                    SimpleDialog.sptInputStringDiagnose(MainActivity.contexts, inputStr.getTitle(), inputStr.getContent());
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN")) {
                    ArrayList<SptTroubleTest> troubleCodeFrozenList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN");
                    r0 = new Intent(MainActivity.this, FaultCodeFrozenActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(MainActivity.this.progressDialog);
                    MainActivity.this.startActivity(r0);
                    MainActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                    ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                    r0 = new Intent(MainActivity.this, VWDataStreamActivity.class);
                    r0.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(MainActivity.this.progressDialog);
                    MainActivity.this.startActivity(r0);
                    MainActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_SHOW_PICTURE")) {
                    String pictureName = intent.getExtras().getString("SPT_SHOW_PICTURE");
                    SimpleDialog.sptShowPictureDiagnose(MainActivity.contexts, pictureName);
                } else if (intent.getAction().equals("Show_new_action")) {
                    MainActivity.showNewFlag = intent.getExtras().getBoolean("show_new");
                    MainActivity.this.mHandler.obtainMessage(20).sendToTarget();
                } else if (intent.getAction().equals("changeSkinSuccess")) {
                    MainActivity.this.container.setBackgroundDrawable(MySharedPreferences.getBgId(MainActivity.mContexts));
                }
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.MainActivity.1 */
    class C10971 implements PanelClosedEvent {
        C10971() {
        }

        public void onPanelClosed(View panel) {
        }
    }

    /* renamed from: com.ifoer.expeditionphone.MainActivity.2 */
    class C10982 implements PanelOpenedEvent {
        C10982() {
        }

        public void onPanelOpened(View panel) {
        }
    }

    public MainActivity() {
        this.mChinaBaseDiagAdapter = null;
        this.mAsiaBaseDiagAdapter = null;
        this.mEuroBaseDiagAdapter = null;
        this.mAmericaBaseDiagAdapter = null;
        this.res = null;
        this.productType = "X431ADiag";
        this.getSerialNoProductType = "X431 Auto Diag For Android";
        this.productDTOs = null;
        this.listText = null;
        this.showDialogFlag = 0;
        this.initSlidingFlag = 0;
        this.mConnectedDeviceName = null;
        this.width1 = 0;
        this.height1 = 0;
        this.isExecuteM = Boolean.valueOf(false);
        this.carList = new ArrayList();
        this.chinaList = new ArrayList();
        this.asiaList = new ArrayList();
        this.euroList = new ArrayList();
        this.americaLists = new ArrayList();
        this.statusBarHeight = 0;
        this.screenHeight = 0;
        this.isShowExist = false;
        this.specialList = new ArrayList();
        this.mSpecilaBaseDiagAdapeter = null;
        this.lanId = XStream.NO_REFERENCES;
        this.connectTimes = 0;
        this.downLoadCount = 0;
        this.loadSpecail = false;
        this.panelClosedEvent = new C10971();
        this.panelOpenedEvent = new C10982();
        this.downHandler = new C05893();
        this.mHandler = new C05904();
        this.softIdListRes = null;
        this.clickedViewItemSoftId = 0;
    }

    static {
        imageStr = null;
        hasLoadLib = false;
        socketCall = null;
        socketSendThread = null;
        ENLARGE_WIDTH = 0;
        FROMPATH = XmlPullParser.NO_NAMESPACE;
        dataDir = null;
        System.loadLibrary("SearchId");
        System.loadLibrary("LICENSE");
        resetConnectTimes = 0;
        showNewFlag = false;
        mSlidePoint = REQUEST_CONNECT_DEVICE;
        serialPort = null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        contexts = this;
        this.loadSpecail = getIntent().getBooleanExtra("loadSpecail", false);
        FROMPATH = Constant.assestsPath;
        Locale locale = Locale.getDefault();
        country = MainMenuActivity.country;
        if (country == null) {
            country = locale.getCountry();
        }
        this.lanName = AndroidToLan.toLan(country);
        this.language = locale.getLanguage();
        Constant.language = locale.toString();
        if (this.loadSpecail) {
            setContentView(C0136R.layout.specia_function2);
            this.SpecialGridView = (GridView) findViewById(C0136R.id.main_WorkSpace);
            this.SpecialGridView.setHorizontalSpacing(15);
            this.SpecialGridView.setVerticalSpacing(15);
            this.SpecialGridView.setSelector(C0136R.color.transparent);
            this.SpecialGridView.setNumColumns(REQUEST_CIRCLE_ADD_IMAGE);
            this.mSpecilaBaseDiagAdapeter = createBaseDiagAdapter2(this.specialList);
            this.SpecialGridView.setAdapter(this.mSpecilaBaseDiagAdapeter);
            this.SpecialGridView.setOnItemClickListener(this);
        } else {
            setContentView(C0136R.layout.base_diagnose);
            LinearLayout lineLinear = (LinearLayout) findViewById(C0136R.id.diagnose_line2);
            this.mSlidePoinView = new SlidePointView(this);
            lineLinear.addView(this.mSlidePoinView);
            this.mSlidePoinView.postInvalidate();
            this.mChinaCarGridView = new GridView(this);
            this.mAsiaCarGridView = new GridView(this);
            this.mEuroCarGridView = new GridView(this);
            this.mAmericaCarGridView = new GridView(this);
            this.mAmericaCarGridView.setHorizontalSpacing(15);
            this.mAmericaCarGridView.setVerticalSpacing(15);
            this.mAsiaCarGridView.setHorizontalSpacing(15);
            this.mAsiaCarGridView.setVerticalSpacing(15);
            this.mEuroCarGridView.setHorizontalSpacing(15);
            this.mEuroCarGridView.setVerticalSpacing(15);
            this.mAmericaCarGridView.setVerticalScrollBarEnabled(false);
            this.mAsiaCarGridView.setVerticalScrollBarEnabled(false);
            this.mEuroCarGridView.setVerticalScrollBarEnabled(false);
            this.mAsiaCarGridView.setSelector(C0136R.color.transparent);
            this.mEuroCarGridView.setSelector(C0136R.color.transparent);
            this.mAmericaCarGridView.setSelector(C0136R.color.transparent);
            LayoutParams linearParams = new LayoutParams(-2, -2);
            linearParams.width = C0136R.dimen.itemSize;
            linearParams.height = C0136R.dimen.itemSize;
            this.mChinaCarGridView.setLayoutParams(linearParams);
            this.mAsiaCarGridView.setLayoutParams(linearParams);
            this.mEuroCarGridView.setLayoutParams(linearParams);
            this.mAmericaCarGridView.setLayoutParams(linearParams);
            this.mChinaCarGridView.setNumColumns(REQUEST_CIRCLE_ADD_IMAGE);
            this.mAsiaCarGridView.setNumColumns(REQUEST_CIRCLE_ADD_IMAGE);
            this.mEuroCarGridView.setNumColumns(REQUEST_CIRCLE_ADD_IMAGE);
            this.mAmericaCarGridView.setNumColumns(REQUEST_CIRCLE_ADD_IMAGE);
        }
        MyApplication.getInstance().addActivity(this);
        createMainActivity();
        MySharedPreferences.getSharedPref(this).edit().putString("CurrentPosition", Constant.language).commit();
        MySharedPreferences.setString(contexts, MySharedPreferences.generateOperatingRecord, Contact.RELATION_ASK);
        if (MainMenuActivity.database != null) {
            database = MainMenuActivity.database;
        } else {
            database = DBDao.getInstance(this).getConnection();
        }
        new Thread(new UpgradeRunnable(contexts)).start();
        registerBoradcastReceiver();
        try {
            dataDir = getPackageManager().getApplicationInfo(getPackageName(), 0).dataDir;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        try {
            serialPort = new SerialPortManager().getSerialPort();
        } catch (InvalidParameterException e2) {
            e2.printStackTrace();
        } catch (SecurityException e3) {
            e3.printStackTrace();
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        if (MySharedPreferences.share == null) {
            MySharedPreferences.getSharedPref(this);
        }
    }

    private void connectBluetoothAddress() {
        if (Constant.mChatService == null) {
            setupChat();
        }
    }

    public void switchLanguage(Locale locale) {
        Resources resources = getBaseContext().getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        config.locale = locale;
        resources.updateConfiguration(config, dm);
    }

    public void onStart() {
        super.onStart();
        if (Constant.exsitApp) {
            finish();
        }
        if (serialPort != null && Constant.mChatService == null) {
            setupChat();
        }
    }

    public synchronized void onResume() {
        super.onResume();
        FreeMemory.getInstance(this).freeMemory();
        this.isExecuteM = Boolean.valueOf(f2129D);
        FROMPATH = Constant.assestsPath;
        this.cc = MySharedPreferences.getStringValue(contexts, MySharedPreferences.CCKey);
        if (this.loadSpecail) {
            if (TextUtils.isEmpty(this.cc) || !Common.isNetworkAvailable(contexts)) {
                this.btn_login.setVisibility(0);
                this.show_name.setVisibility(MESSAGE_FIRST_TOAST);
            } else {
                this.btn_login.setVisibility(MESSAGE_FIRST_TOAST);
                this.show_name.setVisibility(MESSAGE_FIRST_TOAST);
            }
        } else if (TextUtils.isEmpty(this.cc) || !Common.isNetworkAvailable(contexts)) {
            this.btn_login.setVisibility(0);
            this.show_name.setVisibility(MESSAGE_FIRST_TOAST);
            this.menuBtn.setNextFocusLeftId(C0136R.id.login2);
            this.yzCar.setNextFocusRightId(C0136R.id.login2);
        } else {
            this.btn_login.setVisibility(MESSAGE_FIRST_TOAST);
            this.show_name.setVisibility(MESSAGE_FIRST_TOAST);
            this.menuBtn.setNextFocusLeftId(C0136R.id.yzCar);
            this.yzCar.setNextFocusRightId(C0136R.id.menuBtn1);
        }
        if (Constant.mChatService != null && Constant.mChatService.getState() == 0) {
            Constant.mChatService.start();
        }
    }

    public void sendMsg() {
        sendBroadcast(new Intent("Show_names"));
    }

    public synchronized void onPause() {
        super.onPause();
        this.isExecuteM = Boolean.valueOf(f2129D);
    }

    public void onStop() {
        super.onStop();
        this.isExecuteM = Boolean.valueOf(false);
        this.mChinaBaseDiagAdapter = null;
        this.mAsiaBaseDiagAdapter = null;
        this.mEuroBaseDiagAdapter = null;
        this.mAmericaBaseDiagAdapter = null;
    }

    public void onDestroy() {
        SimpleDialog.closeProgressDialog(this.progressDialog);
        super.onDestroy();
        if (this.service != null) {
            stopService(this.service);
        }
        try {
            if (this.receiver != null) {
                unregisterReceiver(this.receiver);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        this.mChinaBaseDiagAdapter = null;
        this.mAsiaBaseDiagAdapter = null;
        this.mEuroBaseDiagAdapter = null;
        this.mAmericaBaseDiagAdapter = null;
    }

    public void mainView() {
        Intent intent = getIntent();
        this.btn_login = (Button) ((Activity) contexts).findViewById(C0136R.id.login2);
        this.btn_login.setOnClickListener(new C05915());
        this.show_name = (TextView) findViewById(C0136R.id.showName);
        this.show_name.setText(MySharedPreferences.getStringValue(contexts, MySharedPreferences.UserNameKey));
        this.show_name.setVisibility(MESSAGE_FIRST_TOAST);
        this.menuBtn = (LinearLayout) ((Activity) contexts).findViewById(C0136R.id.menuBtn1);
        this.menuBtn.setOnClickListener(new C05926());
        this.initSlidingFlag = intent.getIntExtra("initSlidingFlag", 0);
        this.showDialogFlag = intent.getIntExtra(IfShowDialog, 0);
        if (MySharedPreferences.getStringValue(this, "PRODUCT_TYPE") != null) {
            this.productType = MySharedPreferences.getStringValue(this, "PRODUCT_TYPE");
        }
        if (MySharedPreferences.getStringValue(this, "SERIA_NO_PRODUCT_TYPE") != null) {
            this.getSerialNoProductType = MySharedPreferences.getStringValue(this, "SERIA_NO_PRODUCT_TYPE");
        }
        if (this.loadSpecail) {
            new GetSpecial().start();
            return;
        }
        this.mWorkSpace = (WorkSpace) findViewById(C0136R.id.main_WorkSpace);
        this.mWorkSpace.setOnScrollCompleteLinstenner(this);
        this.mWorkSpace.addView(this.mAmericaCarGridView);
        this.mWorkSpace.addView(this.mEuroCarGridView);
        this.mWorkSpace.addView(this.mAsiaCarGridView);
        this.main_head = (RelativeLayout) findViewById(C0136R.id.main_head);
        this.contentsss = (RelativeLayout) findViewById(C0136R.id.contentsss);
        this.mainLayout = (RelativeLayout) findViewById(C0136R.id.mainLayout);
        this.container = (LinearLayout) findViewById(C0136R.id.container);
        this.derma = (LinearLayout) findViewById(C0136R.id.derma);
        this.zgCar = (Button) findViewById(C0136R.id.zgCar);
        this.zgCar.setText(getResources().getText(C0136R.string.base_cn));
        this.zgCar.setOnClickListener(this);
        this.yzCar = (Button) findViewById(C0136R.id.yzCar);
        this.yzCar.setOnClickListener(this);
        this.ozCar = (Button) findViewById(C0136R.id.ozCar);
        this.ozCar.setOnClickListener(this);
        this.mzCar = (Button) findViewById(C0136R.id.mzCar);
        this.mzCar.setOnClickListener(this);
        refreshUi();
        this.mAsiaBaseDiagAdapter = createBaseDiagAdapter(this.asiaList);
        this.mEuroBaseDiagAdapter = createBaseDiagAdapter(this.euroList);
        this.mAmericaBaseDiagAdapter = createBaseDiagAdapter(this.americaLists);
        this.mAsiaCarGridView.setAdapter(this.mAsiaBaseDiagAdapter);
        this.mEuroCarGridView.setAdapter(this.mEuroBaseDiagAdapter);
        this.mAmericaCarGridView.setAdapter(this.mAmericaBaseDiagAdapter);
        this.mAsiaCarGridView.setOnItemClickListener(this);
        this.mEuroCarGridView.setOnItemClickListener(this);
        this.mAmericaCarGridView.setOnItemClickListener(this);
        int load_cur_screen = MySharedPreferences.getIntValue(this, "CUR_SCREEN");
        if (load_cur_screen != 0) {
            setScrollWorkspace(load_cur_screen);
            this.mWorkSpace.setToScreen(load_cur_screen);
            if (load_cur_screen == REQUEST_CONNECT_DEVICE) {
                this.menuBtn.setSelected(false);
                this.btn_login.setSelected(false);
                this.mzCar.setSelected(false);
                this.yzCar.setSelected(false);
                this.ozCar.requestFocus();
                this.ozCar.setSelected(f2129D);
                this.mzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
                this.yzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
                this.ozCar.setTextColor(mContexts.getResources().getColor(C0136R.color.red));
            } else if (load_cur_screen == REQUEST_ENABLE_BT) {
                this.menuBtn.setSelected(false);
                this.btn_login.setSelected(false);
                this.mzCar.setSelected(false);
                this.yzCar.setSelected(f2129D);
                this.yzCar.requestFocus();
                this.ozCar.setSelected(false);
                this.mzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
                this.yzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.red));
                this.ozCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
            }
        } else {
            setScrollWorkspace(0);
            this.menuBtn.setSelected(false);
            this.btn_login.setSelected(false);
            this.mzCar.setSelected(f2129D);
            this.mzCar.requestFocus();
            this.yzCar.setSelected(false);
            this.ozCar.setSelected(false);
            this.mzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.red));
            this.yzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
            this.ozCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
            this.mWorkSpace.setToScreen(0);
        }
        this.toSearch = (Button) findViewById(C0136R.id.toSearch);
        this.searchEdit = (EditText) findViewById(C0136R.id.topEdit);
        this.searchEdit.addTextChangedListener(new C05937());
        this.toSearch.setOnClickListener(new C05948());
    }

    public void loadSo(String dataDirs, String sdPath) {
        String paths = dataDir + "/libs/" + dataDirs;
        String sdPaths = Constant.LOCAL_SERIALNO_PATH + dataDirs;
        if (Copy_File.copy(sdPaths, paths) == 0) {
            Copy_File.list = new ArrayList();
            Copy_File.findAllSoFile(paths);
            if (Copy_File.list.size() > 0) {
                Intent menu = new Intent(this, CarDiagnoseActivity.class);
                menu.putStringArrayListExtra("FileList", Copy_File.list);
                menu.putExtra("paths", new StringBuilder(String.valueOf(sdPaths)).append(sdPath).toString());
                menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(this.progressDialog);
                startActivity(menu);
                overridePendingTransition(0, 0);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE /*1*/:
                if (serialPort != null) {
                    Constant.mChatService.connect(serialPort, f2129D);
                }
            case REQUEST_CIRCLE_ADD_IMAGE /*3*/:
                if (data != null) {
                    Bitmap photo = (Bitmap) data.getParcelableExtra(DataPacketExtension.ELEMENT_NAME);
                    if (photo == null) {
                        Toast.makeText(this, C0136R.string.toast_select_image, 0).show();
                        return;
                    }
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    photo.compress(CompressFormat.JPEG, 20, outStream);
                    imageStr = MyApplication.encode(outStream.toByteArray());
                }
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                if (resultCode == 10) {
                    this.showDialogFlag = data.getExtras().getInt(IfShowDialog, 0);
                    checkIfFirstCome();
                    if (data.getExtras().getInt("findPassword", 0) == REQUEST_CONNECT_DEVICE && panel == null) {
                        this.initSlidingFlag = 6;
                    }
                } else if (resultCode != 11 || data.getExtras().getInt("findPassword", 0) != REQUEST_CONNECT_DEVICE) {
                } else {
                    if (panel != null) {
                        startActivity(new Intent(this, FindPwdActivity.class));
                        return;
                    }
                    this.initSlidingFlag = 6;
                }
            case REQUEST_USER_LOGIN_OUT /*25*/:
                if (resultCode == 11) {
                    startActivity(new Intent(this, FindPwdActivity.class));
                } else if (resultCode == 10) {
                    initLeftBtn(contexts, REQUEST_CONNECT_DEVICE);
                    panel.removePanelContainer();
                    panel.fillPanelContainer(new DiagnosisdatabaseActivity(contexts));
                    panel.openthreePanelContainer();
                }
            case XStream.PRIORITY_VERY_HIGH /*10000*/:
                if (data != null) {
                    if (!XmlPullParser.NO_NAMESPACE.equals(data.getStringExtra("CAMERA"))) {
                        FastDiagnosisActivity.mVin = data.getStringExtra("CAMERA");
                        FastDiagnosisActivity.etVin.setText(data.getStringExtra("CAMERA"));
                    }
                }
            case FastDiagnosisActivity.PHOTO_ALBUM_REQUEST_CODE /*10001*/:
                if (data == null) {
                    return;
                }
                if (resultCode != -1) {
                    Toast.makeText(this, C0136R.string.ImageGetFail, 0).show();
                } else if (Environment.getExternalStorageState().equals("mounted")) {
                    Uri uri = data.getData();
                    String[] proj = new String[REQUEST_CONNECT_DEVICE];
                    proj[0] = "_data";
                    Cursor cursor = managedQuery(uri, proj, null, null, null);
                    if (cursor != null) {
                        int column_index = cursor.getColumnIndexOrThrow("_data");
                        if (cursor.getCount() <= 0 || !cursor.moveToFirst()) {
                            Toast.makeText(this, C0136R.string.circleImageNoFound, 0).show();
                            return;
                        }
                        String mImagePath = cursor.getString(column_index);
                        Intent intentPhoto = new Intent(this, VinPhotoActivity.class);
                        intentPhoto.putExtra("IMAGE_PATH", mImagePath);
                        startActivityForResult(intentPhoto, XStream.PRIORITY_VERY_HIGH);
                        return;
                    }
                    Toast.makeText(this, C0136R.string.circleImageNoFound, 0).show();
                } else {
                    Toast.makeText(this, C0136R.string.circleSDDisable, 0).show();
                }
            case FastDiagnosisActivity.VOICE_RECOGNITION_REQUEST_CODE /*10002*/:
                if (data != null) {
                    if (!XmlPullParser.NO_NAMESPACE.equals(data.getStringExtra("VOICE"))) {
                        String mStrVin = FastDiagnosisActivity.str2NumbersOrLetters(data.getStringExtra("VOICE")).replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE).toUpperCase();
                        Intent intent = new Intent("SPT_SET_VIN");
                        intent.putExtra("SPT_SET_VIN", mStrVin);
                        contexts.sendBroadcast(intent);
                    }
                }
            default:
        }
    }

    private void sendMessage(String message) {
        if (Constant.mChatService.getState() != REQUEST_CIRCLE_ADD_IMAGE) {
            Toast.makeText(this, C0136R.string.not_connected, 0).show();
        } else {
            message.length();
        }
    }

    private void setupChat() {
        Constant.mChatService = new BluetoothChatService(this, this.mHandler, f2129D);
    }

    public void onClick(View v) {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(contexts);
            this.progressDialog.setCancelable(false);
        } else {
            this.progressDialog.dismiss();
            this.progressDialog = null;
            this.progressDialog = new ProgressDialog(contexts);
            this.progressDialog.setCancelable(false);
        }
        SimpleDialog.openProgressDialog(contexts, getResources().getString(C0136R.string.dataDisposeTilte), getResources().getString(C0136R.string.dataDisposeMessage), this.progressDialog);
        if (v.getId() == C0136R.id.repu_btn) {
            MySharedPreferences.setBoolean(this, MySharedPreferences.IfShowResponsibilityKey, false);
            this.reputPopupWindow.dismiss();
        } else if (v.getId() == C0136R.id.repu_close) {
            MySharedPreferences.setBoolean(contexts, MySharedPreferences.IfShowResponsibilityKey, f2129D);
            MySharedPreferences.setString(contexts, "showResponsibility", Contact.RELATION_FRIEND);
            this.reputPopupWindow.dismiss();
        } else if (v.getId() == C0136R.id.readbtn) {
            if (this.toast_check.isChecked()) {
                MySharedPreferences.setBoolean(this, MySharedPreferences.IfShowNoticKey, false);
            } else {
                MySharedPreferences.share.getBoolean(MySharedPreferences.IfShowNoticKey, f2129D);
                MySharedPreferences.setString(contexts, "showNoticKey", Contact.RELATION_FRIEND);
            }
            if (new File(FROMPATH + MySharedPreferences.getStringValue(mContexts, "CRP229_MANUAL_EN")).exists()) {
                this.intent = getPdfFileIntent(FROMPATH + MySharedPreferences.getStringValue(mContexts, "CRP229_MANUAL_EN"));
                try {
                    contexts.startActivity(this.intent);
                } catch (ActivityNotFoundException e) {
                    tipDiaglog();
                }
            } else {
                Toast.makeText(contexts, contexts.getResources().getString(C0136R.string.main_file_null), 0).show();
            }
            this.toastPopupWindow.dismiss();
        } else if (v.getId() == C0136R.id.mzCar) {
            this.menuBtn.setSelected(false);
            this.btn_login.setSelected(false);
            this.mzCar.setSelected(f2129D);
            this.yzCar.setSelected(false);
            this.ozCar.setSelected(false);
            this.mzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.red));
            this.yzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
            this.ozCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
            setScrollWorkspace(0);
            this.mWorkSpace.setToScreen(0);
        } else if (v.getId() == C0136R.id.ozCar) {
            this.menuBtn.setSelected(false);
            this.btn_login.setSelected(false);
            this.mzCar.setSelected(false);
            this.yzCar.setSelected(false);
            this.ozCar.setSelected(f2129D);
            this.mzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
            this.yzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
            this.ozCar.setTextColor(mContexts.getResources().getColor(C0136R.color.red));
            if (MySharedPreferences.getStringValue(this, "PDY_GRID_TYPE").equals("SIMPALE")) {
                setScrollWorkspace(REQUEST_ENABLE_BT);
                this.mWorkSpace.setToScreen(REQUEST_ENABLE_BT);
            } else {
                setScrollWorkspace(REQUEST_CONNECT_DEVICE);
                this.mWorkSpace.setToScreen(REQUEST_CONNECT_DEVICE);
            }
        } else if (v.getId() == C0136R.id.yzCar) {
            this.menuBtn.setSelected(false);
            this.btn_login.setSelected(false);
            this.mzCar.setSelected(false);
            this.yzCar.setSelected(f2129D);
            this.ozCar.setSelected(false);
            this.mzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
            this.yzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.red));
            this.ozCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
            if (MySharedPreferences.getStringValue(this, "PDY_GRID_TYPE").equals("SIMPALE")) {
                setScrollWorkspace(REQUEST_CONNECT_DEVICE);
                this.mWorkSpace.setToScreen(REQUEST_CONNECT_DEVICE);
            } else {
                setScrollWorkspace(REQUEST_ENABLE_BT);
                this.mWorkSpace.setToScreen(REQUEST_ENABLE_BT);
            }
        } else if (v.getId() == C0136R.id.zgCar) {
            this.menuBtn.setSelected(false);
            this.btn_login.setSelected(false);
            this.mzCar.setSelected(false);
            this.yzCar.setSelected(false);
            this.ozCar.setSelected(false);
            if (MySharedPreferences.getStringValue(this, "PDY_GRID_TYPE").equals("SIMPALE")) {
                setScrollWorkspace(0);
                this.mWorkSpace.setToScreen(0);
            } else {
                setScrollWorkspace(REQUEST_CIRCLE_ADD_IMAGE);
                this.mWorkSpace.setToScreen(REQUEST_CIRCLE_ADD_IMAGE);
            }
        }
        SimpleDialog.closeProgressDialog(this.progressDialog);
    }

    public void checkIfFirstCome() {
        boolean showReput = MySharedPreferences.share.getBoolean(MySharedPreferences.IfShowResponsibilityKey, f2129D);
        boolean showToast = MySharedPreferences.share.getBoolean(MySharedPreferences.IfShowNoticKey, f2129D);
        String showResponsibility = MySharedPreferences.getStringValue(this, "showResponsibility");
        String firstLogin = MySharedPreferences.getStringValue(this, "firstLogin");
        String showNoticKey = MySharedPreferences.getStringValue(this, "showNoticKey");
        if (showReput && this.showDialogFlag == REQUEST_ENABLE_BT) {
            if (showResponsibility == null || showResponsibility.equalsIgnoreCase(Contact.RELATION_FRIEND)) {
                MySharedPreferences.setString(this, "showResponsibility", Contact.RELATION_BACKNAME);
                initPopupWindow();
            }
        } else if (showReput || this.showDialogFlag != REQUEST_ENABLE_BT) {
            if (!showToast || this.showDialogFlag != REQUEST_CONNECT_DEVICE) {
                return;
            }
            if (showNoticKey == null || showNoticKey.equalsIgnoreCase(Contact.RELATION_FRIEND)) {
                MySharedPreferences.setString(this, "showNoticKey", Contact.RELATION_BACKNAME);
                initToastPopupWindow();
            }
        } else if (firstLogin == null || firstLogin.equalsIgnoreCase(Contact.RELATION_FRIEND)) {
            MySharedPreferences.setString(this, "firstLogin", Contact.RELATION_BACKNAME);
        }
    }

    private void dismissPopupWindow() {
        if (this.portPopupWindow != null && this.portPopupWindow.isShowing()) {
            this.portPopupWindow.dismiss();
        }
    }

    public void initToastPopupWindow() {
        this.toastPopupWindow = new Builder(this).create();
        this.toastPopupWindow.setCanceledOnTouchOutside(false);
        this.toastPopupWindow.setView(toastDialogView());
        this.toastPopupWindow.show();
        this.toastPopupWindow.setOnKeyListener(new C05959());
        this.toastPopupWindow.setContentView(toastDialogView(), new ViewGroup.LayoutParams(-1, -1));
    }

    public void initPopupWindow() {
        this.reputPopupWindow = new Builder(this).create();
        this.reputPopupWindow.setCanceledOnTouchOutside(false);
        this.reputPopupWindow.setView(reputDialogView());
        this.reputPopupWindow.show();
        this.reputPopupWindow.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode != MainActivity.MESSAGE_DEVICE_NAME) {
                    return false;
                }
                MySharedPreferences.setBoolean(MainActivity.contexts, MySharedPreferences.IfShowResponsibilityKey, MainActivity.f2129D);
                MySharedPreferences.setString(MainActivity.contexts, "showResponsibility", Contact.RELATION_FRIEND);
                MainActivity.this.reputPopupWindow.dismiss();
                return MainActivity.f2129D;
            }
        });
        this.reputPopupWindow.setContentView(reputDialogView(), new ViewGroup.LayoutParams(-1, -1));
    }

    private View reputDialogView() {
        this.reput_lay = (RelativeLayout) LayoutInflater.from(contexts).inflate(C0136R.layout.reputation_pop, null);
        this.rlPop = (RelativeLayout) this.reput_lay.findViewById(C0136R.id.rl_popdialog);
        if (getResources().getConfiguration().orientation == REQUEST_ENABLE_BT) {
            this.rlPop.setPadding(0, 60, 0, 60);
        } else {
            this.rlPop.setPadding(30, Opcodes.FCMPG, 30, Opcodes.FCMPG);
        }
        this.reput_close = (ImageView) this.reput_lay.findViewById(C0136R.id.repu_close);
        this.reput_check = (CheckBox) this.reput_lay.findViewById(C0136R.id.repu_select);
        this.reput_btn = (Button) this.reput_lay.findViewById(C0136R.id.repu_btn);
        this.reput_btn.setOnClickListener(this);
        this.reput_close.setOnClickListener(this);
        return this.reput_lay;
    }

    private View toastDialogView() {
        this.toast_lay = (RelativeLayout) LayoutInflater.from(contexts).inflate(C0136R.layout.main_notic, null);
        this.toast_check = (CheckBox) this.toast_lay.findViewById(C0136R.id.main_selected);
        this.toast_btn = (Button) this.toast_lay.findViewById(C0136R.id.readbtn);
        this.toast_btn.setOnClickListener(this);
        return this.toast_lay;
    }

    public void registerBoradcastReceiver() {
        this.receiver = new mBroadcastReceiver();
        this.myIntentFilter = new IntentFilter();
        this.myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        this.myIntentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        this.myIntentFilter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
        this.myIntentFilter.addAction("feedbackMeauData");
        this.myIntentFilter.addAction("SPT_ACTIVE_TEST");
        this.myIntentFilter.addAction("SPT_NOBUTTONBOX_TEXT");
        this.myIntentFilter.addAction("closeNobuttonBox");
        this.myIntentFilter.addAction("SPT_TROUBLE_CODE");
        this.myIntentFilter.addAction("SPT_EXIT_SHOW_WINDOW");
        this.myIntentFilter.addAction("SPT_STREAM_SELECT_ID_EX");
        this.myIntentFilter.addAction("SPT_EX_DATASTREAM_ID");
        this.myIntentFilter.addAction("Connected");
        this.myIntentFilter.addAction("SPT_MESSAGEBOX_TEXT");
        this.myIntentFilter.addAction("SPT_INPUTSTRING_EX");
        this.myIntentFilter.addAction("SPT_INPUT_NUMERIC");
        this.myIntentFilter.addAction("SPT_INPUTBOX_TEXT");
        this.myIntentFilter.addAction("SPT_INPUTSTRING");
        this.myIntentFilter.addAction("SPT_TROUBLE_CODE_FROZEN");
        this.myIntentFilter.addAction("SPT_VW_DATASTREAM_ID");
        this.myIntentFilter.addAction("SPT_DS_MENU_ID");
        this.myIntentFilter.addAction("SPT_SHOW_PICTURE");
        this.myIntentFilter.addAction("Display_models");
        this.myIntentFilter.addAction("Show_models");
        this.myIntentFilter.addAction("Show_serial");
        this.myIntentFilter.addAction("Show_names");
        this.myIntentFilter.addAction("Nushow_names");
        this.myIntentFilter.addAction("refreshUi");
        this.myIntentFilter.addAction("show");
        this.myIntentFilter.addAction(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        this.myIntentFilter.addAction("android.intent.action.LOCALE_CHANGED");
        this.myIntentFilter.addAction("SHOW_DOWN_DIALOG");
        this.myIntentFilter.addAction("Show_new_action");
        this.myIntentFilter.addAction("changeSkinSuccess");
        registerReceiver(this.receiver, this.myIntentFilter);
    }

    public static synchronized void deleteFiles(String filename) {
        synchronized (MainActivity.class) {
            Copy_File.delectFile(filename);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == MESSAGE_DEVICE_NAME) {
            finish();
            return f2129D;
        } else if (keyCode == Service.CISCO_TNA) {
            finish();
            return f2129D;
        } else if (keyCode == Service.SUNRPC) {
            finish();
            return f2129D;
        } else if (keyCode == Service.CISCO_SYS) {
            return f2129D;
        } else {
            if (!this.loadSpecail) {
                doLeftRight(keyCode);
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    public Intent getPdfFileIntent(String path) {
        Intent i = new Intent("android.intent.action.VIEW");
        i.addCategory("android.intent.category.DEFAULT");
        i.addFlags(268435456);
        i.setDataAndType(Uri.fromFile(new File(path)), "application/pdf");
        return i;
    }

    private void refreshUi() {
        if (this.loadSpecail) {
            new GetSpecial().start();
            return;
        }
        new GetChina().start();
        new GetAsia().start();
        new GetEuro().start();
        new GetAmerica().start();
    }

    public synchronized List<HashMap<String, Object>> GetSpecialList(String serialNo, Boolean isopen, String carTypeSearch) {
        List<HashMap<String, Object>> list;
        List<HashMap<String, Object>> carAdds = new ArrayList();
        List<HashMap<String, Object>> listImage = new ArrayList();
        this.specialList = createDBDao().query(Contact.RELATION_AGREE, serialNo, this.lanName, database);
        listImage.clear();
        int i;
        if (isopen.booleanValue()) {
            for (i = 0; i < this.specialList.size(); i += REQUEST_CONNECT_DEVICE) {
                if (Integer.parseInt(((HashMap) this.specialList.get(i)).get("flag").toString()) > 0) {
                    listImage.add((HashMap) this.specialList.get(i));
                }
            }
            this.specialList.clear();
            this.specialList = listImage;
            if (carAdds.size() > 0) {
                carAdds.clear();
            }
            if (!(carTypeSearch == null || carTypeSearch.equals(XmlPullParser.NO_NAMESPACE))) {
                for (i = 0; i < this.specialList.size(); i += REQUEST_CONNECT_DEVICE) {
                    if (getString(((Integer) ((HashMap) this.specialList.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                        carAdds.add((HashMap) this.specialList.get(i));
                    } else if (getString(((Integer) ((HashMap) this.specialList.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                        carAdds.add((HashMap) this.specialList.get(i));
                    }
                }
                this.specialList.clear();
                this.specialList = carAdds;
            }
            list = this.specialList;
        } else {
            if (carTypeSearch != null) {
                if (!carTypeSearch.equals(XmlPullParser.NO_NAMESPACE)) {
                    for (i = 0; i < this.specialList.size(); i += REQUEST_CONNECT_DEVICE) {
                        if (getString(((Integer) ((HashMap) this.specialList.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                            carAdds.add((HashMap) this.specialList.get(i));
                        } else if (getString(((Integer) ((HashMap) this.specialList.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                            carAdds.add((HashMap) this.specialList.get(i));
                        }
                    }
                    this.specialList.clear();
                    this.specialList = carAdds;
                    list = this.specialList;
                }
            }
            list = this.specialList;
        }
        return list;
    }

    public synchronized List<HashMap<String, Object>> GetChinaList(String serialNo, Boolean isopen, String carTypeSearch) {
        List<HashMap<String, Object>> list;
        String quaryLanName;
        List<HashMap<String, Object>> carAdds = new ArrayList();
        List<HashMap<String, Object>> listImage = new ArrayList();
        if (this.lanName == "HK" || this.lanName == "TW") {
            quaryLanName = Constants.DEFAULT_LANGUAGE;
        } else {
            quaryLanName = this.lanName;
        }
        this.chinaList = createDBDao().query(Contact.RELATION_BACKNAME, serialNo, quaryLanName, database);
        listImage.clear();
        int i;
        if (isopen.booleanValue()) {
            for (i = 0; i < this.chinaList.size(); i += REQUEST_CONNECT_DEVICE) {
                if (Integer.parseInt(((HashMap) this.chinaList.get(i)).get("flag").toString()) > 0) {
                    listImage.add((HashMap) this.chinaList.get(i));
                }
            }
            this.chinaList.clear();
            this.chinaList = listImage;
            if (carAdds.size() > 0) {
                carAdds.clear();
            }
            if (!(carTypeSearch == null || carTypeSearch.equals(XmlPullParser.NO_NAMESPACE))) {
                for (i = 0; i < this.chinaList.size(); i += REQUEST_CONNECT_DEVICE) {
                    if (getString(((Integer) ((HashMap) this.chinaList.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                        carAdds.add((HashMap) this.chinaList.get(i));
                    } else if (getString(((Integer) ((HashMap) this.chinaList.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                        carAdds.add((HashMap) this.chinaList.get(i));
                    }
                }
                this.chinaList.clear();
                this.chinaList = carAdds;
            }
            list = this.chinaList;
        } else {
            if (carTypeSearch != null) {
                if (!carTypeSearch.equals(XmlPullParser.NO_NAMESPACE)) {
                    for (i = 0; i < this.chinaList.size(); i += REQUEST_CONNECT_DEVICE) {
                        if (getString(((Integer) ((HashMap) this.chinaList.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                            carAdds.add((HashMap) this.chinaList.get(i));
                        } else if (getString(((Integer) ((HashMap) this.chinaList.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                            carAdds.add((HashMap) this.chinaList.get(i));
                        }
                    }
                    this.chinaList.clear();
                    this.chinaList = carAdds;
                    list = this.chinaList;
                }
            }
            list = this.chinaList;
        }
        return list;
    }

    public synchronized List<HashMap<String, Object>> GetAsiaList(String serialNo, Boolean isopen, String carTypeSearch) {
        List<HashMap<String, Object>> list;
        List<HashMap<String, Object>> carAdds = new ArrayList();
        List<HashMap<String, Object>> listImage = new ArrayList();
        this.asiaList.clear();
        this.asiaList = createDBDao().query(Contact.RELATION_NOAGREE, serialNo, this.lanName, database);
        int i;
        if (isopen.booleanValue()) {
            for (i = 0; i < this.asiaList.size(); i += REQUEST_CONNECT_DEVICE) {
                if (Integer.parseInt(((HashMap) this.asiaList.get(i)).get("flag").toString()) > 0) {
                    listImage.add((HashMap) this.asiaList.get(i));
                }
            }
            this.asiaList.clear();
            this.asiaList = listImage;
            if (carAdds.size() > 0) {
                carAdds.clear();
            }
            if (!(carTypeSearch == null || carTypeSearch.equals(XmlPullParser.NO_NAMESPACE))) {
                for (i = 0; i < this.asiaList.size(); i += REQUEST_CONNECT_DEVICE) {
                    if (getString(((Integer) ((HashMap) this.asiaList.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                        carAdds.add((HashMap) this.asiaList.get(i));
                    } else if (getString(((Integer) ((HashMap) this.asiaList.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                        carAdds.add((HashMap) this.asiaList.get(i));
                    }
                }
                this.asiaList.clear();
                this.asiaList = carAdds;
            }
            list = this.asiaList;
        } else {
            if (carAdds.size() > 0) {
                carAdds.clear();
            }
            if (carTypeSearch == null || carTypeSearch.equals(XmlPullParser.NO_NAMESPACE)) {
                list = this.asiaList;
            } else {
                for (i = 0; i < this.asiaList.size(); i += REQUEST_CONNECT_DEVICE) {
                    if (getString(((Integer) ((HashMap) this.asiaList.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                        carAdds.add((HashMap) this.asiaList.get(i));
                    } else if (getString(((Integer) ((HashMap) this.asiaList.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                        carAdds.add((HashMap) this.asiaList.get(i));
                    }
                }
                this.asiaList.clear();
                this.asiaList = carAdds;
                list = this.asiaList;
            }
        }
        return list;
    }

    public synchronized List<HashMap<String, Object>> GetEuroList(String serialNo, Boolean isopen, String carTypeSearch) {
        List<HashMap<String, Object>> list;
        List<HashMap<String, Object>> carAdds = new ArrayList();
        List<HashMap<String, Object>> listImage = new ArrayList();
        this.euroList.clear();
        this.euroList = createDBDao().query(Contact.RELATION_FRIEND, serialNo, this.lanName, database);
        int i;
        if (isopen.booleanValue()) {
            for (i = 0; i < this.euroList.size(); i += REQUEST_CONNECT_DEVICE) {
                if (Integer.parseInt(((HashMap) this.euroList.get(i)).get("flag").toString()) > 0) {
                    listImage.add((HashMap) this.euroList.get(i));
                }
            }
            this.euroList.clear();
            this.euroList = listImage;
            if (carAdds.size() > 0) {
                carAdds.clear();
            }
            if (!(carTypeSearch == null || carTypeSearch.equals(XmlPullParser.NO_NAMESPACE))) {
                for (i = 0; i < this.euroList.size(); i += REQUEST_CONNECT_DEVICE) {
                    if (getString(((Integer) ((HashMap) this.euroList.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                        carAdds.add((HashMap) this.euroList.get(i));
                    } else if (getString(((Integer) ((HashMap) this.euroList.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                        carAdds.add((HashMap) this.euroList.get(i));
                    }
                }
                this.euroList.clear();
                this.euroList = carAdds;
            }
            list = this.euroList;
        } else {
            if (carAdds.size() > 0) {
                carAdds.clear();
            }
            if (carTypeSearch == null || carTypeSearch.equals(XmlPullParser.NO_NAMESPACE)) {
                list = this.euroList;
            } else {
                for (i = 0; i < this.euroList.size(); i += REQUEST_CONNECT_DEVICE) {
                    if (getString(((Integer) ((HashMap) this.euroList.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                        carAdds.add((HashMap) this.euroList.get(i));
                    } else if (getString(((Integer) ((HashMap) this.euroList.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                        carAdds.add((HashMap) this.euroList.get(i));
                    }
                }
                this.euroList.clear();
                this.euroList = carAdds;
                list = this.euroList;
            }
        }
        return list;
    }

    public synchronized List<HashMap<String, Object>> GetAmericaLists(String serialNo, Boolean isopen, String carTypeSearch) {
        List<HashMap<String, Object>> list;
        List<HashMap<String, Object>> carAdds = new ArrayList();
        List<HashMap<String, Object>> listImage = new ArrayList();
        this.americaLists.clear();
        this.americaLists = createDBDao().query(Contact.RELATION_NODONE, serialNo, this.lanName, database);
        int i;
        if (isopen.booleanValue()) {
            for (i = 0; i < this.americaLists.size(); i += REQUEST_CONNECT_DEVICE) {
                if (Integer.parseInt(((HashMap) this.americaLists.get(i)).get("flag").toString()) > 0) {
                    listImage.add((HashMap) this.americaLists.get(i));
                }
            }
            this.americaLists.clear();
            this.americaLists = listImage;
            if (carAdds.size() > 0) {
                carAdds.clear();
            }
            if (!(carTypeSearch == null || carTypeSearch.equals(XmlPullParser.NO_NAMESPACE))) {
                for (i = 0; i < this.americaLists.size(); i += REQUEST_CONNECT_DEVICE) {
                    if (getString(((Integer) ((HashMap) this.americaLists.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                        carAdds.add((HashMap) this.americaLists.get(i));
                    } else if (getString(((Integer) ((HashMap) this.americaLists.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                        carAdds.add((HashMap) this.americaLists.get(i));
                    }
                }
                this.americaLists.clear();
                this.americaLists = carAdds;
            }
            list = this.americaLists;
        } else {
            if (carTypeSearch != null) {
                if (!carTypeSearch.equals(XmlPullParser.NO_NAMESPACE)) {
                    for (i = 0; i < this.americaLists.size(); i += REQUEST_CONNECT_DEVICE) {
                        if (getString(((Integer) ((HashMap) this.americaLists.get(i)).get("name_zh")).intValue()).contains(carTypeSearch)) {
                            carAdds.add((HashMap) this.americaLists.get(i));
                        } else if (getString(((Integer) ((HashMap) this.americaLists.get(i)).get("name")).intValue()).contains(carTypeSearch.toUpperCase())) {
                            carAdds.add((HashMap) this.americaLists.get(i));
                        }
                    }
                    this.americaLists.clear();
                    this.americaLists = carAdds;
                    list = this.americaLists;
                }
            }
            list = this.americaLists;
        }
        return list;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mChinaCarGridView.setNumColumns(getResources().getInteger(C0136R.integer.itemColumNumber));
        this.mAsiaCarGridView.setNumColumns(getResources().getInteger(C0136R.integer.itemColumNumber));
        this.mEuroCarGridView.setNumColumns(getResources().getInteger(C0136R.integer.itemColumNumber));
        this.mAmericaCarGridView.setNumColumns(getResources().getInteger(C0136R.integer.itemColumNumber));
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        int screenWidth = dm.widthPixels;
        int whole_height = (screenHeight - this.main_head.getMeasuredHeight()) - this.statusBarHeight;
        if (panel != null) {
            panel.setLayoutParams(screenWidth, whole_height);
        }
        if (getResources().getConfiguration().orientation == REQUEST_ENABLE_BT) {
            if (this.reputPopupWindow != null && this.reputPopupWindow.isShowing()) {
                this.rlPop.setPadding(0, 60, 0, 60);
            }
        } else if (getResources().getConfiguration().orientation == REQUEST_CONNECT_DEVICE && this.reputPopupWindow != null && this.reputPopupWindow.isShowing()) {
            this.rlPop.setPadding(30, Opcodes.FCMPG, 30, Opcodes.FCMPG);
        }
    }

    public BaseDiagAdapter createBaseDiagAdapter(List<HashMap<String, Object>> carList) {
        return new BaseDiagAdapter(this, carList, this.language);
    }

    public SpecialFunctionActivityAdapter createBaseDiagAdapter2(List<HashMap<String, Object>> carList) {
        return new SpecialFunctionActivityAdapter(this, carList, this.language);
    }

    public static Intent getmKeyToUpgradeIntent() {
        return mKeyToUpgradeIntent;
    }

    public void setmKeyToUpgradeIntent(Intent mKeyToUpgradeActivity) {
        mKeyToUpgradeIntent = mKeyToUpgradeActivity;
    }

    public void createKeyUpGrade() {
        setmKeyToUpgradeIntent(new Intent(this, KeyToUpgradeActivity.class));
    }

    public void createMainActivity() {
        mMainActivityIntent = new Intent(this, MainActivity.class);
    }

    public DBDao createDBDao() {
        return DBDao.getInstance(this);
    }

    private void setScrollWorkspace(int slidePoint) {
        mSlidePoint = slidePoint + REQUEST_CONNECT_DEVICE;
        this.mSlidePoinView.postInvalidate();
        if (slidePoint == 0) {
            this.menuBtn.setSelected(false);
            this.btn_login.setSelected(false);
            this.mzCar.setSelected(f2129D);
            this.yzCar.setSelected(false);
            this.ozCar.setSelected(false);
            this.mzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.red));
            this.yzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
            this.ozCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
        } else if (slidePoint == REQUEST_CONNECT_DEVICE) {
            this.menuBtn.setSelected(false);
            this.btn_login.setSelected(false);
            this.mzCar.setSelected(false);
            this.yzCar.setSelected(false);
            this.ozCar.setSelected(f2129D);
            this.mzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
            this.yzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
            this.ozCar.setTextColor(mContexts.getResources().getColor(C0136R.color.red));
        } else if (slidePoint == REQUEST_ENABLE_BT) {
            this.menuBtn.setSelected(false);
            this.btn_login.setSelected(false);
            this.mzCar.setSelected(false);
            this.yzCar.setSelected(f2129D);
            this.ozCar.setSelected(false);
            this.mzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
            this.yzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.red));
            this.ozCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
        }
    }

    public void onScrollComplete(ScrollEvent e) {
        setScrollWorkspace(e.curScreen);
        MySharedPreferences.setInt(this, "CUR_SCREEN", e.curScreen);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        BaseDiagAdapter baseDiagAdapter = (BaseDiagAdapter) arg0.getAdapter();
        baseDiagAdapter.setSelectItem(arg2);
        baseDiagAdapter.notifyDataSetChanged();
        DataCleanManager.cleanLibs(contexts);
        this.cc = MySharedPreferences.getStringValue(contexts, MySharedPreferences.CCKey);
        if (this.cc != null) {
            if (!this.cc.equals(XmlPullParser.NO_NAMESPACE)) {
                String type = MySharedPreferences.getStringValue(this, "getSerialNo");
                if (type == null || type.equals(Contact.RELATION_FRIEND)) {
                    if (Common.isNetworkAvailable(this)) {
                        new PortNumAsy(REQUEST_CONNECT_DEVICE).execute(new String[0]);
                    } else {
                        Toast.makeText(this, C0136R.string.network_exception, 0).show();
                    }
                }
            }
        }
        if (Environment.getExternalStorageState().equals("mounted")) {
            HashMap<String, Object> map = (HashMap) arg0.getItemAtPosition(arg2);
            String softPackageId = map.get("softPackageId").toString();
            String softId = map.get("softId").toString();
            String icon = map.get("icon").toString();
            MySharedPreferences.setString(contexts, MySharedPreferences.savesoftPackageId, softPackageId);
            String serialNo1 = MySharedPreferences.getStringValue(contexts, MySharedPreferences.serialNoKey);
            MySharedPreferences.setString(contexts, MySharedPreferences.generateOperatingRecord, Contact.RELATION_ASK);
            if (serialNo1 != null) {
                if (!XmlPullParser.NO_NAMESPACE.equals(serialNo1)) {
                    if (country == null || country.length() <= 0) {
                        country = Locale.getDefault().getCountry();
                    }
                    country = Locale.getDefault().getCountry();
                    if (!DBDao.getInstance(this).isDownload(softPackageId, serialNo1, AndroidToLan.toLan(country), database)) {
                        this.cc = MySharedPreferences.getStringValue(contexts, MySharedPreferences.CCKey);
                        if (this.cc != null) {
                            if (!this.cc.equals(XmlPullParser.NO_NAMESPACE)) {
                                String serialNo = MySharedPreferences.getStringValue(contexts, MySharedPreferences.serialNoKey);
                                if (serialNo != null) {
                                    if (!XmlPullParser.NO_NAMESPACE.equals(serialNo)) {
                                        String softName = ((TextView) arg1.findViewById(C0136R.id.car_name)).getText().toString();
                                        if (Common.isNetworkAvailable(this)) {
                                            SoftIdListTask softListTask = new SoftIdListTask();
                                            String[] strArr = new String[MESSAGE_DEVICE_NAME];
                                            strArr[0] = softId;
                                            strArr[REQUEST_CONNECT_DEVICE] = softPackageId;
                                            strArr[REQUEST_ENABLE_BT] = softName;
                                            strArr[REQUEST_CIRCLE_ADD_IMAGE] = icon;
                                            softListTask.execute(strArr);
                                            return;
                                        }
                                        Toast.makeText(this, C0136R.string.network_exception, 0).show();
                                        return;
                                    }
                                }
                                Toast.makeText(this, C0136R.string.port_input, 0).show();
                                return;
                            }
                        }
                        SimpleDialog.ToastToLogin(this);
                        return;
                    } else if (serialPort != null) {
                        openConnect(serialNo1, softPackageId);
                        return;
                    } else {
                        return;
                    }
                }
            }
            startActivity(new Intent(this, SerialNumberActivity.class));
            overridePendingTransition(0, 0);
            return;
        }
        Toast.makeText(this, getResources().getString(C0136R.string.sdcard), 0).show();
    }

    protected void showMenu(Context context) {
        Activity activity = (Activity) context;
        if (menuWindow == null) {
            menuWindow = new MenuSelectPicPopupWindow((Activity) context);
        }
        menuWindow.setMainActitity(this);
        if (menuWindow.isShowing()) {
            menuWindow.dismiss();
        } else {
            menuWindow.showAtLocation(activity.findViewById(16908290).getRootView(), 17, 0, 0);
        }
    }

    private void openApp() throws NameNotFoundException {
        PackageInfo pi = getPackageManager().getPackageInfo("com.cnlaunch.golo3", 0);
        Intent resolveIntent = new Intent("android.intent.action.MAIN", null);
        resolveIntent.addCategory("android.intent.category.LAUNCHER");
        resolveIntent.setPackage(pi.packageName);
        ResolveInfo ri = (ResolveInfo) getPackageManager().queryIntentActivities(resolveIntent, 0).iterator().next();
        if (ri != null) {
            String packageName1 = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setComponent(new ComponentName(packageName1, className));
            startActivity(intent);
        }
    }

    public void doLeftRight(int keyCode) {
        if (!this.mzCar.hasFocus() && !this.ozCar.hasFocus() && !this.yzCar.hasFocus() && !this.menuBtn.hasFocus() && !this.btn_login.hasFocus()) {
            int mCurScreen = this.mWorkSpace.getmCurScreen();
            int index;
            if (keyCode == 21) {
                if (mCurScreen > 0) {
                    this.mWorkSpace.setmCurScreen(mCurScreen - 1);
                    index = this.mWorkSpace.getmCurScreen();
                    this.mWorkSpace.snapToScreen(index);
                    if (index == 0) {
                        this.menuBtn.setSelected(false);
                        this.btn_login.setSelected(false);
                        this.mzCar.setSelected(f2129D);
                        this.yzCar.setSelected(false);
                        this.ozCar.setSelected(false);
                        this.mzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.red));
                        this.yzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
                        this.ozCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
                    } else if (index == REQUEST_CONNECT_DEVICE) {
                        this.menuBtn.setSelected(false);
                        this.btn_login.setSelected(false);
                        this.mzCar.setSelected(false);
                        this.yzCar.setSelected(false);
                        this.ozCar.setSelected(f2129D);
                        this.mzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
                        this.yzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
                        this.ozCar.setTextColor(mContexts.getResources().getColor(C0136R.color.red));
                    } else if (index == REQUEST_ENABLE_BT) {
                        this.menuBtn.setSelected(false);
                        this.btn_login.setSelected(false);
                        this.mzCar.setSelected(false);
                        this.yzCar.setSelected(f2129D);
                        this.ozCar.setSelected(false);
                        this.mzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
                        this.yzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.red));
                        this.ozCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
                    }
                }
            } else if (keyCode == 22 && mCurScreen < this.mWorkSpace.getChildCount() - 1) {
                this.mWorkSpace.setmCurScreen(mCurScreen + REQUEST_CONNECT_DEVICE);
                index = this.mWorkSpace.getmCurScreen();
                this.mWorkSpace.snapToScreen(index);
                if (index == 0) {
                    this.menuBtn.setSelected(false);
                    this.btn_login.setSelected(false);
                    this.mzCar.setSelected(f2129D);
                    this.yzCar.setSelected(false);
                    this.ozCar.setSelected(false);
                    this.mzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.red));
                    this.yzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
                    this.ozCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
                } else if (index == REQUEST_CONNECT_DEVICE) {
                    this.menuBtn.setSelected(false);
                    this.btn_login.setSelected(false);
                    this.mzCar.setSelected(false);
                    this.yzCar.setSelected(false);
                    this.ozCar.setSelected(f2129D);
                    this.mzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
                    this.yzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
                    this.ozCar.setTextColor(mContexts.getResources().getColor(C0136R.color.red));
                } else if (index == REQUEST_ENABLE_BT) {
                    this.menuBtn.setSelected(false);
                    this.btn_login.setSelected(false);
                    this.mzCar.setSelected(false);
                    this.yzCar.setSelected(f2129D);
                    this.ozCar.setSelected(false);
                    this.mzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
                    this.yzCar.setTextColor(mContexts.getResources().getColor(C0136R.color.red));
                    this.ozCar.setTextColor(mContexts.getResources().getColor(C0136R.color.white));
                }
            }
        }
    }

    private void onWindowsChangeSet() {
        initLeftBtnNew(this, 0);
    }

    private String[] readSdFile(String carid) {
        File file = new File(Constant.LOCAL_SERIALNO_PATH + MySharedPreferences.getStringValue(contexts, MySharedPreferences.serialNoKey) + "/DIAGNOSTIC/VEHICLES/" + carid);
        String[] verSionString = new String[((int) file.length())];
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < file.listFiles().length; i += REQUEST_CONNECT_DEVICE) {
                verSionString[i] = files[i].getName();
            }
        }
        return verSionString;
    }

    public void checkConnector() {
        Builder builder = new Builder(this);
        builder.setMessage(getResources().getString(C0136R.string.check_connector_before_upgrade));
        builder.setTitle(contexts.getString(C0136R.string.initializeTilte));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(C0136R.string.Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                new DownloadBinUpdate(MainActivity.this, Constant.mChatService, MainActivity.this.mHandler, false).checkUpdateAsync();
            }
        });
        builder.setNegativeButton(getResources().getString(C0136R.string.No), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private View downLoadAdobeReader() {
        WindowManager wm = (WindowManager) mContexts.getSystemService("window");
        this.width1 = wm.getDefaultDisplay().getWidth();
        this.height1 = wm.getDefaultDisplay().getHeight();
        this.tipsView = LayoutInflater.from(mContexts).inflate(C0136R.layout.download_adobe, null, false);
        this.version = (TextView) this.tipsView.findViewById(C0136R.id.about_text);
        this.version.setText(new StringBuilder(String.valueOf(mContexts.getResources().getString(C0136R.string.main_tools_pdf))).append(":").append(SpecilApiUtil.LINE_SEP).append("http://get.adobe.com/reader/otherversions/").toString());
        this.aboutKnowBtn = (Button) this.tipsView.findViewById(C0136R.id.ikonwBtn);
        this.aboutKnowBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.aboutDialog.dismiss();
            }
        });
        return this.tipsView;
    }

    private void tipDiaglog() {
        this.aboutDialog = new Builder(mContexts).create();
        this.aboutDialog.show();
        this.aboutDialog.setCancelable(f2129D);
        if (getResources().getConfiguration().orientation == REQUEST_ENABLE_BT) {
            this.aboutDialog.setContentView(downLoadAdobeReader(), new ViewGroup.LayoutParams(this.width1 - 150, this.height1 - 100));
        } else if (getResources().getConfiguration().orientation == REQUEST_CONNECT_DEVICE) {
            this.aboutDialog.setContentView(downLoadAdobeReader(), new ViewGroup.LayoutParams(-1, -1));
        }
    }

    public void openConnect(String serialNo1, String softPackageId) {
        this.serialNo = serialNo1;
        this.packgeId = softPackageId;
        if (Constant.mChatService.getState() == REQUEST_CIRCLE_ADD_IMAGE) {
            loadDymicLid(serialNo1, softPackageId);
        } else {
            Constant.mChatService.connect(serialPort, f2129D);
        }
    }

    public void loadDymicLid(String serialNo1, String softPackageId) {
        ArrayList<CarVersionInfo> list;
        String tempLanName = Locale.getDefault().getCountry().toUpperCase();
        if (tempLanName.equals(Constants.DEFAULT_LANGUAGE) || tempLanName.equals("ZH") || tempLanName.equals("CN") || tempLanName.equals("TW") || tempLanName.equals("HK")) {
            tempLanName = this.lanName;
            list = DBDao.getInstance(this).queryCarVersion(softPackageId, this.lanName, serialNo1, database);
            if (list.size() <= 0 && (tempLanName.equals("TW") || tempLanName.equals("HK"))) {
                country = Constants.DEFAULT_LANGUAGE;
                list = DBDao.getInstance(this).queryCarVersion(softPackageId, Constants.DEFAULT_LANGUAGE, serialNo1, database);
            }
        } else {
            list = DBDao.getInstance(this).queryCarVersion(softPackageId, tempLanName, serialNo1, database);
            if (list.size() <= 0) {
                country = Constants.DEFAULT_LANGUAGE;
                list = DBDao.getInstance(this).queryCarVersion(softPackageId, Constants.DEFAULT_LANGUAGE, serialNo1, database);
            }
        }
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i += REQUEST_CONNECT_DEVICE) {
                String carId = ((CarVersionInfo) list.get(i)).getCarId();
                String lanName = ((CarVersionInfo) list.get(i)).getLanName();
                String versionDir = ((CarVersionInfo) list.get(i)).getVersionDir();
                String serialNo = ((CarVersionInfo) list.get(i)).getVersionDir();
                ((CarVersionInfo) list.get(i)).getSoftId();
            }
            Intent intent = new Intent(this, LoadDynamicLibsActivity.class);
            intent.putExtra("loadSpecail", this.loadSpecail);
            intent.putExtra("list", list);
            intent.putExtra("carId", ((CarVersionInfo) list.get(0)).getCarId());
            intent.putExtra("lanName", ((CarVersionInfo) list.get(0)).getLanName());
            startActivity(intent);
            hasLoadLib = f2129D;
            overridePendingTransition(0, 0);
            return;
        }
        Toast.makeText(this, getResources().getString(C0136R.string.main_lanuage_null), 0).show();
    }
}

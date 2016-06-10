package com.ifoer.expedition.BluetoothChat;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.mycar.jni.JniX431FileTest;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.ifoer.adapter.DataValueAdapter;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.entity.DataStream;
import com.ifoer.entity.IntData;
import com.ifoer.entity.MenuData;
import com.ifoer.entity.SptActiveTest;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptInputNumric;
import com.ifoer.entity.SptInputStringEx;
import com.ifoer.entity.SptMessageBoxText;
import com.ifoer.entity.SptStreamSelectIdItem;
import com.ifoer.entity.SptTroubleTest;
import com.ifoer.entity.SptVwDataStreamIdItem;
import com.ifoer.entity.Spt_Nobuttonbox_Text;
import com.ifoer.expedition.cto.CToJava;
import com.ifoer.expeditionphone.BaseActivity;
import com.ifoer.expeditionphone.DiagnoseDetailActivity;
import com.ifoer.expeditionphone.DiagnoseReportActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.mine.Contact;
import com.ifoer.util.DataStreamTask;
import com.ifoer.util.DataStreamTaskManager;
import com.ifoer.util.DataStreamTaskManagerThread;
import com.ifoer.util.Files;
import com.ifoer.util.GraphView;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.util.SimpleDialogControl;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import com.tencent.mm.sdk.platformtools.Util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class DataValueShowActivity extends BaseActivity {
    private static final boolean f2118D = true;
    public static boolean isReceiver;
    private DataValueAdapter adapter;
    private ImageView backSuperior;
    private boolean basicFlag;
    private Bundle bundle;
    private LinearLayout car_maintain;
    private String cc;
    private Chronometer chronometer;
    private Context contexts;
    private int count;
    private int currentCheckedItem;
    DBDao dao;
    private ArrayList<DataStream> dataStreamList;
    private String diagversion;
    private Dialog dialog;
    private SptExDataStreamIdItem exDataStreamIdItem;
    private ArrayList<SptExDataStreamIdItem> exDataStreamIdlist;
    private AlertDialog exidApp;
    private String fileDir;
    private int fileId;
    private Boolean flag;
    private Boolean flag2;
    private GraphView graphView;
    private int grp;
    private int hlsx;
    private ArrayList<IntData> intDataForItemSelected;
    private Boolean isExecuteD;
    private boolean isRecord;
    private JniX431FileTest jnitest;
    private int f2119k;
    private String language;
    private LinearLayout linel;
    private ArrayList<IntData> listStr;
    private List<ArrayList<?>> lists;
    private ListView listview;
    private ArrayList<ArrayList<SptExDataStreamIdItem>> llist;
    private final Handler mHandler;
    private LinearLayout main_head;
    public LinearLayout menuBtn;
    private IntentFilter myIntentFilter;
    private String name;
    private ArrayList<SptExDataStreamIdItem> newExDataStreamIdlist;
    private boolean openFlag;
    private String pathImg;
    private String pathTxt;
    private ProgressDialog progressDialog;
    private mBroadcastReceiver receiver;
    private TextView recordStatus;
    private StringBuffer sb;
    private String sdCardDir;
    private String serialNo;
    private String softPackageId;
    private DataStreamTaskManager taskManager;
    private Timer timer;
    private double times;
    private String verLocal;
    private String x431fileName;

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataValueShowActivity.1 */
    class C04011 extends Handler {
        C04011() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    if (DataValueShowActivity.this.flag.booleanValue() && DataValueShowActivity.this.flag2.booleanValue() && DataValueShowActivity.this.adapter != null && DataValueShowActivity.this.listview != null && DataValueShowActivity.this.listview.getAdapter() != null) {
                        DataValueShowActivity.this.adapter.refresh(DataValueShowActivity.this.newExDataStreamIdlist);
                        DataValueShowActivity.this.llist.add(DataValueShowActivity.this.newExDataStreamIdlist);
                        DataValueShowActivity.this.taskManager.addDownloadTask(new DataStreamTask(DataValueShowActivity.this.contexts, DataValueShowActivity.this.newExDataStreamIdlist, DataValueShowActivity.this.lists, DataValueShowActivity.this.jnitest, DataValueShowActivity.this.grp, DataValueShowActivity.this.openFlag, DataValueShowActivity.this.basicFlag, DataValueShowActivity.isReceiver, DataValueShowActivity.this.isRecord, DataValueShowActivity.this.mHandler));
                    }
                case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                    DataValueShowActivity.this.openFlag = false;
                    DataValueShowActivity.this.basicFlag = false;
                case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
                    DataValueShowActivity.this.openFlag = DataValueShowActivity.f2118D;
                    DataValueShowActivity.this.basicFlag = DataValueShowActivity.f2118D;
                case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                    DataValueShowActivity dataValueShowActivity = DataValueShowActivity.this;
                    dataValueShowActivity.times = dataValueShowActivity.times + 1.0d;
                    if (DataValueShowActivity.this.dialog != null && DataValueShowActivity.this.dialog.isShowing()) {
                        DataValueShowActivity.this.graphView.pushDataToChart(DataValueShowActivity.this.lists, DataValueShowActivity.this.times, DataValueShowActivity.this.currentCheckedItem);
                    }
                case 10101010:
                    if (DataValueShowActivity.this.progressDialog != null && DataValueShowActivity.this.progressDialog.isShowing()) {
                        SimpleDialog.closeProgressDialog(DataValueShowActivity.this.progressDialog);
                    }
                    SimpleDialog.checkConectior(DataValueShowActivity.mContexts, DataValueShowActivity.this.getString(C0136R.string.initializeTilte), DataValueShowActivity.this.getString(C0136R.string.check_connector));
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataValueShowActivity.2 */
    class C04022 implements OnClickListener {
        C04022() {
        }

        public void onClick(View v) {
            DataValueShowActivity.this.finish();
            DataValueShowActivity.this.overridePendingTransition(0, 0);
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataValueShowActivity.3 */
    class C04033 implements DialogInterface.OnClickListener {
        private final /* synthetic */ String val$path;

        C04033(String str) {
            this.val$path = str;
        }

        public void onClick(DialogInterface dialog, int which) {
            if (TextUtils.isEmpty(DataValueShowActivity.this.cc)) {
                DataValueShowActivity.this.toSeeReportOne(this.val$path);
            } else {
                DataValueShowActivity.this.toSeeReportList();
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataValueShowActivity.4 */
    class C04054 implements DialogInterface.OnClickListener {

        /* renamed from: com.ifoer.expedition.BluetoothChat.DataValueShowActivity.4.1 */
        class C04041 extends Thread {
            C04041() {
            }

            public void run() {
                try {
                    C04041.sleep(500);
                    SimpleDialog.closeProgressDialog(DataValueShowActivity.this.progressDialog);
                    DataValueShowActivity.this.overridePendingTransition(0, 0);
                    DataValueShowActivity.this.closeDialog();
                    DataValueShowActivity.this.finish();
                    DataValueShowActivity.this.overridePendingTransition(0, 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        C04054() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Intent exitbroad = new Intent();
            exitbroad.setAction("exitdiagnose_dbscar");
            exitbroad.putExtra("exitdiagnose", Contact.RELATION_FRIEND);
            DataValueShowActivity.this.contexts.sendBroadcast(exitbroad);
            new C04041().start();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataValueShowActivity.5 */
    class C04065 implements DialogInterface.OnClickListener {
        C04065() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class MyTimerTask extends TimerTask {
        MyTimerTask() {
        }

        public void run() {
            CToJava.streamFlag = Boolean.valueOf(DataValueShowActivity.f2118D);
            if (DataValueShowActivity.isReceiver && Constant.bridge != null) {
                Constant.bridge.putData();
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {

        /* renamed from: com.ifoer.expedition.BluetoothChat.DataValueShowActivity.mBroadcastReceiver.1 */
        class C04071 extends Thread {
            C04071() {
            }

            public void run() {
                DataValueShowActivity.this.mHandler.sendMessage(DataValueShowActivity.this.mHandler.obtainMessage(15));
            }
        }

        /* renamed from: com.ifoer.expedition.BluetoothChat.DataValueShowActivity.mBroadcastReceiver.2 */
        class C04082 extends Thread {
            C04082() {
            }

            public void run() {
                try {
                    C04082.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Process.killProcess(Process.myPid());
                DataValueShowActivity.this.finish();
                DataValueShowActivity.this.overridePendingTransition(0, 0);
            }
        }

        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals("DataStreamSelectSite") && intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                SimpleDialog.closeProgressDialog(DataValueShowActivity.this.progressDialog);
                DataValueShowActivity.this.exDataStreamIdlist = null;
                DataValueShowActivity.this.exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                DataValueShowActivity.this.listStr = (ArrayList) DataValueShowActivity.this.bundle.getSerializable("siteList");
                if (DataValueShowActivity.this.listStr != null && DataValueShowActivity.this.listStr.size() > 0) {
                    DataValueShowActivity.this.newExDataStreamIdlist.clear();
                    for (int i = 0; i < DataValueShowActivity.this.listStr.size(); i++) {
                        DataValueShowActivity.this.newExDataStreamIdlist.add((SptExDataStreamIdItem) DataValueShowActivity.this.exDataStreamIdlist.get(((IntData) DataValueShowActivity.this.listStr.get(i)).getItem()));
                    }
                }
                new C04071().start();
            }
            if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                SimpleDialog.closeProgressDialog(DataValueShowActivity.this.progressDialog);
                if (Constant.mChatService != null) {
                    Constant.mChatService.stop();
                }
                DataValueShowActivity.this.closeDialog();
                Toast.makeText(DataValueShowActivity.this, DataValueShowActivity.this.getResources().getString(C0136R.string.connectionLost), 0).show();
                new C04082().start();
            }
            if (!DataValueShowActivity.this.isExecuteD.booleanValue()) {
                return;
            }
            ArrayList<MenuData> menuDataList;
            Intent intent2;
            if (intent.getAction().equals("feedbackMeauData")) {
                DataValueShowActivity.this.timer.cancel();
                CToJava.streamFlag = Boolean.valueOf(false);
                menuDataList = (ArrayList) intent.getExtras().getSerializable("menuData");
                intent2 = new Intent(DataValueShowActivity.this, CarDiagnoseActivity.class);
                intent2.putExtra("menuData", menuDataList);
                intent2.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(DataValueShowActivity.this.progressDialog);
                DataValueShowActivity.this.startActivity(intent2);
                DataValueShowActivity.this.overridePendingTransition(0, 0);
                DataValueShowActivity.this.closeDialog();
                DataValueShowActivity.this.finish();
                DataValueShowActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_DS_MENU_ID")) {
                DataValueShowActivity.this.timer.cancel();
                CToJava.streamFlag = Boolean.valueOf(false);
                menuDataList = (ArrayList) intent.getExtras().getSerializable("SPT_DS_MENU_ID");
                intent2 = new Intent(DataValueShowActivity.this, CarDiagnoseActivity.class);
                intent2.putExtra("menuData", menuDataList);
                intent2.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(DataValueShowActivity.this.progressDialog);
                DataValueShowActivity.this.startActivity(intent2);
                DataValueShowActivity.this.overridePendingTransition(0, 0);
                DataValueShowActivity.this.closeDialog();
                DataValueShowActivity.this.finish();
                DataValueShowActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_ACTIVE_TEST")) {
                DataValueShowActivity.this.timer.cancel();
                CToJava.streamFlag = Boolean.valueOf(false);
                SptActiveTest sptActiveTest = (SptActiveTest) intent.getExtras().getSerializable("ACTIVE_TEST");
                Intent active = new Intent(DataValueShowActivity.this, ActiveTestActivity.class);
                active.putExtra("ACTIVE_TEST", sptActiveTest);
                active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(DataValueShowActivity.this.progressDialog);
                DataValueShowActivity.this.startActivity(active);
                DataValueShowActivity.this.overridePendingTransition(0, 0);
                DataValueShowActivity.this.closeDialog();
                DataValueShowActivity.this.finish();
                DataValueShowActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_NOBUTTONBOX_TEXT")) {
                if (DataValueShowActivity.this.progressDialog == null) {
                    DataValueShowActivity.this.progressDialog = new ProgressDialog(DataValueShowActivity.this.contexts);
                    DataValueShowActivity.this.progressDialog.setCancelable(false);
                } else {
                    DataValueShowActivity.this.progressDialog.dismiss();
                    DataValueShowActivity.this.progressDialog = null;
                    DataValueShowActivity.this.progressDialog = new ProgressDialog(DataValueShowActivity.this.contexts);
                    DataValueShowActivity.this.progressDialog.setCancelable(false);
                }
                Spt_Nobuttonbox_Text nobuttonbox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("Nobuttonbox");
                SimpleDialog.openProgressDialog(DataValueShowActivity.this.contexts, nobuttonbox.getTitle(), nobuttonbox.getContent(), DataValueShowActivity.this.progressDialog);
            } else if (intent.getAction().equals("closeNobuttonBox")) {
                SimpleDialog.closeProgressDialog(DataValueShowActivity.this.progressDialog);
            } else if (intent.getAction().equals("SPT_TROUBLE_CODE")) {
                DataValueShowActivity.this.timer.cancel();
                CToJava.streamFlag = Boolean.valueOf(false);
                ArrayList<SptTroubleTest> troubleTestList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE");
                intent2 = new Intent(DataValueShowActivity.this, FaultCodeActivity.class);
                intent2.putExtra("SPT_TROUBLE_CODE", troubleTestList);
                intent2.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(DataValueShowActivity.this.progressDialog);
                DataValueShowActivity.this.startActivity(intent2);
                DataValueShowActivity.this.overridePendingTransition(0, 0);
                DataValueShowActivity.this.closeDialog();
                DataValueShowActivity.this.finish();
                DataValueShowActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_EXIT_SHOW_WINDOW")) {
                if (DataValueShowActivity.this.openFlag) {
                    String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    DataValueShowActivity.this.openFlag = false;
                    DataValueShowActivity.this.jnitest.writeEndCloseFile(DataValueShowActivity.this.grp, date, DataValueShowActivity.this.fileId, DataValueShowActivity.this.hlsx, DataValueShowActivity.this.x431fileName);
                    DBDao.getInstance(DataValueShowActivity.this.contexts).addReport(DataValueShowActivity.this.x431fileName, date, DataValueShowActivity.this.serialNo, new StringBuilder(String.valueOf(DataValueShowActivity.this.fileDir)).append(DataValueShowActivity.this.x431fileName).toString(), Contact.RELATION_FRIEND, MainActivity.database);
                }
                DataValueShowActivity.this.timer.cancel();
                CToJava.streamFlag = Boolean.valueOf(false);
                if (Constant.mChatService != null) {
                    Constant.mChatService.stop();
                }
                SimpleDialog.closeProgressDialog(DataValueShowActivity.this.progressDialog);
                DataValueShowActivity.this.closeDialog();
                DataValueShowActivity.this.finish();
                DataValueShowActivity.this.overridePendingTransition(0, 0);
                Process.killProcess(Process.myPid());
                System.exit(0);
            } else if (intent.getAction().equals("SPT_STREAM_SELECT_ID_EX")) {
                DataValueShowActivity.this.timer.cancel();
                CToJava.streamFlag = Boolean.valueOf(false);
                ArrayList<SptStreamSelectIdItem> streamSelectIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_STREAM_SELECT_ID_EX");
                intent2 = new Intent(DataValueShowActivity.this, StreamSelectActivity.class);
                intent2.putExtra("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                intent2.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(DataValueShowActivity.this.progressDialog);
                DataValueShowActivity.this.startActivity(intent2);
                DataValueShowActivity.this.overridePendingTransition(0, 0);
                DataValueShowActivity.this.closeDialog();
                DataValueShowActivity.this.finish();
                DataValueShowActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_MESSAGEBOX_TEXT")) {
                SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) intent.getExtras().getSerializable("SPT_MESSAGEBOX_TEXT");
                SimpleDialogControl.showDiaglog(DataValueShowActivity.this.contexts, sptMessageBoxText.getDialogType(), sptMessageBoxText);
            } else if (intent.getAction().equals("ConnectionLost")) {
                Toast.makeText(DataValueShowActivity.this, "Device connection was lost", 0).show();
            } else if (intent.getAction().equals("SPT_INPUTSTRING_EX")) {
                SimpleDialog.closeProgressDialog(DataValueShowActivity.this.progressDialog);
                SptInputStringEx inputString = (SptInputStringEx) intent.getExtras().getSerializable("SPT_INPUTSTRING_EX");
                SimpleDialog.sptInputStringExDiagnose(DataValueShowActivity.this.contexts, inputString.getDialogTitle(), inputString.getDialogContent(), inputString.getInputHint());
            } else if (intent.getAction().equals("SPT_INPUT_NUMERIC")) {
                SimpleDialog.closeProgressDialog(DataValueShowActivity.this.progressDialog);
                SptInputNumric inputNumric = (SptInputNumric) intent.getExtras().getSerializable("SPT_INPUT_NUMERIC");
                SimpleDialog.sptInputNumericDiagnose(DataValueShowActivity.this.contexts, inputNumric.getDialogTitle(), inputNumric.getDialogContent(), inputNumric.getInputHint(), inputNumric.getDigit());
            } else if (intent.getAction().equals("SPT_INPUTBOX_TEXT")) {
                SimpleDialog.closeProgressDialog(DataValueShowActivity.this.progressDialog);
                Spt_Nobuttonbox_Text inputBox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTBOX_TEXT");
                SimpleDialog.sptInputBoxTextDiagnose(DataValueShowActivity.this.contexts, inputBox.getTitle(), inputBox.getContent());
            } else if (intent.getAction().equals("SPT_INPUTSTRING")) {
                SimpleDialog.closeProgressDialog(DataValueShowActivity.this.progressDialog);
                Spt_Nobuttonbox_Text inputStr = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTSTRING");
                SimpleDialog.sptInputStringDiagnose(DataValueShowActivity.this.contexts, inputStr.getTitle(), inputStr.getContent());
            } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN")) {
                DataValueShowActivity.this.timer.cancel();
                CToJava.streamFlag = Boolean.valueOf(false);
                ArrayList<SptTroubleTest> troubleCodeFrozenList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN");
                intent2 = new Intent(DataValueShowActivity.this, FaultCodeFrozenActivity.class);
                intent2.putExtra("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                intent2.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(DataValueShowActivity.this.progressDialog);
                DataValueShowActivity.this.startActivity(intent2);
                DataValueShowActivity.this.overridePendingTransition(0, 0);
                DataValueShowActivity.this.closeDialog();
                DataValueShowActivity.this.finish();
                DataValueShowActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                DataValueShowActivity.this.timer.cancel();
                CToJava.streamFlag = Boolean.valueOf(false);
                ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                intent2 = new Intent(DataValueShowActivity.this, VWDataStreamActivity.class);
                intent2.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                intent2.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(DataValueShowActivity.this.progressDialog);
                DataValueShowActivity.this.startActivity(intent2);
                DataValueShowActivity.this.overridePendingTransition(0, 0);
                DataValueShowActivity.this.closeDialog();
                DataValueShowActivity.this.finish();
                DataValueShowActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_SHOW_PICTURE")) {
                SimpleDialog.closeProgressDialog(DataValueShowActivity.this.progressDialog);
                SimpleDialog.sptShowPictureDiagnose(DataValueShowActivity.this.contexts, intent.getExtras().getString("SPT_SHOW_PICTURE"));
            }
        }
    }

    public DataValueShowActivity() {
        this.exDataStreamIdlist = new ArrayList();
        this.dataStreamList = new ArrayList();
        this.exDataStreamIdItem = null;
        this.llist = new ArrayList();
        this.sb = null;
        this.f2119k = 1;
        this.dao = DBDao.getInstance(this);
        this.isExecuteD = Boolean.valueOf(false);
        this.timer = new Timer();
        this.flag = Boolean.valueOf(false);
        this.flag2 = Boolean.valueOf(false);
        this.lists = new ArrayList();
        this.jnitest = new JniX431FileTest();
        this.openFlag = false;
        this.basicFlag = false;
        this.isRecord = false;
        this.fileDir = XmlPullParser.NO_NAMESPACE;
        this.dialog = null;
        this.currentCheckedItem = 0;
        this.times = 0.0d;
        this.graphView = null;
        this.listStr = null;
        this.newExDataStreamIdlist = new ArrayList();
        this.intDataForItemSelected = new ArrayList();
        this.count = 0;
        this.mHandler = new C04011();
    }

    static {
        isReceiver = f2118D;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        mContexts = this;
        setContentView(C0136R.layout.data_value);
        this.language = Files.getLanguage();
        this.verLocal = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.diagnosticSoftwareVersionNo);
        this.diagversion = this.softPackageId + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + this.verLocal;
        DataStreamTaskManager.getInstance();
        new Thread(new DataStreamTaskManagerThread()).start();
        this.taskManager = DataStreamTaskManager.getInstance();
        MyApplication.getInstance().addActivity(this);
        initLeftBtnNew(this, 0);
        LinearLayout menuBtn = (LinearLayout) findViewById(C0136R.id.menuBtn);
        menuBtn.setClickable(false);
        menuBtn.setVisibility(8);
        registerBoradcastReceiver();
        initView();
        this.cc = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.CCKey);
        this.fileDir = Constant.DST_FILE;
        BluetoothChatService.setHandler(this.mHandler);
    }

    private void initView() {
        this.bundle = getIntent().getExtras();
        if (this.bundle != null) {
            this.exDataStreamIdlist = (ArrayList) this.bundle.getSerializable("SPT_EX_DATASTREAM_ID");
            this.listStr = (ArrayList) this.bundle.getSerializable("siteList");
            this.newExDataStreamIdlist.clear();
            for (int i = 0; i < this.listStr.size(); i++) {
                this.newExDataStreamIdlist.add((SptExDataStreamIdItem) this.exDataStreamIdlist.get(((IntData) this.listStr.get(i)).getItem()));
            }
            this.lists.add(this.newExDataStreamIdlist);
        }
        this.main_head = (LinearLayout) findViewById(C0136R.id.mainTop);
        this.backSuperior = (ImageView) findViewById(C0136R.id.backSuperior);
        this.backSuperior.setVisibility(0);
        this.backSuperior.setOnClickListener(new C04022());
        getDate();
        this.listview = (ListView) findViewById(C0136R.id.data_value_view);
        this.adapter = new DataValueAdapter(this.newExDataStreamIdlist, this);
        this.listview.setAdapter(this.adapter);
        this.flag = Boolean.valueOf(f2118D);
    }

    private void getDate() {
        this.sb = new StringBuffer();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (this.f2119k == 1) {
            this.sb.append("=====" + date + "=====");
            this.sb.append("\n\n");
        }
        for (int i = 0; i < this.newExDataStreamIdlist.size(); i++) {
            this.exDataStreamIdItem = (SptExDataStreamIdItem) this.newExDataStreamIdlist.get(i);
            this.sb.append("   " + this.exDataStreamIdItem.getStreamTextIdContent() + "   " + this.exDataStreamIdItem.getStreamStr() + "   " + this.exDataStreamIdItem.getStreamState() + "   " + "\n\n");
        }
    }

    protected void onResume() {
        super.onResume();
        this.isExecuteD = Boolean.valueOf(f2118D);
        this.softPackageId = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.savesoftPackageId);
        this.serialNo = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.serialNoKey);
        this.name = this.softPackageId + "_DS_";
    }

    protected void onStop() {
        super.onStop();
        this.isExecuteD = Boolean.valueOf(false);
    }

    public void SaveTxt(String name, boolean ss) {
        getDate();
        try {
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            this.sdCardDir = this.fileDir;
            File dirFile = new File(this.sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            name = name.replaceAll("EOBD2", "OBD2");
            this.pathTxt = this.sdCardDir + name + date + ".txt";
            if (!new File(this.pathTxt).exists()) {
                DBDao.getInstance(this).addReport(new StringBuilder(String.valueOf(name)).append(date).append(".txt").toString(), date, this.serialNo, this.pathTxt, Contact.RELATION_ASK, MainActivity.database);
                FileOutputStream outStream = new FileOutputStream(this.pathTxt, f2118D);
                OutputStreamWriter writer = new OutputStreamWriter(outStream, AsyncHttpResponseHandler.DEFAULT_CHARSET);
                writer.write(this.sb.toString());
                writer.flush();
                writer.close();
                outStream.close();
                toSeeReport(this.pathTxt);
            }
        } catch (Exception e) {
        }
    }

    private void toSeeReport(String path) {
        new Builder(this.contexts).setTitle(C0136R.string.report_toast).setPositiveButton(C0136R.string.to_see_report, new C04033(path)).setNegativeButton(C0136R.string.cancel, null).setCancelable(false).show();
    }

    protected void toSeeReportOne(String path) {
        Intent intent = new Intent(this.contexts, DiagnoseDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(LocaleUtil.INDONESIAN, -2);
        bundle.putString("path", path);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    protected void toSeeReportList() {
        Intent intent = new Intent(this.contexts, DiagnoseReportActivity.class);
        intent.putExtra(MultipleAddresses.CC, this.cc);
        startActivity(intent);
    }

    protected void onPause() {
        super.onPause();
    }

    public void saveBitmaps(Bitmap bitmap) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            this.sdCardDir = this.fileDir;
            File dirFile = new File(this.sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            this.name = this.name.replaceAll("EOBD2", "OBD2");
            File file = new File(this.sdCardDir, this.name + date + Util.PHOTO_DEFAULT_EXT);
            if (file.exists()) {
                file.delete();
                file = new File(this.sdCardDir, this.name + date + Util.PHOTO_DEFAULT_EXT);
            } else {
                this.pathImg = this.sdCardDir + this.name + date + Util.PHOTO_DEFAULT_EXT;
                this.dao.addReport(this.name + date + Util.PHOTO_DEFAULT_EXT, date, this.serialNo, this.pathImg, Contact.RELATION_ASK, MainActivity.database);
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                return;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            } catch (IOException e2) {
                e2.printStackTrace();
                return;
            }
        }
        Toast.makeText(this, getResources().getString(C0136R.string.sdcard), 0).show();
    }

    protected void onDestroy() {
        SimpleDialog.closeProgressDialog(this.progressDialog);
        super.onDestroy();
        if (this.receiver != null) {
            unregisterReceiver(this.receiver);
        }
    }

    public Bitmap zoomBitmap(Bitmap target) {
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) width) / ((float) width), ((float) height) / ((float) height));
        int main_height = this.main_head.getHeight();
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int y = (main_height + frame.top) + 50;
        return Bitmap.createBitmap(target, 0, y, width, height - y, matrix, f2118D);
    }

    public void registerBoradcastReceiver() {
        this.receiver = new mBroadcastReceiver();
        this.myIntentFilter = new IntentFilter();
        this.myIntentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        this.myIntentFilter.addAction("feedbackMeauData");
        this.myIntentFilter.addAction("SPT_ACTIVE_TEST");
        this.myIntentFilter.addAction("SPT_NOBUTTONBOX_TEXT");
        this.myIntentFilter.addAction("closeNobuttonBox");
        this.myIntentFilter.addAction("SPT_TROUBLE_CODE");
        this.myIntentFilter.addAction("SPT_EXIT_SHOW_WINDOW");
        this.myIntentFilter.addAction("SPT_STREAM_SELECT_ID_EX");
        this.myIntentFilter.addAction("SPT_EX_DATASTREAM_ID");
        this.myIntentFilter.addAction("SPT_MESSAGEBOX_TEXT");
        this.myIntentFilter.addAction("ConnectionLost");
        this.myIntentFilter.addAction("SPT_INPUTSTRING_EX");
        this.myIntentFilter.addAction("SPT_INPUT_NUMERIC");
        this.myIntentFilter.addAction("SPT_INPUTBOX_TEXT");
        this.myIntentFilter.addAction("SPT_INPUTSTRING");
        this.myIntentFilter.addAction("SPT_TROUBLE_CODE_FROZEN");
        this.myIntentFilter.addAction("SPT_VW_DATASTREAM_ID");
        this.myIntentFilter.addAction("SPT_DS_MENU_ID");
        this.myIntentFilter.addAction("SPT_SHOW_PICTURE");
        this.myIntentFilter.addAction("DataStreamSelectSite");
        this.myIntentFilter.addAction("RCU_State_08");
        this.myIntentFilter.addAction("RCU_State_09");
        this.myIntentFilter.addAction("RCU_State_0A");
        this.myIntentFilter.addAction("com.launch.golo.ExitFloatingWindow");
        this.myIntentFilter.addAction("RCU_OCLICK_DATASTREAM");
        registerReceiver(this.receiver, this.myIntentFilter);
        this.flag2 = Boolean.valueOf(f2118D);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            ExitDialog();
        }
        return f2118D;
    }

    public Dialog ExitDialog() {
        if (this.exidApp == null || !this.exidApp.isShowing()) {
            Builder builder = new Builder(this.contexts);
            builder.setMessage(C0136R.string.exitDynamicDepot);
            builder.setCancelable(f2118D);
            builder.setPositiveButton(getResources().getString(C0136R.string.enter), new C04054());
            builder.setNegativeButton(getResources().getString(C0136R.string.cancel), new C04065());
            this.exidApp = builder.create();
            this.exidApp.show();
        } else {
            this.exidApp.cancel();
        }
        return this.exidApp;
    }

    private void closeDialog() {
        if (this.exidApp != null && this.exidApp.isShowing()) {
            this.exidApp.cancel();
        }
    }
}

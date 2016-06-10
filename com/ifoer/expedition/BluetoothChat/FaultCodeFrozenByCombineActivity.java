package com.ifoer.expedition.BluetoothChat;

import CRP.utils.CRPTools;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.adapter.FaultCodeAdapter;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.entity.SptActiveTest;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptInputNumric;
import com.ifoer.entity.SptInputStringEx;
import com.ifoer.entity.SptMessageBoxText;
import com.ifoer.entity.SptStreamSelectIdItem;
import com.ifoer.entity.SptTroubleTest;
import com.ifoer.entity.SptVwDataStreamIdItem;
import com.ifoer.entity.Spt_Nobuttonbox_Text;
import com.ifoer.expedition.BluetoothOrder.ByteHexHelper;
import com.ifoer.expedition.cto.CToJava;
import com.ifoer.expeditionphone.DiagnoseDetailActivity;
import com.ifoer.expeditionphone.DiagnoseReportActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.expeditionphone.ShowFileActivity;
import com.ifoer.mine.Contact;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.NetPOSPrinter;
import com.ifoer.util.SimpleDialog;
import com.ifoer.util.SimpleDialogControl;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import com.tencent.mm.sdk.platformtools.Util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;

public class FaultCodeFrozenByCombineActivity extends Activity implements OnClickListener {
    private static final boolean f1270D = true;
    public static IntentFilter myIntentFilter;
    private static ProgressDialog progressDialog;
    public static mBroadcastReceiver receiver;
    private final int MSG_PRINT_RESULT_SCREEN_SHOT;
    private final int MSG_PRINT_START;
    private FaultCodeAdapter adapter;
    Handler baseHandler;
    Bitmap bit_first;
    Bitmap bit_second;
    Bitmap bitmap1;
    private Bundle bundle;
    String cc;
    private Button chakan;
    private int cishu;
    private Context contexts;
    private int count;
    DBDao dao;
    private String data;
    private File file;
    private int f1271i;
    private Intent intent;
    private Boolean isExecuteF;
    private boolean isShowDialog;
    private int f1272j;
    private Button jietu;
    private int f1273k;
    private long lastTime;
    private ListView listview;
    private RelativeLayout main_head;
    public LinearLayout menuBtn;
    String name;
    private int pagecount;
    private int pagesize;
    private String pathImg;
    int printResult;
    String f1274s;
    private StringBuffer sb;
    Runnable screenBmp;
    private String sdCardDir;
    private String serialNo;
    private String softPackageId;
    private ArrayList<SptTroubleTest> troubleCodeFrozenList;
    private SptTroubleTest troubleTest;
    private Button wenzi;

    /* renamed from: com.ifoer.expedition.BluetoothChat.FaultCodeFrozenByCombineActivity.1 */
    class C04431 implements Runnable {
        C04431() {
        }

        public void run() {
            NetPOSPrinter bmpPrinter = new NetPOSPrinter(FaultCodeFrozenByCombineActivity.this);
            FaultCodeFrozenByCombineActivity.this.bit_first = bmpPrinter.drawBitFirst();
            FaultCodeFrozenByCombineActivity.this.bit_second = bmpPrinter.drawBitSecond(FaultCodeFrozenByCombineActivity.this.sb.toString());
            FaultCodeFrozenByCombineActivity.this.bitmap1 = NetPOSPrinter.mixtureBitmap(FaultCodeFrozenByCombineActivity.this.bit_first, FaultCodeFrozenByCombineActivity.this.bit_second);
            FaultCodeFrozenByCombineActivity.this.printResult = bmpPrinter.printPic(FaultCodeFrozenByCombineActivity.this.bitmap1);
            bmpPrinter.resultToast(FaultCodeFrozenByCombineActivity.this.printResult);
            FaultCodeFrozenByCombineActivity.this.baseHandler.obtainMessage(0, Integer.valueOf(FaultCodeFrozenByCombineActivity.this.printResult)).sendToTarget();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.FaultCodeFrozenByCombineActivity.2 */
    class C04442 extends Handler {
        C04442() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case InBandBytestreamManager.MAXIMUM_BLOCK_SIZE /*65535*/:
                    FaultCodeFrozenByCombineActivity.this.dayin();
                case 10101010:
                    if (FaultCodeFrozenByCombineActivity.progressDialog != null && FaultCodeFrozenByCombineActivity.progressDialog.isShowing()) {
                        SimpleDialog.closeProgressDialog(FaultCodeFrozenByCombineActivity.progressDialog);
                    }
                    SimpleDialog.checkConectior(FaultCodeFrozenByCombineActivity.this.contexts, FaultCodeFrozenByCombineActivity.this.getString(C0136R.string.initializeTilte), FaultCodeFrozenByCombineActivity.this.getString(C0136R.string.check_connector));
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.FaultCodeFrozenByCombineActivity.3 */
    class C04453 implements OnItemClickListener {
        C04453() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            Constant.feedback = null;
            Constant.feedback = ByteHexHelper.intToHexBytes(arg2);
            CToJava.activeFlag = Boolean.valueOf(false);
            CToJava.streamFlag = Boolean.valueOf(FaultCodeFrozenByCombineActivity.f1270D);
            Constant.streamNextCode = Constant.noInterruptStreamCode;
            Constant.activeNextCode = Constant.noButton;
            if (Constant.bridge != null) {
                Constant.bridge.putData();
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.FaultCodeFrozenByCombineActivity.4 */
    class C04464 implements DialogInterface.OnClickListener {
        private final /* synthetic */ String val$path;

        C04464(String str) {
            this.val$path = str;
        }

        public void onClick(DialogInterface dialog, int which) {
            FaultCodeFrozenByCombineActivity.this.isShowDialog = FaultCodeFrozenByCombineActivity.f1270D;
            if (TextUtils.isEmpty(FaultCodeFrozenByCombineActivity.this.cc)) {
                FaultCodeFrozenByCombineActivity.this.toSeeReportOne(this.val$path);
            } else if (DBDao.getInstance(FaultCodeFrozenByCombineActivity.this.contexts).queryReport(FaultCodeFrozenByCombineActivity.this.cc, Contact.RELATION_ASK, MainActivity.database).size() < 1) {
                FaultCodeFrozenByCombineActivity.this.toSeeReportOne(this.val$path);
            } else {
                FaultCodeFrozenByCombineActivity.this.toSeeReportList();
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.FaultCodeFrozenByCombineActivity.5 */
    class C04475 implements DialogInterface.OnClickListener {
        C04475() {
        }

        public void onClick(DialogInterface dialog, int which) {
            FaultCodeFrozenByCombineActivity.this.isShowDialog = FaultCodeFrozenByCombineActivity.f1270D;
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (FaultCodeFrozenByCombineActivity.this.isExecuteF.booleanValue()) {
                if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                    if ((System.currentTimeMillis() - FaultCodeFrozenByCombineActivity.this.lastTime) / 1000 > 30) {
                        SimpleDialog.ExitDialog(context);
                    }
                }
                if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                    FaultCodeFrozenByCombineActivity.this.lastTime = System.currentTimeMillis();
                }
                if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    SimpleDialog.closeProgressDialog(FaultCodeFrozenByCombineActivity.progressDialog);
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    FaultCodeFrozenByCombineActivity.this.finish();
                    FaultCodeFrozenByCombineActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("feedbackMeauData")) {
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("menuData");
                    menu = new Intent(FaultCodeFrozenByCombineActivity.this, CarDiagnoseActivity.class);
                    menu.putExtra("menuData", menuDataList);
                    menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeFrozenByCombineActivity.progressDialog);
                    FaultCodeFrozenByCombineActivity.this.startActivity(menu);
                    FaultCodeFrozenByCombineActivity.this.overridePendingTransition(0, 0);
                    FaultCodeFrozenByCombineActivity.this.finish();
                    FaultCodeFrozenByCombineActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_DS_MENU_ID")) {
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("SPT_DS_MENU_ID");
                    menu = new Intent(FaultCodeFrozenByCombineActivity.this, CarDiagnoseActivity.class);
                    menu.putExtra("menuData", menuDataList);
                    menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeFrozenByCombineActivity.progressDialog);
                    FaultCodeFrozenByCombineActivity.this.startActivity(menu);
                    FaultCodeFrozenByCombineActivity.this.overridePendingTransition(0, 0);
                    FaultCodeFrozenByCombineActivity.this.finish();
                    FaultCodeFrozenByCombineActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_ACTIVE_TEST")) {
                    SptActiveTest sptActiveTest = (SptActiveTest) intent.getExtras().getSerializable("ACTIVE_TEST");
                    Intent active = new Intent(FaultCodeFrozenByCombineActivity.this, ActiveTestActivity.class);
                    active.putExtra("ACTIVE_TEST", sptActiveTest);
                    active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeFrozenByCombineActivity.progressDialog);
                    FaultCodeFrozenByCombineActivity.this.startActivity(active);
                    FaultCodeFrozenByCombineActivity.this.overridePendingTransition(0, 0);
                    FaultCodeFrozenByCombineActivity.this.finish();
                    FaultCodeFrozenByCombineActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_NOBUTTONBOX_TEXT")) {
                    if (FaultCodeFrozenByCombineActivity.progressDialog == null) {
                        FaultCodeFrozenByCombineActivity.progressDialog = new ProgressDialog(FaultCodeFrozenByCombineActivity.this.contexts);
                        FaultCodeFrozenByCombineActivity.progressDialog.setCancelable(false);
                    } else {
                        FaultCodeFrozenByCombineActivity.progressDialog.dismiss();
                        FaultCodeFrozenByCombineActivity.progressDialog = null;
                        FaultCodeFrozenByCombineActivity.progressDialog = new ProgressDialog(FaultCodeFrozenByCombineActivity.this.contexts);
                        FaultCodeFrozenByCombineActivity.progressDialog.setCancelable(false);
                    }
                    Spt_Nobuttonbox_Text nobuttonbox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("Nobuttonbox");
                    SimpleDialog.openProgressDialog(FaultCodeFrozenByCombineActivity.this.contexts, nobuttonbox.getTitle(), nobuttonbox.getContent(), FaultCodeFrozenByCombineActivity.progressDialog);
                } else if (intent.getAction().equals("closeNobuttonBox")) {
                    SimpleDialog.closeProgressDialog(FaultCodeFrozenByCombineActivity.progressDialog);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE")) {
                    ArrayList<SptTroubleTest> troubleTestList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE");
                    r0 = new Intent(FaultCodeFrozenByCombineActivity.this, FaultCodeFrozenByCombineActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE", troubleTestList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeFrozenByCombineActivity.progressDialog);
                    FaultCodeFrozenByCombineActivity.this.startActivity(r0);
                    FaultCodeFrozenByCombineActivity.this.overridePendingTransition(0, 0);
                    FaultCodeFrozenByCombineActivity.this.finish();
                    FaultCodeFrozenByCombineActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EXIT_SHOW_WINDOW")) {
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    SimpleDialog.closeProgressDialog(FaultCodeFrozenByCombineActivity.progressDialog);
                    FaultCodeFrozenByCombineActivity.this.finish();
                    FaultCodeFrozenByCombineActivity.this.overridePendingTransition(0, 0);
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                } else if (intent.getAction().equals("SPT_STREAM_SELECT_ID_EX")) {
                    ArrayList<SptStreamSelectIdItem> streamSelectIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_STREAM_SELECT_ID_EX");
                    r0 = new Intent(FaultCodeFrozenByCombineActivity.this, StreamSelectActivity.class);
                    r0.putExtra("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeFrozenByCombineActivity.progressDialog);
                    FaultCodeFrozenByCombineActivity.this.startActivity(r0);
                    FaultCodeFrozenByCombineActivity.this.overridePendingTransition(0, 0);
                    FaultCodeFrozenByCombineActivity.this.finish();
                    FaultCodeFrozenByCombineActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                    ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                    r0 = new Intent(FaultCodeFrozenByCombineActivity.this, DataStreamActivity.class);
                    r0.putExtra("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeFrozenByCombineActivity.progressDialog);
                    FaultCodeFrozenByCombineActivity.this.startActivity(r0);
                    FaultCodeFrozenByCombineActivity.this.overridePendingTransition(0, 0);
                    FaultCodeFrozenByCombineActivity.this.finish();
                    FaultCodeFrozenByCombineActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_MESSAGEBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(FaultCodeFrozenByCombineActivity.progressDialog);
                    SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) intent.getExtras().getSerializable("SPT_MESSAGEBOX_TEXT");
                    int dialogType = sptMessageBoxText.getDialogType();
                    SimpleDialogControl.showDiaglog(FaultCodeFrozenByCombineActivity.this.contexts, dialogType, sptMessageBoxText);
                } else if (intent.getAction().equals("ConnectionLost")) {
                    Toast.makeText(FaultCodeFrozenByCombineActivity.this.getApplicationContext(), "Device connection was lost", 0).show();
                } else if (intent.getAction().equals("SPT_INPUTSTRING_EX")) {
                    SimpleDialog.closeProgressDialog(FaultCodeFrozenByCombineActivity.progressDialog);
                    SptInputStringEx inputString = (SptInputStringEx) intent.getExtras().getSerializable("SPT_INPUTSTRING_EX");
                    SimpleDialog.sptInputStringExDiagnose(FaultCodeFrozenByCombineActivity.this.contexts, inputString.getDialogTitle(), inputString.getDialogContent(), inputString.getInputHint());
                } else if (intent.getAction().equals("SPT_INPUT_NUMERIC")) {
                    SimpleDialog.closeProgressDialog(FaultCodeFrozenByCombineActivity.progressDialog);
                    SptInputNumric inputNumric = (SptInputNumric) intent.getExtras().getSerializable("SPT_INPUT_NUMERIC");
                    SimpleDialog.sptInputNumericDiagnose(FaultCodeFrozenByCombineActivity.this.contexts, inputNumric.getDialogTitle(), inputNumric.getDialogContent(), inputNumric.getInputHint(), inputNumric.getDigit());
                } else if (intent.getAction().equals("SPT_INPUTBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(FaultCodeFrozenByCombineActivity.progressDialog);
                    Spt_Nobuttonbox_Text inputBox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTBOX_TEXT");
                    SimpleDialog.sptInputBoxTextDiagnose(FaultCodeFrozenByCombineActivity.this.contexts, inputBox.getTitle(), inputBox.getContent());
                } else if (intent.getAction().equals("SPT_INPUTSTRING")) {
                    SimpleDialog.closeProgressDialog(FaultCodeFrozenByCombineActivity.progressDialog);
                    Spt_Nobuttonbox_Text inputStr = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTSTRING");
                    SimpleDialog.sptInputStringDiagnose(FaultCodeFrozenByCombineActivity.this.contexts, inputStr.getTitle(), inputStr.getContent());
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN")) {
                    ArrayList<SptTroubleTest> troubleCodeFrozenList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN");
                    r0 = new Intent(FaultCodeFrozenByCombineActivity.this, FaultCodeFrozenByCombineActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeFrozenByCombineActivity.progressDialog);
                    FaultCodeFrozenByCombineActivity.this.startActivity(r0);
                    FaultCodeFrozenByCombineActivity.this.overridePendingTransition(0, 0);
                    FaultCodeFrozenByCombineActivity.this.finish();
                    FaultCodeFrozenByCombineActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                    ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                    r0 = new Intent(FaultCodeFrozenByCombineActivity.this, VWDataStreamActivity.class);
                    r0.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeFrozenByCombineActivity.progressDialog);
                    FaultCodeFrozenByCombineActivity.this.startActivity(r0);
                    FaultCodeFrozenByCombineActivity.this.overridePendingTransition(0, 0);
                    FaultCodeFrozenByCombineActivity.this.finish();
                    FaultCodeFrozenByCombineActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_SHOW_PICTURE")) {
                    SimpleDialog.closeProgressDialog(FaultCodeFrozenByCombineActivity.progressDialog);
                    String pictureName = intent.getExtras().getString("SPT_SHOW_PICTURE");
                    SimpleDialog.sptShowPictureDiagnose(FaultCodeFrozenByCombineActivity.this.contexts, pictureName);
                }
            }
        }
    }

    public FaultCodeFrozenByCombineActivity() {
        this.troubleCodeFrozenList = new ArrayList();
        this.troubleTest = null;
        this.sb = null;
        this.f1273k = 1;
        this.pagesize = 7;
        this.f1271i = 1;
        this.f1272j = 1;
        this.data = null;
        this.pagecount = 0;
        this.cishu = 0;
        this.isExecuteF = Boolean.valueOf(false);
        this.f1274s = "ni hao o,hhh";
        this.dao = DBDao.getInstance(this);
        this.softPackageId = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.savesoftPackageId);
        this.serialNo = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.serialNoKey);
        this.name = this.softPackageId + "_DTC_";
        this.bitmap1 = null;
        this.MSG_PRINT_RESULT_SCREEN_SHOT = 0;
        this.MSG_PRINT_START = InBandBytestreamManager.MAXIMUM_BLOCK_SIZE;
        this.count = 0;
        this.isShowDialog = f1270D;
        this.screenBmp = new C04431();
        this.baseHandler = new C04442();
    }

    private void getPrintDate() {
        this.sb = new StringBuffer();
        for (int i = 0; i < this.troubleCodeFrozenList.size(); i++) {
            this.troubleTest = (SptTroubleTest) this.troubleCodeFrozenList.get(i);
            this.sb.append(new StringBuilder(String.valueOf(this.troubleTest.getTroubleCodeContent())).append("   ").append(this.troubleTest.getTroubleDescribeContent()).append(this.troubleTest.getTroubleStateContent()).append(SpecilApiUtil.LINE_SEP).toString());
        }
    }

    public void dayin() {
        getPrintDate();
        new Thread(this.screenBmp).start();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        this.contexts = this;
        setContentView(C0136R.layout.fault_code);
        MyApplication.getInstance().addActivity(this);
        registerBoradcastReceiver();
        BluetoothChatService.setHandler(this.baseHandler);
        initView();
    }

    private void initView() {
        this.bundle = getIntent().getExtras();
        if (this.bundle != null) {
            this.troubleCodeFrozenList = (ArrayList) this.bundle.getSerializable("SPT_TROUBLE_CODE_FROZEN_BY_COMBINE");
        }
        this.main_head = (RelativeLayout) findViewById(C0136R.id.mainTop);
        this.wenzi = (Button) findViewById(C0136R.id.wenzi);
        this.jietu = (Button) findViewById(C0136R.id.jietu);
        this.chakan = (Button) findViewById(C0136R.id.guanli);
        this.wenzi.setOnClickListener(this);
        this.jietu.setOnClickListener(this);
        this.chakan.setOnClickListener(this);
        this.cc = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.CCKey);
        getDate();
        this.listview = (ListView) findViewById(C0136R.id.view);
        if (this.troubleCodeFrozenList != null && this.troubleCodeFrozenList.size() > 0) {
            this.adapter = new FaultCodeAdapter(this.troubleCodeFrozenList, this);
            this.listview.setAdapter(this.adapter);
            this.listview.setDividerHeight(0);
            this.listview.setOnItemClickListener(new C04453());
        }
    }

    private void getDate() {
        int i;
        for (i = 0; i < this.dao.queryReport(this.cc, Contact.RELATION_ASK, MainActivity.database).size(); i++) {
        }
        this.sb = new StringBuffer();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (this.f1273k == 1) {
            this.sb.append("=====" + date + "=====");
            this.sb.append("\n\n");
        }
        if (this.troubleCodeFrozenList.size() % this.pagesize == 0) {
            this.pagecount = this.troubleCodeFrozenList.size() / this.pagesize;
        } else {
            this.pagecount = (this.troubleCodeFrozenList.size() / this.pagesize) + 1;
        }
        for (i = 0; i < this.troubleCodeFrozenList.size(); i++) {
            this.troubleTest = (SptTroubleTest) this.troubleCodeFrozenList.get(i);
            this.sb.append("   " + this.troubleTest.getTroubleCodeContent() + "   " + this.troubleTest.getTroubleDescribeContent() + "   " + this.troubleTest.getTroubleStateContent() + "   " + "\n\n");
        }
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.wenzi) {
            if (this.f1271i != 1) {
                Toast.makeText(this, getResources().getString(C0136R.string.save_report), 0).show();
            } else if (Environment.getExternalStorageState().equals("mounted")) {
                SaveTxt(this.name, f1270D);
                this.f1271i++;
            } else {
                Toast.makeText(this, getResources().getString(C0136R.string.sdcard), 0).show();
            }
        } else if (v.getId() == C0136R.id.jietu) {
            if (this.f1272j == 1) {
                this.f1273k = 1;
                if (Environment.getExternalStorageState().equals("mounted")) {
                    saveImage();
                    this.f1272j++;
                    return;
                }
                Toast.makeText(this, getResources().getString(C0136R.string.sdcard), 0).show();
                return;
            }
            Toast.makeText(this, getResources().getString(C0136R.string.save_report), 0).show();
        } else if (v.getId() == C0136R.id.guanli) {
            this.f1273k = 1;
            this.intent = new Intent();
            this.intent.setClass(this, ShowFileActivity.class);
            startActivity(this.intent);
            overridePendingTransition(0, 0);
        }
    }

    @SuppressLint({"NewApi"})
    private void saveImage() {
        View view = getWindow().getDecorView();
        view.setDrawingCacheEnabled(f1270D);
        Bitmap bitmap = view.getDrawingCache();
        if (checksd((long) bitmap.getByteCount())) {
            saveBitmaps(zoomBitmap(bitmap));
        }
        view.setDrawingCacheEnabled(false);
    }

    public void SaveTxt(String name, boolean ss) {
        getDate();
        String s = this.sb.toString();
        if (checksd((long) s.getBytes().length)) {
            try {
                String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                this.sdCardDir = Constant.DST_FILE;
                File dirFile = new File(this.sdCardDir);
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }
                name = name.replaceAll("EOBD2", "EOBD");
                String path = this.sdCardDir + name + date + ".txt";
                if (!new File(path).exists()) {
                    DBDao.getInstance(this).addReport(new StringBuilder(String.valueOf(name)).append(date).append(".txt").toString(), date, this.serialNo, path, Contact.RELATION_ASK, MainActivity.database);
                    FileOutputStream outStream = new FileOutputStream(path, f1270D);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outStream, AsyncHttpResponseHandler.DEFAULT_CHARSET);
                    outputStreamWriter.write(s);
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                    outStream.close();
                    Toast.makeText(this, getResources().getString(C0136R.string.devfinish), 0).show();
                }
            } catch (Exception e) {
                if (e.toString().equalsIgnoreCase("java.io.IOException: write failed: ENOSPC (No space left on device)")) {
                    Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
                } else {
                    Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.io_exception), 0).show();
                }
            }
        }
    }

    protected void onPause() {
        super.onPause();
        this.cishu++;
    }

    public void saveBitmaps(Bitmap bitmap) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            String sdCardDir = Constant.DST_FILE;
            File dirFile = new File(sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            this.name = this.name.replaceAll("EOBD2", "EOBD");
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            File file = new File(sdCardDir, this.name + date + Util.PHOTO_DEFAULT_EXT);
            if (file.exists()) {
                file.delete();
                file = new File(sdCardDir, this.name + date + Util.PHOTO_DEFAULT_EXT);
            } else {
                String tempSDCardDir = sdCardDir;
                String[] tempPaths = Constant.getDefaultExternalStoragePathList();
                if (tempPaths.length > 1) {
                    tempSDCardDir = tempSDCardDir.replaceAll(tempPaths[1], tempPaths[0]);
                }
                tempSDCardDir = new StringBuilder(String.valueOf(tempSDCardDir)).append(this.name).append(date).append(Util.PHOTO_DEFAULT_EXT).toString();
                this.dao.addReport(this.name + date + Util.PHOTO_DEFAULT_EXT, date, this.serialNo, tempSDCardDir, Contact.RELATION_ASK, MainActivity.database);
                this.pathImg = tempSDCardDir;
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                toSeeReport(this.pathImg);
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

    public Bitmap zoomBitmap(Bitmap target) {
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) width) / ((float) width), ((float) height) / ((float) height));
        int main_height = this.main_head.getHeight();
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int y = main_height + frame.top;
        return Bitmap.createBitmap(target, 0, y, width, height - y, matrix, f1270D);
    }

    public void registerBoradcastReceiver() {
        receiver = new mBroadcastReceiver();
        myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        myIntentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        myIntentFilter.addAction("feedbackMeauData");
        myIntentFilter.addAction("SPT_ACTIVE_TEST");
        myIntentFilter.addAction("SPT_NOBUTTONBOX_TEXT");
        myIntentFilter.addAction("closeNobuttonBox");
        myIntentFilter.addAction("SPT_TROUBLE_CODE");
        myIntentFilter.addAction("SPT_EXIT_SHOW_WINDOW");
        myIntentFilter.addAction("SPT_STREAM_SELECT_ID_EX");
        myIntentFilter.addAction("SPT_EX_DATASTREAM_ID");
        myIntentFilter.addAction("SPT_MESSAGEBOX_TEXT");
        myIntentFilter.addAction("ConnectionLost");
        myIntentFilter.addAction("SPT_INPUTSTRING_EX");
        myIntentFilter.addAction("SPT_INPUT_NUMERIC");
        myIntentFilter.addAction("SPT_INPUTBOX_TEXT");
        myIntentFilter.addAction("SPT_INPUTSTRING");
        myIntentFilter.addAction("SPT_TROUBLE_CODE_FROZEN");
        myIntentFilter.addAction("SPT_VW_DATASTREAM_ID");
        myIntentFilter.addAction("SPT_DS_MENU_ID");
        myIntentFilter.addAction("SPT_SHOW_PICTURE");
        registerReceiver(receiver, myIntentFilter);
    }

    protected void onResume() {
        super.onResume();
        this.isExecuteF = Boolean.valueOf(f1270D);
    }

    protected void onStop() {
        super.onStop();
        this.isExecuteF = Boolean.valueOf(false);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        finish();
        overridePendingTransition(0, 0);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 && keyCode != Service.SUNRPC) {
            return super.onKeyDown(keyCode, event);
        }
        back();
        return f1270D;
    }

    private void back() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this.contexts);
            progressDialog.setCancelable(false);
        } else {
            progressDialog.dismiss();
            progressDialog = null;
            progressDialog = new ProgressDialog(this.contexts);
            progressDialog.setCancelable(false);
        }
        SimpleDialog.openProgressDialog(this.contexts, getResources().getString(C0136R.string.dataDisposeTilte), getResources().getString(C0136R.string.dataDisposeMessage), progressDialog);
        Constant.feedback = null;
        Constant.feedback = Constant.previousMenu;
        if (Constant.bridge != null) {
            Constant.bridge.putData();
        }
    }

    private boolean checksd(long filenght) {
        if (filenght <= CRPTools.getUsableSDCardSize()) {
            return f1270D;
        }
        Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
        return false;
    }

    private void toSeeReport(String path) {
        Builder builder = new Builder(this.contexts);
        builder.setTitle(C0136R.string.report_toast).setPositiveButton(C0136R.string.to_see_report, new C04464(path)).setNegativeButton(C0136R.string.cancel, new C04475()).setCancelable(false);
        if (this.isShowDialog) {
            this.isShowDialog = false;
            builder.show();
        }
    }

    protected void toSeeReportOne(String path) {
        Intent intent = new Intent(this.contexts, DiagnoseDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(LocaleUtil.INDONESIAN, -1);
        bundle.putString("path", path);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    protected void toSeeReportList() {
        Intent intent = new Intent(this.contexts, DiagnoseReportActivity.class);
        intent.putExtra(MultipleAddresses.CC, this.cc);
        startActivity(intent);
    }
}

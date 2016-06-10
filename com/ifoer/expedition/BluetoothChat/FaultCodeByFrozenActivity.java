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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
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
import com.ifoer.expeditionphone.BaseActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.expeditionphone.ShowFileActivity;
import com.ifoer.mine.Contact;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.NetPOSPrinter;
import com.ifoer.util.SimpleDialog;
import com.ifoer.util.SimpleDialogControl;
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
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;

public class FaultCodeByFrozenActivity extends BaseActivity implements OnClickListener {
    private static final boolean f2120D = true;
    public static IntentFilter myIntentFilter;
    private static ProgressDialog progressDialog;
    public static mBroadcastReceiver receiver;
    private final int MSG_PRINT_RESULT;
    private final int MSG_PRINT_RESULT_SCREEN_SHOT;
    private final int MSG_PRINT_START;
    private FaultCodeAdapter adapter;
    private ImageView backSuperior;
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
    public LinearLayout dayinBtn;
    private AlertDialog exidApp;
    private File file;
    private int f2121i;
    private Intent intent;
    private Boolean isExecuteF;
    private int f2122j;
    private Button jietu;
    private int f2123k;
    private long lastTime;
    private ListView listview;
    private RelativeLayout main_head;
    public LinearLayout menuBtn;
    String name;
    private int pagecount;
    private int pagesize;
    int printResult;
    String f2124s;
    private StringBuffer sb;
    Runnable screenBmp;
    private String sdCardDir;
    private String serialNo;
    private String softPackageId;
    private ArrayList<SptTroubleTest> troubleCodeFrozenList;
    private SptTroubleTest troubleTest;
    private Button wenzi;

    /* renamed from: com.ifoer.expedition.BluetoothChat.FaultCodeByFrozenActivity.1 */
    class C04211 implements Runnable {
        C04211() {
        }

        public void run() {
            NetPOSPrinter bmpPrinter = new NetPOSPrinter(FaultCodeByFrozenActivity.this);
            FaultCodeByFrozenActivity.this.bit_first = bmpPrinter.drawBitFirst();
            FaultCodeByFrozenActivity.this.bit_second = bmpPrinter.drawBitSecond(FaultCodeByFrozenActivity.this.sb.toString());
            FaultCodeByFrozenActivity.this.bitmap1 = NetPOSPrinter.mixtureBitmap(FaultCodeByFrozenActivity.this.bit_first, FaultCodeByFrozenActivity.this.bit_second);
            FaultCodeByFrozenActivity.this.printResult = bmpPrinter.printPic(FaultCodeByFrozenActivity.this.bitmap1);
            bmpPrinter.resultToast(FaultCodeByFrozenActivity.this.printResult);
            FaultCodeByFrozenActivity.this.baseHandler.obtainMessage(0, Integer.valueOf(FaultCodeByFrozenActivity.this.printResult)).sendToTarget();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.FaultCodeByFrozenActivity.2 */
    class C04222 extends Handler {
        C04222() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    FaultCodeByFrozenActivity.this.dayinBtn.setClickable(FaultCodeByFrozenActivity.f2120D);
                case InBandBytestreamManager.MAXIMUM_BLOCK_SIZE /*65535*/:
                    FaultCodeByFrozenActivity.this.dayin();
                case 10101010:
                    if (FaultCodeByFrozenActivity.progressDialog != null && FaultCodeByFrozenActivity.progressDialog.isShowing()) {
                        SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
                    }
                    SimpleDialog.checkConectior(FaultCodeByFrozenActivity.this.contexts, FaultCodeByFrozenActivity.this.getString(C0136R.string.initializeTilte), FaultCodeByFrozenActivity.this.getString(C0136R.string.check_connector));
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.FaultCodeByFrozenActivity.3 */
    class C04233 implements OnClickListener {
        C04233() {
        }

        public void onClick(View v) {
            FaultCodeByFrozenActivity.this.baseHandler.sendEmptyMessage(InBandBytestreamManager.MAXIMUM_BLOCK_SIZE);
            FaultCodeByFrozenActivity.this.dayinBtn.setClickable(false);
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.FaultCodeByFrozenActivity.4 */
    class C04244 implements OnClickListener {
        C04244() {
        }

        public void onClick(View v) {
            FaultCodeByFrozenActivity.this.back();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.FaultCodeByFrozenActivity.5 */
    class C04255 implements OnItemClickListener {
        C04255() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            Constant.feedback = null;
            Constant.feedback = ByteHexHelper.intToHexBytes(arg2);
            CToJava.activeFlag = Boolean.valueOf(false);
            CToJava.streamFlag = Boolean.valueOf(FaultCodeByFrozenActivity.f2120D);
            Constant.streamNextCode = Constant.noInterruptStreamCode;
            Constant.activeNextCode = Constant.noButton;
            if (Constant.bridge != null) {
                Constant.bridge.putData();
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.FaultCodeByFrozenActivity.6 */
    class C04266 implements DialogInterface.OnClickListener {
        C04266() {
        }

        public void onClick(DialogInterface dialog, int which) {
            if (Constant.mChatService != null) {
                Constant.mChatService.stop();
            }
            SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
            Process.killProcess(Process.myPid());
            System.exit(0);
            FaultCodeByFrozenActivity.this.finish();
            FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.FaultCodeByFrozenActivity.7 */
    class C04277 implements DialogInterface.OnClickListener {
        C04277() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (FaultCodeByFrozenActivity.this.isExecuteF.booleanValue()) {
                if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                    if ((System.currentTimeMillis() - FaultCodeByFrozenActivity.this.lastTime) / 1000 > 30) {
                        SimpleDialog.ExitDialog(context);
                    }
                }
                if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                    FaultCodeByFrozenActivity.this.lastTime = System.currentTimeMillis();
                }
                if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    FaultCodeByFrozenActivity.this.closeDialog();
                    FaultCodeByFrozenActivity.this.finish();
                    FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("feedbackMeauData")) {
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("menuData");
                    menu = new Intent(FaultCodeByFrozenActivity.this, CarDiagnoseActivity.class);
                    menu.putExtra("menuData", menuDataList);
                    menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
                    FaultCodeByFrozenActivity.this.startActivity(menu);
                    FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByFrozenActivity.this.closeDialog();
                    FaultCodeByFrozenActivity.this.finish();
                    FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_DS_MENU_ID")) {
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("SPT_DS_MENU_ID");
                    menu = new Intent(FaultCodeByFrozenActivity.this, CarDiagnoseActivity.class);
                    menu.putExtra("menuData", menuDataList);
                    menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
                    FaultCodeByFrozenActivity.this.startActivity(menu);
                    FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByFrozenActivity.this.closeDialog();
                    FaultCodeByFrozenActivity.this.finish();
                    FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_ACTIVE_TEST")) {
                    SptActiveTest sptActiveTest = (SptActiveTest) intent.getExtras().getSerializable("ACTIVE_TEST");
                    Intent active = new Intent(FaultCodeByFrozenActivity.this, ActiveTestActivity.class);
                    active.putExtra("ACTIVE_TEST", sptActiveTest);
                    active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
                    FaultCodeByFrozenActivity.this.startActivity(active);
                    FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByFrozenActivity.this.closeDialog();
                    FaultCodeByFrozenActivity.this.finish();
                    FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_NOBUTTONBOX_TEXT")) {
                    if (FaultCodeByFrozenActivity.progressDialog == null) {
                        FaultCodeByFrozenActivity.progressDialog = new ProgressDialog(FaultCodeByFrozenActivity.this.contexts);
                        FaultCodeByFrozenActivity.progressDialog.setCancelable(false);
                    } else {
                        FaultCodeByFrozenActivity.progressDialog.dismiss();
                        FaultCodeByFrozenActivity.progressDialog = null;
                        FaultCodeByFrozenActivity.progressDialog = new ProgressDialog(FaultCodeByFrozenActivity.this.contexts);
                        FaultCodeByFrozenActivity.progressDialog.setCancelable(false);
                    }
                    Spt_Nobuttonbox_Text nobuttonbox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("Nobuttonbox");
                    SimpleDialog.openProgressDialog(FaultCodeByFrozenActivity.this.contexts, nobuttonbox.getTitle(), nobuttonbox.getContent(), FaultCodeByFrozenActivity.progressDialog);
                } else if (intent.getAction().equals("closeNobuttonBox")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE")) {
                    ArrayList<SptTroubleTest> troubleTestList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE");
                    r0 = new Intent(FaultCodeByFrozenActivity.this, FaultCodeByFrozenActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE", troubleTestList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
                    FaultCodeByFrozenActivity.this.startActivity(r0);
                    FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByFrozenActivity.this.closeDialog();
                    FaultCodeByFrozenActivity.this.finish();
                    FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EXIT_SHOW_WINDOW")) {
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
                    FaultCodeByFrozenActivity.this.closeDialog();
                    FaultCodeByFrozenActivity.this.finish();
                    FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                } else if (intent.getAction().equals("SPT_STREAM_SELECT_ID_EX")) {
                    ArrayList<SptStreamSelectIdItem> streamSelectIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_STREAM_SELECT_ID_EX");
                    r0 = new Intent(FaultCodeByFrozenActivity.this, StreamSelectActivity.class);
                    r0.putExtra("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
                    FaultCodeByFrozenActivity.this.startActivity(r0);
                    FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByFrozenActivity.this.closeDialog();
                    FaultCodeByFrozenActivity.this.finish();
                    FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                    ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                    r0 = new Intent(FaultCodeByFrozenActivity.this, DataStreamActivity.class);
                    r0.putExtra("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
                    FaultCodeByFrozenActivity.this.startActivity(r0);
                    FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByFrozenActivity.this.closeDialog();
                    FaultCodeByFrozenActivity.this.finish();
                    FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_MESSAGEBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
                    SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) intent.getExtras().getSerializable("SPT_MESSAGEBOX_TEXT");
                    int dialogType = sptMessageBoxText.getDialogType();
                    SimpleDialogControl.showDiaglog(FaultCodeByFrozenActivity.this.contexts, dialogType, sptMessageBoxText);
                } else if (intent.getAction().equals("ConnectionLost")) {
                    Toast.makeText(FaultCodeByFrozenActivity.this.getApplicationContext(), "Device connection was lost", 0).show();
                } else if (intent.getAction().equals("SPT_INPUTSTRING_EX")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
                    SptInputStringEx inputString = (SptInputStringEx) intent.getExtras().getSerializable("SPT_INPUTSTRING_EX");
                    SimpleDialog.sptInputStringExDiagnose(FaultCodeByFrozenActivity.this.contexts, inputString.getDialogTitle(), inputString.getDialogContent(), inputString.getInputHint());
                } else if (intent.getAction().equals("SPT_INPUT_NUMERIC")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
                    SptInputNumric inputNumric = (SptInputNumric) intent.getExtras().getSerializable("SPT_INPUT_NUMERIC");
                    SimpleDialog.sptInputNumericDiagnose(FaultCodeByFrozenActivity.this.contexts, inputNumric.getDialogTitle(), inputNumric.getDialogContent(), inputNumric.getInputHint(), inputNumric.getDigit());
                } else if (intent.getAction().equals("SPT_INPUTBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
                    Spt_Nobuttonbox_Text inputBox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTBOX_TEXT");
                    SimpleDialog.sptInputBoxTextDiagnose(FaultCodeByFrozenActivity.this.contexts, inputBox.getTitle(), inputBox.getContent());
                } else if (intent.getAction().equals("SPT_INPUTSTRING")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
                    Spt_Nobuttonbox_Text inputStr = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTSTRING");
                    SimpleDialog.sptInputStringDiagnose(FaultCodeByFrozenActivity.this.contexts, inputStr.getTitle(), inputStr.getContent());
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN")) {
                    ArrayList<SptTroubleTest> troubleCodeFrozenList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN");
                    r0 = new Intent(FaultCodeByFrozenActivity.this, FaultCodeByFrozenActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
                    FaultCodeByFrozenActivity.this.startActivity(r0);
                    FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByFrozenActivity.this.closeDialog();
                    FaultCodeByFrozenActivity.this.finish();
                    FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                    ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                    r0 = new Intent(FaultCodeByFrozenActivity.this, VWDataStreamActivity.class);
                    r0.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
                    FaultCodeByFrozenActivity.this.startActivity(r0);
                    FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
                    FaultCodeByFrozenActivity.this.closeDialog();
                    FaultCodeByFrozenActivity.this.finish();
                    FaultCodeByFrozenActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_SHOW_PICTURE")) {
                    SimpleDialog.closeProgressDialog(FaultCodeByFrozenActivity.progressDialog);
                    String pictureName = intent.getExtras().getString("SPT_SHOW_PICTURE");
                    SimpleDialog.sptShowPictureDiagnose(FaultCodeByFrozenActivity.this.contexts, pictureName);
                }
            }
        }
    }

    public FaultCodeByFrozenActivity() {
        this.troubleCodeFrozenList = new ArrayList();
        this.troubleTest = null;
        this.sb = null;
        this.f2123k = 1;
        this.pagesize = 7;
        this.f2121i = 1;
        this.f2122j = 1;
        this.data = null;
        this.pagecount = 0;
        this.cishu = 0;
        this.isExecuteF = Boolean.valueOf(false);
        this.f2124s = "ni hao o,hhh";
        this.dao = DBDao.getInstance(this);
        this.softPackageId = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.savesoftPackageId);
        this.serialNo = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.serialNoKey);
        this.name = this.softPackageId + "_DTC_";
        this.bitmap1 = null;
        this.MSG_PRINT_RESULT = 0;
        this.MSG_PRINT_RESULT_SCREEN_SHOT = 0;
        this.MSG_PRINT_START = InBandBytestreamManager.MAXIMUM_BLOCK_SIZE;
        this.count = 0;
        this.screenBmp = new C04211();
        this.baseHandler = new C04222();
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
        initLeftBtnNew(this, 0);
        MyApplication.getInstance().addActivity(this);
        registerBoradcastReceiver();
        BluetoothChatService.setHandler(this.baseHandler);
        initView();
    }

    private void initView() {
        this.bundle = getIntent().getExtras();
        if (this.bundle != null) {
            this.troubleCodeFrozenList = (ArrayList) this.bundle.getSerializable("SPT_TROUBLE_CODE_FROZEN_EX");
        }
        this.main_head = (RelativeLayout) findViewById(C0136R.id.mainTop);
        this.wenzi = (Button) findViewById(C0136R.id.wenzi);
        this.jietu = (Button) findViewById(C0136R.id.jietu);
        this.chakan = (Button) findViewById(C0136R.id.guanli);
        this.wenzi.setOnClickListener(this);
        this.jietu.setOnClickListener(this);
        this.chakan.setOnClickListener(this);
        this.cc = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.CCKey);
        this.dayinBtn = (LinearLayout) findViewById(C0136R.id.printBtn);
        this.dayinBtn.setVisibility(0);
        this.dayinBtn.setOnClickListener(new C04233());
        this.backSuperior = (ImageView) findViewById(C0136R.id.backSuperior);
        this.backSuperior.setVisibility(0);
        this.backSuperior.setOnClickListener(new C04244());
        getDate();
        this.listview = (ListView) findViewById(C0136R.id.view);
        if (this.troubleCodeFrozenList != null && this.troubleCodeFrozenList.size() > 0) {
            this.adapter = new FaultCodeAdapter(this.troubleCodeFrozenList, this);
            this.listview.setAdapter(this.adapter);
            this.listview.setDividerHeight(0);
            this.listview.setOnItemClickListener(new C04255());
        }
    }

    private void getDate() {
        int i;
        for (i = 0; i < this.dao.queryReport(this.cc, Contact.RELATION_ASK, MainActivity.database).size(); i++) {
        }
        this.sb = new StringBuffer();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (this.f2123k == 1) {
            this.sb.append("=====" + date + "=====");
            this.sb.append("\n\n");
        }
        System.err.println("\u6570\u636e\u603b\u6761\u6570  " + this.troubleCodeFrozenList.size());
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
            if (this.f2121i != 1) {
                Toast.makeText(this, getResources().getString(C0136R.string.save_report), 0).show();
            } else if (Environment.getExternalStorageState().equals("mounted")) {
                SaveTxt(this.sb.toString(), this.name, f2120D);
                this.f2121i++;
            } else {
                Toast.makeText(this, getResources().getString(C0136R.string.sdcard), 0).show();
            }
        } else if (v.getId() == C0136R.id.jietu) {
            if (this.f2122j == 1) {
                this.f2123k = 1;
                if (Environment.getExternalStorageState().equals("mounted")) {
                    saveImage();
                    this.f2122j++;
                    return;
                }
                Toast.makeText(this, getResources().getString(C0136R.string.sdcard), 0).show();
                return;
            }
            Toast.makeText(this, getResources().getString(C0136R.string.save_report), 0).show();
        } else if (v.getId() == C0136R.id.guanli) {
            this.f2123k = 1;
            this.intent = new Intent();
            this.intent.setClass(this, ShowFileActivity.class);
            startActivity(this.intent);
            overridePendingTransition(0, 0);
        }
    }

    private void saveImage() {
        View view = getWindow().getDecorView();
        view.setDrawingCacheEnabled(f2120D);
        saveBitmaps(zoomBitmap(view.getDrawingCache()));
        Toast.makeText(this, getResources().getString(C0136R.string.devfinish), 0).show();
    }

    public void SaveTxt(String s, String name, boolean ss) {
        getDate();
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
                FileOutputStream outStream = new FileOutputStream(path, f2120D);
                OutputStreamWriter writer = new OutputStreamWriter(outStream, AsyncHttpResponseHandler.DEFAULT_CHARSET);
                writer.write(s);
                writer.flush();
                writer.close();
                outStream.close();
                Toast.makeText(this, getResources().getString(C0136R.string.devfinish), 0).show();
            }
        } catch (Exception e) {
        }
    }

    protected void onPause() {
        super.onPause();
        this.cishu++;
    }

    public void saveBitmaps(Bitmap bitmap) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            this.sdCardDir = Constant.DST_FILE;
            File dirFile = new File(this.sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            this.name = this.name.replaceAll("EOBD2", "EOBD");
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            File file = new File(this.sdCardDir, new StringBuilder(String.valueOf(date)).append(Util.PHOTO_DEFAULT_EXT).toString());
            if (file.exists()) {
                file.delete();
                file = new File(this.sdCardDir, new StringBuilder(String.valueOf(date)).append(Util.PHOTO_DEFAULT_EXT).toString());
            } else {
                this.dao.addReport(this.name + date + Util.PHOTO_DEFAULT_EXT, date, this.serialNo, this.sdCardDir + this.name + date + Util.PHOTO_DEFAULT_EXT, Contact.RELATION_ASK, MainActivity.database);
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

    public Bitmap zoomBitmap(Bitmap target) {
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) width) / ((float) width), ((float) height) / ((float) height));
        int main_height = this.main_head.getHeight();
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int y = main_height + frame.top;
        return Bitmap.createBitmap(target, 0, y, width, height - y, matrix, f2120D);
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
        this.isExecuteF = Boolean.valueOf(f2120D);
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
        closeDialog();
        finish();
        overridePendingTransition(0, 0);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 && keyCode != Service.SUNRPC) {
            return super.onKeyDown(keyCode, event);
        }
        back();
        return f2120D;
    }

    public Dialog ExitDialog() {
        if (this.exidApp == null || !this.exidApp.isShowing()) {
            Builder builder = new Builder(this.contexts);
            builder.setMessage(C0136R.string.exitDynamicDepot);
            builder.setCancelable(f2120D);
            builder.setPositiveButton(getResources().getString(C0136R.string.enter), new C04266());
            builder.setNegativeButton(getResources().getString(C0136R.string.cancel), new C04277());
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
}

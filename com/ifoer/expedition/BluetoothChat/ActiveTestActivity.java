package com.ifoer.expedition.BluetoothChat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.adapter.StremAdapter;
import com.ifoer.entity.Constant;
import com.ifoer.entity.SptActiveTest;
import com.ifoer.entity.SptActiveTestButton;
import com.ifoer.entity.SptActiveTestStream;
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
import com.ifoer.util.MyApplication;
import com.ifoer.util.NetPOSPrinter;
import com.ifoer.util.SimpleDialog;
import com.ifoer.util.SimpleDialogControl;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;

public class ActiveTestActivity extends Activity implements OnClickListener {
    private static final boolean f1231D = false;
    private static ArrayList<SptActiveTestButton> buttonList;
    public static byte[] feedback;
    public static IntentFilter myIntentFilter;
    private static ProgressDialog progressDialog;
    public static mBroadcastReceiver receiver;
    private static ArrayList<SptActiveTestStream> streamList;
    private final int MSG_PRINT_RESULT;
    private final int MSG_PRINT_START;
    private StremAdapter adapter;
    Handler baseHandler;
    Bitmap bit_first;
    Bitmap bit_second;
    Runnable bmp;
    private int btnLen;
    private Bundle bundle;
    private Context contexts;
    private int count;
    private LinearLayout dsLinear;
    private Boolean flag;
    private Boolean flag2;
    private HorizontalScrollView hoscolView;
    private Boolean isExecuteA;
    public boolean isPro;
    private long lastTime;
    private int listLen;
    private ListView listview;
    private final Handler mHandler;
    public LinearLayout menuBtn;
    public TextView oneText;
    Bitmap printBitmap;
    int printResult;
    private SptActiveTestStream printSptActiveTestStream;
    private StringBuffer sb;
    private SptActiveTest sptActiveTest;
    public TextView threeText;
    private Timer timer;
    private int times;
    public TextView twoText;

    /* renamed from: com.ifoer.expedition.BluetoothChat.ActiveTestActivity.1 */
    class C03501 extends Handler {
        C03501() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case InBandBytestreamManager.MAXIMUM_BLOCK_SIZE /*65535*/:
                    ActiveTestActivity.this.dayin();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.ActiveTestActivity.2 */
    class C03512 implements Runnable {
        C03512() {
        }

        public void run() {
            NetPOSPrinter bmpPrinter = new NetPOSPrinter(ActiveTestActivity.this);
            ActiveTestActivity.this.bit_first = bmpPrinter.drawBitFirst();
            ActiveTestActivity.this.bit_second = bmpPrinter.drawBitSecond(ActiveTestActivity.this.sb.toString());
            ActiveTestActivity.this.printBitmap = NetPOSPrinter.mixtureBitmap(ActiveTestActivity.this.bit_first, ActiveTestActivity.this.bit_second);
            ActiveTestActivity.this.printResult = bmpPrinter.printPic(ActiveTestActivity.this.printBitmap);
            bmpPrinter.resultToast(ActiveTestActivity.this.printResult);
            ActiveTestActivity.this.baseHandler.obtainMessage(0, Integer.valueOf(ActiveTestActivity.this.printResult)).sendToTarget();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.ActiveTestActivity.3 */
    class C03523 extends Handler {
        C03523() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    if (ActiveTestActivity.this.flag.booleanValue() && ActiveTestActivity.this.flag2.booleanValue() && ActiveTestActivity.this.adapter != null && ActiveTestActivity.this.dsLinear != null && ActiveTestActivity.this.listview != null && ActiveTestActivity.this.listview.getAdapter() != null) {
                        int btnLens;
                        int listLens;
                        ActiveTestActivity.this.adapter.refresh(ActiveTestActivity.streamList);
                        if (ActiveTestActivity.buttonList != null) {
                            btnLens = ActiveTestActivity.buttonList.size();
                        } else {
                            btnLens = 0;
                        }
                        if (ActiveTestActivity.streamList != null) {
                            listLens = ActiveTestActivity.streamList.size();
                        } else {
                            listLens = 0;
                        }
                        if (ActiveTestActivity.this.btnLen != btnLens) {
                            ActiveTestActivity.this.btnLen = btnLens;
                            CToJava.activeChangeButton = Boolean.valueOf(true);
                        }
                        if (ActiveTestActivity.this.listLen != listLens) {
                            ActiveTestActivity.this.listLen = listLens;
                            CToJava.activeChangeButton = Boolean.valueOf(true);
                        }
                        if (CToJava.activeChangeButton.booleanValue()) {
                            ActiveTestActivity.this.dsLinear.removeAllViews();
                            ActiveTestActivity.this.setButton();
                            CToJava.activeChangeButton = Boolean.valueOf(false);
                        }
                    }
                case 10101010:
                    if (ActiveTestActivity.progressDialog != null && ActiveTestActivity.progressDialog.isShowing()) {
                        SimpleDialog.closeProgressDialog(ActiveTestActivity.progressDialog);
                    }
                    SimpleDialog.checkConectior(ActiveTestActivity.this.contexts, ActiveTestActivity.this.getString(C0136R.string.initializeTilte), ActiveTestActivity.this.getString(C0136R.string.check_connector));
                default:
            }
        }
    }

    class MyTimerTask extends TimerTask {
        MyTimerTask() {
        }

        public void run() {
            CToJava.activeFlag = Boolean.valueOf(true);
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (ActiveTestActivity.this.isExecuteA.booleanValue()) {
                if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                    if ((System.currentTimeMillis() - ActiveTestActivity.this.lastTime) / 1000 > 30) {
                        SimpleDialog.ExitDialog(context);
                    }
                }
                if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                    ActiveTestActivity.this.lastTime = System.currentTimeMillis();
                }
                if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    SimpleDialog.closeProgressDialog(ActiveTestActivity.progressDialog);
                } else if (intent.getAction().equals("feedbackMeauData")) {
                    ActiveTestActivity.this.timer.cancel();
                    CToJava.activeFlag = Boolean.valueOf(false);
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("menuData");
                    menu = new Intent(ActiveTestActivity.this, CarDiagnoseActivity.class);
                    menu.putExtra("menuData", menuDataList);
                    menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(ActiveTestActivity.progressDialog);
                    ActiveTestActivity.this.startActivity(menu);
                    ActiveTestActivity.this.overridePendingTransition(0, 0);
                    ActiveTestActivity.this.finish();
                    ActiveTestActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_DS_MENU_ID")) {
                    ActiveTestActivity.this.timer.cancel();
                    CToJava.activeFlag = Boolean.valueOf(false);
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("SPT_DS_MENU_ID");
                    menu = new Intent(ActiveTestActivity.this, CarDiagnoseActivity.class);
                    menu.putExtra("menuData", menuDataList);
                    menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(ActiveTestActivity.progressDialog);
                    ActiveTestActivity.this.startActivity(menu);
                    ActiveTestActivity.this.overridePendingTransition(0, 0);
                    ActiveTestActivity.this.finish();
                    ActiveTestActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_ACTIVE_TEST")) {
                    SimpleDialog.closeProgressDialog(ActiveTestActivity.progressDialog);
                    ActiveTestActivity.this.sptActiveTest = (SptActiveTest) intent.getExtras().getSerializable("ACTIVE_TEST");
                    if (ActiveTestActivity.this.sptActiveTest != null) {
                        ActiveTestActivity.buttonList = ActiveTestActivity.this.sptActiveTest.getActiveTestButtons();
                        ActiveTestActivity.streamList = ActiveTestActivity.this.sptActiveTest.getActiveTestStreams();
                    }
                    Message msg = ActiveTestActivity.this.mHandler.obtainMessage(15);
                    ActiveTestActivity.this.mHandler.sendMessage(msg);
                } else if (intent.getAction().equals("SPT_NOBUTTONBOX_TEXT")) {
                    if (ActiveTestActivity.progressDialog == null) {
                        ActiveTestActivity.progressDialog = new ProgressDialog(ActiveTestActivity.this.contexts);
                        ActiveTestActivity.progressDialog.setCancelable(false);
                    } else {
                        ActiveTestActivity.progressDialog.dismiss();
                        ActiveTestActivity.progressDialog = null;
                        ActiveTestActivity.progressDialog = new ProgressDialog(ActiveTestActivity.this.contexts);
                    }
                    Spt_Nobuttonbox_Text nobuttonbox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("Nobuttonbox");
                    SimpleDialog.openProgressDialog(ActiveTestActivity.this.contexts, nobuttonbox.getTitle(), nobuttonbox.getContent(), ActiveTestActivity.progressDialog);
                } else if (intent.getAction().equals("closeNobuttonBox")) {
                    SimpleDialog.closeProgressDialog(ActiveTestActivity.progressDialog);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE")) {
                    ActiveTestActivity.this.timer.cancel();
                    CToJava.activeFlag = Boolean.valueOf(false);
                    ArrayList<SptTroubleTest> troubleTestList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE");
                    r0 = new Intent(ActiveTestActivity.this, FaultCodeActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE", troubleTestList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(ActiveTestActivity.progressDialog);
                    ActiveTestActivity.this.startActivity(r0);
                    ActiveTestActivity.this.overridePendingTransition(0, 0);
                    ActiveTestActivity.this.finish();
                    ActiveTestActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EXIT_SHOW_WINDOW")) {
                    ActiveTestActivity.this.timer.cancel();
                    CToJava.activeFlag = Boolean.valueOf(false);
                    SimpleDialog.closeProgressDialog(ActiveTestActivity.progressDialog);
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    ActiveTestActivity.this.finish();
                    ActiveTestActivity.this.overridePendingTransition(0, 0);
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                } else if (intent.getAction().equals("SPT_STREAM_SELECT_ID_EX")) {
                    ActiveTestActivity.this.timer.cancel();
                    CToJava.activeFlag = Boolean.valueOf(false);
                    ArrayList<SptStreamSelectIdItem> streamSelectIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_STREAM_SELECT_ID_EX");
                    r0 = new Intent(ActiveTestActivity.this, StreamSelectActivity.class);
                    r0.putExtra("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(ActiveTestActivity.progressDialog);
                    ActiveTestActivity.this.startActivity(r0);
                    ActiveTestActivity.this.overridePendingTransition(0, 0);
                    ActiveTestActivity.this.finish();
                    ActiveTestActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                    ActiveTestActivity.this.timer.cancel();
                    CToJava.activeFlag = Boolean.valueOf(false);
                    ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                    r0 = new Intent(ActiveTestActivity.this, DataStreamActivity.class);
                    r0.putExtra("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(ActiveTestActivity.progressDialog);
                    ActiveTestActivity.this.startActivity(r0);
                    ActiveTestActivity.this.overridePendingTransition(0, 0);
                    ActiveTestActivity.this.finish();
                    ActiveTestActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_MESSAGEBOX_TEXT")) {
                    SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) intent.getExtras().getSerializable("SPT_MESSAGEBOX_TEXT");
                    int dialogType = sptMessageBoxText.getDialogType();
                    SimpleDialogControl.showDiaglog(ActiveTestActivity.this.contexts, dialogType, sptMessageBoxText);
                } else if (intent.getAction().equals("ConnectionLost")) {
                    Toast.makeText(ActiveTestActivity.this.getApplicationContext(), ActiveTestActivity.this.getResources().getString(C0136R.string.devlost), 0).show();
                    ActiveTestActivity.this.finish();
                    ActiveTestActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_INPUTSTRING_EX")) {
                    SimpleDialog.closeProgressDialog(ActiveTestActivity.progressDialog);
                    SptInputStringEx inputString = (SptInputStringEx) intent.getExtras().getSerializable("SPT_INPUTSTRING_EX");
                    SimpleDialog.sptInputStringExDiagnose(ActiveTestActivity.this.contexts, inputString.getDialogTitle(), inputString.getDialogContent(), inputString.getInputHint());
                } else if (intent.getAction().equals("SPT_INPUT_NUMERIC")) {
                    SimpleDialog.closeProgressDialog(ActiveTestActivity.progressDialog);
                    SptInputNumric inputNumric = (SptInputNumric) intent.getExtras().getSerializable("SPT_INPUT_NUMERIC");
                    SimpleDialog.sptInputNumericDiagnose(ActiveTestActivity.this.contexts, inputNumric.getDialogTitle(), inputNumric.getDialogContent(), inputNumric.getInputHint(), inputNumric.getDigit());
                } else if (intent.getAction().equals("SPT_INPUTBOX_TEXT")) {
                    CToJava.activeFlag = Boolean.valueOf(false);
                    CToJava.inputBox = Boolean.valueOf(false);
                    SimpleDialog.closeProgressDialog(ActiveTestActivity.progressDialog);
                    Spt_Nobuttonbox_Text inputBox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTBOX_TEXT");
                    SimpleDialog.sptInputBoxTextDiagnose(ActiveTestActivity.this.contexts, inputBox.getTitle(), inputBox.getContent());
                } else if (intent.getAction().equals("SPT_INPUTSTRING")) {
                    SimpleDialog.closeProgressDialog(ActiveTestActivity.progressDialog);
                    Spt_Nobuttonbox_Text inputStr = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTSTRING");
                    SimpleDialog.sptInputStringDiagnose(ActiveTestActivity.this.contexts, inputStr.getTitle(), inputStr.getContent());
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN")) {
                    ActiveTestActivity.this.timer.cancel();
                    CToJava.activeFlag = Boolean.valueOf(false);
                    ArrayList<SptTroubleTest> troubleCodeFrozenList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN");
                    r0 = new Intent(ActiveTestActivity.this, FaultCodeFrozenActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(ActiveTestActivity.progressDialog);
                    ActiveTestActivity.this.startActivity(r0);
                    ActiveTestActivity.this.overridePendingTransition(0, 0);
                    ActiveTestActivity.this.finish();
                    ActiveTestActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                    ActiveTestActivity.this.timer.cancel();
                    CToJava.activeFlag = Boolean.valueOf(false);
                    ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                    r0 = new Intent(ActiveTestActivity.this, VWDataStreamActivity.class);
                    r0.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(ActiveTestActivity.progressDialog);
                    ActiveTestActivity.this.startActivity(r0);
                    ActiveTestActivity.this.overridePendingTransition(0, 0);
                    ActiveTestActivity.this.finish();
                    ActiveTestActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_SHOW_PICTURE")) {
                    SimpleDialog.closeProgressDialog(ActiveTestActivity.progressDialog);
                    String pictureName = intent.getExtras().getString("SPT_SHOW_PICTURE");
                    SimpleDialog.sptShowPictureDiagnose(ActiveTestActivity.this.contexts, pictureName);
                }
            }
        }
    }

    public ActiveTestActivity() {
        this.isPro = false;
        this.sptActiveTest = new SptActiveTest();
        this.flag = Boolean.valueOf(false);
        this.flag2 = Boolean.valueOf(false);
        this.timer = new Timer();
        this.isExecuteA = Boolean.valueOf(false);
        this.times = 0;
        this.listLen = 0;
        this.btnLen = 0;
        this.sb = null;
        this.printSptActiveTestStream = null;
        this.MSG_PRINT_RESULT = 0;
        this.MSG_PRINT_START = InBandBytestreamManager.MAXIMUM_BLOCK_SIZE;
        this.count = 0;
        this.baseHandler = new C03501();
        this.bmp = new C03512();
        this.mHandler = new C03523();
    }

    static {
        feedback = null;
        buttonList = new ArrayList();
        streamList = new ArrayList();
    }

    private void getPrintDate() {
        this.sb = new StringBuffer();
        for (int i = 0; i < streamList.size(); i++) {
            this.printSptActiveTestStream = (SptActiveTestStream) streamList.get(i);
            this.sb.append(new StringBuilder(String.valueOf(this.printSptActiveTestStream.getDataStreamContent())).append("   ").append(this.printSptActiveTestStream.getDataStreamStr()).append(this.printSptActiveTestStream.getUnit()).append(SpecilApiUtil.LINE_SEP).toString());
        }
    }

    public void dayin() {
        getPrintDate();
        new Thread(this.bmp).start();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.stream);
        MyApplication.getInstance().addActivity(this);
        this.contexts = this;
        CToJava.inputBox = Boolean.valueOf(true);
        registerBoradcastReceiver();
        initView();
        this.timer = new Timer();
        this.timer.schedule(new MyTimerTask(), 500, 500);
        BluetoothChatService.setHandler(this.mHandler);
    }

    private void initView() {
        this.bundle = getIntent().getExtras();
        if (this.bundle != null) {
            this.sptActiveTest = (SptActiveTest) this.bundle.getSerializable("ACTIVE_TEST");
            if (this.sptActiveTest != null) {
                buttonList = this.sptActiveTest.getActiveTestButtons();
                if (buttonList != null) {
                    this.btnLen = buttonList.size();
                }
                streamList = this.sptActiveTest.getActiveTestStreams();
                if (streamList != null) {
                    this.listLen = streamList.size();
                }
            }
        }
        this.listview = (ListView) findViewById(C0136R.id.view);
        this.adapter = new StremAdapter(this, streamList);
        this.oneText = (TextView) findViewById(C0136R.id.oneText);
        this.twoText = (TextView) findViewById(C0136R.id.twoText);
        this.threeText = (TextView) findViewById(C0136R.id.threeText);
        this.dsLinear = (LinearLayout) findViewById(C0136R.id.dsLinear);
        this.hoscolView = (HorizontalScrollView) findViewById(C0136R.id.horizontal);
        if (checkIsPro()) {
            this.isPro = true;
            setText(this.contexts.getString(C0136R.string.FourText), this.contexts.getString(C0136R.string.download_software_condition), false);
        }
        setButton();
        this.listview.setAdapter(this.adapter);
        this.flag = Boolean.valueOf(true);
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onDestroy() {
        SimpleDialog.closeProgressDialog(progressDialog);
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    protected void onResume() {
        super.onResume();
        this.isExecuteA = Boolean.valueOf(true);
    }

    protected void onStop() {
        super.onStop();
        this.isExecuteA = Boolean.valueOf(false);
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
        this.flag2 = Boolean.valueOf(true);
        CToJava.activeFlag = Boolean.valueOf(true);
    }

    private void setButton() {
        int buttonListNum = buttonList.size();
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        if (buttonList.size() > 0) {
            this.hoscolView.setVisibility(0);
            LayoutParams params = new LayoutParams(-1, -1);
            params.weight = 1.0f;
            params.height = -2;
            params.width = -2;
            params.width = (screenWidth / buttonListNum) - (10 * buttonListNum);
            Log.i("ActiveTestActivity", "button size" + buttonList.size());
            for (int i = 0; i < buttonList.size(); i++) {
                Button button = new Button(this.contexts);
                button.setId(((SptActiveTestButton) buttonList.get(i)).getActiveButtonSite());
                button.setText(((SptActiveTestButton) buttonList.get(i)).getActiveButtonContent());
                button.setBackgroundResource(C0136R.drawable.red_button_selector);
                button.setTextColor(-1);
                button.setLayoutParams(params);
                button.setOnClickListener(this);
                this.dsLinear.addView(button);
            }
            return;
        }
        this.hoscolView.setVisibility(8);
    }

    private void setButton2() {
        int buttonListNum = buttonList.size();
        if (buttonListNum > 0) {
            int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
            LayoutParams params = new LayoutParams(-2, -1);
            params.weight = 1.0f;
            params.height = -1;
            int padding_space = (int) getResources().getDimension(C0136R.dimen.padding_space);
            params.width = (screenWidth / buttonListNum) - (padding_space * buttonListNum);
            params.setMargins(padding_space, padding_space, padding_space, padding_space);
            for (int i = 0; i < buttonListNum; i++) {
                View view = LayoutInflater.from(this.contexts).inflate(C0136R.layout.mag_gallery_item, null);
                Button button = new Button(this.contexts);
                button.setId(((SptActiveTestButton) buttonList.get(i)).getActiveButtonSite());
                button.setText(((SptActiveTestButton) buttonList.get(i)).getActiveButtonContent());
                button.setBackgroundResource(C0136R.drawable.red_button_selector);
                button.setTextColor(-1);
                button.setLayoutParams(params);
                button.setOnClickListener(this);
                this.dsLinear.addView(button);
            }
            return;
        }
        this.hoscolView.setVisibility(8);
    }

    public void onClick(View v) {
        Constant.activeNextCode = ByteHexHelper.intToHexBytes(((SptActiveTestButton) buttonList.get(v.getId())).getActiveButtonSite());
        CToJava.activeChangeButton = Boolean.valueOf(true);
        CToJava.activeFlag = Boolean.valueOf(false);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != Service.SUNRPC && keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        back();
        return true;
    }

    private void back() {
        if ((Constant.mChatService == null || Constant.mChatService.getState() != 3) && !Constant.isDemo) {
            finish();
            overridePendingTransition(0, 0);
            return;
        }
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this.contexts);
            progressDialog.setCancelable(false);
        } else {
            progressDialog.dismiss();
            progressDialog = null;
            progressDialog = new ProgressDialog(this.contexts);
        }
        SimpleDialog.openProgressDialog(this.contexts, getResources().getString(C0136R.string.dataDisposeTilte), getResources().getString(C0136R.string.dataDisposeMessage), progressDialog);
        Constant.feedback = null;
        Constant.feedback = Constant.previousMenu;
        if (Constant.bridge != null) {
            Constant.bridge.putData();
        }
    }

    public void setText(String str, String value, boolean isStream) {
        this.oneText.setText(str);
        this.twoText.setText(value);
        if (isStream) {
            this.threeText.setVisibility(0);
        } else {
            this.threeText.setVisibility(8);
        }
    }

    private boolean checkIsPro() {
        for (int i = 0; i < streamList.size(); i++) {
            String[] list = ((SptActiveTestStream) streamList.get(i)).getDataStreamContent().split(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            String protocol = list[list.length - 1];
            if (!protocol.equalsIgnoreCase("Protocol") || protocol.equals("protocolo")) {
                return false;
            }
        }
        return true;
    }
}

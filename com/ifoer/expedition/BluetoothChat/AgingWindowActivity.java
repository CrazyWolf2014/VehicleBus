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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.Constant;
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
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.util.SimpleDialogControl;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class AgingWindowActivity extends Activity {
    private static final boolean f1232D = false;
    private final int MSG_PRINT_RESULT;
    private final int MSG_PRINT_START;
    private String agingContent;
    Handler baseHandler;
    Bitmap bit_first;
    Bitmap bit_second;
    private Bundle bundle;
    private Context contexts;
    private int count;
    private Boolean flag;
    private Boolean flag2;
    private Boolean isExecuteC;
    private long lastTime;
    private final Handler mHandler;
    public LinearLayout menuBtn;
    private IntentFilter myIntentFilter;
    Bitmap printBitmap;
    int printResult;
    private ProgressDialog progressDialog;
    private mBroadcastReceiver receiver;
    private TextView textView;
    private Timer timer;

    /* renamed from: com.ifoer.expedition.BluetoothChat.AgingWindowActivity.1 */
    class C03531 extends Handler {
        C03531() {
        }

        public void handleMessage(Message msg) {
            int i = msg.what;
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.AgingWindowActivity.2 */
    class C03542 extends Handler {
        C03542() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    if (AgingWindowActivity.this.flag.booleanValue() && AgingWindowActivity.this.flag2.booleanValue() && AgingWindowActivity.this.textView != null) {
                        AgingWindowActivity.this.textView.setText(AgingWindowActivity.this.agingContent);
                    }
                case 10101010:
                    Log.e("nxy AgingWindowActivity", "\u8d85\u65f6\u91cd\u53d13\u6b21");
                    if (AgingWindowActivity.this.progressDialog != null && AgingWindowActivity.this.progressDialog.isShowing()) {
                        SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                    }
                    SimpleDialog.checkConectior(AgingWindowActivity.this.contexts, AgingWindowActivity.this.getString(C0136R.string.initializeTilte), AgingWindowActivity.this.getString(C0136R.string.check_connector));
                default:
            }
        }
    }

    class MyTimerTask extends TimerTask {
        MyTimerTask() {
        }

        public void run() {
            CToJava.agingFlag = Boolean.valueOf(true);
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {

        /* renamed from: com.ifoer.expedition.BluetoothChat.AgingWindowActivity.mBroadcastReceiver.1 */
        class C03551 extends Thread {
            C03551() {
            }

            public void run() {
                AgingWindowActivity.this.mHandler.sendMessage(AgingWindowActivity.this.mHandler.obtainMessage(15));
            }
        }

        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (AgingWindowActivity.this.isExecuteC.booleanValue()) {
                if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                    if ((System.currentTimeMillis() - AgingWindowActivity.this.lastTime) / 1000 > 30) {
                        SimpleDialog.ExitDialog(context);
                    }
                }
                if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                    AgingWindowActivity.this.lastTime = System.currentTimeMillis();
                }
                if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                    AgingWindowActivity.this.finish();
                    AgingWindowActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("feedbackMeauData")) {
                    ArrayList<MenuData> menuDataLists = (ArrayList) intent.getExtras().getSerializable("menuData");
                    menu = new Intent(AgingWindowActivity.this, CarDiagnoseActivityTwo.class);
                    menu.putExtra("menuData", menuDataLists);
                    menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                    AgingWindowActivity.this.startActivity(menu);
                    AgingWindowActivity.this.overridePendingTransition(0, 0);
                    AgingWindowActivity.this.finish();
                    AgingWindowActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_DS_MENU_ID")) {
                    ArrayList<MenuData> menuDataList = (ArrayList) intent.getExtras().getSerializable("SPT_DS_MENU_ID");
                    menu = new Intent(AgingWindowActivity.this, CarDiagnoseActivityTwo.class);
                    menu.putExtra("menuData", menuDataList);
                    menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                    AgingWindowActivity.this.startActivity(menu);
                    AgingWindowActivity.this.overridePendingTransition(0, 0);
                    AgingWindowActivity.this.finish();
                    AgingWindowActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_ACTIVE_TEST")) {
                    SptActiveTest sptActiveTest = (SptActiveTest) intent.getExtras().getSerializable("ACTIVE_TEST");
                    Intent active = new Intent(AgingWindowActivity.this, ActiveTestActivity.class);
                    active.putExtra("ACTIVE_TEST", sptActiveTest);
                    active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    AgingWindowActivity.this.startActivity(active);
                    SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                    AgingWindowActivity.this.overridePendingTransition(0, 0);
                    AgingWindowActivity.this.finish();
                    AgingWindowActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_NOBUTTONBOX_TEXT")) {
                    if (AgingWindowActivity.this.progressDialog == null) {
                        AgingWindowActivity.this.progressDialog = new ProgressDialog(AgingWindowActivity.this.contexts);
                    }
                    Spt_Nobuttonbox_Text nobuttonbox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("Nobuttonbox");
                    SimpleDialog.openProgressDialog(AgingWindowActivity.this.contexts, nobuttonbox.getTitle(), nobuttonbox.getContent(), AgingWindowActivity.this.progressDialog);
                } else if (intent.getAction().equals("closeNobuttonBox")) {
                    SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE")) {
                    ArrayList<SptTroubleTest> troubleTestList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE");
                    r0 = new Intent(AgingWindowActivity.this, FaultCodeActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE", troubleTestList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                    AgingWindowActivity.this.startActivity(r0);
                    AgingWindowActivity.this.overridePendingTransition(0, 0);
                    AgingWindowActivity.this.finish();
                    AgingWindowActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EXIT_SHOW_WINDOW")) {
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                    AgingWindowActivity.this.finish();
                    AgingWindowActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_STREAM_SELECT_ID_EX")) {
                    ArrayList<SptStreamSelectIdItem> streamSelectIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_STREAM_SELECT_ID_EX");
                    r0 = new Intent(AgingWindowActivity.this, StreamSelectActivity.class);
                    r0.putExtra("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                    AgingWindowActivity.this.startActivity(r0);
                    AgingWindowActivity.this.overridePendingTransition(0, 0);
                    AgingWindowActivity.this.finish();
                    AgingWindowActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                    ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                    r0 = new Intent(AgingWindowActivity.this, DataStreamActivity.class);
                    r0.putExtra("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                    AgingWindowActivity.this.startActivity(r0);
                    AgingWindowActivity.this.overridePendingTransition(0, 0);
                    AgingWindowActivity.this.finish();
                    AgingWindowActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_MESSAGEBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                    SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) intent.getExtras().getSerializable("SPT_MESSAGEBOX_TEXT");
                    int dialogType = sptMessageBoxText.getDialogType();
                    SimpleDialogControl.showDiaglog(AgingWindowActivity.this.contexts, dialogType, sptMessageBoxText);
                } else if (intent.getAction().equals("ConnectionLost")) {
                    Toast.makeText(AgingWindowActivity.this.getApplicationContext(), AgingWindowActivity.this.getResources().getString(C0136R.string.devlost), 0).show();
                } else if (intent.getAction().equals("SPT_INPUTSTRING_EX")) {
                    SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                    SptInputStringEx inputString = (SptInputStringEx) intent.getExtras().getSerializable("SPT_INPUTSTRING_EX");
                    SimpleDialog.sptInputStringExDiagnose(AgingWindowActivity.this.contexts, inputString.getDialogTitle(), inputString.getDialogContent(), inputString.getInputHint());
                } else if (intent.getAction().equals("SPT_INPUT_NUMERIC")) {
                    SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                    SptInputNumric inputNumric = (SptInputNumric) intent.getExtras().getSerializable("SPT_INPUT_NUMERIC");
                    SimpleDialog.sptInputNumericDiagnose(AgingWindowActivity.this.contexts, inputNumric.getDialogTitle(), inputNumric.getDialogContent(), inputNumric.getInputHint(), inputNumric.getDigit());
                } else if (intent.getAction().equals("SPT_INPUTBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                    Spt_Nobuttonbox_Text inputBox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTBOX_TEXT");
                    SimpleDialog.sptInputBoxTextDiagnose(AgingWindowActivity.this.contexts, inputBox.getTitle(), inputBox.getContent());
                } else if (intent.getAction().equals("SPT_INPUTSTRING")) {
                    SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                    Spt_Nobuttonbox_Text inputStr = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTSTRING");
                    SimpleDialog.sptInputStringDiagnose(AgingWindowActivity.this.contexts, inputStr.getTitle(), inputStr.getContent());
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN")) {
                    ArrayList<SptTroubleTest> troubleCodeFrozenList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN");
                    r0 = new Intent(AgingWindowActivity.this, FaultCodeFrozenActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                    AgingWindowActivity.this.startActivity(r0);
                    AgingWindowActivity.this.overridePendingTransition(0, 0);
                    AgingWindowActivity.this.finish();
                    AgingWindowActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                    ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                    r0 = new Intent(AgingWindowActivity.this, VWDataStreamActivity.class);
                    r0.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                    AgingWindowActivity.this.startActivity(r0);
                    AgingWindowActivity.this.overridePendingTransition(0, 0);
                    AgingWindowActivity.this.finish();
                    AgingWindowActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_SHOW_PICTURE")) {
                    SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                    String pictureName = intent.getExtras().getString("SPT_SHOW_PICTURE");
                    SimpleDialog.sptShowPictureDiagnose(AgingWindowActivity.this.contexts, pictureName);
                } else if (intent.getAction().equals("SPT_AGING_WINDOW")) {
                    SimpleDialog.closeProgressDialog(AgingWindowActivity.this.progressDialog);
                    AgingWindowActivity.this.agingContent = intent.getExtras().getString("SPT_AGING_WINDOW");
                    new C03551().start();
                }
            }
        }
    }

    public AgingWindowActivity() {
        this.isExecuteC = Boolean.valueOf(false);
        this.timer = new Timer();
        this.agingContent = XmlPullParser.NO_NAMESPACE;
        this.flag = Boolean.valueOf(false);
        this.flag2 = Boolean.valueOf(false);
        this.MSG_PRINT_RESULT = 0;
        this.MSG_PRINT_START = InBandBytestreamManager.MAXIMUM_BLOCK_SIZE;
        this.count = 0;
        this.baseHandler = new C03531();
        this.mHandler = new C03542();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        this.contexts = this;
        setContentView(C0136R.layout.aging_window);
        if (MySharedPreferences.share == null) {
            MySharedPreferences.getSharedPref(getApplicationContext());
        }
        MyApplication.getInstance().addActivity(this);
        initView();
        registerBoradcastReceiver();
        this.timer = new Timer();
        this.timer.schedule(new MyTimerTask(), 500, 500);
        BluetoothChatService.setHandler(this.mHandler);
    }

    private void initView() {
        this.textView = (TextView) findViewById(C0136R.id.textView);
        this.bundle = getIntent().getExtras();
        if (this.bundle != null) {
            this.agingContent = this.bundle.getString("SPT_AGING_WINDOW");
            this.textView.setText(this.agingContent);
        }
        this.flag = Boolean.valueOf(true);
    }

    protected void onResume() {
        super.onResume();
        this.isExecuteC = Boolean.valueOf(true);
    }

    protected void onStop() {
        super.onStop();
        this.isExecuteC = Boolean.valueOf(false);
    }

    protected void onDestroy() {
        SimpleDialog.closeProgressDialog(this.progressDialog);
        super.onDestroy();
        if (this.receiver != null) {
            unregisterReceiver(this.receiver);
        }
    }

    public void registerBoradcastReceiver() {
        this.receiver = new mBroadcastReceiver();
        this.myIntentFilter = new IntentFilter();
        this.myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.myIntentFilter.addAction("android.intent.action.SCREEN_ON");
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
        this.myIntentFilter.addAction("SPT_AGING_WINDOW");
        registerReceiver(this.receiver, this.myIntentFilter);
        this.flag2 = Boolean.valueOf(true);
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
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this.contexts);
            this.progressDialog.setCancelable(false);
        } else {
            this.progressDialog.dismiss();
            this.progressDialog = null;
            this.progressDialog = new ProgressDialog(this.contexts);
        }
        SimpleDialog.openProgressDialog(this.contexts, getResources().getString(C0136R.string.dataDisposeTilte), getResources().getString(C0136R.string.dataDisposeMessage), this.progressDialog);
        Constant.continueObtain = null;
        Constant.continueObtain = Constant.previousMenu;
        CToJava.agingFlag = Boolean.valueOf(true);
    }
}

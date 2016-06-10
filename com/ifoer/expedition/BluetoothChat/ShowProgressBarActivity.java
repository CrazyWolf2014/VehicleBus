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
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.Constant;
import com.ifoer.entity.SptActiveTest;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptInputNumric;
import com.ifoer.entity.SptInputStringEx;
import com.ifoer.entity.SptMessageBoxText;
import com.ifoer.entity.SptStreamSelectIdItem;
import com.ifoer.entity.SptTroubleTest;
import com.ifoer.entity.Spt_Nobuttonbox_Text;
import com.ifoer.entity.Spt_Progressbar_Box;
import com.ifoer.expedition.cto.CToJava;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.NetPOSPrinter;
import com.ifoer.util.SimpleDialog;
import com.ifoer.util.SimpleDialogControl;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;

public class ShowProgressBarActivity extends Activity {
    private static final boolean f1282D = true;
    private static boolean isShow;
    private final int MSG_PRINT_RESULT;
    private final int MSG_PRINT_START;
    Handler baseHandler;
    Bitmap bit_first;
    Bitmap bit_second;
    Runnable bmp;
    private Bundle bundle;
    private TextView content;
    private Context contexts;
    private int count;
    private Boolean isExecuteS;
    private long lastTime;
    private IntentFilter myIntentFilter;
    private ProgressBar pb;
    Bitmap printBitmap;
    int printResult;
    private ProgressDialog progressDialog;
    private mBroadcastReceiver receiver;
    private StringBuffer sb;
    private Spt_Progressbar_Box spb;
    private Timer timer;
    private TextView title;

    /* renamed from: com.ifoer.expedition.BluetoothChat.ShowProgressBarActivity.1 */
    class C04611 implements Runnable {
        C04611() {
        }

        public void run() {
            NetPOSPrinter bmpPrinter = new NetPOSPrinter(ShowProgressBarActivity.this);
            ShowProgressBarActivity.this.bit_first = bmpPrinter.drawBitFirst();
            ShowProgressBarActivity.this.bit_second = bmpPrinter.drawBitSecond(ShowProgressBarActivity.this.sb.toString());
            ShowProgressBarActivity.this.printBitmap = NetPOSPrinter.mixtureBitmap(ShowProgressBarActivity.this.bit_first, ShowProgressBarActivity.this.bit_second);
            ShowProgressBarActivity.this.printResult = bmpPrinter.printPic(ShowProgressBarActivity.this.printBitmap);
            bmpPrinter.resultToast(ShowProgressBarActivity.this.printResult);
            ShowProgressBarActivity.this.baseHandler.obtainMessage(0, Integer.valueOf(ShowProgressBarActivity.this.printResult)).sendToTarget();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.ShowProgressBarActivity.2 */
    class C04622 extends Handler {
        C04622() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case InBandBytestreamManager.MAXIMUM_BLOCK_SIZE /*65535*/:
                    ShowProgressBarActivity.this.dayin();
                case 10101010:
                    if (ShowProgressBarActivity.this.progressDialog != null && ShowProgressBarActivity.this.progressDialog.isShowing()) {
                        SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                    }
                    SimpleDialog.checkConectior(ShowProgressBarActivity.this.contexts, ShowProgressBarActivity.this.getString(C0136R.string.initializeTilte), ShowProgressBarActivity.this.getString(C0136R.string.check_connector));
                default:
            }
        }
    }

    class MyTimerTask extends TimerTask {
        MyTimerTask() {
        }

        public void run() {
            CToJava.progressbarFlag = Boolean.valueOf(ShowProgressBarActivity.f1282D);
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (ShowProgressBarActivity.this.isExecuteS.booleanValue()) {
                if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                    if ((System.currentTimeMillis() - ShowProgressBarActivity.this.lastTime) / 1000 > 30) {
                        SimpleDialog.ExitDialog(context);
                    }
                }
                if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                    ShowProgressBarActivity.this.lastTime = System.currentTimeMillis();
                }
                if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    ShowProgressBarActivity.this.finish();
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("feedbackMeauData")) {
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("menuData");
                    menu = new Intent(ShowProgressBarActivity.this, CarDiagnoseActivity.class);
                    menu.putExtra("menuData", menuDataList);
                    menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    ShowProgressBarActivity.this.startActivity(menu);
                    SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                    ShowProgressBarActivity.this.finish();
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_DS_MENU_ID")) {
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("SPT_DS_MENU_ID");
                    menu = new Intent(ShowProgressBarActivity.this, CarDiagnoseActivity.class);
                    menu.putExtra("menuData", menuDataList);
                    menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                    ShowProgressBarActivity.this.startActivity(menu);
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                    ShowProgressBarActivity.this.finish();
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_ACTIVE_TEST")) {
                    SptActiveTest sptActiveTest = (SptActiveTest) intent.getExtras().getSerializable("ACTIVE_TEST");
                    Intent active = new Intent(ShowProgressBarActivity.this, ActiveTestActivity.class);
                    active.putExtra("ACTIVE_TEST", sptActiveTest);
                    active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    ShowProgressBarActivity.this.startActivity(active);
                    SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                    ShowProgressBarActivity.this.finish();
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_NOBUTTONBOX_TEXT")) {
                    if (ShowProgressBarActivity.this.progressDialog == null) {
                        ShowProgressBarActivity.this.progressDialog = new ProgressDialog(ShowProgressBarActivity.this.contexts);
                        ShowProgressBarActivity.this.progressDialog.setCancelable(false);
                    } else {
                        ShowProgressBarActivity.this.progressDialog.dismiss();
                        ShowProgressBarActivity.this.progressDialog = null;
                        ShowProgressBarActivity.this.progressDialog = new ProgressDialog(ShowProgressBarActivity.this.contexts);
                        ShowProgressBarActivity.this.progressDialog.setCancelable(false);
                    }
                    Spt_Nobuttonbox_Text nobuttonbox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("Nobuttonbox");
                    SimpleDialog.openProgressDialog(ShowProgressBarActivity.this.contexts, nobuttonbox.getTitle(), nobuttonbox.getContent(), ShowProgressBarActivity.this.progressDialog);
                } else if (intent.getAction().equals("closeNobuttonBox")) {
                    SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE")) {
                    ArrayList<SptTroubleTest> troubleTestList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE");
                    r0 = new Intent(ShowProgressBarActivity.this, FaultCodeActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE", troubleTestList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                    ShowProgressBarActivity.this.startActivity(r0);
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                    ShowProgressBarActivity.this.finish();
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EXIT_SHOW_WINDOW")) {
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                    ShowProgressBarActivity.this.finish();
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                } else if (intent.getAction().equals("SPT_STREAM_SELECT_ID_EX")) {
                    ArrayList<SptStreamSelectIdItem> streamSelectIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_STREAM_SELECT_ID_EX");
                    r0 = new Intent(ShowProgressBarActivity.this, StreamSelectActivityTwo.class);
                    r0.putExtra("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    ShowProgressBarActivity.this.startActivity(r0);
                    SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                    ShowProgressBarActivity.this.finish();
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                    ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                    r0 = new Intent(ShowProgressBarActivity.this, DataStreamActivity.class);
                    r0.putExtra("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    ShowProgressBarActivity.this.startActivity(r0);
                    SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                    ShowProgressBarActivity.this.finish();
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_MESSAGEBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                    SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) intent.getExtras().getSerializable("SPT_MESSAGEBOX_TEXT");
                    int dialogType = sptMessageBoxText.getDialogType();
                    SimpleDialogControl.showDiaglog(ShowProgressBarActivity.this.contexts, dialogType, sptMessageBoxText);
                } else if (intent.getAction().equals("ConnectionLost")) {
                    Toast.makeText(ShowProgressBarActivity.this.getApplicationContext(), "Device connection was lost", 0).show();
                } else if (intent.getAction().equals("SPT_INPUTSTRING_EX")) {
                    SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                    SptInputStringEx inputString = (SptInputStringEx) intent.getExtras().getSerializable("SPT_INPUTSTRING_EX");
                    SimpleDialog.sptInputStringExDiagnose(ShowProgressBarActivity.this.contexts, inputString.getDialogTitle(), inputString.getDialogContent(), inputString.getInputHint());
                } else if (intent.getAction().equals("SPT_INPUT_NUMERIC")) {
                    SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                    SptInputNumric inputNumric = (SptInputNumric) intent.getExtras().getSerializable("SPT_INPUT_NUMERIC");
                    SimpleDialog.sptInputNumericDiagnose(ShowProgressBarActivity.this.contexts, inputNumric.getDialogTitle(), inputNumric.getDialogContent(), inputNumric.getInputHint(), inputNumric.getDigit());
                } else if (intent.getAction().equals("SPT_INPUTBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                    Spt_Nobuttonbox_Text inputBox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTBOX_TEXT");
                    SimpleDialog.sptInputBoxTextDiagnose(ShowProgressBarActivity.this.contexts, inputBox.getTitle(), inputBox.getContent());
                } else if (intent.getAction().equals("SPT_INPUTSTRING")) {
                    SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                    Spt_Nobuttonbox_Text inputStr = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTSTRING");
                    SimpleDialog.sptInputStringDiagnose(ShowProgressBarActivity.this.contexts, inputStr.getTitle(), inputStr.getContent());
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN")) {
                    ArrayList<SptTroubleTest> troubleCodeFrozenList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN");
                    r0 = new Intent(ShowProgressBarActivity.this, FaultCodeFrozenActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                    ShowProgressBarActivity.this.startActivity(r0);
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                    ShowProgressBarActivity.this.finish();
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                    ArrayList<SptExDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                    r0 = new Intent(ShowProgressBarActivity.this, VWDataStreamActivity.class);
                    r0.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                    ShowProgressBarActivity.this.startActivity(r0);
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                    ShowProgressBarActivity.this.finish();
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_SHOW_PICTURE")) {
                    SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                    String pictureName = intent.getExtras().getString("SPT_SHOW_PICTURE");
                    SimpleDialog.sptShowPictureDiagnose(ShowProgressBarActivity.this.contexts, pictureName);
                } else if (intent.getAction().equals("SPT_AGING_WINDOW")) {
                    String agingContent = intent.getExtras().getString("SPT_AGING_WINDOW");
                    Intent agingIntent = new Intent(ShowProgressBarActivity.this, AgingWindowActivity.class);
                    agingIntent.putExtra("SPT_AGING_WINDOW", agingContent);
                    agingIntent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(ShowProgressBarActivity.this.progressDialog);
                    ShowProgressBarActivity.this.startActivity(agingIntent);
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                    ShowProgressBarActivity.this.finish();
                    ShowProgressBarActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_PROGRESSBAR_BOX")) {
                    ShowProgressBarActivity.this.bundle = intent.getExtras();
                    if (ShowProgressBarActivity.this.bundle != null && ShowProgressBarActivity.isShow) {
                        ShowProgressBarActivity.this.spb = (Spt_Progressbar_Box) ShowProgressBarActivity.this.bundle.getSerializable("SPT_PROGRESSBAR_BOX");
                        StringBuilder sb = new StringBuilder();
                        sb.append(ShowProgressBarActivity.this.spb.getContent());
                        sb.append("%");
                        ShowProgressBarActivity.this.title.setText(ShowProgressBarActivity.this.spb.getTitle());
                        ShowProgressBarActivity.this.content.setText(sb.toString());
                        ShowProgressBarActivity.this.pb.setMax(100);
                        ShowProgressBarActivity.this.pb.setProgress(ShowProgressBarActivity.this.spb.getProgressbarLen());
                    }
                }
            }
        }
    }

    public ShowProgressBarActivity() {
        this.isExecuteS = Boolean.valueOf(false);
        this.timer = new Timer();
        this.MSG_PRINT_RESULT = 0;
        this.MSG_PRINT_START = InBandBytestreamManager.MAXIMUM_BLOCK_SIZE;
        this.sb = null;
        this.count = 0;
        this.bmp = new C04611();
        this.baseHandler = new C04622();
    }

    static {
        isShow = false;
    }

    private void getPrintDate() {
        this.sb = new StringBuffer();
        this.sb.append(new StringBuilder(String.valueOf(this.spb.getTitle().toString())).append(SpecilApiUtil.LINE_SEP).toString());
        this.sb.append(new StringBuilder(String.valueOf(this.sb.toString().toString())).append(SpecilApiUtil.LINE_SEP).toString());
        this.sb.append(new StringBuilder(String.valueOf(this.spb.getProgressbarLen())).append(SpecilApiUtil.LINE_SEP).toString());
    }

    public void dayin() {
        getPrintDate();
        new Thread(this.bmp).start();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        this.contexts = this;
        setContentView(C0136R.layout.spt_progressbar);
        if (MySharedPreferences.share == null) {
            MySharedPreferences.getSharedPref(getApplicationContext());
        }
        MyApplication.getInstance().addActivity(this);
        initView();
        BluetoothChatService.setHandler(this.baseHandler);
        registerBoradcastReceiver();
        this.timer = new Timer();
        this.timer.schedule(new MyTimerTask(), 500, 500);
    }

    private void initView() {
        isShow = f1282D;
        this.title = (TextView) findViewById(C0136R.id.pb_title);
        this.content = (TextView) findViewById(C0136R.id.pb_context);
        this.pb = (ProgressBar) findViewById(C0136R.id.progressBar);
        this.bundle = getIntent().getExtras();
        if (this.bundle != null) {
            this.spb = (Spt_Progressbar_Box) this.bundle.getSerializable("SPT_PROGRESSBAR_BOX");
            StringBuilder sb = new StringBuilder();
            sb.append(this.spb.getContent());
            sb.append("%");
            this.title.setText(this.spb.getTitle());
            this.content.setText(sb.toString());
            this.pb.setMax(100);
            this.pb.setProgress(this.spb.getProgressbarLen());
        }
    }

    protected void onResume() {
        super.onResume();
        this.isExecuteS = Boolean.valueOf(f1282D);
    }

    protected void onStop() {
        super.onStop();
        this.isExecuteS = Boolean.valueOf(false);
    }

    protected void onDestroy() {
        SimpleDialog.closeProgressDialog(this.progressDialog);
        isShow = false;
        super.onDestroy();
        if (this.receiver != null) {
            unregisterReceiver(this.receiver);
        }
    }

    public synchronized void registerBoradcastReceiver() {
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
        this.myIntentFilter.addAction("SPT_PROGRESSBAR_BOX");
        registerReceiver(this.receiver, this.myIntentFilter);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 || keyCode == Service.SUNRPC) {
            back();
        }
        return f1282D;
    }

    private void back() {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this.contexts);
            this.progressDialog.setCancelable(false);
        } else {
            this.progressDialog.dismiss();
            this.progressDialog = null;
            this.progressDialog = new ProgressDialog(this.contexts);
            this.progressDialog.setCancelable(false);
        }
        SimpleDialog.openProgressDialog(this.contexts, getResources().getString(C0136R.string.dataDisposeTilte), getResources().getString(C0136R.string.dataDisposeMessage), this.progressDialog);
        Constant.progressbarBox = null;
        Constant.progressbarBox = Constant.previousMenu;
        CToJava.progressbarFlag = Boolean.valueOf(f1282D);
        isShow = false;
    }
}

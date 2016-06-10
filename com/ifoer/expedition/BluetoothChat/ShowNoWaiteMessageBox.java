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
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
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
import com.ifoer.entity.Spt_NoWaitMessageBox_Text;
import com.ifoer.entity.Spt_Nobuttonbox_Text;
import com.ifoer.expedition.cto.CToJava;
import com.ifoer.util.MyApplication;
import com.ifoer.util.NetPOSPrinter;
import com.ifoer.util.SimpleDialog;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;

public class ShowNoWaiteMessageBox extends Activity {
    private static final boolean f1281D = true;
    public static IntentFilter myIntentFilter;
    private static ProgressDialog progressDialog;
    private final int MSG_PRINT_RESULT;
    private final int MSG_PRINT_START;
    Handler baseHandler;
    Bitmap bit_first;
    Bitmap bit_second;
    Runnable bmp;
    private Bundle bundle;
    private Context contexts;
    private int count;
    private Boolean isExecuteF;
    private Boolean isexit;
    Spt_NoWaitMessageBox_Text nowaitmessagebox;
    Bitmap printBitmap;
    int printResult;
    mBroadcastReceiver receiver;
    private StringBuffer sb;
    private TextView textview;
    private Timer timer;
    private TextView titleview;

    /* renamed from: com.ifoer.expedition.BluetoothChat.ShowNoWaiteMessageBox.1 */
    class C04591 implements Runnable {
        C04591() {
        }

        public void run() {
            NetPOSPrinter bmpPrinter = new NetPOSPrinter(ShowNoWaiteMessageBox.this);
            ShowNoWaiteMessageBox.this.bit_first = bmpPrinter.drawBitFirst();
            ShowNoWaiteMessageBox.this.bit_second = bmpPrinter.drawBitSecond(ShowNoWaiteMessageBox.this.sb.toString());
            ShowNoWaiteMessageBox.this.printBitmap = NetPOSPrinter.mixtureBitmap(ShowNoWaiteMessageBox.this.bit_first, ShowNoWaiteMessageBox.this.bit_second);
            ShowNoWaiteMessageBox.this.printResult = bmpPrinter.printPic(ShowNoWaiteMessageBox.this.printBitmap);
            bmpPrinter.resultToast(ShowNoWaiteMessageBox.this.printResult);
            ShowNoWaiteMessageBox.this.baseHandler.obtainMessage(0, Integer.valueOf(ShowNoWaiteMessageBox.this.printResult)).sendToTarget();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.ShowNoWaiteMessageBox.2 */
    class C04602 extends Handler {
        C04602() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case InBandBytestreamManager.MAXIMUM_BLOCK_SIZE /*65535*/:
                    ShowNoWaiteMessageBox.this.dayin();
                case 10101010:
                    if (ShowNoWaiteMessageBox.progressDialog != null && ShowNoWaiteMessageBox.progressDialog.isShowing()) {
                        SimpleDialog.closeProgressDialog(ShowNoWaiteMessageBox.progressDialog);
                    }
                    SimpleDialog.checkConectior(ShowNoWaiteMessageBox.this.contexts, ShowNoWaiteMessageBox.this.getString(C0136R.string.initializeTilte), ShowNoWaiteMessageBox.this.getString(C0136R.string.check_connector));
                default:
            }
        }
    }

    class MyTimerTask extends TimerTask {
        MyTimerTask() {
        }

        public void run() {
            CToJava.nowaitmessageboxtext = Boolean.valueOf(ShowNoWaiteMessageBox.f1281D);
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (!ShowNoWaiteMessageBox.this.isExecuteF.booleanValue()) {
                return;
            }
            if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                SimpleDialog.closeProgressDialog(ShowNoWaiteMessageBox.progressDialog);
                ShowNoWaiteMessageBox.this.finish();
                ShowNoWaiteMessageBox.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("feedbackMeauData")) {
                ArrayList<MenuData> menuDataLists = (ArrayList) intent.getExtras().getSerializable("menuData");
                menu = new Intent(ShowNoWaiteMessageBox.this, CarDiagnoseActivityTwo.class);
                menu.putExtra("menuData", menuDataLists);
                menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(ShowNoWaiteMessageBox.progressDialog);
                ShowNoWaiteMessageBox.this.startActivity(menu);
                ShowNoWaiteMessageBox.this.overridePendingTransition(0, 0);
                ShowNoWaiteMessageBox.this.finish();
                ShowNoWaiteMessageBox.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_DS_MENU_ID")) {
                ArrayList<MenuData> menuDataList = (ArrayList) intent.getExtras().getSerializable("SPT_DS_MENU_ID");
                menu = new Intent(ShowNoWaiteMessageBox.this, CarDiagnoseActivityTwo.class);
                menu.putExtra("menuData", menuDataList);
                menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(ShowNoWaiteMessageBox.progressDialog);
                ShowNoWaiteMessageBox.this.startActivity(menu);
                ShowNoWaiteMessageBox.this.overridePendingTransition(0, 0);
                ShowNoWaiteMessageBox.this.finish();
                ShowNoWaiteMessageBox.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_ACTIVE_TEST")) {
                SptActiveTest sptActiveTest = (SptActiveTest) intent.getExtras().getSerializable("ACTIVE_TEST");
                Intent active = new Intent(ShowNoWaiteMessageBox.this, ActiveTestActivity.class);
                active.putExtra("ACTIVE_TEST", sptActiveTest);
                active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                ShowNoWaiteMessageBox.this.startActivity(active);
                SimpleDialog.closeProgressDialog(ShowNoWaiteMessageBox.progressDialog);
                ShowNoWaiteMessageBox.this.overridePendingTransition(0, 0);
                ShowNoWaiteMessageBox.this.finish();
                ShowNoWaiteMessageBox.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_NOBUTTONBOX_TEXT")) {
                if (ShowNoWaiteMessageBox.progressDialog == null) {
                    ShowNoWaiteMessageBox.progressDialog = new ProgressDialog(ShowNoWaiteMessageBox.this.contexts);
                }
                Spt_Nobuttonbox_Text nobuttonbox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("Nobuttonbox");
                SimpleDialog.openProgressDialog(ShowNoWaiteMessageBox.this.contexts, nobuttonbox.getTitle(), nobuttonbox.getContent(), ShowNoWaiteMessageBox.progressDialog);
            } else if (intent.getAction().equals("closeNobuttonBox")) {
                SimpleDialog.closeProgressDialog(ShowNoWaiteMessageBox.progressDialog);
            } else if (intent.getAction().equals("SPT_TROUBLE_CODE")) {
                ArrayList<SptTroubleTest> troubleTestList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE");
                r0 = new Intent(ShowNoWaiteMessageBox.this, FaultCodeActivity.class);
                r0.putExtra("SPT_TROUBLE_CODE", troubleTestList);
                r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(ShowNoWaiteMessageBox.progressDialog);
                ShowNoWaiteMessageBox.this.startActivity(r0);
                ShowNoWaiteMessageBox.this.overridePendingTransition(0, 0);
                ShowNoWaiteMessageBox.this.finish();
                ShowNoWaiteMessageBox.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_EXIT_SHOW_WINDOW")) {
                if (Constant.mChatService != null) {
                    Constant.mChatService.stop();
                }
                SimpleDialog.closeProgressDialog(ShowNoWaiteMessageBox.progressDialog);
                Process.killProcess(Process.myPid());
                System.exit(0);
                ShowNoWaiteMessageBox.this.finish();
                ShowNoWaiteMessageBox.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_STREAM_SELECT_ID_EX")) {
                ArrayList<SptStreamSelectIdItem> streamSelectIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_STREAM_SELECT_ID_EX");
                r0 = new Intent(ShowNoWaiteMessageBox.this, StreamSelectActivity.class);
                r0.putExtra("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(ShowNoWaiteMessageBox.progressDialog);
                ShowNoWaiteMessageBox.this.startActivity(r0);
                ShowNoWaiteMessageBox.this.overridePendingTransition(0, 0);
                ShowNoWaiteMessageBox.this.finish();
                ShowNoWaiteMessageBox.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                r0 = new Intent(ShowNoWaiteMessageBox.this, DataStreamActivity.class);
                r0.putExtra("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(ShowNoWaiteMessageBox.progressDialog);
                ShowNoWaiteMessageBox.this.startActivity(r0);
                ShowNoWaiteMessageBox.this.overridePendingTransition(0, 0);
                ShowNoWaiteMessageBox.this.finish();
                ShowNoWaiteMessageBox.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_MESSAGEBOX_TEXT")) {
                SimpleDialog.closeProgressDialog(ShowNoWaiteMessageBox.progressDialog);
                SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) intent.getExtras().getSerializable("SPT_MESSAGEBOX_TEXT");
                switch (sptMessageBoxText.getDialogType()) {
                    case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                        SimpleDialog.okDialog(ShowNoWaiteMessageBox.this.contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
                    case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                        SimpleDialog.okCancelDialog(ShowNoWaiteMessageBox.this.contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
                    case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                        SimpleDialog.yesNoDialog(ShowNoWaiteMessageBox.this.contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
                    case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                        SimpleDialog.retryCancelDialog(ShowNoWaiteMessageBox.this.contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
                    case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                        SimpleDialog.noButtonDialog(ShowNoWaiteMessageBox.this.contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
                    case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                        SimpleDialog.okPrintDialog(ShowNoWaiteMessageBox.this.contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
                    default:
                }
            } else if (intent.getAction().equals("ConnectionLost")) {
                Toast.makeText(ShowNoWaiteMessageBox.this.getApplicationContext(), ShowNoWaiteMessageBox.this.getResources().getString(C0136R.string.devlost), 0).show();
            } else if (intent.getAction().equals("SPT_INPUTSTRING_EX")) {
                SimpleDialog.closeProgressDialog(ShowNoWaiteMessageBox.progressDialog);
                SptInputStringEx inputString = (SptInputStringEx) intent.getExtras().getSerializable("SPT_INPUTSTRING_EX");
                SimpleDialog.sptInputStringExDiagnose(ShowNoWaiteMessageBox.this.contexts, inputString.getDialogTitle(), inputString.getDialogContent(), inputString.getInputHint());
            } else if (intent.getAction().equals("SPT_INPUT_NUMERIC")) {
                SimpleDialog.closeProgressDialog(ShowNoWaiteMessageBox.progressDialog);
                SptInputNumric inputNumric = (SptInputNumric) intent.getExtras().getSerializable("SPT_INPUT_NUMERIC");
                SimpleDialog.sptInputNumericDiagnose(ShowNoWaiteMessageBox.this.contexts, inputNumric.getDialogTitle(), inputNumric.getDialogContent(), inputNumric.getInputHint(), inputNumric.getDigit());
            } else if (intent.getAction().equals("SPT_INPUTBOX_TEXT")) {
                SimpleDialog.closeProgressDialog(ShowNoWaiteMessageBox.progressDialog);
                Spt_Nobuttonbox_Text inputBox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTBOX_TEXT");
                SimpleDialog.sptInputBoxTextDiagnose(ShowNoWaiteMessageBox.this.contexts, inputBox.getTitle(), inputBox.getContent());
            } else if (intent.getAction().equals("SPT_INPUTSTRING")) {
                SimpleDialog.closeProgressDialog(ShowNoWaiteMessageBox.progressDialog);
                Spt_Nobuttonbox_Text inputStr = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTSTRING");
                SimpleDialog.sptInputStringDiagnose(ShowNoWaiteMessageBox.this.contexts, inputStr.getTitle(), inputStr.getContent());
            } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN")) {
                ArrayList<SptTroubleTest> troubleCodeFrozenList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN");
                r0 = new Intent(ShowNoWaiteMessageBox.this, FaultCodeFrozenActivity.class);
                r0.putExtra("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(ShowNoWaiteMessageBox.progressDialog);
                ShowNoWaiteMessageBox.this.startActivity(r0);
                ShowNoWaiteMessageBox.this.overridePendingTransition(0, 0);
                ShowNoWaiteMessageBox.this.finish();
                ShowNoWaiteMessageBox.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                r0 = new Intent(ShowNoWaiteMessageBox.this, VWDataStreamActivity.class);
                r0.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(ShowNoWaiteMessageBox.progressDialog);
                ShowNoWaiteMessageBox.this.startActivity(r0);
                ShowNoWaiteMessageBox.this.overridePendingTransition(0, 0);
                ShowNoWaiteMessageBox.this.finish();
                ShowNoWaiteMessageBox.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_SHOW_PICTURE")) {
                SimpleDialog.closeProgressDialog(ShowNoWaiteMessageBox.progressDialog);
                String pictureName = intent.getExtras().getString("SPT_SHOW_PICTURE");
                SimpleDialog.sptShowPictureDiagnose(ShowNoWaiteMessageBox.this.contexts, pictureName);
            } else if (intent.getAction().equals("SPT_NOWAITMESSAGEBOX_TEXT")) {
                ShowNoWaiteMessageBox.this.bundle = ShowNoWaiteMessageBox.this.getIntent().getExtras();
                if (ShowNoWaiteMessageBox.this.bundle != null) {
                    if (ShowNoWaiteMessageBox.this.isexit.booleanValue()) {
                        String[] nowaitmessagebox = intent.getExtras().getStringArray("SPT_NOWAITMESSAGEBOX_TEXT");
                        if (nowaitmessagebox != null) {
                            ShowNoWaiteMessageBox.this.titleview.setText(nowaitmessagebox[0]);
                            ShowNoWaiteMessageBox.this.textview.setText(nowaitmessagebox[1]);
                        }
                    }
                }
            }
        }
    }

    public ShowNoWaiteMessageBox() {
        this.isExecuteF = Boolean.valueOf(false);
        this.nowaitmessagebox = new Spt_NoWaitMessageBox_Text();
        this.timer = new Timer();
        this.isexit = Boolean.valueOf(false);
        this.MSG_PRINT_RESULT = 0;
        this.MSG_PRINT_START = InBandBytestreamManager.MAXIMUM_BLOCK_SIZE;
        this.sb = null;
        this.count = 0;
        this.bmp = new C04591();
        this.baseHandler = new C04602();
    }

    private void getPrintDate() {
        this.sb = new StringBuffer();
        this.sb.append(new StringBuilder(String.valueOf(this.titleview.getText().toString())).append(SpecilApiUtil.LINE_SEP).toString());
        this.sb.append(new StringBuilder(String.valueOf(this.textview.getText().toString())).append(SpecilApiUtil.LINE_SEP).toString());
    }

    public void dayin() {
        getPrintDate();
        new Thread(this.bmp).start();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.shownowaitmessagebox);
        this.contexts = this;
        MyApplication.getInstance().addActivity(this);
        initView();
        registerBoradcastReceiver();
        this.timer = new Timer();
        this.timer.schedule(new MyTimerTask(), 500, 500);
        BluetoothChatService.setHandler(this.baseHandler);
    }

    private void initView() {
        this.isexit = Boolean.valueOf(f1281D);
        this.titleview = (TextView) findViewById(C0136R.id.showtitleview);
        this.textview = (TextView) findViewById(C0136R.id.showmessageview);
        String[] nowaitmessagebox = getIntent().getExtras().getStringArray("SPT_NOWAITMESSAGEBOX_TEXT");
        this.titleview.setText(nowaitmessagebox[0]);
        this.textview.setText(nowaitmessagebox[1]);
    }

    public void registerBoradcastReceiver() {
        this.receiver = new mBroadcastReceiver();
        myIntentFilter = new IntentFilter();
        myIntentFilter = new IntentFilter();
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
        myIntentFilter.addAction("SPT_AGING_WINDOW");
        myIntentFilter.addAction("SPT_NOWAITMESSAGEBOX_TEXT");
        registerReceiver(this.receiver, myIntentFilter);
    }

    protected void onResume() {
        super.onResume();
        this.isExecuteF = Boolean.valueOf(f1281D);
    }

    protected void onStop() {
        super.onStop();
        this.isExecuteF = Boolean.valueOf(false);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.receiver != null) {
            unregisterReceiver(this.receiver);
        }
        finish();
        overridePendingTransition(0, 0);
        this.isexit = Boolean.valueOf(false);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 || keyCode == Service.SUNRPC) {
            back();
        }
        return f1281D;
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
        Constant.noWaitMessageButton = null;
        Constant.noWaitMessageButton = Constant.previousMenu;
        CToJava.nowaitmessageboxtext = Boolean.valueOf(f1281D);
        this.isexit = Boolean.valueOf(false);
    }
}

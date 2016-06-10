package com.ifoer.expedition.BluetoothChat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.adapter.CarCombinationAdapter;
import com.ifoer.entity.Constant;
import com.ifoer.entity.SptActiveTest;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptInputNumric;
import com.ifoer.entity.SptInputStringEx;
import com.ifoer.entity.SptMessageBoxText;
import com.ifoer.entity.SptStreamSelectIdItem;
import com.ifoer.entity.SptTroubleTest;
import com.ifoer.entity.Spt_Combination_Menu;
import com.ifoer.entity.Spt_Nobuttonbox_Text;
import com.ifoer.expedition.BluetoothOrder.ByteHexHelper;
import com.ifoer.expedition.cto.CToJava;
import com.ifoer.util.MyApplication;
import com.ifoer.util.NetPOSPrinter;
import com.ifoer.util.SimpleDialog;
import com.ifoer.util.SimpleDialogControl;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.util.ArrayList;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.KEYRecord.Flags;

public class CombinationTwoActivity extends Activity implements OnClickListener {
    private static final boolean f1237D = true;
    private final int MSG_PRINT_RESULT;
    private final int MSG_PRINT_START;
    private CarCombinationAdapter adapter;
    Handler baseHandler;
    Bitmap bit_first;
    Bitmap bit_second;
    Runnable bmp;
    private Context contexts;
    private int count;
    private ArrayList<String> dataList;
    private int firstNum;
    private boolean isExecuteS;
    private long lastTime;
    private ListView listView;
    private IntentFilter myIntentFilter;
    Bitmap printBitmap;
    int printResult;
    String printStringData;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialogs;
    private RadioButton radioBack;
    private RadioButton radioClear;
    private RadioGroup radioGroup;
    private RadioButton radioSure;
    private mBroadcastReceiver receiver;
    private StringBuffer sb;
    private TextView tvTitle;

    /* renamed from: com.ifoer.expedition.BluetoothChat.CombinationTwoActivity.1 */
    class C03711 implements Runnable {
        C03711() {
        }

        public void run() {
            NetPOSPrinter bmpPrinter = new NetPOSPrinter(CombinationTwoActivity.this);
            CombinationTwoActivity.this.bit_first = bmpPrinter.drawBitFirst();
            CombinationTwoActivity.this.bit_second = bmpPrinter.drawBitSecond(CombinationTwoActivity.this.sb.toString());
            CombinationTwoActivity.this.printBitmap = NetPOSPrinter.mixtureBitmap(CombinationTwoActivity.this.bit_first, CombinationTwoActivity.this.bit_second);
            CombinationTwoActivity.this.printResult = bmpPrinter.printPic(CombinationTwoActivity.this.printBitmap);
            bmpPrinter.resultToast(CombinationTwoActivity.this.printResult);
            CombinationTwoActivity.this.baseHandler.obtainMessage(0, Integer.valueOf(CombinationTwoActivity.this.printResult)).sendToTarget();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.CombinationTwoActivity.2 */
    class C03722 extends Handler {
        C03722() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case InBandBytestreamManager.MAXIMUM_BLOCK_SIZE /*65535*/:
                    CombinationTwoActivity.this.dayin();
                case 10101010:
                    if (CombinationTwoActivity.this.progressDialog != null && CombinationTwoActivity.this.progressDialog.isShowing()) {
                        SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                    }
                    SimpleDialog.checkConectior(CombinationTwoActivity.this.contexts, CombinationTwoActivity.this.getString(C0136R.string.initializeTilte), CombinationTwoActivity.this.getString(C0136R.string.check_connector));
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.CombinationTwoActivity.3 */
    class C03733 implements OnItemClickListener {
        C03733() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            CToJava.combinationFlag = Boolean.valueOf(CombinationTwoActivity.f1237D);
            Constant.feedback = null;
            byte[] CombinationMenu = new byte[2];
            CombinationMenu[1] = (byte) (ByteHexHelper.intToHexByte(arg2) + Flags.FLAG8);
            Constant.CombinationMenu = CombinationMenu;
        }
    }

    class InitData extends AsyncTask<Integer, Integer, String> {
        Context context;

        public InitData(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            CombinationTwoActivity.this.progressDialogs = new ProgressDialog(this.context);
            CombinationTwoActivity.this.progressDialogs.setMessage(CombinationTwoActivity.this.getResources().getString(C0136R.string.find_wait));
            CombinationTwoActivity.this.progressDialogs.setCancelable(false);
            CombinationTwoActivity.this.progressDialogs.show();
        }

        protected String doInBackground(Integer... params) {
            return null;
        }

        protected void onPostExecute(String result) {
            if (CombinationTwoActivity.this.adapter == null) {
                CombinationTwoActivity.this.adapter = new CarCombinationAdapter(CombinationTwoActivity.this.dataList, CombinationTwoActivity.this);
                CombinationTwoActivity.this.listView.setAdapter(CombinationTwoActivity.this.adapter);
                CombinationTwoActivity.this.listView.setSelection(CombinationTwoActivity.this.firstNum);
            } else {
                CombinationTwoActivity.this.adapter.notifyDataSetChanged();
            }
            if (CombinationTwoActivity.this.progressDialogs != null && CombinationTwoActivity.this.progressDialogs.isShowing()) {
                CombinationTwoActivity.this.progressDialogs.dismiss();
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (CombinationTwoActivity.this.isExecuteS) {
                if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                    if ((System.currentTimeMillis() - CombinationTwoActivity.this.lastTime) / 1000 > 30) {
                        SimpleDialog.ExitDialog(context);
                    }
                }
                if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                    CombinationTwoActivity.this.lastTime = System.currentTimeMillis();
                }
                if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    CombinationTwoActivity.this.finish();
                    CombinationTwoActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("feedbackMeauData")) {
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("menuData");
                    menu = new Intent(CombinationTwoActivity.this, CarDiagnoseActivity.class);
                    menu.putExtra("menuData", menuDataList);
                    menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    CombinationTwoActivity.this.startActivity(menu);
                    SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                    CombinationTwoActivity.this.overridePendingTransition(0, 0);
                    CombinationTwoActivity.this.finish();
                    CombinationTwoActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_DS_MENU_ID")) {
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("SPT_DS_MENU_ID");
                    menu = new Intent(CombinationTwoActivity.this, CarDiagnoseActivity.class);
                    menu.putExtra("menuData", menuDataList);
                    menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                    CombinationTwoActivity.this.startActivity(menu);
                    CombinationTwoActivity.this.overridePendingTransition(0, 0);
                    CombinationTwoActivity.this.finish();
                    CombinationTwoActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_ACTIVE_TEST")) {
                    SptActiveTest sptActiveTest = (SptActiveTest) intent.getExtras().getSerializable("ACTIVE_TEST");
                    Intent active = new Intent(CombinationTwoActivity.this, ActiveTestActivity.class);
                    active.putExtra("ACTIVE_TEST", sptActiveTest);
                    active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    CombinationTwoActivity.this.startActivity(active);
                    SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                    CombinationTwoActivity.this.overridePendingTransition(0, 0);
                    CombinationTwoActivity.this.finish();
                    CombinationTwoActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_NOBUTTONBOX_TEXT")) {
                    if (CombinationTwoActivity.this.progressDialog == null) {
                        CombinationTwoActivity.this.progressDialog = new ProgressDialog(CombinationTwoActivity.this.contexts);
                        CombinationTwoActivity.this.progressDialog.setCancelable(false);
                    } else {
                        CombinationTwoActivity.this.progressDialog.dismiss();
                        CombinationTwoActivity.this.progressDialog = null;
                        CombinationTwoActivity.this.progressDialog = new ProgressDialog(CombinationTwoActivity.this.contexts);
                        CombinationTwoActivity.this.progressDialog.setCancelable(false);
                    }
                    Spt_Nobuttonbox_Text nobuttonbox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("Nobuttonbox");
                    SimpleDialog.openProgressDialog(CombinationTwoActivity.this.contexts, nobuttonbox.getTitle(), nobuttonbox.getContent(), CombinationTwoActivity.this.progressDialog);
                } else if (intent.getAction().equals("closeNobuttonBox")) {
                    SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE")) {
                    ArrayList<SptTroubleTest> troubleTestList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE");
                    r0 = new Intent(CombinationTwoActivity.this, FaultCodeActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE", troubleTestList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                    CombinationTwoActivity.this.startActivity(r0);
                    CombinationTwoActivity.this.overridePendingTransition(0, 0);
                    CombinationTwoActivity.this.finish();
                    CombinationTwoActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EXIT_SHOW_WINDOW")) {
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                    CombinationTwoActivity.this.finish();
                    CombinationTwoActivity.this.overridePendingTransition(0, 0);
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                } else if (intent.getAction().equals("SPT_STREAM_SELECT_ID_EX")) {
                    ArrayList<SptStreamSelectIdItem> streamSelectIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_STREAM_SELECT_ID_EX");
                    r0 = new Intent(CombinationTwoActivity.this, StreamSelectActivityTwo.class);
                    r0.putExtra("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    CombinationTwoActivity.this.startActivity(r0);
                    SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                    CombinationTwoActivity.this.overridePendingTransition(0, 0);
                    CombinationTwoActivity.this.finish();
                    CombinationTwoActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                    ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                    r0 = new Intent(CombinationTwoActivity.this, DataStreamActivity.class);
                    r0.putExtra("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    CombinationTwoActivity.this.startActivity(r0);
                    SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                    CombinationTwoActivity.this.overridePendingTransition(0, 0);
                    CombinationTwoActivity.this.finish();
                    CombinationTwoActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_MESSAGEBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                    SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) intent.getExtras().getSerializable("SPT_MESSAGEBOX_TEXT");
                    int dialogType = sptMessageBoxText.getDialogType();
                    SimpleDialogControl.showDiaglog(CombinationTwoActivity.this.contexts, dialogType, sptMessageBoxText);
                } else if (intent.getAction().equals("ConnectionLost")) {
                    Toast.makeText(CombinationTwoActivity.this.getApplicationContext(), "Device connection was lost", 0).show();
                } else if (intent.getAction().equals("SPT_INPUTSTRING_EX")) {
                    SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                    SptInputStringEx inputString = (SptInputStringEx) intent.getExtras().getSerializable("SPT_INPUTSTRING_EX");
                    SimpleDialog.sptInputStringExDiagnose(CombinationTwoActivity.this.contexts, inputString.getDialogTitle(), inputString.getDialogContent(), inputString.getInputHint());
                } else if (intent.getAction().equals("SPT_INPUT_NUMERIC")) {
                    SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                    SptInputNumric inputNumric = (SptInputNumric) intent.getExtras().getSerializable("SPT_INPUT_NUMERIC");
                    SimpleDialog.sptInputNumericDiagnose(CombinationTwoActivity.this.contexts, inputNumric.getDialogTitle(), inputNumric.getDialogContent(), inputNumric.getInputHint(), inputNumric.getDigit());
                } else if (intent.getAction().equals("SPT_INPUTBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                    Spt_Nobuttonbox_Text inputBox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTBOX_TEXT");
                    SimpleDialog.sptInputBoxTextDiagnose(CombinationTwoActivity.this.contexts, inputBox.getTitle(), inputBox.getContent());
                } else if (intent.getAction().equals("SPT_INPUTSTRING")) {
                    SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                    Spt_Nobuttonbox_Text inputStr = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTSTRING");
                    SimpleDialog.sptInputStringDiagnose(CombinationTwoActivity.this.contexts, inputStr.getTitle(), inputStr.getContent());
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN")) {
                    ArrayList<SptTroubleTest> troubleCodeFrozenList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN");
                    r0 = new Intent(CombinationTwoActivity.this, FaultCodeFrozenActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                    CombinationTwoActivity.this.startActivity(r0);
                    CombinationTwoActivity.this.overridePendingTransition(0, 0);
                    CombinationTwoActivity.this.finish();
                    CombinationTwoActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                    ArrayList<SptExDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                    r0 = new Intent(CombinationTwoActivity.this, VWDataStreamActivity.class);
                    r0.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                    CombinationTwoActivity.this.startActivity(r0);
                    CombinationTwoActivity.this.overridePendingTransition(0, 0);
                    CombinationTwoActivity.this.finish();
                    CombinationTwoActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_SHOW_PICTURE")) {
                    SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                    String pictureName = intent.getExtras().getString("SPT_SHOW_PICTURE");
                    SimpleDialog.sptShowPictureDiagnose(CombinationTwoActivity.this.contexts, pictureName);
                } else if (intent.getAction().equals("SPT_COMBINATION_MENU")) {
                    Spt_Combination_Menu spt = (Spt_Combination_Menu) intent.getExtras().getSerializable("SPT_COMBINATION_MENU");
                    if (spt.getData() == null || spt.getData().size() <= 0) {
                        CombinationTwoActivity.this.dataList.addAll(spt.getData());
                        CombinationTwoActivity.this.firstNum = spt.getFirstNum();
                        CombinationTwoActivity.this.dataList.remove(0);
                        Integer[] numArr = new Integer[0];
                        new InitData(CombinationTwoActivity.this).execute(r0);
                        SimpleDialog.closeProgressDialog(CombinationTwoActivity.this.progressDialog);
                    }
                }
            }
        }
    }

    public CombinationTwoActivity() {
        this.dataList = new ArrayList();
        this.MSG_PRINT_RESULT = 0;
        this.MSG_PRINT_START = InBandBytestreamManager.MAXIMUM_BLOCK_SIZE;
        this.sb = null;
        this.count = 0;
        this.bmp = new C03711();
        this.baseHandler = new C03722();
    }

    public void dayin() {
        getPrintDate();
        new Thread(this.bmp).start();
    }

    private void getPrintDate() {
        this.sb = new StringBuffer();
        for (int i = 0; i < this.dataList.size(); i++) {
            this.printStringData = (String) this.dataList.get(i);
            this.sb.append(this.printStringData + SpecilApiUtil.LINE_SEP);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        this.contexts = this;
        setContentView(C0136R.layout.combination_menu);
        MyApplication.getInstance().addActivity(this);
        registerBoradcastReceiver();
        initViews();
        initialize();
        BluetoothChatService.setHandler(this.baseHandler);
    }

    protected void onResume() {
        super.onResume();
        this.isExecuteS = f1237D;
    }

    protected void onStop() {
        super.onStop();
        this.isExecuteS = false;
    }

    private void initViews() {
        this.radioGroup = (RadioGroup) findViewById(C0136R.id.combination_bottom);
        this.listView = (ListView) findViewById(C0136R.id.combination_listView_left);
        this.tvTitle = (TextView) findViewById(C0136R.id.combination_title_text);
        this.radioSure = (RadioButton) findViewById(C0136R.id.combination_sure);
        this.radioClear = (RadioButton) findViewById(C0136R.id.combination_clear);
        this.radioBack = (RadioButton) findViewById(C0136R.id.combination_back);
    }

    private void initialize() {
        this.radioSure.setOnClickListener(this);
        this.radioClear.setOnClickListener(this);
        this.radioBack.setOnClickListener(this);
        if (!(getIntent() == null || getIntent().getExtras() == null)) {
            Spt_Combination_Menu spt = (Spt_Combination_Menu) getIntent().getExtras().getSerializable("SPT_COMBINATION_MENU");
            this.firstNum = spt.getFirstNum();
            if (spt.getData() != null && spt.getData().size() > 0) {
                this.tvTitle.setText((CharSequence) spt.getData().get(0));
                this.dataList.addAll(spt.getData());
                this.dataList.remove(0);
            }
        }
        new InitData(this).execute(new Integer[0]);
        this.listView.setOnItemClickListener(new C03733());
    }

    private void backPressed() {
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
        byte[] selectItemsBytes = ByteHexHelper.hexStringToBytes(ByteHexHelper.binaryString2hexString(new StringBuffer().toString()));
        Constant.feedback = null;
        Constant.feedback = selectItemsBytes;
        CToJava.streamFlag = Boolean.valueOf(false);
        Constant.streamNextCode = selectItemsBytes;
        if (Constant.bridge != null) {
            Constant.bridge.putData();
        }
    }

    protected void onDestroy() {
        SimpleDialog.closeProgressDialog(this.progressDialog);
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
        this.myIntentFilter.addAction("SPT_COMBINATION_MENU");
        registerReceiver(this.receiver, this.myIntentFilter);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        Constant.feedback = null;
        byte[] CombinationMenu3 = new byte[2];
        CombinationMenu3[1] = (byte) 2;
        Constant.CombinationMenu = CombinationMenu3;
        CToJava.combinationFlag = Boolean.valueOf(f1237D);
        return f1237D;
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.combination_sure) {
            Constant.feedback = null;
            Constant.CombinationMenu = new byte[2];
            CToJava.combinationFlag = Boolean.valueOf(f1237D);
        } else if (v.getId() == C0136R.id.combination_clear) {
            Constant.feedback = null;
            byte[] CombinationMenu2 = new byte[2];
            CombinationMenu2[1] = (byte) 1;
            Constant.CombinationMenu = CombinationMenu2;
            CToJava.combinationFlag = Boolean.valueOf(f1237D);
        } else if (v.getId() == C0136R.id.combination_back) {
            Constant.feedback = null;
            byte[] CombinationMenu3 = new byte[2];
            CombinationMenu3[1] = (byte) 2;
            Constant.CombinationMenu = CombinationMenu3;
            CToJava.combinationFlag = Boolean.valueOf(f1237D);
        }
    }
}

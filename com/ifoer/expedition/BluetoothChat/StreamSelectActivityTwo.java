package com.ifoer.expedition.BluetoothChat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.adapter.StreamSelectAdapter;
import com.ifoer.adapter.StreamSelectAdapter.Item;
import com.ifoer.entity.Constant;
import com.ifoer.entity.SptActiveTest;
import com.ifoer.entity.SptDataStreamIdEx;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptInputNumric;
import com.ifoer.entity.SptInputStringEx;
import com.ifoer.entity.SptMessageBoxText;
import com.ifoer.entity.SptStreamSelectIdItem;
import com.ifoer.entity.SptTroubleTest;
import com.ifoer.entity.Spt_Nobuttonbox_Text;
import com.ifoer.expedition.BluetoothOrder.ByteHexHelper;
import com.ifoer.expedition.cto.CToJava;
import com.ifoer.expeditionphone.BaseActivity;
import com.ifoer.mine.Contact;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.NetPOSPrinter;
import com.ifoer.util.SimpleDialog;
import com.ifoer.util.SimpleDialogControl;
import com.launch.rcu.socket.DiaLogController;
import com.launch.service.BundleBuilder;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.util.ArrayList;
import java.util.HashMap;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.WKSRecord.Service;

public class StreamSelectActivityTwo extends BaseActivity {
    private static final boolean f2126D = true;
    private final int MSG_PRINT_RESULT;
    private final int MSG_PRINT_START;
    private StreamSelectAdapter adapter;
    Handler baseHandler;
    Bitmap bit_first;
    Bitmap bit_second;
    Runnable bmp;
    private Bundle bundle;
    private Button checkCurrentPageItem;
    private AlertDialog exidApp;
    private ListView gridView;
    private boolean isExPorject;
    private Boolean isExecuteS;
    private long lastTime;
    public LinearLayout linear;
    private ArrayList<SptStreamSelectIdItem> list;
    private Context mContexts;
    private IntentFilter myIntentFilter;
    Bitmap printBitmap;
    int printResult;
    private SptStreamSelectIdItem printSptStreamSelectIdItem;
    private ProgressDialog progressDialog;
    private mBroadcastReceiver receiver;
    private StringBuffer sb;
    private Button sure;
    private TextView titel_name;
    private Button un_select;

    /* renamed from: com.ifoer.expedition.BluetoothChat.StreamSelectActivityTwo.1 */
    class C04761 implements Runnable {
        C04761() {
        }

        public void run() {
            NetPOSPrinter bmpPrinter = new NetPOSPrinter(StreamSelectActivityTwo.this);
            StreamSelectActivityTwo.this.bit_first = bmpPrinter.drawBitFirst();
            StreamSelectActivityTwo.this.bit_second = bmpPrinter.drawBitSecond(StreamSelectActivityTwo.this.sb.toString());
            StreamSelectActivityTwo.this.printBitmap = NetPOSPrinter.mixtureBitmap(StreamSelectActivityTwo.this.bit_first, StreamSelectActivityTwo.this.bit_second);
            StreamSelectActivityTwo.this.printResult = bmpPrinter.printPic(StreamSelectActivityTwo.this.printBitmap);
            bmpPrinter.resultToast(StreamSelectActivityTwo.this.printResult);
            StreamSelectActivityTwo.this.baseHandler.obtainMessage(0, Integer.valueOf(StreamSelectActivityTwo.this.printResult)).sendToTarget();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.StreamSelectActivityTwo.2 */
    class C04772 extends Handler {
        C04772() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case InBandBytestreamManager.MAXIMUM_BLOCK_SIZE /*65535*/:
                    StreamSelectActivityTwo.this.dayin();
                case 10101010:
                    if (StreamSelectActivityTwo.this.progressDialog != null && StreamSelectActivityTwo.this.progressDialog.isShowing()) {
                        SimpleDialog.closeProgressDialog(StreamSelectActivityTwo.this.progressDialog);
                    }
                    SimpleDialog.checkConectior(StreamSelectActivityTwo.this.mContexts, StreamSelectActivityTwo.this.getString(C0136R.string.initializeTilte), StreamSelectActivityTwo.this.getString(C0136R.string.check_connector));
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.StreamSelectActivityTwo.3 */
    class C04783 implements OnItemClickListener {
        C04783() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            Item item = (Item) arg1.getTag();
            item.checkBox.toggle();
            StreamSelectAdapter.getIsSelected().put(Integer.valueOf(arg2), Boolean.valueOf(item.checkBox.isChecked()));
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.StreamSelectActivityTwo.4 */
    class C04794 implements OnClickListener {
        C04794() {
        }

        public void onClick(View v) {
            int i;
            StringBuffer choiceItem = new StringBuffer();
            HashMap<Integer, Boolean> putItemSite = new HashMap();
            for (i = 0; i < StreamSelectActivityTwo.this.list.size(); i++) {
                boolean flag = ((Boolean) StreamSelectAdapter.getIsSelected().get(Integer.valueOf(i))).booleanValue();
                Constant.streamSelectHashMap.put(Integer.valueOf(i), Integer.valueOf(((SptStreamSelectIdItem) StreamSelectActivityTwo.this.list.get(i)).getSite()));
                if (flag) {
                    putItemSite.put(Integer.valueOf(((SptStreamSelectIdItem) StreamSelectActivityTwo.this.list.get(i)).getSite()), Boolean.valueOf(flag));
                    Constant.streamIsSelectedList.add(Integer.valueOf(((SptStreamSelectIdItem) StreamSelectActivityTwo.this.list.get(i)).getSite()));
                } else {
                    putItemSite.put(Integer.valueOf(((SptStreamSelectIdItem) StreamSelectActivityTwo.this.list.get(i)).getSite()), Boolean.valueOf(flag));
                }
            }
            for (i = 1; i <= putItemSite.size(); i++) {
                if (((Boolean) putItemSite.get(Integer.valueOf(i))).booleanValue()) {
                    choiceItem.append(Contact.RELATION_FRIEND);
                } else {
                    choiceItem.append(Contact.RELATION_ASK);
                }
            }
            if (choiceItem.toString().contains(Contact.RELATION_FRIEND)) {
                Constant.setStreamItemNum(StreamSelectActivityTwo.this.mContexts);
                byte[] selectItemsBytes = ByteHexHelper.hexStringToBytes(ByteHexHelper.binaryString2hexString(choiceItem.toString()));
                Constant.feedback = null;
                Constant.feedback = selectItemsBytes;
                CToJava.streamFlag = Boolean.valueOf(StreamSelectActivityTwo.f2126D);
                Constant.streamNextCode = Constant.noInterruptStreamCode;
                if (Constant.bridge != null) {
                    Constant.bridge.putData();
                }
                if (StreamSelectActivityTwo.this.progressDialog == null) {
                    StreamSelectActivityTwo.this.progressDialog = new ProgressDialog(StreamSelectActivityTwo.this.mContexts);
                    StreamSelectActivityTwo.this.progressDialog.setCancelable(false);
                } else {
                    StreamSelectActivityTwo.this.progressDialog.dismiss();
                    StreamSelectActivityTwo.this.progressDialog = null;
                    StreamSelectActivityTwo.this.progressDialog = new ProgressDialog(StreamSelectActivityTwo.this.mContexts);
                    StreamSelectActivityTwo.this.progressDialog.setCancelable(false);
                }
                SimpleDialog.openProgressDialog(StreamSelectActivityTwo.this.mContexts, StreamSelectActivityTwo.this.getResources().getString(C0136R.string.dataDisposeTilte), StreamSelectActivityTwo.this.getResources().getString(C0136R.string.dataDisposeMessage), StreamSelectActivityTwo.this.progressDialog);
                return;
            }
            Toast.makeText(StreamSelectActivityTwo.this, StreamSelectActivityTwo.this.getResources().getString(C0136R.string.choice_one_at_least), 0).show();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.StreamSelectActivityTwo.5 */
    class C04805 implements OnClickListener {
        C04805() {
        }

        public void onClick(View arg0) {
            for (int i = 0; i < StreamSelectActivityTwo.this.gridView.getCount(); i++) {
                StreamSelectAdapter.getIsSelected().put(Integer.valueOf(i), Boolean.valueOf(false));
            }
            StreamSelectActivityTwo.this.adapter.notifyDataSetChanged();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.StreamSelectActivityTwo.6 */
    class C04816 implements OnClickListener {
        C04816() {
        }

        public void onClick(View v) {
            int sPoint = StreamSelectActivityTwo.this.gridView.getFirstVisiblePosition();
            int ePoint = StreamSelectActivityTwo.this.gridView.getLastVisiblePosition();
            for (int i = sPoint; i <= ePoint; i++) {
                StreamSelectAdapter.getIsSelected().put(Integer.valueOf(i), Boolean.valueOf(StreamSelectActivityTwo.f2126D));
            }
            StreamSelectActivityTwo.this.adapter.notifyDataSetChanged();
        }
    }

    @SuppressLint({"UseSparseArrays"})
    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (StreamSelectActivityTwo.this.isExecuteS.booleanValue()) {
                if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - StreamSelectActivityTwo.this.lastTime) / 1000 > 30) {
                    SimpleDialog.ExitDialog(context);
                }
                if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                    StreamSelectActivityTwo.this.lastTime = System.currentTimeMillis();
                }
                if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    SimpleDialog.closeProgressDialog(StreamSelectActivityTwo.this.progressDialog);
                } else if (intent.getAction().equals("feedbackMeauData")) {
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("menuData");
                    r0 = new Intent(StreamSelectActivityTwo.this, CarDiagnoseActivity.class);
                    r0.putExtra("menuData", menuDataList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    StreamSelectActivityTwo.this.startActivity(r0);
                    SimpleDialog.closeProgressDialog(StreamSelectActivityTwo.this.progressDialog);
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                    StreamSelectActivityTwo.this.closeDialog();
                    StreamSelectActivityTwo.this.finish();
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_DS_MENU_ID")) {
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("SPT_DS_MENU_ID");
                    r0 = new Intent(StreamSelectActivityTwo.this, CarDiagnoseActivity.class);
                    r0.putExtra("menuData", menuDataList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(StreamSelectActivityTwo.this.progressDialog);
                    StreamSelectActivityTwo.this.startActivity(r0);
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                    StreamSelectActivityTwo.this.closeDialog();
                    StreamSelectActivityTwo.this.finish();
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_ACTIVE_TEST")) {
                    SptActiveTest sptActiveTest = (SptActiveTest) intent.getExtras().getSerializable("ACTIVE_TEST");
                    Intent active = new Intent(StreamSelectActivityTwo.this, ActiveTestActivity.class);
                    active.putExtra("ACTIVE_TEST", sptActiveTest);
                    active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    StreamSelectActivityTwo.this.startActivity(active);
                    SimpleDialog.closeProgressDialog(StreamSelectActivityTwo.this.progressDialog);
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                    StreamSelectActivityTwo.this.closeDialog();
                    StreamSelectActivityTwo.this.finish();
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_NOBUTTONBOX_TEXT")) {
                    if (StreamSelectActivityTwo.this.progressDialog == null) {
                        StreamSelectActivityTwo.this.progressDialog = new ProgressDialog(StreamSelectActivityTwo.this.mContexts);
                        StreamSelectActivityTwo.this.progressDialog.setCancelable(false);
                    } else {
                        StreamSelectActivityTwo.this.progressDialog.dismiss();
                        StreamSelectActivityTwo.this.progressDialog = null;
                        StreamSelectActivityTwo.this.progressDialog = new ProgressDialog(StreamSelectActivityTwo.this.mContexts);
                        StreamSelectActivityTwo.this.progressDialog.setCancelable(false);
                    }
                    Spt_Nobuttonbox_Text nobuttonbox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("Nobuttonbox");
                    SimpleDialog.openProgressDialog(StreamSelectActivityTwo.this.mContexts, nobuttonbox.getTitle(), nobuttonbox.getContent(), StreamSelectActivityTwo.this.progressDialog);
                } else if (intent.getAction().equals("closeNobuttonBox")) {
                    SimpleDialog.closeProgressDialog(StreamSelectActivityTwo.this.progressDialog);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE")) {
                    ArrayList<SptTroubleTest> troubleTestList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE");
                    r0 = new Intent(StreamSelectActivityTwo.this, FaultCodeActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE", troubleTestList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(StreamSelectActivityTwo.this.progressDialog);
                    StreamSelectActivityTwo.this.startActivity(r0);
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                    StreamSelectActivityTwo.this.closeDialog();
                    StreamSelectActivityTwo.this.finish();
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EXIT_SHOW_WINDOW")) {
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    SimpleDialog.closeProgressDialog(StreamSelectActivityTwo.this.progressDialog);
                    StreamSelectActivityTwo.this.closeDialog();
                    StreamSelectActivityTwo.this.finish();
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                } else if (intent.getAction().equals("SPT_STREAM_SELECT_ID_EX")) {
                    ArrayList<SptStreamSelectIdItem> streamSelectIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_STREAM_SELECT_ID_EX");
                    r0 = new Intent(StreamSelectActivityTwo.this, StreamSelectActivity.class);
                    r0.putExtra("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    StreamSelectActivityTwo.this.startActivity(r0);
                    SimpleDialog.closeProgressDialog(StreamSelectActivityTwo.this.progressDialog);
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                    StreamSelectActivityTwo.this.closeDialog();
                    StreamSelectActivityTwo.this.finish();
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                    ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                    r0 = new Intent(StreamSelectActivityTwo.this, DataStreamActivity.class);
                    r0.putExtra("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    StreamSelectActivityTwo.this.startActivity(r0);
                    SimpleDialog.closeProgressDialog(StreamSelectActivityTwo.this.progressDialog);
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                    StreamSelectActivityTwo.this.closeDialog();
                    StreamSelectActivityTwo.this.finish();
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_MESSAGEBOX_TEXT")) {
                    SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) intent.getExtras().getSerializable("SPT_MESSAGEBOX_TEXT");
                    SimpleDialogControl.showDiaglog(context, sptMessageBoxText.getDialogType(), sptMessageBoxText);
                } else if (intent.getAction().equals("ConnectionLost")) {
                    Toast.makeText(StreamSelectActivityTwo.this.getApplicationContext(), "Device connection was lost", 0).show();
                } else if (intent.getAction().equals("SPT_INPUTSTRING_EX")) {
                    SimpleDialog.closeProgressDialog(StreamSelectActivityTwo.this.progressDialog);
                    SptInputStringEx inputString = (SptInputStringEx) intent.getExtras().getSerializable("SPT_INPUTSTRING_EX");
                    DiaLogController.diaLogControllerRemote2(StreamSelectActivityTwo.this.mContexts, 16, inputString.getDialogTitle(), inputString.getDialogContent(), inputString.getInputHint(), 0);
                } else if (intent.getAction().equals("SPT_INPUT_NUMERIC")) {
                    SimpleDialog.closeProgressDialog(StreamSelectActivityTwo.this.progressDialog);
                    SptInputNumric inputNumric = (SptInputNumric) intent.getExtras().getSerializable("SPT_INPUT_NUMERIC");
                    DiaLogController.diaLogControllerRemote2(StreamSelectActivityTwo.this.mContexts, 15, inputNumric.getDialogTitle(), inputNumric.getDialogContent(), inputNumric.getInputHint(), inputNumric.getDigit());
                } else if (intent.getAction().equals("SPT_INPUTBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(StreamSelectActivityTwo.this.progressDialog);
                    Spt_Nobuttonbox_Text inputBox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTBOX_TEXT");
                    DiaLogController.diaLogControllerRemote(StreamSelectActivityTwo.this.mContexts, 7, inputBox.getTitle(), inputBox.getContent());
                } else if (intent.getAction().equals("SPT_INPUTSTRING")) {
                    SimpleDialog.closeProgressDialog(StreamSelectActivityTwo.this.progressDialog);
                    Spt_Nobuttonbox_Text inputStr = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTSTRING");
                    DiaLogController.diaLogControllerRemote(StreamSelectActivityTwo.this.mContexts, 8, inputStr.getTitle(), inputStr.getContent());
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN")) {
                    ArrayList<SptTroubleTest> troubleCodeFrozenList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN");
                    r0 = new Intent(StreamSelectActivityTwo.this, FaultCodeFrozenActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(StreamSelectActivityTwo.this.progressDialog);
                    StreamSelectActivityTwo.this.startActivity(r0);
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                    StreamSelectActivityTwo.this.closeDialog();
                    StreamSelectActivityTwo.this.finish();
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                    ArrayList<SptExDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                    r0 = new Intent(StreamSelectActivityTwo.this, VWDataStreamActivity.class);
                    r0.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(StreamSelectActivityTwo.this.progressDialog);
                    StreamSelectActivityTwo.this.startActivity(r0);
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                    StreamSelectActivityTwo.this.closeDialog();
                    StreamSelectActivityTwo.this.finish();
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_DATASTREAM_ID_EX")) {
                    ArrayList<SptDataStreamIdEx> list = (ArrayList) intent.getExtras().getSerializable("SPT_DATASTREAM_ID_EX");
                    Intent agingIntent = new Intent(StreamSelectActivityTwo.this, DataStreamItemActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("SPT_DATASTREAM_ID_EX", list);
                    agingIntent.putExtras(bundle);
                    agingIntent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(StreamSelectActivityTwo.this.progressDialog);
                    StreamSelectActivityTwo.this.startActivity(agingIntent);
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                    StreamSelectActivityTwo.this.closeDialog();
                    StreamSelectActivityTwo.this.finish();
                    StreamSelectActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_SHOW_PICTURE")) {
                    SimpleDialog.closeProgressDialog(StreamSelectActivityTwo.this.progressDialog);
                    SimpleDialog.sptShowPictureDiagnose(StreamSelectActivityTwo.this.mContexts, intent.getExtras().getString("SPT_SHOW_PICTURE"));
                } else if (intent.getAction().equals("RCU_L_STREAMSELECT_CHOICE")) {
                    if (MySharedPreferences.getStringValue(StreamSelectActivityTwo.this, BundleBuilder.IdentityType).equals(Contact.RELATION_ASK)) {
                        strData = intent.getExtras().getString("RCU_L_STREAMSELECT_CHOICE_DATA");
                        for (i = 0; i < StreamSelectActivityTwo.this.adapter.getCount(); i++) {
                            if (new StringBuilder(String.valueOf(strData.charAt(i))).toString().equals(Contact.RELATION_FRIEND)) {
                                StreamSelectAdapter.getIsSelected().put(Integer.valueOf(i), Boolean.valueOf(StreamSelectActivityTwo.f2126D));
                            } else {
                                StreamSelectAdapter.getIsSelected().put(Integer.valueOf(i), Boolean.valueOf(false));
                            }
                        }
                        StreamSelectActivityTwo.this.adapter.notifyDataSetChanged();
                    }
                } else if (intent.getAction().equals("RemoteDiagStatus")) {
                    intent.getIntExtra(SharedPref.TYPE, -1);
                } else if (intent.getAction().equals("RemoteDiagSelectItem")) {
                    strData = intent.getExtras().getString("SelectItemFlag");
                    for (i = 0; i < StreamSelectActivityTwo.this.adapter.getCount(); i++) {
                        if (new StringBuilder(String.valueOf(strData.charAt(i))).toString().equals(Contact.RELATION_FRIEND)) {
                            StreamSelectAdapter.getIsSelected().put(Integer.valueOf(i), Boolean.valueOf(StreamSelectActivityTwo.f2126D));
                        } else {
                            StreamSelectAdapter.getIsSelected().put(Integer.valueOf(i), Boolean.valueOf(false));
                        }
                    }
                    StreamSelectActivityTwo.this.adapter.notifyDataSetChanged();
                    StringBuffer choiceItem = new StringBuffer();
                    HashMap<Integer, Boolean> putItemSite = new HashMap();
                    for (i = 0; i < StreamSelectActivityTwo.this.list.size(); i++) {
                        boolean flag = ((Boolean) StreamSelectAdapter.getIsSelected().get(Integer.valueOf(i))).booleanValue();
                        Constant.streamSelectHashMap.put(Integer.valueOf(i), Integer.valueOf(((SptStreamSelectIdItem) StreamSelectActivityTwo.this.list.get(i)).getSite()));
                        if (flag) {
                            putItemSite.put(Integer.valueOf(((SptStreamSelectIdItem) StreamSelectActivityTwo.this.list.get(i)).getSite()), Boolean.valueOf(flag));
                            Constant.streamIsSelectedList.add(Integer.valueOf(((SptStreamSelectIdItem) StreamSelectActivityTwo.this.list.get(i)).getSite()));
                        } else {
                            putItemSite.put(Integer.valueOf(((SptStreamSelectIdItem) StreamSelectActivityTwo.this.list.get(i)).getSite()), Boolean.valueOf(flag));
                        }
                    }
                    for (i = 1; i <= putItemSite.size(); i++) {
                        if (((Boolean) putItemSite.get(Integer.valueOf(i))).booleanValue()) {
                            choiceItem.append(Contact.RELATION_FRIEND);
                        } else {
                            choiceItem.append(Contact.RELATION_ASK);
                        }
                    }
                    if (choiceItem.toString().contains(Contact.RELATION_FRIEND)) {
                        Constant.setStreamItemNum(StreamSelectActivityTwo.this.mContexts);
                        byte[] selectItemsBytes = ByteHexHelper.hexStringToBytes(ByteHexHelper.binaryString2hexString(choiceItem.toString()));
                        Constant.feedback = null;
                        Constant.feedback = selectItemsBytes;
                        CToJava.streamFlag = Boolean.valueOf(StreamSelectActivityTwo.f2126D);
                        Constant.streamNextCode = Constant.noInterruptStreamCode;
                        if (Constant.bridge != null) {
                            Constant.bridge.putData();
                        }
                    }
                }
            }
        }
    }

    public StreamSelectActivityTwo() {
        this.list = new ArrayList();
        this.isExecuteS = Boolean.valueOf(false);
        this.MSG_PRINT_RESULT = 0;
        this.MSG_PRINT_START = InBandBytestreamManager.MAXIMUM_BLOCK_SIZE;
        this.sb = null;
        this.printSptStreamSelectIdItem = null;
        this.isExPorject = f2126D;
        this.bmp = new C04761();
        this.baseHandler = new C04772();
    }

    private void getPrintDate() {
        this.sb = new StringBuffer();
        for (int i = 0; i < this.list.size(); i++) {
            this.printSptStreamSelectIdItem = (SptStreamSelectIdItem) this.list.get(i);
            this.sb.append(new StringBuilder(String.valueOf(this.printSptStreamSelectIdItem.getStreamSelectStr())).append(SpecilApiUtil.LINE_SEP).toString());
        }
    }

    public void dayin() {
        getPrintDate();
        new Thread(this.bmp).start();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContexts = this;
        setContentView(C0136R.layout.stream_select);
        int typeint = MySharedPreferences.getIntValue(this, "PDT_TYPE");
        if (typeint == MyHttpException.ERROR_PASW || typeint == NetPOSPrinter.PRINT_WIDTH || typeint == MyHttpException.ERROR_FAIL_PASW_PROTECTION) {
            this.isExPorject = false;
        } else {
            this.isExPorject = f2126D;
        }
        MyApplication.getInstance().addActivity(this);
        registerBoradcastReceiver();
        initView();
        BluetoothChatService.setHandler(this.baseHandler);
    }

    protected void onResume() {
        super.onResume();
        this.isExecuteS = Boolean.valueOf(f2126D);
    }

    protected void onStop() {
        super.onStop();
        this.isExecuteS = Boolean.valueOf(false);
    }

    private void initView() {
        this.bundle = getIntent().getExtras();
        if (this.bundle != null) {
            this.list = (ArrayList) this.bundle.getSerializable("SPT_STREAM_SELECT_ID_EX");
        }
        if (this.list != null && this.list.size() > 1) {
            MySharedPreferences.setInt(this.mContexts, MySharedPreferences.dataStreamTotal, this.list.size());
        }
        this.gridView = (ListView) findViewById(C0136R.id.stream_item_select_gridView);
        this.adapter = new StreamSelectAdapter(this.list, this, f2126D, f2126D);
        this.gridView.setAdapter(this.adapter);
        this.gridView.setOnItemClickListener(new C04783());
        this.sure = (Button) findViewById(C0136R.id.sure);
        this.sure.setOnClickListener(new C04794());
        this.un_select = (Button) findViewById(C0136R.id.un_select);
        this.checkCurrentPageItem = (Button) findViewById(C0136R.id.check_current_page_item);
        this.un_select.setOnClickListener(new C04805());
        this.checkCurrentPageItem.requestFocus();
        this.checkCurrentPageItem.setOnClickListener(new C04816());
    }

    private void keyBackDo() {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this.mContexts);
            this.progressDialog.setCancelable(false);
        } else {
            this.progressDialog.dismiss();
            this.progressDialog = null;
            this.progressDialog = new ProgressDialog(this.mContexts);
            this.progressDialog.setCancelable(false);
        }
        SimpleDialog.openProgressDialog(this.mContexts, getResources().getString(C0136R.string.dataDisposeTilte), getResources().getString(C0136R.string.dataDisposeMessage), this.progressDialog);
        StringBuffer choiceItem = new StringBuffer();
        for (int i = 0; i < this.list.size(); i++) {
            choiceItem.append(Contact.RELATION_ASK);
        }
        byte[] selectItemsBytes = ByteHexHelper.hexStringToBytes(ByteHexHelper.binaryString2hexString(choiceItem.toString()));
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
        this.myIntentFilter.addAction("SPT_DATASTREAM_ID_EX");
        this.myIntentFilter.addAction("RCU_L_STREAMSELECT_CHOICE");
        this.myIntentFilter.addAction("RemoteDiagStatus");
        this.myIntentFilter.addAction("RemoteDiagSelectItem");
        registerReceiver(this.receiver, this.myIntentFilter);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 && keyCode != Service.SUNRPC) {
            return super.onKeyDown(keyCode, event);
        }
        keyBackDo();
        return f2126D;
    }

    public void closeDialog() {
        if (this.exidApp != null && this.exidApp.isShowing()) {
            this.exidApp.cancel();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == 2) {
            this.adapter = new StreamSelectAdapter(this.list, this, false, false);
        } else if (getResources().getConfiguration().orientation == 1) {
            this.adapter = new StreamSelectAdapter(this.list, this, f2126D, false);
        }
        this.gridView.setAdapter(this.adapter);
    }
}

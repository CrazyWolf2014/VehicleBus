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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.adapter.SpecialFunctionAdapter;
import com.ifoer.entity.Constant;
import com.ifoer.entity.MenuData;
import com.ifoer.entity.SpecialFunction;
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
import com.ifoer.expeditionphone.BaseActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.mine.Contact;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.NetPOSPrinter;
import com.ifoer.util.SimpleDialog;
import com.ifoer.util.SimpleDialogControl;
import com.launch.rcu.socket.DiaLogController;
import com.launch.service.BundleBuilder;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.util.ArrayList;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class SpecialFunctionActivity extends BaseActivity {
    private static final String TAG = "SpecialFunctionActivity";
    private final int MSG_PRINT_RESULT;
    private final int MSG_PRINT_START;
    Handler baseHandler;
    Bitmap bit_first;
    Bitmap bit_second;
    Runnable bmp;
    private AlertDialog exidApp;
    int firstReciverList;
    private boolean isExPorject;
    public LinearLayout linear;
    private LinearLayout mBottomButtonLayout;
    private Bundle mBundle;
    private int mColumsCount;
    SpecialFunctionAdapter mContentAdapter;
    private Context mContexts;
    private TextView mCustomTitle;
    private GridView mGridViewTitle;
    private GridView mGridview;
    private final Handler mHandler;
    private Boolean mIsExecuteC;
    private mBroadcastReceiver mReceiver;
    ArrayList<SpecialFunction> mSptList;
    SpecialFunctionAdapter mTitleAdapter;
    private ImageView menu_img;
    private IntentFilter myIntentFilter;
    private ArrayList<String> preButtonList;
    Bitmap printBitmap;
    int printResult;
    private SpecialFunction printSpecialFunction;
    private ProgressDialog progressDialog;
    private StringBuffer sb;
    private TextView titel_name;

    /* renamed from: com.ifoer.expedition.BluetoothChat.SpecialFunctionActivity.1 */
    class C04631 implements Runnable {
        C04631() {
        }

        public void run() {
            NetPOSPrinter bmpPrinter = new NetPOSPrinter(SpecialFunctionActivity.this);
            SpecialFunctionActivity.this.bit_first = bmpPrinter.drawBitFirst();
            SpecialFunctionActivity.this.bit_second = bmpPrinter.drawBitSecond(SpecialFunctionActivity.this.sb.toString());
            SpecialFunctionActivity.this.printBitmap = NetPOSPrinter.mixtureBitmap(SpecialFunctionActivity.this.bit_first, SpecialFunctionActivity.this.bit_second);
            SpecialFunctionActivity.this.printResult = bmpPrinter.printPic(SpecialFunctionActivity.this.printBitmap);
            bmpPrinter.resultToast(SpecialFunctionActivity.this.printResult);
            SpecialFunctionActivity.this.baseHandler.obtainMessage(0, Integer.valueOf(SpecialFunctionActivity.this.printResult)).sendToTarget();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.SpecialFunctionActivity.2 */
    class C04642 extends Handler {
        C04642() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case InBandBytestreamManager.MAXIMUM_BLOCK_SIZE /*65535*/:
                    SpecialFunctionActivity.this.dayin();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.SpecialFunctionActivity.3 */
    class C04653 extends Handler {
        C04653() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    if (!(SpecialFunctionActivity.this.mSptList == null || ((SpecialFunction) SpecialFunctionActivity.this.mSptList.get(0)).getColumsContentList() == null)) {
                        SpecialFunctionActivity.this.mContentAdapter.refresh(((SpecialFunction) SpecialFunctionActivity.this.mSptList.get(0)).getColumsContentList());
                    }
                    SpecialFunctionActivity.this.initButtons();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.SpecialFunctionActivity.4 */
    class C04664 implements OnItemClickListener {
        C04664() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
            byte[] buttonOrder = new byte[2];
            buttonOrder[1] = Byte.MIN_VALUE;
            buttonOrder[1] = (byte) (buttonOrder[1] + ByteHexHelper.intToHexByte(position / SpecialFunctionActivity.this.mColumsCount));
            Constant.continueSpecia = buttonOrder;
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.SpecialFunctionActivity.5 */
    class C04675 implements OnClickListener {
        private final /* synthetic */ int val$tempOrder;

        C04675(int i) {
            this.val$tempOrder = i;
        }

        public void onClick(View v) {
            byte[] buttonOrder = new byte[2];
            buttonOrder[1] = (byte) (buttonOrder[1] + ByteHexHelper.intToHexByte(this.val$tempOrder));
            if (1 != this.val$tempOrder) {
                Constant.continueSpecia = buttonOrder;
            } else if (((SpecialFunction) SpecialFunctionActivity.this.mSptList.get(0)).getColumsContentList() != null && ((SpecialFunction) SpecialFunctionActivity.this.mSptList.get(0)).getColumsContentList().size() >= SpecialFunctionActivity.this.firstReciverList) {
                Constant.continueSpecia = buttonOrder;
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.SpecialFunctionActivity.6 */
    class C04686 implements DialogInterface.OnClickListener {
        C04686() {
        }

        public void onClick(DialogInterface dialog, int which) {
            if (Constant.mChatService != null) {
                Constant.mChatService.stop();
            }
            SimpleDialog.closeProgressDialog(SpecialFunctionActivity.this.progressDialog);
            SpecialFunctionActivity.this.finish();
            Process.killProcess(Process.myPid());
            System.exit(0);
            SpecialFunctionActivity.this.overridePendingTransition(0, 0);
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.SpecialFunctionActivity.7 */
    class C04697 implements DialogInterface.OnClickListener {
        C04697() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (!SpecialFunctionActivity.this.mIsExecuteC.booleanValue()) {
                return;
            }
            if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                SimpleDialog.closeProgressDialog(SpecialFunctionActivity.this.progressDialog);
            } else if (intent.getAction().equals("feedbackMeauData")) {
                ArrayList<MenuData> menuDataLists = (ArrayList) intent.getExtras().getSerializable("menuData");
                menu = new Intent(SpecialFunctionActivity.this, CarDiagnoseActivityTwo.class);
                menu.putExtra("menuData", menuDataLists);
                menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(SpecialFunctionActivity.this.progressDialog);
                SpecialFunctionActivity.this.startActivity(menu);
                SpecialFunctionActivity.this.overridePendingTransition(0, 0);
                SpecialFunctionActivity.this.closeDialog();
                SpecialFunctionActivity.this.finish();
                SpecialFunctionActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_DS_MENU_ID")) {
                ArrayList<MenuData> menuDataList = (ArrayList) intent.getExtras().getSerializable("SPT_DS_MENU_ID");
                menu = new Intent(SpecialFunctionActivity.this, CarDiagnoseActivityTwo.class);
                menu.putExtra("menuData", menuDataList);
                menu.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(SpecialFunctionActivity.this.progressDialog);
                SpecialFunctionActivity.this.startActivity(menu);
                SpecialFunctionActivity.this.overridePendingTransition(0, 0);
                SpecialFunctionActivity.this.closeDialog();
                SpecialFunctionActivity.this.finish();
                SpecialFunctionActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_ACTIVE_TEST")) {
                SptActiveTest sptActiveTest = (SptActiveTest) intent.getExtras().getSerializable("ACTIVE_TEST");
                Intent active = new Intent(SpecialFunctionActivity.this, ActiveTestActivity.class);
                active.putExtra("ACTIVE_TEST", sptActiveTest);
                active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SpecialFunctionActivity.this.startActivity(active);
                SimpleDialog.closeProgressDialog(SpecialFunctionActivity.this.progressDialog);
                SpecialFunctionActivity.this.overridePendingTransition(0, 0);
                SpecialFunctionActivity.this.closeDialog();
                SpecialFunctionActivity.this.finish();
                SpecialFunctionActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_NOBUTTONBOX_TEXT")) {
                if (SpecialFunctionActivity.this.progressDialog == null) {
                    SpecialFunctionActivity.this.progressDialog = new ProgressDialog(SpecialFunctionActivity.this.mContexts);
                }
                Spt_Nobuttonbox_Text nobuttonbox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("Nobuttonbox");
                SimpleDialog.openProgressDialog(SpecialFunctionActivity.this.mContexts, nobuttonbox.getTitle(), nobuttonbox.getContent(), SpecialFunctionActivity.this.progressDialog);
            } else if (intent.getAction().equals("closeNobuttonBox")) {
                SimpleDialog.closeProgressDialog(SpecialFunctionActivity.this.progressDialog);
            } else if (intent.getAction().equals("SPT_TROUBLE_CODE")) {
                ArrayList<SptTroubleTest> troubleTestList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE");
                r0 = new Intent(SpecialFunctionActivity.this, FaultCodeActivity.class);
                r0.putExtra("SPT_TROUBLE_CODE", troubleTestList);
                r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(SpecialFunctionActivity.this.progressDialog);
                SpecialFunctionActivity.this.startActivity(r0);
                SpecialFunctionActivity.this.overridePendingTransition(0, 0);
                SpecialFunctionActivity.this.closeDialog();
                SpecialFunctionActivity.this.finish();
                SpecialFunctionActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_EXIT_SHOW_WINDOW")) {
                if (Constant.mChatService != null) {
                    Constant.mChatService.stop();
                }
                SimpleDialog.closeProgressDialog(SpecialFunctionActivity.this.progressDialog);
                Process.killProcess(Process.myPid());
                System.exit(0);
                SpecialFunctionActivity.this.closeDialog();
                SpecialFunctionActivity.this.finish();
                SpecialFunctionActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_STREAM_SELECT_ID_EX")) {
                ArrayList<SptStreamSelectIdItem> streamSelectIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_STREAM_SELECT_ID_EX");
                r0 = new Intent(SpecialFunctionActivity.this, StreamSelectActivity.class);
                r0.putExtra("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(SpecialFunctionActivity.this.progressDialog);
                SpecialFunctionActivity.this.startActivity(r0);
                SpecialFunctionActivity.this.overridePendingTransition(0, 0);
                SpecialFunctionActivity.this.closeDialog();
                SpecialFunctionActivity.this.finish();
                SpecialFunctionActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                r0 = new Intent(SpecialFunctionActivity.this, DataStreamActivity.class);
                r0.putExtra("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(SpecialFunctionActivity.this.progressDialog);
                SpecialFunctionActivity.this.startActivity(r0);
                SpecialFunctionActivity.this.overridePendingTransition(0, 0);
                SpecialFunctionActivity.this.closeDialog();
                SpecialFunctionActivity.this.finish();
                SpecialFunctionActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_MESSAGEBOX_TEXT")) {
                SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) intent.getExtras().getSerializable("SPT_MESSAGEBOX_TEXT");
                SimpleDialogControl.showDiaglog(context, sptMessageBoxText.getDialogType(), sptMessageBoxText);
            } else if (intent.getAction().equals("ConnectionLost")) {
                Toast.makeText(SpecialFunctionActivity.this.getApplicationContext(), SpecialFunctionActivity.this.getResources().getString(C0136R.string.connectionLost), 0).show();
            } else if (intent.getAction().equals("SPT_INPUTSTRING_EX")) {
                SimpleDialog.closeProgressDialog(SpecialFunctionActivity.this.progressDialog);
                SptInputStringEx inputString = (SptInputStringEx) intent.getExtras().getSerializable("SPT_INPUTSTRING_EX");
                DiaLogController.diaLogControllerRemote2(SpecialFunctionActivity.this.mContexts, 16, inputString.getDialogTitle(), inputString.getDialogContent(), inputString.getInputHint(), 0);
            } else if (intent.getAction().equals("SPT_INPUT_NUMERIC")) {
                SimpleDialog.closeProgressDialog(SpecialFunctionActivity.this.progressDialog);
                SptInputNumric inputNumric = (SptInputNumric) intent.getExtras().getSerializable("SPT_INPUT_NUMERIC");
                DiaLogController.diaLogControllerRemote2(SpecialFunctionActivity.this.mContexts, 15, inputNumric.getDialogTitle(), inputNumric.getDialogContent(), inputNumric.getInputHint(), inputNumric.getDigit());
            } else if (intent.getAction().equals("SPT_INPUTBOX_TEXT")) {
                SimpleDialog.closeProgressDialog(SpecialFunctionActivity.this.progressDialog);
                Spt_Nobuttonbox_Text inputBox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTBOX_TEXT");
                DiaLogController.diaLogControllerRemote(SpecialFunctionActivity.this.mContexts, 7, inputBox.getTitle(), inputBox.getContent());
            } else if (intent.getAction().equals("SPT_INPUTSTRING")) {
                SimpleDialog.closeProgressDialog(SpecialFunctionActivity.this.progressDialog);
                Spt_Nobuttonbox_Text inputStr = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTSTRING");
                DiaLogController.diaLogControllerRemote(SpecialFunctionActivity.this.mContexts, 8, inputStr.getTitle(), inputStr.getContent());
            } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN")) {
                ArrayList<SptTroubleTest> troubleCodeFrozenList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN");
                r0 = new Intent(SpecialFunctionActivity.this, FaultCodeFrozenActivity.class);
                r0.putExtra("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(SpecialFunctionActivity.this.progressDialog);
                SpecialFunctionActivity.this.startActivity(r0);
                SpecialFunctionActivity.this.overridePendingTransition(0, 0);
                SpecialFunctionActivity.this.closeDialog();
                SpecialFunctionActivity.this.finish();
                SpecialFunctionActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                r0 = new Intent(SpecialFunctionActivity.this, VWDataStreamActivity.class);
                r0.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(SpecialFunctionActivity.this.progressDialog);
                SpecialFunctionActivity.this.startActivity(r0);
                SpecialFunctionActivity.this.overridePendingTransition(0, 0);
                SpecialFunctionActivity.this.closeDialog();
                SpecialFunctionActivity.this.finish();
                SpecialFunctionActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_SHOW_PICTURE")) {
                SimpleDialog.closeProgressDialog(SpecialFunctionActivity.this.progressDialog);
                SimpleDialog.sptShowPictureDiagnose(SpecialFunctionActivity.this.mContexts, intent.getExtras().getString("SPT_SHOW_PICTURE"));
            } else if (intent.getAction().equals("SPT_SPECIAL_FUNCTION_ID")) {
                SimpleDialog.closeProgressDialog(SpecialFunctionActivity.this.progressDialog);
                SpecialFunctionActivity.this.mSptList = null;
                SpecialFunctionActivity.this.mSptList = (ArrayList) intent.getExtras().getSerializable("SPT_SPECIAL_FUNCTION_ID");
                SpecialFunctionActivity.this.mHandler.sendMessage(SpecialFunctionActivity.this.mHandler.obtainMessage(15));
            } else if (intent.getAction().equals("RemoteDiagStatus")) {
                intent.getIntExtra(SharedPref.TYPE, -1);
            }
        }
    }

    public SpecialFunctionActivity() {
        this.mIsExecuteC = Boolean.valueOf(false);
        this.mColumsCount = 0;
        this.MSG_PRINT_RESULT = 0;
        this.MSG_PRINT_START = InBandBytestreamManager.MAXIMUM_BLOCK_SIZE;
        this.sb = null;
        this.printSpecialFunction = null;
        this.isExPorject = true;
        this.bmp = new C04631();
        this.baseHandler = new C04642();
        this.firstReciverList = 0;
        this.mHandler = new C04653();
    }

    private void getPrintDate() {
        this.sb = new StringBuffer();
        for (int i = 0; i < this.mSptList.size(); i++) {
            this.printSpecialFunction = (SpecialFunction) this.mSptList.get(i);
            this.sb.append(new StringBuilder(String.valueOf(this.printSpecialFunction.getCustomTitle())).append("   ").append(this.printSpecialFunction.getColumsCount()).append("   ").append(this.printSpecialFunction.getButtonCount()).append(SpecilApiUtil.LINE_SEP).toString());
        }
    }

    public void dayin() {
        getPrintDate();
        new Thread(this.bmp).start();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.special_function);
        this.isExPorject = true;
        this.mContexts = this;
        if (MySharedPreferences.share == null) {
            MySharedPreferences.getSharedPref(getApplicationContext());
        }
        MyApplication.getInstance().addActivity(this);
        this.mBottomButtonLayout = (LinearLayout) findViewById(C0136R.id.bottom_button_layout);
        this.mBottomButtonLayout.setBackgroundColor(getResources().getColor(C0136R.color.light_gray));
        this.mBottomButtonLayout.setEnabled(true);
        this.mBottomButtonLayout.setClickable(true);
        this.mCustomTitle = (TextView) findViewById(C0136R.id.custom_title_text);
        this.mGridview = (GridView) findViewById(C0136R.id.view_content);
        this.mGridViewTitle = (GridView) findViewById(C0136R.id.view_title);
        initView();
        registerBoradcastReceiver();
    }

    public void registerBoradcastReceiver() {
        this.mReceiver = new mBroadcastReceiver();
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
        this.myIntentFilter.addAction("SPT_SPECIAL_FUNCTION_ID");
        this.myIntentFilter.addAction("RemoteDiagStatus");
        registerReceiver(this.mReceiver, this.myIntentFilter);
    }

    private void initView() {
        this.mBundle = getIntent().getExtras();
        this.mSptList = new ArrayList();
        if (this.mBundle != null) {
            this.mSptList = (ArrayList) this.mBundle.getSerializable("SPT_SPECIAL_FUNCTION_ID");
            if (this.mSptList != null) {
                if (((SpecialFunction) this.mSptList.get(0)).getColumsContentList() != null) {
                    this.firstReciverList = ((SpecialFunction) this.mSptList.get(0)).getColumsContentList().size();
                } else {
                    this.firstReciverList = 0;
                }
            }
        }
        this.mCustomTitle.setText(((SpecialFunction) this.mSptList.get(0)).getCustomTitle());
        this.mColumsCount = ((SpecialFunction) this.mSptList.get(0)).getColumsCount();
        initButtons();
        this.mGridview.setNumColumns(this.mColumsCount);
        this.mGridViewTitle.setNumColumns(this.mColumsCount);
        if (((SpecialFunction) this.mSptList.get(0)).getColumsContentList() != null) {
            this.mContentAdapter = new SpecialFunctionAdapter(((SpecialFunction) this.mSptList.get(0)).getColumsContentList(), this, this.mColumsCount, false);
        } else {
            this.mContentAdapter = new SpecialFunctionAdapter(null, this, this.mColumsCount, false);
        }
        this.mTitleAdapter = new SpecialFunctionAdapter(((SpecialFunction) this.mSptList.get(0)).getColumsTitleList(), this, this.mColumsCount, true);
        this.mGridview.setAdapter(this.mContentAdapter);
        this.mGridViewTitle.setAdapter(this.mTitleAdapter);
        this.mGridview.setOnItemClickListener(new C04664());
    }

    private boolean isSameButton(ArrayList<String> pre, ArrayList<String> current) {
        int curSize = 0;
        int preSize = pre == null ? 0 : pre.size();
        if (current != null) {
            curSize = current.size();
        }
        if (preSize != curSize) {
            return false;
        }
        if (preSize == 0) {
            return true;
        }
        for (int i = 0; i < preSize; i++) {
            if (!(pre.get(i) == null ? XmlPullParser.NO_NAMESPACE : (String) pre.get(i)).equalsIgnoreCase(current.get(i) == null ? XmlPullParser.NO_NAMESPACE : (String) current.get(i))) {
                return false;
            }
        }
        return true;
    }

    private void initButtons() {
        ArrayList<String> buttonList = ((SpecialFunction) this.mSptList.get(0)).getButtonList();
        if (!isSameButton(this.preButtonList, buttonList)) {
            this.preButtonList = buttonList;
            this.mBottomButtonLayout.removeAllViews();
            LayoutParams params = new LayoutParams(-1, -1);
            params.weight = 1.0f;
            params.height = -2;
            params.width = -2;
            int v_padding_space = (int) getResources().getDimension(C0136R.dimen.padding_space);
            if (this.isExPorject) {
                params.setMargins(v_padding_space, v_padding_space, v_padding_space, v_padding_space);
            }
            for (int i = 0; i < buttonList.size(); i++) {
                Button button = new Button(this.mContexts);
                button.setId(i);
                button.setText((CharSequence) buttonList.get(i));
                if (this.isExPorject) {
                    button.setBackgroundResource(C0136R.drawable.red_button_selector);
                }
                button.setTextSize(getResources().getDimension(C0136R.dimen.left_menu_size));
                button.setTextColor(getResources().getColor(C0136R.color.black));
                button.setLayoutParams(params);
                button.setOnClickListener(new C04675(i));
                this.mBottomButtonLayout.addView(button);
            }
        }
    }

    private void printlistContent(ArrayList<String> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Log.i(TAG, (String) list.get(i));
            }
        }
    }

    private void printButtonInfo() {
        if (this.mSptList != null && this.mSptList.get(0) != null && ((SpecialFunction) this.mSptList.get(0)).getButtonList() != null) {
            ((SpecialFunction) this.mSptList.get(0)).getButtonList();
        }
    }

    private void ExitMiniDiag() {
        if (Constant.mChatService != null) {
            Constant.mChatService.stop();
        }
        if (MainActivity.socketCall != null) {
            MainActivity.socketCall.stop();
        }
        MySharedPreferences.setString(this.mContexts, BundleBuilder.IdentityType, Contact.RELATION_FRIEND);
        Process.killProcess(Process.myPid());
        System.exit(0);
        finish();
        overridePendingTransition(0, 0);
    }

    protected void onResume() {
        this.mIsExecuteC = Boolean.valueOf(true);
        super.onResume();
    }

    protected void onStop() {
        this.mIsExecuteC = Boolean.valueOf(false);
        super.onStop();
    }

    protected void onDestroy() {
        SimpleDialog.closeProgressDialog(this.progressDialog);
        if (this.mReceiver != null) {
            unregisterReceiver(this.mReceiver);
        }
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 && keyCode != Service.SUNRPC) {
            return super.onKeyDown(keyCode, event);
        }
        ExitDialog();
        return true;
    }

    public Dialog ExitDialog() {
        SimpleDialog.closeProgressDialog(this.progressDialog);
        if (this.exidApp == null || !this.exidApp.isShowing()) {
            Builder builder = new Builder(this.mContexts);
            builder.setMessage(C0136R.string.exitDynamicDepot);
            builder.setCancelable(true);
            builder.setPositiveButton(getResources().getString(C0136R.string.enter), new C04686());
            builder.setNegativeButton(getResources().getString(C0136R.string.cancel), new C04697());
            this.exidApp = builder.create();
            this.exidApp.show();
        } else {
            this.exidApp.cancel();
        }
        return this.exidApp;
    }

    public void closeDialog() {
        if (this.exidApp != null && this.exidApp.isShowing()) {
            this.exidApp.cancel();
        }
    }
}

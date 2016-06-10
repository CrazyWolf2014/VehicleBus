package com.ifoer.expedition.BluetoothChat;

import android.app.Activity;
import android.app.AlertDialog.Builder;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.example.gpiomanager.MainActivity;
import com.ifoer.adapter.CarDiagnoseAdapter;
import com.ifoer.adapter.CarDiagnoseAdapter.Item;
import com.ifoer.entity.Constant;
import com.ifoer.entity.MenuData;
import com.ifoer.entity.SpecialFunction;
import com.ifoer.entity.SptActiveTest;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptExDataStreamIdItem34;
import com.ifoer.entity.SptInputNumric;
import com.ifoer.entity.SptInputStringEx;
import com.ifoer.entity.SptMessageBoxText;
import com.ifoer.entity.SptStreamSelectIdItem;
import com.ifoer.entity.SptTroubleTest;
import com.ifoer.entity.SptVwDataStreamIdItem;
import com.ifoer.entity.Spt_Combination_Menu;
import com.ifoer.entity.Spt_Nobuttonbox_Text;
import com.ifoer.entity.Spt_Progressbar_Box;
import com.ifoer.expedition.BluetoothOrder.ByteHexHelper;
import com.ifoer.expedition.cto.CToJava;
import com.ifoer.expedition.ndk.DynamicDepot;
import com.ifoer.expedition.ndk.StdJni;
import com.ifoer.util.Copy_File;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.NetPOSPrinter;
import com.ifoer.util.SimpleDialog;
import com.ifoer.util.SimpleDialogControl;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.util.ArrayList;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class CarDiagnoseActivityTwo extends Activity {
    private static final boolean f1235D = false;
    private final int MSG_PRINT_RESULT;
    private final int MSG_PRINT_START;
    private CarDiagnoseAdapter adapter;
    Handler baseHandler;
    Bitmap bit_first;
    Bitmap bit_second;
    Runnable bmp;
    private Builder builder;
    private Bundle bundle;
    private Context contexts;
    private int count;
    private Boolean flag;
    private GridView gridView;
    private Boolean isExecuteC;
    private long lastTime;
    private ArrayList<String> list;
    private final Handler mHandler;
    public LinearLayout menuBtn;
    private ArrayList<MenuData> menuDataList;
    private IntentFilter myIntentFilter;
    private String paths;
    Bitmap printBitmap;
    MenuData printMenuData;
    int printResult;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialogs;
    private mBroadcastReceiver receiver;
    private StringBuffer sb;
    private int site;
    private ListView view;

    /* renamed from: com.ifoer.expedition.BluetoothChat.CarDiagnoseActivityTwo.1 */
    class C03621 implements Runnable {
        C03621() {
        }

        public void run() {
            NetPOSPrinter bmpPrinter = new NetPOSPrinter(CarDiagnoseActivityTwo.this);
            CarDiagnoseActivityTwo.this.bit_first = bmpPrinter.drawBitFirst();
            CarDiagnoseActivityTwo.this.bit_second = bmpPrinter.drawBitSecond(CarDiagnoseActivityTwo.this.sb.toString());
            CarDiagnoseActivityTwo.this.printBitmap = NetPOSPrinter.mixtureBitmap(CarDiagnoseActivityTwo.this.bit_first, CarDiagnoseActivityTwo.this.bit_second);
            CarDiagnoseActivityTwo.this.printResult = bmpPrinter.printPic(CarDiagnoseActivityTwo.this.printBitmap);
            bmpPrinter.resultToast(CarDiagnoseActivityTwo.this.printResult);
            CarDiagnoseActivityTwo.this.baseHandler.obtainMessage(0, Integer.valueOf(CarDiagnoseActivityTwo.this.printResult)).sendToTarget();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.CarDiagnoseActivityTwo.2 */
    class C03632 extends Handler {
        C03632() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case InBandBytestreamManager.MAXIMUM_BLOCK_SIZE /*65535*/:
                    CarDiagnoseActivityTwo.this.dayin();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.CarDiagnoseActivityTwo.3 */
    class C03643 extends Handler {
        C03643() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    CarDiagnoseActivityTwo.this.adapter = new CarDiagnoseAdapter(CarDiagnoseActivityTwo.this.menuDataList, CarDiagnoseActivityTwo.this);
                    if (CToJava.isChexing) {
                        CarDiagnoseActivityTwo.this.gridView.setAdapter(CarDiagnoseActivityTwo.this.adapter);
                        CarDiagnoseActivityTwo.this.gridView.setSelection(CarDiagnoseActivityTwo.this.site);
                        return;
                    }
                    CarDiagnoseActivityTwo.this.view.setAdapter(CarDiagnoseActivityTwo.this.adapter);
                    CarDiagnoseActivityTwo.this.view.setSelection(CarDiagnoseActivityTwo.this.site);
                case 10101010:
                    if (CarDiagnoseActivityTwo.this.progressDialog != null && CarDiagnoseActivityTwo.this.progressDialog.isShowing()) {
                        SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    }
                    SimpleDialog.checkConectior(CarDiagnoseActivityTwo.this.contexts, CarDiagnoseActivityTwo.this.getString(C0136R.string.initializeTilte), CarDiagnoseActivityTwo.this.getString(C0136R.string.check_connector));
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.CarDiagnoseActivityTwo.4 */
    class C03654 implements OnItemClickListener {
        C03654() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            CarDiagnoseActivityTwo.this.gridView.setEnabled(false);
            Constant.feedback = null;
            Constant.feedback = ByteHexHelper.hexStringToBytes(ByteHexHelper.checkedSite(arg2) + ByteHexHelper.checkedSite(arg2));
            CToJava.activeFlag = Boolean.valueOf(false);
            CToJava.streamFlag = Boolean.valueOf(true);
            CToJava.agingFlag = Boolean.valueOf(true);
            CToJava.nowaitmessageboxtext = Boolean.valueOf(true);
            CToJava.progressbarFlag = Boolean.valueOf(true);
            if (Constant.bridge != null) {
                Constant.bridge.putData();
            }
            Constant.itemContent = ((Item) arg1.getTag()).wenzi.getText().toString();
            CarDiagnoseActivityTwo.this.adapter.setSelectedPosition(arg2);
            Constant.streamNextCode = Constant.noInterruptStreamCode;
            Constant.activeNextCode = Constant.noButton;
            Constant.continueObtain = Constant.noInterruptStreamCode;
            Constant.noWaitMessageButton = Constant.noInterruptStreamCode;
            Constant.progressbarBox = Constant.noInterruptStreamCode;
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.CarDiagnoseActivityTwo.5 */
    class C03665 implements OnItemClickListener {
        C03665() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            CarDiagnoseActivityTwo.this.view.setEnabled(false);
            Constant.feedback = null;
            Constant.feedback = ByteHexHelper.hexStringToBytes(ByteHexHelper.checkedSite(arg2) + ByteHexHelper.checkedSite(arg2));
            CToJava.activeFlag = Boolean.valueOf(false);
            CToJava.streamFlag = Boolean.valueOf(true);
            CToJava.agingFlag = Boolean.valueOf(true);
            CToJava.nowaitmessageboxtext = Boolean.valueOf(true);
            CToJava.progressbarFlag = Boolean.valueOf(true);
            if (Constant.bridge != null) {
                Constant.bridge.putData();
            }
            Constant.itemContent = ((Item) arg1.getTag()).wenzi.getText().toString();
            CarDiagnoseActivityTwo.this.adapter.setSelectedPosition(arg2);
            Constant.streamNextCode = Constant.noInterruptStreamCode;
            Constant.activeNextCode = Constant.noButton;
            Constant.continueObtain = Constant.noInterruptStreamCode;
            Constant.noWaitMessageButton = Constant.noInterruptStreamCode;
            Constant.progressbarBox = Constant.noInterruptStreamCode;
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.CarDiagnoseActivityTwo.6 */
    class C03676 implements Runnable {
        C03676() {
        }

        public void run() {
            new DynamicDepot().DiagnoseMain();
        }
    }

    class InitData extends AsyncTask<Integer, Integer, String> {
        Context context;

        public InitData(Context context) {
            this.context = context;
            if (CToJava.isChexing) {
                CarDiagnoseActivityTwo.this.view.setVisibility(8);
                CarDiagnoseActivityTwo.this.gridView.setVisibility(0);
                return;
            }
            CarDiagnoseActivityTwo.this.view.setVisibility(0);
            CarDiagnoseActivityTwo.this.gridView.setVisibility(8);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            CarDiagnoseActivityTwo.this.progressDialogs = new ProgressDialog(this.context);
            CarDiagnoseActivityTwo.this.progressDialogs.setMessage(CarDiagnoseActivityTwo.this.getResources().getString(C0136R.string.find_wait));
            CarDiagnoseActivityTwo.this.progressDialogs.setCancelable(false);
            CarDiagnoseActivityTwo.this.progressDialogs.show();
        }

        protected String doInBackground(Integer... params) {
            return null;
        }

        protected void onPostExecute(String result) {
            if (CarDiagnoseActivityTwo.this.adapter == null) {
                CarDiagnoseActivityTwo.this.adapter = new CarDiagnoseAdapter(CarDiagnoseActivityTwo.this.menuDataList, CarDiagnoseActivityTwo.this);
                if (CToJava.isChexing) {
                    CarDiagnoseActivityTwo.this.gridView.setAdapter(CarDiagnoseActivityTwo.this.adapter);
                    CarDiagnoseActivityTwo.this.gridView.setSelection(CarDiagnoseActivityTwo.this.site);
                } else {
                    CarDiagnoseActivityTwo.this.view.setAdapter(CarDiagnoseActivityTwo.this.adapter);
                    CarDiagnoseActivityTwo.this.view.setSelection(CarDiagnoseActivityTwo.this.site);
                }
            } else {
                CarDiagnoseActivityTwo.this.adapter.notifyDataSetChanged();
            }
            if (CarDiagnoseActivityTwo.this.progressDialogs != null && CarDiagnoseActivityTwo.this.progressDialogs.isShowing()) {
                CarDiagnoseActivityTwo.this.progressDialogs.dismiss();
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (CarDiagnoseActivityTwo.this.isExecuteC.booleanValue()) {
                if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                    if ((System.currentTimeMillis() - CarDiagnoseActivityTwo.this.lastTime) / 1000 > 30) {
                        SimpleDialog.ExitDialog(context);
                    }
                }
                if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                    CarDiagnoseActivityTwo.this.lastTime = System.currentTimeMillis();
                }
                if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("feedbackMeauData")) {
                    ArrayList<MenuData> menuDataLists = (ArrayList) intent.getExtras().getSerializable("menuData");
                    r0 = new Intent(CarDiagnoseActivityTwo.this, CarDiagnoseActivity.class);
                    r0.putExtra("menuData", menuDataLists);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(r0);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_DS_MENU_ID")) {
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("SPT_DS_MENU_ID");
                    r0 = new Intent(CarDiagnoseActivityTwo.this, CarDiagnoseActivity.class);
                    r0.putExtra("menuData", menuDataList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(r0);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_ACTIVE_TEST")) {
                    CarDiagnoseActivityTwo.this.isExecuteC = Boolean.valueOf(false);
                    CToJava.activeFlag = Boolean.valueOf(false);
                    SptActiveTest sptActiveTest = (SptActiveTest) intent.getExtras().getSerializable("ACTIVE_TEST");
                    Intent active = new Intent(CarDiagnoseActivityTwo.this, ActiveTestActivity.class);
                    active.putExtra("ACTIVE_TEST", sptActiveTest);
                    active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    CarDiagnoseActivityTwo.this.startActivity(active);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_NOBUTTONBOX_TEXT")) {
                    if (CarDiagnoseActivityTwo.this.progressDialog == null) {
                        CarDiagnoseActivityTwo.this.progressDialog = new ProgressDialog(CarDiagnoseActivityTwo.this.contexts);
                        CarDiagnoseActivityTwo.this.progressDialog.setCancelable(false);
                    }
                    Spt_Nobuttonbox_Text nobuttonbox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("Nobuttonbox");
                    SimpleDialog.openProgressDialog(CarDiagnoseActivityTwo.this.contexts, nobuttonbox.getTitle(), nobuttonbox.getContent(), CarDiagnoseActivityTwo.this.progressDialog);
                } else if (intent.getAction().equals("closeNobuttonBox")) {
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE")) {
                    ArrayList<SptTroubleTest> troubleTestList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE");
                    r0 = new Intent(CarDiagnoseActivityTwo.this, FaultCodeActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE", troubleTestList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(r0);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EXIT_SHOW_WINDOW")) {
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.finish();
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_STREAM_SELECT_ID_EX")) {
                    ArrayList<SptStreamSelectIdItem> streamSelectIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_STREAM_SELECT_ID_EX");
                    r0 = new Intent(CarDiagnoseActivityTwo.this, StreamSelectActivity.class);
                    r0.putExtra("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(r0);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                    ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                    r0 = new Intent(CarDiagnoseActivityTwo.this, DataStreamActivity.class);
                    r0.putExtra("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(r0);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_DATASTREAM_ID_EX")) {
                    ArrayList<SptExDataStreamIdItem34> list = (ArrayList) intent.getExtras().getSerializable("SPT_DATASTREAM_ID_EX");
                    agingIntent = new Intent(CarDiagnoseActivityTwo.this, DataStreamItemActivity.class);
                    bundle = new Bundle();
                    bundle.putSerializable("SPT_DATASTREAM_ID_EX", list);
                    agingIntent.putExtras(bundle);
                    agingIntent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(agingIntent);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_MESSAGEBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) intent.getExtras().getSerializable("SPT_MESSAGEBOX_TEXT");
                    int dialogType = sptMessageBoxText.getDialogType();
                    SimpleDialogControl.showDiaglog(CarDiagnoseActivityTwo.this.contexts, dialogType, sptMessageBoxText);
                } else if (intent.getAction().equals("ConnectionLost")) {
                    Toast.makeText(CarDiagnoseActivityTwo.this.getApplicationContext(), CarDiagnoseActivityTwo.this.getResources().getString(C0136R.string.devlost), 0).show();
                } else if (intent.getAction().equals("SPT_INPUTSTRING_EX")) {
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    SptInputStringEx inputString = (SptInputStringEx) intent.getExtras().getSerializable("SPT_INPUTSTRING_EX");
                    SimpleDialog.sptInputStringExDiagnose(CarDiagnoseActivityTwo.this.contexts, inputString.getDialogTitle(), inputString.getDialogContent(), inputString.getInputHint());
                } else if (intent.getAction().equals("SPT_INPUT_NUMERIC")) {
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    SptInputNumric inputNumric = (SptInputNumric) intent.getExtras().getSerializable("SPT_INPUT_NUMERIC");
                    SimpleDialog.sptInputNumericDiagnose(CarDiagnoseActivityTwo.this.contexts, inputNumric.getDialogTitle(), inputNumric.getDialogContent(), inputNumric.getInputHint(), inputNumric.getDigit());
                } else if (intent.getAction().equals("SPT_INPUTBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    Spt_Nobuttonbox_Text inputBox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTBOX_TEXT");
                    SimpleDialog.sptInputBoxTextDiagnose(CarDiagnoseActivityTwo.this.contexts, inputBox.getTitle(), inputBox.getContent());
                } else if (intent.getAction().equals("SPT_INPUTSTRING")) {
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    Spt_Nobuttonbox_Text inputStr = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTSTRING");
                    SimpleDialog.sptInputStringDiagnose(CarDiagnoseActivityTwo.this.contexts, inputStr.getTitle(), inputStr.getContent());
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN")) {
                    troubleCodeFrozenList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN");
                    r0 = new Intent(CarDiagnoseActivityTwo.this, FaultCodeFrozenActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(r0);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                    ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                    r0 = new Intent(CarDiagnoseActivityTwo.this, VWDataStreamActivity.class);
                    r0.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(r0);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_SHOW_PICTURE")) {
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    String pictureName = intent.getExtras().getString("SPT_SHOW_PICTURE");
                    SimpleDialog.sptShowPictureDiagnose(CarDiagnoseActivityTwo.this.contexts, pictureName);
                } else if (intent.getAction().equals("SPT_AGING_WINDOW")) {
                    String agingContent = intent.getExtras().getString("SPT_AGING_WINDOW");
                    agingIntent = new Intent(CarDiagnoseActivityTwo.this, AgingWindowActivity.class);
                    agingIntent.putExtra("SPT_AGING_WINDOW", agingContent);
                    agingIntent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(agingIntent);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_SPECIAL_FUNCTION_ID")) {
                    ArrayList<SpecialFunction> list2 = (ArrayList) intent.getExtras().getSerializable("SPT_SPECIAL_FUNCTION_ID");
                    agingIntent = new Intent(CarDiagnoseActivityTwo.this, SpecialFunctionActivity.class);
                    bundle = new Bundle();
                    bundle.putSerializable("SPT_SPECIAL_FUNCTION_ID", list2);
                    agingIntent.putExtras(bundle);
                    agingIntent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(agingIntent);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_ID_EX")) {
                    ArrayList<SptTroubleTest> troubleCodeFrozenIdList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_ID_EX");
                    r0 = new Intent(CarDiagnoseActivityTwo.this, FaultCodeByIdActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_ID_EX", troubleCodeFrozenIdList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(r0);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN_EX")) {
                    troubleCodeFrozenList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN_EX");
                    r0 = new Intent(CarDiagnoseActivityTwo.this, FaultCodeByFrozenActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_FROZEN_EX", troubleCodeFrozenList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(r0);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_ID_BY_COMBINE")) {
                    troublebyCombineList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_ID_BY_COMBINE");
                    r0 = new Intent(CarDiagnoseActivityTwo.this, FaultCodeByCombineActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_ID_BY_COMBINE", troublebyCombineList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(r0);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_ID_EX_BY_COMBINE")) {
                    troublebyCombineList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_ID_EX_BY_COMBINE");
                    r0 = new Intent(CarDiagnoseActivityTwo.this, FaultCodeExByCombineActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_ID_EX_BY_COMBINE", troublebyCombineList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(r0);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN_BY_COMBINE")) {
                    troubleCodeFrozenByCombineList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN_BY_COMBINE");
                    r0 = new Intent(CarDiagnoseActivityTwo.this, FaultCodeFrozenByCombineActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_FROZEN_BY_COMBINE", troubleCodeFrozenByCombineList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(r0);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN_EX_BY_COMBINE")) {
                    troubleCodeFrozenByCombineList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN_EX_BY_COMBINE");
                    r0 = new Intent(CarDiagnoseActivityTwo.this, FaultCodeFrozenExByCombineActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_FROZEN_EX_BY_COMBINE", troubleCodeFrozenByCombineList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(r0);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_ID_BY_LOOPMODE")) {
                    troubleCodeFrozenByCombineList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_ID_BY_LOOPMODE");
                    r0 = new Intent(CarDiagnoseActivityTwo.this, FaultCodeByLoopModeActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_ID_BY_LOOPMODE", troubleCodeFrozenByCombineList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(r0);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_COMBINATION_MENU")) {
                    Spt_Combination_Menu agingContent2 = (Spt_Combination_Menu) intent.getExtras().getSerializable("SPT_COMBINATION_MENU");
                    agingIntent = new Intent(CarDiagnoseActivityTwo.this, CombinationActivity.class);
                    agingIntent.putExtra("SPT_COMBINATION_MENU", agingContent2);
                    agingIntent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(agingIntent);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_NOWAITMESSAGEBOX_TEXT")) {
                    CarDiagnoseActivityTwo.this.isExecuteC = Boolean.valueOf(false);
                    String[] nowaitmessagebox = intent.getExtras().getStringArray("SPT_NOWAITMESSAGEBOX_TEXT");
                    r0 = new Intent(CarDiagnoseActivityTwo.this, ShowNoWaiteMessageBox.class);
                    Bundle bundleNowaitmessagebox = new Bundle();
                    bundleNowaitmessagebox.putStringArray("SPT_NOWAITMESSAGEBOX_TEXT", nowaitmessagebox);
                    r0.putExtras(bundleNowaitmessagebox);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    CarDiagnoseActivityTwo.this.startActivity(r0);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_PROGRESSBAR_BOX")) {
                    Spt_Progressbar_Box spb = (Spt_Progressbar_Box) intent.getExtras().getSerializable("SPT_PROGRESSBAR_BOX");
                    r0 = new Intent(CarDiagnoseActivityTwo.this, ShowProgressBarActivity.class);
                    Bundle bundleProgerssbar = new Bundle();
                    bundleProgerssbar.putSerializable("SPT_PROGRESSBAR_BOX", spb);
                    bundleProgerssbar.putInt(SharedPref.TYPE, 45);
                    r0.putExtras(bundleProgerssbar);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    CarDiagnoseActivityTwo.this.startActivity(r0);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_FILE_MENU")) {
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("SPT_FILE_MENU");
                    r0 = new Intent(CarDiagnoseActivityTwo.this, CarDiagnoseActivity.class);
                    r0.putExtra("menuData", menuDataList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(CarDiagnoseActivityTwo.this.progressDialog);
                    CarDiagnoseActivityTwo.this.startActivity(r0);
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                    CarDiagnoseActivityTwo.this.finish();
                    CarDiagnoseActivityTwo.this.overridePendingTransition(0, 0);
                }
            }
        }
    }

    public CarDiagnoseActivityTwo() {
        this.menuDataList = new ArrayList();
        this.list = new ArrayList();
        this.isExecuteC = Boolean.valueOf(false);
        this.paths = XmlPullParser.NO_NAMESPACE;
        this.builder = null;
        this.flag = Boolean.valueOf(false);
        this.MSG_PRINT_RESULT = 0;
        this.MSG_PRINT_START = InBandBytestreamManager.MAXIMUM_BLOCK_SIZE;
        this.sb = null;
        this.count = 0;
        this.bmp = new C03621();
        this.baseHandler = new C03632();
        this.mHandler = new C03643();
    }

    public void dayin() {
        getPrintDate();
        new Thread(this.bmp).start();
    }

    private void getPrintDate() {
        this.sb = new StringBuffer();
        for (int i = 0; i < this.menuDataList.size(); i++) {
            this.printMenuData = (MenuData) this.menuDataList.get(i);
            this.sb.append(new StringBuilder(String.valueOf(this.printMenuData.getMenuContent())).append(SpecilApiUtil.LINE_SEP).toString());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        this.contexts = this;
        setContentView(C0136R.layout.car_diagnose);
        MyApplication.getInstance().addActivity(this);
        initView();
        registerBoradcastReceiver();
        BluetoothChatService.setHandler(this.mHandler);
    }

    private void initView() {
        if (Constant.hasSetHigh && !Constant.isLANDROVER_DEMO) {
            Constant.hasSetHigh = false;
            if (MySharedPreferences.getBooleanValue(this.contexts, Constants.isSanMu, false)) {
                MainActivity.SanMusetLow(this.contexts);
            } else {
                MainActivity.setLow_OFF();
            }
        }
        this.gridView = (GridView) findViewById(C0136R.id.gridView);
        this.gridView.setHorizontalSpacing(15);
        this.gridView.setVerticalSpacing(15);
        this.gridView.setSelector(C0136R.color.transparent);
        this.gridView.setNumColumns(3);
        this.view = (ListView) findViewById(C0136R.id.view);
        this.gridView.setOnItemClickListener(new C03654());
        this.view = (ListView) findViewById(C0136R.id.view);
        this.view.setOnItemClickListener(new C03665());
        this.bundle = getIntent().getExtras();
        if (this.bundle != null) {
            this.menuDataList = (ArrayList) this.bundle.getSerializable("menuData");
        }
        if (this.menuDataList != null && this.menuDataList.size() > 0) {
            this.site = ((MenuData) this.menuDataList.get(0)).getSite();
            this.menuDataList.remove(0);
            this.menuDataList.remove(0);
            new InitData(this).execute(new Integer[0]);
        }
        this.flag = Boolean.valueOf(true);
    }

    public void loadSo(String sdPaths) {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this.contexts);
            this.progressDialog.setCancelable(false);
        } else {
            this.progressDialog.dismiss();
            this.progressDialog = null;
            this.progressDialog = new ProgressDialog(this.contexts);
            this.progressDialog.setCancelable(false);
        }
        SimpleDialog.openProgressDialog(this.contexts, getResources().getString(C0136R.string.initializeTilte), getResources().getString(C0136R.string.initializeMessage), this.progressDialog);
        if (Copy_File.list.size() > 0) {
            int i;
            for (i = 0; i < Copy_File.list.size(); i++) {
                if (((String) Copy_File.list.get(i)).contains("libCOMM_ABSTRACT_LAYER")) {
                    System.load((String) Copy_File.list.get(i));
                    int site = ((String) Copy_File.list.get(i)).lastIndexOf(FilePathGenerator.ANDROID_DIR_SEP);
                    break;
                }
            }
            for (i = 0; i < Copy_File.list.size(); i++) {
                if (((String) Copy_File.list.get(i)).contains("libSTD")) {
                    System.load((String) Copy_File.list.get(i));
                    break;
                }
            }
            for (i = 0; i < Copy_File.list.size(); i++) {
                if (((String) Copy_File.list.get(i)).contains("libDIAG")) {
                    System.load((String) Copy_File.list.get(i));
                    break;
                }
            }
            i = 0;
            while (i < Copy_File.list.size()) {
                if (!(((String) Copy_File.list.get(i)).contains("libCOMM_ABSTRACT_LAYER") || ((String) Copy_File.list.get(i)).contains("libSTD") || ((String) Copy_File.list.get(i)).contains("libDIAG"))) {
                    System.load((String) Copy_File.list.get(i));
                }
                i++;
            }
        }
        new StdJni().setCallbackEnv(sdPaths);
        Handler handler = new Handler();
        new Thread(new C03676()).start();
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
        this.myIntentFilter.addAction("SPT_DATASTREAM_ID_EX");
        this.myIntentFilter.addAction("SPT_SPECIAL_FUNCTION_ID");
        this.myIntentFilter.addAction("SPT_TROUBLE_CODE_ID_EX");
        this.myIntentFilter.addAction("SPT_TROUBLE_CODE_FROZEN_EX");
        this.myIntentFilter.addAction("SPT_TROUBLE_CODE_ID_BY_COMBINE");
        this.myIntentFilter.addAction("SPT_TROUBLE_CODE_FROZEN_BY_COMBINE");
        this.myIntentFilter.addAction("SPT_TROUBLE_CODE_ID_EX_BY_COMBINE");
        this.myIntentFilter.addAction("SPT_TROUBLE_CODE_FROZEN_EX_BY_COMBINE");
        this.myIntentFilter.addAction("SPT_TROUBLE_CODE_ID_BY_LOOPMODE");
        this.myIntentFilter.addAction("SPT_AGING_WINDOW");
        this.myIntentFilter.addAction("SPT_COMBINATION_MENU");
        this.myIntentFilter.addAction("SPT_NOWAITMESSAGEBOX_TEXT");
        this.myIntentFilter.addAction("SPT_PROGRESSBAR_BOX");
        this.myIntentFilter.addAction("SPT_FILE_MENU");
        this.myIntentFilter.addAction("SPT_HISTORY_RECORD_ID");
        registerReceiver(this.receiver, this.myIntentFilter);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 && keyCode != Service.SUNRPC) {
            return super.onKeyDown(keyCode, event);
        }
        if ((Constant.mChatService == null || Constant.mChatService.getState() != 3) && !Constant.isDemo) {
            finish();
            overridePendingTransition(0, 0);
        } else {
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
            Constant.feedback = null;
            Constant.feedback = Constant.previousMenu;
            if (Constant.bridge != null) {
                Constant.bridge.putData();
            }
        }
        return true;
    }
}

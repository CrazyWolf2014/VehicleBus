package com.ifoer.expedition.BluetoothChat;

import CRP.utils.CRPTools;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.os.SystemClock;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.mycar.jni.JniX431FileTest;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.ifoer.adapter.DataStreamAdapter;
import com.ifoer.adapter.DataStreamAdapter.Item;
import com.ifoer.db.DBDao;
import com.ifoer.entity.BadgeView;
import com.ifoer.entity.Constant;
import com.ifoer.entity.DataStream;
import com.ifoer.entity.IntData;
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
import com.ifoer.expeditionphone.DiagnoseDetailActivity;
import com.ifoer.expeditionphone.DiagnoseReportActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.expeditionphone.ShowFileActivity;
import com.ifoer.expeditionphone.WelcomeActivity;
import com.ifoer.freememory.FreeMemory;
import com.ifoer.mine.Contact;
import com.ifoer.util.DataStreamTask;
import com.ifoer.util.DataStreamTaskManager;
import com.ifoer.util.DataStreamTaskManagerThread;
import com.ifoer.util.Files;
import com.ifoer.util.GraphView;
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
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class DataStreamActivity extends Activity implements OnClickListener {
    private static final boolean f1238D = true;
    private final int MSG_PRINT_RESULT_SCREEN_SHOT;
    private final int MSG_PRINT_START;
    private DataStreamAdapter adapter;
    Handler baseHandler;
    private boolean basicFlag;
    Bitmap bit_first;
    Bitmap bit_second;
    Bitmap bitmap1;
    private Chronometer btn_chronometer;
    private Button btn_jilu;
    private Bundle bundle;
    private BadgeView bv;
    private String cc;
    private Context contexts;
    private int currentCheckedItem;
    DBDao dao;
    private ArrayList<DataStream> dataStreamList;
    private Button date;
    public LinearLayout dayinBtn;
    private String diagversion;
    private Dialog dialog;
    private SptExDataStreamIdItem exDataStreamIdItem;
    private ArrayList<SptExDataStreamIdItem> exDataStreamIdlist;
    private String fileDir;
    private int fileId;
    private Boolean flag;
    private Boolean flag2;
    private GraphView graphView;
    private int graphicNum;
    private int grp;
    private int hlsx;
    private ArrayList<IntData> intDataForItemSelected;
    private Boolean isExecuteD;
    private boolean isReceiver;
    private boolean isRecord;
    private boolean isShowDialog;
    private Button jietu;
    private JniX431FileTest jnitest;
    private int f1239k;
    private String language;
    private long lastTime;
    private List<ArrayList<?>> lists;
    private ListView listview;
    private ArrayList<ArrayList<SptExDataStreamIdItem>> llist;
    private final Handler mHandler;
    private RelativeLayout main_head;
    public LinearLayout menuBtn;
    private IntentFilter myIntentFilter;
    private String name;
    private boolean openFlag;
    private String pathTxt;
    int printResult;
    private ProgressDialog progressDialog;
    private mBroadcastReceiver receiver;
    private StringBuffer sb;
    Runnable screenBmp;
    private String sdCardDir;
    private String serialNo;
    private String softPackageId;
    private ImageView startButton;
    private DataStreamTaskManager taskManager;
    private Timer timer;
    private double times;
    private final int totleNum;
    private String verLocal;
    private Button wenzi;
    private String x431fileName;

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamActivity.1 */
    class C03781 implements Runnable {
        C03781() {
        }

        public void run() {
            NetPOSPrinter bmpPrinter = new NetPOSPrinter(DataStreamActivity.this);
            DataStreamActivity.this.bit_first = bmpPrinter.drawBitFirst();
            DataStreamActivity.this.bit_second = bmpPrinter.drawBitSecond(DataStreamActivity.this.sb.toString());
            DataStreamActivity.this.bitmap1 = NetPOSPrinter.mixtureBitmap(DataStreamActivity.this.bit_first, DataStreamActivity.this.bit_second);
            DataStreamActivity.this.printResult = bmpPrinter.printPic(DataStreamActivity.this.bitmap1);
            bmpPrinter.resultToast(DataStreamActivity.this.printResult);
            DataStreamActivity.this.baseHandler.obtainMessage(0, Integer.valueOf(DataStreamActivity.this.printResult)).sendToTarget();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamActivity.2 */
    class C03792 extends Handler {
        C03792() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    DataStreamActivity.this.dayinBtn.setClickable(DataStreamActivity.f1238D);
                case InBandBytestreamManager.MAXIMUM_BLOCK_SIZE /*65535*/:
                    DataStreamActivity.this.dayin();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamActivity.3 */
    class C03803 extends Handler {
        C03803() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    if (DataStreamActivity.this.flag.booleanValue() && DataStreamActivity.this.flag2.booleanValue() && DataStreamActivity.this.adapter != null && DataStreamActivity.this.listview != null && DataStreamActivity.this.listview.getAdapter() != null) {
                        DataStreamActivity.this.adapter.refresh(DataStreamActivity.this.exDataStreamIdlist);
                        DataStreamActivity.this.llist.add(DataStreamActivity.this.exDataStreamIdlist);
                        DataStreamActivity.this.taskManager.addDownloadTask(new DataStreamTask(DataStreamActivity.this.contexts, DataStreamActivity.this.exDataStreamIdlist, DataStreamActivity.this.lists, DataStreamActivity.this.jnitest, DataStreamActivity.this.grp, DataStreamActivity.this.openFlag, DataStreamActivity.this.basicFlag, DataStreamActivity.this.isReceiver, DataStreamActivity.this.isRecord, DataStreamActivity.this.mHandler));
                    }
                case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                    DataStreamActivity.this.openFlag = false;
                    DataStreamActivity.this.basicFlag = false;
                    Constant.bBasicFlag = false;
                    Constant.bOpenFlag = false;
                case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
                    Constant.needWait = false;
                    DataStreamActivity.this.openFlag = DataStreamActivity.f1238D;
                    DataStreamActivity.this.basicFlag = DataStreamActivity.f1238D;
                    Constant.bBasicFlag = DataStreamActivity.f1238D;
                    Constant.bOpenFlag = DataStreamActivity.f1238D;
                case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                    DataStreamActivity dataStreamActivity = DataStreamActivity.this;
                    dataStreamActivity.times = dataStreamActivity.times + 1.0d;
                    if (DataStreamActivity.this.dialog != null && DataStreamActivity.this.dialog.isShowing()) {
                        DataStreamActivity.this.graphView.pushDataToChart(DataStreamActivity.this.lists, DataStreamActivity.this.times, DataStreamActivity.this.currentCheckedItem);
                    }
                case WelcomeActivity.GPIO_IOCQDATAOUT /*19*/:
                    DataStreamActivity.this.btn_jilu.setBackgroundResource(C0136R.drawable.record_bai_selector);
                    DataStreamActivity.this.btn_jilu.setClickable(DataStreamActivity.f1238D);
                    DataStreamActivity.this.startButton.setImageResource(C0136R.drawable.right_stop_focuses);
                    DataStreamActivity.this.startButton.setClickable(DataStreamActivity.f1238D);
                case Constant.GRAPHIC_ICON_NUM_CHANGE /*911*/:
                    if (DataStreamActivity.this.graphicNum <= 0 || DataStreamActivity.this.date.getVisibility() != 0) {
                        DataStreamActivity.this.bv.setVisibility(8);
                        return;
                    }
                    DataStreamActivity.this.bv.setText(String.valueOf(DataStreamActivity.this.graphicNum));
                    DataStreamActivity.this.bv.setGravity(1);
                    DataStreamActivity.this.bv.setTextSize(12.0f);
                    DataStreamActivity.this.bv.setBadgePosition(2);
                    DataStreamActivity.this.bv.setBackgroundDrawable(DataStreamActivity.this.getResources().getDrawable(C0136R.drawable.data_stream_point));
                    DataStreamActivity.this.bv.show();
                case 10101010:
                    if (DataStreamActivity.this.progressDialog != null && DataStreamActivity.this.progressDialog.isShowing()) {
                        SimpleDialog.closeProgressDialog(DataStreamActivity.this.progressDialog);
                    }
                    SimpleDialog.checkConectior(DataStreamActivity.this.contexts, DataStreamActivity.this.getString(C0136R.string.initializeTilte), DataStreamActivity.this.getString(C0136R.string.check_connector));
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamActivity.4 */
    class C03814 implements OnItemClickListener {
        C03814() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            if (DataStreamActivity.this.date.getVisibility() == 0) {
                Item holder = (Item) arg1.getTag();
                SptExDataStreamIdItem exDataStreamIdItemtemp = (SptExDataStreamIdItem) DataStreamActivity.this.exDataStreamIdlist.get(arg2);
                if (XmlPullParser.NO_NAMESPACE.equals(exDataStreamIdItemtemp.getStreamState()) || !DataStreamUtils.isNumeric(exDataStreamIdItemtemp.getStreamStr())) {
                    Toast.makeText(DataStreamActivity.this, DataStreamActivity.this.getResources().getString(C0136R.string.data_stream_item_select_show_info), 0).show();
                    return;
                }
                holder.flagIcon.toggle();
                DataStreamActivity dataStreamActivity;
                if (holder.flagIcon.isChecked()) {
                    if (DataStreamActivity.this.graphicNum > 3) {
                        Toast.makeText(DataStreamActivity.this, DataStreamActivity.this.getResources().getString(C0136R.string.data_stream_max_item_info_scanPad_mini), 0).show();
                        holder.flagIcon.setChecked(false);
                        return;
                    }
                    dataStreamActivity = DataStreamActivity.this;
                    dataStreamActivity.graphicNum = dataStreamActivity.graphicNum + 1;
                } else if (DataStreamActivity.this.graphicNum > 0) {
                    dataStreamActivity = DataStreamActivity.this;
                    dataStreamActivity.graphicNum = dataStreamActivity.graphicNum - 1;
                }
                DataStreamAdapter.getIsSelected().put(Integer.valueOf(arg2), Boolean.valueOf(holder.flagIcon.isChecked()));
                DataStreamActivity.this.mHandler.sendEmptyMessage(Constant.GRAPHIC_ICON_NUM_CHANGE);
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamActivity.5 */
    class C03825 extends Thread {
        C03825() {
        }

        public void run() {
            try {
                C03825.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            DataStreamActivity.this.mHandler.sendEmptyMessage(19);
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamActivity.6 */
    class C03836 implements DialogInterface.OnClickListener {
        private final /* synthetic */ String val$path;

        C03836(String str) {
            this.val$path = str;
        }

        public void onClick(DialogInterface dialog, int which) {
            DataStreamActivity.this.isShowDialog = DataStreamActivity.f1238D;
            if (TextUtils.isEmpty(DataStreamActivity.this.cc)) {
                DataStreamActivity.this.toSeeReportList();
            } else if (DBDao.getInstance(DataStreamActivity.this.contexts).queryReport(DataStreamActivity.this.cc, Contact.RELATION_ASK, MainActivity.database).size() < 1) {
                DataStreamActivity.this.toSeeReportOne(this.val$path);
            } else {
                DataStreamActivity.this.toSeeReportList();
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamActivity.7 */
    class C03847 implements DialogInterface.OnClickListener {
        C03847() {
        }

        public void onClick(DialogInterface dialog, int which) {
            DataStreamActivity.this.isShowDialog = DataStreamActivity.f1238D;
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {

        /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamActivity.mBroadcastReceiver.1 */
        class C03851 extends Thread {
            C03851() {
            }

            public void run() {
                DataStreamActivity.this.mHandler.sendMessage(DataStreamActivity.this.mHandler.obtainMessage(15));
            }
        }

        /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamActivity.mBroadcastReceiver.2 */
        class C03862 extends Thread {
            C03862() {
            }

            public void run() {
                try {
                    C03862.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Process.killProcess(Process.myPid());
                DataStreamActivity.this.finish();
                DataStreamActivity.this.overridePendingTransition(0, 0);
            }
        }

        /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamActivity.mBroadcastReceiver.3 */
        class C03873 extends Thread {
            C03873() {
            }

            public void run() {
                try {
                    C03873.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Process.killProcess(Process.myPid());
                DataStreamActivity.this.finish();
                DataStreamActivity.this.overridePendingTransition(0, 0);
            }
        }

        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - DataStreamActivity.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                DataStreamActivity.this.lastTime = System.currentTimeMillis();
            }
            if (!intent.getAction().equals("DataStreamSelectSite") && intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                SimpleDialog.closeProgressDialog(DataStreamActivity.this.progressDialog);
                DataStreamActivity.this.exDataStreamIdlist = null;
                DataStreamActivity.this.exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                new C03851().start();
            }
            if (intent.getAction().equals("CLOSE_DATASTREAM_ACTIVITY")) {
                new C03862().start();
            }
            if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                SimpleDialog.closeProgressDialog(DataStreamActivity.this.progressDialog);
                if (Constant.mChatService != null) {
                    Constant.mChatService.stop();
                }
                Toast.makeText(DataStreamActivity.this, DataStreamActivity.this.getResources().getString(C0136R.string.connectionLost), 0).show();
                new C03873().start();
            }
            if (!DataStreamActivity.this.isExecuteD.booleanValue()) {
                return;
            }
            ArrayList<MenuData> menuDataList;
            Intent intent2;
            if (intent.getAction().equals("feedbackMeauData")) {
                DataStreamActivity.this.timer.cancel();
                CToJava.streamFlag = Boolean.valueOf(false);
                menuDataList = (ArrayList) intent.getExtras().getSerializable("menuData");
                intent2 = new Intent(DataStreamActivity.this, CarDiagnoseActivity.class);
                intent2.putExtra("menuData", menuDataList);
                intent2.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(DataStreamActivity.this.progressDialog);
                DataStreamActivity.this.startActivity(intent2);
                DataStreamActivity.this.overridePendingTransition(0, 0);
                DataStreamActivity.this.finish();
                DataStreamActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_DS_MENU_ID")) {
                DataStreamActivity.this.timer.cancel();
                CToJava.streamFlag = Boolean.valueOf(false);
                menuDataList = (ArrayList) intent.getExtras().getSerializable("SPT_DS_MENU_ID");
                intent2 = new Intent(DataStreamActivity.this, CarDiagnoseActivity.class);
                intent2.putExtra("menuData", menuDataList);
                intent2.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(DataStreamActivity.this.progressDialog);
                DataStreamActivity.this.startActivity(intent2);
                DataStreamActivity.this.overridePendingTransition(0, 0);
                DataStreamActivity.this.finish();
                DataStreamActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_ACTIVE_TEST")) {
                DataStreamActivity.this.timer.cancel();
                CToJava.streamFlag = Boolean.valueOf(false);
                SptActiveTest sptActiveTest = (SptActiveTest) intent.getExtras().getSerializable("ACTIVE_TEST");
                Intent active = new Intent(DataStreamActivity.this, ActiveTestActivity.class);
                active.putExtra("ACTIVE_TEST", sptActiveTest);
                active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(DataStreamActivity.this.progressDialog);
                DataStreamActivity.this.startActivity(active);
                DataStreamActivity.this.overridePendingTransition(0, 0);
                DataStreamActivity.this.finish();
                DataStreamActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_NOBUTTONBOX_TEXT")) {
                if (DataStreamActivity.this.progressDialog == null) {
                    DataStreamActivity.this.progressDialog = new ProgressDialog(DataStreamActivity.this.contexts);
                    DataStreamActivity.this.progressDialog.setCancelable(false);
                } else {
                    DataStreamActivity.this.progressDialog.dismiss();
                    DataStreamActivity.this.progressDialog = null;
                    DataStreamActivity.this.progressDialog = new ProgressDialog(DataStreamActivity.this.contexts);
                    DataStreamActivity.this.progressDialog.setCancelable(false);
                }
                Spt_Nobuttonbox_Text nobuttonbox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("Nobuttonbox");
                SimpleDialog.openProgressDialog(DataStreamActivity.this.contexts, nobuttonbox.getTitle(), nobuttonbox.getContent(), DataStreamActivity.this.progressDialog);
            } else if (intent.getAction().equals("closeNobuttonBox")) {
                SimpleDialog.closeProgressDialog(DataStreamActivity.this.progressDialog);
            } else if (intent.getAction().equals("SPT_TROUBLE_CODE")) {
                DataStreamActivity.this.timer.cancel();
                CToJava.streamFlag = Boolean.valueOf(false);
                ArrayList<SptTroubleTest> troubleTestList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE");
                intent2 = new Intent(DataStreamActivity.this, FaultCodeActivity.class);
                intent2.putExtra("SPT_TROUBLE_CODE", troubleTestList);
                intent2.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(DataStreamActivity.this.progressDialog);
                DataStreamActivity.this.startActivity(intent2);
                DataStreamActivity.this.overridePendingTransition(0, 0);
                DataStreamActivity.this.finish();
                DataStreamActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_EXIT_SHOW_WINDOW")) {
                if (DataStreamActivity.this.openFlag) {
                    String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    DataStreamActivity.this.openFlag = false;
                    Constant.bOpenFlag = false;
                    DataStreamActivity.this.jnitest.writeEndCloseFile(DataStreamActivity.this.grp, date, DataStreamActivity.this.fileId, DataStreamActivity.this.hlsx, DataStreamActivity.this.x431fileName);
                    String reportPath = new StringBuilder(String.valueOf(DataStreamActivity.this.fileDir)).append(DataStreamActivity.this.x431fileName).toString();
                    File file = new File(new StringBuilder(String.valueOf(DataStreamActivity.this.fileDir)).append(DataStreamActivity.this.x431fileName).toString());
                    if (file.length() <= 0) {
                        file.delete();
                        Toast.makeText(DataStreamActivity.this.contexts, DataStreamActivity.this.contexts.getString(C0136R.string.fail_creat_file), 0).show();
                        Toast.makeText(DataStreamActivity.this.contexts, DataStreamActivity.this.contexts.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
                    } else {
                        DBDao.getInstance(DataStreamActivity.this.contexts).addReport(DataStreamActivity.this.x431fileName, date, DataStreamActivity.this.serialNo, reportPath, Contact.RELATION_FRIEND, MainActivity.database);
                    }
                }
                DataStreamActivity.this.timer.cancel();
                CToJava.streamFlag = Boolean.valueOf(false);
                if (Constant.mChatService != null) {
                    Constant.mChatService.stop();
                }
                SimpleDialog.closeProgressDialog(DataStreamActivity.this.progressDialog);
                DataStreamActivity.this.finish();
                DataStreamActivity.this.overridePendingTransition(0, 0);
                Process.killProcess(Process.myPid());
                System.exit(0);
            } else if (intent.getAction().equals("SPT_STREAM_SELECT_ID_EX")) {
                DataStreamActivity.this.timer.cancel();
                CToJava.streamFlag = Boolean.valueOf(false);
                ArrayList<SptStreamSelectIdItem> streamSelectIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_STREAM_SELECT_ID_EX");
                intent2 = new Intent(DataStreamActivity.this, StreamSelectActivity.class);
                intent2.putExtra("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                intent2.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(DataStreamActivity.this.progressDialog);
                DataStreamActivity.this.startActivity(intent2);
                DataStreamActivity.this.overridePendingTransition(0, 0);
                DataStreamActivity.this.finish();
                DataStreamActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_MESSAGEBOX_TEXT")) {
                SimpleDialog.closeProgressDialog(DataStreamActivity.this.progressDialog);
                SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) intent.getExtras().getSerializable("SPT_MESSAGEBOX_TEXT");
                SimpleDialogControl.showDiaglog(DataStreamActivity.this.contexts, sptMessageBoxText.getDialogType(), sptMessageBoxText);
            } else if (intent.getAction().equals("ConnectionLost")) {
                Toast.makeText(DataStreamActivity.this, "Device connection was lost", 0).show();
            } else if (intent.getAction().equals("SPT_INPUTSTRING_EX")) {
                SimpleDialog.closeProgressDialog(DataStreamActivity.this.progressDialog);
                SptInputStringEx inputString = (SptInputStringEx) intent.getExtras().getSerializable("SPT_INPUTSTRING_EX");
                SimpleDialog.sptInputStringExDiagnose(DataStreamActivity.this.contexts, inputString.getDialogTitle(), inputString.getDialogContent(), inputString.getInputHint());
            } else if (intent.getAction().equals("SPT_INPUT_NUMERIC")) {
                SimpleDialog.closeProgressDialog(DataStreamActivity.this.progressDialog);
                SptInputNumric inputNumric = (SptInputNumric) intent.getExtras().getSerializable("SPT_INPUT_NUMERIC");
                SimpleDialog.sptInputNumericDiagnose(DataStreamActivity.this.contexts, inputNumric.getDialogTitle(), inputNumric.getDialogContent(), inputNumric.getInputHint(), inputNumric.getDigit());
            } else if (intent.getAction().equals("SPT_INPUTBOX_TEXT")) {
                SimpleDialog.closeProgressDialog(DataStreamActivity.this.progressDialog);
                Spt_Nobuttonbox_Text inputBox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTBOX_TEXT");
                SimpleDialog.sptInputBoxTextDiagnose(DataStreamActivity.this.contexts, inputBox.getTitle(), inputBox.getContent());
            } else if (intent.getAction().equals("SPT_INPUTSTRING")) {
                SimpleDialog.closeProgressDialog(DataStreamActivity.this.progressDialog);
                Spt_Nobuttonbox_Text inputStr = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTSTRING");
                SimpleDialog.sptInputStringDiagnose(DataStreamActivity.this.contexts, inputStr.getTitle(), inputStr.getContent());
            } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN")) {
                DataStreamActivity.this.timer.cancel();
                CToJava.streamFlag = Boolean.valueOf(false);
                ArrayList<SptTroubleTest> troubleCodeFrozenList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN");
                intent2 = new Intent(DataStreamActivity.this, FaultCodeFrozenActivity.class);
                intent2.putExtra("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                intent2.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(DataStreamActivity.this.progressDialog);
                DataStreamActivity.this.startActivity(intent2);
                DataStreamActivity.this.overridePendingTransition(0, 0);
                DataStreamActivity.this.finish();
                DataStreamActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                DataStreamActivity.this.timer.cancel();
                CToJava.streamFlag = Boolean.valueOf(false);
                ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                intent2 = new Intent(DataStreamActivity.this, VWDataStreamActivity.class);
                intent2.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                intent2.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(DataStreamActivity.this.progressDialog);
                DataStreamActivity.this.startActivity(intent2);
                DataStreamActivity.this.overridePendingTransition(0, 0);
                DataStreamActivity.this.finish();
                DataStreamActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_SHOW_PICTURE")) {
                SimpleDialog.closeProgressDialog(DataStreamActivity.this.progressDialog);
                SimpleDialog.sptShowPictureDiagnose(DataStreamActivity.this.contexts, intent.getExtras().getString("SPT_SHOW_PICTURE"));
            }
        }
    }

    public DataStreamActivity() {
        this.exDataStreamIdlist = new ArrayList();
        this.dataStreamList = new ArrayList();
        this.exDataStreamIdItem = null;
        this.llist = new ArrayList();
        this.sb = null;
        this.f1239k = 1;
        this.dao = DBDao.getInstance(this);
        this.isExecuteD = Boolean.valueOf(false);
        this.timer = new Timer();
        this.flag = Boolean.valueOf(false);
        this.flag2 = Boolean.valueOf(false);
        this.lists = new ArrayList();
        this.jnitest = new JniX431FileTest();
        this.openFlag = false;
        this.basicFlag = false;
        this.isReceiver = f1238D;
        this.isRecord = false;
        this.fileDir = XmlPullParser.NO_NAMESPACE;
        this.dialog = null;
        this.currentCheckedItem = 0;
        this.times = 0.0d;
        this.graphView = null;
        this.isShowDialog = f1238D;
        this.bitmap1 = null;
        this.MSG_PRINT_RESULT_SCREEN_SHOT = 0;
        this.MSG_PRINT_START = InBandBytestreamManager.MAXIMUM_BLOCK_SIZE;
        this.graphicNum = 0;
        this.totleNum = 3;
        this.intDataForItemSelected = new ArrayList();
        this.screenBmp = new C03781();
        this.baseHandler = new C03792();
        this.mHandler = new C03803();
    }

    public void dayin() {
        getPrintDate();
        new Thread(this.screenBmp).start();
    }

    private void getPrintDate() {
        this.sb = new StringBuffer();
        for (int i = 0; i < this.exDataStreamIdlist.size(); i++) {
            this.exDataStreamIdItem = (SptExDataStreamIdItem) this.exDataStreamIdlist.get(i);
            this.sb.append(new StringBuilder(String.valueOf(this.exDataStreamIdItem.getStreamTextIdContent())).append("   ").append(this.exDataStreamIdItem.getStreamStr()).append(this.exDataStreamIdItem.getStreamState()).append(SpecilApiUtil.LINE_SEP).toString());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.data_stream);
        this.contexts = this;
        this.language = Files.getLanguage();
        this.verLocal = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.diagnosticSoftwareVersionNo);
        this.diagversion = this.softPackageId + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + this.verLocal;
        DataStreamTaskManager.getInstance();
        new Thread(new DataStreamTaskManagerThread()).start();
        this.taskManager = DataStreamTaskManager.getInstance();
        MyApplication.getInstance().addActivity(this);
        registerBoradcastReceiver();
        initView();
        this.cc = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.CCKey);
        BluetoothChatService.setHandler(this.mHandler);
    }

    private void initView() {
        this.btn_chronometer = (Chronometer) findViewById(C0136R.id.btn_chronometer);
        this.btn_chronometer.setFormat("(%s)");
        this.bundle = getIntent().getExtras();
        if (this.bundle != null) {
            this.exDataStreamIdlist = (ArrayList) this.bundle.getSerializable("SPT_EX_DATASTREAM_ID");
            this.lists.add(this.exDataStreamIdlist);
        }
        this.main_head = (RelativeLayout) findViewById(C0136R.id.mainTop);
        this.startButton = (ImageView) findViewById(C0136R.id.start);
        this.startButton.setOnClickListener(this);
        this.btn_jilu = (Button) findViewById(C0136R.id.btn_jilu);
        this.btn_jilu.setOnClickListener(this);
        this.date = (Button) findViewById(C0136R.id.date);
        this.date.setOnClickListener(this);
        this.bv = new BadgeView((Context) this, this.date);
        this.bv.setGravity(1);
        this.bv.setText(Contact.RELATION_ASK);
        this.bv.setTextSize(12.0f);
        this.bv.setBadgePosition(2);
        this.bv.setBackgroundDrawable(getResources().getDrawable(C0136R.drawable.data_stream_point));
        TextUtils.isEmpty(MySharedPreferences.getStringValue(this, MySharedPreferences.UserNameKey));
        this.wenzi = (Button) findViewById(C0136R.id.wenzi);
        this.jietu = (Button) findViewById(C0136R.id.jietu);
        this.wenzi.setOnClickListener(this);
        this.jietu.setOnClickListener(this);
        this.listview = (ListView) findViewById(C0136R.id.view);
        this.adapter = new DataStreamAdapter(this.exDataStreamIdlist, this);
        this.listview.setAdapter(this.adapter);
        this.listview.setOnItemClickListener(new C03814());
        this.flag = Boolean.valueOf(f1238D);
        this.timer = new Timer();
    }

    private void getDate() {
        this.sb = new StringBuffer();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (this.f1239k == 1) {
            this.sb.append("=====" + date + "=====");
            this.sb.append("\n\n");
        }
        System.err.println("\u6570\u636e\u603b\u6761\u6570  " + this.exDataStreamIdlist.size());
        for (int i = 0; i < this.exDataStreamIdlist.size(); i++) {
            this.exDataStreamIdItem = (SptExDataStreamIdItem) this.exDataStreamIdlist.get(i);
            this.sb.append("   " + this.exDataStreamIdItem.getStreamTextIdContent() + "   " + this.exDataStreamIdItem.getStreamStr() + "   " + this.exDataStreamIdItem.getStreamState() + "   " + "\n\n");
        }
    }

    protected void onResume() {
        super.onResume();
        FreeMemory.getInstance(this).freeMemory();
        this.isExecuteD = Boolean.valueOf(f1238D);
        this.fileDir = Constant.DST_FILE;
        this.softPackageId = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.savesoftPackageId);
        this.serialNo = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.serialNoKey);
        this.name = this.softPackageId + "_DS_";
    }

    protected void onStop() {
        super.onStop();
        this.isExecuteD = Boolean.valueOf(false);
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.wenzi) {
            if (!Environment.getExternalStorageState().equals("mounted")) {
                Toast.makeText(this, getResources().getString(C0136R.string.sdcard), 0).show();
            } else if (this.isShowDialog) {
                SaveTxt(this.name, f1238D);
            }
        } else if (v.getId() == C0136R.id.jietu) {
            this.f1239k = 1;
            if (!Environment.getExternalStorageState().equals("mounted")) {
                Toast.makeText(this, getResources().getString(C0136R.string.sdcard), 0).show();
            } else if (this.isShowDialog) {
                saveImage();
            }
        } else if (v.getId() == C0136R.id.guanli) {
            this.f1239k = 1;
            Intent intent = new Intent();
            intent.addFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
            intent.setClass(this, ShowFileActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (v.getId() == C0136R.id.start) {
            if (this.isReceiver) {
                this.date.setVisibility(8);
                this.isReceiver = false;
                this.mHandler.sendEmptyMessage(Constant.GRAPHIC_ICON_NUM_CHANGE);
                this.startButton.setImageResource(C0136R.drawable.right_bigen_selector);
                this.btn_chronometer.stop();
                this.btn_chronometer.setVisibility(8);
                this.isRecord = f1238D;
                r0 = new SimpleDateFormat("yyyyMMddHHmmss");
                date = r0.format(new Date());
                if (this.openFlag) {
                    this.openFlag = false;
                    Constant.bOpenFlag = false;
                    cols = this.jnitest.readGroupItemColCount(this.grp);
                    num = this.jnitest.readGroupItemCount(this.grp);
                    if (cols <= 0 || num <= 0) {
                        Toast.makeText(this, getResources().getString(C0136R.string.record_time_short), 1).show();
                        r0 = new File(this.fileDir + this.x431fileName);
                        if (r0.exists()) {
                            r0.delete();
                            return;
                        }
                        return;
                    }
                    this.jnitest.writeEndCloseFile(this.grp, date, this.fileId, this.hlsx, this.x431fileName);
                    reportPath = this.fileDir + this.x431fileName;
                    r0 = new File(this.fileDir + this.x431fileName);
                    if (r0.length() <= 0) {
                        r0.delete();
                        Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.fail_creat_file), 0).show();
                        Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
                        return;
                    }
                    DBDao.getInstance(this.contexts).addReport(this.x431fileName, date, this.serialNo, reportPath, Contact.RELATION_FRIEND, MainActivity.database);
                    return;
                }
                return;
            }
            this.date.setVisibility(0);
            this.mHandler.sendEmptyMessage(Constant.GRAPHIC_ICON_NUM_CHANGE);
            this.isReceiver = f1238D;
            this.isRecord = false;
            this.startButton.setImageResource(C0136R.drawable.right_stop_focuses);
        } else if (v.getId() == C0136R.id.date) {
            if (this.isRecord) {
                Toast.makeText(this, this.contexts.getResources().getText(C0136R.string.data_stream_stop_record), 0).show();
                return;
            }
            this.dataStreamList.clear();
            for (int i = 0; i < this.exDataStreamIdlist.size(); i++) {
                DataStream dataStream = new DataStream();
                this.exDataStreamIdItem = (SptExDataStreamIdItem) this.exDataStreamIdlist.get(i);
                dataStream.setCount(i);
                dataStream.setName(this.exDataStreamIdItem.getStreamTextIdContent());
                dataStream.setStreamState(this.exDataStreamIdItem.getStreamState());
                if (!XmlPullParser.NO_NAMESPACE.equals(this.exDataStreamIdItem.getStreamState()) && DataStreamUtils.isNumeric(this.exDataStreamIdItem.getStreamStr())) {
                    this.dataStreamList.add(dataStream);
                }
            }
            List<ArrayList<?>> tempLists = new ArrayList();
            if (this.lists.size() > 1) {
                tempLists.addAll(this.lists);
                ArrayList tempList = (ArrayList) tempLists.get(tempLists.size() - 1);
                tempLists.clear();
                tempLists.add(tempList);
            } else {
                tempLists = this.lists;
            }
            this.intDataForItemSelected.clear();
            for (Entry entry : DataStreamAdapter.getIsSelected().entrySet()) {
                if (((Boolean) entry.getValue()).booleanValue()) {
                    IntData intData = new IntData();
                    intData.setItem(Integer.valueOf(entry.getKey().toString()).intValue());
                    intData.setItemCheckedState(f1238D);
                    this.intDataForItemSelected.add(intData);
                }
            }
            if (this.intDataForItemSelected.size() > 0) {
                Intent active = new Intent(this.contexts, DataStreamChartTabActivity.class);
                active.putExtra("siteList", this.intDataForItemSelected);
                active.putExtra("DataList", (Serializable) tempLists);
                active.putExtra("times", this.times);
                active.putExtra("SPT_EX_DATASTREAM_ID", this.exDataStreamIdlist);
                active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                startActivity(active);
                finish();
                overridePendingTransition(0, 0);
                return;
            }
            Toast.makeText(this.contexts, C0136R.string.pleaseselect, 0).show();
        } else if (v.getId() != C0136R.id.btn_jilu) {
        } else {
            if (!this.isReceiver) {
                Toast.makeText(this, C0136R.string.plase_start, 0).show();
            } else if (checksd(0, f1238D)) {
                this.btn_jilu.setClickable(false);
                this.btn_jilu.setBackgroundResource(C0136R.drawable.record_hui_selector);
                this.startButton.setClickable(false);
                this.startButton.setImageResource(C0136R.drawable.right_close_selector);
                new C03825().start();
                if (this.isRecord) {
                    this.date.setVisibility(0);
                    this.mHandler.sendEmptyMessage(Constant.GRAPHIC_ICON_NUM_CHANGE);
                    this.btn_chronometer.setVisibility(8);
                    this.btn_chronometer.stop();
                    this.isRecord = false;
                    r0 = new SimpleDateFormat("yyyyMMddHHmmss");
                    date = r0.format(new Date());
                    if (this.openFlag && this.fileId > 0) {
                        this.openFlag = false;
                        Constant.bOpenFlag = false;
                        cols = this.jnitest.readGroupItemColCount(this.grp);
                        num = this.jnitest.readGroupItemCount(this.grp);
                        if (cols <= 0 || num <= 0) {
                            Toast.makeText(this, getResources().getString(C0136R.string.record_time_short), 1).show();
                            this.x431fileName = this.x431fileName.replaceAll("EOBD2", "EOBD");
                            r0 = new File(this.fileDir + this.x431fileName);
                            if (r0.exists()) {
                                r0.delete();
                                return;
                            }
                            return;
                        }
                        this.jnitest.writeEndCloseFile(this.grp, date, this.fileId, this.hlsx, this.x431fileName);
                        reportPath = this.fileDir + this.x431fileName;
                        String[] tempPaths = Constant.getDefaultExternalStoragePathList();
                        if (tempPaths.length > 1) {
                            reportPath = reportPath.replaceAll(tempPaths[1], tempPaths[0]);
                        }
                        r0 = new File(this.fileDir + this.x431fileName);
                        if (r0.length() == 0) {
                            r0.delete();
                            Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.fail_creat_file), 0).show();
                            Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
                            return;
                        }
                        DBDao.getInstance(this.contexts).addReport(this.x431fileName, date, this.serialNo, reportPath, Contact.RELATION_FRIEND, MainActivity.database);
                        return;
                    }
                    return;
                }
                this.date.setVisibility(8);
                this.mHandler.sendEmptyMessage(Constant.GRAPHIC_ICON_NUM_CHANGE);
                this.btn_chronometer.setVisibility(0);
                this.btn_chronometer.setBase(SystemClock.elapsedRealtime());
                this.btn_chronometer.start();
                this.isRecord = f1238D;
                this.basicFlag = false;
                Constant.bBasicFlag = false;
                this.hlsx = this.jnitest.init();
                r0 = new SimpleDateFormat("yyyyMMddHHmmss");
                date = r0.format(new Date());
                this.name = this.name.replaceAll("EOBD2", "EOBD");
                this.x431fileName = this.name + date + ".x431";
                this.fileId = this.jnitest.creatFile(this.x431fileName, this.language, this.diagversion, this.serialNo, this.hlsx);
                if (this.fileId > 0) {
                    this.isRecord = f1238D;
                    this.grp = this.jnitest.writeNewGroup(this.fileId, this.softPackageId, date);
                    this.openFlag = f1238D;
                    Constant.bOpenFlag = f1238D;
                    return;
                }
                this.date.setVisibility(8);
                this.mHandler.sendEmptyMessage(Constant.GRAPHIC_ICON_NUM_CHANGE);
                this.isRecord = false;
                this.openFlag = false;
                Constant.bOpenFlag = false;
                Toast.makeText(this, C0136R.string.fail_creat_file, 0).show();
            }
        }
    }

    @SuppressLint({"NewApi"})
    private void saveImage() {
        View view = getWindow().getDecorView();
        view.setDrawingCacheEnabled(f1238D);
        Bitmap bitmap = view.getDrawingCache();
        if (checksd((long) bitmap.getByteCount(), false)) {
            saveBitmaps(zoomBitmap(bitmap));
        }
        view.setDrawingCacheEnabled(false);
    }

    public void SaveTxt(String name, boolean ss) {
        getDate();
        String s = this.sb.toString();
        if (checksd((long) s.getBytes().length, false)) {
            try {
                String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                this.sdCardDir = Constant.DST_FILE;
                File dirFile = new File(this.sdCardDir);
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }
                name = name.replaceAll("EOBD2", "EOBD");
                this.pathTxt = this.sdCardDir + name + date + ".txt";
                if (!new File(this.pathTxt).exists()) {
                    FileOutputStream outStream = new FileOutputStream(this.pathTxt, f1238D);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outStream, AsyncHttpResponseHandler.DEFAULT_CHARSET);
                    outputStreamWriter.write(s);
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                    outStream.close();
                    toSeeReport(this.pathTxt);
                    String[] tempPaths = Constant.getDefaultExternalStoragePathList();
                    String tempSDCardDir = this.sdCardDir;
                    if (tempPaths.length > 1) {
                        tempSDCardDir = tempSDCardDir.replaceAll(tempPaths[1], tempPaths[0]);
                    }
                    DBDao.getInstance(this).addReport(new StringBuilder(String.valueOf(name)).append(date).append(".txt").toString(), date, this.serialNo, new StringBuilder(String.valueOf(tempSDCardDir)).append(name).append(date).append(".txt").toString(), Contact.RELATION_ASK, MainActivity.database);
                }
            } catch (Exception e) {
                es = e.toString();
                erro = "java.io.IOException: write failed: ENOSPC (No space left on device)";
                Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.fail_creat_file), 0).show();
                files = new File(this.pathTxt);
                File files;
                if (files.exists()) {
                    files.delete();
                }
                String es;
                String erro;
                if (es.equalsIgnoreCase(erro)) {
                    Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
                } else {
                    Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.io_exception), 0).show();
                }
            }
        }
    }

    private void toSeeReport(String path) {
        Builder builder = new Builder(this.contexts);
        builder.setTitle(C0136R.string.report_toast).setPositiveButton(C0136R.string.to_see_report, new C03836(path)).setNegativeButton(C0136R.string.cancel, new C03847()).setCancelable(false);
        if (this.isShowDialog) {
            this.isShowDialog = false;
            builder.show();
        }
    }

    protected void toSeeReportOne(String path) {
        Intent intent = new Intent(this.contexts, DiagnoseDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(LocaleUtil.INDONESIAN, -2);
        bundle.putString("path", path);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    protected void toSeeReportList() {
        Intent intent = new Intent(this.contexts, DiagnoseReportActivity.class);
        intent.putExtra(MultipleAddresses.CC, this.cc);
        startActivity(intent);
    }

    protected void onPause() {
        super.onPause();
    }

    public void saveBitmaps(Bitmap bitmap) {
        String imagePath = XmlPullParser.NO_NAMESPACE;
        boolean isOK = f1238D;
        String tempSDCardDir = XmlPullParser.NO_NAMESPACE;
        if (Environment.getExternalStorageState().equals("mounted")) {
            this.sdCardDir = this.fileDir;
            File dirFile = new File(this.sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            this.name = this.name.replaceAll("EOBD2", "EOBD");
            File file = new File(this.sdCardDir, this.name + date + Util.PHOTO_DEFAULT_EXT);
            if (file.exists()) {
                file.delete();
                file = new File(this.sdCardDir, this.name + date + Util.PHOTO_DEFAULT_EXT);
            } else {
                tempSDCardDir = this.sdCardDir;
                String[] tempPaths = Constant.getDefaultExternalStoragePathList();
                if (tempPaths.length > 1) {
                    tempSDCardDir = tempSDCardDir.replaceAll(tempPaths[1], tempPaths[0]);
                }
                tempSDCardDir = new StringBuilder(String.valueOf(tempSDCardDir)).append(this.name).append(date).append(Util.PHOTO_DEFAULT_EXT).toString();
                imagePath = tempSDCardDir;
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                this.dao.addReport(this.name + date + Util.PHOTO_DEFAULT_EXT, date, this.serialNo, tempSDCardDir, Contact.RELATION_ASK, MainActivity.database);
                if (!imagePath.equals(XmlPullParser.NO_NAMESPACE)) {
                    toSeeReport(imagePath);
                }
            } catch (FileNotFoundException e) {
                isOK = false;
                imagePath = XmlPullParser.NO_NAMESPACE;
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            if (!isOK) {
                if (file.exists()) {
                    file.delete();
                }
                Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.circleGetImageFail), 0).show();
                return;
            }
            return;
        }
        Toast.makeText(this, getResources().getString(C0136R.string.sdcard), 0).show();
    }

    protected void onDestroy() {
        SimpleDialog.closeProgressDialog(this.progressDialog);
        super.onDestroy();
        if (this.receiver != null) {
            unregisterReceiver(this.receiver);
        }
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
        return Bitmap.createBitmap(target, 0, y, width, height - y, matrix, f1238D);
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
        this.myIntentFilter.addAction("DataStreamSelectSite");
        this.myIntentFilter.addAction("CLOSE_DATASTREAM_ACTIVITY");
        registerReceiver(this.receiver, this.myIntentFilter);
        this.flag2 = Boolean.valueOf(f1238D);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != Service.SUNRPC && keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        Constant.isDataStreamBack = f1238D;
        back();
        return f1238D;
    }

    private void back() {
        FreeMemory.getInstance(this).freeMemory();
        if (this.isRecord) {
            Toast.makeText(this, this.contexts.getResources().getText(C0136R.string.data_stream_stop_record), 0).show();
        } else if ((Constant.mChatService == null || Constant.mChatService.getState() != 3) && !Constant.isDemo) {
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
            Constant.streamNextCode = new byte[]{(byte) -1};
            if (Constant.bridge != null) {
                Constant.bridge.putData();
            }
            this.isReceiver = f1238D;
        }
    }

    private boolean checksd(long filenght, boolean record) {
        long sdcardUsableSize = CRPTools.getUsableSDCardSize();
        if (record) {
            sdcardUsableSize = (long) (((float) sdcardUsableSize) / 1048576.0f);
            if (sdcardUsableSize > 5) {
                return f1238D;
            }
            if (sdcardUsableSize <= 2) {
                Toast.makeText(this.contexts, getString(C0136R.string.clean_storage_data), 0).show();
                return false;
            }
            Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.lower_storage), 0).show();
            return false;
        } else if (filenght <= sdcardUsableSize) {
            return f1238D;
        } else {
            Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
            return false;
        }
    }
}

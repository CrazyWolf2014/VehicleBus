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
import com.ifoer.adapter.VWDataStreamAdapter;
import com.ifoer.adapter.VWDataStreamAdapter.Item;
import com.ifoer.db.DBDao;
import com.ifoer.entity.BadgeView;
import com.ifoer.entity.Constant;
import com.ifoer.entity.DataStream;
import com.ifoer.entity.IntData;
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
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class VWDataStreamActivity extends Activity implements OnClickListener {
    private static final boolean f1283D = true;
    private final int MSG_PRINT_RESULT_SCREEN_SHOT;
    private final int MSG_PRINT_START;
    private VWDataStreamAdapter adapter;
    Handler baseHandler;
    private boolean basicFlag;
    Bitmap bit_first;
    Bitmap bit_second;
    Bitmap bitmap1;
    private Bundle bundle;
    private BadgeView bv;
    private LinearLayout car_maintain;
    String cc;
    private Chronometer chronometer;
    private int cishu;
    private Context contexts;
    private int count;
    private int currentCheckedItem;
    DBDao dao;
    private String data;
    private ArrayList<DataStream> dataStreamList;
    private Button date;
    private String diagversion;
    private Dialog dialog;
    private Button downChannel;
    private File file;
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
    private Button jilu;
    private JniX431FileTest jnitest;
    private int f1284k;
    private String language;
    private long lastTime;
    private List<ArrayList<?>> lists;
    private ListView listview;
    private ArrayList<ArrayList<SptVwDataStreamIdItem>> llist;
    private final Handler mHandler;
    private RelativeLayout main_head;
    public LinearLayout menuBtn;
    private IntentFilter myIntentFilter;
    String name;
    private boolean openFlag;
    private int pagecount;
    private int pagesize;
    private String pathImg;
    private String pathTxt;
    int printResult;
    private ProgressDialog progressDialog;
    private mBroadcastReceiver receiver;
    private StringBuffer sb;
    Runnable screenBmp;
    private String sdCardDir;
    private String serialNo;
    private String softPackageId;
    private boolean start;
    private ImageView startButton;
    private boolean stop;
    private DataStreamTaskManager taskManager;
    private Timer timer;
    private double times;
    private final int totleNum;
    private Button upChannel;
    private String verLocal;
    private SptVwDataStreamIdItem vwDataStreamIdItem;
    private ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist;
    private Button wenzi;
    private String x431fileName;

    /* renamed from: com.ifoer.expedition.BluetoothChat.VWDataStreamActivity.1 */
    class C04821 implements Runnable {
        C04821() {
        }

        public void run() {
            NetPOSPrinter bmpPrinter = new NetPOSPrinter(VWDataStreamActivity.this);
            VWDataStreamActivity.this.bit_first = bmpPrinter.drawBitFirst();
            VWDataStreamActivity.this.bit_second = bmpPrinter.drawBitSecond(VWDataStreamActivity.this.sb.toString());
            VWDataStreamActivity.this.bitmap1 = NetPOSPrinter.mixtureBitmap(VWDataStreamActivity.this.bit_first, VWDataStreamActivity.this.bit_second);
            VWDataStreamActivity.this.printResult = bmpPrinter.printPic(VWDataStreamActivity.this.bitmap1);
            bmpPrinter.resultToast(VWDataStreamActivity.this.printResult);
            VWDataStreamActivity.this.baseHandler.obtainMessage(0, Integer.valueOf(VWDataStreamActivity.this.printResult)).sendToTarget();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.VWDataStreamActivity.2 */
    class C04832 extends Handler {
        C04832() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case InBandBytestreamManager.MAXIMUM_BLOCK_SIZE /*65535*/:
                    VWDataStreamActivity.this.dayin();
                case 10101010:
                    if (VWDataStreamActivity.this.progressDialog != null && VWDataStreamActivity.this.progressDialog.isShowing()) {
                        SimpleDialog.closeProgressDialog(VWDataStreamActivity.this.progressDialog);
                    }
                    SimpleDialog.checkConectior(VWDataStreamActivity.this.contexts, VWDataStreamActivity.this.getString(C0136R.string.initializeTilte), VWDataStreamActivity.this.getString(C0136R.string.check_connector));
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.VWDataStreamActivity.3 */
    class C04843 extends Handler {
        C04843() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    if (VWDataStreamActivity.this.flag.booleanValue() && VWDataStreamActivity.this.flag2.booleanValue() && VWDataStreamActivity.this.adapter != null && VWDataStreamActivity.this.listview != null && VWDataStreamActivity.this.listview.getAdapter() != null) {
                        VWDataStreamActivity.this.adapter.refresh(VWDataStreamActivity.this.vwDataStreamIdlist);
                        VWDataStreamActivity.this.llist.add(VWDataStreamActivity.this.vwDataStreamIdlist);
                        VWDataStreamActivity.this.taskManager.addDownloadTask(new DataStreamTask(VWDataStreamActivity.this.contexts, VWDataStreamActivity.this.vwDataStreamIdlist, VWDataStreamActivity.this.lists, VWDataStreamActivity.this.jnitest, VWDataStreamActivity.this.grp, VWDataStreamActivity.this.openFlag, VWDataStreamActivity.this.basicFlag, VWDataStreamActivity.this.isReceiver, VWDataStreamActivity.this.isRecord, VWDataStreamActivity.this.mHandler));
                    }
                case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                    VWDataStreamActivity.this.openFlag = false;
                    VWDataStreamActivity.this.basicFlag = false;
                    Constant.bBasicFlag = false;
                    Constant.bOpenFlag = false;
                case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
                    VWDataStreamActivity.this.openFlag = VWDataStreamActivity.f1283D;
                    VWDataStreamActivity.this.basicFlag = VWDataStreamActivity.f1283D;
                    Constant.bBasicFlag = VWDataStreamActivity.f1283D;
                    Constant.bOpenFlag = VWDataStreamActivity.f1283D;
                case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                    VWDataStreamActivity vWDataStreamActivity = VWDataStreamActivity.this;
                    vWDataStreamActivity.times = vWDataStreamActivity.times + 1.0d;
                    if (VWDataStreamActivity.this.dialog != null && VWDataStreamActivity.this.dialog.isShowing()) {
                        VWDataStreamActivity.this.graphView.pushDataToChart(VWDataStreamActivity.this.lists, VWDataStreamActivity.this.times, VWDataStreamActivity.this.currentCheckedItem);
                    }
                    Intent intent = new Intent("pushDataToChart");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DataList", (Serializable) VWDataStreamActivity.this.lists);
                    bundle.putDouble("times", VWDataStreamActivity.this.times);
                    intent.putExtras(bundle);
                    VWDataStreamActivity.this.contexts.sendBroadcast(intent);
                case WelcomeActivity.GPIO_IOCQDATAOUT /*19*/:
                    VWDataStreamActivity.this.jilu.setBackgroundResource(C0136R.drawable.record_bai_selector);
                    VWDataStreamActivity.this.jilu.setClickable(VWDataStreamActivity.f1283D);
                    VWDataStreamActivity.this.startButton.setImageResource(C0136R.drawable.right_stop_focuses);
                    VWDataStreamActivity.this.startButton.setClickable(VWDataStreamActivity.f1283D);
                case Constant.GRAPHIC_ICON_NUM_CHANGE /*911*/:
                    if (VWDataStreamActivity.this.graphicNum <= 0 || VWDataStreamActivity.this.date.getVisibility() != 0) {
                        VWDataStreamActivity.this.bv.setVisibility(8);
                        return;
                    }
                    VWDataStreamActivity.this.bv.setText(String.valueOf(VWDataStreamActivity.this.graphicNum));
                    VWDataStreamActivity.this.bv.setGravity(1);
                    VWDataStreamActivity.this.bv.setTextSize(12.0f);
                    VWDataStreamActivity.this.bv.setBadgePosition(2);
                    VWDataStreamActivity.this.bv.setBackgroundDrawable(VWDataStreamActivity.this.getResources().getDrawable(C0136R.drawable.data_stream_point));
                    VWDataStreamActivity.this.bv.show();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.VWDataStreamActivity.4 */
    class C04854 implements OnItemClickListener {
        C04854() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            if (VWDataStreamActivity.this.date.getVisibility() == 0) {
                Item holder = (Item) arg1.getTag();
                SptVwDataStreamIdItem exDataStreamIdItemtemp = (SptVwDataStreamIdItem) VWDataStreamActivity.this.vwDataStreamIdlist.get(arg2);
                if (XmlPullParser.NO_NAMESPACE.equals(exDataStreamIdItemtemp.getStreamUnitIdContent()) || !DataStreamUtils.isNumeric(exDataStreamIdItemtemp.getStreamStr())) {
                    Toast.makeText(VWDataStreamActivity.this, VWDataStreamActivity.this.getResources().getString(C0136R.string.data_stream_item_select_show_info), 0).show();
                    return;
                }
                holder.flagIcon.toggle();
                VWDataStreamActivity vWDataStreamActivity;
                if (holder.flagIcon.isChecked()) {
                    if (VWDataStreamActivity.this.graphicNum > 3) {
                        Toast.makeText(VWDataStreamActivity.this, VWDataStreamActivity.this.getResources().getString(C0136R.string.data_stream_max_item_info_scanPad_mini), 0).show();
                        holder.flagIcon.setChecked(false);
                        return;
                    }
                    vWDataStreamActivity = VWDataStreamActivity.this;
                    vWDataStreamActivity.graphicNum = vWDataStreamActivity.graphicNum + 1;
                } else if (VWDataStreamActivity.this.graphicNum > 0) {
                    vWDataStreamActivity = VWDataStreamActivity.this;
                    vWDataStreamActivity.graphicNum = vWDataStreamActivity.graphicNum - 1;
                }
                VWDataStreamAdapter.getIsSelected().put(Integer.valueOf(arg2), Boolean.valueOf(holder.flagIcon.isChecked()));
                VWDataStreamActivity.this.mHandler.sendEmptyMessage(Constant.GRAPHIC_ICON_NUM_CHANGE);
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.VWDataStreamActivity.5 */
    class C04865 extends Thread {
        C04865() {
        }

        public void run() {
            try {
                C04865.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            VWDataStreamActivity.this.mHandler.sendEmptyMessage(19);
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.VWDataStreamActivity.6 */
    class C04876 extends Thread {
        C04876() {
        }

        public void run() {
            try {
                C04876.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            VWDataStreamActivity.this.mHandler.sendEmptyMessage(19);
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.VWDataStreamActivity.7 */
    class C04887 implements DialogInterface.OnClickListener {
        private final /* synthetic */ String val$path;

        C04887(String str) {
            this.val$path = str;
        }

        public void onClick(DialogInterface dialog, int which) {
            VWDataStreamActivity.this.isShowDialog = VWDataStreamActivity.f1283D;
            if (TextUtils.isEmpty(VWDataStreamActivity.this.cc)) {
                VWDataStreamActivity.this.toSeeReportList();
            } else if (DBDao.getInstance(VWDataStreamActivity.this.contexts).queryReport(VWDataStreamActivity.this.cc, Contact.RELATION_ASK, MainActivity.database).size() < 1) {
                VWDataStreamActivity.this.toSeeReportOne(this.val$path);
            } else {
                VWDataStreamActivity.this.toSeeReportList();
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.VWDataStreamActivity.8 */
    class C04898 implements DialogInterface.OnClickListener {
        C04898() {
        }

        public void onClick(DialogInterface dialog, int which) {
            VWDataStreamActivity.this.isShowDialog = VWDataStreamActivity.f1283D;
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {

        /* renamed from: com.ifoer.expedition.BluetoothChat.VWDataStreamActivity.mBroadcastReceiver.1 */
        class C04901 extends Thread {
            C04901() {
            }

            public void run() {
                try {
                    C04901.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Process.killProcess(Process.myPid());
                VWDataStreamActivity.this.finish();
                VWDataStreamActivity.this.overridePendingTransition(0, 0);
            }
        }

        /* renamed from: com.ifoer.expedition.BluetoothChat.VWDataStreamActivity.mBroadcastReceiver.2 */
        class C04912 extends Thread {
            C04912() {
            }

            public void run() {
                try {
                    C04912.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Process.killProcess(Process.myPid());
                VWDataStreamActivity.this.finish();
                VWDataStreamActivity.this.overridePendingTransition(0, 0);
            }
        }

        /* renamed from: com.ifoer.expedition.BluetoothChat.VWDataStreamActivity.mBroadcastReceiver.3 */
        class C04923 extends Thread {
            C04923() {
            }

            public void run() {
                VWDataStreamActivity.this.mHandler.sendMessage(VWDataStreamActivity.this.mHandler.obtainMessage(15));
            }
        }

        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (VWDataStreamActivity.this.isExecuteD.booleanValue()) {
                if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - VWDataStreamActivity.this.lastTime) / 1000 > 30) {
                    SimpleDialog.ExitDialog(context);
                }
                if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                    VWDataStreamActivity.this.lastTime = System.currentTimeMillis();
                }
                if (intent.getAction().equals("CLOSE_DATASTREAM_ACTIVITY")) {
                    new C04901().start();
                }
                if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    SimpleDialog.closeProgressDialog(VWDataStreamActivity.this.progressDialog);
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    Toast.makeText(VWDataStreamActivity.this.getApplicationContext(), VWDataStreamActivity.this.getResources().getString(C0136R.string.connectionLost), 0).show();
                    new C04912().start();
                } else if (intent.getAction().equals("feedbackMeauData")) {
                    VWDataStreamActivity.this.timer.cancel();
                    CToJava.streamFlag = Boolean.valueOf(false);
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("menuData");
                    r0 = new Intent(VWDataStreamActivity.this, CarDiagnoseActivity.class);
                    r0.putExtra("menuData", menuDataList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(VWDataStreamActivity.this.progressDialog);
                    VWDataStreamActivity.this.startActivity(r0);
                    VWDataStreamActivity.this.overridePendingTransition(0, 0);
                    VWDataStreamActivity.this.finish();
                    VWDataStreamActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_DS_MENU_ID")) {
                    VWDataStreamActivity.this.timer.cancel();
                    CToJava.streamFlag = Boolean.valueOf(false);
                    menuDataList = (ArrayList) intent.getExtras().getSerializable("SPT_DS_MENU_ID");
                    r0 = new Intent(VWDataStreamActivity.this, CarDiagnoseActivity.class);
                    r0.putExtra("menuData", menuDataList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(VWDataStreamActivity.this.progressDialog);
                    VWDataStreamActivity.this.startActivity(r0);
                    VWDataStreamActivity.this.overridePendingTransition(0, 0);
                    VWDataStreamActivity.this.finish();
                    VWDataStreamActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_ACTIVE_TEST")) {
                    VWDataStreamActivity.this.timer.cancel();
                    CToJava.streamFlag = Boolean.valueOf(false);
                    SptActiveTest sptActiveTest = (SptActiveTest) intent.getExtras().getSerializable("ACTIVE_TEST");
                    Intent active = new Intent(VWDataStreamActivity.this, ActiveTestActivity.class);
                    active.putExtra("ACTIVE_TEST", sptActiveTest);
                    active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(VWDataStreamActivity.this.progressDialog);
                    VWDataStreamActivity.this.startActivity(active);
                    VWDataStreamActivity.this.overridePendingTransition(0, 0);
                    VWDataStreamActivity.this.finish();
                    VWDataStreamActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_NOBUTTONBOX_TEXT")) {
                    if (VWDataStreamActivity.this.progressDialog == null) {
                        VWDataStreamActivity.this.progressDialog = new ProgressDialog(VWDataStreamActivity.this.contexts);
                        VWDataStreamActivity.this.progressDialog.setCancelable(false);
                    } else {
                        VWDataStreamActivity.this.progressDialog.dismiss();
                        VWDataStreamActivity.this.progressDialog = null;
                        VWDataStreamActivity.this.progressDialog = new ProgressDialog(VWDataStreamActivity.this.contexts);
                        VWDataStreamActivity.this.progressDialog.setCancelable(false);
                    }
                    Spt_Nobuttonbox_Text nobuttonbox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("Nobuttonbox");
                    SimpleDialog.openProgressDialog(VWDataStreamActivity.this.contexts, nobuttonbox.getTitle(), nobuttonbox.getContent(), VWDataStreamActivity.this.progressDialog);
                } else if (intent.getAction().equals("closeNobuttonBox")) {
                    SimpleDialog.closeProgressDialog(VWDataStreamActivity.this.progressDialog);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE")) {
                    VWDataStreamActivity.this.timer.cancel();
                    CToJava.streamFlag = Boolean.valueOf(false);
                    ArrayList<SptTroubleTest> troubleTestList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE");
                    r0 = new Intent(VWDataStreamActivity.this, FaultCodeActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE", troubleTestList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(VWDataStreamActivity.this.progressDialog);
                    VWDataStreamActivity.this.startActivity(r0);
                    VWDataStreamActivity.this.overridePendingTransition(0, 0);
                    VWDataStreamActivity.this.finish();
                    VWDataStreamActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EXIT_SHOW_WINDOW")) {
                    if (VWDataStreamActivity.this.openFlag) {
                        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        VWDataStreamActivity.this.openFlag = false;
                        Constant.bOpenFlag = false;
                        VWDataStreamActivity.this.jnitest.writeEndCloseFile(VWDataStreamActivity.this.grp, date, VWDataStreamActivity.this.fileId, VWDataStreamActivity.this.hlsx, VWDataStreamActivity.this.x431fileName);
                        String reportPath = new StringBuilder(String.valueOf(VWDataStreamActivity.this.fileDir)).append(VWDataStreamActivity.this.x431fileName).toString();
                        VWDataStreamActivity.this.file = new File(new StringBuilder(String.valueOf(VWDataStreamActivity.this.fileDir)).append(VWDataStreamActivity.this.x431fileName).toString());
                        if (VWDataStreamActivity.this.file.length() <= 0) {
                            VWDataStreamActivity.this.file.delete();
                            Toast.makeText(VWDataStreamActivity.this.contexts, VWDataStreamActivity.this.contexts.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
                        } else {
                            DBDao.getInstance(VWDataStreamActivity.this.contexts).addReport(VWDataStreamActivity.this.x431fileName, date, VWDataStreamActivity.this.serialNo, reportPath, Contact.RELATION_FRIEND, MainActivity.database);
                        }
                    }
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    VWDataStreamActivity.this.timer.cancel();
                    SimpleDialog.closeProgressDialog(VWDataStreamActivity.this.progressDialog);
                    VWDataStreamActivity.this.finish();
                    VWDataStreamActivity.this.overridePendingTransition(0, 0);
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                } else if (intent.getAction().equals("SPT_STREAM_SELECT_ID_EX")) {
                    VWDataStreamActivity.this.timer.cancel();
                    CToJava.streamFlag = Boolean.valueOf(false);
                    ArrayList<SptStreamSelectIdItem> streamSelectIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_STREAM_SELECT_ID_EX");
                    r0 = new Intent(VWDataStreamActivity.this, StreamSelectActivity.class);
                    r0.putExtra("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(VWDataStreamActivity.this.progressDialog);
                    VWDataStreamActivity.this.startActivity(r0);
                    VWDataStreamActivity.this.overridePendingTransition(0, 0);
                    VWDataStreamActivity.this.finish();
                    VWDataStreamActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_MESSAGEBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(VWDataStreamActivity.this.progressDialog);
                    SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) intent.getExtras().getSerializable("SPT_MESSAGEBOX_TEXT");
                    SimpleDialogControl.showDiaglog(VWDataStreamActivity.this.contexts, sptMessageBoxText.getDialogType(), sptMessageBoxText);
                } else if (intent.getAction().equals("ConnectionLost")) {
                    Toast.makeText(VWDataStreamActivity.this.getApplicationContext(), VWDataStreamActivity.this.getResources().getString(C0136R.string.devlost), 0).show();
                } else if (intent.getAction().equals("SPT_INPUTSTRING_EX")) {
                    SimpleDialog.closeProgressDialog(VWDataStreamActivity.this.progressDialog);
                    SptInputStringEx inputString = (SptInputStringEx) intent.getExtras().getSerializable("SPT_INPUTSTRING_EX");
                    SimpleDialog.sptInputStringExDiagnose(VWDataStreamActivity.this.contexts, inputString.getDialogTitle(), inputString.getDialogContent(), inputString.getInputHint());
                } else if (intent.getAction().equals("SPT_INPUT_NUMERIC")) {
                    SimpleDialog.closeProgressDialog(VWDataStreamActivity.this.progressDialog);
                    SptInputNumric inputNumric = (SptInputNumric) intent.getExtras().getSerializable("SPT_INPUT_NUMERIC");
                    SimpleDialog.sptInputNumericDiagnose(VWDataStreamActivity.this.contexts, inputNumric.getDialogTitle(), inputNumric.getDialogContent(), inputNumric.getInputHint(), inputNumric.getDigit());
                } else if (intent.getAction().equals("SPT_INPUTBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(VWDataStreamActivity.this.progressDialog);
                    Spt_Nobuttonbox_Text inputBox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTBOX_TEXT");
                    SimpleDialog.sptInputBoxTextDiagnose(VWDataStreamActivity.this.contexts, inputBox.getTitle(), inputBox.getContent());
                } else if (intent.getAction().equals("SPT_INPUTSTRING")) {
                    SimpleDialog.closeProgressDialog(VWDataStreamActivity.this.progressDialog);
                    Spt_Nobuttonbox_Text inputStr = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTSTRING");
                    SimpleDialog.sptInputStringDiagnose(VWDataStreamActivity.this.contexts, inputStr.getTitle(), inputStr.getContent());
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN")) {
                    VWDataStreamActivity.this.timer.cancel();
                    CToJava.streamFlag = Boolean.valueOf(false);
                    ArrayList<SptTroubleTest> troubleCodeFrozenList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN");
                    r0 = new Intent(VWDataStreamActivity.this, FaultCodeFrozenActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(VWDataStreamActivity.this.progressDialog);
                    VWDataStreamActivity.this.startActivity(r0);
                    VWDataStreamActivity.this.overridePendingTransition(0, 0);
                    VWDataStreamActivity.this.finish();
                    VWDataStreamActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                    SimpleDialog.closeProgressDialog(VWDataStreamActivity.this.progressDialog);
                    VWDataStreamActivity.this.vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                    new C04923().start();
                } else if (intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                    VWDataStreamActivity.this.timer.cancel();
                    CToJava.streamFlag = Boolean.valueOf(false);
                    ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                    r0 = new Intent(VWDataStreamActivity.this, VWDataStreamActivity.class);
                    r0.putExtra("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(VWDataStreamActivity.this.progressDialog);
                    VWDataStreamActivity.this.startActivity(r0);
                    VWDataStreamActivity.this.overridePendingTransition(0, 0);
                    VWDataStreamActivity.this.finish();
                    VWDataStreamActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_SHOW_PICTURE")) {
                    SimpleDialog.closeProgressDialog(VWDataStreamActivity.this.progressDialog);
                    SimpleDialog.sptShowPictureDiagnose(VWDataStreamActivity.this.contexts, intent.getExtras().getString("SPT_SHOW_PICTURE"));
                }
            }
        }
    }

    public VWDataStreamActivity() {
        this.vwDataStreamIdlist = new ArrayList();
        this.vwDataStreamIdItem = null;
        this.dataStreamList = new ArrayList();
        this.intDataForItemSelected = new ArrayList();
        this.llist = new ArrayList();
        this.sb = null;
        this.f1284k = 1;
        this.pagesize = 7;
        this.data = null;
        this.pagecount = 0;
        this.cishu = 0;
        this.start = f1283D;
        this.isExecuteD = Boolean.valueOf(false);
        this.timer = new Timer();
        this.flag = Boolean.valueOf(false);
        this.flag2 = Boolean.valueOf(false);
        this.dao = DBDao.getInstance(this);
        this.stop = f1283D;
        this.lists = new ArrayList();
        this.jnitest = new JniX431FileTest();
        this.openFlag = false;
        this.basicFlag = false;
        this.isReceiver = f1283D;
        this.isRecord = false;
        this.dialog = null;
        this.currentCheckedItem = 0;
        this.times = 0.0d;
        this.graphView = null;
        this.isShowDialog = f1283D;
        this.bitmap1 = null;
        this.MSG_PRINT_RESULT_SCREEN_SHOT = 0;
        this.MSG_PRINT_START = InBandBytestreamManager.MAXIMUM_BLOCK_SIZE;
        this.count = 0;
        this.graphicNum = 0;
        this.totleNum = 3;
        this.screenBmp = new C04821();
        this.baseHandler = new C04832();
        this.mHandler = new C04843();
    }

    private void getPrintDate() {
        this.sb = new StringBuffer();
        for (int i = 0; i < this.vwDataStreamIdlist.size(); i++) {
            this.vwDataStreamIdItem = (SptVwDataStreamIdItem) this.vwDataStreamIdlist.get(i);
            this.sb.append(new StringBuilder(String.valueOf(this.vwDataStreamIdItem.getStreamTextIdContent())).append("   ").append(this.vwDataStreamIdItem.getStreamStr()).append(this.vwDataStreamIdItem.getStreamUnitIdContent()).append(SpecilApiUtil.LINE_SEP).toString());
        }
    }

    public void dayin() {
        getPrintDate();
        new Thread(this.screenBmp).start();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.data_stream_vw);
        this.contexts = this;
        this.softPackageId = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.savesoftPackageId);
        this.serialNo = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.serialNoKey);
        this.name = this.softPackageId + "_DS_";
        this.language = Files.getLanguage();
        this.verLocal = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.diagnosticSoftwareVersionNo);
        this.diagversion = this.softPackageId + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + this.verLocal;
        DataStreamTaskManager.getInstance();
        new Thread(new DataStreamTaskManagerThread()).start();
        this.taskManager = DataStreamTaskManager.getInstance();
        MyApplication.getInstance().addActivity(this);
        registerBoradcastReceiver();
        this.cc = MySharedPreferences.getStringValue(this.contexts, MySharedPreferences.CCKey);
        initView();
        BluetoothChatService.setHandler(this.baseHandler);
    }

    private void initView() {
        this.chronometer = (Chronometer) findViewById(C0136R.id.btn_chronometer);
        this.chronometer.setFormat("(%s)");
        this.bundle = getIntent().getExtras();
        if (this.bundle != null) {
            this.vwDataStreamIdlist = (ArrayList) this.bundle.getSerializable("SPT_VW_DATASTREAM_ID");
            this.lists.add(this.vwDataStreamIdlist);
        }
        this.main_head = (RelativeLayout) findViewById(C0136R.id.mainTop);
        this.startButton = (ImageView) findViewById(C0136R.id.start);
        this.startButton.setOnClickListener(this);
        this.date = (Button) findViewById(C0136R.id.date);
        this.date.setOnClickListener(this);
        this.bv = new BadgeView((Context) this, this.date);
        this.bv.setGravity(1);
        this.bv.setText(Contact.RELATION_ASK);
        this.bv.setTextSize(12.0f);
        this.bv.setBadgePosition(2);
        this.bv.setBackgroundDrawable(getResources().getDrawable(C0136R.drawable.data_stream_point));
        TextUtils.isEmpty(MySharedPreferences.getStringValue(getApplicationContext(), MySharedPreferences.UserNameKey));
        this.wenzi = (Button) findViewById(C0136R.id.wenzi);
        this.jietu = (Button) findViewById(C0136R.id.jietu);
        this.jilu = (Button) findViewById(C0136R.id.btn_jilu);
        this.wenzi.setOnClickListener(this);
        this.jietu.setOnClickListener(this);
        this.jilu.setOnClickListener(this);
        this.upChannel = (Button) findViewById(C0136R.id.upChannel);
        this.downChannel = (Button) findViewById(C0136R.id.downChannel);
        this.upChannel.setOnClickListener(this);
        this.downChannel.setOnClickListener(this);
        this.listview = (ListView) findViewById(C0136R.id.view);
        this.adapter = new VWDataStreamAdapter(this.vwDataStreamIdlist, this);
        this.listview.setAdapter(this.adapter);
        this.listview.setOnItemClickListener(new C04854());
        this.flag = Boolean.valueOf(f1283D);
        this.timer = new Timer();
    }

    private void getDate() {
        this.sb = new StringBuffer();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (this.f1284k == 1) {
            this.sb.append("=====" + date + "=====");
            this.sb.append("\n\n");
        }
        System.err.println("\u6570\u636e\u603b\u6761\u6570  " + this.vwDataStreamIdlist.size());
        for (int i = 0; i < this.vwDataStreamIdlist.size(); i++) {
            this.vwDataStreamIdItem = (SptVwDataStreamIdItem) this.vwDataStreamIdlist.get(i);
            this.sb.append("   " + this.vwDataStreamIdItem.getStreamTextIdContent() + "   " + this.vwDataStreamIdItem.getStreamStr() + "   " + this.vwDataStreamIdItem.getStreamUnitIdContent() + "   " + "\n\n");
        }
    }

    protected void onResume() {
        super.onResume();
        FreeMemory.getInstance(this).freeMemory();
        this.fileDir = Constant.DST_FILE;
        this.isExecuteD = Boolean.valueOf(f1283D);
    }

    protected void onStop() {
        super.onStop();
        this.isExecuteD = Boolean.valueOf(false);
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.wenzi) {
            SaveTxt(this.name, f1283D);
        } else if (v.getId() == C0136R.id.jietu) {
            this.f1284k = 1;
            if (!Environment.getExternalStorageState().equals("mounted")) {
                Toast.makeText(this, getResources().getString(C0136R.string.sdcard), 0).show();
            } else if (this.isShowDialog) {
                saveImage();
            }
        } else if (v.getId() == C0136R.id.guanli) {
            this.f1284k = 1;
            Intent intent = new Intent();
            intent.addFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
            intent.setClass(this, ShowFileActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (v.getId() == C0136R.id.upChannel) {
            Constant.streamNextCode = ByteHexHelper.intToHexBytes(1);
            CToJava.streamFlag = Boolean.valueOf(false);
        } else if (v.getId() == C0136R.id.downChannel) {
            Constant.streamNextCode = ByteHexHelper.intToHexBytes(2);
            CToJava.streamFlag = Boolean.valueOf(false);
        } else if (v.getId() == C0136R.id.jilu) {
            if (this.isReceiver) {
                this.jilu.setClickable(false);
                this.jilu.setBackgroundResource(C0136R.drawable.hui_btn_bg_focuses);
                this.startButton.setClickable(false);
                this.startButton.setImageResource(C0136R.drawable.right_close_focuses);
                new C04865().start();
                if (this.isRecord) {
                    this.chronometer.setVisibility(8);
                    this.chronometer.stop();
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
                            new File(this.fileDir + this.x431fileName).delete();
                            return;
                        }
                        this.jnitest.writeEndCloseFile(this.grp, date, this.fileId, this.hlsx, this.x431fileName);
                        reportPath = this.fileDir + this.x431fileName;
                        tempPaths = Constant.getDefaultExternalStoragePathList();
                        if (tempPaths.length > 1) {
                            reportPath = reportPath.replaceAll(tempPaths[1], tempPaths[0]);
                        }
                        this.file = new File(this.fileDir + this.x431fileName);
                        if (this.file.length() <= 0) {
                            this.file.delete();
                            Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
                            return;
                        }
                        DBDao.getInstance(this.contexts).addReport(this.x431fileName, date, this.serialNo, reportPath, Contact.RELATION_FRIEND, MainActivity.database);
                        return;
                    }
                    return;
                }
                this.chronometer.setVisibility(0);
                this.chronometer.setBase(SystemClock.elapsedRealtime());
                this.chronometer.start();
                this.isRecord = f1283D;
                this.basicFlag = false;
                Constant.bBasicFlag = false;
                this.hlsx = this.jnitest.init();
                r0 = new SimpleDateFormat("yyyyMMddHHmmss");
                String date = r0.format(new Date());
                this.x431fileName = this.name + date + ".x431";
                this.fileId = this.jnitest.creatFile(this.x431fileName, this.language, this.diagversion, this.serialNo, this.hlsx);
                if (this.fileId > 0) {
                    this.isRecord = f1283D;
                    this.grp = this.jnitest.writeNewGroup(this.fileId, this.softPackageId, date);
                    this.openFlag = f1283D;
                    Constant.bOpenFlag = f1283D;
                    return;
                }
                this.date.setVisibility(0);
                this.chronometer.setVisibility(8);
                this.chronometer.stop();
                this.isRecord = false;
                this.openFlag = false;
                Constant.bOpenFlag = false;
                Toast.makeText(this, C0136R.string.fail_creat_file, 0).show();
                return;
            }
            Toast.makeText(this, C0136R.string.plase_start, 0).show();
        } else if (v.getId() == C0136R.id.start) {
            if (this.isReceiver) {
                this.isReceiver = false;
                this.date.setVisibility(8);
                this.mHandler.sendEmptyMessage(Constant.GRAPHIC_ICON_NUM_CHANGE);
                this.startButton.setImageResource(C0136R.drawable.right_bigen_selector);
                this.chronometer.setVisibility(8);
                this.chronometer.stop();
                this.isRecord = f1283D;
                r0 = new SimpleDateFormat("yyyyMMddHHmmss");
                date = r0.format(new Date());
                if (this.openFlag) {
                    this.openFlag = false;
                    Constant.bOpenFlag = false;
                    cols = this.jnitest.readGroupItemColCount(this.grp);
                    num = this.jnitest.readGroupItemCount(this.grp);
                    if (cols <= 0 || num <= 0) {
                        Toast.makeText(this, getResources().getString(C0136R.string.record_time_short), 1).show();
                        File file = new File(this.fileDir + this.x431fileName);
                        if (file.exists()) {
                            file.delete();
                            return;
                        }
                        return;
                    }
                    this.jnitest.writeEndCloseFile(this.grp, date, this.fileId, this.hlsx, this.x431fileName);
                    reportPath = this.fileDir + this.x431fileName;
                    this.file = new File(this.fileDir + this.x431fileName);
                    if (this.file.length() <= 0) {
                        this.file.delete();
                        Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
                        return;
                    }
                    DBDao.getInstance(this.contexts).addReport(this.x431fileName, date, this.serialNo, reportPath, Contact.RELATION_FRIEND, MainActivity.database);
                    return;
                }
                return;
            }
            this.isReceiver = f1283D;
            this.isRecord = false;
            this.date.setVisibility(0);
            this.mHandler.sendEmptyMessage(Constant.GRAPHIC_ICON_NUM_CHANGE);
            this.startButton.setImageResource(C0136R.drawable.right_stop_focuses);
        } else if (v.getId() == C0136R.id.date) {
            if (this.isRecord) {
                Toast.makeText(this, this.contexts.getResources().getText(C0136R.string.data_stream_stop_record), 0).show();
                return;
            }
            this.dataStreamList.clear();
            for (int i = 0; i < this.vwDataStreamIdlist.size(); i++) {
                DataStream dataStream = new DataStream();
                this.vwDataStreamIdItem = (SptVwDataStreamIdItem) this.vwDataStreamIdlist.get(i);
                dataStream.setCount(i);
                dataStream.setName(this.vwDataStreamIdItem.getStreamTextIdContent());
                dataStream.setStreamState(this.vwDataStreamIdItem.getStreamUnitId());
                dataStream.setStreamStr(this.vwDataStreamIdItem.getStreamStr());
                if (DataStreamUtils.isDigit(this.vwDataStreamIdItem.getStreamStr())) {
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
            for (Entry entry : VWDataStreamAdapter.getIsSelected().entrySet()) {
                if (((Boolean) entry.getValue()).booleanValue()) {
                    IntData intData = new IntData();
                    intData.setItem(Integer.valueOf(entry.getKey().toString()).intValue());
                    intData.setItemCheckedState(f1283D);
                    this.intDataForItemSelected.add(intData);
                }
            }
            if (this.intDataForItemSelected.size() > 0) {
                Intent active = new Intent(this.contexts, DataStreamChartTabActivity.class);
                active.putExtra("siteList", this.intDataForItemSelected);
                active.putExtra("DataList", (Serializable) tempLists);
                active.putExtra("times", this.times);
                active.putExtra("SPT_VW_DATASTREAM_ID", this.vwDataStreamIdlist);
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
            } else if (checksd(0, f1283D)) {
                this.jilu.setClickable(false);
                this.jilu.setBackgroundResource(C0136R.drawable.record_hui_selector);
                this.startButton.setClickable(false);
                this.startButton.setImageResource(C0136R.drawable.right_close_selector);
                new C04876().start();
                if (this.isRecord) {
                    this.date.setVisibility(0);
                    this.mHandler.sendEmptyMessage(Constant.GRAPHIC_ICON_NUM_CHANGE);
                    this.chronometer.setVisibility(8);
                    this.chronometer.stop();
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
                            new File(this.fileDir + this.x431fileName).delete();
                            return;
                        }
                        this.jnitest.writeEndCloseFile(this.grp, date, this.fileId, this.hlsx, this.x431fileName);
                        reportPath = this.fileDir + this.x431fileName;
                        tempPaths = Constant.getDefaultExternalStoragePathList();
                        if (tempPaths.length > 1) {
                            reportPath = reportPath.replaceAll(tempPaths[1], tempPaths[0]);
                        }
                        this.file = new File(this.fileDir + this.x431fileName);
                        if (this.file.length() <= 0) {
                            this.file.delete();
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
                this.chronometer.setVisibility(0);
                this.chronometer.setBase(SystemClock.elapsedRealtime());
                this.chronometer.start();
                this.isRecord = f1283D;
                this.basicFlag = false;
                Constant.bBasicFlag = false;
                this.hlsx = this.jnitest.init();
                r0 = new SimpleDateFormat("yyyyMMddHHmmss");
                date = r0.format(new Date());
                this.x431fileName = this.name + date + ".x431";
                this.fileId = this.jnitest.creatFile(this.x431fileName, this.language, this.diagversion, this.serialNo, this.hlsx);
                if (this.fileId > 0) {
                    this.isRecord = f1283D;
                    this.grp = this.jnitest.writeNewGroup(this.fileId, this.softPackageId, date);
                    this.openFlag = f1283D;
                    Constant.bOpenFlag = f1283D;
                    return;
                }
                this.date.setVisibility(0);
                this.chronometer.setVisibility(8);
                this.chronometer.stop();
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
        view.setDrawingCacheEnabled(f1283D);
        Bitmap bitmap = view.getDrawingCache();
        if (checksd((long) bitmap.getByteCount(), false)) {
            saveBitmaps(zoomBitmap(bitmap));
        }
        view.setDrawingCacheEnabled(false);
    }

    protected void onPause() {
        super.onPause();
        this.cishu++;
    }

    public void SaveTxt(String name, boolean ss) {
        Exception e;
        getDate();
        boolean isOk = f1283D;
        File files = null;
        String tempSDCardDir = XmlPullParser.NO_NAMESPACE;
        String date = XmlPullParser.NO_NAMESPACE;
        try {
            date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            this.sdCardDir = Constant.DST_FILE;
            File dirFile = new File(this.sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            name = name.replaceAll("EOBD2", "EOBD");
            this.pathTxt = this.sdCardDir + name + date + ".txt";
            File files2 = new File(this.pathTxt);
            try {
                if (files2.exists()) {
                    files = files2;
                } else {
                    FileOutputStream outStream = new FileOutputStream(this.pathTxt, f1283D);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outStream, AsyncHttpResponseHandler.DEFAULT_CHARSET);
                    outputStreamWriter.write(this.sb.toString());
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                    outStream.close();
                    String[] tempPaths = Constant.getDefaultExternalStoragePathList();
                    tempSDCardDir = this.sdCardDir;
                    if (tempPaths.length > 1) {
                        tempSDCardDir = tempSDCardDir.replaceAll(tempPaths[1], tempPaths[0]);
                    }
                    tempSDCardDir = new StringBuilder(String.valueOf(tempSDCardDir)).append(name).append(date).append(".txt").toString();
                    files = files2;
                }
            } catch (Exception e2) {
                e = e2;
                files = files2;
                isOk = false;
                if (e.toString().equals("java.io.IOException: write failed: ENOSPC (No space left on device)")) {
                    Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
                } else {
                    Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.io_exception), 0).show();
                }
                if (!isOk) {
                    DBDao.getInstance(this).addReport(new StringBuilder(String.valueOf(name)).append(date).append(".txt").toString(), date, this.serialNo, tempSDCardDir, Contact.RELATION_ASK, MainActivity.database);
                    toSeeReport(this.pathTxt);
                } else if (files != null) {
                } else {
                    return;
                }
            }
        } catch (Exception e3) {
            e = e3;
            isOk = false;
            if (e.toString().equals("java.io.IOException: write failed: ENOSPC (No space left on device)")) {
                Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
            } else {
                Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.io_exception), 0).show();
            }
            if (!isOk) {
                DBDao.getInstance(this).addReport(new StringBuilder(String.valueOf(name)).append(date).append(".txt").toString(), date, this.serialNo, tempSDCardDir, Contact.RELATION_ASK, MainActivity.database);
                toSeeReport(this.pathTxt);
            } else if (files != null) {
                return;
            }
        }
        if (!isOk) {
            DBDao.getInstance(this).addReport(new StringBuilder(String.valueOf(name)).append(date).append(".txt").toString(), date, this.serialNo, tempSDCardDir, Contact.RELATION_ASK, MainActivity.database);
            toSeeReport(this.pathTxt);
        } else if (files != null && files.exists()) {
            files.delete();
        }
    }

    private void toSeeReport(String path) {
        Builder builder = new Builder(this.contexts);
        builder.setTitle(C0136R.string.report_toast).setPositiveButton(C0136R.string.to_see_report, new C04887(path)).setNegativeButton(C0136R.string.cancel, new C04898()).setCancelable(false);
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

    public void saveBitmaps(Bitmap bitmap) {
        String imagePath = XmlPullParser.NO_NAMESPACE;
        String tempSDCardDir = XmlPullParser.NO_NAMESPACE;
        if (Environment.getExternalStorageState().equals("mounted")) {
            this.sdCardDir = Constant.DST_FILE;
            File dirFile = new File(this.sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            this.name = this.name.replaceAll("EOBD2", "EOBD");
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            file = new File(this.sdCardDir, this.name + date + Util.PHOTO_DEFAULT_EXT);
            if (!file.exists()) {
                return;
            }
            if (file.length() > 0) {
                this.dao.addReport(this.name + date + Util.PHOTO_DEFAULT_EXT, date, this.serialNo, tempSDCardDir, Contact.RELATION_ASK, MainActivity.database);
                toSeeReport(imagePath);
                return;
            }
            file.delete();
            Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.circleGetImageFail), 0).show();
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
        int y = (main_height + frame.top) + 50;
        return Bitmap.createBitmap(target, 0, y, width, height - y, matrix, f1283D);
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
        this.myIntentFilter.addAction("CLOSE_DATASTREAM_ACTIVITY");
        registerReceiver(this.receiver, this.myIntentFilter);
        this.flag2 = Boolean.valueOf(f1283D);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 && keyCode != Service.SUNRPC) {
            return super.onKeyDown(keyCode, event);
        }
        keyBackDo();
        return f1283D;
    }

    private boolean checksd(long filenght, boolean record) {
        long sdcardUsableSize = CRPTools.getUsableSDCardSize();
        if (record) {
            sdcardUsableSize = (long) (((float) sdcardUsableSize) / 1048576.0f);
            if (sdcardUsableSize > 5) {
                return f1283D;
            }
            if (sdcardUsableSize <= 2) {
                Toast.makeText(this.contexts, getString(C0136R.string.clean_storage_data), 0).show();
                return false;
            }
            Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.lower_storage), 0).show();
            return false;
        } else if (filenght <= sdcardUsableSize) {
            return f1283D;
        } else {
            Toast.makeText(this.contexts, this.contexts.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
            return false;
        }
    }

    private void back() {
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
            this.isReceiver = f1283D;
        }
    }

    private boolean keyBackDo() {
        FreeMemory.getInstance(this).freeMemory();
        if (this.isRecord) {
            Toast.makeText(this, getResources().getString(C0136R.string.data_stream_stop_record), 0).show();
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
            this.isReceiver = f1283D;
        }
        return f1283D;
    }
}

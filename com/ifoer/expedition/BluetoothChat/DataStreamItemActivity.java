package com.ifoer.expedition.BluetoothChat;

import CRP.utils.CRPTools;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import android.os.SystemClock;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
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
import com.ifoer.expeditionphone.MainActivity;
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
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class DataStreamItemActivity extends Activity implements OnClickListener {
    private static final boolean f1240D = false;
    private final int MSG_PRINT_RESULT;
    private final int MSG_PRINT_START;
    Handler baseHandler;
    Bitmap bit_first;
    Bitmap bit_second;
    Runnable bmp;
    private BadgeView bv;
    private int count;
    private SptExDataStreamIdItem exDataStreamIdItem;
    private int graphicNum;
    private ArrayList<IntData> intDataForItemSelected;
    private long lastTime;
    private boolean mBasicFlag;
    private Bundle mBundle;
    private Chronometer mChronometer;
    DataStreamAdapter mContentAdapter;
    private Context mContext;
    private int mCurrentCheckedItem;
    private TextView mDataStreamItemContent;
    private ArrayList<DataStream> mDataStreamList;
    private TextView mDataStreamTitle;
    private String mDiagversion;
    private Dialog mDialog;
    private String mFileDir;
    private int mFileId;
    private GraphView mGraphView;
    private int mGrp;
    @SuppressLint({"HandlerLeak"})
    private final Handler mHandler;
    private Button mHelpButton;
    private int mHlsx;
    private Button mImageButton;
    private Boolean mIsExecuteC;
    private boolean mIsReceiver;
    private boolean mIsRecord;
    private JniX431FileTest mJniTest;
    private String mLanguage;
    private ListView mListView;
    private List<ArrayList<?>> mLists;
    String mName;
    private boolean mOpenFlag;
    private ProgressDialog mProgressDialog;
    private mBroadcastReceiver mReceiver;
    private Button mRecordButton;
    private String mSerialNo;
    private String mSoftPackageId;
    ArrayList<SptExDataStreamIdItem> mSptDataStreamList;
    private DataStreamTaskManager mTaskManager;
    private Timer mTimer;
    private int mTimes;
    private String mVerLocal;
    private String mX431fileName;
    private IntentFilter myIntentFilter;
    private Button next_page_lay;
    private Button pre_page_lay;
    Bitmap printBitmap;
    int printResult;
    SptExDataStreamIdItem printSptExDataStreamIdItem;
    private int recordTime;
    private StringBuffer sb;
    private int time;
    private final int totleNum;

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamItemActivity.1 */
    class C03901 implements Runnable {
        C03901() {
        }

        public void run() {
            NetPOSPrinter bmpPrinter = new NetPOSPrinter(DataStreamItemActivity.this);
            DataStreamItemActivity.this.bit_first = bmpPrinter.drawBitFirst();
            DataStreamItemActivity.this.bit_second = bmpPrinter.drawBitSecond(DataStreamItemActivity.this.sb.toString());
            DataStreamItemActivity.this.printBitmap = NetPOSPrinter.mixtureBitmap(DataStreamItemActivity.this.bit_first, DataStreamItemActivity.this.bit_second);
            DataStreamItemActivity.this.printResult = bmpPrinter.printPic(DataStreamItemActivity.this.printBitmap);
            bmpPrinter.resultToast(DataStreamItemActivity.this.printResult);
            DataStreamItemActivity.this.baseHandler.obtainMessage(0, Integer.valueOf(DataStreamItemActivity.this.printResult)).sendToTarget();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamItemActivity.2 */
    class C03912 extends Handler {
        C03912() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case InBandBytestreamManager.MAXIMUM_BLOCK_SIZE /*65535*/:
                    DataStreamItemActivity.this.dayin();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamItemActivity.3 */
    class C03923 extends Handler {
        C03923() {
        }

        @SuppressLint({"NewApi"})
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    DataStreamItemActivity.this.mContentAdapter.refresh(DataStreamItemActivity.this.mSptDataStreamList);
                    DataStreamItemActivity.this.mTaskManager.addDownloadTask(new DataStreamTask(DataStreamItemActivity.this.mContext, DataStreamItemActivity.this.mSptDataStreamList, DataStreamItemActivity.this.mLists, DataStreamItemActivity.this.mJniTest, DataStreamItemActivity.this.mGrp, DataStreamItemActivity.this.mOpenFlag, DataStreamItemActivity.this.mBasicFlag, DataStreamItemActivity.this.mIsReceiver, DataStreamItemActivity.this.mIsRecord, DataStreamItemActivity.this.mHandler));
                case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                    DataStreamItemActivity.this.mOpenFlag = false;
                    DataStreamItemActivity.this.mBasicFlag = false;
                    Constant.bBasicFlag = false;
                    Constant.bOpenFlag = false;
                case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
                    Constant.needWait = false;
                    DataStreamItemActivity.this.mOpenFlag = true;
                    DataStreamItemActivity.this.mBasicFlag = true;
                    Constant.bBasicFlag = true;
                    Constant.bOpenFlag = true;
                case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                    DataStreamItemActivity dataStreamItemActivity = DataStreamItemActivity.this;
                    dataStreamItemActivity.mTimes = dataStreamItemActivity.mTimes + 1;
                    if (DataStreamItemActivity.this.mDialog != null && DataStreamItemActivity.this.mDialog.isShowing()) {
                        DataStreamItemActivity.this.mGraphView.pushDataToChart(DataStreamItemActivity.this.mLists, (double) DataStreamItemActivity.this.mTimes, DataStreamItemActivity.this.mCurrentCheckedItem);
                    }
                case WelcomeActivity.GPIO_IOCQDATAOUT /*19*/:
                    DataStreamItemActivity.this.mRecordButton.setBackgroundResource(C0136R.drawable.record_bai_selector);
                    DataStreamItemActivity.this.mRecordButton.setClickable(true);
                case Constant.GRAPHIC_ICON_NUM_CHANGE /*911*/:
                    if (DataStreamItemActivity.this.graphicNum <= 0 || DataStreamItemActivity.this.mImageButton.getVisibility() != 0) {
                        DataStreamItemActivity.this.bv.setVisibility(8);
                        return;
                    }
                    DataStreamItemActivity.this.bv.setText(String.valueOf(DataStreamItemActivity.this.graphicNum));
                    DataStreamItemActivity.this.bv.setGravity(1);
                    DataStreamItemActivity.this.bv.setTextSize(12.0f);
                    DataStreamItemActivity.this.bv.setBadgePosition(2);
                    DataStreamItemActivity.this.bv.setBackgroundDrawable(DataStreamItemActivity.this.getResources().getDrawable(C0136R.drawable.data_stream_point));
                    DataStreamItemActivity.this.bv.show();
                case Constant.PRE_PAGE_UNAVAILABLE /*995*/:
                    DataStreamItemActivity.this.pre_page_lay.setBackground(DataStreamItemActivity.this.getResources().getDrawable(C0136R.drawable.btn_pre_page_unselector));
                case Constant.PRE_PAGE_AVAILABLE /*996*/:
                    DataStreamItemActivity.this.pre_page_lay.setBackground(DataStreamItemActivity.this.getResources().getDrawable(C0136R.drawable.btn_pre_page_selector));
                case Constant.NEXT_PAGE_UNAVAILABLE /*997*/:
                    DataStreamItemActivity.this.next_page_lay.setBackground(DataStreamItemActivity.this.getResources().getDrawable(C0136R.drawable.btn_next_page_unselector));
                case Constant.NEXT_PAGE_AVAILABLE /*998*/:
                    DataStreamItemActivity.this.next_page_lay.setBackground(DataStreamItemActivity.this.getResources().getDrawable(C0136R.drawable.btn_next_page_selector));
                case 10101010:
                    if (DataStreamItemActivity.this.mProgressDialog != null && DataStreamItemActivity.this.mProgressDialog.isShowing()) {
                        SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    }
                    SimpleDialog.checkConectior(DataStreamItemActivity.this.mContext, DataStreamItemActivity.this.getString(C0136R.string.initializeTilte), DataStreamItemActivity.this.getString(C0136R.string.check_connector));
                default:
            }
        }

        @SuppressLint({"NewApi"})
        private void setButtonStyle(TextView diyBtn, int resources, boolean position) {
            if (position) {
                diyBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, DataStreamItemActivity.this.getResources().getDrawable(resources), null, null);
            } else {
                diyBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, DataStreamItemActivity.this.getResources().getDrawable(resources), null, null);
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamItemActivity.4 */
    class C03934 implements OnChronometerTickListener {
        C03934() {
        }

        public void onChronometerTick(Chronometer arg0) {
            DataStreamItemActivity dataStreamItemActivity = DataStreamItemActivity.this;
            dataStreamItemActivity.time = dataStreamItemActivity.time + 1;
            Log.e("DataStreamItem", " mChronometer time" + DataStreamItemActivity.this.time);
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamItemActivity.5 */
    class C03945 implements OnItemClickListener {
        C03945() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
            Item holder = (Item) view.getTag();
            SptExDataStreamIdItem exDataStreamIdItemtemp = (SptExDataStreamIdItem) DataStreamItemActivity.this.mSptDataStreamList.get(position);
            if (XmlPullParser.NO_NAMESPACE.equals(exDataStreamIdItemtemp.getStreamState()) || !DataStreamUtils.isNumeric(exDataStreamIdItemtemp.getStreamStr())) {
                Toast.makeText(DataStreamItemActivity.this, DataStreamItemActivity.this.getResources().getString(C0136R.string.data_stream_item_select_show_info), 0).show();
                return;
            }
            holder.flagIcon.toggle();
            DataStreamItemActivity dataStreamItemActivity;
            if (holder.flagIcon.isChecked()) {
                if (DataStreamItemActivity.this.graphicNum > 3) {
                    Toast.makeText(DataStreamItemActivity.this, DataStreamItemActivity.this.getResources().getString(C0136R.string.data_stream_max_item_info_scanPad_mini), 0).show();
                    holder.flagIcon.setChecked(false);
                    return;
                }
                dataStreamItemActivity = DataStreamItemActivity.this;
                dataStreamItemActivity.graphicNum = dataStreamItemActivity.graphicNum + 1;
            } else if (DataStreamItemActivity.this.graphicNum > 0) {
                dataStreamItemActivity = DataStreamItemActivity.this;
                dataStreamItemActivity.graphicNum = dataStreamItemActivity.graphicNum - 1;
            }
            DataStreamAdapter.getIsSelected().put(Integer.valueOf(position), Boolean.valueOf(holder.flagIcon.isChecked()));
            DataStreamItemActivity.this.mHandler.sendEmptyMessage(Constant.GRAPHIC_ICON_NUM_CHANGE);
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamItemActivity.6 */
    class C03956 extends Thread {
        C03956() {
        }

        public void run() {
            try {
                C03956.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            DataStreamItemActivity.this.mHandler.sendEmptyMessage(19);
        }
    }

    class MyTimerTask extends TimerTask {
        MyTimerTask() {
        }

        public void run() {
            CToJava.dataStreamFlag = Boolean.valueOf(true);
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {

        /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamItemActivity.mBroadcastReceiver.1 */
        class C03961 extends Thread {
            C03961() {
            }

            public void run() {
                try {
                    C03961.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Process.killProcess(Process.myPid());
                DataStreamItemActivity.this.finish();
                DataStreamItemActivity.this.overridePendingTransition(0, 0);
            }
        }

        /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamItemActivity.mBroadcastReceiver.2 */
        class C03972 extends Thread {
            C03972() {
            }

            public void run() {
                DataStreamItemActivity.this.mHandler.sendMessage(DataStreamItemActivity.this.mHandler.obtainMessage(15));
            }
        }

        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (DataStreamItemActivity.this.mIsExecuteC.booleanValue()) {
                if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - DataStreamItemActivity.this.lastTime) / 1000 > 30) {
                    SimpleDialog.ExitDialog(context);
                }
                if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                    DataStreamItemActivity.this.lastTime = System.currentTimeMillis();
                }
                if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    Toast.makeText(DataStreamItemActivity.this.getApplicationContext(), DataStreamItemActivity.this.getResources().getString(C0136R.string.connectionLost), 0).show();
                    new C03961().start();
                } else if (intent.getAction().equals("feedbackMeauData")) {
                    ArrayList<MenuData> menuDataLists = (ArrayList) intent.getExtras().getSerializable("menuData");
                    r0 = new Intent(DataStreamItemActivity.this, CarDiagnoseActivityTwo.class);
                    r0.putExtra("menuData", menuDataLists);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    DataStreamItemActivity.this.startActivity(r0);
                    DataStreamItemActivity.this.overridePendingTransition(0, 0);
                    DataStreamItemActivity.this.finish();
                    DataStreamItemActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_DS_MENU_ID")) {
                    ArrayList<MenuData> menuDataList = (ArrayList) intent.getExtras().getSerializable("SPT_DS_MENU_ID");
                    r0 = new Intent(DataStreamItemActivity.this, CarDiagnoseActivityTwo.class);
                    r0.putExtra("menuData", menuDataList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    DataStreamItemActivity.this.startActivity(r0);
                    DataStreamItemActivity.this.overridePendingTransition(0, 0);
                    DataStreamItemActivity.this.finish();
                    DataStreamItemActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_ACTIVE_TEST")) {
                    SptActiveTest sptActiveTest = (SptActiveTest) intent.getExtras().getSerializable("ACTIVE_TEST");
                    Intent active = new Intent(DataStreamItemActivity.this, ActiveTestActivity.class);
                    active.putExtra("ACTIVE_TEST", sptActiveTest);
                    active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    DataStreamItemActivity.this.startActivity(active);
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    DataStreamItemActivity.this.overridePendingTransition(0, 0);
                    DataStreamItemActivity.this.finish();
                    DataStreamItemActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_NOBUTTONBOX_TEXT")) {
                    if (DataStreamItemActivity.this.mProgressDialog == null) {
                        DataStreamItemActivity.this.mProgressDialog = new ProgressDialog(DataStreamItemActivity.this.mContext);
                    }
                    Spt_Nobuttonbox_Text nobuttonbox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("Nobuttonbox");
                    SimpleDialog.openProgressDialog(DataStreamItemActivity.this.mContext, nobuttonbox.getTitle(), nobuttonbox.getContent(), DataStreamItemActivity.this.mProgressDialog);
                } else if (intent.getAction().equals("closeNobuttonBox")) {
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE")) {
                    ArrayList<SptTroubleTest> troubleTestList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE");
                    r0 = new Intent(DataStreamItemActivity.this, FaultCodeActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE", troubleTestList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    DataStreamItemActivity.this.startActivity(r0);
                    DataStreamItemActivity.this.overridePendingTransition(0, 0);
                    DataStreamItemActivity.this.finish();
                    DataStreamItemActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EXIT_SHOW_WINDOW")) {
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                    DataStreamItemActivity.this.finish();
                    DataStreamItemActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EXIT_SHOW_WINDOW")) {
                    if (DataStreamItemActivity.this.mOpenFlag) {
                        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        DataStreamItemActivity.this.mOpenFlag = false;
                        Constant.bOpenFlag = false;
                        DataStreamItemActivity.this.mJniTest.writeEndCloseFile(DataStreamItemActivity.this.mGrp, date, DataStreamItemActivity.this.mFileId, DataStreamItemActivity.this.mHlsx, DataStreamItemActivity.this.mX431fileName);
                        DBDao.getInstance(DataStreamItemActivity.this.mContext).addReport(DataStreamItemActivity.this.mX431fileName, date, DataStreamItemActivity.this.mSerialNo, new StringBuilder(String.valueOf(DataStreamItemActivity.this.mFileDir)).append(DataStreamItemActivity.this.mX431fileName).toString(), Contact.RELATION_FRIEND, MainActivity.database);
                    }
                    DataStreamItemActivity.this.mTimer.cancel();
                    CToJava.streamFlag = Boolean.valueOf(false);
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    DataStreamItemActivity.this.finish();
                    DataStreamItemActivity.this.overridePendingTransition(0, 0);
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                } else if (intent.getAction().equals("SPT_STREAM_SELECT_ID_EX")) {
                    DataStreamItemActivity.this.mTimer.cancel();
                    ArrayList<SptStreamSelectIdItem> streamSelectIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_STREAM_SELECT_ID_EX");
                    r0 = new Intent(DataStreamItemActivity.this, StreamSelectActivity.class);
                    r0.putExtra("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    DataStreamItemActivity.this.startActivity(r0);
                    DataStreamItemActivity.this.overridePendingTransition(0, 0);
                    DataStreamItemActivity.this.finish();
                    DataStreamItemActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                    ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                    r0 = new Intent(DataStreamItemActivity.this, DataStreamActivity.class);
                    r0.putExtra("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    DataStreamItemActivity.this.startActivity(r0);
                    DataStreamItemActivity.this.overridePendingTransition(0, 0);
                    DataStreamItemActivity.this.finish();
                    DataStreamItemActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_MESSAGEBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) intent.getExtras().getSerializable("SPT_MESSAGEBOX_TEXT");
                    SimpleDialogControl.showDiaglog(context, sptMessageBoxText.getDialogType(), sptMessageBoxText);
                } else if (intent.getAction().equals("ConnectionLost")) {
                    Toast.makeText(DataStreamItemActivity.this.getApplicationContext(), DataStreamItemActivity.this.getResources().getString(C0136R.string.devlost), 0).show();
                } else if (intent.getAction().equals("SPT_INPUTSTRING_EX")) {
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    SptInputStringEx inputString = (SptInputStringEx) intent.getExtras().getSerializable("SPT_INPUTSTRING_EX");
                    SimpleDialog.sptInputStringExDiagnose(DataStreamItemActivity.this.mContext, inputString.getDialogTitle(), inputString.getDialogContent(), inputString.getInputHint());
                } else if (intent.getAction().equals("SPT_INPUT_NUMERIC")) {
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    SptInputNumric inputNumric = (SptInputNumric) intent.getExtras().getSerializable("SPT_INPUT_NUMERIC");
                    SimpleDialog.sptInputNumericDiagnose(DataStreamItemActivity.this.mContext, inputNumric.getDialogTitle(), inputNumric.getDialogContent(), inputNumric.getInputHint(), inputNumric.getDigit());
                } else if (intent.getAction().equals("SPT_INPUTBOX_TEXT")) {
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    Spt_Nobuttonbox_Text inputBox = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTBOX_TEXT");
                    SimpleDialog.sptInputBoxTextDiagnose(DataStreamItemActivity.this.mContext, inputBox.getTitle(), inputBox.getContent());
                } else if (intent.getAction().equals("SPT_INPUTSTRING")) {
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    Spt_Nobuttonbox_Text inputStr = (Spt_Nobuttonbox_Text) intent.getExtras().getSerializable("SPT_INPUTSTRING");
                    SimpleDialog.sptInputStringDiagnose(DataStreamItemActivity.this.mContext, inputStr.getTitle(), inputStr.getContent());
                } else if (intent.getAction().equals("SPT_TROUBLE_CODE_FROZEN")) {
                    ArrayList<SptTroubleTest> troubleCodeFrozenList = (ArrayList) intent.getExtras().getSerializable("SPT_TROUBLE_CODE_FROZEN");
                    r0 = new Intent(DataStreamItemActivity.this, FaultCodeFrozenActivity.class);
                    r0.putExtra("SPT_TROUBLE_CODE_FROZEN", troubleCodeFrozenList);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    DataStreamItemActivity.this.startActivity(r0);
                    DataStreamItemActivity.this.overridePendingTransition(0, 0);
                    DataStreamItemActivity.this.finish();
                    DataStreamItemActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                    ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                    r0 = new Intent(DataStreamItemActivity.this, VWDataStreamActivity.class);
                    r0.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                    r0.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    DataStreamItemActivity.this.startActivity(r0);
                    DataStreamItemActivity.this.overridePendingTransition(0, 0);
                    DataStreamItemActivity.this.finish();
                    DataStreamItemActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_SHOW_PICTURE")) {
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    SimpleDialog.sptShowPictureDiagnose(DataStreamItemActivity.this.mContext, intent.getExtras().getString("SPT_SHOW_PICTURE"));
                } else if (intent.getAction().equals("SPT_DATASTREAM_ID_EX")) {
                    SimpleDialog.closeProgressDialog(DataStreamItemActivity.this.mProgressDialog);
                    DataStreamItemActivity.this.mSptDataStreamList = null;
                    DataStreamItemActivity.this.mSptDataStreamList = (ArrayList) intent.getExtras().getSerializable("SPT_DATASTREAM_ID_EX");
                    new C03972().start();
                }
            }
        }
    }

    public DataStreamItemActivity() {
        this.mDialog = null;
        this.mGraphView = null;
        this.mCurrentCheckedItem = 0;
        this.mIsExecuteC = Boolean.valueOf(false);
        this.mTimer = new Timer();
        this.mLists = new ArrayList();
        this.mJniTest = new JniX431FileTest();
        this.mOpenFlag = false;
        this.mBasicFlag = false;
        this.mIsReceiver = true;
        this.mIsRecord = false;
        this.mFileDir = XmlPullParser.NO_NAMESPACE;
        this.mDataStreamList = new ArrayList();
        this.exDataStreamIdItem = null;
        this.intDataForItemSelected = new ArrayList();
        this.MSG_PRINT_RESULT = 0;
        this.MSG_PRINT_START = InBandBytestreamManager.MAXIMUM_BLOCK_SIZE;
        this.sb = null;
        this.count = 0;
        this.graphicNum = 0;
        this.totleNum = 3;
        this.time = 0;
        this.recordTime = 0;
        this.bmp = new C03901();
        this.baseHandler = new C03912();
        this.mHandler = new C03923();
    }

    public void dayin() {
        getPrintDate();
        new Thread(this.bmp).start();
    }

    private void getPrintDate() {
        this.sb = new StringBuffer();
        for (int i = 0; i < this.mSptDataStreamList.size(); i++) {
            this.printSptExDataStreamIdItem = (SptExDataStreamIdItem) this.mSptDataStreamList.get(i);
            this.sb.append(new StringBuilder(String.valueOf(this.printSptExDataStreamIdItem.getStreamTextIdContent())).append("   ").append(this.printSptExDataStreamIdItem.getStreamStr()).append(this.printSptExDataStreamIdItem.getStreamState()).append(SpecilApiUtil.LINE_SEP).toString());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.data_stream_34);
        this.mContext = this;
        if (MySharedPreferences.share == null) {
            MySharedPreferences.getSharedPref(getApplicationContext());
        }
        MyApplication.getInstance().addActivity(this);
        DataStreamTaskManager.getInstance();
        new Thread(new DataStreamTaskManagerThread()).start();
        this.mTaskManager = DataStreamTaskManager.getInstance();
        this.mDataStreamTitle = (TextView) findViewById(C0136R.id.data_stream_title_text);
        this.mDataStreamItemContent = (TextView) findViewById(C0136R.id.list_item_content);
        this.mImageButton = (Button) findViewById(C0136R.id.image_button);
        this.bv = new BadgeView((Context) this, this.mImageButton);
        this.bv.setGravity(1);
        this.bv.setText(Contact.RELATION_ASK);
        this.bv.setTextSize(12.0f);
        this.bv.setBadgePosition(2);
        this.bv.setBackgroundDrawable(getResources().getDrawable(C0136R.drawable.data_stream_point));
        this.mRecordButton = (Button) findViewById(C0136R.id.record_button);
        this.mHelpButton = (Button) findViewById(C0136R.id.help_button);
        this.mImageButton.setOnClickListener(this);
        this.mRecordButton.setOnClickListener(this);
        this.mHelpButton.setOnClickListener(this);
        this.pre_page_lay = (Button) findViewById(C0136R.id.pre_page_lay);
        this.next_page_lay = (Button) findViewById(C0136R.id.next_page_lay);
        this.pre_page_lay.setOnClickListener(this);
        this.next_page_lay.setOnClickListener(this);
        this.mListView = (ListView) findViewById(C0136R.id.list_view);
        initView();
        registerBoradcastReceiver();
        this.mTimer = new Timer();
        this.mLanguage = Files.getLanguage();
        this.mSerialNo = MySharedPreferences.getStringValue(this.mContext, MySharedPreferences.serialNoKey);
        this.mSoftPackageId = MySharedPreferences.getStringValue(this, MySharedPreferences.savesoftPackageId);
        this.mName = this.mSoftPackageId + "_DS_";
        this.mVerLocal = MySharedPreferences.getStringValue(this.mContext, MySharedPreferences.diagnosticSoftwareVersionNo);
        this.mDiagversion = this.mSoftPackageId + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + this.mVerLocal;
        BluetoothChatService.setHandler(this.mHandler);
    }

    protected void onStart() {
        super.onStart();
        this.mFileDir = Constant.DST_FILE;
    }

    public void registerBoradcastReceiver() {
        this.mReceiver = new mBroadcastReceiver();
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
        registerReceiver(this.mReceiver, this.myIntentFilter);
    }

    private void initView() {
        this.mChronometer = (Chronometer) findViewById(C0136R.id.chronometer);
        this.mChronometer.setFormat("(%s)");
        this.mChronometer.setOnChronometerTickListener(new C03934());
        this.mBundle = getIntent().getExtras();
        this.mSptDataStreamList = new ArrayList();
        if (this.mBundle != null) {
            this.mSptDataStreamList = (ArrayList) this.mBundle.getSerializable("SPT_DATASTREAM_ID_EX");
            if (this.mSptDataStreamList.size() > 8) {
                ArrayList<SptExDataStreamIdItem> mSptDataStreamListTemp = new ArrayList();
                for (int i = 0; i <= 7; i++) {
                    mSptDataStreamListTemp.add((SptExDataStreamIdItem) this.mSptDataStreamList.get(i));
                }
                this.mSptDataStreamList.clear();
                this.mSptDataStreamList.addAll(mSptDataStreamListTemp);
            }
            this.mLists.add(this.mSptDataStreamList);
        }
        this.mContentAdapter = new DataStreamAdapter(this.mSptDataStreamList, this.mContext);
        if (Constant.streamIsSelectedList.size() <= 5) {
            this.next_page_lay.setBackground(getResources().getDrawable(C0136R.drawable.btn_next_page_unselector));
        }
        this.pre_page_lay.setBackground(getResources().getDrawable(C0136R.drawable.btn_pre_page_unselector));
        this.mListView.setAdapter(this.mContentAdapter);
        this.mListView.setOnItemClickListener(new C03945());
    }

    protected void onResume() {
        super.onResume();
        FreeMemory.getInstance(this).freeMemory();
        this.mIsExecuteC = Boolean.valueOf(true);
        this.mSerialNo = MySharedPreferences.getStringValue(this.mContext, MySharedPreferences.serialNoKey);
        this.mSoftPackageId = MySharedPreferences.getStringValue(this.mContext, MySharedPreferences.savesoftPackageId);
        this.mName = this.mSoftPackageId + "_DS_";
    }

    protected void onStop() {
        super.onStop();
        this.mIsExecuteC = Boolean.valueOf(false);
    }

    protected void onDestroy() {
        SimpleDialog.closeProgressDialog(this.mProgressDialog);
        super.onDestroy();
        if (this.mReceiver != null) {
            unregisterReceiver(this.mReceiver);
        }
        Constant.streamSelectHashMap.clear();
        Constant.streamIsSelectedList.clear();
        Constant.currentPage = 0;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 && keyCode != Service.SUNRPC) {
            return super.onKeyDown(keyCode, event);
        }
        Constant.isDataStreamChange = true;
        back();
        return true;
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.pre_page_lay) {
            initSelectDataForNext();
            if (Constant.currentPage == 0) {
                this.mHandler.sendEmptyMessage(Constant.PRE_PAGE_UNAVAILABLE);
            } else {
                this.mHandler.sendEmptyMessage(Constant.NEXT_PAGE_AVAILABLE);
                Constant.currentPage--;
                if (Constant.currentPage == 0) {
                    this.mHandler.sendEmptyMessage(Constant.PRE_PAGE_UNAVAILABLE);
                }
                if (this.mProgressDialog == null) {
                    this.mProgressDialog = new ProgressDialog(this.mContext);
                    this.mProgressDialog.setCancelable(false);
                } else {
                    this.mProgressDialog.dismiss();
                    this.mProgressDialog = null;
                    this.mProgressDialog = new ProgressDialog(this.mContext);
                }
                SimpleDialog.openProgressDialog(this.mContext, getResources().getString(C0136R.string.dataDisposeTilte), getResources().getString(C0136R.string.dataDisposeMessage), this.mProgressDialog);
                Constant.spt_data_stream_button = new byte[]{(byte) 9};
            }
        } else if (v.getId() == C0136R.id.next_page_lay) {
            initSelectDataForNext();
            if ((Constant.currentPage + 1) * Constant.streamSelectHashMap_totle >= Constant.streamIsSelectedList.size()) {
                this.mHandler.sendEmptyMessage(Constant.NEXT_PAGE_UNAVAILABLE);
            } else {
                this.mHandler.sendEmptyMessage(Constant.PRE_PAGE_AVAILABLE);
                Constant.currentPage++;
                if ((Constant.currentPage + 1) * Constant.streamSelectHashMap_totle >= Constant.streamIsSelectedList.size()) {
                    this.mHandler.sendEmptyMessage(Constant.NEXT_PAGE_UNAVAILABLE);
                }
                if (this.mProgressDialog == null) {
                    this.mProgressDialog = new ProgressDialog(this.mContext);
                    this.mProgressDialog.setCancelable(false);
                } else {
                    this.mProgressDialog.dismiss();
                    this.mProgressDialog = null;
                    this.mProgressDialog = new ProgressDialog(this.mContext);
                }
                SimpleDialog.openProgressDialog(this.mContext, getResources().getString(C0136R.string.dataDisposeTilte), getResources().getString(C0136R.string.dataDisposeMessage), this.mProgressDialog);
                Constant.spt_data_stream_button = new byte[]{(byte) 8};
            }
        }
        if (v.getId() == C0136R.id.image_button) {
            if (this.mIsRecord) {
                Toast.makeText(this, this.mContext.getResources().getText(C0136R.string.data_stream_stop_record), 0).show();
                return;
            }
            this.mDataStreamList.clear();
            for (int i = 0; i < this.mSptDataStreamList.size(); i++) {
                DataStream dataStream = new DataStream();
                this.exDataStreamIdItem = (SptExDataStreamIdItem) this.mSptDataStreamList.get(i);
                if (DataStreamUtils.isNum(this.exDataStreamIdItem.getStreamStr())) {
                    dataStream.setCount(i);
                    dataStream.setName(this.exDataStreamIdItem.getStreamTextIdContent());
                    dataStream.setStreamState(this.exDataStreamIdItem.getStreamState());
                    this.mDataStreamList.add(dataStream);
                }
            }
            List<ArrayList<?>> tempLists = new ArrayList();
            if (this.mLists.size() > 1) {
                tempLists.addAll(this.mLists);
                ArrayList tempList = (ArrayList) tempLists.get(tempLists.size() - 1);
                tempLists.clear();
                tempLists.add(tempList);
            } else {
                tempLists = this.mLists;
            }
            this.intDataForItemSelected.clear();
            for (Entry entry : DataStreamAdapter.getIsSelected().entrySet()) {
                if (((Boolean) entry.getValue()).booleanValue()) {
                    IntData intData = new IntData();
                    intData.setItem(Integer.valueOf(entry.getKey().toString()).intValue());
                    intData.setItemCheckedState(true);
                    this.intDataForItemSelected.add(intData);
                }
            }
            if (this.intDataForItemSelected.size() > 0) {
                Intent active = new Intent(this.mContext, DataStreamChartTabActivity.class);
                active.putExtra("siteList", this.intDataForItemSelected);
                active.putExtra("DataList", (Serializable) tempLists);
                active.putExtra("times", this.mTimes);
                active.putExtra("SPT_EX_DATASTREAM_ID", this.exDataStreamIdItem);
                active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                startActivity(active);
                overridePendingTransition(0, 0);
                return;
            }
            Toast.makeText(this.mContext, C0136R.string.pleaseselect, 0).show();
        } else if (v.getId() == C0136R.id.record_button) {
            if (!this.mIsReceiver) {
                Toast.makeText(this, C0136R.string.plase_start, 0).show();
            } else if (checksd(0, true)) {
                this.mRecordButton.setClickable(false);
                this.mRecordButton.setBackgroundResource(C0136R.drawable.record2);
                new C03956().start();
                if (this.mIsRecord) {
                    this.mChronometer.setVisibility(8);
                    this.mHandler.sendEmptyMessage(Constant.GRAPHIC_ICON_NUM_CHANGE);
                    this.mChronometer.stop();
                    this.recordTime = this.time;
                    this.time = 0;
                    this.mIsRecord = false;
                    String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    if (this.mOpenFlag && this.mFileId > 0) {
                        this.mOpenFlag = false;
                        Constant.bOpenFlag = false;
                        int cols = this.mJniTest.readGroupItemColCount(this.mGrp);
                        int num = this.mJniTest.readGroupItemCount(this.mGrp);
                        if (cols <= 0 || num <= 0) {
                            if (this.recordTime < 12) {
                                Toast.makeText(this, getResources().getString(C0136R.string.record_time_short), 1).show();
                            } else {
                                Toast.makeText(this, C0136R.string.fail_creat_file, 0).show();
                            }
                            File file = new File(this.mFileDir + this.mX431fileName);
                            if (file.exists()) {
                                file.delete();
                                return;
                            }
                            return;
                        }
                        this.mJniTest.writeEndCloseFile(this.mGrp, date, this.mFileId, this.mHlsx, this.mX431fileName);
                        DBDao.getInstance(this.mContext).addReport(this.mX431fileName, date, this.mSerialNo, this.mFileDir + this.mX431fileName, Contact.RELATION_FRIEND, MainActivity.database);
                        return;
                    }
                    return;
                }
                this.mChronometer.setVisibility(0);
                this.mHandler.sendEmptyMessage(Constant.GRAPHIC_ICON_NUM_CHANGE);
                this.mChronometer.setBase(SystemClock.elapsedRealtime());
                this.mChronometer.start();
                this.mIsRecord = true;
                this.mBasicFlag = false;
                Constant.bBasicFlag = false;
                this.mHlsx = this.mJniTest.init();
                String date2 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                this.mX431fileName = this.mName + date2 + ".x431";
                this.mFileId = this.mJniTest.creatFile(this.mX431fileName, this.mLanguage, this.mDiagversion, this.mSerialNo, this.mHlsx);
                if (this.mFileId > 0) {
                    this.mIsRecord = true;
                    this.mGrp = this.mJniTest.writeNewGroup(this.mFileId, this.mSoftPackageId, date2);
                    this.mOpenFlag = true;
                    Constant.bOpenFlag = true;
                    return;
                }
                this.mChronometer.setVisibility(8);
                this.mHandler.sendEmptyMessage(Constant.GRAPHIC_ICON_NUM_CHANGE);
                this.mChronometer.stop();
                this.recordTime = this.time;
                this.time = 0;
                this.mIsRecord = false;
                this.mOpenFlag = false;
                Constant.bOpenFlag = false;
                Toast.makeText(this, C0136R.string.fail_creat_file, 0).show();
            }
        } else if (v.getId() != C0136R.id.help_button && v.getId() == C0136R.id.backSuperior) {
            Constant.isDataStreamChange = true;
            back();
        }
    }

    private void back() {
        FreeMemory.getInstance(this).freeMemory();
        if (this.mIsRecord) {
            Toast.makeText(this, this.mContext.getResources().getText(C0136R.string.data_stream_stop_record), 0).show();
        } else if (Constant.mChatService == null || Constant.mChatService.getState() != 3) {
            finish();
            overridePendingTransition(0, 0);
        } else {
            if (this.mProgressDialog == null) {
                this.mProgressDialog = new ProgressDialog(this.mContext);
                this.mProgressDialog.setCancelable(false);
            } else {
                this.mProgressDialog.dismiss();
                this.mProgressDialog = null;
                this.mProgressDialog = new ProgressDialog(this.mContext);
            }
            SimpleDialog.openProgressDialog(this.mContext, getResources().getString(C0136R.string.dataDisposeTilte), getResources().getString(C0136R.string.dataDisposeMessage), this.mProgressDialog);
            Constant.spt_data_stream_button = new byte[]{(byte) 7};
        }
    }

    private boolean isString(String string) {
        return Pattern.compile("[\\u4e00-\\u9fa5]").matcher(string).find();
    }

    public static boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    private boolean checksd(long filenght, boolean record) {
        long sdcardUsableSize = CRPTools.getUsableSDCardSize();
        if (record) {
            sdcardUsableSize = (long) (((float) sdcardUsableSize) / 1048576.0f);
            if (sdcardUsableSize > 5) {
                return true;
            }
            if (sdcardUsableSize <= 2) {
                Toast.makeText(this.mContext, getString(C0136R.string.clean_storage_data), 0).show();
                return false;
            }
            Toast.makeText(this.mContext, this.mContext.getString(C0136R.string.lower_storage), 0).show();
            return false;
        } else if (filenght <= sdcardUsableSize) {
            return true;
        } else {
            Toast.makeText(this.mContext, this.mContext.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
            return false;
        }
    }

    private void initSelectDataForNext() {
        initSelectData();
        this.mHandler.sendEmptyMessage(Constant.GRAPHIC_ICON_NUM_CHANGE);
        this.mContentAdapter.notifyDataSetChanged();
    }

    private void initSelectData() {
        for (int i = 0; i <= this.mSptDataStreamList.size(); i++) {
            DataStreamAdapter.getIsSelected().put(Integer.valueOf(i), Boolean.valueOf(false));
        }
        this.graphicNum = 0;
    }
}

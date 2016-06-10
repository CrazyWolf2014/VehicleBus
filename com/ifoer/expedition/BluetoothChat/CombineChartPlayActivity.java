package com.ifoer.expedition.BluetoothChat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.ifoer.entity.Constant;
import com.ifoer.entity.IntData;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptExDataStreamIdItem34;
import com.ifoer.entity.SptVwDataStreamIdItem;
import com.ifoer.freememory.FreeMemory;
import com.ifoer.util.DataStreamChartTask;
import com.ifoer.util.DataStreamChartTaskManager;
import com.ifoer.util.DataStreamChartTaskManagerThread;
import com.ifoer.util.GraphView;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;

public class CombineChartPlayActivity extends Activity {
    private Bundle bundle;
    private int count;
    private ArrayList<?> exDataStreamIdlist;
    private Boolean isExecuteD;
    private boolean isShowChart;
    private long lastTime;
    private ArrayList<IntData> listStr;
    private List<ArrayList<?>> lists;
    private Context mContexts;
    private GraphView mGraphView;
    private final Handler mHandler;
    private ArrayList<Integer> mListCheck;
    private ProgressDialog mProgressDialog;
    private int mSize;
    private DataStreamChartTaskManager mTaskManager;
    public LinearLayout menuBtn;
    private IntentFilter myIntentFilter;
    private mBroadcastReceiver receiver;
    private double times;

    /* renamed from: com.ifoer.expedition.BluetoothChat.CombineChartPlayActivity.1 */
    class C03741 extends Handler {
        C03741() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    if (CombineChartPlayActivity.this.isExecuteD.booleanValue()) {
                        CombineChartPlayActivity.this.mTaskManager.addDownloadTask(new DataStreamChartTask(CombineChartPlayActivity.this.exDataStreamIdlist, CombineChartPlayActivity.this.lists, CombineChartPlayActivity.this.mHandler));
                    }
                case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                    CombineChartPlayActivity combineChartPlayActivity = CombineChartPlayActivity.this;
                    combineChartPlayActivity.times = combineChartPlayActivity.times + 1.0d;
                    if (CombineChartPlayActivity.this.lists != null && CombineChartPlayActivity.this.lists.size() > 0) {
                        CombineChartPlayActivity.this.mGraphView.pushDataToChartCombine(CombineChartPlayActivity.this.mSize, CombineChartPlayActivity.this.lists, CombineChartPlayActivity.this.times, CombineChartPlayActivity.this.mListCheck);
                    }
                case 10101010:
                    if (!(CombineChartPlayActivity.this.mProgressDialog == null || CombineChartPlayActivity.this.mProgressDialog == null || !CombineChartPlayActivity.this.mProgressDialog.isShowing())) {
                        SimpleDialog.closeProgressDialog(CombineChartPlayActivity.this.mProgressDialog);
                    }
                    SimpleDialog.checkConectior(CombineChartPlayActivity.this.mContexts, CombineChartPlayActivity.this.getString(C0136R.string.initializeTilte), CombineChartPlayActivity.this.getString(C0136R.string.check_connector));
                default:
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {

        /* renamed from: com.ifoer.expedition.BluetoothChat.CombineChartPlayActivity.mBroadcastReceiver.1 */
        class C03751 extends Thread {
            C03751() {
            }

            public void run() {
                CombineChartPlayActivity.this.mHandler.sendMessage(CombineChartPlayActivity.this.mHandler.obtainMessage(15));
            }
        }

        /* renamed from: com.ifoer.expedition.BluetoothChat.CombineChartPlayActivity.mBroadcastReceiver.2 */
        class C03762 extends Thread {
            C03762() {
            }

            public void run() {
                CombineChartPlayActivity.this.mHandler.sendMessage(CombineChartPlayActivity.this.mHandler.obtainMessage(15));
            }
        }

        /* renamed from: com.ifoer.expedition.BluetoothChat.CombineChartPlayActivity.mBroadcastReceiver.3 */
        class C03773 extends Thread {
            C03773() {
            }

            public void run() {
                CombineChartPlayActivity.this.mHandler.sendMessage(CombineChartPlayActivity.this.mHandler.obtainMessage(15));
            }
        }

        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (CombineChartPlayActivity.this.isExecuteD.booleanValue()) {
                if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - CombineChartPlayActivity.this.lastTime) / 1000 > 30) {
                    SimpleDialog.ExitDialog(context);
                }
                if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                    CombineChartPlayActivity.this.lastTime = System.currentTimeMillis();
                }
                if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    if (CombineChartPlayActivity.this.lists != null) {
                        CombineChartPlayActivity.this.lists.clear();
                    }
                    CombineChartPlayActivity.this.finish();
                    CombineChartPlayActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                    if (CombineChartPlayActivity.this.isShowChart) {
                        CombineChartPlayActivity.this.exDataStreamIdlist = null;
                        CombineChartPlayActivity.this.exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                        new C03751().start();
                        return;
                    }
                    ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                    troubleIntent = new Intent(CombineChartPlayActivity.this, DataStreamActivity.class);
                    troubleIntent.putExtra("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                    troubleIntent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    CombineChartPlayActivity.this.startActivity(troubleIntent);
                    CombineChartPlayActivity.this.overridePendingTransition(0, 0);
                    CombineChartPlayActivity.this.finish();
                    CombineChartPlayActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                    if (CombineChartPlayActivity.this.isShowChart) {
                        CombineChartPlayActivity.this.exDataStreamIdlist = null;
                        CombineChartPlayActivity.this.exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                        new C03762().start();
                        return;
                    }
                    ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                    troubleIntent = new Intent(CombineChartPlayActivity.this, VWDataStreamActivity.class);
                    troubleIntent.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                    troubleIntent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    CombineChartPlayActivity.this.startActivity(troubleIntent);
                    CombineChartPlayActivity.this.overridePendingTransition(0, 0);
                    CombineChartPlayActivity.this.finish();
                    CombineChartPlayActivity.this.overridePendingTransition(0, 0);
                } else if (!intent.getAction().equals("SPT_DATASTREAM_ID_EX")) {
                } else {
                    if (CombineChartPlayActivity.this.isShowChart) {
                        CombineChartPlayActivity.this.exDataStreamIdlist = null;
                        CombineChartPlayActivity.this.exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_DATASTREAM_ID_EX");
                        new C03773().start();
                        return;
                    }
                    ArrayList<SptExDataStreamIdItem34> exDataStreamIdlist2 = (ArrayList) intent.getExtras().getSerializable("SPT_DATASTREAM_ID_EX");
                    troubleIntent = new Intent(CombineChartPlayActivity.this, DataStreamItemActivity.class);
                    troubleIntent.putExtra("SPT_DATASTREAM_ID_EX", exDataStreamIdlist2);
                    troubleIntent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    CombineChartPlayActivity.this.startActivity(troubleIntent);
                    CombineChartPlayActivity.this.overridePendingTransition(0, 0);
                    CombineChartPlayActivity.this.mProgressDialog.dismiss();
                    CombineChartPlayActivity.this.finish();
                    CombineChartPlayActivity.this.overridePendingTransition(0, 0);
                }
            }
        }
    }

    public CombineChartPlayActivity() {
        this.listStr = null;
        this.lists = new ArrayList();
        this.times = 0.0d;
        this.mGraphView = null;
        this.isShowChart = true;
        this.isExecuteD = Boolean.valueOf(false);
        this.mSize = 0;
        this.count = 0;
        this.mListCheck = new ArrayList();
        this.mHandler = new C03741();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        this.mContexts = this;
        setContentView(C0136R.layout.combine_chart_play);
        MyApplication.getInstance().addActivity(this);
        if (MySharedPreferences.share == null) {
            MySharedPreferences.getSharedPref(getApplicationContext());
        }
        DataStreamChartTaskManager.getInstance();
        new Thread(new DataStreamChartTaskManagerThread()).start();
        this.mTaskManager = DataStreamChartTaskManager.getInstance();
        initView();
        registerBoradcastReceiver();
        BluetoothChatService.setHandler(this.mHandler);
    }

    private void initView() {
        this.mListCheck.clear();
        this.bundle = getIntent().getExtras();
        if (this.bundle != null) {
            this.listStr = (ArrayList) this.bundle.getSerializable("siteList");
            this.lists = (List) this.bundle.getSerializable("DataList");
            this.times = this.bundle.getDouble("times");
        }
        this.mSize = this.listStr.size();
        String[] titles = new String[this.mSize];
        String[] units = new String[this.mSize];
        for (int i = 0; i < this.mSize; i++) {
            if (this.lists != null && this.lists.size() > 0) {
                if (((ArrayList) this.lists.get(0)).get(0) instanceof SptVwDataStreamIdItem) {
                    ArrayList<SptVwDataStreamIdItem> itemList = (ArrayList) this.lists.get(0);
                    titles[i] = new String(((SptVwDataStreamIdItem) itemList.get(((IntData) this.listStr.get(i)).getItem())).getStreamTextIdContent());
                    units[i] = ((SptVwDataStreamIdItem) itemList.get(((IntData) this.listStr.get(i)).getItem())).getStreamUnitIdContent();
                    this.mListCheck.add(Integer.valueOf(((IntData) this.listStr.get(i)).getItem()));
                } else {
                    ArrayList<SptExDataStreamIdItem> itemList2 = (ArrayList) this.lists.get(0);
                    titles[i] = new String(((SptExDataStreamIdItem) itemList2.get(((IntData) this.listStr.get(i)).getItem())).getStreamTextIdContent());
                    units[i] = ((SptExDataStreamIdItem) itemList2.get(((IntData) this.listStr.get(i)).getItem())).getStreamState();
                    this.mListCheck.add(Integer.valueOf(((IntData) this.listStr.get(i)).getItem()));
                }
            }
        }
        LinearLayout layout = (LinearLayout) findViewById(C0136R.id.chart);
        this.mGraphView = null;
        this.mGraphView = new GraphView(this.mContexts, layout, AsyncTaskManager.REQUEST_SUCCESS_CODE, AsyncTaskManager.REQUEST_SUCCESS_CODE, titles, units, this.mListCheck);
        layout.addView(this.mGraphView, new LayoutParams(-2, -2));
    }

    protected void onResume() {
        super.onResume();
        FreeMemory.getInstance(this).freeMemory();
        this.times = this.bundle.getDouble("times");
        this.isExecuteD = Boolean.valueOf(true);
    }

    protected void onPause() {
        super.onPause();
        this.isExecuteD = Boolean.valueOf(false);
    }

    protected void onStop() {
        super.onStop();
        this.isExecuteD = Boolean.valueOf(false);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.isExecuteD = Boolean.valueOf(false);
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
        this.myIntentFilter.addAction("pushDataToChart");
        this.myIntentFilter.addAction("SPT_EX_DATASTREAM_ID");
        this.myIntentFilter.addAction("SPT_VW_DATASTREAM_ID");
        this.myIntentFilter.addAction("SPT_DATASTREAM_ID_EX");
        registerReceiver(this.receiver, this.myIntentFilter);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 && keyCode != Service.SUNRPC) {
            return super.onKeyDown(keyCode, event);
        }
        back();
        return true;
    }

    private void back() {
        if (Constant.mChatService == null || Constant.mChatService.getState() != 3) {
            finish();
            overridePendingTransition(0, 0);
            return;
        }
        this.isShowChart = false;
        if (this.mProgressDialog == null) {
            this.mProgressDialog = new ProgressDialog(this.mContexts);
            this.mProgressDialog.setCancelable(false);
        } else {
            this.mProgressDialog.dismiss();
            this.mProgressDialog = null;
            this.mProgressDialog = new ProgressDialog(this.mContexts);
            this.mProgressDialog.setCancelable(false);
        }
        SimpleDialog.openProgressDialog(this.mContexts, getResources().getString(C0136R.string.dataDisposeTilte), getResources().getString(C0136R.string.dataDisposeMessage), this.mProgressDialog);
    }
}

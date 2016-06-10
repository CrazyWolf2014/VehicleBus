package com.ifoer.expedition.BluetoothChat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.GridView;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.ifoer.adapter.MostChartPlayAdapter;
import com.ifoer.entity.Constant;
import com.ifoer.entity.IntData;
import com.ifoer.entity.SptDataStreamIdEx;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptExDataStreamIdItem34;
import com.ifoer.entity.SptVwDataStreamIdItem;
import com.ifoer.freememory.FreeMemory;
import com.ifoer.util.DataStreamChartTask;
import com.ifoer.util.DataStreamChartTaskManager;
import com.ifoer.util.DataStreamChartTaskManagerThread;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;

public class MostChartPlayActivity extends Activity {
    private static final boolean f1280D = false;
    public static int mSelectCount;
    private MostChartPlayAdapter adapter;
    private Bundle bundle;
    private Context contexts;
    private int count;
    private ArrayList<?> exDataStreamIdlist;
    private GridView gridView;
    private Boolean isExecuteD;
    private boolean isShowChart;
    private long lastTime;
    private ArrayList<IntData> listStr;
    private List<ArrayList<?>> lists;
    SptDataStreamIdEx mExDataStreamIdItem;
    private final Handler mHandler;
    ArrayList<SptDataStreamIdEx> mSptDataStreamList;
    private WindowManager manager;
    private IntentFilter myIntentFilter;
    private ProgressDialog progressDialog;
    private mBroadcastReceiver receiver;
    private DataStreamChartTaskManager taskManager;
    private double times;

    /* renamed from: com.ifoer.expedition.BluetoothChat.MostChartPlayActivity.1 */
    class C04551 extends Handler {
        C04551() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    MostChartPlayActivity.this.taskManager.addDownloadTask(new DataStreamChartTask(MostChartPlayActivity.this.exDataStreamIdlist, MostChartPlayActivity.this.lists, MostChartPlayActivity.this.mHandler));
                case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                    MostChartPlayActivity mostChartPlayActivity = MostChartPlayActivity.this;
                    mostChartPlayActivity.times = mostChartPlayActivity.times + 1.0d;
                    if (MostChartPlayActivity.this.adapter != null) {
                        MostChartPlayActivity.this.adapter.refresh(MostChartPlayActivity.this.lists, MostChartPlayActivity.this.times);
                    } else if (MostChartPlayActivity.this.lists != null && MostChartPlayActivity.this.lists.size() > 0) {
                        MostChartPlayActivity.this.adapter = new MostChartPlayAdapter(MostChartPlayActivity.this.contexts, MostChartPlayActivity.this.lists, MostChartPlayActivity.this.times, MostChartPlayActivity.this.listStr, MostChartPlayActivity.this.manager);
                        MostChartPlayActivity.this.gridView.setAdapter(MostChartPlayActivity.this.adapter);
                    }
                case 10101010:
                    if (!(MostChartPlayActivity.this.progressDialog == null || MostChartPlayActivity.this.progressDialog == null || !MostChartPlayActivity.this.progressDialog.isShowing())) {
                        SimpleDialog.closeProgressDialog(MostChartPlayActivity.this.progressDialog);
                    }
                    SimpleDialog.checkConectior(MostChartPlayActivity.this.contexts, MostChartPlayActivity.this.getString(C0136R.string.initializeTilte), MostChartPlayActivity.this.getString(C0136R.string.check_connector));
                default:
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {

        /* renamed from: com.ifoer.expedition.BluetoothChat.MostChartPlayActivity.mBroadcastReceiver.1 */
        class C04561 extends Thread {
            C04561() {
            }

            public void run() {
                MostChartPlayActivity.this.mHandler.sendMessage(MostChartPlayActivity.this.mHandler.obtainMessage(15));
            }
        }

        /* renamed from: com.ifoer.expedition.BluetoothChat.MostChartPlayActivity.mBroadcastReceiver.2 */
        class C04572 extends Thread {
            C04572() {
            }

            public void run() {
                MostChartPlayActivity.this.mHandler.sendMessage(MostChartPlayActivity.this.mHandler.obtainMessage(15));
            }
        }

        /* renamed from: com.ifoer.expedition.BluetoothChat.MostChartPlayActivity.mBroadcastReceiver.3 */
        class C04583 extends Thread {
            C04583() {
            }

            public void run() {
                MostChartPlayActivity.this.mHandler.sendMessage(MostChartPlayActivity.this.mHandler.obtainMessage(15));
            }
        }

        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("pushDataToChart")) {
                MostChartPlayActivity.this.lists = (List) intent.getExtras().getSerializable("DataList");
                MostChartPlayActivity.this.times = intent.getExtras().getDouble("times");
                MostChartPlayActivity.this.adapter.refresh(MostChartPlayActivity.this.lists, MostChartPlayActivity.this.times);
            }
            if (MostChartPlayActivity.this.isExecuteD.booleanValue()) {
                if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - MostChartPlayActivity.this.lastTime) / 1000 > 30) {
                    SimpleDialog.ExitDialog(context);
                }
                if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                    MostChartPlayActivity.this.lastTime = System.currentTimeMillis();
                }
                if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    if (Constant.mChatService != null) {
                        Constant.mChatService.stop();
                    }
                    if (MostChartPlayActivity.this.lists != null) {
                        MostChartPlayActivity.this.lists.clear();
                    }
                    MostChartPlayActivity.this.finish();
                    MostChartPlayActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                    if (MostChartPlayActivity.this.isShowChart) {
                        MostChartPlayActivity.this.exDataStreamIdlist = null;
                        MostChartPlayActivity.this.exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                        new C04561().start();
                        return;
                    }
                    ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                    troubleIntent = new Intent(MostChartPlayActivity.this, DataStreamActivity.class);
                    troubleIntent.putExtra("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                    troubleIntent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    MostChartPlayActivity.this.startActivity(troubleIntent);
                    MostChartPlayActivity.this.overridePendingTransition(0, 0);
                    MostChartPlayActivity.this.progressDialog.dismiss();
                    MostChartPlayActivity.this.finish();
                    MostChartPlayActivity.this.overridePendingTransition(0, 0);
                } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                    if (MostChartPlayActivity.this.isShowChart) {
                        MostChartPlayActivity.this.exDataStreamIdlist = null;
                        MostChartPlayActivity.this.exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                        new C04572().start();
                        return;
                    }
                    ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                    troubleIntent = new Intent(MostChartPlayActivity.this, VWDataStreamActivity.class);
                    troubleIntent.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                    troubleIntent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    MostChartPlayActivity.this.startActivity(troubleIntent);
                    MostChartPlayActivity.this.overridePendingTransition(0, 0);
                    MostChartPlayActivity.this.progressDialog.dismiss();
                    MostChartPlayActivity.this.finish();
                    MostChartPlayActivity.this.overridePendingTransition(0, 0);
                } else if (!intent.getAction().equals("SPT_DATASTREAM_ID_EX")) {
                } else {
                    if (MostChartPlayActivity.this.isShowChart) {
                        MostChartPlayActivity.this.exDataStreamIdlist = null;
                        MostChartPlayActivity.this.exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_DATASTREAM_ID_EX");
                        new C04583().start();
                        return;
                    }
                    ArrayList<SptExDataStreamIdItem34> exDataStreamIdlist2 = (ArrayList) intent.getExtras().getSerializable("SPT_DATASTREAM_ID_EX");
                    troubleIntent = new Intent(MostChartPlayActivity.this, DataStreamItemActivity.class);
                    troubleIntent.putExtra("SPT_DATASTREAM_ID_EX", exDataStreamIdlist2);
                    troubleIntent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    MostChartPlayActivity.this.startActivity(troubleIntent);
                    MostChartPlayActivity.this.overridePendingTransition(0, 0);
                    MostChartPlayActivity.this.progressDialog.dismiss();
                    MostChartPlayActivity.this.finish();
                    MostChartPlayActivity.this.overridePendingTransition(0, 0);
                }
            }
        }
    }

    public MostChartPlayActivity() {
        this.adapter = null;
        this.listStr = null;
        this.lists = new ArrayList();
        this.times = 0.0d;
        this.isExecuteD = Boolean.valueOf(false);
        this.isShowChart = true;
        this.count = 0;
        this.mHandler = new C04551();
    }

    static {
        mSelectCount = 0;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        this.contexts = this;
        setContentView(C0136R.layout.most_chart_play);
        MyApplication.getInstance().addActivity(this);
        if (MySharedPreferences.share == null) {
            MySharedPreferences.getSharedPref(getApplicationContext());
        }
        DataStreamChartTaskManager.getInstance();
        new Thread(new DataStreamChartTaskManagerThread()).start();
        this.taskManager = DataStreamChartTaskManager.getInstance();
        BluetoothChatService.setHandler(this.mHandler);
        initView();
        registerBoradcastReceiver();
    }

    private void initView() {
        this.bundle = getIntent().getExtras();
        if (this.bundle != null) {
            this.listStr = (ArrayList) this.bundle.getSerializable("siteList");
            this.lists = (List) this.bundle.getSerializable("DataList");
            this.times = this.bundle.getDouble("times");
            this.manager = getWindowManager();
            if (this.listStr != null) {
                mSelectCount = this.listStr.size();
            }
        }
        this.gridView = (GridView) findViewById(C0136R.id.gridView);
        if (getResources().getConfiguration().orientation == 2) {
            if (mSelectCount <= 2) {
                this.gridView.setNumColumns(mSelectCount);
            } else {
                this.gridView.setNumColumns(2);
            }
            this.gridView.setGravity(17);
        } else if (getResources().getConfiguration().orientation == 1) {
            if (mSelectCount <= 2) {
                this.gridView.setNumColumns(mSelectCount);
            } else {
                this.gridView.setNumColumns(2);
            }
        }
        this.adapter = new MostChartPlayAdapter(this.contexts, this.lists, this.times, this.listStr, this.manager);
        this.gridView.setAdapter(this.adapter);
    }

    protected void onResume() {
        super.onResume();
        FreeMemory.getInstance(this).freeMemory();
        this.isExecuteD = Boolean.valueOf(true);
    }

    protected void onStop() {
        super.onStop();
        this.isExecuteD = Boolean.valueOf(false);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.receiver != null) {
            unregisterReceiver(this.receiver);
        }
    }

    public void registerBoradcastReceiver() {
        this.receiver = new mBroadcastReceiver();
        this.myIntentFilter = new IntentFilter();
        this.myIntentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        this.myIntentFilter.addAction("pushDataToChart");
        this.myIntentFilter.addAction("SPT_EX_DATASTREAM_ID");
        this.myIntentFilter.addAction("SPT_VW_DATASTREAM_ID");
        this.myIntentFilter.addAction("SPT_DATASTREAM_ID_EX");
        this.myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(this.receiver, this.myIntentFilter);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 && keyCode != Service.SUNRPC) {
            return super.onKeyDown(keyCode, event);
        }
        back();
        return true;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == 2) {
            if (mSelectCount <= 2) {
                this.gridView.setNumColumns(mSelectCount);
            } else {
                this.gridView.setNumColumns(2);
            }
            this.gridView.setGravity(17);
        } else if (getResources().getConfiguration().orientation == 1) {
            if (mSelectCount <= 2) {
                this.gridView.setNumColumns(mSelectCount);
            } else {
                this.gridView.setNumColumns(2);
            }
        }
        this.adapter = new MostChartPlayAdapter(this.contexts, this.lists, this.times, this.listStr, this.manager);
        this.gridView.setAdapter(this.adapter);
    }

    private void back() {
        if (Constant.mChatService == null || Constant.mChatService.getState() != 3) {
            finish();
            overridePendingTransition(0, 0);
            return;
        }
        this.isShowChart = false;
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
    }
}

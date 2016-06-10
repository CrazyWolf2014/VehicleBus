package com.ifoer.expedition.BluetoothChat;

import android.app.Activity;
import android.app.LocalActivityManager;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.Constant;
import com.ifoer.entity.IntData;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptExDataStreamIdItem34;
import com.ifoer.entity.SptMessageBoxText;
import com.ifoer.entity.SptStreamSelectIdItem;
import com.ifoer.entity.SptVwDataStreamIdItem;
import com.ifoer.expedition.cto.CToJava;
import com.ifoer.freememory.FreeMemory;
import com.ifoer.util.MyApplication;
import com.ifoer.util.NetPOSPrinter;
import com.ifoer.util.SimpleDialog;
import com.ifoer.util.SimpleDialogControl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Type;

public class DataStreamChartTabActivity extends Activity implements OnClickListener {
    public static DataStreamChartTabActivity instance;
    public static boolean needStop;
    private final int MSG_PRINT_RESULT;
    private final int MSG_PRINT_START;
    Handler baseHandler;
    Bitmap bitmap;
    Runnable bmp;
    private int count;
    private boolean isMosta;
    private boolean isShowChart;
    private long lastTime;
    private boolean leftDown;
    private FrameLayout mBoday;
    private Bundle mBundle;
    private Bundle mBundler;
    private Button mCombinButton;
    private Intent mCombinIntent;
    private Context mContexts;
    private IntentFilter mIntentFilter;
    private ArrayList<IntData> mListStr;
    private List<ArrayList<?>> mLists;
    private Button mMostButton;
    private Intent mMostIntent;
    private ProgressDialog mProgressDialog;
    private mBroadcastReceiver mReceiver;
    private double mTimes;
    private LocalActivityManager mlocalActivityManager;
    private boolean needChange;
    int printResult;
    private boolean rightUp;
    private TextView showValueTx;
    View view;

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamChartTabActivity.1 */
    class C03881 implements Runnable {
        C03881() {
        }

        public void run() {
            NetPOSPrinter bmpPrinter = new NetPOSPrinter(DataStreamChartTabActivity.this);
            DataStreamChartTabActivity.this.bitmap = DataStreamChartTabActivity.this.screenShot();
            Bitmap printBitmap = bmpPrinter.zoomBitmap(DataStreamChartTabActivity.this.bitmap);
            DataStreamChartTabActivity.this.mBoday.setDrawingCacheEnabled(false);
            DataStreamChartTabActivity.this.printResult = bmpPrinter.printPic(printBitmap);
            bmpPrinter.resultToast(DataStreamChartTabActivity.this.printResult);
            DataStreamChartTabActivity.this.baseHandler.obtainMessage(0, Integer.valueOf(DataStreamChartTabActivity.this.printResult)).sendToTarget();
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamChartTabActivity.2 */
    class C03892 extends Handler {
        C03892() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case InBandBytestreamManager.MAXIMUM_BLOCK_SIZE /*65535*/:
                    DataStreamChartTabActivity.this.dayin();
                case 10101010:
                    if (DataStreamChartTabActivity.this.mProgressDialog != null && DataStreamChartTabActivity.this.mProgressDialog.isShowing()) {
                        SimpleDialog.closeProgressDialog(DataStreamChartTabActivity.this.mProgressDialog);
                    }
                    SimpleDialog.checkConectior(DataStreamChartTabActivity.this.mContexts, DataStreamChartTabActivity.this.getString(C0136R.string.initializeTilte), DataStreamChartTabActivity.this.getString(C0136R.string.check_connector));
                default:
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - DataStreamChartTabActivity.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                DataStreamChartTabActivity.this.lastTime = System.currentTimeMillis();
            }
            if (intent.getAction().equals("feedbackMeauData")) {
                if (DataStreamChartTabActivity.this.mProgressDialog != null && DataStreamChartTabActivity.this.mProgressDialog.isShowing()) {
                    SimpleDialog.closeProgressDialog(DataStreamChartTabActivity.this.mProgressDialog);
                }
                DataStreamChartTabActivity.this.finish();
                Process.killProcess(Process.myPid());
                System.exit(0);
            }
            if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                if (Constant.mChatService != null) {
                    Constant.mChatService.stop();
                }
                if (DataStreamChartTabActivity.this.mLists != null) {
                    DataStreamChartTabActivity.this.mLists.clear();
                }
                DataStreamChartTabActivity.this.finish();
                DataStreamChartTabActivity.this.overridePendingTransition(0, 0);
            } else if (intent.getAction().equals("SPT_EX_DATASTREAM_ID")) {
                r9 = DataStreamChartTabActivity.this;
                r9.mTimes = r9.mTimes + 1.0d;
                if (!DataStreamChartTabActivity.this.isShowChart) {
                    DataStreamChartTabActivity.this.mProgressDialog.dismiss();
                    DataStreamChartTabActivity.this.mlocalActivityManager.dispatchStop();
                    DataStreamChartTabActivity.this.mlocalActivityManager.dispatchDestroy(DataStreamChartTabActivity.this.isFinishing());
                    ArrayList<SptExDataStreamIdItem> exDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_EX_DATASTREAM_ID");
                    troubleIntent = new Intent(DataStreamChartTabActivity.this, DataStreamActivity.class);
                    troubleIntent.putExtra("SPT_EX_DATASTREAM_ID", exDataStreamIdlist);
                    troubleIntent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    DataStreamChartTabActivity.this.startActivity(troubleIntent);
                    DataStreamChartTabActivity.this.overridePendingTransition(0, 0);
                    DataStreamChartTabActivity.this.mProgressDialog.dismiss();
                    DataStreamChartTabActivity.this.finish();
                    DataStreamChartTabActivity.this.overridePendingTransition(0, 0);
                }
            } else if (intent.getAction().equals("SPT_VW_DATASTREAM_ID")) {
                r9 = DataStreamChartTabActivity.this;
                r9.mTimes = r9.mTimes + 1.0d;
                if (!DataStreamChartTabActivity.this.isShowChart) {
                    DataStreamChartTabActivity.this.mProgressDialog.dismiss();
                    DataStreamChartTabActivity.this.mlocalActivityManager.dispatchStop();
                    DataStreamChartTabActivity.this.mlocalActivityManager.dispatchDestroy(DataStreamChartTabActivity.this.isFinishing());
                    ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_VW_DATASTREAM_ID");
                    troubleIntent = new Intent(DataStreamChartTabActivity.this, VWDataStreamActivity.class);
                    troubleIntent.putExtra("SPT_VW_DATASTREAM_ID", vwDataStreamIdlist);
                    troubleIntent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    DataStreamChartTabActivity.this.startActivity(troubleIntent);
                    DataStreamChartTabActivity.this.overridePendingTransition(0, 0);
                    DataStreamChartTabActivity.this.mProgressDialog.dismiss();
                    DataStreamChartTabActivity.this.finish();
                    DataStreamChartTabActivity.this.overridePendingTransition(0, 0);
                }
            } else if (intent.getAction().equals("SPT_DATASTREAM_ID_EX")) {
                r9 = DataStreamChartTabActivity.this;
                r9.mTimes = r9.mTimes + 1.0d;
                if (!DataStreamChartTabActivity.this.isShowChart) {
                    DataStreamChartTabActivity.this.mProgressDialog.dismiss();
                    DataStreamChartTabActivity.this.mlocalActivityManager.dispatchStop();
                    DataStreamChartTabActivity.this.mlocalActivityManager.dispatchDestroy(DataStreamChartTabActivity.this.isFinishing());
                    ArrayList<SptExDataStreamIdItem34> exDataStreamIdlist2 = (ArrayList) intent.getExtras().getSerializable("SPT_DATASTREAM_ID_EX");
                    troubleIntent = new Intent(DataStreamChartTabActivity.this, DataStreamItemActivity.class);
                    troubleIntent.putExtra("SPT_DATASTREAM_ID_EX", exDataStreamIdlist2);
                    troubleIntent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    DataStreamChartTabActivity.this.startActivity(troubleIntent);
                    DataStreamChartTabActivity.this.overridePendingTransition(0, 0);
                    DataStreamChartTabActivity.this.mProgressDialog.dismiss();
                    DataStreamChartTabActivity.this.finish();
                    DataStreamChartTabActivity.this.overridePendingTransition(0, 0);
                }
            } else if (intent.getAction().equals("SPT_MESSAGEBOX_TEXT")) {
                SimpleDialog.closeProgressDialog(DataStreamChartTabActivity.this.mProgressDialog);
                SptMessageBoxText sptMessageBoxText = (SptMessageBoxText) intent.getExtras().getSerializable("SPT_MESSAGEBOX_TEXT");
                SimpleDialogControl.showDiaglog(context, sptMessageBoxText.getDialogType(), sptMessageBoxText);
            } else if (intent.getAction().equals("SPT_STREAM_SELECT_ID_EX")) {
                CToJava.streamFlag = Boolean.valueOf(false);
                ArrayList<SptStreamSelectIdItem> streamSelectIdlist = (ArrayList) intent.getExtras().getSerializable("SPT_STREAM_SELECT_ID_EX");
                troubleIntent = new Intent(DataStreamChartTabActivity.this, StreamSelectActivity.class);
                troubleIntent.putExtra("SPT_STREAM_SELECT_ID_EX", streamSelectIdlist);
                troubleIntent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                SimpleDialog.closeProgressDialog(DataStreamChartTabActivity.this.mProgressDialog);
                DataStreamChartTabActivity.this.startActivity(troubleIntent);
                DataStreamChartTabActivity.this.overridePendingTransition(0, 0);
                DataStreamChartTabActivity.this.finish();
                DataStreamChartTabActivity.this.overridePendingTransition(0, 0);
            }
        }
    }

    public DataStreamChartTabActivity() {
        this.mListStr = null;
        this.mLists = new ArrayList();
        this.mTimes = 0.0d;
        this.isMosta = true;
        this.isShowChart = true;
        this.leftDown = true;
        this.rightUp = true;
        this.MSG_PRINT_RESULT = 0;
        this.MSG_PRINT_START = InBandBytestreamManager.MAXIMUM_BLOCK_SIZE;
        this.count = 0;
        this.needChange = false;
        this.bmp = new C03881();
        this.baseHandler = new C03892();
    }

    static {
        needStop = false;
    }

    public void dayin() {
        new Thread(this.bmp).start();
    }

    public Bitmap screenShot() {
        View view = getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.data_stream_chart);
        this.mContexts = this;
        instance = this;
        this.mBundle = getIntent().getExtras();
        if (this.mBundle != null) {
            this.mListStr = (ArrayList) this.mBundle.getSerializable("siteList");
            this.mLists = (List) this.mBundle.getSerializable("DataList");
            this.mTimes = this.mBundle.getDouble("times");
        }
        this.showValueTx = (TextView) findViewById(C0136R.id.showValue);
        this.showValueTx.setText(new StringBuilder(String.valueOf(Constant.GRAPHIC_X / 4)).toString());
        this.mBoday = (FrameLayout) findViewById(C0136R.id.frame);
        this.mMostButton = (Button) findViewById(C0136R.id.MostActivity);
        this.mMostButton.requestFocus();
        this.mMostButton.setSelected(true);
        this.mCombinButton = (Button) findViewById(C0136R.id.CombinActivity);
        this.mMostButton.setOnClickListener(this);
        this.mCombinButton.setOnClickListener(this);
        this.mlocalActivityManager = new LocalActivityManager(this, false);
        this.mlocalActivityManager.dispatchCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        registerBoradcastReceiver();
        BluetoothChatService.setHandler(this.baseHandler);
        this.mMostIntent = new Intent(this, MostChartPlayActivity.class);
        this.mCombinIntent = new Intent(this, CombineChartPlayActivity.class);
        this.mMostIntent.setFlags(67108864);
        this.mCombinIntent.setFlags(67108864);
        this.mBundler = new Bundle();
        this.mBundler.putSerializable("siteList", this.mListStr);
        this.mBundler.putSerializable("DataList", (Serializable) this.mLists);
        this.mBundler.putSerializable("times", Double.valueOf(this.mTimes));
        this.mMostIntent.putExtras(this.mBundler);
        this.mCombinIntent.putExtras(this.mBundler);
        View view = this.mlocalActivityManager.startActivity("one", this.mMostIntent).getDecorView();
        view.setDrawingCacheEnabled(true);
        this.mBoday.removeAllViews();
        this.mBoday.addView(view);
    }

    protected void onResume() {
        FreeMemory.getInstance(this).freeMemory();
        this.mlocalActivityManager.dispatchResume();
        super.onResume();
    }

    protected void onPause() {
        this.mlocalActivityManager.dispatchPause(isFinishing());
        super.onPause();
    }

    protected void onStop() {
        this.mlocalActivityManager.dispatchStop();
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
        Constant.GRAPHIC_X = 100;
        this.mlocalActivityManager.dispatchDestroy(isFinishing());
        if (this.mReceiver != null) {
            unregisterReceiver(this.mReceiver);
        }
    }

    public void registerBoradcastReceiver() {
        this.mReceiver = new mBroadcastReceiver();
        this.mIntentFilter = new IntentFilter();
        this.mIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.mIntentFilter.addAction("android.intent.action.SCREEN_ON");
        this.mIntentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        this.mIntentFilter.addAction("SPT_EX_DATASTREAM_ID");
        this.mIntentFilter.addAction("SPT_VW_DATASTREAM_ID");
        this.mIntentFilter.addAction("SPT_DATASTREAM_ID_EX");
        this.mIntentFilter.addAction("SPT_MESSAGEBOX_TEXT");
        this.mIntentFilter.addAction("SPT_STREAM_SELECT_ID_EX");
        this.mIntentFilter.addAction("feedbackMeauData");
        registerReceiver(this.mReceiver, this.mIntentFilter);
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.MostActivity) {
            doMost();
        } else if (v.getId() == C0136R.id.CombinActivity) {
            doCombine();
        } else if (v.getId() != C0136R.id.backSuperior) {
        } else {
            if (Constant.mChatService == null || Constant.mChatService.getState() != 3) {
                finish();
                overridePendingTransition(0, 0);
                return;
            }
            this.mlocalActivityManager.dispatchPause(isFinishing());
            this.mlocalActivityManager.dispatchStop();
            this.mlocalActivityManager.dispatchDestroy(isFinishing());
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
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
        View mostView = this.mlocalActivityManager.startActivity("one", this.mMostIntent).getDecorView();
        this.mBoday.removeAllViews();
        this.mBoday.addView(mostView);
        this.mlocalActivityManager = new LocalActivityManager(this, false);
        this.mlocalActivityManager.dispatchPause(isFinishing());
        this.mlocalActivityManager.dispatchStop();
        this.mlocalActivityManager.dispatchDestroy(isFinishing());
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

    public void DoLeft(View v) {
        if (Constant.GRAPHIC_X > 25) {
            v.setBackground(getResources().getDrawable(C0136R.drawable.jian_seletor));
            Constant.GRAPHIC_X -= 25;
            if (!this.rightUp) {
                ((Button) findViewById(C0136R.id.right)).setBackground(getResources().getDrawable(C0136R.drawable.jiahao_selector));
                this.rightUp = true;
            }
            this.showValueTx.setText(new StringBuilder(String.valueOf(Constant.GRAPHIC_X / 5)).toString());
            if (this.isMosta) {
                doMost();
                return;
            } else {
                doCombine();
                return;
            }
        }
        v.setBackground(getResources().getDrawable(C0136R.drawable.jian_hui_selector));
        this.leftDown = false;
    }

    public void DoRight(View v) {
        if (Constant.GRAPHIC_X < Type.TSIG) {
            v.setBackground(getResources().getDrawable(C0136R.drawable.jiahao_selector));
            Constant.GRAPHIC_X += 25;
            if (!this.leftDown) {
                ((Button) findViewById(C0136R.id.left)).setBackground(getResources().getDrawable(C0136R.drawable.jian_seletor));
                this.leftDown = true;
            }
            this.showValueTx.setText(new StringBuilder(String.valueOf(Constant.GRAPHIC_X / 5)).toString());
            if (this.isMosta) {
                doMost();
                return;
            } else {
                doCombine();
                return;
            }
        }
        v.setBackground(getResources().getDrawable(C0136R.drawable.jia_hui_selector));
        this.rightUp = false;
    }

    private void doMost() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("times", Double.valueOf(this.mTimes));
        this.mMostIntent.putExtras(bundle);
        View mostView = this.mlocalActivityManager.startActivity("one", this.mMostIntent).getDecorView();
        this.mBoday.removeAllViews();
        this.mBoday.addView(mostView);
        this.mMostButton.setSelected(true);
        this.mCombinButton.setSelected(false);
        this.isMosta = true;
    }

    private void doCombine() {
        this.isMosta = false;
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("times", Double.valueOf(this.mTimes));
        this.mCombinIntent.putExtras(bundle2);
        if (this.mlocalActivityManager != null) {
            View comView = this.mlocalActivityManager.startActivity("one", this.mCombinIntent).getDecorView();
            this.mBoday.removeAllViews();
            this.mBoday.addView(comView);
            this.mCombinButton.setSelected(true);
            this.mMostButton.setSelected(false);
        }
    }
}

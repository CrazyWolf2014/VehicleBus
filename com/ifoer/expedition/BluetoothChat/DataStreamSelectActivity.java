package com.ifoer.expedition.BluetoothChat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
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
import android.widget.ListView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.adapter.DataStreamSelectAdapter;
import com.ifoer.adapter.DataStreamSelectAdapter.Item;
import com.ifoer.entity.Constant;
import com.ifoer.entity.DataStream;
import com.ifoer.entity.IntData;
import com.ifoer.util.DialogUtil;
import com.ifoer.util.MyApplication;
import com.ifoer.util.SimpleDialog;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.KEYRecord.Flags;

public class DataStreamSelectActivity extends Activity implements OnClickListener {
    private DataStreamSelectAdapter adapter;
    private Bundle bundle;
    private Button cancel;
    private Context contexts;
    private int count;
    private AlertDialog exidApp;
    ArrayList<DataStream> list;
    private List<ArrayList<?>> listData;
    private ArrayList<IntData> listDatas;
    private ArrayList<IntData> listStr;
    ArrayList<DataStream> lists;
    private ListView listview;
    public Handler mhandler;
    private IntentFilter myIntentFilter;
    private mBroadcastReceiver receiver;
    private Button sure;
    private double times;

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamSelectActivity.1 */
    class C03981 extends Handler {
        C03981() {
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 10101010:
                    SimpleDialog.checkConectior(DataStreamSelectActivity.this.contexts, DataStreamSelectActivity.this.getString(C0136R.string.initializeTilte), DataStreamSelectActivity.this.getString(C0136R.string.check_connector));
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamSelectActivity.2 */
    class C03992 implements OnItemClickListener {
        C03992() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
            Item item = (Item) view.getTag();
            item.checkBox.toggle();
            DataStreamSelectAdapter.isSelected.put(Integer.valueOf(position), Boolean.valueOf(item.checkBox.isChecked()));
            if (item.checkBox.isChecked()) {
                ((IntData) DataStreamSelectActivity.this.listStr.get(position)).setItemCheckedState(true);
            } else {
                ((IntData) DataStreamSelectActivity.this.listStr.get(position)).setItemCheckedState(false);
            }
        }
    }

    /* renamed from: com.ifoer.expedition.BluetoothChat.DataStreamSelectActivity.3 */
    class C04003 extends Thread {
        C04003() {
        }

        public void run() {
            try {
                C04003.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Constant.mChatService != null) {
                Constant.mChatService.stop();
            }
            Process.killProcess(Process.myPid());
            System.exit(0);
            DataStreamSelectActivity.this.finish();
            DataStreamSelectActivity.this.overridePendingTransition(0, 0);
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                if (Constant.mChatService != null) {
                    Constant.mChatService.stop();
                }
                DataStreamSelectActivity.this.ExitDialog();
            }
        }
    }

    public DataStreamSelectActivity() {
        this.list = new ArrayList();
        this.lists = new ArrayList();
        this.listStr = null;
        this.listData = new ArrayList();
        this.listDatas = null;
        this.times = 0.0d;
        this.count = 0;
        this.mhandler = new C03981();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        this.contexts = this;
        setContentView(C0136R.layout.data_stream_select);
        MyApplication.getInstance().addActivity(this);
        DialogUtil.setDialogSize(this);
        initView();
        registerBoradcastReceiver();
        BluetoothChatService.setHandler(this.mhandler);
    }

    public void registerBoradcastReceiver() {
        this.receiver = new mBroadcastReceiver();
        this.myIntentFilter = new IntentFilter();
        this.myIntentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        registerReceiver(this.receiver, this.myIntentFilter);
    }

    protected void onDestroy() {
        unregisterReceiver(this.receiver);
        super.onDestroy();
    }

    private void initView() {
        this.bundle = getIntent().getExtras();
        if (this.bundle != null) {
            this.list = (ArrayList) this.bundle.getSerializable("siteList");
            this.listData = (List) this.bundle.getSerializable("DataList");
            this.times = this.bundle.getDouble("times");
        }
        this.listStr = new ArrayList();
        for (int i = 0; i < this.list.size(); i++) {
            this.lists.add((DataStream) this.list.get(i));
            IntData itemInt = new IntData();
            itemInt.setItem(((DataStream) this.list.get(i)).getCount());
            this.listStr.add(itemInt);
        }
        this.listview = (ListView) findViewById(C0136R.id.listview);
        this.adapter = new DataStreamSelectAdapter(this.lists, this);
        this.listview.setAdapter(this.adapter);
        this.listview.setOnItemClickListener(new C03992());
        this.sure = (Button) findViewById(C0136R.id.sure);
        this.cancel = (Button) findViewById(C0136R.id.cancel);
        this.sure.setOnClickListener(this);
        this.cancel.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == C0136R.id.sure) {
            if (this.listDatas != null) {
                this.listDatas.clear();
                this.listDatas = null;
            }
            this.listDatas = new ArrayList();
            this.listDatas.addAll(this.listStr);
            int leng = this.listDatas.size();
            if (leng <= 0) {
                Toast.makeText(this.contexts, getString(C0136R.string.data_list) + getString(C0136R.string.nulls), 0).show();
                this.contexts.sendBroadcast(new Intent("DataStreamSelectSiteCancel"));
                finish();
                overridePendingTransition(0, 0);
            }
            int i = 0;
            while (i < leng) {
                if (!((IntData) this.listDatas.get(i)).getItemCheckedState()) {
                    this.listDatas.remove(i);
                    leng = this.listDatas.size();
                    i--;
                }
                i++;
            }
            if (this.listDatas.size() > 4) {
                Toast.makeText(this.contexts, C0136R.string.max_four, 0).show();
            } else if (this.listDatas.size() == 0) {
                Toast.makeText(this.contexts, C0136R.string.pleaseselect, 0).show();
            } else {
                Intent intent = new Intent("DataStreamSelectSite");
                Bundle bundle = new Bundle();
                bundle.putSerializable("siteList", this.listDatas);
                intent.putExtras(bundle);
                this.contexts.sendBroadcast(intent);
                Intent active = new Intent(this, DataStreamChartTabActivity.class);
                active.putExtra("siteList", this.listDatas);
                active.putExtra("DataList", (Serializable) this.listData);
                active.putExtra("times", this.times);
                active.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                startActivity(active);
                finish();
                overridePendingTransition(0, 0);
            }
        } else if (v.getId() == C0136R.id.cancel) {
            this.contexts.sendBroadcast(new Intent("DataStreamSelectSiteCancel"));
            finish();
            overridePendingTransition(0, 0);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        this.contexts.sendBroadcast(new Intent("DataStreamSelectSiteCancel"));
        finish();
        overridePendingTransition(0, 0);
        return true;
    }

    public void ExitDialog() {
        Toast.makeText(this, C0136R.string.connectionLost, 1).show();
        new C04003().start();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DialogUtil.setDialogSize(this);
    }
}

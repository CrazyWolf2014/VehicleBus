package com.ifoer.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.adapter.RecordofCardAdapter;
import com.ifoer.entity.CardConsumeDTO;
import com.ifoer.entity.CardConsumeListResult;
import com.ifoer.util.MyApplication;
import com.ifoer.util.SimpleDialog;
import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.KEYRecord.Flags;

public class RecordOfpinCardActivity extends Activity {
    private RecordofCardAdapter adapter;
    private Button btn_back;
    private Context context;
    private List<CardConsumeDTO> dtoList;
    private long lastTime;
    private BroadcastReceiver mBroadcastReceiver;
    private ListView recordList;

    /* renamed from: com.ifoer.ui.RecordOfpinCardActivity.1 */
    class C07201 extends BroadcastReceiver {
        C07201() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - RecordOfpinCardActivity.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                RecordOfpinCardActivity.this.lastTime = System.currentTimeMillis();
            }
            if (!intent.getAction().equals("upgrade")) {
            }
        }
    }

    /* renamed from: com.ifoer.ui.RecordOfpinCardActivity.2 */
    class C07212 implements OnClickListener {
        C07212() {
        }

        public void onClick(View arg0) {
            RecordOfpinCardActivity.this.finish();
        }
    }

    public RecordOfpinCardActivity() {
        this.dtoList = new ArrayList();
        this.mBroadcastReceiver = new C07201();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.record_of_card);
        MyApplication.getInstance().addActivity(this);
        this.context = this;
        this.dtoList = ((CardConsumeListResult) getIntent().getSerializableExtra("list")).getCardListReust();
        this.btn_back = (Button) findViewById(C0136R.id.returnBtn);
        this.recordList = (ListView) findViewById(C0136R.id.record_list);
        if (this.dtoList != null && this.dtoList.size() > 0) {
            Log.i("GetUpdateRecord", "dtoList " + this.dtoList.size());
            this.adapter = new RecordofCardAdapter(this.context, this.dtoList);
            this.recordList.setAdapter(this.adapter);
        }
        this.btn_back.setOnClickListener(new C07212());
        registerBoradcastReceiver();
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        this.context.registerReceiver(this.mBroadcastReceiver, myIntentFilter);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mBroadcastReceiver != null) {
            unregisterReceiver(this.mBroadcastReceiver);
        }
    }
}

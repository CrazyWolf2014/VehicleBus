package com.ifoer.expeditionphone;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.adapter.SpaceDiagnosticRecordAdapter;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Report;
import com.ifoer.mine.Contact;
import com.ifoer.util.MyApplication;
import com.ifoer.util.SimpleDialog;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import java.util.List;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xbill.DNS.KEYRecord.Flags;

public class DiagnoseReportActivity extends Activity {
    private static final String LOG = "DiagnoseReportActivity...";
    private SpaceDiagnosticRecordAdapter adapter;
    private ImageView back;
    private String cc;
    private Context context;
    private long lastTime;
    private List<Report> list;
    private ListView listView;
    private mBroadcastReceiver mReceiver;
    private IntentFilter myIntentFilter;

    /* renamed from: com.ifoer.expeditionphone.DiagnoseReportActivity.1 */
    class C05371 implements OnClickListener {
        C05371() {
        }

        public void onClick(View v) {
            DiagnoseReportActivity.this.finish();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DiagnoseReportActivity.2 */
    class C05382 implements OnItemClickListener {
        C05382() {
        }

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Report report = (Report) arg0.getItemAtPosition(arg2);
            if (report != null) {
                String path = report.getReportPath();
                int id = Integer.parseInt(report.getId());
                Intent intent = new Intent(DiagnoseReportActivity.this.context, DiagnoseDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(LocaleUtil.INDONESIAN, id);
                bundle.putString("path", path);
                intent.putExtra("bundle", bundle);
                DiagnoseReportActivity.this.startActivity(intent);
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                DiagnoseReportActivity.this.finish();
                DiagnoseReportActivity.this.overridePendingTransition(0, 0);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - DiagnoseReportActivity.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                DiagnoseReportActivity.this.lastTime = System.currentTimeMillis();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.diagnose_report);
        MyApplication.getInstance().addActivity(this);
        initResource();
        registerBoradcastReceiver();
    }

    private void initResource() {
        this.context = this;
        this.cc = getIntent().getStringExtra(MultipleAddresses.CC);
        this.listView = (ListView) findViewById(C0136R.id.pngListview);
        this.back = (ImageView) findViewById(C0136R.id.toright);
        this.back.setOnClickListener(new C05371());
    }

    protected void onStart() {
        super.onStart();
        this.list = DBDao.getInstance(this.context).queryReport(this.cc, Contact.RELATION_ASK, MainActivity.database);
        this.adapter = new SpaceDiagnosticRecordAdapter(this.list, this.context);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(new C05382());
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mReceiver != null) {
            unregisterReceiver(this.mReceiver);
        }
    }

    private void registerBoradcastReceiver() {
        this.mReceiver = new mBroadcastReceiver();
        this.myIntentFilter = new IntentFilter();
        this.myIntentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        this.myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(this.mReceiver, this.myIntentFilter);
    }
}

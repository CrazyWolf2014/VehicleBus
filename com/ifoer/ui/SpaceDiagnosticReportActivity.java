package com.ifoer.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.adapter.SpaceDiagnosticRecordAdapter;
import com.ifoer.adapter.SpaceDiagnosticReportAdapter;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.entity.Report;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.mine.Contact;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.xbill.DNS.KEYRecord;

public class SpaceDiagnosticReportActivity extends Activity implements OnClickListener {
    private SpaceDiagnosticReportAdapter adapter;
    private Button btn_Playback;
    private ImageView btn_back;
    private Button btn_record;
    private String cc;
    Context context;
    private boolean flag;
    private boolean isPlayback;
    private long lastTime;
    private List<Report> lists;
    private ListView listview;
    private Handler mHandler;
    public IntentFilter myIntentFilter;
    private ProgressDialog progressDialogs;
    public mBroadcastReceiver receiver;
    private SpaceDiagnosticRecordAdapter recordAdapter;
    private List<Report> recordList;
    private ListView recordListview;

    /* renamed from: com.ifoer.ui.SpaceDiagnosticReportActivity.1 */
    class C07271 extends Handler {
        C07271() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    if (SpaceDiagnosticReportActivity.this.progressDialogs != null && SpaceDiagnosticReportActivity.this.progressDialogs.isShowing()) {
                        SpaceDiagnosticReportActivity.this.progressDialogs.dismiss();
                    }
                    Toast.makeText(SpaceDiagnosticReportActivity.this.context, C0136R.string.timeout, 0).show();
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    SpaceDiagnosticReportActivity.this.lists.clear();
                    SpaceDiagnosticReportActivity.this.lists = DBDao.getInstance(SpaceDiagnosticReportActivity.this.context).queryReport(SpaceDiagnosticReportActivity.this.cc, Contact.RELATION_ASK, MainActivity.database);
                    SpaceDiagnosticReportActivity.this.adapter = new SpaceDiagnosticReportAdapter(SpaceDiagnosticReportActivity.this.lists, SpaceDiagnosticReportActivity.this.context);
                    SpaceDiagnosticReportActivity.this.adapter.notifyDataSetChanged();
                    SpaceDiagnosticReportActivity.this.listview.setAdapter(SpaceDiagnosticReportActivity.this.adapter);
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    SpaceDiagnosticReportActivity.this.recordList.clear();
                    SpaceDiagnosticReportActivity.this.recordList = DBDao.getInstance(SpaceDiagnosticReportActivity.this.context).queryReport(SpaceDiagnosticReportActivity.this.cc, Contact.RELATION_FRIEND, MainActivity.database);
                    SpaceDiagnosticReportActivity.this.recordAdapter = new SpaceDiagnosticRecordAdapter(SpaceDiagnosticReportActivity.this.recordList, SpaceDiagnosticReportActivity.this.context);
                    SpaceDiagnosticReportActivity.this.recordAdapter.notifyDataSetChanged();
                    SpaceDiagnosticReportActivity.this.listview.setAdapter(SpaceDiagnosticReportActivity.this.recordAdapter);
                default:
            }
        }
    }

    /* renamed from: com.ifoer.ui.SpaceDiagnosticReportActivity.2 */
    class C07282 implements OnItemClickListener {
        C07282() {
        }

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            String path;
            File tempFile;
            String[] tempPaths;
            if (SpaceDiagnosticReportActivity.this.isPlayback) {
                path = ((Report) SpaceDiagnosticReportActivity.this.recordList.get(arg2)).getReportPath();
                tempFile = new File(path);
                if (!tempFile.exists()) {
                    tempPaths = Constant.getDefaultExternalStoragePathList();
                    if (tempPaths.length > 1) {
                        path = path.replaceAll(tempPaths[0], tempPaths[1]);
                    }
                    tempFile = new File(path);
                }
                if (tempFile.exists()) {
                    int id = Integer.parseInt(((Report) SpaceDiagnosticReportActivity.this.recordList.get(arg2)).getId());
                    Intent intent = new Intent(SpaceDiagnosticReportActivity.this, SpaceCurrentModeActivity.class);
                    intent.putExtra(LocaleUtil.INDONESIAN, id);
                    intent.putExtra("path", path);
                    SpaceDiagnosticReportActivity.this.startActivityForResult(intent, 1);
                    return;
                }
                Toast.makeText(SpaceDiagnosticReportActivity.this.context, C0136R.string.main_file_null, 1).show();
                if (DBDao.getInstance(SpaceDiagnosticReportActivity.this.context).deleteReport(Integer.parseInt(((Report) SpaceDiagnosticReportActivity.this.recordList.get(arg2)).getId()), MainActivity.database) > 0) {
                    SpaceDiagnosticReportActivity.this.mHandler.sendEmptyMessage(4);
                    return;
                }
                return;
            }
            Report report = (Report) arg0.getItemAtPosition(arg2);
            if (report != null) {
                path = report.getReportPath();
                tempFile = new File(path);
                if (!tempFile.exists()) {
                    tempPaths = Constant.getDefaultExternalStoragePathList();
                    if (tempPaths.length > 1) {
                        path = path.replaceAll(tempPaths[0], tempPaths[1]);
                    }
                }
                if (tempFile.exists()) {
                    id = Integer.parseInt(report.getId());
                    intent = new Intent(SpaceDiagnosticReportActivity.this, ReadFileUiActivity.class);
                    intent.putExtra(LocaleUtil.INDONESIAN, id);
                    intent.putExtra("path", path);
                    SpaceDiagnosticReportActivity.this.startActivityForResult(intent, 2);
                    return;
                }
                Toast.makeText(SpaceDiagnosticReportActivity.this.context, C0136R.string.main_file_null, 1).show();
                if (DBDao.getInstance(SpaceDiagnosticReportActivity.this.context).deleteReport(Integer.parseInt(report.getId()), MainActivity.database) > 0) {
                    SpaceDiagnosticReportActivity.this.mHandler.sendEmptyMessage(3);
                }
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - SpaceDiagnosticReportActivity.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                SpaceDiagnosticReportActivity.this.lastTime = System.currentTimeMillis();
            }
        }
    }

    public SpaceDiagnosticReportActivity() {
        this.flag = false;
        this.isPlayback = false;
        this.mHandler = new C07271();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setContentView(C0136R.layout.space_diagnostic_report1);
        init();
        registerBoradcastReceiver();
    }

    private void init() {
        this.cc = MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey);
        this.lists = DBDao.getInstance(this.context).queryReport(this.cc, Contact.RELATION_ASK, MainActivity.database);
        this.recordList = DBDao.getInstance(this.context).queryReport(this.cc, Contact.RELATION_FRIEND, MainActivity.database);
        this.btn_record = (Button) findViewById(C0136R.id.bgtext);
        this.btn_record.setOnClickListener(this);
        this.btn_record.setSelected(true);
        this.btn_Playback = (Button) findViewById(C0136R.id.diagnosis_playback);
        this.btn_Playback.setOnClickListener(this);
        this.btn_back = (ImageView) findViewById(C0136R.id.backSuperior);
        this.btn_back.setOnClickListener(this);
        this.listview = (ListView) findViewById(C0136R.id.pngListview);
        this.recordListview = (ListView) findViewById(C0136R.id.recordListview);
        this.adapter = new SpaceDiagnosticReportAdapter(this.lists, this.context);
        this.recordAdapter = new SpaceDiagnosticRecordAdapter(this.recordList, this.context);
        this.listview.setAdapter(this.adapter);
        this.listview.setOnItemClickListener(new C07282());
        if (this.recordList.size() != 0) {
        }
    }

    public void deleteFolderFile(String filePath, boolean deleteThisPath) throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File absolutePath : files) {
                    deleteFolderFile(absolutePath.getAbsolutePath(), true);
                }
            }
            if (!deleteThisPath) {
                return;
            }
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.listFiles().length == 0) {
                file.delete();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            this.mHandler.sendEmptyMessage(3);
        } else if (requestCode == 1) {
            this.mHandler.sendEmptyMessage(4);
        }
    }

    public void onClick(View view) {
        Integer eventId = Integer.valueOf(view.getId());
        if (view.getId() == C0136R.id.bgtext) {
            this.isPlayback = false;
            this.mHandler.sendEmptyMessage(3);
            this.btn_record.setSelected(true);
            this.btn_Playback.setSelected(false);
        } else if (eventId.intValue() == C0136R.id.diagnosis_playback) {
            this.isPlayback = true;
            this.mHandler.sendEmptyMessage(4);
            this.btn_Playback.setSelected(true);
            this.btn_record.setSelected(false);
        }
        if (eventId.intValue() == C0136R.id.backSuperior) {
            finish();
        }
    }

    public void onDestroy() {
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
        registerReceiver(this.receiver, this.myIntentFilter);
    }
}

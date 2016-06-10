package com.ifoer.expeditionphone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.adapter.SpaceDiagnosticRecordAdapter;
import com.ifoer.adapter.SpaceDiagnosticReportAdapter;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.entity.Report;
import com.ifoer.mine.Contact;
import com.ifoer.util.MySharedPreferences;
import java.io.File;
import java.util.List;
import org.xbill.DNS.KEYRecord;

public class SpaceDiagnosticReportLayout extends MySpaceManagermentLayout {
    private SpaceDiagnosticReportAdapter adapter;
    private View baseView;
    private String cc;
    Context context;
    private List<Report> lists;
    private ListView listview;
    private Handler mHandler;
    private ProgressDialog progressDialogs;
    private SpaceDiagnosticRecordAdapter recordAdapter;
    private List<Report> recordList;
    private ListView recordListview;

    /* renamed from: com.ifoer.expeditionphone.SpaceDiagnosticReportLayout.1 */
    class C06411 extends Handler {
        C06411() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    if (SpaceDiagnosticReportLayout.this.progressDialogs != null && SpaceDiagnosticReportLayout.this.progressDialogs.isShowing()) {
                        SpaceDiagnosticReportLayout.this.progressDialogs.dismiss();
                    }
                    Toast.makeText(SpaceDiagnosticReportLayout.this.context, C0136R.string.timeout, 0).show();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.SpaceDiagnosticReportLayout.2 */
    class C06422 implements OnItemClickListener {
        C06422() {
        }

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Report report = (Report) arg0.getItemAtPosition(arg2);
            if (report != null) {
                String path = report.getReportPath();
                if (!new File(path).exists()) {
                    String[] tempPaths = Constant.getDefaultExternalStoragePathList();
                    if (tempPaths.length > 1) {
                        path = path.replaceAll(tempPaths[0], tempPaths[1]);
                    }
                }
                int id = Integer.parseInt(report.getId());
                MainActivity.panel.removePanelContainer();
                MainActivity.panel.fillPanelContainer(new ReadFile(id, path, SpaceDiagnosticReportLayout.this.context));
                MainActivity.panel.openthreePanelContainer();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.SpaceDiagnosticReportLayout.3 */
    class C06433 implements OnItemClickListener {
        C06433() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            String path = ((Report) SpaceDiagnosticReportLayout.this.recordList.get(arg2)).getReportPath();
            File tempFile = new File(path);
            if (!tempFile.exists()) {
                String[] tempPaths = Constant.getDefaultExternalStoragePathList();
                if (tempPaths.length > 1) {
                    path = path.replaceAll(tempPaths[0], tempPaths[1]);
                }
                tempFile = new File(path);
            }
            if (tempFile.exists()) {
                int id = Integer.parseInt(((Report) SpaceDiagnosticReportLayout.this.recordList.get(arg2)).getId());
                MainActivity.panel.removePanelContainer();
                MainActivity.panel.fillPanelContainer(new SpaceCurrentModeLayout(id, path, SpaceDiagnosticReportLayout.this.context));
                MainActivity.panel.openthreePanelContainer();
                return;
            }
            Toast.makeText(SpaceDiagnosticReportLayout.this.context, C0136R.string.main_file_null, 1).show();
        }
    }

    public SpaceDiagnosticReportLayout(Context context) {
        super(context);
        this.mHandler = new C06411();
        this.context = context;
        this.baseView = ((Activity) context).getLayoutInflater().inflate(C0136R.layout.space_diagnostic_report, this);
        initTopView(this.baseView);
        setTopView(context, 0);
        init();
    }

    private void init() {
        this.cc = MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey);
        this.lists = DBDao.getInstance(this.context).queryReport(this.cc, Contact.RELATION_ASK, MainActivity.database);
        this.recordList = DBDao.getInstance(this.context).queryReport(this.cc, Contact.RELATION_FRIEND, MainActivity.database);
        this.listview = (ListView) this.baseView.findViewById(C0136R.id.pngListview);
        this.recordListview = (ListView) this.baseView.findViewById(C0136R.id.recordListview);
        this.adapter = new SpaceDiagnosticReportAdapter(this.lists, this.context);
        this.recordListview.setAdapter(this.adapter);
        this.listview.setAdapter(this.adapter);
        this.listview.setOnItemClickListener(new C06422());
        this.recordAdapter = new SpaceDiagnosticRecordAdapter(this.recordList, this.context);
        this.recordListview.setAdapter(this.recordAdapter);
        this.recordListview.setOnItemClickListener(new C06433());
    }
}

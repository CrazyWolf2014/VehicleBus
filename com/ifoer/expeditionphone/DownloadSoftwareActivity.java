package com.ifoer.expeditionphone;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.adapter.DownloadSoftwareAdapter;
import com.ifoer.dbentity.DownloadStatus;
import com.ifoer.download.DownloadTask;
import com.ifoer.download.DownloadTaskManager;
import com.ifoer.download.DownloadTaskManagerThread;
import com.ifoer.entity.Constant;
import com.ifoer.util.DialogUtil;
import com.ifoer.util.MyApplication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.xbill.DNS.KEYRecord;

public class DownloadSoftwareActivity extends Activity {
    private DownloadSoftwareAdapter adapter;
    private Button back;
    private int count;
    private ArrayList<X431PadDtoSoft> data;
    private Button download;
    private ArrayList<X431PadDtoSoft> downloadList;
    private ListView downloadListview;
    DownloadTaskManager downloadTaskMananger;
    private Map<String, DownloadStatus> downloads;
    private ArrayList<X431PadDtoSoft> failTaskList;
    Handler handler;
    private MyBroadcastReceiver myBroadcastReceiver;
    private String sdPath;
    private String serialNo;

    /* renamed from: com.ifoer.expeditionphone.DownloadSoftwareActivity.1 */
    class C05501 extends Handler {
        C05501() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String softId = msg.obj;
            int fileSize = msg.arg2;
            int downloadSize = msg.arg1;
            Iterator it;
            X431PadDtoSoft dto;
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    DownloadStatus status0 = new DownloadStatus();
                    status0.setStatus(DownloadSoftwareActivity.this.getResources().getString(C0136R.string.download_fail));
                    status0.setFileSize((long) fileSize);
                    status0.setDownloadSize((long) downloadSize);
                    status0.setStatusRedownload(false);
                    DownloadSoftwareActivity.this.downloads.put(softId, status0);
                    if (DownloadSoftwareActivity.this.adapter != null) {
                        DownloadSoftwareActivity.this.adapter.notifyDataSetChanged();
                    }
                    it = DownloadSoftwareActivity.this.downloadList.iterator();
                    while (it.hasNext()) {
                        dto = (X431PadDtoSoft) it.next();
                        if (dto.getSoftId().equals(softId)) {
                            DownloadSoftwareActivity.this.failTaskList.add(dto);
                        }
                    }
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    DownloadStatus status1 = new DownloadStatus();
                    status1.setStatus(DownloadSoftwareActivity.this.getResources().getString(C0136R.string.download_now));
                    status1.setFileSize((long) fileSize);
                    status1.setDownloadSize((long) downloadSize);
                    status1.setStatusRedownload(false);
                    DownloadSoftwareActivity.this.downloads.put(softId, status1);
                    if (DownloadSoftwareActivity.this.adapter != null) {
                        DownloadSoftwareActivity.this.adapter.notifyDataSetChanged();
                    }
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    DownloadStatus status2 = new DownloadStatus();
                    status2.setStatus(DownloadSoftwareActivity.this.getResources().getString(C0136R.string.download_finish));
                    status2.setFileSize((long) fileSize);
                    status2.setDownloadSize((long) downloadSize);
                    status2.setStatusRedownload(false);
                    DownloadSoftwareActivity.this.downloads.put(softId, status2);
                    if (DownloadSoftwareActivity.this.adapter != null) {
                        DownloadSoftwareActivity.this.adapter.notifyDataSetChanged();
                    }
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    DownloadStatus status3 = new DownloadStatus();
                    status3.setStatus(DownloadSoftwareActivity.this.getResources().getString(C0136R.string.unziping));
                    status3.setFileSize((long) fileSize);
                    status3.setDownloadSize((long) fileSize);
                    status3.setStatusRedownload(false);
                    DownloadSoftwareActivity.this.downloads.put(softId, status3);
                    if (DownloadSoftwareActivity.this.adapter != null) {
                        DownloadSoftwareActivity.this.adapter.notifyDataSetChanged();
                    }
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    DownloadStatus status4 = new DownloadStatus();
                    status4.setStatus(DownloadSoftwareActivity.this.getResources().getString(C0136R.string.unzip_success));
                    status4.setFileSize((long) fileSize);
                    status4.setDownloadSize((long) fileSize);
                    status4.setStatusRedownload(false);
                    DownloadSoftwareActivity.this.downloads.put(softId, status4);
                    if (DownloadSoftwareActivity.this.adapter != null) {
                        DownloadSoftwareActivity.this.adapter.notifyDataSetChanged();
                    }
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    DownloadStatus status5 = new DownloadStatus();
                    status5.setStatus(DownloadSoftwareActivity.this.getResources().getString(C0136R.string.unzip_fail));
                    status5.setFileSize((long) fileSize);
                    status5.setDownloadSize((long) fileSize);
                    status5.setStatusRedownload(false);
                    DownloadSoftwareActivity.this.downloads.put(softId, status5);
                    if (DownloadSoftwareActivity.this.adapter != null) {
                        DownloadSoftwareActivity.this.adapter.notifyDataSetChanged();
                    }
                case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                    if (DownloadSoftwareActivity.this.downloadTaskMananger != null) {
                        DownloadSoftwareActivity.this.downloadTaskMananger.taskIdSet.clear();
                    }
                    it = DownloadSoftwareActivity.this.failTaskList.iterator();
                    while (it.hasNext()) {
                        dto = (X431PadDtoSoft) it.next();
                        DownloadStatus status6 = (DownloadStatus) DownloadSoftwareActivity.this.downloads.get(dto.getSoftId());
                        status6.setStatus(DownloadSoftwareActivity.this.getResources().getString(C0136R.string.redownload));
                        status6.setFileSize(1);
                        status6.setDownloadSize(0);
                        status6.setStatusRedownload(true);
                    }
                    if (DownloadSoftwareActivity.this.adapter != null) {
                        DownloadSoftwareActivity.this.adapter.notifyDataSetChanged();
                    }
                case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                    DownloadTaskManager.getInstance();
                    new Thread(new DownloadTaskManagerThread()).start();
                    it = DownloadSoftwareActivity.this.failTaskList.iterator();
                    while (it.hasNext()) {
                        dto = (X431PadDtoSoft) it.next();
                        if (dto.getSoftId().equals(softId)) {
                            DownloadSoftwareActivity downloadSoftwareActivity = DownloadSoftwareActivity.this;
                            downloadSoftwareActivity.downloadTaskMananger = DownloadTaskManager.getInstance();
                            DownloadSoftwareActivity.this.downloadTaskMananger.addDownloadTask(new DownloadTask(DownloadSoftwareActivity.this, DownloadSoftwareActivity.this.handler, dto, DownloadSoftwareActivity.this.serialNo));
                        }
                    }
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DownloadSoftwareActivity.2 */
    class C05512 implements OnClickListener {
        C05512() {
        }

        public void onClick(View v) {
            DownloadSoftwareActivity.this.finish();
        }
    }

    class MyBroadcastReceiver extends BroadcastReceiver {
        MyBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("show")) {
                DownloadSoftwareActivity downloadSoftwareActivity = DownloadSoftwareActivity.this;
                downloadSoftwareActivity.count = downloadSoftwareActivity.count + 1;
                if (DownloadSoftwareActivity.this.count == DownloadSoftwareActivity.this.downloadList.size()) {
                    DownloadSoftwareActivity.this.handler.obtainMessage(6).sendToTarget();
                    DownloadSoftwareActivity.this.back.setVisibility(0);
                }
            }
        }
    }

    public DownloadSoftwareActivity() {
        this.data = new ArrayList();
        this.sdPath = Constant.LOCAL_SERIALNO_PATH;
        this.downloads = new HashMap();
        this.count = 0;
        this.failTaskList = new ArrayList();
        this.handler = new C05501();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.download_software);
        MyApplication.getInstance().addActivity(this);
        this.myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("show");
        registerReceiver(this.myBroadcastReceiver, filter);
        DialogUtil.setDialogSize(this);
        Intent intent = getIntent();
        if (intent != null) {
            this.downloadList = (ArrayList) intent.getSerializableExtra("downloadList");
            this.serialNo = intent.getStringExtra(Constants.serialNo);
        }
        findView();
    }

    private void findView() {
        int i;
        this.back = (Button) findViewById(C0136R.id.back);
        this.back.setOnClickListener(new C05512());
        for (i = 0; i < this.downloadList.size(); i++) {
            DownloadStatus status = new DownloadStatus();
            status.setDownloadSize(0);
            status.setFileSize(1);
            status.setStatus(getResources().getString(C0136R.string.download_wait));
            this.downloads.put(((X431PadDtoSoft) this.downloadList.get(i)).getSoftId(), status);
        }
        this.downloadListview = (ListView) findViewById(C0136R.id.downloadListview);
        this.adapter = new DownloadSoftwareAdapter(this.downloadList, this.downloads, this, this.handler);
        this.downloadListview.setAdapter(this.adapter);
        this.download = (Button) findViewById(C0136R.id.download);
        this.download.setVisibility(4);
        this.back.setVisibility(4);
        if (Environment.getExternalStorageState().equals("mounted")) {
            DownloadTaskManager.getInstance();
            new Thread(new DownloadTaskManagerThread()).start();
            for (i = 0; i < this.downloadList.size(); i++) {
                X431PadDtoSoft dto = (X431PadDtoSoft) this.downloadList.get(i);
                this.downloadTaskMananger = DownloadTaskManager.getInstance();
                this.downloadTaskMananger.addDownloadTask(new DownloadTask(this, this.handler, dto, this.serialNo));
            }
            return;
        }
        Toast.makeText(this, getResources().getString(C0136R.string.sd_not_use), 0).show();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.downloadTaskMananger != null) {
            this.downloadTaskMananger.taskIdSet.clear();
        }
        unregisterReceiver(this.myBroadcastReceiver);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0136R.menu.main, menu);
        return true;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DialogUtil.setDialogSize(this);
    }
}

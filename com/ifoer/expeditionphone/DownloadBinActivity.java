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
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.adapter.DownloadSoftwareAdapter;
import com.ifoer.dbentity.DownloadStatus;
import com.ifoer.download.DownloadBinManager;
import com.ifoer.download.DownloadBinManagerThread;
import com.ifoer.download.DownloadBinNewVersion;
import com.ifoer.entity.SoftMaxVersion;
import com.ifoer.util.DialogUtil;
import com.ifoer.util.MyApplication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.xbill.DNS.KEYRecord;

public class DownloadBinActivity extends Activity {
    private DownloadSoftwareAdapter adapter;
    private Button back;
    private int count;
    private Button download;
    private SoftMaxVersion downloadBinInfo;
    private ArrayList<X431PadDtoSoft> downloadList;
    private ListView downloadListview;
    private DownloadBinManager downloadTaskMananger;
    private TextView download_title;
    private Map<String, DownloadStatus> downloads;
    Handler handler;
    private MyBroadcastReceiver myBroadcastReceiver;
    private String serialNo;

    /* renamed from: com.ifoer.expeditionphone.DownloadBinActivity.1 */
    class C05441 extends Handler {
        C05441() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String softId = msg.obj;
            int fileSize = msg.arg2;
            int downloadSize = msg.arg1;
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    DownloadStatus status0 = new DownloadStatus();
                    status0.setStatus(DownloadBinActivity.this.getResources().getString(C0136R.string.download_fail));
                    status0.setFileSize((long) fileSize);
                    status0.setDownloadSize((long) downloadSize);
                    status0.setStatusRedownload(false);
                    DownloadBinActivity.this.downloads.put(softId, status0);
                    if (DownloadBinActivity.this.adapter != null) {
                        DownloadBinActivity.this.adapter.notifyDataSetChanged();
                    }
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    DownloadStatus status1 = new DownloadStatus();
                    status1.setStatus(DownloadBinActivity.this.getResources().getString(C0136R.string.download_now));
                    status1.setFileSize((long) fileSize);
                    status1.setDownloadSize((long) downloadSize);
                    status1.setStatusRedownload(false);
                    DownloadBinActivity.this.downloads.put(softId, status1);
                    if (DownloadBinActivity.this.adapter != null) {
                        DownloadBinActivity.this.adapter.notifyDataSetChanged();
                    }
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    DownloadStatus status2 = new DownloadStatus();
                    status2.setStatus(DownloadBinActivity.this.getResources().getString(C0136R.string.download_finish));
                    status2.setFileSize((long) fileSize);
                    status2.setDownloadSize((long) downloadSize);
                    status2.setStatusRedownload(false);
                    DownloadBinActivity.this.downloads.put(softId, status2);
                    if (DownloadBinActivity.this.adapter != null) {
                        DownloadBinActivity.this.adapter.notifyDataSetChanged();
                    }
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    DownloadStatus status3 = new DownloadStatus();
                    status3.setStatus(DownloadBinActivity.this.getResources().getString(C0136R.string.unziping));
                    status3.setFileSize((long) fileSize);
                    status3.setDownloadSize((long) fileSize);
                    status3.setStatusRedownload(false);
                    DownloadBinActivity.this.downloads.put(softId, status3);
                    if (DownloadBinActivity.this.adapter != null) {
                        DownloadBinActivity.this.adapter.notifyDataSetChanged();
                    }
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    DownloadStatus status4 = new DownloadStatus();
                    status4.setStatus(DownloadBinActivity.this.getResources().getString(C0136R.string.unzip_success));
                    status4.setFileSize((long) fileSize);
                    status4.setDownloadSize((long) fileSize);
                    status4.setStatusRedownload(false);
                    DownloadBinActivity.this.downloads.put(softId, status4);
                    if (DownloadBinActivity.this.adapter != null) {
                        DownloadBinActivity.this.adapter.notifyDataSetChanged();
                    }
                    Intent intent = new Intent();
                    intent.setAction("upgrade");
                    DownloadBinActivity.this.sendBroadcast(intent);
                    DownloadBinActivity.this.finish();
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    DownloadStatus status5 = new DownloadStatus();
                    status5.setStatus(DownloadBinActivity.this.getResources().getString(C0136R.string.unzip_fail));
                    status5.setFileSize((long) fileSize);
                    status5.setDownloadSize((long) fileSize);
                    status5.setStatusRedownload(false);
                    DownloadBinActivity.this.downloads.put(softId, status5);
                    if (DownloadBinActivity.this.adapter != null) {
                        DownloadBinActivity.this.adapter.notifyDataSetChanged();
                    }
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DownloadBinActivity.2 */
    class C05452 implements OnClickListener {
        C05452() {
        }

        public void onClick(View v) {
            DownloadBinActivity.this.finish();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DownloadBinActivity.3 */
    class C05463 implements OnClickListener {
        C05463() {
        }

        public void onClick(View v) {
            DownloadBinActivity.this.download.setVisibility(4);
            DownloadBinActivity.this.back.setVisibility(4);
            if (Environment.getExternalStorageState().equals("mounted")) {
                DownloadBinManager.getInstance();
                new Thread(new DownloadBinManagerThread()).start();
                for (int i = 0; i < DownloadBinActivity.this.downloadList.size(); i++) {
                    X431PadDtoSoft dto = (X431PadDtoSoft) DownloadBinActivity.this.downloadList.get(i);
                    DownloadBinActivity.this.downloadTaskMananger = DownloadBinManager.getInstance();
                    DownloadBinActivity.this.downloadTaskMananger.addDownloadTask(new DownloadBinNewVersion(DownloadBinActivity.this, DownloadBinActivity.this.handler, dto, DownloadBinActivity.this.serialNo));
                }
                return;
            }
            Toast.makeText(DownloadBinActivity.this, DownloadBinActivity.this.getResources().getString(C0136R.string.sd_not_use), 0).show();
        }
    }

    class MyBroadcastReceiver extends BroadcastReceiver {
        MyBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("show")) {
                DownloadBinActivity downloadBinActivity = DownloadBinActivity.this;
                downloadBinActivity.count = downloadBinActivity.count + 1;
                if (DownloadBinActivity.this.count == DownloadBinActivity.this.downloadList.size()) {
                    DownloadBinActivity.this.back.setVisibility(0);
                }
            }
        }
    }

    public DownloadBinActivity() {
        this.downloadList = new ArrayList();
        this.downloads = new HashMap();
        this.count = 0;
        this.handler = new C05441();
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
            this.downloadBinInfo = (SoftMaxVersion) intent.getSerializableExtra("downloadBinInfo");
            X431PadDtoSoft info = new X431PadDtoSoft();
            info.setSoftId("001");
            info.setSoftName("download");
            info.setVersionDetailId(new StringBuilder(String.valueOf(this.downloadBinInfo.getVersionDetailId())).toString());
            info.setVersionNo(this.downloadBinInfo.getVersionNo());
            this.downloadList.add(info);
        }
        findView();
    }

    private void findView() {
        int i;
        this.download_title = (TextView) findViewById(C0136R.id.download_title);
        this.download_title.setText(C0136R.string.FirmwareUpgrade);
        this.download_title.setTextSize(15.0f);
        this.back = (Button) findViewById(C0136R.id.back);
        this.back.setOnClickListener(new C05452());
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
        this.download.setVisibility(8);
        this.download.setOnClickListener(new C05463());
        this.download.setVisibility(4);
        if (Environment.getExternalStorageState().equals("mounted")) {
            DownloadBinManager.getInstance();
            new Thread(new DownloadBinManagerThread()).start();
            for (i = 0; i < this.downloadList.size(); i++) {
                X431PadDtoSoft dto = (X431PadDtoSoft) this.downloadList.get(i);
                this.downloadTaskMananger = DownloadBinManager.getInstance();
                this.downloadTaskMananger.addDownloadTask(new DownloadBinNewVersion(this, this.handler, dto, this.serialNo));
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

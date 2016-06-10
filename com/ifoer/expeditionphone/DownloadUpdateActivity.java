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
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.adapter.DownloadSoftwareAdapter;
import com.ifoer.db.DBDao;
import com.ifoer.dbentity.CarVersionInfo;
import com.ifoer.dbentity.DownloadStatus;
import com.ifoer.download.DownloadTask;
import com.ifoer.download.DownloadTaskManager;
import com.ifoer.download.DownloadTaskManagerThread;
import com.ifoer.entity.Constant;
import com.ifoer.util.AndroidToLan;
import com.ifoer.util.DialogUtil;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.xbill.DNS.KEYRecord;

public class DownloadUpdateActivity extends Activity {
    private DownloadSoftwareAdapter adapter;
    private Button back;
    private int count;
    private ArrayList<X431PadDtoSoft> data;
    private Button download;
    private ArrayList<X431PadDtoSoft> downloadList;
    private ListView downloadListview;
    DownloadTaskManager downloadTaskMananger;
    private Map<String, DownloadStatus> downloads;
    Handler handler;
    private MyBroadcastReceiver myBroadcastReceiver;
    private String sdPath;
    private String serialNo;

    /* renamed from: com.ifoer.expeditionphone.DownloadUpdateActivity.1 */
    class C05521 extends Handler {
        C05521() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String softId = msg.obj;
            int fileSize = msg.arg2;
            int downloadSize = msg.arg1;
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    DownloadStatus status0 = new DownloadStatus();
                    status0.setStatus(DownloadUpdateActivity.this.getResources().getString(C0136R.string.download_fail));
                    status0.setFileSize((long) fileSize);
                    status0.setDownloadSize((long) downloadSize);
                    status0.setStatusRedownload(false);
                    DownloadUpdateActivity.this.downloads.put(softId, status0);
                    if (DownloadUpdateActivity.this.adapter != null) {
                        DownloadUpdateActivity.this.adapter.notifyDataSetChanged();
                    }
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    DownloadStatus status1 = new DownloadStatus();
                    status1.setStatus(DownloadUpdateActivity.this.getResources().getString(C0136R.string.download_now));
                    status1.setFileSize((long) fileSize);
                    status1.setDownloadSize((long) downloadSize);
                    status1.setStatusRedownload(false);
                    DownloadUpdateActivity.this.downloads.put(softId, status1);
                    if (DownloadUpdateActivity.this.adapter != null) {
                        DownloadUpdateActivity.this.adapter.notifyDataSetChanged();
                    }
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    DownloadStatus status2 = new DownloadStatus();
                    status2.setStatus(DownloadUpdateActivity.this.getResources().getString(C0136R.string.download_finish));
                    status2.setFileSize((long) fileSize);
                    status2.setDownloadSize((long) downloadSize);
                    status2.setStatusRedownload(false);
                    DownloadUpdateActivity.this.downloads.put(softId, status2);
                    if (DownloadUpdateActivity.this.adapter != null) {
                        DownloadUpdateActivity.this.adapter.notifyDataSetChanged();
                    }
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    DownloadStatus status3 = new DownloadStatus();
                    status3.setStatus(DownloadUpdateActivity.this.getResources().getString(C0136R.string.unziping));
                    status3.setFileSize((long) fileSize);
                    status3.setDownloadSize((long) fileSize);
                    status3.setStatusRedownload(false);
                    DownloadUpdateActivity.this.downloads.put(softId, status3);
                    if (DownloadUpdateActivity.this.adapter != null) {
                        DownloadUpdateActivity.this.adapter.notifyDataSetChanged();
                    }
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    DownloadStatus status4 = new DownloadStatus();
                    status4.setStatus(DownloadUpdateActivity.this.getResources().getString(C0136R.string.unzip_success));
                    status4.setFileSize((long) fileSize);
                    status4.setDownloadSize((long) fileSize);
                    status4.setStatusRedownload(false);
                    DownloadUpdateActivity.this.downloads.put(softId, status4);
                    if (DownloadUpdateActivity.this.adapter != null) {
                        DownloadUpdateActivity.this.adapter.notifyDataSetChanged();
                    }
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    DownloadStatus status5 = new DownloadStatus();
                    status5.setStatus(DownloadUpdateActivity.this.getResources().getString(C0136R.string.unzip_fail));
                    status5.setFileSize((long) fileSize);
                    status5.setDownloadSize((long) fileSize);
                    status5.setStatusRedownload(false);
                    DownloadUpdateActivity.this.downloads.put(softId, status5);
                    if (DownloadUpdateActivity.this.adapter != null) {
                        DownloadUpdateActivity.this.adapter.notifyDataSetChanged();
                    }
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DownloadUpdateActivity.2 */
    class C05532 implements OnClickListener {
        C05532() {
        }

        public void onClick(View v) {
            if (MainActivity.country == null || MainActivity.country.length() <= 0) {
                MainActivity.country = Locale.getDefault().getCountry();
            }
            String lanName = AndroidToLan.toLan(Locale.getDefault().getLanguage());
            ArrayList<CarVersionInfo> list = DBDao.getInstance(DownloadUpdateActivity.this).queryCarVersion(((X431PadDtoSoft) DownloadUpdateActivity.this.downloadList.get(0)).getSoftPackageID(), lanName, MySharedPreferences.getStringValue(DownloadUpdateActivity.this, MySharedPreferences.serialNoKey), MainActivity.database);
            Intent intent = new Intent(DownloadUpdateActivity.this, LoadDynamicLibsActivity.class);
            intent.putExtra("list", list);
            intent.putExtra("carId", ((X431PadDtoSoft) DownloadUpdateActivity.this.downloadList.get(0)).getSoftPackageID());
            intent.putExtra("lanName", lanName);
            DownloadUpdateActivity.this.startActivity(intent);
            DownloadUpdateActivity.this.finish();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DownloadUpdateActivity.3 */
    class C05543 implements OnClickListener {
        C05543() {
        }

        public void onClick(View v) {
            DownloadUpdateActivity.this.download.setVisibility(4);
            DownloadUpdateActivity.this.back.setVisibility(4);
            if (Environment.getExternalStorageState().equals("mounted")) {
                DownloadTaskManager.getInstance();
                new Thread(new DownloadTaskManagerThread()).start();
                for (int i = 0; i < DownloadUpdateActivity.this.downloadList.size(); i++) {
                    X431PadDtoSoft dto = (X431PadDtoSoft) DownloadUpdateActivity.this.downloadList.get(i);
                    DownloadUpdateActivity.this.downloadTaskMananger = DownloadTaskManager.getInstance();
                    DownloadUpdateActivity.this.downloadTaskMananger.addDownloadTask(new DownloadTask(DownloadUpdateActivity.this, DownloadUpdateActivity.this.handler, dto, DownloadUpdateActivity.this.serialNo));
                }
                return;
            }
            Toast.makeText(DownloadUpdateActivity.this, DownloadUpdateActivity.this.getResources().getString(C0136R.string.sd_not_use), 0).show();
        }
    }

    class MyBroadcastReceiver extends BroadcastReceiver {
        MyBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("show")) {
                DownloadUpdateActivity downloadUpdateActivity = DownloadUpdateActivity.this;
                downloadUpdateActivity.count = downloadUpdateActivity.count + 1;
                if (DownloadUpdateActivity.this.count == DownloadUpdateActivity.this.downloadList.size()) {
                    DownloadUpdateActivity.this.back.setVisibility(0);
                }
            }
        }
    }

    public DownloadUpdateActivity() {
        this.data = new ArrayList();
        this.sdPath = Constant.LOCAL_SERIALNO_PATH;
        this.downloads = new HashMap();
        this.count = 0;
        this.handler = new C05521();
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
        }
        findView();
    }

    private void findView() {
        this.back = (Button) findViewById(C0136R.id.back);
        this.back.setOnClickListener(new C05532());
        for (int i = 0; i < this.downloadList.size(); i++) {
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
        this.download.setOnClickListener(new C05543());
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

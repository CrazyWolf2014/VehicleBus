package com.ifoer.expeditionphone;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
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
import com.cnlaunch.framework.network.download.DownloadManager;
import com.cnlaunch.framework.utils.lang.LangManager;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.adapter.DownloadSoftwareAdapter;
import com.ifoer.dbentity.DownloadStatus;
import com.ifoer.download.DownloadTask;
import com.ifoer.download.DownloadTaskManager;
import com.ifoer.download.DownloadTaskManagerThread;
import com.ifoer.download.FirmUpgrade;
import com.ifoer.entity.Constant;
import com.ifoer.util.ApkDownLoad;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class DownloadAllSoftwareActivity extends Activity {
    private static final String apkPath;
    private static final String apkStrorePath;
    private DownloadSoftwareAdapter adapter;
    private int allDownload;
    private Button back;
    private int count;
    private ArrayList<X431PadDtoSoft> data;
    private String diagSotrePath;
    private Button download;
    private ArrayList<X431PadDtoSoft> downloadList;
    private ListView downloadListview;
    private DownloadManager downloadMgr;
    DownloadTaskManager downloadTaskMananger;
    private Map<String, DownloadStatus> downloads;
    private boolean faildDownLoad;
    private long firmDownLoadSize;
    private String firmSotrePath;
    Handler handler;
    private boolean hasDownLoadApk;
    private boolean hasDownLoadFirm;
    private int hasDownload;
    private long lastTime;
    private Context mContext;
    private MyBroadcastReceiver myBroadcastReceiver;
    private IntentFilter myIntentFilter;
    private String sdPath;
    private String serialNo;

    /* renamed from: com.ifoer.expeditionphone.DownloadAllSoftwareActivity.1 */
    class C05391 extends Handler {
        C05391() {
        }

        public void handleMessage(Message msg) {
            String softId = XmlPullParser.NO_NAMESPACE;
            long fileSize = 0;
            long downloadSize = 0;
            super.handleMessage(msg);
            if (msg.obj != null) {
                if (msg.obj.getClass().equals(X431PadDtoSoft.class)) {
                    X431PadDtoSoft dto = msg.obj;
                    softId = dto.getSoftId();
                    fileSize = dto.getFileSize();
                    downloadSize = dto.getDownloadSize();
                } else {
                    softId = msg.obj;
                    fileSize = (long) msg.arg2;
                    downloadSize = (long) msg.arg1;
                }
            }
            Intent i;
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    DownloadStatus status0 = new DownloadStatus();
                    status0.setStatus(DownloadAllSoftwareActivity.this.getResources().getString(C0136R.string.download_fail));
                    status0.setFileSize(fileSize);
                    status0.setDownloadSize(downloadSize);
                    status0.setStatusRedownload(false);
                    DownloadAllSoftwareActivity.this.downloads.put(softId, status0);
                    DownloadAllSoftwareActivity.this.faildDownLoad = true;
                    if (DownloadAllSoftwareActivity.this.adapter != null) {
                        DownloadAllSoftwareActivity.this.adapter.notifyDataSetChanged();
                    }
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    DownloadStatus status1 = new DownloadStatus();
                    status1.setStatus(DownloadAllSoftwareActivity.this.getResources().getString(C0136R.string.download_now));
                    status1.setFileSize(fileSize);
                    status1.setDownloadSize(downloadSize);
                    status1.setStatusRedownload(false);
                    DownloadAllSoftwareActivity.this.downloads.put(softId, status1);
                    if (DownloadAllSoftwareActivity.this.adapter != null) {
                        DownloadAllSoftwareActivity.this.adapter.notifyDataSetChanged();
                    }
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    DownloadStatus status2 = new DownloadStatus();
                    status2.setStatus(DownloadAllSoftwareActivity.this.getResources().getString(C0136R.string.download_finish));
                    status2.setFileSize(fileSize);
                    status2.setDownloadSize(downloadSize);
                    status2.setStatusRedownload(false);
                    DownloadAllSoftwareActivity.this.downloads.put(softId, status2);
                    if (DownloadAllSoftwareActivity.this.adapter != null) {
                        DownloadAllSoftwareActivity.this.adapter.notifyDataSetChanged();
                    }
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    DownloadStatus status3 = new DownloadStatus();
                    status3.setStatus(DownloadAllSoftwareActivity.this.getResources().getString(C0136R.string.unziping));
                    status3.setFileSize(fileSize);
                    status3.setDownloadSize(fileSize);
                    status3.setStatusRedownload(false);
                    DownloadAllSoftwareActivity.this.downloads.put(softId, status3);
                    if (DownloadAllSoftwareActivity.this.adapter != null) {
                        DownloadAllSoftwareActivity.this.adapter.notifyDataSetChanged();
                    }
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    DownloadAllSoftwareActivity downloadAllSoftwareActivity;
                    DownloadStatus status4 = new DownloadStatus();
                    if (!softId.equals("001")) {
                        if (!softId.equals("002")) {
                            status4.setStatus(DownloadAllSoftwareActivity.this.getResources().getString(C0136R.string.unzip_success));
                            status4.setFileSize(fileSize);
                            status4.setDownloadSize(fileSize);
                            downloadAllSoftwareActivity = DownloadAllSoftwareActivity.this;
                            downloadAllSoftwareActivity.hasDownload = downloadAllSoftwareActivity.hasDownload + 1;
                            status4.setStatusRedownload(false);
                            DownloadAllSoftwareActivity.this.downloads.put(softId, status4);
                            if (DownloadAllSoftwareActivity.this.adapter != null) {
                                DownloadAllSoftwareActivity.this.adapter.notifyDataSetChanged();
                            }
                            if (softId.equals("002")) {
                                DownloadAllSoftwareActivity.this.firmDownLoadSize = fileSize;
                                DownloadAllSoftwareActivity.this.hasDownLoadFirm = true;
                            }
                            if (softId.equals("001")) {
                                DownloadAllSoftwareActivity.this.hasDownLoadApk = true;
                            }
                            if (DownloadAllSoftwareActivity.this.hasDownload == DownloadAllSoftwareActivity.this.allDownload) {
                                if (!DownloadAllSoftwareActivity.this.hasDownLoadApk) {
                                    if (!DownloadAllSoftwareActivity.this.hasDownLoadFirm) {
                                        Toast.makeText(DownloadAllSoftwareActivity.this, DownloadAllSoftwareActivity.this.getResources().getString(C0136R.string.download_finish), 0).show();
                                        DownloadAllSoftwareActivity.this.setResult(10);
                                        DownloadAllSoftwareActivity.this.finish();
                                        return;
                                    }
                                }
                                if (DownloadAllSoftwareActivity.this.hasDownLoadFirm) {
                                    if (DownloadAllSoftwareActivity.this.hasDownLoadApk) {
                                        i = new Intent("android.intent.action.VIEW");
                                        i.setDataAndType(Uri.parse("file://" + DownloadAllSoftwareActivity.apkPath), "application/vnd.android.package-archive");
                                        DownloadAllSoftwareActivity.this.startActivity(i);
                                        return;
                                    }
                                    return;
                                }
                                new FirmUpgrade(DownloadAllSoftwareActivity.this, DownloadAllSoftwareActivity.this.handler).upgrade();
                            }
                        }
                    }
                    status4.setStatus(DownloadAllSoftwareActivity.this.getResources().getString(C0136R.string.download_finish));
                    status4.setFileSize(fileSize);
                    status4.setDownloadSize(fileSize);
                    downloadAllSoftwareActivity = DownloadAllSoftwareActivity.this;
                    downloadAllSoftwareActivity.hasDownload = downloadAllSoftwareActivity.hasDownload + 1;
                    status4.setStatusRedownload(false);
                    DownloadAllSoftwareActivity.this.downloads.put(softId, status4);
                    if (DownloadAllSoftwareActivity.this.adapter != null) {
                        DownloadAllSoftwareActivity.this.adapter.notifyDataSetChanged();
                    }
                    if (softId.equals("002")) {
                        DownloadAllSoftwareActivity.this.firmDownLoadSize = fileSize;
                        DownloadAllSoftwareActivity.this.hasDownLoadFirm = true;
                    }
                    if (softId.equals("001")) {
                        DownloadAllSoftwareActivity.this.hasDownLoadApk = true;
                    }
                    if (DownloadAllSoftwareActivity.this.hasDownload == DownloadAllSoftwareActivity.this.allDownload) {
                        if (DownloadAllSoftwareActivity.this.hasDownLoadApk) {
                            if (DownloadAllSoftwareActivity.this.hasDownLoadFirm) {
                                Toast.makeText(DownloadAllSoftwareActivity.this, DownloadAllSoftwareActivity.this.getResources().getString(C0136R.string.download_finish), 0).show();
                                DownloadAllSoftwareActivity.this.setResult(10);
                                DownloadAllSoftwareActivity.this.finish();
                                return;
                            }
                        }
                        if (DownloadAllSoftwareActivity.this.hasDownLoadFirm) {
                            if (DownloadAllSoftwareActivity.this.hasDownLoadApk) {
                                i = new Intent("android.intent.action.VIEW");
                                i.setDataAndType(Uri.parse("file://" + DownloadAllSoftwareActivity.apkPath), "application/vnd.android.package-archive");
                                DownloadAllSoftwareActivity.this.startActivity(i);
                                return;
                            }
                            return;
                        }
                        new FirmUpgrade(DownloadAllSoftwareActivity.this, DownloadAllSoftwareActivity.this.handler).upgrade();
                    }
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    DownloadStatus status5 = new DownloadStatus();
                    status5.setStatus(DownloadAllSoftwareActivity.this.getResources().getString(C0136R.string.unzip_fail));
                    DownloadAllSoftwareActivity.this.faildDownLoad = true;
                    status5.setFileSize(fileSize);
                    status5.setDownloadSize(fileSize);
                    status5.setStatusRedownload(false);
                    DownloadAllSoftwareActivity.this.downloads.put(softId, status5);
                    if (DownloadAllSoftwareActivity.this.adapter != null) {
                        DownloadAllSoftwareActivity.this.adapter.notifyDataSetChanged();
                    }
                case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                    DownloadStatus status6 = new DownloadStatus();
                    status6.setStatus(DownloadAllSoftwareActivity.this.getResources().getString(C0136R.string.sdcard_storage_insufficient));
                    status6.setFileSize(fileSize);
                    status6.setDownloadSize(downloadSize);
                    status6.setStatusRedownload(false);
                    DownloadAllSoftwareActivity.this.downloads.put(softId, status6);
                    if (DownloadAllSoftwareActivity.this.adapter != null) {
                        DownloadAllSoftwareActivity.this.adapter.notifyDataSetChanged();
                    }
                case ApkDownLoad.DOWNlOAD_SD_LOW /*915*/:
                    if (DownloadAllSoftwareActivity.this.hasDownLoadApk) {
                        i = new Intent("android.intent.action.VIEW");
                        i.setDataAndType(Uri.parse("file://" + DownloadAllSoftwareActivity.apkPath), "application/vnd.android.package-archive");
                        DownloadAllSoftwareActivity.this.startActivity(i);
                        return;
                    }
                    SimpleDialog.reStart(DownloadAllSoftwareActivity.this);
                case ApkDownLoad.DOWNlOAD_NO_SD /*916*/:
                    DownloadStatus status9 = new DownloadStatus();
                    status9.setStatus(DownloadAllSoftwareActivity.this.getResources().getString(C0136R.string.installed_failed));
                    status9.setStatusRedownload(false);
                    status9.setDownloadSize(DownloadAllSoftwareActivity.this.firmDownLoadSize);
                    status9.setFileSize(DownloadAllSoftwareActivity.this.firmDownLoadSize);
                    DownloadAllSoftwareActivity.this.downloads.put("002", status9);
                    DownloadAllSoftwareActivity.this.faildDownLoad = true;
                    if (DownloadAllSoftwareActivity.this.adapter != null) {
                        DownloadAllSoftwareActivity.this.adapter.notifyDataSetChanged();
                    }
                    if (DownloadAllSoftwareActivity.this.hasDownLoadApk) {
                        i = new Intent("android.intent.action.VIEW");
                        i.setDataAndType(Uri.parse("file://" + DownloadAllSoftwareActivity.apkPath), "application/vnd.android.package-archive");
                        DownloadAllSoftwareActivity.this.startActivity(i);
                        return;
                    }
                    SimpleDialog.reStart(DownloadAllSoftwareActivity.this);
                case ApkDownLoad.DOWNLOAD_FINISHED /*917*/:
                    DownloadStatus status10 = new DownloadStatus();
                    status10.setStatus(DownloadAllSoftwareActivity.this.getResources().getString(C0136R.string.installed_failed));
                    status10.setStatusRedownload(false);
                    status10.setDownloadSize(DownloadAllSoftwareActivity.this.firmDownLoadSize);
                    status10.setFileSize(DownloadAllSoftwareActivity.this.firmDownLoadSize);
                    DownloadAllSoftwareActivity.this.downloads.put("002", status10);
                    DownloadAllSoftwareActivity.this.faildDownLoad = true;
                    if (DownloadAllSoftwareActivity.this.adapter != null) {
                        DownloadAllSoftwareActivity.this.adapter.notifyDataSetChanged();
                    }
                    if (DownloadAllSoftwareActivity.this.hasDownLoadApk) {
                        i = new Intent("android.intent.action.VIEW");
                        i.setDataAndType(Uri.parse("file://" + DownloadAllSoftwareActivity.apkPath), "application/vnd.android.package-archive");
                        DownloadAllSoftwareActivity.this.startActivity(i);
                        return;
                    }
                    Toast.makeText(DownloadAllSoftwareActivity.this, DownloadAllSoftwareActivity.this.getResources().getString(C0136R.string.download_finish), 0).show();
                    DownloadAllSoftwareActivity.this.setResult(10);
                    DownloadAllSoftwareActivity.this.finish();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DownloadAllSoftwareActivity.2 */
    class C05402 implements OnClickListener {
        C05402() {
        }

        public void onClick(View v) {
            if (DownloadAllSoftwareActivity.this.hasDownload < DownloadAllSoftwareActivity.this.allDownload || DownloadAllSoftwareActivity.this.faildDownLoad) {
                DownloadAllSoftwareActivity.this.ExitDialog();
                return;
            }
            DownloadAllSoftwareActivity.this.setResult(10);
            DownloadAllSoftwareActivity.this.finish();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DownloadAllSoftwareActivity.3 */
    class C05413 implements OnClickListener {
        C05413() {
        }

        public void onClick(View v) {
            DownloadAllSoftwareActivity.this.download.setVisibility(4);
            DownloadAllSoftwareActivity.this.back.setVisibility(4);
            if (Environment.getExternalStorageState().equals("mounted")) {
                DownloadTaskManager.getInstance();
                new Thread(new DownloadTaskManagerThread()).start();
                for (int i = 0; i < DownloadAllSoftwareActivity.this.downloadList.size(); i++) {
                    X431PadDtoSoft dto = (X431PadDtoSoft) DownloadAllSoftwareActivity.this.downloadList.get(i);
                    DownloadAllSoftwareActivity.this.downloadTaskMananger = DownloadTaskManager.getInstance();
                    DownloadAllSoftwareActivity.this.downloadTaskMananger.addDownloadTask(new DownloadTask(DownloadAllSoftwareActivity.this, DownloadAllSoftwareActivity.this.handler, dto, DownloadAllSoftwareActivity.this.serialNo));
                }
                return;
            }
            Toast.makeText(DownloadAllSoftwareActivity.this, DownloadAllSoftwareActivity.this.getResources().getString(C0136R.string.sd_not_use), 0).show();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DownloadAllSoftwareActivity.4 */
    class C05424 implements DialogInterface.OnClickListener {
        C05424() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            Constant.needExistDownLoad = true;
            if (DownloadAllSoftwareActivity.this.downloadTaskMananger != null) {
                DownloadAllSoftwareActivity.this.downloadTaskMananger.taskIdSet.clear();
            }
            Constant.needExistDownLoad = true;
            DownloadAllSoftwareActivity.this.setResult(11);
            DownloadAllSoftwareActivity.this.finish();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DownloadAllSoftwareActivity.5 */
    class C05435 implements DialogInterface.OnClickListener {
        C05435() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class MyBroadcastReceiver extends BroadcastReceiver {
        MyBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("show")) {
                DownloadAllSoftwareActivity downloadAllSoftwareActivity = DownloadAllSoftwareActivity.this;
                downloadAllSoftwareActivity.count = downloadAllSoftwareActivity.count + 1;
                if (DownloadAllSoftwareActivity.this.count == DownloadAllSoftwareActivity.this.downloadList.size()) {
                    DownloadAllSoftwareActivity.this.back.setVisibility(0);
                }
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - DownloadAllSoftwareActivity.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                DownloadAllSoftwareActivity.this.lastTime = System.currentTimeMillis();
            }
        }
    }

    public DownloadAllSoftwareActivity() {
        this.data = new ArrayList();
        this.sdPath = Constant.LOCAL_SERIALNO_PATH;
        this.downloads = new HashMap();
        this.count = 0;
        this.hasDownload = 0;
        this.faildDownLoad = false;
        this.hasDownLoadApk = false;
        this.hasDownLoadFirm = false;
        this.firmSotrePath = Constant.STORE_PATH;
        this.diagSotrePath = Constant.UPLOAD_ZIP_PATH;
        this.handler = new C05391();
    }

    static {
        apkPath = Constant.APK_PATH + "CRP229.apk";
        apkStrorePath = Constant.APK_PATH;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.download_software);
        Constant.needExistDownLoad = false;
        MyApplication.getInstance().addActivity(this);
        this.mContext = this;
        registerBoradcastReceiver();
        Intent intent = getIntent();
        if (intent != null) {
            this.downloadList = (ArrayList) intent.getSerializableExtra("downloadList");
            this.allDownload = this.downloadList.size();
            MySharedPreferences.setInt(this, MySharedPreferences.isDownLoadTaskOver, this.downloadList.size());
        }
        this.downloadMgr = DownloadManager.getInstance();
        findView();
    }

    private void findView() {
        int i;
        this.back = (Button) findViewById(C0136R.id.back);
        this.back.setOnClickListener(new C05402());
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
        this.download.setOnClickListener(new C05413());
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

    private void registerBoradcastReceiver() {
        this.myBroadcastReceiver = new MyBroadcastReceiver();
        this.myIntentFilter = new IntentFilter();
        this.myIntentFilter.addAction("show");
        this.myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(this.myBroadcastReceiver, this.myIntentFilter);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.downloadTaskMananger != null) {
            this.downloadTaskMananger.taskIdSet.clear();
        }
        if (this.myBroadcastReceiver != null) {
            unregisterReceiver(this.myBroadcastReceiver);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0136R.menu.main, menu);
        return true;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public String getDownloadFileName(X431PadDtoSoft bean) {
        StringBuilder nameBuilder = new StringBuilder(bean.getSoftPackageID() + "_" + bean.getVersionNo().replace(".", "_") + "_" + LangManager.getLangCode(bean.getLanId()));
        if (bean.getType() == 1) {
            nameBuilder.append(".apk");
        } else if (bean.getType() == 2) {
            nameBuilder.append(".zip");
        } else if (bean.getType() == 3) {
            nameBuilder.append(".zip");
        }
        return nameBuilder.toString();
    }

    private String getDownLoadPath(int type) {
        if (type == 1) {
            return apkPath;
        }
        if (type == 2) {
            return this.firmSotrePath;
        }
        return this.diagSotrePath;
    }

    public void ExitDialog() {
        Builder builder = new Builder(this);
        builder.setMessage(getString(C0136R.string.exitDownLoad));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(C0136R.string.enter), new C05424());
        builder.setNegativeButton(getString(C0136R.string.cancel), new C05435());
        builder.create().show();
    }
}

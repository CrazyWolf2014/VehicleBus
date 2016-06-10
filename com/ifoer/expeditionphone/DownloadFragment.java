package com.ifoer.expeditionphone;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.cnlaunch.framework.network.download.DownLoadCallback;
import com.cnlaunch.framework.network.download.DownloadManager;
import com.cnlaunch.framework.network.download.DownloadParam;
import com.cnlaunch.framework.network.http.RequestParams;
import com.cnlaunch.framework.utils.NLog;
import com.cnlaunch.framework.utils.lang.LangManager;
import com.cnlaunch.mycar.jni.FileUtils;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.cnlaunch.x431pro.utils.file.UnZipListener;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.adapter.DownloadSoftwareAdapter;
import com.ifoer.db.DBDao;
import com.ifoer.db.SaveDataRunnable;
import com.ifoer.db.SaveDownloadRunnable;
import com.ifoer.dbentity.CarVersionInfo;
import com.ifoer.dbentity.DownloadStatus;
import com.ifoer.download.FirmUpgrade;
import com.ifoer.entity.Constant;
import com.ifoer.util.ApkDownLoad;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

@SuppressLint({"HandlerLeak"})
public class DownloadFragment extends BaseActivity implements OnClickListener {
    private static final int DEFAULT_MAX_CONNECTIONS = 5;
    public static String DOWNLOADLIST_KEY;
    private static final String apkPath;
    private static final String apkStrorePath;
    public static String country;
    private final int MSG_CONFLICTING_SIGNATURE;
    private final int MSG_DOWNLOAD_FINISHED;
    private final int MSG_ENABLE_DOWNLOAD_BUTTON;
    private final int MSG_LESS_SPACE;
    private final int MSG_LOGOUT;
    private final int MSG_NETWORK_ERROR;
    private final int MSG_REFRESH_UI;
    private final int MSG_RELOGIN;
    private final int MSG_STOP;
    private final int MSG_UPDATE_LIST;
    private final int MSG_UPDATE_STATISTICS;
    public final int REQ_DOFILE_CODE;
    private DownloadSoftwareAdapter adapter;
    private int allDownload;
    private Button back;
    public ArrayList<CarVersionInfo> data;
    private String diagSotrePath;
    private String diagUnzipPath;
    private ArrayList<X431PadDtoSoft> downloadList;
    private ListView downloadListview;
    private DownloadManager downloadMgr;
    private Map<String, DownloadStatus> downloads;
    private boolean faildDownLoad;
    private String fileUnzipPath;
    private long firmDownLoadSize;
    private String firmSotrePath;
    private String firmUnzipPath;
    private boolean hasDownLoadApk;
    private boolean hasDownLoadFirm;
    private int hasDownload;
    private Context mContext;
    private Integer mDownloadCount;
    private Integer mDownloadFailed;
    private Integer mDownloadOK;
    private boolean mIsHasInstallManually;
    private boolean mIsRoot;
    private boolean mIsWaiting;
    private BroadcastReceiver mLogoutBroadcast;
    private IntentFilter mLogoutFilter;
    private Handler mUIHandler;
    private ProgressDialog progressDialogs;
    private String serialNo;
    private final String tag;
    private ThreadPoolExecutor threadPool;

    /* renamed from: com.ifoer.expeditionphone.DownloadFragment.1 */
    class C05481 extends BroadcastReceiver {

        /* renamed from: com.ifoer.expeditionphone.DownloadFragment.1.1 */
        class C05471 extends Thread {
            C05471() {
            }

            public void run() {
                try {
                    DownloadFragment.this.threadPool.awaitTermination(10000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DownloadFragment.this.mUIHandler.sendMessage(DownloadFragment.this.mUIHandler.obtainMessage(13, 0, 0));
            }
        }

        C05481() {
        }

        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.getAction().equalsIgnoreCase("logout")) {
                DownloadFragment.this.downloadMgr.setDownLoadCallback(null);
                DownloadFragment.this.downloadMgr.close();
                DownloadFragment.this.threadPool.shutdownNow();
                if (DownloadFragment.this.threadPool.isTerminating()) {
                    new C05471().start();
                    return;
                }
                DownloadFragment.this.mUIHandler.sendMessage(DownloadFragment.this.mUIHandler.obtainMessage(13, 0, 0));
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DownloadFragment.2 */
    class C05492 extends Handler {
        C05492() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String softId = XmlPullParser.NO_NAMESPACE;
            if (msg.obj != null) {
                X431PadDtoSoft bean = msg.obj;
                softId = bean.getSoftId();
                long fileSize = bean.getFileSize();
                long downloadSize = bean.getDownloadSize();
                Intent i;
                switch (msg.what) {
                    case KEYRecord.OWNER_USER /*0*/:
                        DownloadStatus status0 = new DownloadStatus();
                        status0.setStatus(DownloadFragment.this.getResources().getString(C0136R.string.download_fail));
                        status0.setFileSize(fileSize);
                        status0.setDownloadSize(downloadSize);
                        status0.setStatusRedownload(false);
                        DownloadFragment.this.downloads.put(softId, status0);
                        DownloadFragment.this.faildDownLoad = true;
                        if (DownloadFragment.this.adapter != null) {
                            DownloadFragment.this.adapter.notifyDataSetChanged();
                            return;
                        }
                        return;
                    case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                        DownloadStatus status1 = new DownloadStatus();
                        status1.setStatus(DownloadFragment.this.getResources().getString(C0136R.string.download_now));
                        status1.setFileSize(fileSize);
                        status1.setDownloadSize(downloadSize);
                        status1.setStatusRedownload(false);
                        DownloadFragment.this.downloads.put(softId, status1);
                        if (DownloadFragment.this.adapter != null) {
                            DownloadFragment.this.adapter.notifyDataSetChanged();
                            return;
                        }
                        return;
                    case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                        DownloadStatus status2 = new DownloadStatus();
                        status2.setStatus(DownloadFragment.this.getResources().getString(C0136R.string.download_finish));
                        status2.setFileSize(fileSize);
                        status2.setDownloadSize(downloadSize);
                        status2.setStatusRedownload(false);
                        DownloadFragment.this.downloads.put(softId, status2);
                        if (DownloadFragment.this.adapter != null) {
                            DownloadFragment.this.adapter.notifyDataSetChanged();
                            return;
                        }
                        return;
                    case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                        DownloadStatus status3 = new DownloadStatus();
                        status3.setStatus(DownloadFragment.this.getResources().getString(C0136R.string.unziping));
                        status3.setFileSize(fileSize);
                        status3.setDownloadSize(fileSize);
                        status3.setStatusRedownload(false);
                        DownloadFragment.this.downloads.put(softId, status3);
                        if (DownloadFragment.this.adapter != null) {
                            DownloadFragment.this.adapter.notifyDataSetChanged();
                            return;
                        }
                        return;
                    case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                        DownloadFragment downloadFragment;
                        DownloadStatus status4 = new DownloadStatus();
                        if (!softId.equals("001")) {
                            if (!softId.equals("002")) {
                                status4.setStatus(DownloadFragment.this.getResources().getString(C0136R.string.unzip_success));
                                status4.setFileSize(fileSize);
                                status4.setDownloadSize(fileSize);
                                downloadFragment = DownloadFragment.this;
                                downloadFragment.hasDownload = downloadFragment.hasDownload + 1;
                                status4.setStatusRedownload(false);
                                DownloadFragment.this.downloads.put(softId, status4);
                                if (DownloadFragment.this.adapter != null) {
                                    DownloadFragment.this.adapter.notifyDataSetChanged();
                                }
                                if (softId.equals("002")) {
                                    DownloadFragment.this.firmDownLoadSize = fileSize;
                                    DownloadFragment.this.hasDownLoadFirm = true;
                                }
                                if (softId.equals("001")) {
                                    DownloadFragment.this.hasDownLoadApk = true;
                                }
                                if (DownloadFragment.this.hasDownload == DownloadFragment.this.allDownload) {
                                    if (!DownloadFragment.this.hasDownLoadApk) {
                                        if (!DownloadFragment.this.hasDownLoadFirm) {
                                            Toast.makeText(DownloadFragment.this.mContext, DownloadFragment.this.getResources().getString(C0136R.string.download_finish), 0).show();
                                            DownloadFragment.this.setResult(10);
                                            DownloadFragment.this.finish();
                                            return;
                                        }
                                    }
                                    if (DownloadFragment.this.hasDownLoadFirm) {
                                        if (DownloadFragment.this.hasDownLoadApk) {
                                            i = new Intent("android.intent.action.VIEW");
                                            i.setDataAndType(Uri.parse("file://" + DownloadFragment.apkPath), "application/vnd.android.package-archive");
                                            DownloadFragment.this.mContext.startActivity(i);
                                            return;
                                        }
                                        return;
                                    }
                                    new FirmUpgrade(DownloadFragment.this.mContext, DownloadFragment.this.mUIHandler).upgrade();
                                    return;
                                }
                                return;
                            }
                        }
                        status4.setStatus(DownloadFragment.this.getResources().getString(C0136R.string.download_finish));
                        status4.setFileSize(fileSize);
                        status4.setDownloadSize(fileSize);
                        downloadFragment = DownloadFragment.this;
                        downloadFragment.hasDownload = downloadFragment.hasDownload + 1;
                        status4.setStatusRedownload(false);
                        DownloadFragment.this.downloads.put(softId, status4);
                        if (DownloadFragment.this.adapter != null) {
                            DownloadFragment.this.adapter.notifyDataSetChanged();
                        }
                        if (softId.equals("002")) {
                            DownloadFragment.this.firmDownLoadSize = fileSize;
                            DownloadFragment.this.hasDownLoadFirm = true;
                        }
                        if (softId.equals("001")) {
                            DownloadFragment.this.hasDownLoadApk = true;
                        }
                        if (DownloadFragment.this.hasDownload == DownloadFragment.this.allDownload) {
                            if (DownloadFragment.this.hasDownLoadApk) {
                                if (DownloadFragment.this.hasDownLoadFirm) {
                                    Toast.makeText(DownloadFragment.this.mContext, DownloadFragment.this.getResources().getString(C0136R.string.download_finish), 0).show();
                                    DownloadFragment.this.setResult(10);
                                    DownloadFragment.this.finish();
                                    return;
                                }
                            }
                            if (DownloadFragment.this.hasDownLoadFirm) {
                                if (DownloadFragment.this.hasDownLoadApk) {
                                    i = new Intent("android.intent.action.VIEW");
                                    i.setDataAndType(Uri.parse("file://" + DownloadFragment.apkPath), "application/vnd.android.package-archive");
                                    DownloadFragment.this.mContext.startActivity(i);
                                    return;
                                }
                                return;
                            }
                            new FirmUpgrade(DownloadFragment.this.mContext, DownloadFragment.this.mUIHandler).upgrade();
                            return;
                        }
                        return;
                    case DownloadFragment.DEFAULT_MAX_CONNECTIONS /*5*/:
                        DownloadStatus status5 = new DownloadStatus();
                        status5.setStatus(DownloadFragment.this.getResources().getString(C0136R.string.unzip_fail));
                        DownloadFragment.this.faildDownLoad = true;
                        status5.setFileSize(fileSize);
                        status5.setDownloadSize(fileSize);
                        status5.setStatusRedownload(false);
                        DownloadFragment.this.downloads.put(softId, status5);
                        if (DownloadFragment.this.adapter != null) {
                            DownloadFragment.this.adapter.notifyDataSetChanged();
                            return;
                        }
                        return;
                    case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                        DownloadStatus status6 = new DownloadStatus();
                        status6.setStatus(DownloadFragment.this.getResources().getString(C0136R.string.sdcard_storage_insufficient));
                        status6.setFileSize(fileSize);
                        status6.setDownloadSize(downloadSize);
                        status6.setStatusRedownload(false);
                        DownloadFragment.this.downloads.put(softId, status6);
                        if (DownloadFragment.this.adapter != null) {
                            DownloadFragment.this.adapter.notifyDataSetChanged();
                            return;
                        }
                        return;
                    case ApkDownLoad.DOWNlOAD_SD_LOW /*915*/:
                        if (DownloadFragment.this.hasDownLoadApk) {
                            i = new Intent("android.intent.action.VIEW");
                            i.setDataAndType(Uri.parse("file://" + DownloadFragment.apkPath), "application/vnd.android.package-archive");
                            DownloadFragment.this.mContext.startActivity(i);
                            return;
                        }
                        SimpleDialog.reStart(DownloadFragment.this.mContext);
                        return;
                    case ApkDownLoad.DOWNlOAD_NO_SD /*916*/:
                        DownloadStatus status9 = new DownloadStatus();
                        status9.setStatus(DownloadFragment.this.getResources().getString(C0136R.string.installed_failed));
                        status9.setStatusRedownload(false);
                        status9.setDownloadSize(DownloadFragment.this.firmDownLoadSize);
                        status9.setFileSize(DownloadFragment.this.firmDownLoadSize);
                        DownloadFragment.this.downloads.put("002", status9);
                        DownloadFragment.this.faildDownLoad = true;
                        if (DownloadFragment.this.adapter != null) {
                            DownloadFragment.this.adapter.notifyDataSetChanged();
                        }
                        if (DownloadFragment.this.hasDownLoadApk) {
                            i = new Intent("android.intent.action.VIEW");
                            i.setDataAndType(Uri.parse("file://" + DownloadFragment.apkPath), "application/vnd.android.package-archive");
                            DownloadFragment.this.mContext.startActivity(i);
                            return;
                        }
                        SimpleDialog.reStart(DownloadFragment.this.mContext);
                        return;
                    case ApkDownLoad.DOWNLOAD_FINISHED /*917*/:
                        DownloadStatus status10 = new DownloadStatus();
                        status10.setStatus(DownloadFragment.this.getResources().getString(C0136R.string.installed_failed));
                        status10.setStatusRedownload(false);
                        status10.setDownloadSize(DownloadFragment.this.firmDownLoadSize);
                        status10.setFileSize(DownloadFragment.this.firmDownLoadSize);
                        DownloadFragment.this.downloads.put("002", status10);
                        DownloadFragment.this.faildDownLoad = true;
                        if (DownloadFragment.this.adapter != null) {
                            DownloadFragment.this.adapter.notifyDataSetChanged();
                        }
                        if (DownloadFragment.this.hasDownLoadApk) {
                            i = new Intent("android.intent.action.VIEW");
                            i.setDataAndType(Uri.parse("file://" + DownloadFragment.apkPath), "application/vnd.android.package-archive");
                            DownloadFragment.this.mContext.startActivity(i);
                            return;
                        }
                        Toast.makeText(DownloadFragment.this.mContext, DownloadFragment.this.getResources().getString(C0136R.string.download_finish), 0).show();
                        DownloadFragment.this.setResult(10);
                        DownloadFragment.this.finish();
                        return;
                    default:
                        return;
                }
            }
            Log.i("DownLoadFram", "msg.what" + msg.what);
        }
    }

    class ZipThread implements Runnable {
        private String fileName;
        private String filePath;
        private String softPackageID;

        public ZipThread(String fileName, String filePath, String softPackageID) {
            this.fileName = fileName;
            this.filePath = filePath;
            this.softPackageID = softPackageID;
        }

        public void run() {
            Iterator it = DownloadFragment.this.downloadList.iterator();
            while (it.hasNext()) {
                X431PadDtoSoft bean = (X431PadDtoSoft) it.next();
                if (!TextUtils.isEmpty(this.fileName) && this.fileName.equals(bean.getFileName())) {
                    Log.i("DownLoadFram", "\u89e3\u538b" + this.fileName + bean.getType());
                    String zip;
                    String car;
                    if (3 == bean.getType()) {
                        DownloadFragment.this.fileUnzipPath = DownloadFragment.this.diagUnzipPath;
                        String carVersion = new StringBuilder(String.valueOf(DownloadFragment.this.fileUnzipPath)).append("car").append("/DIAGNOSTIC/VEHICLES/").append(this.softPackageID).append(FilePathGenerator.ANDROID_DIR_SEP).toString();
                        if (this.softPackageID.equalsIgnoreCase("AutoSearch")) {
                            MySharedPreferences.setString(DownloadFragment.this.mContext, MySharedPreferences.vinAutoPaths, new StringBuilder(String.valueOf(carVersion)).append("versionNo").append(FilePathGenerator.ANDROID_DIR_SEP).toString());
                        }
                        zip = new StringBuilder(String.valueOf(DownloadFragment.this.getDownLoadPath(bean.getType()))).append(bean.getFileName()).toString();
                        car = new StringBuilder(String.valueOf(DownloadFragment.this.fileUnzipPath)).append("car").append(FilePathGenerator.ANDROID_DIR_SEP).toString();
                        DownloadFragment.this.data = DownloadFragment.this.deleteOldVersion(bean);
                        String serialNo = MySharedPreferences.getStringValue(DownloadFragment.this.mContext, MySharedPreferences.serialNoKey);
                        List<Double> versions = new ArrayList();
                        if (DownloadFragment.this.data.size() > 0) {
                            int i;
                            for (i = 0; i < DownloadFragment.this.data.size(); i++) {
                                String versionNoStr = ((CarVersionInfo) DownloadFragment.this.data.get(i)).getVersionNo();
                                List<Double> list = versions;
                                list.add(Double.valueOf(Double.parseDouble(versionNoStr.substring(1, versionNoStr.length()))));
                            }
                            Collections.sort(versions);
                            double minOldVersion = ((Double) versions.get(0)).doubleValue();
                            if (DownloadFragment.this.data.size() == 3) {
                                for (i = 0; i < DownloadFragment.this.data.size(); i++) {
                                    if (Double.parseDouble(((CarVersionInfo) DownloadFragment.this.data.get(i)).getVersionNo().substring(1, ((CarVersionInfo) DownloadFragment.this.data.get(i)).getVersionNo().length())) == minOldVersion) {
                                        boolean isSuccess = DownloadFragment.this.deleteDynamicFile(((CarVersionInfo) DownloadFragment.this.data.get(i)).getVersionDir());
                                        DBDao.getInstance(DownloadFragment.this.mContext).deleteDynamicLibrary(serialNo, ((CarVersionInfo) DownloadFragment.this.data.get(i)).getCarId(), ((CarVersionInfo) DownloadFragment.this.data.get(i)).getVersionNo(), MainActivity.database);
                                    }
                                }
                            }
                        }
                        String message = FileUtils.unZipEx(zip, car, false, new UnZipCallBack(bean));
                        new SaveDataRunnable(DownloadFragment.this.mContext, carVersion, zip, car, bean, DownloadFragment.this.mUIHandler, serialNo).start();
                        return;
                    } else if (1 == bean.getType()) {
                        DownloadFragment.this.hasDownLoadApk = true;
                        bean.setState(4);
                        DownloadFragment.this.mUIHandler.obtainMessage(bean.getState(), bean).sendToTarget();
                        Log.i("DownLoadTask", "\u4e0b\u8f7d\u5b8capK");
                        return;
                    } else if (2 == bean.getType()) {
                        DownloadFragment.this.fileUnzipPath = DownloadFragment.this.firmUnzipPath;
                        bean.setState(4);
                        DownloadFragment.this.mUIHandler.obtainMessage(bean.getState(), bean).sendToTarget();
                        zip = new StringBuilder(String.valueOf(DownloadFragment.this.getDownLoadPath(bean.getType()))).append(bean.getFileName()).toString();
                        String stringBuilder = new StringBuilder(String.valueOf(DownloadFragment.this.fileUnzipPath)).append(FilePathGenerator.ANDROID_DIR_SEP).toString();
                        new SaveDownloadRunnable(DownloadFragment.this.mContext, zip, car, DownloadFragment.this.mUIHandler, bean).start();
                        return;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DownloadFragment.3 */
    class C10963 extends DownLoadCallback {
        C10963() {
        }

        public void onLoading(String fileName, int bytesWritten, int totalSize) {
            super.onLoading(fileName, bytesWritten, totalSize);
            Iterator it = DownloadFragment.this.downloadList.iterator();
            while (it.hasNext()) {
                X431PadDtoSoft bean = (X431PadDtoSoft) it.next();
                if (fileName.equals(bean.getFileName())) {
                    bean.setDownloadSize((long) bytesWritten);
                    bean.setFileSize((long) totalSize);
                    bean.setState(1);
                    DownloadFragment.this.mUIHandler.obtainMessage(bean.getState(), bean).sendToTarget();
                    return;
                }
            }
        }

        public void onSuccess(String fileName, String filePath) {
            super.onSuccess(fileName, filePath);
            Iterator it = DownloadFragment.this.downloadList.iterator();
            while (it.hasNext()) {
                X431PadDtoSoft bean = (X431PadDtoSoft) it.next();
                if (fileName.equals(bean.getFileName())) {
                    Log.i("initManager", "\u4e0b\u8f7d\u6210\u529f" + bean.getSoftName());
                    bean.setState(2);
                    DownloadFragment.this.mUIHandler.obtainMessage(bean.getState(), bean).sendToTarget();
                    try {
                        DownloadFragment.this.threadPool.submit(new ZipThread(fileName, filePath, bean.getSoftPackageID()));
                        return;
                    } catch (RejectedExecutionException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }

        public void onFailure(String fileName, String strMsg) {
            Iterator it;
            X431PadDtoSoft bean;
            super.onFailure(fileName, strMsg);
            NLog.m917e(DownloadFragment.this.tag, "onFailure: " + fileName + ", strMsg: " + strMsg);
            boolean ENOSPC = false;
            if (strMsg != null && strMsg.contains("ENOSPC")) {
                ENOSPC = true;
            } else if (strMsg != null && (strMsg.contains("ETIMEDOUT") || strMsg.contains("UnknownHostException"))) {
                DownloadFragment.this.downloadMgr.setDownLoadCallback(null);
                if (DownloadFragment.this.mContext != null && DownloadFragment.this.downloadMgr.isRunning()) {
                    DownloadFragment.this.downloadMgr.close();
                    DownloadFragment.this.mUIHandler.sendMessage(DownloadFragment.this.mUIHandler.obtainMessage(12, 0, 0));
                    it = DownloadFragment.this.downloadList.iterator();
                    while (it.hasNext()) {
                        bean = (X431PadDtoSoft) it.next();
                        if (fileName.equals(bean.getFileName())) {
                            bean.setProgress(100);
                            bean.setState(0);
                            DownloadFragment.this.adapter.notifyDataSetChanged();
                            DownloadFragment.this.mUIHandler.obtainMessage(bean.getState(), bean).sendToTarget();
                            return;
                        }
                    }
                    return;
                }
            }
            it = DownloadFragment.this.downloadList.iterator();
            while (it.hasNext()) {
                bean = (X431PadDtoSoft) it.next();
                if (fileName.equals(bean.getFileName())) {
                    bean.setProgress(100);
                    if (ENOSPC) {
                        bean.setState(6);
                        DownloadFragment.this.mUIHandler.obtainMessage(bean.getState(), bean).sendToTarget();
                    } else {
                        bean.setState(0);
                        DownloadFragment.this.mUIHandler.obtainMessage(bean.getState(), bean).sendToTarget();
                    }
                    DownloadFragment.this.adapter.notifyDataSetChanged();
                    if (strMsg == null && strMsg.equals("Token is invalid!") && DownloadFragment.this.downloadMgr.isRunning()) {
                        DownloadFragment.this.downloadMgr.setDownLoadCallback(null);
                        DownloadFragment.this.downloadMgr.close();
                        DownloadFragment.this.mUIHandler.sendMessage(DownloadFragment.this.mUIHandler.obtainMessage(7, 0, 0));
                        DownloadFragment.this.mUIHandler.sendMessage(DownloadFragment.this.mUIHandler.obtainMessage(4, 0, 0));
                        return;
                    } else if (ENOSPC) {
                        Log.i("DownLoadFrame", "\u4e0b\u8f7d\u5931\u8d25");
                        return;
                    } else {
                        DownloadFragment.this.downloadMgr.setDownLoadCallback(null);
                        DownloadFragment.this.mUIHandler.sendMessage(DownloadFragment.this.mUIHandler.obtainMessage(7, 0, 0));
                        DownloadFragment.this.mUIHandler.sendMessage(DownloadFragment.this.mUIHandler.obtainMessage(11, 0, 0));
                    }
                }
            }
            DownloadFragment.this.adapter.notifyDataSetChanged();
            if (strMsg == null) {
            }
            if (ENOSPC) {
                Log.i("DownLoadFrame", "\u4e0b\u8f7d\u5931\u8d25");
                return;
            }
            DownloadFragment.this.downloadMgr.setDownLoadCallback(null);
            DownloadFragment.this.mUIHandler.sendMessage(DownloadFragment.this.mUIHandler.obtainMessage(7, 0, 0));
            DownloadFragment.this.mUIHandler.sendMessage(DownloadFragment.this.mUIHandler.obtainMessage(11, 0, 0));
        }
    }

    class UnZipCallBack implements UnZipListener {
        private X431PadDtoSoft bean;

        UnZipCallBack(X431PadDtoSoft bean) {
            this.bean = null;
            this.bean = bean;
        }

        public void start() {
            if (this.bean != null) {
                this.bean.setProgress(0);
                this.bean.setState(6);
                DownloadFragment.this.mUIHandler.sendMessage(DownloadFragment.this.mUIHandler.obtainMessage(9, 0, 0));
            }
        }

        public void progress(int step, int total) {
            if (this.bean != null) {
                this.bean.setProgress((step * 100) / total);
                DownloadFragment.this.mUIHandler.sendMessage(DownloadFragment.this.mUIHandler.obtainMessage(9, 0, 0));
            }
        }

        public void finished() {
            if (this.bean != null) {
                this.bean.setProgress(100);
                DownloadFragment.this.mUIHandler.sendMessage(DownloadFragment.this.mUIHandler.obtainMessage(9, 0, 0));
            }
        }

        public void error(int code, Throwable error) {
            NLog.m917e(DownloadFragment.this.tag, "Error code: " + code);
            boolean ENOSPC = false;
            if (error != null) {
                String strMsg = error.getMessage();
                NLog.m917e(DownloadFragment.this.tag, "Error code: " + code + " strMsg: " + strMsg);
                if (strMsg.contains("ENOSPC")) {
                    ENOSPC = true;
                }
            }
            if (this.bean != null) {
                this.bean.setProgress(100);
                if (ENOSPC) {
                    this.bean.setState(7);
                    DownloadFragment.this.downloadMgr.setDownLoadCallback(null);
                    DownloadFragment.this.mUIHandler.sendMessage(DownloadFragment.this.mUIHandler.obtainMessage(7, 0, 0));
                    DownloadFragment.this.mUIHandler.sendMessage(DownloadFragment.this.mUIHandler.obtainMessage(9, 0, 0));
                    DownloadFragment.this.mUIHandler.sendMessage(DownloadFragment.this.mUIHandler.obtainMessage(11, 0, 0));
                    return;
                }
                this.bean.setState(DownloadFragment.DEFAULT_MAX_CONNECTIONS);
                DownloadFragment.this.mUIHandler.sendMessage(DownloadFragment.this.mUIHandler.obtainMessage(9, 0, 0));
            }
        }
    }

    public DownloadFragment() {
        this.tag = DownloadFragment.class.getSimpleName();
        this.REQ_DOFILE_CODE = 9010;
        this.MSG_REFRESH_UI = 1;
        this.MSG_UPDATE_STATISTICS = 2;
        this.MSG_DOWNLOAD_FINISHED = 3;
        this.MSG_RELOGIN = 4;
        this.MSG_STOP = 7;
        this.MSG_CONFLICTING_SIGNATURE = 8;
        this.MSG_UPDATE_LIST = 9;
        this.MSG_ENABLE_DOWNLOAD_BUTTON = 10;
        this.MSG_LESS_SPACE = 11;
        this.MSG_NETWORK_ERROR = 12;
        this.MSG_LOGOUT = 13;
        this.mUIHandler = null;
        this.mDownloadCount = Integer.valueOf(0);
        this.mDownloadOK = Integer.valueOf(0);
        this.mDownloadFailed = Integer.valueOf(0);
        this.mIsHasInstallManually = false;
        this.mIsWaiting = false;
        this.mIsRoot = false;
        this.mLogoutBroadcast = null;
        this.mLogoutFilter = null;
        this.firmSotrePath = Constant.STORE_PATH;
        this.diagSotrePath = Constant.UPLOAD_ZIP_PATH;
        this.diagUnzipPath = Constant.LOCAL_SERIALNO_PATH;
        this.firmUnzipPath = Constant.UNZIP_PATH;
        this.hasDownLoadApk = false;
        this.hasDownLoadFirm = false;
        this.hasDownload = 0;
        this.faildDownLoad = false;
        this.downloads = new HashMap();
    }

    static {
        DOWNLOADLIST_KEY = "downloadList";
        apkPath = Constant.APK_PATH + "CRP229.apk";
        apkStrorePath = Constant.APK_PATH;
    }

    public void onDestroy() {
        super.onDestroy();
        NLog.m916d(this.tag, "onDestroy");
        this.downloadMgr.close();
        this.threadPool.shutdownNow();
        if (this.mLogoutBroadcast != null && this.mContext != null) {
            this.mContext.unregisterReceiver(this.mLogoutBroadcast);
            this.mLogoutBroadcast = null;
            NLog.m916d(this.tag, "onDestroy: unregisterReceiver: mLogoutBroadcast");
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.download_software);
        Constant.needExistDownLoad = false;
        MyApplication.getInstance().addActivity(this);
        this.mContext = this;
        this.progressDialogs = new ProgressDialog(this);
        this.progressDialogs.setMessage(getResources().getString(C0136R.string.shopping_wait));
        this.progressDialogs.setCancelable(false);
        String stringValue = MySharedPreferences.getStringValue(this, Constants.SERIALNO);
        this.serialNo = stringValue;
        this.serialNo = stringValue;
        country = Locale.getDefault().getCountry();
        Intent intent = getIntent();
        if (intent != null) {
            this.downloadList = (ArrayList) intent.getSerializableExtra("downloadList");
            this.allDownload = this.downloadList.size();
            MySharedPreferences.setInt(this, MySharedPreferences.isDownLoadTaskOver, this.downloadList.size());
        }
        this.mLogoutBroadcast = new C05481();
        for (int i = 0; i < this.downloadList.size(); i++) {
            DownloadStatus status = new DownloadStatus();
            status.setDownloadSize(0);
            status.setFileSize(1);
            status.setStatus(getResources().getString(C0136R.string.download_wait));
            this.downloads.put(((X431PadDtoSoft) this.downloadList.get(i)).getSoftId(), status);
        }
        this.mLogoutFilter = new IntentFilter("logout");
        this.mContext.registerReceiver(this.mLogoutBroadcast, this.mLogoutFilter);
        this.downloadListview = (ListView) findViewById(C0136R.id.downloadListview);
        this.adapter = new DownloadSoftwareAdapter(this.downloadList, this.downloads, this, this.mUIHandler);
        this.downloadListview.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
        this.mDownloadCount = Integer.valueOf(0);
        this.mDownloadOK = Integer.valueOf(0);
        this.mDownloadFailed = Integer.valueOf(0);
        initManager();
        initUIHandler();
        initData();
    }

    private void initData() {
        this.mIsWaiting = false;
        if (this.downloadList != null) {
            Iterator it = this.downloadList.iterator();
            while (it.hasNext()) {
                X431PadDtoSoft bean = (X431PadDtoSoft) it.next();
                if (!(4 == bean.getState() || (2 == bean.getState() && !this.mIsRoot && 1 == bean.getType()))) {
                    RequestParams params = new RequestParams();
                    params.put(Constants.serialNo, this.serialNo);
                    params.put("versionDetailId", bean.getVersionDetailId());
                    bean.setFileName(getDownloadFileName(bean));
                    bean.setState(0);
                    bean.setProgress(0);
                    DownloadParam download = new DownloadParam();
                    download.setContext(this.mContext);
                    download.setParams(params);
                    download.setFileSize(bean.getFileSize());
                    download.setUrl(bean.getUrl());
                    download.setFileName(bean.getFileName());
                    download.setVersionNo(bean.getVersionNo());
                    download.setDownPath(getDownLoadPath(bean.getType()));
                    download.setEnableBreakpoint(MySharedPreferences.getBooleanValue(this.mContext, Constants.ENABLE_BREAKPOINTRESUME, false));
                    this.downloadMgr.addHandler(download);
                }
            }
        }
    }

    private void initUIHandler() {
        this.mUIHandler = new C05492();
    }

    private void initManager() {
        this.threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(DEFAULT_MAX_CONNECTIONS);
        this.downloadMgr = DownloadManager.getInstance();
        this.downloadMgr.setDownLoadCallback(new C10963());
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

    public void updateAPKInstalledState(String packageName, boolean installed) {
        if (packageName != null) {
            Iterator it;
            X431PadDtoSoft bean;
            if (packageName.equals(Constants.PACKAGE_NAME_OSCILLOSCOPE)) {
                it = this.downloadList.iterator();
                while (it.hasNext()) {
                    bean = (X431PadDtoSoft) it.next();
                    if (bean.getSoftPackageID().equalsIgnoreCase(Constants.PACKAGE_ID_OSCILLOSCOPE_PADII) || bean.getSoftPackageID().equalsIgnoreCase(Constants.PACKAGE_ID_OSCILLOSCOPE_PRO)) {
                        if (2 != bean.getState() && installed) {
                            bean.setState(4);
                        } else if (!installed) {
                            bean.setState(DEFAULT_MAX_CONNECTIONS);
                        }
                    } else if (bean.getSoftPackageID().contains(Constants.PACKAGE_ID_OSCILLOSCOPE_PADII)) {
                        if (2 != bean.getState()) {
                        }
                        if (installed) {
                            bean.setState(DEFAULT_MAX_CONNECTIONS);
                        }
                    }
                }
            } else if (packageName.equals(Constants.PACKAGE_NAME_SENSOR) || packageName.equals(Constants.PACKAGE_NAME_SENSOR)) {
                it = this.downloadList.iterator();
                while (it.hasNext()) {
                    bean = (X431PadDtoSoft) it.next();
                    if (!bean.getSoftPackageID().equalsIgnoreCase(Constants.PACKAGE_ID_SENSOR_PADII)) {
                        if (bean.getSoftPackageID().equalsIgnoreCase(Constants.PACKAGE_ID_SENSOR_PRO)) {
                        }
                    }
                    if (2 == bean.getState() && installed) {
                        bean.setState(4);
                    } else if (!installed) {
                        bean.setState(DEFAULT_MAX_CONNECTIONS);
                    }
                }
            } else if (packageName.equals(Constants.PACKAGE_NAME_BATTERYTEST)) {
                it = this.downloadList.iterator();
                while (it.hasNext()) {
                    bean = (X431PadDtoSoft) it.next();
                    if (!bean.getSoftPackageID().equalsIgnoreCase(Constants.PACKAGE_ID_BATTERYTEST_PADII)) {
                        if (bean.getSoftPackageID().equalsIgnoreCase(Constants.PACKAGE_ID_BATTERYTEST_PRO)) {
                        }
                    }
                    if (2 == bean.getState() && installed) {
                        bean.setState(4);
                    } else if (!installed) {
                        bean.setState(DEFAULT_MAX_CONNECTIONS);
                    }
                }
            } else if (packageName.equals(Constants.PACKAGE_NAME_IGNITION)) {
                it = this.downloadList.iterator();
                while (it.hasNext()) {
                    bean = (X431PadDtoSoft) it.next();
                    if (!bean.getSoftPackageID().equalsIgnoreCase(Constants.PACKAGE_ID_IGNITION_PADII)) {
                        if (bean.getSoftPackageID().equalsIgnoreCase(Constants.PACKAGE_ID_IGNITION_PRO)) {
                        }
                    }
                    if (2 == bean.getState() && installed) {
                        bean.setState(4);
                    } else if (!installed) {
                        bean.setState(DEFAULT_MAX_CONNECTIONS);
                    }
                }
            } else if (!(packageName.equals(Constants.PACKAGE_NAME_PHOTO) || packageName.equals(Constants.PACKAGE_NAME_PHOTO))) {
                packageName.equals(Constants.PACKAGE_NAME_WEBBROWSER);
            }
            this.mUIHandler.sendMessage(this.mUIHandler.obtainMessage(9, 0, 0));
        }
    }

    private ArrayList<CarVersionInfo> deleteOldVersion(X431PadDtoSoft bean) {
        String serialNo1 = MySharedPreferences.getStringValue(this.mContext, MySharedPreferences.serialNoKey);
        ArrayList<CarVersionInfo> list = DBDao.getInstance(this.mContext).queryCarVersion(bean.getSoftPackageID(), Locale.getDefault().getCountry().toUpperCase(), serialNo1, MainActivity.database);
        if (list.size() > 0) {
            return list;
        }
        country = Constants.DEFAULT_LANGUAGE;
        return DBDao.getInstance(this.mContext).queryCarVersion(bean.getSoftPackageID(), Constants.DEFAULT_LANGUAGE, serialNo1, MainActivity.database);
    }

    private boolean deleteDynamicFile(String filePath) {
        deleteAllFile(filePath);
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    private void deleteAllFile(String path) {
        File file = new File(new StringBuilder(String.valueOf(path)).append(FilePathGenerator.ANDROID_DIR_SEP).toString());
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files.length > 0) {
                for (File eachFile : files) {
                    if (eachFile.exists()) {
                        eachFile.delete();
                    }
                }
            }
        }
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

    public void onClick(View arg0) {
    }
}

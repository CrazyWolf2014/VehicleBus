package com.ifoer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import com.car.result.DownloadBinResult;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.db.DBDao;
import com.ifoer.dbentity.DownloadStatus;
import com.ifoer.download.DownloadBinManager;
import com.ifoer.download.DownloadBinManagerThread;
import com.ifoer.download.DownloadBinNewVersion;
import com.ifoer.entity.SoftMaxVersion;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.webservice.UpdateSoftware;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public class MyDownLoadBinUpdate {
    public static String DOWNLOAD_BIN_BASE_VERSION = null;
    private static final String TAG = "MyDownLoadBinUpdate";
    private static MyDownLoadBinUpdate mydownLoadbinupdate;
    private HttpURLConnection con;
    private ArrayList<X431PadDtoSoft> downloadList;
    private DownloadBinManager downloadTaskMananger;
    private Map<String, DownloadStatus> downloads;
    @SuppressLint({"HandlerLeak"})
    Handler handler;
    private Context mContext;
    private Updater mWork;
    String message;
    private Handler mhandler;
    public IntentFilter myIntentFilter;
    private String serialNo;

    /* renamed from: com.ifoer.util.MyDownLoadBinUpdate.1 */
    class C07531 extends Handler {
        C07531() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String softId = msg.obj;
            int fileSize = msg.arg2;
            int downloadSize = msg.arg1;
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    DownloadStatus status0 = new DownloadStatus();
                    status0.setStatus(MyDownLoadBinUpdate.this.mContext.getResources().getString(C0136R.string.download_fail));
                    status0.setFileSize((long) fileSize);
                    status0.setDownloadSize((long) downloadSize);
                    status0.setStatusRedownload(false);
                    MyDownLoadBinUpdate.this.downloads.put(softId, status0);
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    DownloadStatus status1 = new DownloadStatus();
                    status1.setStatus(MyDownLoadBinUpdate.this.mContext.getResources().getString(C0136R.string.download_now));
                    status1.setFileSize((long) fileSize);
                    status1.setDownloadSize((long) downloadSize);
                    status1.setStatusRedownload(false);
                    MyDownLoadBinUpdate.this.downloads.put(softId, status1);
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    DownloadStatus status2 = new DownloadStatus();
                    status2.setStatus(MyDownLoadBinUpdate.this.mContext.getResources().getString(C0136R.string.download_finish));
                    status2.setFileSize((long) fileSize);
                    status2.setDownloadSize((long) downloadSize);
                    status2.setStatusRedownload(false);
                    MyDownLoadBinUpdate.this.downloads.put(softId, status2);
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    DownloadStatus status3 = new DownloadStatus();
                    status3.setStatus(MyDownLoadBinUpdate.this.mContext.getResources().getString(C0136R.string.unziping));
                    status3.setFileSize((long) fileSize);
                    status3.setDownloadSize((long) fileSize);
                    status3.setStatusRedownload(false);
                    MyDownLoadBinUpdate.this.downloads.put(softId, status3);
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    DownloadStatus status4 = new DownloadStatus();
                    status4.setStatus(MyDownLoadBinUpdate.this.mContext.getResources().getString(C0136R.string.unzip_success));
                    status4.setFileSize((long) fileSize);
                    status4.setDownloadSize((long) fileSize);
                    status4.setStatusRedownload(false);
                    MyDownLoadBinUpdate.this.downloads.put(softId, status4);
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    DownloadStatus status5 = new DownloadStatus();
                    status5.setStatus(MyDownLoadBinUpdate.this.mContext.getResources().getString(C0136R.string.unzip_fail));
                    status5.setFileSize((long) fileSize);
                    status5.setDownloadSize((long) fileSize);
                    status5.setStatusRedownload(false);
                    MyDownLoadBinUpdate.this.downloads.put(softId, status5);
                default:
            }
        }
    }

    class Updater extends Thread {
        Updater() {
        }

        public void run() {
            checkUpdate();
        }

        void checkUpdate() {
            String cc = MySharedPreferences.getStringValue(MyDownLoadBinUpdate.this.mContext, MySharedPreferences.CCKey);
            String productSerialNo = MySharedPreferences.getStringValue(MyDownLoadBinUpdate.this.mContext, MySharedPreferences.serialNoKey);
            String version = DBDao.getInstance(MyDownLoadBinUpdate.this.mContext).getDownloadVersion(MainActivity.database);
            if (version.length() > 0) {
                MyDownLoadBinUpdate.DOWNLOAD_BIN_BASE_VERSION = version;
            }
            DownloadBinResult result = null;
            try {
                result = new UpdateSoftware().getBinFileMaxVersion(MyDownLoadBinUpdate.this.mContext, cc, productSerialNo, MyDownLoadBinUpdate.DOWNLOAD_BIN_BASE_VERSION, "CN");
            } catch (SocketTimeoutException e) {
            } catch (NullPointerException e2) {
            }
            if (result == null) {
                return;
            }
            if (result.getCode() == 0) {
                if (result.getVersioninfo() != null) {
                    SoftMaxVersion rs = result.getVersioninfo();
                    if (rs.getVersionNo() != null && !rs.getVersionNo().equalsIgnoreCase(MyDownLoadBinUpdate.DOWNLOAD_BIN_BASE_VERSION)) {
                        int i;
                        X431PadDtoSoft info = new X431PadDtoSoft();
                        info.setSoftId("001");
                        info.setSoftName("download");
                        info.setVersionDetailId(new StringBuilder(String.valueOf(rs.getVersionDetailId())).toString());
                        info.setVersionNo(rs.getVersionNo());
                        MyDownLoadBinUpdate.this.downloadList.add(info);
                        for (i = 0; i < MyDownLoadBinUpdate.this.downloadList.size(); i++) {
                            DownloadStatus status = new DownloadStatus();
                            status.setDownloadSize(0);
                            status.setFileSize(1);
                            MyDownLoadBinUpdate.this.downloads.put(((X431PadDtoSoft) MyDownLoadBinUpdate.this.downloadList.get(i)).getSoftId(), status);
                        }
                        if (Environment.getExternalStorageState().equals("mounted")) {
                            DownloadBinManager.getInstance();
                            new Thread(new DownloadBinManagerThread()).start();
                            for (i = 0; i < MyDownLoadBinUpdate.this.downloadList.size(); i++) {
                                X431PadDtoSoft dto = (X431PadDtoSoft) MyDownLoadBinUpdate.this.downloadList.get(i);
                                MyDownLoadBinUpdate.this.downloadTaskMananger = DownloadBinManager.getInstance();
                                MyDownLoadBinUpdate.this.downloadTaskMananger.addDownloadTask(new DownloadBinNewVersion(MyDownLoadBinUpdate.this.mContext, MyDownLoadBinUpdate.this.handler, dto, MyDownLoadBinUpdate.this.serialNo));
                            }
                            return;
                        }
                        MyDownLoadBinUpdate.this.handler.obtainMessage(6).sendToTarget();
                    } else if (rs.getVersionNo() == null || !rs.getVersionNo().equalsIgnoreCase(MyDownLoadBinUpdate.DOWNLOAD_BIN_BASE_VERSION)) {
                        MyDownLoadBinUpdate.this.handler.obtainMessage(8).sendToTarget();
                    } else {
                        MyDownLoadBinUpdate.this.handler.obtainMessage(7).sendToTarget();
                    }
                }
            } else if (result.getCode() == MyHttpException.ERROR_RESULT_NOT_EXIST) {
                MyDownLoadBinUpdate.this.handler.obtainMessage(9).sendToTarget();
            }
        }
    }

    static {
        DOWNLOAD_BIN_BASE_VERSION = "00.00";
        mydownLoadbinupdate = null;
    }

    public MyDownLoadBinUpdate(Context context) {
        this.downloadList = new ArrayList();
        this.message = XmlPullParser.NO_NAMESPACE;
        this.downloads = new HashMap();
        this.con = null;
        this.handler = new C07531();
        this.mContext = context;
    }

    public static synchronized MyDownLoadBinUpdate getMyDownLoadBinUpdate(Context context) {
        MyDownLoadBinUpdate myDownLoadBinUpdate;
        synchronized (MyDownLoadBinUpdate.class) {
            if (mydownLoadbinupdate == null) {
                myDownLoadBinUpdate = new MyDownLoadBinUpdate(context);
            } else {
                myDownLoadBinUpdate = mydownLoadbinupdate;
            }
        }
        return myDownLoadBinUpdate;
    }

    public void checkUpdateAsync() {
        if (this.mWork == null) {
            this.mWork = new Updater();
            this.mWork.start();
        }
    }
}

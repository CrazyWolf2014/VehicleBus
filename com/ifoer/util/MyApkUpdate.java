package com.ifoer.util;

import CRP.utils.CRPTools;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.entity.Constant;
import com.ifoer.entity.SoftMaxVersion;
import com.ifoer.expeditionphone.BaseActivity;
import com.ifoer.expeditionphone.KeyToUpgradeActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.expeditionphone.MoreActivity;
import com.ifoer.webservice.UpdateSoftware;
import com.launch.service.BundleBuilder;
import com.thoughtworks.xstream.XStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class MyApkUpdate {
    private static final int ISNEW = 100;
    private static final String LOCAL_APK_PATH;
    private static final int NOUPGRADE = 101;
    private static final String TAG = "ApkUpdate";
    private static final String apkUpdateUrl = "http://mycar.x431.com/mobile/softCenter/downloadPhoneSoftWs.action";
    private static MyApkUpdate myApkUpdate;
    static int number;
    private static SoftMaxVersion rs;
    private HttpURLConnection con;
    private int forceUpgrade;
    @SuppressLint({"HandlerLeak"})
    Handler handler;
    private InputStream is;
    private boolean isKeyUpgrade;
    private Context mContext;
    private Updater mWork;
    private Handler mhandler;
    public IntentFilter myIntentFilter;
    private OutputStream os;
    private ProgressDialog pdialog;
    public mBroadcastReceiver receiver;
    private UpdaterAPP updaterAPP;
    private String verL2;
    private String verLo2;

    /* renamed from: com.ifoer.util.MyApkUpdate.1 */
    class C07471 extends Handler {
        C07471() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    MyApkUpdate.this.pdialog.setProgress(0);
                    MyApkUpdate.this.pdialog.setMessage(msg.obj.toString());
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    MyApkUpdate.this.pdialog.setProgress(Integer.parseInt(msg.obj.toString()));
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    MyApkUpdate.this.pdialog.setProgress(Integer.parseInt(msg.obj.toString()));
                    MyApkUpdate.this.pdialog.dismiss();
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    if (MyApkUpdate.this.pdialog != null && MyApkUpdate.this.pdialog.isShowing()) {
                        MyApkUpdate.this.pdialog.dismiss();
                    }
                    Toast.makeText(MyApkUpdate.this.mContext, C0136R.string.sd_not_exist, 0).show();
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    Toast.makeText(MyApkUpdate.this.mContext, MyApkUpdate.this.mContext.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.util.MyApkUpdate.2 */
    class C07482 implements OnClickListener {
        C07482() {
        }

        public void onClick(DialogInterface dialog, int which) {
            MyApkUpdate.this.cancelUpdate();
        }
    }

    class Updater extends Thread {

        /* renamed from: com.ifoer.util.MyApkUpdate.Updater.1 */
        class C07521 implements Runnable {

            /* renamed from: com.ifoer.util.MyApkUpdate.Updater.1.1 */
            class C07491 implements OnClickListener {
                C07491() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    new UpdaterAPP().start();
                    MyApkUpdate.this.pdialog.show();
                    MySharedPreferences.getSharedPref(MyApkUpdate.this.mContext).edit().putInt(MySharedPreferences.BgIdKey, 0).commit();
                }
            }

            /* renamed from: com.ifoer.util.MyApkUpdate.Updater.1.2 */
            class C07502 implements OnClickListener {
                C07502() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    MyApkUpdate.this.updaterAPP = new UpdaterAPP();
                    MyApkUpdate.this.updaterAPP.start();
                    MyApkUpdate.this.pdialog.show();
                    MySharedPreferences.getSharedPref(MyApkUpdate.this.mContext).edit().putInt(MySharedPreferences.BgIdKey, 0).commit();
                }
            }

            /* renamed from: com.ifoer.util.MyApkUpdate.Updater.1.3 */
            class C07513 implements OnClickListener {
                C07513() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    MyApkUpdate.this.mhandler.sendEmptyMessage(MyApkUpdate.NOUPGRADE);
                }
            }

            C07521() {
            }

            public void run() {
                Builder uploadBuild = new Builder(MyApkUpdate.this.mContext);
                if (MyApkUpdate.this.forceUpgrade == 2) {
                    uploadBuild.setTitle(MyApkUpdate.this.mContext.getResources().getText(C0136R.string.order_notic)).setCancelable(false).setMessage(MyApkUpdate.this.mContext.getResources().getText(C0136R.string.updatesoft_right)).setPositiveButton(MyApkUpdate.this.mContext.getResources().getText(C0136R.string.Ensure), new C07491());
                } else {
                    uploadBuild.setTitle(MyApkUpdate.this.mContext.getResources().getText(C0136R.string.order_notic)).setCancelable(false).setMessage(MyApkUpdate.this.mContext.getResources().getText(C0136R.string.updatesoft_right)).setPositiveButton(MyApkUpdate.this.mContext.getResources().getText(C0136R.string.Ensure), new C07502()).setNegativeButton(MyApkUpdate.this.mContext.getResources().getText(C0136R.string.Cancel), new C07513());
                }
                uploadBuild.create().show();
            }
        }

        class UpdaterAPP extends Thread {
            public volatile boolean mCancel;

            UpdaterAPP() {
                this.mCancel = false;
            }

            public void run() {
                try {
                    if (Environment.getExternalStorageState().equals("mounted")) {
                        File f = new File(MyApkUpdate.LOCAL_APK_PATH);
                        if (!f.exists()) {
                            f.getParentFile().mkdirs();
                        }
                        MyApkUpdate.this.download(new StringBuilder(String.valueOf(MyApkUpdate.rs.getVersionDetailId())).toString(), MyApkUpdate.LOCAL_APK_PATH);
                        return;
                    }
                    MyApkUpdate.this.handler.obtainMessage(3).sendToTarget();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Updater() {
        }

        public void run() {
            checkUpdate();
        }

        void checkUpdate() {
            BufferedInputStream fin = null;
            String packageName = MyApkUpdate.this.mContext.getPackageName();
            try {
                String verLocal = MyApkUpdate.this.mContext.getPackageManager().getPackageInfo(packageName, 0).versionName;
                MyApkUpdate.rs = new UpdateSoftware().getMobileAppSoftMaxVersion(MyApkUpdate.this.mContext, verLocal, MySharedPreferences.getIntValue(BaseActivity.mContexts, "PDT_TYPE"), XStream.NO_REFERENCES);
                if (MyApkUpdate.rs != null) {
                    String verLocal1 = verLocal.replace(".", XmlPullParser.NO_NAMESPACE);
                    String verLocal2 = MyApkUpdate.rs.getVersionNo();
                    MySharedPreferences.setString(MyApkUpdate.this.mContext, MySharedPreferences.versionName, verLocal2);
                    String verL2;
                    if (MyApkUpdate.this.isKeyUpgrade) {
                        if (verLocal2 != null) {
                            verL2 = verLocal2.toUpperCase().replace("V", XmlPullParser.NO_NAMESPACE);
                            MyApkUpdate.this.verLo2 = verL2.replace(".", XmlPullParser.NO_NAMESPACE);
                            Constant.apkVersionDetailId = new StringBuilder(String.valueOf(MyApkUpdate.rs.getVersionDetailId())).toString();
                            Constant.apkLocalVersion = "V" + verLocal;
                            Constant.apkNetWorkVersion = verLocal2.toUpperCase();
                            X431PadDtoSoft apkinfo = new X431PadDtoSoft();
                            apkinfo.setSoftId("001");
                            apkinfo.setMaxOldVersion(Constant.apkLocalVersion);
                            apkinfo.setSoftName(MyApkUpdate.this.mContext.getResources().getString(C0136R.string.APK));
                            apkinfo.setType(1);
                            apkinfo.setVersionDetailId(new StringBuilder(String.valueOf(MyApkUpdate.rs.getVersionDetailId())).toString());
                            apkinfo.setVersionNo(MyApkUpdate.rs.getVersionNo());
                            ((KeyToUpgradeActivity) MyApkUpdate.this.mContext).setApkDto(apkinfo);
                            MyApkUpdate.this.mhandler.sendEmptyMessage(Constant.GET_APK_VERSION);
                        } else {
                            Constant.apkVersionDetailId = new StringBuilder(String.valueOf(MyApkUpdate.rs.getVersionDetailId())).toString();
                            Constant.apkLocalVersion = "V" + verLocal;
                            MyApkUpdate.this.mhandler.sendEmptyMessage(Constant.ERROR_GET_DATA_FAILED);
                        }
                    } else if (verLocal2 != null) {
                        verL2 = verLocal2.toUpperCase().replace("V", XmlPullParser.NO_NAMESPACE);
                        MyApkUpdate.this.verLo2 = verL2.replace(".", XmlPullParser.NO_NAMESPACE);
                        MyApkUpdate.this.forceUpgrade = 0;
                        if (verLocal1.length() != MyApkUpdate.this.verLo2.length()) {
                            MyApkUpdate myApkUpdate = MyApkUpdate.this;
                            myApkUpdate.verLo2 = myApkUpdate.verLo2 + "000";
                        }
                        if (Integer.parseInt(MyApkUpdate.this.verLo2) > Integer.parseInt(verLocal1)) {
                            if (MoreActivity.mProgressDialog != null && MoreActivity.mProgressDialog.isShowing()) {
                                MoreActivity.mProgressDialog.dismiss();
                            }
                            checkInstallDialog(MyApkUpdate.LOCAL_APK_PATH);
                        } else {
                            msg = new Message();
                            msg.what = MyApkUpdate.ISNEW;
                            MyApkUpdate.this.mhandler.sendMessage(msg);
                        }
                    } else if (MyApkUpdate.rs.getMessage() == null || MyApkUpdate.rs.getMessage().equals(XmlPullParser.NO_NAMESPACE)) {
                        msg = new Message();
                        msg.what = MyApkUpdate.ISNEW;
                        MyApkUpdate.this.mhandler.sendMessage(msg);
                    } else {
                        msg = new Message();
                        msg.what = Constant.ERROR_SERVER;
                        Bundle data = new Bundle();
                        data.putString(BundleBuilder.AskFromMessage, MyApkUpdate.rs.getMessage());
                        msg.setData(data);
                        MyApkUpdate.this.mhandler.sendMessage(msg);
                    }
                    if (fin != null) {
                        try {
                            fin.close();
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    return;
                } else if (MyApkUpdate.rs == null) {
                    MyApkUpdate.this.mhandler.sendEmptyMessage(Constant.ERROR_SERVER);
                    if (fin != null) {
                        try {
                            fin.close();
                            return;
                        } catch (IOException e2) {
                            e2.printStackTrace();
                            return;
                        }
                    }
                    return;
                }
                if (fin != null) {
                    try {
                        fin.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                }
            } catch (Exception e3) {
                e3.printStackTrace();
                if (fin != null) {
                    try {
                        fin.close();
                    } catch (IOException e222) {
                        e222.printStackTrace();
                    }
                }
            } catch (Throwable th) {
                if (fin != null) {
                    try {
                        fin.close();
                    } catch (IOException e2222) {
                        e2222.printStackTrace();
                    }
                }
            }
        }

        public void downloadAndInstall(String filename) {
            try {
                if (Environment.getExternalStorageState().equals("mounted")) {
                    File f = new File(filename);
                    if (!f.exists()) {
                        f.getParentFile().mkdirs();
                    }
                    MyApkUpdate.this.download(new StringBuilder(String.valueOf(MyApkUpdate.rs.getVersionDetailId())).toString(), filename);
                    return;
                }
                MyApkUpdate.this.handler.obtainMessage(3).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void checkInstallDialog(String filename) {
            ((Activity) MyApkUpdate.this.mContext).runOnUiThread(new C07521());
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            boolean success = false;
            if (State.CONNECTED == ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(1).getState()) {
                success = true;
            }
            if (MyApkUpdate.this.pdialog != null && !success) {
                Toast.makeText(context, context.getString(C0136R.string.no_network_tips), 1).show();
                MyApkUpdate.this.pdialog.setCancelable(true);
                MyApkUpdate.this.pdialog.dismiss();
            }
        }
    }

    static {
        LOCAL_APK_PATH = Environment.getExternalStorageDirectory() + MySharedPreferences.getStringValue(MainActivity.contexts, "LOCAL_UPGRADE_APK");
        number = 0;
        myApkUpdate = null;
    }

    public MyApkUpdate(Context context, Handler handler, boolean isKeyUpgrade) {
        this.pdialog = null;
        this.con = null;
        this.is = null;
        this.os = null;
        this.isKeyUpgrade = false;
        this.handler = new C07471();
        this.mContext = context;
        this.mhandler = handler;
        this.isKeyUpgrade = isKeyUpgrade;
        this.pdialog = new ProgressDialog(context);
        this.pdialog.setProgressStyle(1);
        this.pdialog.setMax(ISNEW);
        this.pdialog.setProgress(0);
        this.pdialog.setIndeterminate(false);
        this.pdialog.setTitle(context.getResources().getString(C0136R.string.updateApk_now));
        this.pdialog.setButton(context.getResources().getString(C0136R.string.cancel), new C07482());
        this.pdialog.setCancelable(false);
        registerBoradcastReceiver();
    }

    public void registerBoradcastReceiver() {
        this.receiver = new mBroadcastReceiver();
        this.myIntentFilter = new IntentFilter();
        this.myIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
    }

    public static synchronized MyApkUpdate getMyApkUpdate(Context context, Handler handler, boolean isKeyUpgrade) {
        MyApkUpdate myApkUpdate;
        synchronized (MyApkUpdate.class) {
            if (myApkUpdate == null) {
                myApkUpdate = new MyApkUpdate(context, handler, isKeyUpgrade);
            } else {
                myApkUpdate = myApkUpdate;
            }
        }
        return myApkUpdate;
    }

    public void checkUpdateAsync() {
        if (this.mWork == null) {
            this.mWork = new Updater();
            this.mWork.start();
        }
    }

    void cancelUpdate() {
        this.updaterAPP.mCancel = true;
    }

    public void download(String id, String filename) throws Exception {
        this.con = (HttpURLConnection) new URL(apkUpdateUrl).openConnection();
        this.con.setRequestMethod("POST");
        this.con.setRequestProperty("Connection", "Keep-Alive");
        this.con.setRequestProperty("Charset", AsyncHttpResponseHandler.DEFAULT_CHARSET);
        this.con.setRequestProperty("content-type", "application/x-www-form-urlencoded");
        this.con.setDoInput(true);
        this.con.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(this.con.getOutputStream());
        out.write("versionDetailId=" + id);
        out.flush();
        out.close();
        int code = this.con.getResponseCode();
        long sdcardUsableSize = CRPTools.getUsableSDCardSize();
        long downloadSize = 0;
        Message msg;
        if (code == AsyncTaskManager.REQUEST_SUCCESS_CODE) {
            this.is = this.con.getInputStream();
            int filesize = this.con.getContentLength();
            if (((long) filesize) <= sdcardUsableSize) {
                byte[] bs = new byte[Flags.FLAG5];
                this.os = new FileOutputStream(filename);
                while (!this.updaterAPP.mCancel) {
                    int len = this.is.read(bs);
                    if (len == -1) {
                        break;
                    }
                    this.os.write(bs, 0, len);
                    downloadSize += (long) len;
                    msg = new Message();
                    msg.what = 1;
                    msg.obj = Long.valueOf((100 * downloadSize) / ((long) filesize));
                    this.handler.sendMessage(msg);
                    if (downloadSize == ((long) filesize)) {
                        Message msgs = new Message();
                        msgs.what = 2;
                        msgs.obj = Integer.valueOf(ISNEW);
                        this.handler.sendMessage(msgs);
                        Intent i = new Intent("android.intent.action.VIEW");
                        i.setDataAndType(Uri.parse("file://" + filename), "application/vnd.android.package-archive");
                        this.mContext.startActivity(i);
                    }
                }
            } else {
                msg = new Message();
                msg.what = 4;
                this.handler.sendMessage(msg);
            }
        } else {
            msg = this.handler.obtainMessage();
            msg.what = 0;
            msg.obj = this.mContext.getResources().getText(C0136R.string.Connect_to_server_fail);
            this.handler.sendMessage(msg);
        }
        this.os.close();
        this.is.close();
    }
}

package com.ifoer.util;

import CRP.utils.CRPTools;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.expeditionphone.MainActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.xbill.DNS.KEYRecord.Flags;

public class ApkDownLoad extends Thread {
    public static final int DOWNLOADING_APK = 914;
    public static final int DOWNLOAD_FINISHED = 917;
    public static final int DOWNlOAD_APK_BEGIN = 913;
    public static final int DOWNlOAD_NO_SD = 916;
    public static final int DOWNlOAD_SD_LOW = 915;
    private static final String LOCAL_APK_PATH;
    private static final String apkUpdateUrl = "http://mycar.x431.com/mobile/softCenter/downloadPhoneSoftWs.action";
    private HttpURLConnection con;
    private Context context;
    @SuppressLint({"HandlerLeak"})
    Handler handler;
    private int id;
    private InputStream is;
    private boolean isCancel;
    private OutputStream os;
    private ProgressDialog pdialog;

    /* renamed from: com.ifoer.util.ApkDownLoad.1 */
    class C07411 extends Handler {
        C07411() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ApkDownLoad.DOWNlOAD_APK_BEGIN /*913*/:
                    ApkDownLoad.this.pdialog.show();
                    ApkDownLoad.this.pdialog.setProgress(0);
                    ApkDownLoad.this.pdialog.setMessage(msg.obj.toString());
                case ApkDownLoad.DOWNLOADING_APK /*914*/:
                    ApkDownLoad.this.pdialog.setProgress(Integer.parseInt(msg.obj.toString()));
                case ApkDownLoad.DOWNlOAD_SD_LOW /*915*/:
                    Toast.makeText(ApkDownLoad.this.context, ApkDownLoad.this.context.getString(C0136R.string.sdcard_storage_insufficient), 0).show();
                case ApkDownLoad.DOWNlOAD_NO_SD /*916*/:
                    if (ApkDownLoad.this.pdialog != null && ApkDownLoad.this.pdialog.isShowing()) {
                        ApkDownLoad.this.pdialog.dismiss();
                    }
                    Toast.makeText(ApkDownLoad.this.context, C0136R.string.sd_not_exist, 0).show();
                case ApkDownLoad.DOWNLOAD_FINISHED /*917*/:
                    ApkDownLoad.this.pdialog.setProgress(Integer.parseInt(msg.obj.toString()));
                    ApkDownLoad.this.pdialog.dismiss();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.util.ApkDownLoad.2 */
    class C07422 implements OnClickListener {
        C07422() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ApkDownLoad.this.isCancel = true;
        }
    }

    static {
        LOCAL_APK_PATH = Environment.getExternalStorageDirectory() + MySharedPreferences.getStringValue(MainActivity.contexts, "LOCAL_UPGRADE_APK");
    }

    public ApkDownLoad(Context context, int id) {
        this.con = null;
        this.is = null;
        this.os = null;
        this.pdialog = null;
        this.isCancel = false;
        this.handler = new C07411();
        this.context = context;
        this.id = id;
        this.isCancel = false;
        this.pdialog = new ProgressDialog(context);
        this.pdialog.setProgressStyle(1);
        this.pdialog.setMax(100);
        this.pdialog.setProgress(0);
        this.pdialog.setIndeterminate(false);
        this.pdialog.setTitle(context.getResources().getString(C0136R.string.updateApk_now));
        this.pdialog.setButton(context.getResources().getString(C0136R.string.cancel), new C07422());
        this.pdialog.setCancelable(false);
        this.pdialog.show();
    }

    public void run() {
        super.run();
        Log.i("ApkDownLoad", "\u6267\u884c\u4e0b\u8f7d\u7ebf\u7a0b");
        try {
            if (Environment.getExternalStorageState().equals("mounted")) {
                File f = new File(LOCAL_APK_PATH);
                if (!f.exists()) {
                    f.getParentFile().mkdirs();
                }
                download(new StringBuilder(String.valueOf(this.id)).toString(), LOCAL_APK_PATH);
                return;
            }
            this.handler.obtainMessage(DOWNlOAD_NO_SD).sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void download(String id, String filename) throws Exception {
        URL url = new URL(apkUpdateUrl);
        Log.i("ApkDownLoad", "download \u65b9\u6cd5" + filename);
        this.con = (HttpURLConnection) url.openConnection();
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
                while (!this.isCancel) {
                    int len = this.is.read(bs);
                    if (len == -1) {
                        break;
                    }
                    this.os.write(bs, 0, len);
                    downloadSize += (long) len;
                    msg = new Message();
                    msg.what = DOWNLOADING_APK;
                    msg.obj = Long.valueOf((100 * downloadSize) / ((long) filesize));
                    this.handler.sendMessage(msg);
                    if (downloadSize == ((long) filesize)) {
                        Message msgs = new Message();
                        msgs.what = DOWNLOAD_FINISHED;
                        msgs.obj = Integer.valueOf(100);
                        this.handler.sendMessage(msgs);
                        Log.i("ApkDownLoad", "DOWNLOAD_FINISHED");
                        Intent i = new Intent("android.intent.action.VIEW");
                        i.setDataAndType(Uri.parse("file://" + filename), "application/vnd.android.package-archive");
                        this.context.startActivity(i);
                    }
                }
            } else {
                msg = new Message();
                msg.what = DOWNlOAD_SD_LOW;
                this.handler.sendMessage(msg);
                Log.i("ApkDownLoad", "DOWNlOAD_SD_LOW");
            }
        } else {
            msg = this.handler.obtainMessage();
            msg.what = DOWNlOAD_APK_BEGIN;
            msg.obj = this.context.getResources().getText(C0136R.string.Connect_to_server_fail);
            Log.i("ApkDownLoad", "DOWNlOAD_APK_BEGIN");
            this.handler.sendMessage(msg);
        }
        this.os.close();
        this.is.close();
    }
}

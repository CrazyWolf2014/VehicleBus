package com.alipay.android.appDemo4;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.entity.Constant;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.jivesoftware.smackx.bytestreams.ibb.packet.DataPacketExtension;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;
import org.json.JSONException;
import org.json.JSONObject;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class MobileSecurePayHelper {
    static final String TAG = "MobileSecurePayHelper";
    Context mContext;
    private Handler mHandler;
    private ProgressDialog mProgress;

    /* renamed from: com.alipay.android.appDemo4.MobileSecurePayHelper.1 */
    class C00781 extends Handler {
        C00781() {
        }

        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                        MobileSecurePayHelper.this.closeProgress();
                        MobileSecurePayHelper.this.showInstallConfirmDialog(MobileSecurePayHelper.this.mContext, msg.obj);
                        break;
                }
                super.handleMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.alipay.android.appDemo4.MobileSecurePayHelper.2 */
    class C00792 implements Runnable {
        private final /* synthetic */ String val$cachePath;

        C00792(String str) {
            this.val$cachePath = str;
        }

        public void run() {
            String newApkdlUrl = MobileSecurePayHelper.this.checkNewUpdate(MobileSecurePayHelper.getApkInfo(MobileSecurePayHelper.this.mContext, this.val$cachePath));
            if (newApkdlUrl != null) {
                MobileSecurePayHelper.this.retrieveApkFromNet(MobileSecurePayHelper.this.mContext, newApkdlUrl, this.val$cachePath);
            }
            Message msg = new Message();
            msg.what = 2;
            msg.obj = this.val$cachePath;
            MobileSecurePayHelper.this.mHandler.sendMessage(msg);
        }
    }

    /* renamed from: com.alipay.android.appDemo4.MobileSecurePayHelper.3 */
    class C00803 implements OnClickListener {
        private final /* synthetic */ String val$cachePath;
        private final /* synthetic */ Context val$context;

        C00803(String str, Context context) {
            this.val$cachePath = str;
            this.val$context = context;
        }

        public void onClick(DialogInterface dialog, int which) {
            BaseHelper.chmod("777", this.val$cachePath);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(268435456);
            intent.setDataAndType(Uri.parse("file://" + this.val$cachePath), "application/vnd.android.package-archive");
            this.val$context.startActivity(intent);
        }
    }

    /* renamed from: com.alipay.android.appDemo4.MobileSecurePayHelper.4 */
    class C00814 implements OnClickListener {
        C00814() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    public MobileSecurePayHelper(Context context) {
        this.mProgress = null;
        this.mContext = null;
        this.mHandler = new C00781();
        this.mContext = context;
    }

    public boolean detectMobile_sp() {
        boolean isMobile_spExist = isMobile_spExist();
        if (!isMobile_spExist) {
            String cachePath = new StringBuilder(String.valueOf(this.mContext.getCacheDir().getAbsolutePath())).append("/temp.apk").toString();
            retrieveApkFromAssets(this.mContext, PartnerConfig.ALIPAY_PLUGIN_NAME, cachePath);
            this.mProgress = BaseHelper.showProgress(this.mContext, null, "\u6b63\u5728\u68c0\u6d4b\u5b89\u5168\u652f\u4ed8\u670d\u52a1\u7248\u672c", false, true);
            new Thread(new C00792(cachePath)).start();
        }
        return isMobile_spExist;
    }

    public void showInstallConfirmDialog(Context context, String cachePath) {
        Builder tDialog = new Builder(context);
        tDialog.setIcon(C0136R.drawable.info);
        tDialog.setTitle(context.getResources().getString(C0136R.string.confirm_install_hint));
        tDialog.setMessage(context.getResources().getString(C0136R.string.confirm_install));
        tDialog.setPositiveButton(C0136R.string.Ensure, new C00803(cachePath, context));
        tDialog.setNegativeButton(context.getResources().getString(C0136R.string.Cancel), new C00814());
        tDialog.show();
    }

    public boolean isMobile_spExist() {
        List<PackageInfo> pkgList = this.mContext.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < pkgList.size(); i++) {
            if (((PackageInfo) pkgList.get(i)).packageName.equalsIgnoreCase("com.alipay.android.app")) {
                return true;
            }
        }
        return false;
    }

    public boolean retrieveApkFromAssets(Context context, String fileName, String path) {
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[Flags.FLAG5];
            while (true) {
                int i = is.read(temp);
                if (i <= 0) {
                    fos.close();
                    is.close();
                    return true;
                }
                fos.write(temp, 0, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static PackageInfo getApkInfo(Context context, String archiveFilePath) {
        return context.getPackageManager().getPackageArchiveInfo(archiveFilePath, Flags.FLAG8);
    }

    public String checkNewUpdate(PackageInfo packageInfo) {
        String url = null;
        try {
            JSONObject resp = sendCheckNewUpdate(packageInfo.versionName);
            if (resp.getString("needUpdate").equalsIgnoreCase("true")) {
                url = resp.getString("updateUrl");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public JSONObject sendCheckNewUpdate(String versionName) {
        JSONObject objResp = null;
        try {
            JSONObject req = new JSONObject();
            req.put(Constant.ACTION, Item.UPDATE_ACTION);
            Object data = new JSONObject();
            data.put(AlixDefine.platform, (Object) "android");
            data.put(AlixDefine.VERSION, (Object) versionName);
            data.put(AlixDefine.partner, XmlPullParser.NO_NAMESPACE);
            req.put(DataPacketExtension.ELEMENT_NAME, data);
            objResp = sendRequest(req.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objResp;
    }

    public JSONObject sendRequest(String content) {
        NetworkManager nM = new NetworkManager(this.mContext);
        JSONObject jsonResponse = null;
        try {
            String response;
            synchronized (nM) {
                response = nM.SendAndWaitResponse(content, Constant.server_url);
            }
            jsonResponse = new JSONObject(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jsonResponse != null) {
            BaseHelper.log(TAG, jsonResponse.toString());
        }
        return jsonResponse;
    }

    public boolean retrieveApkFromNet(Context context, String strurl, String filename) {
        boolean bRet = false;
        try {
            bRet = new NetworkManager(this.mContext).urlDownloadToFile(context, strurl, filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bRet;
    }

    void closeProgress() {
        try {
            if (this.mProgress != null) {
                this.mProgress.dismiss();
                this.mProgress = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

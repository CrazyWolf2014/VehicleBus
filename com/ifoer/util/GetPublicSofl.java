package com.ifoer.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.cnlaunch.framework.common.KeyConstant;
import com.cnlaunch.framework.network.http.HttpException;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.cnlaunch.x431pro.module.config.ConfigDBManager;
import com.cnlaunch.x431pro.module.upgrade.action.UpgradeAction;
import com.cnlaunch.x431pro.module.upgrade.model.LatestPublicSoftsResponse;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.ifoer.entity.Constant;
import com.ifoer.expeditionphone.KeyToUpgradeActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.launch.service.BundleBuilder;
import com.thoughtworks.xstream.XStream;
import java.util.List;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParser;

public class GetPublicSofl extends AsyncTask<String, String, LatestPublicSoftsResponse> {
    private static String DOWNLOAD_BIN_BASE_VERSION;
    KeyToUpgradeActivity activity;
    private Context context;
    private Handler mHandler;
    private ProgressDialog progress;
    String publicsoftUrl;

    static {
        DOWNLOAD_BIN_BASE_VERSION = "00.00";
    }

    public GetPublicSofl(Context context, ProgressDialog progressDiag, Handler handler) {
        this.publicsoftUrl = XmlPullParser.NO_NAMESPACE;
        this.context = context;
        if (progressDiag != null) {
            this.progress = progressDiag;
        }
        this.mHandler = handler;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.progress = new ProgressDialog(this.context);
        this.progress.setMessage(this.context.getResources().getString(C0136R.string.shopping_wait));
        this.progress.setCancelable(false);
        if (this.progress != null && !this.progress.isShowing()) {
            this.progress.show();
        }
    }

    protected LatestPublicSoftsResponse doInBackground(String... params) {
        String cc = MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey);
        String productSerialNo = MySharedPreferences.getStringValue(this.context, MySharedPreferences.serialNoKey);
        LatestPublicSoftsResponse res = null;
        String version = MySharedPreferences.getStringValue(this.context, Constants.DOWNLOADBIN);
        if (version.length() > 0) {
            DOWNLOAD_BIN_BASE_VERSION = version;
            Log.e("nxy", "DOWNLOAD_BIN_BASE_VERSION  " + DOWNLOAD_BIN_BASE_VERSION);
        }
        try {
            this.publicsoftUrl = ConfigDBManager.getInstance(this.context).getUrlByKey(KeyConstant.publicsoft_download);
        } catch (HttpException e) {
            e.printStackTrace();
        }
        if (MainActivity.country == null || MainActivity.country.length() <= 0) {
            MainActivity.country = Locale.getDefault().getCountry();
        }
        this.activity = (KeyToUpgradeActivity) this.context;
        int lanId = AndroidToLan.getLanId(MainActivity.country);
        try {
            res = new UpgradeAction(this.context).queryLatestPublicSofts(productSerialNo, new StringBuilder(String.valueOf(lanId)).toString(), new StringBuilder(String.valueOf(XStream.NO_REFERENCES)).toString());
        } catch (HttpException e1) {
            this.mHandler.obtainMessage(0).sendToTarget();
            e1.printStackTrace();
        } catch (NullPointerException e2) {
            this.mHandler.obtainMessage(Constant.ERROR_GET_DATA_FAILED).sendToTarget();
            e2.printStackTrace();
        } catch (Exception e3) {
            this.mHandler.obtainMessage(Constant.ERROR_NETWORK).sendToTarget();
            e3.printStackTrace();
        }
        return res;
    }

    protected void onPostExecute(LatestPublicSoftsResponse res) {
        super.onPostExecute(res);
        if (this.progress != null) {
            if (this.progress.isShowing()) {
                this.progress.dismiss();
            }
        }
        if (res != null) {
            Log.i("getPublicSoft", "\u83b7\u53d6\u516c\u5171\u8f6f\u4ef6\u4e0b\u8f7d\u4fe1\u606f" + res.toString());
        }
        if (res != null && res.getCode() == 0) {
            String verLocal = XmlPullParser.NO_NAMESPACE;
            List<X431PadDtoSoft> list = res.getX431PadSoftList();
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    X431PadDtoSoft bean = (X431PadDtoSoft) list.get(i);
                    if (bean != null) {
                        X431PadDtoSoft info = new X431PadDtoSoft();
                        bean.setChecked(true);
                        if (bean.getSoftPackageID().equalsIgnoreCase(Constants.PACKAGE_ID_FIRMWARE_PADII) || bean.getSoftPackageID().equalsIgnoreCase(Constants.PACKAGE_ID_FIRMWARE_MAXGO) || bean.getSoftPackageID().equalsIgnoreCase(Constants.PACKAGE_ID_FIRMWARE_MAXIMUS) || bean.getSoftPackageID().equalsIgnoreCase(Constants.PACKAGE_ID_FIRMWARE_SCANPAD071) || bean.getSoftPackageID().equalsIgnoreCase(Constants.PACKAGE_ID_FIRMWARE_SCANPAD101) || bean.getSoftPackageID().equalsIgnoreCase(Constants.PACKAGE_ID_FIRMWARE_PRO) || bean.getSoftPackageID().equalsIgnoreCase(Constants.PACKAGE_ID_FIRMWARE_PRO3)) {
                            bean.setMust(true);
                        }
                        bean.setUrl(this.publicsoftUrl);
                        double readVersion;
                        double netVersion;
                        if (bean.getSoftPackageID().startsWith("DOWNLOAD") || bean.getSoftPackageID().contains("firmware")) {
                            readVersion = Double.valueOf(DOWNLOAD_BIN_BASE_VERSION).doubleValue();
                            netVersion = Double.valueOf(bean.getVersionNo().substring(1)).doubleValue();
                            Constant.firmDetailId = bean.getVersionDetailId();
                            Constant.firmLocalVer = "V" + readVersion;
                            Constant.firmNetVer = "V" + netVersion;
                            info.setSoftId("002");
                            Log.e("nxy", "download bin" + readVersion);
                            info.setMaxOldVersion("V" + readVersion);
                            info.setSoftName(this.context.getResources().getString(C0136R.string.downloadbin_upgrade));
                            info.setType(2);
                            info.setUrl(this.publicsoftUrl);
                            info.setVersionDetailId(new StringBuilder(String.valueOf(bean.getVersionDetailId())).toString());
                            info.setVersionNo(bean.getVersionNo());
                            bean.setType(2);
                            this.activity.setFirmDto(info);
                        } else {
                            String packageName = this.context.getPackageName();
                            try {
                                verLocal = this.context.getPackageManager().getPackageInfo(packageName, 0).versionName;
                                readVersion = Double.valueOf(verLocal.replace(".", XmlPullParser.NO_NAMESPACE)).doubleValue();
                                netVersion = Double.valueOf(bean.getVersionNo().substring(1).replace(".", XmlPullParser.NO_NAMESPACE)).doubleValue();
                                if (verLocal.length() != bean.getVersionNo().substring(1).length()) {
                                    Constant.apkNetWorkVersion = "V" + netVersion + "000";
                                } else {
                                    Constant.apkNetWorkVersion = "V" + netVersion;
                                }
                                bean.setType(1);
                                Constant.apkVersionDetailId = bean.getVersionDetailId();
                                Constant.apkLocalVersion = "V" + verLocal;
                                Constant.apkNetWorkVersion = "V" + netVersion;
                                X431PadDtoSoft apkinfo = new X431PadDtoSoft();
                                apkinfo.setSoftId("001");
                                apkinfo.setMaxOldVersion("V" + verLocal);
                                apkinfo.setSoftName(this.context.getResources().getString(C0136R.string.APK));
                                apkinfo.setType(1);
                                apkinfo.setUrl(this.publicsoftUrl);
                                apkinfo.setVersionDetailId(new StringBuilder(String.valueOf(bean.getVersionDetailId())).toString());
                                apkinfo.setVersionNo(bean.getVersionNo());
                                this.activity.setApkDto(apkinfo);
                            } catch (NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                this.activity.setX431PublicSoftList(list);
                this.mHandler.sendEmptyMessage(ApkDownLoad.DOWNlOAD_APK_BEGIN);
            } else if (res.getMsg() == null || res.getMsg().equals(XmlPullParser.NO_NAMESPACE)) {
                this.mHandler.sendEmptyMessage(Constant.ERROR_GET_DATA_FAILED);
            } else {
                Bundle data = new Bundle();
                data.putString(BundleBuilder.AskFromMessage, res.getMsg());
                Message msg = new Message();
                msg.what = Constant.ERROR_GET_DATA_FAILED;
                msg.setData(data);
                this.mHandler.sendMessage(msg);
            }
        } else if (res != null) {
            if (this.progress != null) {
                if (this.progress.isShowing()) {
                    this.progress.dismiss();
                }
            }
            if (res.getCode() == -1) {
                this.mHandler.obtainMessage(2).sendToTarget();
            }
            if (res.getCode() == 500) {
                this.mHandler.sendEmptyMessage(Constant.ERROR_SERVER);
            }
        } else if (this.progress != null) {
            if (this.progress.isShowing()) {
                this.progress.dismiss();
                this.mHandler.sendEmptyMessage(Constant.ERROR_SERVER);
            }
        }
    }
}

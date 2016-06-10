package com.ifoer.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.car.result.DownloadBinResult;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.ifoer.entity.Constant;
import com.ifoer.entity.SoftMaxVersion;
import com.ifoer.expeditionphone.ComRegActivity;
import com.ifoer.webservice.UpdateSoftware;
import com.launch.service.BundleBuilder;
import java.net.SocketTimeoutException;
import org.xmlpull.v1.XmlPullParser;

public class GetDownLoadVersion extends AsyncTask<String, String, DownloadBinResult> {
    private static String DOWNLOAD_BIN_BASE_VERSION;
    private Context context;
    private Handler mHandler;
    private ProgressDialog progress;

    static {
        DOWNLOAD_BIN_BASE_VERSION = "00.00";
    }

    public GetDownLoadVersion(Context context, ProgressDialog progressDiag, Handler handler) {
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

    protected DownloadBinResult doInBackground(String... params) {
        String cc = MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey);
        String productSerialNo = MySharedPreferences.getStringValue(this.context, MySharedPreferences.serialNoKey);
        UpdateSoftware updateSoftware = new UpdateSoftware();
        DownloadBinResult result = null;
        String version = MySharedPreferences.getStringValue(this.context, Constants.DOWNLOADBIN);
        if (version.length() > 0) {
            DOWNLOAD_BIN_BASE_VERSION = version;
        }
        try {
            result = updateSoftware.getBinFileMaxVersion(this.context, cc, productSerialNo, DOWNLOAD_BIN_BASE_VERSION, Constants.DEFAULT_LANGUAGE);
        } catch (SocketTimeoutException e) {
            this.mHandler.obtainMessage(0).sendToTarget();
        } catch (NullPointerException e2) {
            this.mHandler.obtainMessage(Constant.ERROR_GET_DATA_FAILED).sendToTarget();
            e2.printStackTrace();
        } catch (Exception e3) {
            this.mHandler.obtainMessage(Constant.ERROR_NETWORK).sendToTarget();
            e3.printStackTrace();
        }
        return result;
    }

    protected void onPostExecute(DownloadBinResult result) {
        super.onPostExecute(result);
        if (result != null) {
            if (this.progress != null && this.progress.isShowing()) {
                this.progress.dismiss();
            }
            Bundle data;
            Message msg;
            if (result.getCode() == 0) {
                if (result.getVersioninfo() != null) {
                    SoftMaxVersion rs = result.getVersioninfo();
                    if (rs.getVersionNo() != null) {
                        double readVersion = Double.valueOf(DOWNLOAD_BIN_BASE_VERSION).doubleValue();
                        double netVersion = Double.valueOf(rs.getVersionNo()).doubleValue();
                        X431PadDtoSoft info = new X431PadDtoSoft();
                        info.setSoftId("002");
                        info.setMaxOldVersion("V" + readVersion);
                        info.setSoftName(this.context.getResources().getString(C0136R.string.downloadbin_upgrade));
                        info.setType(2);
                        info.setVersionDetailId(new StringBuilder(String.valueOf(rs.getVersionDetailId())).toString());
                        info.setVersionNo("V" + rs.getVersionNo());
                        this.context.setFirmDto(info);
                        Constant.firmDetailId = new StringBuilder(String.valueOf(rs.getVersionDetailId())).toString();
                        Constant.firmLocalVer = "V" + readVersion;
                        Constant.firmNetVer = "V" + netVersion;
                        this.mHandler.sendEmptyMessage(ApkDownLoad.DOWNlOAD_APK_BEGIN);
                    } else if (rs.getMessage() == null || rs.getMessage().equals(XmlPullParser.NO_NAMESPACE)) {
                        this.mHandler.sendEmptyMessage(Constant.ERROR_GET_DATA_FAILED);
                    } else {
                        data = new Bundle();
                        data.putString(BundleBuilder.AskFromMessage, rs.getMessage());
                        msg = new Message();
                        msg.what = Constant.ERROR_GET_DATA_FAILED;
                        msg.setData(data);
                        this.mHandler.sendMessage(msg);
                    }
                }
            } else if (result.getCode() == MyHttpException.ERROR_NOT_REG_THIS) {
                ((Activity) this.context).startActivityForResult(new Intent(this.context, ComRegActivity.class), 11);
            } else if (TextUtils.isEmpty(result.getMessage())) {
                Log.i("\u5f02\u5e38", "\u5176\u4ed6null");
                this.mHandler.sendEmptyMessage(Constant.ERROR_GET_DATA_FAILED);
            } else {
                Log.i("\u5f02\u5e38", "\u5176\u4ed6" + result.getCode());
                data = new Bundle();
                data.putString(BundleBuilder.AskFromMessage, result.getMessage());
                msg = new Message();
                msg.what = Constant.ERROR_GET_DATA_FAILED;
                msg.setData(data);
                this.mHandler.sendMessage(msg);
            }
        } else if (this.progress != null && this.progress.isShowing()) {
            this.progress.dismiss();
            this.mHandler.sendEmptyMessage(Constant.ERROR_SERVER);
        }
    }
}

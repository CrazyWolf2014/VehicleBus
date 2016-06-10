package com.ifoer.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.cnlaunch.framework.common.KeyConstant;
import com.cnlaunch.framework.network.http.HttpException;
import com.cnlaunch.framework.utils.NLog;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.cnlaunch.x431pro.module.config.ConfigDBManager;
import com.cnlaunch.x431pro.module.upgrade.action.UpgradeAction;
import com.cnlaunch.x431pro.module.upgrade.model.LatestDiagSoftsResponse;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.expeditionphone.KeyToUpgradeActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.ui.MainMenuActivity;
import com.launch.service.BundleBuilder;
import com.thoughtworks.xstream.XStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

public class GetDiagSoft extends AsyncTask<String, String, LatestDiagSoftsResponse> {
    private static String DOWNLOAD_BIN_BASE_VERSION;
    KeyToUpgradeActivity activity;
    private Context context;
    private String diagsoftUrl;
    private Handler mHandler;
    private ProgressDialog progress;

    static {
        DOWNLOAD_BIN_BASE_VERSION = "00.00";
    }

    public GetDiagSoft(Context context, ProgressDialog progressDiag, Handler handler) {
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

    protected LatestDiagSoftsResponse doInBackground(String... params) {
        String cc = MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey);
        String productSerialNo = MySharedPreferences.getStringValue(this.context, MySharedPreferences.serialNoKey);
        LatestDiagSoftsResponse res = null;
        String version = MySharedPreferences.getStringValue(this.context, Constants.DOWNLOADBIN);
        this.activity = (KeyToUpgradeActivity) this.context;
        if (version.length() > 0) {
            DOWNLOAD_BIN_BASE_VERSION = version;
        }
        if (MainActivity.country == null || MainActivity.country.length() <= 0) {
            MainActivity.country = Locale.getDefault().getCountry();
        }
        try {
            this.diagsoftUrl = ConfigDBManager.getInstance(this.context).getUrlByKey(KeyConstant.diagsoft_download_ex);
        } catch (HttpException e2) {
            e2.printStackTrace();
        }
        int lanId = AndroidToLan.getLanId(MainActivity.country);
        try {
            res = new UpgradeAction(this.context).queryLatestDiagSofts(cc, productSerialNo, new StringBuilder(String.valueOf(lanId)).toString(), new StringBuilder(String.valueOf(XStream.NO_REFERENCES)).toString());
        } catch (HttpException e1) {
            this.mHandler.obtainMessage(0).sendToTarget();
            e1.printStackTrace();
        } catch (NullPointerException e) {
            this.mHandler.obtainMessage(Constant.ERROR_GET_DATA_FAILED).sendToTarget();
            e.printStackTrace();
        } catch (Exception e3) {
            this.mHandler.obtainMessage(Constant.ERROR_NETWORK).sendToTarget();
            e3.printStackTrace();
        }
        return res;
    }

    protected void onPostExecute(LatestDiagSoftsResponse res1) {
        super.onPostExecute(res1);
        if (res1 != null && res1.getCode() == 0) {
            if (this.progress != null) {
                if (this.progress.isShowing()) {
                    this.progress.dismiss();
                }
            }
            Log.i("getPublicSoft", "\u83b7\u53d6\u8bca\u65ad\u8f6f\u4ef6\u4e0b\u8f7d\u4fe1\u606f" + res1.toString());
            String serialNo = MySharedPreferences.getStringValue(this.context, MySharedPreferences.serialNoKey);
            String lanName = AndroidToLan.toLan(MainMenuActivity.country);
            List<X431PadDtoSoft> list = res1.getX431PadSoftList();
            if (list != null && list.size() > 0) {
                for (X431PadDtoSoft bean : list) {
                    bean.setChecked(true);
                    String name = bean.getSoftName().replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE);
                    if (name.equalsIgnoreCase("EOBD")) {
                        name = "EOBD2";
                    }
                    if (name.equalsIgnoreCase("ISUZU(Japan)")) {
                        name = "JPISUZU";
                    }
                    if (name.equalsIgnoreCase("AUSTFORD")) {
                        name = "FORD";
                    }
                    String maxOldVersion2 = DBDao.getInstance(this.context).getMaxVersion(serialNo, name, lanName, MainActivity.database);
                    if (maxOldVersion2 != null) {
                        double maxOld = Double.parseDouble(maxOldVersion2.split("V")[1]);
                        bean.setMaxOldVersion(maxOldVersion2);
                    }
                    double version = Double.parseDouble(bean.getVersionNo().split("V")[1]);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        if (dateFormat.parse(bean.getFreeUseEndTime()).before(dateFormat.parse(bean.getServerCurrentTime()))) {
                            bean.setExpired(true);
                            bean.setRemarks("\u8fc7\u671f");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    NLog.m916d("GetDiagSoft", "FreeUseEndTime: " + bean.getFreeUseEndTime() + ", ServerCurrentTime: " + bean.getServerCurrentTime());
                    bean.setType(3);
                    bean.setUrl(this.diagsoftUrl);
                }
                this.activity.setX431PublicSoftList(list);
            } else if (res1.getMsg() == null || res1.getMsg().equals(XmlPullParser.NO_NAMESPACE)) {
                this.mHandler.sendEmptyMessage(Constant.ERROR_GET_DATA_FAILED);
            } else {
                Bundle data = new Bundle();
                data.putString(BundleBuilder.AskFromMessage, res1.getMsg());
                Message msg = new Message();
                msg.what = Constant.ERROR_GET_DATA_FAILED;
                msg.setData(data);
                this.mHandler.sendMessage(msg);
            }
        } else if (this.progress != null) {
            if (this.progress.isShowing()) {
                this.progress.dismiss();
                this.mHandler.sendEmptyMessage(Constant.ERROR_SERVER);
            }
        }
    }
}

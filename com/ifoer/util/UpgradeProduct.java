package com.ifoer.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.Constant;
import com.ifoer.entity.UpgradeProductResult;
import com.ifoer.webservice.UserServiceClient;
import java.net.SocketTimeoutException;

public class UpgradeProduct extends AsyncTask<String, String, UpgradeProductResult> {
    private Context context;
    private Handler mHandler;
    private ProgressDialog progress;
    private String psw;

    public UpgradeProduct(Context context, ProgressDialog progressDiag, Handler handler, String psw) {
        this.context = context;
        if (progressDiag != null) {
            this.progress = progressDiag;
        }
        this.mHandler = handler;
        this.psw = psw;
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

    protected UpgradeProductResult doInBackground(String... params) {
        String cc = MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey);
        String productSerialNo = MySharedPreferences.getStringValue(this.context, MySharedPreferences.serialNoKey);
        String token = MySharedPreferences.getStringValue(this.context, MySharedPreferences.TokenKey);
        UserServiceClient updateSoftware = new UserServiceClient();
        updateSoftware.setCc(cc);
        updateSoftware.setToken(token);
        UpgradeProductResult result = null;
        try {
            Log.i("useractivity", "\u6267\u884c\u5145\u503c");
            result = updateSoftware.upgradeProduct(productSerialNo, this.psw);
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

    protected void onPostExecute(UpgradeProductResult result) {
        super.onPostExecute(result);
        if (result != null) {
            if (this.progress != null && this.progress.isShowing()) {
                this.progress.dismiss();
            }
            if (result.getCode() == 0) {
                this.mHandler.obtainMessage(Constant.CHARGE_SECCUSS, result).sendToTarget();
            } else if (result.getCode() == Constant.ERROR_CODE) {
                Toast.makeText(this.context, this.context.getString(C0136R.string.error_card_psw_805), 0).show();
            } else if (result.getCode() == Constant.ERROR_GET_DATA_FAILED) {
                Toast.makeText(this.context, this.context.getString(C0136R.string.error_card_psw_806), 0).show();
            } else if (result.getCode() == Constant.CHECK_UPDATE_SUCCESS) {
                Toast.makeText(this.context, this.context.getString(C0136R.string.error_card_psw_807), 0).show();
            } else if (result.getCode() == Constant.CHARGE_SECCUSS) {
                Toast.makeText(this.context, this.context.getString(C0136R.string.error_card_psw_808), 0).show();
            } else if (result.getCode() == Constant.NEED_BUY) {
                Toast.makeText(this.context, this.context.getString(C0136R.string.error_card_psw_809), 0).show();
            } else {
                result.failToast(this.context, result.getCode());
                this.mHandler.obtainMessage(Constant.ERROR_GET_DATA_FAILED).sendToTarget();
            }
        } else if (this.progress != null && this.progress.isShowing()) {
            this.progress.dismiss();
            this.mHandler.sendEmptyMessage(Constant.ERROR_SERVER);
        }
    }
}

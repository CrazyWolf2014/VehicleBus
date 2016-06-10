package com.ifoer.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.car.result.WSResult;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.dto.Msg;
import com.ifoer.entity.Constant;
import com.ifoer.entity.ProductToUpgradeToInfo;
import com.ifoer.webservice.UserServiceClient;
import java.net.SocketTimeoutException;

public class CheckProductToUpgrade extends AsyncTask<String, String, WSResult> {
    private Context context;
    private Handler mHandler;
    private ProgressDialog progress;

    public CheckProductToUpgrade(Context context, ProgressDialog progressDiag, Handler handler) {
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

    protected WSResult doInBackground(String... params) {
        String cc = MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey);
        String productSerialNo = MySharedPreferences.getStringValue(this.context, MySharedPreferences.serialNoKey);
        String token = MySharedPreferences.getStringValue(this.context, MySharedPreferences.TokenKey);
        UserServiceClient updateSoftware = new UserServiceClient();
        updateSoftware.setCc(cc);
        updateSoftware.setToken(token);
        WSResult result = null;
        try {
            result = updateSoftware.checkProductToUpgrade(productSerialNo, token);
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

    protected void onPostExecute(WSResult result) {
        super.onPostExecute(result);
        if (result != null) {
            if (this.progress != null && this.progress.isShowing()) {
                this.progress.dismiss();
            }
            if (result.getCode() == 0) {
                ProductToUpgradeToInfo info = (ProductToUpgradeToInfo) result;
                if (info.getNeedRenew() == 0) {
                    this.mHandler.sendEmptyMessage(Constant.CHECK_UPDATE_SUCCESS);
                    return;
                }
                this.mHandler.sendEmptyMessage(Constant.NEED_BUY);
                Log.i("CheckProductToUpgrade", new StringBuilder(Msg.DATE).append(info.getFreeTime()).toString());
            } else if (result.getCode() == MyHttpException.ERROR_SERIAL_NOEXIST) {
                Toast.makeText(this.context, C0136R.string.ERROR_SERIAL_NOEXIST, 0).show();
            } else if (result.getCode() == MyHttpException.ERROR_PRODUCT_CONF_EMPTY) {
                Toast.makeText(this.context, C0136R.string.ERROR_PRODUCT_CONF_EMPTY, 0).show();
            } else if (result.getCode() == MyHttpException.ERROR_UNREGISTER) {
                Toast.makeText(this.context, C0136R.string.ERROR_UNREGISTER_PRODUCT, 0).show();
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

package com.ifoer.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.CardInfo;
import com.ifoer.entity.Constant;
import com.ifoer.ui.CheckCardInfoActivity;
import com.ifoer.webservice.UserServiceClient;
import java.net.SocketTimeoutException;
import org.jivesoftware.smackx.Form;

public class GetChargCardInfo extends AsyncTask<String, String, CardInfo> {
    private String cardNo;
    private Context context;
    private Handler mHandler;
    private ProgressDialog progress;

    public GetChargCardInfo(Context context, String cardNo, ProgressDialog progressDiag, Handler handler) {
        this.context = context;
        if (progressDiag != null) {
            this.progress = progressDiag;
        }
        this.mHandler = handler;
        this.cardNo = cardNo;
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

    protected CardInfo doInBackground(String... params) {
        String cc = MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey);
        String token = MySharedPreferences.getStringValue(this.context, MySharedPreferences.TokenKey);
        UserServiceClient updateSoftware = new UserServiceClient();
        updateSoftware.setCc(cc);
        updateSoftware.setToken(token);
        CardInfo result = null;
        try {
            result = updateSoftware.getSysCardInfoByCardNo(this.cardNo, token);
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

    protected void onPostExecute(CardInfo result) {
        super.onPostExecute(result);
        if (result != null) {
            if (this.progress != null && this.progress.isShowing()) {
                this.progress.dismiss();
            }
            if (result.getCode() == 0) {
                if (result.isDtoisNull()) {
                    Toast.makeText(this.context, this.context.getString(C0136R.string.ERROR_SERIAL_NOEXIST), 0).show();
                    return;
                }
                Intent intent = new Intent(this.context, CheckCardInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Form.TYPE_RESULT, result);
                intent.putExtras(bundle);
                this.context.startActivity(intent);
            } else if (result.getCode() == 811) {
                Toast.makeText(this.context, this.context.getString(C0136R.string.upgrade_record_null), 0).show();
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

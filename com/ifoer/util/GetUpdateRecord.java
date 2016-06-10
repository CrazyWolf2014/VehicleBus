package com.ifoer.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.car.result.WSResult;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.CardConsumeListResult;
import com.ifoer.entity.Constant;
import com.ifoer.ui.RecordOfpinCardActivity;
import com.ifoer.webservice.UserServiceClient;
import java.net.SocketTimeoutException;
import org.jivesoftware.smackx.Form;

public class GetUpdateRecord extends AsyncTask<String, String, WSResult> {
    private Context context;
    private Handler mHandler;
    private ProgressDialog progress;

    public GetUpdateRecord(Context context, ProgressDialog progressDiag, Handler handler) {
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
            result = updateSoftware.getProductUpgradeRecord(productSerialNo, token);
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
                CardConsumeListResult listresult = (CardConsumeListResult) result;
                Log.i("GetUpdateRecord", new StringBuilder(Form.TYPE_RESULT).append(listresult.getCardListReust().size()).toString());
                Intent intent = new Intent(this.context, RecordOfpinCardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", listresult);
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

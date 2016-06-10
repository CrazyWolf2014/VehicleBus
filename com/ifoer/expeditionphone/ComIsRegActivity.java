package com.ifoer.expeditionphone;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.ifoer.entity.UserInfoResult;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.webservice.WebServiceClient;
import java.net.SocketTimeoutException;

public class ComIsRegActivity extends Activity {
    private WebServiceClient clientService;
    private String editText;
    private Context mContext;
    private String pswText;

    public class AsyTask extends AsyncTask<Void, Void, String> {
        UserInfoResult logReg;

        public AsyTask() {
            this.logReg = null;
        }

        protected String doInBackground(Void... params) {
            ComIsRegActivity.this.clientService = new WebServiceClient();
            Log.e("zdy", "serialport" + ComIsRegActivity.this.getEditText().toString());
            try {
                this.logReg = ComIsRegActivity.this.clientService.getUserInfoBySerialNo(ComIsRegActivity.this.getEditText().toString(), ComIsRegActivity.this.mContext);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("zdy", "ComIsRegActivity****onPostExecute" + this.logReg);
            if (this.logReg != null) {
                Log.e("zdy", "ComIsRegActivity****logReg.getCode()=" + this.logReg.getCode());
                if (this.logReg.getCode() == 0) {
                    MySharedPreferences.setString(ComIsRegActivity.this.mContext, MySharedPreferences.isSerialPortIDReg, "yes");
                } else if (this.logReg.getCode() != MyHttpException.ERROR_PARAMETER_EMPTY && this.logReg.getCode() != MyHttpException.ERROR_SERIAL_NOEXIST && this.logReg.getCode() != MyHttpException.ERROR_NOT_SELL && this.logReg.getCode() != MyHttpException.ERROR_NULLIFY) {
                    this.logReg.getCode();
                }
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    public ComIsRegActivity() {
        this.mContext = this;
    }

    public String getEditText() {
        return this.editText;
    }

    public void setEditText(String editText) {
        this.editText = editText;
    }

    public String getPswText() {
        return this.pswText;
    }

    public void setPswText(String pswText) {
        this.pswText = pswText;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
    }
}

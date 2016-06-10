package com.ifoer.expeditionphone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.ifoer.entity.Constant;
import com.ifoer.entity.EndUserFullDTO;
import com.ifoer.entity.UserInfoResult;
import com.ifoer.mine.model.BaseCode;
import com.ifoer.mine.model.UserData;
import com.ifoer.mine.model.UserDataResult;
import com.ifoer.util.Common;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.webservice.UserServiceClient;
import com.ifoer.webservice.WebServiceClient;
import com.launch.service.BundleBuilder;
import com.tencent.mm.sdk.plugin.BaseProfile;
import java.net.SocketTimeoutException;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class MoreDataActivty extends Activity implements OnClickListener {
    private Button backBtn;
    private Button cancelBtn;
    private String cc;
    private TextView ccTv;
    private Context context;
    private Button dataBtn;
    private EndUserFullDTO eu;
    private boolean hasLogin;
    private long lastTime;
    private BroadcastReceiver mBroadcastReceiver;
    private final Handler mHandler;
    private String mail;
    private TextView mailEd;
    private String name;
    private TextView nameEd;
    private boolean needModiName;
    private TextView phoneEd;
    private ProgressDialog progressDialogs;
    private String regMobile;
    private String token;
    private int usertypeId;
    private BaseCode ws;
    private String zipCode;
    private LinearLayout zipCodeLay;
    private TextView zipEdt;

    /* renamed from: com.ifoer.expeditionphone.MoreDataActivty.1 */
    class C06081 extends BroadcastReceiver {
        C06081() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - MoreDataActivty.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                MoreDataActivty.this.lastTime = System.currentTimeMillis();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.MoreDataActivty.2 */
    class C06092 extends Handler {
        C06092() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    if (MoreDataActivty.this.progressDialogs != null && MoreDataActivty.this.progressDialogs.isShowing()) {
                        MoreDataActivty.this.progressDialogs.dismiss();
                    }
                    Toast.makeText(MoreDataActivty.this.context, C0136R.string.timeout, 0).show();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.MoreDataActivty.3 */
    class C06103 implements OnClickListener {
        C06103() {
        }

        public void onClick(View v) {
            MoreDataActivty.this.finish();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.MoreDataActivty.4 */
    class C06114 implements OnClickListener {
        C06114() {
        }

        public void onClick(View v) {
            MoreDataActivty.this.finish();
        }
    }

    class GetContactAsy extends AsyncTask<String, String, String> {
        UserDataResult userData;

        GetContactAsy() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            UserServiceClient client = new UserServiceClient();
            MoreDataActivty.this.cc = MySharedPreferences.getStringValue(MoreDataActivty.this.context, MySharedPreferences.CCKey);
            MoreDataActivty.this.token = MySharedPreferences.getStringValue(MoreDataActivty.this.context, MySharedPreferences.TokenKey);
            client.setCc(MoreDataActivty.this.cc);
            client.setToken(MoreDataActivty.this.token);
            try {
                this.userData = client.getContactData(MoreDataActivty.this.cc);
            } catch (Exception e) {
                MoreDataActivty.this.mHandler.obtainMessage(0).sendToTarget();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (MoreDataActivty.this.progressDialogs != null && MoreDataActivty.this.progressDialogs.isShowing()) {
                MoreDataActivty.this.progressDialogs.dismiss();
            }
            if (this.userData == null) {
                Log.i("\u4fee\u6539\u7528\u6237\u8d44\u6599\u754c\u9762", "\u76f8\u5e94\u6d88\u606f\u7a7a");
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.getString(C0136R.string.error_server), 0).show();
            } else if (this.userData.getCode() == 0) {
                MoreDataActivty.this.mailEd.setText(this.userData.getuEmail());
                MoreDataActivty.this.phoneEd.setText(this.userData.getuMobile());
                MoreDataActivty.this.nameEd.setText(MySharedPreferences.getStringValue(MoreDataActivty.this.context, BaseProfile.COL_USERNAME));
                MoreDataActivty.this.ccTv.setText(MySharedPreferences.getStringValue(MoreDataActivty.this.context, MySharedPreferences.CCKey));
            } else if (TextUtils.isEmpty(this.userData.getMsg())) {
                Toast.makeText(MoreDataActivty.this.context, this.userData.getMsg(), 0).show();
            } else {
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.getString(C0136R.string.error_server), 0).show();
            }
        }
    }

    class GetDataAsy extends AsyncTask<String, String, String> {
        UserData userData;

        GetDataAsy() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            MoreDataActivty.this.progressDialogs = new ProgressDialog(MoreDataActivty.this.context);
            MoreDataActivty.this.progressDialogs.setMessage(MoreDataActivty.this.getResources().getString(C0136R.string.find_wait));
            MoreDataActivty.this.progressDialogs.setCancelable(false);
            MoreDataActivty.this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            UserServiceClient client = new UserServiceClient();
            MoreDataActivty.this.cc = MySharedPreferences.getStringValue(MoreDataActivty.this.context, MySharedPreferences.CCKey);
            MoreDataActivty.this.token = MySharedPreferences.getStringValue(MoreDataActivty.this.context, MySharedPreferences.TokenKey);
            client.setCc(MoreDataActivty.this.cc);
            client.setToken(MoreDataActivty.this.token);
            try {
                this.userData = client.getData(MoreDataActivty.this.cc);
            } catch (Exception e) {
                MoreDataActivty.this.mHandler.obtainMessage(0).sendToTarget();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (this.userData == null) {
                if (MoreDataActivty.this.progressDialogs != null && MoreDataActivty.this.progressDialogs.isShowing()) {
                    MoreDataActivty.this.progressDialogs.dismiss();
                }
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.getString(C0136R.string.error_server), 0).show();
            } else if (this.userData.getCode() == 0) {
                if (!TextUtils.isEmpty(this.userData.getZipCode())) {
                    MoreDataActivty.this.zipEdt.setText(this.userData.getZipCode().toString());
                    MoreDataActivty.this.zipEdt.setEnabled(false);
                }
                new GetContactAsy().execute(new String[0]);
            } else {
                if (MoreDataActivty.this.progressDialogs != null && MoreDataActivty.this.progressDialogs.isShowing()) {
                    MoreDataActivty.this.progressDialogs.dismiss();
                }
                if (TextUtils.isEmpty(this.userData.getMsg())) {
                    Toast.makeText(MoreDataActivty.this.context, this.userData.getMsg(), 0).show();
                } else {
                    Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.getString(C0136R.string.error_server), 0).show();
                }
            }
        }
    }

    public class GetUserInfo extends AsyncTask<Void, Void, String> {
        UserInfoResult logReg;

        public GetUserInfo() {
            this.logReg = null;
        }

        protected String doInBackground(Void... params) {
            try {
                this.logReg = new WebServiceClient().getUserInfoBySerialNo(MySharedPreferences.getStringValue(MoreDataActivty.this.context, Constants.SERIALNO), MoreDataActivty.this.context);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (MoreDataActivty.this.progressDialogs != null) {
                MoreDataActivty.this.progressDialogs.dismiss();
            }
            if (this.logReg == null) {
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.context.getResources().getString(C0136R.string.ERROR_SERVER), 0).show();
            } else if (this.logReg.getCode() == 0) {
                MoreDataActivty.this.mailEd.setText(this.logReg.getEmail());
                MoreDataActivty.this.nameEd.setText(this.logReg.getUserName());
                MoreDataActivty.this.ccTv.setText(this.logReg.getCc());
            } else if (this.logReg.getCode() == MyHttpException.ERROR_PARAMETER_EMPTY) {
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.context.getResources().getString(C0136R.string.notic_null), 0).show();
            } else if (this.logReg.getCode() == MyHttpException.ERROR_SERIAL_NOEXIST) {
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.context.getResources().getString(C0136R.string.check_file), 0).show();
            } else if (this.logReg.getCode() == MyHttpException.ERROR_NOT_SELL) {
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.context.getResources().getString(C0136R.string.no_purchase), 0).show();
            } else if (this.logReg.getCode() == MyHttpException.ERROR_NULLIFY) {
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.context.getResources().getString(C0136R.string.obsolete), 0).show();
            } else if (this.logReg.getCode() == MyHttpException.ERROR_UNREGISTER) {
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.context.getResources().getString(C0136R.string.ERROR_UNREGISTER_PRODUCT), 1).show();
                ((Activity) MoreDataActivty.this.context).startActivityForResult(new Intent(MoreDataActivty.this.context, ComRegActivity.class), 11);
            } else if (this.logReg.getCode() == MyHttpException.ERROR_SERVER) {
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.context.getResources().getString(C0136R.string.ERROR_SERVER), 0).show();
            } else if (this.logReg.getCode() != Constant.ERROR_CODE) {
            } else {
                if (this.logReg.getMessage() == null || this.logReg.getMessage().equals(XmlPullParser.NO_NAMESPACE)) {
                    MoreDataActivty.this.mHandler.sendEmptyMessage(Constant.ERROR_CODE);
                    return;
                }
                Toast.makeText(MoreDataActivty.this.context, this.logReg.getMessage(), 0).show();
                Bundle data = new Bundle();
                data.putString(BundleBuilder.AskFromMessage, this.logReg.getMessage());
                Message msg = new Message();
                msg.what = Constant.ERROR_CODE;
                msg.setData(data);
                MoreDataActivty.this.mHandler.sendMessage(msg);
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            MoreDataActivty.this.progressDialogs = new ProgressDialog(MoreDataActivty.this.context);
            MoreDataActivty.this.progressDialogs.setMessage(MoreDataActivty.this.context.getResources().getString(C0136R.string.login_wait));
            MoreDataActivty.this.progressDialogs.setCancelable(false);
            MoreDataActivty.this.progressDialogs.show();
        }
    }

    class UpdateDataAsy extends AsyncTask<String, String, String> {
        UpdateDataAsy() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            MoreDataActivty.this.progressDialogs = new ProgressDialog(MoreDataActivty.this.context);
            MoreDataActivty.this.progressDialogs.setMessage(MoreDataActivty.this.getResources().getString(C0136R.string.modi_data_now));
            MoreDataActivty.this.progressDialogs.setCancelable(false);
            MoreDataActivty.this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            WebServiceClient client = new WebServiceClient();
            MoreDataActivty.this.cc = MySharedPreferences.getStringValue(MoreDataActivty.this.context, MySharedPreferences.CCKey);
            MoreDataActivty.this.token = MySharedPreferences.getStringValue(MoreDataActivty.this.context, MySharedPreferences.TokenKey);
            try {
                MoreDataActivty.this.ws = client.setPhone(MoreDataActivty.this.regMobile, MoreDataActivty.this.cc);
            } catch (Exception e) {
                MoreDataActivty.this.mHandler.obtainMessage(0).sendToTarget();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (MoreDataActivty.this.ws == null) {
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.context.getResources().getString(C0136R.string.modi_data_faild), 0).show();
                MoreDataActivty.this.progressDialogs.dismiss();
            } else if (MoreDataActivty.this.ws.getCode() == 0) {
                if (MoreDataActivty.this.needModiName) {
                    new UpdateName().execute(new String[0]);
                    return;
                }
                MoreDataActivty.this.progressDialogs.dismiss();
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.context.getResources().getString(C0136R.string.modi_data_success), 0).show();
                MoreDataActivty.this.finish();
            } else if (MoreDataActivty.this.ws.getCode() == 30027) {
                MoreDataActivty.this.progressDialogs.dismiss();
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.getString(C0136R.string.reg_phone_wrong), 0).show();
            } else {
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.context.getResources().getString(C0136R.string.modi_data_faild), 0).show();
                MoreDataActivty.this.progressDialogs.dismiss();
            }
        }
    }

    class UpdateName extends AsyncTask<String, String, String> {
        UpdateName() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            WebServiceClient client = new WebServiceClient();
            MoreDataActivty.this.cc = MySharedPreferences.getStringValue(MoreDataActivty.this.context, MySharedPreferences.CCKey);
            MoreDataActivty.this.token = MySharedPreferences.getStringValue(MoreDataActivty.this.context, MySharedPreferences.TokenKey);
            try {
                MoreDataActivty.this.ws = client.setName(MoreDataActivty.this.name, MoreDataActivty.this.cc);
            } catch (Exception e) {
                MoreDataActivty.this.mHandler.obtainMessage(0).sendToTarget();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (MoreDataActivty.this.progressDialogs != null && MoreDataActivty.this.progressDialogs.isShowing()) {
                MoreDataActivty.this.progressDialogs.dismiss();
            }
            if (MoreDataActivty.this.ws == null) {
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.context.getResources().getString(C0136R.string.modi_data_faild), 0).show();
            } else if (MoreDataActivty.this.ws.getCode() == -1) {
                SimpleDialog.validTokenDialog(MoreDataActivty.this.context);
            } else if (MoreDataActivty.this.ws.getCode() == 0) {
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.context.getResources().getString(C0136R.string.modi_data_success), 0).show();
                MoreDataActivty.this.finish();
            } else if (MoreDataActivty.this.ws.getCode() == 1) {
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.context.getResources().getString(C0136R.string.reg_user_length), 0).show();
            } else if (MoreDataActivty.this.ws.getCode() == 2) {
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.context.getResources().getString(C0136R.string.reg_user_length), 0).show();
            } else if (MoreDataActivty.this.ws.getCode() == 3) {
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.context.getResources().getString(C0136R.string.reg_user_wrong), 0).show();
            } else if (MoreDataActivty.this.ws.getCode() == 4) {
                Toast.makeText(MoreDataActivty.this.context, MoreDataActivty.this.context.getResources().getString(C0136R.string.reg_user_replay), 0).show();
            }
        }
    }

    public MoreDataActivty() {
        this.usertypeId = 2;
        this.ws = null;
        this.eu = new EndUserFullDTO();
        this.needModiName = false;
        this.hasLogin = false;
        this.mBroadcastReceiver = new C06081();
        this.mHandler = new C06092();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.more_person_data);
        this.context = this;
        createView();
    }

    private void createView() {
        registerBoradcastReceiver();
        this.hasLogin = getIntent().getBooleanExtra("hasLogin", false);
        this.backBtn = (Button) findViewById(C0136R.id.returnBtn);
        this.mailEd = (TextView) findViewById(C0136R.id.e_mail);
        this.backBtn.setOnClickListener(new C06103());
        this.nameEd = (TextView) findViewById(C0136R.id.name);
        this.phoneEd = (TextView) findViewById(C0136R.id.pwd);
        this.zipCodeLay = (LinearLayout) findViewById(C0136R.id.zipL);
        this.ccTv = (TextView) findViewById(C0136R.id.cc);
        this.zipEdt = (TextView) findViewById(C0136R.id.zipEdt);
        this.dataBtn = (Button) findViewById(C0136R.id.registerbtn);
        this.dataBtn.setOnClickListener(this);
        this.cancelBtn = (Button) findViewById(C0136R.id.cancel_btn);
        if (this.hasLogin) {
            new GetDataAsy().execute(new String[0]);
        } else {
            this.zipCodeLay.setVisibility(8);
            new GetUserInfo().execute(new Void[0]);
        }
        this.cancelBtn.setOnClickListener(new C06114());
    }

    public void onClick(View v) {
        if (v.getId() != C0136R.id.registerbtn) {
            return;
        }
        if (Common.isNetworkAvailable(this.context)) {
            this.regMobile = this.phoneEd.getText().toString();
            this.zipCode = this.zipEdt.getText().toString().trim();
            this.mail = this.mailEd.getText().toString().trim();
            this.name = this.nameEd.getText().toString().trim();
            if (this.name.equals(MySharedPreferences.getStringValue(this.context, BaseProfile.COL_USERNAME))) {
                this.needModiName = false;
            } else {
                this.needModiName = true;
            }
            if (this.mail.equals(XmlPullParser.NO_NAMESPACE)) {
                Toast.makeText(this.context, C0136R.string.Email_cannot_empty, 1).show();
                return;
            } else if (Common.isEmail(this.mail)) {
                MySharedPreferences.setString(this.context, MySharedPreferences.savemail, this.mail);
                if (this.regMobile.equals(XmlPullParser.NO_NAMESPACE)) {
                    Toast.makeText(this.context, C0136R.string.modi_phone_right, 0).show();
                    return;
                } else if (!Common.isMobileNO(this.regMobile)) {
                    Toast.makeText(this.context, C0136R.string.reg_user_phone, 0).show();
                    return;
                } else if (TextUtils.isEmpty(this.zipCode)) {
                    Toast.makeText(this.context, this.context.getResources().getString(C0136R.string.can_not_be_empty), 0).show();
                    return;
                } else if (TextUtils.isEmpty(this.name)) {
                    Toast.makeText(this.context, this.context.getResources().getString(C0136R.string.can_not_be_empty), 0).show();
                    return;
                } else if (Common.isEmail(this.name)) {
                    Toast.makeText(this.context, this.context.getResources().getString(C0136R.string.reg_user_wrong), 0).show();
                    return;
                } else if (Common.isNetworkAvailable(this.context)) {
                    new UpdateDataAsy().execute(new String[0]);
                    return;
                } else {
                    Toast.makeText(this.context, C0136R.string.network, 0).show();
                    return;
                }
            } else {
                Toast.makeText(this.context, C0136R.string.reg_ToastEmail, 1).show();
                return;
            }
        }
        Toast.makeText(this.context, C0136R.string.network, 0).show();
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        this.context.registerReceiver(this.mBroadcastReceiver, myIntentFilter);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mBroadcastReceiver != null) {
            unregisterReceiver(this.mBroadcastReceiver);
        }
    }
}

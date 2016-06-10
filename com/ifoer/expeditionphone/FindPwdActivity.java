package com.ifoer.expeditionphone;

import CRP.utils.CRPTools;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.cnlaunch.x431pro.common.RequestCode;
import com.ifoer.entity.UserInfoResult;
import com.ifoer.mine.Contact;
import com.ifoer.mine.model.BaseCode;
import com.ifoer.ui.MainMenuActivity;
import com.ifoer.util.Common;
import com.ifoer.util.Files;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.NetPOSPrinter;
import com.ifoer.util.SimpleDialog;
import com.ifoer.util.Tools;
import com.ifoer.view.MenuHorizontalScrollView;
import com.ifoer.webservice.WebServiceClient;
import com.tencent.mm.sdk.platformtools.Util;
import java.net.SocketTimeoutException;
import java.util.regex.Pattern;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public class FindPwdActivity extends Activity {
    private String SN;
    private Button backBtn;
    private BaseCode baseCode;
    private Button btn_sendVerifyCode;
    protected LinearLayout car_maintain;
    private String cc;
    private LinearLayout circleLay;
    private EditText comPswEd;
    private String comfirPsw;
    private Context context;
    CountDownTimer countdownTimer;
    private LinearLayout dataLay;
    private String email;
    private Button findButton;
    private TextView findPTV;
    private Button find_mail;
    private LayoutInflater inflater;
    private LinearLayout inforLay;
    private String language;
    private long lastTime;
    private BroadcastReceiver mBroadcastReceiver;
    private final Handler mHandler;
    private RelativeLayout menu;
    public Button menuBtn;
    private LinearLayout moreLay;
    private LinearLayout mySpaceLay;
    private boolean needResendVerifyCode;
    private String newPsw;
    private EditText newPswEd;
    private ProgressDialog progressDialogs;
    private MenuHorizontalScrollView scrollView;
    private int time;
    private String token;
    private LinearLayout userLay;
    private String userName;
    private String verifyCode;
    private EditText verifyCodeEd;
    private UserInfoResult ws;

    /* renamed from: com.ifoer.expeditionphone.FindPwdActivity.1 */
    class C05701 extends Handler {
        C05701() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    if (FindPwdActivity.this.progressDialogs != null && FindPwdActivity.this.progressDialogs.isShowing()) {
                        FindPwdActivity.this.progressDialogs.dismiss();
                    }
                    Toast.makeText(FindPwdActivity.this.context, C0136R.string.timeout, 1).show();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.FindPwdActivity.2 */
    class C05712 extends BroadcastReceiver {
        C05712() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - FindPwdActivity.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                FindPwdActivity.this.lastTime = System.currentTimeMillis();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.FindPwdActivity.3 */
    class C05723 extends CountDownTimer {
        C05723(long $anonymous0, long $anonymous1) {
            super($anonymous0, $anonymous1);
        }

        public void onTick(long millisUntilFinished) {
            FindPwdActivity.this.btn_sendVerifyCode.setEnabled(false);
            FindPwdActivity.this.time = (int) (millisUntilFinished / 1000);
            FindPwdActivity.this.btn_sendVerifyCode.setTextSize(10.0f);
            FindPwdActivity.this.btn_sendVerifyCode.setText(FindPwdActivity.this.getString(C0136R.string.set_verify_send, new Object[]{String.valueOf(FindPwdActivity.this.time)}));
        }

        public void onFinish() {
            FindPwdActivity.this.btn_sendVerifyCode.setEnabled(true);
            FindPwdActivity.this.btn_sendVerifyCode.setTextSize(15.0f);
            FindPwdActivity.this.btn_sendVerifyCode.setText(FindPwdActivity.this.getString(C0136R.string.set_verify_resend));
            if (FindPwdActivity.this.countdownTimer != null) {
                FindPwdActivity.this.countdownTimer.cancel();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.FindPwdActivity.4 */
    class C05734 implements OnClickListener {
        C05734() {
        }

        public void onClick(View v) {
            FindPwdActivity.this.finish();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.FindPwdActivity.5 */
    class C05745 implements OnClickListener {
        C05745() {
        }

        public void onClick(View arg0) {
            if (!FindPwdActivity.this.needResendVerifyCode) {
                FindPwdActivity.this.SN = FindPwdActivity.this.findPTV.getText().toString();
                if (FindPwdActivity.this.SN.trim().equals(XmlPullParser.NO_NAMESPACE) || FindPwdActivity.this.SN.trim() == null) {
                    Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.context.getResources().getString(C0136R.string.input_sn), 0).show();
                } else if (Common.isNetworkAvailable(FindPwdActivity.this.context)) {
                    new getUserNamedAsy().execute(new String[0]);
                } else {
                    Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.context.getResources().getString(C0136R.string.network), 1).show();
                }
            } else if (Common.isNetworkAvailable(FindPwdActivity.this.context)) {
                new sendVerifyCodeAsy().execute(new String[0]);
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.FindPwdActivity.6 */
    class C05756 implements OnClickListener {
        C05756() {
        }

        public void onClick(View v) {
            FindPwdActivity.this.checkPrama();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.FindPwdActivity.7 */
    class C05767 implements OnClickListener {
        C05767() {
        }

        public void onClick(View v) {
            FindPwdActivity.this.checkPrama();
        }
    }

    class VerifyVerifyCodeAsy extends AsyncTask<String, String, String> {
        VerifyVerifyCodeAsy() {
        }

        protected String doInBackground(String... arg0) {
            try {
                FindPwdActivity.this.baseCode = new WebServiceClient().verifyVerifyCode(FindPwdActivity.this.userName, FindPwdActivity.this.verifyCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPreExecute() {
            FindPwdActivity.this.progressDialogs.setMessage(FindPwdActivity.this.getString(C0136R.string.register_check_identify_code));
            FindPwdActivity.this.progressDialogs.show();
        }

        protected void onPostExecute(String result) {
            if (FindPwdActivity.this.progressDialogs != null && FindPwdActivity.this.progressDialogs.isShowing()) {
                FindPwdActivity.this.progressDialogs.dismiss();
            }
            if (FindPwdActivity.this.baseCode == null) {
                Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.getString(C0136R.string.error_server), 0).show();
            } else if (FindPwdActivity.this.baseCode.getCode() == 0) {
                new findPwdAsy().execute(new String[0]);
            } else if (FindPwdActivity.this.baseCode.getCode() == 110101) {
                Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.getString(C0136R.string.ERROR_PASTDUE_AUTH_CODE), 0).show();
            } else {
                Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.getString(C0136R.string.error_server), 0).show();
            }
        }
    }

    class findPwdAsy extends AsyncTask<String, String, String> {
        BaseCode baseCode;

        findPwdAsy() {
            this.baseCode = null;
        }

        protected void onPreExecute() {
            if (FindPwdActivity.this.progressDialogs == null) {
                FindPwdActivity.this.progressDialogs = new ProgressDialog(FindPwdActivity.this.context);
            }
            FindPwdActivity.this.progressDialogs.setMessage(FindPwdActivity.this.getString(C0136R.string.modi_pwd_now));
            FindPwdActivity.this.progressDialogs.show();
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            try {
                this.baseCode = new WebServiceClient().findPsw(FindPwdActivity.this.userName, FindPwdActivity.this.newPsw, FindPwdActivity.this.comfirPsw, FindPwdActivity.this.verifyCode);
            } catch (SocketTimeoutException e) {
                FindPwdActivity.this.mHandler.obtainMessage(0).sendToTarget();
            } catch (NullPointerException e2) {
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (this.baseCode != null) {
                if (FindPwdActivity.this.progressDialogs != null && FindPwdActivity.this.progressDialogs.isShowing()) {
                    FindPwdActivity.this.progressDialogs.dismiss();
                }
                if (this.baseCode.getCode() == -1) {
                    SimpleDialog.validTokenDialog(FindPwdActivity.this.context);
                } else if (this.baseCode.getCode() == MyHttpException.ERROR_PARAMETER_EMPTY) {
                    Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.context.getResources().getString(C0136R.string.find_wait), 1).show();
                } else if (this.baseCode.getCode() == NetPOSPrinter.PRINT_WIDTH) {
                    Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.context.getResources().getString(C0136R.string.user_null), 1).show();
                } else if (this.baseCode.getCode() == MyHttpException.ERROR_NOT_SET_PHONE) {
                    Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.context.getResources().getString(C0136R.string.find_email), 1).show();
                } else if (this.baseCode.getCode() == MyHttpException.ERROR_SERVER) {
                    Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.context.getResources().getString(C0136R.string.notic_serv), 1).show();
                } else if (this.baseCode.getCode() == MyHttpException.ERROR_NETWORK) {
                    Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.context.getResources().getString(C0136R.string.network), 1).show();
                } else if (this.baseCode.getCode() == 0) {
                    Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.context.getResources().getString(C0136R.string.reset_password_succeed), 1).show();
                    CRPTools.writeFileData(FindPwdActivity.this.cc, FindPwdActivity.this.newPsw, FindPwdActivity.this.email);
                    FindPwdActivity.this.finish();
                } else if (this.baseCode.getCode() == MyHttpException.ERROR_NOT_SET_EMAIL) {
                    Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.context.getResources().getString(C0136R.string.user_not_set_email), 1).show();
                } else if (this.baseCode.getCode() == 110101) {
                    Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.context.getResources().getString(C0136R.string.register_fail_prompt_1025), 1).show();
                } else if (this.baseCode.getCode() == 110202) {
                    Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.context.getResources().getString(C0136R.string.reset_password_fail), 1).show();
                } else if (this.baseCode.getCode() == RequestCode.REQ_QUERY_REPORT) {
                    Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.context.getResources().getString(C0136R.string.reg_pwd_wrong), 1).show();
                }
            } else if (FindPwdActivity.this.progressDialogs != null && FindPwdActivity.this.progressDialogs.isShowing()) {
                FindPwdActivity.this.progressDialogs.dismiss();
                Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.context.getResources().getString(C0136R.string.find_failed), 1).show();
            }
        }
    }

    class getUserNamedAsy extends AsyncTask<String, String, String> {
        getUserNamedAsy() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            FindPwdActivity.this.progressDialogs = new ProgressDialog(FindPwdActivity.this.context);
            FindPwdActivity.this.progressDialogs.setMessage(FindPwdActivity.this.getResources().getString(C0136R.string.find_wait));
            FindPwdActivity.this.progressDialogs.setCancelable(false);
            FindPwdActivity.this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            WebServiceClient client = new WebServiceClient();
            FindPwdActivity.this.cc = MySharedPreferences.getStringValue(FindPwdActivity.this.context, MySharedPreferences.CCKey);
            FindPwdActivity.this.token = MySharedPreferences.getStringValue(FindPwdActivity.this.context, MySharedPreferences.TokenKey);
            try {
                FindPwdActivity.this.ws = client.getUserInfoBySerialNo(FindPwdActivity.this.findPTV.getText().toString(), FindPwdActivity.this.context);
            } catch (SocketTimeoutException e) {
                FindPwdActivity.this.mHandler.obtainMessage(0).sendToTarget();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (FindPwdActivity.this.ws == null) {
                FindPwdActivity.this.progressDialogs.dismiss();
            } else if (FindPwdActivity.this.ws.getCode() == 0) {
                MySharedPreferences.setString(FindPwdActivity.this.context, MySharedPreferences.savecc, FindPwdActivity.this.ws.getUserName());
                FindPwdActivity.this.userName = FindPwdActivity.this.ws.getUserName();
                FindPwdActivity.this.email = FindPwdActivity.this.ws.getEmail();
                FindPwdActivity.this.cc = FindPwdActivity.this.ws.getCc();
                FindPwdActivity.this.needResendVerifyCode = true;
                new sendVerifyCodeAsy().execute(new String[0]);
            } else {
                if (!(FindPwdActivity.this.ws.getMessage().equalsIgnoreCase(XmlPullParser.NO_NAMESPACE) || FindPwdActivity.this.ws.getMessage().equalsIgnoreCase("null"))) {
                    Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.ws.getMessage(), 0).show();
                }
                FindPwdActivity.this.progressDialogs.dismiss();
            }
        }
    }

    class sendVerifyCodeAsy extends AsyncTask<String, String, String> {
        sendVerifyCodeAsy() {
        }

        protected String doInBackground(String... arg0) {
            WebServiceClient client = new WebServiceClient();
            try {
                Log.i("sendVerifyCodeAsy", "email" + FindPwdActivity.this.email);
                FindPwdActivity.this.baseCode = client.getVerifyCode(FindPwdActivity.this.userName, MainMenuActivity.country, Contact.RELATION_ASK);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPreExecute() {
            if (FindPwdActivity.this.needResendVerifyCode) {
                FindPwdActivity.this.progressDialogs.show();
            }
        }

        protected void onPostExecute(String result) {
            if (FindPwdActivity.this.progressDialogs != null && FindPwdActivity.this.progressDialogs.isShowing()) {
                FindPwdActivity.this.progressDialogs.dismiss();
            }
            if (FindPwdActivity.this.baseCode == null) {
                Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.getString(C0136R.string.error_server), 0).show();
            } else if (FindPwdActivity.this.baseCode.getCode() == 0) {
                Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.getString(C0136R.string.identify_send_to_email), 0).show();
                FindPwdActivity.this.countdownTimer.start();
                FindPwdActivity.this.findPTV.setEnabled(false);
            } else {
                Toast.makeText(FindPwdActivity.this.context, FindPwdActivity.this.getString(C0136R.string.set_verify_get_failure), 0).show();
            }
        }
    }

    public FindPwdActivity() {
        this.inflater = null;
        this.language = null;
        this.ws = null;
        this.needResendVerifyCode = false;
        this.time = 0;
        this.mHandler = new C05701();
        this.mBroadcastReceiver = new C05712();
        this.countdownTimer = new C05723(Util.MILLSECONDS_OF_MINUTE, 1000);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.findpwd);
        this.context = this;
        createView();
    }

    private void createView() {
        registerBoradcastReceiver();
        this.language = Files.getLanguage();
        String country = Files.getCountry();
        if (this.language.equals("zh") && country.equalsIgnoreCase("cn")) {
            this.language = "zh-cn";
        } else if (this.language.equalsIgnoreCase("zh") && country.equalsIgnoreCase("hk")) {
            this.language = "zh-hk";
        } else if (this.language.equalsIgnoreCase("zh") && country.equalsIgnoreCase("tw")) {
            this.language = "zh-tw";
        }
        this.backBtn = (Button) findViewById(C0136R.id.returnBtn);
        this.backBtn.setOnClickListener(new C05734());
        this.btn_sendVerifyCode = (Button) findViewById(C0136R.id.send_verycode);
        this.btn_sendVerifyCode.setOnClickListener(new C05745());
        this.menuBtn = (Button) findViewById(C0136R.id.menuBtn);
        this.SN = MySharedPreferences.getStringValue(this, Constants.SERIALNO);
        this.findPTV = (TextView) findViewById(C0136R.id.findpwd_ed);
        this.newPswEd = (EditText) findViewById(C0136R.id.new_pwd);
        this.comPswEd = (EditText) findViewById(C0136R.id.new_pwd_affirm);
        this.verifyCodeEd = (EditText) findViewById(C0136R.id.verification_code);
        if (!(this.SN.equalsIgnoreCase(XmlPullParser.NO_NAMESPACE) || this.SN.equalsIgnoreCase("null"))) {
            this.findPTV.setText(this.SN);
        }
        this.findButton = (Button) findViewById(C0136R.id.find_btn);
        this.findButton.setOnClickListener(new C05756());
        this.find_mail = (Button) findViewById(C0136R.id.find_mail);
        this.find_mail.setOnClickListener(new C05767());
    }

    public void checkPrama() {
        if (Common.isNetworkAvailable(this.context)) {
            this.SN = this.findPTV.getText().toString();
            this.newPsw = this.newPswEd.getText().toString();
            this.comfirPsw = this.comPswEd.getText().toString();
            this.verifyCode = this.verifyCodeEd.getText().toString();
            if (this.SN.trim().equals(XmlPullParser.NO_NAMESPACE) || this.SN.trim() == null) {
                Toast.makeText(this.context, this.context.getResources().getString(C0136R.string.input_sn), 0).show();
                return;
            } else if (TextUtils.isEmpty(this.verifyCode)) {
                Toast.makeText(this.context, C0136R.string.mine_verification_code_prompt, 0).show();
                return;
            } else if (!Tools.isNumeric(this.verifyCode) || this.verifyCode.length() != 4) {
                Toast.makeText(this.context, C0136R.string.reset_password_identificode_error, 0).show();
                return;
            } else if (this.newPsw.length() <= 0) {
                Toast.makeText(this.context, getResources().getString(C0136R.string.newPw), 1).show();
                return;
            } else if (TextUtils.isEmpty(this.newPsw)) {
                Toast.makeText(this.context, C0136R.string.newPw, 1).show();
                return;
            } else {
                Pattern pat = Pattern.compile("^[a-zA-Z0-9_@]+$");
                if (!pat.matcher(this.newPsw).matches()) {
                    Toast.makeText(this.context, C0136R.string.reg_pwd_wrong, 1).show();
                    return;
                } else if (this.newPsw.length() < 6 || this.newPsw.length() > 20) {
                    Toast.makeText(this.context, getResources().getString(C0136R.string.reg_user_pwd), 1).show();
                    return;
                } else if (XmlPullParser.NO_NAMESPACE.equalsIgnoreCase(this.comfirPsw)) {
                    Toast.makeText(this.context, C0136R.string.newPwAgain, 1).show();
                    return;
                } else if (!pat.matcher(this.comfirPsw).matches()) {
                    Toast.makeText(this.context, C0136R.string.reg_pwd_wrong, 1).show();
                    return;
                } else if (this.comfirPsw.length() < 6 || this.comfirPsw.length() > 20) {
                    Toast.makeText(this.context, getResources().getString(C0136R.string.reg_user_pwd), 1).show();
                    return;
                } else if (this.newPsw.equals(this.comfirPsw)) {
                    new findPwdAsy().execute(new String[0]);
                    return;
                } else {
                    Toast.makeText(this.context, getResources().getString(C0136R.string.modi_pwd_inconfor), 1).show();
                    return;
                }
            }
        }
        Toast.makeText(this.context, getResources().getString(C0136R.string.network), 1).show();
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

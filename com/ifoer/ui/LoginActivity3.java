package com.ifoer.ui;

import CRP.utils.CRPTools;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.car.result.GetEndUserFullResult;
import com.car.result.LoginResult;
import com.car.result.WSResult;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.db.DBDao;
import com.ifoer.entity.Constant;
import com.ifoer.entity.UserInfoResult;
import com.ifoer.expedition.client.NotificationService;
import com.ifoer.expedition.client.ServiceManager;
import com.ifoer.expeditionphone.ComRegActivity;
import com.ifoer.expeditionphone.FindPwdActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.expeditionphone.RegRuleActivity;
import com.ifoer.mine.Contact;
import com.ifoer.util.Common;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.MySoftUpdate;
import com.ifoer.util.SimpleDialog;
import com.ifoer.util.XmppTool;
import com.ifoer.webservice.BillService;
import com.ifoer.webservice.UserServiceClient;
import com.ifoer.webservice.WebServiceClient;
import com.launch.service.BundleBuilder;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import com.tencent.mm.sdk.plugin.BaseProfile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.http.util.EncodingUtils;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class LoginActivity3 extends Activity {
    private static final boolean f1300D = true;
    public static LoginActivity3 loginActivity2;
    private String ChatPasw;
    private Button btn_regist;
    private String cc;
    private WebServiceClient clientService;
    private Context context;
    private EditText editAc;
    private boolean hasChange;
    private long lastTime;
    List<String> listpaykey;
    private Button login;
    private String loginKey;
    private EditText loginPwd;
    private LoginResult loginResult;
    private String loginpwdText;
    private final Handler mHandler;
    private String mobileAppVersion;
    public IntentFilter myIntentFilter;
    private boolean needWrite;
    private String paykey;
    ProgressDialog progressDialogs;
    private TextView pwdreset;
    private String readName;
    private String readPsw;
    public mBroadcastReceiver receiver;
    private CheckBox remberPwd;
    private String sN;
    private String token;

    /* renamed from: com.ifoer.ui.LoginActivity3.1 */
    class C06981 extends Handler {
        C06981() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    if (LoginActivity3.this.progressDialogs != null && LoginActivity3.this.progressDialogs.isShowing()) {
                        LoginActivity3.this.progressDialogs.dismiss();
                    }
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.timeout, 1).show();
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    if (LoginActivity3.this.progressDialogs != null && LoginActivity3.this.progressDialogs.isShowing()) {
                        LoginActivity3.this.progressDialogs.dismiss();
                    }
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.the_service_side_abnormal, 1).show();
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    LoginActivity3.this.editAc.setText(MySharedPreferences.getStringValue(LoginActivity3.this.context, BaseProfile.COL_USERNAME));
                    LoginActivity3.this.loginPwd.setText(MySharedPreferences.getStringValue(LoginActivity3.this.context, "password"));
                case Constant.ERROR_NETWORK /*803*/:
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.ERROR_NETWORK, 0).show();
                case Constant.ERROR_CODE /*805*/:
                    if (msg.getData() == null || msg.getData().getString(BundleBuilder.AskFromMessage) == null || msg.getData().getString(BundleBuilder.AskFromMessage).equals(XmlPullParser.NO_NAMESPACE)) {
                        Toast.makeText(LoginActivity3.this.context, LoginActivity3.this.getString(C0136R.string.error_server), 0).show();
                    } else {
                        Toast.makeText(LoginActivity3.this.context, msg.getData().getString(BundleBuilder.AskFromMessage), 0).show();
                    }
                default:
            }
        }
    }

    /* renamed from: com.ifoer.ui.LoginActivity3.2 */
    class C06992 implements OnClickListener {
        C06992() {
        }

        public void onClick(View arg0) {
            LoginActivity3.this.toRegister();
        }
    }

    /* renamed from: com.ifoer.ui.LoginActivity3.3 */
    class C07003 implements OnClickListener {
        C07003() {
        }

        public void onClick(View v) {
            LoginActivity3.this.hideKeyboard();
            LoginActivity3.this.startActivity(new Intent(LoginActivity3.this, FindPwdActivity.class));
            LoginActivity3.this.finish();
            LoginActivity3.this.overridePendingTransition(0, 0);
        }
    }

    /* renamed from: com.ifoer.ui.LoginActivity3.4 */
    class C07014 implements OnClickListener {
        C07014() {
        }

        public void onClick(View v) {
            boolean isChecked = LoginActivity3.this.remberPwd.isChecked();
            MySharedPreferences.setBoolean(LoginActivity3.this.getApplicationContext(), MySharedPreferences.IfSaveNamePswKey, isChecked);
            LoginActivity3.this.loginKey = LoginActivity3.this.editAc.getText().toString();
            LoginActivity3.this.loginpwdText = LoginActivity3.this.loginPwd.getText().toString();
            if (LoginActivity3.this.loginKey.equals(LoginActivity3.this.readName) && !LoginActivity3.this.loginpwdText.equals(LoginActivity3.this.readPsw)) {
                LoginActivity3.this.hasChange = LoginActivity3.f1300D;
            }
            if (!isChecked) {
                MySharedPreferences.setString(LoginActivity3.this.getApplicationContext(), BaseProfile.COL_USERNAME, LoginActivity3.this.loginKey);
                MySharedPreferences.setString(LoginActivity3.this.getApplicationContext(), "password", null);
            }
            LoginActivity3.this.hideKeyboard();
            LoginActivity3.this.toLogin();
        }
    }

    public class AsyTask extends AsyncTask<Void, Void, String> {
        UserInfoResult logReg;

        public AsyTask() {
            this.logReg = null;
        }

        protected String doInBackground(Void... params) {
            LoginActivity3.this.clientService = new WebServiceClient();
            try {
                this.logReg = LoginActivity3.this.clientService.getUserInfoBySerialNo(LoginActivity3.this.sN, LoginActivity3.this.context);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (this.logReg == null) {
                return;
            }
            if (this.logReg.getCode() == 0) {
                Toast.makeText(LoginActivity3.this, LoginActivity3.this.getString(C0136R.string.registed), 0).show();
                MySharedPreferences.setString(LoginActivity3.this, MySharedPreferences.isSerialPortIDReg, "yes");
            } else if (this.logReg.getCode() == MyHttpException.ERROR_PARAMETER_EMPTY) {
                Toast.makeText(LoginActivity3.this, LoginActivity3.this.getString(C0136R.string.notic_null), 0).show();
            } else if (this.logReg.getCode() == MyHttpException.ERROR_SERIAL_NOEXIST) {
                Toast.makeText(LoginActivity3.this, LoginActivity3.this.getString(C0136R.string.check_file), 0).show();
            } else if (this.logReg.getCode() == MyHttpException.ERROR_NOT_SELL) {
                Toast.makeText(LoginActivity3.this, LoginActivity3.this.getString(C0136R.string.no_purchase), 0).show();
            } else if (this.logReg.getCode() == MyHttpException.ERROR_NULLIFY) {
                Toast.makeText(LoginActivity3.this, LoginActivity3.this.getString(C0136R.string.obsolete), 0).show();
            } else if (this.logReg.getCode() != MyHttpException.ERROR_UNREGISTER) {
            } else {
                if (Locale.getDefault().getLanguage().equalsIgnoreCase(LocaleUtil.JAPANESE)) {
                    LoginActivity3.this.startActivityForResult(new Intent(LoginActivity3.this, RegRuleActivity.class).putExtra("isShow", LoginActivity3.f1300D), 11);
                    return;
                }
                LoginActivity3.this.startActivityForResult(new Intent(LoginActivity3.this, ComRegActivity.class), 11);
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    class EndUserFullAsy extends AsyncTask<String, String, String> {
        GetEndUserFullResult full;

        EndUserFullAsy() {
            this.full = null;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            LoginActivity3.this.login.setClickable(false);
        }

        protected String doInBackground(String... params) {
            UserServiceClient client2 = new UserServiceClient();
            client2.setCc(LoginActivity3.this.cc);
            client2.setToken(LoginActivity3.this.token);
            try {
                this.full = client2.getEndUserFullOne(LoginActivity3.this.cc);
            } catch (SocketTimeoutException e) {
                LoginActivity3.this.mHandler.obtainMessage(0).sendToTarget();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Intent intent;
            if (this.full == null) {
                intent = new Intent();
                intent.putExtra(MainMenuActivity.IfShowDialog, 1);
                LoginActivity3.this.setResult(10, intent);
                LoginActivity3.this.sendMsg();
                LoginActivity3.this.finish();
                LoginActivity3.this.overridePendingTransition(0, 0);
            } else if (this.full.getAddress() == null) {
                intent = new Intent();
                intent.putExtra(MainMenuActivity.IfShowDialog, 1);
                LoginActivity3.this.setResult(10, intent);
                LoginActivity3.this.sendMsg();
                LoginActivity3.this.finish();
                LoginActivity3.this.overridePendingTransition(0, 0);
            } else {
                intent = new Intent();
                intent.putExtra(MainMenuActivity.IfShowDialog, 1);
                LoginActivity3.this.setResult(10, intent);
                LoginActivity3.this.finish();
                LoginActivity3.this.sendMsg();
                LoginActivity3.this.overridePendingTransition(0, 0);
            }
        }
    }

    class GetPayKey implements Runnable {
        GetPayKey() {
        }

        public void run() {
            String path = Constant.DST_FILE + "paypal_paykey.txt";
            if (new File(path).exists()) {
                String[] payKeyAll = LoginActivity3.this.readFileSdcard(path).split("=");
                for (String split : payKeyAll) {
                    split.split(";");
                }
            }
        }
    }

    class GetPaypalInfoTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialogs;
        WSResult wSResult;

        GetPaypalInfoTask() {
            this.wSResult = new WSResult();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialogs = new ProgressDialog(LoginActivity3.this.context);
            this.progressDialogs.setMessage(LoginActivity3.this.getResources().getString(C0136R.string.find_wait));
            this.progressDialogs.setCancelable(false);
            this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            BillService softService = new BillService();
            String cc = MySharedPreferences.getStringValue(LoginActivity3.this.context, MySharedPreferences.CCKey);
            String token = MySharedPreferences.getStringValue(LoginActivity3.this.context, MySharedPreferences.TokenKey);
            softService.setCc(cc);
            softService.setToken(token);
            try {
                this.wSResult = softService.checkMobilePaypalPayment(LoginActivity3.this.paykey);
            } catch (Exception e) {
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (this.wSResult != null) {
                this.progressDialogs.dismiss();
                if (this.wSResult.getCode() == -1) {
                    SimpleDialog.validTokenDialog(LoginActivity3.this.context);
                    return;
                } else if (this.wSResult.getCode() == 0) {
                    DBDao.getInstance(LoginActivity3.this.context).updatePayKey(LoginActivity3.this.paykey, MainActivity.database);
                    return;
                } else if (this.wSResult.getCode() == MyHttpException.ERROR_PARAMETER_EMPTY) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.notic_null, 0).show();
                    return;
                } else if (this.wSResult.getCode() == MyHttpException.ERROR_UNREGISTER) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.ERROR_UNREGISTER_PRODUCT, 0).show();
                    return;
                } else if (this.wSResult.getCode() == MyHttpException.ERROR_SERVER) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.the_service_side_abnormal, 0).show();
                    return;
                } else if (this.wSResult.getCode() == MyHttpException.IDIAG_PACKAGE_EXISTS) {
                    DBDao.getInstance(LoginActivity3.this.context).updatePayKey(LoginActivity3.this.paykey, MainActivity.database);
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.Package_has_to_buy, 0).show();
                    return;
                } else if (this.wSResult.getCode() == MyHttpException.ERROR_UNREGISTER_PRODUCT) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.io_exception, 0).show();
                    return;
                } else if (this.wSResult.getCode() == MyHttpException.ERROR_USER_PRODUCT_MISMATCHING) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.not_match_the_product, 0).show();
                    return;
                } else if (this.wSResult.getCode() == MyHttpException.ERROR_UNLOGIN_USER) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.User_didnt_log_in, 0).show();
                    return;
                } else if (this.wSResult.getCode() == MyHttpException.ERROR_NOEXIST_PACKAGE) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.package_null, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 774) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.Serial_numbe_is_empty, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 775) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.Select_software_number_is_greater, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 776) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.software_in_the_package, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 778) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.requesting_data_configuration_errors, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 779) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.data_is_used_in_invalid_credentials, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 780) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.Http_request_data_anomalies, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 781) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.response_when_the_request_data, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 782) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.When_the_requested_data_CLIENT_ACTION_REQUIRED, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 783) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.request_data_lack_of_credentials, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 784) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.request_data_authentication_anomalies, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 785) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.Request_to_interrupt, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 786) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.In_response_to_failure, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 787) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.PAYPAL_TRANSACTION_NOT_COMPLETE, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 788) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.PAYPAL_PRICE_ERROR, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 789) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.PAYPAL_ORDER_NULL, 0).show();
                    return;
                } else if (this.wSResult.getCode() == 790) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.pay_success, 0).show();
                    DBDao.getInstance(LoginActivity3.this.context).updatePayKey(LoginActivity3.this.paykey, MainActivity.database);
                    return;
                } else if (this.wSResult.getCode() == 791) {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.PAYPAL_CURRENCY_CODE_ERROR, 0).show();
                    return;
                } else {
                    return;
                }
            }
            this.progressDialogs.dismiss();
        }
    }

    class LoginAsy extends AsyncTask<String, String, String> {
        LoginAsy() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            LoginActivity3.this.login.setClickable(false);
            LoginActivity3.this.progressDialogs = new ProgressDialog(LoginActivity3.this);
            LoginActivity3.this.progressDialogs.setMessage(LoginActivity3.this.getResources().getString(C0136R.string.login_wait));
            LoginActivity3.this.progressDialogs.setCancelable(false);
            LoginActivity3.this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            LoginActivity3.this.clientService = new WebServiceClient();
            try {
                LoginActivity3.this.loginResult = LoginActivity3.this.clientService.userLogin(LoginActivity3.this.context, LoginActivity3.this.loginKey, LoginActivity3.this.mobileAppVersion, LoginActivity3.this.loginpwdText);
            } catch (SocketTimeoutException e) {
                LoginActivity3.this.mHandler.obtainMessage(0).sendToTarget();
            } catch (NullPointerException e2) {
                LoginActivity3.this.mHandler.obtainMessage(1).sendToTarget();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            LoginActivity3.this.login.setClickable(LoginActivity3.f1300D);
            if (LoginActivity3.this.loginResult == null) {
                LoginActivity3.this.progressDialogs.dismiss();
                LoginActivity3.this.mHandler.obtainMessage(Constant.ERROR_NETWORK).sendToTarget();
            } else if (LoginActivity3.this.loginResult.getCode() != 0 || LoginActivity3.this.loginResult.getCc() == null || LoginActivity3.this.loginResult.getCc().equals(XmlPullParser.NO_NAMESPACE)) {
                MySharedPreferences.setString(LoginActivity3.this.context, MySharedPreferences.CCKey, XmlPullParser.NO_NAMESPACE);
                if (LoginActivity3.this.loginResult.getCode() == 100001) {
                    Toast.makeText(LoginActivity3.this, LoginActivity3.this.getResources().getString(C0136R.string.login_pwd_wrong), 1).show();
                    LoginActivity3.this.progressDialogs.dismiss();
                } else if (LoginActivity3.this.loginResult.getCode() == 100002) {
                    Toast.makeText(LoginActivity3.this, LoginActivity3.this.getResources().getString(C0136R.string.user_null), 1).show();
                    LoginActivity3.this.progressDialogs.dismiss();
                } else if (LoginActivity3.this.loginResult.getCode() == MyHttpException.ERROR_USER_STATE) {
                    Toast.makeText(LoginActivity3.this, LoginActivity3.this.getResources().getString(C0136R.string.login_user_throws), 1).show();
                    LoginActivity3.this.progressDialogs.dismiss();
                } else {
                    Toast.makeText(LoginActivity3.this.context, C0136R.string.notic_serv, 1).show();
                }
            } else {
                LoginActivity3.this.hideKeyboard();
                NotificationService.startActivity = Boolean.valueOf(LoginActivity3.f1300D);
                if (MainActivity.serviceManager != null) {
                    MainActivity.serviceManager.stopService();
                }
                MainActivity.serviceManager = new ServiceManager(LoginActivity3.this);
                MainActivity.serviceManager.setNotificationIcon(C0136R.drawable.notification);
                MainActivity.serviceManager.startService();
                LoginActivity3.this.cc = LoginActivity3.this.loginResult.getCc();
                LoginActivity3.this.token = LoginActivity3.this.loginResult.getToken();
                MySharedPreferences.setString(LoginActivity3.this.getApplicationContext(), MySharedPreferences.CCKey, LoginActivity3.this.cc);
                MySharedPreferences.setString(LoginActivity3.this.getApplicationContext(), MySharedPreferences.TokenKey, LoginActivity3.this.token);
                MySharedPreferences.setString(LoginActivity3.this, MySharedPreferences.UserNameKey, LoginActivity3.this.loginResult.getUserName());
                String serialNum = MySharedPreferences.getStringValue(LoginActivity3.this.context, MySharedPreferences.serialNoKey);
                if (!(serialNum == null || XmlPullParser.NO_NAMESPACE.equals(serialNum))) {
                    new MySoftUpdate(LoginActivity3.this.context).checkUpdateAsync();
                }
                MySharedPreferences.setString(LoginActivity3.this.getApplicationContext(), "loginKey", LoginActivity3.this.editAc.getText().toString());
                MySharedPreferences.setString(LoginActivity3.this.getApplicationContext(), "loginpassword", LoginActivity3.this.loginPwd.getText().toString());
                if (LoginActivity3.this.remberPwd.isChecked()) {
                    MySharedPreferences.setString(LoginActivity3.this.getApplicationContext(), BaseProfile.COL_USERNAME, LoginActivity3.this.editAc.getText().toString());
                    MySharedPreferences.setString(LoginActivity3.this.getApplicationContext(), "password", LoginActivity3.this.loginPwd.getText().toString());
                    MySharedPreferences.setBoolean(LoginActivity3.this.context, MySharedPreferences.IfSaveNamePswKey, LoginActivity3.f1300D);
                } else {
                    MySharedPreferences.setString(LoginActivity3.this.getApplicationContext(), BaseProfile.COL_USERNAME, XmlPullParser.NO_NAMESPACE);
                    MySharedPreferences.setString(LoginActivity3.this.getApplicationContext(), "password", XmlPullParser.NO_NAMESPACE);
                }
                if (LoginActivity3.this.hasChange || LoginActivity3.this.needWrite) {
                    LoginActivity3.this.writeFileData(LoginActivity3.this.editAc.getText().toString(), LoginActivity3.this.loginPwd.getText().toString(), LoginActivity3.this.loginResult.getEmail());
                }
                MySharedPreferences.setString(LoginActivity3.this.getApplicationContext(), MySharedPreferences.UserNameKey, LoginActivity3.this.loginKey);
                Toast.makeText(LoginActivity3.this, LoginActivity3.this.getResources().getString(C0136R.string.login_success), 1).show();
                MySharedPreferences.setString(LoginActivity3.this.context, "openshowuser", Contact.RELATION_FRIEND);
                MySharedPreferences.setString(LoginActivity3.this.getApplicationContext(), "usernames", LoginActivity3.this.editAc.getText().toString());
                LoginActivity3.this.listpaykey = DBDao.getInstance(LoginActivity3.this.context).queryPayKey(LoginActivity3.this.cc, MainActivity.database);
                if (LoginActivity3.this.listpaykey.size() > 0) {
                    for (int i = 0; i < LoginActivity3.this.listpaykey.size(); i++) {
                        LoginActivity3.this.paykey = (String) LoginActivity3.this.listpaykey.get(i);
                        if (Common.isNetworkAvailable(LoginActivity3.this)) {
                            new GetPaypalInfoTask().execute(new String[0]);
                        } else {
                            Toast.makeText(LoginActivity3.this, C0136R.string.network_exception, 0).show();
                        }
                    }
                }
                new EndUserFullAsy().execute(new String[0]);
                LoginActivity3.this.progressDialogs.dismiss();
            }
        }
    }

    class ReaadPaykey implements Runnable {
        String paths;

        ReaadPaykey() {
            this.paths = Constant.PAYPAL_PATH + "paypal_paykey.txt";
        }

        public void run() {
            if (new File(this.paths).exists()) {
                String ss = LoginActivity3.this.readFileSdcard(this.paths);
                StringBuffer sbs = new StringBuffer();
                String[] payKeyAll = ss.split("=");
                for (String split : payKeyAll) {
                    String[] payKey = split.split(";");
                    DBDao.getInstance(LoginActivity3.this.context).addPayKey(payKey[0].trim(), payKey[1], payKey[2], payKey[3], payKey[4], MainActivity.database);
                }
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - LoginActivity3.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                LoginActivity3.this.lastTime = System.currentTimeMillis();
            }
        }
    }

    public LoginActivity3() {
        this.context = this;
        this.clientService = null;
        this.loginKey = null;
        this.mobileAppVersion = Constant.APP_VERSION;
        this.loginpwdText = null;
        this.paykey = null;
        this.ChatPasw = "000000";
        this.listpaykey = new ArrayList();
        this.hasChange = false;
        this.needWrite = f1300D;
        this.mHandler = new C06981();
    }

    static {
        loginActivity2 = null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.login_pop3);
        creatView();
        loginActivity2 = this;
        registerBoradcastReceiver();
    }

    private void creatView() {
        this.editAc = (EditText) findViewById(C0136R.id.name);
        this.loginPwd = (EditText) findViewById(C0136R.id.password);
        this.loginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.remberPwd = (CheckBox) findViewById(C0136R.id.check);
        this.remberPwd.setChecked(f1300D);
        this.pwdreset = (TextView) findViewById(C0136R.id.pwdreset);
        this.btn_regist = (Button) findViewById(C0136R.id.regist_btn);
        String cc = getIntent().getStringExtra(MultipleAddresses.CC);
        if (cc == null || cc.equals(XmlPullParser.NO_NAMESPACE)) {
            boolean isChecked = MySharedPreferences.getBooleanValue(this, MySharedPreferences.IfSaveNamePswKey, false);
            readProfileTxt(this.context);
            if (isChecked) {
                this.editAc.setText(MySharedPreferences.getStringValue(this.context, BaseProfile.COL_USERNAME));
                this.loginPwd.setText(MySharedPreferences.getStringValue(this.context, "password"));
            }
        } else {
            this.editAc.setText(cc);
            this.editAc.setEnabled(false);
            this.btn_regist.setVisibility(8);
        }
        String first = MySharedPreferences.getStringValue(this, MySharedPreferences.first);
        if (first == null || first.equalsIgnoreCase(Contact.RELATION_ASK)) {
            new Thread(new ReaadPaykey()).start();
        }
        MySharedPreferences.setString(getApplicationContext(), "firstLogin", Contact.RELATION_BACKNAME);
        this.btn_regist.setOnClickListener(new C06992());
        this.pwdreset.setOnClickListener(new C07003());
        this.login = (Button) findViewById(C0136R.id.login_btn);
        this.login.setOnClickListener(new C07014());
    }

    protected void onResume() {
        super.onResume();
    }

    private void toLogin() {
        Pattern pat = Pattern.compile("^[a-zA-Z0-9_@]+$");
        if (TextUtils.isEmpty(this.loginKey)) {
            Toast.makeText(this, getResources().getString(C0136R.string.login_null), 0).show();
        } else if (TextUtils.isEmpty(this.loginpwdText)) {
            Toast.makeText(this, getResources().getString(C0136R.string.pwd_not_null), 0).show();
        } else {
            MySharedPreferences.setString(this, MySharedPreferences.UserNameKey, this.loginKey);
            if (this.remberPwd.isChecked()) {
                MySharedPreferences.setBoolean(this.context, MySharedPreferences.iflogout, false);
                MySharedPreferences.setString(this, MySharedPreferences.UserPswKey, this.loginpwdText);
            }
            if (Common.isNetworkAvailable(this.context)) {
                new LoginAsy().execute(new String[0]);
            } else {
                Toast.makeText(this.context, getResources().getString(C0136R.string.network), 0).show();
            }
        }
    }

    private void toRegister() {
        if (CRPTools.readSnTxt(this)) {
            this.sN = MySharedPreferences.getStringValue(this, Constants.SERIALNO);
            if (Common.isNetworkAvailable(this.context)) {
                new AsyTask().execute(new Void[0]);
                return;
            } else {
                Toast.makeText(this.context, getResources().getString(C0136R.string.network), 0).show();
                return;
            }
        }
        Toast.makeText(this.context, getString(C0136R.string.check_file), 1).show();
    }

    private void RegChatAct() {
        XMPPConnection conn;
        try {
            conn = XmppTool.getConnection();
            if (conn != null) {
                Registration reg = new Registration();
                reg.setType(Type.SET);
                reg.setTo(conn.getServiceName());
                reg.setUsername(this.cc);
                reg.setPassword(this.ChatPasw);
                reg.addAttribute("android", "geolo_createUser_android");
                PacketCollector collector = conn.createPacketCollector(new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class)));
                conn.sendPacket(reg);
                IQ result = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
                collector.cancel();
                if (!(result == null || result.getType() == Type.ERROR)) {
                    result.getType();
                    Type type = Type.RESULT;
                }
            }
        } catch (XMPPException e) {
        } catch (IllegalStateException e2) {
            e2.printStackTrace();
        }
        try {
            conn = XmppTool.getConnection();
            if (conn != null) {
                conn.login(this.cc, this.ChatPasw);
            }
        } catch (XMPPException e3) {
            e3.printStackTrace();
        } catch (IllegalStateException e22) {
            e22.printStackTrace();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent;
        if (keyCode == 4) {
            intent = new Intent();
            intent.putExtra(MainMenuActivity.IfShowDialog, 1);
            setResult(10, intent);
            finish();
            overridePendingTransition(0, 0);
            MySharedPreferences.setString(this.context, "openshowuser", Contact.RELATION_ASK);
            this.context.sendBroadcast(new Intent("Nushow_names"));
            finish();
            return f1300D;
        } else if (keyCode != Service.SUNRPC) {
            return super.onKeyDown(keyCode, event);
        } else {
            intent = new Intent();
            intent.putExtra(MainMenuActivity.IfShowDialog, 1);
            setResult(10, intent);
            finish();
            overridePendingTransition(0, 0);
            MySharedPreferences.setString(this.context, "openshowuser", Contact.RELATION_ASK);
            this.context.sendBroadcast(new Intent("Nushow_names"));
            finish();
            return f1300D;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                Intent intent;
                if (resultCode == 10) {
                    intent = new Intent();
                    intent.putExtra(MainMenuActivity.IfShowDialog, 1);
                    setResult(10, intent);
                    finish();
                    overridePendingTransition(0, 0);
                } else if (resultCode == 12) {
                    intent = new Intent();
                    intent.putExtra(MainMenuActivity.IfShowDialog, 1);
                    setResult(12, intent);
                    finish();
                    overridePendingTransition(0, 0);
                } else if (resultCode == 20 || resultCode == 21) {
                    if (data.getIntExtra("close_click", 0) == 1) {
                        finish();
                        overridePendingTransition(0, 0);
                    }
                } else if (resultCode == 13) {
                    readProfileTxt(this.context);
                }
            default:
        }
    }

    public void sendMsg() {
        sendBroadcast(new Intent("Show_names"));
    }

    private void hideKeyboard() {
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.loginPwd.getWindowToken(), 0);
    }

    public String readFileSdcard(String fileName) {
        String res = XmlPullParser.NO_NAMESPACE;
        try {
            FileInputStream fin = new FileInputStream(fileName);
            byte[] buffer = new byte[fin.available()];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, AsyncHttpResponseHandler.DEFAULT_CHARSET);
            fin.close();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
    }

    public void readProfileTxt(Context mContexts) {
        String name1 = MySharedPreferences.getStringValue(this.context, BaseProfile.COL_USERNAME);
        String psw2 = MySharedPreferences.getStringValue(this.context, "password");
        if (name1 == null || psw2 == null || name1.equals(XmlPullParser.NO_NAMESPACE) || psw2.equals(XmlPullParser.NO_NAMESPACE)) {
            String filePath = Environment.getExternalStorageDirectory() + "/cnlaunch/profile.txt";
            String res = XmlPullParser.NO_NAMESPACE;
            if (new File(filePath).exists()) {
                try {
                    FileInputStream fin = new FileInputStream(filePath);
                    byte[] buffer = new byte[fin.available()];
                    fin.read(buffer);
                    res = EncodingUtils.getString(buffer, AsyncHttpResponseHandler.DEFAULT_CHARSET);
                    fin.close();
                    String[] sn = res.split(AlixDefine.split);
                    String name = sn[0];
                    String psw = sn[1];
                    this.readName = name;
                    this.readPsw = psw;
                    MySharedPreferences.setString(mContexts, BaseProfile.COL_USERNAME, name);
                    MySharedPreferences.setString(mContexts, "password", psw);
                    this.mHandler.obtainMessage(2).sendToTarget();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            return;
        }
        this.editAc.setText(name1);
        this.loginPwd.setText(psw2);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.receiver != null) {
            unregisterReceiver(this.receiver);
        }
    }

    public void registerBoradcastReceiver() {
        this.receiver = new mBroadcastReceiver();
        this.myIntentFilter = new IntentFilter();
        this.myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(this.receiver, this.myIntentFilter);
    }

    public boolean writeFileData(String userName, String psw, String email) {
        File file = new File(Environment.getExternalStorageDirectory() + "/cnlaunch/profile.txt");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(new StringBuilder(String.valueOf(userName)).append(AlixDefine.split).append(psw).append(AlixDefine.split).append(email).toString().getBytes());
            fout.close();
            return f1300D;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

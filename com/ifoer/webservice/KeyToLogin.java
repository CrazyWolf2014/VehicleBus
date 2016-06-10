package com.ifoer.webservice;

import CRP.utils.CRPTools;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.car.result.LoginResult;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.cnlaunch.x431pro.common.RequestCode;
import com.ifoer.entity.Constant;
import com.ifoer.entity.UserInfoResult;
import com.ifoer.entity.UserSerial;
import com.ifoer.expedition.client.ServiceManager;
import com.ifoer.expeditionphone.ComRegActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.mine.Contact;
import com.ifoer.ui.LoginActivity3;
import com.ifoer.util.Common;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.NetPOSPrinter;
import com.launch.service.BundleBuilder;
import com.tencent.mm.sdk.plugin.BaseProfile;
import java.io.File;
import java.io.FileInputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.util.EncodingUtils;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xmlpull.v1.XmlPullParser;

public class KeyToLogin {
    private String ID;
    private String SN;
    private String cc;
    private WebServiceClient clientService;
    private Context context;
    private boolean hasProfile;
    List<String> listpaykey;
    private LoginResult loginResult;
    private Handler mHandler;
    private String mobileAppVersion;
    private String passWord;
    private String paykey;
    private ProgressDialog progressDialogs;
    private String token;
    private String userName;
    private UserInfoResult ws;
    private UserSerial wss;

    public class AsyTask extends AsyncTask<Void, Void, String> {
        UserInfoResult logReg;

        public AsyTask() {
            this.logReg = null;
        }

        protected String doInBackground(Void... params) {
            KeyToLogin.this.clientService = new WebServiceClient();
            try {
                this.logReg = KeyToLogin.this.clientService.getUserInfoBySerialNo(KeyToLogin.this.SN, KeyToLogin.this.context);
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
                if (KeyToLogin.this.progressDialogs != null) {
                    KeyToLogin.this.progressDialogs.dismiss();
                }
                Toast.makeText(KeyToLogin.this.context, KeyToLogin.this.context.getResources().getString(C0136R.string.ERROR_SERVER), 0).show();
            } else if (this.logReg.getCode() == 0) {
                MySharedPreferences.setString(KeyToLogin.this.context, MySharedPreferences.isSerialPortIDReg, "yes");
                MySharedPreferences.setString(KeyToLogin.this.context, MySharedPreferences.savecc, this.logReg.getUserName());
                KeyToLogin.this.userName = this.logReg.getUserName();
                KeyToLogin.this.passWord = KeyToLogin.this.ID;
                KeyToLogin.this.cc = this.logReg.getCc();
                if (KeyToLogin.this.readProfileTxt(KeyToLogin.this.context, KeyToLogin.this.cc)) {
                    new LoginAsy().execute(new String[0]);
                    return;
                }
                if (KeyToLogin.this.progressDialogs != null) {
                    KeyToLogin.this.progressDialogs.dismiss();
                }
                Intent intent = new Intent(KeyToLogin.this.context, LoginActivity3.class);
                intent.putExtra(MultipleAddresses.CC, KeyToLogin.this.cc);
                intent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                ((Activity) KeyToLogin.this.context).startActivityForResult(intent, 11);
            } else if (this.logReg.getCode() == MyHttpException.ERROR_PARAMETER_EMPTY) {
                if (KeyToLogin.this.progressDialogs != null) {
                    KeyToLogin.this.progressDialogs.dismiss();
                }
                Toast.makeText(KeyToLogin.this.context, KeyToLogin.this.context.getResources().getString(C0136R.string.notic_null), 0).show();
            } else if (this.logReg.getCode() == MyHttpException.ERROR_SERIAL_NOEXIST) {
                if (KeyToLogin.this.progressDialogs != null) {
                    KeyToLogin.this.progressDialogs.dismiss();
                }
                Toast.makeText(KeyToLogin.this.context, KeyToLogin.this.context.getResources().getString(C0136R.string.check_file), 0).show();
            } else if (this.logReg.getCode() == MyHttpException.ERROR_NOT_SELL) {
                if (KeyToLogin.this.progressDialogs != null) {
                    KeyToLogin.this.progressDialogs.dismiss();
                }
                Toast.makeText(KeyToLogin.this.context, KeyToLogin.this.context.getResources().getString(C0136R.string.no_purchase), 0).show();
            } else if (this.logReg.getCode() == MyHttpException.ERROR_NULLIFY) {
                if (KeyToLogin.this.progressDialogs != null) {
                    KeyToLogin.this.progressDialogs.dismiss();
                }
                Toast.makeText(KeyToLogin.this.context, KeyToLogin.this.context.getResources().getString(C0136R.string.obsolete), 0).show();
            } else if (this.logReg.getCode() == MyHttpException.ERROR_UNREGISTER) {
                if (KeyToLogin.this.progressDialogs != null) {
                    KeyToLogin.this.progressDialogs.dismiss();
                }
                Toast.makeText(KeyToLogin.this.context, KeyToLogin.this.context.getResources().getString(C0136R.string.ERROR_UNREGISTER_PRODUCT), 1).show();
                ((Activity) KeyToLogin.this.context).startActivityForResult(new Intent(KeyToLogin.this.context, ComRegActivity.class), 11);
            } else if (this.logReg.getCode() == MyHttpException.ERROR_SERVER) {
                if (KeyToLogin.this.progressDialogs != null) {
                    KeyToLogin.this.progressDialogs.dismiss();
                }
                Toast.makeText(KeyToLogin.this.context, KeyToLogin.this.context.getResources().getString(C0136R.string.ERROR_SERVER), 0).show();
            } else if (this.logReg.getCode() == Constant.ERROR_CODE) {
                if (KeyToLogin.this.progressDialogs != null) {
                    KeyToLogin.this.progressDialogs.dismiss();
                }
                if (this.logReg.getMessage() == null || this.logReg.getMessage().equals(XmlPullParser.NO_NAMESPACE)) {
                    KeyToLogin.this.mHandler.sendEmptyMessage(Constant.ERROR_CODE);
                    return;
                }
                Toast.makeText(KeyToLogin.this.context, this.logReg.getMessage(), 0).show();
                Bundle data = new Bundle();
                data.putString(BundleBuilder.AskFromMessage, this.logReg.getMessage());
                Message msg = new Message();
                msg.what = Constant.ERROR_CODE;
                msg.setData(data);
                KeyToLogin.this.mHandler.sendMessage(msg);
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            KeyToLogin.this.progressDialogs = new ProgressDialog(KeyToLogin.this.context);
            KeyToLogin.this.progressDialogs.setMessage(KeyToLogin.this.context.getResources().getString(C0136R.string.login_wait));
            KeyToLogin.this.progressDialogs.setCancelable(false);
            KeyToLogin.this.progressDialogs.show();
        }
    }

    class LoginAsy extends AsyncTask<String, String, String> {
        LoginAsy() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            KeyToLogin.this.clientService = new WebServiceClient();
            try {
                KeyToLogin.this.loginResult = KeyToLogin.this.clientService.userLogin(KeyToLogin.this.context, KeyToLogin.this.cc, KeyToLogin.this.mobileAppVersion, KeyToLogin.this.passWord);
            } catch (SocketTimeoutException e) {
                KeyToLogin.this.mHandler.obtainMessage(Constant.TIME_OUT).sendToTarget();
            } catch (NullPointerException e2) {
                Log.i("KeyToLogin", "NullPointerException");
                KeyToLogin.this.mHandler.obtainMessage(Constant.SETVICE_ABNORMAL).sendToTarget();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (KeyToLogin.this.progressDialogs != null) {
                KeyToLogin.this.progressDialogs.dismiss();
            }
            if (KeyToLogin.this.loginResult == null) {
                KeyToLogin.this.mHandler.obtainMessage(Constant.ERROR_NETWORK).sendToTarget();
            } else if (KeyToLogin.this.loginResult.getCode() != 0 || KeyToLogin.this.loginResult.getCc() == null || KeyToLogin.this.loginResult.getCc().equals(XmlPullParser.NO_NAMESPACE)) {
                MySharedPreferences.setString(KeyToLogin.this.context, MySharedPreferences.CCKey, XmlPullParser.NO_NAMESPACE);
                Intent intent;
                if (KeyToLogin.this.loginResult.getCode() == MyHttpException.ERROR_PASW || KeyToLogin.this.loginResult.getCode() == 100001) {
                    intent = new Intent(KeyToLogin.this.context, LoginActivity3.class);
                    intent.putExtra(MultipleAddresses.CC, KeyToLogin.this.cc);
                    intent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    ((Activity) KeyToLogin.this.context).startActivityForResult(intent, 11);
                } else if (KeyToLogin.this.loginResult.getCode() == NetPOSPrinter.PRINT_WIDTH || KeyToLogin.this.loginResult.getCode() == 100002) {
                    Toast.makeText(KeyToLogin.this.context, KeyToLogin.this.context.getResources().getString(C0136R.string.user_null), 1).show();
                    intent = new Intent(KeyToLogin.this.context, LoginActivity3.class);
                    intent.putExtra("userName", KeyToLogin.this.userName);
                    intent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    ((Activity) KeyToLogin.this.context).startActivityForResult(intent, 11);
                } else if (KeyToLogin.this.loginResult.getCode() == 40000) {
                    Toast.makeText(KeyToLogin.this.context, C0136R.string.login_fail_prompt_40000, 1).show();
                } else if (KeyToLogin.this.loginResult.getCode() == RequestCode.REQ_GET_TARGETDETAIL) {
                    Toast.makeText(KeyToLogin.this.context, C0136R.string.login_fail_prompt_40001, 1).show();
                } else if (KeyToLogin.this.loginResult.getCode() == MyHttpException.ERROR_USER_STATE) {
                    Toast.makeText(KeyToLogin.this.context, KeyToLogin.this.context.getResources().getString(C0136R.string.login_user_throws), 1).show();
                } else if (KeyToLogin.this.loginResult.getCode() != Constant.ERROR_CODE) {
                    Toast.makeText(KeyToLogin.this.context, C0136R.string.notic_serv, 1).show();
                } else if (KeyToLogin.this.loginResult.getMessage() == null || KeyToLogin.this.loginResult.getMessage().endsWith(XmlPullParser.NO_NAMESPACE)) {
                    KeyToLogin.this.mHandler.sendEmptyMessage(Constant.ERROR_CODE);
                } else {
                    Toast.makeText(KeyToLogin.this.context, KeyToLogin.this.loginResult.getMessage(), 1).show();
                    Bundle data = new Bundle();
                    data.putString(BundleBuilder.AskFromMessage, KeyToLogin.this.loginResult.getMessage());
                    Message msg = new Message();
                    msg.what = Constant.ERROR_CODE;
                    msg.setData(data);
                    KeyToLogin.this.mHandler.sendMessage(msg);
                }
            } else {
                if (MainActivity.serviceManager != null) {
                    MainActivity.serviceManager.stopService();
                }
                MainActivity.serviceManager = new ServiceManager(KeyToLogin.this.context);
                MainActivity.serviceManager.setNotificationIcon(C0136R.drawable.notification);
                MainActivity.serviceManager.startService();
                KeyToLogin.this.cc = KeyToLogin.this.loginResult.getCc();
                KeyToLogin.this.token = KeyToLogin.this.loginResult.getToken();
                MySharedPreferences.setString(KeyToLogin.this.context, MySharedPreferences.CCKey, KeyToLogin.this.loginResult.getCc());
                MySharedPreferences.setString(KeyToLogin.this.context, MySharedPreferences.TokenKey, KeyToLogin.this.token);
                MySharedPreferences.setString(KeyToLogin.this.context, "loginKey", KeyToLogin.this.loginResult.getCc());
                MySharedPreferences.setString(KeyToLogin.this.context, "loginpassword", KeyToLogin.this.passWord);
                MySharedPreferences.setString(KeyToLogin.this.context, BaseProfile.COL_USERNAME, KeyToLogin.this.loginResult.getUserName());
                MySharedPreferences.setString(KeyToLogin.this.context, "password", KeyToLogin.this.passWord);
                MySharedPreferences.setBoolean(KeyToLogin.this.context, MySharedPreferences.IfSaveNamePswKey, true);
                MySharedPreferences.setString(KeyToLogin.this.context, MySharedPreferences.UserNameKey, KeyToLogin.this.loginResult.getUserName());
                MySharedPreferences.setString(KeyToLogin.this.context, "openshowuser", Contact.RELATION_FRIEND);
                MySharedPreferences.setString(KeyToLogin.this.context, "usernames", KeyToLogin.this.loginResult.getUserName());
                Toast.makeText(KeyToLogin.this.context, KeyToLogin.this.context.getResources().getString(C0136R.string.login_success), 0).show();
                KeyToLogin.this.sendMsg();
                KeyToLogin.this.mHandler.obtainMessage(Constant.LOGIN_SUCCESS).sendToTarget();
                Log.i("KeyToLogin", "\u8868\u793a\u767b\u9646\u6210\u529f");
                Log.i("KeyToLogin", "\u8868\u793a\u767b\u9646\u6210\u529f token" + KeyToLogin.this.token);
            }
        }
    }

    public KeyToLogin(Context context, Handler handler, ProgressDialog progressDialogs) {
        this.mobileAppVersion = Constant.APP_VERSION;
        this.paykey = null;
        this.hasProfile = false;
        this.listpaykey = new ArrayList();
        this.context = context;
        this.mHandler = handler;
        if (progressDialogs == null) {
            this.progressDialogs = new ProgressDialog(context);
        } else {
            this.progressDialogs = progressDialogs;
        }
    }

    public boolean readProfileTxt(Context mContexts, String netcc) {
        String ccLocal = MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey);
        String psw2 = MySharedPreferences.getStringValue(this.context, "password");
        if (ccLocal == null || psw2 == null || ccLocal.equals(XmlPullParser.NO_NAMESPACE) || psw2.equals(XmlPullParser.NO_NAMESPACE)) {
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
                    String cc2 = sn[0];
                    String psw = sn[1];
                    this.cc = cc2;
                    this.passWord = psw;
                    MySharedPreferences.setString(mContexts, MySharedPreferences.CCKey, cc2);
                    MySharedPreferences.setString(mContexts, "password", psw);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            this.cc = ccLocal;
            this.passWord = psw2;
        }
        if (this.cc == null || netcc == null) {
            return false;
        }
        if (this.cc.equals(netcc)) {
            return true;
        }
        this.cc = netcc;
        return false;
    }

    public void login() {
        if (CRPTools.readSnTxt(this.context)) {
            this.SN = MySharedPreferences.getStringValue(this.context, Constants.SERIALNO);
            this.ID = MySharedPreferences.getStringValue(this.context, Constants.SERIALID);
            if (Common.isNetworkAvailable(this.context)) {
                new AsyTask().execute(new Void[0]);
                return;
            } else {
                Toast.makeText(this.context, this.context.getResources().getString(C0136R.string.network), 0).show();
                return;
            }
        }
        this.SN = MySharedPreferences.getStringValue(this.context, Constants.SERIALNO);
        this.ID = MySharedPreferences.getStringValue(this.context, Constants.SERIALID);
        if (TextUtils.isEmpty(this.SN) || TextUtils.isEmpty(this.ID)) {
            Toast.makeText(this.context, this.context.getResources().getString(C0136R.string.need_restart), 1).show();
        } else if (Common.isNetworkAvailable(this.context)) {
            new AsyTask().execute(new Void[0]);
        } else {
            Toast.makeText(this.context, this.context.getResources().getString(C0136R.string.network), 0).show();
        }
    }

    public void sendMsg() {
        this.context.sendBroadcast(new Intent("Show_names"));
    }
}

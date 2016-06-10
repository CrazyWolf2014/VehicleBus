package com.ifoer.expeditionphone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.car.result.LoginResult;
import com.car.result.RegisterResult;
import com.car.result.StateResult;
import com.car.result.WSResult;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.cnlaunch.x431pro.common.RequestCode;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.adapter.CountryInfoAdapter;
import com.ifoer.entity.Constant;
import com.ifoer.entity.CountryInfo;
import com.ifoer.entity.CountryListResult;
import com.ifoer.mine.Contact;
import com.ifoer.mine.model.BaseCode;
import com.ifoer.service.GetLocationService;
import com.ifoer.ui.MainMenuActivity;
import com.ifoer.util.Common;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.util.Tools;
import com.ifoer.view.DialogPopWindow;
import com.ifoer.webservice.ProductService;
import com.ifoer.webservice.WebServiceClient;
import com.launch.service.BundleBuilder;
import com.tencent.mm.sdk.plugin.BaseProfile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import org.apache.harmony.javax.security.auth.callback.ConfirmationCallback;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class ComRegActivity extends Activity {
    private ImageView btn_sendVerifyCode;
    protected LinearLayout car_maintain;
    private String cc;
    private WebServiceClient clientService;
    private Context context;
    private TextView countryName;
    private Drawable drawable;
    private EditText emailEditText;
    private EditText et_zip;
    private LayoutInflater inflater;
    private ArrayList<CountryInfo> infoList;
    private int intNationId;
    private long lastTime;
    private ListView list;
    private GetLocationService locationService;
    private LoginResult loginResult;
    private CountryInfoAdapter mAdapter;
    private final Handler mHandler;
    public Button menuBtn;
    private String mobileAppVersion;
    public IntentFilter myIntentFilter;
    private boolean needResendVerifyCode;
    private DialogPopWindow popWidow;
    private String portPsw;
    private ProgressDialog progressDialogs;
    public mBroadcastReceiver receiver;
    private String regAc;
    private String regEmail;
    private String regPwd;
    private Button registerBtn;
    private RegisterResult regrs;
    private StateResult res;
    private Button returnBtn;
    private TextView selectCountry;
    private String serialNo;
    private int time;
    private String token;
    private String url;
    private String username;
    private int usertypeId;
    private EditText verifyCodeEd;
    private String veryfiCode;
    private PopupWindow window;
    private WSResult wsResult;

    /* renamed from: com.ifoer.expeditionphone.ComRegActivity.1 */
    class C05141 extends Handler {

        /* renamed from: com.ifoer.expeditionphone.ComRegActivity.1.1 */
        class C05131 implements Runnable {
            C05131() {
            }

            public void run() {
                try {
                    ComRegActivity.this.drawable = Drawable.createFromStream(new URL(ComRegActivity.this.url).openStream(), null);
                    ComRegActivity.this.mHandler.sendEmptyMessage(Service.ISO_TSAP);
                } catch (MalformedURLException e) {
                    try {
                        e.printStackTrace();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }

        C05141() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    if (ComRegActivity.this.progressDialogs != null && ComRegActivity.this.progressDialogs.isShowing()) {
                        ComRegActivity.this.progressDialogs.dismiss();
                    }
                    Toast.makeText(ComRegActivity.this.context, C0136R.string.timeout, 0).show();
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    if (ComRegActivity.this.progressDialogs != null && ComRegActivity.this.progressDialogs.isShowing()) {
                        ComRegActivity.this.progressDialogs.dismiss();
                    }
                    Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.context.getResources().getString(C0136R.string.port_success), 1).show();
                    Intent intent = new Intent();
                    intent.setAction("SHOW_DOWN_DIALOG");
                    ComRegActivity.this.sendBroadcast(intent);
                    MySharedPreferences.setString(ComRegActivity.this, MySharedPreferences.isSerialPortIDReg, "yes");
                    if (ComRegActivity.this.writeFileData()) {
                        ComRegActivity.this.setResult(13);
                        ComRegActivity.this.finish();
                    }
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    switch (ComRegActivity.this.res.getCode()) {
                        case ConfirmationCallback.UNSPECIFIED_OPTION /*-1*/:
                            SimpleDialog.validTokenDialog(ComRegActivity.this.context);
                        case MyHttpException.ERROR_PARAMETER_EMPTY /*401*/:
                            Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.context.getResources().getString(C0136R.string.port_input), 0).show();
                        case MyHttpException.ERROR_RESULT_NOT_EXIST /*405*/:
                            Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.context.getResources().getString(C0136R.string.serialNo_not_exist), 0).show();
                        case MyHttpException.ERROR_SERVER /*500*/:
                            Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.context.getResources().getString(C0136R.string.notic_serv), 0).show();
                        case MyHttpException.ERROR_NOT_SELL /*650*/:
                            Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.context.getResources().getString(C0136R.string.port_not_seller), 0).show();
                        case MyHttpException.ERROR_REGISTERED /*651*/:
                            Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.context.getResources().getString(C0136R.string.port_have_reg), 0).show();
                        case MyHttpException.ERROR_NULLIFY /*652*/:
                            Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.context.getResources().getString(C0136R.string.port_have_nullify), 0).show();
                        case MyHttpException.ERROR_PASW_PD /*655*/:
                            Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.context.getResources().getString(C0136R.string.port_pwd_wrong), 0).show();
                        case MyHttpException.ERROR_DEALER_CODE /*656*/:
                            Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.context.getResources().getString(C0136R.string.port_delcar), 0).show();
                        case MyHttpException.ERROR_SERIAL_NOEXIST /*658*/:
                            Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.context.getResources().getString(C0136R.string.port_null), 0).show();
                        case MyHttpException.ERROR_OTHER_REGISTER /*659*/:
                            Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.context.getResources().getString(C0136R.string.port_others_reg), 0).show();
                        case MyHttpException.ERROR_PRODUCT_CONF_EMPTY /*660*/:
                            Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.context.getResources().getString(C0136R.string.port_product_null), 0).show();
                        default:
                    }
                case Service.METAGRAM /*99*/:
                    Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.context.getResources().getString(C0136R.string.port_success), 0).show();
                case Service.HOSTNAME /*101*/:
                    new Thread(new C05131()).start();
                case Service.ISO_TSAP /*102*/:
                    ComRegActivity.this.btn_sendVerifyCode.setImageDrawable(ComRegActivity.this.drawable);
                case 202:
                    if (ComRegActivity.this.wsResult.getMessage() != null && ComRegActivity.this.wsResult.getMessage().equals(XmlPullParser.NO_NAMESPACE)) {
                        Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.wsResult.getMessage(), 0).show();
                    }
                case Constant.ERROR_CODE /*805*/:
                    if (msg.getData() == null || msg.getData().getString(BundleBuilder.AskFromMessage) == null || msg.getData().getString(BundleBuilder.AskFromMessage).equals(XmlPullParser.NO_NAMESPACE)) {
                        Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.getString(C0136R.string.error_server), 0).show();
                    } else {
                        Toast.makeText(ComRegActivity.this.context, msg.getData().getString(BundleBuilder.AskFromMessage), 0).show();
                    }
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.ComRegActivity.2 */
    class C05152 implements OnClickListener {
        C05152() {
        }

        public void onClick(View v) {
            ComRegActivity.this.checkPrams();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.ComRegActivity.3 */
    class C05163 implements OnClickListener {
        C05163() {
        }

        public void onClick(View arg0) {
            if (Common.isNetworkAvailable(ComRegActivity.this.context)) {
                new GetVerifyCodeAsy().execute(new String[0]);
            } else {
                Toast.makeText(ComRegActivity.this, C0136R.string.network, 1).show();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.ComRegActivity.4 */
    class C05174 implements OnClickListener {
        C05174() {
        }

        public void onClick(View v) {
            ComRegActivity.this.finish();
            ComRegActivity.this.overridePendingTransition(0, 0);
        }
    }

    /* renamed from: com.ifoer.expeditionphone.ComRegActivity.5 */
    class C05185 implements OnClickListener {
        C05185() {
        }

        public void onClick(View arg0) {
            new GetAreaCountry().execute(new String[0]);
        }
    }

    /* renamed from: com.ifoer.expeditionphone.ComRegActivity.6 */
    class C05196 implements OnItemClickListener {
        C05196() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            ComRegActivity.this.window.dismiss();
            ComRegActivity.this.selectCountry.setText(((CountryInfo) ComRegActivity.this.infoList.get(position)).getCountry());
            ComRegActivity.this.intNationId = ((CountryInfo) ComRegActivity.this.infoList.get(position)).getnCode();
        }
    }

    class AsyncTaskPort extends AsyncTask<Void, Void, Void> {
        AsyncTaskPort() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... arg0) {
            ProductService productClient = new ProductService();
            ComRegActivity.this.cc = MySharedPreferences.getStringValue(ComRegActivity.this.context, MySharedPreferences.CCKey);
            ComRegActivity.this.token = MySharedPreferences.getStringValue(ComRegActivity.this.context, MySharedPreferences.TokenKey);
            productClient.setCc(ComRegActivity.this.cc);
            productClient.setToken(ComRegActivity.this.token);
            try {
                ComRegActivity.this.res = productClient.registerProductForPad(ComRegActivity.this.context, ComRegActivity.this.serialNo, "EEE", ComRegActivity.this.portPsw);
            } catch (SocketTimeoutException e) {
                ComRegActivity.this.mHandler.obtainMessage(0).sendToTarget();
            } catch (NullPointerException e2) {
                ComRegActivity.this.mHandler.obtainMessage(0).sendToTarget();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (ComRegActivity.this.res == null) {
                ComRegActivity.this.mHandler.obtainMessage(0).sendToTarget();
            } else if (ComRegActivity.this.res.getCode() == 0) {
                ComRegActivity.this.mHandler.sendEmptyMessage(1);
            } else {
                if (ComRegActivity.this.progressDialogs != null && ComRegActivity.this.progressDialogs.isShowing()) {
                    ComRegActivity.this.progressDialogs.dismiss();
                }
                MySharedPreferences.setString(ComRegActivity.this.context, BaseProfile.COL_USERNAME, XmlPullParser.NO_NAMESPACE);
                MySharedPreferences.setString(ComRegActivity.this.context, "password", XmlPullParser.NO_NAMESPACE);
                MySharedPreferences.setString(ComRegActivity.this.context, MySharedPreferences.CCKey, XmlPullParser.NO_NAMESPACE);
                if (ComRegActivity.this.res.getMessage() == null || ComRegActivity.this.res.getMessage().equalsIgnoreCase(XmlPullParser.NO_NAMESPACE) || ComRegActivity.this.res.getMessage().equalsIgnoreCase("null")) {
                    ComRegActivity.this.mHandler.sendMessage(ComRegActivity.this.mHandler.obtainMessage(2, ComRegActivity.this.res));
                } else {
                    Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.res.getMessage(), 0).show();
                }
            }
        }
    }

    class GetAreaCountry extends AsyncTask<String, String, String> {
        CountryListResult baseCode;

        GetAreaCountry() {
            this.baseCode = null;
        }

        protected String doInBackground(String... arg0) {
            try {
                this.baseCode = new WebServiceClient().getAreaCountry(MainMenuActivity.country);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPreExecute() {
            if (ComRegActivity.this.progressDialogs != null && !ComRegActivity.this.progressDialogs.isShowing()) {
                ComRegActivity.this.progressDialogs.show();
            }
        }

        protected void onPostExecute(String result) {
            if (ComRegActivity.this.progressDialogs != null && ComRegActivity.this.progressDialogs.isShowing()) {
                ComRegActivity.this.progressDialogs.dismiss();
            }
            if (this.baseCode == null) {
                Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.getString(C0136R.string.error_server), 0).show();
            } else if (!this.baseCode.isError()) {
                if (this.baseCode.getCode() == 0) {
                    ComRegActivity.this.infoList = this.baseCode.getDatas();
                    Log.i("ComActivity", "\u83b7\u53d6\u56fd\u5bb6\u5217\u8868\u6210\u529f" + ComRegActivity.this.infoList.size());
                    ComRegActivity.this.popAwindow(ComRegActivity.this.et_zip);
                    return;
                }
                Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.getString(C0136R.string.set_verify_get_failure), 0).show();
            }
        }
    }

    public class GetLocationNameThread extends Thread {
        int times;

        public GetLocationNameThread() {
            this.times = 0;
        }

        public void run() {
            while (true) {
                try {
                    this.times++;
                    sleep(5000);
                    if (Constant.geoLat != 0.0d && Constant.geoLng != 0.0d) {
                        String address = ComRegActivity.this.locationService.GetLocationName(Constant.geoLat, Constant.geoLng);
                        Constant.ADDRESS = address;
                        Log.e("ComRegActivity", "\u5730\u70b9" + address);
                        ComRegActivity.this.locationService.onDestroy();
                        return;
                    } else if (this.times > 12) {
                        ComRegActivity.this.locationService.onDestroy();
                        return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class GetVerifyCodeAsy extends AsyncTask<String, String, String> {
        BaseCode baseCode;

        GetVerifyCodeAsy() {
            this.baseCode = null;
        }

        protected String doInBackground(String... arg0) {
            try {
                this.baseCode = new WebServiceClient().getVerifyCode(ComRegActivity.this.regAc, MainMenuActivity.country, Contact.RELATION_FRIEND);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPreExecute() {
            if (ComRegActivity.this.progressDialogs == null) {
                ComRegActivity.this.progressDialogs = new ProgressDialog(ComRegActivity.this);
                ComRegActivity.this.progressDialogs.setMessage(ComRegActivity.this.context.getResources().getString(C0136R.string.find_wait));
                ComRegActivity.this.progressDialogs.setCancelable(false);
            }
            ComRegActivity.this.progressDialogs.show();
        }

        protected void onPostExecute(String result) {
            if (this.baseCode == null) {
                Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.getString(C0136R.string.error_server), 0).show();
            } else if (this.baseCode.getCode() == 0) {
                ComRegActivity.this.progressDialogs.dismiss();
                ComRegActivity.this.url = this.baseCode.getData().toString().substring(this.baseCode.getData().toString().indexOf("http"), this.baseCode.getData().toString().length() - 1);
                Log.i("ComReg", "url  =   " + ComRegActivity.this.url);
                ComRegActivity.this.mHandler.sendEmptyMessage(Service.HOSTNAME);
            } else if (this.baseCode.getCode() == 110001) {
                if (ComRegActivity.this.regAc.length() < 20) {
                    ComRegActivity comRegActivity = ComRegActivity.this;
                    comRegActivity.regAc = comRegActivity.regAc + ComRegActivity.this.randomABC(1);
                }
                cancel(true);
                if (isCancelled()) {
                    new GetVerifyCodeAsy().execute(new String[0]);
                }
            } else {
                Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.getString(C0136R.string.set_verify_get_failure), 0).show();
            }
        }
    }

    class LoginingAsy extends AsyncTask<String, String, String> {
        LoginingAsy() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            ComRegActivity.this.clientService = new WebServiceClient();
            try {
                ComRegActivity.this.loginResult = ComRegActivity.this.clientService.userLogin(ComRegActivity.this.context, ComRegActivity.this.regAc, ComRegActivity.this.mobileAppVersion, ComRegActivity.this.regPwd);
            } catch (SocketTimeoutException e) {
                ComRegActivity.this.mHandler.obtainMessage(0).sendToTarget();
            } catch (Exception e2) {
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (ComRegActivity.this.loginResult == null) {
                if (ComRegActivity.this.progressDialogs != null && ComRegActivity.this.progressDialogs.isShowing()) {
                    ComRegActivity.this.progressDialogs.dismiss();
                }
                Toast.makeText(ComRegActivity.this.context, C0136R.string.notic_serv, 1).show();
            } else if (ComRegActivity.this.loginResult.getCode() == 0) {
                MySharedPreferences.setString(ComRegActivity.this.getApplicationContext(), "loginKey", ComRegActivity.this.regAc);
                MySharedPreferences.setString(ComRegActivity.this.getApplicationContext(), "loginpassword", ComRegActivity.this.regPwd);
                MySharedPreferences.setString(ComRegActivity.this.getApplicationContext(), BaseProfile.COL_USERNAME, ComRegActivity.this.regAc);
                MySharedPreferences.setString(ComRegActivity.this.getApplicationContext(), "password", ComRegActivity.this.regPwd);
                ComRegActivity.this.cc = ComRegActivity.this.loginResult.getCc();
                ComRegActivity.this.token = ComRegActivity.this.loginResult.getToken();
                ComRegActivity.this.username = ComRegActivity.this.loginResult.getUserName();
                MySharedPreferences.setString(ComRegActivity.this.context, "openshowuser", Contact.RELATION_FRIEND);
                MySharedPreferences.setString(ComRegActivity.this.getApplicationContext(), "usernames", ComRegActivity.this.regAc);
                MySharedPreferences.setString(ComRegActivity.this.getApplicationContext(), MySharedPreferences.CCKey, ComRegActivity.this.cc);
                MySharedPreferences.setString(ComRegActivity.this.getApplicationContext(), MySharedPreferences.TokenKey, ComRegActivity.this.token);
                MySharedPreferences.setString(ComRegActivity.this.getApplicationContext(), MySharedPreferences.UserNameKey, ComRegActivity.this.regAc);
                new AsyncTaskPort().execute(new Void[0]);
            } else {
                if (ComRegActivity.this.progressDialogs != null && ComRegActivity.this.progressDialogs.isShowing()) {
                    ComRegActivity.this.progressDialogs.dismiss();
                }
                if (ComRegActivity.this.loginResult.getCode() == 100001) {
                    Toast.makeText(ComRegActivity.this, C0136R.string.login_pwd_wrong, 1).show();
                } else if (ComRegActivity.this.loginResult.getCode() == 100002) {
                    Toast.makeText(ComRegActivity.this, C0136R.string.modi_user_null, 1).show();
                } else if (ComRegActivity.this.loginResult.getCode() == 40000) {
                    Toast.makeText(ComRegActivity.this, C0136R.string.login_fail_prompt_40000, 1).show();
                } else if (ComRegActivity.this.loginResult.getCode() == RequestCode.REQ_GET_TARGETDETAIL) {
                    Toast.makeText(ComRegActivity.this, C0136R.string.login_fail_prompt_40001, 1).show();
                } else if (ComRegActivity.this.loginResult.getCode() == MyHttpException.ERROR_USER_STATE) {
                    Toast.makeText(ComRegActivity.this, C0136R.string.login_user_throws, 1).show();
                } else if (ComRegActivity.this.loginResult.getCode() != Constant.ERROR_CODE) {
                    Toast.makeText(ComRegActivity.this.context, C0136R.string.notic_serv, 1).show();
                } else if (ComRegActivity.this.loginResult.getMessage() != null && !ComRegActivity.this.loginResult.getMessage().equals(XmlPullParser.NO_NAMESPACE)) {
                    Toast.makeText(ComRegActivity.this.context, ComRegActivity.this.loginResult.getMessage(), 1).show();
                }
            }
        }
    }

    class RegAsy extends AsyncTask<String, String, String> {
        private final Handler mHandler;

        /* renamed from: com.ifoer.expeditionphone.ComRegActivity.RegAsy.1 */
        class C05201 extends Handler {
            C05201() {
            }

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case KEYRecord.OWNER_USER /*0*/:
                        if (ComRegActivity.this.progressDialogs != null && ComRegActivity.this.progressDialogs.isShowing()) {
                            ComRegActivity.this.progressDialogs.dismiss();
                        }
                        Toast.makeText(ComRegActivity.this.context, C0136R.string.timeout, 0).show();
                    case Constant.ERROR_NETWORK /*803*/:
                        Toast.makeText(ComRegActivity.this.context, C0136R.string.ERROR_NETWORK, 0).show();
                    default:
                }
            }
        }

        RegAsy() {
            this.mHandler = new C05201();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            ComRegActivity.this.progressDialogs = new ProgressDialog(ComRegActivity.this);
            ComRegActivity.this.progressDialogs.setMessage(ComRegActivity.this.context.getResources().getString(C0136R.string.reg_now));
            ComRegActivity.this.progressDialogs.setCancelable(false);
            ComRegActivity.this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            ComRegActivity.this.clientService = new WebServiceClient();
            try {
                MySharedPreferences.setString(ComRegActivity.this.context, MySharedPreferences.savemail, ComRegActivity.this.regEmail);
                ComRegActivity.this.regrs = ComRegActivity.this.clientService.userRegist(new StringBuilder(String.valueOf(ComRegActivity.this.intNationId)).toString(), ComRegActivity.this.regAc, ComRegActivity.this.regAc, ComRegActivity.this.regPwd, ComRegActivity.this.et_zip.getText().toString().trim(), ComRegActivity.this.veryfiCode, ComRegActivity.this.regEmail);
            } catch (Exception e) {
                this.mHandler.obtainMessage(0).sendToTarget();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (ComRegActivity.this.regrs == null) {
                if (ComRegActivity.this.progressDialogs != null && ComRegActivity.this.progressDialogs.isShowing()) {
                    ComRegActivity.this.progressDialogs.dismiss();
                }
                this.mHandler.obtainMessage(Constant.ERROR_SERVER).sendToTarget();
            } else if (ComRegActivity.this.regrs.getCode() == 0) {
                new LoginingAsy().execute(new String[0]);
            } else {
                if (ComRegActivity.this.progressDialogs != null && ComRegActivity.this.progressDialogs.isShowing()) {
                    ComRegActivity.this.progressDialogs.dismiss();
                }
                if (ComRegActivity.this.regrs.getCode() == 30012) {
                    Toast.makeText(ComRegActivity.this, C0136R.string.reg_user_wrong, 1).show();
                } else if (ComRegActivity.this.regrs.getCode() == RequestCode.REQ_QUERY_REPORT) {
                    Toast.makeText(ComRegActivity.this, C0136R.string.reg_pwd_wrong, 1).show();
                } else if (ComRegActivity.this.regrs.getCode() == 30009) {
                    Toast.makeText(ComRegActivity.this, C0136R.string.reg_email, 1).show();
                } else if (ComRegActivity.this.regrs.getCode() == 30010) {
                    Toast.makeText(ComRegActivity.this, C0136R.string.register_fail_prompt_300010, 1).show();
                } else if (ComRegActivity.this.regrs.getCode() == Flags.FLAG5) {
                    if (ComRegActivity.this.regAc.length() < 20) {
                        ComRegActivity comRegActivity = ComRegActivity.this;
                        comRegActivity.regAc = comRegActivity.regAc + ComRegActivity.this.randomABC(1);
                    }
                    cancel(true);
                    if (isCancelled()) {
                        new RegAsy().execute(new String[0]);
                    }
                } else if (ComRegActivity.this.regrs.getCode() == MyHttpException.ERROR_SERVER) {
                    Toast.makeText(ComRegActivity.this, C0136R.string.notic_serv, 1).show();
                } else if (ComRegActivity.this.regrs.getCode() == MyHttpException.ERROR_NETWORK) {
                    Toast.makeText(ComRegActivity.this, C0136R.string.network, 1).show();
                } else if (ComRegActivity.this.regrs.getCode() == 1025) {
                    Toast.makeText(ComRegActivity.this, C0136R.string.register_fail_prompt_1025, 1).show();
                } else if (ComRegActivity.this.regrs.getCode() == 30011) {
                    Toast.makeText(ComRegActivity.this, C0136R.string.register_fail_prompt_300011, 1).show();
                } else if (ComRegActivity.this.regrs.getMessage() == null || ComRegActivity.this.regrs.getMessage().equals(XmlPullParser.NO_NAMESPACE)) {
                    Toast.makeText(ComRegActivity.this.context, C0136R.string.notic_serv, 1).show();
                } else {
                    Toast.makeText(ComRegActivity.this, ComRegActivity.this.regrs.getMessage(), 1).show();
                }
            }
        }
    }

    class checkForCRPdAsy extends AsyncTask<String, String, String> {
        WSResult ws;

        checkForCRPdAsy() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            ComRegActivity.this.progressDialogs = new ProgressDialog(ComRegActivity.this.context);
            ComRegActivity.this.progressDialogs.setMessage(ComRegActivity.this.getResources().getString(C0136R.string.find_wait));
            ComRegActivity.this.progressDialogs.setCancelable(false);
            ComRegActivity.this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            try {
                this.ws = new WebServiceClient().checkForCRP(ComRegActivity.this.serialNo, ComRegActivity.this.portPsw, ComRegActivity.this.context);
            } catch (SocketTimeoutException e) {
                ComRegActivity.this.mHandler.obtainMessage(0).sendToTarget();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (ComRegActivity.this.progressDialogs != null && ComRegActivity.this.progressDialogs.isShowing()) {
                ComRegActivity.this.progressDialogs.dismiss();
            }
            if (this.ws == null) {
                ComRegActivity.this.mHandler.obtainMessage(Constant.ERROR_NETWORK).sendToTarget();
            } else if (this.ws.getCode() == 0) {
                new RegAsy().execute(new String[0]);
            } else if (this.ws.getMessage() == null || this.ws.getMessage().equalsIgnoreCase(XmlPullParser.NO_NAMESPACE) || this.ws.getMessage().equalsIgnoreCase("null")) {
                ComRegActivity.this.mHandler.obtainMessage(Constant.ERROR_SERVER).sendToTarget();
            } else {
                Toast.makeText(ComRegActivity.this.context, this.ws.getMessage(), 0).show();
            }
        }
    }

    private class mBroadcastReceiver extends BroadcastReceiver {
        private mBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - ComRegActivity.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                ComRegActivity.this.lastTime = System.currentTimeMillis();
            }
        }
    }

    public ComRegActivity() {
        this.context = this;
        this.regrs = null;
        this.clientService = null;
        this.mobileAppVersion = Constant.APP_VERSION;
        this.serialNo = XmlPullParser.NO_NAMESPACE;
        this.portPsw = XmlPullParser.NO_NAMESPACE;
        this.wsResult = null;
        this.needResendVerifyCode = false;
        this.window = null;
        this.infoList = new ArrayList();
        this.intNationId = -1;
        this.mHandler = new C05141();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.regist3);
        registerBoradcastReceiver();
        MyApplication.getInstance().addActivity(this);
        creatView();
    }

    private void creatView() {
        this.inflater = LayoutInflater.from(this.context);
        this.popWidow = new DialogPopWindow(this, this);
        String serialPortID = MySharedPreferences.getStringValue(this, Constants.SERIALID);
        String serialPortNO = MySharedPreferences.getStringValue(this, Constants.SERIALNO);
        this.regAc = "CRP2_" + serialPortNO;
        this.regPwd = serialPortID;
        this.serialNo = serialPortNO;
        this.portPsw = serialPortID;
        this.verifyCodeEd = (EditText) findViewById(C0136R.id.ed_verifycode);
        this.et_zip = (EditText) findViewById(C0136R.id.et_zip);
        this.emailEditText = (EditText) findViewById(C0136R.id.email);
        this.registerBtn = (Button) findViewById(C0136R.id.registerbtn);
        this.registerBtn.setOnClickListener(new C05152());
        this.btn_sendVerifyCode = (ImageView) findViewById(C0136R.id.send_verycode);
        this.btn_sendVerifyCode.setOnClickListener(new C05163());
        this.returnBtn = (Button) findViewById(C0136R.id.btn_back);
        this.returnBtn.setOnClickListener(new C05174());
        this.selectCountry = (TextView) findViewById(C0136R.id.selecte_country);
        this.selectCountry.setOnClickListener(new C05185());
        if (Common.isNetworkAvailable(this.context)) {
            new GetVerifyCodeAsy().execute(new String[0]);
        } else {
            Toast.makeText(this, C0136R.string.network, 1).show();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.progressDialogs != null && this.progressDialogs.isShowing()) {
            this.progressDialogs.dismiss();
        }
        if (this.receiver != null) {
            unregisterReceiver(this.receiver);
        }
    }

    public void checkPrams() {
        hideKeyboard();
        this.regEmail = this.emailEditText.getText().toString();
        this.veryfiCode = this.verifyCodeEd.getText().toString();
        if (TextUtils.isEmpty(this.veryfiCode)) {
            Toast.makeText(this.context, C0136R.string.mine_verification_code_prompt, 0).show();
        } else if (!Tools.isNumeric(this.veryfiCode) || this.veryfiCode.length() != 4) {
            Toast.makeText(this.context, C0136R.string.reset_password_identificode_error, 0).show();
        } else if (XmlPullParser.NO_NAMESPACE.equalsIgnoreCase(this.regEmail)) {
            Toast.makeText(this, C0136R.string.Input_email_address, 0).show();
        } else if (TextUtils.isEmpty(this.et_zip.getText().toString())) {
            Toast.makeText(this.context, this.context.getResources().getString(C0136R.string.can_not_be_empty), 0).show();
        } else if (this.intNationId == -1) {
            Toast.makeText(this.context, this.context.getResources().getString(C0136R.string.register_select_contury), 0).show();
        } else if (!Common.isEmail(this.regEmail)) {
            Toast.makeText(this, C0136R.string.reg_ToastEmail, 1).show();
        } else if (!Common.isNetworkAvailable(this.context)) {
            Toast.makeText(this.context, getResources().getString(C0136R.string.network), 1).show();
        } else if (!this.popWidow.isShowing()) {
            this.popWidow.showAtLocation(findViewById(16908290).getRootView(), 17, 0, 0);
        }
    }

    public void Register2() {
        if (!Common.isEmail(this.regEmail)) {
            return;
        }
        if (Common.isNetworkAvailable(this.context)) {
            new checkForCRPdAsy().execute(new String[0]);
        } else {
            Toast.makeText(this, C0136R.string.network, 1).show();
        }
    }

    private void hideKeyboard() {
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.emailEditText.getWindowToken(), 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                if (resultCode == 10) {
                    setResult(10);
                    finish();
                    overridePendingTransition(0, 0);
                } else if (resultCode == 12) {
                    setResult(12);
                    finish();
                    overridePendingTransition(0, 0);
                }
            default:
        }
    }

    public String randomABC(int length) {
        Random random = new Random();
        String val = XmlPullParser.NO_NAMESPACE;
        for (int i = 0; i < length; i++) {
            int choice;
            if (random.nextInt(2) % 2 == 0) {
                choice = 97;
            } else {
                choice = 97;
            }
            val = new StringBuilder(String.valueOf(val)).append((char) (random.nextInt(26) + 97)).toString();
        }
        return val;
    }

    public boolean writeFileData() {
        String filePath = Environment.getExternalStorageDirectory() + "/cnlaunch/profile.txt";
        String message = this.cc + AlixDefine.split + this.regPwd + AlixDefine.split + this.emailEditText.getText().toString();
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(message.getBytes());
            fout.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void registerBoradcastReceiver() {
        this.receiver = new mBroadcastReceiver();
        this.myIntentFilter = new IntentFilter();
        this.myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(this.receiver, this.myIntentFilter);
    }

    private void popAwindow(View poptext) {
        if (this.window == null) {
            View v = this.inflater.inflate(C0136R.layout.popwindow, null);
            this.list = (ListView) v.findViewById(C0136R.id.lv);
            this.list.setItemsCanFocus(false);
            this.list.setChoiceMode(2);
            this.mAdapter = new CountryInfoAdapter(this.context, this.infoList);
            this.countryName = (TextView) v.findViewById(C0136R.id.tvname);
            this.countryName.setText(getString(C0136R.string.register_select_contury));
            this.list.setAdapter(this.mAdapter);
            this.window = new PopupWindow(v, poptext.getWidth(), -1);
        } else if (!this.window.isShowing()) {
            this.window.setWidth(poptext.getWidth());
            this.window.update();
        }
        this.list.setOnItemClickListener(new C05196());
        this.window.setFocusable(true);
        this.window.setTouchable(true);
        this.window.setOutsideTouchable(true);
        this.window.setBackgroundDrawable(new ColorDrawable());
        this.window.update();
        this.window.showAtLocation(findViewById(16908290).getRootView(), 17, 0, 0);
    }
}

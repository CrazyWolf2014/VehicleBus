package com.ifoer.expeditionphone;

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
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.RequestCode;
import com.ifoer.mine.model.BaseCode;
import com.ifoer.util.Common;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.NetPOSPrinter;
import com.ifoer.util.SimpleDialog;
import com.ifoer.view.MenuHorizontalScrollView;
import com.ifoer.webservice.WebServiceClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import org.apache.http.util.EncodingUtils;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class ModiPassWordActivity extends Activity {
    private Button affirmBtn;
    private Button backBtn;
    private Button cancelBtn;
    protected LinearLayout car_maintain;
    private String cc;
    private LinearLayout circleLay;
    private Context context;
    private LinearLayout dataLay;
    private LayoutInflater inflater;
    private LinearLayout inforLay;
    private long lastTime;
    private BroadcastReceiver mBroadcastReceiver;
    private RelativeLayout menu;
    private LinearLayout moreLay;
    private LinearLayout mySpaceLay;
    private EditText newPwd;
    private String newPwdEd;
    private EditText oldPwd;
    private String oldPwdEd;
    private ProgressDialog progressDialogs;
    private EditText pwdAffirm;
    private String pwdAffirmEd;
    private BaseCode res;
    private MenuHorizontalScrollView scrollView;
    private String token;
    private LinearLayout userLay;

    /* renamed from: com.ifoer.expeditionphone.ModiPassWordActivity.1 */
    class C05981 extends BroadcastReceiver {
        C05981() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON") && (System.currentTimeMillis() - ModiPassWordActivity.this.lastTime) / 1000 > 30) {
                SimpleDialog.ExitDialog(context);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                ModiPassWordActivity.this.lastTime = System.currentTimeMillis();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.ModiPassWordActivity.2 */
    class C05992 implements OnClickListener {
        C05992() {
        }

        public void onClick(View v) {
            ModiPassWordActivity.this.finish();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.ModiPassWordActivity.3 */
    class C06003 implements OnClickListener {
        C06003() {
        }

        public void onClick(View v) {
            if (Common.isNetworkAvailable(ModiPassWordActivity.this.context)) {
                ModiPassWordActivity.this.oldPwdEd = ModiPassWordActivity.this.oldPwd.getText().toString();
                ModiPassWordActivity.this.newPwdEd = ModiPassWordActivity.this.newPwd.getText().toString();
                ModiPassWordActivity.this.pwdAffirmEd = ModiPassWordActivity.this.pwdAffirm.getText().toString();
                if (ModiPassWordActivity.this.oldPwdEd.length() <= 0) {
                    Toast.makeText(ModiPassWordActivity.this.context, ModiPassWordActivity.this.getResources().getString(C0136R.string.oldPw), 1).show();
                    return;
                } else if (XmlPullParser.NO_NAMESPACE.equalsIgnoreCase(ModiPassWordActivity.this.newPwdEd)) {
                    Toast.makeText(ModiPassWordActivity.this.context, C0136R.string.newPw, 1).show();
                    return;
                } else {
                    Pattern pat = Pattern.compile("^[a-zA-Z0-9_@]+$");
                    if (!pat.matcher(ModiPassWordActivity.this.newPwdEd).matches()) {
                        Toast.makeText(ModiPassWordActivity.this.context, C0136R.string.reg_pwd_wrong, 1).show();
                        return;
                    } else if (ModiPassWordActivity.this.newPwdEd.length() < 6 || ModiPassWordActivity.this.newPwdEd.length() > 20) {
                        Toast.makeText(ModiPassWordActivity.this.context, ModiPassWordActivity.this.getResources().getString(C0136R.string.reg_user_pwd), 1).show();
                        return;
                    } else if (XmlPullParser.NO_NAMESPACE.equalsIgnoreCase(ModiPassWordActivity.this.pwdAffirmEd)) {
                        Toast.makeText(ModiPassWordActivity.this.context, C0136R.string.newPwAgain, 1).show();
                        return;
                    } else if (!pat.matcher(ModiPassWordActivity.this.pwdAffirmEd).matches()) {
                        Toast.makeText(ModiPassWordActivity.this.context, C0136R.string.reg_pwd_wrong, 1).show();
                        return;
                    } else if (ModiPassWordActivity.this.pwdAffirmEd.length() < 6 || ModiPassWordActivity.this.newPwdEd.length() > 20) {
                        Toast.makeText(ModiPassWordActivity.this.context, ModiPassWordActivity.this.getResources().getString(C0136R.string.reg_user_pwd), 1).show();
                        return;
                    } else if (ModiPassWordActivity.this.oldPwdEd.equalsIgnoreCase(ModiPassWordActivity.this.newPwdEd)) {
                        Toast.makeText(ModiPassWordActivity.this.context, ModiPassWordActivity.this.getResources().getString(C0136R.string.resetpsw), 0).show();
                        return;
                    } else if (ModiPassWordActivity.this.newPwdEd.equals(ModiPassWordActivity.this.pwdAffirmEd)) {
                        new modiPwdAsy().execute(new String[0]);
                        return;
                    } else {
                        Toast.makeText(ModiPassWordActivity.this.context, ModiPassWordActivity.this.getResources().getString(C0136R.string.modi_pwd_inconfor), 1).show();
                        return;
                    }
                }
            }
            Toast.makeText(ModiPassWordActivity.this.context, ModiPassWordActivity.this.getResources().getString(C0136R.string.network), 1).show();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.ModiPassWordActivity.4 */
    class C06014 implements OnClickListener {
        C06014() {
        }

        public void onClick(View v) {
            ModiPassWordActivity.this.finish();
        }
    }

    class modiPwdAsy extends AsyncTask<String, String, String> {
        private final Handler mHandler;

        /* renamed from: com.ifoer.expeditionphone.ModiPassWordActivity.modiPwdAsy.1 */
        class C06021 extends Handler {
            C06021() {
            }

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case KEYRecord.OWNER_USER /*0*/:
                        if (ModiPassWordActivity.this.progressDialogs != null && ModiPassWordActivity.this.progressDialogs.isShowing()) {
                            ModiPassWordActivity.this.progressDialogs.dismiss();
                        }
                        Toast.makeText(ModiPassWordActivity.this.context, C0136R.string.timeout, 0).show();
                    default:
                }
            }
        }

        modiPwdAsy() {
            this.mHandler = new C06021();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            ModiPassWordActivity.this.progressDialogs = new ProgressDialog(ModiPassWordActivity.this.context);
            ModiPassWordActivity.this.progressDialogs.setMessage(ModiPassWordActivity.this.context.getResources().getString(C0136R.string.modi_pwd_now));
            ModiPassWordActivity.this.progressDialogs.setCancelable(false);
            ModiPassWordActivity.this.progressDialogs.show();
        }

        protected String doInBackground(String... params) {
            WebServiceClient client = new WebServiceClient();
            ModiPassWordActivity.this.cc = MySharedPreferences.getStringValue(ModiPassWordActivity.this.context, MySharedPreferences.CCKey);
            try {
                ModiPassWordActivity.this.res = client.modiPsw(ModiPassWordActivity.this.cc, ModiPassWordActivity.this.oldPwdEd, ModiPassWordActivity.this.newPwdEd);
            } catch (NullPointerException e) {
                this.mHandler.obtainMessage(0).sendToTarget();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (ModiPassWordActivity.this.progressDialogs != null && ModiPassWordActivity.this.progressDialogs.isShowing()) {
                ModiPassWordActivity.this.progressDialogs.dismiss();
            }
            if (ModiPassWordActivity.this.res == null) {
                ModiPassWordActivity.this.progressDialogs.dismiss();
            } else if (ModiPassWordActivity.this.res.getCode() == -1) {
                SimpleDialog.validTokenDialog(ModiPassWordActivity.this.context);
            } else if (ModiPassWordActivity.this.res.getCode() == 0) {
                Toast.makeText(ModiPassWordActivity.this.context, ModiPassWordActivity.this.getResources().getString(C0136R.string.modi_pwd_success), 1).show();
                ModiPassWordActivity.this.writeFileData();
                ModiPassWordActivity.this.finish();
            } else if (ModiPassWordActivity.this.res.getCode() == MyHttpException.ERROR_PASW) {
                Toast.makeText(ModiPassWordActivity.this.context, ModiPassWordActivity.this.getResources().getString(C0136R.string.modi_pwd_wrong), 1).show();
            } else if (ModiPassWordActivity.this.res.getCode() == NetPOSPrinter.PRINT_WIDTH) {
                Toast.makeText(ModiPassWordActivity.this.context, ModiPassWordActivity.this.getResources().getString(C0136R.string.modi_user_null), 1).show();
            } else if (ModiPassWordActivity.this.res.getCode() == RequestCode.REQ_QUERY_REPORT) {
                Toast.makeText(ModiPassWordActivity.this.context, ModiPassWordActivity.this.getResources().getString(C0136R.string.modi_pwd_wrong), 1).show();
            } else if (ModiPassWordActivity.this.res.getCode() == RequestCode.REQ_GET_PERSONINFO) {
                Toast.makeText(ModiPassWordActivity.this.context, ModiPassWordActivity.this.getResources().getString(C0136R.string.modi_pwd_wrong), 1).show();
            } else if (ModiPassWordActivity.this.res.getCode() == 110202) {
                Toast.makeText(ModiPassWordActivity.this.context, ModiPassWordActivity.this.getResources().getString(C0136R.string.mine_modipass_failure), 1).show();
            } else if (ModiPassWordActivity.this.res.getCode() == RequestCode.REQ_CHANGE_SEX_CODE) {
                Toast.makeText(ModiPassWordActivity.this.context, ModiPassWordActivity.this.getString(C0136R.string.modi_user_null), 0).show();
            }
        }
    }

    public ModiPassWordActivity() {
        this.inflater = null;
        this.res = null;
        this.mBroadcastReceiver = new C05981();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Flags.FLAG8, Flags.FLAG8);
        setContentView(C0136R.layout.modi_pwd);
        this.context = this;
        createView();
    }

    private void createView() {
        registerBoradcastReceiver();
        this.menu = (RelativeLayout) findViewById(C0136R.id.main_leftmenu);
        this.oldPwd = (EditText) findViewById(C0136R.id.old_pwd);
        this.oldPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.newPwd = (EditText) findViewById(C0136R.id.new_pwd);
        this.newPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.pwdAffirm = (EditText) findViewById(C0136R.id.new_pwd_affirm);
        this.pwdAffirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.backBtn = (Button) findViewById(C0136R.id.returnBtn);
        this.backBtn.setOnClickListener(new C05992());
        this.affirmBtn = (Button) findViewById(C0136R.id.modipwd_affirm);
        this.cancelBtn = (Button) findViewById(C0136R.id.cancel_btn);
        this.affirmBtn.setOnClickListener(new C06003());
        this.cancelBtn.setOnClickListener(new C06014());
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        myIntentFilter.addAction("android.intent.action.SCREEN_ON");
        this.context.registerReceiver(this.mBroadcastReceiver, myIntentFilter);
    }

    public boolean writeFileData() {
        String filePath = Environment.getExternalStorageDirectory() + "/cnlaunch/profile.txt";
        File file = new File(filePath);
        String res = XmlPullParser.NO_NAMESPACE;
        if (!file.exists()) {
            return false;
        }
        try {
            FileInputStream fin = new FileInputStream(filePath);
            byte[] buffer = new byte[fin.available()];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, AsyncHttpResponseHandler.DEFAULT_CHARSET);
            fin.close();
            String[] sn = res.split(AlixDefine.split);
            String name = sn[0];
            String email = sn[2];
            file.delete();
            file.createNewFile();
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(new StringBuilder(String.valueOf(name)).append(AlixDefine.split).append(this.newPwd.getText().toString()).append(AlixDefine.split).append(email).toString().getBytes());
            fout.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mBroadcastReceiver != null) {
            unregisterReceiver(this.mBroadcastReceiver);
        }
    }
}

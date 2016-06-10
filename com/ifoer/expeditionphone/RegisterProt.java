package com.ifoer.expeditionphone;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.car.result.StateResult;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.Constants;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.db.DBDao;
import com.ifoer.util.Common;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.ifoer.webservice.ProductService;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.harmony.javax.security.auth.callback.ConfirmationCallback;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public class RegisterProt extends Activity {
    private String cc;
    private ImageView close;
    private Context context;
    private final Handler mHandler;
    private EditText portEd;
    private String portNum;
    private String portPwd;
    private EditText portPwdEd;
    private StateResult res;
    private Button submit;
    private String token;
    private TextView tvErrorNum;
    private TextView tvErrorPwd;
    private String venderCode;

    /* renamed from: com.ifoer.expeditionphone.RegisterProt.1 */
    class C06231 extends Handler {
        C06231() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    Toast.makeText(RegisterProt.this.context, C0136R.string.timeout, 0).show();
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    RegisterProt.this.dismissDialog(0);
                    Toast.makeText(RegisterProt.this.context, RegisterProt.this.context.getResources().getString(C0136R.string.port_success), 0).show();
                    Intent intent = new Intent();
                    intent.setAction("SHOW_DOWN_DIALOG");
                    RegisterProt.this.sendBroadcast(intent);
                    RegisterProt.this.finish();
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    RegisterProt.this.dismissDialog(0);
                    switch (RegisterProt.this.res.getCode()) {
                        case ConfirmationCallback.UNSPECIFIED_OPTION /*-1*/:
                            SimpleDialog.validTokenDialog(RegisterProt.this.context);
                        case MyHttpException.ERROR_PARAMETER_EMPTY /*401*/:
                            Toast.makeText(RegisterProt.this.context, RegisterProt.this.context.getResources().getString(C0136R.string.port_input), 0).show();
                        case MyHttpException.ERROR_RESULT_NOT_EXIST /*405*/:
                            Toast.makeText(RegisterProt.this.context, RegisterProt.this.context.getResources().getString(C0136R.string.serialNo_not_exist), 0).show();
                        case MyHttpException.ERROR_SERVER /*500*/:
                            Toast.makeText(RegisterProt.this.context, RegisterProt.this.context.getResources().getString(C0136R.string.notic_serv), 0).show();
                        case MyHttpException.ERROR_NOT_SELL /*650*/:
                            Toast.makeText(RegisterProt.this.context, RegisterProt.this.context.getResources().getString(C0136R.string.port_not_seller), 0).show();
                        case MyHttpException.ERROR_REGISTERED /*651*/:
                            Toast.makeText(RegisterProt.this.context, RegisterProt.this.context.getResources().getString(C0136R.string.port_have_reg), 0).show();
                        case MyHttpException.ERROR_NULLIFY /*652*/:
                            Toast.makeText(RegisterProt.this.context, RegisterProt.this.context.getResources().getString(C0136R.string.port_have_nullify), 0).show();
                        case MyHttpException.ERROR_PASW_PD /*655*/:
                            RegisterProt.this.tvErrorPwd.setVisibility(0);
                            Toast.makeText(RegisterProt.this.context, RegisterProt.this.context.getResources().getString(C0136R.string.port_pwd_wrong), 0).show();
                        case MyHttpException.ERROR_DEALER_CODE /*656*/:
                            Toast.makeText(RegisterProt.this.context, RegisterProt.this.context.getResources().getString(C0136R.string.port_delcar), 0).show();
                        case MyHttpException.ERROR_SERIAL_NOEXIST /*658*/:
                            Toast.makeText(RegisterProt.this.context, RegisterProt.this.context.getResources().getString(C0136R.string.port_null), 0).show();
                        case MyHttpException.ERROR_OTHER_REGISTER /*659*/:
                            Toast.makeText(RegisterProt.this.context, RegisterProt.this.context.getResources().getString(C0136R.string.port_others_reg), 0).show();
                        case MyHttpException.ERROR_PRODUCT_CONF_EMPTY /*660*/:
                            Toast.makeText(RegisterProt.this.context, RegisterProt.this.context.getResources().getString(C0136R.string.port_product_null), 0).show();
                        default:
                    }
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.RegisterProt.2 */
    class C06242 implements OnClickListener {
        C06242() {
        }

        public void onClick(View v) {
            RegisterProt.this.finish();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.RegisterProt.3 */
    class C06253 implements TextWatcher {
        C06253() {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void afterTextChanged(Editable s) {
            RegisterProt.this.tvErrorNum.setVisibility(8);
        }
    }

    /* renamed from: com.ifoer.expeditionphone.RegisterProt.4 */
    class C06264 implements TextWatcher {
        C06264() {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void afterTextChanged(Editable s) {
            RegisterProt.this.tvErrorPwd.setVisibility(8);
        }
    }

    /* renamed from: com.ifoer.expeditionphone.RegisterProt.5 */
    class C06285 implements OnClickListener {

        /* renamed from: com.ifoer.expeditionphone.RegisterProt.5.1 */
        class C06271 implements Runnable {
            C06271() {
            }

            public void run() {
                ProductService productClient = new ProductService();
                RegisterProt.this.cc = MySharedPreferences.getStringValue(RegisterProt.this.context, MySharedPreferences.CCKey);
                RegisterProt.this.token = MySharedPreferences.getStringValue(RegisterProt.this.context, MySharedPreferences.TokenKey);
                productClient.setCc(RegisterProt.this.cc);
                productClient.setToken(RegisterProt.this.token);
                try {
                    RegisterProt.this.res = productClient.registerProductForPad(RegisterProt.this.context, RegisterProt.this.portNum, RegisterProt.this.venderCode, RegisterProt.this.portPwd);
                    if (RegisterProt.this.res == null) {
                        RegisterProt.this.mHandler.obtainMessage(0).sendToTarget();
                    } else if (RegisterProt.this.res.getCode() == 0) {
                        List<HashMap<String, String>> list = new ArrayList();
                        HashMap<String, String> map = new HashMap();
                        map.put(MultipleAddresses.CC, MySharedPreferences.getStringValue(RegisterProt.this.context, MySharedPreferences.CCKey));
                        map.put(Constants.serialNo, RegisterProt.this.portNum);
                        list.add(map);
                        DBDao.getInstance(RegisterProt.this.context).addSerialNo(list, MainActivity.database);
                        MySharedPreferences.setString(RegisterProt.this.context, MySharedPreferences.serialNoKey, RegisterProt.this.portNum);
                        RegisterProt.this.mHandler.sendEmptyMessage(1);
                    } else {
                        RegisterProt.this.mHandler.sendMessage(RegisterProt.this.mHandler.obtainMessage(2, RegisterProt.this.res));
                    }
                } catch (SocketTimeoutException e) {
                    RegisterProt.this.mHandler.obtainMessage(0).sendToTarget();
                } catch (NullPointerException e2) {
                    RegisterProt.this.mHandler.obtainMessage(0).sendToTarget();
                }
            }
        }

        C06285() {
        }

        public void onClick(View v) {
            RegisterProt.this.hideKeyboard();
            RegisterProt.this.portNum = RegisterProt.this.portEd.getText().toString();
            String portStart = XmlPullParser.NO_NAMESPACE;
            if (RegisterProt.this.portNum.length() > 5) {
                portStart = RegisterProt.this.portNum.substring(0, 5);
            }
            RegisterProt.this.portPwd = RegisterProt.this.portPwdEd.getText().toString();
            if (RegisterProt.this.portNum.trim().equals(XmlPullParser.NO_NAMESPACE)) {
                Toast.makeText(RegisterProt.this.context, RegisterProt.this.context.getResources().getString(C0136R.string.port_input), 0).show();
            } else if (RegisterProt.this.portPwd.trim().equals(XmlPullParser.NO_NAMESPACE)) {
                Toast.makeText(RegisterProt.this.context, RegisterProt.this.context.getResources().getString(C0136R.string.port_input_pwd), 0).show();
            } else if (RegisterProt.this.portNum.length() != 12 || !Common.isNumber(RegisterProt.this.portNum) || !portStart.equalsIgnoreCase(MySharedPreferences.getStringValue(RegisterProt.this.context, "PORT_START"))) {
                RegisterProt.this.tvErrorNum.setVisibility(0);
                Toast.makeText(RegisterProt.this.context, RegisterProt.this.context.getResources().getString(C0136R.string.port_warn), 0).show();
            } else if (RegisterProt.this.portPwd.trim().equals(XmlPullParser.NO_NAMESPACE)) {
                RegisterProt.this.tvErrorPwd.setVisibility(0);
                Toast.makeText(RegisterProt.this.context, RegisterProt.this.context.getResources().getString(C0136R.string.port_input_pwd), 0).show();
            } else if (Common.isNetworkAvailable(RegisterProt.this.context)) {
                RegisterProt.this.showDialog(0);
                new Thread(new C06271()).start();
            } else {
                Toast.makeText(RegisterProt.this.context, RegisterProt.this.context.getResources().getString(C0136R.string.network), 0).show();
            }
        }
    }

    public RegisterProt() {
        this.venderCode = "86X";
        this.mHandler = new C06231();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.login_pop_register);
        this.context = this;
        initView();
    }

    public void initView() {
        this.portEd = (EditText) findViewById(C0136R.id.login_pop_register_serial_num);
        this.portPwdEd = (EditText) findViewById(C0136R.id.login_pop_register_serial_password);
        this.close = (ImageView) findViewById(C0136R.id.login_pop_register_close);
        this.submit = (Button) findViewById(C0136R.id.login_pop_register_sure);
        this.tvErrorNum = (TextView) findViewById(C0136R.id.login_pop_register_tishi_num);
        this.tvErrorPwd = (TextView) findViewById(C0136R.id.login_pop_register_tishi_pwd);
        this.close.setOnClickListener(new C06242());
        this.portEd.addTextChangedListener(new C06253());
        this.portPwdEd.addTextChangedListener(new C06264());
        this.submit.setOnClickListener(new C06285());
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case KEYRecord.OWNER_USER /*0*/:
                return ProgressDialog.show(this.context, null, this.context.getString(C0136R.string.port_now));
            default:
                return super.onCreateDialog(id);
        }
    }

    private void hideKeyboard() {
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.portPwdEd.getWindowToken(), 0);
    }
}

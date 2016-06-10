package com.ifoer.expeditionphone;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.util.MyApplication;
import com.ifoer.util.MySharedPreferences;
import java.util.Timer;
import java.util.TimerTask;
import org.xmlpull.v1.XmlPullParser;

public class DiagnoseActivity extends BaseActivity {
    private EditText acEditText;
    private EditText editAc;
    private GridView gridView;
    private ImageView imageRight;
    private Button left;
    private Button login;
    private EditText loginPwd;
    private Button login_close;
    private RelativeLayout login_lay;
    private Handler mHandler;
    private CheckBox notic_check;
    private RelativeLayout notic_lay;
    private PopupWindow popupWindow;
    private EditText pwdEditText;
    private TextView pwdReset;
    private Button readBtn;
    private Button regButton;
    private Button register_close;
    private RelativeLayout register_lay;
    private CheckBox remberPwd;
    private Button reput_btn;
    private CheckBox reput_check;
    private ImageView reput_close;
    private RelativeLayout reput_lay;
    private EditText repwdEditText;
    private TextView show_name;
    private LinearLayout zhuce;

    /* renamed from: com.ifoer.expeditionphone.DiagnoseActivity.1 */
    class C05261 extends Handler {
        C05261() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    DiagnoseActivity.this.reputLay();
                default:
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DiagnoseActivity.2 */
    class C05272 implements OnClickListener {
        C05272() {
        }

        public void onClick(View v) {
            System.out.println("----------");
            DiagnoseActivity.this.popupWindow.dismiss();
            DiagnoseActivity.this.initPop(DiagnoseActivity.this.login_lay);
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DiagnoseActivity.3 */
    class C05283 implements OnClickListener {
        C05283() {
        }

        public void onClick(View v) {
            System.out.println("===++++++++++++");
            DiagnoseActivity.this.popupWindow.dismiss();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DiagnoseActivity.4 */
    class C05294 implements OnClickListener {
        C05294() {
        }

        public void onClick(View v) {
            System.out.println("\u767b\u9646----");
            DiagnoseActivity.this.popupWindow.dismiss();
            DiagnoseActivity.this.initPop(DiagnoseActivity.this.notic_lay);
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DiagnoseActivity.5 */
    class C05305 implements OnClickListener {
        C05305() {
        }

        public void onClick(View v) {
            System.out.println("\u6ce8\u518c----");
            DiagnoseActivity.this.popupWindow.dismiss();
            DiagnoseActivity.this.initPop(DiagnoseActivity.this.register_lay);
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DiagnoseActivity.6 */
    class C05316 implements OnClickListener {
        C05316() {
        }

        public void onClick(View v) {
            System.out.println("\u6ce8\u518c----");
            DiagnoseActivity.this.popupWindow.dismiss();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.DiagnoseActivity.7 */
    class C05327 implements OnClickListener {
        C05327() {
        }

        public void onClick(View v) {
            DiagnoseActivity.this.popupWindow.dismiss();
        }
    }

    private class initPopupWindow extends TimerTask {
        private initPopupWindow() {
        }

        public void run() {
            Message message = new Message();
            message.what = 1;
            DiagnoseActivity.this.mHandler.sendMessage(message);
        }
    }

    public DiagnoseActivity() {
        this.mHandler = new C05261();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.base_diagnose);
        MyApplication.getInstance().addActivity(this);
        if (MySharedPreferences.share == null) {
            MySharedPreferences.getSharedPref(getApplicationContext());
        }
        this.show_name = (TextView) findViewById(C0136R.id.show_name);
        this.show_name.setVisibility(0);
        String name = MySharedPreferences.getStringValue(this, "usernames");
        if (!(name == null || XmlPullParser.NO_NAMESPACE.equals(name))) {
            this.show_name.setText(name);
        }
        checkIfFirstCome();
    }

    private void checkIfFirstCome() {
        if (false) {
            new Timer().schedule(new initPopupWindow(), 100);
        }
    }

    private void reputLay() {
        LayoutInflater mLayoutInflater = LayoutInflater.from(this);
        this.reput_lay = (RelativeLayout) mLayoutInflater.inflate(C0136R.layout.reputation_pop, null);
        this.login_lay = (RelativeLayout) mLayoutInflater.inflate(C0136R.layout.login_pop, null);
        this.register_lay = (RelativeLayout) mLayoutInflater.inflate(C0136R.layout.register_pop, null);
        this.notic_lay = (RelativeLayout) mLayoutInflater.inflate(C0136R.layout.main_notic, null);
        initPop(this.reput_lay);
        findReptLayView();
        findLoginView();
        findRegisterView();
        findNotionView();
    }

    private void initPop(View view) {
        this.popupWindow = new PopupWindow(view, -1, -1, true);
        this.popupWindow.setOutsideTouchable(false);
        this.popupWindow.setBackgroundDrawable(new BitmapDrawable());
        this.popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        this.popupWindow.showAtLocation(findViewById(C0136R.id.gridView), 17, 0, 0);
        this.popupWindow.update();
    }

    private void findReptLayView() {
        this.reput_close = (ImageView) this.reput_lay.findViewById(C0136R.id.repu_close);
        this.reput_check = (CheckBox) this.reput_lay.findViewById(C0136R.id.repu_select);
        this.reput_btn = (Button) this.reput_lay.findViewById(C0136R.id.repu_btn);
        this.reput_btn.setOnClickListener(new C05272());
        this.reput_close.setOnClickListener(new C05283());
    }

    private void findLoginView() {
        this.editAc = (EditText) this.login_lay.findViewById(C0136R.id.name);
        this.loginPwd = (EditText) this.login_lay.findViewById(C0136R.id.password);
        this.remberPwd = (CheckBox) this.login_lay.findViewById(C0136R.id.check);
        this.pwdReset = (TextView) this.login_lay.findViewById(C0136R.id.pwdreset);
        this.login = (Button) this.login_lay.findViewById(C0136R.id.login_btn);
        this.login.setOnClickListener(new C05294());
        this.zhuce = (LinearLayout) this.login_lay.findViewById(C0136R.id.text_zhuce);
        this.zhuce.setOnClickListener(new C05305());
    }

    private void findRegisterView() {
        this.acEditText = (EditText) this.register_lay.findViewById(C0136R.id.account);
        this.pwdEditText = (EditText) this.register_lay.findViewById(C0136R.id.pwd);
        this.repwdEditText = (EditText) this.register_lay.findViewById(C0136R.id.regpwd);
        this.left = (Button) this.register_lay.findViewById(C0136R.id.left);
        this.regButton = (Button) this.register_lay.findViewById(C0136R.id.registerbtn);
        this.regButton.setOnClickListener(new C05316());
    }

    private void findNotionView() {
        this.notic_check = (CheckBox) this.notic_lay.findViewById(C0136R.id.main_selected);
        this.readBtn = (Button) this.notic_lay.findViewById(C0136R.id.readbtn);
        this.readBtn.setOnClickListener(new C05327());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0136R.menu.main, menu);
        return true;
    }
}

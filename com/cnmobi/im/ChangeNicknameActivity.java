package com.cnmobi.im;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.util.UiUtils;
import com.cnmobi.im.util.XmppConnection;
import org.xmlpull.v1.XmlPullParser;

public class ChangeNicknameActivity extends Activity {
    private static final int CHANGE_FAIL = 101;
    private static final int CHANGE_SUCCESS = 100;
    private static final String TAG = "ChangeNicknameActivity";
    private Button mChangeNicknameBtn;
    private Handler mHandler;
    private String mJid;
    private EditText mNickname;
    private String mOldNickname;
    private TextView mTopBarTitle;

    /* renamed from: com.cnmobi.im.ChangeNicknameActivity.1 */
    class C01411 extends Handler {
        C01411() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ChangeNicknameActivity.CHANGE_SUCCESS /*100*/:
                    Toast.makeText(ChangeNicknameActivity.this, ChangeNicknameActivity.this.getString(C0136R.string.changeNickNameSuccess), 0).show();
                    ChangeNicknameActivity.this.finish();
                case ChangeNicknameActivity.CHANGE_FAIL /*101*/:
                    Toast.makeText(ChangeNicknameActivity.this, ChangeNicknameActivity.this.getString(C0136R.string.changeNickNameFail), 0).show();
                default:
            }
        }
    }

    /* renamed from: com.cnmobi.im.ChangeNicknameActivity.2 */
    class C01422 implements OnClickListener {
        C01422() {
        }

        public void onClick(View v) {
            String riderNumberStr = ChangeNicknameActivity.this.mNickname.getText().toString().trim();
            if (XmlPullParser.NO_NAMESPACE.endsWith(riderNumberStr)) {
                Toast.makeText(ChangeNicknameActivity.this, ChangeNicknameActivity.this.getString(C0136R.string.enterNickName), 0).show();
                return;
            }
            InputMethodManager imm = (InputMethodManager) ChangeNicknameActivity.this.getSystemService("input_method");
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(ChangeNicknameActivity.this.mNickname.getWindowToken(), 0);
            }
            new ChangeNicknameThread(riderNumberStr, null).start();
        }
    }

    class ChangeNicknameThread extends Thread {
        private String nickName;

        private ChangeNicknameThread(String nickName) {
            this.nickName = nickName;
        }

        public void run() {
            boolean result = false;
            try {
                XmppConnection.getConnection().getRoster().getEntry(ChangeNicknameActivity.this.mJid).setName(this.nickName);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result) {
                ChangeNicknameActivity.this.mHandler.sendEmptyMessage(ChangeNicknameActivity.CHANGE_SUCCESS);
            } else {
                ChangeNicknameActivity.this.mHandler.sendEmptyMessage(ChangeNicknameActivity.CHANGE_FAIL);
            }
        }
    }

    public ChangeNicknameActivity() {
        this.mHandler = new C01411();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.change_nickname);
        init();
    }

    private void init() {
        this.mTopBarTitle = (TextView) findViewById(C0136R.id.topBarTitle);
        this.mTopBarTitle.setText(getResources().getString(C0136R.string.changeNickName));
        UiUtils.enabledBackButton(this);
        this.mOldNickname = getIntent().getExtras().getString("RiderName");
        this.mJid = getIntent().getExtras().getString("jid");
        this.mNickname = (EditText) findViewById(C0136R.id.nickname);
        this.mNickname.setText(this.mOldNickname);
        this.mChangeNicknameBtn = (Button) findViewById(C0136R.id.changeNicknameBtn);
        this.mChangeNicknameBtn.setOnClickListener(new C01422());
    }
}

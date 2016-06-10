package com.cnmobi.im;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.bo.ImConstants;
import com.cnmobi.im.bo.MsgCallback;
import com.cnmobi.im.bo.MsgManager;
import com.cnmobi.im.dto.MessageVo;
import com.cnmobi.im.util.FileConstant;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.ifoer.util.MyApplication;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.SmackAndroid;

public class ImMainActivity extends TabActivity implements MsgCallback {
    private static final String TAG = "ImMainActivity";
    public static Context context;
    private static boolean isAlive;
    public static String mOwerJid;
    public static String mUserId;
    public static String xmppShowUserName;
    public static String xmppUser;
    private Handler mHandler;
    private TabHost mHost;
    private MediaPlayer mMediaPlayer;
    private TextView mNewMsgView;
    private SmackAndroid mSmackAndroid;
    private String mUserPwd;
    private Vibrator mVibrator;
    private List<RadioButton> radioButtons;
    OnCheckedChangeListener radioListener;
    private final int refreshTime;
    private SharedPreferences sp;

    /* renamed from: com.cnmobi.im.ImMainActivity.1 */
    class C01661 extends Handler {
        C01661() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                    int newMsgCount = MsgManager.getInstance().getUnReadCount();
                    if (newMsgCount > 0) {
                        ImMainActivity.this.mNewMsgView.setText(String.valueOf(newMsgCount));
                        ImMainActivity.this.mNewMsgView.setVisibility(0);
                    } else {
                        ImMainActivity.this.mNewMsgView.setVisibility(4);
                    }
                    if (msg.obj != null) {
                        MessageVo message = msg.obj;
                        ImMainActivity.this.ringShock();
                        if (!ImMainActivity.isAlive) {
                            ImMainActivity.this.notification(message.content);
                        }
                    }
                case ParseCharStream.HISTORY_LENGTH /*100*/:
                    Toast.makeText(ImMainActivity.this, ImMainActivity.this.getString(C0136R.string.connectedImServerFail), 0).show();
                default:
            }
        }
    }

    /* renamed from: com.cnmobi.im.ImMainActivity.2 */
    class C01672 implements OnCheckedChangeListener {
        C01672() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                ImMainActivity.this.mHost.setCurrentTabByTag((String) buttonView.getTag());
                for (RadioButton button : ImMainActivity.this.radioButtons) {
                    if (button.getTag().equals(buttonView.getTag())) {
                        button.setChecked(true);
                    } else {
                        button.setChecked(false);
                    }
                }
            }
        }
    }

    public ImMainActivity() {
        this.radioButtons = new ArrayList();
        this.refreshTime = 1000;
        this.mUserPwd = "000000";
        this.mHandler = new C01661();
        this.radioListener = new C01672();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.im_main);
        MyApplication.getInstance().addActivity(this);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                mUserId = bundle.getString(ImConstants.USER_ID);
            }
        }
        if (mUserId == null) {
            mUserId = "10100";
            Log.w(TAG, "There is no user id,Please check,default 10100!");
        }
        mOwerJid = mUserId + "@im.x431.com";
        this.mHost = getTabHost();
        this.mHost.addTab(this.mHost.newTabSpec("ONE").setIndicator("ONE").setContent(new Intent(this, MessageActivity.class)));
        this.mHost.addTab(this.mHost.newTabSpec("TWO").setIndicator("TWO").setContent(new Intent(this, FriendActivity.class)));
        this.mHost.addTab(this.mHost.newTabSpec("THREE").setIndicator("THREE").setContent(new Intent(this, CircleActivity.class)));
        this.mHost.addTab(this.mHost.newTabSpec("FOUR").setIndicator("FOUR").setContent(new Intent(this, MineActivity.class)));
        loadRadioButtons();
        MsgManager.getInstance().addCallBack(this);
        this.mMediaPlayer = MediaPlayer.create(this, C0136R.raw.notificationsound);
        this.mVibrator = (Vibrator) getSystemService("vibrator");
        this.mSmackAndroid = SmackAndroid.init(this);
        this.mNewMsgView = (TextView) findViewById(C0136R.id.im_tab_new_msg);
        context = this;
        isAlive = true;
        this.sp = PreferenceManager.getDefaultSharedPreferences(this);
        File file = new File(FileConstant.FILE_HEAD_PORTRAIT);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void onResume() {
        isAlive = true;
        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
        this.mSmackAndroid.onDestroy();
        isAlive = false;
    }

    private void loadRadioButtons() {
        RadioButton radioButton = (RadioButton) findViewById(C0136R.id.im_tab_message);
        radioButton.setOnCheckedChangeListener(this.radioListener);
        radioButton.setTag("ONE");
        this.radioButtons.add(radioButton);
        radioButton = (RadioButton) findViewById(C0136R.id.im_tab_friend);
        radioButton.setOnCheckedChangeListener(this.radioListener);
        radioButton.setTag("TWO");
        this.radioButtons.add(radioButton);
        radioButton = (RadioButton) findViewById(C0136R.id.im_tab_circle);
        radioButton.setOnCheckedChangeListener(this.radioListener);
        radioButton.setTag("THREE");
        this.radioButtons.add(radioButton);
        radioButton = (RadioButton) findViewById(C0136R.id.im_tab_mine);
        radioButton.setOnCheckedChangeListener(this.radioListener);
        radioButton.setTag("FOUR");
        this.radioButtons.add(radioButton);
    }

    public void ringShock() {
        if (this.sp.getBoolean("ring", true) && !this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.start();
        }
        if (this.sp.getBoolean("shock", true)) {
            this.mVibrator.vibrate(1000);
        }
    }

    public void notification(String content) {
        NotificationManager nm = (NotificationManager) getSystemService("notification");
        Notification n = new Notification(C0136R.drawable.notification, getString(C0136R.string.imNewMessage), System.currentTimeMillis());
        n.setLatestEventInfo(this, getString(C0136R.string.app_name), content, PendingIntent.getActivity(this, 0, new Intent(this, ImMainActivity.class), 0));
        n.flags = 16;
        nm.notify(C0136R.id.JID, n);
    }

    public void msgChange(MessageVo msg) {
        Message message = this.mHandler.obtainMessage();
        message.obj = msg;
        message.what = 10;
        this.mHandler.sendMessage(message);
    }

    public void connect2XmppServer() {
        new XmppInitThread(this.mHandler, mUserId, this.mUserPwd).start();
    }
}

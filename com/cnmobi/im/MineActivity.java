package com.cnmobi.im;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.bo.LogoManager;
import com.cnmobi.im.cnlaunch.net.CnlaunchDataManager;
import com.cnmobi.im.dto.MeVo;
import com.cnmobi.im.util.UiUtils;
import org.xmlpull.v1.XmlPullParser;

public class MineActivity extends Activity {
    private static final int IMAGE_INFO_BACK = 101;
    private static final int ME_INFO_BACK = 100;
    public static MineActivity context;
    private LinearLayout mFirstView;
    private Handler mHandler;
    private View mHeadLayout;
    private ImageView mHeadView;
    private LinearLayout mSecondView;
    private ImageButton mSettingBtn;
    private TextView mTopBarTitle;

    /* renamed from: com.cnmobi.im.MineActivity.1 */
    class C01701 extends Handler {
        C01701() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MineActivity.ME_INFO_BACK /*100*/:
                    MeVo me = msg.obj;
                    ((TextView) MineActivity.this.findViewById(C0136R.id.me_name)).setText(me.getName());
                    ((TextView) MineActivity.this.findViewById(C0136R.id.me_account)).setText(me.getAccount());
                    ((TextView) MineActivity.this.findViewById(C0136R.id.me_sex)).setText(me.getSex());
                    ((TextView) MineActivity.this.findViewById(C0136R.id.me_area)).setText(me.getArea());
                    ((TextView) MineActivity.this.findViewById(C0136R.id.me_phone)).setText(me.getPhone());
                    ((TextView) MineActivity.this.findViewById(C0136R.id.me_email)).setText(me.getEmail());
                case MineActivity.IMAGE_INFO_BACK /*101*/:
                    MineActivity.this.mHeadView.setImageBitmap(msg.obj);
                default:
            }
        }
    }

    /* renamed from: com.cnmobi.im.MineActivity.2 */
    class C01712 implements OnClickListener {
        C01712() {
        }

        public void onClick(View v) {
            MineActivity.this.startActivity(new Intent(MineActivity.this, SettingActivity.class));
        }
    }

    /* renamed from: com.cnmobi.im.MineActivity.3 */
    class C01723 implements OnClickListener {
        C01723() {
        }

        public void onClick(View v) {
            MineActivity.this.startActivity(new Intent(MineActivity.this, PicMenuDialog.class));
        }
    }

    class GetLogoThread extends Thread {
        GetLogoThread() {
        }

        public void run() {
            try {
                Bitmap bitmap = LogoManager.getInstance().getBitmapByJid(ImMainActivity.mOwerJid);
                if (bitmap != null) {
                    Message msg = MineActivity.this.mHandler.obtainMessage();
                    msg.what = MineActivity.IMAGE_INFO_BACK;
                    msg.obj = bitmap;
                    MineActivity.this.mHandler.sendMessage(msg);
                }
            } catch (Exception e) {
            }
        }
    }

    class GetMeInfoThread extends Thread {
        GetMeInfoThread() {
        }

        public void run() {
            MeVo me = CnlaunchDataManager.getInstance().getMeInfo(ImMainActivity.mUserId);
            Message msg = MineActivity.this.mHandler.obtainMessage();
            msg.obj = me;
            msg.what = MineActivity.ME_INFO_BACK;
            MineActivity.this.mHandler.sendMessage(msg);
        }
    }

    public MineActivity() {
        this.mHandler = new C01701();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.im_tab_mine);
        init();
        this.mTopBarTitle.setText(getResources().getString(C0136R.string.mine));
        context = this;
        new GetMeInfoThread().start();
    }

    private void init() {
        this.mTopBarTitle = (TextView) findViewById(C0136R.id.topBarTitle);
        UiUtils.enabledBackButton(this);
        this.mSettingBtn = (ImageButton) findViewById(C0136R.id.titleRightBtn);
        this.mSettingBtn.setImageResource(C0136R.drawable.im_setting_btn);
        this.mSettingBtn.setVisibility(0);
        this.mSettingBtn.setOnClickListener(new C01712());
        this.mFirstView = (LinearLayout) findViewById(C0136R.id.firstArea);
        this.mFirstView.addView(getItemView(getString(C0136R.string.name), XmlPullParser.NO_NAMESPACE, C0136R.id.me_name, true));
        this.mFirstView.addView(getItemView(getString(C0136R.string.account), XmlPullParser.NO_NAMESPACE, C0136R.id.me_account, true));
        this.mFirstView.addView(getItemView(getString(C0136R.string.ccNumber), ImMainActivity.mUserId, 0, false));
        this.mSecondView = (LinearLayout) findViewById(C0136R.id.secondArea);
        this.mSecondView.addView(getItemView(getString(C0136R.string.sex), XmlPullParser.NO_NAMESPACE, C0136R.id.me_sex, true));
        this.mSecondView.addView(getItemView(getString(C0136R.string.area), XmlPullParser.NO_NAMESPACE, C0136R.id.me_area, true));
        this.mSecondView.addView(getItemView(getString(C0136R.string.phone), XmlPullParser.NO_NAMESPACE, C0136R.id.me_phone, true));
        this.mSecondView.addView(getItemView(getString(C0136R.string.email), XmlPullParser.NO_NAMESPACE, C0136R.id.me_email, false));
        this.mHeadLayout = findViewById(C0136R.id.headLayout);
        this.mHeadLayout.setOnClickListener(new C01723());
        this.mHeadView = (ImageView) findViewById(C0136R.id.head);
        getLogo();
    }

    public void getLogo() {
        new GetLogoThread().start();
    }

    private View getItemView(String title, String value, int valueViewId, boolean isShowLine) {
        View view = LayoutInflater.from(this).inflate(C0136R.layout.im_area_item, null);
        ((TextView) view.findViewById(C0136R.id.title)).setText(title);
        TextView tv = (TextView) view.findViewById(C0136R.id.value);
        if (valueViewId != 0) {
            tv.setId(valueViewId);
        }
        tv.setText(value);
        tv = (TextView) view.findViewById(C0136R.id.line);
        if (isShowLine) {
            tv.setVisibility(0);
        } else {
            tv.setVisibility(8);
        }
        return view;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}

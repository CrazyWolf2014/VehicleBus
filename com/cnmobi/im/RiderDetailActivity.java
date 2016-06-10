package com.cnmobi.im;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.bo.LogoManager;
import com.cnmobi.im.cnlaunch.net.CnlaunchDataManager;
import com.cnmobi.im.dao.RiderDao;
import com.cnmobi.im.dto.MeVo;
import com.cnmobi.im.dto.RiderVo;
import com.cnmobi.im.util.UiUtils;
import com.cnmobi.im.util.XmppConnection;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xmlpull.v1.XmlPullParser;

public class RiderDetailActivity extends Activity {
    public static final String ACTION = "action";
    public static final int ACTION_ADD = 1;
    public static final int ACTION_CHECK = 2;
    private static final int ADD_RIDER_FAIL = 101;
    private static final int ADD_RIDER_SUCCESS = 100;
    private static final int LOGO_INFO_BACK = 103;
    private static final int ME_INFO_BACK = 102;
    private static final String TAG = "RiderDetailActivity";
    private int mActionType;
    private String mCCNumber;
    private Button mChangeNicknameBtn;
    private LinearLayout mFirstView;
    private Handler mHandler;
    private String mJid;
    private ImageView mLogoView;
    private ImageButton mRemoveBtn;
    private Button mRiderBtn;
    private String mRiderName;
    private LinearLayout mSecondView;
    private TextView mTopBarTitle;

    /* renamed from: com.cnmobi.im.RiderDetailActivity.1 */
    class C01811 extends Handler {
        C01811() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RiderDetailActivity.ADD_RIDER_SUCCESS /*100*/:
                    Toast.makeText(RiderDetailActivity.this, RiderDetailActivity.this.getString(C0136R.string.addFriendRequestSend), 0).show();
                    RiderDetailActivity.this.finish();
                case RiderDetailActivity.ADD_RIDER_FAIL /*101*/:
                    Toast.makeText(RiderDetailActivity.this, RiderDetailActivity.this.getString(C0136R.string.addRiderFailTips), 0).show();
                case RiderDetailActivity.ME_INFO_BACK /*102*/:
                    MeVo me = msg.obj;
                    ((TextView) RiderDetailActivity.this.findViewById(C0136R.id.me_name)).setText(me.getName());
                    ((TextView) RiderDetailActivity.this.findViewById(C0136R.id.me_account)).setText(me.getAccount());
                    ((TextView) RiderDetailActivity.this.findViewById(C0136R.id.me_sex)).setText(me.getSex());
                    ((TextView) RiderDetailActivity.this.findViewById(C0136R.id.me_area)).setText(me.getArea());
                    ((TextView) RiderDetailActivity.this.findViewById(C0136R.id.me_phone)).setText(me.getPhone());
                    ((TextView) RiderDetailActivity.this.findViewById(C0136R.id.me_email)).setText(me.getEmail());
                case RiderDetailActivity.LOGO_INFO_BACK /*103*/:
                    Bitmap bitmap = msg.obj;
                    if (bitmap != null) {
                        RiderDetailActivity.this.mLogoView.setImageBitmap(bitmap);
                    }
                default:
            }
        }
    }

    /* renamed from: com.cnmobi.im.RiderDetailActivity.2 */
    class C01822 implements OnClickListener {
        C01822() {
        }

        public void onClick(View v) {
            new AddRiderThread(RiderDetailActivity.this.mRiderName, RiderDetailActivity.this.mJid, null).start();
        }
    }

    /* renamed from: com.cnmobi.im.RiderDetailActivity.3 */
    class C01833 implements OnClickListener {
        C01833() {
        }

        public void onClick(View v) {
            String userName = RiderDetailActivity.this.mRiderName;
            String JID = RiderDetailActivity.this.mJid;
            Intent intent = new Intent(RiderDetailActivity.this, ChatActivity.class);
            intent.putExtra("TALKTO", userName);
            intent.putExtra("JID", JID);
            RiderDetailActivity.this.startActivity(intent);
        }
    }

    /* renamed from: com.cnmobi.im.RiderDetailActivity.4 */
    class C01854 implements OnClickListener {

        /* renamed from: com.cnmobi.im.RiderDetailActivity.4.1 */
        class C01841 implements DialogInterface.OnClickListener {
            C01841() {
            }

            public void onClick(DialogInterface dialog, int which) {
                Roster roster = XmppConnection.getConnection().getRoster();
                if (roster == null || roster.getEntry(RiderDetailActivity.this.mJid) == null) {
                    Toast.makeText(RiderDetailActivity.this, RiderDetailActivity.this.getString(C0136R.string.connectedServerException), 0).show();
                    return;
                }
                try {
                    RiderDao.getInstance().delete(RiderDetailActivity.this.mJid, ImMainActivity.mOwerJid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    roster.removeEntry(roster.getEntry(RiderDetailActivity.this.mJid));
                    Toast.makeText(RiderDetailActivity.this, RiderDetailActivity.this.getString(C0136R.string.deleteFriendSuccess), 0).show();
                    RiderDetailActivity.this.finish();
                } catch (XMPPException e2) {
                    e2.printStackTrace();
                }
            }
        }

        C01854() {
        }

        public void onClick(View v) {
            new Builder(RiderDetailActivity.this).setMessage(RiderDetailActivity.this.getString(C0136R.string.imSureDeleteFriend)).setPositiveButton(RiderDetailActivity.this.getString(C0136R.string.imConfirm), new C01841()).setNegativeButton(RiderDetailActivity.this.getString(C0136R.string.imCancle), null).show();
        }
    }

    /* renamed from: com.cnmobi.im.RiderDetailActivity.5 */
    class C01865 implements OnClickListener {
        C01865() {
        }

        public void onClick(View v) {
            Intent intent = new Intent(RiderDetailActivity.this, ChangeNicknameActivity.class);
            intent.putExtra("RiderName", RiderDetailActivity.this.mRiderName);
            intent.putExtra("jid", RiderDetailActivity.this.mJid);
            RiderDetailActivity.this.startActivity(intent);
        }
    }

    class AddRiderThread extends Thread {
        private String jid;
        private String riderName;

        private AddRiderThread(String riderName, String jid) {
            this.riderName = riderName;
            this.jid = jid;
        }

        public void run() {
            boolean addResult = false;
            try {
                XmppConnection.getConnection().getRoster().createEntry(this.jid, this.riderName, null);
                addResult = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (addResult) {
                RiderDetailActivity.this.mHandler.sendEmptyMessage(RiderDetailActivity.ADD_RIDER_SUCCESS);
            } else {
                RiderDetailActivity.this.mHandler.sendEmptyMessage(RiderDetailActivity.ADD_RIDER_FAIL);
            }
        }
    }

    class GetMeInfoThread extends Thread {
        private String ccNumber;
        private String jid;

        public GetMeInfoThread(String ccNumber, String jid) {
            this.ccNumber = ccNumber;
            this.jid = jid;
        }

        public void run() {
            MeVo me = CnlaunchDataManager.getInstance().getMeInfo(this.ccNumber);
            Message msg = RiderDetailActivity.this.mHandler.obtainMessage();
            msg.obj = me;
            msg.what = RiderDetailActivity.ME_INFO_BACK;
            RiderDetailActivity.this.mHandler.sendMessage(msg);
            Bitmap bitmap = LogoManager.getInstance().getBitmapByJid(this.jid);
            msg = RiderDetailActivity.this.mHandler.obtainMessage();
            msg.obj = bitmap;
            msg.what = RiderDetailActivity.LOGO_INFO_BACK;
            RiderDetailActivity.this.mHandler.sendMessage(msg);
        }
    }

    public RiderDetailActivity() {
        this.mHandler = new C01811();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.im_rider_detail);
        init();
        new GetMeInfoThread(this.mCCNumber, this.mJid).start();
    }

    private void init() {
        UiUtils.enabledBackButton(this);
        this.mTopBarTitle = (TextView) findViewById(C0136R.id.topBarTitle);
        this.mRiderBtn = (Button) findViewById(C0136R.id.riderBtn);
        this.mChangeNicknameBtn = (Button) findViewById(C0136R.id.chageNickNameBtn);
        this.mRiderName = getIntent().getExtras().getString("RiderName");
        this.mJid = getIntent().getExtras().getString("jid");
        this.mActionType = getIntent().getExtras().getInt(ACTION);
        this.mLogoView = (ImageView) findViewById(C0136R.id.head);
        this.mCCNumber = UiUtils.getCCNumber(this.mJid);
        switch (this.mActionType) {
            case ACTION_ADD /*1*/:
                this.mTopBarTitle.setText(C0136R.string.addRider);
                this.mRiderBtn.setText(C0136R.string.addRider);
                this.mRiderBtn.setOnClickListener(new C01822());
                try {
                    RiderVo rider = RiderDao.getInstance().getRiderByJid(this.mJid, ImMainActivity.mOwerJid);
                    boolean isShow = true;
                    if ((rider != null && (PrivacyRule.SUBSCRIPTION_BOTH.equals(rider.type) || MultipleAddresses.TO.equals(rider.type))) || this.mJid.equals(ImMainActivity.mOwerJid)) {
                        isShow = false;
                    }
                    if (!isShow) {
                        this.mRiderBtn.setVisibility(8);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
                break;
            case ACTION_CHECK /*2*/:
                this.mTopBarTitle.setText(C0136R.string.riderDetail);
                this.mRiderBtn.setText(C0136R.string.sendMessage);
                this.mRiderBtn.setOnClickListener(new C01833());
                this.mRemoveBtn = (ImageButton) findViewById(C0136R.id.titleRightBtn);
                this.mRemoveBtn.setImageResource(C0136R.drawable.im_clean_btn);
                this.mRemoveBtn.setVisibility(0);
                this.mRemoveBtn.setOnClickListener(new C01854());
                this.mChangeNicknameBtn.setVisibility(0);
                this.mChangeNicknameBtn.setOnClickListener(new C01865());
                break;
        }
        this.mFirstView = (LinearLayout) findViewById(C0136R.id.firstArea);
        this.mFirstView.addView(getItemView(getString(C0136R.string.name), XmlPullParser.NO_NAMESPACE, C0136R.id.me_name, true));
        this.mFirstView.addView(getItemView(getString(C0136R.string.account), XmlPullParser.NO_NAMESPACE, C0136R.id.me_account, true));
        this.mFirstView.addView(getItemView(getString(C0136R.string.ccNumber), this.mCCNumber, 0, false));
        this.mSecondView = (LinearLayout) findViewById(C0136R.id.secondArea);
        this.mSecondView.addView(getItemView(getString(C0136R.string.sex), XmlPullParser.NO_NAMESPACE, C0136R.id.me_sex, true));
        this.mSecondView.addView(getItemView(getString(C0136R.string.area), XmlPullParser.NO_NAMESPACE, C0136R.id.me_area, true));
        this.mSecondView.addView(getItemView(getString(C0136R.string.phone), XmlPullParser.NO_NAMESPACE, C0136R.id.me_phone, true));
        this.mSecondView.addView(getItemView(getString(C0136R.string.email), XmlPullParser.NO_NAMESPACE, C0136R.id.me_email, false));
        this.mSecondView.setVisibility(8);
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
}

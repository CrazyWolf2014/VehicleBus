package com.cnmobi.im;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.bo.CnmobiImListener;
import com.cnmobi.im.bo.CnmobiImManager;
import com.cnmobi.im.bo.LogoManager;
import com.cnmobi.im.bo.LogoManager.LogoBackListener;
import com.cnmobi.im.bo.MsgManager;
import com.cnmobi.im.bo.PresenceCallback;
import com.cnmobi.im.dao.RiderDao;
import com.cnmobi.im.dto.RiderVo;
import com.cnmobi.im.util.UiUtils;
import com.ifoer.entity.Constant;
import java.util.Collection;
import java.util.List;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;

public class FriendActivity extends Activity implements CnmobiImListener, PresenceCallback, LogoBackListener {
    private static final int FRIEND_LIST_CHANGE = 100;
    private static final int NEW_FRIEND_NUMBER_CHANGE = 101;
    private static final int RIDER_LOGO_BACK = 102;
    private static final String TAG = "FriendActivity";
    private RiderItemAdapter mAdapter;
    private ImageButton mAddFriendBtn;
    private ListView mFriendList;
    private Handler mHandler;
    private TextView mNewFriendNum;
    private TextView mTopBarTitle;
    private View newFriendArea;

    /* renamed from: com.cnmobi.im.FriendActivity.1 */
    class C01621 extends Handler {
        C01621() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FriendActivity.FRIEND_LIST_CHANGE /*100*/:
                    FriendActivity.this.mAdapter.resetRiders(msg.obj);
                    FriendActivity.this.mAdapter.notifyDataSetChanged();
                case FriendActivity.NEW_FRIEND_NUMBER_CHANGE /*101*/:
                    int newFriendNum = msg.arg1;
                    FriendActivity.this.mNewFriendNum.setText(String.valueOf(newFriendNum));
                    if (newFriendNum > 0) {
                        FriendActivity.this.newFriendArea.setVisibility(0);
                        ImMainActivity.context.ringShock();
                        return;
                    }
                    FriendActivity.this.newFriendArea.setVisibility(8);
                case FriendActivity.RIDER_LOGO_BACK /*102*/:
                    LogoHolder holder = msg.obj;
                    ImageView iv = FriendActivity.this.mAdapter.getLogoView(holder.jid);
                    if (iv == null) {
                        return;
                    }
                    if (holder.bitmap != null) {
                        iv.setImageBitmap(holder.bitmap);
                    } else {
                        iv.setImageResource(C0136R.drawable.default_logo);
                    }
                default:
            }
        }
    }

    /* renamed from: com.cnmobi.im.FriendActivity.2 */
    class C01632 implements OnClickListener {
        C01632() {
        }

        public void onClick(View v) {
            FriendActivity.this.startActivity(new Intent(FriendActivity.this, SearchRiderActivity.class));
        }
    }

    /* renamed from: com.cnmobi.im.FriendActivity.3 */
    class C01643 implements OnItemClickListener {
        C01643() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int arg2, long arg3) {
            String riderName = (String) view.getTag(C0136R.id.riderName);
            String jid = (String) view.getTag(C0136R.id.JID);
            Intent intent = new Intent(FriendActivity.this, RiderDetailActivity.class);
            intent.putExtra("RiderName", riderName);
            intent.putExtra("jid", jid);
            intent.putExtra(Constant.ACTION, 2);
            FriendActivity.this.startActivity(intent);
        }
    }

    /* renamed from: com.cnmobi.im.FriendActivity.4 */
    class C01654 implements OnClickListener {
        C01654() {
        }

        public void onClick(View v) {
            FriendActivity.this.startActivity(new Intent(FriendActivity.this, NewFriendListActivity.class));
        }
    }

    public class GetFriendRunnable extends Thread {
        public void run() {
            List<RiderVo> riders = null;
            try {
                riders = CnmobiImManager.getInstance().getRiders(ImMainActivity.mOwerJid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FriendActivity.this.riderChange(riders);
        }
    }

    class LogoHolder {
        Bitmap bitmap;
        String jid;

        LogoHolder() {
        }
    }

    public FriendActivity() {
        this.mHandler = new C01621();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.im_tab_friend);
        init();
        this.mTopBarTitle.setText(getResources().getString(C0136R.string.friend));
        CnmobiImManager.getInstance().addImListener(TAG, this);
        MsgManager.getInstance().addPresenceCallBack(this);
        LogoManager.getInstance().add(this);
        new GetFriendRunnable().start();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onDestroy() {
        CnmobiImManager.getInstance().removeImListener(TAG);
        super.onDestroy();
        MsgManager.getInstance().removePresenceCallBack(this);
        LogoManager.getInstance().remove(this);
    }

    private void init() {
        this.mTopBarTitle = (TextView) findViewById(C0136R.id.topBarTitle);
        this.mFriendList = (ListView) findViewById(C0136R.id.friendList);
        UiUtils.enabledBackButton(this);
        this.mAddFriendBtn = (ImageButton) findViewById(C0136R.id.titleRightBtn);
        this.mAddFriendBtn.setImageResource(C0136R.drawable.add_friend_btn);
        this.mAddFriendBtn.setVisibility(0);
        this.mAddFriendBtn.setOnClickListener(new C01632());
        this.mAdapter = new RiderItemAdapter(this, true, false);
        this.mFriendList.setAdapter(this.mAdapter);
        this.mFriendList.setOnItemClickListener(new C01643());
        this.newFriendArea = findViewById(C0136R.id.newFriendArea);
        this.newFriendArea.setOnClickListener(new C01654());
        this.mNewFriendNum = (TextView) findViewById(C0136R.id.newFriendNum);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public void riderChange(List<RiderVo> riders) {
        Message msg = this.mHandler.obtainMessage();
        msg.what = FRIEND_LIST_CHANGE;
        msg.obj = riders;
        this.mHandler.sendMessage(msg);
        int newFriendNum = 0;
        try {
            List<RiderVo> newRiders = RiderDao.getInstance().getRiders(ImMainActivity.mOwerJid, PrivacyRule.SUBSCRIPTION_FROM);
            if (newRiders != null) {
                newFriendNum = newRiders.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        msg = this.mHandler.obtainMessage();
        msg.what = NEW_FRIEND_NUMBER_CHANGE;
        msg.arg1 = newFriendNum;
        this.mHandler.sendMessage(msg);
    }

    public void latelyRiderChange(List<RiderVo> list) {
    }

    public void presenceChange(Presence presence) {
        List<RiderVo> riders = null;
        try {
            riders = CnmobiImManager.getInstance().getRiders(ImMainActivity.mOwerJid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Message msg = this.mHandler.obtainMessage();
        msg.what = FRIEND_LIST_CHANGE;
        msg.obj = riders;
        this.mHandler.sendMessage(msg);
        int newFriendNum = 0;
        try {
            List<RiderVo> newRiders = RiderDao.getInstance().getRiders(ImMainActivity.mOwerJid, PrivacyRule.SUBSCRIPTION_FROM);
            if (newRiders != null) {
                newFriendNum = newRiders.size();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        msg = this.mHandler.obtainMessage();
        msg.what = NEW_FRIEND_NUMBER_CHANGE;
        msg.arg1 = newFriendNum;
        this.mHandler.sendMessage(msg);
    }

    public void entriesChange(Collection<String> collection, int type) {
        List<RiderVo> riders = null;
        try {
            riders = CnmobiImManager.getInstance().getRiders(ImMainActivity.mOwerJid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Message msg = this.mHandler.obtainMessage();
        msg.what = FRIEND_LIST_CHANGE;
        msg.obj = riders;
        this.mHandler.sendMessage(msg);
        int newFriendNum = 0;
        try {
            List<RiderVo> newRiders = RiderDao.getInstance().getRiders(ImMainActivity.mOwerJid, PrivacyRule.SUBSCRIPTION_FROM);
            if (newRiders != null) {
                newFriendNum = newRiders.size();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        msg = this.mHandler.obtainMessage();
        msg.what = NEW_FRIEND_NUMBER_CHANGE;
        msg.arg1 = newFriendNum;
        this.mHandler.sendMessage(msg);
    }

    public void logoback(String jid, Bitmap bitmap) {
        LogoHolder holder = new LogoHolder();
        holder.jid = jid;
        holder.bitmap = bitmap;
        Message msg = this.mHandler.obtainMessage();
        msg.what = RIDER_LOGO_BACK;
        msg.obj = holder;
        this.mHandler.sendMessage(msg);
    }
}

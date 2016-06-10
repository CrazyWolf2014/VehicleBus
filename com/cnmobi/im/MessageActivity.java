package com.cnmobi.im;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.bo.CnmobiImListener;
import com.cnmobi.im.bo.CnmobiImManager;
import com.cnmobi.im.bo.LogoManager;
import com.cnmobi.im.bo.LogoManager.LogoBackListener;
import com.cnmobi.im.bo.MsgCallback;
import com.cnmobi.im.bo.MsgManager;
import com.cnmobi.im.dto.MessageVo;
import com.cnmobi.im.dto.RiderVo;
import com.cnmobi.im.receiver.NetworkStateListener;
import com.cnmobi.im.receiver.NetworkStateReceiver;
import com.cnmobi.im.util.UiUtils;
import java.util.List;

public class MessageActivity extends Activity implements CnmobiImListener, NetworkStateListener, MsgCallback, LogoBackListener {
    private static final int FRIEND_LIST_CHANGE = 100;
    private static final int NETWORK_STATUS_CHANGE = 1;
    private static final int RIDER_LOGO_BACK = 102;
    private static final String TAG = "MessageActivity";
    private RiderItemAdapter mAdapter;
    private ListView mFriendList;
    private Handler mHandler;
    private TextView mTopBarTitle;
    private NetworkStateReceiver networkStateReceiver;
    private View networkStatus;

    /* renamed from: com.cnmobi.im.MessageActivity.1 */
    class C01681 extends Handler {
        C01681() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessageActivity.NETWORK_STATUS_CHANGE /*1*/:
                    if (((Boolean) msg.obj).booleanValue()) {
                        MessageActivity.this.networkStatus.setVisibility(8);
                    } else {
                        MessageActivity.this.networkStatus.setVisibility(0);
                    }
                case MessageActivity.FRIEND_LIST_CHANGE /*100*/:
                    MessageActivity.this.mAdapter.resetRiders(msg.obj);
                    MessageActivity.this.mAdapter.notifyDataSetChanged();
                case MessageActivity.RIDER_LOGO_BACK /*102*/:
                    LogoHolder holder = msg.obj;
                    ImageView iv = MessageActivity.this.mAdapter.getLogoView(holder.jid);
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

    /* renamed from: com.cnmobi.im.MessageActivity.2 */
    class C01692 implements OnItemClickListener {
        C01692() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int arg2, long arg3) {
            String userName = (String) view.getTag(C0136R.id.riderName);
            String JID = (String) view.getTag(C0136R.id.JID);
            Intent intent = new Intent(MessageActivity.this, ChatActivity.class);
            intent.putExtra("TALKTO", userName);
            intent.putExtra("JID", JID);
            MessageActivity.this.startActivity(intent);
        }
    }

    class GetLastFriendRunnable extends Thread {
        GetLastFriendRunnable() {
        }

        public void run() {
            List<RiderVo> riders = null;
            try {
                riders = CnmobiImManager.getInstance().getRecentRiders(ImMainActivity.mOwerJid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            MessageActivity.this.latelyRiderChange(riders);
        }
    }

    class LogoHolder {
        Bitmap bitmap;
        String jid;

        LogoHolder() {
        }
    }

    public MessageActivity() {
        this.mHandler = new C01681();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.im_tab_message);
        init();
        this.mTopBarTitle.setText(getResources().getString(C0136R.string.message));
        this.networkStateReceiver = new NetworkStateReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(this.networkStateReceiver, filter);
        CnmobiImManager.getInstance().addImListener(TAG, this);
        MsgManager.getInstance().addCallBack(this);
        LogoManager.getInstance().add(this);
    }

    private void init() {
        this.mTopBarTitle = (TextView) findViewById(C0136R.id.topBarTitle);
        this.networkStatus = findViewById(C0136R.id.networkStatus);
        UiUtils.enabledBackButton(this);
        this.mFriendList = (ListView) findViewById(C0136R.id.friendList);
        this.mAdapter = new RiderItemAdapter(this, true, true);
        this.mFriendList.setAdapter(this.mAdapter);
        this.mFriendList.setOnItemClickListener(new C01692());
    }

    protected void onResume() {
        super.onResume();
        new GetLastFriendRunnable().start();
    }

    protected void onDestroy() {
        CnmobiImManager.getInstance().removeImListener(TAG);
        unregisterReceiver(this.networkStateReceiver);
        super.onDestroy();
        MsgManager.getInstance().removeCallBack(this);
        LogoManager.getInstance().remove(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public void riderChange(List<RiderVo> list) {
    }

    public void latelyRiderChange(List<RiderVo> riders) {
        Message msg = this.mHandler.obtainMessage();
        msg.what = FRIEND_LIST_CHANGE;
        msg.obj = riders;
        this.mHandler.sendMessage(msg);
    }

    public void sendMessage(int networkStatus) {
        Log.d(TAG, "networkStatus change!");
        boolean isNetworkConnected = true;
        if (networkStatus == 9) {
            isNetworkConnected = false;
        }
        Message msg = this.mHandler.obtainMessage();
        msg.what = NETWORK_STATUS_CHANGE;
        msg.obj = Boolean.valueOf(isNetworkConnected);
        this.mHandler.sendMessage(msg);
        if (isNetworkConnected) {
            ImMainActivity.context.connect2XmppServer();
        }
    }

    public void msgChange(MessageVo msg) {
        List<RiderVo> riders = null;
        try {
            riders = CnmobiImManager.getInstance().getRecentRiders(ImMainActivity.mOwerJid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        latelyRiderChange(riders);
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

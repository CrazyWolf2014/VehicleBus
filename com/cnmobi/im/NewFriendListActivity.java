package com.cnmobi.im;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.bo.MsgManager;
import com.cnmobi.im.dao.RiderDao;
import com.cnmobi.im.dto.RiderVo;
import com.cnmobi.im.util.UiUtils;
import com.cnmobi.im.util.XmppConnection;
import java.util.List;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;

public class NewFriendListActivity extends Activity {
    private static final int ADD_RIDER_FAIL = 111;
    private static final int ADD_RIDER_SUCCESS = 110;
    private static final int NEW_FRIEND_LIST_CHANGE = 100;
    private static final String TAG = "NewFriendListActivity";
    private NewRiderItemAdapter mAdapter;
    private ImageButton mCleanBtn;
    private Handler mHandler;
    private ListView mNewFriendList;
    private TextView mTopBarTitle;

    /* renamed from: com.cnmobi.im.NewFriendListActivity.1 */
    class C01741 extends Handler {

        /* renamed from: com.cnmobi.im.NewFriendListActivity.1.1 */
        class C01731 implements Runnable {
            C01731() {
            }

            public void run() {
                NewFriendListActivity.this.refreshRiders();
            }
        }

        C01741() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NewFriendListActivity.NEW_FRIEND_LIST_CHANGE /*100*/:
                    List<RiderVo> riders = msg.obj;
                    if (riders == null || riders.size() == 0) {
                        Toast.makeText(NewFriendListActivity.this, NewFriendListActivity.this.getString(C0136R.string.noNewFriendTips), 0).show();
                        NewFriendListActivity.this.finish();
                    }
                    NewFriendListActivity.this.mAdapter.resetRiders(riders);
                    NewFriendListActivity.this.mAdapter.notifyDataSetChanged();
                case NewFriendListActivity.ADD_RIDER_SUCCESS /*110*/:
                    Toast.makeText(NewFriendListActivity.this, NewFriendListActivity.this.getString(C0136R.string.addRiderSuccessTips), 0).show();
                    NewFriendListActivity.this.mHandler.postDelayed(new C01731(), 1000);
                case NewFriendListActivity.ADD_RIDER_FAIL /*111*/:
                    Toast.makeText(NewFriendListActivity.this, NewFriendListActivity.this.getString(C0136R.string.addRiderFailTips), 0).show();
                default:
            }
        }
    }

    /* renamed from: com.cnmobi.im.NewFriendListActivity.2 */
    class C01762 implements OnClickListener {

        /* renamed from: com.cnmobi.im.NewFriendListActivity.2.1 */
        class C01751 implements DialogInterface.OnClickListener {
            C01751() {
            }

            public void onClick(DialogInterface dialog, int which) {
                Roster roster = XmppConnection.getConnection().getRoster();
                if (roster == null) {
                    Toast.makeText(NewFriendListActivity.this, NewFriendListActivity.this.getString(C0136R.string.connectedServerException), 0).show();
                }
                List<RiderVo> riders = null;
                try {
                    riders = RiderDao.getInstance().getRiders(ImMainActivity.mOwerJid, PrivacyRule.SUBSCRIPTION_FROM);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if (riders != null && riders.size() > 0) {
                    for (RiderVo rider : riders) {
                        String jid = rider.jId;
                        try {
                            roster.removeEntry(roster.getEntry(jid));
                            try {
                                RiderDao.getInstance().delete(jid, ImMainActivity.mOwerJid);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (XMPPException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
                Toast.makeText(NewFriendListActivity.this, NewFriendListActivity.this.getString(C0136R.string.deleteFriendRequestSuccess), 0).show();
                NewFriendListActivity.this.finish();
            }
        }

        C01762() {
        }

        public void onClick(View v) {
            new Builder(NewFriendListActivity.this).setMessage(NewFriendListActivity.this.getString(C0136R.string.imSureDeleteFriendRequest)).setPositiveButton(NewFriendListActivity.this.getString(C0136R.string.imConfirm), new C01751()).setNegativeButton(NewFriendListActivity.this.getString(C0136R.string.imCancle), null).show();
        }
    }

    class NewRiderItemAdapter extends BaseAdapter {
        private Context context;
        OnClickListener listener;
        private List<RiderVo> riders;

        /* renamed from: com.cnmobi.im.NewFriendListActivity.NewRiderItemAdapter.1 */
        class C01771 implements OnClickListener {
            C01771() {
            }

            public void onClick(View view) {
                new AddRiderThread((String) view.getTag(C0136R.id.riderName), (String) view.getTag(C0136R.id.JID), null).start();
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
                    try {
                        RiderDao.getInstance().updateType(this.jid, ImMainActivity.mOwerJid, PrivacyRule.SUBSCRIPTION_BOTH);
                        MsgManager.getInstance().entriesChange(null, 3);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    NewFriendListActivity.this.mHandler.sendEmptyMessage(NewFriendListActivity.ADD_RIDER_SUCCESS);
                    return;
                }
                NewFriendListActivity.this.mHandler.sendEmptyMessage(NewFriendListActivity.ADD_RIDER_FAIL);
            }
        }

        public NewRiderItemAdapter(Context context) {
            this.listener = new C01771();
            this.context = context;
        }

        public void resetRiders(List<RiderVo> riders) {
            this.riders = riders;
        }

        public int getCount() {
            if (this.riders != null) {
                return this.riders.size();
            }
            return 0;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            RiderVo rider = (RiderVo) this.riders.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(this.context).inflate(C0136R.layout.new_rider_list_item, null);
            }
            ((TextView) convertView.findViewById(C0136R.id.riderName)).setText(rider.name);
            convertView.setTag(C0136R.id.riderName, rider.name);
            convertView.setTag(C0136R.id.JID, rider.jId);
            Button addBtn = (Button) convertView.findViewById(C0136R.id.agreeAndAddBtn);
            addBtn.setTag(C0136R.id.riderName, rider.name);
            addBtn.setTag(C0136R.id.JID, rider.jId);
            addBtn.setOnClickListener(this.listener);
            return convertView;
        }
    }

    public NewFriendListActivity() {
        this.mHandler = new C01741();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.im_new_friend_list);
        init();
    }

    protected void onResume() {
        super.onResume();
        refreshRiders();
    }

    protected void on() {
        super.onDestroy();
    }

    private void refreshRiders() {
        List<RiderVo> riders = null;
        try {
            riders = RiderDao.getInstance().getRiders(ImMainActivity.mOwerJid, PrivacyRule.SUBSCRIPTION_FROM);
        } catch (Exception e) {
            Log.e(TAG, "Get TYPE_FROM riders error:", e);
        }
        Message msg = this.mHandler.obtainMessage();
        msg.what = NEW_FRIEND_LIST_CHANGE;
        msg.obj = riders;
        this.mHandler.sendMessage(msg);
    }

    private void init() {
        this.mTopBarTitle = (TextView) findViewById(C0136R.id.topBarTitle);
        this.mTopBarTitle.setText(getResources().getString(C0136R.string.newFriend));
        UiUtils.enabledBackButton(this);
        this.mNewFriendList = (ListView) findViewById(C0136R.id.friendList);
        this.mAdapter = new NewRiderItemAdapter(this);
        this.mNewFriendList.setAdapter(this.mAdapter);
        this.mCleanBtn = (ImageButton) findViewById(C0136R.id.titleRightBtn);
        this.mCleanBtn.setImageResource(C0136R.drawable.im_clean_btn);
        this.mCleanBtn.setVisibility(0);
        this.mCleanBtn.setOnClickListener(new C01762());
    }
}

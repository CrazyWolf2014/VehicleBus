package com.cnmobi.im;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.util.UiUtils;
import com.cnmobi.im.util.XmppConnection;
import com.launch.rcu.socket.SocketCode;
import java.util.Collection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.xmlpull.v1.XmlPullParser;

public class ChatroomDetailActivity extends Activity {
    private static final int CHATROOM_INFO_BACK = 100;
    private static final String TAG = "ChatroomDetailActivity";
    private Button mDeleteBtn;
    private LinearLayout mFirstView;
    private Handler mHandler;
    private MultiUserChat mMultiUserChat;
    private TextView mTopBarTitle;
    private String roomJid;

    /* renamed from: com.cnmobi.im.ChatroomDetailActivity.1 */
    class C01561 extends Handler {
        C01561() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ChatroomDetailActivity.CHATROOM_INFO_BACK /*100*/:
                    String[] values = msg.obj;
                    String owerJid = values[0];
                    String desc = values[1];
                    if (owerJid != null) {
                        owerJid = owerJid.split(XmppConnection.JID_SEPARATOR)[0];
                    }
                    ((TextView) ChatroomDetailActivity.this.findViewById(C0136R.id.me_name)).setText(owerJid);
                    ((TextView) ChatroomDetailActivity.this.findViewById(C0136R.id.me_account)).setText(desc);
                    if (ImMainActivity.mUserId.equals(owerJid)) {
                        ChatroomDetailActivity.this.mDeleteBtn.setVisibility(0);
                    }
                default:
            }
        }
    }

    /* renamed from: com.cnmobi.im.ChatroomDetailActivity.2 */
    class C01582 implements OnClickListener {

        /* renamed from: com.cnmobi.im.ChatroomDetailActivity.2.1 */
        class C01571 implements DialogInterface.OnClickListener {
            C01571() {
            }

            public void onClick(DialogInterface dialog, int which) {
                try {
                    ChatroomDetailActivity.this.mMultiUserChat.destroy(null, null);
                    Toast.makeText(ChatroomDetailActivity.this, ChatroomDetailActivity.this.getString(C0136R.string.deleteChatroomSuccessTips), 0).show();
                } catch (XMPPException e) {
                    Toast.makeText(ChatroomDetailActivity.this, ChatroomDetailActivity.this.getString(C0136R.string.deleteChatroomFailTips), 0).show();
                }
                Intent data = new Intent(ChatroomDetailActivity.this, ChatRoomWindowsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean(SocketCode.REMOTE_BACK, true);
                data.putExtras(bundle);
                ChatroomDetailActivity.this.setResult(-1, data);
                ChatroomDetailActivity.this.finish();
            }
        }

        C01582() {
        }

        public void onClick(View v) {
            new Builder(ChatroomDetailActivity.this).setMessage(ChatroomDetailActivity.this.getString(C0136R.string.imSureDeleteChatroom)).setPositiveButton(ChatroomDetailActivity.this.getString(C0136R.string.imConfirm), new C01571()).setNegativeButton(ChatroomDetailActivity.this.getString(C0136R.string.imCancle), null).show();
        }
    }

    class GetRoomDetalThread extends Thread {
        GetRoomDetalThread() {
        }

        public void run() {
            String[] values = new String[2];
            try {
                Collection<Affiliate> affiliates = ChatroomDetailActivity.this.mMultiUserChat.getOwners();
                if (affiliates != null && affiliates.size() > 0) {
                    values[0] = affiliates.toArray()[0].getJid();
                }
                values[1] = MultiUserChat.getRoomInfo(XmppConnection.getConnection(), ChatroomDetailActivity.this.roomJid).getSubject();
            } catch (XMPPException e) {
                e.printStackTrace();
            }
            Message msg = ChatroomDetailActivity.this.mHandler.obtainMessage();
            msg.what = ChatroomDetailActivity.CHATROOM_INFO_BACK;
            msg.obj = values;
            ChatroomDetailActivity.this.mHandler.sendMessage(msg);
        }
    }

    public ChatroomDetailActivity() {
        this.mHandler = new C01561();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.im_chatroom_detail);
        init();
    }

    private void init() {
        UiUtils.enabledBackButton(this);
        this.mTopBarTitle = (TextView) findViewById(C0136R.id.topBarTitle);
        this.mTopBarTitle.setText(C0136R.string.chatroonDeital);
        String roomName = getIntent().getStringExtra("RoomName");
        this.roomJid = getIntent().getStringExtra("RoomJid");
        ((TextView) findViewById(C0136R.id.chatroomName)).setText(roomName);
        this.mDeleteBtn = (Button) findViewById(C0136R.id.deleteBtn);
        this.mDeleteBtn.setOnClickListener(new C01582());
        this.mFirstView = (LinearLayout) findViewById(C0136R.id.firstArea);
        this.mFirstView.addView(getItemView(getString(C0136R.string.chatroomOwner), XmlPullParser.NO_NAMESPACE, C0136R.id.me_name, true));
        this.mFirstView.addView(getItemView(getString(C0136R.string.chatroomDesc), XmlPullParser.NO_NAMESPACE, C0136R.id.me_account, false));
        try {
            this.mMultiUserChat = new MultiUserChat(XmppConnection.getConnection(), this.roomJid);
            new GetRoomDetalThread().start();
        } catch (Exception e) {
        }
    }

    private View getItemView(String title, String value, int valueViewId, boolean isShowLine) {
        View view = LayoutInflater.from(this).inflate(C0136R.layout.im_area_item2, null);
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

package com.cnmobi.im;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.adapter.ChatMessageAdapter;
import com.cnmobi.im.bo.LogoManager;
import com.cnmobi.im.bo.LogoManager.LogoBackListener;
import com.cnmobi.im.dto.MessageVo;
import com.cnmobi.im.util.TimeRender;
import com.cnmobi.im.util.UiUtils;
import com.cnmobi.im.util.XmppConnection;
import com.launch.rcu.socket.SocketCode;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.xmlpull.v1.XmlPullParser;

public class ChatRoomWindowsActivity extends Activity implements LogoBackListener {
    private static final int NEW_MESSAGE_COME_IN = 100;
    private static final int REQUEST_DETAIL_CODE = 101;
    private static final int RIDER_LOGO_BACK = 102;
    private static final String TAG = "ChatRoomWindowsActivity";
    private ChatMessageAdapter mAdapter;
    private ImageButton mChatSelectBtn;
    private ImageButton mChatroomDetailBtn;
    private Handler mHandler;
    private ListView mMessageList;
    private EditText mMsgEditText;
    private MultiUserChat mMultiUserChat;
    private Button mSendBtn;
    private ImageButton mSetChatModeBtn;
    private TextView mTitleTextView;
    private List<MessageVo> messages;
    private String roomJid;

    /* renamed from: com.cnmobi.im.ChatRoomWindowsActivity.1 */
    class C01471 extends Handler {
        C01471() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ChatRoomWindowsActivity.NEW_MESSAGE_COME_IN /*100*/:
                    ChatRoomWindowsActivity.this.messages.add(msg.obj);
                    ChatRoomWindowsActivity.this.mAdapter.notifyDataSetChanged();
                case ChatRoomWindowsActivity.RIDER_LOGO_BACK /*102*/:
                    LogoHolder holder = msg.obj;
                    List<ImageView> ivs = ChatRoomWindowsActivity.this.mAdapter.getImageViewByTag(holder.jid);
                    if (ivs != null && ivs.size() != 0) {
                        for (ImageView iv : ivs) {
                            if (holder.bitmap != null) {
                                iv.setImageBitmap(holder.bitmap);
                            } else {
                                iv.setImageResource(C0136R.drawable.default_logo);
                            }
                        }
                    }
                default:
            }
        }
    }

    /* renamed from: com.cnmobi.im.ChatRoomWindowsActivity.2 */
    class C01482 implements OnClickListener {
        C01482() {
        }

        public void onClick(View v) {
            String msg = ChatRoomWindowsActivity.this.mMsgEditText.getText().toString().trim();
            if (msg.length() > 0) {
                try {
                    ChatRoomWindowsActivity.this.mMultiUserChat.sendMessage(msg);
                } catch (Exception e) {
                    Log.e(ChatRoomWindowsActivity.TAG, "Send Message error : ", e);
                    Toast.makeText(ChatRoomWindowsActivity.this, ChatRoomWindowsActivity.this.getString(C0136R.string.connectedServerException), 0).show();
                }
            } else {
                Toast.makeText(ChatRoomWindowsActivity.this, ChatRoomWindowsActivity.this.getString(C0136R.string.sendMsgCannotNull), 0).show();
            }
            ChatRoomWindowsActivity.this.mMsgEditText.setText(XmlPullParser.NO_NAMESPACE);
        }
    }

    /* renamed from: com.cnmobi.im.ChatRoomWindowsActivity.3 */
    class C01493 implements OnClickListener {
        private final /* synthetic */ String val$roomName;

        C01493(String str) {
            this.val$roomName = str;
        }

        public void onClick(View v) {
            Intent intent = new Intent(ChatRoomWindowsActivity.this, ChatroomDetailActivity.class);
            intent.putExtra("RoomName", this.val$roomName);
            intent.putExtra("RoomJid", ChatRoomWindowsActivity.this.roomJid);
            ChatRoomWindowsActivity.this.startActivityForResult(intent, ChatRoomWindowsActivity.REQUEST_DETAIL_CODE);
        }
    }

    class JoinChatRoomThread extends Thread {
        JoinChatRoomThread() {
        }

        public void run() {
            try {
                ChatRoomWindowsActivity.this.mMultiUserChat = new MultiUserChat(XmppConnection.getConnection(), ChatRoomWindowsActivity.this.roomJid);
                DiscussionHistory history = new DiscussionHistory();
                history.setMaxStanzas(50);
                ChatRoomWindowsActivity.this.mMultiUserChat.join(ImMainActivity.mUserId, null, history, (long) SmackConfiguration.getPacketReplyTimeout());
                ChatRoomWindowsActivity.this.mMultiUserChat.addMessageListener(new MultiListener());
                System.out.println("\u4f1a\u8bae\u5ba4\u52a0\u5165\u6210\u529f........");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("\u4f1a\u8bae\u5ba4\u52a0\u5165\u5931\u8d25........");
            }
        }
    }

    class LogoHolder {
        Bitmap bitmap;
        String jid;

        LogoHolder() {
        }
    }

    class MultiListener implements PacketListener {
        MultiListener() {
        }

        public void processPacket(Packet packet) {
            System.out.println("**********************************");
            org.jivesoftware.smack.packet.Message message = (org.jivesoftware.smack.packet.Message) packet;
            String from = StringUtils.parseResource(message.getFrom());
            if (from != null && !XmlPullParser.NO_NAMESPACE.equals(from)) {
                Date date = null;
                try {
                    date = message.getExtensions().toArray()[0].getStamp();
                } catch (Exception e) {
                }
                if (date == null) {
                    date = new Date();
                }
                System.out.println("00000-->" + from);
                from = new StringBuilder(String.valueOf(from)).append(XmppConnection.JID_SEPARATOR).append(XmppConnection.SERVER_HOST).toString();
                String direction = MessageVo.DIRECTION_IN;
                if (XmppConnection.getConnection().getUser().contains(from)) {
                    direction = MessageVo.DIRECTION_OUT;
                }
                String fromRoomName = StringUtils.parseName(message.getFrom());
                MessageVo msg = new MessageVo(from, TimeRender.getDate(date), message.getBody(), direction);
                Message temp = ChatRoomWindowsActivity.this.mHandler.obtainMessage();
                temp.what = ChatRoomWindowsActivity.NEW_MESSAGE_COME_IN;
                temp.obj = msg;
                ChatRoomWindowsActivity.this.mHandler.sendMessage(temp);
            }
        }
    }

    public ChatRoomWindowsActivity() {
        this.mHandler = new C01471();
        this.messages = new LinkedList();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.chat_room_windows);
        UiUtils.enabledBackButton(this);
        init();
        new JoinChatRoomThread().start();
        LogoManager.getInstance().add(this);
    }

    private void init() {
        this.mTitleTextView = (TextView) findViewById(C0136R.id.topBarTitle);
        String roomName = getIntent().getStringExtra("RoomName");
        this.mTitleTextView.setText(roomName);
        this.roomJid = getIntent().getStringExtra("RoomJid");
        this.mSendBtn = (Button) findViewById(C0136R.id.formclient_btsend);
        this.mMsgEditText = (EditText) findViewById(C0136R.id.formclient_text);
        this.mSendBtn.setOnClickListener(new C01482());
        this.mMessageList = (ListView) findViewById(C0136R.id.formclient_listview);
        this.mAdapter = new ChatMessageAdapter(this, this.messages);
        this.mMessageList.setAdapter(this.mAdapter);
        this.mMessageList.setTranscriptMode(2);
        this.mChatroomDetailBtn = (ImageButton) findViewById(C0136R.id.titleRightBtn);
        this.mChatroomDetailBtn.setImageResource(C0136R.drawable.im_chatroom_detail_btn);
        this.mChatroomDetailBtn.setVisibility(0);
        this.mChatroomDetailBtn.setOnClickListener(new C01493(roomName));
        this.mSetChatModeBtn = (ImageButton) findViewById(C0136R.id.setChatModeBtn);
        this.mSetChatModeBtn.setVisibility(8);
        this.mChatSelectBtn = (ImageButton) findViewById(C0136R.id.chatSelectBtn);
        this.mChatSelectBtn.setVisibility(8);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_DETAIL_CODE == requestCode && data != null && data.getBooleanExtra(SocketCode.REMOTE_BACK, false)) {
            finish();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mMultiUserChat != null) {
            try {
                this.mMultiUserChat.leave();
            } catch (Exception e) {
            }
        }
        LogoManager.getInstance().remove(this);
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

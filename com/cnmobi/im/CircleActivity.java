package com.cnmobi.im;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.adapter.ChatRoomAdapter;
import com.cnmobi.im.bo.ChatroomManager;
import com.cnmobi.im.bo.ChatroomManager.ChatRoomListener;
import com.cnmobi.im.dao.ChatroomDao;
import com.cnmobi.im.dto.ChatRoom;
import com.cnmobi.im.util.UiUtils;
import java.util.List;

public class CircleActivity extends Activity implements ChatRoomListener {
    private static final int CHAT_ROOM_CHANGE = 100;
    private static final String TAG = "CircleActivity";
    private ChatRoomAdapter mAdapter;
    private ImageButton mAddChatRoomBtn;
    private ListView mChatRoomList;
    private Handler mHandler;
    private TextView mTopBarTitle;

    /* renamed from: com.cnmobi.im.CircleActivity.1 */
    class C01591 extends Handler {
        C01591() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CircleActivity.CHAT_ROOM_CHANGE /*100*/:
                    if (msg.obj != null) {
                        CircleActivity.this.mAdapter.resetChatRooms(msg.obj);
                        CircleActivity.this.mAdapter.notifyDataSetChanged();
                    }
                default:
            }
        }
    }

    /* renamed from: com.cnmobi.im.CircleActivity.2 */
    class C01602 implements OnClickListener {
        C01602() {
        }

        public void onClick(View v) {
            CircleActivity.this.startActivity(new Intent(CircleActivity.this, AddChatRoomActivity.class));
        }
    }

    class GetHostedRoomThread extends Thread {
        GetHostedRoomThread() {
        }

        public void run() {
            try {
                ChatroomManager.getInstance().refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CircleActivity() {
        this.mHandler = new C01591();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.im_tab_circle);
        init();
        this.mTopBarTitle.setText(getResources().getString(C0136R.string.circle));
        ChatroomManager.getInstance().addChatRoomListener(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        ChatroomManager.getInstance().removeChatRoomListener(this);
    }

    private void init() {
        this.mTopBarTitle = (TextView) findViewById(C0136R.id.topBarTitle);
        UiUtils.enabledBackButton(this);
        this.mChatRoomList = (ListView) findViewById(C0136R.id.chatRoomList);
        this.mAdapter = new ChatRoomAdapter(this);
        try {
            this.mAdapter.resetChatRooms(ChatroomDao.getInstance().getChatRooms());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mChatRoomList.setAdapter(this.mAdapter);
        this.mAddChatRoomBtn = (ImageButton) findViewById(C0136R.id.titleRightBtn);
        this.mAddChatRoomBtn.setImageResource(C0136R.drawable.add_circle_btn);
        this.mAddChatRoomBtn.setVisibility(0);
        this.mAddChatRoomBtn.setOnClickListener(new C01602());
    }

    protected void onResume() {
        super.onResume();
        new GetHostedRoomThread().start();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public void chatRoomChange() {
        List<ChatRoom> chatRooms = null;
        try {
            chatRooms = ChatroomDao.getInstance().getChatRooms();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (chatRooms != null) {
            Message msg = this.mHandler.obtainMessage();
            msg.obj = chatRooms;
            msg.what = CHAT_ROOM_CHANGE;
            this.mHandler.sendMessage(msg);
        }
    }
}

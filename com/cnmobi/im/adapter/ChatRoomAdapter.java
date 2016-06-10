package com.cnmobi.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.ChatRoomWindowsActivity;
import com.cnmobi.im.dto.ChatRoom;
import java.util.List;

public class ChatRoomAdapter extends BaseAdapter {
    private List<ChatRoom> chatRooms;
    private Context context;
    private LayoutInflater inflater;
    private OnClickListener listener;

    /* renamed from: com.cnmobi.im.adapter.ChatRoomAdapter.1 */
    class C01931 implements OnClickListener {
        C01931() {
        }

        public void onClick(View v) {
            String chatRoomJid = (String) v.getTag(C0136R.id.JID);
            String chatRoomName = (String) v.getTag(C0136R.id.chatRoomName);
            Intent intent = new Intent(ChatRoomAdapter.this.context, ChatRoomWindowsActivity.class);
            intent.putExtra("RoomJid", chatRoomJid);
            intent.putExtra("RoomName", chatRoomName);
            ChatRoomAdapter.this.context.startActivity(intent);
        }
    }

    public ChatRoomAdapter(Context context) {
        this.listener = new C01931();
        this.context = context;
        this.inflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
    }

    public void resetChatRooms(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }

    public int getCount() {
        if (this.chatRooms == null) {
            return 0;
        }
        return this.chatRooms.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ChatRoom chatRoom = (ChatRoom) this.chatRooms.get(position);
        if (convertView == null) {
            convertView = this.inflater.inflate(C0136R.layout.chatroom_list_item, null);
        }
        TextView chatRoomDesc = (TextView) convertView.findViewById(C0136R.id.chatRoomDesc);
        ((TextView) convertView.findViewById(C0136R.id.chatRoomName)).setText(chatRoom.getName());
        chatRoomDesc.setText(chatRoom.getSubject());
        convertView.setTag(C0136R.id.JID, chatRoom.getJid());
        convertView.setTag(C0136R.id.chatRoomName, chatRoom.getName());
        convertView.setOnClickListener(this.listener);
        return convertView;
    }
}

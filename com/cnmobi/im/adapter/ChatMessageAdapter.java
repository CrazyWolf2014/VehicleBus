package com.cnmobi.im.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.bo.LogoManager;
import com.cnmobi.im.dto.MessageVo;
import com.cnmobi.im.util.XmppConnection;
import com.launch.rcu.socket.SocketCode;
import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<MessageVo> messages;
    private MediaPlayer musicPlayer;

    public ChatMessageAdapter(Context context, List<MessageVo> messages) {
        this.context = context;
        this.inflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
        this.messages = messages;
        this.musicPlayer = new MediaPlayer();
    }

    public List<ImageView> getImageViewByTag(String jid) {
        List<ImageView> ivs = new ArrayList();
        for (MessageVo message : this.messages) {
            if (message.logoView == null) {
                break;
            } else if (jid.equals(message.logoView.getTag())) {
                ivs.add(message.logoView);
            }
        }
        return ivs;
    }

    public int getCount() {
        return this.messages.size();
    }

    public Object getItem(int position) {
        return this.messages.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MessageVo message = (MessageVo) this.messages.get(position);
        if (MessageVo.DIRECTION_OUT.equals(message.direction)) {
            convertView = this.inflater.inflate(C0136R.layout.formclient_chat_out, null);
        } else {
            convertView = this.inflater.inflate(C0136R.layout.formclient_chat_in, null);
        }
        TextView dateView = (TextView) convertView.findViewById(C0136R.id.formclient_row_date);
        TextView msgView = (TextView) convertView.findViewById(C0136R.id.formclient_row_msg);
        ((TextView) convertView.findViewById(C0136R.id.formclient_row_userid)).setText(message.jId.split(XmppConnection.JID_SEPARATOR)[0]);
        dateView.setText(message.time);
        msgView.setText(message.content);
        ImageView logoView = (ImageView) convertView.findViewById(C0136R.id.head);
        logoView.setTag(message.jId);
        message.logoView = logoView;
        Bitmap bitmap = LogoManager.getInstance().getBitmapFromCache(message.jId);
        if (bitmap != null) {
            logoView.setImageBitmap(bitmap);
        } else {
            LogoManager.getInstance().start2GetLogo(message.jId);
        }
        if (SocketCode.REMOTE_RECORD_BUTTON.equals(message.type)) {
            ((TextView) convertView.findViewById(C0136R.id.msg_status)).setVisibility(8);
        } else {
            ((TextView) convertView.findViewById(C0136R.id.msg_status)).setVisibility(8);
        }
        return convertView;
    }
}

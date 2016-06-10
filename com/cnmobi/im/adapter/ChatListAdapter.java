package com.cnmobi.im.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.ImMainActivity;
import com.cnmobi.im.bo.LogoManager;
import com.cnmobi.im.dto.MessageVo;
import com.cnmobi.im.dto.Msg;
import com.launch.rcu.socket.SocketCode;
import java.io.File;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class ChatListAdapter extends BaseAdapter {
    private String chaterJid;
    private Context cxt;
    private LayoutInflater inflater;
    private boolean isPlaying;
    private List<Msg> listMsg;
    private MediaPlayer musicPlayer;
    private String playingFilePath;

    /* renamed from: com.cnmobi.im.adapter.ChatListAdapter.1 */
    class C01911 implements OnCompletionListener {
        C01911() {
        }

        public void onCompletion(MediaPlayer mp) {
            ChatListAdapter.this.isPlaying = false;
            ChatListAdapter.this.playingFilePath = null;
        }
    }

    /* renamed from: com.cnmobi.im.adapter.ChatListAdapter.2 */
    class C01922 implements OnClickListener {
        private final /* synthetic */ Msg val$msg;

        C01922(Msg msg) {
            this.val$msg = msg;
        }

        public void onClick(View v) {
            String filePath = this.val$msg.getFilePath();
            if (filePath != null && !XmlPullParser.NO_NAMESPACE.equals(filePath)) {
                Log.d("AudioPath : ", filePath);
                File file = new File(filePath);
                if (file.exists()) {
                    String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase().trim().toLowerCase();
                    if (!end.equals("m4a") && !end.equals("mp3") && !end.equals("mid") && !end.equals("xmf") && !end.equals("ogg") && !end.equals("wav") && !end.equals("amr")) {
                        return;
                    }
                    if (filePath.equals(ChatListAdapter.this.playingFilePath)) {
                        ChatListAdapter.this.musicPlayer.stop();
                        ChatListAdapter.this.playingFilePath = null;
                        return;
                    }
                    ChatListAdapter.this.playingFilePath = filePath;
                    ChatListAdapter.this.musicPlayer.reset();
                    try {
                        ChatListAdapter.this.musicPlayer.setDataSource(filePath);
                        ChatListAdapter.this.musicPlayer.prepare();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ChatListAdapter.this.musicPlayer.start();
                }
            }
        }
    }

    public ChatListAdapter(Context formClient, List<Msg> list, String chaterJid) {
        this.cxt = formClient;
        this.listMsg = list;
        this.musicPlayer = new MediaPlayer();
        this.inflater = (LayoutInflater) this.cxt.getSystemService("layout_inflater");
        this.chaterJid = chaterJid;
        this.musicPlayer.setOnCompletionListener(new C01911());
    }

    public int getCount() {
        return this.listMsg.size();
    }

    public Object getItem(int position) {
        return this.listMsg.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public void release() {
        this.musicPlayer.reset();
        this.musicPlayer.release();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String bitmapJid;
        if (MessageVo.DIRECTION_OUT.equals(((Msg) this.listMsg.get(position)).getFrom())) {
            if (convertView == null || ((Integer) convertView.getTag()).intValue() != C0136R.layout.formclient_chat_out) {
                Log.i("ChatListAdapter", "OUT XML No Hit");
                convertView = this.inflater.inflate(C0136R.layout.formclient_chat_out, null);
                convertView.setTag(Integer.valueOf(C0136R.layout.formclient_chat_out));
            } else {
                Log.i("ChatListAdapter", "OUT XML Hit");
            }
        } else if (convertView == null || ((Integer) convertView.getTag()).intValue() != C0136R.layout.formclient_chat_in) {
            Log.i("ChatListAdapter", "In XML No Hit");
            convertView = this.inflater.inflate(C0136R.layout.formclient_chat_in, null);
            convertView.setTag(Integer.valueOf(C0136R.layout.formclient_chat_in));
        } else {
            Log.i("ChatListAdapter", "In XML Hit");
        }
        TextView dateView = (TextView) convertView.findViewById(C0136R.id.formclient_row_date);
        TextView msgView = (TextView) convertView.findViewById(C0136R.id.formclient_row_msg);
        ((TextView) convertView.findViewById(C0136R.id.formclient_row_userid)).setText(((Msg) this.listMsg.get(position)).getUserid());
        dateView.setText(((Msg) this.listMsg.get(position)).getDate());
        msgView.setText(((Msg) this.listMsg.get(position)).getMsg());
        ImageView logoView = (ImageView) convertView.findViewById(C0136R.id.head);
        if (MessageVo.DIRECTION_OUT.equals(((Msg) this.listMsg.get(position)).getFrom())) {
            bitmapJid = ImMainActivity.mOwerJid;
        } else {
            bitmapJid = this.chaterJid;
        }
        Bitmap bitmap = LogoManager.getInstance().getBitmapFromCache(bitmapJid);
        if (bitmap != null) {
            logoView.setImageBitmap(bitmap);
        }
        String type = ((Msg) this.listMsg.get(position)).getType();
        if (SocketCode.REMOTE_RECORD_BUTTON.equals(type) || Msg.TYPE[2].equals(type)) {
            Msg msg = (Msg) this.listMsg.get(position);
            TextView msgStatus = (TextView) convertView.findViewById(C0136R.id.msg_status);
            if (((Msg) this.listMsg.get(position)).getReceive() != null) {
                msgStatus.setText(((Msg) this.listMsg.get(position)).getReceive());
            }
            convertView.setOnClickListener(new C01922(msg));
        } else {
            ((TextView) convertView.findViewById(C0136R.id.msg_status)).setVisibility(8);
        }
        return convertView;
    }
}

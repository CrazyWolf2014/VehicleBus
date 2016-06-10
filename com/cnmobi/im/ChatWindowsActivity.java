package com.cnmobi.im;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.bo.MsgCallback;
import com.cnmobi.im.bo.MsgManager;
import com.cnmobi.im.dao.MessageDao;
import com.cnmobi.im.dto.MessageVo;
import com.cnmobi.im.dto.Msg;
import com.cnmobi.im.util.NetworkUtils;
import com.cnmobi.im.util.TimeRender;
import com.cnmobi.im.util.UiUtils;
import com.cnmobi.im.util.XmppConnection;
import com.cnmobi.im.view.RecordButton;
import com.cnmobi.im.view.RecordButton.OnFinishedRecordListener;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.xmlpull.v1.XmlPullParser;

public class ChatWindowsActivity extends Activity implements MsgCallback {
    public static String FILE_ROOT_PATH;
    public static String RECORD_ROOT_PATH;
    private BaseAdapter adapter;
    private File file;
    private Handler handler;
    private List<Msg> listMsg;
    private View mKeyboardModeArea;
    private RecordButton mPressed2TalkBtn;
    private ImageButton mSetChatModeBtn;
    private TextView mTitleTextView;
    private EditText msgText;
    private Chat newchat;
    private ProgressBar pb;
    private FileTransferRequest request;
    private OutgoingFileTransfer sendTransfer;
    private String talkTojid;
    private String userChatSendFile;

    /* renamed from: com.cnmobi.im.ChatWindowsActivity.1 */
    class C01531 extends Handler {

        /* renamed from: com.cnmobi.im.ChatWindowsActivity.1.1 */
        class C01511 implements OnClickListener {
            private final /* synthetic */ IncomingFileTransfer val$infiletransfer;

            /* renamed from: com.cnmobi.im.ChatWindowsActivity.1.1.1 */
            class C01501 extends TimerTask {
                private final /* synthetic */ IncomingFileTransfer val$infiletransfer;

                C01501(IncomingFileTransfer incomingFileTransfer) {
                    this.val$infiletransfer = incomingFileTransfer;
                }

                public void run() {
                    if (this.val$infiletransfer.getAmountWritten() >= ChatWindowsActivity.this.request.getFileSize() || this.val$infiletransfer.getStatus() == Status.error || this.val$infiletransfer.getStatus() == Status.refused || this.val$infiletransfer.getStatus() == Status.cancelled || this.val$infiletransfer.getStatus() == Status.complete) {
                        cancel();
                        ChatWindowsActivity.this.handler.sendEmptyMessage(4);
                        return;
                    }
                    long p = (this.val$infiletransfer.getAmountWritten() * 100) / this.val$infiletransfer.getFileSize();
                    Message message = ChatWindowsActivity.this.handler.obtainMessage();
                    message.arg1 = Math.round((float) p);
                    message.what = 3;
                    message.sendToTarget();
                }
            }

            C01511(IncomingFileTransfer incomingFileTransfer) {
                this.val$infiletransfer = incomingFileTransfer;
            }

            public void onClick(DialogInterface dialog, int id) {
                try {
                    this.val$infiletransfer.recieveFile(ChatWindowsActivity.this.file);
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
                ChatWindowsActivity.this.handler.sendEmptyMessage(2);
                new Timer().scheduleAtFixedRate(new C01501(this.val$infiletransfer), 10, 10);
                dialog.dismiss();
            }
        }

        /* renamed from: com.cnmobi.im.ChatWindowsActivity.1.2 */
        class C01522 implements OnClickListener {
            C01522() {
            }

            public void onClick(DialogInterface dialog, int id) {
                ChatWindowsActivity.this.request.reject();
                dialog.cancel();
            }
        }

        C01531() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    String[] args = msg.obj;
                    ChatWindowsActivity.this.listMsg.add(new Msg(args[0], args[1], args[2], args[3]));
                    ChatWindowsActivity.this.adapter.notifyDataSetChanged();
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    if (ChatWindowsActivity.this.pb.getVisibility() == 8) {
                        ChatWindowsActivity.this.pb.setMax(100);
                        ChatWindowsActivity.this.pb.setProgress(0);
                        ChatWindowsActivity.this.pb.setVisibility(0);
                    }
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    ChatWindowsActivity.this.pb.setProgress(msg.arg1);
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    ChatWindowsActivity.this.pb.setVisibility(8);
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    new Builder(ChatWindowsActivity.this).setTitle("receive file").setCancelable(false).setPositiveButton("Receive", new C01511(ChatWindowsActivity.this.request.accept())).setNegativeButton("Reject", new C01522()).show();
                default:
            }
        }
    }

    /* renamed from: com.cnmobi.im.ChatWindowsActivity.2 */
    class C01542 implements View.OnClickListener {
        private final /* synthetic */ String val$JID;

        C01542(String str) {
            this.val$JID = str;
        }

        public void onClick(View v) {
            String msg = ChatWindowsActivity.this.msgText.getText().toString().trim();
            if (msg.length() <= 0) {
                return;
            }
            if (NetworkUtils.getNetworkInfo(ChatWindowsActivity.this) == 9) {
                Toast.makeText(ChatWindowsActivity.this, ChatWindowsActivity.this.getString(C0136R.string.no_network_tips), 0).show();
            } else if (XmppConnection.getConnection() == null || !XmppConnection.getConnection().isConnected()) {
                Toast.makeText(ChatWindowsActivity.this, ChatWindowsActivity.this.getString(C0136R.string.connectedServerException), 0).show();
                if (XmppConnection.getConnection() != null) {
                    try {
                        XmppConnection.getConnection().connect();
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                ChatWindowsActivity.this.listMsg.add(new Msg(ImMainActivity.xmppShowUserName, msg, TimeRender.getDate(), MessageVo.DIRECTION_OUT));
                ChatWindowsActivity.this.adapter.notifyDataSetChanged();
                try {
                    ChatWindowsActivity.this.newchat.sendMessage(msg);
                    try {
                        MessageDao.getInstance().insert(new MessageVo(this.val$JID, String.valueOf(System.currentTimeMillis()), msg, MessageVo.DIRECTION_OUT));
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } catch (XMPPException e3) {
                    e3.printStackTrace();
                }
                ChatWindowsActivity.this.msgText.setText(XmlPullParser.NO_NAMESPACE);
            }
        }
    }

    /* renamed from: com.cnmobi.im.ChatWindowsActivity.4 */
    class C01554 implements View.OnClickListener {
        C01554() {
        }

        public void onClick(View v) {
            if (((Integer) v.getTag()).intValue() == 0) {
                ChatWindowsActivity.this.mSetChatModeBtn.setTag(Integer.valueOf(1));
                ChatWindowsActivity.this.mSetChatModeBtn.setImageResource(C0136R.drawable.chat_keyboard_btn);
                ChatWindowsActivity.this.mKeyboardModeArea.setVisibility(8);
                ChatWindowsActivity.this.mPressed2TalkBtn.setVisibility(0);
                return;
            }
            ChatWindowsActivity.this.mSetChatModeBtn.setTag(Integer.valueOf(0));
            ChatWindowsActivity.this.mSetChatModeBtn.setImageResource(C0136R.drawable.chat_voice_btn);
            ChatWindowsActivity.this.mKeyboardModeArea.setVisibility(0);
            ChatWindowsActivity.this.mPressed2TalkBtn.setVisibility(8);
        }
    }

    class MyAdapter extends BaseAdapter {
        private Context cxt;
        private LayoutInflater inflater;

        public MyAdapter(Context cxt) {
            this.cxt = cxt;
        }

        public int getCount() {
            return ChatWindowsActivity.this.listMsg.size();
        }

        public Object getItem(int position) {
            return ChatWindowsActivity.this.listMsg.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            this.inflater = (LayoutInflater) this.cxt.getSystemService("layout_inflater");
            if (((Msg) ChatWindowsActivity.this.listMsg.get(position)).getFrom().equals(MessageVo.DIRECTION_IN)) {
                convertView = this.inflater.inflate(C0136R.layout.chat_in, null);
            } else {
                convertView = this.inflater.inflate(C0136R.layout.chat_out, null);
            }
            TextView dateView = (TextView) convertView.findViewById(C0136R.id.formclient_row_date);
            TextView msgView = (TextView) convertView.findViewById(C0136R.id.formclient_row_msg);
            ((TextView) convertView.findViewById(C0136R.id.formclient_row_userid)).setText(((Msg) ChatWindowsActivity.this.listMsg.get(position)).getUserid());
            dateView.setText(((Msg) ChatWindowsActivity.this.listMsg.get(position)).getDate());
            msgView.setText(((Msg) ChatWindowsActivity.this.listMsg.get(position)).getMsg());
            return convertView;
        }
    }

    class MyFileStatusThread extends Thread {
        private Msg msg;
        private FileTransfer transfer;

        public MyFileStatusThread(FileTransfer tf, Msg msg) {
            this.transfer = tf;
            this.msg = msg;
        }

        public void run() {
            System.out.println(this.transfer.getStatus());
            System.out.println(this.transfer.getProgress());
            Message message = new Message();
            message.what = 3;
            while (!this.transfer.isDone()) {
                System.out.println(this.transfer.getStatus());
                System.out.println(this.transfer.getProgress());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (this.transfer.getStatus().equals(Status.error)) {
                this.msg.setReceive(Msg.STATUS[2]);
            } else if (this.transfer.getStatus().equals(Status.refused)) {
                this.msg.setReceive(Msg.STATUS[1]);
            } else {
                this.msg.setReceive(Msg.STATUS[0]);
            }
            ChatWindowsActivity.this.handler.sendMessage(message);
        }
    }

    /* renamed from: com.cnmobi.im.ChatWindowsActivity.3 */
    class C10483 implements OnFinishedRecordListener {
        C10483() {
        }

        public void onFinishedRecord(String audioPath, int time) {
            Log.i("RECORD!!!", "finished!!!!!!!!!! save to " + audioPath);
            if (audioPath != null) {
                try {
                    Msg myChatMsg = new Msg(ChatWindowsActivity.this.talkTojid, new StringBuilder(String.valueOf(time)).append("\u201d").append(ChatWindowsActivity.this.getString(C0136R.string.voiceMessage)).toString(), TimeRender.getDate(), Msg.FROM_TYPE[1], Msg.TYPE[0], Msg.STATUS[3], new StringBuilder(String.valueOf(time)).toString(), audioPath);
                    ChatWindowsActivity.this.listMsg.add(myChatMsg);
                    String[] pathStrings = audioPath.split(FilePathGenerator.ANDROID_DIR_SEP);
                    String fileName = null;
                    if (pathStrings != null && pathStrings.length > 0) {
                        fileName = pathStrings[pathStrings.length - 1];
                    }
                    Msg sendChatMsg = new Msg(ChatWindowsActivity.this.talkTojid, new StringBuilder(String.valueOf(time)).append("\u201d").append(ChatWindowsActivity.this.getString(C0136R.string.voiceMessage)).toString(), TimeRender.getDate(), Msg.FROM_TYPE[0], Msg.TYPE[0], Msg.STATUS[3], new StringBuilder(String.valueOf(time)).toString(), fileName);
                    ChatWindowsActivity.this.adapter.notifyDataSetChanged();
                    ChatWindowsActivity.this.newchat.sendMessage(Msg.toJson(sendChatMsg));
                    ChatWindowsActivity.this.sendFile(audioPath, myChatMsg);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            Toast.makeText(ChatWindowsActivity.this, ChatWindowsActivity.this.getString(C0136R.string.sendFail), 0).show();
        }
    }

    class RecFileTransferListener implements FileTransferListener {
        RecFileTransferListener() {
        }

        public void fileTransferRequest(FileTransferRequest prequest) {
            System.out.println("The file received from: " + prequest.getRequestor());
            ChatWindowsActivity.this.file = new File("mnt/sdcard/" + prequest.getFileName());
            ChatWindowsActivity.this.request = prequest;
            ChatWindowsActivity.this.handler.sendEmptyMessage(5);
        }
    }

    public ChatWindowsActivity() {
        this.listMsg = new ArrayList();
        this.userChatSendFile = XmlPullParser.NO_NAMESPACE;
        this.handler = new C01531();
    }

    static {
        FILE_ROOT_PATH = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/cnmobi/chat/file").toString();
        RECORD_ROOT_PATH = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/cnmobi/chat/record").toString();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.chat_windows);
        UiUtils.enabledBackButton(this);
        init();
        String JID = getIntent().getStringExtra("JID");
        this.talkTojid = JID;
        this.userChatSendFile = new StringBuilder(String.valueOf(JID)).append("/Cnmobi").toString();
        MsgManager.getInstance().resetTalk2Jid(JID);
        MsgManager.getInstance().resetChatWindowsCallBack(this);
        MsgManager.getInstance().read(JID);
        String talkToName = JID.split(XmppConnection.JID_SEPARATOR)[0];
        ListView listview = (ListView) findViewById(C0136R.id.formclient_listview);
        listview.setTranscriptMode(2);
        this.adapter = new MyAdapter(this);
        listview.setAdapter(this.adapter);
        this.msgText = (EditText) findViewById(C0136R.id.formclient_text);
        this.pb = (ProgressBar) findViewById(C0136R.id.formclient_pb);
        try {
            List<MessageVo> messages = MessageDao.getInstance().getMessagesByJid(JID, ImMainActivity.mOwerJid, 1);
            for (int i = messages.size() - 1; i >= 0; i--) {
                String userid;
                String form;
                MessageVo msgda = (MessageVo) messages.get(i);
                if (msgda.direction == MessageVo.DIRECTION_OUT) {
                    userid = ImMainActivity.xmppShowUserName;
                    form = MessageVo.DIRECTION_OUT;
                } else {
                    userid = talkToName;
                    form = MessageVo.DIRECTION_IN;
                }
                this.listMsg.add(new Msg(userid, msgda.content, msgda.time, form));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        this.newchat = XmppConnection.getConnection().getChatManager().createChat(JID, null);
        ((Button) findViewById(C0136R.id.formclient_btsend)).setOnClickListener(new C01542(JID));
    }

    private void init() {
        this.mTitleTextView = (TextView) findViewById(C0136R.id.topBarTitle);
        this.mTitleTextView.setText(getIntent().getStringExtra("TALKTO"));
        this.mKeyboardModeArea = findViewById(C0136R.id.keyboardModeArea);
        this.mPressed2TalkBtn = (RecordButton) findViewById(C0136R.id.voiceModeBtn);
        String path = RECORD_ROOT_PATH;
        new File(path).mkdirs();
        this.mPressed2TalkBtn.setSavePath(new StringBuilder(String.valueOf(path)).append(FilePathGenerator.ANDROID_DIR_SEP).append(System.currentTimeMillis()).append(".amr").toString());
        this.mPressed2TalkBtn.setOnFinishedRecordListener(new C10483());
        this.mSetChatModeBtn = (ImageButton) findViewById(C0136R.id.setChatModeBtn);
        this.mSetChatModeBtn.setTag(Integer.valueOf(0));
        this.mSetChatModeBtn.setOnClickListener(new C01554());
    }

    public void onDestroy() {
        super.onDestroy();
        MsgManager.getInstance().resetTalk2Jid(null);
        MsgManager.getInstance().resetChatWindowsCallBack(null);
    }

    public void msgChange(MessageVo message) {
        String[] args = new String[]{message.jId, message.content, TimeRender.getDate(), MessageVo.DIRECTION_IN};
        Message msg = this.handler.obtainMessage();
        msg.what = 1;
        msg.obj = args;
        this.handler.sendMessage(msg);
    }

    public void sendFile(String path, Msg msg) {
        this.sendTransfer = new FileTransferManager(XmppConnection.getConnection()).createOutgoingFileTransfer(this.userChatSendFile);
        try {
            this.sendTransfer.sendFile(new File(path), "send file");
            new MyFileStatusThread(this.sendTransfer, msg).start();
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
}

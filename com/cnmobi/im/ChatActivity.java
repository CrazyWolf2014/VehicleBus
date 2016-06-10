package com.cnmobi.im;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.adapter.ChatListAdapter;
import com.cnmobi.im.adapter.ChatSelectAdapter;
import com.cnmobi.im.bo.MsgCallback;
import com.cnmobi.im.bo.MsgManager;
import com.cnmobi.im.dao.MessageDao;
import com.cnmobi.im.dao.RiderDao;
import com.cnmobi.im.dto.MessageVo;
import com.cnmobi.im.dto.Msg;
import com.cnmobi.im.dto.RiderVo;
import com.cnmobi.im.util.TimeRender;
import com.cnmobi.im.util.UiUtils;
import com.cnmobi.im.util.XmppConnection;
import com.cnmobi.im.view.RecordButton;
import com.cnmobi.im.view.RecordButton.OnFinishedRecordListener;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.launch.rcu.socket.SocketCode;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.xmlpull.v1.XmlPullParser;

public class ChatActivity extends Activity implements MsgCallback {
    public static String FILE_ROOT_PATH = null;
    public static String RECEIVED_RECORD_PATH = null;
    public static String RECORD_ROOT_PATH = null;
    private static final String TAG = "ChatActivity";
    private ChatListAdapter adapter;
    private TextView chat_name;
    private ChatManager cm;
    private Handler handler;
    private View headView;
    private List<Msg> listMsg;
    private ImageButton mChatSelectBtn;
    private GridView mChatSelectGrid;
    private View mChatSelectView;
    private View mKeyboardModeArea;
    private RecordButton mPressed2TalkBtn;
    private ImageButton mSetChatModeBtn;
    private EditText msgText;
    private Chat newchat;
    private String pFRIENDID;
    private String pUSERID;
    private int pageNo;
    private OutgoingFileTransfer sendTransfer;
    private String userChat;
    private String userChatSendFile;

    /* renamed from: com.cnmobi.im.ChatActivity.1 */
    class C01431 extends Handler {
        C01431() {
        }

        public void handleMessage(Message msg) {
            Exception e;
            switch (msg.what) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    Msg chatMsg = null;
                    try {
                        MessageVo message = msg.obj;
                        Msg chatMsg2 = new Msg(ChatActivity.this.pFRIENDID, message.content, TimeRender.getDate(), Msg.FROM_TYPE[0]);
                        try {
                            chatMsg2.setType(message.type);
                            chatMsg2.setFilePath(message.filePath);
                            chatMsg2.setReceive(Msg.STATUS[3]);
                            chatMsg = chatMsg2;
                        } catch (Exception e2) {
                            e = e2;
                            chatMsg = chatMsg2;
                            e.printStackTrace();
                            if (chatMsg == null) {
                                ChatActivity.this.listMsg.add(chatMsg);
                                ChatActivity.this.adapter.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception e3) {
                        e = e3;
                        e.printStackTrace();
                        if (chatMsg == null) {
                            ChatActivity.this.listMsg.add(chatMsg);
                            ChatActivity.this.adapter.notifyDataSetChanged();
                        }
                    }
                    if (chatMsg == null) {
                        ChatActivity.this.listMsg.add(chatMsg);
                        ChatActivity.this.adapter.notifyDataSetChanged();
                    }
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    ChatActivity.this.adapter.notifyDataSetChanged();
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    Msg msg2 = msg.obj;
                    System.out.println("\u63a5\u6536\u6587\u4ef6\uff1a" + msg2.getFilePath());
                    ChatActivity.this.listMsg.add(msg2);
                    ChatActivity.this.adapter.notifyDataSetChanged();
                default:
            }
        }
    }

    /* renamed from: com.cnmobi.im.ChatActivity.3 */
    class C01443 implements OnClickListener {
        C01443() {
        }

        public void onClick(View v) {
            if (((Integer) v.getTag()).intValue() == 0) {
                ChatActivity.this.mSetChatModeBtn.setTag(Integer.valueOf(1));
                ChatActivity.this.mSetChatModeBtn.setImageResource(C0136R.drawable.chat_keyboard_btn);
                ChatActivity.this.mKeyboardModeArea.setVisibility(8);
                ChatActivity.this.mPressed2TalkBtn.setVisibility(0);
                return;
            }
            ChatActivity.this.mSetChatModeBtn.setTag(Integer.valueOf(0));
            ChatActivity.this.mSetChatModeBtn.setImageResource(C0136R.drawable.chat_voice_btn);
            ChatActivity.this.mKeyboardModeArea.setVisibility(0);
            ChatActivity.this.mPressed2TalkBtn.setVisibility(8);
        }
    }

    /* renamed from: com.cnmobi.im.ChatActivity.4 */
    class C01454 implements OnClickListener {
        C01454() {
        }

        public void onClick(View v) {
            if (ChatActivity.this.mChatSelectView.getVisibility() == 8) {
                ChatActivity.this.mChatSelectView.setVisibility(0);
            } else {
                ChatActivity.this.mChatSelectView.setVisibility(8);
            }
        }
    }

    /* renamed from: com.cnmobi.im.ChatActivity.6 */
    class C01466 implements OnClickListener {
        C01466() {
        }

        public void onClick(View v) {
            String msg = ChatActivity.this.msgText.getText().toString().trim();
            if (msg.length() > 0) {
                Msg chatMsg = new Msg(ChatActivity.this.pUSERID, msg, TimeRender.getDate(), Msg.FROM_TYPE[1]);
                chatMsg.setType(MessageVo.TYPE_TEXT);
                ChatActivity.this.listMsg.add(chatMsg);
                Msg sendChatMsg = new Msg(ChatActivity.this.pUSERID, msg, TimeRender.getDate(), Msg.FROM_TYPE[0]);
                ChatActivity.this.adapter.notifyDataSetChanged();
                try {
                    sendChatMsg.setType(MessageVo.TYPE_TEXT);
                    String sendJson = Msg.toJson(sendChatMsg);
                    Log.d(ChatActivity.TAG, sendJson);
                    ChatActivity.this.newchat.sendMessage(sendJson);
                    MessageVo msgDatabase = new MessageVo(ChatActivity.this.userChat, TimeRender.getDate(), msg, MessageVo.DIRECTION_OUT);
                    msgDatabase.type = MessageVo.TYPE_TEXT;
                    msgDatabase.duration = 0;
                    msgDatabase.filePath = null;
                    try {
                        MessageDao.getInstance().insert(msgDatabase);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else {
                Toast.makeText(ChatActivity.this, ChatActivity.this.getString(C0136R.string.sendMsgCannotNull), 0).show();
            }
            ChatActivity.this.msgText.setText(XmlPullParser.NO_NAMESPACE);
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
            while (!this.transfer.isDone()) {
                System.out.println("transfer.getStatus-->" + this.transfer.getStatus());
                System.out.println("transfer.getProgress-->" + this.transfer.getProgress());
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
            System.out.println("--------------------------" + this.msg.getReceive());
            ChatActivity.this.handler.sendEmptyMessage(3);
        }
    }

    /* renamed from: com.cnmobi.im.ChatActivity.2 */
    class C10442 implements OnFinishedRecordListener {
        C10442() {
        }

        public void onFinishedRecord(String audioPath, int time) {
            try {
                RiderVo rider = RiderDao.getInstance().getRiderByJid(ChatActivity.this.userChat, ImMainActivity.mOwerJid);
                if (rider != null && rider.online == 0) {
                    Toast.makeText(ChatActivity.this, ChatActivity.this.getString(C0136R.string.unSupportSendVoiceOffline), 0).show();
                    return;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            Log.i("RECORD!!!", "finished!!!!!!!!!! save to " + audioPath);
            ChatActivity.this.mPressed2TalkBtn.setSavePath(ChatActivity.RECORD_ROOT_PATH + FilePathGenerator.ANDROID_DIR_SEP + System.currentTimeMillis() + ".amr");
            if (audioPath != null) {
                try {
                    Msg myChatMsg = new Msg(ChatActivity.this.pUSERID, new StringBuilder(String.valueOf(time)).append("\u201d").append(ChatActivity.this.getString(C0136R.string.voiceMessage)).toString(), TimeRender.getDate(), Msg.FROM_TYPE[1], Msg.TYPE[0], Msg.STATUS[3], new StringBuilder(String.valueOf(time)).toString(), audioPath);
                    ChatActivity.this.listMsg.add(myChatMsg);
                    MessageVo msgDatabase = new MessageVo(ChatActivity.this.userChat, TimeRender.getDate(), new StringBuilder(String.valueOf(time)).append("\u201d").append(ChatActivity.this.getString(C0136R.string.voiceMessage)).toString(), MessageVo.DIRECTION_OUT);
                    msgDatabase.type = SocketCode.REMOTE_RECORD_BUTTON;
                    msgDatabase.duration = time;
                    msgDatabase.filePath = audioPath;
                    try {
                        MessageDao.getInstance().insert(msgDatabase);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String[] pathStrings = audioPath.split(FilePathGenerator.ANDROID_DIR_SEP);
                    String fileName = null;
                    if (pathStrings != null && pathStrings.length > 0) {
                        fileName = pathStrings[pathStrings.length - 1];
                    }
                    Msg sendChatMsg = new Msg(ChatActivity.this.pUSERID, new StringBuilder(String.valueOf(time)).append("\u201d").append(ChatActivity.this.getString(C0136R.string.voiceMessage)).toString(), TimeRender.getDate(), Msg.FROM_TYPE[0], Msg.TYPE[0], Msg.STATUS[3], new StringBuilder(String.valueOf(time)).toString(), fileName);
                    ChatActivity.this.adapter.notifyDataSetChanged();
                    ChatActivity.this.newchat.sendMessage(Msg.toJson(sendChatMsg));
                    ChatActivity.this.sendFile(audioPath, myChatMsg);
                    return;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return;
                }
            }
            Toast.makeText(ChatActivity.this, ChatActivity.this.getString(C0136R.string.sendFail), 0).show();
        }
    }

    /* renamed from: com.cnmobi.im.ChatActivity.5 */
    class C10465 implements ChatManagerListener {

        /* renamed from: com.cnmobi.im.ChatActivity.5.1 */
        class C10451 implements MessageListener {
            C10451() {
            }

            public void processMessage(Chat chat2, org.jivesoftware.smack.packet.Message message) {
                if (message.getFrom().contains(ChatActivity.this.userChat) && message.getBody() != null) {
                    Message msg = ChatActivity.this.handler.obtainMessage();
                    System.out.println("\u670d\u52a1\u5668\u53d1\u6765\u7684\u6d88\u606f\u662f chat\uff1a" + message.getBody());
                    msg.what = 1;
                    msg.obj = message.getBody();
                    msg.sendToTarget();
                }
            }
        }

        C10465() {
        }

        public void chatCreated(Chat chat, boolean able) {
            chat.addMessageListener(new C10451());
        }
    }

    /* renamed from: com.cnmobi.im.ChatActivity.7 */
    class C10477 implements FileTransferListener {
        C10477() {
        }

        public void fileTransferRequest(FileTransferRequest request) {
            Log.d("receivedFile ", " receive file");
            Msg msgInfo;
            if (ChatActivity.this.shouldAccept(request)) {
                IncomingFileTransfer transfer = request.accept();
                try {
                    System.out.println(request.getFileName());
                    File file = new File(ChatActivity.RECORD_ROOT_PATH + request.getFileName());
                    Message msg = ChatActivity.this.handler.obtainMessage();
                    transfer.recieveFile(file);
                    msgInfo = null;
                    for (Msg temp : ChatActivity.this.listMsg) {
                        System.out.println("FilePath : " + temp.getFilePath());
                        if (temp.getFilePath() != null && temp.getFilePath().contains(file.getName())) {
                            msgInfo = temp;
                            break;
                        }
                    }
                    msgInfo.setFilePath(file.getPath());
                    new MyFileStatusThread(transfer, msgInfo).start();
                    return;
                } catch (XMPPException e) {
                    e.printStackTrace();
                    return;
                }
            }
            request.reject();
            String[] args = new String[]{ChatActivity.this.userChat, request.getFileName(), TimeRender.getDate(), MessageVo.DIRECTION_IN, Msg.TYPE[0], Msg.STATUS[1]};
            msgInfo = new Msg(args[0], "redio", args[2], args[3], Msg.TYPE[0], Msg.STATUS[1]);
            msg = ChatActivity.this.handler.obtainMessage();
            msg.what = 5;
            msg.obj = msgInfo;
            ChatActivity.this.handler.sendMessage(msg);
        }
    }

    public ChatActivity() {
        this.userChat = XmlPullParser.NO_NAMESPACE;
        this.userChatSendFile = XmlPullParser.NO_NAMESPACE;
        this.listMsg = new LinkedList();
        this.pageNo = 1;
        this.handler = new C01431();
    }

    static {
        FILE_ROOT_PATH = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/chat/file").toString();
        RECORD_ROOT_PATH = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/chat/record").toString();
        RECEIVED_RECORD_PATH = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/chat").toString();
        new File(FILE_ROOT_PATH).mkdirs();
        new File(RECORD_ROOT_PATH).mkdirs();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(C0136R.layout.chat_windows);
        init();
        initMsg();
        String path = RECORD_ROOT_PATH;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        this.mPressed2TalkBtn.setSavePath(new StringBuilder(String.valueOf(path)).append(FilePathGenerator.ANDROID_DIR_SEP).append(System.currentTimeMillis()).append(".amr").toString());
        this.mPressed2TalkBtn.setOnFinishedRecordListener(new C10442());
    }

    private void init() {
        this.pUSERID = ImMainActivity.mUserId;
        this.userChat = getIntent().getStringExtra("JID");
        this.userChatSendFile = this.userChat + "/Smack";
        this.pFRIENDID = getIntent().getStringExtra("TALKTO");
        System.out.println("\u63a5\u6536\u6d88\u606f\u7684\u7528\u6237pFRIENDID\u662f\uff1a" + this.userChat);
        System.out.println("\u53d1\u9001\u6d88\u606f\u7684\u7528\u6237pUSERID\u662f\uff1a" + this.pUSERID);
        System.out.println(" \u6d88\u606f\u7684\u7528\u6237pFRIENDID\u662f\uff1a" + this.pFRIENDID);
        this.chat_name = (TextView) findViewById(C0136R.id.topBarTitle);
        this.chat_name.setText(this.pFRIENDID);
        ListView listview = (ListView) findViewById(C0136R.id.formclient_listview);
        listview.setTranscriptMode(2);
        this.adapter = new ChatListAdapter(this, this.listMsg, this.userChat);
        listview.setAdapter(this.adapter);
        this.msgText = (EditText) findViewById(C0136R.id.formclient_text);
        this.cm = XmppConnection.getConnection().getChatManager();
        UiUtils.enabledBackButton(this);
        MsgManager.getInstance().resetTalk2Jid(this.userChat);
        MsgManager.getInstance().resetChatWindowsCallBack(this);
        MsgManager.getInstance().read(this.userChat);
        this.mPressed2TalkBtn = (RecordButton) findViewById(C0136R.id.voiceModeBtn);
        this.mKeyboardModeArea = findViewById(C0136R.id.keyboardModeArea);
        this.mSetChatModeBtn = (ImageButton) findViewById(C0136R.id.setChatModeBtn);
        this.mSetChatModeBtn.setTag(Integer.valueOf(0));
        this.mSetChatModeBtn.setOnClickListener(new C01443());
        sendMsg();
        receivedFile();
        this.mChatSelectView = findViewById(C0136R.id.chatSelectArea);
        this.mChatSelectBtn = (ImageButton) findViewById(C0136R.id.chatSelectBtn);
        this.mChatSelectBtn.setVisibility(8);
        this.mChatSelectGrid = (GridView) findViewById(C0136R.id.chatSelectGrid);
        this.mChatSelectGrid.setAdapter(new ChatSelectAdapter(this));
        this.mChatSelectBtn.setOnClickListener(new C01454());
    }

    private void initMsg() {
        try {
            List<MessageVo> messages = MessageDao.getInstance().getMessagesByJid(this.userChat, ImMainActivity.mOwerJid, this.pageNo);
            for (int i = messages.size() - 1; i >= 0; i--) {
                String userid;
                String form;
                MessageVo msgda = (MessageVo) messages.get(i);
                if (MessageVo.DIRECTION_OUT.equals(msgda.direction)) {
                    userid = ImMainActivity.xmppShowUserName;
                    form = MessageVo.DIRECTION_OUT;
                } else {
                    userid = this.pFRIENDID;
                    form = MessageVo.DIRECTION_IN;
                }
                Msg tem = new Msg(userid, msgda.content, msgda.time, form);
                tem.setType(msgda.type);
                tem.setFilePath(msgda.filePath);
                this.listMsg.add(tem);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void receivedMsg() {
        this.cm.addChatListener(new C10465());
    }

    public void sendMsg() {
        Button btsend = (Button) findViewById(C0136R.id.formclient_btsend);
        this.newchat = this.cm.createChat(this.userChat, null);
        btsend.setOnClickListener(new C01466());
    }

    public void receivedFile() {
        new FileTransferManager(XmppConnection.getConnection()).addFileTransferListener(new C10477());
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

    private boolean shouldAccept(FileTransferRequest request) {
        boolean[] isAccept = new boolean[1];
        return true;
    }

    protected void dialog() {
    }

    private Msg queryMsgForListMsg(String filePath) {
        Msg msg = null;
        for (int i = this.listMsg.size() - 1; i >= 0; i--) {
            msg = (Msg) this.listMsg.get(i);
            if (filePath != null && filePath.contains(msg.getFilePath())) {
                return msg;
            }
        }
        return msg;
    }

    public void msgChange(MessageVo message) {
        Message msg = this.handler.obtainMessage();
        msg.what = 1;
        msg.obj = message;
        this.handler.sendMessage(msg);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mChatSelectView.setVisibility(8);
        if (102 == requestCode && -1 == resultCode && data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String filePath = bundle.getString(MessageVo.TYPE_FILE);
                Msg myChatMsg = new Msg(this.pUSERID, getString(C0136R.string.sendFile), TimeRender.getDate(), Msg.FROM_TYPE[1], Msg.TYPE[2], Msg.STATUS[3], XmlPullParser.NO_NAMESPACE, filePath);
                this.listMsg.add(myChatMsg);
                MessageVo messageVo = new MessageVo(this.userChat, TimeRender.getDate(), getString(C0136R.string.sendFile), MessageVo.DIRECTION_OUT);
                messageVo.type = MessageVo.TYPE_FILE;
                messageVo.duration = 0;
                messageVo.filePath = filePath;
                try {
                    MessageDao.getInstance().insert(messageVo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String[] pathStrings = filePath.split(FilePathGenerator.ANDROID_DIR_SEP);
                String fileName = null;
                if (pathStrings != null && pathStrings.length > 0) {
                    fileName = pathStrings[pathStrings.length - 1];
                }
                Msg sendChatMsg = new Msg(this.pUSERID, getString(C0136R.string.sendFile), TimeRender.getDate(), Msg.FROM_TYPE[0], Msg.TYPE[2], Msg.STATUS[3], XmlPullParser.NO_NAMESPACE, fileName);
                this.adapter.notifyDataSetChanged();
                try {
                    this.newchat.sendMessage(Msg.toJson(sendChatMsg));
                } catch (XMPPException e2) {
                    e2.printStackTrace();
                }
                sendFile(filePath, myChatMsg);
            }
        }
    }

    public void onBackPressed() {
        if (this.mChatSelectView.getVisibility() == 8) {
            super.onBackPressed();
        } else {
            this.mChatSelectView.setVisibility(8);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        MsgManager.getInstance().resetTalk2Jid(null);
        MsgManager.getInstance().resetChatWindowsCallBack(null);
        this.adapter.release();
    }
}

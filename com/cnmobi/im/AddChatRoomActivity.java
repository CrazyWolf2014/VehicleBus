package com.cnmobi.im;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.util.UiUtils;
import com.cnmobi.im.util.XmppConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.xmlpull.v1.XmlPullParser;

public class AddChatRoomActivity extends Activity {
    private static final String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~\uff01@#\uffe5%\u2026\u2026& amp;*\uff08\uff09\u2014\u2014+|{}\u3010\u3011\u2018\uff1b\uff1a\u201d\u201c\u2019\u3002\uff0c\u3001\uff1f]";
    private Button mAddFinished;
    private EditText mChatRoomDesc;
    private EditText mChatRoomName;
    private TextView mTopBarTitle;
    private MultiUserChat muc;
    private Pattern pattern;

    /* renamed from: com.cnmobi.im.AddChatRoomActivity.1 */
    class C01401 implements OnClickListener {
        C01401() {
        }

        public void onClick(View v) {
            String name = AddChatRoomActivity.this.mChatRoomName.getText().toString().trim();
            String desc = AddChatRoomActivity.this.mChatRoomDesc.getText().toString().trim();
            if (XmlPullParser.NO_NAMESPACE.equals(name)) {
                Toast.makeText(AddChatRoomActivity.this, AddChatRoomActivity.this.getString(C0136R.string.inputChatroomNameTips), 0).show();
            } else if (XmlPullParser.NO_NAMESPACE.equals(desc)) {
                Toast.makeText(AddChatRoomActivity.this, AddChatRoomActivity.this.getString(C0136R.string.inputChatroomDescTips), 0).show();
            } else if (AddChatRoomActivity.this.pattern.matcher(name).find()) {
                Toast.makeText(AddChatRoomActivity.this, AddChatRoomActivity.this.getString(C0136R.string.chatroomNameIllegal), 0).show();
            } else if (XmppConnection.getConnection() == null || !XmppConnection.getConnection().isConnected()) {
                Toast.makeText(AddChatRoomActivity.this, AddChatRoomActivity.this.getString(C0136R.string.donotConnectedRelogin), 0).show();
            } else {
                try {
                    AddChatRoomActivity.this.muc = new MultiUserChat(XmppConnection.getConnection(), new StringBuilder(String.valueOf(name)).append("@conference.").append(XmppConnection.getConnection().getServiceName()).toString());
                    try {
                        AddChatRoomActivity.this.muc.create(name);
                        AddChatRoomActivity.this.muc.changeSubject(desc);
                        Form form = AddChatRoomActivity.this.muc.getConfigurationForm();
                        Form submitForm = form.createAnswerForm();
                        Iterator<FormField> fields = form.getFields();
                        while (fields.hasNext()) {
                            FormField field = (FormField) fields.next();
                            if (!(FormField.TYPE_HIDDEN.equals(field.getType()) || field.getVariable() == null)) {
                                submitForm.setDefaultAnswer(field.getVariable());
                            }
                        }
                        List owners = new ArrayList();
                        owners.add(XmppConnection.getConnection().getUser());
                        submitForm.setAnswer("muc#roomconfig_roomowners", owners);
                        submitForm.setAnswer("muc#roomconfig_persistentroom", true);
                        submitForm.setAnswer("muc#roomconfig_membersonly", false);
                        submitForm.setAnswer("muc#roomconfig_allowinvites", true);
                        submitForm.setAnswer("muc#roomconfig_enablelogging", true);
                        submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
                        submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
                        submitForm.setAnswer("x-muc#roomconfig_registration", false);
                        submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", true);
                        AddChatRoomActivity.this.muc.sendConfigurationForm(submitForm);
                        Toast.makeText(AddChatRoomActivity.this, AddChatRoomActivity.this.getString(C0136R.string.createChatroomSuccess), 0).show();
                        AddChatRoomActivity.this.finish();
                    } catch (XMPPException e) {
                        Toast.makeText(AddChatRoomActivity.this, AddChatRoomActivity.this.getString(C0136R.string.chatroomNameAlreadyExist), 0).show();
                    }
                } catch (XMPPException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public AddChatRoomActivity() {
        this.pattern = Pattern.compile(regEx);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.add_chatroom);
        init();
    }

    private void init() {
        this.mTopBarTitle = (TextView) findViewById(C0136R.id.topBarTitle);
        this.mTopBarTitle.setText(getResources().getString(C0136R.string.addChatRoom));
        UiUtils.enabledBackButton(this);
        this.mChatRoomName = (EditText) findViewById(C0136R.id.chatRoomName);
        this.mChatRoomDesc = (EditText) findViewById(C0136R.id.chatRoomDesc);
        this.mAddFinished = (Button) findViewById(C0136R.id.addFinished);
        this.mAddFinished.setOnClickListener(new C01401());
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.muc != null) {
            try {
                this.muc.leave();
            } catch (Exception e) {
            }
        }
    }
}

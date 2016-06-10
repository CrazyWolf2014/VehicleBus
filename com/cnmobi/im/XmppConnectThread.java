package com.cnmobi.im;

import android.os.Handler;
import android.util.Log;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.bo.CnmobiImManager;
import com.cnmobi.im.bo.MsgManager;
import com.cnmobi.im.bo.RiderManager;
import com.cnmobi.im.dao.RiderDao;
import com.cnmobi.im.dto.MessageVo;
import com.cnmobi.im.dto.RiderVo;
import com.cnmobi.im.util.XmppConnection;
import com.ifoer.mine.Contact;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.Registration;
import org.xmlpull.v1.XmlPullParser;

public class XmppConnectThread extends Thread {
    public static final int CONNECT_SERVER_FAIL = 100;
    public static final int NEW_MSG_COMING = 10;
    private static final String TAG = "XmppConnectThread";
    private Handler mHandler;
    private String mUserId;
    private String mUserPwd;

    /* renamed from: com.cnmobi.im.XmppConnectThread.1 */
    class C10501 implements ChatManagerListener {

        /* renamed from: com.cnmobi.im.XmppConnectThread.1.1 */
        class C10491 implements MessageListener {
            C10491() {
            }

            public void processMessage(Chat chat2, Message msg) {
                Log.i("XMPP", "new Message in!" + msg.getFrom() + "-" + msg.getType());
                MsgManager.getInstance().addNewMsg(new MessageVo(msg.getFrom().split(FilePathGenerator.ANDROID_DIR_SEP)[0], String.valueOf(System.currentTimeMillis()), msg.getBody(), MessageVo.DIRECTION_IN));
            }
        }

        C10501() {
        }

        public void chatCreated(Chat chat, boolean able) {
            chat.addMessageListener(new C10491());
        }
    }

    /* renamed from: com.cnmobi.im.XmppConnectThread.2 */
    class C10512 implements RosterListener {
        private final /* synthetic */ Roster val$roster;

        C10512(Roster roster) {
            this.val$roster = roster;
        }

        public void presenceChanged(Presence presence) {
            System.out.println("presenceChanged : " + presence.getFrom() + "-" + presence.getType() + "-" + presence.getStatus());
            if ("error".equals(presence.getType().name()) && "CANCEL".equals(presence.getError().getType().name())) {
                try {
                    this.val$roster.removeEntry(this.val$roster.getEntry(presence.getFrom()));
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
            String jId = presence.getFrom().split(FilePathGenerator.ANDROID_DIR_SEP)[0];
            int online = 0;
            if ("available".equals(presence.getType().name())) {
                online = 1;
            }
            String signature = presence.getStatus();
            if (online == 0) {
                signature = ImMainActivity.context.getString(C0136R.string.offline);
            }
            try {
                RiderManager.getInstance().updatePresence(jId, online, signature);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            MsgManager.getInstance().presenceChange(presence);
        }

        public void entriesUpdated(Collection<String> collection) {
            System.out.println("entriesUpdated" + collection.toString());
            MsgManager.getInstance().entriesChange(collection, 3);
        }

        public void entriesDeleted(Collection<String> collection) {
            System.out.println("entriesUpdated" + collection.toString());
            MsgManager.getInstance().entriesChange(collection, 2);
        }

        public void entriesAdded(Collection<String> collection) {
            System.out.println("entriesAdded" + collection.toString());
            for (String jid : collection) {
                String type = this.val$roster.getEntry(jid).getType().name();
                RiderVo rider = new RiderVo();
                rider.jId = jid;
                rider.name = jid.split(XmppConnection.JID_SEPARATOR)[0];
                rider.online = 0;
                rider.signature = XmlPullParser.NO_NAMESPACE;
                rider.type = type;
                try {
                    if (!RiderDao.getInstance().isRiderExist(rider.jId, ImMainActivity.mOwerJid)) {
                        RiderDao.getInstance().insert(rider);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            MsgManager.getInstance().entriesChange(collection, 1);
        }
    }

    public XmppConnectThread(Handler handler, String userId, String usePwd) {
        this.mHandler = handler;
        this.mUserId = userId;
        this.mUserPwd = usePwd;
    }

    public void run() {
        if (XmppConnection.getConnection() != null && XmppConnection.getConnection().isConnected()) {
            XmppConnection.getConnection().disconnect();
        }
        if (XmppConnection.getConnection().isConnected()) {
            if (!XmppConnection.getConnection().isAuthenticated()) {
                SmackAndroid.init(ImMainActivity.context);
                try {
                    XmppConnection.getConnection().login(this.mUserId, this.mUserPwd);
                    XmppConnection.getConnection().sendPacket(new Presence(Type.available));
                } catch (Exception e) {
                    Log.e(TAG, "Login to Xmpp Server error", e);
                    if (Contact.RELATION_FRIEND.equals(regist(XmppConnection.getConnection(), this.mUserId, this.mUserPwd))) {
                        this.mHandler.postDelayed(this, 1000);
                        return;
                    } else {
                        this.mHandler.sendEmptyMessage(CONNECT_SERVER_FAIL);
                        return;
                    }
                }
            }
            ImMainActivity.xmppUser = XmppConnection.getConnection().getUser();
            ImMainActivity.xmppShowUserName = ImMainActivity.xmppUser.split(XmppConnection.JID_SEPARATOR)[0];
            XmppConnection.getConnection().sendPacket(new Presence(Type.available));
            ChatManager cm = XmppConnection.getConnection().getChatManager();
            if (cm.getChatListeners() == null || cm.getChatListeners().size() == 0) {
                cm.addChatListener(new C10501());
            }
            Roster roster = XmppConnection.getConnection().getRoster();
            roster.addRosterListener(new C10512(roster));
            this.mHandler.sendEmptyMessage(0);
            if (XmppConnection.getConnection().isAuthenticated()) {
                Log.i(TAG, "Login to Xmpp Server Success!");
                try {
                    RiderManager.getInstance().refresh(ImMainActivity.mOwerJid);
                    return;
                } catch (Exception e2) {
                    Log.e(TAG, "Refresh Friends error:", e2);
                    return;
                }
            }
            Log.i(TAG, "Login to Xmpp Server Fail!");
            return;
        }
        Log.e(TAG, "Connect to Xmpp Server error");
    }

    private void getRiders() {
        Collection<RosterGroup> entriesGroup = XmppConnection.getConnection().getRoster().getGroups();
        List<RiderVo> riders = new ArrayList();
        for (RosterGroup group : entriesGroup) {
            for (RosterEntry entry : group.getEntries()) {
                System.out.println("name-->" + entry.getName());
                RiderVo rider = new RiderVo();
                rider.jId = entry.getName() + XmppConnection.JID_SEPARATOR + XmppConnection.getConnection().getServiceName();
                rider.name = entry.getName();
                rider.online = 0;
                rider.signature = ImMainActivity.context.getString(C0136R.string.offline);
                rider.type = entry.getType().name();
                riders.add(rider);
            }
        }
        CnmobiImManager.getInstance().riderChange(riders);
    }

    public String regist(XMPPConnection connection, String account, String password) {
        if (connection == null) {
            return Contact.RELATION_ASK;
        }
        Registration reg = new Registration();
        reg.setType(IQ.Type.SET);
        reg.setTo(connection.getServiceName());
        reg.setUsername(account);
        reg.setPassword(password);
        reg.addAttribute("android", "auto_create_by_android");
        PacketCollector collector = connection.createPacketCollector(new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class)));
        connection.sendPacket(reg);
        IQ result = (IQ) collector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (result == null) {
            Log.e("RegistActivity", "No response from server.");
            return Contact.RELATION_ASK;
        } else if (result.getType() == IQ.Type.RESULT) {
            return Contact.RELATION_FRIEND;
        } else {
            if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
                Log.e("RegistActivity", "IQ.Type.ERROR: " + result.getError().toString());
                return Contact.RELATION_BACKNAME;
            }
            Log.e("RegistActivity", "IQ.Type.ERROR: " + result.getError().toString());
            return Contact.RELATION_NODONE;
        }
    }
}

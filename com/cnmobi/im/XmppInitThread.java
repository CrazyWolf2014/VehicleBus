package com.cnmobi.im;

import android.os.Handler;
import android.util.Log;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.bo.MsgManager;
import com.cnmobi.im.bo.RiderManager;
import com.cnmobi.im.dao.RiderDao;
import com.cnmobi.im.dto.MessageVo;
import com.cnmobi.im.dto.RiderVo;
import com.cnmobi.im.util.XmppConnection;
import com.ifoer.mine.Contact;
import com.launch.rcu.socket.SocketCode;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.util.Collection;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.jivesoftware.smack.packet.Registration;

public class XmppInitThread extends Thread {
    public static final int CONNECT_SERVER_FAIL = 100;
    private static final String TAG = "XmppInitThread";
    private static int autoReConnectCount;
    public static boolean isConnected;
    private Handler mHandler;
    private String mUserId;
    private String mUserPwd;

    /* renamed from: com.cnmobi.im.XmppInitThread.1 */
    class C10521 implements ConnectionListener {
        C10521() {
        }

        public void connectionClosed() {
            Log.d(XmppInitThread.TAG, "Connection closed !");
            XmppInitThread.isConnected = false;
        }

        public void connectionClosedOnError(Exception e) {
            Log.d(XmppInitThread.TAG, "Connection closed due to an exception!");
            XmppInitThread.isConnected = false;
            e.printStackTrace();
        }

        public void reconnectionFailed(Exception e) {
            Log.d(XmppInitThread.TAG, "Reconnection failed due to an exception!");
            XmppInitThread.isConnected = false;
            e.printStackTrace();
        }

        public void reconnectionSuccessful() {
            Log.d(XmppInitThread.TAG, "Connection reconnected ");
            XmppInitThread.isConnected = true;
        }

        public void reconnectingIn(int seconds) {
            Log.d(XmppInitThread.TAG, "Connection  will reconnect in " + seconds);
            XmppInitThread.isConnected = false;
        }
    }

    /* renamed from: com.cnmobi.im.XmppInitThread.2 */
    class C10542 implements ChatManagerListener {

        /* renamed from: com.cnmobi.im.XmppInitThread.2.1 */
        class C10531 implements MessageListener {
            C10531() {
            }

            public void processMessage(Chat chat2, Message msg) {
                Log.i("XMPP", "new Message in!" + msg.getFrom() + "-" + msg.getType() + "--->" + msg.getBody());
                String jid = msg.getFrom().split(FilePathGenerator.ANDROID_DIR_SEP)[0];
                RosterEntry rosterEntry = XmppConnection.getConnection().getRoster().getEntry(jid);
                if (rosterEntry != null) {
                    if (PrivacyRule.SUBSCRIPTION_BOTH.equals(rosterEntry.getType().name())) {
                        MessageVo message = new MessageVo(msg.getBody());
                        message.jId = jid;
                        message.direction = MessageVo.DIRECTION_IN;
                        if (SocketCode.REMOTE_RECORD_BUTTON.equals(message.type)) {
                            message.filePath = ChatActivity.RECEIVED_RECORD_PATH + "/record" + message.filePath;
                            System.out.println("message.filePath" + message.filePath);
                        }
                        MsgManager.getInstance().addNewMsg(message);
                    }
                }
            }
        }

        C10542() {
        }

        public void chatCreated(Chat chat, boolean able) {
            chat.addMessageListener(new C10531());
        }
    }

    /* renamed from: com.cnmobi.im.XmppInitThread.3 */
    class C10553 implements RosterListener {
        private final /* synthetic */ Roster val$roster;

        C10553(Roster roster) {
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
            } else {
                signature = ImMainActivity.context.getString(C0136R.string.online);
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
            for (String jid : collection) {
                RosterEntry rosterEntry = this.val$roster.getEntry(jid);
                String type = rosterEntry.getType().name();
                String nickname = rosterEntry.getName();
                try {
                    if (PrivacyRule.SUBSCRIPTION_BOTH.equals(RiderDao.getInstance().getRiderByJid(jid, ImMainActivity.mOwerJid).type) && type.equals(PrivacyRule.SUBSCRIPTION_FROM)) {
                        type = PrivacyRule.SUBSCRIPTION_BOTH;
                    }
                    RiderDao.getInstance().update(jid, type, nickname);
                } catch (Exception e) {
                    Log.e(XmppInitThread.TAG, "Update Rider Type Error:", e);
                }
            }
            MsgManager.getInstance().entriesChange(collection, 3);
        }

        public void entriesDeleted(Collection<String> collection) {
            System.out.println("entriesDeleted" + collection.toString());
            for (String jid : collection) {
                try {
                    RiderDao.getInstance().delete(jid, ImMainActivity.mOwerJid);
                    MsgManager.getInstance().read(jid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            MsgManager.getInstance().entriesChange(collection, 2);
        }

        public void entriesAdded(Collection<String> collection) {
            System.out.println("entriesAdded" + collection.toString());
            for (String jid : collection) {
                RosterEntry rosterEntry = this.val$roster.getEntry(jid);
                String type = rosterEntry.getType().name();
                String showName = rosterEntry.getName();
                if (showName == null) {
                    showName = jid.split(XmppConnection.JID_SEPARATOR)[0];
                }
                RiderVo rider = new RiderVo();
                rider.jId = jid;
                rider.name = showName;
                rider.online = 0;
                rider.signature = ImMainActivity.context.getString(C0136R.string.offline);
                rider.type = type;
                try {
                    if (!RiderDao.getInstance().isRiderExist(rider.jId, ImMainActivity.mOwerJid)) {
                        RiderDao.getInstance().insert(rider);
                        PrivacyRule.SUBSCRIPTION_FROM.equals(type);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            MsgManager.getInstance().entriesChange(collection, 1);
        }
    }

    public XmppInitThread(Handler handler, String userId, String usePwd) {
        this.mHandler = handler;
        this.mUserId = userId;
        this.mUserPwd = usePwd;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r10 = this;
        r8 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r7 = 0;
        r4 = com.cnmobi.im.util.XmppConnection.getConnection();
        r5 = r4.isConnected();
        if (r5 == 0) goto L_0x0032;
    L_0x000d:
        r5 = r4.isAuthenticated();
        if (r5 == 0) goto L_0x0032;
    L_0x0013:
        r5 = r4.getUser();
        r6 = "@";
        r5 = r5.split(r6);
        r3 = r5[r7];
        r5 = r10.mUserId;
        r5 = r5.equals(r3);
        if (r5 == 0) goto L_0x002f;
    L_0x0027:
        r5 = "XmppInitThread";
        r6 = "********Already Login!";
        android.util.Log.d(r5, r6);
    L_0x002e:
        return;
    L_0x002f:
        r4.disconnect();
    L_0x0032:
        r4 = com.cnmobi.im.util.XmppConnection.getConnection();
        if (r4 == 0) goto L_0x003e;
    L_0x0038:
        r5 = r4.isConnected();
        if (r5 != 0) goto L_0x005c;
    L_0x003e:
        if (r4 == 0) goto L_0x0043;
    L_0x0040:
        com.cnmobi.im.util.XmppConnection.closeConnection();
    L_0x0043:
        r5 = autoReConnectCount;
        r6 = 10;
        if (r5 >= r6) goto L_0x0054;
    L_0x0049:
        r5 = r10.mHandler;
        r5.postDelayed(r10, r8);
        r5 = autoReConnectCount;
        r5 = r5 + 1;
        autoReConnectCount = r5;
    L_0x0054:
        r5 = "XmppInitThread";
        r6 = "Connect to Xmpp Server error!";
        android.util.Log.e(r5, r6);
        goto L_0x002e;
    L_0x005c:
        autoReConnectCount = r7;
        r5 = com.cnmobi.im.util.XmppConnection.getConnection();	 Catch:{ Exception -> 0x00e3 }
        r6 = r10.mUserId;	 Catch:{ Exception -> 0x00e3 }
        r7 = r10.mUserPwd;	 Catch:{ Exception -> 0x00e3 }
        r5.login(r6, r7);	 Catch:{ Exception -> 0x00e3 }
        r5 = 1;
        isConnected = r5;	 Catch:{ Exception -> 0x00da }
        r1 = new org.jivesoftware.smack.packet.Presence;	 Catch:{ Exception -> 0x00da }
        r5 = org.jivesoftware.smack.packet.Presence.Type.available;	 Catch:{ Exception -> 0x00da }
        r1.<init>(r5);	 Catch:{ Exception -> 0x00da }
        r5 = com.cnmobi.im.util.XmppConnection.getConnection();	 Catch:{ Exception -> 0x00da }
        r5.sendPacket(r1);	 Catch:{ Exception -> 0x00da }
        r5 = com.cnmobi.im.util.XmppConnection.getConnection();	 Catch:{ Exception -> 0x00da }
        r5 = r5.getUser();	 Catch:{ Exception -> 0x00da }
        com.cnmobi.im.ImMainActivity.xmppUser = r5;	 Catch:{ Exception -> 0x00da }
        r5 = java.lang.System.out;	 Catch:{ Exception -> 0x00da }
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00da }
        r7 = "ImMainActivity.xmppUser--->";
        r6.<init>(r7);	 Catch:{ Exception -> 0x00da }
        r7 = com.cnmobi.im.ImMainActivity.xmppUser;	 Catch:{ Exception -> 0x00da }
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x00da }
        r6 = r6.toString();	 Catch:{ Exception -> 0x00da }
        r5.println(r6);	 Catch:{ Exception -> 0x00da }
        r5 = com.cnmobi.im.ImMainActivity.xmppUser;	 Catch:{ Exception -> 0x00da }
        r6 = "@";
        r5 = r5.split(r6);	 Catch:{ Exception -> 0x00da }
        r6 = 0;
        r5 = r5[r6];	 Catch:{ Exception -> 0x00da }
        com.cnmobi.im.ImMainActivity.xmppShowUserName = r5;	 Catch:{ Exception -> 0x00da }
        r10.initConnectionListener();	 Catch:{ Exception -> 0x00da }
        r10.initChatListener();	 Catch:{ Exception -> 0x00da }
        r10.initRosterListener();	 Catch:{ Exception -> 0x00da }
        r5 = com.cnmobi.im.util.XmppConnection.getConnection();	 Catch:{ Exception -> 0x00da }
        r5 = r5.isAuthenticated();	 Catch:{ Exception -> 0x00da }
        if (r5 == 0) goto L_0x0123;
    L_0x00ba:
        r5 = "XmppInitThread";
        r6 = "Login to Xmpp Server Success!";
        android.util.Log.i(r5, r6);	 Catch:{ Exception -> 0x00da }
        r5 = com.cnmobi.im.bo.RiderManager.getInstance();	 Catch:{ Exception -> 0x0111 }
        r6 = com.cnmobi.im.ImMainActivity.mOwerJid;	 Catch:{ Exception -> 0x0111 }
        r5.refresh(r6);	 Catch:{ Exception -> 0x0111 }
    L_0x00ca:
        r5 = com.cnmobi.im.bo.ChatroomManager.getInstance();	 Catch:{ Exception -> 0x011a }
        r5.refresh();	 Catch:{ Exception -> 0x011a }
    L_0x00d1:
        r5 = com.cnmobi.im.bo.LogoManager.getInstance();	 Catch:{ Exception -> 0x00da }
        r5.getRiderLogos();	 Catch:{ Exception -> 0x00da }
        goto L_0x002e;
    L_0x00da:
        r0 = move-exception;
        com.cnmobi.im.util.XmppConnection.closeConnection();
        r0.printStackTrace();
        goto L_0x002e;
    L_0x00e3:
        r0 = move-exception;
        r5 = "XmppInitThread";
        r6 = "Login to Xmpp Server error";
        android.util.Log.e(r5, r6, r0);	 Catch:{ Exception -> 0x00da }
        r5 = com.cnmobi.im.util.XmppConnection.getConnection();	 Catch:{ Exception -> 0x00da }
        r6 = r10.mUserId;	 Catch:{ Exception -> 0x00da }
        r7 = r10.mUserPwd;	 Catch:{ Exception -> 0x00da }
        r2 = r10.regist(r5, r6, r7);	 Catch:{ Exception -> 0x00da }
        r5 = "1";
        r5 = r5.equals(r2);	 Catch:{ Exception -> 0x00da }
        if (r5 == 0) goto L_0x0108;
    L_0x00ff:
        r5 = r10.mHandler;	 Catch:{ Exception -> 0x00da }
        r6 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r5.postDelayed(r10, r6);	 Catch:{ Exception -> 0x00da }
        goto L_0x002e;
    L_0x0108:
        r5 = r10.mHandler;	 Catch:{ Exception -> 0x00da }
        r6 = 100;
        r5.sendEmptyMessage(r6);	 Catch:{ Exception -> 0x00da }
        goto L_0x002e;
    L_0x0111:
        r0 = move-exception;
        r5 = "XmppInitThread";
        r6 = "Refresh Friends error:";
        android.util.Log.e(r5, r6, r0);	 Catch:{ Exception -> 0x00da }
        goto L_0x00ca;
    L_0x011a:
        r0 = move-exception;
        r5 = "XmppInitThread";
        r6 = "Refresh Chatrooms error:";
        android.util.Log.e(r5, r6, r0);	 Catch:{ Exception -> 0x00da }
        goto L_0x00d1;
    L_0x0123:
        r5 = "XmppInitThread";
        r6 = "Login to Xmpp Server Fail!";
        android.util.Log.i(r5, r6);	 Catch:{ Exception -> 0x00da }
        goto L_0x002e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cnmobi.im.XmppInitThread.run():void");
    }

    private void initConnectionListener() {
        XmppConnection.getConnection().addConnectionListener(new C10521());
    }

    private void initChatListener() {
        ChatManager cm = XmppConnection.getConnection().getChatManager();
        if (cm.getChatListeners() == null || cm.getChatListeners().size() == 0) {
            cm.addChatListener(new C10542());
        }
    }

    private void initRosterListener() {
        Roster roster = XmppConnection.getConnection().getRoster();
        roster.addRosterListener(new C10553(roster));
    }

    public String regist(XMPPConnection connection, String account, String password) {
        if (connection == null) {
            return Contact.RELATION_ASK;
        }
        Registration reg = new Registration();
        reg.setType(Type.SET);
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
        } else if (result.getType() == Type.RESULT) {
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

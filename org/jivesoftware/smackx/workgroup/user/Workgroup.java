package org.jivesoftware.smackx.workgroup.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.MUCUser;
import org.jivesoftware.smackx.packet.MUCUser.Invite;
import org.jivesoftware.smackx.workgroup.MetaData;
import org.jivesoftware.smackx.workgroup.WorkgroupInvitation;
import org.jivesoftware.smackx.workgroup.WorkgroupInvitationListener;
import org.jivesoftware.smackx.workgroup.ext.forms.WorkgroupForm;
import org.jivesoftware.smackx.workgroup.packet.AgentStatusRequest;
import org.jivesoftware.smackx.workgroup.packet.DepartQueuePacket;
import org.jivesoftware.smackx.workgroup.packet.QueueUpdate;
import org.jivesoftware.smackx.workgroup.packet.SessionID;
import org.jivesoftware.smackx.workgroup.packet.UserID;
import org.jivesoftware.smackx.workgroup.settings.ChatSetting;
import org.jivesoftware.smackx.workgroup.settings.ChatSettings;
import org.jivesoftware.smackx.workgroup.settings.OfflineSettings;
import org.jivesoftware.smackx.workgroup.settings.SoundSettings;
import org.jivesoftware.smackx.workgroup.settings.WorkgroupProperties;

public class Workgroup {
    private Connection connection;
    private boolean inQueue;
    private List<WorkgroupInvitationListener> invitationListeners;
    private List<QueueListener> queueListeners;
    private int queuePosition;
    private int queueRemainingTime;
    private String workgroupJID;

    /* renamed from: org.jivesoftware.smackx.workgroup.user.Workgroup.1 */
    class C12181 implements QueueListener {
        C12181() {
        }

        public void joinedQueue() {
            Workgroup.this.inQueue = true;
        }

        public void departedQueue() {
            Workgroup.this.inQueue = false;
            Workgroup.this.queuePosition = -1;
            Workgroup.this.queueRemainingTime = -1;
        }

        public void queuePositionUpdated(int i) {
            Workgroup.this.queuePosition = i;
        }

        public void queueWaitTimeUpdated(int i) {
            Workgroup.this.queueRemainingTime = i;
        }
    }

    /* renamed from: org.jivesoftware.smackx.workgroup.user.Workgroup.2 */
    class C12192 implements InvitationListener {
        C12192() {
        }

        public void invitationReceived(Connection connection, String str, String str2, String str3, String str4, Message message) {
            Workgroup.this.inQueue = false;
            Workgroup.this.queuePosition = -1;
            Workgroup.this.queueRemainingTime = -1;
        }
    }

    /* renamed from: org.jivesoftware.smackx.workgroup.user.Workgroup.3 */
    class C12203 implements PacketListener {
        C12203() {
        }

        public void processPacket(Packet packet) {
            Workgroup.this.handlePacket(packet);
        }
    }

    private class JoinQueuePacket extends IQ {
        private DataForm form;
        private String userID;

        public JoinQueuePacket(String str, Form form, String str2) {
            this.userID = null;
            this.userID = str2;
            setTo(str);
            setType(Type.SET);
            this.form = form.getDataFormToSend();
            addExtension(this.form);
        }

        public String getChildElementXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<join-queue xmlns=\"http://jabber.org/protocol/workgroup\">");
            stringBuilder.append("<queue-notifications/>");
            if (Workgroup.this.connection.isAnonymous()) {
                stringBuilder.append(new UserID(this.userID).toXML());
            }
            stringBuilder.append(this.form.toXML());
            stringBuilder.append("</join-queue>");
            return stringBuilder.toString();
        }
    }

    public Workgroup(String str, Connection connection) {
        this.queuePosition = -1;
        this.queueRemainingTime = -1;
        if (connection.isAuthenticated()) {
            this.workgroupJID = str;
            this.connection = connection;
            this.inQueue = false;
            this.invitationListeners = new ArrayList();
            this.queueListeners = new ArrayList();
            addQueueListener(new C12181());
            MultiUserChat.addInvitationListener(connection, new C12192());
            connection.addPacketListener(new C12203(), new PacketTypeFilter(Message.class));
            return;
        }
        throw new IllegalStateException("Must login to server before creating workgroup.");
    }

    public String getWorkgroupJID() {
        return this.workgroupJID;
    }

    public boolean isInQueue() {
        return this.inQueue;
    }

    public boolean isAvailable() {
        Packet presence = new Presence(Presence.Type.available);
        presence.setTo(this.workgroupJID);
        PacketTypeFilter packetTypeFilter = new PacketTypeFilter(Presence.class);
        FromContainsFilter fromContainsFilter = new FromContainsFilter(this.workgroupJID);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new AndFilter(fromContainsFilter, packetTypeFilter));
        this.connection.sendPacket(presence);
        Presence presence2 = (Presence) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (presence2 == null || presence2.getError() != null) {
            return false;
        }
        return Presence.Type.available == presence2.getType();
    }

    public int getQueuePosition() {
        return this.queuePosition;
    }

    public int getQueueRemainingTime() {
        return this.queueRemainingTime;
    }

    public void joinQueue() throws XMPPException {
        joinQueue(null);
    }

    public void joinQueue(Form form) throws XMPPException {
        joinQueue(form, null);
    }

    public void joinQueue(Form form, String str) throws XMPPException {
        if (this.inQueue) {
            throw new IllegalStateException("Already in queue " + this.workgroupJID);
        }
        Packet joinQueuePacket = new JoinQueuePacket(this.workgroupJID, form, str);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(joinQueuePacket.getPacketID()));
        this.connection.sendPacket(joinQueuePacket);
        IQ iq = (IQ) createPacketCollector.nextResult(10000);
        createPacketCollector.cancel();
        if (iq == null) {
            throw new XMPPException("No response from the server.");
        } else if (iq.getError() != null) {
            throw new XMPPException(iq.getError());
        } else {
            fireQueueJoinedEvent();
        }
    }

    public void joinQueue(Map<String, Object> map, String str) throws XMPPException {
        if (this.inQueue) {
            throw new IllegalStateException("Already in queue " + this.workgroupJID);
        }
        Form form = new Form(Form.TYPE_SUBMIT);
        for (String str2 : map.keySet()) {
            String obj = map.get(str2).toString();
            String str22 = StringUtils.escapeForXML(str22);
            obj = StringUtils.escapeForXML(obj);
            FormField formField = new FormField(str22);
            formField.setType(FormField.TYPE_TEXT_SINGLE);
            form.addField(formField);
            form.setAnswer(str22, obj);
        }
        joinQueue(form, str);
    }

    public void departQueue() throws XMPPException {
        if (this.inQueue) {
            Packet departQueuePacket = new DepartQueuePacket(this.workgroupJID);
            PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(departQueuePacket.getPacketID()));
            this.connection.sendPacket(departQueuePacket);
            IQ iq = (IQ) createPacketCollector.nextResult(5000);
            createPacketCollector.cancel();
            if (iq == null) {
                throw new XMPPException("No response from the server.");
            } else if (iq.getError() != null) {
                throw new XMPPException(iq.getError());
            } else {
                fireQueueDepartedEvent();
            }
        }
    }

    public void addQueueListener(QueueListener queueListener) {
        synchronized (this.queueListeners) {
            if (!this.queueListeners.contains(queueListener)) {
                this.queueListeners.add(queueListener);
            }
        }
    }

    public void removeQueueListener(QueueListener queueListener) {
        synchronized (this.queueListeners) {
            this.queueListeners.remove(queueListener);
        }
    }

    public void addInvitationListener(WorkgroupInvitationListener workgroupInvitationListener) {
        synchronized (this.invitationListeners) {
            if (!this.invitationListeners.contains(workgroupInvitationListener)) {
                this.invitationListeners.add(workgroupInvitationListener);
            }
        }
    }

    public void removeQueueListener(WorkgroupInvitationListener workgroupInvitationListener) {
        synchronized (this.invitationListeners) {
            this.invitationListeners.remove(workgroupInvitationListener);
        }
    }

    private void fireInvitationEvent(WorkgroupInvitation workgroupInvitation) {
        synchronized (this.invitationListeners) {
            for (WorkgroupInvitationListener invitationReceived : this.invitationListeners) {
                invitationReceived.invitationReceived(workgroupInvitation);
            }
        }
    }

    private void fireQueueJoinedEvent() {
        synchronized (this.queueListeners) {
            for (QueueListener joinedQueue : this.queueListeners) {
                joinedQueue.joinedQueue();
            }
        }
    }

    private void fireQueueDepartedEvent() {
        synchronized (this.queueListeners) {
            for (QueueListener departedQueue : this.queueListeners) {
                departedQueue.departedQueue();
            }
        }
    }

    private void fireQueuePositionEvent(int i) {
        synchronized (this.queueListeners) {
            for (QueueListener queuePositionUpdated : this.queueListeners) {
                queuePositionUpdated.queuePositionUpdated(i);
            }
        }
    }

    private void fireQueueTimeEvent(int i) {
        synchronized (this.queueListeners) {
            for (QueueListener queueWaitTimeUpdated : this.queueListeners) {
                queueWaitTimeUpdated.queueWaitTimeUpdated(i);
            }
        }
    }

    private void handlePacket(Packet packet) {
        if (packet instanceof Message) {
            Message message = (Message) packet;
            PacketExtension extension = message.getExtension("depart-queue", AgentStatusRequest.NAMESPACE);
            PacketExtension extension2 = message.getExtension(QueueUpdate.ELEMENT_NAME, AgentStatusRequest.NAMESPACE);
            if (extension != null) {
                fireQueueDepartedEvent();
            } else if (extension2 != null) {
                QueueUpdate queueUpdate = (QueueUpdate) extension2;
                if (queueUpdate.getPosition() != -1) {
                    fireQueuePositionEvent(queueUpdate.getPosition());
                }
                if (queueUpdate.getRemaingTime() != -1) {
                    fireQueueTimeEvent(queueUpdate.getRemaingTime());
                }
            } else {
                MUCUser mUCUser = (MUCUser) message.getExtension(GroupChatInvitation.ELEMENT_NAME, "http://jabber.org/protocol/muc#user");
                Invite invite = mUCUser != null ? mUCUser.getInvite() : null;
                if (invite != null && this.workgroupJID.equals(invite.getFrom())) {
                    String sessionID;
                    Map metaData;
                    extension2 = message.getExtension(SessionID.ELEMENT_NAME, WorkgroupProperties.NAMESPACE);
                    if (extension2 != null) {
                        sessionID = ((SessionID) extension2).getSessionID();
                    } else {
                        sessionID = null;
                    }
                    extension2 = message.getExtension(MetaData.ELEMENT_NAME, WorkgroupProperties.NAMESPACE);
                    if (extension2 != null) {
                        metaData = ((MetaData) extension2).getMetaData();
                    } else {
                        metaData = null;
                    }
                    fireInvitationEvent(new WorkgroupInvitation(this.connection.getUser(), message.getFrom(), this.workgroupJID, sessionID, message.getBody(), message.getFrom(), metaData));
                }
            }
        }
    }

    public ChatSetting getChatSetting(String str) throws XMPPException {
        return getChatSettings(str, -1).getFirstEntry();
    }

    public ChatSettings getChatSettings(int i) throws XMPPException {
        return getChatSettings(null, i);
    }

    public ChatSettings getChatSettings() throws XMPPException {
        return getChatSettings(null, -1);
    }

    private ChatSettings getChatSettings(String str, int i) throws XMPPException {
        Packet chatSettings = new ChatSettings();
        if (str != null) {
            chatSettings.setKey(str);
        }
        if (i != -1) {
            chatSettings.setType(i);
        }
        chatSettings.setType(Type.GET);
        chatSettings.setTo(this.workgroupJID);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(chatSettings.getPacketID()));
        this.connection.sendPacket(chatSettings);
        ChatSettings chatSettings2 = (ChatSettings) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (chatSettings2 == null) {
            throw new XMPPException("No response from server.");
        } else if (chatSettings2.getError() == null) {
            return chatSettings2;
        } else {
            throw new XMPPException(chatSettings2.getError());
        }
    }

    public boolean isEmailAvailable() {
        try {
            return ServiceDiscoveryManager.getInstanceFor(this.connection).discoverInfo(StringUtils.parseServer(this.workgroupJID)).containsFeature("jive:email:provider");
        } catch (XMPPException e) {
            return false;
        }
    }

    public OfflineSettings getOfflineSettings() throws XMPPException {
        Packet offlineSettings = new OfflineSettings();
        offlineSettings.setType(Type.GET);
        offlineSettings.setTo(this.workgroupJID);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(offlineSettings.getPacketID()));
        this.connection.sendPacket(offlineSettings);
        OfflineSettings offlineSettings2 = (OfflineSettings) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (offlineSettings2 == null) {
            throw new XMPPException("No response from server.");
        } else if (offlineSettings2.getError() == null) {
            return offlineSettings2;
        } else {
            throw new XMPPException(offlineSettings2.getError());
        }
    }

    public SoundSettings getSoundSettings() throws XMPPException {
        Packet soundSettings = new SoundSettings();
        soundSettings.setType(Type.GET);
        soundSettings.setTo(this.workgroupJID);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(soundSettings.getPacketID()));
        this.connection.sendPacket(soundSettings);
        SoundSettings soundSettings2 = (SoundSettings) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (soundSettings2 == null) {
            throw new XMPPException("No response from server.");
        } else if (soundSettings2.getError() == null) {
            return soundSettings2;
        } else {
            throw new XMPPException(soundSettings2.getError());
        }
    }

    public WorkgroupProperties getWorkgroupProperties() throws XMPPException {
        Packet workgroupProperties = new WorkgroupProperties();
        workgroupProperties.setType(Type.GET);
        workgroupProperties.setTo(this.workgroupJID);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(workgroupProperties.getPacketID()));
        this.connection.sendPacket(workgroupProperties);
        WorkgroupProperties workgroupProperties2 = (WorkgroupProperties) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (workgroupProperties2 == null) {
            throw new XMPPException("No response from server.");
        } else if (workgroupProperties2.getError() == null) {
            return workgroupProperties2;
        } else {
            throw new XMPPException(workgroupProperties2.getError());
        }
    }

    public WorkgroupProperties getWorkgroupProperties(String str) throws XMPPException {
        Packet workgroupProperties = new WorkgroupProperties();
        workgroupProperties.setJid(str);
        workgroupProperties.setType(Type.GET);
        workgroupProperties.setTo(this.workgroupJID);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(workgroupProperties.getPacketID()));
        this.connection.sendPacket(workgroupProperties);
        WorkgroupProperties workgroupProperties2 = (WorkgroupProperties) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (workgroupProperties2 == null) {
            throw new XMPPException("No response from server.");
        } else if (workgroupProperties2.getError() == null) {
            return workgroupProperties2;
        } else {
            throw new XMPPException(workgroupProperties2.getError());
        }
    }

    public Form getWorkgroupForm() throws XMPPException {
        Packet workgroupForm = new WorkgroupForm();
        workgroupForm.setType(Type.GET);
        workgroupForm.setTo(this.workgroupJID);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(workgroupForm.getPacketID()));
        this.connection.sendPacket(workgroupForm);
        WorkgroupForm workgroupForm2 = (WorkgroupForm) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (workgroupForm2 == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (workgroupForm2.getError() == null) {
            return Form.getFormFrom(workgroupForm2);
        } else {
            throw new XMPPException(workgroupForm2.getError());
        }
    }
}

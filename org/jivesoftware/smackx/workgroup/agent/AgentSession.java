package org.jivesoftware.smackx.workgroup.agent;

import com.ifoer.util.MyHttpException;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.codehaus.jackson.util.BufferRecycler;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.OrFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.packet.MUCUser;
import org.jivesoftware.smackx.packet.MUCUser.Invite;
import org.jivesoftware.smackx.workgroup.MetaData;
import org.jivesoftware.smackx.workgroup.QueueUser;
import org.jivesoftware.smackx.workgroup.WorkgroupInvitation;
import org.jivesoftware.smackx.workgroup.WorkgroupInvitationListener;
import org.jivesoftware.smackx.workgroup.agent.WorkgroupQueue.Status;
import org.jivesoftware.smackx.workgroup.ext.history.AgentChatHistory;
import org.jivesoftware.smackx.workgroup.ext.history.ChatMetadata;
import org.jivesoftware.smackx.workgroup.ext.macros.MacroGroup;
import org.jivesoftware.smackx.workgroup.ext.macros.Macros;
import org.jivesoftware.smackx.workgroup.ext.notes.ChatNotes;
import org.jivesoftware.smackx.workgroup.packet.AgentStatus;
import org.jivesoftware.smackx.workgroup.packet.AgentStatusRequest;
import org.jivesoftware.smackx.workgroup.packet.DepartQueuePacket;
import org.jivesoftware.smackx.workgroup.packet.MonitorPacket;
import org.jivesoftware.smackx.workgroup.packet.OccupantsInfo;
import org.jivesoftware.smackx.workgroup.packet.OfferRequestProvider.OfferRequestPacket;
import org.jivesoftware.smackx.workgroup.packet.OfferRevokeProvider.OfferRevokePacket;
import org.jivesoftware.smackx.workgroup.packet.QueueDetails;
import org.jivesoftware.smackx.workgroup.packet.QueueOverview;
import org.jivesoftware.smackx.workgroup.packet.RoomInvitation;
import org.jivesoftware.smackx.workgroup.packet.RoomTransfer;
import org.jivesoftware.smackx.workgroup.packet.SessionID;
import org.jivesoftware.smackx.workgroup.packet.Transcript;
import org.jivesoftware.smackx.workgroup.packet.Transcripts;
import org.jivesoftware.smackx.workgroup.settings.GenericSettings;
import org.jivesoftware.smackx.workgroup.settings.SearchSettings;
import org.jivesoftware.smackx.workgroup.settings.WorkgroupProperties;
import org.xmlpull.v1.XmlPullParser;

public class AgentSession {
    private Agent agent;
    private AgentRoster agentRoster;
    private Connection connection;
    private final List<WorkgroupInvitationListener> invitationListeners;
    private int maxChats;
    private final Map<String, List<String>> metaData;
    private final List<OfferListener> offerListeners;
    private boolean online;
    private PacketListener packetListener;
    private Mode presenceMode;
    private final List<QueueUsersListener> queueUsersListeners;
    private Map<String, WorkgroupQueue> queues;
    private TranscriptManager transcriptManager;
    private TranscriptSearchManager transcriptSearchManager;
    private String workgroupJID;

    /* renamed from: org.jivesoftware.smackx.workgroup.agent.AgentSession.1 */
    class C12171 implements PacketListener {
        C12171() {
        }

        public void processPacket(Packet packet) {
            try {
                AgentSession.this.handlePacket(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.workgroup.agent.AgentSession.2 */
    class C12942 extends IQ {
        C12942() {
        }

        public String getChildElementXML() {
            return null;
        }
    }

    /* renamed from: org.jivesoftware.smackx.workgroup.agent.AgentSession.3 */
    class C12953 extends IQ {
        C12953() {
        }

        public String getChildElementXML() {
            return null;
        }
    }

    /* renamed from: org.jivesoftware.smackx.workgroup.agent.AgentSession.4 */
    class C12964 extends IQ {
        final /* synthetic */ RoomInvitation val$invitation;

        C12964(RoomInvitation roomInvitation) {
            this.val$invitation = roomInvitation;
        }

        public String getChildElementXML() {
            return this.val$invitation.toXML();
        }
    }

    /* renamed from: org.jivesoftware.smackx.workgroup.agent.AgentSession.5 */
    class C12975 extends IQ {
        final /* synthetic */ RoomTransfer val$transfer;

        C12975(RoomTransfer roomTransfer) {
            this.val$transfer = roomTransfer;
        }

        public String getChildElementXML() {
            return this.val$transfer.toXML();
        }
    }

    public AgentSession(String str, Connection connection) {
        this.online = false;
        this.agentRoster = null;
        if (connection.isAuthenticated()) {
            this.workgroupJID = str;
            this.connection = connection;
            this.transcriptManager = new TranscriptManager(connection);
            this.transcriptSearchManager = new TranscriptSearchManager(connection);
            this.maxChats = -1;
            this.metaData = new HashMap();
            this.queues = new HashMap();
            this.offerListeners = new ArrayList();
            this.invitationListeners = new ArrayList();
            this.queueUsersListeners = new ArrayList();
            PacketFilter orFilter = new OrFilter();
            orFilter.addFilter(new PacketTypeFilter(OfferRequestPacket.class));
            orFilter.addFilter(new PacketTypeFilter(OfferRevokePacket.class));
            orFilter.addFilter(new PacketTypeFilter(Presence.class));
            orFilter.addFilter(new PacketTypeFilter(Message.class));
            this.packetListener = new C12171();
            connection.addPacketListener(this.packetListener, orFilter);
            this.agent = new Agent(connection, str);
            return;
        }
        throw new IllegalStateException("Must login to server before creating workgroup.");
    }

    public void close() {
        this.connection.removePacketListener(this.packetListener);
    }

    public AgentRoster getAgentRoster() {
        if (this.agentRoster == null) {
            this.agentRoster = new AgentRoster(this.connection, this.workgroupJID);
        }
        int i = 0;
        while (!this.agentRoster.rosterInitialized && i <= BufferRecycler.DEFAULT_WRITE_CONCAT_BUFFER_LEN) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
            i += MyHttpException.ERROR_SERVER;
        }
        return this.agentRoster;
    }

    public Mode getPresenceMode() {
        return this.presenceMode;
    }

    public int getMaxChats() {
        return this.maxChats;
    }

    public boolean isOnline() {
        return this.online;
    }

    public void setMetaData(String str, String str2) throws XMPPException {
        synchronized (this.metaData) {
            List list = (List) this.metaData.get(str);
            if (list == null || !((String) list.get(0)).equals(str2)) {
                list.set(0, str2);
                setStatus(this.presenceMode, this.maxChats);
            }
        }
    }

    public void removeMetaData(String str) throws XMPPException {
        synchronized (this.metaData) {
            if (((List) this.metaData.remove(str)) != null) {
                setStatus(this.presenceMode, this.maxChats);
            }
        }
    }

    public List<String> getMetaData(String str) {
        return (List) this.metaData.get(str);
    }

    public void setOnline(boolean z) throws XMPPException {
        if (this.online != z) {
            Packet presence;
            if (z) {
                presence = new Presence(Type.available);
                presence.setTo(this.workgroupJID);
                presence.addExtension(new DefaultPacketExtension(AgentStatus.ELEMENT_NAME, AgentStatusRequest.NAMESPACE));
                PacketCollector createPacketCollector = this.connection.createPacketCollector(new AndFilter(new PacketTypeFilter(Presence.class), new FromContainsFilter(this.workgroupJID)));
                this.connection.sendPacket(presence);
                Presence presence2 = (Presence) createPacketCollector.nextResult(5000);
                createPacketCollector.cancel();
                if (!presence2.isAvailable()) {
                    throw new XMPPException("No response from server on status set.");
                } else if (presence2.getError() != null) {
                    throw new XMPPException(presence2.getError());
                } else {
                    this.online = z;
                    return;
                }
            }
            this.online = z;
            presence = new Presence(Type.unavailable);
            presence.setTo(this.workgroupJID);
            presence.addExtension(new DefaultPacketExtension(AgentStatus.ELEMENT_NAME, AgentStatusRequest.NAMESPACE));
            this.connection.sendPacket(presence);
        }
    }

    public void setStatus(Mode mode, int i) throws XMPPException {
        setStatus(mode, i, null);
    }

    public void setStatus(Mode mode, int i, String str) throws XMPPException {
        if (this.online) {
            if (mode == null) {
                mode = Mode.available;
            }
            this.presenceMode = mode;
            this.maxChats = i;
            Packet presence = new Presence(Type.available);
            presence.setMode(mode);
            presence.setTo(getWorkgroupJID());
            if (str != null) {
                presence.setStatus(str);
            }
            PacketExtension defaultPacketExtension = new DefaultPacketExtension(AgentStatus.ELEMENT_NAME, AgentStatusRequest.NAMESPACE);
            defaultPacketExtension.setValue("max-chats", XmlPullParser.NO_NAMESPACE + i);
            presence.addExtension(defaultPacketExtension);
            presence.addExtension(new MetaData(this.metaData));
            PacketCollector createPacketCollector = this.connection.createPacketCollector(new AndFilter(new PacketTypeFilter(Presence.class), new FromContainsFilter(this.workgroupJID)));
            this.connection.sendPacket(presence);
            Presence presence2 = (Presence) createPacketCollector.nextResult(5000);
            createPacketCollector.cancel();
            if (!presence2.isAvailable()) {
                throw new XMPPException("No response from server on status set.");
            } else if (presence2.getError() != null) {
                throw new XMPPException(presence2.getError());
            } else {
                return;
            }
        }
        throw new IllegalStateException("Cannot set status when the agent is not online.");
    }

    public void setStatus(Mode mode, String str) throws XMPPException {
        if (this.online) {
            if (mode == null) {
                mode = Mode.available;
            }
            this.presenceMode = mode;
            Packet presence = new Presence(Type.available);
            presence.setMode(mode);
            presence.setTo(getWorkgroupJID());
            if (str != null) {
                presence.setStatus(str);
            }
            presence.addExtension(new MetaData(this.metaData));
            PacketCollector createPacketCollector = this.connection.createPacketCollector(new AndFilter(new PacketTypeFilter(Presence.class), new FromContainsFilter(this.workgroupJID)));
            this.connection.sendPacket(presence);
            Presence presence2 = (Presence) createPacketCollector.nextResult(5000);
            createPacketCollector.cancel();
            if (!presence2.isAvailable()) {
                throw new XMPPException("No response from server on status set.");
            } else if (presence2.getError() != null) {
                throw new XMPPException(presence2.getError());
            } else {
                return;
            }
        }
        throw new IllegalStateException("Cannot set status when the agent is not online.");
    }

    public void dequeueUser(String str) throws XMPPException {
        this.connection.sendPacket(new DepartQueuePacket(this.workgroupJID));
    }

    public Transcripts getTranscripts(String str) throws XMPPException {
        return this.transcriptManager.getTranscripts(this.workgroupJID, str);
    }

    public Transcript getTranscript(String str) throws XMPPException {
        return this.transcriptManager.getTranscript(this.workgroupJID, str);
    }

    public Form getTranscriptSearchForm() throws XMPPException {
        return this.transcriptSearchManager.getSearchForm(StringUtils.parseServer(this.workgroupJID));
    }

    public ReportedData searchTranscripts(Form form) throws XMPPException {
        return this.transcriptSearchManager.submitSearch(StringUtils.parseServer(this.workgroupJID), form);
    }

    public OccupantsInfo getOccupantsInfo(String str) throws XMPPException {
        Packet occupantsInfo = new OccupantsInfo(str);
        occupantsInfo.setType(IQ.Type.GET);
        occupantsInfo.setTo(this.workgroupJID);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(occupantsInfo.getPacketID()));
        this.connection.sendPacket(occupantsInfo);
        OccupantsInfo occupantsInfo2 = (OccupantsInfo) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (occupantsInfo2 == null) {
            throw new XMPPException("No response from server.");
        } else if (occupantsInfo2.getError() == null) {
            return occupantsInfo2;
        } else {
            throw new XMPPException(occupantsInfo2.getError());
        }
    }

    public String getWorkgroupJID() {
        return this.workgroupJID;
    }

    public Agent getAgent() {
        return this.agent;
    }

    public WorkgroupQueue getQueue(String str) {
        return (WorkgroupQueue) this.queues.get(str);
    }

    public Iterator<WorkgroupQueue> getQueues() {
        return Collections.unmodifiableMap(new HashMap(this.queues)).values().iterator();
    }

    public void addQueueUsersListener(QueueUsersListener queueUsersListener) {
        synchronized (this.queueUsersListeners) {
            if (!this.queueUsersListeners.contains(queueUsersListener)) {
                this.queueUsersListeners.add(queueUsersListener);
            }
        }
    }

    public void removeQueueUsersListener(QueueUsersListener queueUsersListener) {
        synchronized (this.queueUsersListeners) {
            this.queueUsersListeners.remove(queueUsersListener);
        }
    }

    public void addOfferListener(OfferListener offerListener) {
        synchronized (this.offerListeners) {
            if (!this.offerListeners.contains(offerListener)) {
                this.offerListeners.add(offerListener);
            }
        }
    }

    public void removeOfferListener(OfferListener offerListener) {
        synchronized (this.offerListeners) {
            this.offerListeners.remove(offerListener);
        }
    }

    public void addInvitationListener(WorkgroupInvitationListener workgroupInvitationListener) {
        synchronized (this.invitationListeners) {
            if (!this.invitationListeners.contains(workgroupInvitationListener)) {
                this.invitationListeners.add(workgroupInvitationListener);
            }
        }
    }

    public void removeInvitationListener(WorkgroupInvitationListener workgroupInvitationListener) {
        synchronized (this.invitationListeners) {
            this.invitationListeners.remove(workgroupInvitationListener);
        }
    }

    private void fireOfferRequestEvent(OfferRequestPacket offerRequestPacket) {
        Offer offer = new Offer(this.connection, this, offerRequestPacket.getUserID(), offerRequestPacket.getUserJID(), getWorkgroupJID(), new Date(new Date().getTime() + ((long) (offerRequestPacket.getTimeout() * 1000))), offerRequestPacket.getSessionID(), offerRequestPacket.getMetaData(), offerRequestPacket.getContent());
        synchronized (this.offerListeners) {
            for (OfferListener offerReceived : this.offerListeners) {
                offerReceived.offerReceived(offer);
            }
        }
    }

    private void fireOfferRevokeEvent(OfferRevokePacket offerRevokePacket) {
        RevokedOffer revokedOffer = new RevokedOffer(offerRevokePacket.getUserJID(), offerRevokePacket.getUserID(), getWorkgroupJID(), offerRevokePacket.getSessionID(), offerRevokePacket.getReason(), new Date());
        synchronized (this.offerListeners) {
            for (OfferListener offerRevoked : this.offerListeners) {
                offerRevoked.offerRevoked(revokedOffer);
            }
        }
    }

    private void fireInvitationEvent(String str, String str2, String str3, String str4, Map<String, List<String>> map) {
        WorkgroupInvitation workgroupInvitation = new WorkgroupInvitation(this.connection.getUser(), str, this.workgroupJID, str2, str3, str4, map);
        synchronized (this.invitationListeners) {
            for (WorkgroupInvitationListener invitationReceived : this.invitationListeners) {
                invitationReceived.invitationReceived(workgroupInvitation);
            }
        }
    }

    private void fireQueueUsersEvent(WorkgroupQueue workgroupQueue, Status status, int i, Date date, Set<QueueUser> set) {
        synchronized (this.queueUsersListeners) {
            for (QueueUsersListener queueUsersListener : this.queueUsersListeners) {
                if (status != null) {
                    queueUsersListener.statusUpdated(workgroupQueue, status);
                }
                if (i != -1) {
                    queueUsersListener.averageWaitTimeUpdated(workgroupQueue, i);
                }
                if (date != null) {
                    queueUsersListener.oldestEntryUpdated(workgroupQueue, date);
                }
                if (set != null) {
                    queueUsersListener.usersUpdated(workgroupQueue, set);
                }
            }
        }
    }

    private void handlePacket(Packet packet) {
        Map map = null;
        Packet c12942;
        if (packet instanceof OfferRequestPacket) {
            c12942 = new C12942();
            c12942.setPacketID(packet.getPacketID());
            c12942.setTo(packet.getFrom());
            c12942.setType(IQ.Type.RESULT);
            this.connection.sendPacket(c12942);
            fireOfferRequestEvent((OfferRequestPacket) packet);
        } else if (packet instanceof Presence) {
            Presence presence = (Presence) packet;
            r2 = StringUtils.parseResource(presence.getFrom());
            WorkgroupQueue workgroupQueue = (WorkgroupQueue) this.queues.get(r2);
            if (workgroupQueue == null) {
                workgroupQueue = new WorkgroupQueue(r2);
                this.queues.put(r2, workgroupQueue);
            }
            QueueOverview queueOverview = (QueueOverview) presence.getExtension(QueueOverview.ELEMENT_NAME, QueueOverview.NAMESPACE);
            if (queueOverview != null) {
                if (queueOverview.getStatus() == null) {
                    workgroupQueue.setStatus(Status.CLOSED);
                } else {
                    workgroupQueue.setStatus(queueOverview.getStatus());
                }
                workgroupQueue.setAverageWaitTime(queueOverview.getAverageWaitTime());
                workgroupQueue.setOldestEntry(queueOverview.getOldestEntry());
                fireQueueUsersEvent(workgroupQueue, queueOverview.getStatus(), queueOverview.getAverageWaitTime(), queueOverview.getOldestEntry(), null);
                return;
            }
            QueueDetails queueDetails = (QueueDetails) packet.getExtension(QueueDetails.ELEMENT_NAME, AgentStatusRequest.NAMESPACE);
            if (queueDetails != null) {
                workgroupQueue.setUsers(queueDetails.getUsers());
                fireQueueUsersEvent(workgroupQueue, null, -1, null, queueDetails.getUsers());
                return;
            }
            DefaultPacketExtension defaultPacketExtension = (DefaultPacketExtension) presence.getExtension("notify-agents", AgentStatusRequest.NAMESPACE);
            if (defaultPacketExtension != null) {
                int parseInt = Integer.parseInt(defaultPacketExtension.getValue("current-chats"));
                int parseInt2 = Integer.parseInt(defaultPacketExtension.getValue("max-chats"));
                workgroupQueue.setCurrentChats(parseInt);
                workgroupQueue.setMaxChats(parseInt2);
            }
        } else if (packet instanceof Message) {
            Message message = (Message) packet;
            MUCUser mUCUser = (MUCUser) message.getExtension(GroupChatInvitation.ELEMENT_NAME, "http://jabber.org/protocol/muc#user");
            Invite invite = mUCUser != null ? mUCUser.getInvite() : null;
            if (invite != null && this.workgroupJID.equals(invite.getFrom())) {
                SessionID sessionID = (SessionID) message.getExtension(SessionID.ELEMENT_NAME, WorkgroupProperties.NAMESPACE);
                if (sessionID != null) {
                    r2 = sessionID.getSessionID();
                } else {
                    r2 = null;
                }
                MetaData metaData = (MetaData) message.getExtension(MetaData.ELEMENT_NAME, WorkgroupProperties.NAMESPACE);
                if (metaData != null) {
                    map = metaData.getMetaData();
                }
                fireInvitationEvent(message.getFrom(), r2, message.getBody(), message.getFrom(), map);
            }
        } else if (packet instanceof OfferRevokePacket) {
            c12942 = new C12953();
            c12942.setPacketID(packet.getPacketID());
            c12942.setType(IQ.Type.RESULT);
            this.connection.sendPacket(c12942);
            fireOfferRevokeEvent((OfferRevokePacket) packet);
        }
    }

    public void setNote(String str, String str2) throws XMPPException {
        String escapeForXML = StringUtils.escapeForXML(ChatNotes.replace(str2, SpecilApiUtil.LINE_SEP, "\\n"));
        Packet chatNotes = new ChatNotes();
        chatNotes.setType(IQ.Type.SET);
        chatNotes.setTo(this.workgroupJID);
        chatNotes.setSessionID(str);
        chatNotes.setNotes(escapeForXML);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(chatNotes.getPacketID()));
        this.connection.sendPacket(chatNotes);
        IQ iq = (IQ) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (iq == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (iq.getError() != null) {
            throw new XMPPException(iq.getError());
        }
    }

    public ChatNotes getNote(String str) throws XMPPException {
        Packet chatNotes = new ChatNotes();
        chatNotes.setType(IQ.Type.GET);
        chatNotes.setTo(this.workgroupJID);
        chatNotes.setSessionID(str);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(chatNotes.getPacketID()));
        this.connection.sendPacket(chatNotes);
        ChatNotes chatNotes2 = (ChatNotes) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (chatNotes2 == null) {
            throw new XMPPException("No response from server.");
        } else if (chatNotes2.getError() == null) {
            return chatNotes2;
        } else {
            throw new XMPPException(chatNotes2.getError());
        }
    }

    public AgentChatHistory getAgentHistory(String str, int i, Date date) throws XMPPException {
        Packet agentChatHistory;
        if (date != null) {
            agentChatHistory = new AgentChatHistory(str, i, date);
        } else {
            agentChatHistory = new AgentChatHistory(str, i);
        }
        agentChatHistory.setType(IQ.Type.GET);
        agentChatHistory.setTo(this.workgroupJID);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(agentChatHistory.getPacketID()));
        this.connection.sendPacket(agentChatHistory);
        AgentChatHistory agentChatHistory2 = (AgentChatHistory) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (agentChatHistory2 == null) {
            throw new XMPPException("No response from server.");
        } else if (agentChatHistory2.getError() == null) {
            return agentChatHistory2;
        } else {
            throw new XMPPException(agentChatHistory2.getError());
        }
    }

    public SearchSettings getSearchSettings() throws XMPPException {
        Packet searchSettings = new SearchSettings();
        searchSettings.setType(IQ.Type.GET);
        searchSettings.setTo(this.workgroupJID);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(searchSettings.getPacketID()));
        this.connection.sendPacket(searchSettings);
        SearchSettings searchSettings2 = (SearchSettings) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (searchSettings2 == null) {
            throw new XMPPException("No response from server.");
        } else if (searchSettings2.getError() == null) {
            return searchSettings2;
        } else {
            throw new XMPPException(searchSettings2.getError());
        }
    }

    public MacroGroup getMacros(boolean z) throws XMPPException {
        Packet macros = new Macros();
        macros.setType(IQ.Type.GET);
        macros.setTo(this.workgroupJID);
        macros.setPersonal(!z);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(macros.getPacketID()));
        this.connection.sendPacket(macros);
        Macros macros2 = (Macros) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (macros2 == null) {
            throw new XMPPException("No response from server.");
        } else if (macros2.getError() == null) {
            return macros2.getRootGroup();
        } else {
            throw new XMPPException(macros2.getError());
        }
    }

    public void saveMacros(MacroGroup macroGroup) throws XMPPException {
        Packet macros = new Macros();
        macros.setType(IQ.Type.SET);
        macros.setTo(this.workgroupJID);
        macros.setPersonal(true);
        macros.setPersonalMacroGroup(macroGroup);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(macros.getPacketID()));
        this.connection.sendPacket(macros);
        IQ iq = (IQ) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (iq == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (iq.getError() != null) {
            throw new XMPPException(iq.getError());
        }
    }

    public Map<String, List<String>> getChatMetadata(String str) throws XMPPException {
        Packet chatMetadata = new ChatMetadata();
        chatMetadata.setType(IQ.Type.GET);
        chatMetadata.setTo(this.workgroupJID);
        chatMetadata.setSessionID(str);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(chatMetadata.getPacketID()));
        this.connection.sendPacket(chatMetadata);
        ChatMetadata chatMetadata2 = (ChatMetadata) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (chatMetadata2 == null) {
            throw new XMPPException("No response from server.");
        } else if (chatMetadata2.getError() == null) {
            return chatMetadata2.getMetadata();
        } else {
            throw new XMPPException(chatMetadata2.getError());
        }
    }

    public void sendRoomInvitation(RoomInvitation.Type type, String str, String str2, String str3) throws XMPPException {
        Packet c12964 = new C12964(new RoomInvitation(type, str, str2, str3));
        c12964.setType(IQ.Type.SET);
        c12964.setTo(this.workgroupJID);
        c12964.setFrom(this.connection.getUser());
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(c12964.getPacketID()));
        this.connection.sendPacket(c12964);
        IQ iq = (IQ) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (iq == null) {
            throw new XMPPException("No response from server.");
        } else if (iq.getError() != null) {
            throw new XMPPException(iq.getError());
        }
    }

    public void sendRoomTransfer(RoomTransfer.Type type, String str, String str2, String str3) throws XMPPException {
        Packet c12975 = new C12975(new RoomTransfer(type, str, str2, str3));
        c12975.setType(IQ.Type.SET);
        c12975.setTo(this.workgroupJID);
        c12975.setFrom(this.connection.getUser());
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(c12975.getPacketID()));
        this.connection.sendPacket(c12975);
        IQ iq = (IQ) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (iq == null) {
            throw new XMPPException("No response from server.");
        } else if (iq.getError() != null) {
            throw new XMPPException(iq.getError());
        }
    }

    public GenericSettings getGenericSettings(Connection connection, String str) throws XMPPException {
        Packet genericSettings = new GenericSettings();
        genericSettings.setType(IQ.Type.GET);
        genericSettings.setTo(this.workgroupJID);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(genericSettings.getPacketID()));
        this.connection.sendPacket(genericSettings);
        GenericSettings genericSettings2 = (GenericSettings) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (genericSettings2 == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (genericSettings2.getError() == null) {
            return genericSettings2;
        } else {
            throw new XMPPException(genericSettings2.getError());
        }
    }

    public boolean hasMonitorPrivileges(Connection connection) throws XMPPException {
        Packet monitorPacket = new MonitorPacket();
        monitorPacket.setType(IQ.Type.GET);
        monitorPacket.setTo(this.workgroupJID);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(monitorPacket.getPacketID()));
        this.connection.sendPacket(monitorPacket);
        MonitorPacket monitorPacket2 = (MonitorPacket) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (monitorPacket2 == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (monitorPacket2.getError() == null) {
            return monitorPacket2.isMonitor();
        } else {
            throw new XMPPException(monitorPacket2.getError());
        }
    }

    public void makeRoomOwner(Connection connection, String str) throws XMPPException {
        Packet monitorPacket = new MonitorPacket();
        monitorPacket.setType(IQ.Type.SET);
        monitorPacket.setTo(this.workgroupJID);
        monitorPacket.setSessionID(str);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(monitorPacket.getPacketID()));
        this.connection.sendPacket(monitorPacket);
        monitorPacket = createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (monitorPacket == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (monitorPacket.getError() != null) {
            throw new XMPPException(monitorPacket.getError());
        }
    }
}

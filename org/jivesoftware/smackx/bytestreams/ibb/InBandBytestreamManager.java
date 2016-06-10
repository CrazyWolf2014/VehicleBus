package org.jivesoftware.smackx.bytestreams.ibb;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.util.SyncPacketSend;
import org.jivesoftware.smackx.bytestreams.BytestreamListener;
import org.jivesoftware.smackx.bytestreams.BytestreamManager;
import org.jivesoftware.smackx.bytestreams.ibb.packet.Open;
import org.xbill.DNS.KEYRecord.Flags;

public class InBandBytestreamManager implements BytestreamManager {
    public static final int MAXIMUM_BLOCK_SIZE = 65535;
    public static final String NAMESPACE = "http://jabber.org/protocol/ibb";
    private static final String SESSION_ID_PREFIX = "jibb_";
    private static final Map<Connection, InBandBytestreamManager> managers;
    private static final Random randomGenerator;
    private final List<BytestreamListener> allRequestListeners;
    private final CloseListener closeListener;
    private final Connection connection;
    private final DataListener dataListener;
    private int defaultBlockSize;
    private List<String> ignoredBytestreamRequests;
    private final InitiationListener initiationListener;
    private int maximumBlockSize;
    private final Map<String, InBandBytestreamSession> sessions;
    private StanzaType stanza;
    private final Map<String, BytestreamListener> userListeners;

    public enum StanzaType {
        IQ,
        MESSAGE
    }

    /* renamed from: org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager.1 */
    static class C11781 implements ConnectionCreationListener {

        /* renamed from: org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager.1.1 */
        class C12901 extends AbstractConnectionListener {
            final /* synthetic */ InBandBytestreamManager val$manager;

            C12901(InBandBytestreamManager inBandBytestreamManager) {
                this.val$manager = inBandBytestreamManager;
            }

            public void connectionClosed() {
                this.val$manager.disableService();
            }
        }

        C11781() {
        }

        public void connectionCreated(Connection connection) {
            connection.addConnectionListener(new C12901(InBandBytestreamManager.getByteStreamManager(connection)));
        }
    }

    static {
        Connection.addConnectionCreationListener(new C11781());
        randomGenerator = new Random();
        managers = new HashMap();
    }

    public static synchronized InBandBytestreamManager getByteStreamManager(Connection connection) {
        InBandBytestreamManager inBandBytestreamManager;
        synchronized (InBandBytestreamManager.class) {
            if (connection == null) {
                inBandBytestreamManager = null;
            } else {
                inBandBytestreamManager = (InBandBytestreamManager) managers.get(connection);
                if (inBandBytestreamManager == null) {
                    inBandBytestreamManager = new InBandBytestreamManager(connection);
                    managers.put(connection, inBandBytestreamManager);
                }
            }
        }
        return inBandBytestreamManager;
    }

    private InBandBytestreamManager(Connection connection) {
        this.userListeners = new ConcurrentHashMap();
        this.allRequestListeners = Collections.synchronizedList(new LinkedList());
        this.sessions = new ConcurrentHashMap();
        this.defaultBlockSize = Flags.EXTEND;
        this.maximumBlockSize = MAXIMUM_BLOCK_SIZE;
        this.stanza = StanzaType.IQ;
        this.ignoredBytestreamRequests = Collections.synchronizedList(new LinkedList());
        this.connection = connection;
        this.initiationListener = new InitiationListener(this);
        this.connection.addPacketListener(this.initiationListener, this.initiationListener.getFilter());
        this.dataListener = new DataListener(this);
        this.connection.addPacketListener(this.dataListener, this.dataListener.getFilter());
        this.closeListener = new CloseListener(this);
        this.connection.addPacketListener(this.closeListener, this.closeListener.getFilter());
    }

    public void addIncomingBytestreamListener(BytestreamListener bytestreamListener) {
        this.allRequestListeners.add(bytestreamListener);
    }

    public void removeIncomingBytestreamListener(BytestreamListener bytestreamListener) {
        this.allRequestListeners.remove(bytestreamListener);
    }

    public void addIncomingBytestreamListener(BytestreamListener bytestreamListener, String str) {
        this.userListeners.put(str, bytestreamListener);
    }

    public void removeIncomingBytestreamListener(String str) {
        this.userListeners.remove(str);
    }

    public void ignoreBytestreamRequestOnce(String str) {
        this.ignoredBytestreamRequests.add(str);
    }

    public int getDefaultBlockSize() {
        return this.defaultBlockSize;
    }

    public void setDefaultBlockSize(int i) {
        if (i <= 0 || i > MAXIMUM_BLOCK_SIZE) {
            throw new IllegalArgumentException("Default block size must be between 1 and 65535");
        }
        this.defaultBlockSize = i;
    }

    public int getMaximumBlockSize() {
        return this.maximumBlockSize;
    }

    public void setMaximumBlockSize(int i) {
        if (i <= 0 || i > MAXIMUM_BLOCK_SIZE) {
            throw new IllegalArgumentException("Maximum block size must be between 1 and 65535");
        }
        this.maximumBlockSize = i;
    }

    public StanzaType getStanza() {
        return this.stanza;
    }

    public void setStanza(StanzaType stanzaType) {
        this.stanza = stanzaType;
    }

    public InBandBytestreamSession establishSession(String str) throws XMPPException {
        return establishSession(str, getNextSessionID());
    }

    public InBandBytestreamSession establishSession(String str, String str2) throws XMPPException {
        Packet open = new Open(str2, this.defaultBlockSize, this.stanza);
        open.setTo(str);
        SyncPacketSend.getReply(this.connection, open);
        InBandBytestreamSession inBandBytestreamSession = new InBandBytestreamSession(this.connection, open, str);
        this.sessions.put(str2, inBandBytestreamSession);
        return inBandBytestreamSession;
    }

    protected void replyRejectPacket(IQ iq) {
        this.connection.sendPacket(IQ.createErrorResponse(iq, new XMPPError(Condition.no_acceptable)));
    }

    protected void replyResourceConstraintPacket(IQ iq) {
        this.connection.sendPacket(IQ.createErrorResponse(iq, new XMPPError(Condition.resource_constraint)));
    }

    protected void replyItemNotFoundPacket(IQ iq) {
        this.connection.sendPacket(IQ.createErrorResponse(iq, new XMPPError(Condition.item_not_found)));
    }

    private String getNextSessionID() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SESSION_ID_PREFIX);
        stringBuilder.append(Math.abs(randomGenerator.nextLong()));
        return stringBuilder.toString();
    }

    protected Connection getConnection() {
        return this.connection;
    }

    protected BytestreamListener getUserListener(String str) {
        return (BytestreamListener) this.userListeners.get(str);
    }

    protected List<BytestreamListener> getAllRequestListeners() {
        return this.allRequestListeners;
    }

    protected Map<String, InBandBytestreamSession> getSessions() {
        return this.sessions;
    }

    protected List<String> getIgnoredBytestreamRequests() {
        return this.ignoredBytestreamRequests;
    }

    private void disableService() {
        managers.remove(this.connection);
        this.connection.removePacketListener(this.initiationListener);
        this.connection.removePacketListener(this.dataListener);
        this.connection.removePacketListener(this.closeListener);
        this.initiationListener.shutdown();
        this.userListeners.clear();
        this.allRequestListeners.clear();
        this.sessions.clear();
        this.ignoredBytestreamRequests.clear();
    }
}

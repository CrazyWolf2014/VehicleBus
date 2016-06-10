package org.jivesoftware.smack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Privacy;
import org.jivesoftware.smack.packet.PrivacyItem;
import org.xmlpull.v1.XmlPullParser;

public class PrivacyListManager {
    private static Map<Connection, PrivacyListManager> instances;
    private Connection connection;
    private final List<PrivacyListListener> listeners;
    PacketFilter packetFilter;

    /* renamed from: org.jivesoftware.smack.PrivacyListManager.1 */
    static class C11541 implements ConnectionCreationListener {
        C11541() {
        }

        public void connectionCreated(Connection connection) {
            PrivacyListManager privacyListManager = new PrivacyListManager(null);
        }
    }

    /* renamed from: org.jivesoftware.smack.PrivacyListManager.2 */
    class C11552 implements ConnectionListener {
        C11552() {
        }

        public void connectionClosed() {
            PrivacyListManager.instances.remove(PrivacyListManager.this.connection);
        }

        public void connectionClosedOnError(Exception exception) {
        }

        public void reconnectionFailed(Exception exception) {
        }

        public void reconnectingIn(int i) {
        }

        public void reconnectionSuccessful() {
        }
    }

    /* renamed from: org.jivesoftware.smack.PrivacyListManager.3 */
    class C11563 implements PacketListener {

        /* renamed from: org.jivesoftware.smack.PrivacyListManager.3.1 */
        class C12821 extends IQ {
            C12821() {
            }

            public String getChildElementXML() {
                return XmlPullParser.NO_NAMESPACE;
            }
        }

        C11563() {
        }

        public void processPacket(Packet packet) {
            if (packet != null && packet.getError() == null) {
                Privacy privacy = (Privacy) packet;
                synchronized (PrivacyListManager.this.listeners) {
                    for (PrivacyListListener privacyListListener : PrivacyListManager.this.listeners) {
                        for (Entry entry : privacy.getItemLists().entrySet()) {
                            String str = (String) entry.getKey();
                            List list = (List) entry.getValue();
                            if (list.isEmpty()) {
                                privacyListListener.updatedPrivacyList(str);
                            } else {
                                privacyListListener.setPrivacyList(str, list);
                            }
                        }
                    }
                }
                Packet c12821 = new C12821();
                c12821.setType(Type.RESULT);
                c12821.setFrom(packet.getFrom());
                c12821.setPacketID(packet.getPacketID());
                PrivacyListManager.this.connection.sendPacket(c12821);
            }
        }
    }

    static {
        instances = Collections.synchronizedMap(new WeakHashMap());
        Connection.addConnectionCreationListener(new C11541());
    }

    private PrivacyListManager(Connection connection) {
        this.listeners = new ArrayList();
        this.packetFilter = new AndFilter(new IQTypeFilter(Type.SET), new PacketExtensionFilter("query", "jabber:iq:privacy"));
        this.connection = connection;
        init();
    }

    private String getUser() {
        return this.connection.getUser();
    }

    private void init() {
        instances.put(this.connection, this);
        this.connection.addConnectionListener(new C11552());
        this.connection.addPacketListener(new C11563(), this.packetFilter);
    }

    public static PrivacyListManager getInstanceFor(Connection connection) {
        return (PrivacyListManager) instances.get(connection);
    }

    private Privacy getRequest(Privacy privacy) throws XMPPException {
        privacy.setType(Type.GET);
        privacy.setFrom(getUser());
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(privacy.getPacketID()));
        this.connection.sendPacket(privacy);
        Privacy privacy2 = (Privacy) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (privacy2 == null) {
            throw new XMPPException("No response from server.");
        } else if (privacy2.getError() == null) {
            return privacy2;
        } else {
            throw new XMPPException(privacy2.getError());
        }
    }

    private Packet setRequest(Privacy privacy) throws XMPPException {
        privacy.setType(Type.SET);
        privacy.setFrom(getUser());
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(privacy.getPacketID()));
        this.connection.sendPacket(privacy);
        Packet nextResult = createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (nextResult == null) {
            throw new XMPPException("No response from server.");
        } else if (nextResult.getError() == null) {
            return nextResult;
        } else {
            throw new XMPPException(nextResult.getError());
        }
    }

    private Privacy getPrivacyWithListNames() throws XMPPException {
        return getRequest(new Privacy());
    }

    public PrivacyList getActiveList() throws XMPPException {
        Privacy privacyWithListNames = getPrivacyWithListNames();
        String activeName = privacyWithListNames.getActiveName();
        boolean z = (privacyWithListNames.getActiveName() == null || privacyWithListNames.getDefaultName() == null || !privacyWithListNames.getActiveName().equals(privacyWithListNames.getDefaultName())) ? false : true;
        return new PrivacyList(true, z, activeName, getPrivacyListItems(activeName));
    }

    public PrivacyList getDefaultList() throws XMPPException {
        Privacy privacyWithListNames = getPrivacyWithListNames();
        String defaultName = privacyWithListNames.getDefaultName();
        boolean z = (privacyWithListNames.getActiveName() == null || privacyWithListNames.getDefaultName() == null || !privacyWithListNames.getActiveName().equals(privacyWithListNames.getDefaultName())) ? false : true;
        return new PrivacyList(z, true, defaultName, getPrivacyListItems(defaultName));
    }

    private List<PrivacyItem> getPrivacyListItems(String str) throws XMPPException {
        Privacy privacy = new Privacy();
        privacy.setPrivacyList(str, new ArrayList());
        return getRequest(privacy).getPrivacyList(str);
    }

    public PrivacyList getPrivacyList(String str) throws XMPPException {
        return new PrivacyList(false, false, str, getPrivacyListItems(str));
    }

    public PrivacyList[] getPrivacyLists() throws XMPPException {
        Privacy privacyWithListNames = getPrivacyWithListNames();
        Set<String> privacyListNames = privacyWithListNames.getPrivacyListNames();
        PrivacyList[] privacyListArr = new PrivacyList[privacyListNames.size()];
        int i = 0;
        for (String str : privacyListNames) {
            privacyListArr[i] = new PrivacyList(str.equals(privacyWithListNames.getActiveName()), str.equals(privacyWithListNames.getDefaultName()), str, getPrivacyListItems(str));
            i++;
        }
        return privacyListArr;
    }

    public void setActiveListName(String str) throws XMPPException {
        Privacy privacy = new Privacy();
        privacy.setActiveName(str);
        setRequest(privacy);
    }

    public void declineActiveList() throws XMPPException {
        Privacy privacy = new Privacy();
        privacy.setDeclineActiveList(true);
        setRequest(privacy);
    }

    public void setDefaultListName(String str) throws XMPPException {
        Privacy privacy = new Privacy();
        privacy.setDefaultName(str);
        setRequest(privacy);
    }

    public void declineDefaultList() throws XMPPException {
        Privacy privacy = new Privacy();
        privacy.setDeclineDefaultList(true);
        setRequest(privacy);
    }

    public void createPrivacyList(String str, List<PrivacyItem> list) throws XMPPException {
        updatePrivacyList(str, list);
    }

    public void updatePrivacyList(String str, List<PrivacyItem> list) throws XMPPException {
        Privacy privacy = new Privacy();
        privacy.setPrivacyList(str, list);
        setRequest(privacy);
    }

    public void deletePrivacyList(String str) throws XMPPException {
        Privacy privacy = new Privacy();
        privacy.setPrivacyList(str, new ArrayList());
        setRequest(privacy);
    }

    public void addListener(PrivacyListListener privacyListListener) {
        synchronized (this.listeners) {
            this.listeners.add(privacyListListener);
        }
    }
}

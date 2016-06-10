package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smackx.entitycaps.EntityCapsManager;
import org.jivesoftware.smackx.entitycaps.EntityCapsManager.NodeVerHash;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.packet.DiscoverItems;

public class ServiceDiscoveryManager {
    private static final String DEFAULT_IDENTITY_CATEGORY = "client";
    private static final String DEFAULT_IDENTITY_NAME = "Smack";
    private static final String DEFAULT_IDENTITY_TYPE = "pc";
    private static List<Identity> identities;
    private static Map<Connection, ServiceDiscoveryManager> instances;
    private EntityCapsManager capsManager;
    private Connection connection;
    private DataForm extendedInfo;
    private final Set<String> features;
    private Map<String, NodeInformationProvider> nodeInformationProviders;

    /* renamed from: org.jivesoftware.smackx.ServiceDiscoveryManager.1 */
    static class C11731 implements ConnectionCreationListener {
        C11731() {
        }

        public void connectionCreated(Connection connection) {
            ServiceDiscoveryManager serviceDiscoveryManager = new ServiceDiscoveryManager(connection);
        }
    }

    /* renamed from: org.jivesoftware.smackx.ServiceDiscoveryManager.2 */
    class C11742 implements ConnectionListener {
        C11742() {
        }

        public void connectionClosed() {
            ServiceDiscoveryManager.instances.remove(ServiceDiscoveryManager.this.connection);
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

    /* renamed from: org.jivesoftware.smackx.ServiceDiscoveryManager.3 */
    class C11753 implements PacketListener {
        C11753() {
        }

        public void processPacket(Packet packet) {
            DiscoverItems discoverItems = (DiscoverItems) packet;
            if (discoverItems != null && discoverItems.getType() == Type.GET) {
                Packet discoverItems2 = new DiscoverItems();
                discoverItems2.setType(Type.RESULT);
                discoverItems2.setTo(discoverItems.getFrom());
                discoverItems2.setPacketID(discoverItems.getPacketID());
                discoverItems2.setNode(discoverItems.getNode());
                NodeInformationProvider access$200 = ServiceDiscoveryManager.this.getNodeInformationProvider(discoverItems.getNode());
                if (access$200 != null) {
                    discoverItems2.addItems(access$200.getNodeItems());
                    discoverItems2.addExtensions(access$200.getNodePacketExtensions());
                } else if (discoverItems.getNode() != null) {
                    discoverItems2.setType(Type.ERROR);
                    discoverItems2.setError(new XMPPError(Condition.item_not_found));
                }
                ServiceDiscoveryManager.this.connection.sendPacket(discoverItems2);
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.ServiceDiscoveryManager.4 */
    class C11764 implements PacketListener {
        C11764() {
        }

        public void processPacket(Packet packet) {
            DiscoverInfo discoverInfo = (DiscoverInfo) packet;
            if (discoverInfo != null && discoverInfo.getType() == Type.GET) {
                Packet discoverInfo2 = new DiscoverInfo();
                discoverInfo2.setType(Type.RESULT);
                discoverInfo2.setTo(discoverInfo.getFrom());
                discoverInfo2.setPacketID(discoverInfo.getPacketID());
                discoverInfo2.setNode(discoverInfo.getNode());
                if (discoverInfo.getNode() == null) {
                    ServiceDiscoveryManager.this.addDiscoverInfoTo(discoverInfo2);
                } else {
                    NodeInformationProvider access$200 = ServiceDiscoveryManager.this.getNodeInformationProvider(discoverInfo.getNode());
                    if (access$200 != null) {
                        discoverInfo2.addFeatures(access$200.getNodeFeatures());
                        discoverInfo2.addIdentities(access$200.getNodeIdentities());
                        discoverInfo2.addExtensions(access$200.getNodePacketExtensions());
                    } else {
                        discoverInfo2.setType(Type.ERROR);
                        discoverInfo2.setError(new XMPPError(Condition.item_not_found));
                    }
                }
                ServiceDiscoveryManager.this.connection.sendPacket(discoverInfo2);
            }
        }
    }

    static {
        identities = new LinkedList();
        instances = new ConcurrentHashMap();
        Connection.addConnectionCreationListener(new C11731());
        identities.add(new Identity(DEFAULT_IDENTITY_CATEGORY, DEFAULT_IDENTITY_NAME, DEFAULT_IDENTITY_TYPE));
    }

    public ServiceDiscoveryManager(Connection connection) {
        this.features = new HashSet();
        this.extendedInfo = null;
        this.nodeInformationProviders = new ConcurrentHashMap();
        this.connection = connection;
        init();
    }

    public static ServiceDiscoveryManager getInstanceFor(Connection connection) {
        return (ServiceDiscoveryManager) instances.get(connection);
    }

    public static String getIdentityName() {
        Identity identity = (Identity) identities.get(0);
        if (identity != null) {
            return identity.getName();
        }
        return null;
    }

    public static void setIdentityName(String str) {
        Identity identity = (Identity) identities.remove(0);
        identities.add(new Identity(DEFAULT_IDENTITY_CATEGORY, str, DEFAULT_IDENTITY_TYPE));
    }

    public static String getIdentityType() {
        Identity identity = (Identity) identities.get(0);
        if (identity != null) {
            return identity.getType();
        }
        return null;
    }

    public static void setIdentityType(String str) {
        Identity identity = (Identity) identities.get(0);
        if (identity != null) {
            identity.setType(str);
            return;
        }
        identities.add(new Identity(DEFAULT_IDENTITY_CATEGORY, DEFAULT_IDENTITY_NAME, str));
    }

    public static List<Identity> getIdentities() {
        return Collections.unmodifiableList(identities);
    }

    private void init() {
        instances.put(this.connection, this);
        addFeature(DiscoverInfo.NAMESPACE);
        addFeature(DiscoverItems.NAMESPACE);
        this.connection.addConnectionListener(new C11742());
        PacketFilter packetTypeFilter = new PacketTypeFilter(DiscoverItems.class);
        this.connection.addPacketListener(new C11753(), packetTypeFilter);
        packetTypeFilter = new PacketTypeFilter(DiscoverInfo.class);
        this.connection.addPacketListener(new C11764(), packetTypeFilter);
    }

    public void addDiscoverInfoTo(DiscoverInfo discoverInfo) {
        discoverInfo.addIdentities(identities);
        synchronized (this.features) {
            Iterator features = getFeatures();
            while (features.hasNext()) {
                discoverInfo.addFeature((String) features.next());
            }
            discoverInfo.addExtension(this.extendedInfo);
        }
    }

    private NodeInformationProvider getNodeInformationProvider(String str) {
        if (str == null) {
            return null;
        }
        return (NodeInformationProvider) this.nodeInformationProviders.get(str);
    }

    public void setNodeInformationProvider(String str, NodeInformationProvider nodeInformationProvider) {
        this.nodeInformationProviders.put(str, nodeInformationProvider);
    }

    public void removeNodeInformationProvider(String str) {
        this.nodeInformationProviders.remove(str);
    }

    public Iterator<String> getFeatures() {
        Iterator<String> it;
        synchronized (this.features) {
            it = Collections.unmodifiableList(new ArrayList(this.features)).iterator();
        }
        return it;
    }

    public List<String> getFeaturesList() {
        List linkedList;
        synchronized (this.features) {
            linkedList = new LinkedList(this.features);
        }
        return linkedList;
    }

    public void addFeature(String str) {
        synchronized (this.features) {
            this.features.add(str);
            renewEntityCapsVersion();
        }
    }

    public void removeFeature(String str) {
        synchronized (this.features) {
            this.features.remove(str);
            renewEntityCapsVersion();
        }
    }

    public boolean includesFeature(String str) {
        boolean contains;
        synchronized (this.features) {
            contains = this.features.contains(str);
        }
        return contains;
    }

    public void setExtendedInfo(DataForm dataForm) {
        this.extendedInfo = dataForm;
        renewEntityCapsVersion();
    }

    public DataForm getExtendedInfo() {
        return this.extendedInfo;
    }

    public List<PacketExtension> getExtendedInfoAsList() {
        if (this.extendedInfo == null) {
            return null;
        }
        List<PacketExtension> arrayList = new ArrayList(1);
        arrayList.add(this.extendedInfo);
        return arrayList;
    }

    public void removeExtendedInfo() {
        this.extendedInfo = null;
        renewEntityCapsVersion();
    }

    public DiscoverInfo discoverInfo(String str) throws XMPPException {
        String str2 = null;
        if (str == null) {
            return discoverInfo(null, null);
        }
        DiscoverInfo discoverInfoByUser = EntityCapsManager.getDiscoverInfoByUser(str);
        if (discoverInfoByUser != null) {
            return discoverInfoByUser;
        }
        NodeVerHash nodeVerHashByJid = EntityCapsManager.getNodeVerHashByJid(str);
        if (nodeVerHashByJid != null) {
            str2 = nodeVerHashByJid.getNodeVer();
        }
        DiscoverInfo discoverInfo = discoverInfo(str, str2);
        if (nodeVerHashByJid == null || !EntityCapsManager.verifyDiscvoerInfoVersion(nodeVerHashByJid.getVer(), nodeVerHashByJid.getHash(), discoverInfo)) {
            return discoverInfo;
        }
        EntityCapsManager.addDiscoverInfoByNode(nodeVerHashByJid.getNodeVer(), discoverInfo);
        return discoverInfo;
    }

    public DiscoverInfo discoverInfo(String str, String str2) throws XMPPException {
        Packet discoverInfo = new DiscoverInfo();
        discoverInfo.setType(Type.GET);
        discoverInfo.setTo(str);
        discoverInfo.setNode(str2);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(discoverInfo.getPacketID()));
        this.connection.sendPacket(discoverInfo);
        IQ iq = (IQ) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (iq == null) {
            throw new XMPPException("No response from the server.");
        } else if (iq.getType() != Type.ERROR) {
            return (DiscoverInfo) iq;
        } else {
            throw new XMPPException(iq.getError());
        }
    }

    public DiscoverItems discoverItems(String str) throws XMPPException {
        return discoverItems(str, null);
    }

    public DiscoverItems discoverItems(String str, String str2) throws XMPPException {
        Packet discoverItems = new DiscoverItems();
        discoverItems.setType(Type.GET);
        discoverItems.setTo(str);
        discoverItems.setNode(str2);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(discoverItems.getPacketID()));
        this.connection.sendPacket(discoverItems);
        IQ iq = (IQ) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (iq == null) {
            throw new XMPPException("No response from the server.");
        } else if (iq.getType() != Type.ERROR) {
            return (DiscoverItems) iq;
        } else {
            throw new XMPPException(iq.getError());
        }
    }

    public boolean canPublishItems(String str) throws XMPPException {
        return canPublishItems(discoverInfo(str));
    }

    public static boolean canPublishItems(DiscoverInfo discoverInfo) {
        return discoverInfo.containsFeature("http://jabber.org/protocol/disco#publish");
    }

    public void publishItems(String str, DiscoverItems discoverItems) throws XMPPException {
        publishItems(str, null, discoverItems);
    }

    public void publishItems(String str, String str2, DiscoverItems discoverItems) throws XMPPException {
        discoverItems.setType(Type.SET);
        discoverItems.setTo(str);
        discoverItems.setNode(str2);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(discoverItems.getPacketID()));
        this.connection.sendPacket(discoverItems);
        IQ iq = (IQ) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (iq == null) {
            throw new XMPPException("No response from the server.");
        } else if (iq.getType() == Type.ERROR) {
            throw new XMPPException(iq.getError());
        }
    }

    public void setEntityCapsManager(EntityCapsManager entityCapsManager) {
        this.capsManager = entityCapsManager;
    }

    private void renewEntityCapsVersion() {
        if (this.capsManager != null && this.capsManager.entityCapsEnabled()) {
            this.capsManager.updateLocalEntityCaps();
        }
    }
}

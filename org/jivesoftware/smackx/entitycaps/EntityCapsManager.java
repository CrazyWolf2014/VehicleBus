package org.jivesoftware.smackx.entitycaps;

import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.NotFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.Base64;
import org.jivesoftware.smack.util.Cache;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.NodeInformationProvider;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.entitycaps.cache.EntityCapsPersistentCache;
import org.jivesoftware.smackx.entitycaps.packet.CapsExtension;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverInfo.Feature;
import org.jivesoftware.smackx.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;
import org.xmlpull.v1.XmlPullParser;

public class EntityCapsManager {
    public static final String ELEMENT = "c";
    private static final String ENTITY_NODE = "http://www.igniterealtime.org/projects/smack";
    public static final String NAMESPACE = "http://jabber.org/protocol/caps";
    private static final Map<String, MessageDigest> SUPPORTED_HASHES;
    protected static Map<String, DiscoverInfo> caps;
    private static Map<Connection, EntityCapsManager> instances;
    protected static Map<String, NodeVerHash> jidCaps;
    protected static EntityCapsPersistentCache persistentCache;
    private String currentCapsVersion;
    private boolean entityCapsEnabled;
    private Queue<String> lastLocalCapsVersions;
    private boolean presenceSend;
    private ServiceDiscoveryManager sdm;
    private WeakReference<Connection> weakRefConnection;

    /* renamed from: org.jivesoftware.smackx.entitycaps.EntityCapsManager.8 */
    static class C09768 implements Comparator<FormField> {
        C09768() {
        }

        public int compare(FormField formField, FormField formField2) {
            return formField.getVariable().compareTo(formField2.getVariable());
        }
    }

    public static class NodeVerHash {
        private String hash;
        private String node;
        private String nodeVer;
        private String ver;

        NodeVerHash(String str, String str2, String str3) {
            this.node = str;
            this.ver = str2;
            this.hash = str3;
            this.nodeVer = str + "#" + str2;
        }

        public String getNodeVer() {
            return this.nodeVer;
        }

        public String getNode() {
            return this.node;
        }

        public String getHash() {
            return this.hash;
        }

        public String getVer() {
            return this.ver;
        }
    }

    /* renamed from: org.jivesoftware.smackx.entitycaps.EntityCapsManager.1 */
    static class C11901 implements ConnectionCreationListener {
        C11901() {
        }

        public void connectionCreated(Connection connection) {
            if (connection instanceof XMPPConnection) {
                EntityCapsManager entityCapsManager = new EntityCapsManager(null);
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.entitycaps.EntityCapsManager.2 */
    class C11912 implements ConnectionListener {
        C11912() {
        }

        public void connectionClosed() {
            EntityCapsManager.this.presenceSend = false;
            EntityCapsManager.instances.remove(EntityCapsManager.this.weakRefConnection.get());
        }

        public void connectionClosedOnError(Exception exception) {
            EntityCapsManager.this.presenceSend = false;
        }

        public void reconnectionFailed(Exception exception) {
        }

        public void reconnectingIn(int i) {
        }

        public void reconnectionSuccessful() {
        }
    }

    /* renamed from: org.jivesoftware.smackx.entitycaps.EntityCapsManager.3 */
    class C11923 implements PacketListener {
        C11923() {
        }

        public void processPacket(Packet packet) {
            if (EntityCapsManager.this.entityCapsEnabled()) {
                CapsExtension capsExtension = (CapsExtension) packet.getExtension(EntityCapsManager.ELEMENT, EntityCapsManager.NAMESPACE);
                String toLowerCase = capsExtension.getHash().toLowerCase();
                if (EntityCapsManager.SUPPORTED_HASHES.containsKey(toLowerCase)) {
                    EntityCapsManager.jidCaps.put(packet.getFrom(), new NodeVerHash(capsExtension.getNode(), capsExtension.getVer(), toLowerCase));
                }
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.entitycaps.EntityCapsManager.4 */
    class C11934 implements PacketListener {
        C11934() {
        }

        public void processPacket(Packet packet) {
            EntityCapsManager.jidCaps.remove(packet.getFrom());
        }
    }

    /* renamed from: org.jivesoftware.smackx.entitycaps.EntityCapsManager.5 */
    class C11945 implements PacketListener {
        C11945() {
        }

        public void processPacket(Packet packet) {
            EntityCapsManager.this.presenceSend = true;
        }
    }

    /* renamed from: org.jivesoftware.smackx.entitycaps.EntityCapsManager.6 */
    class C11956 implements PacketInterceptor {
        C11956() {
        }

        public void interceptPacket(Packet packet) {
            if (EntityCapsManager.this.entityCapsEnabled) {
                packet.addExtension(new CapsExtension(EntityCapsManager.ENTITY_NODE, EntityCapsManager.this.getCapsVersion(), "sha-1"));
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.entitycaps.EntityCapsManager.7 */
    class C11967 implements NodeInformationProvider {
        List<String> features;
        List<Identity> identities;
        List<PacketExtension> packetExtensions;

        C11967() {
            this.features = EntityCapsManager.this.sdm.getFeaturesList();
            this.identities = new LinkedList(ServiceDiscoveryManager.getIdentities());
            this.packetExtensions = EntityCapsManager.this.sdm.getExtendedInfoAsList();
        }

        public List<Item> getNodeItems() {
            return null;
        }

        public List<String> getNodeFeatures() {
            return this.features;
        }

        public List<Identity> getNodeIdentities() {
            return this.identities;
        }

        public List<PacketExtension> getNodePacketExtensions() {
            return this.packetExtensions;
        }
    }

    static {
        SUPPORTED_HASHES = new HashMap();
        instances = Collections.synchronizedMap(new WeakHashMap());
        caps = new Cache(1000, -1);
        jidCaps = new Cache(XStream.PRIORITY_VERY_HIGH, -1);
        Connection.addConnectionCreationListener(new C11901());
        try {
            SUPPORTED_HASHES.put("sha-1", MessageDigest.getInstance("SHA-1"));
        } catch (NoSuchAlgorithmException e) {
        }
    }

    public static void addDiscoverInfoByNode(String str, DiscoverInfo discoverInfo) {
        caps.put(str, discoverInfo);
        if (persistentCache != null) {
            persistentCache.addDiscoverInfoByNodePersistent(str, discoverInfo);
        }
    }

    public static String getNodeVersionByJid(String str) {
        NodeVerHash nodeVerHash = (NodeVerHash) jidCaps.get(str);
        if (nodeVerHash != null) {
            return nodeVerHash.nodeVer;
        }
        return null;
    }

    public static NodeVerHash getNodeVerHashByJid(String str) {
        return (NodeVerHash) jidCaps.get(str);
    }

    public static DiscoverInfo getDiscoverInfoByUser(String str) {
        NodeVerHash nodeVerHash = (NodeVerHash) jidCaps.get(str);
        if (nodeVerHash == null) {
            return null;
        }
        return getDiscoveryInfoByNodeVer(nodeVerHash.nodeVer);
    }

    public static DiscoverInfo getDiscoveryInfoByNodeVer(String str) {
        DiscoverInfo discoverInfo = (DiscoverInfo) caps.get(str);
        if (discoverInfo != null) {
            return new DiscoverInfo(discoverInfo);
        }
        return discoverInfo;
    }

    public static void setPersistentCache(EntityCapsPersistentCache entityCapsPersistentCache) throws IOException {
        if (persistentCache != null) {
            throw new IllegalStateException("Entity Caps Persistent Cache was already set");
        }
        persistentCache = entityCapsPersistentCache;
        persistentCache.replay();
    }

    public static void setJidCapsMaxCacheSize(int i) {
        ((Cache) jidCaps).setMaxCacheSize(i);
    }

    public static void setCapsMaxCacheSize(int i) {
        ((Cache) caps).setMaxCacheSize(i);
    }

    private EntityCapsManager(Connection connection) {
        this.presenceSend = false;
        this.lastLocalCapsVersions = new ConcurrentLinkedQueue();
        this.weakRefConnection = new WeakReference(connection);
        this.sdm = ServiceDiscoveryManager.getInstanceFor(connection);
        init();
    }

    private void init() {
        Connection connection = (Connection) this.weakRefConnection.get();
        instances.put(connection, this);
        connection.addConnectionListener(new C11912());
        updateLocalEntityCaps();
        if (SmackConfiguration.autoEnableEntityCaps()) {
            enableEntityCaps();
        }
        connection.addPacketListener(new C11923(), new AndFilter(new PacketTypeFilter(Presence.class), new PacketExtensionFilter(ELEMENT, NAMESPACE)));
        connection.addPacketListener(new C11934(), new AndFilter(new PacketTypeFilter(Presence.class), new NotFilter(new PacketExtensionFilter(ELEMENT, NAMESPACE))));
        connection.addPacketSendingListener(new C11945(), new PacketTypeFilter(Presence.class));
        connection.addPacketInterceptor(new C11956(), new PacketTypeFilter(Presence.class));
        this.sdm.setEntityCapsManager(this);
    }

    public static synchronized EntityCapsManager getInstanceFor(Connection connection) {
        EntityCapsManager entityCapsManager = null;
        synchronized (EntityCapsManager.class) {
            if (connection instanceof XMPPConnection) {
                if (SUPPORTED_HASHES.size() > 0) {
                    entityCapsManager = (EntityCapsManager) instances.get(connection);
                    if (entityCapsManager == null) {
                        entityCapsManager = new EntityCapsManager(connection);
                    }
                }
            }
        }
        return entityCapsManager;
    }

    public void enableEntityCaps() {
        this.sdm.addFeature(NAMESPACE);
        updateLocalEntityCaps();
        this.entityCapsEnabled = true;
    }

    public void disableEntityCaps() {
        this.entityCapsEnabled = false;
        this.sdm.removeFeature(NAMESPACE);
    }

    public boolean entityCapsEnabled() {
        return this.entityCapsEnabled;
    }

    public void removeUserCapsNode(String str) {
        jidCaps.remove(str);
    }

    public String getCapsVersion() {
        return this.currentCapsVersion;
    }

    public String getLocalNodeVer() {
        return "http://www.igniterealtime.org/projects/smack#" + getCapsVersion();
    }

    public boolean areEntityCapsSupported(String str) {
        boolean z = false;
        if (str != null) {
            try {
                z = this.sdm.discoverInfo(str).containsFeature(NAMESPACE);
            } catch (XMPPException e) {
            }
        }
        return z;
    }

    public boolean areEntityCapsSupportedByServer() {
        return areEntityCapsSupported(((Connection) this.weakRefConnection.get()).getServiceName());
    }

    public void updateLocalEntityCaps() {
        Connection connection = (Connection) this.weakRefConnection.get();
        DiscoverInfo discoverInfo = new DiscoverInfo();
        discoverInfo.setType(Type.RESULT);
        discoverInfo.setNode(getLocalNodeVer());
        if (connection != null) {
            discoverInfo.setFrom(connection.getUser());
        }
        this.sdm.addDiscoverInfoTo(discoverInfo);
        this.currentCapsVersion = generateVerificationString(discoverInfo, "sha-1");
        addDiscoverInfoByNode("http://www.igniterealtime.org/projects/smack#" + this.currentCapsVersion, discoverInfo);
        if (this.lastLocalCapsVersions.size() > 10) {
            this.sdm.removeNodeInformationProvider("http://www.igniterealtime.org/projects/smack#" + ((String) this.lastLocalCapsVersions.poll()));
        }
        this.lastLocalCapsVersions.add(this.currentCapsVersion);
        caps.put(this.currentCapsVersion, discoverInfo);
        if (connection != null) {
            jidCaps.put(connection.getUser(), new NodeVerHash(ENTITY_NODE, this.currentCapsVersion, "sha-1"));
        }
        this.sdm.setNodeInformationProvider("http://www.igniterealtime.org/projects/smack#" + this.currentCapsVersion, new C11967());
        if (connection != null && connection.isAuthenticated() && this.presenceSend) {
            connection.sendPacket(new Presence(Presence.Type.available));
        }
    }

    public static boolean verifyDiscvoerInfoVersion(String str, String str2, DiscoverInfo discoverInfo) {
        if (discoverInfo.containsDuplicateIdentities() || discoverInfo.containsDuplicateFeatures() || verifyPacketExtensions(discoverInfo) || !str.equals(generateVerificationString(discoverInfo, str2))) {
            return false;
        }
        return true;
    }

    protected static boolean verifyPacketExtensions(DiscoverInfo discoverInfo) {
        List<FormField> linkedList = new LinkedList();
        for (PacketExtension packetExtension : discoverInfo.getExtensions()) {
            if (packetExtension.getNamespace().equals(Form.NAMESPACE)) {
                Iterator fields = ((DataForm) packetExtension).getFields();
                while (fields.hasNext()) {
                    FormField formField = (FormField) fields.next();
                    if (formField.getVariable().equals("FORM_TYPE")) {
                        for (FormField equals : linkedList) {
                            if (formField.equals(equals)) {
                                return true;
                            }
                        }
                        linkedList.add(formField);
                    }
                }
                continue;
            }
        }
        return false;
    }

    protected static String generateVerificationString(DiscoverInfo discoverInfo, String str) {
        MessageDigest messageDigest = (MessageDigest) SUPPORTED_HASHES.get(str.toLowerCase());
        if (messageDigest == null) {
            return null;
        }
        DataForm dataForm = (DataForm) discoverInfo.getExtension(GroupChatInvitation.ELEMENT_NAME, Form.NAMESPACE);
        StringBuilder stringBuilder = new StringBuilder();
        SortedSet<Identity> treeSet = new TreeSet();
        Iterator identities = discoverInfo.getIdentities();
        while (identities.hasNext()) {
            treeSet.add(identities.next());
        }
        for (Identity identity : treeSet) {
            stringBuilder.append(identity.getCategory());
            stringBuilder.append(FilePathGenerator.ANDROID_DIR_SEP);
            stringBuilder.append(identity.getType());
            stringBuilder.append(FilePathGenerator.ANDROID_DIR_SEP);
            stringBuilder.append(identity.getLanguage() == null ? XmlPullParser.NO_NAMESPACE : identity.getLanguage());
            stringBuilder.append(FilePathGenerator.ANDROID_DIR_SEP);
            stringBuilder.append(identity.getName() == null ? XmlPullParser.NO_NAMESPACE : identity.getName());
            stringBuilder.append("<");
        }
        SortedSet<String> treeSet2 = new TreeSet();
        Iterator features = discoverInfo.getFeatures();
        while (features.hasNext()) {
            treeSet2.add(((Feature) features.next()).getVar());
        }
        for (String append : treeSet2) {
            stringBuilder.append(append);
            stringBuilder.append("<");
        }
        if (dataForm != null && dataForm.hasHiddenFromTypeField()) {
            synchronized (dataForm) {
                FormField formField;
                SortedSet<FormField> treeSet3 = new TreeSet(new C09768());
                Iterator fields = dataForm.getFields();
                FormField formField2 = null;
                while (fields.hasNext()) {
                    formField = (FormField) fields.next();
                    if (!formField.getVariable().equals("FORM_TYPE")) {
                        treeSet3.add(formField);
                        formField = formField2;
                    }
                    formField2 = formField;
                }
                if (formField2 != null) {
                    formFieldValuesToCaps(formField2.getValues(), stringBuilder);
                }
                for (FormField formField3 : treeSet3) {
                    stringBuilder.append(formField3.getVariable());
                    stringBuilder.append("<");
                    formFieldValuesToCaps(formField3.getValues(), stringBuilder);
                }
            }
        }
        return Base64.encodeBytes(messageDigest.digest(stringBuilder.toString().getBytes()));
    }

    private static void formFieldValuesToCaps(Iterator<String> it, StringBuilder stringBuilder) {
        SortedSet<String> treeSet = new TreeSet();
        while (it.hasNext()) {
            treeSet.add(it.next());
        }
        for (String append : treeSet) {
            stringBuilder.append(append);
            stringBuilder.append("<");
        }
    }
}

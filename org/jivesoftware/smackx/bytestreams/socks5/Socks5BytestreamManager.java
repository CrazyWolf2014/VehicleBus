package org.jivesoftware.smackx.bytestreams.socks5;

import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.util.SyncPacketSend;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.bytestreams.BytestreamListener;
import org.jivesoftware.smackx.bytestreams.BytestreamManager;
import org.jivesoftware.smackx.bytestreams.socks5.packet.Bytestream;
import org.jivesoftware.smackx.bytestreams.socks5.packet.Bytestream.StreamHost;
import org.jivesoftware.smackx.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public final class Socks5BytestreamManager implements BytestreamManager {
    public static final String NAMESPACE = "http://jabber.org/protocol/bytestreams";
    private static final String SESSION_ID_PREFIX = "js5_";
    private static final Map<Connection, Socks5BytestreamManager> managers;
    private static final Random randomGenerator;
    private final List<BytestreamListener> allRequestListeners;
    private final Connection connection;
    private List<String> ignoredBytestreamRequests;
    private final InitiationListener initiationListener;
    private String lastWorkingProxy;
    private final List<String> proxyBlacklist;
    private int proxyConnectionTimeout;
    private boolean proxyPrioritizationEnabled;
    private int targetResponseTimeout;
    private final Map<String, BytestreamListener> userListeners;

    /* renamed from: org.jivesoftware.smackx.bytestreams.socks5.Socks5BytestreamManager.1 */
    static class C11811 implements ConnectionCreationListener {

        /* renamed from: org.jivesoftware.smackx.bytestreams.socks5.Socks5BytestreamManager.1.1 */
        class C12911 extends AbstractConnectionListener {
            final /* synthetic */ Connection val$connection;
            final /* synthetic */ Socks5BytestreamManager val$manager;

            C12911(Socks5BytestreamManager socks5BytestreamManager, Connection connection) {
                this.val$manager = socks5BytestreamManager;
                this.val$connection = connection;
            }

            public void connectionClosed() {
                this.val$manager.disableService();
            }

            public void connectionClosedOnError(Exception exception) {
                this.val$manager.disableService();
            }

            public void reconnectionSuccessful() {
                Socks5BytestreamManager.managers.put(this.val$connection, this.val$manager);
            }
        }

        C11811() {
        }

        public void connectionCreated(Connection connection) {
            connection.addConnectionListener(new C12911(Socks5BytestreamManager.getBytestreamManager(connection), connection));
        }
    }

    static {
        Connection.addConnectionCreationListener(new C11811());
        randomGenerator = new Random();
        managers = new WeakHashMap();
    }

    public static synchronized Socks5BytestreamManager getBytestreamManager(Connection connection) {
        Socks5BytestreamManager socks5BytestreamManager;
        synchronized (Socks5BytestreamManager.class) {
            if (connection == null) {
                socks5BytestreamManager = null;
            } else {
                socks5BytestreamManager = (Socks5BytestreamManager) managers.get(connection);
                if (socks5BytestreamManager == null) {
                    socks5BytestreamManager = new Socks5BytestreamManager(connection);
                    managers.put(connection, socks5BytestreamManager);
                    socks5BytestreamManager.activate();
                }
            }
        }
        return socks5BytestreamManager;
    }

    private Socks5BytestreamManager(Connection connection) {
        this.userListeners = new ConcurrentHashMap();
        this.allRequestListeners = Collections.synchronizedList(new LinkedList());
        this.targetResponseTimeout = XStream.PRIORITY_VERY_HIGH;
        this.proxyConnectionTimeout = XStream.PRIORITY_VERY_HIGH;
        this.proxyBlacklist = Collections.synchronizedList(new LinkedList());
        this.lastWorkingProxy = null;
        this.proxyPrioritizationEnabled = true;
        this.ignoredBytestreamRequests = Collections.synchronizedList(new LinkedList());
        this.connection = connection;
        this.initiationListener = new InitiationListener(this);
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

    public synchronized void disableService() {
        this.connection.removePacketListener(this.initiationListener);
        this.initiationListener.shutdown();
        this.allRequestListeners.clear();
        this.userListeners.clear();
        this.lastWorkingProxy = null;
        this.proxyBlacklist.clear();
        this.ignoredBytestreamRequests.clear();
        managers.remove(this.connection);
        if (managers.size() == 0) {
            Socks5Proxy.getSocks5Proxy().stop();
        }
        ServiceDiscoveryManager instanceFor = ServiceDiscoveryManager.getInstanceFor(this.connection);
        if (instanceFor != null) {
            instanceFor.removeFeature(NAMESPACE);
        }
    }

    public int getTargetResponseTimeout() {
        if (this.targetResponseTimeout <= 0) {
            this.targetResponseTimeout = XStream.PRIORITY_VERY_HIGH;
        }
        return this.targetResponseTimeout;
    }

    public void setTargetResponseTimeout(int i) {
        this.targetResponseTimeout = i;
    }

    public int getProxyConnectionTimeout() {
        if (this.proxyConnectionTimeout <= 0) {
            this.proxyConnectionTimeout = XStream.PRIORITY_VERY_HIGH;
        }
        return this.proxyConnectionTimeout;
    }

    public void setProxyConnectionTimeout(int i) {
        this.proxyConnectionTimeout = i;
    }

    public boolean isProxyPrioritizationEnabled() {
        return this.proxyPrioritizationEnabled;
    }

    public void setProxyPrioritizationEnabled(boolean z) {
        this.proxyPrioritizationEnabled = z;
    }

    public Socks5BytestreamSession establishSession(String str) throws XMPPException, IOException, InterruptedException {
        return establishSession(str, getNextSessionID());
    }

    public Socks5BytestreamSession establishSession(String str, String str2) throws XMPPException, IOException, InterruptedException {
        if (supportsSocks5(str)) {
            XMPPException xMPPException;
            List arrayList = new ArrayList();
            try {
                arrayList.addAll(determineProxies());
                xMPPException = null;
            } catch (XMPPException e) {
                xMPPException = e;
            }
            List<Object> determineStreamHostInfos = determineStreamHostInfos(arrayList);
            if (determineStreamHostInfos.isEmpty()) {
                if (xMPPException == null) {
                    xMPPException = new XMPPException("no SOCKS5 proxies available");
                }
                throw xMPPException;
            }
            String createDigest = Socks5Utils.createDigest(str2, this.connection.getUser(), str);
            if (this.proxyPrioritizationEnabled && this.lastWorkingProxy != null) {
                for (Object obj : determineStreamHostInfos) {
                    if (obj.getJID().equals(this.lastWorkingProxy)) {
                        break;
                    }
                }
                Object obj2 = null;
                if (obj2 != null) {
                    determineStreamHostInfos.remove(obj2);
                    determineStreamHostInfos.add(0, obj2);
                }
            }
            Socks5Proxy socks5Proxy = Socks5Proxy.getSocks5Proxy();
            try {
                socks5Proxy.addTransfer(createDigest);
                Packet createBytestreamInitiation = createBytestreamInitiation(str2, str, determineStreamHostInfos);
                StreamHost streamHost = createBytestreamInitiation.getStreamHost(((Bytestream) SyncPacketSend.getReply(this.connection, createBytestreamInitiation, (long) getTargetResponseTimeout())).getUsedHost().getJID());
                if (streamHost == null) {
                    throw new XMPPException("Remote user responded with unknown host");
                }
                Socket socket = new Socks5ClientForInitiator(streamHost, createDigest, this.connection, str2, str).getSocket(getProxyConnectionTimeout());
                this.lastWorkingProxy = streamHost.getJID();
                Socks5BytestreamSession socks5BytestreamSession = new Socks5BytestreamSession(socket, streamHost.getJID().equals(this.connection.getUser()));
                socks5Proxy.removeTransfer(createDigest);
                return socks5BytestreamSession;
            } catch (TimeoutException e2) {
                throw new IOException("Timeout while connecting to SOCKS5 proxy");
            } catch (Throwable th) {
                socks5Proxy.removeTransfer(createDigest);
            }
        } else {
            throw new XMPPException(str + " doesn't support SOCKS5 Bytestream");
        }
    }

    private boolean supportsSocks5(String str) throws XMPPException {
        return ServiceDiscoveryManager.getInstanceFor(this.connection).discoverInfo(str).containsFeature(NAMESPACE);
    }

    private List<String> determineProxies() throws XMPPException {
        ServiceDiscoveryManager instanceFor = ServiceDiscoveryManager.getInstanceFor(this.connection);
        List<String> arrayList = new ArrayList();
        Iterator items = instanceFor.discoverItems(this.connection.getServiceName()).getItems();
        while (items.hasNext()) {
            Item item = (Item) items.next();
            if (!this.proxyBlacklist.contains(item.getEntityID())) {
                try {
                    Iterator identities = instanceFor.discoverInfo(item.getEntityID()).getIdentities();
                    while (identities.hasNext()) {
                        Identity identity = (Identity) identities.next();
                        if ("proxy".equalsIgnoreCase(identity.getCategory()) && "bytestreams".equalsIgnoreCase(identity.getType())) {
                            arrayList.add(item.getEntityID());
                            break;
                        }
                        this.proxyBlacklist.add(item.getEntityID());
                    }
                } catch (XMPPException e) {
                    this.proxyBlacklist.add(item.getEntityID());
                }
            }
        }
        return arrayList;
    }

    private List<StreamHost> determineStreamHostInfos(List<String> list) {
        List<StreamHost> arrayList = new ArrayList();
        Collection localStreamHost = getLocalStreamHost();
        if (localStreamHost != null) {
            arrayList.addAll(localStreamHost);
        }
        for (String str : list) {
            try {
                arrayList.addAll(((Bytestream) SyncPacketSend.getReply(this.connection, createStreamHostRequest(str))).getStreamHosts());
            } catch (XMPPException e) {
                this.proxyBlacklist.add(str);
            }
        }
        return arrayList;
    }

    private Bytestream createStreamHostRequest(String str) {
        Bytestream bytestream = new Bytestream();
        bytestream.setType(Type.GET);
        bytestream.setTo(str);
        return bytestream;
    }

    private List<StreamHost> getLocalStreamHost() {
        Socks5Proxy socks5Proxy = Socks5Proxy.getSocks5Proxy();
        if (socks5Proxy.isRunning()) {
            List<String> localAddresses = socks5Proxy.getLocalAddresses();
            int port = socks5Proxy.getPort();
            if (localAddresses.size() >= 1) {
                List<StreamHost> arrayList = new ArrayList();
                for (String streamHost : localAddresses) {
                    StreamHost streamHost2 = new StreamHost(this.connection.getUser(), streamHost);
                    streamHost2.setPort(port);
                    arrayList.add(streamHost2);
                }
                return arrayList;
            }
        }
        return null;
    }

    private Bytestream createBytestreamInitiation(String str, String str2, List<StreamHost> list) {
        Bytestream bytestream = new Bytestream(str);
        for (StreamHost addStreamHost : list) {
            bytestream.addStreamHost(addStreamHost);
        }
        bytestream.setType(Type.SET);
        bytestream.setTo(str2);
        return bytestream;
    }

    protected void replyRejectPacket(IQ iq) {
        this.connection.sendPacket(IQ.createErrorResponse(iq, new XMPPError(Condition.no_acceptable)));
    }

    private void activate() {
        this.connection.addPacketListener(this.initiationListener, this.initiationListener.getFilter());
        enableService();
    }

    private void enableService() {
        ServiceDiscoveryManager instanceFor = ServiceDiscoveryManager.getInstanceFor(this.connection);
        if (!instanceFor.includesFeature(NAMESPACE)) {
            instanceFor.addFeature(NAMESPACE);
        }
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

    protected List<String> getIgnoredBytestreamRequests() {
        return this.ignoredBytestreamRequests;
    }
}

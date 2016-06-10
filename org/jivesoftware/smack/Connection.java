package org.jivesoftware.smack;

import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import org.jivesoftware.smack.debugger.SmackDebugger;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

public abstract class Connection {
    public static boolean DEBUG_ENABLED;
    private static final AtomicInteger connectionCounter;
    private static final Set<ConnectionCreationListener> connectionEstablishedListeners;
    private AccountManager accountManager;
    protected ChatManager chatManager;
    protected final Collection<PacketCollector> collectors;
    protected final ConnectionConfiguration config;
    protected final int connectionCounterValue;
    protected final Collection<ConnectionListener> connectionListeners;
    protected SmackDebugger debugger;
    protected final Map<PacketInterceptor, InterceptorWrapper> interceptors;
    protected Reader reader;
    protected final Map<PacketListener, ListenerWrapper> recvListeners;
    protected RosterStorage rosterStorage;
    protected SASLAuthentication saslAuthentication;
    protected final Map<PacketListener, ListenerWrapper> sendListeners;
    private String serviceCapsNode;
    protected Writer writer;

    protected static class InterceptorWrapper {
        private PacketFilter packetFilter;
        private PacketInterceptor packetInterceptor;

        public InterceptorWrapper(PacketInterceptor packetInterceptor, PacketFilter packetFilter) {
            this.packetInterceptor = packetInterceptor;
            this.packetFilter = packetFilter;
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj instanceof InterceptorWrapper) {
                return ((InterceptorWrapper) obj).packetInterceptor.equals(this.packetInterceptor);
            }
            if (obj instanceof PacketInterceptor) {
                return obj.equals(this.packetInterceptor);
            }
            return false;
        }

        public void notifyListener(Packet packet) {
            if (this.packetFilter == null || this.packetFilter.accept(packet)) {
                this.packetInterceptor.interceptPacket(packet);
            }
        }
    }

    protected static class ListenerWrapper {
        private PacketFilter packetFilter;
        private PacketListener packetListener;

        public ListenerWrapper(PacketListener packetListener, PacketFilter packetFilter) {
            this.packetListener = packetListener;
            this.packetFilter = packetFilter;
        }

        public void notifyListener(Packet packet) {
            if (this.packetFilter == null || this.packetFilter.accept(packet)) {
                this.packetListener.processPacket(packet);
            }
        }
    }

    public abstract void connect() throws XMPPException;

    public abstract void disconnect(Presence presence);

    public abstract String getConnectionID();

    public abstract Roster getRoster();

    public abstract String getUser();

    public abstract boolean isAnonymous();

    public abstract boolean isAuthenticated();

    public abstract boolean isConnected();

    public abstract boolean isSecureConnection();

    public abstract boolean isUsingCompression();

    public abstract void login(String str, String str2, String str3) throws XMPPException;

    public abstract void loginAnonymously() throws XMPPException;

    public abstract void sendPacket(Packet packet);

    public abstract void setRosterStorage(RosterStorage rosterStorage) throws IllegalStateException;

    static {
        connectionCounter = new AtomicInteger(0);
        connectionEstablishedListeners = new CopyOnWriteArraySet();
        DEBUG_ENABLED = false;
        try {
            DEBUG_ENABLED = Boolean.getBoolean("smack.debugEnabled");
        } catch (Exception e) {
        }
        SmackConfiguration.getVersion();
    }

    protected Connection(ConnectionConfiguration connectionConfiguration) {
        this.connectionListeners = new CopyOnWriteArrayList();
        this.collectors = new ConcurrentLinkedQueue();
        this.recvListeners = new ConcurrentHashMap();
        this.sendListeners = new ConcurrentHashMap();
        this.interceptors = new ConcurrentHashMap();
        this.accountManager = null;
        this.chatManager = null;
        this.debugger = null;
        this.saslAuthentication = new SASLAuthentication(this);
        this.connectionCounterValue = connectionCounter.getAndIncrement();
        this.config = connectionConfiguration;
    }

    protected ConnectionConfiguration getConfiguration() {
        return this.config;
    }

    public String getServiceName() {
        return this.config.getServiceName();
    }

    public String getHost() {
        return this.config.getHost();
    }

    public int getPort() {
        return this.config.getPort();
    }

    protected boolean isReconnectionAllowed() {
        return this.config.isReconnectionAllowed();
    }

    public void login(String str, String str2) throws XMPPException {
        login(str, str2, "Smack");
    }

    public AccountManager getAccountManager() {
        if (this.accountManager == null) {
            this.accountManager = new AccountManager(this);
        }
        return this.accountManager;
    }

    public synchronized ChatManager getChatManager() {
        if (this.chatManager == null) {
            this.chatManager = new ChatManager(this);
        }
        return this.chatManager;
    }

    public SASLAuthentication getSASLAuthentication() {
        return this.saslAuthentication;
    }

    public void disconnect() {
        disconnect(new Presence(Type.unavailable));
    }

    public static void addConnectionCreationListener(ConnectionCreationListener connectionCreationListener) {
        connectionEstablishedListeners.add(connectionCreationListener);
    }

    public static void removeConnectionCreationListener(ConnectionCreationListener connectionCreationListener) {
        connectionEstablishedListeners.remove(connectionCreationListener);
    }

    protected static Collection<ConnectionCreationListener> getConnectionCreationListeners() {
        return Collections.unmodifiableCollection(connectionEstablishedListeners);
    }

    public void addConnectionListener(ConnectionListener connectionListener) {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected to server.");
        } else if (connectionListener != null && !this.connectionListeners.contains(connectionListener)) {
            this.connectionListeners.add(connectionListener);
        }
    }

    public void removeConnectionListener(ConnectionListener connectionListener) {
        this.connectionListeners.remove(connectionListener);
    }

    protected Collection<ConnectionListener> getConnectionListeners() {
        return this.connectionListeners;
    }

    public PacketCollector createPacketCollector(PacketFilter packetFilter) {
        PacketCollector packetCollector = new PacketCollector(this, packetFilter);
        this.collectors.add(packetCollector);
        return packetCollector;
    }

    protected void removePacketCollector(PacketCollector packetCollector) {
        this.collectors.remove(packetCollector);
    }

    protected Collection<PacketCollector> getPacketCollectors() {
        return this.collectors;
    }

    public void addPacketListener(PacketListener packetListener, PacketFilter packetFilter) {
        if (packetListener == null) {
            throw new NullPointerException("Packet listener is null.");
        }
        this.recvListeners.put(packetListener, new ListenerWrapper(packetListener, packetFilter));
    }

    public void removePacketListener(PacketListener packetListener) {
        this.recvListeners.remove(packetListener);
    }

    protected Map<PacketListener, ListenerWrapper> getPacketListeners() {
        return this.recvListeners;
    }

    public void addPacketSendingListener(PacketListener packetListener, PacketFilter packetFilter) {
        if (packetListener == null) {
            throw new NullPointerException("Packet listener is null.");
        }
        this.sendListeners.put(packetListener, new ListenerWrapper(packetListener, packetFilter));
    }

    public void removePacketSendingListener(PacketListener packetListener) {
        this.sendListeners.remove(packetListener);
    }

    protected Map<PacketListener, ListenerWrapper> getPacketSendingListeners() {
        return this.sendListeners;
    }

    protected void firePacketSendingListeners(Packet packet) {
        for (ListenerWrapper notifyListener : this.sendListeners.values()) {
            notifyListener.notifyListener(packet);
        }
    }

    public void addPacketInterceptor(PacketInterceptor packetInterceptor, PacketFilter packetFilter) {
        if (packetInterceptor == null) {
            throw new NullPointerException("Packet interceptor is null.");
        }
        this.interceptors.put(packetInterceptor, new InterceptorWrapper(packetInterceptor, packetFilter));
    }

    public void removePacketInterceptor(PacketInterceptor packetInterceptor) {
        this.interceptors.remove(packetInterceptor);
    }

    public boolean isSendPresence() {
        return this.config.isSendPresence();
    }

    protected Map<PacketInterceptor, InterceptorWrapper> getPacketInterceptors() {
        return this.interceptors;
    }

    protected void firePacketInterceptors(Packet packet) {
        if (packet != null) {
            for (InterceptorWrapper notifyListener : this.interceptors.values()) {
                notifyListener.notifyListener(packet);
            }
        }
    }

    protected void initDebugger() {
        Class cls = null;
        if (this.reader == null || this.writer == null) {
            throw new NullPointerException("Reader or writer isn't initialized.");
        } else if (!this.config.isDebuggerEnabled()) {
        } else {
            if (this.debugger == null) {
                String property;
                Class cls2;
                try {
                    property = System.getProperty("smack.debuggerClass");
                } catch (Throwable th) {
                    Object obj = cls;
                }
                if (property != null) {
                    try {
                        cls = Class.forName(property);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (cls == null) {
                    try {
                        cls2 = Class.forName("de.measite.smack.AndroidDebugger");
                    } catch (Exception e2) {
                        try {
                            cls2 = Class.forName("org.jivesoftware.smack.debugger.ConsoleDebugger");
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                    }
                    this.debugger = (SmackDebugger) cls2.getConstructor(new Class[]{Connection.class, Writer.class, Reader.class}).newInstance(new Object[]{this, this.writer, this.reader});
                    this.reader = this.debugger.getReader();
                    this.writer = this.debugger.getWriter();
                    return;
                }
                cls2 = cls;
                try {
                    this.debugger = (SmackDebugger) cls2.getConstructor(new Class[]{Connection.class, Writer.class, Reader.class}).newInstance(new Object[]{this, this.writer, this.reader});
                    this.reader = this.debugger.getReader();
                    this.writer = this.debugger.getWriter();
                    return;
                } catch (Throwable e4) {
                    throw new IllegalArgumentException("Can't initialize the configured debugger!", e4);
                }
            }
            this.reader = this.debugger.newConnectionReader(this.reader);
            this.writer = this.debugger.newConnectionWriter(this.writer);
        }
    }

    protected void setServiceCapsNode(String str) {
        this.serviceCapsNode = str;
    }

    public String getServiceCapsNode() {
        return this.serviceCapsNode;
    }
}

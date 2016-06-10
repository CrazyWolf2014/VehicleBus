package org.jivesoftware.smackx.ping;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.ping.packet.Ping;
import org.jivesoftware.smackx.ping.packet.Pong;

public class PingManager {
    public static final String ELEMENT = "ping";
    public static final String NAMESPACE = "urn:xmpp:ping";
    private static Map<Connection, PingManager> instances;
    private Connection connection;
    private long lastPingStamp;
    private long lastServerPingStamp;
    private Set<PingFailedListener> pingFailedListeners;
    private int pingInterval;
    private long pingMinDelta;
    private ServerPingTask serverPingTask;
    private Thread serverPingThread;

    /* renamed from: org.jivesoftware.smackx.ping.PingManager.1 */
    static class C12151 implements ConnectionCreationListener {
        C12151() {
        }

        public void connectionCreated(Connection connection) {
            PingManager pingManager = new PingManager(null);
        }
    }

    private class PingPacketListener implements PacketListener {
        public void processPacket(Packet packet) {
            if (PingManager.this.pingMinDelta > 0) {
                long currentTimeMillis = System.currentTimeMillis();
                long access$500 = currentTimeMillis - PingManager.this.lastPingStamp;
                PingManager.this.lastPingStamp = currentTimeMillis;
                if (access$500 < PingManager.this.pingMinDelta) {
                    return;
                }
            }
            PingManager.this.connection.sendPacket(new Pong((Ping) packet));
        }
    }

    private class PingConnectionListener extends AbstractConnectionListener {
        private PingConnectionListener() {
        }

        public void connectionClosed() {
            PingManager.this.maybeStopPingServerTask();
        }

        public void connectionClosedOnError(Exception exception) {
            PingManager.this.maybeStopPingServerTask();
        }

        public void reconnectionSuccessful() {
            PingManager.this.maybeStartPingServerTask();
        }
    }

    static {
        instances = Collections.synchronizedMap(new WeakHashMap());
        Connection.addConnectionCreationListener(new C12151());
    }

    private PingManager(Connection connection) {
        this.pingInterval = SmackConfiguration.getDefaultPingInterval();
        this.pingFailedListeners = Collections.synchronizedSet(new HashSet());
        this.pingMinDelta = 100;
        this.lastPingStamp = 0;
        this.lastServerPingStamp = -1;
        ServiceDiscoveryManager.getInstanceFor(connection).addFeature(NAMESPACE);
        this.connection = connection;
        connection.addPacketListener(new PingPacketListener(), new PacketTypeFilter(Ping.class));
        connection.addConnectionListener(new PingConnectionListener());
        instances.put(connection, this);
        maybeStartPingServerTask();
    }

    public static PingManager getInstanceFor(Connection connection) {
        PingManager pingManager = (PingManager) instances.get(connection);
        if (pingManager == null) {
            return new PingManager(connection);
        }
        return pingManager;
    }

    public void setPingIntervall(int i) {
        this.pingInterval = i;
        if (this.serverPingTask != null) {
            this.serverPingTask.setPingInterval(i);
        }
    }

    public int getPingIntervall() {
        return this.pingInterval;
    }

    public void registerPingFailedListener(PingFailedListener pingFailedListener) {
        this.pingFailedListeners.add(pingFailedListener);
    }

    public void unregisterPingFailedListener(PingFailedListener pingFailedListener) {
        this.pingFailedListeners.remove(pingFailedListener);
    }

    public void disablePingFloodProtection() {
        setPingMinimumInterval(-1);
    }

    public void setPingMinimumInterval(long j) {
        this.pingMinDelta = j;
    }

    public long getPingMinimumInterval() {
        return this.pingMinDelta;
    }

    public IQ ping(String str, long j) {
        if (!this.connection.isAuthenticated()) {
            return null;
        }
        Packet ping = new Ping(this.connection.getUser(), str);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(ping.getPacketID()));
        this.connection.sendPacket(ping);
        IQ iq = (IQ) createPacketCollector.nextResult(j);
        createPacketCollector.cancel();
        return iq;
    }

    public IQ ping(String str) {
        return ping(str, (long) SmackConfiguration.getPacketReplyTimeout());
    }

    public boolean pingEntity(String str, long j) {
        IQ ping = ping(str, j);
        if (ping == null || ping.getType() == Type.ERROR) {
            return false;
        }
        return true;
    }

    public boolean pingEntity(String str) {
        return pingEntity(str, (long) SmackConfiguration.getPacketReplyTimeout());
    }

    public boolean pingMyServer(long j) {
        if (ping(this.connection.getServiceName(), j) == null) {
            for (PingFailedListener pingFailed : this.pingFailedListeners) {
                pingFailed.pingFailed();
            }
            return false;
        }
        this.lastServerPingStamp = System.currentTimeMillis();
        return true;
    }

    public boolean pingMyServer() {
        return pingMyServer((long) SmackConfiguration.getPacketReplyTimeout());
    }

    public boolean isPingSupported(String str) {
        try {
            return ServiceDiscoveryManager.getInstanceFor(this.connection).discoverInfo(str).containsFeature(NAMESPACE);
        } catch (XMPPException e) {
            return false;
        }
    }

    public long getLastSuccessfulPing() {
        long j = -1;
        if (this.serverPingTask != null) {
            j = this.serverPingTask.getLastSucessfulPing();
        }
        return Math.max(j, this.lastServerPingStamp);
    }

    protected Set<PingFailedListener> getPingFailedListeners() {
        return this.pingFailedListeners;
    }

    private void maybeStartPingServerTask() {
        if (this.serverPingTask != null) {
            this.serverPingTask.setDone();
            this.serverPingThread.interrupt();
            this.serverPingTask = null;
            this.serverPingThread = null;
        }
        if (this.pingInterval > 0) {
            this.serverPingTask = new ServerPingTask(this.connection, this.pingInterval);
            this.serverPingThread = new Thread(this.serverPingTask);
            this.serverPingThread.setDaemon(true);
            this.serverPingThread.setName("Smack Ping Server Task (" + this.connection.getServiceName() + ")");
            this.serverPingThread.start();
        }
    }

    private void maybeStopPingServerTask() {
        if (this.serverPingThread != null) {
            this.serverPingTask.setDone();
            this.serverPingThread.interrupt();
        }
    }
}

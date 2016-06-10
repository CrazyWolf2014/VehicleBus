package org.jivesoftware.smackx.bytestreams.socks5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.bytestreams.BytestreamListener;
import org.jivesoftware.smackx.bytestreams.BytestreamRequest;
import org.jivesoftware.smackx.bytestreams.socks5.packet.Bytestream;

final class InitiationListener implements PacketListener {
    private final PacketFilter initFilter;
    private final ExecutorService initiationListenerExecutor;
    private final Socks5BytestreamManager manager;

    /* renamed from: org.jivesoftware.smackx.bytestreams.socks5.InitiationListener.1 */
    class C09721 implements Runnable {
        final /* synthetic */ Packet val$packet;

        C09721(Packet packet) {
            this.val$packet = packet;
        }

        public void run() {
            InitiationListener.this.processRequest(this.val$packet);
        }
    }

    protected InitiationListener(Socks5BytestreamManager socks5BytestreamManager) {
        this.initFilter = new AndFilter(new PacketTypeFilter(Bytestream.class), new IQTypeFilter(Type.SET));
        this.manager = socks5BytestreamManager;
        this.initiationListenerExecutor = Executors.newCachedThreadPool();
    }

    public void processPacket(Packet packet) {
        this.initiationListenerExecutor.execute(new C09721(packet));
    }

    private void processRequest(Packet packet) {
        Bytestream bytestream = (Bytestream) packet;
        if (!this.manager.getIgnoredBytestreamRequests().remove(bytestream.getSessionID())) {
            BytestreamRequest socks5BytestreamRequest = new Socks5BytestreamRequest(this.manager, bytestream);
            BytestreamListener userListener = this.manager.getUserListener(bytestream.getFrom());
            if (userListener != null) {
                userListener.incomingBytestreamRequest(socks5BytestreamRequest);
            } else if (this.manager.getAllRequestListeners().isEmpty()) {
                this.manager.replyRejectPacket(bytestream);
            } else {
                for (BytestreamListener userListener2 : this.manager.getAllRequestListeners()) {
                    userListener2.incomingBytestreamRequest(socks5BytestreamRequest);
                }
            }
        }
    }

    protected PacketFilter getFilter() {
        return this.initFilter;
    }

    protected void shutdown() {
        this.initiationListenerExecutor.shutdownNow();
    }
}

package org.jivesoftware.smackx.bytestreams.ibb;

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
import org.jivesoftware.smackx.bytestreams.ibb.packet.Open;

class InitiationListener implements PacketListener {
    private final PacketFilter initFilter;
    private final ExecutorService initiationListenerExecutor;
    private final InBandBytestreamManager manager;

    /* renamed from: org.jivesoftware.smackx.bytestreams.ibb.InitiationListener.1 */
    class C09711 implements Runnable {
        final /* synthetic */ Packet val$packet;

        C09711(Packet packet) {
            this.val$packet = packet;
        }

        public void run() {
            InitiationListener.this.processRequest(this.val$packet);
        }
    }

    protected InitiationListener(InBandBytestreamManager inBandBytestreamManager) {
        this.initFilter = new AndFilter(new PacketTypeFilter(Open.class), new IQTypeFilter(Type.SET));
        this.manager = inBandBytestreamManager;
        this.initiationListenerExecutor = Executors.newCachedThreadPool();
    }

    public void processPacket(Packet packet) {
        this.initiationListenerExecutor.execute(new C09711(packet));
    }

    private void processRequest(Packet packet) {
        Open open = (Open) packet;
        if (open.getBlockSize() > this.manager.getMaximumBlockSize()) {
            this.manager.replyResourceConstraintPacket(open);
        } else if (!this.manager.getIgnoredBytestreamRequests().remove(open.getSessionID())) {
            BytestreamRequest inBandBytestreamRequest = new InBandBytestreamRequest(this.manager, open);
            BytestreamListener userListener = this.manager.getUserListener(open.getFrom());
            if (userListener != null) {
                userListener.incomingBytestreamRequest(inBandBytestreamRequest);
            } else if (this.manager.getAllRequestListeners().isEmpty()) {
                this.manager.replyRejectPacket(open);
            } else {
                for (BytestreamListener userListener2 : this.manager.getAllRequestListeners()) {
                    userListener2.incomingBytestreamRequest(inBandBytestreamRequest);
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

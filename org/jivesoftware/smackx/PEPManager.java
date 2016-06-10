package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.PEPEvent;
import org.jivesoftware.smackx.packet.PEPItem;
import org.jivesoftware.smackx.packet.PEPPubSub;

public class PEPManager {
    private Connection connection;
    private PacketFilter packetFilter;
    private PacketListener packetListener;
    private List<PEPListener> pepListeners;

    /* renamed from: org.jivesoftware.smackx.PEPManager.1 */
    class C11711 implements PacketListener {
        C11711() {
        }

        public void processPacket(Packet packet) {
            Message message = (Message) packet;
            PEPManager.this.firePEPListeners(message.getFrom(), (PEPEvent) message.getExtension("event", "http://jabber.org/protocol/pubsub#event"));
        }
    }

    public PEPManager(Connection connection) {
        this.pepListeners = new ArrayList();
        this.packetFilter = new PacketExtensionFilter("event", "http://jabber.org/protocol/pubsub#event");
        this.connection = connection;
        init();
    }

    public void addPEPListener(PEPListener pEPListener) {
        synchronized (this.pepListeners) {
            if (!this.pepListeners.contains(pEPListener)) {
                this.pepListeners.add(pEPListener);
            }
        }
    }

    public void removePEPListener(PEPListener pEPListener) {
        synchronized (this.pepListeners) {
            this.pepListeners.remove(pEPListener);
        }
    }

    public void publish(PEPItem pEPItem) {
        Packet pEPPubSub = new PEPPubSub(pEPItem);
        pEPPubSub.setType(Type.SET);
        this.connection.sendPacket(pEPPubSub);
    }

    private void firePEPListeners(String str, PEPEvent pEPEvent) {
        synchronized (this.pepListeners) {
            PEPListener[] pEPListenerArr = new PEPListener[this.pepListeners.size()];
            this.pepListeners.toArray(pEPListenerArr);
        }
        for (PEPListener eventReceived : pEPListenerArr) {
            eventReceived.eventReceived(str, pEPEvent);
        }
    }

    private void init() {
        this.packetListener = new C11711();
        this.connection.addPacketListener(this.packetListener, this.packetFilter);
    }

    public void destroy() {
        if (this.connection != null) {
            this.connection.removePacketListener(this.packetListener);
        }
    }

    protected void finalize() throws Throwable {
        destroy();
        super.finalize();
    }
}

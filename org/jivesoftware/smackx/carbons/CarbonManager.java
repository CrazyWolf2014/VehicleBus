package org.jivesoftware.smackx.carbons;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.carbons.Carbon.Private;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;

public class CarbonManager {
    private static Map<Connection, CarbonManager> instances;
    private Connection connection;
    private volatile boolean enabled_state;

    /* renamed from: org.jivesoftware.smackx.carbons.CarbonManager.1 */
    static class C11821 implements ConnectionCreationListener {
        C11821() {
        }

        public void connectionCreated(Connection connection) {
            CarbonManager carbonManager = new CarbonManager(null);
        }
    }

    /* renamed from: org.jivesoftware.smackx.carbons.CarbonManager.3 */
    class C11833 implements PacketListener {
        final /* synthetic */ boolean val$new_state;

        C11833(boolean z) {
            this.val$new_state = z;
        }

        public void processPacket(Packet packet) {
            if (((IQ) packet).getType() == Type.RESULT) {
                CarbonManager.this.enabled_state = this.val$new_state;
            }
            CarbonManager.this.connection.removePacketListener(this);
        }
    }

    /* renamed from: org.jivesoftware.smackx.carbons.CarbonManager.2 */
    class C12922 extends IQ {
        final /* synthetic */ boolean val$new_state;

        C12922(boolean z) {
            this.val$new_state = z;
        }

        public String getChildElementXML() {
            return "<" + (this.val$new_state ? "enable" : "disable") + " xmlns='" + Carbon.NAMESPACE + "'/>";
        }
    }

    static {
        instances = Collections.synchronizedMap(new WeakHashMap());
        Connection.addConnectionCreationListener(new C11821());
    }

    private CarbonManager(Connection connection) {
        this.enabled_state = false;
        ServiceDiscoveryManager.getInstanceFor(connection).addFeature(Carbon.NAMESPACE);
        this.connection = connection;
        instances.put(connection, this);
    }

    public static CarbonManager getInstanceFor(Connection connection) {
        CarbonManager carbonManager = (CarbonManager) instances.get(connection);
        if (carbonManager == null) {
            return new CarbonManager(connection);
        }
        return carbonManager;
    }

    private IQ carbonsEnabledIQ(boolean z) {
        IQ c12922 = new C12922(z);
        c12922.setType(Type.SET);
        return c12922;
    }

    public boolean isSupportedByServer() {
        try {
            return ServiceDiscoveryManager.getInstanceFor(this.connection).discoverInfo(this.connection.getServiceName()).containsFeature(Carbon.NAMESPACE);
        } catch (XMPPException e) {
            return false;
        }
    }

    public void sendCarbonsEnabled(boolean z) {
        Packet carbonsEnabledIQ = carbonsEnabledIQ(z);
        this.connection.addPacketListener(new C11833(z), new PacketIDFilter(carbonsEnabledIQ.getPacketID()));
        this.connection.sendPacket(carbonsEnabledIQ);
    }

    public boolean setCarbonsEnabled(boolean z) {
        if (this.enabled_state == z) {
            return true;
        }
        Packet carbonsEnabledIQ = carbonsEnabledIQ(z);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(carbonsEnabledIQ.getPacketID()));
        this.connection.sendPacket(carbonsEnabledIQ);
        IQ iq = (IQ) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (iq == null || iq.getType() != Type.RESULT) {
            return false;
        }
        this.enabled_state = z;
        return true;
    }

    public boolean enableCarbons() {
        return setCarbonsEnabled(true);
    }

    public boolean disableCarbons() {
        return setCarbonsEnabled(false);
    }

    public boolean getCarbonsEnabled() {
        return this.enabled_state;
    }

    public static Carbon getCarbon(Message message) {
        Carbon carbon = (Carbon) message.getExtension(DeliveryReceipt.ELEMENT, Carbon.NAMESPACE);
        if (carbon == null) {
            return (Carbon) message.getExtension("sent", Carbon.NAMESPACE);
        }
        return carbon;
    }

    public static void disableCarbons(Message message) {
        message.addExtension(new Private());
    }
}

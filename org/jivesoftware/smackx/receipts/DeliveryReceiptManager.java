package org.jivesoftware.smackx.receipts;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.ServiceDiscoveryManager;

public class DeliveryReceiptManager implements PacketListener {
    private static Map<Connection, DeliveryReceiptManager> instances;
    private boolean auto_receipts_enabled;
    private Connection connection;
    private Set<ReceiptReceivedListener> receiptReceivedListeners;

    public interface ReceiptReceivedListener {
        void onReceiptReceived(String str, String str2, String str3);
    }

    /* renamed from: org.jivesoftware.smackx.receipts.DeliveryReceiptManager.1 */
    static class C12161 implements ConnectionCreationListener {
        C12161() {
        }

        public void connectionCreated(Connection connection) {
            DeliveryReceiptManager deliveryReceiptManager = new DeliveryReceiptManager(null);
        }
    }

    static {
        instances = Collections.synchronizedMap(new WeakHashMap());
        Connection.addConnectionCreationListener(new C12161());
    }

    private DeliveryReceiptManager(Connection connection) {
        this.auto_receipts_enabled = false;
        this.receiptReceivedListeners = Collections.synchronizedSet(new HashSet());
        ServiceDiscoveryManager.getInstanceFor(connection).addFeature(DeliveryReceipt.NAMESPACE);
        this.connection = connection;
        instances.put(connection, this);
        connection.addPacketListener(this, new PacketExtensionFilter(DeliveryReceipt.NAMESPACE));
    }

    public static synchronized DeliveryReceiptManager getInstanceFor(Connection connection) {
        DeliveryReceiptManager deliveryReceiptManager;
        synchronized (DeliveryReceiptManager.class) {
            deliveryReceiptManager = (DeliveryReceiptManager) instances.get(connection);
            if (deliveryReceiptManager == null) {
                deliveryReceiptManager = new DeliveryReceiptManager(connection);
            }
        }
        return deliveryReceiptManager;
    }

    public boolean isSupported(String str) {
        try {
            return ServiceDiscoveryManager.getInstanceFor(this.connection).discoverInfo(str).containsFeature(DeliveryReceipt.NAMESPACE);
        } catch (XMPPException e) {
            return false;
        }
    }

    public void processPacket(Packet packet) {
        DeliveryReceipt deliveryReceipt = (DeliveryReceipt) packet.getExtension(DeliveryReceipt.ELEMENT, DeliveryReceipt.NAMESPACE);
        if (deliveryReceipt != null) {
            for (ReceiptReceivedListener onReceiptReceived : this.receiptReceivedListeners) {
                onReceiptReceived.onReceiptReceived(packet.getFrom(), packet.getTo(), deliveryReceipt.getId());
            }
        }
        if (this.auto_receipts_enabled && ((DeliveryReceiptRequest) packet.getExtension(DeliveryReceiptRequest.ELEMENT, DeliveryReceipt.NAMESPACE)) != null) {
            Packet message = new Message(packet.getFrom(), Type.normal);
            message.addExtension(new DeliveryReceipt(packet.getPacketID()));
            this.connection.sendPacket(message);
        }
    }

    public void setAutoReceiptsEnabled(boolean z) {
        this.auto_receipts_enabled = z;
    }

    public void enableAutoReceipts() {
        setAutoReceiptsEnabled(true);
    }

    public void disableAutoReceipts() {
        setAutoReceiptsEnabled(false);
    }

    public boolean getAutoReceiptsEnabled() {
        return this.auto_receipts_enabled;
    }

    public void registerReceiptReceivedListener(ReceiptReceivedListener receiptReceivedListener) {
        this.receiptReceivedListeners.add(receiptReceivedListener);
    }

    public void unregisterReceiptReceivedListener(ReceiptReceivedListener receiptReceivedListener) {
        this.receiptReceivedListeners.remove(receiptReceivedListener);
    }

    public static boolean hasDeliveryReceiptRequest(Packet packet) {
        return packet.getExtension(DeliveryReceiptRequest.ELEMENT, DeliveryReceipt.NAMESPACE) != null;
    }

    public static void addDeliveryReceiptRequest(Message message) {
        message.addExtension(new DeliveryReceiptRequest());
    }
}

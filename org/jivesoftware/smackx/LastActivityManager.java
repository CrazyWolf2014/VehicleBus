package org.jivesoftware.smackx;

import com.google.protobuf.DescriptorProtos.MessageOptions;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smackx.packet.LastActivity;

public class LastActivityManager {
    private Connection connection;
    private long lastMessageSent;

    /* renamed from: org.jivesoftware.smackx.LastActivityManager.5 */
    static /* synthetic */ class C09695 {
        static final /* synthetic */ int[] $SwitchMap$org$jivesoftware$smack$packet$Presence$Mode;

        static {
            $SwitchMap$org$jivesoftware$smack$packet$Presence$Mode = new int[Mode.values().length];
            try {
                $SwitchMap$org$jivesoftware$smack$packet$Presence$Mode[Mode.available.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$jivesoftware$smack$packet$Presence$Mode[Mode.chat.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.LastActivityManager.1 */
    static class C11651 implements ConnectionCreationListener {
        C11651() {
        }

        public void connectionCreated(Connection connection) {
            LastActivityManager lastActivityManager = new LastActivityManager(null);
        }
    }

    /* renamed from: org.jivesoftware.smackx.LastActivityManager.2 */
    class C11662 implements PacketListener {
        C11662() {
        }

        public void processPacket(Packet packet) {
            Mode mode = ((Presence) packet).getMode();
            if (mode != null) {
                switch (C09695.$SwitchMap$org$jivesoftware$smack$packet$Presence$Mode[mode.ordinal()]) {
                    case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                        LastActivityManager.this.resetIdleTime();
                    default:
                }
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.LastActivityManager.3 */
    class C11673 implements PacketListener {
        C11673() {
        }

        public void processPacket(Packet packet) {
            if (((Message) packet).getType() != Type.error) {
                LastActivityManager.this.resetIdleTime();
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.LastActivityManager.4 */
    class C11684 implements PacketListener {
        C11684() {
        }

        public void processPacket(Packet packet) {
            Packet lastActivity = new LastActivity();
            lastActivity.setType(IQ.Type.RESULT);
            lastActivity.setTo(packet.getFrom());
            lastActivity.setFrom(packet.getTo());
            lastActivity.setPacketID(packet.getPacketID());
            lastActivity.setLastActivity(LastActivityManager.this.getIdleTime());
            LastActivityManager.this.connection.sendPacket(lastActivity);
        }
    }

    static {
        Connection.addConnectionCreationListener(new C11651());
    }

    private LastActivityManager(Connection connection) {
        this.connection = connection;
        connection.addPacketSendingListener(new C11662(), new PacketTypeFilter(Presence.class));
        connection.addPacketListener(new C11673(), new PacketTypeFilter(Message.class));
        connection.addPacketListener(new C11684(), new AndFilter(new IQTypeFilter(IQ.Type.GET), new PacketTypeFilter(LastActivity.class)));
        ServiceDiscoveryManager.getInstanceFor(connection).addFeature(LastActivity.NAMESPACE);
        resetIdleTime();
    }

    private void resetIdleTime() {
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (this) {
            this.lastMessageSent = currentTimeMillis;
        }
    }

    private long getIdleTime() {
        long j;
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (this) {
            j = this.lastMessageSent;
        }
        return (currentTimeMillis - j) / 1000;
    }

    public static LastActivity getLastActivity(Connection connection, String str) throws XMPPException {
        Packet lastActivity = new LastActivity();
        lastActivity.setTo(str);
        PacketCollector createPacketCollector = connection.createPacketCollector(new PacketIDFilter(lastActivity.getPacketID()));
        connection.sendPacket(lastActivity);
        LastActivity lastActivity2 = (LastActivity) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (lastActivity2 == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (lastActivity2.getError() == null) {
            return lastActivity2;
        } else {
            throw new XMPPException(lastActivity2.getError());
        }
    }

    public static boolean isLastActivitySupported(Connection connection, String str) {
        try {
            return ServiceDiscoveryManager.getInstanceFor(connection).discoverInfo(str).containsFeature(LastActivity.NAMESPACE);
        } catch (XMPPException e) {
            return false;
        }
    }
}

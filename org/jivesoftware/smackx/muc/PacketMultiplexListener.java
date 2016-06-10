package org.jivesoftware.smackx.muc;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.GroupChatInvitation;

class PacketMultiplexListener implements PacketListener {
    private static final PacketFilter DECLINES_FILTER;
    private static final PacketFilter MESSAGE_FILTER;
    private static final PacketFilter PRESENCE_FILTER;
    private static final PacketFilter SUBJECT_FILTER;
    private PacketListener declinesListener;
    private ConnectionDetachedPacketCollector messageCollector;
    private PacketListener presenceListener;
    private PacketListener subjectListener;

    /* renamed from: org.jivesoftware.smackx.muc.PacketMultiplexListener.1 */
    static class C12071 implements PacketFilter {
        C12071() {
        }

        public boolean accept(Packet packet) {
            return ((Message) packet).getSubject() != null;
        }
    }

    static {
        MESSAGE_FILTER = new MessageTypeFilter(Type.groupchat);
        PRESENCE_FILTER = new PacketTypeFilter(Presence.class);
        SUBJECT_FILTER = new C12071();
        DECLINES_FILTER = new PacketExtensionFilter(GroupChatInvitation.ELEMENT_NAME, "http://jabber.org/protocol/muc#user");
    }

    public PacketMultiplexListener(ConnectionDetachedPacketCollector connectionDetachedPacketCollector, PacketListener packetListener, PacketListener packetListener2, PacketListener packetListener3) {
        if (connectionDetachedPacketCollector == null) {
            throw new IllegalArgumentException("MessageCollector is null");
        } else if (packetListener == null) {
            throw new IllegalArgumentException("Presence listener is null");
        } else if (packetListener2 == null) {
            throw new IllegalArgumentException("Subject listener is null");
        } else if (packetListener3 == null) {
            throw new IllegalArgumentException("Declines listener is null");
        } else {
            this.messageCollector = connectionDetachedPacketCollector;
            this.presenceListener = packetListener;
            this.subjectListener = packetListener2;
            this.declinesListener = packetListener3;
        }
    }

    public void processPacket(Packet packet) {
        if (PRESENCE_FILTER.accept(packet)) {
            this.presenceListener.processPacket(packet);
        } else if (MESSAGE_FILTER.accept(packet)) {
            this.messageCollector.processPacket(packet);
            if (SUBJECT_FILTER.accept(packet)) {
                this.subjectListener.processPacket(packet);
            }
        } else if (DECLINES_FILTER.accept(packet)) {
            this.declinesListener.processPacket(packet);
        }
    }
}

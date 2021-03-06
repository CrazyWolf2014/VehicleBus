package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

public class PacketIDFilter implements PacketFilter {
    private String packetID;

    public PacketIDFilter(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Packet ID cannot be null.");
        }
        this.packetID = str;
    }

    public boolean accept(Packet packet) {
        return this.packetID.equals(packet.getPacketID());
    }

    public String toString() {
        return "PacketIDFilter by id: " + this.packetID;
    }
}

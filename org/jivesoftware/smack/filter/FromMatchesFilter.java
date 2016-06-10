package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.xmlpull.v1.XmlPullParser;

public class FromMatchesFilter implements PacketFilter {
    private String address;
    private boolean matchBareJID;

    public FromMatchesFilter(String str) {
        this.matchBareJID = false;
        if (str == null) {
            throw new IllegalArgumentException("Parameter cannot be null.");
        }
        this.address = str.toLowerCase();
        this.matchBareJID = XmlPullParser.NO_NAMESPACE.equals(StringUtils.parseResource(str));
    }

    public boolean accept(Packet packet) {
        if (packet.getFrom() == null) {
            return false;
        }
        if (this.matchBareJID) {
            return packet.getFrom().toLowerCase().startsWith(this.address);
        }
        return this.address.equals(packet.getFrom().toLowerCase());
    }

    public String toString() {
        return "FromMatchesFilter: " + this.address;
    }
}

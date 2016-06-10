package org.jivesoftware.smack.filter;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.packet.Packet;

public class AndFilter implements PacketFilter {
    private List<PacketFilter> filters;

    public AndFilter() {
        this.filters = new ArrayList();
    }

    public AndFilter(PacketFilter... packetFilterArr) {
        this.filters = new ArrayList();
        if (packetFilterArr == null) {
            throw new IllegalArgumentException("Parameter cannot be null.");
        }
        for (Object obj : packetFilterArr) {
            if (obj == null) {
                throw new IllegalArgumentException("Parameter cannot be null.");
            }
            this.filters.add(obj);
        }
    }

    public void addFilter(PacketFilter packetFilter) {
        if (packetFilter == null) {
            throw new IllegalArgumentException("Parameter cannot be null.");
        }
        this.filters.add(packetFilter);
    }

    public boolean accept(Packet packet) {
        for (PacketFilter accept : this.filters) {
            if (!accept.accept(packet)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return this.filters.toString();
    }
}

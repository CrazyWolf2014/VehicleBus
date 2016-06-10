package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Iterator;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.GroupChatInvitation;

public class MessageEvent implements PacketExtension {
    public static final String CANCELLED = "cancelled";
    public static final String COMPOSING = "composing";
    public static final String DELIVERED = "delivered";
    public static final String DISPLAYED = "displayed";
    public static final String OFFLINE = "offline";
    private boolean cancelled;
    private boolean composing;
    private boolean delivered;
    private boolean displayed;
    private boolean offline;
    private String packetID;

    public MessageEvent() {
        this.offline = false;
        this.delivered = false;
        this.displayed = false;
        this.composing = false;
        this.cancelled = true;
        this.packetID = null;
    }

    public String getElementName() {
        return GroupChatInvitation.ELEMENT_NAME;
    }

    public String getNamespace() {
        return "jabber:x:event";
    }

    public boolean isComposing() {
        return this.composing;
    }

    public boolean isDelivered() {
        return this.delivered;
    }

    public boolean isDisplayed() {
        return this.displayed;
    }

    public boolean isOffline() {
        return this.offline;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public String getPacketID() {
        return this.packetID;
    }

    public Iterator<String> getEventTypes() {
        ArrayList arrayList = new ArrayList();
        if (isDelivered()) {
            arrayList.add(DELIVERED);
        }
        if (!isMessageEventRequest() && isCancelled()) {
            arrayList.add(CANCELLED);
        }
        if (isComposing()) {
            arrayList.add(COMPOSING);
        }
        if (isDisplayed()) {
            arrayList.add(DISPLAYED);
        }
        if (isOffline()) {
            arrayList.add(OFFLINE);
        }
        return arrayList.iterator();
    }

    public void setComposing(boolean z) {
        this.composing = z;
        setCancelled(false);
    }

    public void setDelivered(boolean z) {
        this.delivered = z;
        setCancelled(false);
    }

    public void setDisplayed(boolean z) {
        this.displayed = z;
        setCancelled(false);
    }

    public void setOffline(boolean z) {
        this.offline = z;
        setCancelled(false);
    }

    public void setCancelled(boolean z) {
        this.cancelled = z;
    }

    public void setPacketID(String str) {
        this.packetID = str;
    }

    public boolean isMessageEventRequest() {
        return this.packetID == null;
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
        if (isOffline()) {
            stringBuilder.append("<").append(OFFLINE).append("/>");
        }
        if (isDelivered()) {
            stringBuilder.append("<").append(DELIVERED).append("/>");
        }
        if (isDisplayed()) {
            stringBuilder.append("<").append(DISPLAYED).append("/>");
        }
        if (isComposing()) {
            stringBuilder.append("<").append(COMPOSING).append("/>");
        }
        if (getPacketID() != null) {
            stringBuilder.append("<id>").append(getPacketID()).append("</id>");
        }
        stringBuilder.append("</").append(getElementName()).append(">");
        return stringBuilder.toString();
    }
}

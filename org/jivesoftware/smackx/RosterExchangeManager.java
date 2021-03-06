package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.packet.RosterExchange;

public class RosterExchangeManager {
    private Connection con;
    private PacketFilter packetFilter;
    private PacketListener packetListener;
    private List<RosterExchangeListener> rosterExchangeListeners;

    /* renamed from: org.jivesoftware.smackx.RosterExchangeManager.1 */
    class C11721 implements PacketListener {
        C11721() {
        }

        public void processPacket(Packet packet) {
            Message message = (Message) packet;
            RosterExchangeManager.this.fireRosterExchangeListeners(message.getFrom(), ((RosterExchange) message.getExtension(GroupChatInvitation.ELEMENT_NAME, "jabber:x:roster")).getRosterEntries());
        }
    }

    public RosterExchangeManager(Connection connection) {
        this.rosterExchangeListeners = new ArrayList();
        this.packetFilter = new PacketExtensionFilter(GroupChatInvitation.ELEMENT_NAME, "jabber:x:roster");
        this.con = connection;
        init();
    }

    public void addRosterListener(RosterExchangeListener rosterExchangeListener) {
        synchronized (this.rosterExchangeListeners) {
            if (!this.rosterExchangeListeners.contains(rosterExchangeListener)) {
                this.rosterExchangeListeners.add(rosterExchangeListener);
            }
        }
    }

    public void removeRosterListener(RosterExchangeListener rosterExchangeListener) {
        synchronized (this.rosterExchangeListeners) {
            this.rosterExchangeListeners.remove(rosterExchangeListener);
        }
    }

    public void send(Roster roster, String str) {
        Packet message = new Message(str);
        message.addExtension(new RosterExchange(roster));
        this.con.sendPacket(message);
    }

    public void send(RosterEntry rosterEntry, String str) {
        Packet message = new Message(str);
        PacketExtension rosterExchange = new RosterExchange();
        rosterExchange.addRosterEntry(rosterEntry);
        message.addExtension(rosterExchange);
        this.con.sendPacket(message);
    }

    public void send(RosterGroup rosterGroup, String str) {
        Packet message = new Message(str);
        PacketExtension rosterExchange = new RosterExchange();
        for (RosterEntry addRosterEntry : rosterGroup.getEntries()) {
            rosterExchange.addRosterEntry(addRosterEntry);
        }
        message.addExtension(rosterExchange);
        this.con.sendPacket(message);
    }

    private void fireRosterExchangeListeners(String str, Iterator<RemoteRosterEntry> it) {
        synchronized (this.rosterExchangeListeners) {
            RosterExchangeListener[] rosterExchangeListenerArr = new RosterExchangeListener[this.rosterExchangeListeners.size()];
            this.rosterExchangeListeners.toArray(rosterExchangeListenerArr);
        }
        for (RosterExchangeListener entriesReceived : rosterExchangeListenerArr) {
            entriesReceived.entriesReceived(str, it);
        }
    }

    private void init() {
        this.packetListener = new C11721();
        this.con.addPacketListener(this.packetListener, this.packetFilter);
    }

    public void destroy() {
        if (this.con != null) {
            this.con.removePacketListener(this.packetListener);
        }
    }

    protected void finalize() throws Throwable {
        destroy();
        super.finalize();
    }
}

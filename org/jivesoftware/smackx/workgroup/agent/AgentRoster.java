package org.jivesoftware.smackx.workgroup.agent;

import com.cnmobi.im.util.XmppConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.workgroup.packet.AgentStatus;
import org.jivesoftware.smackx.workgroup.packet.AgentStatusRequest;
import org.jivesoftware.smackx.workgroup.packet.AgentStatusRequest.Item;

public class AgentRoster {
    private static final int EVENT_AGENT_ADDED = 0;
    private static final int EVENT_AGENT_REMOVED = 1;
    private static final int EVENT_PRESENCE_CHANGED = 2;
    private Connection connection;
    private List<String> entries;
    private List<AgentRosterListener> listeners;
    private Map<String, Map<String, Presence>> presenceMap;
    boolean rosterInitialized;
    private String workgroupJID;

    private class AgentStatusListener implements PacketListener {
        private AgentStatusListener() {
        }

        public void processPacket(Packet packet) {
            if (packet instanceof AgentStatusRequest) {
                for (Item item : ((AgentStatusRequest) packet).getAgents()) {
                    String jid = item.getJID();
                    if (DiscoverItems.Item.REMOVE_ACTION.equals(item.getType())) {
                        AgentRoster.this.presenceMap.remove(StringUtils.parseName(StringUtils.parseName(jid) + XmppConnection.JID_SEPARATOR + StringUtils.parseServer(jid)));
                        AgentRoster.this.fireEvent(AgentRoster.EVENT_AGENT_REMOVED, jid);
                    } else {
                        AgentRoster.this.entries.add(jid);
                        AgentRoster.this.fireEvent(AgentRoster.EVENT_AGENT_ADDED, jid);
                    }
                }
                AgentRoster.this.rosterInitialized = true;
            }
        }
    }

    private class PresencePacketListener implements PacketListener {
        private PresencePacketListener() {
        }

        public void processPacket(Packet packet) {
            Presence presence = (Presence) packet;
            String from = presence.getFrom();
            if (from == null) {
                System.out.println("Presence with no FROM: " + presence.toXML());
                return;
            }
            String access$200 = AgentRoster.this.getPresenceMapKey(from);
            if (presence.getType() == Type.available) {
                AgentStatus agentStatus = (AgentStatus) presence.getExtension(AgentStatus.ELEMENT_NAME, AgentStatusRequest.NAMESPACE);
                if (agentStatus != null && AgentRoster.this.workgroupJID.equals(agentStatus.getWorkgroupJID())) {
                    Map hashMap;
                    if (AgentRoster.this.presenceMap.get(access$200) == null) {
                        hashMap = new HashMap();
                        AgentRoster.this.presenceMap.put(access$200, hashMap);
                    } else {
                        hashMap = (Map) AgentRoster.this.presenceMap.get(access$200);
                    }
                    synchronized (hashMap) {
                        hashMap.put(StringUtils.parseResource(from), presence);
                    }
                    synchronized (AgentRoster.this.entries) {
                        for (String toLowerCase : AgentRoster.this.entries) {
                            if (toLowerCase.toLowerCase().equals(StringUtils.parseBareAddress(access$200).toLowerCase())) {
                                AgentRoster.this.fireEvent(AgentRoster.EVENT_PRESENCE_CHANGED, packet);
                            }
                        }
                    }
                }
            } else if (presence.getType() == Type.unavailable) {
                if (AgentRoster.this.presenceMap.get(access$200) != null) {
                    Map map = (Map) AgentRoster.this.presenceMap.get(access$200);
                    synchronized (map) {
                        map.remove(StringUtils.parseResource(from));
                    }
                    if (map.isEmpty()) {
                        AgentRoster.this.presenceMap.remove(access$200);
                    }
                }
                synchronized (AgentRoster.this.entries) {
                    for (String toLowerCase2 : AgentRoster.this.entries) {
                        if (toLowerCase2.toLowerCase().equals(StringUtils.parseBareAddress(access$200).toLowerCase())) {
                            AgentRoster.this.fireEvent(AgentRoster.EVENT_PRESENCE_CHANGED, packet);
                        }
                    }
                }
            }
        }
    }

    AgentRoster(Connection connection, String str) {
        this.rosterInitialized = false;
        this.connection = connection;
        this.workgroupJID = str;
        this.entries = new ArrayList();
        this.listeners = new ArrayList();
        this.presenceMap = new HashMap();
        connection.addPacketListener(new AgentStatusListener(), new PacketTypeFilter(AgentStatusRequest.class));
        connection.addPacketListener(new PresencePacketListener(), new PacketTypeFilter(Presence.class));
        Packet agentStatusRequest = new AgentStatusRequest();
        agentStatusRequest.setTo(str);
        connection.sendPacket(agentStatusRequest);
    }

    public void reload() {
        Packet agentStatusRequest = new AgentStatusRequest();
        agentStatusRequest.setTo(this.workgroupJID);
        this.connection.sendPacket(agentStatusRequest);
    }

    public void addListener(AgentRosterListener agentRosterListener) {
        synchronized (this.listeners) {
            if (!this.listeners.contains(agentRosterListener)) {
                this.listeners.add(agentRosterListener);
                for (String str : getAgents()) {
                    if (this.entries.contains(str)) {
                        agentRosterListener.agentAdded(str);
                        Map map = (Map) this.presenceMap.get(str);
                        if (map != null) {
                            for (Presence presenceChanged : map.values()) {
                                agentRosterListener.presenceChanged(presenceChanged);
                            }
                        }
                    }
                }
            }
        }
    }

    public void removeListener(AgentRosterListener agentRosterListener) {
        synchronized (this.listeners) {
            this.listeners.remove(agentRosterListener);
        }
    }

    public int getAgentCount() {
        return this.entries.size();
    }

    public Set<String> getAgents() {
        Set hashSet = new HashSet();
        synchronized (this.entries) {
            for (Object add : this.entries) {
                hashSet.add(add);
            }
        }
        return Collections.unmodifiableSet(hashSet);
    }

    public boolean contains(String str) {
        if (str == null) {
            return false;
        }
        synchronized (this.entries) {
            for (String toLowerCase : this.entries) {
                if (toLowerCase.toLowerCase().equals(str.toLowerCase())) {
                    return true;
                }
            }
            return false;
        }
    }

    public Presence getPresence(String str) {
        Map map = (Map) this.presenceMap.get(getPresenceMapKey(str));
        if (map == null) {
            Presence presence = new Presence(Type.unavailable);
            presence.setFrom(str);
            return presence;
        }
        presence = null;
        for (Object obj : map.keySet()) {
            Presence presence2 = (Presence) map.get(obj);
            if (presence == null) {
                presence = presence2;
            } else if (presence2.getPriority() > presence.getPriority()) {
                presence = presence2;
            }
        }
        if (presence != null) {
            return presence;
        }
        presence = new Presence(Type.unavailable);
        presence.setFrom(str);
        return presence;
    }

    private String getPresenceMapKey(String str) {
        if (contains(str)) {
            return str;
        }
        return StringUtils.parseBareAddress(str).toLowerCase();
    }

    private void fireEvent(int i, Object obj) {
        synchronized (this.listeners) {
            AgentRosterListener[] agentRosterListenerArr = new AgentRosterListener[this.listeners.size()];
            this.listeners.toArray(agentRosterListenerArr);
        }
        for (int i2 = EVENT_AGENT_ADDED; i2 < agentRosterListenerArr.length; i2 += EVENT_AGENT_REMOVED) {
            switch (i) {
                case EVENT_AGENT_ADDED /*0*/:
                    agentRosterListenerArr[i2].agentAdded((String) obj);
                    break;
                case EVENT_AGENT_REMOVED /*1*/:
                    agentRosterListenerArr[i2].agentRemoved((String) obj);
                    break;
                case EVENT_PRESENCE_CHANGED /*2*/:
                    agentRosterListenerArr[i2].presenceChanged((Presence) obj);
                    break;
                default:
                    break;
            }
        }
    }
}

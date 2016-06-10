package com.cnmobi.im.util;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

public class XmppService {
    public static boolean deleteAccount(XMPPConnection connection) {
        try {
            connection.disconnect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static List<RosterGroup> getGroups(Roster roster) {
        List<RosterGroup> groupsList = new ArrayList();
        for (RosterGroup add : roster.getGroups()) {
            groupsList.add(add);
        }
        return groupsList;
    }

    public static List<RosterEntry> getEntriesByGroup(Roster roster, String groupName) {
        List<RosterEntry> EntriesList = new ArrayList();
        for (RosterEntry add : roster.getGroup(groupName).getEntries()) {
            EntriesList.add(add);
        }
        return EntriesList;
    }

    public static List<RosterEntry> getAllEntries(Roster roster) {
        List<RosterEntry> EntriesList = new ArrayList();
        for (RosterEntry add : roster.getEntries()) {
            EntriesList.add(add);
        }
        return EntriesList;
    }

    public static boolean addGroup(Roster roster, String groupName) {
        try {
            roster.createGroup(groupName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeGroup(Roster roster, String groupName) {
        return false;
    }

    public static boolean addUser(Roster roster, String userName, String name) {
        try {
            roster.createEntry(userName, name, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addUsers(Roster roster, String userName, String name, String groupName) {
        try {
            roster.createEntry(userName, name, new String[]{groupName});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeUser(Roster roster, String userJid) {
        try {
            roster.removeEntry(roster.getEntry(userJid));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void addUserToGroup(String userJid, String groupName, XMPPConnection connection) {
        RosterGroup group = connection.getRoster().getGroup(groupName);
        RosterEntry entry = connection.getRoster().getEntry(userJid);
        if (group == null) {
            RosterGroup newGroup = connection.getRoster().createGroup("\u6211\u7684\u597d\u53cb");
            if (entry != null) {
                newGroup.addEntry(entry);
            }
        } else if (entry != null) {
            try {
                group.addEntry(entry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeUserFromGroup(String userJid, String groupName, XMPPConnection connection) {
        RosterGroup group = connection.getRoster().getGroup(groupName);
        if (group != null) {
            try {
                RosterEntry entry = connection.getRoster().getEntry(userJid);
                if (entry != null) {
                    group.removeEntry(entry);
                }
            } catch (XMPPException e) {
                e.printStackTrace();
            }
        }
    }

    public static void changeStateMessage(XMPPConnection connection, String status) {
        Presence presence = new Presence(Type.available);
        presence.setStatus(status);
        connection.sendPacket(presence);
    }
}

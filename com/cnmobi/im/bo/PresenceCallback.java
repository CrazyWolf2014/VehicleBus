package com.cnmobi.im.bo;

import java.util.Collection;
import org.jivesoftware.smack.packet.Presence;

public interface PresenceCallback {
    void entriesChange(Collection<String> collection, int i);

    void presenceChange(Presence presence);
}

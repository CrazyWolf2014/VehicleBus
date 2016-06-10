package org.jivesoftware.smackx.workgroup.agent;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.jivesoftware.smackx.workgroup.QueueUser;

public class WorkgroupQueue {
    private int averageWaitTime;
    private int currentChats;
    private int maxChats;
    private String name;
    private Date oldestEntry;
    private Status status;
    private Set<QueueUser> users;

    public static class Status {
        public static final Status ACTIVE;
        public static final Status CLOSED;
        public static final Status OPEN;
        private String value;

        static {
            OPEN = new Status("open");
            ACTIVE = new Status("active");
            CLOSED = new Status("closed");
        }

        public static Status fromString(String str) {
            if (str == null) {
                return null;
            }
            String toLowerCase = str.toLowerCase();
            if (OPEN.toString().equals(toLowerCase)) {
                return OPEN;
            }
            if (ACTIVE.toString().equals(toLowerCase)) {
                return ACTIVE;
            }
            if (CLOSED.toString().equals(toLowerCase)) {
                return CLOSED;
            }
            return null;
        }

        private Status(String str) {
            this.value = str;
        }

        public String toString() {
            return this.value;
        }
    }

    WorkgroupQueue(String str) {
        this.status = Status.CLOSED;
        this.averageWaitTime = -1;
        this.oldestEntry = null;
        this.users = Collections.emptySet();
        this.maxChats = 0;
        this.currentChats = 0;
        this.name = str;
    }

    public String getName() {
        return this.name;
    }

    public Status getStatus() {
        return this.status;
    }

    void setStatus(Status status) {
        this.status = status;
    }

    public int getUserCount() {
        if (this.users == null) {
            return 0;
        }
        return this.users.size();
    }

    public Iterator<QueueUser> getUsers() {
        if (this.users == null) {
            return new HashSet().iterator();
        }
        return Collections.unmodifiableSet(this.users).iterator();
    }

    void setUsers(Set<QueueUser> set) {
        this.users = set;
    }

    public int getAverageWaitTime() {
        return this.averageWaitTime;
    }

    void setAverageWaitTime(int i) {
        this.averageWaitTime = i;
    }

    public Date getOldestEntry() {
        return this.oldestEntry;
    }

    void setOldestEntry(Date date) {
        this.oldestEntry = date;
    }

    public int getMaxChats() {
        return this.maxChats;
    }

    void setMaxChats(int i) {
        this.maxChats = i;
    }

    public int getCurrentChats() {
        return this.currentChats;
    }

    void setCurrentChats(int i) {
        this.currentChats = i;
    }
}

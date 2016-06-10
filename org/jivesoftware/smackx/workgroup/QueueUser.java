package org.jivesoftware.smackx.workgroup;

import java.util.Date;

public class QueueUser {
    private int estimatedTime;
    private Date joinDate;
    private int queuePosition;
    private String userID;

    public QueueUser(String str, int i, int i2, Date date) {
        this.userID = str;
        this.queuePosition = i;
        this.estimatedTime = i2;
        this.joinDate = date;
    }

    public String getUserID() {
        return this.userID;
    }

    public int getQueuePosition() {
        return this.queuePosition;
    }

    public int getEstimatedRemainingTime() {
        return this.estimatedTime;
    }

    public Date getQueueJoinTimestamp() {
        return this.joinDate;
    }
}

package org.jivesoftware.smackx.workgroup.agent;

import java.util.Date;

public class RevokedOffer {
    private String reason;
    private String sessionID;
    private Date timestamp;
    private String userID;
    private String userJID;
    private String workgroupName;

    RevokedOffer(String str, String str2, String str3, String str4, String str5, Date date) {
        this.userJID = str;
        this.userID = str2;
        this.workgroupName = str3;
        this.sessionID = str4;
        this.reason = str5;
        this.timestamp = date;
    }

    public String getUserJID() {
        return this.userJID;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getWorkgroupName() {
        return this.workgroupName;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public String getReason() {
        return this.reason;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }
}

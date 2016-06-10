package org.jivesoftware.smackx.workgroup.ext.history;

import java.util.Date;

public class AgentChatSession {
    public long duration;
    public String question;
    public String sessionID;
    public Date startDate;
    public String visitorsEmail;
    public String visitorsName;

    public AgentChatSession(Date date, long j, String str, String str2, String str3, String str4) {
        this.startDate = date;
        this.duration = j;
        this.visitorsName = str;
        this.visitorsEmail = str2;
        this.sessionID = str3;
        this.question = str4;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date date) {
        this.startDate = date;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long j) {
        this.duration = j;
    }

    public String getVisitorsName() {
        return this.visitorsName;
    }

    public void setVisitorsName(String str) {
        this.visitorsName = str;
    }

    public String getVisitorsEmail() {
        return this.visitorsEmail;
    }

    public void setVisitorsEmail(String str) {
        this.visitorsEmail = str;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(String str) {
        this.sessionID = str;
    }

    public void setQuestion(String str) {
        this.question = str;
    }

    public String getQuestion() {
        return this.question;
    }
}

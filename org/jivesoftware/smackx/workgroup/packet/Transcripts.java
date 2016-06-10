package org.jivesoftware.smackx.workgroup.packet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.IQ;

public class Transcripts extends IQ {
    private static final SimpleDateFormat UTC_FORMAT;
    private List<TranscriptSummary> summaries;
    private String userID;

    public static class AgentDetail {
        private String agentJID;
        private Date joinTime;
        private Date leftTime;

        public AgentDetail(String str, Date date, Date date2) {
            this.agentJID = str;
            this.joinTime = date;
            this.leftTime = date2;
        }

        public String getAgentJID() {
            return this.agentJID;
        }

        public Date getJoinTime() {
            return this.joinTime;
        }

        public Date getLeftTime() {
            return this.leftTime;
        }

        public String toXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<agent>");
            if (this.agentJID != null) {
                stringBuilder.append("<agentJID>").append(this.agentJID).append("</agentJID>");
            }
            if (this.joinTime != null) {
                stringBuilder.append("<joinTime>").append(Transcripts.UTC_FORMAT.format(this.joinTime)).append("</joinTime>");
            }
            if (this.leftTime != null) {
                stringBuilder.append("<leftTime>").append(Transcripts.UTC_FORMAT.format(this.leftTime)).append("</leftTime>");
            }
            stringBuilder.append("</agent>");
            return stringBuilder.toString();
        }
    }

    public static class TranscriptSummary {
        private List<AgentDetail> agentDetails;
        private Date joinTime;
        private Date leftTime;
        private String sessionID;

        public TranscriptSummary(String str, Date date, Date date2, List<AgentDetail> list) {
            this.sessionID = str;
            this.joinTime = date;
            this.leftTime = date2;
            this.agentDetails = list;
        }

        public String getSessionID() {
            return this.sessionID;
        }

        public Date getJoinTime() {
            return this.joinTime;
        }

        public Date getLeftTime() {
            return this.leftTime;
        }

        public List<AgentDetail> getAgentDetails() {
            return this.agentDetails;
        }

        public String toXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<transcript sessionID=\"").append(this.sessionID).append("\">");
            if (this.joinTime != null) {
                stringBuilder.append("<joinTime>").append(Transcripts.UTC_FORMAT.format(this.joinTime)).append("</joinTime>");
            }
            if (this.leftTime != null) {
                stringBuilder.append("<leftTime>").append(Transcripts.UTC_FORMAT.format(this.leftTime)).append("</leftTime>");
            }
            stringBuilder.append("<agents>");
            for (AgentDetail toXML : this.agentDetails) {
                stringBuilder.append(toXML.toXML());
            }
            stringBuilder.append("</agents></transcript>");
            return stringBuilder.toString();
        }
    }

    static {
        UTC_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
        UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+0"));
    }

    public Transcripts(String str) {
        this.userID = str;
        this.summaries = new ArrayList();
    }

    public Transcripts(String str, List<TranscriptSummary> list) {
        this.userID = str;
        this.summaries = list;
    }

    public String getUserID() {
        return this.userID;
    }

    public List<TranscriptSummary> getSummaries() {
        return Collections.unmodifiableList(this.summaries);
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<transcripts xmlns=\"http://jivesoftware.com/protocol/workgroup\" userID=\"").append(this.userID).append("\">");
        for (TranscriptSummary toXML : this.summaries) {
            stringBuilder.append(toXML.toXML());
        }
        stringBuilder.append("</transcripts>");
        return stringBuilder.toString();
    }
}

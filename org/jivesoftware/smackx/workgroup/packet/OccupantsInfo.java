package org.jivesoftware.smackx.workgroup.packet;

import com.tencent.mm.sdk.plugin.BaseProfile;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class OccupantsInfo extends IQ {
    public static final String ELEMENT_NAME = "occupants-info";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private static final SimpleDateFormat UTC_FORMAT;
    private final Set<OccupantInfo> occupants;
    private String roomID;

    public static class OccupantInfo {
        private String jid;
        private Date joined;
        private String nickname;

        public OccupantInfo(String str, String str2, Date date) {
            this.jid = str;
            this.nickname = str2;
            this.joined = date;
        }

        public String getJID() {
            return this.jid;
        }

        public String getNickname() {
            return this.nickname;
        }

        public Date getJoined() {
            return this.joined;
        }
    }

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            if (xmlPullParser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            IQ occupantsInfo = new OccupantsInfo(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "roomID"));
            Object obj = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2 && "occupant".equals(xmlPullParser.getName())) {
                    occupantsInfo.occupants.add(parseOccupantInfo(xmlPullParser));
                } else if (next == 3 && OccupantsInfo.ELEMENT_NAME.equals(xmlPullParser.getName())) {
                    obj = 1;
                }
            }
            return occupantsInfo;
        }

        private OccupantInfo parseOccupantInfo(XmlPullParser xmlPullParser) throws Exception {
            Date date = null;
            Object obj = null;
            String str = null;
            String str2 = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2 && "jid".equals(xmlPullParser.getName())) {
                    str2 = xmlPullParser.nextText();
                } else if (next == 2 && BaseProfile.COL_NICKNAME.equals(xmlPullParser.getName())) {
                    str = xmlPullParser.nextText();
                } else if (next == 2 && "joined".equals(xmlPullParser.getName())) {
                    date = OccupantsInfo.UTC_FORMAT.parse(xmlPullParser.nextText());
                } else if (next == 3 && "occupant".equals(xmlPullParser.getName())) {
                    obj = 1;
                }
            }
            return new OccupantInfo(str2, str, date);
        }
    }

    static {
        UTC_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
        UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+0"));
    }

    public OccupantsInfo(String str) {
        this.roomID = str;
        this.occupants = new HashSet();
    }

    public String getRoomID() {
        return this.roomID;
    }

    public int getOccupantsCount() {
        return this.occupants.size();
    }

    public Set<OccupantInfo> getOccupants() {
        return Collections.unmodifiableSet(this.occupants);
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append(NAMESPACE);
        stringBuilder.append("\" roomID=\"").append(this.roomID).append("\">");
        synchronized (this.occupants) {
            for (OccupantInfo occupantInfo : this.occupants) {
                stringBuilder.append("<occupant>");
                stringBuilder.append("<jid>");
                stringBuilder.append(occupantInfo.getJID());
                stringBuilder.append("</jid>");
                stringBuilder.append("<name>");
                stringBuilder.append(occupantInfo.getNickname());
                stringBuilder.append("</name>");
                stringBuilder.append("<joined>");
                stringBuilder.append(UTC_FORMAT.format(occupantInfo.getJoined()));
                stringBuilder.append("</joined>");
                stringBuilder.append("</occupant>");
            }
        }
        stringBuilder.append("</").append(ELEMENT_NAME).append("> ");
        return stringBuilder.toString();
    }
}

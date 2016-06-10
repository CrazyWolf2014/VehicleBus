package org.jivesoftware.smackx.workgroup.packet;

import com.cnmobi.im.dto.Msg;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.workgroup.QueueUser;
import org.xmlpull.v1.XmlPullParser;

public class QueueDetails implements PacketExtension {
    private static final String DATE_FORMAT = "yyyyMMdd'T'HH:mm:ss";
    public static final String ELEMENT_NAME = "notify-queue-details";
    public static final String NAMESPACE = "http://jabber.org/protocol/workgroup";
    private SimpleDateFormat dateFormat;
    private Set<QueueUser> users;

    public static class Provider implements PacketExtensionProvider {
        public PacketExtension parseExtension(XmlPullParser xmlPullParser) throws Exception {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(QueueDetails.DATE_FORMAT);
            PacketExtension queueDetails = new QueueDetails();
            int eventType = xmlPullParser.getEventType();
            while (eventType != 3 && QueueDetails.ELEMENT_NAME.equals(xmlPullParser.getName())) {
                eventType = xmlPullParser.next();
                while (eventType == 2 && UserID.ELEMENT_NAME.equals(xmlPullParser.getName())) {
                    int i;
                    int i2;
                    int next;
                    Date date;
                    String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "jid");
                    if (attributeValue == null) {
                        i = -1;
                        i2 = -1;
                        next = xmlPullParser.next();
                        date = null;
                    } else {
                        i = -1;
                        i2 = -1;
                        next = xmlPullParser.next();
                        date = null;
                    }
                    while (true) {
                        if (next == 3 && UserID.ELEMENT_NAME.equals(xmlPullParser.getName())) {
                            break;
                        }
                        if ("position".equals(xmlPullParser.getName())) {
                            i2 = Integer.parseInt(xmlPullParser.nextText());
                        } else if (Msg.TIME_REDIO.equals(xmlPullParser.getName())) {
                            i = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("join-time".equals(xmlPullParser.getName())) {
                            date = simpleDateFormat.parse(xmlPullParser.nextText());
                        } else if (xmlPullParser.getName().equals("waitTime")) {
                            System.out.println(simpleDateFormat.parse(xmlPullParser.nextText()));
                        }
                        next = xmlPullParser.next();
                        if (next != 3) {
                        }
                    }
                    queueDetails.addUser(new QueueUser(attributeValue, i2, i, date));
                    eventType = xmlPullParser.next();
                }
            }
            return queueDetails;
        }
    }

    private QueueDetails() {
        this.dateFormat = new SimpleDateFormat(DATE_FORMAT);
        this.users = new HashSet();
    }

    public int getUserCount() {
        return this.users.size();
    }

    public Set<QueueUser> getUsers() {
        Set<QueueUser> set;
        synchronized (this.users) {
            set = this.users;
        }
        return set;
    }

    private void addUser(QueueUser queueUser) {
        synchronized (this.users) {
            this.users.add(queueUser);
        }
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return NAMESPACE;
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append(NAMESPACE).append("\">");
        synchronized (this.users) {
            for (QueueUser queueUser : this.users) {
                int queuePosition = queueUser.getQueuePosition();
                int estimatedRemainingTime = queueUser.getEstimatedRemainingTime();
                Date queueJoinTimestamp = queueUser.getQueueJoinTimestamp();
                stringBuilder.append("<user jid=\"").append(queueUser.getUserID()).append("\">");
                if (queuePosition != -1) {
                    stringBuilder.append("<position>").append(queuePosition).append("</position>");
                }
                if (estimatedRemainingTime != -1) {
                    stringBuilder.append("<time>").append(estimatedRemainingTime).append("</time>");
                }
                if (queueJoinTimestamp != null) {
                    stringBuilder.append("<join-time>");
                    stringBuilder.append(this.dateFormat.format(queueJoinTimestamp));
                    stringBuilder.append("</join-time>");
                }
                stringBuilder.append("</user>");
            }
        }
        stringBuilder.append("</").append(ELEMENT_NAME).append(">");
        return stringBuilder.toString();
    }
}

package org.jivesoftware.smackx.workgroup.packet;

import com.amap.mapapi.location.LocationManagerProxy;
import com.cnmobi.im.dto.Msg;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.workgroup.agent.WorkgroupQueue.Status;
import org.xmlpull.v1.XmlPullParser;

public class QueueOverview implements PacketExtension {
    private static final String DATE_FORMAT = "yyyyMMdd'T'HH:mm:ss";
    public static String ELEMENT_NAME;
    public static String NAMESPACE;
    private int averageWaitTime;
    private SimpleDateFormat dateFormat;
    private Date oldestEntry;
    private Status status;
    private int userCount;

    public static class Provider implements PacketExtensionProvider {
        public PacketExtension parseExtension(XmlPullParser xmlPullParser) throws Exception {
            int eventType = xmlPullParser.getEventType();
            QueueOverview queueOverview = new QueueOverview();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(QueueOverview.DATE_FORMAT);
            eventType = eventType != 2 ? xmlPullParser.next() : xmlPullParser.next();
            while (true) {
                if (eventType == 3 && QueueOverview.ELEMENT_NAME.equals(xmlPullParser.getName())) {
                    return eventType != 3 ? queueOverview : queueOverview;
                } else {
                    if ("count".equals(xmlPullParser.getName())) {
                        queueOverview.setUserCount(Integer.parseInt(xmlPullParser.nextText()));
                    } else if (Msg.TIME_REDIO.equals(xmlPullParser.getName())) {
                        queueOverview.setAverageWaitTime(Integer.parseInt(xmlPullParser.nextText()));
                    } else if ("oldest".equals(xmlPullParser.getName())) {
                        queueOverview.setOldestEntry(simpleDateFormat.parse(xmlPullParser.nextText()));
                    } else if (LocationManagerProxy.KEY_STATUS_CHANGED.equals(xmlPullParser.getName())) {
                        queueOverview.setStatus(Status.fromString(xmlPullParser.nextText()));
                    }
                    eventType = xmlPullParser.next();
                    if (eventType != 3) {
                    }
                }
            }
        }
    }

    static {
        ELEMENT_NAME = "notify-queue";
        NAMESPACE = AgentStatusRequest.NAMESPACE;
    }

    QueueOverview() {
        this.dateFormat = new SimpleDateFormat(DATE_FORMAT);
        this.averageWaitTime = -1;
        this.oldestEntry = null;
        this.userCount = -1;
        this.status = null;
    }

    void setAverageWaitTime(int i) {
        this.averageWaitTime = i;
    }

    public int getAverageWaitTime() {
        return this.averageWaitTime;
    }

    void setOldestEntry(Date date) {
        this.oldestEntry = date;
    }

    public Date getOldestEntry() {
        return this.oldestEntry;
    }

    void setUserCount(int i) {
        this.userCount = i;
    }

    public int getUserCount() {
        return this.userCount;
    }

    public Status getStatus() {
        return this.status;
    }

    void setStatus(Status status) {
        this.status = status;
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
        if (this.userCount != -1) {
            stringBuilder.append("<count>").append(this.userCount).append("</count>");
        }
        if (this.oldestEntry != null) {
            stringBuilder.append("<oldest>").append(this.dateFormat.format(this.oldestEntry)).append("</oldest>");
        }
        if (this.averageWaitTime != -1) {
            stringBuilder.append("<time>").append(this.averageWaitTime).append("</time>");
        }
        if (this.status != null) {
            stringBuilder.append("<status>").append(this.status).append("</status>");
        }
        stringBuilder.append("</").append(ELEMENT_NAME).append(">");
        return stringBuilder.toString();
    }
}

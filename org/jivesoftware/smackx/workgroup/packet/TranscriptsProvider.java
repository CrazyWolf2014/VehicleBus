package org.jivesoftware.smackx.workgroup.packet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.workgroup.packet.Transcripts.AgentDetail;
import org.jivesoftware.smackx.workgroup.packet.Transcripts.TranscriptSummary;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class TranscriptsProvider implements IQProvider {
    private static final SimpleDateFormat UTC_FORMAT;

    static {
        UTC_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
        UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+0"));
    }

    public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
        String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "userID");
        List arrayList = new ArrayList();
        Object obj = null;
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals("transcript")) {
                    arrayList.add(parseSummary(xmlPullParser));
                }
            } else if (next == 3 && xmlPullParser.getName().equals("transcripts")) {
                obj = 1;
            }
        }
        return new Transcripts(attributeValue, arrayList);
    }

    private TranscriptSummary parseSummary(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        Date date = null;
        String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "sessionID");
        List arrayList = new ArrayList();
        Object obj = null;
        Date date2 = null;
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals("joinTime")) {
                    try {
                        date2 = UTC_FORMAT.parse(xmlPullParser.nextText());
                    } catch (ParseException e) {
                    }
                } else if (xmlPullParser.getName().equals("leftTime")) {
                    try {
                        date = UTC_FORMAT.parse(xmlPullParser.nextText());
                    } catch (ParseException e2) {
                    }
                } else if (xmlPullParser.getName().equals("agents")) {
                    arrayList = parseAgents(xmlPullParser);
                }
            } else if (next == 3 && xmlPullParser.getName().equals("transcript")) {
                obj = 1;
            }
        }
        return new TranscriptSummary(attributeValue, date2, date, arrayList);
    }

    private List<AgentDetail> parseAgents(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        List<AgentDetail> arrayList = new ArrayList();
        Object obj = null;
        Date date = null;
        Date date2 = null;
        String str = null;
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals("agentJID")) {
                    str = xmlPullParser.nextText();
                } else if (xmlPullParser.getName().equals("joinTime")) {
                    try {
                        date2 = UTC_FORMAT.parse(xmlPullParser.nextText());
                    } catch (ParseException e) {
                    }
                } else if (xmlPullParser.getName().equals("leftTime")) {
                    try {
                        date = UTC_FORMAT.parse(xmlPullParser.nextText());
                    } catch (ParseException e2) {
                    }
                } else if (xmlPullParser.getName().equals("agent")) {
                    date = null;
                    date2 = null;
                    str = null;
                }
            } else if (next == 3) {
                if (xmlPullParser.getName().equals("agents")) {
                    obj = 1;
                } else if (xmlPullParser.getName().equals("agent")) {
                    arrayList.add(new AgentDetail(str, date2, date));
                }
            }
        }
        return arrayList;
    }
}

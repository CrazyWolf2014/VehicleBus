package org.jivesoftware.smackx.workgroup.packet;

import com.launch.service.BundleBuilder;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.xmlpull.v1.XmlPullParser;

public class TranscriptProvider implements IQProvider {
    public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
        String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "sessionID");
        List arrayList = new ArrayList();
        Object obj = null;
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals(BundleBuilder.AskFromMessage)) {
                    arrayList.add(PacketParserUtils.parseMessage(xmlPullParser));
                } else if (xmlPullParser.getName().equals("presence")) {
                    arrayList.add(PacketParserUtils.parsePresence(xmlPullParser));
                }
            } else if (next == 3 && xmlPullParser.getName().equals("transcript")) {
                obj = 1;
            }
        }
        return new Transcript(attributeValue, arrayList);
    }
}

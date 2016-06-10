package org.jivesoftware.smackx.bytestreams.ibb.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager.StanzaType;
import org.jivesoftware.smackx.bytestreams.ibb.packet.Open;
import org.xmlpull.v1.XmlPullParser;

public class OpenIQProvider implements IQProvider {
    public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
        StanzaType stanzaType;
        String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, AlixDefine.SID);
        int parseInt = Integer.parseInt(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "block-size"));
        String attributeValue2 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "stanza");
        if (attributeValue2 == null) {
            stanzaType = StanzaType.IQ;
        } else {
            stanzaType = StanzaType.valueOf(attributeValue2.toUpperCase());
        }
        return new Open(attributeValue, parseInt, stanzaType);
    }
}

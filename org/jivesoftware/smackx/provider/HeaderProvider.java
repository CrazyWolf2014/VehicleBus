package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.packet.Header;
import org.xmlpull.v1.XmlPullParser;

public class HeaderProvider implements PacketExtensionProvider {
    public PacketExtension parseExtension(XmlPullParser xmlPullParser) throws Exception {
        String str = null;
        String attributeValue = xmlPullParser.getAttributeValue(null, "name");
        xmlPullParser.next();
        if (xmlPullParser.getEventType() == 4) {
            str = xmlPullParser.getText();
        }
        while (xmlPullParser.getEventType() != 3) {
            xmlPullParser.next();
        }
        return new Header(attributeValue, str);
    }
}

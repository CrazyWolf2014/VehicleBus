package org.jivesoftware.smackx.provider;

import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.packet.MessageEvent;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xmlpull.v1.XmlPullParser;

public class MultipleAddressesProvider implements PacketExtensionProvider {
    public PacketExtension parseExtension(XmlPullParser xmlPullParser) throws Exception {
        Object obj = null;
        PacketExtension multipleAddresses = new MultipleAddresses();
        while (obj == null) {
            Object obj2;
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals("address")) {
                    multipleAddresses.addAddress(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, SharedPref.TYPE), xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "jid"), xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "node"), xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "desc"), "true".equals(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, MessageEvent.DELIVERED)), xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "uri"));
                    obj2 = obj;
                }
                obj2 = obj;
            } else {
                if (next == 3 && xmlPullParser.getName().equals(multipleAddresses.getElementName())) {
                    obj2 = 1;
                }
                obj2 = obj;
            }
            obj = obj2;
        }
        return multipleAddresses;
    }
}

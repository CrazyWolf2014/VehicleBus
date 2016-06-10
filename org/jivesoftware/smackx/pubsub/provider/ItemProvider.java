package org.jivesoftware.smackx.pubsub.provider;

import com.tencent.mm.sdk.platformtools.LocaleUtil;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.PayloadItem;
import org.jivesoftware.smackx.pubsub.SimplePayload;
import org.xmlpull.v1.XmlPullParser;

public class ItemProvider implements PacketExtensionProvider {
    public PacketExtension parseExtension(XmlPullParser xmlPullParser) throws Exception {
        String attributeValue = xmlPullParser.getAttributeValue(null, LocaleUtil.INDONESIAN);
        String attributeValue2 = xmlPullParser.getAttributeValue(null, "node");
        String name = xmlPullParser.getName();
        int next = xmlPullParser.next();
        if (next == 3) {
            return new Item(attributeValue, attributeValue2);
        }
        String name2 = xmlPullParser.getName();
        String namespace = xmlPullParser.getNamespace();
        if (ProviderManager.getInstance().getExtensionProvider(name2, namespace) != null) {
            return new PayloadItem(attributeValue, attributeValue2, PacketParserUtils.parsePacketExtension(name2, namespace, xmlPullParser));
        }
        StringBuilder stringBuilder = new StringBuilder();
        int i = next;
        Object obj = null;
        while (obj == null) {
            if (i == 3 && xmlPullParser.getName().equals(name)) {
                obj = 1;
            } else if (xmlPullParser.getEventType() == 2) {
                stringBuilder.append("<").append(xmlPullParser.getName());
                if (xmlPullParser.getName().equals(name2) && !XmlPullParser.NO_NAMESPACE.equals(namespace)) {
                    stringBuilder.append(" xmlns=\"").append(namespace).append("\"");
                }
                int attributeCount = xmlPullParser.getAttributeCount();
                for (i = 0; i < attributeCount; i++) {
                    stringBuilder.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(xmlPullParser.getAttributeName(i)).append("=\"").append(xmlPullParser.getAttributeValue(i)).append("\"");
                }
                if (xmlPullParser.isEmptyElementTag()) {
                    stringBuilder.append("/>");
                    next = 1;
                } else {
                    stringBuilder.append(">");
                }
            } else if (xmlPullParser.getEventType() == 3) {
                if (obj != null) {
                    obj = null;
                } else {
                    stringBuilder.append("</").append(xmlPullParser.getName()).append(">");
                }
            } else if (xmlPullParser.getEventType() == 4) {
                stringBuilder.append(xmlPullParser.getText());
            }
            i = xmlPullParser.next();
        }
        return new PayloadItem(attributeValue, attributeValue2, new SimplePayload(name2, namespace, stringBuilder.toString()));
    }
}

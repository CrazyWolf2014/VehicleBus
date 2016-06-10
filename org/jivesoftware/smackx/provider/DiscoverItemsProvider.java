package org.jivesoftware.smackx.provider;

import com.ifoer.entity.Constant;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;
import org.xmlpull.v1.XmlPullParser;

public class DiscoverItemsProvider implements IQProvider {
    public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
        IQ discoverItems = new DiscoverItems();
        Object obj = null;
        String str = XmlPullParser.NO_NAMESPACE;
        String str2 = XmlPullParser.NO_NAMESPACE;
        String str3 = XmlPullParser.NO_NAMESPACE;
        String str4 = XmlPullParser.NO_NAMESPACE;
        discoverItems.setNode(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "node"));
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2 && "item".equals(xmlPullParser.getName())) {
                str = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "jid");
                str2 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "name");
                str4 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "node");
                str3 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, Constant.ACTION);
            } else if (next == 3 && "item".equals(xmlPullParser.getName())) {
                Item item = new Item(str);
                item.setName(str2);
                item.setNode(str4);
                item.setAction(str3);
                discoverItems.addItem(item);
            } else if (next == 3 && "query".equals(xmlPullParser.getName())) {
                obj = 1;
            }
        }
        return discoverItems;
    }
}

package org.jivesoftware.smackx.provider;

import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverInfo.Identity;
import org.xmlpull.v1.XmlPullParser;

public class DiscoverInfoProvider implements IQProvider {
    public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
        IQ discoverInfo = new DiscoverInfo();
        Object obj = null;
        String str = XmlPullParser.NO_NAMESPACE;
        String str2 = XmlPullParser.NO_NAMESPACE;
        String str3 = XmlPullParser.NO_NAMESPACE;
        String str4 = XmlPullParser.NO_NAMESPACE;
        String str5 = XmlPullParser.NO_NAMESPACE;
        discoverInfo.setNode(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "node"));
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals("identity")) {
                    str = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "category");
                    str2 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "name");
                    str3 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, SharedPref.TYPE);
                    str5 = xmlPullParser.getAttributeValue(xmlPullParser.getNamespace("xml"), "lang");
                } else if (xmlPullParser.getName().equals("feature")) {
                    str4 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "var");
                } else {
                    discoverInfo.addExtension(PacketParserUtils.parsePacketExtension(xmlPullParser.getName(), xmlPullParser.getNamespace(), xmlPullParser));
                }
            } else if (next == 3) {
                if (xmlPullParser.getName().equals("identity")) {
                    Identity identity = new Identity(str, str2, str3);
                    if (str5 != null) {
                        identity.setLanguage(str5);
                    }
                    discoverInfo.addIdentity(identity);
                }
                if (xmlPullParser.getName().equals("feature")) {
                    discoverInfo.addFeature(str4);
                }
                if (xmlPullParser.getName().equals("query")) {
                    obj = 1;
                }
            }
        }
        return discoverInfo;
    }
}

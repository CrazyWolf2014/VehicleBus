package org.jivesoftware.smack.provider;

import com.ifoer.entity.Constant;
import com.launch.service.BundleBuilder;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Privacy;
import org.jivesoftware.smack.packet.PrivacyItem;
import org.xmlpull.v1.XmlPullParser;

public class PrivacyProvider implements IQProvider {
    public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
        IQ privacy = new Privacy();
        privacy.addExtension(new DefaultPacketExtension(xmlPullParser.getName(), xmlPullParser.getNamespace()));
        boolean z = false;
        while (!z) {
            int next = xmlPullParser.next();
            if (next == 2) {
                String attributeValue;
                if (xmlPullParser.getName().equals("active")) {
                    attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "name");
                    if (attributeValue == null) {
                        privacy.setDeclineActiveList(true);
                    } else {
                        privacy.setActiveName(attributeValue);
                    }
                } else if (xmlPullParser.getName().equals("default")) {
                    attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "name");
                    if (attributeValue == null) {
                        privacy.setDeclineDefaultList(true);
                    } else {
                        privacy.setDefaultName(attributeValue);
                    }
                } else if (xmlPullParser.getName().equals("list")) {
                    parseList(xmlPullParser, privacy);
                }
            } else if (next == 3 && xmlPullParser.getName().equals("query")) {
                z = true;
            }
        }
        return privacy;
    }

    public void parseList(XmlPullParser xmlPullParser, Privacy privacy) throws Exception {
        Object obj = null;
        String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "name");
        List arrayList = new ArrayList();
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals("item")) {
                    arrayList.add(parseItem(xmlPullParser));
                }
            } else if (next == 3 && xmlPullParser.getName().equals("list")) {
                obj = 1;
            }
        }
        privacy.setPrivacyList(attributeValue, arrayList);
    }

    public PrivacyItem parseItem(XmlPullParser xmlPullParser) throws Exception {
        boolean z;
        String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, Constant.ACTION);
        String attributeValue2 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "order");
        String attributeValue3 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, SharedPref.TYPE);
        if ("allow".equalsIgnoreCase(attributeValue)) {
            z = true;
        } else if ("deny".equalsIgnoreCase(attributeValue)) {
            z = false;
        } else {
            z = true;
        }
        PrivacyItem privacyItem = new PrivacyItem(attributeValue3, z, Integer.parseInt(attributeValue2));
        privacyItem.setValue(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, SharedPref.VALUE));
        z = false;
        while (!z) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals("iq")) {
                    privacyItem.setFilterIQ(true);
                }
                if (xmlPullParser.getName().equals(BundleBuilder.AskFromMessage)) {
                    privacyItem.setFilterMessage(true);
                }
                if (xmlPullParser.getName().equals("presence-in")) {
                    privacyItem.setFilterPresence_in(true);
                }
                if (xmlPullParser.getName().equals("presence-out")) {
                    privacyItem.setFilterPresence_out(true);
                }
            } else if (next == 3 && xmlPullParser.getName().equals("item")) {
                z = true;
            }
        }
        return privacyItem;
    }
}

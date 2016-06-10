package org.jivesoftware.smackx.provider;

import java.text.ParseException;
import java.util.Date;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.DelayInformation;
import org.xmlpull.v1.XmlPullParser;

public class DelayInformationProvider implements PacketExtensionProvider {
    public PacketExtension parseExtension(XmlPullParser xmlPullParser) throws Exception {
        Date parseDate;
        String str = null;
        try {
            parseDate = StringUtils.parseDate(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "stamp"));
        } catch (ParseException e) {
            parseDate = null == null ? new Date(0) : null;
        }
        PacketExtension delayInformation = new DelayInformation(parseDate);
        delayInformation.setFrom(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, PrivacyRule.SUBSCRIPTION_FROM));
        String nextText = xmlPullParser.nextText();
        if (!XmlPullParser.NO_NAMESPACE.equals(nextText)) {
            str = nextText;
        }
        delayInformation.setReason(str);
        return delayInformation;
    }
}

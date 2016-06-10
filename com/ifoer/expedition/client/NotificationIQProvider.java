package com.ifoer.expedition.client;

import com.launch.service.BundleBuilder;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import com.tencent.mm.sdk.plugin.BaseProfile;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.OAuth;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import org.achartengine.ChartFactory;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class NotificationIQProvider implements IQProvider {
    public IQ parseIQ(XmlPullParser parser) throws Exception {
        NotificationIQ notification = new NotificationIQ();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (SharedPref.TYPE.equals(parser.getName())) {
                    notification.setTypes(parser.nextText());
                } else if ("serialno".equals(parser.getName())) {
                    notification.setSerialno(parser.nextText());
                } else if (BaseProfile.COL_USERNAME.equals(parser.getName())) {
                    notification.setUsername(parser.nextText());
                } else if (LocaleUtil.INDONESIAN.equals(parser.getName())) {
                    notification.setId(parser.nextText());
                } else if (OAuth.API_KEY.equals(parser.getName())) {
                    notification.setApiKey(parser.nextText());
                } else if (ChartFactory.TITLE.equals(parser.getName())) {
                    notification.setTitle(parser.nextText());
                } else if (BundleBuilder.AskFromMessage.equals(parser.getName())) {
                    notification.setMessage(parser.nextText());
                } else if ("uri".equals(parser.getName())) {
                    notification.setUri(parser.nextText());
                }
            } else if (eventType == 3 && "notification".equals(parser.getName())) {
                done = true;
            }
        }
        return notification;
    }
}

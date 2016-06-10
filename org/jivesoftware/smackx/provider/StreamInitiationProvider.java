package org.jivesoftware.smackx.provider;

import com.cnmobi.im.dto.MessageVo;
import com.cnmobi.im.dto.Msg;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import java.text.ParseException;
import java.util.Date;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.StreamInitiation;
import org.jivesoftware.smackx.packet.StreamInitiation.File;
import org.xmlpull.v1.XmlPullParser;

public class StreamInitiationProvider implements IQProvider {
    public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
        String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, LocaleUtil.INDONESIAN);
        String attributeValue2 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "mime-type");
        IQ streamInitiation = new StreamInitiation();
        DataFormProvider dataFormProvider = new DataFormProvider();
        String str = null;
        String str2 = null;
        Object obj = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        boolean z = false;
        DataForm dataForm = null;
        while (obj == null) {
            int next = xmlPullParser.next();
            String name = xmlPullParser.getName();
            String namespace = xmlPullParser.getNamespace();
            if (next == 2) {
                if (name.equals(MessageVo.TYPE_FILE)) {
                    str3 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "name");
                    namespace = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "size");
                    name = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "hash");
                    str4 = name;
                    str = namespace;
                    str2 = str3;
                    str3 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, Msg.DATE);
                } else if (name.equals("desc")) {
                    str5 = xmlPullParser.nextText();
                } else if (name.equals("range")) {
                    z = true;
                } else if (name.equals(GroupChatInvitation.ELEMENT_NAME) && namespace.equals(Form.NAMESPACE)) {
                    dataForm = (DataForm) dataFormProvider.parseExtension(xmlPullParser);
                }
            } else if (next == 3) {
                if (name.equals("si")) {
                    obj = 1;
                } else if (name.equals(MessageVo.TYPE_FILE)) {
                    long j = 0;
                    if (!(str == null || str.trim().length() == 0)) {
                        try {
                            j = Long.parseLong(str);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    Date date = new Date();
                    if (str3 != null) {
                        try {
                            date = StringUtils.parseXEP0082Date(str3);
                        } catch (ParseException e2) {
                        }
                    }
                    File file = new File(str2, j);
                    file.setHash(str4);
                    file.setDate(date);
                    file.setDesc(str5);
                    file.setRanged(z);
                    streamInitiation.setFile(file);
                }
            }
        }
        streamInitiation.setSesssionID(attributeValue);
        streamInitiation.setMimeType(attributeValue2);
        streamInitiation.setFeatureNegotiationForm(dataForm);
        return streamInitiation;
    }
}

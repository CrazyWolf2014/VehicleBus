package org.jivesoftware.smackx.workgroup.ext.forms;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.xmlpull.v1.XmlPullParser;

public class WorkgroupForm extends IQ {
    public static final String ELEMENT_NAME = "workgroup-form";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            IQ workgroupForm = new WorkgroupForm();
            Object obj = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2) {
                    workgroupForm.addExtension(PacketParserUtils.parsePacketExtension(xmlPullParser.getName(), xmlPullParser.getNamespace(), xmlPullParser));
                } else if (next == 3 && xmlPullParser.getName().equals(WorkgroupForm.ELEMENT_NAME)) {
                    obj = 1;
                }
            }
            return workgroupForm;
        }
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append(NAMESPACE).append("\">");
        stringBuilder.append(getExtensionsXML());
        stringBuilder.append("</").append(ELEMENT_NAME).append("> ");
        return stringBuilder.toString();
    }
}

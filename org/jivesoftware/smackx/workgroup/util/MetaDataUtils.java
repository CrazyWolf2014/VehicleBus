package org.jivesoftware.smackx.workgroup.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.workgroup.MetaData;
import org.jivesoftware.smackx.workgroup.settings.WorkgroupProperties;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class MetaDataUtils {
    public static Map<String, List<String>> parseMetaData(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        if (xmlPullParser.getEventType() != 2 || !xmlPullParser.getName().equals(MetaData.ELEMENT_NAME) || !xmlPullParser.getNamespace().equals(WorkgroupProperties.NAMESPACE)) {
            return Collections.emptyMap();
        }
        Map<String, List<String>> hashtable = new Hashtable();
        int nextTag = xmlPullParser.nextTag();
        while (true) {
            if (nextTag == 3 && xmlPullParser.getName().equals(MetaData.ELEMENT_NAME)) {
                return hashtable;
            }
            String attributeValue = xmlPullParser.getAttributeValue(0);
            String nextText = xmlPullParser.nextText();
            if (hashtable.containsKey(attributeValue)) {
                ((List) hashtable.get(attributeValue)).add(nextText);
            } else {
                List arrayList = new ArrayList();
                arrayList.add(nextText);
                hashtable.put(attributeValue, arrayList);
            }
            nextTag = xmlPullParser.nextTag();
        }
    }

    public static String serializeMetaData(Map<String, List<String>> map) {
        StringBuilder stringBuilder = new StringBuilder();
        if (map != null && map.size() > 0) {
            stringBuilder.append("<metadata xmlns=\"http://jivesoftware.com/protocol/workgroup\">");
            for (String str : map.keySet()) {
                for (String str2 : (List) map.get(str)) {
                    stringBuilder.append("<value name=\"").append(str).append("\">");
                    stringBuilder.append(StringUtils.escapeForXML(str2));
                    stringBuilder.append("</value>");
                }
            }
            stringBuilder.append("</metadata>");
        }
        return stringBuilder.toString();
    }
}

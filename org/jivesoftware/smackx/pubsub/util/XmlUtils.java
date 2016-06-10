package org.jivesoftware.smackx.pubsub.util;

import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class XmlUtils {
    public static void appendAttribute(StringBuilder stringBuilder, String str, String str2) {
        stringBuilder.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuilder.append(str);
        stringBuilder.append("='");
        stringBuilder.append(str2);
        stringBuilder.append("'");
    }
}

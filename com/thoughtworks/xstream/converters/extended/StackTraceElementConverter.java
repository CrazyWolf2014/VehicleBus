package com.thoughtworks.xstream.converters.extended;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;

public class StackTraceElementConverter extends AbstractSingleValueConverter {
    private static final StackTraceElementFactory FACTORY;
    private static final Pattern PATTERN;

    static {
        PATTERN = Pattern.compile("^(.+)\\.([^\\(]+)\\(([^:]*)(:(\\d+))?\\)$");
        FACTORY = new StackTraceElementFactory();
    }

    public boolean canConvert(Class type) {
        return StackTraceElement.class.equals(type);
    }

    public String toString(Object obj) {
        return super.toString(obj).replaceFirst(":\\?\\?\\?", XmlPullParser.NO_NAMESPACE);
    }

    public Object fromString(String str) {
        Matcher matcher = PATTERN.matcher(str);
        if (matcher.matches()) {
            String declaringClass = matcher.group(1);
            String methodName = matcher.group(2);
            String fileName = matcher.group(3);
            if (fileName.equals("Unknown Source")) {
                return FACTORY.unknownSourceElement(declaringClass, methodName);
            }
            if (fileName.equals("Native Method")) {
                return FACTORY.nativeMethodElement(declaringClass, methodName);
            }
            if (matcher.group(4) == null) {
                return FACTORY.element(declaringClass, methodName, fileName);
            }
            return FACTORY.element(declaringClass, methodName, fileName, Integer.parseInt(matcher.group(5)));
        }
        throw new ConversionException("Could not parse StackTraceElement : " + str);
    }
}

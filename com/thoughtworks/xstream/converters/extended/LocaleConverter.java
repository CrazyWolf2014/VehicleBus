package com.thoughtworks.xstream.converters.extended;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParser;

public class LocaleConverter extends AbstractSingleValueConverter {
    public boolean canConvert(Class type) {
        return type.equals(Locale.class);
    }

    public Object fromString(String str) {
        String language;
        String country;
        String variant;
        int[] underscorePositions = underscorePositions(str);
        if (underscorePositions[0] == -1) {
            language = str;
            country = XmlPullParser.NO_NAMESPACE;
            variant = XmlPullParser.NO_NAMESPACE;
        } else if (underscorePositions[1] == -1) {
            language = str.substring(0, underscorePositions[0]);
            country = str.substring(underscorePositions[0] + 1);
            variant = XmlPullParser.NO_NAMESPACE;
        } else {
            language = str.substring(0, underscorePositions[0]);
            country = str.substring(underscorePositions[0] + 1, underscorePositions[1]);
            variant = str.substring(underscorePositions[1] + 1);
        }
        return new Locale(language, country, variant);
    }

    private int[] underscorePositions(String in) {
        int[] result = new int[2];
        int i = 0;
        while (i < result.length) {
            result[i] = in.indexOf(95, (i == 0 ? 0 : result[i - 1]) + 1);
            i++;
        }
        return result;
    }
}

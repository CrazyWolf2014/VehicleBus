package org.jivesoftware.smackx.workgroup.util;

import com.tencent.mm.sdk.platformtools.Util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.achartengine.chart.TimeChart;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

public final class ModelUtil {
    private ModelUtil() {
    }

    public static final boolean areEqual(Object obj, Object obj2) {
        if (obj == obj2) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        return obj.equals(obj2);
    }

    public static final boolean areBooleansEqual(Boolean bool, Boolean bool2) {
        return (bool == Boolean.TRUE && bool2 == Boolean.TRUE) || !(bool == Boolean.TRUE || bool2 == Boolean.TRUE);
    }

    public static final boolean areDifferent(Object obj, Object obj2) {
        return !areEqual(obj, obj2);
    }

    public static final boolean areBooleansDifferent(Boolean bool, Boolean bool2) {
        return !areBooleansEqual(bool, bool2);
    }

    public static final boolean hasNonNullElement(Object[] objArr) {
        if (objArr == null) {
            return false;
        }
        for (Object obj : objArr) {
            if (obj != null) {
                return true;
            }
        }
        return false;
    }

    public static final String concat(String[] strArr) {
        return concat(strArr, MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
    }

    public static final String concat(String[] strArr, String str) {
        if (strArr == null) {
            return XmlPullParser.NO_NAMESPACE;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String str2 : strArr) {
            if (str2 != null) {
                stringBuilder.append(str2).append(str);
            }
        }
        int length = stringBuilder.length();
        if (length > 0) {
            stringBuilder.setLength(length - 1);
        }
        return stringBuilder.toString();
    }

    public static final boolean hasLength(String str) {
        return str != null && str.length() > 0;
    }

    public static final String nullifyIfEmpty(String str) {
        return hasLength(str) ? str : null;
    }

    public static final String nullifyingToString(Object obj) {
        return obj != null ? nullifyIfEmpty(obj.toString()) : null;
    }

    public static boolean hasStringChanged(String str, String str2) {
        if (str == null && str2 == null) {
            return false;
        }
        if (str == null && str2 != null) {
            return true;
        }
        if ((str == null || str2 != null) && str.equals(str2)) {
            return false;
        }
        return true;
    }

    public static String getTimeFromLong(long j) {
        long j2 = j % TimeChart.DAY;
        long j3 = j2 / Util.MILLSECONDS_OF_HOUR;
        j2 %= Util.MILLSECONDS_OF_HOUR;
        long j4 = j2 / Util.MILLSECONDS_OF_MINUTE;
        j2 %= Util.MILLSECONDS_OF_MINUTE;
        long j5 = j2 / 1000;
        j2 %= 1000;
        StringBuilder stringBuilder = new StringBuilder();
        if (j3 > 0) {
            stringBuilder.append(j3 + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "h" + ", ");
        }
        if (j4 > 0) {
            stringBuilder.append(j4 + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "min" + ", ");
        }
        stringBuilder.append(j5 + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "sec");
        return stringBuilder.toString();
    }

    public static <T> List<T> iteratorAsList(Iterator<T> it) {
        List arrayList = new ArrayList(10);
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList;
    }

    public static <T> Iterator<T> reverseListIterator(ListIterator<T> listIterator) {
        return new ReverseListIterator(listIterator);
    }
}

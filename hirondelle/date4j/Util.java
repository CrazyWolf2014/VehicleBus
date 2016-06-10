package hirondelle.date4j;

import java.lang.reflect.Array;
import java.util.logging.Logger;

final class Util {
    private static final String SINGLE_QUOTE = "'";

    Util() {
    }

    private static void checkObjectIsArray(Object obj) {
        if (!obj.getClass().isArray()) {
            throw new IllegalArgumentException("Object is not an array.");
        }
    }

    static String getArrayAsString(Object obj) {
        if (obj == null) {
            return "null";
        }
        checkObjectIsArray(obj);
        StringBuilder stringBuilder = new StringBuilder("[");
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            Object obj2 = Array.get(obj, i);
            if (isNonNullArray(obj2)) {
                stringBuilder.append(getArrayAsString(obj2));
            } else {
                stringBuilder.append(obj2);
            }
            if (!isLastItem(i, length)) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    static Logger getLogger(Class<?> cls) {
        return Logger.getLogger(cls.getPackage().getName());
    }

    private static boolean isLastItem(int i, int i2) {
        return i == i2 + -1;
    }

    private static boolean isNonNullArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    static String quote(Object obj) {
        return SINGLE_QUOTE + String.valueOf(obj) + SINGLE_QUOTE;
    }

    static boolean textHasContent(String str) {
        return str != null && str.trim().length() > 0;
    }
}

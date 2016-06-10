package com.thoughtworks.xstream.core.util;

import java.util.HashMap;
import java.util.Map;

public final class Primitives {
    private static final Map BOX;
    private static final Map NAMED_PRIMITIVE;
    private static final Map REPRESENTING_CHAR;
    private static final Map UNBOX;

    static {
        BOX = new HashMap();
        UNBOX = new HashMap();
        NAMED_PRIMITIVE = new HashMap();
        REPRESENTING_CHAR = new HashMap();
        Class[][] boxing = new Class[9][];
        boxing[0] = new Class[]{Byte.TYPE, Byte.class};
        boxing[1] = new Class[]{Character.TYPE, Character.class};
        boxing[2] = new Class[]{Short.TYPE, Short.class};
        boxing[3] = new Class[]{Integer.TYPE, Integer.class};
        boxing[4] = new Class[]{Long.TYPE, Long.class};
        boxing[5] = new Class[]{Float.TYPE, Float.class};
        boxing[6] = new Class[]{Double.TYPE, Double.class};
        boxing[7] = new Class[]{Boolean.TYPE, Boolean.class};
        boxing[8] = new Class[]{Void.TYPE, Void.class};
        Character[] representingChars = new Character[]{new Character('B'), new Character('C'), new Character('S'), new Character('I'), new Character('J'), new Character('F'), new Character('D'), new Character('Z'), null};
        for (int i = 0; i < boxing.length; i++) {
            Class primitiveType = boxing[i][0];
            Class boxedType = boxing[i][1];
            BOX.put(primitiveType, boxedType);
            UNBOX.put(boxedType, primitiveType);
            NAMED_PRIMITIVE.put(primitiveType.getName(), primitiveType);
            REPRESENTING_CHAR.put(primitiveType, representingChars[i]);
        }
    }

    public static Class box(Class type) {
        return (Class) BOX.get(type);
    }

    public static Class unbox(Class type) {
        return (Class) UNBOX.get(type);
    }

    public static boolean isBoxed(Class type) {
        return UNBOX.containsKey(type);
    }

    public static Class primitiveType(String name) {
        return (Class) NAMED_PRIMITIVE.get(name);
    }

    public static char representingChar(Class type) {
        Character ch = (Character) REPRESENTING_CHAR.get(type);
        return ch == null ? '\u0000' : ch.charValue();
    }
}

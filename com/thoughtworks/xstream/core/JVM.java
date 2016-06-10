package com.thoughtworks.xstream.core;

import com.cnmobi.im.dto.MessageVo;
import com.thoughtworks.xstream.converters.reflection.FieldDictionary;
import com.thoughtworks.xstream.converters.reflection.ObjectAccessException;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.core.util.DependencyInjectionFactory;
import com.thoughtworks.xstream.core.util.PresortedMap;
import com.thoughtworks.xstream.core.util.PresortedSet;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessControlException;
import java.text.AttributedString;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class JVM implements Caching {
    private static final float DEFAULT_JAVA_VERSION = 1.4f;
    private static final boolean canAllocateWithUnsafe;
    private static final boolean canParseUTCDateFormat;
    private static final boolean isAWTAvailable;
    private static final boolean isSQLAvailable;
    private static final boolean isSwingAvailable;
    private static final float majorJavaVersion;
    private static final boolean optimizedTreeMapPutAll;
    private static final boolean optimizedTreeSetAddAll;
    private static final Class reflectionProviderType;
    private static final boolean reverseFieldOrder = false;
    private static final String vendor;
    private ReflectionProvider reflectionProvider;

    /* renamed from: com.thoughtworks.xstream.core.JVM.1 */
    static class C08911 implements Comparator {
        C08911() {
        }

        public int compare(Object o1, Object o2) {
            throw new RuntimeException();
        }
    }

    static class Broken {
        Broken() {
            throw new UnsupportedOperationException();
        }
    }

    static {
        boolean test;
        vendor = System.getProperty("java.vm.vendor");
        majorJavaVersion = getMajorJavaVersion();
        try {
            Class unsafeClass = Class.forName("sun.misc.Unsafe");
            Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Object unsafe = unsafeField.get(null);
            Method allocateInstance = unsafeClass.getDeclaredMethod("allocateInstance", new Class[]{Class.class});
            allocateInstance.setAccessible(true);
            test = allocateInstance.invoke(unsafe, new Object[]{Broken.class}) != null;
        } catch (Exception e) {
            test = false;
        } catch (Error e2) {
            test = false;
        }
        canAllocateWithUnsafe = test;
        Comparator comparator = new C08911();
        SortedMap map = new PresortedMap(comparator);
        map.put("one", null);
        map.put("two", null);
        try {
            new TreeMap(comparator).putAll(map);
            test = true;
        } catch (RuntimeException e3) {
            test = false;
        }
        optimizedTreeMapPutAll = test;
        SortedSet set = new PresortedSet(comparator);
        set.addAll(map.keySet());
        try {
            new TreeSet(comparator).addAll(set);
            test = true;
        } catch (RuntimeException e4) {
            test = false;
        }
        optimizedTreeSetAddAll = test;
        try {
            new SimpleDateFormat("z").parse("UTC");
            test = true;
        } catch (ParseException e5) {
            test = false;
        }
        canParseUTCDateFormat = test;
        isAWTAvailable = loadClassForName("java.awt.Color", false) != null;
        isSwingAvailable = loadClassForName("javax.swing.LookAndFeel", false) != null;
        isSQLAvailable = loadClassForName("java.sql.Date") != null;
        Class type = PureJavaReflectionProvider.class;
        if (canUseSun14ReflectionProvider()) {
            Class cls = loadClassForName("com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider");
            if (cls != null) {
                try {
                    DependencyInjectionFactory.newInstance(cls, null);
                    type = cls;
                } catch (ObjectAccessException e6) {
                }
            }
        }
        reflectionProviderType = type;
    }

    private static final float getMajorJavaVersion() {
        try {
            return isAndroid() ? 1.5f : Float.parseFloat(System.getProperty("java.specification.version"));
        } catch (NumberFormatException e) {
            return DEFAULT_JAVA_VERSION;
        }
    }

    public static boolean is14() {
        return majorJavaVersion >= DEFAULT_JAVA_VERSION;
    }

    public static boolean is15() {
        return majorJavaVersion >= 1.5f;
    }

    public static boolean is16() {
        return majorJavaVersion >= 1.6f;
    }

    public static boolean is17() {
        return majorJavaVersion >= 1.7f;
    }

    public static boolean is18() {
        return majorJavaVersion >= 1.8f;
    }

    private static boolean isIBM() {
        return vendor.indexOf("IBM") != -1;
    }

    private static boolean isAndroid() {
        return vendor.indexOf("Android") != -1;
    }

    public static Class loadClassForName(String name) {
        return loadClassForName(name, true);
    }

    public Class loadClass(String name) {
        return loadClassForName(name, true);
    }

    public static Class loadClassForName(String name, boolean initialize) {
        Class cls = null;
        try {
            cls = Class.forName(name, initialize, JVM.class.getClassLoader());
        } catch (LinkageError e) {
        } catch (ClassNotFoundException e2) {
        }
        return cls;
    }

    public Class loadClass(String name, boolean initialize) {
        return loadClassForName(name, initialize);
    }

    public static ReflectionProvider newReflectionProvider() {
        return (ReflectionProvider) DependencyInjectionFactory.newInstance(reflectionProviderType, null);
    }

    public static ReflectionProvider newReflectionProvider(FieldDictionary dictionary) {
        return (ReflectionProvider) DependencyInjectionFactory.newInstance(reflectionProviderType, new Object[]{dictionary});
    }

    public static Class getStaxInputFactory() throws ClassNotFoundException {
        if (!is16()) {
            return null;
        }
        if (isIBM()) {
            return Class.forName("com.ibm.xml.xlxp.api.stax.XMLInputFactoryImpl");
        }
        return Class.forName("com.sun.xml.internal.stream.XMLInputFactoryImpl");
    }

    public static Class getStaxOutputFactory() throws ClassNotFoundException {
        if (!is16()) {
            return null;
        }
        if (isIBM()) {
            return Class.forName("com.ibm.xml.xlxp.api.stax.XMLOutputFactoryImpl");
        }
        return Class.forName("com.sun.xml.internal.stream.XMLOutputFactoryImpl");
    }

    public synchronized ReflectionProvider bestReflectionProvider() {
        if (this.reflectionProvider == null) {
            String className = null;
            try {
                if (canUseSun14ReflectionProvider()) {
                    className = "com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider";
                }
                if (className != null) {
                    Class cls = loadClassForName(className);
                    if (cls != null) {
                        this.reflectionProvider = (ReflectionProvider) cls.newInstance();
                    }
                }
            } catch (InstantiationException e) {
            } catch (IllegalAccessException e2) {
            } catch (AccessControlException e3) {
            }
            if (this.reflectionProvider == null) {
                this.reflectionProvider = new PureJavaReflectionProvider();
            }
        }
        return this.reflectionProvider;
    }

    private static boolean canUseSun14ReflectionProvider() {
        return canAllocateWithUnsafe && is14();
    }

    public static boolean reverseFieldDefinition() {
        return false;
    }

    public static boolean isAWTAvailable() {
        return isAWTAvailable;
    }

    public boolean supportsAWT() {
        return isAWTAvailable;
    }

    public static boolean isSwingAvailable() {
        return isSwingAvailable;
    }

    public boolean supportsSwing() {
        return isSwingAvailable;
    }

    public static boolean isSQLAvailable() {
        return isSQLAvailable;
    }

    public boolean supportsSQL() {
        return isSQLAvailable;
    }

    public static boolean hasOptimizedTreeSetAddAll() {
        return optimizedTreeSetAddAll;
    }

    public static boolean hasOptimizedTreeMapPutAll() {
        return optimizedTreeMapPutAll;
    }

    public static boolean canParseUTCDateFormat() {
        return canParseUTCDateFormat;
    }

    public void flushCache() {
    }

    public static void main(String[] args) {
        int i;
        String staxInputFactory;
        String staxOutputFactory;
        boolean reverse = false;
        Field[] fields = AttributedString.class.getDeclaredFields();
        for (i = 0; i < fields.length; i++) {
            if (fields[i].getName().equals(MessageVo.TYPE_TEXT)) {
                if (i > 3) {
                    reverse = true;
                } else {
                    reverse = false;
                }
                if (reverse) {
                    fields = JVM.class.getDeclaredFields();
                    i = 0;
                    while (i < fields.length) {
                        if (fields[i].getName().equals("reflectionProvider")) {
                            i++;
                        } else if (i <= 2) {
                            reverse = true;
                        } else {
                            reverse = false;
                        }
                    }
                }
                staxInputFactory = null;
                staxInputFactory = getStaxInputFactory().getName();
                staxOutputFactory = null;
                staxOutputFactory = getStaxOutputFactory().getName();
                System.out.println("XStream JVM diagnostics");
                System.out.println("java.specification.version: " + System.getProperty("java.specification.version"));
                System.out.println("java.specification.vendor: " + System.getProperty("java.specification.vendor"));
                System.out.println("java.specification.name: " + System.getProperty("java.specification.name"));
                System.out.println("java.vm.vendor: " + vendor);
                System.out.println("java.vendor: " + System.getProperty("java.vendor"));
                System.out.println("java.vm.name: " + System.getProperty("java.vm.name"));
                System.out.println("Version: " + majorJavaVersion);
                System.out.println("XStream support for enhanced Mode: " + canUseSun14ReflectionProvider());
                System.out.println("Supports AWT: " + isAWTAvailable());
                System.out.println("Supports Swing: " + isSwingAvailable());
                System.out.println("Supports SQL: " + isSQLAvailable());
                System.out.println("Standard StAX XMLInputFactory: " + staxInputFactory);
                System.out.println("Standard StAX XMLOutputFactory: " + staxOutputFactory);
                System.out.println("Optimized TreeSet.addAll: " + hasOptimizedTreeSetAddAll());
                System.out.println("Optimized TreeMap.putAll: " + hasOptimizedTreeMapPutAll());
                System.out.println("Can parse UTC date format: " + canParseUTCDateFormat());
                System.out.println("Reverse field order detected (only if JVM class itself has been compiled): " + reverse);
            }
        }
        if (reverse) {
            fields = JVM.class.getDeclaredFields();
            i = 0;
            while (i < fields.length) {
                if (fields[i].getName().equals("reflectionProvider")) {
                    i++;
                } else if (i <= 2) {
                    reverse = false;
                } else {
                    reverse = true;
                }
            }
        }
        staxInputFactory = null;
        try {
            staxInputFactory = getStaxInputFactory().getName();
        } catch (ClassNotFoundException e) {
            staxInputFactory = e.getMessage();
        } catch (NullPointerException e2) {
        }
        staxOutputFactory = null;
        try {
            staxOutputFactory = getStaxOutputFactory().getName();
        } catch (ClassNotFoundException e3) {
            staxOutputFactory = e3.getMessage();
        } catch (NullPointerException e4) {
        }
        System.out.println("XStream JVM diagnostics");
        System.out.println("java.specification.version: " + System.getProperty("java.specification.version"));
        System.out.println("java.specification.vendor: " + System.getProperty("java.specification.vendor"));
        System.out.println("java.specification.name: " + System.getProperty("java.specification.name"));
        System.out.println("java.vm.vendor: " + vendor);
        System.out.println("java.vendor: " + System.getProperty("java.vendor"));
        System.out.println("java.vm.name: " + System.getProperty("java.vm.name"));
        System.out.println("Version: " + majorJavaVersion);
        System.out.println("XStream support for enhanced Mode: " + canUseSun14ReflectionProvider());
        System.out.println("Supports AWT: " + isAWTAvailable());
        System.out.println("Supports Swing: " + isSwingAvailable());
        System.out.println("Supports SQL: " + isSQLAvailable());
        System.out.println("Standard StAX XMLInputFactory: " + staxInputFactory);
        System.out.println("Standard StAX XMLOutputFactory: " + staxOutputFactory);
        System.out.println("Optimized TreeSet.addAll: " + hasOptimizedTreeSetAddAll());
        System.out.println("Optimized TreeMap.putAll: " + hasOptimizedTreeMapPutAll());
        System.out.println("Can parse UTC date format: " + canParseUTCDateFormat());
        System.out.println("Reverse field order detected (only if JVM class itself has been compiled): " + reverse);
    }
}

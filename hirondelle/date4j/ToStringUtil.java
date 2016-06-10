package hirondelle.date4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.xbill.DNS.WKSRecord.Service;

final class ToStringUtil {
    private static String HIDDEN_PASSWORD_VALUE = null;
    private static final String NEW_LINE;
    private static Pattern PASSWORD_PATTERN = null;
    private static final String fAVOID_CIRCULAR_REFERENCES = "[circular reference]";
    private static final String fCLONE = "clone";
    private static final String fGET = "get";
    private static final String fGET_CLASS = "getClass";
    private static final String fHASH_CODE = "hashCode";
    private static final String fINDENT = "";
    private static final Logger fLogger;
    private static final Object[] fNO_ARGS;
    private static final Class[] fNO_PARAMS;
    private static final String fTO_STRING = "toString";

    private static final class Ping {
        private Pong fPong;

        private Ping() {
        }

        public Integer getId() {
            return new Integer(Service.NTP);
        }

        public Pong getPong() {
            return this.fPong;
        }

        public String getUserPassword() {
            return "blah";
        }

        public void setPong(Pong pong) {
            this.fPong = pong;
        }

        public String toString() {
            return ToStringUtil.getText(this);
        }
    }

    private static final class Pong {
        private Ping fPing;

        private Pong() {
        }

        public Ping getPing() {
            return this.fPing;
        }

        public void setPing(Ping ping) {
            this.fPing = ping;
        }

        public String toString() {
            return ToStringUtil.getTextAvoidCyclicRefs(this, Ping.class, "getId");
        }
    }

    static {
        fNO_ARGS = new Object[0];
        fNO_PARAMS = new Class[0];
        fLogger = Util.getLogger(ToStringUtil.class);
        NEW_LINE = System.getProperty("line.separator");
        PASSWORD_PATTERN = Pattern.compile("password", 2);
        HIDDEN_PASSWORD_VALUE = "****";
    }

    private ToStringUtil() {
    }

    private static void addEndLine(StringBuilder stringBuilder) {
        stringBuilder.append("}");
        stringBuilder.append(NEW_LINE);
    }

    private static void addLineForGetXXXMethod(Object obj, Method method, StringBuilder stringBuilder, Class cls, String str) {
        stringBuilder.append(fINDENT);
        stringBuilder.append(getMethodNameMinusGet(method));
        stringBuilder.append(": ");
        Object methodReturnValue = getMethodReturnValue(obj, method);
        if (methodReturnValue != null && methodReturnValue.getClass().isArray()) {
            stringBuilder.append(Util.getArrayAsString(methodReturnValue));
        } else if (cls == null) {
            stringBuilder.append(methodReturnValue);
        } else if (cls == methodReturnValue.getClass()) {
            Method methodFromName = getMethodFromName(cls, str);
            if (isContributingMethod(methodFromName, cls)) {
                stringBuilder.append(getMethodReturnValue(methodReturnValue, methodFromName));
            } else {
                stringBuilder.append(fAVOID_CIRCULAR_REFERENCES);
            }
        }
        stringBuilder.append(NEW_LINE);
    }

    private static void addStartLine(Object obj, StringBuilder stringBuilder) {
        stringBuilder.append(obj.getClass().getName());
        stringBuilder.append(" {");
        stringBuilder.append(NEW_LINE);
    }

    private static Object dontShowPasswords(Object obj, Method method) {
        return PASSWORD_PATTERN.matcher(method.getName()).find() ? HIDDEN_PASSWORD_VALUE : obj;
    }

    private static Method getMethodFromName(Class cls, String str) {
        Method method = null;
        try {
            method = cls.getMethod(str, fNO_PARAMS);
        } catch (NoSuchMethodException e) {
            vomit(cls, str);
        }
        return method;
    }

    private static String getMethodNameMinusGet(Method method) {
        String name = method.getName();
        return name.startsWith(fGET) ? name.substring(fGET.length()) : name;
    }

    private static Object getMethodReturnValue(Object obj, Method method) {
        Object obj2 = null;
        try {
            obj2 = method.invoke(obj, fNO_ARGS);
        } catch (IllegalAccessException e) {
            vomit(obj, method);
        } catch (InvocationTargetException e2) {
            vomit(obj, method);
        }
        return dontShowPasswords(obj2, method);
    }

    static String getText(Object obj) {
        return getTextAvoidCyclicRefs(obj, null, null);
    }

    static String getTextAvoidCyclicRefs(Object obj, Class cls, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        addStartLine(obj, stringBuilder);
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (isContributingMethod(method, obj.getClass())) {
                addLineForGetXXXMethod(obj, method, stringBuilder, cls, str);
            }
        }
        addEndLine(stringBuilder);
        return stringBuilder.toString();
    }

    private static boolean isContributingMethod(Method method, Class cls) {
        boolean isPublic = Modifier.isPublic(method.getModifiers());
        boolean z = method.getParameterTypes().length == 0;
        boolean z2 = method.getReturnType() != Void.TYPE;
        boolean z3 = method.getReturnType() == cls;
        boolean z4 = method.getName().equals(fCLONE) || method.getName().equals(fGET_CLASS) || method.getName().equals(fHASH_CODE) || method.getName().equals(fTO_STRING);
        return isPublic && z && z2 && !z4 && !z3;
    }

    public static void main(String... strArr) {
        List arrayList = new ArrayList();
        arrayList.add("blah");
        arrayList.add("blah");
        arrayList.add("blah");
        System.out.println(getText(arrayList));
        System.out.println(getText(new StringTokenizer("This is the end.")));
        Ping ping = new Ping();
        Pong pong = new Pong();
        ping.setPong(pong);
        pong.setPing(ping);
        System.out.println(ping);
        System.out.println(pong);
    }

    private static void vomit(Class cls, String str) {
        fLogger.severe("Reflection fails to get no-arg method named: " + Util.quote(str) + " for class: " + cls.getName());
    }

    private static void vomit(Object obj, Method method) {
        fLogger.severe("Cannot get return value using reflection. Class: " + obj.getClass().getName() + " Method: " + method.getName());
    }
}

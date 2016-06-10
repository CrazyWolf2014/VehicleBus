package com.thoughtworks.xstream.io.json;

import com.ifoer.util.MyHttpException;
import com.thoughtworks.xstream.core.util.FastStack;
import com.thoughtworks.xstream.io.AbstractWriter;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.mapper.Mapper.Null;
import java.io.Externalizable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public abstract class AbstractJsonWriter extends AbstractWriter {
    public static final int DROP_ROOT_MODE = 1;
    public static final int EXPLICIT_MODE = 4;
    public static final int IEEE_754_MODE = 8;
    private static final Set NUMBER_TYPES;
    private static final int STATE_END_ATTRIBUTES = 32;
    private static final int STATE_END_ELEMENTS = 256;
    private static final int STATE_END_OBJECT = 2;
    private static final int STATE_NEXT_ATTRIBUTE = 16;
    private static final int STATE_NEXT_ELEMENT = 128;
    private static final int STATE_ROOT = 1;
    private static final int STATE_SET_VALUE = 512;
    private static final int STATE_START_ATTRIBUTES = 8;
    private static final int STATE_START_ELEMENTS = 64;
    private static final int STATE_START_OBJECT = 4;
    public static final int STRICT_MODE = 2;
    private int expectedStates;
    private int mode;
    private FastStack stack;

    private static class IllegalWriterStateException extends IllegalStateException {
        public IllegalWriterStateException(int from, int to, String element) {
            super("Cannot turn from state " + getState(from) + " into state " + getState(to) + (element == null ? XmlPullParser.NO_NAMESPACE : " for property " + element));
        }

        private static String getState(int state) {
            switch (state) {
                case AbstractJsonWriter.STATE_ROOT /*1*/:
                    return "ROOT";
                case AbstractJsonWriter.STRICT_MODE /*2*/:
                    return "END_OBJECT";
                case AbstractJsonWriter.STATE_START_OBJECT /*4*/:
                    return "START_OBJECT";
                case AbstractJsonWriter.STATE_START_ATTRIBUTES /*8*/:
                    return "START_ATTRIBUTES";
                case AbstractJsonWriter.STATE_NEXT_ATTRIBUTE /*16*/:
                    return "NEXT_ATTRIBUTE";
                case AbstractJsonWriter.STATE_END_ATTRIBUTES /*32*/:
                    return "END_ATTRIBUTES";
                case AbstractJsonWriter.STATE_START_ELEMENTS /*64*/:
                    return "START_ELEMENTS";
                case AbstractJsonWriter.STATE_NEXT_ELEMENT /*128*/:
                    return "NEXT_ELEMENT";
                case AbstractJsonWriter.STATE_END_ELEMENTS /*256*/:
                    return "END_ELEMENTS";
                case AbstractJsonWriter.STATE_SET_VALUE /*512*/:
                    return "SET_VALUE";
                default:
                    throw new IllegalArgumentException("Unknown state provided: " + state + ", cannot create message for IllegalWriterStateException");
            }
        }
    }

    private static class StackElement {
        int status;
        final Class type;

        public StackElement(Class type, int status) {
            this.type = type;
            this.status = status;
        }
    }

    public static class Type {
        public static Type BOOLEAN;
        public static Type NULL;
        public static Type NUMBER;
        public static Type STRING;

        static {
            NULL = new Type();
            STRING = new Type();
            NUMBER = new Type();
            BOOLEAN = new Type();
        }
    }

    protected abstract void addLabel(String str);

    protected abstract void addValue(String str, Type type);

    protected abstract void endArray();

    protected abstract void endObject();

    protected abstract void nextElement();

    protected abstract void startArray();

    protected abstract void startObject();

    static {
        NUMBER_TYPES = new HashSet(Arrays.asList(new Class[]{Byte.TYPE, Byte.class, Short.TYPE, Short.class, Integer.TYPE, Integer.class, Long.TYPE, Long.class, Float.TYPE, Float.class, Double.TYPE, Double.class, BigInteger.class, BigDecimal.class}));
    }

    public AbstractJsonWriter() {
        this(new NoNameCoder());
    }

    public AbstractJsonWriter(int mode) {
        this(mode, new NoNameCoder());
    }

    public AbstractJsonWriter(NameCoder nameCoder) {
        this(0, nameCoder);
    }

    public AbstractJsonWriter(int mode, NameCoder nameCoder) {
        super(nameCoder);
        this.stack = new FastStack(STATE_NEXT_ATTRIBUTE);
        if ((mode & STATE_START_OBJECT) > 0) {
            mode = STATE_START_OBJECT;
        }
        this.mode = mode;
        this.stack.push(new StackElement(null, STATE_ROOT));
        this.expectedStates = STATE_START_OBJECT;
    }

    public void startNode(String name, Class clazz) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        this.stack.push(new StackElement(clazz, ((StackElement) this.stack.peek()).status));
        handleCheckedStateTransition(STATE_START_OBJECT, name, null);
        this.expectedStates = MyHttpException.ERROR_NOEXIST_SERIAL_NUB;
    }

    public void startNode(String name) {
        startNode(name, null);
    }

    public void addAttribute(String name, String value) {
        handleCheckedStateTransition(STATE_NEXT_ATTRIBUTE, name, value);
        this.expectedStates = MyHttpException.ERROR_NOEXIST_SERIAL_NUB;
    }

    public void setValue(String text) {
        Class type = ((StackElement) this.stack.peek()).type;
        if ((type == Character.class || type == Character.TYPE) && XmlPullParser.NO_NAMESPACE.equals(text)) {
            text = "\u0000";
        }
        handleCheckedStateTransition(STATE_SET_VALUE, null, text);
        this.expectedStates = Service.PWDGEN;
    }

    public void endNode() {
        int size = this.stack.size();
        int nextState = size > STRICT_MODE ? STATE_NEXT_ELEMENT : STATE_ROOT;
        handleCheckedStateTransition(nextState, null, null);
        this.stack.pop();
        ((StackElement) this.stack.peek()).status = nextState;
        this.expectedStates = STATE_START_OBJECT;
        if (size > STRICT_MODE) {
            this.expectedStates |= Service.PWDGEN;
        }
    }

    private void handleCheckedStateTransition(int requiredState, String elementToAdd, String valueToAdd) {
        StackElement stackElement = (StackElement) this.stack.peek();
        if ((this.expectedStates & requiredState) == 0) {
            throw new IllegalWriterStateException(stackElement.status, requiredState, elementToAdd);
        }
        stackElement.status = handleStateTransition(stackElement.status, requiredState, elementToAdd, valueToAdd);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int handleStateTransition(int r12, int r13, java.lang.String r14, java.lang.String r15) {
        /*
        r11 = this;
        r7 = r11.stack;
        r6 = r7.size();
        r7 = r11.stack;
        r7 = r7.peek();
        r7 = (com.thoughtworks.xstream.io.json.AbstractJsonWriter.StackElement) r7;
        r0 = r7.type;
        r7 = 1;
        if (r6 <= r7) goto L_0x0039;
    L_0x0013:
        r7 = r11.isArray(r0);
        if (r7 == 0) goto L_0x0039;
    L_0x0019:
        r1 = 1;
    L_0x001a:
        r7 = 1;
        if (r6 <= r7) goto L_0x003b;
    L_0x001d:
        r7 = r11.stack;
        r8 = r6 + -2;
        r7 = r7.get(r8);
        r7 = (com.thoughtworks.xstream.io.json.AbstractJsonWriter.StackElement) r7;
        r7 = r7.type;
        r7 = r11.isArray(r7);
        if (r7 == 0) goto L_0x003b;
    L_0x002f:
        r2 = 1;
    L_0x0030:
        switch(r12) {
            case 1: goto L_0x003d;
            case 2: goto L_0x004f;
            case 4: goto L_0x007e;
            case 8: goto L_0x021f;
            case 16: goto L_0x0222;
            case 32: goto L_0x0328;
            case 64: goto L_0x014a;
            case 128: goto L_0x00e9;
            case 256: goto L_0x0205;
            case 512: goto L_0x034f;
            default: goto L_0x0033;
        };
    L_0x0033:
        r7 = new com.thoughtworks.xstream.io.json.AbstractJsonWriter$IllegalWriterStateException;
        r7.<init>(r12, r13, r14);
        throw r7;
    L_0x0039:
        r1 = 0;
        goto L_0x001a;
    L_0x003b:
        r2 = 0;
        goto L_0x0030;
    L_0x003d:
        r7 = 4;
        if (r13 != r7) goto L_0x0049;
    L_0x0040:
        r7 = 64;
        r8 = 4;
        r9 = 0;
        r12 = r11.handleStateTransition(r7, r8, r14, r9);
    L_0x0048:
        return r13;
    L_0x0049:
        r7 = new com.thoughtworks.xstream.io.json.AbstractJsonWriter$IllegalWriterStateException;
        r7.<init>(r12, r13, r14);
        throw r7;
    L_0x004f:
        switch(r13) {
            case 1: goto L_0x006b;
            case 4: goto L_0x0058;
            case 128: goto L_0x0067;
            default: goto L_0x0052;
        };
    L_0x0052:
        r7 = new com.thoughtworks.xstream.io.json.AbstractJsonWriter$IllegalWriterStateException;
        r7.<init>(r12, r13, r14);
        throw r7;
    L_0x0058:
        r7 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        r7 = 4;
        r8 = 0;
        r12 = r11.handleStateTransition(r12, r7, r14, r8);
        goto L_0x0048;
    L_0x0067:
        r11.nextElement();
        goto L_0x0048;
    L_0x006b:
        r7 = r11.mode;
        r7 = r7 & 1;
        if (r7 == 0) goto L_0x0074;
    L_0x0071:
        r7 = 2;
        if (r6 <= r7) goto L_0x0048;
    L_0x0074:
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 != 0) goto L_0x0048;
    L_0x007a:
        r11.endObject();
        goto L_0x0048;
    L_0x007e:
        switch(r13) {
            case 1: goto L_0x0087;
            case 4: goto L_0x0087;
            case 8: goto L_0x00c3;
            case 16: goto L_0x00ce;
            case 128: goto L_0x0087;
            case 512: goto L_0x0087;
            default: goto L_0x0081;
        };
    L_0x0081:
        r7 = new com.thoughtworks.xstream.io.json.AbstractJsonWriter$IllegalWriterStateException;
        r7.<init>(r12, r13, r14);
        throw r7;
    L_0x0087:
        if (r2 == 0) goto L_0x008f;
    L_0x0089:
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 == 0) goto L_0x009f;
    L_0x008f:
        r7 = 8;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        r7 = 32;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
    L_0x009f:
        r12 = 64;
        switch(r13) {
            case 1: goto L_0x00a5;
            case 4: goto L_0x00bc;
            case 128: goto L_0x00a5;
            case 512: goto L_0x00b4;
            default: goto L_0x00a4;
        };
    L_0x00a4:
        goto L_0x0048;
    L_0x00a5:
        r7 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        r7 = 0;
        r8 = 0;
        r12 = r11.handleStateTransition(r12, r13, r7, r8);
        goto L_0x0048;
    L_0x00b4:
        r7 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        r8 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r15);
        goto L_0x0048;
    L_0x00bc:
        r7 = 4;
        r8 = 0;
        r12 = r11.handleStateTransition(r12, r7, r14, r8);
        goto L_0x0048;
    L_0x00c3:
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 == 0) goto L_0x0048;
    L_0x00c9:
        r11.startArray();
        goto L_0x0048;
    L_0x00ce:
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 != 0) goto L_0x00d6;
    L_0x00d4:
        if (r1 != 0) goto L_0x00e6;
    L_0x00d6:
        r7 = 8;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        r7 = 16;
        r12 = r11.handleStateTransition(r12, r7, r14, r15);
        goto L_0x0048;
    L_0x00e6:
        r13 = 4;
        goto L_0x0048;
    L_0x00e9:
        switch(r13) {
            case 1: goto L_0x0111;
            case 2: goto L_0x0121;
            case 4: goto L_0x00f2;
            case 128: goto L_0x0121;
            case 256: goto L_0x013d;
            default: goto L_0x00ec;
        };
    L_0x00ec:
        r7 = new com.thoughtworks.xstream.io.json.AbstractJsonWriter$IllegalWriterStateException;
        r7.<init>(r12, r13, r14);
        throw r7;
    L_0x00f2:
        r11.nextElement();
        if (r2 != 0) goto L_0x014a;
    L_0x00f7:
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 != 0) goto L_0x014a;
    L_0x00fd:
        r7 = r11.encodeNode(r14);
        r11.addLabel(r7);
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 != 0) goto L_0x0048;
    L_0x010a:
        if (r1 == 0) goto L_0x0048;
    L_0x010c:
        r11.startArray();
        goto L_0x0048;
    L_0x0111:
        r7 = 2;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        r7 = 1;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        goto L_0x0048;
    L_0x0121:
        r7 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        r7 = 2;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 != 0) goto L_0x0048;
    L_0x0136:
        if (r1 != 0) goto L_0x0048;
    L_0x0138:
        r11.endObject();
        goto L_0x0048;
    L_0x013d:
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 != 0) goto L_0x0048;
    L_0x0143:
        if (r1 == 0) goto L_0x0048;
    L_0x0145:
        r11.endArray();
        goto L_0x0048;
    L_0x014a:
        switch(r13) {
            case 4: goto L_0x0153;
            case 128: goto L_0x01f3;
            case 256: goto L_0x01f3;
            case 512: goto L_0x018c;
            default: goto L_0x014d;
        };
    L_0x014d:
        r7 = new com.thoughtworks.xstream.io.json.AbstractJsonWriter$IllegalWriterStateException;
        r7.<init>(r12, r13, r14);
        throw r7;
    L_0x0153:
        r7 = r11.mode;
        r7 = r7 & 1;
        if (r7 == 0) goto L_0x015c;
    L_0x0159:
        r7 = 2;
        if (r6 <= r7) goto L_0x017f;
    L_0x015c:
        if (r2 == 0) goto L_0x0164;
    L_0x015e:
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 == 0) goto L_0x0176;
    L_0x0164:
        r7 = "";
        r7 = r7.equals(r15);
        if (r7 != 0) goto L_0x016f;
    L_0x016c:
        r11.startObject();
    L_0x016f:
        r7 = r11.encodeNode(r14);
        r11.addLabel(r7);
    L_0x0176:
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 == 0) goto L_0x017f;
    L_0x017c:
        r11.startArray();
    L_0x017f:
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 != 0) goto L_0x0048;
    L_0x0185:
        if (r1 == 0) goto L_0x0048;
    L_0x0187:
        r11.startArray();
        goto L_0x0048;
    L_0x018c:
        r7 = r11.mode;
        r7 = r7 & 2;
        if (r7 == 0) goto L_0x019d;
    L_0x0192:
        r7 = 2;
        if (r6 != r7) goto L_0x019d;
    L_0x0195:
        r7 = new com.thoughtworks.xstream.converters.ConversionException;
        r8 = "Single value cannot be root element";
        r7.<init>(r8);
        throw r7;
    L_0x019d:
        if (r15 != 0) goto L_0x01bc;
    L_0x019f:
        r7 = com.thoughtworks.xstream.mapper.Mapper.Null.class;
        if (r0 != r7) goto L_0x01ac;
    L_0x01a3:
        r7 = "null";
        r8 = com.thoughtworks.xstream.io.json.AbstractJsonWriter.Type.NULL;
        r11.addValue(r7, r8);
        goto L_0x0048;
    L_0x01ac:
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 != 0) goto L_0x0048;
    L_0x01b2:
        if (r1 != 0) goto L_0x0048;
    L_0x01b4:
        r11.startObject();
        r11.endObject();
        goto L_0x0048;
    L_0x01bc:
        r7 = r11.mode;
        r7 = r7 & 8;
        if (r7 == 0) goto L_0x01ea;
    L_0x01c2:
        r7 = java.lang.Long.TYPE;
        if (r0 == r7) goto L_0x01ca;
    L_0x01c6:
        r7 = java.lang.Long.class;
        if (r0 != r7) goto L_0x01ea;
    L_0x01ca:
        r3 = java.lang.Long.parseLong(r15);
        r7 = 9007199254740992; // 0x20000000000000 float:0.0 double:4.450147717014403E-308;
        r7 = (r3 > r7 ? 1 : (r3 == r7 ? 0 : -1));
        if (r7 > 0) goto L_0x01da;
    L_0x01d4:
        r7 = -9007199254740992; // 0xffe0000000000000 float:0.0 double:-8.98846567431158E307;
        r7 = (r3 > r7 ? 1 : (r3 == r7 ? 0 : -1));
        if (r7 >= 0) goto L_0x01e1;
    L_0x01da:
        r7 = com.thoughtworks.xstream.io.json.AbstractJsonWriter.Type.STRING;
        r11.addValue(r15, r7);
        goto L_0x0048;
    L_0x01e1:
        r7 = r11.getType(r0);
        r11.addValue(r15, r7);
        goto L_0x0048;
    L_0x01ea:
        r7 = r11.getType(r0);
        r11.addValue(r15, r7);
        goto L_0x0048;
    L_0x01f3:
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 != 0) goto L_0x0048;
    L_0x01f9:
        if (r1 == 0) goto L_0x0200;
    L_0x01fb:
        r11.endArray();
        goto L_0x0048;
    L_0x0200:
        r11.endObject();
        goto L_0x0048;
    L_0x0205:
        switch(r13) {
            case 2: goto L_0x020e;
            default: goto L_0x0208;
        };
    L_0x0208:
        r7 = new com.thoughtworks.xstream.io.json.AbstractJsonWriter$IllegalWriterStateException;
        r7.<init>(r12, r13, r14);
        throw r7;
    L_0x020e:
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 == 0) goto L_0x0048;
    L_0x0214:
        r11.endArray();
        r11.endArray();
        r11.endObject();
        goto L_0x0048;
    L_0x021f:
        switch(r13) {
            case 16: goto L_0x022b;
            default: goto L_0x0222;
        };
    L_0x0222:
        switch(r13) {
            case 1: goto L_0x0310;
            case 4: goto L_0x02a7;
            case 16: goto L_0x0272;
            case 32: goto L_0x025a;
            case 128: goto L_0x02ff;
            case 512: goto L_0x02a7;
            default: goto L_0x0225;
        };
    L_0x0225:
        r7 = new com.thoughtworks.xstream.io.json.AbstractJsonWriter$IllegalWriterStateException;
        r7.<init>(r12, r13, r14);
        throw r7;
    L_0x022b:
        if (r14 == 0) goto L_0x0048;
    L_0x022d:
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 != 0) goto L_0x0257;
    L_0x0238:
        r7 = "@";
    L_0x023a:
        r7 = r8.append(r7);
        r7 = r7.append(r14);
        r5 = r7.toString();
        r11.startObject();
        r7 = r11.encodeAttribute(r5);
        r11.addLabel(r7);
        r7 = com.thoughtworks.xstream.io.json.AbstractJsonWriter.Type.STRING;
        r11.addValue(r15, r7);
        goto L_0x0048;
    L_0x0257:
        r7 = "";
        goto L_0x023a;
    L_0x025a:
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 == 0) goto L_0x0048;
    L_0x0260:
        r7 = 16;
        if (r12 != r7) goto L_0x0267;
    L_0x0264:
        r11.endObject();
    L_0x0267:
        r11.endArray();
        r11.nextElement();
        r11.startArray();
        goto L_0x0048;
    L_0x0272:
        if (r1 == 0) goto L_0x027a;
    L_0x0274:
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 == 0) goto L_0x0048;
    L_0x027a:
        r11.nextElement();
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 != 0) goto L_0x02a4;
    L_0x0288:
        r7 = "@";
    L_0x028a:
        r7 = r8.append(r7);
        r7 = r7.append(r14);
        r5 = r7.toString();
        r7 = r11.encodeAttribute(r5);
        r11.addLabel(r7);
        r7 = com.thoughtworks.xstream.io.json.AbstractJsonWriter.Type.STRING;
        r11.addValue(r15, r7);
        goto L_0x0048;
    L_0x02a4:
        r7 = "";
        goto L_0x028a;
    L_0x02a7:
        r7 = 32;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        r7 = 64;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        switch(r13) {
            case 2: goto L_0x02bc;
            case 4: goto L_0x02ee;
            case 512: goto L_0x02cd;
            default: goto L_0x02ba;
        };
    L_0x02ba:
        goto L_0x0048;
    L_0x02bc:
        r7 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        r7 = 2;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        goto L_0x0048;
    L_0x02cd:
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 != 0) goto L_0x02dc;
    L_0x02d3:
        r7 = "$";
        r7 = r11.encodeNode(r7);
        r11.addLabel(r7);
    L_0x02dc:
        r7 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        r8 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r15);
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 != 0) goto L_0x0048;
    L_0x02e9:
        r11.endObject();
        goto L_0x0048;
    L_0x02ee:
        r8 = 4;
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 != 0) goto L_0x02fd;
    L_0x02f5:
        r7 = "";
    L_0x02f7:
        r12 = r11.handleStateTransition(r12, r8, r14, r7);
        goto L_0x0048;
    L_0x02fd:
        r7 = 0;
        goto L_0x02f7;
    L_0x02ff:
        r7 = 32;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        r7 = 2;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        goto L_0x0048;
    L_0x0310:
        r7 = 32;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        r7 = 2;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        r7 = 1;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        goto L_0x0048;
    L_0x0328:
        switch(r13) {
            case 2: goto L_0x033c;
            case 64: goto L_0x0331;
            default: goto L_0x032b;
        };
    L_0x032b:
        r7 = new com.thoughtworks.xstream.io.json.AbstractJsonWriter$IllegalWriterStateException;
        r7.<init>(r12, r13, r14);
        throw r7;
    L_0x0331:
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 != 0) goto L_0x0048;
    L_0x0337:
        r11.nextElement();
        goto L_0x0048;
    L_0x033c:
        r7 = 64;
        r8 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        r9 = 0;
        r10 = 0;
        r12 = r11.handleStateTransition(r7, r8, r9, r10);
        r7 = 2;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        goto L_0x0048;
    L_0x034f:
        switch(r13) {
            case 1: goto L_0x0376;
            case 128: goto L_0x0365;
            case 256: goto L_0x0358;
            default: goto L_0x0352;
        };
    L_0x0352:
        r7 = new com.thoughtworks.xstream.io.json.AbstractJsonWriter$IllegalWriterStateException;
        r7.<init>(r12, r13, r14);
        throw r7;
    L_0x0358:
        r7 = r11.mode;
        r7 = r7 & 4;
        if (r7 != 0) goto L_0x0048;
    L_0x035e:
        if (r1 == 0) goto L_0x0048;
    L_0x0360:
        r11.endArray();
        goto L_0x0048;
    L_0x0365:
        r7 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        r7 = 2;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        goto L_0x0048;
    L_0x0376:
        r7 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        r7 = 2;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        r7 = 1;
        r8 = 0;
        r9 = 0;
        r12 = r11.handleStateTransition(r12, r7, r8, r9);
        goto L_0x0048;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.thoughtworks.xstream.io.json.AbstractJsonWriter.handleStateTransition(int, int, java.lang.String, java.lang.String):int");
    }

    protected Type getType(Class clazz) {
        if (clazz == Null.class) {
            return Type.NULL;
        }
        if (clazz == Boolean.class || clazz == Boolean.TYPE) {
            return Type.BOOLEAN;
        }
        return NUMBER_TYPES.contains(clazz) ? Type.NUMBER : Type.STRING;
    }

    protected boolean isArray(Class clazz) {
        return clazz != null && (clazz.isArray() || Collection.class.isAssignableFrom(clazz) || Externalizable.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz) || Entry.class.isAssignableFrom(clazz));
    }
}

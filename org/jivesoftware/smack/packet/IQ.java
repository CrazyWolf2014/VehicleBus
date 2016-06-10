package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;

public abstract class IQ extends Packet {
    private Type type;

    public static class Type {
        public static final Type ERROR;
        public static final Type GET;
        public static final Type RESULT;
        public static final Type SET;
        private String value;

        static {
            GET = new Type("get");
            SET = new Type("set");
            RESULT = new Type(Form.TYPE_RESULT);
            ERROR = new Type("error");
        }

        public static Type fromString(String str) {
            if (str == null) {
                return null;
            }
            String toLowerCase = str.toLowerCase();
            if (GET.toString().equals(toLowerCase)) {
                return GET;
            }
            if (SET.toString().equals(toLowerCase)) {
                return SET;
            }
            if (ERROR.toString().equals(toLowerCase)) {
                return ERROR;
            }
            if (RESULT.toString().equals(toLowerCase)) {
                return RESULT;
            }
            return null;
        }

        private Type(String str) {
            this.value = str;
        }

        public String toString() {
            return this.value;
        }
    }

    /* renamed from: org.jivesoftware.smack.packet.IQ.1 */
    static class C12841 extends IQ {
        C12841() {
        }

        public String getChildElementXML() {
            return null;
        }
    }

    /* renamed from: org.jivesoftware.smack.packet.IQ.2 */
    static class C12852 extends IQ {
        final /* synthetic */ IQ val$request;

        C12852(IQ iq) {
            this.val$request = iq;
        }

        public String getChildElementXML() {
            return this.val$request.getChildElementXML();
        }
    }

    public abstract String getChildElementXML();

    public IQ() {
        this.type = Type.GET;
    }

    public IQ(IQ iq) {
        super(iq);
        this.type = Type.GET;
        this.type = iq.getType();
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        if (type == null) {
            this.type = Type.GET;
        } else {
            this.type = type;
        }
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<iq ");
        if (getPacketID() != null) {
            stringBuilder.append("id=\"" + getPacketID() + "\" ");
        }
        if (getTo() != null) {
            stringBuilder.append("to=\"").append(StringUtils.escapeForXML(getTo())).append("\" ");
        }
        if (getFrom() != null) {
            stringBuilder.append("from=\"").append(StringUtils.escapeForXML(getFrom())).append("\" ");
        }
        if (this.type == null) {
            stringBuilder.append("type=\"get\">");
        } else {
            stringBuilder.append("type=\"").append(getType()).append("\">");
        }
        String childElementXML = getChildElementXML();
        if (childElementXML != null) {
            stringBuilder.append(childElementXML);
        }
        XMPPError error = getError();
        if (error != null) {
            stringBuilder.append(error.toXML());
        }
        stringBuilder.append("</iq>");
        return stringBuilder.toString();
    }

    public static IQ createResultIQ(IQ iq) {
        if (iq.getType() == Type.GET || iq.getType() == Type.SET) {
            IQ c12841 = new C12841();
            c12841.setType(Type.RESULT);
            c12841.setPacketID(iq.getPacketID());
            c12841.setFrom(iq.getTo());
            c12841.setTo(iq.getFrom());
            return c12841;
        }
        throw new IllegalArgumentException("IQ must be of type 'set' or 'get'. Original IQ: " + iq.toXML());
    }

    public static IQ createErrorResponse(IQ iq, XMPPError xMPPError) {
        if (iq.getType() == Type.GET || iq.getType() == Type.SET) {
            IQ c12852 = new C12852(iq);
            c12852.setType(Type.ERROR);
            c12852.setPacketID(iq.getPacketID());
            c12852.setFrom(iq.getTo());
            c12852.setTo(iq.getFrom());
            c12852.setError(xMPPError);
            return c12852;
        }
        throw new IllegalArgumentException("IQ must be of type 'set' or 'get'. Original IQ: " + iq.toXML());
    }
}

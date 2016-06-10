package org.jivesoftware.smack.packet;

import com.ifoer.util.MyHttpException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class XMPPError {
    private List<PacketExtension> applicationExtensions;
    private int code;
    private String condition;
    private String message;
    private Type type;

    public static class Condition {
        public static final Condition bad_request;
        public static final Condition conflict;
        public static final Condition feature_not_implemented;
        public static final Condition forbidden;
        public static final Condition gone;
        public static final Condition interna_server_error;
        public static final Condition item_not_found;
        public static final Condition jid_malformed;
        public static final Condition no_acceptable;
        public static final Condition not_allowed;
        public static final Condition not_authorized;
        public static final Condition payment_required;
        public static final Condition recipient_unavailable;
        public static final Condition redirect;
        public static final Condition registration_required;
        public static final Condition remote_server_error;
        public static final Condition remote_server_not_found;
        public static final Condition remote_server_timeout;
        public static final Condition request_timeout;
        public static final Condition resource_constraint;
        public static final Condition service_unavailable;
        public static final Condition subscription_required;
        public static final Condition undefined_condition;
        public static final Condition unexpected_request;
        private String value;

        static {
            interna_server_error = new Condition("internal-server-error");
            forbidden = new Condition("forbidden");
            bad_request = new Condition("bad-request");
            conflict = new Condition("conflict");
            feature_not_implemented = new Condition("feature-not-implemented");
            gone = new Condition("gone");
            item_not_found = new Condition("item-not-found");
            jid_malformed = new Condition("jid-malformed");
            no_acceptable = new Condition("not-acceptable");
            not_allowed = new Condition("not-allowed");
            not_authorized = new Condition("not-authorized");
            payment_required = new Condition("payment-required");
            recipient_unavailable = new Condition("recipient-unavailable");
            redirect = new Condition("redirect");
            registration_required = new Condition("registration-required");
            remote_server_error = new Condition("remote-server-error");
            remote_server_not_found = new Condition("remote-server-not-found");
            remote_server_timeout = new Condition("remote-server-timeout");
            resource_constraint = new Condition("resource-constraint");
            service_unavailable = new Condition("service-unavailable");
            subscription_required = new Condition("subscription-required");
            undefined_condition = new Condition("undefined-condition");
            unexpected_request = new Condition("unexpected-request");
            request_timeout = new Condition("request-timeout");
        }

        public Condition(String str) {
            this.value = str;
        }

        public String toString() {
            return this.value;
        }
    }

    private static class ErrorSpecification {
        private static Map<Condition, ErrorSpecification> instances;
        private int code;
        private Condition condition;
        private Type type;

        static {
            instances = errorSpecifications();
        }

        private ErrorSpecification(Condition condition, Type type, int i) {
            this.code = i;
            this.type = type;
            this.condition = condition;
        }

        private static Map<Condition, ErrorSpecification> errorSpecifications() {
            Map<Condition, ErrorSpecification> hashMap = new HashMap(22);
            hashMap.put(Condition.interna_server_error, new ErrorSpecification(Condition.interna_server_error, Type.WAIT, MyHttpException.ERROR_SERVER));
            hashMap.put(Condition.forbidden, new ErrorSpecification(Condition.forbidden, Type.AUTH, 403));
            hashMap.put(Condition.bad_request, new ErrorSpecification(Condition.bad_request, Type.MODIFY, MyHttpException.ERROR_PARAMETER));
            hashMap.put(Condition.item_not_found, new ErrorSpecification(Condition.item_not_found, Type.CANCEL, 404));
            hashMap.put(Condition.conflict, new ErrorSpecification(Condition.conflict, Type.CANCEL, 409));
            hashMap.put(Condition.feature_not_implemented, new ErrorSpecification(Condition.feature_not_implemented, Type.CANCEL, MyHttpException.ERROR_NETWORK));
            hashMap.put(Condition.gone, new ErrorSpecification(Condition.gone, Type.MODIFY, 302));
            hashMap.put(Condition.jid_malformed, new ErrorSpecification(Condition.jid_malformed, Type.MODIFY, MyHttpException.ERROR_PARAMETER));
            hashMap.put(Condition.no_acceptable, new ErrorSpecification(Condition.no_acceptable, Type.MODIFY, 406));
            hashMap.put(Condition.not_allowed, new ErrorSpecification(Condition.not_allowed, Type.CANCEL, MyHttpException.ERROR_RESULT_NOT_EXIST));
            hashMap.put(Condition.not_authorized, new ErrorSpecification(Condition.not_authorized, Type.AUTH, MyHttpException.ERROR_PARAMETER_EMPTY));
            hashMap.put(Condition.payment_required, new ErrorSpecification(Condition.payment_required, Type.AUTH, MyHttpException.ERROR_PARAMETER_ILLEGAL));
            hashMap.put(Condition.recipient_unavailable, new ErrorSpecification(Condition.recipient_unavailable, Type.WAIT, 404));
            hashMap.put(Condition.redirect, new ErrorSpecification(Condition.redirect, Type.MODIFY, 302));
            hashMap.put(Condition.registration_required, new ErrorSpecification(Condition.registration_required, Type.AUTH, 407));
            hashMap.put(Condition.remote_server_not_found, new ErrorSpecification(Condition.remote_server_not_found, Type.CANCEL, 404));
            hashMap.put(Condition.remote_server_timeout, new ErrorSpecification(Condition.remote_server_timeout, Type.WAIT, 504));
            hashMap.put(Condition.remote_server_error, new ErrorSpecification(Condition.remote_server_error, Type.CANCEL, 502));
            hashMap.put(Condition.resource_constraint, new ErrorSpecification(Condition.resource_constraint, Type.WAIT, MyHttpException.ERROR_SERVER));
            hashMap.put(Condition.service_unavailable, new ErrorSpecification(Condition.service_unavailable, Type.CANCEL, 503));
            hashMap.put(Condition.subscription_required, new ErrorSpecification(Condition.subscription_required, Type.AUTH, 407));
            hashMap.put(Condition.undefined_condition, new ErrorSpecification(Condition.undefined_condition, Type.WAIT, MyHttpException.ERROR_SERVER));
            hashMap.put(Condition.unexpected_request, new ErrorSpecification(Condition.unexpected_request, Type.WAIT, MyHttpException.ERROR_PARAMETER));
            hashMap.put(Condition.request_timeout, new ErrorSpecification(Condition.request_timeout, Type.CANCEL, 408));
            return hashMap;
        }

        protected static ErrorSpecification specFor(Condition condition) {
            return (ErrorSpecification) instances.get(condition);
        }

        protected Condition getCondition() {
            return this.condition;
        }

        protected Type getType() {
            return this.type;
        }

        protected int getCode() {
            return this.code;
        }
    }

    public enum Type {
        WAIT,
        CANCEL,
        MODIFY,
        AUTH,
        CONTINUE
    }

    public XMPPError(Condition condition) {
        this.applicationExtensions = null;
        init(condition);
        this.message = null;
    }

    public XMPPError(Condition condition, String str) {
        this.applicationExtensions = null;
        init(condition);
        this.message = str;
    }

    public XMPPError(int i) {
        this.applicationExtensions = null;
        this.code = i;
        this.message = null;
    }

    public XMPPError(int i, String str) {
        this.applicationExtensions = null;
        this.code = i;
        this.message = str;
    }

    public XMPPError(int i, Type type, String str, String str2, List<PacketExtension> list) {
        this.applicationExtensions = null;
        this.code = i;
        this.type = type;
        this.condition = str;
        this.message = str2;
        this.applicationExtensions = list;
    }

    private void init(Condition condition) {
        ErrorSpecification specFor = ErrorSpecification.specFor(condition);
        this.condition = condition.value;
        if (specFor != null) {
            this.type = specFor.getType();
            this.code = specFor.getCode();
        }
    }

    public String getCondition() {
        return this.condition;
    }

    public Type getType() {
        return this.type;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<error code=\"").append(this.code).append("\"");
        if (this.type != null) {
            stringBuilder.append(" type=\"");
            stringBuilder.append(this.type.name());
            stringBuilder.append("\"");
        }
        stringBuilder.append(">");
        if (this.condition != null) {
            stringBuilder.append("<").append(this.condition);
            stringBuilder.append(" xmlns=\"urn:ietf:params:xml:ns:xmpp-stanzas\"/>");
        }
        if (this.message != null) {
            stringBuilder.append("<text xml:lang=\"en\" xmlns=\"urn:ietf:params:xml:ns:xmpp-stanzas\">");
            stringBuilder.append(this.message);
            stringBuilder.append("</text>");
        }
        for (PacketExtension toXML : getExtensions()) {
            stringBuilder.append(toXML.toXML());
        }
        stringBuilder.append("</error>");
        return stringBuilder.toString();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.condition != null) {
            stringBuilder.append(this.condition);
        }
        stringBuilder.append("(").append(this.code).append(")");
        if (this.message != null) {
            stringBuilder.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(this.message);
        }
        return stringBuilder.toString();
    }

    public synchronized List<PacketExtension> getExtensions() {
        List<PacketExtension> emptyList;
        if (this.applicationExtensions == null) {
            emptyList = Collections.emptyList();
        } else {
            emptyList = Collections.unmodifiableList(this.applicationExtensions);
        }
        return emptyList;
    }

    public synchronized PacketExtension getExtension(String str, String str2) {
        PacketExtension packetExtension;
        if (this.applicationExtensions == null || str == null || str2 == null) {
            packetExtension = null;
        } else {
            for (PacketExtension packetExtension2 : this.applicationExtensions) {
                if (str.equals(packetExtension2.getElementName()) && str2.equals(packetExtension2.getNamespace())) {
                    break;
                }
            }
            packetExtension2 = null;
        }
        return packetExtension2;
    }

    public synchronized void addExtension(PacketExtension packetExtension) {
        if (this.applicationExtensions == null) {
            this.applicationExtensions = new ArrayList();
        }
        this.applicationExtensions.add(packetExtension);
    }

    public synchronized void setExtension(List<PacketExtension> list) {
        this.applicationExtensions = list;
    }
}

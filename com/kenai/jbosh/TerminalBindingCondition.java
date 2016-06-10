package com.kenai.jbosh;

import com.ifoer.util.MyHttpException;
import java.util.HashMap;
import java.util.Map;

final class TerminalBindingCondition {
    static final TerminalBindingCondition BAD_REQUEST;
    private static final Map<Integer, TerminalBindingCondition> CODE_TO_INSTANCE;
    private static final Map<String, TerminalBindingCondition> COND_TO_INSTANCE;
    static final TerminalBindingCondition HOST_GONE;
    static final TerminalBindingCondition HOST_UNKNOWN;
    static final TerminalBindingCondition IMPROPER_ADDRESSING;
    static final TerminalBindingCondition INTERNAL_SERVER_ERROR;
    static final TerminalBindingCondition ITEM_NOT_FOUND;
    static final TerminalBindingCondition OTHER_REQUEST;
    static final TerminalBindingCondition POLICY_VIOLATION;
    static final TerminalBindingCondition REMOTE_CONNECTION_FAILED;
    static final TerminalBindingCondition REMOTE_STREAM_ERROR;
    static final TerminalBindingCondition SEE_OTHER_URI;
    static final TerminalBindingCondition SYSTEM_SHUTDOWN;
    static final TerminalBindingCondition UNDEFINED_CONDITION;
    private final String cond;
    private final String msg;

    static {
        COND_TO_INSTANCE = new HashMap();
        CODE_TO_INSTANCE = new HashMap();
        BAD_REQUEST = createWithCode("bad-request", "The format of an HTTP header or binding element received from the client is unacceptable (e.g., syntax error).", Integer.valueOf(MyHttpException.ERROR_PARAMETER));
        HOST_GONE = create("host-gone", "The target domain specified in the 'to' attribute or the target host or port specified in the 'route' attribute is no longer serviced by the connection manager.");
        HOST_UNKNOWN = create("host-unknown", "The target domain specified in the 'to' attribute or the target host or port specified in the 'route' attribute is unknown to the connection manager.");
        IMPROPER_ADDRESSING = create("improper-addressing", "The initialization element lacks a 'to' or 'route' attribute (or the attribute has no value) but the connection manager requires one.");
        INTERNAL_SERVER_ERROR = create("internal-server-error", "The connection manager has experienced an internal error that prevents it from servicing the request.");
        ITEM_NOT_FOUND = createWithCode("item-not-found", "(1) 'sid' is not valid, (2) 'stream' is not valid, (3) 'rid' is larger than the upper limit of the expected window, (4) connection manager is unable to resend response, (5) 'key' sequence is invalid.", Integer.valueOf(404));
        OTHER_REQUEST = create("other-request", "Another request being processed at the same time as this request caused the session to terminate.");
        POLICY_VIOLATION = createWithCode("policy-violation", "The client has broken the session rules (polling too frequently, requesting too frequently, sending too many simultaneous requests).", Integer.valueOf(403));
        REMOTE_CONNECTION_FAILED = create("remote-connection-failed", "The connection manager was unable to connect to, or unable to connect securely to, or has lost its connection to, the server.");
        REMOTE_STREAM_ERROR = create("remote-stream-error", "Encapsulated transport protocol error.");
        SEE_OTHER_URI = create("see-other-uri", "The connection manager does not operate at this URI (e.g., the connection manager accepts only SSL or TLS connections at some https: URI rather than the http: URI requested by the client).");
        SYSTEM_SHUTDOWN = create("system-shutdown", "The connection manager is being shut down. All active HTTP sessions are being terminated. No new sessions can be created.");
        UNDEFINED_CONDITION = create("undefined-condition", "Unknown or undefined error condition.");
    }

    private TerminalBindingCondition(String str, String str2) {
        this.cond = str;
        this.msg = str2;
    }

    private static TerminalBindingCondition create(String str, String str2) {
        return createWithCode(str, str2, null);
    }

    private static TerminalBindingCondition createWithCode(String str, String str2, Integer num) {
        if (str == null) {
            throw new IllegalArgumentException("condition may not be null");
        } else if (str2 == null) {
            throw new IllegalArgumentException("message may not be null");
        } else if (COND_TO_INSTANCE.get(str) != null) {
            throw new IllegalStateException("Multiple definitions of condition: " + str);
        } else {
            TerminalBindingCondition terminalBindingCondition = new TerminalBindingCondition(str, str2);
            COND_TO_INSTANCE.put(str, terminalBindingCondition);
            if (num != null) {
                if (CODE_TO_INSTANCE.get(num) != null) {
                    throw new IllegalStateException("Multiple definitions of code: " + num);
                }
                CODE_TO_INSTANCE.put(num, terminalBindingCondition);
            }
            return terminalBindingCondition;
        }
    }

    static TerminalBindingCondition forString(String str) {
        return (TerminalBindingCondition) COND_TO_INSTANCE.get(str);
    }

    static TerminalBindingCondition forHTTPResponseCode(int i) {
        return (TerminalBindingCondition) CODE_TO_INSTANCE.get(Integer.valueOf(i));
    }

    String getCondition() {
        return this.cond;
    }

    String getMessage() {
        return this.msg;
    }
}

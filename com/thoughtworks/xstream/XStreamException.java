package com.thoughtworks.xstream;

import com.thoughtworks.xstream.core.BaseException;
import org.xmlpull.v1.XmlPullParser;

public class XStreamException extends BaseException {
    private Throwable cause;

    protected XStreamException() {
        this(XmlPullParser.NO_NAMESPACE, null);
    }

    public XStreamException(String message) {
        this(message, null);
    }

    public XStreamException(Throwable cause) {
        this(XmlPullParser.NO_NAMESPACE, cause);
    }

    public XStreamException(String message, Throwable cause) {
        super(message + (cause == null ? XmlPullParser.NO_NAMESPACE : " : " + cause.getMessage()));
        this.cause = cause;
    }

    public Throwable getCause() {
        return this.cause;
    }
}

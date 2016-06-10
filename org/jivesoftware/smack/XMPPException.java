package org.jivesoftware.smack;

import java.io.PrintStream;
import java.io.PrintWriter;
import org.jivesoftware.smack.packet.StreamError;
import org.jivesoftware.smack.packet.XMPPError;

public class XMPPException extends Exception {
    private XMPPError error;
    private StreamError streamError;
    private Throwable wrappedThrowable;

    public XMPPException() {
        this.streamError = null;
        this.error = null;
        this.wrappedThrowable = null;
    }

    public XMPPException(String str) {
        super(str);
        this.streamError = null;
        this.error = null;
        this.wrappedThrowable = null;
    }

    public XMPPException(Throwable th) {
        this.streamError = null;
        this.error = null;
        this.wrappedThrowable = null;
        this.wrappedThrowable = th;
    }

    public XMPPException(StreamError streamError) {
        this.streamError = null;
        this.error = null;
        this.wrappedThrowable = null;
        this.streamError = streamError;
    }

    public XMPPException(XMPPError xMPPError) {
        this.streamError = null;
        this.error = null;
        this.wrappedThrowable = null;
        this.error = xMPPError;
    }

    public XMPPException(String str, Throwable th) {
        super(str);
        this.streamError = null;
        this.error = null;
        this.wrappedThrowable = null;
        this.wrappedThrowable = th;
    }

    public XMPPException(String str, XMPPError xMPPError, Throwable th) {
        super(str);
        this.streamError = null;
        this.error = null;
        this.wrappedThrowable = null;
        this.error = xMPPError;
        this.wrappedThrowable = th;
    }

    public XMPPException(String str, XMPPError xMPPError) {
        super(str);
        this.streamError = null;
        this.error = null;
        this.wrappedThrowable = null;
        this.error = xMPPError;
    }

    public XMPPError getXMPPError() {
        return this.error;
    }

    public StreamError getStreamError() {
        return this.streamError;
    }

    public Throwable getWrappedThrowable() {
        return this.wrappedThrowable;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream printStream) {
        super.printStackTrace(printStream);
        if (this.wrappedThrowable != null) {
            printStream.println("Nested Exception: ");
            this.wrappedThrowable.printStackTrace(printStream);
        }
    }

    public void printStackTrace(PrintWriter printWriter) {
        super.printStackTrace(printWriter);
        if (this.wrappedThrowable != null) {
            printWriter.println("Nested Exception: ");
            this.wrappedThrowable.printStackTrace(printWriter);
        }
    }

    public String getMessage() {
        String message = super.getMessage();
        if (message == null && this.error != null) {
            return this.error.toString();
        }
        if (message != null || this.streamError == null) {
            return message;
        }
        return this.streamError.toString();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String message = super.getMessage();
        if (message != null) {
            stringBuilder.append(message).append(": ");
        }
        if (this.error != null) {
            stringBuilder.append(this.error);
        }
        if (this.streamError != null) {
            stringBuilder.append(this.streamError);
        }
        if (this.wrappedThrowable != null) {
            stringBuilder.append("\n  -- caused by: ").append(this.wrappedThrowable);
        }
        return stringBuilder.toString();
    }
}

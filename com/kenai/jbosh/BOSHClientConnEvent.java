package com.kenai.jbosh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;

public final class BOSHClientConnEvent extends EventObject {
    private static final long serialVersionUID = 1;
    private final Throwable cause;
    private final boolean connected;
    private final List<ComposableBody> requests;

    private BOSHClientConnEvent(BOSHClient bOSHClient, boolean z, List<ComposableBody> list, Throwable th) {
        super(bOSHClient);
        this.connected = z;
        this.cause = th;
        if (this.connected) {
            if (th != null) {
                throw new IllegalStateException("Cannot be connected and have a cause");
            } else if (list != null && list.size() > 0) {
                throw new IllegalStateException("Cannot be connected and have outstanding requests");
            }
        }
        if (list == null) {
            this.requests = Collections.emptyList();
        } else {
            this.requests = Collections.unmodifiableList(new ArrayList(list));
        }
    }

    static BOSHClientConnEvent createConnectionEstablishedEvent(BOSHClient bOSHClient) {
        return new BOSHClientConnEvent(bOSHClient, true, null, null);
    }

    static BOSHClientConnEvent createConnectionClosedEvent(BOSHClient bOSHClient) {
        return new BOSHClientConnEvent(bOSHClient, false, null, null);
    }

    static BOSHClientConnEvent createConnectionClosedOnErrorEvent(BOSHClient bOSHClient, List<ComposableBody> list, Throwable th) {
        return new BOSHClientConnEvent(bOSHClient, false, list, th);
    }

    public BOSHClient getBOSHClient() {
        return (BOSHClient) getSource();
    }

    public boolean isConnected() {
        return this.connected;
    }

    public boolean isError() {
        return this.cause != null;
    }

    public Throwable getCause() {
        return this.cause;
    }

    public List<ComposableBody> getOutstandingRequests() {
        return this.requests;
    }
}

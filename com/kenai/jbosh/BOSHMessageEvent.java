package com.kenai.jbosh;

import java.util.EventObject;

public final class BOSHMessageEvent extends EventObject {
    private static final long serialVersionUID = 1;
    private final AbstractBody body;

    private BOSHMessageEvent(Object obj, AbstractBody abstractBody) {
        super(obj);
        if (abstractBody == null) {
            throw new IllegalArgumentException("message body may not be null");
        }
        this.body = abstractBody;
    }

    static BOSHMessageEvent createRequestSentEvent(BOSHClient bOSHClient, AbstractBody abstractBody) {
        return new BOSHMessageEvent(bOSHClient, abstractBody);
    }

    static BOSHMessageEvent createResponseReceivedEvent(BOSHClient bOSHClient, AbstractBody abstractBody) {
        return new BOSHMessageEvent(bOSHClient, abstractBody);
    }

    public AbstractBody getBody() {
        return this.body;
    }
}

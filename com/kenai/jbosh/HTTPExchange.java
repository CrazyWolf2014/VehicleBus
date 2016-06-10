package com.kenai.jbosh;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

final class HTTPExchange {
    private static final Logger LOG;
    private final Lock lock;
    private final Condition ready;
    private final AbstractBody request;
    private HTTPResponse response;

    static {
        LOG = Logger.getLogger(HTTPExchange.class.getName());
    }

    HTTPExchange(AbstractBody abstractBody) {
        this.lock = new ReentrantLock();
        this.ready = this.lock.newCondition();
        if (abstractBody == null) {
            throw new IllegalArgumentException("Request body cannot be null");
        }
        this.request = abstractBody;
    }

    AbstractBody getRequest() {
        return this.request;
    }

    void setHTTPResponse(HTTPResponse hTTPResponse) {
        this.lock.lock();
        try {
            if (this.response != null) {
                throw new IllegalStateException("HTTPResponse was already set");
            }
            this.response = hTTPResponse;
            this.ready.signalAll();
        } finally {
            this.lock.unlock();
        }
    }

    HTTPResponse getHTTPResponse() {
        this.lock.lock();
        while (this.response == null) {
            try {
                this.ready.await();
            } catch (Throwable e) {
                LOG.log(Level.FINEST, "Interrupted", e);
            } catch (Throwable th) {
                this.lock.unlock();
            }
        }
        HTTPResponse hTTPResponse = this.response;
        this.lock.unlock();
        return hTTPResponse;
    }
}

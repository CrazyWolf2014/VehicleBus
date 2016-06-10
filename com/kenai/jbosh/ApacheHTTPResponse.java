package com.kenai.jbosh;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

final class ApacheHTTPResponse implements HTTPResponse {
    private static final String ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ACCEPT_ENCODING_VAL;
    private static final String CHARSET = "UTF-8";
    private static final String CONTENT_TYPE = "text/xml; charset=utf-8";
    private AbstractBody body;
    private final HttpClient client;
    private final HttpContext context;
    private final Lock lock;
    private final HttpPost post;
    private boolean sent;
    private int statusCode;
    private BOSHException toThrow;

    static {
        ACCEPT_ENCODING_VAL = ZLIBCodec.getID() + ", " + GZIPCodec.getID();
    }

    ApacheHTTPResponse(HttpClient httpClient, BOSHClientConfig bOSHClientConfig, CMSessionParams cMSessionParams, AbstractBody abstractBody) {
        this.lock = new ReentrantLock();
        this.client = httpClient;
        this.context = new BasicHttpContext();
        this.post = new HttpPost(bOSHClientConfig.getURI().toString());
        this.sent = false;
        try {
            byte[] bytes = abstractBody.toXML().getBytes(CHARSET);
            String str = null;
            if (bOSHClientConfig.isCompressionEnabled() && cMSessionParams != null) {
                AttrAccept accept = cMSessionParams.getAccept();
                if (accept != null) {
                    if (accept.isAccepted(ZLIBCodec.getID())) {
                        str = ZLIBCodec.getID();
                        bytes = ZLIBCodec.encode(bytes);
                    } else if (accept.isAccepted(GZIPCodec.getID())) {
                        str = GZIPCodec.getID();
                        bytes = GZIPCodec.encode(bytes);
                    }
                }
            }
            HttpEntity byteArrayEntity = new ByteArrayEntity(bytes);
            byteArrayEntity.setContentType(CONTENT_TYPE);
            if (str != null) {
                byteArrayEntity.setContentEncoding(str);
            }
            this.post.setEntity(byteArrayEntity);
            if (bOSHClientConfig.isCompressionEnabled()) {
                this.post.setHeader(ACCEPT_ENCODING, ACCEPT_ENCODING_VAL);
            }
        } catch (Throwable e) {
            this.toThrow = new BOSHException("Could not generate request", e);
        }
    }

    public void abort() {
        if (this.post != null) {
            this.post.abort();
            this.toThrow = new BOSHException("HTTP request aborted");
        }
    }

    public AbstractBody getBody() throws InterruptedException, BOSHException {
        if (this.toThrow != null) {
            throw this.toThrow;
        }
        this.lock.lock();
        try {
            if (!this.sent) {
                awaitResponse();
            }
            this.lock.unlock();
            return this.body;
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }

    public int getHTTPStatus() throws InterruptedException, BOSHException {
        if (this.toThrow != null) {
            throw this.toThrow;
        }
        this.lock.lock();
        try {
            if (!this.sent) {
                awaitResponse();
            }
            this.lock.unlock();
            return this.statusCode;
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }

    private synchronized void awaitResponse() throws BOSHException {
        try {
            HttpResponse execute = this.client.execute(this.post, this.context);
            HttpEntity entity = execute.getEntity();
            byte[] toByteArray = EntityUtils.toByteArray(entity);
            String value = entity.getContentEncoding() != null ? entity.getContentEncoding().getValue() : null;
            if (ZLIBCodec.getID().equalsIgnoreCase(value)) {
                toByteArray = ZLIBCodec.decode(toByteArray);
            } else if (GZIPCodec.getID().equalsIgnoreCase(value)) {
                toByteArray = GZIPCodec.decode(toByteArray);
            }
            this.body = StaticBody.fromString(new String(toByteArray, CHARSET));
            this.statusCode = execute.getStatusLine().getStatusCode();
            this.sent = true;
        } catch (Throwable e) {
            abort();
            this.toThrow = new BOSHException("Could not obtain response", e);
            throw this.toThrow;
        } catch (RuntimeException e2) {
            abort();
            throw e2;
        }
    }
}

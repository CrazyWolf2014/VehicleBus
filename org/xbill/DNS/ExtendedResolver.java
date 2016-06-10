package org.xbill.DNS;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ExtendedResolver implements Resolver {
    private static final int quantum = 5;
    private int lbStart;
    private boolean loadBalance;
    private List resolvers;
    private int retries;

    private static class Resolution implements ResolverListener {
        boolean done;
        Object[] inprogress;
        ResolverListener listener;
        int outstanding;
        Message query;
        Resolver[] resolvers;
        Message response;
        int retries;
        int[] sent;
        Throwable thrown;

        public Resolution(ExtendedResolver extendedResolver, Message message) {
            List access$000 = extendedResolver.resolvers;
            this.resolvers = (Resolver[]) access$000.toArray(new Resolver[access$000.size()]);
            if (extendedResolver.loadBalance) {
                int length = this.resolvers.length;
                int access$208 = extendedResolver.lbStart = extendedResolver.lbStart + 1 % length;
                if (extendedResolver.lbStart > length) {
                    ExtendedResolver.access$244(extendedResolver, length);
                }
                if (access$208 > 0) {
                    Resolver[] resolverArr = new Resolver[length];
                    for (int i = 0; i < length; i++) {
                        resolverArr[i] = this.resolvers[(i + access$208) % length];
                    }
                    this.resolvers = resolverArr;
                }
            }
            this.sent = new int[this.resolvers.length];
            this.inprogress = new Object[this.resolvers.length];
            this.retries = extendedResolver.retries;
            this.query = message;
        }

        public void send(int i) {
            int[] iArr = this.sent;
            iArr[i] = iArr[i] + 1;
            this.outstanding++;
            try {
                this.inprogress[i] = this.resolvers[i].sendAsync(this.query, this);
            } catch (Throwable th) {
                synchronized (this) {
                }
                this.thrown = th;
                this.done = true;
                if (this.listener == null) {
                    notifyAll();
                }
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public org.xbill.DNS.Message start() throws java.io.IOException {
            /*
            r4 = this;
            r3 = 0;
            r0 = r4.sent;	 Catch:{ Exception -> 0x0026 }
            r1 = 0;
            r2 = r0[r1];	 Catch:{ Exception -> 0x0026 }
            r2 = r2 + 1;
            r0[r1] = r2;	 Catch:{ Exception -> 0x0026 }
            r0 = r4.outstanding;	 Catch:{ Exception -> 0x0026 }
            r0 = r0 + 1;
            r4.outstanding = r0;	 Catch:{ Exception -> 0x0026 }
            r0 = r4.inprogress;	 Catch:{ Exception -> 0x0026 }
            r1 = 0;
            r2 = new java.lang.Object;	 Catch:{ Exception -> 0x0026 }
            r2.<init>();	 Catch:{ Exception -> 0x0026 }
            r0[r1] = r2;	 Catch:{ Exception -> 0x0026 }
            r0 = r4.resolvers;	 Catch:{ Exception -> 0x0026 }
            r1 = 0;
            r0 = r0[r1];	 Catch:{ Exception -> 0x0026 }
            r1 = r4.query;	 Catch:{ Exception -> 0x0026 }
            r0 = r0.send(r1);	 Catch:{ Exception -> 0x0026 }
        L_0x0025:
            return r0;
        L_0x0026:
            r0 = move-exception;
            r1 = r4.inprogress;
            r1 = r1[r3];
            r4.handleException(r1, r0);
            monitor-enter(r4);
        L_0x002f:
            r0 = r4.done;	 Catch:{ all -> 0x0041 }
            if (r0 != 0) goto L_0x0039;
        L_0x0033:
            r4.wait();	 Catch:{ InterruptedException -> 0x0037 }
            goto L_0x002f;
        L_0x0037:
            r0 = move-exception;
            goto L_0x002f;
        L_0x0039:
            monitor-exit(r4);	 Catch:{ all -> 0x0041 }
            r0 = r4.response;
            if (r0 == 0) goto L_0x0044;
        L_0x003e:
            r0 = r4.response;
            goto L_0x0025;
        L_0x0041:
            r0 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0041 }
            throw r0;
        L_0x0044:
            r0 = r4.thrown;
            r0 = r0 instanceof java.io.IOException;
            if (r0 == 0) goto L_0x004f;
        L_0x004a:
            r0 = r4.thrown;
            r0 = (java.io.IOException) r0;
            throw r0;
        L_0x004f:
            r0 = r4.thrown;
            r0 = r0 instanceof java.lang.RuntimeException;
            if (r0 == 0) goto L_0x005a;
        L_0x0055:
            r0 = r4.thrown;
            r0 = (java.lang.RuntimeException) r0;
            throw r0;
        L_0x005a:
            r0 = r4.thrown;
            r0 = r0 instanceof java.lang.Error;
            if (r0 == 0) goto L_0x0065;
        L_0x0060:
            r0 = r4.thrown;
            r0 = (java.lang.Error) r0;
            throw r0;
        L_0x0065:
            r0 = new java.lang.IllegalStateException;
            r1 = "ExtendedResolver failure";
            r0.<init>(r1);
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.xbill.DNS.ExtendedResolver.Resolution.start():org.xbill.DNS.Message");
        }

        public void startAsync(ResolverListener resolverListener) {
            this.listener = resolverListener;
            send(0);
        }

        public void receiveMessage(Object obj, Message message) {
            if (Options.check("verbose")) {
                System.err.println("ExtendedResolver: received message");
            }
            synchronized (this) {
                if (this.done) {
                    return;
                }
                this.response = message;
                this.done = true;
                if (this.listener == null) {
                    notifyAll();
                    return;
                }
                this.listener.receiveMessage(this, this.response);
            }
        }

        public void handleException(Object obj, Exception exception) {
            Object obj2 = 1;
            if (Options.check("verbose")) {
                System.err.println("ExtendedResolver: got " + exception);
            }
            synchronized (this) {
                this.outstanding--;
                if (this.done) {
                    return;
                }
                int i = 0;
                while (i < this.inprogress.length && this.inprogress[i] != obj) {
                    i++;
                }
                if (i == this.inprogress.length) {
                    return;
                }
                if (this.sent[i] != 1 || i >= this.resolvers.length - 1) {
                    obj2 = null;
                }
                if (exception instanceof InterruptedIOException) {
                    if (this.sent[i] < this.retries) {
                        send(i);
                    }
                    if (this.thrown == null) {
                        this.thrown = exception;
                    }
                } else if (!(exception instanceof SocketException)) {
                    this.thrown = exception;
                } else if (this.thrown == null || (this.thrown instanceof InterruptedIOException)) {
                    this.thrown = exception;
                }
                if (this.done) {
                    return;
                }
                if (obj2 != null) {
                    send(i + 1);
                }
                if (this.done) {
                    return;
                }
                if (this.outstanding == 0) {
                    this.done = true;
                    if (this.listener == null) {
                        notifyAll();
                        return;
                    }
                }
                if (this.done) {
                    if (!(this.thrown instanceof Exception)) {
                        this.thrown = new RuntimeException(this.thrown.getMessage());
                    }
                    this.listener.handleException(this, (Exception) this.thrown);
                    return;
                }
            }
        }
    }

    static /* synthetic */ int access$244(ExtendedResolver extendedResolver, int i) {
        int i2 = extendedResolver.lbStart % i;
        extendedResolver.lbStart = i2;
        return i2;
    }

    private void init() {
        this.resolvers = new ArrayList();
    }

    public ExtendedResolver() throws UnknownHostException {
        int i = 0;
        this.loadBalance = false;
        this.lbStart = 0;
        this.retries = 3;
        init();
        String[] servers = ResolverConfig.getCurrentConfig().servers();
        if (servers != null) {
            while (i < servers.length) {
                Resolver simpleResolver = new SimpleResolver(servers[i]);
                simpleResolver.setTimeout(quantum);
                this.resolvers.add(simpleResolver);
                i++;
            }
            return;
        }
        this.resolvers.add(new SimpleResolver());
    }

    public ExtendedResolver(String[] strArr) throws UnknownHostException {
        int i = 0;
        this.loadBalance = false;
        this.lbStart = 0;
        this.retries = 3;
        init();
        while (i < strArr.length) {
            Resolver simpleResolver = new SimpleResolver(strArr[i]);
            simpleResolver.setTimeout(quantum);
            this.resolvers.add(simpleResolver);
            i++;
        }
    }

    public ExtendedResolver(Resolver[] resolverArr) throws UnknownHostException {
        int i = 0;
        this.loadBalance = false;
        this.lbStart = 0;
        this.retries = 3;
        init();
        while (i < resolverArr.length) {
            this.resolvers.add(resolverArr[i]);
            i++;
        }
    }

    public void setPort(int i) {
        for (int i2 = 0; i2 < this.resolvers.size(); i2++) {
            ((Resolver) this.resolvers.get(i2)).setPort(i);
        }
    }

    public void setTCP(boolean z) {
        for (int i = 0; i < this.resolvers.size(); i++) {
            ((Resolver) this.resolvers.get(i)).setTCP(z);
        }
    }

    public void setIgnoreTruncation(boolean z) {
        for (int i = 0; i < this.resolvers.size(); i++) {
            ((Resolver) this.resolvers.get(i)).setIgnoreTruncation(z);
        }
    }

    public void setEDNS(int i) {
        for (int i2 = 0; i2 < this.resolvers.size(); i2++) {
            ((Resolver) this.resolvers.get(i2)).setEDNS(i);
        }
    }

    public void setEDNS(int i, int i2, int i3, List list) {
        for (int i4 = 0; i4 < this.resolvers.size(); i4++) {
            ((Resolver) this.resolvers.get(i4)).setEDNS(i, i2, i3, list);
        }
    }

    public void setTSIGKey(TSIG tsig) {
        for (int i = 0; i < this.resolvers.size(); i++) {
            ((Resolver) this.resolvers.get(i)).setTSIGKey(tsig);
        }
    }

    public void setTimeout(int i, int i2) {
        for (int i3 = 0; i3 < this.resolvers.size(); i3++) {
            ((Resolver) this.resolvers.get(i3)).setTimeout(i, i2);
        }
    }

    public void setTimeout(int i) {
        setTimeout(i, 0);
    }

    public Message send(Message message) throws IOException {
        return new Resolution(this, message).start();
    }

    public Object sendAsync(Message message, ResolverListener resolverListener) {
        Resolution resolution = new Resolution(this, message);
        resolution.startAsync(resolverListener);
        return resolution;
    }

    public Resolver getResolver(int i) {
        if (i < this.resolvers.size()) {
            return (Resolver) this.resolvers.get(i);
        }
        return null;
    }

    public Resolver[] getResolvers() {
        return (Resolver[]) this.resolvers.toArray(new Resolver[this.resolvers.size()]);
    }

    public void addResolver(Resolver resolver) {
        this.resolvers.add(resolver);
    }

    public void deleteResolver(Resolver resolver) {
        this.resolvers.remove(resolver);
    }

    public void setLoadBalance(boolean z) {
        this.loadBalance = z;
    }

    public void setRetries(int i) {
        this.retries = i;
    }
}

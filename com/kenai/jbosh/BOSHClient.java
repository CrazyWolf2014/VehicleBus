package com.kenai.jbosh;

import com.ifoer.mine.Contact;
import com.kenai.jbosh.ComposableBody.Builder;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class BOSHClient {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final boolean ASSERTIONS;
    private static final int DEFAULT_EMPTY_REQUEST_DELAY = 100;
    private static final int DEFAULT_PAUSE_MARGIN = 500;
    private static final int EMPTY_REQUEST_DELAY;
    private static final String ERROR = "error";
    private static final String INTERRUPTED = "Interrupted";
    private static final Logger LOG;
    private static final String NULL_LISTENER = "Listener may not b enull";
    private static final int PAUSE_MARGIN;
    private static final String TERMINATE = "terminate";
    private static final String UNHANDLED = "Unhandled Exception";
    private final BOSHClientConfig cfg;
    private CMSessionParams cmParams;
    private final Set<BOSHClientConnListener> connListeners;
    private final Condition drained;
    private ScheduledFuture emptyRequestFuture;
    private final Runnable emptyRequestRunnable;
    private final AtomicReference<ExchangeInterceptor> exchInterceptor;
    private Queue<HTTPExchange> exchanges;
    private final HTTPSender httpSender;
    private final ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;
    private List<ComposableBody> pendingRequestAcks;
    private SortedSet<Long> pendingResponseAcks;
    private final Runnable procRunnable;
    private Thread procThread;
    private final RequestIDSequence requestIDSeq;
    private final Set<BOSHClientRequestListener> requestListeners;
    private Long responseAck;
    private final Set<BOSHClientResponseListener> responseListeners;
    private final ScheduledExecutorService schedExec;

    /* renamed from: com.kenai.jbosh.BOSHClient.1 */
    class C07771 implements Runnable {
        C07771() {
        }

        public void run() {
            BOSHClient.this.processMessages();
        }
    }

    /* renamed from: com.kenai.jbosh.BOSHClient.2 */
    class C07782 implements Runnable {
        C07782() {
        }

        public void run() {
            BOSHClient.this.sendEmptyRequest();
        }
    }

    static abstract class ExchangeInterceptor {
        abstract HTTPExchange interceptExchange(HTTPExchange hTTPExchange);

        ExchangeInterceptor() {
        }
    }

    static {
        boolean z = true;
        $assertionsDisabled = !BOSHClient.class.desiredAssertionStatus() ? true : ASSERTIONS;
        LOG = Logger.getLogger(BOSHClient.class.getName());
        EMPTY_REQUEST_DELAY = Integer.getInteger(BOSHClient.class.getName() + ".emptyRequestDelay", DEFAULT_EMPTY_REQUEST_DELAY).intValue();
        PAUSE_MARGIN = Integer.getInteger(BOSHClient.class.getName() + ".pauseMargin", DEFAULT_PAUSE_MARGIN).intValue();
        String str = BOSHClient.class.getSimpleName() + ".assertionsEnabled";
        if (System.getProperty(str) != null) {
            z = Boolean.getBoolean(str);
        } else if ($assertionsDisabled) {
            z = ASSERTIONS;
        }
        ASSERTIONS = z;
    }

    private BOSHClient(BOSHClientConfig bOSHClientConfig) {
        this.connListeners = new CopyOnWriteArraySet();
        this.requestListeners = new CopyOnWriteArraySet();
        this.responseListeners = new CopyOnWriteArraySet();
        this.lock = new ReentrantLock();
        this.notEmpty = this.lock.newCondition();
        this.notFull = this.lock.newCondition();
        this.drained = this.lock.newCondition();
        this.procRunnable = new C07771();
        this.emptyRequestRunnable = new C07782();
        this.httpSender = new ApacheHTTPSender();
        this.exchInterceptor = new AtomicReference();
        this.requestIDSeq = new RequestIDSequence();
        this.schedExec = Executors.newSingleThreadScheduledExecutor();
        this.exchanges = new LinkedList();
        this.pendingResponseAcks = new TreeSet();
        this.responseAck = Long.valueOf(-1);
        this.pendingRequestAcks = new ArrayList();
        this.cfg = bOSHClientConfig;
        init();
    }

    public static BOSHClient create(BOSHClientConfig bOSHClientConfig) {
        if (bOSHClientConfig != null) {
            return new BOSHClient(bOSHClientConfig);
        }
        throw new IllegalArgumentException("Client configuration may not be null");
    }

    public BOSHClientConfig getBOSHClientConfig() {
        return this.cfg;
    }

    public void addBOSHClientConnListener(BOSHClientConnListener bOSHClientConnListener) {
        if (bOSHClientConnListener == null) {
            throw new IllegalArgumentException(NULL_LISTENER);
        }
        this.connListeners.add(bOSHClientConnListener);
    }

    public void removeBOSHClientConnListener(BOSHClientConnListener bOSHClientConnListener) {
        if (bOSHClientConnListener == null) {
            throw new IllegalArgumentException(NULL_LISTENER);
        }
        this.connListeners.remove(bOSHClientConnListener);
    }

    public void addBOSHClientRequestListener(BOSHClientRequestListener bOSHClientRequestListener) {
        if (bOSHClientRequestListener == null) {
            throw new IllegalArgumentException(NULL_LISTENER);
        }
        this.requestListeners.add(bOSHClientRequestListener);
    }

    public void removeBOSHClientRequestListener(BOSHClientRequestListener bOSHClientRequestListener) {
        if (bOSHClientRequestListener == null) {
            throw new IllegalArgumentException(NULL_LISTENER);
        }
        this.requestListeners.remove(bOSHClientRequestListener);
    }

    public void addBOSHClientResponseListener(BOSHClientResponseListener bOSHClientResponseListener) {
        if (bOSHClientResponseListener == null) {
            throw new IllegalArgumentException(NULL_LISTENER);
        }
        this.responseListeners.add(bOSHClientResponseListener);
    }

    public void removeBOSHClientResponseListener(BOSHClientResponseListener bOSHClientResponseListener) {
        if (bOSHClientResponseListener == null) {
            throw new IllegalArgumentException(NULL_LISTENER);
        }
        this.responseListeners.remove(bOSHClientResponseListener);
    }

    public void send(ComposableBody composableBody) throws BOSHException {
        HTTPExchange hTTPExchange;
        assertUnlocked();
        if (composableBody == null) {
            throw new IllegalArgumentException("Message body may not be null");
        }
        this.lock.lock();
        blockUntilSendable(composableBody);
        if (isWorking() || isTermination(composableBody)) {
            try {
                AbstractBody applySessionCreationRequest;
                long nextRID = this.requestIDSeq.getNextRID();
                CMSessionParams cMSessionParams = this.cmParams;
                if (cMSessionParams == null && this.exchanges.isEmpty()) {
                    applySessionCreationRequest = applySessionCreationRequest(nextRID, composableBody);
                } else {
                    applySessionCreationRequest = applySessionData(nextRID, composableBody);
                    if (this.cmParams.isAckingRequests()) {
                        this.pendingRequestAcks.add(applySessionCreationRequest);
                    }
                }
                hTTPExchange = new HTTPExchange(applySessionCreationRequest);
                this.exchanges.add(hTTPExchange);
                this.notEmpty.signalAll();
                clearEmptyRequest();
                applySessionCreationRequest = hTTPExchange.getRequest();
                hTTPExchange.setHTTPResponse(this.httpSender.send(cMSessionParams, applySessionCreationRequest));
                fireRequestSent(applySessionCreationRequest);
            } finally {
                hTTPExchange = this.lock;
                hTTPExchange.unlock();
            }
        } else {
            throw new BOSHException("Cannot send message when session is closed");
        }
    }

    public boolean pause() {
        assertUnlocked();
        this.lock.lock();
        try {
            if (this.cmParams == null) {
                return ASSERTIONS;
            }
            AttrMaxPause maxPause = this.cmParams.getMaxPause();
            if (maxPause == null) {
                this.lock.unlock();
                return ASSERTIONS;
            }
            this.lock.unlock();
            try {
                send(ComposableBody.builder().setAttribute(Attributes.PAUSE, maxPause.toString()).build());
            } catch (Throwable e) {
                LOG.log(Level.FINEST, "Could not send pause", e);
            }
            return true;
        } finally {
            this.lock.unlock();
        }
    }

    public void disconnect() throws BOSHException {
        disconnect(ComposableBody.builder().build());
    }

    public void disconnect(ComposableBody composableBody) throws BOSHException {
        if (composableBody == null) {
            throw new IllegalArgumentException("Message body may not be null");
        }
        Builder rebuild = composableBody.rebuild();
        rebuild.setAttribute(Attributes.TYPE, TERMINATE);
        send(rebuild.build());
    }

    public void close() {
        dispose(new BOSHException("Session explicitly closed by caller"));
    }

    CMSessionParams getCMSessionParams() {
        this.lock.lock();
        try {
            CMSessionParams cMSessionParams = this.cmParams;
            return cMSessionParams;
        } finally {
            this.lock.unlock();
        }
    }

    void drain() {
        this.lock.lock();
        LOG.finest("Waiting while draining...");
        while (isWorking() && (this.emptyRequestFuture == null || this.emptyRequestFuture.isDone())) {
            try {
                this.drained.await();
            } catch (Throwable e) {
                LOG.log(Level.FINEST, INTERRUPTED, e);
            } catch (Throwable th) {
                this.lock.unlock();
            }
        }
        LOG.finest("Drained");
        this.lock.unlock();
    }

    void setExchangeInterceptor(ExchangeInterceptor exchangeInterceptor) {
        this.exchInterceptor.set(exchangeInterceptor);
    }

    private void init() {
        assertUnlocked();
        this.lock.lock();
        try {
            this.httpSender.init(this.cfg);
            this.procThread = new Thread(this.procRunnable);
            this.procThread.setDaemon(true);
            this.procThread.setName(BOSHClient.class.getSimpleName() + "[" + System.identityHashCode(this) + "]: Receive thread");
            this.procThread.start();
        } finally {
            this.lock.unlock();
        }
    }

    private void dispose(Throwable th) {
        assertUnlocked();
        this.lock.lock();
        try {
            if (this.procThread != null) {
                this.procThread = null;
                this.lock.unlock();
                if (th == null) {
                    fireConnectionClosed();
                } else {
                    fireConnectionClosedOnError(th);
                }
                this.lock.lock();
                try {
                    clearEmptyRequest();
                    this.exchanges = null;
                    this.cmParams = null;
                    this.pendingResponseAcks = null;
                    this.pendingRequestAcks = null;
                    this.notEmpty.signalAll();
                    this.notFull.signalAll();
                    this.drained.signalAll();
                    this.httpSender.destroy();
                    this.schedExec.shutdownNow();
                } finally {
                    this.lock.unlock();
                }
            }
        } finally {
            this.lock.unlock();
        }
    }

    private static boolean isPause(AbstractBody abstractBody) {
        return abstractBody.getAttribute(Attributes.PAUSE) != null ? true : ASSERTIONS;
    }

    private static boolean isTermination(AbstractBody abstractBody) {
        return TERMINATE.equals(abstractBody.getAttribute(Attributes.TYPE));
    }

    private TerminalBindingCondition getTerminalBindingCondition(int i, AbstractBody abstractBody) {
        assertLocked();
        if (isTermination(abstractBody)) {
            return TerminalBindingCondition.forString(abstractBody.getAttribute(Attributes.CONDITION));
        }
        if (this.cmParams == null || this.cmParams.getVersion() != null) {
            return null;
        }
        return TerminalBindingCondition.forHTTPResponseCode(i);
    }

    private boolean isImmediatelySendable(AbstractBody abstractBody) {
        assertLocked();
        if (this.cmParams == null) {
            return this.exchanges.isEmpty();
        }
        AttrRequests requests = this.cmParams.getRequests();
        if (requests == null) {
            return true;
        }
        int intValue = requests.intValue();
        if (this.exchanges.size() < intValue) {
            return true;
        }
        if (this.exchanges.size() == intValue && (isTermination(abstractBody) || isPause(abstractBody))) {
            return true;
        }
        return ASSERTIONS;
    }

    private boolean isWorking() {
        assertLocked();
        return this.procThread != null ? true : ASSERTIONS;
    }

    private void blockUntilSendable(AbstractBody abstractBody) {
        assertLocked();
        while (isWorking() && !isImmediatelySendable(abstractBody)) {
            try {
                this.notFull.await();
            } catch (Throwable e) {
                LOG.log(Level.FINEST, INTERRUPTED, e);
            }
        }
    }

    private ComposableBody applySessionCreationRequest(long j, ComposableBody composableBody) throws BOSHException {
        assertLocked();
        Builder rebuild = composableBody.rebuild();
        rebuild.setAttribute(Attributes.TO, this.cfg.getTo());
        rebuild.setAttribute(Attributes.XML_LANG, this.cfg.getLang());
        rebuild.setAttribute(Attributes.VER, AttrVersion.getSupportedVersion().toString());
        rebuild.setAttribute(Attributes.WAIT, "60");
        rebuild.setAttribute(Attributes.HOLD, Contact.RELATION_FRIEND);
        rebuild.setAttribute(Attributes.RID, Long.toString(j));
        applyRoute(rebuild);
        applyFrom(rebuild);
        rebuild.setAttribute(Attributes.ACK, Contact.RELATION_FRIEND);
        rebuild.setAttribute(Attributes.SID, null);
        return rebuild.build();
    }

    private void applyRoute(Builder builder) {
        assertLocked();
        String route = this.cfg.getRoute();
        if (route != null) {
            builder.setAttribute(Attributes.ROUTE, route);
        }
    }

    private void applyFrom(Builder builder) {
        assertLocked();
        String from = this.cfg.getFrom();
        if (from != null) {
            builder.setAttribute(Attributes.FROM, from);
        }
    }

    private ComposableBody applySessionData(long j, ComposableBody composableBody) throws BOSHException {
        assertLocked();
        Builder rebuild = composableBody.rebuild();
        rebuild.setAttribute(Attributes.SID, this.cmParams.getSessionID().toString());
        rebuild.setAttribute(Attributes.RID, Long.toString(j));
        applyResponseAcknowledgement(rebuild, j);
        return rebuild.build();
    }

    private void applyResponseAcknowledgement(Builder builder, long j) {
        assertLocked();
        if (!this.responseAck.equals(Long.valueOf(-1))) {
            if (!this.responseAck.equals(Long.valueOf(j - 1))) {
                builder.setAttribute(Attributes.ACK, this.responseAck.toString());
            }
        }
    }

    private void processMessages() {
        LOG.log(Level.FINEST, "Processing thread starting");
        while (true) {
            HTTPExchange nextExchange = nextExchange();
            if (nextExchange == null) {
                LOG.log(Level.FINEST, "Processing thread exiting");
                return;
            }
            HTTPExchange interceptExchange;
            ExchangeInterceptor exchangeInterceptor = (ExchangeInterceptor) this.exchInterceptor.get();
            if (exchangeInterceptor != null) {
                interceptExchange = exchangeInterceptor.interceptExchange(nextExchange);
                if (interceptExchange == null) {
                    LOG.log(Level.FINE, "Discarding exchange on request of test hook: RID=" + nextExchange.getRequest().getAttribute(Attributes.RID));
                    this.lock.lock();
                    try {
                        this.exchanges.remove(nextExchange);
                        this.lock.unlock();
                    } catch (Throwable th) {
                        LOG.log(Level.FINEST, "Processing thread exiting");
                    }
                }
            } else {
                interceptExchange = nextExchange;
            }
            processExchange(interceptExchange);
        }
    }

    private HTTPExchange nextExchange() {
        assertUnlocked();
        Thread currentThread = Thread.currentThread();
        HTTPExchange hTTPExchange = null;
        this.lock.lock();
        do {
            if (!currentThread.equals(this.procThread)) {
                break;
            }
            try {
                hTTPExchange = (HTTPExchange) this.exchanges.peek();
                if (hTTPExchange == null) {
                    this.notEmpty.await();
                    continue;
                }
            } catch (Throwable e) {
                LOG.log(Level.FINEST, INTERRUPTED, e);
                continue;
            } catch (Throwable th) {
                this.lock.unlock();
            }
        } while (hTTPExchange == null);
        this.lock.unlock();
        return hTTPExchange;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void processExchange(com.kenai.jbosh.HTTPExchange r7) {
        /*
        r6 = this;
        r0 = 0;
        r6.assertUnlocked();
        r1 = r7.getHTTPResponse();	 Catch:{ BOSHException -> 0x0064, InterruptedException -> 0x0072 }
        r2 = r1.getBody();	 Catch:{ BOSHException -> 0x0064, InterruptedException -> 0x0072 }
        r1 = r1.getHTTPStatus();	 Catch:{ BOSHException -> 0x0064, InterruptedException -> 0x0072 }
        r6.fireResponseReceived(r2);
        r3 = r7.getRequest();
        r4 = r6.lock;
        r4.lock();
        r4 = r6.cmParams;	 Catch:{ BOSHException -> 0x00b9 }
        if (r4 != 0) goto L_0x0029;
    L_0x0020:
        r4 = com.kenai.jbosh.CMSessionParams.fromSessionInit(r3, r2);	 Catch:{ BOSHException -> 0x00b9 }
        r6.cmParams = r4;	 Catch:{ BOSHException -> 0x00b9 }
        r6.fireConnectionEstablished();	 Catch:{ BOSHException -> 0x00b9 }
    L_0x0029:
        r4 = r6.cmParams;	 Catch:{ BOSHException -> 0x00b9 }
        r6.checkForTerminalBindingConditions(r2, r1);	 Catch:{ BOSHException -> 0x00b9 }
        r1 = isTermination(r2);	 Catch:{ BOSHException -> 0x00b9 }
        if (r1 == 0) goto L_0x0087;
    L_0x0034:
        r0 = r6.lock;	 Catch:{ BOSHException -> 0x00b9 }
        r0.unlock();	 Catch:{ BOSHException -> 0x00b9 }
        r0 = 0;
        r6.dispose(r0);	 Catch:{ BOSHException -> 0x00b9 }
        r0 = r6.lock;
        r0 = r0.isHeldByCurrentThread();
        if (r0 == 0) goto L_0x0063;
    L_0x0045:
        r0 = r6.exchanges;	 Catch:{ all -> 0x0080 }
        r0.remove(r7);	 Catch:{ all -> 0x0080 }
        r0 = r6.exchanges;	 Catch:{ all -> 0x0080 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0080 }
        if (r0 == 0) goto L_0x0059;
    L_0x0052:
        r0 = r6.processPauseRequest(r3);	 Catch:{ all -> 0x0080 }
        r6.scheduleEmptyRequest(r0);	 Catch:{ all -> 0x0080 }
    L_0x0059:
        r0 = r6.notFull;	 Catch:{ all -> 0x0080 }
        r0.signalAll();	 Catch:{ all -> 0x0080 }
        r0 = r6.lock;
        r0.unlock();
    L_0x0063:
        return;
    L_0x0064:
        r0 = move-exception;
        r1 = LOG;
        r2 = java.util.logging.Level.FINEST;
        r3 = "Could not obtain response";
        r1.log(r2, r3, r0);
        r6.dispose(r0);
        goto L_0x0063;
    L_0x0072:
        r0 = move-exception;
        r1 = LOG;
        r2 = java.util.logging.Level.FINEST;
        r3 = "Interrupted";
        r1.log(r2, r3, r0);
        r6.dispose(r0);
        goto L_0x0063;
    L_0x0080:
        r0 = move-exception;
        r1 = r6.lock;
        r1.unlock();
        throw r0;
    L_0x0087:
        r1 = isRecoverableBindingCondition(r2);	 Catch:{ BOSHException -> 0x00b9 }
        if (r1 == 0) goto L_0x017f;
    L_0x008d:
        if (r0 != 0) goto L_0x01b1;
    L_0x008f:
        r1 = new java.util.ArrayList;	 Catch:{ BOSHException -> 0x00b9 }
        r0 = r6.exchanges;	 Catch:{ BOSHException -> 0x00b9 }
        r0 = r0.size();	 Catch:{ BOSHException -> 0x00b9 }
        r1.<init>(r0);	 Catch:{ BOSHException -> 0x00b9 }
    L_0x009a:
        r0 = r6.exchanges;	 Catch:{ BOSHException -> 0x00b9 }
        r2 = r0.iterator();	 Catch:{ BOSHException -> 0x00b9 }
    L_0x00a0:
        r0 = r2.hasNext();	 Catch:{ BOSHException -> 0x00b9 }
        if (r0 == 0) goto L_0x00f3;
    L_0x00a6:
        r0 = r2.next();	 Catch:{ BOSHException -> 0x00b9 }
        r0 = (com.kenai.jbosh.HTTPExchange) r0;	 Catch:{ BOSHException -> 0x00b9 }
        r5 = new com.kenai.jbosh.HTTPExchange;	 Catch:{ BOSHException -> 0x00b9 }
        r0 = r0.getRequest();	 Catch:{ BOSHException -> 0x00b9 }
        r5.<init>(r0);	 Catch:{ BOSHException -> 0x00b9 }
        r1.add(r5);	 Catch:{ BOSHException -> 0x00b9 }
        goto L_0x00a0;
    L_0x00b9:
        r0 = move-exception;
        r1 = LOG;	 Catch:{ all -> 0x0109 }
        r2 = java.util.logging.Level.FINEST;	 Catch:{ all -> 0x0109 }
        r4 = "Could not process response";
        r1.log(r2, r4, r0);	 Catch:{ all -> 0x0109 }
        r1 = r6.lock;	 Catch:{ all -> 0x0109 }
        r1.unlock();	 Catch:{ all -> 0x0109 }
        r6.dispose(r0);	 Catch:{ all -> 0x0109 }
        r0 = r6.lock;
        r0 = r0.isHeldByCurrentThread();
        if (r0 == 0) goto L_0x0063;
    L_0x00d3:
        r0 = r6.exchanges;	 Catch:{ all -> 0x01a3 }
        r0.remove(r7);	 Catch:{ all -> 0x01a3 }
        r0 = r6.exchanges;	 Catch:{ all -> 0x01a3 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x01a3 }
        if (r0 == 0) goto L_0x00e7;
    L_0x00e0:
        r0 = r6.processPauseRequest(r3);	 Catch:{ all -> 0x01a3 }
        r6.scheduleEmptyRequest(r0);	 Catch:{ all -> 0x01a3 }
    L_0x00e7:
        r0 = r6.notFull;	 Catch:{ all -> 0x01a3 }
        r0.signalAll();	 Catch:{ all -> 0x01a3 }
        r0 = r6.lock;
        r0.unlock();
        goto L_0x0063;
    L_0x00f3:
        r2 = r1.iterator();	 Catch:{ BOSHException -> 0x00b9 }
    L_0x00f7:
        r0 = r2.hasNext();	 Catch:{ BOSHException -> 0x00b9 }
        if (r0 == 0) goto L_0x0131;
    L_0x00fd:
        r0 = r2.next();	 Catch:{ BOSHException -> 0x00b9 }
        r0 = (com.kenai.jbosh.HTTPExchange) r0;	 Catch:{ BOSHException -> 0x00b9 }
        r5 = r6.exchanges;	 Catch:{ BOSHException -> 0x00b9 }
        r5.add(r0);	 Catch:{ BOSHException -> 0x00b9 }
        goto L_0x00f7;
    L_0x0109:
        r0 = move-exception;
        r1 = r6.lock;
        r1 = r1.isHeldByCurrentThread();
        if (r1 == 0) goto L_0x0130;
    L_0x0112:
        r1 = r6.exchanges;	 Catch:{ all -> 0x01aa }
        r1.remove(r7);	 Catch:{ all -> 0x01aa }
        r1 = r6.exchanges;	 Catch:{ all -> 0x01aa }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x01aa }
        if (r1 == 0) goto L_0x0126;
    L_0x011f:
        r1 = r6.processPauseRequest(r3);	 Catch:{ all -> 0x01aa }
        r6.scheduleEmptyRequest(r1);	 Catch:{ all -> 0x01aa }
    L_0x0126:
        r1 = r6.notFull;	 Catch:{ all -> 0x01aa }
        r1.signalAll();	 Catch:{ all -> 0x01aa }
        r1 = r6.lock;
        r1.unlock();
    L_0x0130:
        throw r0;
    L_0x0131:
        r0 = r1;
    L_0x0132:
        r1 = r6.lock;
        r1 = r1.isHeldByCurrentThread();
        if (r1 == 0) goto L_0x0158;
    L_0x013a:
        r1 = r6.exchanges;	 Catch:{ all -> 0x019c }
        r1.remove(r7);	 Catch:{ all -> 0x019c }
        r1 = r6.exchanges;	 Catch:{ all -> 0x019c }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x019c }
        if (r1 == 0) goto L_0x014e;
    L_0x0147:
        r1 = r6.processPauseRequest(r3);	 Catch:{ all -> 0x019c }
        r6.scheduleEmptyRequest(r1);	 Catch:{ all -> 0x019c }
    L_0x014e:
        r1 = r6.notFull;	 Catch:{ all -> 0x019c }
        r1.signalAll();	 Catch:{ all -> 0x019c }
        r1 = r6.lock;
        r1.unlock();
    L_0x0158:
        if (r0 == 0) goto L_0x0063;
    L_0x015a:
        r1 = r0.iterator();
    L_0x015e:
        r0 = r1.hasNext();
        if (r0 == 0) goto L_0x0063;
    L_0x0164:
        r0 = r1.next();
        r0 = (com.kenai.jbosh.HTTPExchange) r0;
        r2 = r6.httpSender;
        r3 = r0.getRequest();
        r2 = r2.send(r4, r3);
        r0.setHTTPResponse(r2);
        r0 = r0.getRequest();
        r6.fireRequestSent(r0);
        goto L_0x015e;
    L_0x017f:
        r6.processRequestAcknowledgements(r3, r2);	 Catch:{ BOSHException -> 0x00b9 }
        r6.processResponseAcknowledgementData(r3);	 Catch:{ BOSHException -> 0x00b9 }
        r1 = r6.processResponseAcknowledgementReport(r2);	 Catch:{ BOSHException -> 0x00b9 }
        if (r1 == 0) goto L_0x0132;
    L_0x018b:
        if (r0 != 0) goto L_0x0132;
    L_0x018d:
        r0 = new java.util.ArrayList;	 Catch:{ BOSHException -> 0x00b9 }
        r2 = 1;
        r0.<init>(r2);	 Catch:{ BOSHException -> 0x00b9 }
        r0.add(r1);	 Catch:{ BOSHException -> 0x00b9 }
        r2 = r6.exchanges;	 Catch:{ BOSHException -> 0x00b9 }
        r2.add(r1);	 Catch:{ BOSHException -> 0x00b9 }
        goto L_0x0132;
    L_0x019c:
        r0 = move-exception;
        r1 = r6.lock;
        r1.unlock();
        throw r0;
    L_0x01a3:
        r0 = move-exception;
        r1 = r6.lock;
        r1.unlock();
        throw r0;
    L_0x01aa:
        r0 = move-exception;
        r1 = r6.lock;
        r1.unlock();
        throw r0;
    L_0x01b1:
        r1 = r0;
        goto L_0x009a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.kenai.jbosh.BOSHClient.processExchange(com.kenai.jbosh.HTTPExchange):void");
    }

    private void clearEmptyRequest() {
        assertLocked();
        if (this.emptyRequestFuture != null) {
            this.emptyRequestFuture.cancel(ASSERTIONS);
            this.emptyRequestFuture = null;
        }
    }

    private long getDefaultEmptyRequestDelay() {
        assertLocked();
        AttrPolling pollingInterval = this.cmParams.getPollingInterval();
        if (pollingInterval == null) {
            return (long) EMPTY_REQUEST_DELAY;
        }
        return (long) pollingInterval.getInMilliseconds();
    }

    private void scheduleEmptyRequest(long j) {
        assertLocked();
        if (j < 0) {
            throw new IllegalArgumentException("Empty request delay must be >= 0 (was: " + j + ")");
        }
        clearEmptyRequest();
        if (isWorking()) {
            if (LOG.isLoggable(Level.FINER)) {
                LOG.finer("Scheduling empty request in " + j + LocaleUtil.MALAY);
            }
            try {
                this.emptyRequestFuture = this.schedExec.schedule(this.emptyRequestRunnable, j, TimeUnit.MILLISECONDS);
            } catch (Throwable e) {
                LOG.log(Level.FINEST, "Could not schedule empty request", e);
            }
            this.drained.signalAll();
        }
    }

    private void sendEmptyRequest() {
        assertUnlocked();
        LOG.finest("Sending empty request");
        try {
            send(ComposableBody.builder().build());
        } catch (Throwable e) {
            dispose(e);
        }
    }

    private void assertLocked() {
        if (ASSERTIONS && !this.lock.isHeldByCurrentThread()) {
            throw new AssertionError("Lock is not held by current thread");
        }
    }

    private void assertUnlocked() {
        if (ASSERTIONS && this.lock.isHeldByCurrentThread()) {
            throw new AssertionError("Lock is held by current thread");
        }
    }

    private void checkForTerminalBindingConditions(AbstractBody abstractBody, int i) throws BOSHException {
        TerminalBindingCondition terminalBindingCondition = getTerminalBindingCondition(i, abstractBody);
        if (terminalBindingCondition != null) {
            throw new BOSHException("Terminal binding condition encountered: " + terminalBindingCondition.getCondition() + "  (" + terminalBindingCondition.getMessage() + ")");
        }
    }

    private static boolean isRecoverableBindingCondition(AbstractBody abstractBody) {
        return ERROR.equals(abstractBody.getAttribute(Attributes.TYPE));
    }

    private long processPauseRequest(AbstractBody abstractBody) {
        assertLocked();
        if (!(this.cmParams == null || this.cmParams.getMaxPause() == null)) {
            try {
                AttrPause createFromString = AttrPause.createFromString(abstractBody.getAttribute(Attributes.PAUSE));
                if (createFromString != null) {
                    long inMilliseconds = (long) (createFromString.getInMilliseconds() - PAUSE_MARGIN);
                    if (inMilliseconds < 0) {
                        return (long) EMPTY_REQUEST_DELAY;
                    }
                    return inMilliseconds;
                }
            } catch (Throwable e) {
                LOG.log(Level.FINEST, "Could not extract", e);
            }
        }
        return getDefaultEmptyRequestDelay();
    }

    private void processRequestAcknowledgements(AbstractBody abstractBody, AbstractBody abstractBody2) {
        assertLocked();
        if (this.cmParams.isAckingRequests() && abstractBody2.getAttribute(Attributes.REPORT) == null) {
            Long valueOf;
            String attribute = abstractBody2.getAttribute(Attributes.ACK);
            if (attribute == null) {
                valueOf = Long.valueOf(Long.parseLong(abstractBody.getAttribute(Attributes.RID)));
            } else {
                valueOf = Long.valueOf(Long.parseLong(attribute));
            }
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("Removing pending acks up to: " + valueOf);
            }
            Iterator it = this.pendingRequestAcks.iterator();
            while (it.hasNext()) {
                if (Long.valueOf(Long.parseLong(((AbstractBody) it.next()).getAttribute(Attributes.RID))).compareTo(valueOf) <= 0) {
                    it.remove();
                }
            }
        }
    }

    private void processResponseAcknowledgementData(AbstractBody abstractBody) {
        assertLocked();
        Long valueOf = Long.valueOf(Long.parseLong(abstractBody.getAttribute(Attributes.RID)));
        if (this.responseAck.equals(Long.valueOf(-1))) {
            this.responseAck = valueOf;
            return;
        }
        this.pendingResponseAcks.add(valueOf);
        for (valueOf = this.responseAck; valueOf.equals(this.pendingResponseAcks.first()); valueOf = Long.valueOf(valueOf.longValue() + 1)) {
            this.responseAck = valueOf;
            this.pendingResponseAcks.remove(valueOf);
        }
    }

    private HTTPExchange processResponseAcknowledgementReport(AbstractBody abstractBody) throws BOSHException {
        AbstractBody abstractBody2 = null;
        assertLocked();
        String attribute = abstractBody.getAttribute(Attributes.REPORT);
        if (attribute == null) {
            return null;
        }
        Long valueOf = Long.valueOf(Long.parseLong(attribute));
        Long valueOf2 = Long.valueOf(Long.parseLong(abstractBody.getAttribute(Attributes.TIME)));
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("Received report of missing request (RID=" + valueOf + ", time=" + valueOf2 + "ms)");
        }
        Iterator it = this.pendingRequestAcks.iterator();
        while (it.hasNext() && abstractBody2 == null) {
            AbstractBody abstractBody3 = (AbstractBody) it.next();
            if (!valueOf.equals(Long.valueOf(Long.parseLong(abstractBody3.getAttribute(Attributes.RID))))) {
                abstractBody3 = abstractBody2;
            }
            abstractBody2 = abstractBody3;
        }
        if (abstractBody2 == null) {
            throw new BOSHException("Report of missing message with RID '" + attribute + "' but local copy of that request was not found");
        }
        HTTPExchange hTTPExchange = new HTTPExchange(abstractBody2);
        this.exchanges.add(hTTPExchange);
        this.notEmpty.signalAll();
        return hTTPExchange;
    }

    private void fireRequestSent(AbstractBody abstractBody) {
        assertUnlocked();
        BOSHMessageEvent bOSHMessageEvent = null;
        for (BOSHClientRequestListener bOSHClientRequestListener : this.requestListeners) {
            if (bOSHMessageEvent == null) {
                bOSHMessageEvent = BOSHMessageEvent.createRequestSentEvent(this, abstractBody);
            }
            try {
                bOSHClientRequestListener.requestSent(bOSHMessageEvent);
            } catch (Throwable e) {
                LOG.log(Level.WARNING, UNHANDLED, e);
            }
        }
    }

    private void fireResponseReceived(AbstractBody abstractBody) {
        assertUnlocked();
        BOSHMessageEvent bOSHMessageEvent = null;
        for (BOSHClientResponseListener bOSHClientResponseListener : this.responseListeners) {
            if (bOSHMessageEvent == null) {
                bOSHMessageEvent = BOSHMessageEvent.createResponseReceivedEvent(this, abstractBody);
            }
            try {
                bOSHClientResponseListener.responseReceived(bOSHMessageEvent);
            } catch (Throwable e) {
                LOG.log(Level.WARNING, UNHANDLED, e);
            }
        }
    }

    private void fireConnectionEstablished() {
        boolean isHeldByCurrentThread = this.lock.isHeldByCurrentThread();
        if (isHeldByCurrentThread) {
            this.lock.unlock();
        }
        try {
            BOSHClientConnEvent bOSHClientConnEvent = null;
            for (BOSHClientConnListener bOSHClientConnListener : this.connListeners) {
                if (bOSHClientConnEvent == null) {
                    bOSHClientConnEvent = BOSHClientConnEvent.createConnectionEstablishedEvent(this);
                }
                bOSHClientConnListener.connectionEvent(bOSHClientConnEvent);
            }
            if (isHeldByCurrentThread) {
                this.lock.lock();
            }
        } catch (Throwable e) {
            LOG.log(Level.WARNING, UNHANDLED, e);
        } catch (Throwable th) {
            if (isHeldByCurrentThread) {
                this.lock.lock();
            }
        }
    }

    private void fireConnectionClosed() {
        assertUnlocked();
        BOSHClientConnEvent bOSHClientConnEvent = null;
        for (BOSHClientConnListener bOSHClientConnListener : this.connListeners) {
            if (bOSHClientConnEvent == null) {
                bOSHClientConnEvent = BOSHClientConnEvent.createConnectionClosedEvent(this);
            }
            try {
                bOSHClientConnListener.connectionEvent(bOSHClientConnEvent);
            } catch (Throwable e) {
                LOG.log(Level.WARNING, UNHANDLED, e);
            }
        }
    }

    private void fireConnectionClosedOnError(Throwable th) {
        assertUnlocked();
        BOSHClientConnEvent bOSHClientConnEvent = null;
        for (BOSHClientConnListener bOSHClientConnListener : this.connListeners) {
            if (bOSHClientConnEvent == null) {
                bOSHClientConnEvent = BOSHClientConnEvent.createConnectionClosedOnErrorEvent(this, this.pendingRequestAcks, th);
            }
            try {
                bOSHClientConnListener.connectionEvent(bOSHClientConnEvent);
            } catch (Throwable e) {
                LOG.log(Level.WARNING, UNHANDLED, e);
            }
        }
    }
}

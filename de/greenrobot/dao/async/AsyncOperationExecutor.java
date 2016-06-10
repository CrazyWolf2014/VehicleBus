package de.greenrobot.dao.async;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import de.greenrobot.dao.DaoException;
import de.greenrobot.dao.DaoLog;
import de.greenrobot.dao.async.AsyncOperation.OperationType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

class AsyncOperationExecutor implements Runnable, Callback {
    private static ExecutorService executorService;
    private int countOperationsCompleted;
    private int countOperationsEnqueued;
    private volatile boolean executorRunning;
    private Handler handlerMainThread;
    private int lastSequenceNumber;
    private volatile AsyncOperationListener listener;
    private volatile AsyncOperationListener listenerMainThread;
    private volatile int maxOperationCountToMerge;
    private final BlockingQueue<AsyncOperation> queue;
    private volatile int waitForMergeMillis;

    /* renamed from: de.greenrobot.dao.async.AsyncOperationExecutor.1 */
    static /* synthetic */ class C09071 {
        static final /* synthetic */ int[] $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType;

        static {
            $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType = new int[OperationType.values().length];
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.Delete.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.DeleteInTxIterable.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.DeleteInTxArray.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.Insert.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.InsertInTxIterable.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.InsertInTxArray.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.InsertOrReplace.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.InsertOrReplaceInTxIterable.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.InsertOrReplaceInTxArray.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.Update.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.UpdateInTxIterable.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.UpdateInTxArray.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.TransactionRunnable.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.TransactionCallable.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.QueryList.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.QueryUnique.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.DeleteByKey.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.DeleteAll.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.Load.ordinal()] = 19;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.LoadAll.ordinal()] = 20;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.Count.ordinal()] = 21;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType[OperationType.Refresh.ordinal()] = 22;
            } catch (NoSuchFieldError e22) {
            }
        }
    }

    static {
        executorService = Executors.newCachedThreadPool();
    }

    AsyncOperationExecutor() {
        this.queue = new LinkedBlockingQueue();
        this.maxOperationCountToMerge = 50;
        this.waitForMergeMillis = 50;
    }

    public void enqueue(AsyncOperation operation) {
        synchronized (this) {
            int i = this.lastSequenceNumber + 1;
            this.lastSequenceNumber = i;
            operation.sequenceNumber = i;
            this.queue.add(operation);
            this.countOperationsEnqueued++;
            if (!this.executorRunning) {
                this.executorRunning = true;
                executorService.execute(this);
            }
        }
    }

    public int getMaxOperationCountToMerge() {
        return this.maxOperationCountToMerge;
    }

    public void setMaxOperationCountToMerge(int maxOperationCountToMerge) {
        this.maxOperationCountToMerge = maxOperationCountToMerge;
    }

    public int getWaitForMergeMillis() {
        return this.waitForMergeMillis;
    }

    public void setWaitForMergeMillis(int waitForMergeMillis) {
        this.waitForMergeMillis = waitForMergeMillis;
    }

    public AsyncOperationListener getListener() {
        return this.listener;
    }

    public void setListener(AsyncOperationListener listener) {
        this.listener = listener;
    }

    public AsyncOperationListener getListenerMainThread() {
        return this.listenerMainThread;
    }

    public void setListenerMainThread(AsyncOperationListener listenerMainThread) {
        this.listenerMainThread = listenerMainThread;
    }

    public synchronized boolean isCompleted() {
        return this.countOperationsEnqueued == this.countOperationsCompleted;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void waitForCompletion() {
        /*
        r3 = this;
        monitor-enter(r3);
    L_0x0001:
        r1 = r3.isCompleted();	 Catch:{ all -> 0x0014 }
        if (r1 != 0) goto L_0x0017;
    L_0x0007:
        r3.wait();	 Catch:{ InterruptedException -> 0x000b }
        goto L_0x0001;
    L_0x000b:
        r0 = move-exception;
        r1 = new de.greenrobot.dao.DaoException;	 Catch:{ all -> 0x0014 }
        r2 = "Interrupted while waiting for all operations to complete";
        r1.<init>(r2, r0);	 Catch:{ all -> 0x0014 }
        throw r1;	 Catch:{ all -> 0x0014 }
    L_0x0014:
        r1 = move-exception;
        monitor-exit(r3);
        throw r1;
    L_0x0017:
        monitor-exit(r3);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: de.greenrobot.dao.async.AsyncOperationExecutor.waitForCompletion():void");
    }

    public synchronized boolean waitForCompletion(int maxMillis) {
        if (!isCompleted()) {
            try {
                wait((long) maxMillis);
            } catch (InterruptedException e) {
                throw new DaoException("Interrupted while waiting for all operations to complete", e);
            }
        }
        return isCompleted();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r9 = this;
        r8 = 0;
    L_0x0001:
        r4 = r9.queue;	 Catch:{ InterruptedException -> 0x0045 }
        r5 = 1;
        r7 = java.util.concurrent.TimeUnit.SECONDS;	 Catch:{ InterruptedException -> 0x0045 }
        r2 = r4.poll(r5, r7);	 Catch:{ InterruptedException -> 0x0045 }
        r2 = (de.greenrobot.dao.async.AsyncOperation) r2;	 Catch:{ InterruptedException -> 0x0045 }
        if (r2 != 0) goto L_0x0024;
    L_0x000f:
        monitor-enter(r9);	 Catch:{ InterruptedException -> 0x0045 }
        r4 = r9.queue;	 Catch:{ all -> 0x0067 }
        r4 = r4.poll();	 Catch:{ all -> 0x0067 }
        r0 = r4;
        r0 = (de.greenrobot.dao.async.AsyncOperation) r0;	 Catch:{ all -> 0x0067 }
        r2 = r0;
        if (r2 != 0) goto L_0x0023;
    L_0x001c:
        r4 = 0;
        r9.executorRunning = r4;	 Catch:{ all -> 0x0067 }
        monitor-exit(r9);	 Catch:{ all -> 0x0067 }
        r9.executorRunning = r8;
    L_0x0022:
        return;
    L_0x0023:
        monitor-exit(r9);	 Catch:{ all -> 0x0067 }
    L_0x0024:
        if (r2 == 0) goto L_0x0001;
    L_0x0026:
        r4 = r2.isMergeTx();	 Catch:{ InterruptedException -> 0x0045 }
        if (r4 == 0) goto L_0x0075;
    L_0x002c:
        r4 = r9.queue;	 Catch:{ InterruptedException -> 0x0045 }
        r5 = r9.waitForMergeMillis;	 Catch:{ InterruptedException -> 0x0045 }
        r5 = (long) r5;	 Catch:{ InterruptedException -> 0x0045 }
        r7 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ InterruptedException -> 0x0045 }
        r3 = r4.poll(r5, r7);	 Catch:{ InterruptedException -> 0x0045 }
        r3 = (de.greenrobot.dao.async.AsyncOperation) r3;	 Catch:{ InterruptedException -> 0x0045 }
        if (r3 == 0) goto L_0x0075;
    L_0x003b:
        r4 = r2.isMergeableWith(r3);	 Catch:{ InterruptedException -> 0x0045 }
        if (r4 == 0) goto L_0x006e;
    L_0x0041:
        r9.mergeTxAndExecute(r2, r3);	 Catch:{ InterruptedException -> 0x0045 }
        goto L_0x0001;
    L_0x0045:
        r1 = move-exception;
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x006a }
        r4.<init>();	 Catch:{ all -> 0x006a }
        r5 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x006a }
        r5 = r5.getName();	 Catch:{ all -> 0x006a }
        r4 = r4.append(r5);	 Catch:{ all -> 0x006a }
        r5 = " was interruppted";
        r4 = r4.append(r5);	 Catch:{ all -> 0x006a }
        r4 = r4.toString();	 Catch:{ all -> 0x006a }
        de.greenrobot.dao.DaoLog.m1710w(r4, r1);	 Catch:{ all -> 0x006a }
        r9.executorRunning = r8;
        goto L_0x0022;
    L_0x0067:
        r4 = move-exception;
        monitor-exit(r9);	 Catch:{ all -> 0x0067 }
        throw r4;	 Catch:{ InterruptedException -> 0x0045 }
    L_0x006a:
        r4 = move-exception;
        r9.executorRunning = r8;
        throw r4;
    L_0x006e:
        r9.executeOperationAndPostCompleted(r2);	 Catch:{ InterruptedException -> 0x0045 }
        r9.executeOperationAndPostCompleted(r3);	 Catch:{ InterruptedException -> 0x0045 }
        goto L_0x0001;
    L_0x0075:
        r9.executeOperationAndPostCompleted(r2);	 Catch:{ InterruptedException -> 0x0045 }
        goto L_0x0001;
        */
        throw new UnsupportedOperationException("Method not decompiled: de.greenrobot.dao.async.AsyncOperationExecutor.run():void");
    }

    private void mergeTxAndExecute(AsyncOperation operation1, AsyncOperation operation2) {
        ArrayList<AsyncOperation> mergedOps = new ArrayList();
        mergedOps.add(operation1);
        mergedOps.add(operation2);
        SQLiteDatabase db = operation1.getDatabase();
        db.beginTransaction();
        boolean failed = false;
        int i = 0;
        while (i < mergedOps.size()) {
            try {
                AsyncOperation operation = (AsyncOperation) mergedOps.get(i);
                executeOperation(operation);
                if (operation.isFailed()) {
                    failed = true;
                    break;
                }
                if (i == mergedOps.size() - 1) {
                    AsyncOperation peekedOp = (AsyncOperation) this.queue.peek();
                    if (i >= this.maxOperationCountToMerge || !operation.isMergeableWith(peekedOp)) {
                        db.setTransactionSuccessful();
                    } else {
                        AsyncOperation removedOp = (AsyncOperation) this.queue.remove();
                        if (removedOp != peekedOp) {
                            throw new DaoException("Internal error: peeked op did not match removed op");
                        }
                        mergedOps.add(removedOp);
                    }
                }
                i++;
            } catch (Throwable th) {
                db.endTransaction();
            }
        }
        db.endTransaction();
        Iterator i$;
        if (failed) {
            DaoLog.m1705i("Revered merged transaction because one of the operations failed. Executing operations one by one instead...");
            i$ = mergedOps.iterator();
            while (i$.hasNext()) {
                AsyncOperation asyncOperation = (AsyncOperation) i$.next();
                asyncOperation.reset();
                executeOperationAndPostCompleted(asyncOperation);
            }
            return;
        }
        int mergedCount = mergedOps.size();
        i$ = mergedOps.iterator();
        while (i$.hasNext()) {
            asyncOperation = (AsyncOperation) i$.next();
            asyncOperation.mergedOperationsCount = mergedCount;
            handleOperationCompleted(asyncOperation);
        }
    }

    private void handleOperationCompleted(AsyncOperation operation) {
        operation.setCompleted();
        AsyncOperationListener listenerToCall = this.listener;
        if (listenerToCall != null) {
            listenerToCall.onAsyncOperationCompleted(operation);
        }
        if (this.listenerMainThread != null) {
            if (this.handlerMainThread == null) {
                this.handlerMainThread = new Handler(Looper.getMainLooper(), this);
            }
            this.handlerMainThread.sendMessage(this.handlerMainThread.obtainMessage(1, operation));
        }
        synchronized (this) {
            this.countOperationsCompleted++;
            if (this.countOperationsCompleted == this.countOperationsEnqueued) {
                notifyAll();
            }
        }
    }

    private void executeOperationAndPostCompleted(AsyncOperation operation) {
        executeOperation(operation);
        handleOperationCompleted(operation);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void executeOperation(de.greenrobot.dao.async.AsyncOperation r5) {
        /*
        r4 = this;
        r1 = java.lang.System.currentTimeMillis();
        r5.timeStarted = r1;
        r1 = de.greenrobot.dao.async.AsyncOperationExecutor.C09071.$SwitchMap$de$greenrobot$dao$async$AsyncOperation$OperationType;	 Catch:{ Throwable -> 0x002e }
        r2 = r5.type;	 Catch:{ Throwable -> 0x002e }
        r2 = r2.ordinal();	 Catch:{ Throwable -> 0x002e }
        r1 = r1[r2];	 Catch:{ Throwable -> 0x002e }
        switch(r1) {
            case 1: goto L_0x0038;
            case 2: goto L_0x0040;
            case 3: goto L_0x004a;
            case 4: goto L_0x0056;
            case 5: goto L_0x005e;
            case 6: goto L_0x0068;
            case 7: goto L_0x0074;
            case 8: goto L_0x007c;
            case 9: goto L_0x0086;
            case 10: goto L_0x0092;
            case 11: goto L_0x009a;
            case 12: goto L_0x00a4;
            case 13: goto L_0x00b0;
            case 14: goto L_0x00b5;
            case 15: goto L_0x00ba;
            case 16: goto L_0x00c6;
            case 17: goto L_0x00d2;
            case 18: goto L_0x00db;
            case 19: goto L_0x00e2;
            case 20: goto L_0x00ee;
            case 21: goto L_0x00f8;
            case 22: goto L_0x0106;
            default: goto L_0x0013;
        };	 Catch:{ Throwable -> 0x002e }
    L_0x0013:
        r1 = new de.greenrobot.dao.DaoException;	 Catch:{ Throwable -> 0x002e }
        r2 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x002e }
        r2.<init>();	 Catch:{ Throwable -> 0x002e }
        r3 = "Unsupported operation: ";
        r2 = r2.append(r3);	 Catch:{ Throwable -> 0x002e }
        r3 = r5.type;	 Catch:{ Throwable -> 0x002e }
        r2 = r2.append(r3);	 Catch:{ Throwable -> 0x002e }
        r2 = r2.toString();	 Catch:{ Throwable -> 0x002e }
        r1.<init>(r2);	 Catch:{ Throwable -> 0x002e }
        throw r1;	 Catch:{ Throwable -> 0x002e }
    L_0x002e:
        r0 = move-exception;
        r5.throwable = r0;
    L_0x0031:
        r1 = java.lang.System.currentTimeMillis();
        r5.timeCompleted = r1;
        return;
    L_0x0038:
        r1 = r5.dao;	 Catch:{ Throwable -> 0x002e }
        r2 = r5.parameter;	 Catch:{ Throwable -> 0x002e }
        r1.delete(r2);	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x0040:
        r2 = r5.dao;	 Catch:{ Throwable -> 0x002e }
        r1 = r5.parameter;	 Catch:{ Throwable -> 0x002e }
        r1 = (java.lang.Iterable) r1;	 Catch:{ Throwable -> 0x002e }
        r2.deleteInTx(r1);	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x004a:
        r2 = r5.dao;	 Catch:{ Throwable -> 0x002e }
        r1 = r5.parameter;	 Catch:{ Throwable -> 0x002e }
        r1 = (java.lang.Object[]) r1;	 Catch:{ Throwable -> 0x002e }
        r1 = (java.lang.Object[]) r1;	 Catch:{ Throwable -> 0x002e }
        r2.deleteInTx(r1);	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x0056:
        r1 = r5.dao;	 Catch:{ Throwable -> 0x002e }
        r2 = r5.parameter;	 Catch:{ Throwable -> 0x002e }
        r1.insert(r2);	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x005e:
        r2 = r5.dao;	 Catch:{ Throwable -> 0x002e }
        r1 = r5.parameter;	 Catch:{ Throwable -> 0x002e }
        r1 = (java.lang.Iterable) r1;	 Catch:{ Throwable -> 0x002e }
        r2.insertInTx(r1);	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x0068:
        r2 = r5.dao;	 Catch:{ Throwable -> 0x002e }
        r1 = r5.parameter;	 Catch:{ Throwable -> 0x002e }
        r1 = (java.lang.Object[]) r1;	 Catch:{ Throwable -> 0x002e }
        r1 = (java.lang.Object[]) r1;	 Catch:{ Throwable -> 0x002e }
        r2.insertInTx(r1);	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x0074:
        r1 = r5.dao;	 Catch:{ Throwable -> 0x002e }
        r2 = r5.parameter;	 Catch:{ Throwable -> 0x002e }
        r1.insertOrReplace(r2);	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x007c:
        r2 = r5.dao;	 Catch:{ Throwable -> 0x002e }
        r1 = r5.parameter;	 Catch:{ Throwable -> 0x002e }
        r1 = (java.lang.Iterable) r1;	 Catch:{ Throwable -> 0x002e }
        r2.insertOrReplaceInTx(r1);	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x0086:
        r2 = r5.dao;	 Catch:{ Throwable -> 0x002e }
        r1 = r5.parameter;	 Catch:{ Throwable -> 0x002e }
        r1 = (java.lang.Object[]) r1;	 Catch:{ Throwable -> 0x002e }
        r1 = (java.lang.Object[]) r1;	 Catch:{ Throwable -> 0x002e }
        r2.insertOrReplaceInTx(r1);	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x0092:
        r1 = r5.dao;	 Catch:{ Throwable -> 0x002e }
        r2 = r5.parameter;	 Catch:{ Throwable -> 0x002e }
        r1.update(r2);	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x009a:
        r2 = r5.dao;	 Catch:{ Throwable -> 0x002e }
        r1 = r5.parameter;	 Catch:{ Throwable -> 0x002e }
        r1 = (java.lang.Iterable) r1;	 Catch:{ Throwable -> 0x002e }
        r2.updateInTx(r1);	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x00a4:
        r2 = r5.dao;	 Catch:{ Throwable -> 0x002e }
        r1 = r5.parameter;	 Catch:{ Throwable -> 0x002e }
        r1 = (java.lang.Object[]) r1;	 Catch:{ Throwable -> 0x002e }
        r1 = (java.lang.Object[]) r1;	 Catch:{ Throwable -> 0x002e }
        r2.updateInTx(r1);	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x00b0:
        r4.executeTransactionRunnable(r5);	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x00b5:
        r4.executeTransactionCallable(r5);	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x00ba:
        r1 = r5.parameter;	 Catch:{ Throwable -> 0x002e }
        r1 = (de.greenrobot.dao.query.Query) r1;	 Catch:{ Throwable -> 0x002e }
        r1 = r1.list();	 Catch:{ Throwable -> 0x002e }
        r5.result = r1;	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x00c6:
        r1 = r5.parameter;	 Catch:{ Throwable -> 0x002e }
        r1 = (de.greenrobot.dao.query.Query) r1;	 Catch:{ Throwable -> 0x002e }
        r1 = r1.unique();	 Catch:{ Throwable -> 0x002e }
        r5.result = r1;	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x00d2:
        r1 = r5.dao;	 Catch:{ Throwable -> 0x002e }
        r2 = r5.parameter;	 Catch:{ Throwable -> 0x002e }
        r1.deleteByKey(r2);	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x00db:
        r1 = r5.dao;	 Catch:{ Throwable -> 0x002e }
        r1.deleteAll();	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x00e2:
        r1 = r5.dao;	 Catch:{ Throwable -> 0x002e }
        r2 = r5.parameter;	 Catch:{ Throwable -> 0x002e }
        r1 = r1.load(r2);	 Catch:{ Throwable -> 0x002e }
        r5.result = r1;	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x00ee:
        r1 = r5.dao;	 Catch:{ Throwable -> 0x002e }
        r1 = r1.loadAll();	 Catch:{ Throwable -> 0x002e }
        r5.result = r1;	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x00f8:
        r1 = r5.dao;	 Catch:{ Throwable -> 0x002e }
        r1 = r1.count();	 Catch:{ Throwable -> 0x002e }
        r1 = java.lang.Long.valueOf(r1);	 Catch:{ Throwable -> 0x002e }
        r5.result = r1;	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
    L_0x0106:
        r1 = r5.dao;	 Catch:{ Throwable -> 0x002e }
        r2 = r5.parameter;	 Catch:{ Throwable -> 0x002e }
        r1.refresh(r2);	 Catch:{ Throwable -> 0x002e }
        goto L_0x0031;
        */
        throw new UnsupportedOperationException("Method not decompiled: de.greenrobot.dao.async.AsyncOperationExecutor.executeOperation(de.greenrobot.dao.async.AsyncOperation):void");
    }

    private void executeTransactionRunnable(AsyncOperation operation) {
        SQLiteDatabase db = operation.getDatabase();
        db.beginTransaction();
        try {
            ((Runnable) operation.parameter).run();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void executeTransactionCallable(AsyncOperation operation) throws Exception {
        SQLiteDatabase db = operation.getDatabase();
        db.beginTransaction();
        try {
            operation.result = ((Callable) operation.parameter).call();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public boolean handleMessage(Message msg) {
        AsyncOperationListener listenerToCall = this.listenerMainThread;
        if (listenerToCall != null) {
            listenerToCall.onAsyncOperationCompleted((AsyncOperation) msg.obj);
        }
        return false;
    }
}

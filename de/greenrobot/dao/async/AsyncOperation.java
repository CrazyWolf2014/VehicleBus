package de.greenrobot.dao.async;

import android.database.sqlite.SQLiteDatabase;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoException;

public class AsyncOperation {
    public static final int FLAG_MERGE_TX = 1;
    public static final int FLAG_STOP_QUEUE_ON_EXCEPTION = 2;
    private volatile boolean completed;
    final AbstractDao<Object, Object> dao;
    private final SQLiteDatabase database;
    final int flags;
    volatile int mergedOperationsCount;
    final Object parameter;
    volatile Object result;
    int sequenceNumber;
    volatile Throwable throwable;
    volatile long timeCompleted;
    volatile long timeStarted;
    final OperationType type;

    public enum OperationType {
        Insert,
        InsertInTxIterable,
        InsertInTxArray,
        InsertOrReplace,
        InsertOrReplaceInTxIterable,
        InsertOrReplaceInTxArray,
        Update,
        UpdateInTxIterable,
        UpdateInTxArray,
        Delete,
        DeleteInTxIterable,
        DeleteInTxArray,
        DeleteByKey,
        DeleteAll,
        TransactionRunnable,
        TransactionCallable,
        QueryList,
        QueryUnique,
        Load,
        LoadAll,
        Count,
        Refresh
    }

    AsyncOperation(OperationType type, AbstractDao<?, ?> dao, Object parameter, int flags) {
        this.type = type;
        this.flags = flags;
        this.dao = dao;
        this.database = null;
        this.parameter = parameter;
    }

    AsyncOperation(OperationType type, SQLiteDatabase database, Object parameter, int flags) {
        this.type = type;
        this.database = database;
        this.flags = flags;
        this.dao = null;
        this.parameter = parameter;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public OperationType getType() {
        return this.type;
    }

    public Object getParameter() {
        return this.parameter;
    }

    public synchronized Object getResult() {
        if (!this.completed) {
            waitForCompletion();
        }
        if (this.throwable != null) {
            throw new AsyncDaoException(this, this.throwable);
        }
        return this.result;
    }

    public boolean isMergeTx() {
        return (this.flags & FLAG_MERGE_TX) != 0;
    }

    SQLiteDatabase getDatabase() {
        return this.database != null ? this.database : this.dao.getDatabase();
    }

    boolean isMergeableWith(AsyncOperation other) {
        return other != null && isMergeTx() && other.isMergeTx() && getDatabase() == other.getDatabase();
    }

    public long getTimeStarted() {
        return this.timeStarted;
    }

    public long getTimeCompleted() {
        return this.timeCompleted;
    }

    public long getDuration() {
        if (this.timeCompleted != 0) {
            return this.timeCompleted - this.timeStarted;
        }
        throw new DaoException("This operation did not yet complete");
    }

    public boolean isFailed() {
        return this.throwable != null;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.Object waitForCompletion() {
        /*
        r3 = this;
        monitor-enter(r3);
    L_0x0001:
        r1 = r3.completed;	 Catch:{ all -> 0x0012 }
        if (r1 != 0) goto L_0x0015;
    L_0x0005:
        r3.wait();	 Catch:{ InterruptedException -> 0x0009 }
        goto L_0x0001;
    L_0x0009:
        r0 = move-exception;
        r1 = new de.greenrobot.dao.DaoException;	 Catch:{ all -> 0x0012 }
        r2 = "Interrupted while waiting for operation to complete";
        r1.<init>(r2, r0);	 Catch:{ all -> 0x0012 }
        throw r1;	 Catch:{ all -> 0x0012 }
    L_0x0012:
        r1 = move-exception;
        monitor-exit(r3);
        throw r1;
    L_0x0015:
        r1 = r3.result;	 Catch:{ all -> 0x0012 }
        monitor-exit(r3);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: de.greenrobot.dao.async.AsyncOperation.waitForCompletion():java.lang.Object");
    }

    public synchronized boolean waitForCompletion(int maxMillis) {
        if (!this.completed) {
            try {
                wait((long) maxMillis);
            } catch (InterruptedException e) {
                throw new DaoException("Interrupted while waiting for operation to complete", e);
            }
        }
        return this.completed;
    }

    synchronized void setCompleted() {
        this.completed = true;
        notifyAll();
    }

    public boolean isCompletedSucessfully() {
        return this.completed && this.throwable == null;
    }

    public int getMergedOperationsCount() {
        return this.mergedOperationsCount;
    }

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    void reset() {
        this.timeStarted = 0;
        this.timeCompleted = 0;
        this.completed = false;
        this.throwable = null;
        this.result = null;
        this.mergedOperationsCount = 0;
    }
}

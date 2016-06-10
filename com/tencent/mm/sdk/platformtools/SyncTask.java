package com.tencent.mm.sdk.platformtools;

import android.os.Handler;

public abstract class SyncTask<R> {
    private final long bg;
    private long bh;
    private long bi;
    private Runnable bj;
    private Object lock;
    private R result;

    /* renamed from: com.tencent.mm.sdk.platformtools.SyncTask.1 */
    class C08731 implements Runnable {
        final /* synthetic */ SyncTask bk;

        C08731(SyncTask syncTask) {
            this.bk = syncTask;
        }

        public void run() {
            this.bk.bi = Util.ticksToNow(this.bk.bh);
            this.bk.setResult(this.bk.run());
        }
    }

    public SyncTask() {
        this(0, null);
    }

    public SyncTask(long j, R r) {
        this.lock = new Object();
        this.bj = new C08731(this);
        this.bg = j;
        this.result = r;
    }

    public R exec(Handler handler) {
        if (handler == null) {
            Log.m1655d("MicroMsg.SDK.SyncTask", "null handler, task in exec thread, return now");
            return run();
        } else if (Thread.currentThread().getId() == handler.getLooper().getThread().getId()) {
            Log.m1655d("MicroMsg.SDK.SyncTask", "same tid, task in exec thread, return now");
            return run();
        } else {
            this.bh = Util.currentTicks();
            try {
                synchronized (this.lock) {
                    handler.post(this.bj);
                    this.lock.wait(this.bg);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long ticksToNow = Util.ticksToNow(this.bh);
            Log.m1664v("MicroMsg.SDK.SyncTask", "sync task done, return=%s, cost=%d(wait=%d, run=%d)", this.result, Long.valueOf(ticksToNow), Long.valueOf(this.bi), Long.valueOf(ticksToNow - this.bi));
            return this.result;
        }
    }

    protected abstract R run();

    public void setResult(R r) {
        this.result = r;
        synchronized (this.lock) {
            this.lock.notify();
        }
    }
}

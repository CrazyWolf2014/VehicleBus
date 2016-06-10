package com.tencent.mm.sdk.platformtools;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import junit.framework.Assert;

public class MMHandlerThread {
    public static long mainThreadID;
    private HandlerThread ao;
    private Handler ap;

    /* renamed from: com.tencent.mm.sdk.platformtools.MMHandlerThread.3 */
    class C08663 implements Runnable {
        final /* synthetic */ IWaitWorkThread aq;
        final /* synthetic */ MMHandlerThread ar;

        /* renamed from: com.tencent.mm.sdk.platformtools.MMHandlerThread.3.1 */
        class C08651 implements Runnable {
            final /* synthetic */ C08663 au;

            C08651(C08663 c08663) {
                this.au = c08663;
            }

            public void run() {
                this.au.aq.onPostExecute();
            }
        }

        C08663(MMHandlerThread mMHandlerThread, IWaitWorkThread iWaitWorkThread) {
            this.ar = mMHandlerThread;
            this.aq = iWaitWorkThread;
        }

        public void run() {
            this.aq.doInBackground();
            MMHandlerThread.m1668a(new C08651(this));
        }
    }

    public interface IWaitWorkThread {
        boolean doInBackground();

        boolean onPostExecute();
    }

    public interface ResetCallback {
        void callback();
    }

    /* renamed from: com.tencent.mm.sdk.platformtools.MMHandlerThread.1 */
    class C11131 implements IWaitWorkThread {
        final /* synthetic */ IWaitWorkThread aq;
        final /* synthetic */ MMHandlerThread ar;

        C11131(MMHandlerThread mMHandlerThread, IWaitWorkThread iWaitWorkThread) {
            this.ar = mMHandlerThread;
            this.aq = iWaitWorkThread;
        }

        public boolean doInBackground() {
            if (this.aq != null) {
                return this.aq.doInBackground();
            }
            this.ar.ao.quit();
            this.ar.m1670c();
            return true;
        }

        public boolean onPostExecute() {
            return this.aq != null ? this.aq.onPostExecute() : true;
        }
    }

    /* renamed from: com.tencent.mm.sdk.platformtools.MMHandlerThread.2 */
    class C11142 implements IWaitWorkThread {
        final /* synthetic */ MMHandlerThread ar;
        final /* synthetic */ ResetCallback as;
        final /* synthetic */ Object at;

        C11142(MMHandlerThread mMHandlerThread, ResetCallback resetCallback, Object obj) {
            this.ar = mMHandlerThread;
            this.as = resetCallback;
            this.at = obj;
        }

        public boolean doInBackground() {
            Log.m1655d("MicroMsg.MMHandlerThread", "syncReset doInBackground");
            this.ar.ao.quit();
            if (this.as != null) {
                this.as.callback();
            }
            this.ar.m1670c();
            synchronized (this.at) {
                this.at.notify();
            }
            return true;
        }

        public boolean onPostExecute() {
            Log.m1655d("MicroMsg.MMHandlerThread", "syncReset onPostExecute");
            return true;
        }
    }

    static {
        mainThreadID = -1;
    }

    public MMHandlerThread() {
        this.ao = null;
        this.ap = null;
        m1670c();
    }

    static /* synthetic */ void m1668a(Runnable runnable) {
        if (runnable != null) {
            new Handler(Looper.getMainLooper()).postAtFrontOfQueue(runnable);
        }
    }

    private void m1670c() {
        Log.m1656d("MicroMsg.MMHandlerThread", "MMHandlerThread init [%s]", Util.getStack());
        this.ap = null;
        this.ao = new HandlerThread("MMHandlerThread", 1);
        this.ao.start();
    }

    public static boolean isMainThread() {
        Assert.assertFalse("mainThreadID not init ", mainThreadID == -1);
        return Thread.currentThread().getId() == mainThreadID;
    }

    public static void postToMainThread(Runnable runnable) {
        if (runnable != null) {
            new Handler(Looper.getMainLooper()).post(runnable);
        }
    }

    public static void postToMainThreadDelayed(Runnable runnable, long j) {
        if (runnable != null) {
            new Handler(Looper.getMainLooper()).postDelayed(runnable, j);
        }
    }

    public static void setMainThreadID(long j) {
        if (mainThreadID < 0 && j > 0) {
            mainThreadID = j;
        }
    }

    public Looper getLooper() {
        return this.ao.getLooper();
    }

    public Handler getWorkerHandler() {
        if (this.ap == null) {
            this.ap = new Handler(this.ao.getLooper());
        }
        return this.ap;
    }

    public int postAtFrontOfWorker(IWaitWorkThread iWaitWorkThread) {
        return iWaitWorkThread == null ? -1 : new Handler(getLooper()).postAtFrontOfQueue(new C08663(this, iWaitWorkThread)) ? 0 : -2;
    }

    public int postToWorker(Runnable runnable) {
        if (runnable == null) {
            return -1;
        }
        getWorkerHandler().post(runnable);
        return 0;
    }

    public int reset(IWaitWorkThread iWaitWorkThread) {
        return postAtFrontOfWorker(new C11131(this, iWaitWorkThread));
    }

    public int syncReset(ResetCallback resetCallback) {
        int postAtFrontOfWorker;
        Assert.assertTrue("syncReset should in mainThread", isMainThread());
        Object obj = new byte[0];
        IWaitWorkThread c11142 = new C11142(this, resetCallback, obj);
        synchronized (obj) {
            postAtFrontOfWorker = postAtFrontOfWorker(c11142);
            if (postAtFrontOfWorker == 0) {
                try {
                    obj.wait();
                } catch (Exception e) {
                }
            }
        }
        return postAtFrontOfWorker;
    }
}

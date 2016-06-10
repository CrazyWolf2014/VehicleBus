package com.tencent.mm.sdk.platformtools;

import android.os.Handler;
import android.os.Message;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.xmlpull.v1.XmlPullParser;

public class QueueWorkerThread {
    private LinkedBlockingQueue<ThreadObject> aQ;
    private boolean aR;
    private int aS;
    private Vector<WorkerThread> aT;
    private Handler aU;
    private Object lock;
    private String name;
    private int priority;

    /* renamed from: com.tencent.mm.sdk.platformtools.QueueWorkerThread.1 */
    class C08721 extends Handler {
        final /* synthetic */ QueueWorkerThread aV;

        C08721(QueueWorkerThread queueWorkerThread) {
            this.aV = queueWorkerThread;
        }

        public void handleMessage(Message message) {
            if (message != null && message.obj != null) {
                ((ThreadObject) message.obj).onPostExecute();
            }
        }
    }

    public interface ThreadObject {
        boolean doInBackground();

        boolean onPostExecute();
    }

    final class WorkerThread extends Thread {
        final /* synthetic */ QueueWorkerThread aV;
        private int aW;

        private WorkerThread(QueueWorkerThread queueWorkerThread) {
            this.aV = queueWorkerThread;
            super(queueWorkerThread.name);
            this.aW = 60;
            setPriority(queueWorkerThread.priority);
            queueWorkerThread.aT.add(this);
        }

        public final void run() {
            while (this.aW > 0) {
                ThreadObject threadObject;
                synchronized (this.aV.lock) {
                    try {
                        if (this.aV.aR) {
                            this.aV.lock.wait();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    threadObject = (ThreadObject) this.aV.aQ.poll(2000, TimeUnit.MILLISECONDS);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    threadObject = null;
                }
                if (threadObject == null) {
                    this.aW--;
                } else {
                    this.aW = 60;
                    if (threadObject.doInBackground()) {
                        this.aV.aU.sendMessage(this.aV.aU.obtainMessage(0, threadObject));
                    }
                }
            }
            this.aV.aT.remove(this);
            Log.m1655d("QueueWorkerThread.QueueWorkerThread", "dktest Finish queueToReqSize:" + this.aV.aQ.size() + " ThreadSize:" + this.aV.aT.size());
        }
    }

    public QueueWorkerThread(int i, String str) {
        this(i, str, 1);
    }

    public QueueWorkerThread(int i, String str, int i2) {
        this.aQ = new LinkedBlockingQueue();
        this.aR = false;
        this.aS = 1;
        this.priority = 1;
        this.name = XmlPullParser.NO_NAMESPACE;
        this.lock = new byte[0];
        this.aT = new Vector();
        this.aU = new C08721(this);
        this.aS = i2;
        this.name = str;
        this.priority = i;
    }

    public int add(ThreadObject threadObject) {
        if (threadObject == null) {
            Log.m1657e("QueueWorkerThread.QueueWorkerThread", "add empty thread object");
            return -1;
        }
        try {
            if (!this.aQ.offer(threadObject, 1, TimeUnit.MILLISECONDS)) {
                Log.m1657e("QueueWorkerThread.QueueWorkerThread", "add To Queue failed");
                return -2;
            } else if (this.aT.size() != 0 && (this.aQ.size() <= 0 || this.aS <= this.aT.size())) {
                return 0;
            } else {
                new WorkerThread().start();
                return 0;
            }
        } catch (Exception e) {
            Log.m1657e("QueueWorkerThread.QueueWorkerThread", "add To Queue failed :" + e.getMessage());
            e.printStackTrace();
            return -3;
        }
    }

    public int getQueueSize() {
        return this.aQ.size();
    }

    public boolean isDead() {
        return this.aT == null || this.aT.size() == 0;
    }

    public void pause(boolean z) {
        synchronized (this.lock) {
            this.aR = z;
            if (!z) {
                synchronized (this.lock) {
                    this.lock.notifyAll();
                }
            }
        }
    }
}

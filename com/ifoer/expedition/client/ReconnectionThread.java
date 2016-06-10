package com.ifoer.expedition.client;

import android.util.Log;
import com.cnlaunch.mycar.jni.JniX431File;

public class ReconnectionThread extends Thread {
    private static final String LOGTAG;
    private int waiting;
    private final XmppManager xmppManager;

    /* renamed from: com.ifoer.expedition.client.ReconnectionThread.1 */
    class C04981 implements Runnable {
        private final /* synthetic */ InterruptedException val$e;

        C04981(InterruptedException interruptedException) {
            this.val$e = interruptedException;
        }

        public void run() {
            ReconnectionThread.this.xmppManager.getConnectionListener().reconnectionFailed(this.val$e);
        }
    }

    static {
        LOGTAG = LogUtil.makeLogTag(ReconnectionThread.class);
    }

    ReconnectionThread(XmppManager xmppManager) {
        this.xmppManager = xmppManager;
        this.waiting = 0;
    }

    public void run() {
        while (NotificationService.startActivity.booleanValue()) {
            try {
                Log.d(LOGTAG, "Trying to reconnect in " + waiting() + " seconds");
                Thread.sleep(((long) waiting()) * 1000);
                this.xmppManager.connect();
                this.waiting++;
            } catch (InterruptedException e) {
                this.xmppManager.getHandler().post(new C04981(e));
                return;
            }
        }
    }

    private int waiting() {
        if (this.waiting > 20) {
            return 600;
        }
        if (this.waiting > 13) {
            return JniX431File.MAX_DS_COLNUMBER;
        }
        return this.waiting <= 7 ? 10 : 60;
    }
}

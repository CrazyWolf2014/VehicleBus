package org.jivesoftware.smackx.ping;

import java.lang.ref.WeakReference;
import org.jivesoftware.smack.Connection;

class ServerPingTask implements Runnable {
    private int delta;
    private volatile long lastSuccessfulPing;
    private int pingInterval;
    private int tries;
    private WeakReference<Connection> weakConnection;

    protected ServerPingTask(Connection connection, int i) {
        this.lastSuccessfulPing = -1;
        this.delta = 1000;
        this.tries = 3;
        this.weakConnection = new WeakReference(connection);
        this.pingInterval = i;
    }

    protected void setDone() {
        this.pingInterval = -1;
    }

    protected void setPingInterval(int i) {
        this.pingInterval = i;
    }

    protected int getIntInterval() {
        return this.pingInterval;
    }

    protected long getLastSucessfulPing() {
        return this.lastSuccessfulPing;
    }

    public void run() {
        sleep(60000);
        while (this.pingInterval > 0) {
            Connection connection = (Connection) this.weakConnection.get();
            if (connection != null) {
                if (connection.isAuthenticated()) {
                    PingManager instanceFor = PingManager.getInstanceFor(connection);
                    boolean z = false;
                    for (int i = 0; i < this.tries; i++) {
                        if (i != 0) {
                            try {
                                Thread.sleep((long) this.delta);
                            } catch (InterruptedException e) {
                                return;
                            }
                        }
                        z = instanceFor.pingMyServer();
                        if (z) {
                            this.lastSuccessfulPing = System.currentTimeMillis();
                            break;
                        }
                    }
                    if (!z) {
                        for (PingFailedListener pingFailed : instanceFor.getPingFailedListeners()) {
                            pingFailed.pingFailed();
                        }
                    }
                }
                sleep();
            } else {
                return;
            }
        }
    }

    private void sleep(int i) {
        int i2 = this.pingInterval + i;
        if (i2 > 0) {
            try {
                Thread.sleep((long) i2);
            } catch (InterruptedException e) {
            }
        }
    }

    private void sleep() {
        sleep(0);
    }
}

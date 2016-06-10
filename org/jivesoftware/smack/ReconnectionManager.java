package org.jivesoftware.smack;

import java.util.Random;
import org.jivesoftware.smack.packet.StreamError;

public class ReconnectionManager implements ConnectionListener {
    private Connection connection;
    boolean done;
    private int randomBase;
    private Thread reconnectionThread;

    /* renamed from: org.jivesoftware.smack.ReconnectionManager.2 */
    class C09592 extends Thread {
        private int attempts;

        C09592() {
            this.attempts = 0;
        }

        private int timeDelay() {
            this.attempts++;
            if (this.attempts > 13) {
                return (ReconnectionManager.this.randomBase * 6) * 5;
            }
            if (this.attempts > 7) {
                return ReconnectionManager.this.randomBase * 6;
            }
            return ReconnectionManager.this.randomBase;
        }

        public void run() {
            while (ReconnectionManager.this.isReconnectionAllowed()) {
                int timeDelay = timeDelay();
                while (ReconnectionManager.this.isReconnectionAllowed() && timeDelay > 0) {
                    try {
                        Thread.sleep(1000);
                        timeDelay--;
                        ReconnectionManager.this.notifyAttemptToReconnectIn(timeDelay);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ReconnectionManager.this.notifyReconnectionFailed(e);
                    }
                }
                try {
                    if (ReconnectionManager.this.isReconnectionAllowed()) {
                        ReconnectionManager.this.connection.connect();
                    }
                } catch (Exception e2) {
                    ReconnectionManager.this.notifyReconnectionFailed(e2);
                }
            }
        }
    }

    /* renamed from: org.jivesoftware.smack.ReconnectionManager.1 */
    static class C11571 implements ConnectionCreationListener {
        C11571() {
        }

        public void connectionCreated(Connection connection) {
            connection.addConnectionListener(new ReconnectionManager(null));
        }
    }

    static {
        Connection.addConnectionCreationListener(new C11571());
    }

    private ReconnectionManager(Connection connection) {
        this.randomBase = new Random().nextInt(11) + 5;
        this.done = false;
        this.connection = connection;
    }

    private boolean isReconnectionAllowed() {
        return (this.done || this.connection.isConnected() || !this.connection.isReconnectionAllowed()) ? false : true;
    }

    protected synchronized void reconnect() {
        if (isReconnectionAllowed() && (this.reconnectionThread == null || !this.reconnectionThread.isAlive())) {
            this.reconnectionThread = new C09592();
            this.reconnectionThread.setName("Smack Reconnection Manager");
            this.reconnectionThread.setDaemon(true);
            this.reconnectionThread.start();
        }
    }

    protected void notifyReconnectionFailed(Exception exception) {
        if (isReconnectionAllowed()) {
            for (ConnectionListener reconnectionFailed : this.connection.connectionListeners) {
                reconnectionFailed.reconnectionFailed(exception);
            }
        }
    }

    protected void notifyAttemptToReconnectIn(int i) {
        if (isReconnectionAllowed()) {
            for (ConnectionListener reconnectingIn : this.connection.connectionListeners) {
                reconnectingIn.reconnectingIn(i);
            }
        }
    }

    public void connectionClosed() {
        this.done = true;
    }

    public void connectionClosedOnError(Exception exception) {
        this.done = false;
        if (exception instanceof XMPPException) {
            StreamError streamError = ((XMPPException) exception).getStreamError();
            if (streamError != null) {
                if ("conflict".equals(streamError.getCode())) {
                    return;
                }
            }
        }
        if (isReconnectionAllowed()) {
            reconnect();
        }
    }

    public void reconnectingIn(int i) {
    }

    public void reconnectionFailed(Exception exception) {
    }

    public void reconnectionSuccessful() {
    }
}

package com.ifoer.expedition.client;

import android.util.Log;
import org.jivesoftware.smack.ConnectionListener;

public class PersistentConnectionListener implements ConnectionListener {
    private static final String LOGTAG;
    private final XmppManager xmppManager;

    static {
        LOGTAG = LogUtil.makeLogTag(PersistentConnectionListener.class);
    }

    public PersistentConnectionListener(XmppManager xmppManager) {
        this.xmppManager = xmppManager;
    }

    public void connectionClosed() {
        Log.d(LOGTAG, "connectionClosed()...");
    }

    public void connectionClosedOnError(Exception e) {
        Log.d(LOGTAG, "connectionClosedOnError()...");
        if (this.xmppManager.getConnection() != null && this.xmppManager.getConnection().isConnected()) {
            this.xmppManager.getConnection().disconnect();
        }
        this.xmppManager.startReconnectionThread();
    }

    public void reconnectingIn(int seconds) {
        Log.d(LOGTAG, "reconnectingIn()...");
    }

    public void reconnectionFailed(Exception e) {
        Log.d(LOGTAG, "reconnectionFailed()...");
    }

    public void reconnectionSuccessful() {
        Log.d(LOGTAG, "reconnectionSuccessful()...");
    }
}

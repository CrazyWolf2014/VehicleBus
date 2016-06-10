package com.ifoer.expedition.client;

import android.content.Intent;
import android.util.Log;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

public class NotificationPacketListener implements PacketListener {
    private static final String LOGTAG;
    private final XmppManager xmppManager;

    static {
        LOGTAG = LogUtil.makeLogTag(NotificationPacketListener.class);
    }

    public NotificationPacketListener(XmppManager xmppManager) {
        this.xmppManager = xmppManager;
    }

    public void processPacket(Packet packet) {
        Log.d(LOGTAG, "NotificationPacketListener.processPacket()...");
        Log.d(LOGTAG, "packet.toXML()=" + packet.toXML());
        if (packet instanceof NotificationIQ) {
            NotificationIQ notification = (NotificationIQ) packet;
            if (notification.getChildElementXML().contains("androidpn:iq:notification")) {
                String notificationType = notification.getTypes();
                String notificationSerialno = notification.getSerialno();
                String notificationUsername = notification.getUsername();
                String notificationId = notification.getId();
                String notificationApiKey = notification.getApiKey();
                String notificationTitle = notification.getTitle();
                String notificationMessage = notification.getMessage();
                String notificationUri = notification.getUri();
                Intent intent = new Intent(Constants.ACTION_SHOW_NOTIFICATION);
                intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
                intent.putExtra(Constants.NOTIFICATION_API_KEY, notificationApiKey);
                intent.putExtra(Constants.NOTIFICATION_TITLE, notificationTitle);
                intent.putExtra(Constants.NOTIFICATION_MESSAGE, notificationMessage);
                intent.putExtra(Constants.NOTIFICATION_TYPE, notificationType);
                intent.putExtra(Constants.NOTIFICATION_SERIALNO, notificationSerialno);
                intent.putExtra(Constants.NOTIFICATION_USERNAME, notificationUsername);
                intent.putExtra(Constants.NOTIFICATION_URI, notificationUri);
                this.xmppManager.getContext().sendBroadcast(intent);
            }
        }
    }
}

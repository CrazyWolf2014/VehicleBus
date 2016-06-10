package com.ifoer.expedition.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.ifoer.entity.Constant;

public final class NotificationReceiver extends BroadcastReceiver {
    private static final String LOGTAG;

    static {
        LOGTAG = LogUtil.makeLogTag(NotificationReceiver.class);
    }

    public void onReceive(Context context, Intent intent) {
        Log.d(LOGTAG, "NotificationReceiver.onReceive()...");
        String action = intent.getAction();
        Log.d(LOGTAG, new StringBuilder(Constant.ACTION_REGEX).append(action).toString());
        if (Constants.ACTION_SHOW_NOTIFICATION.equals(action)) {
            String notificationType = intent.getStringExtra(Constants.NOTIFICATION_TYPE);
            String notificationSerialno = intent.getStringExtra(Constants.NOTIFICATION_SERIALNO);
            String notificationUsername = intent.getStringExtra(Constants.NOTIFICATION_USERNAME);
            String notificationId = intent.getStringExtra(Constants.NOTIFICATION_ID);
            String notificationApiKey = intent.getStringExtra(Constants.NOTIFICATION_API_KEY);
            String notificationTitle = intent.getStringExtra(Constants.NOTIFICATION_TITLE);
            String notificationMessage = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
            String notificationUri = intent.getStringExtra(Constants.NOTIFICATION_URI);
            Log.d(LOGTAG, "notificationType=" + notificationType);
            Log.d(LOGTAG, "notificationSerialno=" + notificationSerialno);
            Log.d(LOGTAG, "notificationUsername=" + notificationUsername);
            Log.d(LOGTAG, "notificationId=" + notificationId);
            Log.d(LOGTAG, "notificationApiKey=" + notificationApiKey);
            Log.d(LOGTAG, "notificationTitle=" + notificationTitle);
            Log.d(LOGTAG, "notificationMessage=" + notificationMessage);
            Log.d(LOGTAG, "notificationUri=" + notificationUri);
            new Notifier(context).notify(notificationId, notificationApiKey, notificationTitle, notificationMessage, notificationUri, notificationType, notificationSerialno, notificationUsername);
        }
    }
}

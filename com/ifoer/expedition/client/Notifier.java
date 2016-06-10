package com.ifoer.expedition.client;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import com.ifoer.expeditionphone.NotificationActivity;
import com.ifoer.mine.Contact;
import java.util.Random;

public class Notifier {
    private static final String LOGTAG;
    private static final Random random;
    private Context context;
    private NotificationManager notificationManager;
    private SharedPreferences sharedPrefs;

    static {
        LOGTAG = LogUtil.makeLogTag(Notifier.class);
        random = new Random(System.currentTimeMillis());
    }

    public Notifier(Context context) {
        this.context = context;
        this.sharedPrefs = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, 0);
        this.notificationManager = (NotificationManager) context.getSystemService("notification");
    }

    public void notify(String notificationId, String apiKey, String title, String message, String uri, String notificationType, String notificationSerialno, String notificationUsername) {
        Log.d(LOGTAG, "notify()...");
        Log.d(LOGTAG, "notificationId=" + notificationId);
        Log.d(LOGTAG, "notificationApiKey=" + apiKey);
        Log.d(LOGTAG, "notificationTitle=" + title);
        Log.d(LOGTAG, "notificationMessage=" + message);
        Log.d(LOGTAG, "notificationUri=" + uri);
        if (!isNotificationEnabled() || notificationType.equals(Contact.RELATION_NOAGREE) || notificationType.equals(Contact.RELATION_AGREE)) {
            Log.w(LOGTAG, "Notificaitons disabled.");
            return;
        }
        if (isNotificationToastEnabled()) {
            Toast.makeText(this.context, message, 1).show();
        }
        Notification notification = new Notification();
        notification.icon = getNotificationIcon();
        notification.defaults = 4;
        if (isNotificationSoundEnabled()) {
            notification.defaults |= 1;
        }
        if (isNotificationVibrateEnabled()) {
            notification.defaults |= 2;
        }
        notification.flags |= 16;
        notification.when = System.currentTimeMillis();
        notification.tickerText = message;
        Intent intent = new Intent(this.context, NotificationActivity.class);
        intent.putExtra(Constants.NOTIFICATION_TYPE, notificationType);
        intent.putExtra(Constants.NOTIFICATION_SERIALNO, notificationSerialno);
        intent.putExtra(Constants.NOTIFICATION_USERNAME, notificationUsername);
        intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
        intent.putExtra(Constants.NOTIFICATION_API_KEY, apiKey);
        intent.putExtra(Constants.NOTIFICATION_TITLE, title);
        intent.putExtra(Constants.NOTIFICATION_MESSAGE, message);
        intent.putExtra(Constants.NOTIFICATION_URI, uri);
        intent.setFlags(268435456);
        intent.setFlags(8388608);
        intent.setFlags(1073741824);
        intent.setFlags(536870912);
        intent.setFlags(67108864);
        notification.setLatestEventInfo(this.context, title, message, PendingIntent.getActivity(this.context, 0, intent, 134217728));
        this.notificationManager.notify(random.nextInt(), notification);
    }

    private int getNotificationIcon() {
        return this.sharedPrefs.getInt(Constants.NOTIFICATION_ICON, 0);
    }

    private boolean isNotificationEnabled() {
        return this.sharedPrefs.getBoolean(Constants.SETTINGS_NOTIFICATION_ENABLED, true);
    }

    private boolean isNotificationSoundEnabled() {
        return this.sharedPrefs.getBoolean(Constants.SETTINGS_SOUND_ENABLED, true);
    }

    private boolean isNotificationVibrateEnabled() {
        return this.sharedPrefs.getBoolean(Constants.SETTINGS_VIBRATE_ENABLED, true);
    }

    private boolean isNotificationToastEnabled() {
        return this.sharedPrefs.getBoolean(Constants.SETTINGS_TOAST_ENABLED, false);
    }
}

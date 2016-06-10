package com.ifoer.expedition.client;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.ifoer.expeditionphone.BaseActivity;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.util.MySharedPreferences;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.xmlpull.v1.XmlPullParser;

public class NotificationService extends Service {
    private static final String LOGTAG = "NotificationService_iDiag...";
    public static String SERVICE_NAME;
    public static String deviceId;
    public static Boolean startActivity;
    private BroadcastReceiver connectivityReceiver;
    private ExecutorService executorService;
    private BroadcastReceiver notificationReceiver;
    private PhoneStateListener phoneStateListener;
    private SharedPreferences sharedPrefs;
    private TaskSubmitter taskSubmitter;
    private TaskTracker taskTracker;
    private TelephonyManager telephonyManager;
    private XmppManager xmppManager;

    /* renamed from: com.ifoer.expedition.client.NotificationService.1 */
    class C04941 implements Runnable {
        C04941() {
        }

        public void run() {
            NotificationService.this.start();
        }
    }

    /* renamed from: com.ifoer.expedition.client.NotificationService.2 */
    class C04952 implements Runnable {
        C04952() {
        }

        public void run() {
            NotificationService.this.getXmppManager().connect();
        }
    }

    /* renamed from: com.ifoer.expedition.client.NotificationService.3 */
    class C04963 implements Runnable {
        C04963() {
        }

        public void run() {
            NotificationService.this.getXmppManager().disconnect();
        }
    }

    public class TaskSubmitter {
        final NotificationService notificationService;

        public TaskSubmitter(NotificationService notificationService) {
            this.notificationService = notificationService;
        }

        public Future submit(Runnable task) {
            if (this.notificationService.getExecutorService().isTerminated() || this.notificationService.getExecutorService().isShutdown() || task == null) {
                return null;
            }
            return this.notificationService.getExecutorService().submit(task);
        }
    }

    public class TaskTracker {
        public int count;
        final NotificationService notificationService;

        public TaskTracker(NotificationService notificationService) {
            this.notificationService = notificationService;
            this.count = 0;
        }

        public void increase() {
            synchronized (this.notificationService.getTaskTracker()) {
                TaskTracker taskTracker = this.notificationService.getTaskTracker();
                taskTracker.count++;
                Log.d(NotificationService.LOGTAG, "Incremented task count to " + this.count);
            }
        }

        public void decrease() {
            synchronized (this.notificationService.getTaskTracker()) {
                TaskTracker taskTracker = this.notificationService.getTaskTracker();
                taskTracker.count--;
                Log.d(NotificationService.LOGTAG, "Decremented task count to " + this.count);
            }
        }
    }

    static {
        SERVICE_NAME = XmlPullParser.NO_NAMESPACE;
        startActivity = Boolean.valueOf(false);
    }

    public NotificationService() {
        this.notificationReceiver = new NotificationReceiver();
        this.connectivityReceiver = new ConnectivityReceiver(this);
        this.phoneStateListener = new PhoneStateChangeListener(this);
        this.executorService = Executors.newSingleThreadExecutor();
        this.taskSubmitter = new TaskSubmitter(this);
        this.taskTracker = new TaskTracker(this);
    }

    public void onCreate() {
        Log.d(LOGTAG, "NotificationService onCreate()...");
        this.telephonyManager = (TelephonyManager) getSystemService("phone");
        this.sharedPrefs = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, 0);
        deviceId = this.telephonyManager.getDeviceId();
        Editor editor = this.sharedPrefs.edit();
        editor.putString(Constants.DEVICE_ID, deviceId);
        editor.commit();
        if (MainActivity.contexts != null) {
            String cc = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.CCKey);
            editor = this.sharedPrefs.edit();
            editor.putString(Constants.XMPP_USERNAME, cc);
            editor.putString(Constants.XMPP_PASSWORD, "123456");
            editor.commit();
        } else {
            editor = this.sharedPrefs.edit();
            editor.putString(Constants.XMPP_USERNAME, XmlPullParser.NO_NAMESPACE);
            editor.putString(Constants.XMPP_PASSWORD, XmlPullParser.NO_NAMESPACE);
            editor.commit();
        }
        if (deviceId == null || deviceId.trim().length() == 0 || deviceId.matches("0+")) {
            if (this.sharedPrefs.contains(Constants.EMULATOR_DEVICE_ID)) {
                deviceId = this.sharedPrefs.getString(Constants.EMULATOR_DEVICE_ID, XmlPullParser.NO_NAMESPACE);
            } else {
                deviceId = "EMU" + new Random(System.currentTimeMillis()).nextLong();
                editor.putString(Constants.EMULATOR_DEVICE_ID, deviceId);
                editor.commit();
            }
        }
        Log.d(LOGTAG, "deviceId=" + deviceId);
        this.xmppManager = new XmppManager(this);
        this.taskSubmitter.submit(new C04941());
    }

    public void onStart(Intent intent, int startId) {
        Log.d(LOGTAG, "onStart()...");
    }

    public void onDestroy() {
        Log.d(LOGTAG, "onDestroy()...");
        stop();
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOGTAG, "onBind()...");
        return null;
    }

    public void onRebind(Intent intent) {
        Log.d(LOGTAG, "onRebind()...");
    }

    public boolean onUnbind(Intent intent) {
        Log.d(LOGTAG, "onUnbind()...");
        return true;
    }

    public static Intent getIntent() {
        if (MySharedPreferences.getStringValue(BaseActivity.mContexts, "SERVICE_NAME") != null) {
            SERVICE_NAME = MySharedPreferences.getStringValue(BaseActivity.mContexts, "SERVICE_NAME");
        }
        return new Intent(SERVICE_NAME);
    }

    public ExecutorService getExecutorService() {
        return this.executorService;
    }

    public TaskSubmitter getTaskSubmitter() {
        return this.taskSubmitter;
    }

    public TaskTracker getTaskTracker() {
        return this.taskTracker;
    }

    public XmppManager getXmppManager() {
        return this.xmppManager;
    }

    public SharedPreferences getSharedPreferences() {
        return this.sharedPrefs;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void connect() {
        Log.d(LOGTAG, "connect()...");
        this.taskSubmitter.submit(new C04952());
    }

    public void disconnect() {
        Log.d(LOGTAG, "disconnect()...");
        this.taskSubmitter.submit(new C04963());
    }

    private void registerNotificationReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_SHOW_NOTIFICATION);
        filter.addAction(Constants.ACTION_NOTIFICATION_CLICKED);
        filter.addAction(Constants.ACTION_NOTIFICATION_CLEARED);
        registerReceiver(this.notificationReceiver, filter);
    }

    private void unregisterNotificationReceiver() {
        unregisterReceiver(this.notificationReceiver);
    }

    private void registerConnectivityReceiver() {
        Log.d(LOGTAG, "registerConnectivityReceiver()...");
        this.telephonyManager.listen(this.phoneStateListener, 64);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(this.connectivityReceiver, filter);
    }

    private void unregisterConnectivityReceiver() {
        Log.d(LOGTAG, "unregisterConnectivityReceiver()...");
        this.telephonyManager.listen(this.phoneStateListener, 0);
        unregisterReceiver(this.connectivityReceiver);
    }

    private void start() {
        Log.d(LOGTAG, "start()...");
        registerNotificationReceiver();
        registerConnectivityReceiver();
        this.xmppManager.connect();
    }

    private void stop() {
        Log.d(LOGTAG, "stop()...");
        unregisterNotificationReceiver();
        unregisterConnectivityReceiver();
        this.xmppManager.disconnect();
        this.executorService.shutdown();
    }
}

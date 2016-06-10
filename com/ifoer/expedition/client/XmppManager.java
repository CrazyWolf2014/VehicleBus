package com.ifoer.expedition.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;
import com.ifoer.expedition.client.NotificationService.TaskSubmitter;
import com.ifoer.expedition.client.NotificationService.TaskTracker;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.util.MySharedPreferences;
import com.tencent.mm.sdk.plugin.BaseProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmlpull.v1.XmlPullParser;

public class XmppManager {
    private static final String LOGTAG;
    private static final String XMPP_RESOURCE_NAME = "AndroidpnClient";
    private XMPPConnection connection;
    private ConnectionListener connectionListener;
    private Context context;
    private Future<?> futureTask;
    private Handler handler;
    private String newPassword;
    private String newUsername;
    private PacketListener notificationPacketListener;
    private String password;
    private Thread reconnection;
    private boolean running;
    private SharedPreferences sharedPrefs;
    private List<Runnable> taskList;
    private TaskSubmitter taskSubmitter;
    private TaskTracker taskTracker;
    private String username;
    private String xmppHost;
    private int xmppPort;

    /* renamed from: com.ifoer.expedition.client.XmppManager.1 */
    class C05001 implements Runnable {
        final XmppManager xmppManager;

        C05001() {
            this.xmppManager = XmppManager.this;
        }

        public void run() {
            if (this.xmppManager.isConnected()) {
                Log.d(XmppManager.LOGTAG, "terminatePersistentConnection()... run()");
                this.xmppManager.getConnection().removePacketListener(this.xmppManager.getNotificationPacketListener());
                this.xmppManager.getConnection().disconnect();
            }
            this.xmppManager.runTask();
        }
    }

    private class ConnectTask implements Runnable {
        final XmppManager xmppManager;

        private ConnectTask() {
            this.xmppManager = XmppManager.this;
        }

        public void run() {
            Log.i(XmppManager.LOGTAG, "ConnectTask.run()...");
            if (this.xmppManager.isConnected()) {
                Log.i(XmppManager.LOGTAG, "XMPP connected already");
                System.out.println("\u4eae\u4eae\u4eae XMPP connected already ");
                this.xmppManager.runTask();
                return;
            }
            ConnectionConfiguration connConfig = new ConnectionConfiguration(XmppManager.this.xmppHost, XmppManager.this.xmppPort);
            connConfig.setSecurityMode(SecurityMode.required);
            connConfig.setSASLAuthenticationEnabled(false);
            connConfig.setCompressionEnabled(false);
            XMPPConnection connection = new XMPPConnection(connConfig);
            this.xmppManager.setConnection(connection);
            try {
                connection.connect();
                System.out.println("\u4eae\u4eae\u4eae  XMPP connected successfully ");
                Log.i(XmppManager.LOGTAG, "XMPP connected successfully");
                ProviderManager.getInstance().addIQProvider("notification", "androidpn:iq:notification", new NotificationIQProvider());
            } catch (XMPPException e) {
                Log.e(XmppManager.LOGTAG, "XMPP connection failed", e);
                System.out.println("\u4eae\u4eae\u4eae XMPP connection failed ");
            }
            this.xmppManager.runTask();
        }
    }

    private class LoginTask implements Runnable {
        final XmppManager xmppManager;

        private LoginTask() {
            this.xmppManager = XmppManager.this;
        }

        public void run() {
            Log.i(XmppManager.LOGTAG, "LoginTask.run()...");
            if (this.xmppManager.isAuthenticated()) {
                Log.i(XmppManager.LOGTAG, "Logged in already");
                this.xmppManager.runTask();
                return;
            }
            Log.d(XmppManager.LOGTAG, "username=" + XmppManager.this.username);
            Log.d(XmppManager.LOGTAG, "password=" + XmppManager.this.password);
            try {
                this.xmppManager.getConnection().login(this.xmppManager.getUsername(), this.xmppManager.getPassword(), XmppManager.XMPP_RESOURCE_NAME);
                Log.d(XmppManager.LOGTAG, "Loggedn in successfully");
                if (this.xmppManager.getConnectionListener() != null) {
                    this.xmppManager.getConnection().addConnectionListener(this.xmppManager.getConnectionListener());
                }
                PacketFilter packetFilter = new PacketTypeFilter(NotificationIQ.class);
                XmppManager.this.connection.addPacketListener(this.xmppManager.getNotificationPacketListener(), packetFilter);
                this.xmppManager.runTask();
            } catch (XMPPException e) {
                Log.e(XmppManager.LOGTAG, "LoginTask.run()... xmpp error");
                Log.e(XmppManager.LOGTAG, "Failed to login to xmpp server. Caused by: " + e.getMessage());
                String INVALID_CREDENTIALS_ERROR_CODE = "401";
                String errorMessage = e.getMessage();
                if (errorMessage == null || !errorMessage.contains(INVALID_CREDENTIALS_ERROR_CODE)) {
                    this.xmppManager.startReconnectionThread();
                } else {
                    this.xmppManager.reregisterAccount();
                }
            } catch (Exception e2) {
                Log.e(XmppManager.LOGTAG, "LoginTask.run()... other error");
                Log.e(XmppManager.LOGTAG, "Failed to login to xmpp server. Caused by: " + e2.getMessage());
                this.xmppManager.startReconnectionThread();
            }
        }
    }

    private class RegisterTask implements Runnable {
        final XmppManager xmppManager;

        /* renamed from: com.ifoer.expedition.client.XmppManager.RegisterTask.1 */
        class C10951 implements PacketListener {
            C10951() {
            }

            public void processPacket(Packet packet) {
                Log.d("RegisterTask.PacketListener", "processPacket().....");
                Log.d("RegisterTask.PacketListener", "packet=" + packet.toXML());
                if (packet instanceof IQ) {
                    IQ response = (IQ) packet;
                    if (response.getType() == Type.ERROR) {
                        if (!response.getError().toString().contains("409")) {
                            Log.e(XmppManager.LOGTAG, "Unknown error while registering XMPP account! " + response.getError().getCondition());
                        }
                    } else if (response.getType() == Type.RESULT) {
                        RegisterTask.this.xmppManager.setUsername(XmppManager.this.newUsername);
                        RegisterTask.this.xmppManager.setPassword(XmppManager.this.newPassword);
                        Log.d(XmppManager.LOGTAG, "username=" + XmppManager.this.newUsername);
                        Log.d(XmppManager.LOGTAG, "password=" + XmppManager.this.newPassword);
                        Editor editor = XmppManager.this.sharedPrefs.edit();
                        editor.putString(Constants.XMPP_USERNAME, XmppManager.this.newUsername);
                        editor.putString(Constants.XMPP_PASSWORD, XmppManager.this.newPassword);
                        editor.commit();
                        Log.i(XmppManager.LOGTAG, "Account registered successfully");
                        RegisterTask.this.xmppManager.runTask();
                    }
                }
            }
        }

        private RegisterTask() {
            this.xmppManager = XmppManager.this;
        }

        public void run() {
            Log.i(XmppManager.LOGTAG, "RegisterTask.run()...");
            if (this.xmppManager.isRegistered()) {
                Log.i(XmppManager.LOGTAG, "Account registered already");
                this.xmppManager.runTask();
                return;
            }
            if (MainActivity.contexts != null) {
                XmppManager.this.newUsername = MySharedPreferences.getStringValue(MainActivity.contexts, MySharedPreferences.CCKey);
                XmppManager.this.newPassword = "123456";
            } else {
                XmppManager.this.newUsername = XmlPullParser.NO_NAMESPACE;
                XmppManager.this.newPassword = XmlPullParser.NO_NAMESPACE;
            }
            Registration registration = new Registration();
            PacketFilter packetFilter = new AndFilter(new PacketIDFilter(registration.getPacketID()), new PacketTypeFilter(IQ.class));
            XmppManager.this.connection.addPacketListener(new C10951(), packetFilter);
            registration.setType(Type.SET);
            registration.addAttribute(BaseProfile.COL_USERNAME, XmppManager.this.newUsername);
            registration.addAttribute("password", XmppManager.this.newPassword);
            XmppManager.this.connection.sendPacket(registration);
        }
    }

    static {
        LOGTAG = LogUtil.makeLogTag(XmppManager.class);
    }

    public XmppManager(NotificationService notificationService) {
        this.running = false;
        this.newUsername = XmlPullParser.NO_NAMESPACE;
        this.newPassword = XmlPullParser.NO_NAMESPACE;
        this.context = notificationService;
        this.taskSubmitter = notificationService.getTaskSubmitter();
        this.taskTracker = notificationService.getTaskTracker();
        this.sharedPrefs = notificationService.getSharedPreferences();
        this.xmppHost = this.sharedPrefs.getString(Constants.XMPP_HOST, "localhost");
        this.xmppPort = this.sharedPrefs.getInt(Constants.XMPP_PORT, 5222);
        this.username = this.sharedPrefs.getString(Constants.XMPP_USERNAME, XmlPullParser.NO_NAMESPACE);
        this.password = this.sharedPrefs.getString(Constants.XMPP_PASSWORD, XmlPullParser.NO_NAMESPACE);
        this.connectionListener = new PersistentConnectionListener(this);
        this.notificationPacketListener = new NotificationPacketListener(this);
        this.handler = new Handler();
        this.taskList = new ArrayList();
        this.reconnection = new ReconnectionThread(this);
    }

    public Context getContext() {
        return this.context;
    }

    public void connect() {
        Log.d(LOGTAG, "connect()...");
        submitLoginTask();
    }

    public void disconnect() {
        Log.d(LOGTAG, "disconnect()...");
        terminatePersistentConnection();
    }

    public void terminatePersistentConnection() {
        Log.d(LOGTAG, "terminatePersistentConnection()...");
        addTask(new C05001());
    }

    public XMPPConnection getConnection() {
        return this.connection;
    }

    public void setConnection(XMPPConnection connection) {
        this.connection = connection;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ConnectionListener getConnectionListener() {
        return this.connectionListener;
    }

    public PacketListener getNotificationPacketListener() {
        return this.notificationPacketListener;
    }

    public void startReconnectionThread() {
        synchronized (this.reconnection) {
            if (!this.reconnection.isAlive()) {
                this.reconnection.setName("Xmpp Reconnection Thread");
                this.reconnection.start();
            }
        }
    }

    public Handler getHandler() {
        return this.handler;
    }

    public void reregisterAccount() {
        removeAccount();
        submitLoginTask();
        runTask();
    }

    public List<Runnable> getTaskList() {
        return this.taskList;
    }

    public Future<?> getFutureTask() {
        return this.futureTask;
    }

    public void runTask() {
        Log.d(LOGTAG, "runTask()...");
        synchronized (this.taskList) {
            this.running = false;
            this.futureTask = null;
            if (!this.taskList.isEmpty()) {
                Runnable runnable = (Runnable) this.taskList.get(0);
                this.taskList.remove(0);
                this.running = true;
                this.futureTask = this.taskSubmitter.submit(runnable);
                if (this.futureTask == null) {
                    this.taskTracker.decrease();
                }
            }
        }
        this.taskTracker.decrease();
        Log.d(LOGTAG, "runTask()...done");
    }

    private String newRandomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", XmlPullParser.NO_NAMESPACE);
    }

    private boolean isConnected() {
        return this.connection != null && this.connection.isConnected();
    }

    private boolean isAuthenticated() {
        return this.connection != null && this.connection.isConnected() && this.connection.isAuthenticated();
    }

    private boolean isRegistered() {
        return this.sharedPrefs.contains(Constants.XMPP_USERNAME) && this.sharedPrefs.contains(Constants.XMPP_PASSWORD);
    }

    private void submitConnectTask() {
        Log.d(LOGTAG, "submitConnectTask()...");
        addTask(new ConnectTask());
    }

    private void submitRegisterTask() {
        Log.d(LOGTAG, "submitRegisterTask()...");
        submitConnectTask();
        addTask(new RegisterTask());
    }

    private void submitLoginTask() {
        Log.d(LOGTAG, "submitLoginTask()...");
        submitRegisterTask();
        addTask(new LoginTask());
    }

    private void addTask(Runnable runnable) {
        Log.d(LOGTAG, "addTask(runnable)...");
        this.taskTracker.increase();
        synchronized (this.taskList) {
            if (!this.taskList.isEmpty() || this.running) {
                this.taskList.add(runnable);
            } else {
                this.running = true;
                this.futureTask = this.taskSubmitter.submit(runnable);
                if (this.futureTask == null) {
                    this.taskTracker.decrease();
                }
            }
        }
        Log.d(LOGTAG, "addTask(runnable)... done");
    }

    private void removeAccount() {
        Editor editor = this.sharedPrefs.edit();
        editor.remove(Constants.XMPP_USERNAME);
        editor.remove(Constants.XMPP_PASSWORD);
        editor.commit();
    }
}

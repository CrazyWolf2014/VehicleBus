package org.jivesoftware.smackx.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.NodeInformationProvider;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition;
import org.jivesoftware.smackx.packet.AdHocCommandData;
import org.jivesoftware.smackx.packet.AdHocCommandData.SpecificError;
import org.jivesoftware.smackx.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public class AdHocCommandManager {
    private static final String DISCO_NAMESPACE = "http://jabber.org/protocol/commands";
    private static final int SESSION_TIMEOUT = 120;
    private static final String discoNode = "http://jabber.org/protocol/commands";
    private static Map<Connection, AdHocCommandManager> instances;
    private Map<String, AdHocCommandInfo> commands;
    private Connection connection;
    private Map<String, LocalCommand> executingCommands;
    private Thread sessionsSweeper;

    /* renamed from: org.jivesoftware.smackx.commands.AdHocCommandManager.7 */
    class C09757 implements Runnable {
        C09757() {
        }

        public void run() {
            while (true) {
                for (String str : AdHocCommandManager.this.executingCommands.keySet()) {
                    LocalCommand localCommand = (LocalCommand) AdHocCommandManager.this.executingCommands.get(str);
                    if (localCommand != null) {
                        if (System.currentTimeMillis() - localCommand.getCreationDate() > 240000) {
                            AdHocCommandManager.this.executingCommands.remove(str);
                        }
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private static class AdHocCommandInfo {
        private LocalCommandFactory factory;
        private String name;
        private String node;
        private String ownerJID;

        public AdHocCommandInfo(String str, String str2, String str3, LocalCommandFactory localCommandFactory) {
            this.node = str;
            this.name = str2;
            this.ownerJID = str3;
            this.factory = localCommandFactory;
        }

        public LocalCommand getCommandInstance() throws InstantiationException, IllegalAccessException {
            return this.factory.getInstance();
        }

        public String getName() {
            return this.name;
        }

        public String getNode() {
            return this.node;
        }

        public String getOwnerJID() {
            return this.ownerJID;
        }
    }

    /* renamed from: org.jivesoftware.smackx.commands.AdHocCommandManager.1 */
    static class C11841 implements ConnectionCreationListener {
        C11841() {
        }

        public void connectionCreated(Connection connection) {
            AdHocCommandManager adHocCommandManager = new AdHocCommandManager(null);
        }
    }

    /* renamed from: org.jivesoftware.smackx.commands.AdHocCommandManager.2 */
    class C11852 implements LocalCommandFactory {
        final /* synthetic */ Class val$clazz;

        C11852(Class cls) {
            this.val$clazz = cls;
        }

        public LocalCommand getInstance() throws InstantiationException, IllegalAccessException {
            return (LocalCommand) this.val$clazz.newInstance();
        }
    }

    /* renamed from: org.jivesoftware.smackx.commands.AdHocCommandManager.3 */
    class C11863 implements NodeInformationProvider {
        final /* synthetic */ String val$name;

        C11863(String str) {
            this.val$name = str;
        }

        public List<Item> getNodeItems() {
            return null;
        }

        public List<String> getNodeFeatures() {
            List<String> arrayList = new ArrayList();
            arrayList.add(AdHocCommandManager.discoNode);
            arrayList.add(Form.NAMESPACE);
            return arrayList;
        }

        public List<Identity> getNodeIdentities() {
            List<Identity> arrayList = new ArrayList();
            arrayList.add(new Identity("automation", this.val$name, "command-node"));
            return arrayList;
        }

        public List<PacketExtension> getNodePacketExtensions() {
            return null;
        }
    }

    /* renamed from: org.jivesoftware.smackx.commands.AdHocCommandManager.4 */
    class C11874 implements ConnectionListener {
        C11874() {
        }

        public void connectionClosed() {
            AdHocCommandManager.instances.remove(AdHocCommandManager.this.connection);
        }

        public void connectionClosedOnError(Exception exception) {
            AdHocCommandManager.instances.remove(AdHocCommandManager.this.connection);
        }

        public void reconnectionSuccessful() {
            AdHocCommandManager.instances.put(AdHocCommandManager.this.connection, AdHocCommandManager.this);
        }

        public void reconnectingIn(int i) {
        }

        public void reconnectionFailed(Exception exception) {
        }
    }

    /* renamed from: org.jivesoftware.smackx.commands.AdHocCommandManager.5 */
    class C11885 implements NodeInformationProvider {
        C11885() {
        }

        public List<Item> getNodeItems() {
            List<Item> arrayList = new ArrayList();
            for (AdHocCommandInfo adHocCommandInfo : AdHocCommandManager.this.getRegisteredCommands()) {
                Item item = new Item(adHocCommandInfo.getOwnerJID());
                item.setName(adHocCommandInfo.getName());
                item.setNode(adHocCommandInfo.getNode());
                arrayList.add(item);
            }
            return arrayList;
        }

        public List<String> getNodeFeatures() {
            return null;
        }

        public List<Identity> getNodeIdentities() {
            return null;
        }

        public List<PacketExtension> getNodePacketExtensions() {
            return null;
        }
    }

    /* renamed from: org.jivesoftware.smackx.commands.AdHocCommandManager.6 */
    class C11896 implements PacketListener {
        C11896() {
        }

        public void processPacket(Packet packet) {
            AdHocCommandManager.this.processAdHocCommand((AdHocCommandData) packet);
        }
    }

    static {
        instances = new ConcurrentHashMap();
        Connection.addConnectionCreationListener(new C11841());
    }

    public static AdHocCommandManager getAddHocCommandsManager(Connection connection) {
        return (AdHocCommandManager) instances.get(connection);
    }

    private AdHocCommandManager(Connection connection) {
        this.commands = Collections.synchronizedMap(new WeakHashMap());
        this.executingCommands = new ConcurrentHashMap();
        this.connection = connection;
        init();
    }

    public void registerCommand(String str, String str2, Class<? extends LocalCommand> cls) {
        registerCommand(str, str2, new C11852(cls));
    }

    public void registerCommand(String str, String str2, LocalCommandFactory localCommandFactory) {
        this.commands.put(str, new AdHocCommandInfo(str, str2, this.connection.getUser(), localCommandFactory));
        ServiceDiscoveryManager.getInstanceFor(this.connection).setNodeInformationProvider(str, new C11863(str2));
    }

    public DiscoverItems discoverCommands(String str) throws XMPPException {
        return ServiceDiscoveryManager.getInstanceFor(this.connection).discoverItems(str, discoNode);
    }

    public void publishCommands(String str) throws XMPPException {
        ServiceDiscoveryManager instanceFor = ServiceDiscoveryManager.getInstanceFor(this.connection);
        DiscoverItems discoverItems = new DiscoverItems();
        for (AdHocCommandInfo adHocCommandInfo : getRegisteredCommands()) {
            Item item = new Item(adHocCommandInfo.getOwnerJID());
            item.setName(adHocCommandInfo.getName());
            item.setNode(adHocCommandInfo.getNode());
            discoverItems.addItem(item);
        }
        instanceFor.publishItems(str, discoNode, discoverItems);
    }

    public RemoteCommand getRemoteCommand(String str, String str2) {
        return new RemoteCommand(this.connection, str2, str);
    }

    private void init() {
        instances.put(this.connection, this);
        this.connection.addConnectionListener(new C11874());
        ServiceDiscoveryManager.getInstanceFor(this.connection).addFeature(discoNode);
        ServiceDiscoveryManager.getInstanceFor(this.connection).setNodeInformationProvider(discoNode, new C11885());
        this.connection.addPacketListener(new C11896(), new PacketTypeFilter(AdHocCommandData.class));
        this.sessionsSweeper = null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void processAdHocCommand(org.jivesoftware.smackx.packet.AdHocCommandData r9) {
        /*
        r8 = this;
        r0 = r9.getType();
        r1 = org.jivesoftware.smack.packet.IQ.Type.SET;
        if (r0 == r1) goto L_0x0009;
    L_0x0008:
        return;
    L_0x0009:
        r2 = new org.jivesoftware.smackx.packet.AdHocCommandData;
        r2.<init>();
        r0 = r9.getFrom();
        r2.setTo(r0);
        r0 = r9.getPacketID();
        r2.setPacketID(r0);
        r0 = r9.getNode();
        r2.setNode(r0);
        r0 = r9.getTo();
        r2.setId(r0);
        r3 = r9.getSessionID();
        r0 = r9.getNode();
        if (r3 != 0) goto L_0x00ee;
    L_0x0034:
        r1 = r8.commands;
        r1 = r1.containsKey(r0);
        if (r1 != 0) goto L_0x0042;
    L_0x003c:
        r0 = org.jivesoftware.smack.packet.XMPPError.Condition.item_not_found;
        r8.respondError(r2, r0);
        goto L_0x0008;
    L_0x0042:
        r1 = 15;
        r1 = org.jivesoftware.smack.util.StringUtils.randomString(r1);
        r0 = r8.newInstanceOfCmd(r0, r1);	 Catch:{ XMPPException -> 0x0064 }
        r3 = org.jivesoftware.smack.packet.IQ.Type.RESULT;	 Catch:{ XMPPException -> 0x0064 }
        r2.setType(r3);	 Catch:{ XMPPException -> 0x0064 }
        r0.setData(r2);	 Catch:{ XMPPException -> 0x0064 }
        r3 = r9.getFrom();	 Catch:{ XMPPException -> 0x0064 }
        r3 = r0.hasPermission(r3);	 Catch:{ XMPPException -> 0x0064 }
        if (r3 != 0) goto L_0x0086;
    L_0x005e:
        r0 = org.jivesoftware.smack.packet.XMPPError.Condition.forbidden;	 Catch:{ XMPPException -> 0x0064 }
        r8.respondError(r2, r0);	 Catch:{ XMPPException -> 0x0064 }
        goto L_0x0008;
    L_0x0064:
        r0 = move-exception;
        r3 = r0.getXMPPError();
        r4 = org.jivesoftware.smack.packet.XMPPError.Type.CANCEL;
        r5 = r3.getType();
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x007f;
    L_0x0075:
        r4 = org.jivesoftware.smackx.commands.AdHocCommand.Status.canceled;
        r2.setStatus(r4);
        r4 = r8.executingCommands;
        r4.remove(r1);
    L_0x007f:
        r8.respondError(r2, r3);
        r0.printStackTrace();
        goto L_0x0008;
    L_0x0086:
        r3 = r9.getAction();	 Catch:{ XMPPException -> 0x0064 }
        if (r3 == 0) goto L_0x009d;
    L_0x008c:
        r4 = org.jivesoftware.smackx.commands.AdHocCommand.Action.unknown;	 Catch:{ XMPPException -> 0x0064 }
        r4 = r3.equals(r4);	 Catch:{ XMPPException -> 0x0064 }
        if (r4 == 0) goto L_0x009d;
    L_0x0094:
        r0 = org.jivesoftware.smack.packet.XMPPError.Condition.bad_request;	 Catch:{ XMPPException -> 0x0064 }
        r3 = org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition.malformedAction;	 Catch:{ XMPPException -> 0x0064 }
        r8.respondError(r2, r0, r3);	 Catch:{ XMPPException -> 0x0064 }
        goto L_0x0008;
    L_0x009d:
        if (r3 == 0) goto L_0x00b0;
    L_0x009f:
        r4 = org.jivesoftware.smackx.commands.AdHocCommand.Action.execute;	 Catch:{ XMPPException -> 0x0064 }
        r3 = r3.equals(r4);	 Catch:{ XMPPException -> 0x0064 }
        if (r3 != 0) goto L_0x00b0;
    L_0x00a7:
        r0 = org.jivesoftware.smack.packet.XMPPError.Condition.bad_request;	 Catch:{ XMPPException -> 0x0064 }
        r3 = org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition.badAction;	 Catch:{ XMPPException -> 0x0064 }
        r8.respondError(r2, r0, r3);	 Catch:{ XMPPException -> 0x0064 }
        goto L_0x0008;
    L_0x00b0:
        r0.incrementStage();	 Catch:{ XMPPException -> 0x0064 }
        r0.execute();	 Catch:{ XMPPException -> 0x0064 }
        r3 = r0.isLastStage();	 Catch:{ XMPPException -> 0x0064 }
        if (r3 == 0) goto L_0x00c8;
    L_0x00bc:
        r0 = org.jivesoftware.smackx.commands.AdHocCommand.Status.completed;	 Catch:{ XMPPException -> 0x0064 }
        r2.setStatus(r0);	 Catch:{ XMPPException -> 0x0064 }
    L_0x00c1:
        r0 = r8.connection;	 Catch:{ XMPPException -> 0x0064 }
        r0.sendPacket(r2);	 Catch:{ XMPPException -> 0x0064 }
        goto L_0x0008;
    L_0x00c8:
        r3 = org.jivesoftware.smackx.commands.AdHocCommand.Status.executing;	 Catch:{ XMPPException -> 0x0064 }
        r2.setStatus(r3);	 Catch:{ XMPPException -> 0x0064 }
        r3 = r8.executingCommands;	 Catch:{ XMPPException -> 0x0064 }
        r3.put(r1, r0);	 Catch:{ XMPPException -> 0x0064 }
        r0 = r8.sessionsSweeper;	 Catch:{ XMPPException -> 0x0064 }
        if (r0 != 0) goto L_0x00c1;
    L_0x00d6:
        r0 = new java.lang.Thread;	 Catch:{ XMPPException -> 0x0064 }
        r3 = new org.jivesoftware.smackx.commands.AdHocCommandManager$7;	 Catch:{ XMPPException -> 0x0064 }
        r3.<init>();	 Catch:{ XMPPException -> 0x0064 }
        r0.<init>(r3);	 Catch:{ XMPPException -> 0x0064 }
        r8.sessionsSweeper = r0;	 Catch:{ XMPPException -> 0x0064 }
        r0 = r8.sessionsSweeper;	 Catch:{ XMPPException -> 0x0064 }
        r3 = 1;
        r0.setDaemon(r3);	 Catch:{ XMPPException -> 0x0064 }
        r0 = r8.sessionsSweeper;	 Catch:{ XMPPException -> 0x0064 }
        r0.start();	 Catch:{ XMPPException -> 0x0064 }
        goto L_0x00c1;
    L_0x00ee:
        r0 = r8.executingCommands;
        r0 = r0.get(r3);
        r0 = (org.jivesoftware.smackx.commands.LocalCommand) r0;
        if (r0 != 0) goto L_0x0101;
    L_0x00f8:
        r0 = org.jivesoftware.smack.packet.XMPPError.Condition.bad_request;
        r1 = org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition.badSessionid;
        r8.respondError(r2, r0, r1);
        goto L_0x0008;
    L_0x0101:
        r4 = r0.getCreationDate();
        r6 = java.lang.System.currentTimeMillis();
        r4 = r6 - r4;
        r6 = 120000; // 0x1d4c0 float:1.68156E-40 double:5.9288E-319;
        r1 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r1 <= 0) goto L_0x0120;
    L_0x0112:
        r0 = r8.executingCommands;
        r0.remove(r3);
        r0 = org.jivesoftware.smack.packet.XMPPError.Condition.not_allowed;
        r1 = org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition.sessionExpired;
        r8.respondError(r2, r0, r1);
        goto L_0x0008;
    L_0x0120:
        monitor-enter(r0);
        r1 = r9.getAction();	 Catch:{ all -> 0x0139 }
        if (r1 == 0) goto L_0x013c;
    L_0x0127:
        r4 = org.jivesoftware.smackx.commands.AdHocCommand.Action.unknown;	 Catch:{ all -> 0x0139 }
        r4 = r1.equals(r4);	 Catch:{ all -> 0x0139 }
        if (r4 == 0) goto L_0x013c;
    L_0x012f:
        r1 = org.jivesoftware.smack.packet.XMPPError.Condition.bad_request;	 Catch:{ all -> 0x0139 }
        r3 = org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition.malformedAction;	 Catch:{ all -> 0x0139 }
        r8.respondError(r2, r1, r3);	 Catch:{ all -> 0x0139 }
        monitor-exit(r0);	 Catch:{ all -> 0x0139 }
        goto L_0x0008;
    L_0x0139:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0139 }
        throw r1;
    L_0x013c:
        if (r1 == 0) goto L_0x0146;
    L_0x013e:
        r4 = org.jivesoftware.smackx.commands.AdHocCommand.Action.execute;	 Catch:{ all -> 0x0139 }
        r4 = r4.equals(r1);	 Catch:{ all -> 0x0139 }
        if (r4 == 0) goto L_0x014a;
    L_0x0146:
        r1 = r0.getExecuteAction();	 Catch:{ all -> 0x0139 }
    L_0x014a:
        r4 = r0.isValidAction(r1);	 Catch:{ all -> 0x0139 }
        if (r4 != 0) goto L_0x015a;
    L_0x0150:
        r1 = org.jivesoftware.smack.packet.XMPPError.Condition.bad_request;	 Catch:{ all -> 0x0139 }
        r3 = org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition.badAction;	 Catch:{ all -> 0x0139 }
        r8.respondError(r2, r1, r3);	 Catch:{ all -> 0x0139 }
        monitor-exit(r0);	 Catch:{ all -> 0x0139 }
        goto L_0x0008;
    L_0x015a:
        r4 = org.jivesoftware.smack.packet.IQ.Type.RESULT;	 Catch:{ XMPPException -> 0x0192 }
        r2.setType(r4);	 Catch:{ XMPPException -> 0x0192 }
        r0.setData(r2);	 Catch:{ XMPPException -> 0x0192 }
        r4 = org.jivesoftware.smackx.commands.AdHocCommand.Action.next;	 Catch:{ XMPPException -> 0x0192 }
        r4 = r4.equals(r1);	 Catch:{ XMPPException -> 0x0192 }
        if (r4 == 0) goto L_0x01b4;
    L_0x016a:
        r0.incrementStage();	 Catch:{ XMPPException -> 0x0192 }
        r1 = new org.jivesoftware.smackx.Form;	 Catch:{ XMPPException -> 0x0192 }
        r4 = r9.getForm();	 Catch:{ XMPPException -> 0x0192 }
        r1.<init>(r4);	 Catch:{ XMPPException -> 0x0192 }
        r0.next(r1);	 Catch:{ XMPPException -> 0x0192 }
        r1 = r0.isLastStage();	 Catch:{ XMPPException -> 0x0192 }
        if (r1 == 0) goto L_0x018c;
    L_0x017f:
        r1 = org.jivesoftware.smackx.commands.AdHocCommand.Status.completed;	 Catch:{ XMPPException -> 0x0192 }
        r2.setStatus(r1);	 Catch:{ XMPPException -> 0x0192 }
    L_0x0184:
        r1 = r8.connection;	 Catch:{ XMPPException -> 0x0192 }
        r1.sendPacket(r2);	 Catch:{ XMPPException -> 0x0192 }
    L_0x0189:
        monitor-exit(r0);	 Catch:{ all -> 0x0139 }
        goto L_0x0008;
    L_0x018c:
        r1 = org.jivesoftware.smackx.commands.AdHocCommand.Status.executing;	 Catch:{ XMPPException -> 0x0192 }
        r2.setStatus(r1);	 Catch:{ XMPPException -> 0x0192 }
        goto L_0x0184;
    L_0x0192:
        r1 = move-exception;
        r4 = r1.getXMPPError();	 Catch:{ all -> 0x0139 }
        r5 = org.jivesoftware.smack.packet.XMPPError.Type.CANCEL;	 Catch:{ all -> 0x0139 }
        r6 = r4.getType();	 Catch:{ all -> 0x0139 }
        r5 = r5.equals(r6);	 Catch:{ all -> 0x0139 }
        if (r5 == 0) goto L_0x01ad;
    L_0x01a3:
        r5 = org.jivesoftware.smackx.commands.AdHocCommand.Status.canceled;	 Catch:{ all -> 0x0139 }
        r2.setStatus(r5);	 Catch:{ all -> 0x0139 }
        r5 = r8.executingCommands;	 Catch:{ all -> 0x0139 }
        r5.remove(r3);	 Catch:{ all -> 0x0139 }
    L_0x01ad:
        r8.respondError(r2, r4);	 Catch:{ all -> 0x0139 }
        r1.printStackTrace();	 Catch:{ all -> 0x0139 }
        goto L_0x0189;
    L_0x01b4:
        r4 = org.jivesoftware.smackx.commands.AdHocCommand.Action.complete;	 Catch:{ XMPPException -> 0x0192 }
        r4 = r4.equals(r1);	 Catch:{ XMPPException -> 0x0192 }
        if (r4 == 0) goto L_0x01d6;
    L_0x01bc:
        r0.incrementStage();	 Catch:{ XMPPException -> 0x0192 }
        r1 = new org.jivesoftware.smackx.Form;	 Catch:{ XMPPException -> 0x0192 }
        r4 = r9.getForm();	 Catch:{ XMPPException -> 0x0192 }
        r1.<init>(r4);	 Catch:{ XMPPException -> 0x0192 }
        r0.complete(r1);	 Catch:{ XMPPException -> 0x0192 }
        r1 = org.jivesoftware.smackx.commands.AdHocCommand.Status.completed;	 Catch:{ XMPPException -> 0x0192 }
        r2.setStatus(r1);	 Catch:{ XMPPException -> 0x0192 }
        r1 = r8.executingCommands;	 Catch:{ XMPPException -> 0x0192 }
        r1.remove(r3);	 Catch:{ XMPPException -> 0x0192 }
        goto L_0x0184;
    L_0x01d6:
        r4 = org.jivesoftware.smackx.commands.AdHocCommand.Action.prev;	 Catch:{ XMPPException -> 0x0192 }
        r4 = r4.equals(r1);	 Catch:{ XMPPException -> 0x0192 }
        if (r4 == 0) goto L_0x01e5;
    L_0x01de:
        r0.decrementStage();	 Catch:{ XMPPException -> 0x0192 }
        r0.prev();	 Catch:{ XMPPException -> 0x0192 }
        goto L_0x0184;
    L_0x01e5:
        r4 = org.jivesoftware.smackx.commands.AdHocCommand.Action.cancel;	 Catch:{ XMPPException -> 0x0192 }
        r1 = r4.equals(r1);	 Catch:{ XMPPException -> 0x0192 }
        if (r1 == 0) goto L_0x0184;
    L_0x01ed:
        r0.cancel();	 Catch:{ XMPPException -> 0x0192 }
        r1 = org.jivesoftware.smackx.commands.AdHocCommand.Status.canceled;	 Catch:{ XMPPException -> 0x0192 }
        r2.setStatus(r1);	 Catch:{ XMPPException -> 0x0192 }
        r1 = r8.executingCommands;	 Catch:{ XMPPException -> 0x0192 }
        r1.remove(r3);	 Catch:{ XMPPException -> 0x0192 }
        goto L_0x0184;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smackx.commands.AdHocCommandManager.processAdHocCommand(org.jivesoftware.smackx.packet.AdHocCommandData):void");
    }

    private void respondError(AdHocCommandData adHocCommandData, Condition condition) {
        respondError(adHocCommandData, new XMPPError(condition));
    }

    private void respondError(AdHocCommandData adHocCommandData, Condition condition, SpecificErrorCondition specificErrorCondition) {
        XMPPError xMPPError = new XMPPError(condition);
        xMPPError.addExtension(new SpecificError(specificErrorCondition));
        respondError(adHocCommandData, xMPPError);
    }

    private void respondError(AdHocCommandData adHocCommandData, XMPPError xMPPError) {
        adHocCommandData.setType(Type.ERROR);
        adHocCommandData.setError(xMPPError);
        this.connection.sendPacket(adHocCommandData);
    }

    private LocalCommand newInstanceOfCmd(String str, String str2) throws XMPPException {
        AdHocCommandInfo adHocCommandInfo = (AdHocCommandInfo) this.commands.get(str);
        try {
            LocalCommand commandInstance = adHocCommandInfo.getCommandInstance();
            commandInstance.setSessionID(str2);
            commandInstance.setName(adHocCommandInfo.getName());
            commandInstance.setNode(adHocCommandInfo.getNode());
            return commandInstance;
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new XMPPException(new XMPPError(Condition.interna_server_error));
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            throw new XMPPException(new XMPPError(Condition.interna_server_error));
        }
    }

    private Collection<AdHocCommandInfo> getRegisteredCommands() {
        return this.commands.values();
    }
}

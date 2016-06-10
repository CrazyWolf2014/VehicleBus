package com.cnmobi.im.util;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager.PrivateDataIQProvider;
import org.jivesoftware.smackx.bytestreams.socks5.Socks5BytestreamManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.AdHocCommandData.SpecificError;
import org.jivesoftware.smackx.packet.ChatStateExtension.Provider;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.MessageEvent;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider.BadLocaleError;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider.BadPayloadError;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider.BadSessionIDError;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider.MalformedActionError;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider.SessionExpiredError;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

public class XmppConnection {
    public static final int ENTRY_ADD = 1;
    public static final int ENTRY_DEL = 2;
    public static final int ENTRY_UPDATE = 3;
    public static final String JID_SEPARATOR = "@";
    public static final String JID_SEPARATOR_RESOURCE = "/";
    public static String SERVER_HOST;
    public static int SERVER_PORT;
    private static XMPPConnection connection;

    static {
        SERVER_PORT = 5222;
        SERVER_HOST = "im.x431.com";
        connection = null;
    }

    private static void openConnection() {
        try {
            if (connection == null || !connection.isAuthenticated()) {
                XMPPConnection.DEBUG_ENABLED = true;
                ConnectionConfiguration config = new ConnectionConfiguration(SERVER_HOST, SERVER_PORT);
                config.setReconnectionAllowed(true);
                config.setSecurityMode(SecurityMode.enabled);
                config.setSASLAuthenticationEnabled(false);
                config.setTruststorePath("/system/etc/security/cacerts.bks");
                config.setTruststorePassword("changeit");
                config.setTruststoreType("bks");
                connection = new XMPPConnection(config);
                connection.connect();
                configureConnection(ProviderManager.getInstance());
            }
        } catch (XMPPException xe) {
            xe.printStackTrace();
        }
    }

    public static XMPPConnection getConnection() {
        if (connection == null) {
            openConnection();
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.disconnect();
            } catch (Exception e) {
            }
            connection = null;
        }
    }

    private static void configureConnection(ProviderManager pm) {
        pm.addIQProvider("query", "jabber:iq:private", new PrivateDataIQProvider());
        try {
            pm.addIQProvider("query", "jabber:iq:time", Class.forName("org.jivesoftware.smackx.packet.Time"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        pm.addExtensionProvider(GroupChatInvitation.ELEMENT_NAME, "jabber:x:roster", new RosterExchangeProvider());
        pm.addExtensionProvider(GroupChatInvitation.ELEMENT_NAME, "jabber:x:event", new MessageEventProvider());
        pm.addExtensionProvider("active", "http://jabber.org/protocol/chatstates", new Provider());
        pm.addExtensionProvider(MessageEvent.COMPOSING, "http://jabber.org/protocol/chatstates", new Provider());
        pm.addExtensionProvider("paused", "http://jabber.org/protocol/chatstates", new Provider());
        pm.addExtensionProvider("inactive", "http://jabber.org/protocol/chatstates", new Provider());
        pm.addExtensionProvider("gone", "http://jabber.org/protocol/chatstates", new Provider());
        pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im", new XHTMLExtensionProvider());
        pm.addExtensionProvider(GroupChatInvitation.ELEMENT_NAME, GroupChatInvitation.NAMESPACE, new GroupChatInvitation.Provider());
        pm.addIQProvider("query", DiscoverItems.NAMESPACE, new DiscoverItemsProvider());
        pm.addIQProvider("query", DiscoverInfo.NAMESPACE, new DiscoverInfoProvider());
        pm.addExtensionProvider(GroupChatInvitation.ELEMENT_NAME, Form.NAMESPACE, new DataFormProvider());
        pm.addExtensionProvider(GroupChatInvitation.ELEMENT_NAME, "http://jabber.org/protocol/muc#user", new MUCUserProvider());
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin", new MUCAdminProvider());
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());
        pm.addExtensionProvider(GroupChatInvitation.ELEMENT_NAME, "jabber:x:delay", new DelayInformationProvider());
        try {
            pm.addIQProvider("query", "jabber:iq:version", Class.forName("org.jivesoftware.smackx.packet.Version"));
        } catch (ClassNotFoundException e2) {
        }
        pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
        pm.addIQProvider(MessageEvent.OFFLINE, "http://jabber.org/protocol/offline", new OfflineMessageRequest.Provider());
        pm.addExtensionProvider(MessageEvent.OFFLINE, "http://jabber.org/protocol/offline", new OfflineMessageInfo.Provider());
        pm.addIQProvider("query", LastActivity.NAMESPACE, new LastActivity.Provider());
        pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
        pm.addIQProvider("sharedgroup", "http://www.jivesoftware.org/protocol/sharedgroup", new SharedGroupsInfo.Provider());
        pm.addExtensionProvider("addresses", "http://jabber.org/protocol/address", new MultipleAddressesProvider());
        pm.addIQProvider("si", "http://jabber.org/protocol/si", new StreamInitiationProvider());
        pm.addIQProvider("query", Socks5BytestreamManager.NAMESPACE, new BytestreamsProvider());
        pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
        pm.addIQProvider("command", SpecificError.namespace, new AdHocCommandDataProvider());
        pm.addExtensionProvider("malformed-action", SpecificError.namespace, new MalformedActionError());
        pm.addExtensionProvider("bad-locale", SpecificError.namespace, new BadLocaleError());
        pm.addExtensionProvider("bad-payload", SpecificError.namespace, new BadPayloadError());
        pm.addExtensionProvider("bad-sessionid", SpecificError.namespace, new BadSessionIDError());
        pm.addExtensionProvider("session-expired", SpecificError.namespace, new SessionExpiredError());
    }
}

package com.kenai.jbosh;

import com.cnlaunch.framework.common.Constants;
import com.cnmobi.im.dto.Msg;
import com.launch.rcu.socket.SocketCode;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.jivesoftware.smackx.packet.MultipleAddresses;

final class Attributes {
    static final BodyQName ACCEPT;
    static final BodyQName ACK;
    static final BodyQName AUTHID;
    static final BodyQName CHARSETS;
    static final BodyQName CONDITION;
    static final BodyQName CONTENT;
    static final BodyQName FROM;
    static final BodyQName HOLD;
    static final BodyQName INACTIVITY;
    static final BodyQName KEY;
    static final BodyQName MAXPAUSE;
    static final BodyQName NEWKEY;
    static final BodyQName PAUSE;
    static final BodyQName POLLING;
    static final BodyQName REPORT;
    static final BodyQName REQUESTS;
    static final BodyQName RID;
    static final BodyQName ROUTE;
    static final BodyQName SECURE;
    static final BodyQName SID;
    static final BodyQName STREAM;
    static final BodyQName TIME;
    static final BodyQName TO;
    static final BodyQName TYPE;
    static final BodyQName VER;
    static final BodyQName WAIT;
    static final BodyQName XML_LANG;

    private Attributes() {
    }

    static {
        ACCEPT = BodyQName.createBOSH("accept");
        AUTHID = BodyQName.createBOSH("authid");
        ACK = BodyQName.createBOSH("ack");
        CHARSETS = BodyQName.createBOSH("charsets");
        CONDITION = BodyQName.createBOSH("condition");
        CONTENT = BodyQName.createBOSH("content");
        FROM = BodyQName.createBOSH(PrivacyRule.SUBSCRIPTION_FROM);
        HOLD = BodyQName.createBOSH("hold");
        INACTIVITY = BodyQName.createBOSH("inactivity");
        KEY = BodyQName.createBOSH(SharedPref.KEY);
        MAXPAUSE = BodyQName.createBOSH("maxpause");
        NEWKEY = BodyQName.createBOSH("newkey");
        PAUSE = BodyQName.createBOSH(SocketCode.REMOTE_START);
        POLLING = BodyQName.createBOSH("polling");
        REPORT = BodyQName.createBOSH("report");
        REQUESTS = BodyQName.createBOSH("requests");
        RID = BodyQName.createBOSH("rid");
        ROUTE = BodyQName.createBOSH("route");
        SECURE = BodyQName.createBOSH("secure");
        SID = BodyQName.createBOSH(AlixDefine.SID);
        STREAM = BodyQName.createBOSH("stream");
        TIME = BodyQName.createBOSH(Msg.TIME_REDIO);
        TO = BodyQName.createBOSH(MultipleAddresses.TO);
        TYPE = BodyQName.createBOSH(SharedPref.TYPE);
        VER = BodyQName.createBOSH(Constants.VER);
        WAIT = BodyQName.createBOSH("wait");
        XML_LANG = BodyQName.createWithPrefix("http://www.w3.org/XML/1998/namespace", "lang", "xml");
    }
}

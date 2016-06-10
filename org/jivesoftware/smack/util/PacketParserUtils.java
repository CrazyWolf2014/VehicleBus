package org.jivesoftware.smack.util;

import com.amap.mapapi.location.LocationManagerProxy;
import com.cnlaunch.framework.common.Constants;
import com.cnmobi.im.dto.MessageVo;
import com.launch.service.BundleBuilder;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import com.tencent.mm.sdk.plugin.BaseProfile;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.packet.Authentication;
import org.jivesoftware.smack.packet.Bind;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.RosterPacket.Item;
import org.jivesoftware.smack.packet.RosterPacket.ItemStatus;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;
import org.jivesoftware.smack.packet.StreamError;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.sasl.SASLMechanism.Failure;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class PacketParserUtils {
    private static final String PROPERTIES_NAMESPACE = "http://www.jivesoftware.com/xmlns/xmpp/properties";

    /* renamed from: org.jivesoftware.smack.util.PacketParserUtils.1 */
    static class C12861 extends IQ {
        C12861() {
        }

        public String getChildElementXML() {
            return null;
        }
    }

    /* renamed from: org.jivesoftware.smack.util.PacketParserUtils.2 */
    static class C12872 extends IQ {
        C12872() {
        }

        public String getChildElementXML() {
            return null;
        }
    }

    public static Packet parseMessage(XmlPullParser xmlPullParser) throws Exception {
        Map map = null;
        Packet message = new Message();
        String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, LocaleUtil.INDONESIAN);
        if (attributeValue == null) {
            attributeValue = Packet.ID_NOT_AVAILABLE;
        }
        message.setPacketID(attributeValue);
        message.setTo(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, MultipleAddresses.TO));
        message.setFrom(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, PrivacyRule.SUBSCRIPTION_FROM));
        message.setType(Type.fromString(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, SharedPref.TYPE)));
        attributeValue = getLanguageAttribute(xmlPullParser);
        if (attributeValue == null || XmlPullParser.NO_NAMESPACE.equals(attributeValue.trim())) {
            attributeValue = Packet.getDefaultLanguage();
        } else {
            message.setLanguage(attributeValue);
        }
        Object obj = null;
        String str = null;
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                String name = xmlPullParser.getName();
                String namespace = xmlPullParser.getNamespace();
                if (name.equals("subject")) {
                    name = getLanguageAttribute(xmlPullParser);
                    if (name == null) {
                        name = attributeValue;
                    }
                    namespace = parseContent(xmlPullParser);
                    if (message.getSubject(name) == null) {
                        message.addSubject(name, namespace);
                    }
                } else if (name.equals("body")) {
                    name = getLanguageAttribute(xmlPullParser);
                    if (name == null) {
                        name = attributeValue;
                    }
                    namespace = parseContent(xmlPullParser);
                    if (message.getBody(name) == null) {
                        message.addBody(name, namespace);
                    }
                } else if (name.equals("thread")) {
                    if (str == null) {
                        str = xmlPullParser.nextText();
                    }
                } else if (name.equals("error")) {
                    message.setError(parseError(xmlPullParser));
                } else if (name.equals("properties") && namespace.equals(PROPERTIES_NAMESPACE)) {
                    map = parseProperties(xmlPullParser);
                } else {
                    message.addExtension(parsePacketExtension(name, namespace, xmlPullParser));
                }
            } else if (next == 3 && xmlPullParser.getName().equals(BundleBuilder.AskFromMessage)) {
                obj = 1;
            }
        }
        message.setThread(str);
        if (map != null) {
            for (String attributeValue2 : map.keySet()) {
                message.setProperty(attributeValue2, map.get(attributeValue2));
            }
        }
        return message;
    }

    private static String parseContent(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        String str = XmlPullParser.NO_NAMESPACE;
        int depth = xmlPullParser.getDepth();
        while (true) {
            if (xmlPullParser.next() == 3 && xmlPullParser.getDepth() == depth) {
                return str;
            }
            str = str + xmlPullParser.getText();
        }
    }

    public static Presence parsePresence(XmlPullParser xmlPullParser) throws Exception {
        String str;
        Presence.Type type = Presence.Type.available;
        String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, SharedPref.TYPE);
        if (!(attributeValue == null || attributeValue.equals(XmlPullParser.NO_NAMESPACE))) {
            try {
                type = Presence.Type.valueOf(attributeValue);
            } catch (IllegalArgumentException e) {
                System.err.println("Found invalid presence type " + attributeValue);
            }
        }
        Presence presence = new Presence(type);
        presence.setTo(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, MultipleAddresses.TO));
        presence.setFrom(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, PrivacyRule.SUBSCRIPTION_FROM));
        attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, LocaleUtil.INDONESIAN);
        if (attributeValue == null) {
            str = Packet.ID_NOT_AVAILABLE;
        } else {
            str = attributeValue;
        }
        presence.setPacketID(str);
        str = getLanguageAttribute(xmlPullParser);
        if (!(str == null || XmlPullParser.NO_NAMESPACE.equals(str.trim()))) {
            presence.setLanguage(str);
        }
        if (attributeValue == null) {
            attributeValue = Packet.ID_NOT_AVAILABLE;
        }
        presence.setPacketID(attributeValue);
        int i = 0;
        while (i == 0) {
            int next = xmlPullParser.next();
            if (next == 2) {
                str = xmlPullParser.getName();
                String namespace = xmlPullParser.getNamespace();
                if (str.equals(LocationManagerProxy.KEY_STATUS_CHANGED)) {
                    presence.setStatus(xmlPullParser.nextText());
                } else if (str.equals("priority")) {
                    try {
                        presence.setPriority(Integer.parseInt(xmlPullParser.nextText()));
                    } catch (NumberFormatException e2) {
                    } catch (IllegalArgumentException e3) {
                        presence.setPriority(0);
                    }
                } else if (str.equals("show")) {
                    str = xmlPullParser.nextText();
                    try {
                        presence.setMode(Mode.valueOf(str));
                    } catch (IllegalArgumentException e4) {
                        System.err.println("Found invalid presence mode " + str);
                    }
                } else if (str.equals("error")) {
                    presence.setError(parseError(xmlPullParser));
                } else if (str.equals("properties") && namespace.equals(PROPERTIES_NAMESPACE)) {
                    Map parseProperties = parseProperties(xmlPullParser);
                    for (String str2 : parseProperties.keySet()) {
                        presence.setProperty(str2, parseProperties.get(str2));
                    }
                } else {
                    presence.addExtension(parsePacketExtension(str2, namespace, xmlPullParser));
                }
                next = i;
            } else if (next == 3 && xmlPullParser.getName().equals("presence")) {
                next = 1;
            } else {
                next = i;
            }
            i = next;
        }
        return presence;
    }

    public static IQ parseIQ(XmlPullParser xmlPullParser, Connection connection) throws Exception {
        String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, LocaleUtil.INDONESIAN);
        String attributeValue2 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, MultipleAddresses.TO);
        String attributeValue3 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, PrivacyRule.SUBSCRIPTION_FROM);
        IQ.Type fromString = IQ.Type.fromString(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, SharedPref.TYPE));
        Object obj = null;
        XMPPError xMPPError = null;
        IQ iq = null;
        while (obj == null) {
            Object iQProvider;
            int next = xmlPullParser.next();
            if (next == 2) {
                XMPPError parseError;
                IQ iq2;
                String name = xmlPullParser.getName();
                String namespace = xmlPullParser.getNamespace();
                if (name.equals("error")) {
                    parseError = parseError(xmlPullParser);
                    iq2 = iq;
                } else if (name.equals("query") && namespace.equals("jabber:iq:auth")) {
                    r11 = xMPPError;
                    iq2 = parseAuthentication(xmlPullParser);
                    parseError = r11;
                } else if (name.equals("query") && namespace.equals("jabber:iq:roster")) {
                    r11 = xMPPError;
                    iq2 = parseRoster(xmlPullParser);
                    parseError = r11;
                } else if (name.equals("query") && namespace.equals("jabber:iq:register")) {
                    r11 = xMPPError;
                    iq2 = parseRegistration(xmlPullParser);
                    parseError = r11;
                } else if (name.equals("bind") && namespace.equals("urn:ietf:params:xml:ns:xmpp-bind")) {
                    r11 = xMPPError;
                    iq2 = parseResourceBinding(xmlPullParser);
                    parseError = r11;
                } else {
                    iQProvider = ProviderManager.getInstance().getIQProvider(name, namespace);
                    if (iQProvider != null) {
                        if (iQProvider instanceof IQProvider) {
                            r11 = xMPPError;
                            iq2 = ((IQProvider) iQProvider).parseIQ(xmlPullParser);
                            parseError = r11;
                        } else if (iQProvider instanceof Class) {
                            r11 = xMPPError;
                            iq2 = (IQ) parseWithIntrospection(name, (Class) iQProvider, xmlPullParser);
                            parseError = r11;
                        }
                    }
                    parseError = xMPPError;
                    iq2 = iq;
                }
                iq = iq2;
                xMPPError = parseError;
                iQProvider = obj;
            } else if (next == 3 && xmlPullParser.getName().equals("iq")) {
                iQProvider = 1;
            } else {
                iQProvider = obj;
            }
            obj = iQProvider;
        }
        if (iq == null) {
            if (IQ.Type.GET == fromString || IQ.Type.SET == fromString) {
                Packet c12861 = new C12861();
                c12861.setPacketID(attributeValue);
                c12861.setTo(attributeValue3);
                c12861.setFrom(attributeValue2);
                c12861.setType(IQ.Type.ERROR);
                c12861.setError(new XMPPError(Condition.feature_not_implemented));
                connection.sendPacket(c12861);
                return null;
            }
            iq = new C12872();
        }
        iq.setPacketID(attributeValue);
        iq.setTo(attributeValue2);
        iq.setFrom(attributeValue3);
        iq.setType(fromString);
        iq.setError(xMPPError);
        return iq;
    }

    private static Authentication parseAuthentication(XmlPullParser xmlPullParser) throws Exception {
        Authentication authentication = new Authentication();
        Object obj = null;
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals(BaseProfile.COL_USERNAME)) {
                    authentication.setUsername(xmlPullParser.nextText());
                } else if (xmlPullParser.getName().equals("password")) {
                    authentication.setPassword(xmlPullParser.nextText());
                } else if (xmlPullParser.getName().equals("digest")) {
                    authentication.setDigest(xmlPullParser.nextText());
                } else if (xmlPullParser.getName().equals("resource")) {
                    authentication.setResource(xmlPullParser.nextText());
                }
            } else if (next == 3 && xmlPullParser.getName().equals("query")) {
                obj = 1;
            }
        }
        return authentication;
    }

    private static RosterPacket parseRoster(XmlPullParser xmlPullParser) throws Exception {
        RosterPacket rosterPacket = new RosterPacket();
        Object obj = null;
        Item item = null;
        while (obj == null) {
            Object obj2;
            if (xmlPullParser.getEventType() == 2 && xmlPullParser.getName().equals("query")) {
                rosterPacket.setVersion(xmlPullParser.getAttributeValue(null, Constants.VER));
            }
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals("item")) {
                    Item item2 = new Item(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "jid"), xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "name"));
                    item2.setItemStatus(ItemStatus.fromString(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "ask")));
                    String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "subscription");
                    if (attributeValue == null) {
                        attributeValue = PrivacyRule.SUBSCRIPTION_NONE;
                    }
                    item2.setItemType(ItemType.valueOf(attributeValue));
                    item = item2;
                }
                if (xmlPullParser.getName().equals("group") && item != null) {
                    String nextText = xmlPullParser.nextText();
                    if (nextText != null && nextText.trim().length() > 0) {
                        item.addGroupName(nextText);
                    }
                    obj2 = obj;
                }
                obj2 = obj;
            } else {
                if (next == 3) {
                    if (xmlPullParser.getName().equals("item")) {
                        rosterPacket.addRosterItem(item);
                    }
                    if (xmlPullParser.getName().equals("query")) {
                        obj2 = 1;
                    }
                }
                obj2 = obj;
            }
            obj = obj2;
        }
        return rosterPacket;
    }

    private static Registration parseRegistration(XmlPullParser xmlPullParser) throws Exception {
        Registration registration = new Registration();
        Map map = null;
        Object obj = null;
        while (obj == null) {
            Map map2;
            Object obj2;
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getNamespace().equals("jabber:iq:register")) {
                    String name = xmlPullParser.getName();
                    String str = XmlPullParser.NO_NAMESPACE;
                    if (map == null) {
                        map = new HashMap();
                    }
                    if (xmlPullParser.next() == 4) {
                        str = xmlPullParser.getText();
                    }
                    if (name.equals("instructions")) {
                        registration.setInstructions(str);
                    } else {
                        map.put(name, str);
                    }
                    map2 = map;
                    obj2 = obj;
                } else {
                    registration.addExtension(parsePacketExtension(xmlPullParser.getName(), xmlPullParser.getNamespace(), xmlPullParser));
                    map2 = map;
                    obj2 = obj;
                }
            } else if (next == 3 && xmlPullParser.getName().equals("query")) {
                map2 = map;
                int i = 1;
            } else {
                map2 = map;
                obj2 = obj;
            }
            obj = obj2;
            map = map2;
        }
        registration.setAttributes(map);
        return registration;
    }

    private static Bind parseResourceBinding(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        Bind bind = new Bind();
        Object obj = null;
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals("resource")) {
                    bind.setResource(xmlPullParser.nextText());
                } else if (xmlPullParser.getName().equals("jid")) {
                    bind.setJid(xmlPullParser.nextText());
                }
            } else if (next == 3 && xmlPullParser.getName().equals("bind")) {
                obj = 1;
            }
        }
        return bind;
    }

    public static Collection<String> parseMechanisms(XmlPullParser xmlPullParser) throws Exception {
        Collection arrayList = new ArrayList();
        Object obj = null;
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals("mechanism")) {
                    arrayList.add(xmlPullParser.nextText());
                }
            } else if (next == 3 && xmlPullParser.getName().equals("mechanisms")) {
                obj = 1;
            }
        }
        return arrayList;
    }

    public static Collection<String> parseCompressionMethods(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        Collection arrayList = new ArrayList();
        Object obj = null;
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals("method")) {
                    arrayList.add(xmlPullParser.nextText());
                }
            } else if (next == 3 && xmlPullParser.getName().equals("compression")) {
                obj = 1;
            }
        }
        return arrayList;
    }

    public static Map<String, Object> parseProperties(XmlPullParser xmlPullParser) throws Exception {
        Map<String, Object> hashMap = new HashMap();
        while (true) {
            int next = xmlPullParser.next();
            if (next == 2 && xmlPullParser.getName().equals("property")) {
                Object obj = null;
                Object obj2 = null;
                Object obj3 = null;
                Object obj4 = null;
                String str = null;
                while (obj4 == null) {
                    int next2 = xmlPullParser.next();
                    if (next2 == 2) {
                        String name = xmlPullParser.getName();
                        if (name.equals("name")) {
                            obj3 = xmlPullParser.nextText();
                        } else if (name.equals(SharedPref.VALUE)) {
                            obj2 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, SharedPref.TYPE);
                            str = xmlPullParser.nextText();
                        }
                    } else if (next2 == 3 && xmlPullParser.getName().equals("property")) {
                        if ("integer".equals(obj2)) {
                            obj4 = Integer.valueOf(str);
                        } else if ("long".equals(obj2)) {
                            Long valueOf = Long.valueOf(str);
                        } else if ("float".equals(obj2)) {
                            Float valueOf2 = Float.valueOf(str);
                        } else if ("double".equals(obj2)) {
                            Double valueOf3 = Double.valueOf(str);
                        } else if (FormField.TYPE_BOOLEAN.equals(obj2)) {
                            Boolean valueOf4 = Boolean.valueOf(str);
                        } else if ("string".equals(obj2)) {
                            String str2 = str;
                        } else {
                            if ("java-object".equals(obj2)) {
                                try {
                                    obj4 = new ObjectInputStream(new ByteArrayInputStream(StringUtils.decodeBase64(str))).readObject();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            obj4 = obj;
                        }
                        if (!(obj3 == null || obj4 == null)) {
                            hashMap.put(obj3, obj4);
                        }
                        Object obj5 = obj4;
                        obj4 = 1;
                        obj = obj5;
                    }
                }
            } else if (next == 3 && xmlPullParser.getName().equals("properties")) {
                return hashMap;
            }
        }
    }

    public static Failure parseSASLFailure(XmlPullParser xmlPullParser) throws Exception {
        String str = null;
        Object obj = null;
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (!xmlPullParser.getName().equals("failure")) {
                    str = xmlPullParser.getName();
                }
            } else if (next == 3 && xmlPullParser.getName().equals("failure")) {
                obj = 1;
            }
        }
        return new Failure(str);
    }

    public static StreamError parseStreamError(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        StreamError streamError = null;
        Object obj = null;
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                streamError = new StreamError(xmlPullParser.getName());
            } else if (next == 3 && xmlPullParser.getName().equals("error")) {
                obj = 1;
            }
        }
        return streamError;
    }

    public static XMPPError parseError(XmlPullParser xmlPullParser) throws Exception {
        String attributeValue;
        XMPPError.Type valueOf;
        XMPPError.Type type;
        String str = null;
        List arrayList = new ArrayList();
        int i = 0;
        String str2 = "-1";
        String str3 = null;
        while (i < xmlPullParser.getAttributeCount()) {
            if (xmlPullParser.getAttributeName(i).equals("code")) {
                attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "code");
            } else {
                attributeValue = str2;
            }
            if (xmlPullParser.getAttributeName(i).equals(SharedPref.TYPE)) {
                str3 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, SharedPref.TYPE);
            }
            i++;
            str2 = attributeValue;
        }
        Object obj = null;
        attributeValue = null;
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals(MessageVo.TYPE_TEXT)) {
                    attributeValue = xmlPullParser.nextText();
                } else {
                    String name = xmlPullParser.getName();
                    String namespace = xmlPullParser.getNamespace();
                    if ("urn:ietf:params:xml:ns:xmpp-stanzas".equals(namespace)) {
                        str = name;
                    } else {
                        arrayList.add(parsePacketExtension(name, namespace, xmlPullParser));
                    }
                }
            } else if (next == 3 && xmlPullParser.getName().equals("error")) {
                obj = 1;
            }
        }
        XMPPError.Type type2 = XMPPError.Type.CANCEL;
        if (str3 != null) {
            try {
                valueOf = XMPPError.Type.valueOf(str3.toUpperCase());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                type = type2;
            }
        } else {
            valueOf = type2;
        }
        type = valueOf;
        return new XMPPError(Integer.parseInt(str2), type, str, attributeValue, arrayList);
    }

    public static PacketExtension parsePacketExtension(String str, String str2, XmlPullParser xmlPullParser) throws Exception {
        Object extensionProvider = ProviderManager.getInstance().getExtensionProvider(str, str2);
        if (extensionProvider != null) {
            if (extensionProvider instanceof PacketExtensionProvider) {
                return ((PacketExtensionProvider) extensionProvider).parseExtension(xmlPullParser);
            }
            if (extensionProvider instanceof Class) {
                return (PacketExtension) parseWithIntrospection(str, (Class) extensionProvider, xmlPullParser);
            }
        }
        DefaultPacketExtension defaultPacketExtension = new DefaultPacketExtension(str, str2);
        extensionProvider = null;
        while (extensionProvider == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                String name = xmlPullParser.getName();
                if (xmlPullParser.isEmptyElementTag()) {
                    defaultPacketExtension.setValue(name, XmlPullParser.NO_NAMESPACE);
                } else if (xmlPullParser.next() == 4) {
                    defaultPacketExtension.setValue(name, xmlPullParser.getText());
                }
            } else if (next == 3 && xmlPullParser.getName().equals(str)) {
                extensionProvider = 1;
            }
        }
        return defaultPacketExtension;
    }

    private static String getLanguageAttribute(XmlPullParser xmlPullParser) {
        int i = 0;
        while (i < xmlPullParser.getAttributeCount()) {
            String attributeName = xmlPullParser.getAttributeName(i);
            if ("xml:lang".equals(attributeName) || ("lang".equals(attributeName) && "xml".equals(xmlPullParser.getAttributePrefix(i)))) {
                return xmlPullParser.getAttributeValue(i);
            }
            i++;
        }
        return null;
    }

    public static Object parseWithIntrospection(String str, Class<?> cls, XmlPullParser xmlPullParser) throws Exception {
        Object newInstance = cls.newInstance();
        int i = 0;
        while (i == 0) {
            int next = xmlPullParser.next();
            if (next == 2) {
                String name = xmlPullParser.getName();
                String nextText = xmlPullParser.nextText();
                Object decode = decode(newInstance.getClass().getMethod("get" + Character.toUpperCase(name.charAt(0)) + name.substring(1), new Class[0]).getReturnType(), nextText);
                newInstance.getClass().getMethod("set" + Character.toUpperCase(name.charAt(0)) + name.substring(1), new Class[]{r6}).invoke(newInstance, new Object[]{decode});
            } else if (next == 3 && xmlPullParser.getName().equals(str)) {
                i = 1;
            }
        }
        return newInstance;
    }

    private static Object decode(Class<?> cls, String str) throws Exception {
        if (cls.getName().equals("java.lang.String")) {
            return str;
        }
        if (cls.getName().equals(FormField.TYPE_BOOLEAN)) {
            return Boolean.valueOf(str);
        }
        if (cls.getName().equals("int")) {
            return Integer.valueOf(str);
        }
        if (cls.getName().equals("long")) {
            return Long.valueOf(str);
        }
        if (cls.getName().equals("float")) {
            return Float.valueOf(str);
        }
        if (cls.getName().equals("double")) {
            return Double.valueOf(str);
        }
        if (cls.getName().equals("java.lang.Class")) {
            return Class.forName(str);
        }
        return null;
    }
}

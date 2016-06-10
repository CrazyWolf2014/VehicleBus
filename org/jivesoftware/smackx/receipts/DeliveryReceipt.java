package org.jivesoftware.smackx.receipts;

import com.tencent.mm.sdk.platformtools.LocaleUtil;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.EmbeddedExtensionProvider;

public class DeliveryReceipt implements PacketExtension {
    public static final String ELEMENT = "received";
    public static final String NAMESPACE = "urn:xmpp:receipts";
    private String id;

    public static class Provider extends EmbeddedExtensionProvider {
        protected PacketExtension createReturnExtension(String str, String str2, Map<String, String> map, List<? extends PacketExtension> list) {
            return new DeliveryReceipt((String) map.get(LocaleUtil.INDONESIAN));
        }
    }

    public DeliveryReceipt(String str) {
        this.id = str;
    }

    public String getId() {
        return this.id;
    }

    public String getElementName() {
        return ELEMENT;
    }

    public String getNamespace() {
        return NAMESPACE;
    }

    public String toXML() {
        return "<received xmlns='urn:xmpp:receipts' id='" + this.id + "'/>";
    }
}

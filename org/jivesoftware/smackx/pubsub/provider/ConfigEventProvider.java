package org.jivesoftware.smackx.pubsub.provider;

import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.provider.EmbeddedExtensionProvider;
import org.jivesoftware.smackx.pubsub.ConfigurationEvent;
import org.jivesoftware.smackx.pubsub.ConfigureForm;

public class ConfigEventProvider extends EmbeddedExtensionProvider {
    protected PacketExtension createReturnExtension(String str, String str2, Map<String, String> map, List<? extends PacketExtension> list) {
        if (list.size() == 0) {
            return new ConfigurationEvent((String) map.get("node"));
        }
        return new ConfigurationEvent((String) map.get("node"), new ConfigureForm((DataForm) list.iterator().next()));
    }
}

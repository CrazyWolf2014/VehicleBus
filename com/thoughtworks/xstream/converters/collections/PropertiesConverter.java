package com.thoughtworks.xstream.converters.collections;

import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.core.util.Fields;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

public class PropertiesConverter implements Converter {
    private static final Field defaultsField;
    private final boolean sort;

    static {
        defaultsField = Fields.locate(Properties.class, Properties.class, false);
    }

    public PropertiesConverter() {
        this(false);
    }

    public PropertiesConverter(boolean sort) {
        this.sort = sort;
    }

    public boolean canConvert(Class type) {
        return Properties.class == type;
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        Properties properties = (Properties) source;
        if (this.sort) {
            Map map = new TreeMap(properties);
        } else {
            Object map2 = properties;
        }
        for (Entry entry : map.entrySet()) {
            writer.startNode("property");
            writer.addAttribute("name", entry.getKey().toString());
            writer.addAttribute(SharedPref.VALUE, entry.getValue().toString());
            writer.endNode();
        }
        if (defaultsField != null) {
            Properties defaults = (Properties) Fields.read(defaultsField, properties);
            if (defaults != null) {
                writer.startNode("defaults");
                marshal(defaults, writer, context);
                writer.endNode();
            }
        }
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Properties properties = new Properties();
        Properties defaults = null;
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            if (reader.getNodeName().equals("defaults")) {
                defaults = (Properties) unmarshal(reader, context);
            } else {
                properties.setProperty(reader.getAttribute("name"), reader.getAttribute(SharedPref.VALUE));
            }
            reader.moveUp();
        }
        if (defaults == null) {
            return properties;
        }
        Properties propertiesWithDefaults = new Properties(defaults);
        propertiesWithDefaults.putAll(properties);
        return propertiesWithDefaults;
    }
}

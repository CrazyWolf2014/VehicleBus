package com.thoughtworks.xstream.mapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.xmlpull.v1.XmlPullParser;

public class PackageAliasingMapper extends MapperWrapper implements Serializable {
    private static final Comparator REVERSE;
    protected transient Map nameToPackage;
    private Map packageToName;

    /* renamed from: com.thoughtworks.xstream.mapper.PackageAliasingMapper.1 */
    static class C09051 implements Comparator {
        C09051() {
        }

        public int compare(Object o1, Object o2) {
            return ((String) o2).compareTo((String) o1);
        }
    }

    static {
        REVERSE = new C09051();
    }

    public PackageAliasingMapper(Mapper wrapped) {
        super(wrapped);
        this.packageToName = new TreeMap(REVERSE);
        this.nameToPackage = new HashMap();
    }

    public void addPackageAlias(String name, String pkg) {
        if (name.length() > 0 && name.charAt(name.length() - 1) != '.') {
            name = name + '.';
        }
        if (pkg.length() > 0 && pkg.charAt(pkg.length() - 1) != '.') {
            pkg = pkg + '.';
        }
        this.nameToPackage.put(name, pkg);
        this.packageToName.put(pkg, name);
    }

    public String serializedClass(Class type) {
        String className = type.getName();
        int length = className.length();
        int dot;
        do {
            dot = className.lastIndexOf(46, length);
            String alias = (String) this.packageToName.get(dot < 0 ? XmlPullParser.NO_NAMESPACE : className.substring(0, dot + 1));
            if (alias != null) {
                StringBuilder append = new StringBuilder().append(alias);
                if (dot >= 0) {
                    className = className.substring(dot + 1);
                }
                return append.append(className).toString();
            }
            length = dot - 1;
        } while (dot >= 0);
        return super.serializedClass(type);
    }

    public Class realClass(String elementName) {
        int length = elementName.length();
        int dot;
        do {
            dot = elementName.lastIndexOf(46, length);
            String packageName = (String) this.nameToPackage.get(dot < 0 ? XmlPullParser.NO_NAMESPACE : elementName.substring(0, dot) + '.');
            if (packageName != null) {
                StringBuilder append = new StringBuilder().append(packageName);
                if (dot >= 0) {
                    elementName = elementName.substring(dot + 1);
                }
                elementName = append.append(elementName).toString();
            } else {
                length = dot - 1;
            }
            return super.realClass(elementName);
        } while (dot >= 0);
        return super.realClass(elementName);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(new HashMap(this.packageToName));
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.packageToName = new TreeMap(REVERSE);
        this.packageToName.putAll((Map) in.readObject());
        this.nameToPackage = new HashMap();
        for (Object type : this.packageToName.keySet()) {
            this.nameToPackage.put(this.packageToName.get(type), type);
        }
    }
}

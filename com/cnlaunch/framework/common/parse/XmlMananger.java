package com.cnlaunch.framework.common.parse;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import java.io.InputStream;

public class XmlMananger {
    private static XmlMananger instance;
    private final String tag;
    private XStream xmlMapper;

    /* renamed from: com.cnlaunch.framework.common.parse.XmlMananger.1 */
    class C10401 extends XStream {

        /* renamed from: com.cnlaunch.framework.common.parse.XmlMananger.1.1 */
        class C12681 extends MapperWrapper {
            C12681(Mapper $anonymous0) {
                super($anonymous0);
            }

            public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                if (definedIn == Object.class) {
                    return false;
                }
                return super.shouldSerializeMember(definedIn, fieldName);
            }
        }

        C10401(HierarchicalStreamDriver $anonymous0) {
            super($anonymous0);
        }

        protected MapperWrapper wrapMapper(MapperWrapper next) {
            return new C12681(next);
        }
    }

    private XmlMananger() {
        this.tag = XmlMananger.class.getSimpleName();
        if (this.xmlMapper == null) {
            this.xmlMapper = new C10401(new XppDriver());
        }
    }

    public static XmlMananger getInstance() {
        if (instance == null) {
            synchronized (XmlMananger.class) {
                if (instance == null) {
                    instance = new XmlMananger();
                }
            }
        }
        return instance;
    }

    public <T> T xmlToBean(String xml, Class<T> cls) {
        this.xmlMapper.processAnnotations((Class) cls);
        return this.xmlMapper.fromXML(xml);
    }

    public <T> T xmlToBean(InputStream xml, Class<T> cls) {
        this.xmlMapper.processAnnotations((Class) cls);
        return this.xmlMapper.fromXML(xml);
    }

    public String beanToXml(Object obj) {
        return this.xmlMapper.toXML(obj);
    }

    public XStream getXmlMapper() {
        return this.xmlMapper;
    }
}

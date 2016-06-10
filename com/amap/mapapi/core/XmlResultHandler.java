package com.amap.mapapi.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* renamed from: com.amap.mapapi.core.v */
public abstract class XmlResultHandler<T, V> extends ProtocalHandler<T, V> {
    private ArrayList<String> f1798i;

    protected abstract V m1878b(NodeList nodeList);

    public XmlResultHandler(T t, Proxy proxy, String str, String str2) {
        super(t, proxy, str, str2);
        this.f1798i = new ArrayList();
    }

    protected NodeList m1876a(InputStream inputStream) throws AMapException {
        return CoreUtil.m492b(m525c(inputStream)).getDocumentElement().getChildNodes();
    }

    protected V m1877b(InputStream inputStream) throws AMapException {
        V b = m1878b(m1876a(inputStream));
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new AMapException(AMapException.ERROR_IO);
            }
        }
        return b;
    }

    protected String m1875a(Node node) {
        if (node == null) {
            return null;
        }
        Node firstChild = node.getFirstChild();
        if (firstChild == null || firstChild.getNodeType() != (short) 3) {
            return null;
        }
        String nodeValue = firstChild.getNodeValue();
        int indexOf = nodeValue.indexOf("ppppppppShitJava");
        if (indexOf < 0) {
            return nodeValue;
        }
        return (String) this.f1798i.get(Integer.parseInt(nodeValue.substring(indexOf + "ppppppppShitJava".length())));
    }
}

package com.amap.mapapi.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* renamed from: com.amap.mapapi.core.t */
public abstract class XmlListResultHandler<T, V> extends XmlResultHandler<T, ArrayList<V>> {
    protected abstract void m2512a(ArrayList<V> arrayList);

    protected abstract void m2513a(Node node, ArrayList<V> arrayList);

    protected /* synthetic */ Object m2514b(InputStream inputStream) throws AMapException {
        return m2516d(inputStream);
    }

    protected /* synthetic */ Object m2515b(NodeList nodeList) {
        return m2511a(nodeList);
    }

    public XmlListResultHandler(T t, Proxy proxy, String str, String str2) {
        super(t, proxy, str, str2);
    }

    protected ArrayList<V> m2511a(NodeList nodeList) {
        return null;
    }

    protected ArrayList<V> m2516d(InputStream inputStream) throws AMapException {
        NodeList a = m1876a(inputStream);
        ArrayList arrayList = new ArrayList();
        int length = a.getLength();
        for (int i = 0; i < length; i++) {
            m2513a(a.item(i), arrayList);
        }
        m2512a(arrayList);
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new AMapException(AMapException.ERROR_IO);
            }
        }
        return arrayList;
    }
}

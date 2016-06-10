package com.amap.mapapi.core;

import com.amap.mapapi.core.GeoPoint.C0086b;
import java.net.Proxy;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* renamed from: com.amap.mapapi.core.h */
public class LocTansServerHandler extends XmlResultHandler<C0086b, C0086b> {
    private C0086b f2354i;

    protected /* synthetic */ Object m2506b(NodeList nodeList) {
        return m2505a(nodeList);
    }

    public LocTansServerHandler(C0086b c0086b, Proxy proxy, String str, String str2) {
        super(c0086b, proxy, str, str2);
        this.f2354i = c0086b;
    }

    protected String[] m2508f() {
        String[] strArr = new String[3];
        strArr[0] = "&enc=utf-8";
        strArr[1] = "&x1=" + String.format("%f", new Object[]{Double.valueOf(((C0086b) this.b).f302a)});
        strArr[2] = "&y1=" + String.format("%f", new Object[]{Double.valueOf(((C0086b) this.b).f303b)});
        return strArr;
    }

    protected String m2510h() {
        if (m2507e()) {
            return MapServerUrl.m503a().m509d() + "?config=RGC&resType=xml&flag=true";
        }
        return MapServerUrl.m503a().m509d();
    }

    protected C0086b m2505a(NodeList nodeList) {
        C0086b c0086b = this.f2354i;
        int length = nodeList.getLength();
        for (int i = 0; i < length; i++) {
            m2504a(nodeList.item(i), c0086b);
        }
        return c0086b;
    }

    private C0086b m2504a(Node node, C0086b c0086b) {
        if (node.getNodeType() == (short) 1 && node.getNodeName().equals("Item")) {
            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node item = childNodes.item(i);
                if (item.getNodeName().equals(GroupChatInvitation.ELEMENT_NAME)) {
                    c0086b.f302a = Double.parseDouble(m1875a(item));
                } else if (item.getNodeName().equals("y")) {
                    c0086b.f303b = Double.parseDouble(m1875a(item));
                }
            }
        }
        return c0086b;
    }

    protected boolean m2507e() {
        return true;
    }

    protected byte[] m2509g() {
        return null;
    }
}

package com.amap.mapapi.route;

import com.ifoer.entity.Constant;
import java.net.Proxy;
import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.amap.mapapi.route.g */
class WalkHandler extends DriveWalkHandler {
    public WalkHandler(RouteParam routeParam, Proxy proxy, String str, String str2) {
        super(routeParam, proxy, str, str2);
    }

    protected void m4951a(ArrayList<Route> arrayList) {
    }

    protected void m4952a(Node node, ArrayList<Route> arrayList) {
    }

    protected Segment m4953b(Node node) {
        NodeList childNodes = node.getChildNodes();
        Segment driveWalkSegment = new DriveWalkSegment();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            String nodeName = item.getNodeName();
            if (nodeName.equals("roadName")) {
                driveWalkSegment.setRoadName(m1875a(item));
            } else if (nodeName.equals("roadLength")) {
                driveWalkSegment.setLength(Integer.parseInt(m1875a(item)));
            } else if (nodeName.equals(Constant.ACTION)) {
                driveWalkSegment.setActionDescription(m4950a(m1875a(item)));
            } else if (nodeName.equals("coor")) {
                driveWalkSegment.setShapes(m2670a(m1875a(item).split(",")));
            }
        }
        return driveWalkSegment;
    }

    private String m4950a(String str) {
        if (str == null || !str.endsWith("\u884c\u9a76")) {
            return str;
        }
        char[] toCharArray = str.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        while (i < toCharArray.length) {
            if (!("\u5411".equals(XmlPullParser.NO_NAMESPACE + toCharArray[i]) || "\u884c".equals(XmlPullParser.NO_NAMESPACE + toCharArray[i]) || "\u9a76".equals(XmlPullParser.NO_NAMESPACE + toCharArray[i]))) {
                stringBuffer.append(toCharArray[i]);
            }
            i++;
        }
        return stringBuffer.toString();
    }

    protected boolean m4954e() {
        return false;
    }

    protected byte[] m4955g() {
        return null;
    }
}

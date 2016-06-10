package com.amap.mapapi.route;

import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.MapServerUrl;
import com.ifoer.entity.Constant;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.amap.mapapi.route.d */
abstract class DriveWalkHandler extends RouteHandler {
    protected /* synthetic */ Object m2708b(InputStream inputStream) throws AMapException {
        return m2710d(inputStream);
    }

    public DriveWalkHandler(RouteParam routeParam, Proxy proxy, String str, String str2) {
        super(routeParam, proxy, str, str2);
    }

    protected Segment m2707b(Node node) {
        NodeList childNodes = node.getChildNodes();
        Segment driveWalkSegment = new DriveWalkSegment();
        int length = childNodes.getLength();
        String str = XmlPullParser.NO_NAMESPACE;
        for (int i = 0; i < length; i++) {
            Node item = childNodes.item(i);
            String nodeName = item.getNodeName();
            if (nodeName.equals("roadName")) {
                driveWalkSegment.setRoadName(m1875a(item));
            } else if (nodeName.equals("roadLength")) {
                try {
                    driveWalkSegment.setLength(Integer.parseInt(m1875a(item)));
                } catch (NumberFormatException e) {
                    e.getStackTrace();
                }
            } else if (nodeName.equals(Constant.ACTION)) {
                driveWalkSegment.setActionDescription(m1875a(item));
            } else if (nodeName.equals("coor")) {
                driveWalkSegment.setShapes(m2670a(m1875a(item).split(",")));
            } else if (nodeName.equals("driveTime")) {
                driveWalkSegment.setConsumeTime(m1875a(item));
            }
        }
        return driveWalkSegment;
    }

    protected void m2706a(Route route) {
        DriveWalkSegment driveWalkSegment;
        for (int stepCount = route.getStepCount() - 1; stepCount > 0; stepCount--) {
            driveWalkSegment = (DriveWalkSegment) route.getStep(stepCount);
            DriveWalkSegment driveWalkSegment2 = (DriveWalkSegment) route.getStep(stepCount - 1);
            driveWalkSegment.setActionCode(driveWalkSegment2.getActionCode());
            driveWalkSegment.setActionDescription(driveWalkSegment2.getActionDescription());
        }
        driveWalkSegment = (DriveWalkSegment) route.getStep(0);
        driveWalkSegment.setActionCode(-1);
        driveWalkSegment.setActionDescription(XmlPullParser.NO_NAMESPACE);
    }

    protected String[] m2711f() {
        return new String[]{"&x1=" + ((RouteParam) this.b).m894a(), "&y1=" + ((RouteParam) this.b).m897c(), "&x2=" + ((RouteParam) this.b).m896b(), "&y2=" + ((RouteParam) this.b).m898d(), "&routeType=" + (((RouteParam) this.b).f746b % 10)};
    }

    protected String m2712h() {
        if (m526e()) {
            return MapServerUrl.m503a().m509d() + "?highLight=false&enc=utf-8&ver=2.0&config=R&resType=xml";
        }
        return MapServerUrl.m503a().m509d();
    }

    protected ArrayList<Route> m2710d(InputStream inputStream) throws AMapException {
        NodeList a = m1876a(inputStream);
        ArrayList<Route> arrayList = new ArrayList();
        for (int i = 0; i < a.getLength(); i++) {
            Node item = a.item(i);
            String nodeName = item.getNodeName();
            if (nodeName.equals("count")) {
                Integer.parseInt(m1875a(item));
            } else if (nodeName.equals("segmengList")) {
                try {
                    Route c = m2709c(item);
                    if (c != null) {
                        c.setStartPlace(this.i);
                        c.setTargetPlace(this.j);
                        arrayList.add(c);
                    }
                } catch (Exception e) {
                }
            }
        }
        if (arrayList.size() == 0) {
            throw new AMapException(AMapException.ERROR_IO);
        }
        m2512a((ArrayList) arrayList);
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e2) {
                throw new AMapException(AMapException.ERROR_IO);
            }
        }
        return arrayList;
    }

    protected Route m2709c(Node node) {
        Route route;
        List linkedList = new LinkedList();
        if (node.getNodeName().equals("segmengList")) {
            NodeList childNodes = node.getChildNodes();
            childNodes.getLength();
            Route route2 = new Route(((RouteParam) this.b).f746b);
            for (int i = 0; i < childNodes.getLength(); i++) {
                Segment b = m2707b(childNodes.item(i));
                if (b.getShapes().length != 0) {
                    linkedList.add(b);
                }
            }
            if (linkedList.size() == 0) {
                return null;
            }
            route2.m885a(linkedList);
            m2706a(route2);
            for (Segment route3 : route2.m884a()) {
                route3.setRoute(route2);
            }
            route = route2;
        } else {
            route = null;
        }
        return route;
    }
}

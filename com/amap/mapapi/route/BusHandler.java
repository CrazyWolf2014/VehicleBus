package com.amap.mapapi.route;

import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.ClientInfoUtil;
import com.amap.mapapi.core.CoreUtil;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.MapServerUrl;
import com.amap.mapapi.map.ByteUtil;
import com.amap.p004a.BusRouteProtoBuf.BusRouteProtoBuf.BusRouteProtoBuf;
import com.amap.p004a.CommonProtoBuf.CommonProtoBuf.CommonProtoBuf;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.amap.mapapi.route.a */
class BusHandler extends RouteHandler {
    protected /* synthetic */ Object m2699b(InputStream inputStream) throws AMapException {
        return m2701d(inputStream);
    }

    public BusHandler(RouteParam routeParam, Proxy proxy, String str, String str2) {
        super(routeParam, proxy, str, str2);
    }

    private List<Segment> m2691a(LinkedList<Segment> linkedList) {
        int size = linkedList.size();
        Segment[] segmentArr = new Segment[(size - 1)];
        for (int i = 0; i <= size - 2; i++) {
            segmentArr[i] = m2690a(((Segment) linkedList.get(i)).getLastPoint(), ((Segment) linkedList.get(i + 1)).getFirstPoint());
        }
        for (int i2 = 0; i2 <= size - 2; i2++) {
            linkedList.add((i2 * 2) + 1, segmentArr[i2]);
        }
        linkedList.addFirst(m2690a(this.k, ((Segment) linkedList.getFirst()).getFirstPoint()));
        linkedList.addLast(m2690a(((Segment) linkedList.getLast()).getLastPoint(), this.l));
        return linkedList;
    }

    private Segment m2690a(GeoPoint geoPoint, GeoPoint geoPoint2) {
        Segment segment = new Segment();
        r1 = new GeoPoint[2];
        segment.setShapes(r1);
        r1[0] = geoPoint;
        r1[1] = geoPoint2;
        int latitudeE6 = geoPoint2.getLatitudeE6() - geoPoint.getLatitudeE6();
        int longitudeE6 = geoPoint2.getLongitudeE6() - geoPoint.getLongitudeE6();
        segment.setLength(CoreUtil.m482a((int) Math.sqrt((double) ((latitudeE6 * latitudeE6) + (longitudeE6 * longitudeE6)))));
        if (segment.getLength() == 0) {
            segment.setLength(10);
        }
        return segment;
    }

    private void m2692a(BusSegment busSegment, String str) {
        busSegment.setLineName(str);
        busSegment.setFirstStationName(XmlPullParser.NO_NAMESPACE);
        busSegment.setLastStationName(XmlPullParser.NO_NAMESPACE);
        int indexOf = str.indexOf("(");
        int lastIndexOf = str.lastIndexOf(")");
        if (indexOf > 0 && lastIndexOf > 0 && lastIndexOf > indexOf) {
            busSegment.setLineName(str.substring(0, indexOf));
            try {
                String[] split = str.substring(indexOf + 1, lastIndexOf).split("--");
                busSegment.setFirstStationName(split[0]);
                busSegment.setLastStationName(split[1]);
            } catch (Exception e) {
            }
        }
    }

    private void m2694a(String[] strArr, String[] strArr2) {
        for (int i = 0; i < strArr2.length; i++) {
            strArr[i + 1] = strArr2[i];
        }
    }

    private void m2693a(GeoPoint[] geoPointArr, String[] strArr) {
        int i = 0;
        int i2 = 1;
        while (i < strArr.length) {
            geoPointArr[i2] = new GeoPoint((int) (Double.parseDouble(strArr[i + 1]) * 1000000.0d), (int) (Double.parseDouble(strArr[i]) * 1000000.0d));
            i += 2;
            i2++;
        }
    }

    protected Segment m2698b(Node node) {
        BusSegment busSegment = new BusSegment();
        String str = XmlPullParser.NO_NAMESPACE;
        String str2 = XmlPullParser.NO_NAMESPACE;
        NodeList childNodes = node.getChildNodes();
        String[] strArr = null;
        String[] strArr2 = null;
        String[] strArr3 = null;
        GeoPoint[] geoPointArr = null;
        String[] strArr4 = null;
        int i = 0;
        String str3 = str2;
        str2 = str;
        for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
            Node item = childNodes.item(i2);
            String nodeName = item.getNodeName();
            if (nodeName.equals("startName")) {
                str2 = m1875a(item);
            } else if (nodeName.equals("endName")) {
                str3 = m1875a(item);
            } else if (nodeName.equals("busName")) {
                m2692a(busSegment, m1875a(item));
            } else if (nodeName.equals("driverLength")) {
                busSegment.setLength(Integer.parseInt(m1875a(item)));
            } else if (nodeName.equals("passDepotCount")) {
                i = Integer.parseInt(m1875a(item));
                strArr4 = new String[(i + 2)];
                geoPointArr = new GeoPoint[(i + 2)];
            } else if (nodeName.equals("passDepotName")) {
                strArr3 = m1875a(item).split(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            } else if (nodeName.equals("passDepotCoordinate")) {
                strArr2 = m1875a(item).split(",");
            } else if (nodeName.equals("coordinateList")) {
                strArr = m1875a(item).split(",");
            }
        }
        m2694a(strArr4, strArr3);
        m2693a(geoPointArr, strArr2);
        busSegment.setShapes(m2670a(strArr));
        geoPointArr[0] = busSegment.getFirstPoint().m468e();
        geoPointArr[i + 1] = busSegment.getLastPoint().m468e();
        strArr4[0] = str2;
        strArr4[i + 1] = str3;
        busSegment.setPassStopPos(geoPointArr);
        busSegment.setPassStopName(strArr4);
        return busSegment;
    }

    protected void m2695a(Route route) {
        route.m885a(m2691a((LinkedList) route.m884a()));
    }

    protected String[] m2703f() {
        String[] strArr = new String[8];
        strArr[0] = "&enc=utf-8";
        try {
            strArr[1] = "&cityCode=" + URLEncoder.encode(((RouteParam) this.b).f747c, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        strArr[2] = "&x1=" + ((RouteParam) this.b).m894a();
        strArr[3] = "&y1=" + ((RouteParam) this.b).m897c();
        strArr[4] = "&x2=" + ((RouteParam) this.b).m896b();
        strArr[5] = "&y2=" + ((RouteParam) this.b).m898d();
        strArr[6] = "&routeType=" + ((RouteParam) this.b).f746b;
        strArr[7] = "&ver=2.0";
        return strArr;
    }

    protected String m2705h() {
        if (m2702e()) {
            return MapServerUrl.m503a().m509d() + "?&config=BR";
        }
        return MapServerUrl.m503a().m509d();
    }

    protected void m2696a(ArrayList<Route> arrayList) {
    }

    protected void m2697a(Node node, ArrayList<Route> arrayList) {
    }

    protected Route m2700c(Node node) {
        List linkedList = new LinkedList();
        NodeList childNodes = node.getChildNodes();
        Route route = null;
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeName().equals("segmentList")) {
                NodeList childNodes2 = item.getChildNodes();
                childNodes2.getLength();
                Route route2 = new Route(((RouteParam) this.b).f746b);
                for (int i2 = 0; i2 < childNodes2.getLength(); i2++) {
                    Segment b = m2698b(childNodes2.item(i2));
                    if (b.getShapes().length != 0) {
                        linkedList.add(b);
                    }
                }
                if (linkedList.size() == 0) {
                    return null;
                }
                route2.m885a(linkedList);
                m2695a(route2);
                for (Segment route3 : route2.m884a()) {
                    route3.setRoute(route2);
                }
                route = route2;
            }
        }
        return route;
    }

    protected boolean m2702e() {
        return false;
    }

    protected byte[] m2704g() {
        BusRouteProtoBuf v = com.amap.p004a.BusRouteProtoBuf.BusRouteProtoBuf.m3164v();
        CommonProtoBuf l = com.amap.p004a.CommonProtoBuf.CommonProtoBuf.m3409l();
        l.m3393b(ClientInfoUtil.m471a(null).m472a());
        l.m3391a("BR");
        l.m3396d("GBK");
        l.m3394c("buf");
        v.m3119a(l);
        v.m3126b(((RouteParam) this.b).m894a() + XmlPullParser.NO_NAMESPACE);
        v.m3127c(((RouteParam) this.b).m897c() + XmlPullParser.NO_NAMESPACE);
        v.m3129d(((RouteParam) this.b).m896b() + XmlPullParser.NO_NAMESPACE);
        v.m3131e(((RouteParam) this.b).m898d() + XmlPullParser.NO_NAMESPACE);
        v.m3137h("2.0");
        v.m3123a(((RouteParam) this.b).f747c);
        v.m3135g(((RouteParam) this.b).f746b + XmlPullParser.NO_NAMESPACE);
        v.m3133f("50");
        byte[] a = ByteUtil.m837a(1815);
        byte[] toByteArray = v.m3130d().toByteArray();
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(toByteArray.length + a.length);
        byteArrayBuffer.append(a, 0, a.length);
        byteArrayBuffer.append(toByteArray, 0, toByteArray.length);
        return byteArrayBuffer.toByteArray();
    }

    protected ArrayList<Route> m2701d(InputStream inputStream) throws AMapException {
        if (m2702e()) {
            return super.m2673d(inputStream);
        }
        ArrayList<Route> arrayList = new ArrayList();
        if (inputStream == null) {
            return null;
        }
        try {
            int i;
            byte[] a = ByteUtil.m838a(inputStream);
            byte[] bArr = new byte[4];
            for (i = 0; i < 4; i++) {
                bArr[i] = a[i];
            }
            ByteUtil.m836a(bArr);
            int b = ByteUtil.m839b(bArr) + 4;
            byte[] bArr2 = new byte[(a.length - b)];
            for (i = 0; i < a.length - b; i++) {
                bArr2[i] = a[b + i];
            }
            List d = com.amap.p004a.BusRouteProtoBuf.BusRouteProtoBuf.m3221a(bArr2).m3244m().m3306d();
            int size = d.size();
            for (b = 0; b < size; b++) {
                com.amap.p004a.BusRouteProtoBuf.BusRouteProtoBuf busRouteProtoBuf = (com.amap.p004a.BusRouteProtoBuf.BusRouteProtoBuf) d.get(b);
                Route route = new Route(((RouteParam) this.b).f746b);
                List d2 = busRouteProtoBuf.m3063d();
                int size2 = d2.size();
                List linkedList = new LinkedList();
                for (int i2 = 0; i2 < size2; i2++) {
                    BusSegment busSegment = new BusSegment();
                    com.amap.p004a.BusRouteProtoBuf.BusRouteProtoBuf busRouteProtoBuf2 = (com.amap.p004a.BusRouteProtoBuf.BusRouteProtoBuf) d2.get(i2);
                    String str = XmlPullParser.NO_NAMESPACE;
                    str = XmlPullParser.NO_NAMESPACE;
                    str = busRouteProtoBuf2.m3365e();
                    String g = busRouteProtoBuf2.m3367g();
                    m2692a(busSegment, busRouteProtoBuf2.m3369i());
                    busSegment.setLength(Integer.parseInt(busRouteProtoBuf2.m3373m()));
                    int parseInt = Integer.parseInt(busRouteProtoBuf2.m3377q());
                    String[] strArr = new String[(parseInt + 2)];
                    GeoPoint[] geoPointArr = new GeoPoint[(parseInt + 2)];
                    String[] split = busRouteProtoBuf2.m3371k().split(",");
                    String[] split2 = busRouteProtoBuf2.m3381u().split(",");
                    String[] split3 = busRouteProtoBuf2.m3379s().split(",");
                    m2694a(strArr, split);
                    m2693a(geoPointArr, split2);
                    busSegment.setShapes(m2670a(split3));
                    geoPointArr[0] = busSegment.getFirstPoint().m468e();
                    geoPointArr[parseInt + 1] = busSegment.getLastPoint().m468e();
                    strArr[0] = str;
                    strArr[parseInt + 1] = g;
                    busSegment.setPassStopPos(geoPointArr);
                    busSegment.setPassStopName(strArr);
                    linkedList.add(busSegment);
                }
                route.m885a(linkedList);
                m2695a(route);
                for (Segment route2 : route.m884a()) {
                    route2.setRoute(route);
                }
                if (route != null) {
                    route.setStartPlace(this.i);
                    route.setTargetPlace(this.j);
                    arrayList.add(route);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}

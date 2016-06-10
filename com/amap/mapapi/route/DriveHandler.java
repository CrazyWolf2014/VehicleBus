package com.amap.mapapi.route;

import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.ClientInfoUtil;
import com.amap.mapapi.map.ByteUtil;
import com.amap.p004a.CommonProtoBuf.CommonProtoBuf.CommonProtoBuf;
import com.amap.p004a.RouteProtoBuf.RouteProtoBuf.RouteProtoBuf;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;
import org.w3c.dom.Node;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.amap.mapapi.route.c */
class DriveHandler extends DriveWalkHandler {
    protected /* synthetic */ Object m4946b(InputStream inputStream) throws AMapException {
        return m4947d(inputStream);
    }

    public DriveHandler(RouteParam routeParam, Proxy proxy, String str, String str2) {
        super(routeParam, proxy, str, str2);
    }

    protected void m4944a(ArrayList<Route> arrayList) {
    }

    protected void m4945a(Node node, ArrayList<Route> arrayList) {
    }

    protected boolean m4948e() {
        return false;
    }

    protected byte[] m4949g() {
        RouteProtoBuf F = com.amap.p004a.RouteProtoBuf.RouteProtoBuf.m4742F();
        CommonProtoBuf l = com.amap.p004a.CommonProtoBuf.CommonProtoBuf.m3409l();
        l.m3393b(ClientInfoUtil.m471a(null).m472a());
        l.m3391a("R");
        l.m3396d("GBK");
        l.m3394c("buf");
        F.m4713a(l);
        F.m4718a(((RouteParam) this.b).m894a() + XmlPullParser.NO_NAMESPACE);
        F.m4721b(((RouteParam) this.b).m897c() + XmlPullParser.NO_NAMESPACE);
        F.m4722c(((RouteParam) this.b).m896b() + XmlPullParser.NO_NAMESPACE);
        F.m4724d(((RouteParam) this.b).m898d() + XmlPullParser.NO_NAMESPACE);
        F.m4741m("2.0");
        F.m4732h((((RouteParam) this.b).f746b % 10) + XmlPullParser.NO_NAMESPACE);
        F.m4726e("50");
        byte[] a = ByteUtil.m837a(1814);
        byte[] toByteArray = F.m4725d().toByteArray();
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(toByteArray.length + a.length);
        byteArrayBuffer.append(a, 0, a.length);
        byteArrayBuffer.append(toByteArray, 0, toByteArray.length);
        return byteArrayBuffer.toByteArray();
    }

    protected ArrayList<Route> m4947d(InputStream inputStream) throws AMapException {
        if (m4948e()) {
            return super.m2710d(inputStream);
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
            List d = com.amap.p004a.RouteProtoBuf.RouteProtoBuf.m4851a(bArr2).m4878o().m4941d();
            int size = d.size();
            Route route = new Route(((RouteParam) this.b).f746b);
            List linkedList = new LinkedList();
            for (b = 0; b < size; b++) {
                com.amap.p004a.RouteProtoBuf.RouteProtoBuf routeProtoBuf = (com.amap.p004a.RouteProtoBuf.RouteProtoBuf) d.get(b);
                DriveWalkSegment driveWalkSegment = new DriveWalkSegment();
                String i2 = routeProtoBuf.m4564i();
                if (i2.contains("\u5343\u7c73")) {
                    i2 = ((int) (Double.parseDouble(i2.substring(0, i2.length() - 2)) * 1000.0d)) + XmlPullParser.NO_NAMESPACE;
                } else if (i2.contains("\u7c73")) {
                    i2 = i2.substring(0, i2.length() - 1);
                } else if (i2.contains("\u516c\u91cc")) {
                    i2 = ((int) (Double.parseDouble(i2.substring(0, i2.length() - 2)) * 1000.0d)) + XmlPullParser.NO_NAMESPACE;
                }
                driveWalkSegment.setLength(Integer.parseInt(i2));
                driveWalkSegment.setRoadName(routeProtoBuf.m4560e());
                driveWalkSegment.setActionDescription(routeProtoBuf.m4566k());
                driveWalkSegment.setShapes(m2670a(routeProtoBuf.m4578w().split(",")));
                driveWalkSegment.setConsumeTime(routeProtoBuf.m4570o());
                if (driveWalkSegment.getShapes().length != 0) {
                    linkedList.add(driveWalkSegment);
                }
            }
            if (linkedList.size() == 0) {
                return null;
            }
            route.m885a(linkedList);
            m2706a(route);
            for (Segment route2 : route.m884a()) {
                route2.setRoute(route);
            }
            if (route != null) {
                route.setStartPlace(this.i);
                route.setTargetPlace(this.j);
            }
            arrayList.add(route);
            if (arrayList.size() == 0) {
                throw new AMapException(AMapException.ERROR_IO);
            }
            m4944a(arrayList);
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new AMapException(AMapException.ERROR_IO);
                }
            }
            return arrayList;
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}

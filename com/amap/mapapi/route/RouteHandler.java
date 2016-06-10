package com.amap.mapapi.route;

import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.XmlListResultHandler;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* renamed from: com.amap.mapapi.route.e */
public abstract class RouteHandler extends XmlListResultHandler<RouteParam, Route> {
    protected String f2414i;
    protected String f2415j;
    protected GeoPoint f2416k;
    protected GeoPoint f2417l;

    protected abstract Route m2672c(Node node);

    protected /* synthetic */ Object m2671b(InputStream inputStream) throws AMapException {
        return m2673d(inputStream);
    }

    public RouteHandler(RouteParam routeParam, Proxy proxy, String str, String str2) {
        super(routeParam, proxy, str, str2);
        this.f2414i = "\u8d77\u70b9";
        this.f2415j = "\u7ec8\u70b9";
        this.f2416k = ((RouteParam) this.b).f745a.mFrom;
        this.f2417l = ((RouteParam) this.b).f745a.mTo;
    }

    protected ArrayList<Route> m2673d(InputStream inputStream) throws AMapException {
        NodeList a = m1876a(inputStream);
        ArrayList<Route> arrayList = new ArrayList();
        int length = a.getLength();
        for (int i = 0; i < length; i++) {
            Node item = a.item(i);
            String nodeName = item.getNodeName();
            if (nodeName.equals("count")) {
                Integer.parseInt(m1875a(item));
            } else if (nodeName.equals("busList")) {
                NodeList childNodes = item.getChildNodes();
                for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
                    try {
                        Route c = m2672c(childNodes.item(i2));
                        if (c != null) {
                            c.setStartPlace(this.f2414i);
                            c.setTargetPlace(this.f2415j);
                            arrayList.add(c);
                        }
                    } catch (Exception e) {
                    }
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

    protected GeoPoint[] m2670a(String[] strArr) {
        int i = 0;
        GeoPoint[] geoPointArr = new GeoPoint[(strArr.length / 2)];
        int length = strArr.length;
        int i2 = 0;
        while (i2 < length) {
            geoPointArr[i] = new GeoPoint((long) (Double.parseDouble(strArr[i2 + 1]) * 1000000.0d), (long) (Double.parseDouble(strArr[i2]) * 1000000.0d));
            i2 += 2;
            i++;
        }
        return geoPointArr;
    }
}

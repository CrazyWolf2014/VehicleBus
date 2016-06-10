package com.amap.mapapi.poisearch;

import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.ClientInfoUtil;
import com.amap.mapapi.core.CoreUtil;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.MapServerUrl;
import com.amap.mapapi.core.PoiItem;
import com.amap.mapapi.core.XmlListResultHandler;
import com.amap.mapapi.core.XmlObject;
import com.amap.mapapi.geocoder.Geocoder;
import com.amap.mapapi.map.ByteUtil;
import com.amap.mapapi.poisearch.PoiSearch.Query;
import com.amap.mapapi.poisearch.PoiSearch.SearchBound;
import com.amap.p004a.CommonProtoBuf.CommonProtoBuf;
import com.amap.p004a.LocationSearchProtoBuf.LocationSearchProtoBuf;
import com.ifoer.mine.Contact;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.bytestreams.ibb.packet.DataPacketExtension;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.amap.mapapi.poisearch.a */
public class PoiSearchServerHandler extends XmlListResultHandler<QueryInternal, PoiItem> {
    private int f2410i;
    private int f2411j;
    private int f2412k;
    private ArrayList<String> f2413l;

    /* renamed from: com.amap.mapapi.poisearch.a.a */
    static class PoiSearchServerHandler {
        String f721a;
        String f722b;
        String f723c;
        String f724d;
        String f725e;
        String f726f;
        String f727g;
        double f728h;
        double f729i;
        String f730j;

        PoiSearchServerHandler() {
        }

        public PoiItem m866a() {
            PoiItem poiItem = new PoiItem(this.f721a, new GeoPoint(CoreUtil.m484a(this.f729i), CoreUtil.m484a(this.f728h)), this.f722b, this.f725e);
            poiItem.setAdCode(this.f727g);
            poiItem.setTel(this.f726f);
            poiItem.setTypeCode(this.f723c);
            poiItem.setTypeDes(this.f724d);
            poiItem.setXmlNode(this.f730j);
            return poiItem;
        }
    }

    public /* synthetic */ Object m2659b(InputStream inputStream) throws AMapException {
        return m2663d(inputStream);
    }

    public PoiSearchServerHandler(QueryInternal queryInternal, Proxy proxy, String str, String str2) {
        super(queryInternal, proxy, str, str2);
        this.f2410i = 1;
        this.f2411j = 20;
        this.f2412k = 0;
        this.f2413l = new ArrayList();
    }

    public void m2655a(int i) {
        this.f2410i = i;
    }

    public void m2660b(int i) {
        int i2;
        int i3 = 20;
        if (i > 20) {
            i2 = 20;
        } else {
            i2 = i;
        }
        if (i2 > 0) {
            i3 = i2;
        }
        this.f2411j = i3;
    }

    public int m2653a() {
        return this.f2411j;
    }

    public int m2658b() {
        return this.f2410i;
    }

    public int m2661c() {
        return this.f2412k;
    }

    public Query m2662d() {
        return ((QueryInternal) this.b).f731a;
    }

    public SearchBound m2668l() {
        return ((QueryInternal) this.b).f732b;
    }

    public List<String> m2669m() {
        return this.f2413l;
    }

    protected String[] m2665f() {
        String[] strArr;
        String city;
        if (((QueryInternal) this.b).f732b == null) {
            strArr = new String[6];
            city = ((QueryInternal) this.b).f731a.getCity();
            if (m2650a(city)) {
                strArr[0] = "&cityCode=total";
            } else {
                try {
                    strArr[0] = "&cityCode=" + URLEncoder.encode(city, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            city = ((QueryInternal) this.b).f731a.getQueryString();
            try {
                city = URLEncoder.encode(city, "utf-8");
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            }
            strArr[1] = "&searchName=" + city;
            city = ((QueryInternal) this.b).f731a.getCategory();
            try {
                city = URLEncoder.encode(city, "utf-8");
            } catch (UnsupportedEncodingException e22) {
                e22.printStackTrace();
            }
            strArr[2] = "&searchType=" + city;
            strArr[3] = "&number=" + (XmlPullParser.NO_NAMESPACE + this.f2411j);
            strArr[4] = "&batch=" + (XmlPullParser.NO_NAMESPACE + this.f2410i);
            strArr[5] = "&enc=utf-8";
            return strArr;
        }
        SearchBound searchBound = ((QueryInternal) this.b).f732b;
        if (SearchBound.BOUND_SHAPE.equals(searchBound.getShape())) {
            strArr = new String[9];
            strArr[0] = "&cityCode=total";
            city = ((QueryInternal) this.b).f731a.getQueryString();
            try {
                city = URLEncoder.encode(city, "utf-8");
            } catch (UnsupportedEncodingException e222) {
                e222.printStackTrace();
            }
            strArr[1] = "&searchName=" + city;
            city = ((QueryInternal) this.b).f731a.getCategory();
            try {
                city = URLEncoder.encode(city, "utf-8");
            } catch (UnsupportedEncodingException e2222) {
                e2222.printStackTrace();
            }
            strArr[2] = "&searchType=" + city;
            strArr[3] = "&cenX=" + (XmlPullParser.NO_NAMESPACE + ((((float) ((QueryInternal) this.b).f732b.getCenter().m462a()) * 1.0f) / 1000000.0f));
            strArr[4] = "&cenY=" + (XmlPullParser.NO_NAMESPACE + ((((float) ((QueryInternal) this.b).f732b.getCenter().m464b()) * 1.0f) / 1000000.0f));
            strArr[5] = "&number=" + (XmlPullParser.NO_NAMESPACE + this.f2411j);
            strArr[6] = "&batch=" + (XmlPullParser.NO_NAMESPACE + this.f2410i);
            strArr[7] = "&enc=utf-8";
            strArr[8] = "&range=" + ((QueryInternal) this.b).f732b.getRange();
            return strArr;
        } else if (!SearchBound.RECTANGLE_SHAPE.equals(searchBound.getShape())) {
            return null;
        } else {
            Object queryString = ((QueryInternal) this.b).f731a.getQueryString();
            Object category = ((QueryInternal) this.b).f731a.getCategory();
            GeoPoint lowerLeft = searchBound.getLowerLeft();
            GeoPoint upperRight = searchBound.getUpperRight();
            double a = CoreUtil.m481a(lowerLeft.m464b());
            double a2 = CoreUtil.m481a(lowerLeft.m462a());
            double a3 = CoreUtil.m481a(upperRight.m464b());
            double a4 = CoreUtil.m481a(upperRight.m462a());
            Object valueOf = String.valueOf(this.f2410i);
            Object valueOf2 = String.valueOf(this.f2411j);
            XmlObject xmlObject = new XmlObject("spatial_request");
            xmlObject.m553a("method", (Object) "searchPoiInGeoObject");
            XmlObject xmlObject2 = new XmlObject("searchName");
            xmlObject2.m552a(queryString);
            xmlObject.m551a(xmlObject2);
            XmlObject xmlObject3 = new XmlObject("searchType");
            xmlObject3.m552a(category);
            xmlObject.m551a(xmlObject3);
            XmlObject xmlObject4 = new XmlObject("pageNum");
            xmlObject4.m552a(valueOf2);
            xmlObject.m551a(xmlObject4);
            xmlObject4 = new XmlObject("batch");
            xmlObject4.m552a(valueOf);
            xmlObject.m551a(xmlObject4);
            xmlObject4 = new XmlObject("spatial_geos");
            xmlObject3 = new XmlObject("spatial_geo");
            xmlObject3.m553a(SharedPref.TYPE, searchBound.getShape());
            XmlObject xmlObject5 = new XmlObject("bounds");
            xmlObject5.m552a(a2 + ";" + a + ";" + a4 + ";" + a3);
            xmlObject3.m551a(xmlObject5);
            xmlObject5 = new XmlObject("buffer");
            xmlObject5.m552a(Integer.valueOf(0));
            xmlObject3.m551a(xmlObject5);
            xmlObject4.m551a(xmlObject3);
            xmlObject.m551a(xmlObject4);
            city = xmlObject.m549a();
            try {
                city = URLEncoder.encode(city, "utf-8");
            } catch (UnsupportedEncodingException e3) {
                e3.printStackTrace();
            }
            strArr = new String[4];
            strArr[0] = "&enc=utf-8";
            strArr[1] = "&spatialXml=" + city;
            strArr[2] = "&enc=utf-8";
            return strArr;
        }
    }

    private boolean m2650a(String str) {
        if (str == null || str.equals(XmlPullParser.NO_NAMESPACE)) {
            return true;
        }
        return false;
    }

    protected NodeList m2654a(InputStream inputStream) throws AMapException {
        if (((QueryInternal) this.b).f732b == null || !((QueryInternal) this.b).f732b.getShape().equals(SearchBound.RECTANGLE_SHAPE)) {
            return super.m1876a(inputStream);
        }
        try {
            Element documentElement = CoreUtil.m492b(m525c(inputStream)).getDocumentElement();
            if (documentElement.getNodeName().equals("searchresult")) {
                return documentElement.getChildNodes();
            }
            return documentElement.getFirstChild().getChildNodes();
        } catch (Exception e) {
            throw new AMapException(e.getMessage());
        }
    }

    protected void m2656a(ArrayList<PoiItem> arrayList) {
    }

    protected void m2657a(Node node, ArrayList<PoiItem> arrayList) {
        if (node.getNodeType() == (short) 1) {
            int b = m2658b();
            String nodeName = node.getNodeName();
            if (nodeName.equals("count") && b == 1) {
                m2651b(node);
            }
            if (nodeName.equals("pinyin") && b == 1) {
                m2652c(node);
            }
            if (nodeName.equals("list")) {
                NodeList childNodes = node.getChildNodes();
                for (b = 0; b < childNodes.getLength(); b++) {
                    Node item = childNodes.item(b);
                    if (item.getNodeType() == (short) 1 && item.getNodeName().equals("poi")) {
                        PoiSearchServerHandler poiSearchServerHandler = new PoiSearchServerHandler();
                        m2649a(item, poiSearchServerHandler);
                        arrayList.add(poiSearchServerHandler.m866a());
                    }
                }
            }
        }
    }

    private void m2651b(Node node) {
        this.f2412k = Integer.parseInt(m1875a(node));
    }

    private void m2652c(Node node) {
        NodeList childNodes = node.getChildNodes();
        int length = childNodes.getLength();
        for (int i = 0; i < length; i++) {
            Node item = childNodes.item(i);
            if (item.getNodeType() == (short) 1 && item.getNodeName().equals("list")) {
                NodeList childNodes2 = item.getChildNodes();
                int length2 = childNodes2.getLength();
                for (int i2 = 0; i2 < length2; i2++) {
                    Node item2 = childNodes2.item(i2);
                    if (item2.getNodeType() == (short) 1 && item2.getNodeName().equals(DataPacketExtension.ELEMENT_NAME)) {
                        this.f2413l.add(m1875a(item2));
                    }
                }
            }
        }
    }

    private void m2649a(Node node, PoiSearchServerHandler poiSearchServerHandler) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeType() == (short) 1) {
                try {
                    m2648a(poiSearchServerHandler, item.getNodeName(), m1875a(item));
                } catch (Exception e) {
                }
            }
        }
    }

    private void m2648a(PoiSearchServerHandler poiSearchServerHandler, String str, String str2) {
        if (str.equals("name")) {
            poiSearchServerHandler.f722b = str2;
        } else if (str.equals("pguid")) {
            poiSearchServerHandler.f721a = str2;
        } else if (str.equals("newtype")) {
            poiSearchServerHandler.f723c = str2.substring(0, 4);
        } else if (str.equals(SharedPref.TYPE)) {
            String[] split = str2.split(";");
            poiSearchServerHandler.f724d = split[0] + PoiItem.DesSplit + split[1];
        } else if (str.equals("address")) {
            poiSearchServerHandler.f725e = str2;
        } else if (str.equals("tel")) {
            poiSearchServerHandler.f726f = str2;
        } else if (str.equals("code")) {
            poiSearchServerHandler.f727g = str2;
        } else if (str.equals(GroupChatInvitation.ELEMENT_NAME)) {
            poiSearchServerHandler.f728h = Double.parseDouble(str2);
        } else if (str.equals("y")) {
            poiSearchServerHandler.f729i = Double.parseDouble(str2);
        } else if (str.equals("xml")) {
            poiSearchServerHandler.f730j = str2;
        }
    }

    protected String m2667h() {
        String str;
        if (((QueryInternal) this.b).f732b == null) {
            str = MapServerUrl.m503a().m509d() + "?&config=BESN&resType=xml";
        } else {
            SearchBound searchBound = ((QueryInternal) this.b).f732b;
            if (searchBound.getShape().equals(SearchBound.BOUND_SHAPE)) {
                str = MapServerUrl.m503a().m509d() + "?&config=BELSBXY&resType=xml";
            } else if (searchBound.getShape().equals(SearchBound.RECTANGLE_SHAPE)) {
                str = MapServerUrl.m503a().m509d() + "?&config=SPAS&resType=xml";
            } else {
                str = null;
            }
        }
        return m2664e() ? str : MapServerUrl.m503a().m509d();
    }

    public ArrayList<PoiItem> m2663d(InputStream inputStream) throws AMapException {
        int i = 0;
        if (m2664e()) {
            return super.m2516d(inputStream);
        }
        ArrayList<PoiItem> arrayList = new ArrayList();
        if (inputStream == null) {
            return null;
        }
        try {
            int i2;
            byte[] a = ByteUtil.m838a(inputStream);
            byte[] bArr = new byte[4];
            for (i2 = 0; i2 < 4; i2++) {
                bArr[i2] = a[i2];
            }
            ByteUtil.m836a(bArr);
            i2 = ByteUtil.m839b(bArr) + 4;
            bArr = new byte[(a.length - i2)];
            while (i < a.length - i2) {
                bArr[i] = a[i2 + i];
                i++;
            }
            LocationSearchProtoBuf a2 = LocationSearchProtoBuf.m4010a(bArr);
            this.f2412k = a2.m4029i();
            for (CommonProtoBuf commonProtoBuf : a2.m4039s().m4072d()) {
                PoiSearchServerHandler poiSearchServerHandler = new PoiSearchServerHandler();
                poiSearchServerHandler.f725e = commonProtoBuf.m3534q();
                poiSearchServerHandler.f722b = commonProtoBuf.m3524g();
                poiSearchServerHandler.f721a = commonProtoBuf.m3522e();
                poiSearchServerHandler.f726f = commonProtoBuf.m3510O();
                poiSearchServerHandler.f728h = Double.parseDouble(commonProtoBuf.m3528k());
                poiSearchServerHandler.f729i = Double.parseDouble(commonProtoBuf.m3530m());
                poiSearchServerHandler.f723c = commonProtoBuf.m3542y();
                poiSearchServerHandler.f727g = commonProtoBuf.m3540w();
                arrayList.add(poiSearchServerHandler.m866a());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    protected boolean m2664e() {
        return false;
    }

    protected byte[] m2666g() {
        LocationSearchProtoBuf.LocationSearchProtoBuf D = LocationSearchProtoBuf.m3904D();
        CommonProtoBuf.CommonProtoBuf l = CommonProtoBuf.m3409l();
        l.m3396d("GBK");
        l.m3394c("buf");
        int i = 1800;
        if (((QueryInternal) this.b).f732b == null) {
            l.m3391a("BESN");
            i = 1800;
        } else {
            SearchBound searchBound = ((QueryInternal) this.b).f732b;
            if (searchBound.getShape().equals(SearchBound.BOUND_SHAPE)) {
                l.m3391a("BELSBXY");
                i = 1802;
            } else if (searchBound.getShape().equals(SearchBound.RECTANGLE_SHAPE)) {
                l.m3391a("BELSBN");
                i = 1803;
            }
        }
        l.m3393b(ClientInfoUtil.m471a(null).m472a());
        D.m3879a(l);
        String city;
        if (((QueryInternal) this.b).f732b == null) {
            city = ((QueryInternal) this.b).f731a.getCity();
            if (m2650a(city)) {
                D.m3888c("total");
            } else {
                D.m3888c(city);
            }
            D.m3884a(((QueryInternal) this.b).f731a.getQueryString());
            D.m3890d(((QueryInternal) this.b).f731a.getCategory());
            D.m3902k(XmlPullParser.NO_NAMESPACE + this.f2411j);
            D.m3903l(XmlPullParser.NO_NAMESPACE + this.f2410i);
        } else {
            SearchBound searchBound2 = ((QueryInternal) this.b).f732b;
            if (SearchBound.BOUND_SHAPE.equals(searchBound2.getShape())) {
                D.m3888c("total");
                D.m3884a(((QueryInternal) this.b).f731a.getQueryString());
                D.m3890d(((QueryInternal) this.b).f731a.getCategory());
                D.m3894f(XmlPullParser.NO_NAMESPACE + ((((float) ((QueryInternal) this.b).f732b.getCenter().m462a()) * 1.0f) / 1000000.0f));
                D.m3897g(XmlPullParser.NO_NAMESPACE + ((((float) ((QueryInternal) this.b).f732b.getCenter().m464b()) * 1.0f) / 1000000.0f));
                D.m3902k(XmlPullParser.NO_NAMESPACE + this.f2411j);
                D.m3903l(XmlPullParser.NO_NAMESPACE + this.f2410i);
                D.m3898h(((QueryInternal) this.b).f732b.getRange() + XmlPullParser.NO_NAMESPACE);
            } else if (SearchBound.RECTANGLE_SHAPE.equals(searchBound2.getShape())) {
                String queryString = ((QueryInternal) this.b).f731a.getQueryString();
                city = ((QueryInternal) this.b).f731a.getCategory();
                GeoPoint lowerLeft = searchBound2.getLowerLeft();
                GeoPoint upperRight = searchBound2.getUpperRight();
                double a = CoreUtil.m481a(lowerLeft.m464b());
                double a2 = CoreUtil.m481a(lowerLeft.m462a());
                double a3 = CoreUtil.m481a(upperRight.m464b());
                double a4 = CoreUtil.m481a(upperRight.m462a());
                String valueOf = String.valueOf(this.f2410i);
                String valueOf2 = String.valueOf(this.f2411j);
                D.m3884a(queryString);
                D.m3892e(XmlPullParser.NO_NAMESPACE);
                D.m3894f(((a2 + a4) / 2.0d) + XmlPullParser.NO_NAMESPACE);
                D.m3897g(((a + a3) / 2.0d) + XmlPullParser.NO_NAMESPACE);
                D.m3898h("3000");
                D.m3890d(city);
                D.m3902k(valueOf2);
                D.m3903l(valueOf);
                D.m3887b(Geocoder.POI);
                D.m3901j(Contact.RELATION_FRIEND);
                D.m3900i(Contact.RELATION_FRIEND);
            }
        }
        byte[] a5 = ByteUtil.m837a(i);
        byte[] toByteArray = D.m3891d().toByteArray();
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(toByteArray.length + a5.length);
        byteArrayBuffer.append(a5, 0, a5.length);
        byteArrayBuffer.append(toByteArray, 0, toByteArray.length);
        return byteArrayBuffer.toByteArray();
    }
}

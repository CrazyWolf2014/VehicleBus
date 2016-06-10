package com.amap.mapapi.core;

import android.location.Address;
import com.amap.mapapi.geocoder.Geocoder;
import com.amap.mapapi.map.ByteUtil;
import com.amap.p004a.CommonProtoBuf.CommonProtoBuf;
import com.amap.p004a.ReverseGeocodingProtoBuf.ReverseGeocodingProtoBuf;
import com.ifoer.util.MyHttpException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.amap.mapapi.core.o */
public class ReverseGeocodingHandler extends XmlListResultHandler<ReverseGeocodingParam, Address> {
    private String f2396i;
    private String f2397j;
    private String f2398k;
    private int f2399l;
    private ArrayList<Address> f2400m;
    private ArrayList<Address> f2401n;
    private ArrayList<Address> f2402o;
    private boolean f2403p;
    private boolean f2404q;
    private boolean f2405r;
    private String f2406s;

    protected /* synthetic */ Object m2621b(InputStream inputStream) throws AMapException {
        return m2622d(inputStream);
    }

    public ReverseGeocodingHandler(ReverseGeocodingParam reverseGeocodingParam, Proxy proxy, String str, String str2) {
        super(reverseGeocodingParam, proxy, str, str2);
        this.f2396i = null;
        this.f2397j = null;
        this.f2398k = null;
        this.f2399l = 0;
        this.f2406s = "<?xml version='1.0' encoding='utf-8' ?><spatial_request method='searchPoint'><x>%f</x><y>%f</y><poiNumber>%d</poiNumber><range>%d</range><pattern>0</pattern><roadLevel>0</roadLevel></spatial_request>";
        this.f2399l = reverseGeocodingParam.f371c;
        this.f2400m = new ArrayList();
        this.f2401n = new ArrayList();
        this.f2402o = new ArrayList();
    }

    protected String[] m2624f() {
        String encode;
        String[] strArr = new String[2];
        strArr[0] = "&enc=utf-8";
        String str = XmlPullParser.NO_NAMESPACE;
        try {
            encode = URLEncoder.encode(String.format(this.f2406s, new Object[]{Double.valueOf(((ReverseGeocodingParam) this.b).f369a), Double.valueOf(((ReverseGeocodingParam) this.b).f370b), Integer.valueOf(((ReverseGeocodingParam) this.b).f371c), Integer.valueOf(MyHttpException.ERROR_SERVER)}), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            encode = str;
        }
        strArr[1] = "&spatialXml=" + encode;
        return strArr;
    }

    protected void m2620a(Node node, ArrayList<Address> arrayList) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            m2618b(childNodes.item(i), arrayList);
        }
    }

    private void m2618b(Node node, ArrayList<Address> arrayList) {
        String nodeName = node.getNodeName();
        if (nodeName.equals("Province")) {
            this.f2397j = m2615b(node);
        } else if (nodeName.equals("City")) {
            this.f2396i = m2615b(node);
        } else if (nodeName.equals("District")) {
            this.f2398k = m2615b(node);
        } else if (nodeName.equals("roadList")) {
            m2614a(node, (ArrayList) arrayList, "Road");
        } else if (nodeName.equals("poiList")) {
            m2614a(node, (ArrayList) arrayList, "poi");
        } else if (nodeName.equals("crossPoiList")) {
            m2614a(node, (ArrayList) arrayList, "cross");
            m2616b((ArrayList) arrayList);
        }
    }

    private String m2615b(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeType() == (short) 1 && item.getNodeName().equals("name")) {
                return m1875a(item);
            }
        }
        return null;
    }

    private void m2614a(Node node, ArrayList<Address> arrayList, String str) {
        if (this.f2399l > 0) {
            NodeList childNodes = node.getChildNodes();
            int min = Math.min(this.f2399l, childNodes.getLength());
            for (int i = 0; i < min; i++) {
                Node item = childNodes.item(i);
                if (item.getNodeType() == (short) 1) {
                    String nodeName = item.getNodeName();
                    if (nodeName.equals(str)) {
                        Address a = m2611a(item, nodeName);
                        Method method;
                        if (nodeName.equals("Road")) {
                            try {
                                method = a.getClass().getMethod("setPremises", new Class[]{String.class});
                                if (method != null) {
                                    method.invoke(a, new Object[]{Geocoder.Street_Road});
                                }
                            } catch (Exception e) {
                            }
                            if (a != null) {
                                this.f2400m.add(a);
                                this.f2403p = true;
                            }
                        } else if (nodeName.equals("poi")) {
                            try {
                                method = a.getClass().getMethod("setPremises", new Class[]{String.class});
                                if (method != null) {
                                    method.invoke(a, new Object[]{Geocoder.POI});
                                }
                            } catch (Exception e2) {
                            }
                            if (a != null) {
                                this.f2401n.add(a);
                                this.f2404q = true;
                            }
                        } else if (nodeName.equals("cross")) {
                            try {
                                method = a.getClass().getMethod("setPremises", new Class[]{String.class});
                                if (method != null) {
                                    method.invoke(a, new Object[]{Geocoder.Cross});
                                }
                            } catch (Exception e3) {
                            }
                            if (a != null) {
                                this.f2402o.add(a);
                                this.f2405r = true;
                            }
                        }
                    }
                }
            }
        }
    }

    private ArrayList<Address> m2616b(ArrayList<Address> arrayList) {
        if (this.f2399l > 0) {
            if (this.f2405r) {
                arrayList.add(this.f2402o.get(0));
            }
            m2613a((ArrayList) arrayList, this.f2401n);
            if (this.f2399l - arrayList.size() != 0) {
                m2613a((ArrayList) arrayList, this.f2400m);
            } else if (this.f2403p) {
                arrayList.set(arrayList.size() - 1, this.f2400m.get(0));
            }
        }
        return arrayList;
    }

    private void m2613a(ArrayList<Address> arrayList, ArrayList<Address> arrayList2) {
        int size = arrayList2.size();
        int size2 = this.f2399l - arrayList.size();
        for (int i = 0; i < size2; i++) {
            if (size > i) {
                arrayList.add(arrayList2.get(i));
            }
        }
    }

    private Address m2611a(Node node, String str) {
        if (!node.hasChildNodes()) {
            return null;
        }
        Address b = CoreUtil.m490b();
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            String nodeName = item.getNodeName();
            String a = m1875a(item);
            if (item.getNodeType() == (short) 1 && !CoreUtil.m488a(a)) {
                m2612a(nodeName, a, b);
            }
        }
        return b;
    }

    private void m2612a(String str, String str2, Address address) {
        if (str.equals("address")) {
            address.setAddressLine(0, str2);
        } else if (str.equals("tel")) {
            address.setPhone(str2);
        } else if (str.equals("name")) {
            address.setFeatureName(str2);
        } else {
            m2617b(str, str2, address);
        }
    }

    private void m2617b(String str, String str2, Address address) {
        if (str.equals(GroupChatInvitation.ELEMENT_NAME)) {
            address.setLongitude(Double.parseDouble(str2));
        } else if (str.equals("y")) {
            address.setLatitude(Double.parseDouble(str2));
        }
    }

    protected void m2619a(ArrayList<Address> arrayList) {
        if (this.f2397j != null && arrayList.size() == 0) {
            arrayList.add(CoreUtil.m490b());
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Address address = (Address) it.next();
            address.setAdminArea(this.f2397j);
            address.setLocality(this.f2396i);
            try {
                Method method = address.getClass().getMethod("setSubLocality", new Class[]{String.class});
                if (method != null) {
                    method.invoke(address, new Object[]{this.f2398k});
                }
            } catch (Exception e) {
            }
        }
    }

    protected String m2626h() {
        if (m2623e()) {
            return MapServerUrl.m503a().m509d() + "?&config=SPAS&resType=xml";
        }
        return MapServerUrl.m503a().m509d();
    }

    protected ArrayList<Address> m2622d(InputStream inputStream) throws AMapException {
        if (m2623e()) {
            return super.m2516d(inputStream);
        }
        Address b;
        ArrayList<Address> arrayList = new ArrayList();
        if (inputStream != null) {
            int i;
            byte[] a = ByteUtil.m838a(inputStream);
            byte[] bArr = new byte[4];
            for (i = 0; i < 4; i++) {
                bArr[i] = a[i];
            }
            ByteUtil.m836a(bArr);
            int b2 = ByteUtil.m839b(bArr) + 4;
            byte[] bArr2 = new byte[(a.length - b2)];
            for (i = 0; i < a.length - b2; i++) {
                bArr2[i] = a[b2 + i];
            }
            List d = ReverseGeocodingProtoBuf.m4320a(bArr2).m4331d();
            int min = Math.min(this.f2399l, d.size());
            for (int i2 = 0; i2 < min; i2++) {
                ReverseGeocodingProtoBuf reverseGeocodingProtoBuf = (ReverseGeocodingProtoBuf) d.get(i2);
                this.f2397j = reverseGeocodingProtoBuf.m4487e().m4258e();
                this.f2396i = reverseGeocodingProtoBuf.m4489g().m4106e();
                this.f2398k = reverseGeocodingProtoBuf.m4491i().m4189e();
                ReverseGeocodingProtoBuf k = reverseGeocodingProtoBuf.m4493k();
                ReverseGeocodingProtoBuf m = reverseGeocodingProtoBuf.m4495m();
                reverseGeocodingProtoBuf.m4497o();
                int min2 = Math.min(this.f2399l, k.m4427d());
                for (i = 0; i < min2; i++) {
                    Address b3 = CoreUtil.m490b();
                    ReverseGeocodingProtoBuf a2 = k.m4424a(i);
                    b3.setAddressLine(0, a2.m4388i());
                    b3.setFeatureName(a2.m4386g());
                    try {
                        Method method = b3.getClass().getMethod("setPremises", new Class[]{String.class});
                        if (method != null) {
                            method.invoke(b3, new Object[]{Geocoder.Street_Road});
                        }
                    } catch (Exception e) {
                    }
                    if (b3 != null) {
                        try {
                            this.f2400m.add(b3);
                            this.f2403p = true;
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }
                m2619a(this.f2400m);
                int min3 = Math.min(this.f2399l, m.m4228d());
                for (i = 0; i < min3; i++) {
                    Address b4 = CoreUtil.m490b();
                    CommonProtoBuf a3 = m.m4225a(i);
                    b4.setAddressLine(0, a3.m3534q());
                    b4.setFeatureName(a3.m3524g());
                    b4.setPhone(a3.m3510O());
                    b4.setLatitude(Double.parseDouble(a3.m3530m()));
                    b4.setLongitude(Double.parseDouble(a3.m3528k()));
                    try {
                        Method method2 = b4.getClass().getMethod("setPremises", new Class[]{String.class});
                        if (method2 != null) {
                            method2.invoke(b4, new Object[]{Geocoder.POI});
                        }
                    } catch (Exception e3) {
                    }
                    if (b4 != null) {
                        this.f2401n.add(b4);
                        this.f2404q = true;
                    }
                }
                m2619a(this.f2401n);
                b = CoreUtil.m490b();
                try {
                    Method method3 = b.getClass().getMethod("setPremises", new Class[]{String.class});
                    if (method3 != null) {
                        method3.invoke(b, new Object[]{Geocoder.Cross});
                    }
                } catch (Exception e4) {
                }
                if (b.hasLatitude()) {
                    this.f2402o.add(b);
                    this.f2405r = true;
                }
            }
        }
        if (this.f2405r || this.f2404q || this.f2403p) {
            return m2616b((ArrayList) arrayList);
        }
        b = CoreUtil.m490b();
        b.setFeatureName(this.f2397j + this.f2396i + this.f2398k);
        arrayList.add(b);
        return arrayList;
    }

    protected boolean m2623e() {
        return false;
    }

    protected byte[] m2625g() {
        String format;
        ReverseGeocodingProtoBuf.ReverseGeocodingProtoBuf h = ReverseGeocodingProtoBuf.m4288h();
        CommonProtoBuf.CommonProtoBuf l = CommonProtoBuf.m3409l();
        l.m3396d("GBK");
        l.m3394c("buf");
        l.m3391a("SPAS");
        l.m3393b(ClientInfoUtil.m471a(null).m472a());
        String str = "<?xml version=\"1.0\" encoding=\"GB2312\"?><spatial_request method=\"searchPoint\"><x>%f</x><y>%f</y><poinumber>%d</poinumber><roadnumber>10</roadnumber><crossnumber>0</crossnumber><range>500</range><pattern>10</pattern></spatial_request>";
        String str2 = XmlPullParser.NO_NAMESPACE;
        try {
            format = String.format(str, new Object[]{Double.valueOf(((ReverseGeocodingParam) this.b).f369a), Double.valueOf(((ReverseGeocodingParam) this.b).f370b), Integer.valueOf(((ReverseGeocodingParam) this.b).f371c)});
        } catch (Exception e) {
            e.printStackTrace();
            format = str2;
        }
        h.m4273a(format);
        h.m4268a(l);
        byte[] a = ByteUtil.m837a(1818);
        byte[] toByteArray = h.m4277d().toByteArray();
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(toByteArray.length + a.length);
        byteArrayBuffer.append(a, 0, a.length);
        byteArrayBuffer.append(toByteArray, 0, toByteArray.length);
        return byteArrayBuffer.toByteArray();
    }
}

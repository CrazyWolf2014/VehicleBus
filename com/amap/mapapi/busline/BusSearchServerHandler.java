package com.amap.mapapi.busline;

import android.util.Log;
import com.amap.mapapi.busline.BusQuery.SearchType;
import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.ClientInfoUtil;
import com.amap.mapapi.core.CoreUtil;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.MapServerUrl;
import com.amap.mapapi.core.XmlListResultHandler;
import com.amap.mapapi.location.LocationManagerProxy;
import com.amap.mapapi.map.ByteUtil;
import com.amap.p004a.BusLineProtoBuf.BusLineProtoBuf.BusLineProtoBuf;
import com.amap.p004a.CommonProtoBuf.CommonProtoBuf.CommonProtoBuf;
import com.ifoer.mine.Contact;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.apache.http.util.ByteArrayBuffer;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.amap.mapapi.busline.a */
public class BusSearchServerHandler extends XmlListResultHandler<BusQuery, BusLineItem> {
    private int f2393i;
    private int f2394j;
    private int f2395k;

    /* renamed from: com.amap.mapapi.busline.a.a */
    static class BusSearchServerHandler {
        private ArrayList<BusStationItem> f272A;
        private float f273a;
        private String f274b;
        private int f275c;
        private String f276d;
        private int f277e;
        private float f278f;
        private ArrayList<GeoPoint> f279g;
        private String f280h;
        private String f281i;
        private String f282j;
        private String f283k;
        private String f284l;
        private String f285m;
        private String f286n;
        private float f287o;
        private float f288p;
        private boolean f289q;
        private boolean f290r;
        private boolean f291s;
        private boolean f292t;
        private boolean f293u;
        private int f294v;
        private boolean f295w;
        private String f296x;
        private String f297y;
        private boolean f298z;

        BusSearchServerHandler() {
        }

        public BusLineItem m461a() {
            BusLineItem busLineItem = new BusLineItem();
            busLineItem.setmLength(this.f273a);
            busLineItem.setmName(this.f274b);
            busLineItem.setmType(this.f275c);
            busLineItem.setmDescription(this.f276d);
            busLineItem.setmStatus(this.f277e);
            busLineItem.setmSpeed(this.f278f);
            busLineItem.setmXys(this.f279g);
            busLineItem.setmLineId(this.f280h);
            busLineItem.setmKeyName(this.f281i);
            busLineItem.setmFrontName(this.f282j);
            busLineItem.setmTerminalName(this.f283k);
            busLineItem.setmStartTime(this.f284l);
            busLineItem.setmEndTime(this.f285m);
            busLineItem.setmCompany(this.f286n);
            busLineItem.setmBasicPrice(this.f287o);
            busLineItem.setmTotalPrice(this.f288p);
            busLineItem.setmCommunicationTicket(this.f289q);
            busLineItem.setmAuto(this.f290r);
            busLineItem.setmIcCard(this.f291s);
            busLineItem.setmLoop(this.f292t);
            busLineItem.setmDoubleDeck(this.f293u);
            busLineItem.setmDataSource(this.f294v);
            busLineItem.setmAir(this.f295w);
            busLineItem.setmFrontSpell(this.f296x);
            busLineItem.setmTerminalSpell(this.f297y);
            busLineItem.setmExpressWay(this.f298z);
            busLineItem.setmStations(this.f272A);
            return busLineItem;
        }
    }

    public BusSearchServerHandler(BusQuery busQuery, Proxy proxy, String str, String str2) {
        super(busQuery, proxy, str, str2);
        this.f2393i = 1;
        this.f2394j = 10;
        this.f2395k = 0;
    }

    public void m2600a(int i) {
        this.f2393i = i;
    }

    public void m2604b(int i) {
        int i2 = 20;
        if (i <= 20) {
            i2 = i;
        }
        if (i2 <= 0) {
            i2 = 10;
        }
        this.f2394j = i2;
    }

    public int m2598a() {
        return this.f2394j;
    }

    public int m2603b() {
        return this.f2393i;
    }

    public BusQuery m2605c() {
        return (BusQuery) this.b;
    }

    public int m2606d() {
        return this.f2395k;
    }

    private void m2594b(Node node) {
        this.f2395k = Integer.parseInt(m1875a(node));
    }

    protected void m2602a(Node node, ArrayList<BusLineItem> arrayList) {
        if (node.getNodeType() == (short) 1) {
            int b = m2603b();
            String nodeName = node.getNodeName();
            if (nodeName.equals("count") && b == 1) {
                m2594b(node);
            }
            if (nodeName.equals("list")) {
                NodeList childNodes = node.getChildNodes();
                for (b = 0; b < childNodes.getLength(); b++) {
                    Node item = childNodes.item(b);
                    if (item.getNodeType() == (short) 1 && item.getNodeName().equals("bus")) {
                        BusSearchServerHandler busSearchServerHandler = new BusSearchServerHandler();
                        m2592a(item, busSearchServerHandler);
                        arrayList.add(busSearchServerHandler.m461a());
                    }
                }
            }
        }
    }

    private void m2592a(Node node, BusSearchServerHandler busSearchServerHandler) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeType() == (short) 1) {
                try {
                    m2591a(busSearchServerHandler, item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ArrayList<GeoPoint> m2590a(String str) {
        String[] split = str.split(",");
        ArrayList<GeoPoint> arrayList = new ArrayList();
        for (int i = 0; i < split.length - 1; i += 2) {
            arrayList.add(new GeoPoint((int) (Double.parseDouble(split[i + 1]) * 1000000.0d), (int) (Double.parseDouble(split[i]) * 1000000.0d)));
        }
        return arrayList;
    }

    private String m2593b(String str) {
        return str.replaceAll("&lt;\\?.*\\?&gt;", XmlPullParser.NO_NAMESPACE).replaceAll("(&lt;)([^&]*)(&gt;)", "<$2>");
    }

    private String m2595c(Node node) {
        if (node == null) {
            return null;
        }
        Node firstChild = node.getFirstChild();
        if (firstChild == null || firstChild.getNodeType() != (short) 4) {
            return null;
        }
        return firstChild.getNodeValue();
    }

    private ArrayList<BusStationItem> m2596c(NodeList nodeList) {
        if (nodeList == null || nodeList.getLength() <= 0) {
            return null;
        }
        Node item = nodeList.item(0);
        ArrayList<BusStationItem> arrayList = new ArrayList();
        if (item.getNodeName().equals("CONTENT")) {
            NodeList childNodes = item.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node item2 = childNodes.item(i);
                if (item2.getNodeType() == (short) 1 && item2.getNodeName().equals("STATION")) {
                    BusStationItem busStationItem = new BusStationItem();
                    NodeList childNodes2 = item2.getChildNodes();
                    for (int i2 = 0; i2 < childNodes2.getLength(); i2++) {
                        Node item3 = childNodes2.item(i2);
                        if (item3.getNodeType() == (short) 1) {
                            String nodeValue = item3.getAttributes().getNamedItem("NAME").getNodeValue();
                            String c = m2595c(item3);
                            if (nodeValue.equals("NAME")) {
                                busStationItem.setmName(c);
                            } else if (nodeValue.equals("XY_COORDS")) {
                                String[] split = c.split(";");
                                if (split.length >= 2) {
                                    busStationItem.setmCoord(new GeoPoint((int) (Double.parseDouble(split[1]) * 1000000.0d), (int) (Double.parseDouble(split[0]) * 1000000.0d)));
                                }
                            } else if (nodeValue.equals("SPELL")) {
                                busStationItem.setmSpell(c);
                            } else if (nodeValue.equals("Code")) {
                                busStationItem.setmCode(c);
                            } else if (nodeValue.equals("STATION_NUM")) {
                                busStationItem.setmStationNum(Integer.parseInt(c));
                            }
                        }
                    }
                    arrayList.add(busStationItem);
                }
            }
        }
        return arrayList;
    }

    private void m2591a(BusSearchServerHandler busSearchServerHandler, Node node) {
        String nodeName = node.getNodeName();
        String a = m1875a(node);
        if (nodeName.equals("length") && a != null) {
            busSearchServerHandler.f273a = Float.parseFloat(a);
        } else if (nodeName.equals("name")) {
            busSearchServerHandler.f274b = a;
        } else if (nodeName.equals(SharedPref.TYPE) && a != null) {
            busSearchServerHandler.f275c = Integer.parseInt(a);
        } else if (nodeName.equals("description")) {
            busSearchServerHandler.f276d = a;
        } else if (nodeName.equals(LocationManagerProxy.KEY_STATUS_CHANGED) && a != null) {
            busSearchServerHandler.f277e = Integer.parseInt(a);
        } else if (nodeName.equals("speed") && a != null) {
            busSearchServerHandler.f278f = Float.parseFloat(a);
        } else if (nodeName.equals("xys") && a != null && !a.equals(XmlPullParser.NO_NAMESPACE)) {
            busSearchServerHandler.f279g = m2590a(a);
        } else if (nodeName.equals("line_id")) {
            busSearchServerHandler.f280h = a;
        } else if (nodeName.equals("key_name")) {
            busSearchServerHandler.f281i = a;
        } else if (nodeName.equals("front_name")) {
            busSearchServerHandler.f282j = a;
        } else if (nodeName.equals("terminal_name")) {
            busSearchServerHandler.f283k = a;
        } else if (nodeName.equals("start_time")) {
            busSearchServerHandler.f284l = a;
        } else if (nodeName.equals("end_time")) {
            busSearchServerHandler.f285m = a;
        } else if (nodeName.equals("company")) {
            busSearchServerHandler.f286n = a;
        } else if (nodeName.equals("basic_price") && a != null) {
            busSearchServerHandler.f287o = Float.parseFloat(a);
        } else if (nodeName.equals("total_price") && a != null) {
            busSearchServerHandler.f288p = Float.parseFloat(a);
        } else if (nodeName.equals("commutation_ticket") && a != null) {
            busSearchServerHandler.f289q = a.equals(Contact.RELATION_FRIEND);
        } else if (nodeName.equals("auto") && a != null) {
            busSearchServerHandler.f290r = a.equals(Contact.RELATION_FRIEND);
        } else if (nodeName.equals("ic_card") && a != null) {
            busSearchServerHandler.f291s = a.equals(Contact.RELATION_FRIEND);
        } else if (nodeName.equals("loop") && a != null) {
            busSearchServerHandler.f292t = a.equals(Contact.RELATION_FRIEND);
        } else if (nodeName.equals("double_deck") && a != null) {
            busSearchServerHandler.f293u = a.equals(Contact.RELATION_FRIEND);
        } else if (nodeName.equals("data_source") && a != null) {
            busSearchServerHandler.f294v = Integer.parseInt(a);
        } else if (nodeName.equals("air") && a != null) {
            busSearchServerHandler.f295w = a.equals(Contact.RELATION_FRIEND);
        } else if (nodeName.equals("front_spell")) {
            busSearchServerHandler.f296x = a;
        } else if (nodeName.equals("terminal_spell")) {
            busSearchServerHandler.f297y = a;
        } else if (nodeName.equals("express_way") && a != null) {
            busSearchServerHandler.f298z = a.equals(Contact.RELATION_FRIEND);
        } else if (nodeName.equals("stationdes")) {
            busSearchServerHandler.f272A = m2596c(node.getChildNodes());
        }
    }

    protected void m2601a(ArrayList<BusLineItem> arrayList) {
    }

    protected boolean m2607e() {
        return true;
    }

    private boolean m2597c(String str) {
        if (str == null || str.equals(XmlPullParser.NO_NAMESPACE)) {
            return true;
        }
        return false;
    }

    protected String[] m2608f() {
        String encode;
        String[] strArr = new String[6];
        strArr[0] = "?config=BusLine";
        String city = ((BusQuery) this.b).getCity();
        if (m2597c(city)) {
            strArr[1] = "&cityCode=total";
        } else {
            try {
                strArr[1] = "&cityCode=" + URLEncoder.encode(city, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        city = ((BusQuery) this.b).getQueryString();
        try {
            encode = URLEncoder.encode(city, "utf-8");
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            encode = city;
        }
        if (((BusQuery) this.b).getCategory() == SearchType.BY_LINE_NAME) {
            strArr[2] = "&busName=" + encode;
        } else if (((BusQuery) this.b).getCategory() == SearchType.BY_ID) {
            strArr[2] = "&ids=" + encode;
        } else if (((BusQuery) this.b).getCategory() == SearchType.BY_STATION_NAME) {
            strArr[2] = "&stationName=" + encode;
        }
        strArr[3] = "&number=" + (XmlPullParser.NO_NAMESPACE + this.f2394j);
        strArr[4] = "&batch=" + (XmlPullParser.NO_NAMESPACE + this.f2393i);
        strArr[5] = "&enc=utf-8";
        return strArr;
    }

    protected byte[] m2609g() {
        BusLineProtoBuf t = com.amap.p004a.BusLineProtoBuf.BusLineProtoBuf.m2940t();
        CommonProtoBuf l = com.amap.p004a.CommonProtoBuf.CommonProtoBuf.m3409l();
        l.m3396d("GBK");
        l.m3394c("buf");
        l.m3393b(ClientInfoUtil.m471a(null).m472a());
        l.m3391a("BusLine");
        t.m2904a(l);
        String city = ((BusQuery) this.b).getCity();
        if (m2597c(city)) {
            t.m2908a("total");
        } else {
            t.m2908a(city);
        }
        String queryString = ((BusQuery) this.b).getQueryString();
        if (((BusQuery) this.b).getCategory() == SearchType.BY_LINE_NAME) {
            t.m2912c(queryString);
        } else if (((BusQuery) this.b).getCategory() == SearchType.BY_ID) {
            t.m2911b(queryString);
        } else if (((BusQuery) this.b).getCategory() == SearchType.BY_STATION_NAME) {
            t.m2914d(queryString);
        }
        t.m2916e(XmlPullParser.NO_NAMESPACE + this.f2394j);
        t.m2918f(XmlPullParser.NO_NAMESPACE + this.f2393i);
        byte[] a = ByteUtil.m837a(1800);
        byte[] toByteArray = t.m2915d().toByteArray();
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(toByteArray.length + a.length);
        byteArrayBuffer.append(a, 0, a.length);
        byteArrayBuffer.append(toByteArray, 0, toByteArray.length);
        return byteArrayBuffer.toByteArray();
    }

    protected String m2610h() {
        return MapServerUrl.m503a().m509d();
    }

    protected NodeList m2599a(InputStream inputStream) throws AMapException {
        String c = m525c(inputStream);
        Log.d("BUS", "XML string length = " + c.length());
        return CoreUtil.m492b(m2593b(c)).getDocumentElement().getChildNodes();
    }
}

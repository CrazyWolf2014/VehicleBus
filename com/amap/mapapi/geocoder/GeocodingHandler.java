package com.amap.mapapi.geocoder;

import android.location.Address;
import android.support.v4.view.MotionEventCompat;
import android.text.TextUtils;
import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.ClientInfoUtil;
import com.amap.mapapi.core.CoreUtil;
import com.amap.mapapi.core.MapServerUrl;
import com.amap.mapapi.core.XmlListResultHandler;
import com.amap.p004a.CommonProtoBuf.CommonProtoBuf.CommonProtoBuf;
import com.amap.p004a.GeocodingProtoBuf.GeocodingProtoBuf;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.achartengine.renderer.DefaultRenderer;
import org.apache.http.util.ByteArrayBuffer;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.amap.mapapi.geocoder.a */
class GeocodingHandler extends XmlListResultHandler<GeocodingParam, Address> {
    public int f2407i;

    protected /* synthetic */ Object m2634b(InputStream inputStream) throws AMapException {
        return m2636d(inputStream);
    }

    protected /* synthetic */ Object m2635b(NodeList nodeList) {
        return m2631a(nodeList);
    }

    public GeocodingHandler(GeocodingParam geocodingParam, Proxy proxy, String str, String str2) {
        super(geocodingParam, proxy, str, str2);
        this.f2407i = 0;
        this.f2407i = geocodingParam.f404b;
    }

    private byte[] m2629a(int i) {
        return new byte[]{(byte) (i & KEYRecord.PROTOCOL_ANY), (byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i) >> 8), (byte) ((16711680 & i) >> 16), (byte) ((DefaultRenderer.BACKGROUND_COLOR & i) >> 24)};
    }

    private int m2627a(byte[] bArr) {
        return (((bArr[3] & KEYRecord.PROTOCOL_ANY) | ((bArr[2] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK)) | ((bArr[1] << 16) & 16711680)) | ((bArr[0] << 24) & DefaultRenderer.BACKGROUND_COLOR);
    }

    protected String[] m2638f() {
        String[] strArr = new String[3];
        strArr[0] = "&enc=utf-8";
        strArr[1] = "&ver=2";
        String str = ((GeocodingParam) this.b).f403a;
        try {
            str = URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        strArr[2] = "&address=" + str;
        return strArr;
    }

    protected ArrayList<Address> m2631a(NodeList nodeList) {
        return super.m2511a(nodeList);
    }

    public static byte[] m2630e(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[Flags.EXTEND];
        while (true) {
            int read = inputStream.read(bArr, 0, Flags.EXTEND);
            if (read == -1) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    protected ArrayList<Address> m2636d(InputStream inputStream) throws AMapException {
        int i = 0;
        if (m2637e()) {
            return super.m2516d(inputStream);
        }
        ArrayList<Address> arrayList = new ArrayList();
        if (inputStream == null) {
            return arrayList;
        }
        try {
            int i2;
            byte[] e = GeocodingHandler.m2630e(inputStream);
            byte[] bArr = new byte[4];
            for (i2 = 0; i2 < 4; i2++) {
                bArr[i2] = e[i2];
            }
            int a = m2627a(bArr) + 4;
            byte[] bArr2 = new byte[(e.length - a)];
            for (i2 = 0; i2 < e.length - a; i2++) {
                bArr2[i2] = e[a + i2];
            }
            GeocodingProtoBuf g = GeocodingProtoBuf.m3789a(bArr2).m3800g();
            while (i < g.m3711d()) {
                GeocodingProtoBuf a2 = g.m3708a(i);
                Address b = CoreUtil.m490b();
                m2628a(a2, b);
                arrayList.add(b);
                i++;
            }
            return arrayList;
        } catch (IOException e2) {
            e2.printStackTrace();
            return arrayList;
        } catch (Exception e3) {
            e3.printStackTrace();
            return arrayList;
        }
    }

    private void m2628a(GeocodingProtoBuf geocodingProtoBuf, Address address) {
        Object m = geocodingProtoBuf.m3669m();
        if (!TextUtils.isEmpty(m)) {
            address.setFeatureName(m);
        }
        m = geocodingProtoBuf.m3673q();
        if (!TextUtils.isEmpty(m)) {
            address.setLocality(m);
        }
        m = geocodingProtoBuf.m3671o();
        if (!TextUtils.isEmpty(m)) {
            address.setAdminArea(m);
        }
        m = geocodingProtoBuf.m3677u();
        if (!TextUtils.isEmpty(m)) {
            address.setSubLocality(m);
        }
        m = geocodingProtoBuf.m3665i();
        if (!TextUtils.isEmpty(m)) {
            address.setLongitude(Double.parseDouble(m));
        }
        m = geocodingProtoBuf.m3667k();
        if (!TextUtils.isEmpty(m)) {
            address.setLatitude(Double.parseDouble(m));
        }
    }

    protected void m2632a(ArrayList<Address> arrayList) {
    }

    protected String m2640h() {
        return MapServerUrl.m503a().m509d();
    }

    protected boolean m2637e() {
        return false;
    }

    protected void m2633a(Node node, ArrayList<Address> arrayList) {
    }

    protected byte[] m2639g() {
        GeocodingProtoBuf.GeocodingProtoBuf l = GeocodingProtoBuf.m3742l();
        CommonProtoBuf l2 = com.amap.p004a.CommonProtoBuf.CommonProtoBuf.m3409l();
        l2.m3393b(ClientInfoUtil.m471a(null).m472a());
        l2.m3391a("GOC");
        l2.m3394c("buf");
        l2.m3396d(AsyncHttpResponseHandler.DEFAULT_CHARSET);
        l.m3719a(l2.m3397d());
        l.m3723a(((GeocodingParam) this.b).f403a);
        l.m3726b(((GeocodingParam) this.b).f404b + XmlPullParser.NO_NAMESPACE);
        l.m3727c("2.0");
        byte[] toByteArray = l.m3729d().toByteArray();
        byte[] a = m2629a(1848);
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(a.length + toByteArray.length);
        byteArrayBuffer.append(a, 0, a.length);
        byteArrayBuffer.append(toByteArray, 0, toByteArray.length);
        return byteArrayBuffer.toByteArray();
    }
}

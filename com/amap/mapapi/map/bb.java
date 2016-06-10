package com.amap.mapapi.map;

import android.graphics.PointF;
import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.HttpTool;
import com.amap.mapapi.core.MapServerUrl;
import com.amap.mapapi.core.ProtocalHandler;
import com.amap.mapapi.core.SyncList;
import com.amap.mapapi.map.TrafficProtos.TrafficTile;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficSegment;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficSegment.TrafficSpeed;
import com.amap.mapapi.map.ar.Tile;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.google.protobuf.InvalidProtocolBufferException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: TrafficServerHandler */
class bb extends ProtocalHandler<ArrayList<Tile>, ArrayList<Tile>> {
    private LayerPropertys f1873i;

    protected /* synthetic */ Object m1968b(InputStream inputStream) throws AMapException {
        return m1966a(inputStream);
    }

    protected /* synthetic */ Object m1974k() {
        return m1965a();
    }

    public bb(ArrayList<Tile> arrayList, Proxy proxy, String str, String str2) {
        super(arrayList, proxy, str, str2);
        this.f1873i = null;
    }

    public void m1967a(LayerPropertys layerPropertys) {
        this.f1873i = layerPropertys;
    }

    protected ArrayList<Tile> m1965a() {
        ArrayList<Tile> arrayList = new ArrayList();
        Iterator it = ((ArrayList) this.b).iterator();
        while (it.hasNext()) {
            arrayList.add(new Tile((Tile) it.next()));
        }
        return arrayList;
    }

    public int m1964a(List<ax> list, Tile tile) {
        if (tile == null) {
            return -1;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tile.f630b);
        stringBuilder.append("-");
        stringBuilder.append(tile.f631c);
        stringBuilder.append("-");
        stringBuilder.append(tile.f632d);
        int a = this.f1873i.f1930m.m832a(null, null, false, list, stringBuilder.toString());
        if (a < 0) {
            return -1;
        }
        m1963a(tile, a);
        return a;
    }

    private void m1963a(Tile tile, int i) {
        if (tile != null && i >= 0) {
            SyncList syncList = this.f1873i.f1932o;
            int size = syncList.size();
            for (int i2 = 0; i2 < size; i2++) {
                Tile tile2 = (Tile) syncList.get(i2);
                if (tile2 != null && tile2.equals(tile)) {
                    tile2.f635g = i;
                    return;
                }
            }
        }
    }

    protected String m1972h() {
        return MapServerUrl.m503a().m513f() + "/traffic?scale=1&";
    }

    protected byte[] m1973i() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("p=http://172.17.41.96/traffic?&size=1&z=").append(((Tile) ((ArrayList) this.b).get(0)).f632d).append("&&");
        stringBuilder.append("s=x=").append(((Tile) ((ArrayList) this.b).get(0)).f630b).append("&y=").append(((Tile) ((ArrayList) this.b).get(0)).f631c);
        stringBuilder.trimToSize();
        String str = null;
        try {
            str = URLEncoder.encode(stringBuilder.toString(), AsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (Exception e) {
        }
        return str.getBytes();
    }

    protected String[] m1970f() {
        return new String[]{"post"};
    }

    protected ArrayList<Tile> m1966a(InputStream inputStream) throws AMapException {
        Object a = HttpTool.m502a(inputStream);
        byte[] bArr = new byte[2];
        System.arraycopy(a, 0, bArr, 0, 2);
        short a2 = HttpTool.m501a(bArr);
        System.arraycopy(a, 2, new byte[a2], 0, a2);
        byte[] bArr2 = new byte[2];
        System.arraycopy(a, a2 + 2, bArr2, 0, 2);
        short a3 = HttpTool.m501a(bArr2);
        byte[] bArr3 = new byte[a3];
        System.arraycopy(a, (a2 + 2) + 2, bArr3, 0, a3);
        try {
            TrafficTile parseFrom = TrafficTile.parseFrom(bArr3);
            byte[] toByteArray = parseFrom.getVertices().toByteArray();
            List trafficSegmentList = parseFrom.getTrafficSegmentList();
            List arrayList = new ArrayList();
            int size = trafficSegmentList.size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                ax axVar = new ax();
                TrafficSegment trafficSegment = (TrafficSegment) trafficSegmentList.get(i2);
                int vertexOffset = trafficSegment.getVertexOffset();
                int vertexCount = trafficSegment.getVertexCount();
                axVar.m809a(vertexOffset);
                axVar.m812b(vertexCount);
                TrafficSpeed speed = trafficSegment.getSpeed();
                if (speed.name().equals("SLOW")) {
                    axVar.m813c(1);
                } else if (speed.name().equals("MEDIUM")) {
                    axVar.m813c(2);
                } else if (speed.name().equals("FAST")) {
                    axVar.m813c(3);
                }
                axVar.m814d(trafficSegment.getWidth());
                vertexCount = BitUtil.m819a(toByteArray, i, 8, true) + 1;
                int i3 = i + 8;
                int a4 = BitUtil.m819a(toByteArray, i3, 4, true);
                i3 += 4;
                vertexOffset = BitUtil.m819a(toByteArray, i3, 10, true) - 10;
                i3 += 10;
                int a5 = BitUtil.m819a(toByteArray, i3, 10, true) - 10;
                i = i3 + 10;
                List arrayList2 = new ArrayList(vertexCount);
                arrayList2.add(new PointF((float) vertexOffset, (float) a5));
                for (vertexOffset = 1; vertexOffset < vertexCount; vertexOffset++) {
                    a5 = BitUtil.m819a(toByteArray, i, a4, false);
                    i3 = i + a4;
                    i = i3 + a4;
                    float f = (float) a5;
                    arrayList2.add(new PointF(f + ((PointF) arrayList2.get(vertexOffset - 1)).x, ((PointF) arrayList2.get(vertexOffset - 1)).y + ((float) BitUtil.m819a(toByteArray, i3, a4, false))));
                }
                axVar.m810a(arrayList2);
                arrayList.add(axVar);
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new AMapException(AMapException.ERROR_IO);
                }
            }
            if (m1964a(arrayList, (Tile) ((ArrayList) this.b).get(0)) >= 0) {
                return null;
            }
            ArrayList<Tile> arrayList3 = new ArrayList();
            Iterator it = ((ArrayList) this.b).iterator();
            while (it.hasNext()) {
                arrayList3.add(new Tile((Tile) it.next()));
            }
            return arrayList3;
        } catch (InvalidProtocolBufferException e2) {
            throw new AMapException(e2.getMessage());
        }
    }

    protected boolean m1969e() {
        return false;
    }

    protected byte[] m1971g() {
        return null;
    }
}

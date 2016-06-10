package com.amap.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.ProtocalHandler;
import com.amap.mapapi.core.SyncList;
import com.amap.mapapi.map.ar.Tile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: TileServerHandler */
class av extends ProtocalHandler<ArrayList<Tile>, ArrayList<Tile>> {
    private LayerPropertys f1872i;

    protected /* synthetic */ Object m1955b(InputStream inputStream) throws AMapException {
        return m1953a(inputStream);
    }

    protected /* synthetic */ Object m1960k() {
        return m1952a();
    }

    public av(ArrayList<Tile> arrayList, Proxy proxy, String str, String str2) {
        super(arrayList, proxy, str, str2);
        this.f1872i = null;
    }

    public void m1954a(LayerPropertys layerPropertys) {
        this.f1872i = layerPropertys;
    }

    protected ArrayList<Tile> m1952a() {
        ArrayList<Tile> arrayList = new ArrayList();
        Iterator it = ((ArrayList) this.b).iterator();
        while (it.hasNext()) {
            arrayList.add(new Tile((Tile) it.next()));
        }
        return arrayList;
    }

    protected ArrayList<Tile> m1953a(InputStream inputStream) throws AMapException {
        ArrayList<Tile> arrayList = null;
        if (this.b != null) {
            int size = ((ArrayList) this.b).size();
            int i = 0;
            while (i < size) {
                Tile tile = (Tile) ((ArrayList) this.b).get(i);
                if (m1951a(inputStream, tile) < 0) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(new Tile(tile));
                }
                i++;
                arrayList = arrayList;
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new AMapException(AMapException.ERROR_IO);
                }
            }
        }
        return arrayList;
    }

    protected String[] m1957f() {
        return null;
    }

    protected String m1959h() {
        return this.f1872i.f1927j.m817a(((Tile) ((ArrayList) this.b).get(0)).f630b, ((Tile) ((ArrayList) this.b).get(0)).f631c, ((Tile) ((ArrayList) this.b).get(0)).f632d);
    }

    public int m1951a(InputStream inputStream, Tile tile) {
        if (tile == null || inputStream == null) {
            return -1;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tile.f630b);
        stringBuilder.append("-");
        stringBuilder.append(tile.f631c);
        stringBuilder.append("-");
        stringBuilder.append(tile.f632d);
        if (this.f1872i == null || this.f1872i.f1930m == null) {
            return -1;
        }
        int a = this.f1872i.f1930m.m832a(null, inputStream, false, null, stringBuilder.toString());
        if (a < 0) {
            return -1;
        }
        m1949a(tile, a);
        if (this.f1872i == null || !this.f1872i.f1924g) {
            return a;
        }
        byte[] a2 = m1950a(this.f1872i.f1930m.m833a(a));
        if (this.f1872i == null || this.f1872i.f1931n == null) {
            return a;
        }
        this.f1872i.f1931n.m847a(a2, tile.f630b, tile.f631c, tile.f632d);
        return a;
    }

    private byte[] m1950a(Bitmap bitmap) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void m1949a(Tile tile, int i) {
        if (tile != null && i >= 0 && this.f1872i != null && this.f1872i.f1932o != null) {
            SyncList syncList = this.f1872i.f1932o;
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

    protected boolean m1956e() {
        return true;
    }

    protected byte[] m1958g() {
        return null;
    }
}

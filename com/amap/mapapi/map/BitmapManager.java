package com.amap.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import java.io.InputStream;
import java.util.List;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.amap.mapapi.map.i */
class BitmapManager {
    protected final BitmapManager[] f680a;
    protected final int f681b;
    protected final int f682c;
    protected final BitmapManager[] f683d;
    Paint f684e;
    Path f685f;
    private boolean f686g;
    private long f687h;

    /* renamed from: com.amap.mapapi.map.i.a */
    class BitmapManager {
        Bitmap f672a;
        String f673b;
        boolean f674c;
        long f675d;
        int f676e;
        long f677f;
        List<ax> f678g;
        final /* synthetic */ BitmapManager f679h;

        BitmapManager(BitmapManager bitmapManager) {
            this.f679h = bitmapManager;
            this.f672a = null;
            this.f673b = XmlPullParser.NO_NAMESPACE;
            this.f674c = false;
            this.f675d = 0;
            this.f676e = -1;
            this.f677f = 0;
            this.f678g = null;
        }
    }

    /* renamed from: com.amap.mapapi.map.i.1 */
    class BitmapManager implements BitmapMaker {
        final /* synthetic */ List f1903a;
        final /* synthetic */ BitmapManager f1904b;

        BitmapManager(BitmapManager bitmapManager, List list) {
            this.f1904b = bitmapManager;
            this.f1903a = list;
        }

        public void m1990a(Canvas canvas) {
            if (this.f1904b.f684e == null) {
                this.f1904b.f684e = new Paint();
                this.f1904b.f684e.setStyle(Style.STROKE);
                this.f1904b.f684e.setDither(true);
                this.f1904b.f684e.setAntiAlias(true);
                this.f1904b.f684e.setStrokeJoin(Join.ROUND);
                this.f1904b.f684e.setStrokeCap(Cap.ROUND);
            }
            if (this.f1904b.f685f == null) {
                this.f1904b.f685f = new Path();
            }
            int size = this.f1903a.size();
            for (int i = 0; i < size; i++) {
                ax axVar = (ax) this.f1903a.get(i);
                this.f1904b.f684e.setStrokeWidth(3.0f);
                int b = axVar.m811b();
                if (b == 1) {
                    this.f1904b.f684e.setColor(-65536);
                } else if (b == 2) {
                    this.f1904b.f684e.setColor(-256);
                } else if (b == 3) {
                    this.f1904b.f684e.setColor(-16711936);
                }
                List a = axVar.m808a();
                int size2 = a.size();
                int i2 = 0;
                boolean z = true;
                while (i2 < size2) {
                    boolean z2;
                    PointF pointF = (PointF) a.get(i2);
                    if (z) {
                        this.f1904b.f685f.moveTo(pointF.x, pointF.y);
                        z2 = false;
                    } else {
                        this.f1904b.f685f.lineTo(pointF.x, pointF.y);
                        z2 = z;
                    }
                    i2++;
                    z = z2;
                }
                canvas.drawPath(this.f1904b.f685f, this.f1904b.f684e);
                this.f1904b.f685f.reset();
            }
        }
    }

    public BitmapManager(int i, int i2, boolean z, long j) {
        this.f686g = false;
        this.f687h = 0;
        this.f684e = null;
        this.f685f = null;
        this.f681b = i;
        this.f682c = i2;
        this.f686g = z;
        this.f687h = 1000000 * j;
        if (this.f681b > 0) {
            this.f680a = new BitmapManager[this.f681b];
            this.f683d = new BitmapManager[this.f682c];
            return;
        }
        this.f680a = null;
        this.f683d = null;
    }

    protected int m831a(String str) {
        if (str.equals(XmlPullParser.NO_NAMESPACE)) {
            return -1;
        }
        int i = 0;
        while (i < this.f681b) {
            if (this.f680a[i] == null || !this.f680a[i].f673b.equals(str)) {
                i++;
            } else if (!this.f680a[i].f674c) {
                return -1;
            } else {
                if (!this.f686g || m829d() - this.f680a[i].f677f <= this.f687h) {
                    this.f680a[i].f675d = m829d();
                    return i;
                }
                this.f680a[i].f674c = false;
                return -1;
            }
        }
        return -1;
    }

    protected Bitmap m833a(int i) {
        if (i < 0 || i >= this.f681b || this.f680a[i] == null) {
            return null;
        }
        return this.f680a[i].f672a;
    }

    protected synchronized int m832a(byte[] bArr, InputStream inputStream, boolean z, List<ax> list, String str) {
        int i = -1;
        synchronized (this) {
            if (!(bArr == null && inputStream == null && list == null)) {
                int b = m834b();
                if (b < 0) {
                    b = m830a();
                }
                if (b >= 0 && this.f680a != null) {
                    if (!(this.f680a[b] == null || this.f680a[b].f672a == null || this.f680a[b].f672a.isRecycled())) {
                        this.f680a[b].f672a.recycle();
                        this.f680a[b].f672a = null;
                    }
                    if (this.f680a[b].f678g != null) {
                        this.f680a[b].f678g.clear();
                        this.f680a[b].f678g = null;
                    }
                    if (z && bArr != null) {
                        try {
                            this.f680a[b].f672a = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
                        } catch (OutOfMemoryError e) {
                        }
                    } else if (inputStream != null) {
                        try {
                            this.f680a[b].f672a = BitmapFactory.decodeStream(inputStream);
                        } catch (OutOfMemoryError e2) {
                        }
                    }
                    if (list != null) {
                        this.f680a[b].f672a = Bitmap.createBitmap(KEYRecord.OWNER_ZONE, KEYRecord.OWNER_ZONE, Config.ARGB_4444);
                        m828a(this.f680a[b].f672a, list);
                    }
                    if (!(this.f680a == null || (this.f680a[b].f672a == null && this.f680a[b].f678g == null))) {
                        if (this.f680a[b] != null) {
                            this.f680a[b].f674c = true;
                            this.f680a[b].f673b = str;
                            this.f680a[b].f675d = m829d();
                            if (this.f686g) {
                                this.f680a[b].f677f = m829d();
                            }
                        }
                        i = b;
                    }
                }
            }
        }
        return i;
    }

    private long m829d() {
        return System.nanoTime();
    }

    protected int m830a() {
        int i;
        for (i = 0; i < this.f682c; i++) {
            this.f683d[i] = null;
        }
        for (int i2 = 0; i2 < this.f681b; i2++) {
            BitmapManager bitmapManager = this.f680a[i2];
            int i3 = 0;
            while (i3 < this.f682c) {
                if (this.f683d[i3] == null) {
                    this.f683d[i3] = bitmapManager;
                    break;
                }
                BitmapManager bitmapManager2;
                if (this.f683d[i3].f675d > bitmapManager.f675d) {
                    bitmapManager2 = this.f683d[i3];
                    this.f683d[i3] = bitmapManager;
                } else {
                    bitmapManager2 = bitmapManager;
                }
                i3++;
                bitmapManager = bitmapManager2;
            }
        }
        int i4 = -1;
        for (i = 0; i < this.f682c; i++) {
            if (this.f683d[i] != null) {
                this.f683d[i].f674c = false;
                if (i4 < 0) {
                    i4 = this.f683d[i].f676e;
                }
            }
        }
        return i4;
    }

    protected int m834b() {
        int i = -1;
        for (int i2 = 0; i2 < this.f681b; i2++) {
            if (this.f680a[i2] == null) {
                this.f680a[i2] = new BitmapManager(this);
                this.f680a[i2].f676e = i2;
                return i2;
            }
            if (!this.f680a[i2].f674c && i < 0) {
                i = i2;
            }
        }
        return i;
    }

    protected void m835c() {
        int i = 0;
        while (i < this.f681b) {
            if (this.f680a[i] != null) {
                if (!(this.f680a[i].f672a == null || this.f680a[i].f672a.isRecycled())) {
                    this.f680a[i].f672a.recycle();
                }
                this.f680a[i].f672a = null;
            }
            i++;
        }
    }

    private void m828a(Bitmap bitmap, List<ax> list) {
        BitmapMaker bitmapManager = new BitmapManager(this, list);
        BitmapDrawer bitmapDrawer = new BitmapDrawer(null);
        bitmapDrawer.m824a(bitmap);
        bitmapDrawer.m825a(bitmapManager);
    }
}

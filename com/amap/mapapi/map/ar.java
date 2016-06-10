package com.amap.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import org.xbill.DNS.KEYRecord;

/* compiled from: Tile */
class ar {
    private static Paint f636a;
    private static Bitmap f637b;
    private static int f638c;

    /* renamed from: com.amap.mapapi.map.ar.a */
    static class Tile {
        public int f629a;
        public final int f630b;
        public final int f631c;
        public final int f632d;
        public final int f633e;
        public PointF f634f;
        public int f635g;

        public /* synthetic */ Object clone() throws CloneNotSupportedException {
            return m794a();
        }

        public Tile(int i, int i2, int i3, int i4) {
            this.f629a = 0;
            this.f635g = -1;
            this.f630b = i;
            this.f631c = i2;
            this.f632d = i3;
            this.f633e = i4;
        }

        public Tile(Tile tile) {
            this.f629a = 0;
            this.f635g = -1;
            this.f630b = tile.f630b;
            this.f631c = tile.f631c;
            this.f632d = tile.f632d;
            this.f633e = tile.f633e;
            this.f634f = tile.f634f;
            this.f629a = tile.f629a;
        }

        public Tile m794a() {
            return new Tile(this);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Tile)) {
                return false;
            }
            Tile tile = (Tile) obj;
            if (this.f630b == tile.f630b && this.f631c == tile.f631c && this.f632d == tile.f632d && this.f633e == tile.f633e) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (((this.f630b * 7) + (this.f631c * 11)) + (this.f632d * 13)) + this.f633e;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.f630b);
            stringBuilder.append("-");
            stringBuilder.append(this.f631c);
            stringBuilder.append("-");
            stringBuilder.append(this.f632d);
            stringBuilder.append("-");
            stringBuilder.append(this.f633e);
            return stringBuilder.toString();
        }
    }

    /* renamed from: com.amap.mapapi.map.ar.1 */
    static class Tile implements BitmapMaker {
        Tile() {
        }

        public void m1947a(Canvas canvas) {
            Paint b = ar.m796b();
            canvas.drawColor(ar.m795a());
            for (int i = 0; i < 235; i += 21) {
                canvas.drawLine((float) i, 0.0f, (float) i, 256.0f, b);
                canvas.drawLine(0.0f, (float) i, 256.0f, (float) i, b);
            }
        }
    }

    ar() {
    }

    static {
        f636a = null;
        f637b = null;
        f638c = Color.rgb(222, 215, 214);
    }

    public static int m795a() {
        return f638c;
    }

    public static Paint m796b() {
        if (f636a == null) {
            f636a = new Paint();
            f636a.setColor(-7829368);
            f636a.setAlpha(90);
            f636a.setPathEffect(new DashPathEffect(new float[]{2.0f, 2.5f}, 1.0f));
        }
        return f636a;
    }

    public static Bitmap m797c() {
        if (f637b == null) {
            BitmapMaker tile = new Tile();
            BitmapDrawer bitmapDrawer = new BitmapDrawer(Config.ARGB_4444);
            bitmapDrawer.m823a(KEYRecord.OWNER_ZONE, KEYRecord.OWNER_ZONE);
            bitmapDrawer.m825a(tile);
            f637b = bitmapDrawer.m826b();
        }
        return f637b;
    }
}

package com.mapabc.minimap.map.vmap;

import android.graphics.Point;
import com.amap.mapapi.map.bf;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Service;

public class VMapProjection {
    public static final double EarthCircumferenceInMeters = 4.007501668557849E7d;
    public static final int EarthRadiusInMeters = 6378137;
    public static final int MAXZOOMLEVEL = 20;
    public static final double MaxLatitude = 85.0511287798d;
    public static final double MaxLongitude = 180.0d;
    public static final double MinLatitude = -85.0511287798d;
    public static final double MinLongitude = -180.0d;
    public static final int PixelsPerTile = 256;
    public static final int TileSplitLevel = 0;

    public static double Clip(double d, double d2, double d3) {
        return Math.min(Math.max(d, d2), d3);
    }

    public static bf LatLongToPixels(int i, int i2, int i3) {
        return LatLongToPixels(((double) i2) / 3600000.0d, ((double) i) / 3600000.0d, i3);
    }

    public static bf LatLongToPixels(double d, double d2, int i) {
        bf bfVar = new bf();
        double Clip = (Clip(d2, MinLongitude, MaxLongitude) * 3.141592653589793d) / MaxLongitude;
        double sin = Math.sin((Clip(d, MinLatitude, MaxLatitude) * 3.141592653589793d) / MaxLongitude);
        double log = 3189068.0d * Math.log((1.0d + sin) / (1.0d - sin));
        long j = 256 << i;
        double d3 = EarthCircumferenceInMeters / ((double) j);
        bfVar.f665a = (int) Clip((((Clip * 6378137.0d) + 2.0037508342789244E7d) / d3) + 0.5d, 0.0d, (double) (j - 1));
        bfVar.f666b = (int) Clip((((double) ((long) (2.0037508342789244E7d - log))) / d3) + 0.5d, 0.0d, (double) (j - 1));
        return bfVar;
    }

    public static bf PixelsToPixels(long j, long j2, int i, int i2) {
        int i3 = i2 - i;
        if (i3 > 0) {
            j >>= i3;
            j2 >>= i3;
        } else if (i3 < 0) {
            j <<= i3;
            j2 <<= i3;
        }
        bf bfVar = new bf();
        bfVar.f665a = (int) j;
        bfVar.f666b = (int) j2;
        return bfVar;
    }

    public static DPoint PixelsToLatLong(long j, long j2, int i) {
        DPoint dPoint = new DPoint();
        double d = EarthCircumferenceInMeters / ((double) ((1 << i) * PixelsPerTile));
        double d2 = (((double) j) * d) - 2.0037508342789244E7d;
        dPoint.f1324b = 1.5707963267948966d - (Math.atan(Math.exp((-(2.0037508342789244E7d - (d * ((double) j2)))) / 6378137.0d)) * 2.0d);
        dPoint.f1324b *= 57.29577951308232d;
        dPoint.f1323a = d2 / 6378137.0d;
        dPoint.f1323a *= 57.29577951308232d;
        return dPoint;
    }

    public static bf PixelsToTile(int i, int i2) {
        bf bfVar = new bf();
        bfVar.f665a = i / PixelsPerTile;
        bfVar.f666b = i2 / PixelsPerTile;
        return bfVar;
    }

    public static String TileToQuadKey(int i, int i2, int i3) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i4 = i3 + 0; i4 > 0; i4--) {
            long j = (long) (1 << (i4 - 1));
            int i5 = 0;
            if ((((long) i) & j) != 0) {
                i5 = 1;
            }
            if ((j & ((long) i2)) != 0) {
                i5 += 2;
            }
            stringBuffer.append(i5);
        }
        return stringBuffer.toString();
    }

    public static bf QuadKeyToTitle(String str) {
        int i = 0;
        int length = str.length();
        int i2 = 0;
        for (int i3 = 1; i3 <= length; i3++) {
            int i4 = 1 << (length - i3);
            switch (str.charAt(i3 - 1)) {
                case Type.DNSKEY /*48*/:
                    i2 &= i4 ^ -1;
                    i &= i4 ^ -1;
                    break;
                case Service.LOGIN /*49*/:
                    i2 |= i4;
                    i &= i4 ^ -1;
                    break;
                case Type.NSEC3 /*50*/:
                    i2 &= i4 ^ -1;
                    i |= i4;
                    break;
                case Service.LA_MAINT /*51*/:
                    i2 |= i4;
                    i |= i4;
                    break;
                default:
                    break;
            }
        }
        bf bfVar = new bf();
        bfVar.f665a = i2;
        bfVar.f666b = i;
        return bfVar;
    }

    public static Point QuadKeyToTile(String str) {
        int i = 0;
        int length = str.length();
        int i2 = 0;
        for (int i3 = 1; i3 <= length; i3++) {
            int i4 = 1 << (length - i3);
            switch (str.charAt(i3 - 1)) {
                case Type.DNSKEY /*48*/:
                    i2 &= i4 ^ -1;
                    i &= i4 ^ -1;
                    break;
                case Service.LOGIN /*49*/:
                    i2 |= i4;
                    i &= i4 ^ -1;
                    break;
                case Type.NSEC3 /*50*/:
                    i2 &= i4 ^ -1;
                    i |= i4;
                    break;
                case Service.LA_MAINT /*51*/:
                    i2 |= i4;
                    i |= i4;
                    break;
                default:
                    break;
            }
        }
        Point point = new Point();
        point.x = i2;
        point.y = i;
        return point;
    }
}

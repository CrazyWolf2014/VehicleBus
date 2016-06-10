package com.amap.mapapi.route;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.ClientInfoUtil;
import com.amap.mapapi.core.CoreUtil;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.GeoPoint.C0086b;
import com.amap.mapapi.core.LocTansServerHandler;
import com.amap.mapapi.core.ReverseGeocodingHandler;
import com.amap.mapapi.core.ReverseGeocodingParam;
import com.amap.mapapi.core.RouteResource;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.RouteMessageHandler;
import com.amap.mapapi.map.RouteOverlay;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import com.thoughtworks.xstream.XStream;
import java.net.Proxy;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.achartengine.renderer.DefaultRenderer;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public class Route {
    public static final int BusDefault = 0;
    public static final int BusLeaseChange = 2;
    public static final int BusLeaseWalk = 3;
    public static final int BusMostComfortable = 4;
    public static final int BusSaveMoney = 1;
    public static final int DrivingDefault = 10;
    public static final int DrivingLeastDistance = 12;
    public static final int DrivingNoFastRoad = 13;
    public static final int DrivingSaveMoney = 11;
    private GeoPoint f734a;
    private GeoPoint f735b;
    private int f736c;
    public C0097d mHelper;
    protected List<Segment> mSegs;
    protected String mStartPlace;
    protected String mTargetPlace;

    public static class FromAndTo {
        public static final int NoTrans = 0;
        public static final int TransBothPoint = 3;
        public static final int TransFromPoint = 1;
        public static final int TransToPoint = 2;
        public GeoPoint mFrom;
        public GeoPoint mTo;
        public int mTrans;

        public FromAndTo(GeoPoint geoPoint, GeoPoint geoPoint2, int i) {
            this.mFrom = geoPoint;
            this.mTo = geoPoint2;
            this.mTrans = i;
        }

        public FromAndTo(GeoPoint geoPoint, GeoPoint geoPoint2) {
            this(geoPoint, geoPoint2, NoTrans);
        }

        private void m868a(Context context, GeoPoint geoPoint, GeoPoint geoPoint2, int i) throws AMapException {
            C0086b a;
            switch (this.mTrans) {
                case NoTrans /*0*/:
                    this.mFrom = geoPoint;
                    this.mTo = geoPoint2;
                case TransFromPoint /*1*/:
                    a = m867a(context, geoPoint);
                    this.mFrom = new GeoPoint(CoreUtil.m484a(a.f303b), CoreUtil.m484a(a.f302a));
                case TransToPoint /*2*/:
                    a = m867a(context, geoPoint2);
                    this.mTo = new GeoPoint(CoreUtil.m484a(a.f303b), CoreUtil.m484a(a.f302a));
                case TransBothPoint /*3*/:
                    a = m867a(context, geoPoint);
                    this.mFrom = new GeoPoint(CoreUtil.m484a(a.f303b), CoreUtil.m484a(a.f302a));
                    a = m867a(context, geoPoint2);
                    this.mTo = new GeoPoint(CoreUtil.m484a(a.f303b), CoreUtil.m484a(a.f302a));
                default:
            }
        }

        private C0086b m867a(Context context, GeoPoint geoPoint) throws AMapException {
            return (C0086b) new LocTansServerHandler(new C0086b(CoreUtil.m481a(geoPoint.m462a()), CoreUtil.m481a(geoPoint.m464b())), CoreUtil.m491b(context), CoreUtil.m485a(context), null).m531j();
        }
    }

    /* renamed from: com.amap.mapapi.route.Route.d */
    public abstract class C0097d {
        final /* synthetic */ Route f733b;

        public abstract Paint m871a(int i);

        public abstract String m873a();

        protected abstract Drawable m879f(int i);

        public C0097d(Route route) {
            this.f733b = route;
        }

        public String m875b(int i) {
            return m870h(i);
        }

        private String m870h(int i) {
            if (i == 0) {
                return this.f733b.mStartPlace;
            }
            if (i == this.f733b.getStepCount()) {
                return this.f733b.mTargetPlace;
            }
            return null;
        }

        public Spanned m876c(int i) {
            String h = m870h(i);
            return h == null ? null : CoreUtil.m493c(CoreUtil.m486a(h, "#000000"));
        }

        public int m877d(int i) {
            if (i >= this.f733b.getStepCount()) {
                return -1;
            }
            return i + Route.BusSaveMoney;
        }

        public int m878e(int i) {
            if (i <= 0) {
                return -1;
            }
            return i - 1;
        }

        public View m872a(MapView mapView, Context context, RouteMessageHandler routeMessageHandler, RouteOverlay routeOverlay, int i) {
            return null;
        }

        public GeoPoint m880g(int i) {
            if (i == this.f733b.getStepCount()) {
                return this.f733b.getStep(i - 1).getLastPoint();
            }
            return this.f733b.getStep(i).getFirstPoint();
        }

        public View m874b(MapView mapView, Context context, RouteMessageHandler routeMessageHandler, RouteOverlay routeOverlay, int i) {
            if (i < 0 || i > this.f733b.getStepCount()) {
                return null;
            }
            View linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(Route.BusSaveMoney);
            linearLayout.setBackgroundColor(Color.argb(KEYRecord.PROTOCOL_ANY, KEYRecord.PROTOCOL_ANY, KEYRecord.PROTOCOL_ANY, KEYRecord.PROTOCOL_ANY));
            View linearLayout2 = new LinearLayout(context);
            linearLayout2.setOrientation(Route.BusDefault);
            linearLayout2.setBackgroundColor(-1);
            linearLayout2.setGravity(Route.BusLeaseChange);
            View imageView = new ImageView(context);
            imageView.setBackgroundColor(-1);
            imageView.setImageDrawable(m879f(i));
            imageView.setPadding(Route.BusLeaseWalk, Route.BusLeaseWalk, Route.BusDefault, Route.BusDefault);
            linearLayout2.addView(imageView, new LayoutParams(-2, -2));
            imageView = new TextView(context);
            imageView.setBackgroundColor(-1);
            String[] split = m876c(i).toString().split("\\n", Route.BusLeaseChange);
            imageView.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
            imageView.setText(CoreUtil.m493c(split[Route.BusDefault]));
            imageView.setPadding(Route.BusLeaseWalk, Route.BusDefault, Route.BusDefault, Route.BusLeaseWalk);
            linearLayout2.addView(imageView, new LayoutParams(-1, -2));
            imageView = new TextView(context);
            imageView.setBackgroundColor(Color.rgb(Opcodes.IF_ACMPEQ, Opcodes.IF_ACMPNE, Opcodes.IF_ACMPEQ));
            imageView.setLayoutParams(new LayoutParams(-1, Route.BusSaveMoney));
            View linearLayout3 = new LinearLayout(context);
            linearLayout3.setOrientation(Route.BusSaveMoney);
            linearLayout3.setBackgroundColor(-1);
            View textView = new TextView(context);
            textView.setBackgroundColor(-1);
            if (split.length == Route.BusLeaseChange) {
                linearLayout3.addView(imageView, new LayoutParams(-1, Route.BusSaveMoney));
                textView.setText(CoreUtil.m493c(split[Route.BusSaveMoney]));
                textView.setTextColor(Color.rgb(82, 85, 82));
                linearLayout3.addView(textView, new LayoutParams(-1, -2));
            }
            imageView = new LinearLayout(context);
            imageView.setOrientation(Route.BusDefault);
            imageView.setGravity(Route.BusSaveMoney);
            imageView.setBackgroundColor(-1);
            linearLayout.addView(linearLayout2, new LayoutParams(-1, -2));
            linearLayout.addView(linearLayout3, new LayoutParams(-1, Route.BusSaveMoney));
            linearLayout.addView(imageView, new LayoutParams(-1, -2));
            DisplayMetrics displayMetrics = new DisplayMetrics();
            displayMetrics = context.getApplicationContext().getResources().getDisplayMetrics();
            if (((long) (displayMetrics.heightPixels * displayMetrics.widthPixels)) <= 153600) {
                return linearLayout;
            }
            linearLayout2 = new TextView(context);
            linearLayout2.setText(XmlPullParser.NO_NAMESPACE);
            linearLayout2.setHeight(5);
            linearLayout2.setWidth(Route.BusSaveMoney);
            linearLayout.addView(linearLayout2);
            return linearLayout;
        }
    }

    /* renamed from: com.amap.mapapi.route.Route.a */
    public class C1038a extends C0097d {
        final /* synthetic */ Route f1936a;

        public C1038a(Route route) {
            this.f1936a = route;
            super(route);
        }

        public Paint m2016a(int i) {
            Paint paint = RouteResource.f383k;
            if (this.f1936a.getStep(i) instanceof BusSegment) {
                return RouteResource.f384l;
            }
            return paint;
        }

        public String m2018a() {
            int i;
            StringBuilder stringBuilder = new StringBuilder();
            int stepCount = this.f1936a.getStepCount();
            int i2 = Route.BusDefault;
            for (i = Route.BusSaveMoney; i < stepCount; i += Route.BusLeaseChange) {
                BusSegment busSegment = (BusSegment) this.f1936a.getStep(i);
                if (i != Route.BusSaveMoney) {
                    stringBuilder.append(" -> ");
                }
                stringBuilder.append(busSegment.getLineName());
                i2 += busSegment.getLength();
            }
            if (i2 != 0) {
                stringBuilder.append(SpecilApiUtil.LINE_SEP);
            }
            i = Route.BusDefault;
            for (int i3 = Route.BusDefault; i3 < stepCount; i3 += Route.BusLeaseChange) {
                i += this.f1936a.getStep(i3).getLength();
            }
            Object[] objArr = new Object[Route.BusMostComfortable];
            objArr[Route.BusDefault] = "\u4e58\u8f66";
            objArr[Route.BusSaveMoney] = Route.m882a(i2);
            objArr[Route.BusLeaseChange] = "\u6b65\u884c";
            objArr[Route.BusLeaseWalk] = Route.m882a(i);
            stringBuilder.append(String.format("%s%s  %s%s", objArr));
            return stringBuilder.toString();
        }

        public String m2019b(int i) {
            String b = super.m875b(i);
            if (b != null) {
                return b;
            }
            if (this.f1936a.getStep(i) instanceof BusSegment) {
                return m2013i(i);
            }
            return m2012h(i);
        }

        public Spanned m2020c(int i) {
            Spanned c = super.m876c(i);
            if (c != null) {
                return c;
            }
            if (this.f1936a.getStep(i) instanceof BusSegment) {
                return m2015k(i);
            }
            return m2014j(i);
        }

        private String m2012h(int i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\u6b65\u884c").append("\u53bb\u5f80");
            if (i == this.f1936a.getStepCount() - 1) {
                stringBuilder.append("\u76ee\u7684\u5730");
            } else {
                stringBuilder.append(((BusSegment) this.f1936a.getStep(i + Route.BusSaveMoney)).getLineName() + "\u8f66\u7ad9");
            }
            stringBuilder.append("\n\u5927\u7ea6" + Route.m882a(this.f1936a.getStep(i).getLength()));
            return stringBuilder.toString();
        }

        private String m2013i(int i) {
            BusSegment busSegment = (BusSegment) this.f1936a.getStep(i);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(String.format("%s ( %s -- %s ) - %s%s\n", new Object[]{busSegment.getLineName(), busSegment.getFirstStationName(), busSegment.getLastStationName(), busSegment.getLastStationName(), "\u65b9\u5411"}));
            stringBuffer.append("\u4e0a\u8f66 : " + busSegment.getOnStationName() + SpecilApiUtil.LINE_SEP);
            stringBuffer.append("\u4e0b\u8f66 : " + busSegment.getOffStationName() + SpecilApiUtil.LINE_SEP);
            Object[] objArr = new Object[Route.BusMostComfortable];
            objArr[Route.BusDefault] = "\u516c\u4ea4";
            objArr[Route.BusSaveMoney] = Integer.valueOf(busSegment.getStopNumber() - 1);
            objArr[Route.BusLeaseChange] = "\u7ad9";
            objArr[Route.BusLeaseWalk] = "\u5927\u7ea6" + Route.m882a(busSegment.getLength());
            stringBuffer.append(String.format("%s%d%s (%s)", objArr));
            return stringBuffer.toString();
        }

        private Spanned m2014j(int i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\u6b65\u884c").append("\u53bb\u5f80");
            if (i == this.f1936a.getStepCount() - 1) {
                stringBuilder.append(CoreUtil.m486a("\u76ee\u7684\u5730", "#808080"));
            } else {
                stringBuilder.append(CoreUtil.m486a(((BusSegment) this.f1936a.getStep(i + Route.BusSaveMoney)).getLineName() + "\u8f66\u7ad9", "#808080"));
            }
            stringBuilder.append(CoreUtil.m494c());
            stringBuilder.append("\u5927\u7ea6" + Route.m882a(this.f1936a.getStep(i).getLength()));
            return CoreUtil.m493c(stringBuilder.toString());
        }

        private Spanned m2015k(int i) {
            BusSegment busSegment = (BusSegment) this.f1936a.getStep(i);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(CoreUtil.m486a(busSegment.getLineName(), "#000000"));
            stringBuffer.append(CoreUtil.m495c((int) Route.BusLeaseWalk));
            stringBuffer.append(CoreUtil.m486a(busSegment.getLastStationName(), "#000000"));
            stringBuffer.append("\u65b9\u5411");
            stringBuffer.append(CoreUtil.m494c());
            stringBuffer.append("\u4e0a\u8f66 : ");
            stringBuffer.append(CoreUtil.m486a(busSegment.getOnStationName(), "#000000"));
            stringBuffer.append(CoreUtil.m495c((int) Route.BusLeaseWalk));
            stringBuffer.append(CoreUtil.m494c());
            stringBuffer.append("\u4e0b\u8f66 : ");
            stringBuffer.append(CoreUtil.m486a(busSegment.getOffStationName(), "#000000"));
            stringBuffer.append(CoreUtil.m494c());
            Object[] objArr = new Object[Route.BusLeaseWalk];
            objArr[Route.BusDefault] = "\u516c\u4ea4";
            objArr[Route.BusSaveMoney] = Integer.valueOf(busSegment.getStopNumber() - 1);
            objArr[Route.BusLeaseChange] = "\u7ad9";
            stringBuffer.append(String.format("%s%d%s , ", objArr));
            stringBuffer.append("\u5927\u7ea6" + Route.m882a(busSegment.getLength()));
            return CoreUtil.m493c(stringBuffer.toString());
        }

        public int m2021d(int i) {
            do {
                i += Route.BusSaveMoney;
                if (i >= this.f1936a.getStepCount() - 1) {
                    break;
                }
            } while (!(this.f1936a.getStep(i) instanceof BusSegment));
            return i;
        }

        public int m2022e(int i) {
            if (i == this.f1936a.getStepCount()) {
                return i - 1;
            }
            while (true) {
                int i2 = i - 1;
                if (i2 <= 0) {
                    return i2;
                }
                if (this.f1936a.getStep(i2) instanceof BusSegment) {
                    return i2;
                }
                i = i2;
            }
        }

        public View m2017a(MapView mapView, Context context, RouteMessageHandler routeMessageHandler, RouteOverlay routeOverlay, int i) {
            Drawable f = m2023f(i);
            if (i == 0 || i == this.f1936a.getStepCount()) {
                return null;
            }
            CharSequence lineName;
            Segment step = this.f1936a.getStep(i);
            if (step instanceof BusSegment) {
                lineName = ((BusSegment) step).getLineName();
            } else {
                lineName = null;
            }
            if (lineName == null && f == null) {
                return null;
            }
            View linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(Route.BusDefault);
            View imageView = new ImageView(context);
            imageView.setImageDrawable(f);
            imageView.setPadding(Route.BusLeaseWalk, Route.BusLeaseWalk, Route.BusSaveMoney, 5);
            linearLayout.addView(imageView, new LayoutParams(-2, -2));
            if (lineName != null) {
                imageView = new TextView(context);
                imageView.setText(lineName);
                imageView.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
                imageView.setPadding(Route.BusLeaseWalk, Route.BusDefault, Route.BusLeaseWalk, Route.BusLeaseWalk);
                linearLayout.addView(imageView, new LayoutParams(-2, -2));
            }
            new Route(mapView, routeMessageHandler, routeOverlay, i, Route.BusMostComfortable).m893a(linearLayout);
            return linearLayout;
        }

        protected Drawable m2023f(int i) {
            if (i == this.f1936a.getStepCount() - 1) {
                return RouteResource.f375c;
            }
            if (i < this.f1936a.getStepCount() && (this.f1936a.getStep(i) instanceof BusSegment)) {
                return RouteResource.f377e;
            }
            if (i == 0) {
                return RouteResource.f378f;
            }
            if (i == this.f1936a.getStepCount()) {
                return RouteResource.f379g;
            }
            return null;
        }
    }

    /* renamed from: com.amap.mapapi.route.Route.e */
    abstract class C1039e extends C0097d {
        final /* synthetic */ Route f1937c;

        C1039e(Route route) {
            this.f1937c = route;
            super(route);
        }

        public Paint m2024a(int i) {
            return RouteResource.f385m;
        }

        public String m2025a() {
            StringBuffer stringBuffer = new StringBuffer();
            String str = XmlPullParser.NO_NAMESPACE;
            int stepCount = this.f1937c.getStepCount();
            int i = Route.BusDefault;
            int i2 = Route.BusDefault;
            while (i < stepCount) {
                String str2;
                DriveWalkSegment driveWalkSegment = (DriveWalkSegment) this.f1937c.getStep(i);
                i2 += driveWalkSegment.getLength();
                if (CoreUtil.m488a(driveWalkSegment.getRoadName()) || driveWalkSegment.getRoadName().equals(str)) {
                    str2 = str;
                } else {
                    if (!CoreUtil.m488a(stringBuffer.toString())) {
                        stringBuffer.append(" -> ");
                    }
                    stringBuffer.append(driveWalkSegment.getRoadName());
                    str2 = driveWalkSegment.getRoadName();
                }
                i += Route.BusSaveMoney;
                str = str2;
            }
            if (!CoreUtil.m488a(stringBuffer.toString())) {
                stringBuffer.append(SpecilApiUtil.LINE_SEP);
            }
            Object[] objArr = new Object[Route.BusSaveMoney];
            objArr[Route.BusDefault] = "\u5927\u7ea6" + Route.m882a(i2);
            stringBuffer.append(String.format("%s", objArr));
            return stringBuffer.toString();
        }

        public String m2026b(int i) {
            String b = super.m875b(i);
            if (b != null) {
                return b;
            }
            String str = XmlPullParser.NO_NAMESPACE;
            DriveWalkSegment driveWalkSegment = (DriveWalkSegment) this.f1937c.getStep(i);
            if (!CoreUtil.m488a(driveWalkSegment.getRoadName())) {
                str = driveWalkSegment.getRoadName() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR;
            }
            StringBuilder append = new StringBuilder().append(str + driveWalkSegment.getActionDescription() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            Object[] objArr = new Object[Route.BusLeaseChange];
            objArr[Route.BusDefault] = "\u5927\u7ea6";
            objArr[Route.BusSaveMoney] = Route.m882a(driveWalkSegment.getLength());
            return append.append(String.format("%s%s", objArr)).toString();
        }

        public Spanned m2027c(int i) {
            Spanned c = super.m876c(i);
            if (c != null) {
                return c;
            }
            String str;
            String str2 = XmlPullParser.NO_NAMESPACE;
            DriveWalkSegment driveWalkSegment = (DriveWalkSegment) this.f1937c.getStep(i);
            if (CoreUtil.m488a(driveWalkSegment.getRoadName()) || CoreUtil.m488a(driveWalkSegment.getActionDescription())) {
                str = driveWalkSegment.getActionDescription() + driveWalkSegment.getRoadName();
            } else {
                str = driveWalkSegment.getActionDescription() + " --> " + driveWalkSegment.getRoadName();
            }
            StringBuilder append = new StringBuilder().append(CoreUtil.m486a(str, "#808080") + CoreUtil.m494c());
            Object[] objArr = new Object[Route.BusLeaseChange];
            objArr[Route.BusDefault] = "\u5927\u7ea6";
            objArr[Route.BusSaveMoney] = Route.m882a(driveWalkSegment.getLength());
            return CoreUtil.m493c(append.append(String.format("%s%s", objArr)).toString());
        }
    }

    /* renamed from: com.amap.mapapi.route.Route.b */
    class C1266b extends C1039e {
        final /* synthetic */ Route f2373a;

        C1266b(Route route) {
            this.f2373a = route;
            super(route);
        }

        protected Drawable m2544f(int i) {
            return RouteResource.f376d;
        }
    }

    /* renamed from: com.amap.mapapi.route.Route.c */
    class C1267c extends C1039e {
        final /* synthetic */ Route f2374a;

        C1267c(Route route) {
            this.f2374a = route;
            super(route);
        }

        public Paint m2545a(int i) {
            return RouteResource.f383k;
        }

        protected Drawable m2546f(int i) {
            return RouteResource.f375c;
        }
    }

    public int getMode() {
        return this.f736c;
    }

    public int getLength() {
        int i = BusDefault;
        for (Segment length : this.mSegs) {
            i = length.getLength() + i;
        }
        return i;
    }

    private void m883b() {
        int longitudeE6;
        int i = Integer.MIN_VALUE;
        int i2 = Integer.MAX_VALUE;
        int i3 = Integer.MAX_VALUE;
        for (Segment lowerLeftPoint : this.mSegs) {
            GeoPoint lowerLeftPoint2 = lowerLeftPoint.getLowerLeftPoint();
            longitudeE6 = lowerLeftPoint2.getLongitudeE6();
            int latitudeE6 = lowerLeftPoint2.getLatitudeE6();
            if (longitudeE6 < i3) {
                i3 = longitudeE6;
            }
            if (latitudeE6 >= i2) {
                latitudeE6 = i2;
            }
            i2 = latitudeE6;
        }
        longitudeE6 = Integer.MIN_VALUE;
        for (Segment lowerLeftPoint3 : this.mSegs) {
            lowerLeftPoint2 = lowerLeftPoint3.getUpperRightPoint();
            int longitudeE62 = lowerLeftPoint2.getLongitudeE6();
            latitudeE6 = lowerLeftPoint2.getLatitudeE6();
            if (longitudeE62 > longitudeE6) {
                longitudeE6 = longitudeE62;
            }
            if (latitudeE6 <= i) {
                latitudeE6 = i;
            }
            i = latitudeE6;
        }
        this.f734a = new GeoPoint(i2, i3);
        this.f735b = new GeoPoint(i, longitudeE6);
    }

    public GeoPoint getLowerLeftPoint() {
        if (this.f734a == null) {
            m883b();
        }
        return this.f734a;
    }

    public GeoPoint getUpperRightPoint() {
        if (this.f735b == null) {
            m883b();
        }
        return this.f735b;
    }

    public static List<Route> calculateRoute(Context context, FromAndTo fromAndTo, int i) throws AMapException {
        RouteHandler busHandler;
        ClientInfoUtil.m471a(context);
        RouteParam routeParam = new RouteParam(fromAndTo, i);
        Proxy b = CoreUtil.m491b(context);
        String a = CoreUtil.m485a(context);
        fromAndTo.m868a(context, fromAndTo.mFrom, fromAndTo.mTo, fromAndTo.mTrans);
        double a2 = CoreUtil.m481a(fromAndTo.mFrom.m462a());
        double a3 = CoreUtil.m481a(fromAndTo.mFrom.m464b());
        if (isBus(i)) {
            String a4 = m881a(a2, a3, b, a);
            if (a4 == null || a4.equals(XmlPullParser.NO_NAMESPACE)) {
                return new ArrayList();
            }
            routeParam.m895a(a4);
            busHandler = new BusHandler(routeParam, b, a, null);
        } else if (isWalk(i)) {
            busHandler = new WalkHandler(routeParam, b, a, null);
        } else {
            busHandler = new DriveHandler(routeParam, b, a, null);
        }
        return (List) busHandler.m531j();
    }

    private static String m881a(double d, double d2, Proxy proxy, String str) throws AMapException {
        String str2 = XmlPullParser.NO_NAMESPACE;
        List list = (List) new ReverseGeocodingHandler(new ReverseGeocodingParam(d, d2, BusSaveMoney, false), proxy, str, null).m531j();
        if (list.size() <= 0) {
            return str2;
        }
        Address address = (Address) list.get(BusDefault);
        String locality = address.getLocality();
        if (locality == null || locality.equals(XmlPullParser.NO_NAMESPACE)) {
            return address.getAdminArea();
        }
        return locality;
    }

    public String getStartPlace() {
        return this.mStartPlace;
    }

    public void setStartPlace(String str) {
        this.mStartPlace = str;
    }

    public String getTargetPlace() {
        return this.mTargetPlace;
    }

    public void setTargetPlace(String str) {
        this.mTargetPlace = str;
    }

    List<Segment> m884a() {
        return this.mSegs;
    }

    void m885a(List<Segment> list) {
        this.mSegs = list;
    }

    public GeoPoint getStartPos() {
        return ((Segment) this.mSegs.get(BusDefault)).getFirstPoint();
    }

    public GeoPoint getTargetPos() {
        return ((Segment) this.mSegs.get(getStepCount() - 1)).getLastPoint();
    }

    public int getStepCount() {
        return this.mSegs.size();
    }

    public Segment getStep(int i) {
        return (Segment) this.mSegs.get(i);
    }

    public int getSegmentIndex(Segment segment) {
        return this.mSegs.indexOf(segment);
    }

    public String getStepedDescription(int i) {
        return this.mHelper.m875b(i);
    }

    public String getOverview() {
        return this.mHelper.m873a();
    }

    public Route(int i) {
        this.f734a = null;
        this.f735b = null;
        this.f736c = i;
        if (isBus(i)) {
            this.mHelper = new C1038a(this);
        } else if (isDrive(i)) {
            this.mHelper = new C1266b(this);
        } else if (isWalk(i)) {
            this.mHelper = new C1267c(this);
        } else {
            throw new IllegalArgumentException("Unkown mode");
        }
    }

    public static boolean isDrive(int i) {
        return i >= DrivingDefault && i <= DrivingNoFastRoad;
    }

    public static boolean isBus(int i) {
        return i >= 0 && i <= BusMostComfortable;
    }

    public static boolean isWalk(int i) {
        return false;
    }

    static String m882a(int i) {
        if (i > XStream.PRIORITY_VERY_HIGH) {
            return (i / 1000) + "\u516c\u91cc";
        } else if (i > 1000) {
            return new DecimalFormat("##0.0").format((double) (((float) i) / 1000.0f)) + "\u516c\u91cc";
        } else if (i > 100) {
            return ((i / 50) * 50) + "\u7c73";
        } else {
            int i2 = (i / DrivingDefault) * DrivingDefault;
            if (i2 == 0) {
                i2 = DrivingDefault;
            }
            return i2 + "\u7c73";
        }
    }
}

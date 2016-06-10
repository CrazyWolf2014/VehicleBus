package com.amap.mapapi.map;

import android.view.MotionEvent;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import org.xbill.DNS.KEYRecord;

/* compiled from: TrackballGestureDetector */
public class aw {
    private static aw f645n;
    private boolean f646a;
    private float f647b;
    private float f648c;
    private long f649d;
    private boolean f650e;
    private boolean f651f;
    private boolean f652g;
    private float f653h;
    private float f654i;
    private float f655j;
    private float f656k;
    private Runnable f657l;
    private Thread f658m;

    /* renamed from: com.amap.mapapi.map.aw.a */
    public interface TrackballGestureDetector {
        void m804a(aw awVar);
    }

    private aw() {
    }

    public static aw m805a() {
        if (f645n == null) {
            f645n = new aw();
        }
        return f645n;
    }

    private void m806b() {
        if (this.f657l != null && this.f650e && this.f658m == null) {
            this.f658m = new Thread(this.f657l);
            this.f658m.start();
        }
    }

    public void m807a(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        float y = motionEvent.getY();
        float x = motionEvent.getX();
        this.f651f = false;
        this.f652g = false;
        switch (action) {
            case KEYRecord.OWNER_USER /*0*/:
                this.f653h = x;
                this.f654i = y;
                this.f647b = x;
                this.f648c = y;
                this.f649d = motionEvent.getDownTime();
                this.f646a = true;
                this.f650e = false;
                break;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                if (this.f646a && motionEvent.getEventTime() - this.f649d < 300) {
                    this.f652g = true;
                    break;
                }
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                if (!this.f650e) {
                    this.f655j = this.f653h - x;
                    this.f656k = this.f654i - y;
                    this.f653h = x;
                    this.f654i = y;
                    if (Math.abs(y - this.f648c) + Math.abs(x - this.f647b) > 0.0f) {
                        this.f646a = false;
                        this.f651f = true;
                        break;
                    }
                }
                break;
            default:
                return;
        }
        m806b();
    }
}

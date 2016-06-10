package com.cnlaunch.x431pro.widget.polites;

import android.graphics.PointF;
import android.util.FloatMath;
import android.view.MotionEvent;

public class VectorF {
    public float angle;
    public final PointF end;
    public float length;
    public final PointF start;

    public VectorF() {
        this.start = new PointF();
        this.end = new PointF();
    }

    public void calculateEndPoint() {
        this.end.x = (FloatMath.cos(this.angle) * this.length) + this.start.x;
        this.end.y = (FloatMath.sin(this.angle) * this.length) + this.start.y;
    }

    public void setStart(PointF p) {
        this.start.x = p.x;
        this.start.y = p.y;
    }

    public void setEnd(PointF p) {
        this.end.x = p.x;
        this.end.y = p.y;
    }

    public void set(MotionEvent event) {
        this.start.x = event.getX(0);
        this.start.y = event.getY(0);
        this.end.x = event.getX(1);
        this.end.y = event.getY(1);
    }

    public float calculateLength() {
        this.length = MathUtils.distance(this.start, this.end);
        return this.length;
    }

    public float calculateAngle() {
        this.angle = MathUtils.angle(this.start, this.end);
        return this.angle;
    }
}

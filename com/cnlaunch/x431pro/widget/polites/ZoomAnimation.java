package com.cnlaunch.x431pro.widget.polites;

import android.graphics.PointF;

public class ZoomAnimation implements Animation {
    private long animationLengthMS;
    private boolean firstFrame;
    private float scaleDiff;
    private float startScale;
    private float startX;
    private float startY;
    private long totalTime;
    private float touchX;
    private float touchY;
    private float xDiff;
    private float yDiff;
    private float zoom;
    private ZoomAnimationListener zoomAnimationListener;

    public ZoomAnimation() {
        this.firstFrame = true;
        this.animationLengthMS = 200;
        this.totalTime = 0;
    }

    public boolean update(GestureImageView view, long time) {
        if (this.firstFrame) {
            this.firstFrame = false;
            this.startX = view.getImageX();
            this.startY = view.getImageY();
            this.startScale = view.getScale();
            this.scaleDiff = (this.zoom * this.startScale) - this.startScale;
            if (this.scaleDiff > 0.0f) {
                VectorF vector = new VectorF();
                vector.setStart(new PointF(this.touchX, this.touchY));
                vector.setEnd(new PointF(this.startX, this.startY));
                vector.calculateAngle();
                vector.length = this.zoom * vector.calculateLength();
                vector.calculateEndPoint();
                this.xDiff = vector.end.x - this.startX;
                this.yDiff = vector.end.y - this.startY;
            } else {
                this.xDiff = view.getCenterX() - this.startX;
                this.yDiff = view.getCenterY() - this.startY;
            }
        }
        this.totalTime += time;
        float ratio = ((float) this.totalTime) / ((float) this.animationLengthMS);
        float newScale;
        float newX;
        float newY;
        if (ratio < 1.0f) {
            if (ratio > 0.0f) {
                newScale = (this.scaleDiff * ratio) + this.startScale;
                newX = (this.xDiff * ratio) + this.startX;
                newY = (this.yDiff * ratio) + this.startY;
                if (this.zoomAnimationListener != null) {
                    this.zoomAnimationListener.onZoom(newScale, newX, newY);
                }
            }
            return true;
        }
        newScale = this.scaleDiff + this.startScale;
        newX = this.xDiff + this.startX;
        newY = this.yDiff + this.startY;
        if (this.zoomAnimationListener == null) {
            return false;
        }
        this.zoomAnimationListener.onZoom(newScale, newX, newY);
        this.zoomAnimationListener.onComplete();
        return false;
    }

    public void reset() {
        this.firstFrame = true;
        this.totalTime = 0;
    }

    public float getZoom() {
        return this.zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public float getTouchX() {
        return this.touchX;
    }

    public void setTouchX(float touchX) {
        this.touchX = touchX;
    }

    public float getTouchY() {
        return this.touchY;
    }

    public void setTouchY(float touchY) {
        this.touchY = touchY;
    }

    public long getAnimationLengthMS() {
        return this.animationLengthMS;
    }

    public void setAnimationLengthMS(long animationLengthMS) {
        this.animationLengthMS = animationLengthMS;
    }

    public ZoomAnimationListener getZoomAnimationListener() {
        return this.zoomAnimationListener;
    }

    public void setZoomAnimationListener(ZoomAnimationListener zoomAnimationListener) {
        this.zoomAnimationListener = zoomAnimationListener;
    }
}

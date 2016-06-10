package org.achartengine.chart;

import android.graphics.RectF;

public class ClickableArea {
    private RectF rect;
    private double f1668x;
    private double f1669y;

    public ClickableArea(RectF rect, double x, double y) {
        this.rect = rect;
        this.f1668x = x;
        this.f1669y = y;
    }

    public RectF getRect() {
        return this.rect;
    }

    public double getX() {
        return this.f1668x;
    }

    public double getY() {
        return this.f1669y;
    }
}

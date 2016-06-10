package com.ifoer.entity;

import android.util.Log;

public class ItemData {
    private double value;
    private double f1229x;
    private double f1230y;

    public double getValue() {
        Log.i("ItemData", "\u83b7\u53d6\u503c" + this.value);
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getX() {
        return this.f1229x;
    }

    public void setX(double x) {
        this.f1229x = x;
    }

    public double getY() {
        return this.f1230y;
    }

    public void setY(double y) {
        this.f1230y = y;
    }
}

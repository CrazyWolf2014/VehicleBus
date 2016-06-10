package org.achartengine.renderer;

import android.graphics.Color;
import android.graphics.Paint.Align;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.googlecode.leptonica.android.Skew;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.achartengine.chart.PointStyle;
import org.xbill.DNS.WKSRecord.Service;

public class XYSeriesRenderer extends SimpleSeriesRenderer {
    private int mAnnotationsColor;
    private Align mAnnotationsTextAlign;
    private float mAnnotationsTextSize;
    private float mChartValuesSpacing;
    private Align mChartValuesTextAlign;
    private float mChartValuesTextSize;
    private boolean mDisplayChartValues;
    private int mDisplayChartValuesDistance;
    private List<FillOutsideLine> mFillBelowLine;
    private boolean mFillPoints;
    private float mLineWidth;
    private float mPointStrokeWidth;
    private PointStyle mPointStyle;

    public static class FillOutsideLine implements Serializable {
        private int mColor;
        private int[] mFillRange;
        private final Type mType;

        public enum Type {
            NONE,
            BOUNDS_ALL,
            BOUNDS_BELOW,
            BOUNDS_ABOVE,
            BELOW,
            ABOVE
        }

        public FillOutsideLine(Type type) {
            this.mColor = Color.argb(Service.LOCUS_MAP, 0, 0, AsyncTaskManager.REQUEST_SUCCESS_CODE);
            this.mType = type;
        }

        public int getColor() {
            return this.mColor;
        }

        public void setColor(int color) {
            this.mColor = color;
        }

        public Type getType() {
            return this.mType;
        }

        public int[] getFillRange() {
            return this.mFillRange;
        }

        public void setFillRange(int[] range) {
            this.mFillRange = range;
        }
    }

    public XYSeriesRenderer() {
        this.mFillPoints = false;
        this.mFillBelowLine = new ArrayList();
        this.mPointStyle = PointStyle.POINT;
        this.mPointStrokeWidth = 1.0f;
        this.mLineWidth = 1.0f;
        this.mDisplayChartValuesDistance = 100;
        this.mChartValuesTextSize = 10.0f;
        this.mChartValuesTextAlign = Align.CENTER;
        this.mChartValuesSpacing = Skew.SWEEP_DELTA;
        this.mAnnotationsTextSize = 10.0f;
        this.mAnnotationsTextAlign = Align.CENTER;
        this.mAnnotationsColor = DefaultRenderer.TEXT_COLOR;
    }

    @Deprecated
    public boolean isFillBelowLine() {
        return this.mFillBelowLine.size() > 0;
    }

    @Deprecated
    public void setFillBelowLine(boolean fill) {
        this.mFillBelowLine.clear();
        if (fill) {
            this.mFillBelowLine.add(new FillOutsideLine(Type.BOUNDS_ALL));
        } else {
            this.mFillBelowLine.add(new FillOutsideLine(Type.NONE));
        }
    }

    public FillOutsideLine[] getFillOutsideLine() {
        return (FillOutsideLine[]) this.mFillBelowLine.toArray(new FillOutsideLine[0]);
    }

    public void addFillOutsideLine(FillOutsideLine fill) {
        this.mFillBelowLine.add(fill);
    }

    public boolean isFillPoints() {
        return this.mFillPoints;
    }

    public void setFillPoints(boolean fill) {
        this.mFillPoints = fill;
    }

    @Deprecated
    public void setFillBelowLineColor(int color) {
        if (this.mFillBelowLine.size() > 0) {
            ((FillOutsideLine) this.mFillBelowLine.get(0)).setColor(color);
        }
    }

    public PointStyle getPointStyle() {
        return this.mPointStyle;
    }

    public void setPointStyle(PointStyle style) {
        this.mPointStyle = style;
    }

    public float getPointStrokeWidth() {
        return this.mPointStrokeWidth;
    }

    public void setPointStrokeWidth(float strokeWidth) {
        this.mPointStrokeWidth = strokeWidth;
    }

    public float getLineWidth() {
        return this.mLineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.mLineWidth = lineWidth;
    }

    public boolean isDisplayChartValues() {
        return this.mDisplayChartValues;
    }

    public void setDisplayChartValues(boolean display) {
        this.mDisplayChartValues = display;
    }

    public int getDisplayChartValuesDistance() {
        return this.mDisplayChartValuesDistance;
    }

    public void setDisplayChartValuesDistance(int distance) {
        this.mDisplayChartValuesDistance = distance;
    }

    public float getChartValuesTextSize() {
        return this.mChartValuesTextSize;
    }

    public void setChartValuesTextSize(float textSize) {
        this.mChartValuesTextSize = textSize;
    }

    public Align getChartValuesTextAlign() {
        return this.mChartValuesTextAlign;
    }

    public void setChartValuesTextAlign(Align align) {
        this.mChartValuesTextAlign = align;
    }

    public float getChartValuesSpacing() {
        return this.mChartValuesSpacing;
    }

    public void setChartValuesSpacing(float spacing) {
        this.mChartValuesSpacing = spacing;
    }

    public float getAnnotationsTextSize() {
        return this.mAnnotationsTextSize;
    }

    public void setAnnotationsTextSize(float textSize) {
        this.mAnnotationsTextSize = textSize;
    }

    public Align getAnnotationsTextAlign() {
        return this.mAnnotationsTextAlign;
    }

    public void setAnnotationsTextAlign(Align align) {
        this.mAnnotationsTextAlign = align;
    }

    public int getAnnotationsColor() {
        return this.mAnnotationsColor;
    }

    public void setAnnotationsColor(int color) {
        this.mAnnotationsColor = color;
    }
}

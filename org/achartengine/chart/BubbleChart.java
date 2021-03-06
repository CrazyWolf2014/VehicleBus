package org.achartengine.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import java.util.List;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYValueSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class BubbleChart extends XYChart {
    private static final int MAX_BUBBLE_SIZE = 20;
    private static final int MIN_BUBBLE_SIZE = 2;
    private static final int SHAPE_WIDTH = 10;
    public static final String TYPE = "Bubble";

    BubbleChart() {
    }

    public BubbleChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
        super(dataset, renderer);
    }

    public void drawSeries(Canvas canvas, Paint paint, List<Float> points, XYSeriesRenderer renderer, float yAxisValue, int seriesIndex, int startIndex) {
        paint.setColor(renderer.getColor());
        paint.setStyle(Style.FILL);
        int length = points.size();
        XYValueSeries series = (XYValueSeries) this.mDataset.getSeriesAt(seriesIndex);
        double coef = 20.0d / series.getMaxValue();
        for (int i = 0; i < length; i += MIN_BUBBLE_SIZE) {
            List<Float> list = points;
            Canvas canvas2 = canvas;
            Paint paint2 = paint;
            drawCircle(canvas2, paint2, ((Float) points.get(i)).floatValue(), ((Float) list.get(i + 1)).floatValue(), (float) ((series.getValue((i / MIN_BUBBLE_SIZE) + startIndex) * coef) + 2.0d));
        }
    }

    protected ClickableArea[] clickableAreasForPoints(List<Float> points, List<Double> values, float yAxisValue, int seriesIndex, int startIndex) {
        int length = points.size();
        XYValueSeries series = (XYValueSeries) this.mDataset.getSeriesAt(seriesIndex);
        double coef = 20.0d / series.getMaxValue();
        ClickableArea[] ret = new ClickableArea[(length / MIN_BUBBLE_SIZE)];
        for (int i = 0; i < length; i += MIN_BUBBLE_SIZE) {
            double size = (series.getValue((i / MIN_BUBBLE_SIZE) + startIndex) * coef) + 2.0d;
            float f = (float) size;
            ret[i / MIN_BUBBLE_SIZE] = new ClickableArea(new RectF(((Float) points.get(i)).floatValue() - ((float) size), ((Float) points.get(i + 1)).floatValue() - ((float) size), ((float) size) + ((Float) points.get(i)).floatValue(), ((Float) points.get(i + 1)).floatValue() + r0), ((Double) values.get(i)).doubleValue(), ((Double) values.get(i + 1)).doubleValue());
        }
        return ret;
    }

    public int getLegendShapeWidth(int seriesIndex) {
        return SHAPE_WIDTH;
    }

    public void drawLegendShape(Canvas canvas, SimpleSeriesRenderer renderer, float x, float y, int seriesIndex, Paint paint) {
        paint.setStyle(Style.FILL);
        drawCircle(canvas, paint, x + 10.0f, y, 3.0f);
    }

    private void drawCircle(Canvas canvas, Paint paint, float x, float y, float radius) {
        canvas.drawCircle(x, y, radius, paint);
    }

    public String getChartType() {
        return TYPE;
    }
}

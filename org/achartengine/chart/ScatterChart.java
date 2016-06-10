package org.achartengine.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import java.util.List;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class ScatterChart extends XYChart {
    private static /* synthetic */ int[] $SWITCH_TABLE$org$achartengine$chart$PointStyle = null;
    private static final int SHAPE_WIDTH = 10;
    private static final float SIZE = 3.0f;
    public static final String TYPE = "Scatter";
    private float size;

    static /* synthetic */ int[] $SWITCH_TABLE$org$achartengine$chart$PointStyle() {
        int[] iArr = $SWITCH_TABLE$org$achartengine$chart$PointStyle;
        if (iArr == null) {
            iArr = new int[PointStyle.values().length];
            try {
                iArr[PointStyle.CIRCLE.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[PointStyle.DIAMOND.ordinal()] = 5;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[PointStyle.POINT.ordinal()] = 6;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[PointStyle.SQUARE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[PointStyle.TRIANGLE.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[PointStyle.X.ordinal()] = 1;
            } catch (NoSuchFieldError e6) {
            }
            $SWITCH_TABLE$org$achartengine$chart$PointStyle = iArr;
        }
        return iArr;
    }

    ScatterChart() {
        this.size = SIZE;
    }

    public ScatterChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
        super(dataset, renderer);
        this.size = SIZE;
        this.size = renderer.getPointSize();
    }

    protected void setDatasetRenderer(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
        super.setDatasetRenderer(dataset, renderer);
        this.size = renderer.getPointSize();
    }

    public void drawSeries(Canvas canvas, Paint paint, List<Float> points, XYSeriesRenderer renderer, float yAxisValue, int seriesIndex, int startIndex) {
        paint.setColor(renderer.getColor());
        float stroke = paint.getStrokeWidth();
        if (renderer.isFillPoints()) {
            paint.setStyle(Style.FILL);
        } else {
            paint.setStrokeWidth(renderer.getPointStrokeWidth());
            paint.setStyle(Style.STROKE);
        }
        int length = points.size();
        int i;
        float[] path;
        switch ($SWITCH_TABLE$org$achartengine$chart$PointStyle()[renderer.getPointStyle().ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                paint.setStrokeWidth(renderer.getPointStrokeWidth());
                for (i = 0; i < length; i += 2) {
                    drawX(canvas, paint, ((Float) points.get(i)).floatValue(), ((Float) points.get(i + 1)).floatValue());
                }
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                for (i = 0; i < length; i += 2) {
                    drawCircle(canvas, paint, ((Float) points.get(i)).floatValue(), ((Float) points.get(i + 1)).floatValue());
                }
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                path = new float[6];
                for (i = 0; i < length; i += 2) {
                    drawTriangle(canvas, paint, path, ((Float) points.get(i)).floatValue(), ((Float) points.get(i + 1)).floatValue());
                }
                break;
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                for (i = 0; i < length; i += 2) {
                    drawSquare(canvas, paint, ((Float) points.get(i)).floatValue(), ((Float) points.get(i + 1)).floatValue());
                }
                break;
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                path = new float[8];
                for (i = 0; i < length; i += 2) {
                    drawDiamond(canvas, paint, path, ((Float) points.get(i)).floatValue(), ((Float) points.get(i + 1)).floatValue());
                }
                break;
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                for (i = 0; i < length; i += 2) {
                    canvas.drawPoint(((Float) points.get(i)).floatValue(), ((Float) points.get(i + 1)).floatValue(), paint);
                }
                break;
        }
        paint.setStrokeWidth(stroke);
    }

    protected ClickableArea[] clickableAreasForPoints(List<Float> points, List<Double> values, float yAxisValue, int seriesIndex, int startIndex) {
        int length = points.size();
        ClickableArea[] ret = new ClickableArea[(length / 2)];
        for (int i = 0; i < length; i += 2) {
            int selectableBuffer = this.mRenderer.getSelectableBuffer();
            ret[i / 2] = new ClickableArea(new RectF(((Float) points.get(i)).floatValue() - ((float) selectableBuffer), ((Float) points.get(i + 1)).floatValue() - ((float) selectableBuffer), ((float) selectableBuffer) + ((Float) points.get(i)).floatValue(), ((Float) points.get(i + 1)).floatValue() + ((float) selectableBuffer)), ((Double) values.get(i)).doubleValue(), ((Double) values.get(i + 1)).doubleValue());
        }
        return ret;
    }

    public int getLegendShapeWidth(int seriesIndex) {
        return SHAPE_WIDTH;
    }

    public void drawLegendShape(Canvas canvas, SimpleSeriesRenderer renderer, float x, float y, int seriesIndex, Paint paint) {
        if (((XYSeriesRenderer) renderer).isFillPoints()) {
            paint.setStyle(Style.FILL);
        } else {
            paint.setStyle(Style.STROKE);
        }
        switch ($SWITCH_TABLE$org$achartengine$chart$PointStyle()[((XYSeriesRenderer) renderer).getPointStyle().ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                drawX(canvas, paint, x + 10.0f, y);
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                drawCircle(canvas, paint, x + 10.0f, y);
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                drawTriangle(canvas, paint, new float[6], x + 10.0f, y);
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                drawSquare(canvas, paint, x + 10.0f, y);
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                drawDiamond(canvas, paint, new float[8], x + 10.0f, y);
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                canvas.drawPoint(x + 10.0f, y, paint);
            default:
        }
    }

    private void drawX(Canvas canvas, Paint paint, float x, float y) {
        canvas.drawLine(x - this.size, y - this.size, x + this.size, y + this.size, paint);
        canvas.drawLine(x + this.size, y - this.size, x - this.size, y + this.size, paint);
    }

    private void drawCircle(Canvas canvas, Paint paint, float x, float y) {
        canvas.drawCircle(x, y, this.size, paint);
    }

    private void drawTriangle(Canvas canvas, Paint paint, float[] path, float x, float y) {
        path[0] = x;
        path[1] = (y - this.size) - (this.size / 2.0f);
        path[2] = x - this.size;
        path[3] = this.size + y;
        path[4] = this.size + x;
        path[5] = path[3];
        drawPath(canvas, path, paint, true);
    }

    private void drawSquare(Canvas canvas, Paint paint, float x, float y) {
        canvas.drawRect(x - this.size, y - this.size, x + this.size, y + this.size, paint);
    }

    private void drawDiamond(Canvas canvas, Paint paint, float[] path, float x, float y) {
        path[0] = x;
        path[1] = y - this.size;
        path[2] = x - this.size;
        path[3] = y;
        path[4] = x;
        path[5] = this.size + y;
        path[6] = this.size + x;
        path[7] = y;
        drawPath(canvas, path, paint, true);
    }

    public String getChartType() {
        return TYPE;
    }
}

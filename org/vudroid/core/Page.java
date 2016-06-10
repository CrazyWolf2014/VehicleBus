package org.vudroid.core;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.text.TextPaint;
import org.achartengine.renderer.DefaultRenderer;

class Page {
    private float aspectRatio;
    RectF bounds;
    private DocumentView documentView;
    private final Paint fillPaint;
    final int index;
    private PageTreeNode node;
    private final Paint strokePaint;
    private final TextPaint textPaint;

    Page(DocumentView documentView, int index) {
        this.textPaint = textPaint();
        this.fillPaint = fillPaint();
        this.strokePaint = strokePaint();
        this.documentView = documentView;
        this.index = index;
        this.node = new PageTreeNode(documentView, new RectF(0.0f, 0.0f, 1.0f, 1.0f), this, 1, null);
    }

    float getPageHeight(int mainWidth, float zoom) {
        return (((float) mainWidth) / getAspectRatio()) * zoom;
    }

    public int getTop() {
        return Math.round(this.bounds.top);
    }

    public void draw(Canvas canvas) {
        if (isVisible()) {
            canvas.drawRect(this.bounds, this.fillPaint);
            canvas.drawText("Page " + (this.index + 1), this.bounds.centerX(), this.bounds.centerY(), this.textPaint);
            this.node.draw(canvas);
            canvas.drawLine(this.bounds.left, this.bounds.top, this.bounds.right, this.bounds.top, this.strokePaint);
            canvas.drawLine(this.bounds.left, this.bounds.bottom, this.bounds.right, this.bounds.bottom, this.strokePaint);
        }
    }

    private Paint strokePaint() {
        Paint strokePaint = new Paint();
        strokePaint.setColor(DefaultRenderer.BACKGROUND_COLOR);
        strokePaint.setStyle(Style.STROKE);
        strokePaint.setStrokeWidth(2.0f);
        return strokePaint;
    }

    private Paint fillPaint() {
        Paint fillPaint = new Paint();
        fillPaint.setColor(-7829368);
        fillPaint.setStyle(Style.FILL);
        return fillPaint;
    }

    private TextPaint textPaint() {
        TextPaint paint = new TextPaint();
        paint.setColor(DefaultRenderer.BACKGROUND_COLOR);
        paint.setAntiAlias(true);
        paint.setTextSize(24.0f);
        paint.setTextAlign(Align.CENTER);
        return paint;
    }

    public float getAspectRatio() {
        return this.aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        if (this.aspectRatio != aspectRatio) {
            this.aspectRatio = aspectRatio;
            this.documentView.invalidatePageSizes();
        }
    }

    public boolean isVisible() {
        return RectF.intersects(this.documentView.getViewRect(), this.bounds);
    }

    public void setAspectRatio(int width, int height) {
        setAspectRatio((((float) width) * 1.0f) / ((float) height));
    }

    void setBounds(RectF pageBounds) {
        this.bounds = pageBounds;
        this.node.invalidateNodeBounds();
    }

    public void updateVisibility() {
        this.node.updateVisibility();
    }

    public void invalidate() {
        this.node.invalidate();
    }
}

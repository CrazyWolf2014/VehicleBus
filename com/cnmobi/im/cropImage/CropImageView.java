package com.cnmobi.im.cropImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.cnmobi.im.cropImage.HighlightView.ModifyMode;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import java.util.ArrayList;
import java.util.Iterator;
import org.xbill.DNS.KEYRecord;

public class CropImageView extends ImageViewTouchBase {
    private CropImage mCropImage;
    public ArrayList<HighlightView> mHighlightViews;
    float mLastX;
    float mLastY;
    int mMotionEdge;
    HighlightView mMotionHighlightView;

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mBitmapDisplayed.getBitmap() != null) {
            Iterator it = this.mHighlightViews.iterator();
            while (it.hasNext()) {
                HighlightView hv = (HighlightView) it.next();
                hv.mMatrix.set(getImageMatrix());
                hv.invalidate();
                if (hv.mIsFocused) {
                    centerBasedOnHighlightView(hv);
                }
            }
        }
    }

    public CropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mHighlightViews = new ArrayList();
        this.mMotionHighlightView = null;
    }

    protected void zoomTo(float scale, float centerX, float centerY) {
        super.zoomTo(scale, centerX, centerY);
        Iterator it = this.mHighlightViews.iterator();
        while (it.hasNext()) {
            HighlightView hv = (HighlightView) it.next();
            hv.mMatrix.set(getImageMatrix());
            hv.invalidate();
        }
    }

    protected void zoomIn() {
        super.zoomIn();
        Iterator it = this.mHighlightViews.iterator();
        while (it.hasNext()) {
            HighlightView hv = (HighlightView) it.next();
            hv.mMatrix.set(getImageMatrix());
            hv.invalidate();
        }
    }

    protected void zoomOut() {
        super.zoomOut();
        Iterator it = this.mHighlightViews.iterator();
        while (it.hasNext()) {
            HighlightView hv = (HighlightView) it.next();
            hv.mMatrix.set(getImageMatrix());
            hv.invalidate();
        }
    }

    protected void postTranslate(float deltaX, float deltaY) {
        super.postTranslate(deltaX, deltaY);
        for (int i = 0; i < this.mHighlightViews.size(); i++) {
            HighlightView hv = (HighlightView) this.mHighlightViews.get(i);
            hv.mMatrix.postTranslate(deltaX, deltaY);
            hv.invalidate();
        }
    }

    private void recomputeFocus(MotionEvent event) {
        int i;
        for (i = 0; i < this.mHighlightViews.size(); i++) {
            HighlightView hv = (HighlightView) this.mHighlightViews.get(i);
            hv.setFocus(false);
            hv.invalidate();
        }
        for (i = 0; i < this.mHighlightViews.size(); i++) {
            hv = (HighlightView) this.mHighlightViews.get(i);
            if (hv.getHit(event.getX(), event.getY()) != 1) {
                if (!hv.hasFocus()) {
                    hv.setFocus(true);
                    hv.invalidate();
                }
                invalidate();
            }
        }
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        CropImage cropImage = this.mCropImage;
        if (cropImage.mSaving) {
            return false;
        }
        int i;
        HighlightView hv;
        switch (event.getAction()) {
            case KEYRecord.OWNER_USER /*0*/:
                if (!cropImage.mWaitingToPick) {
                    for (i = 0; i < this.mHighlightViews.size(); i++) {
                        hv = (HighlightView) this.mHighlightViews.get(i);
                        int edge = hv.getHit(event.getX(), event.getY());
                        if (edge != 1) {
                            ModifyMode modifyMode;
                            this.mMotionEdge = edge;
                            this.mMotionHighlightView = hv;
                            this.mLastX = event.getX();
                            this.mLastY = event.getY();
                            HighlightView highlightView = this.mMotionHighlightView;
                            if (edge == 32) {
                                modifyMode = ModifyMode.Move;
                            } else {
                                modifyMode = ModifyMode.Grow;
                            }
                            highlightView.setMode(modifyMode);
                            break;
                        }
                    }
                    break;
                }
                recomputeFocus(event);
                break;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                if (cropImage.mWaitingToPick) {
                    for (i = 0; i < this.mHighlightViews.size(); i++) {
                        hv = (HighlightView) this.mHighlightViews.get(i);
                        if (hv.hasFocus()) {
                            cropImage.mCrop = hv;
                            for (int j = 0; j < this.mHighlightViews.size(); j++) {
                                if (j != i) {
                                    ((HighlightView) this.mHighlightViews.get(j)).setHidden(true);
                                }
                            }
                            centerBasedOnHighlightView(hv);
                            this.mCropImage.mWaitingToPick = false;
                            return true;
                        }
                    }
                } else if (this.mMotionHighlightView != null) {
                    centerBasedOnHighlightView(this.mMotionHighlightView);
                    this.mMotionHighlightView.setMode(ModifyMode.None);
                }
                this.mMotionHighlightView = null;
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                if (!cropImage.mWaitingToPick) {
                    if (this.mMotionHighlightView != null) {
                        this.mMotionHighlightView.handleMotion(this.mMotionEdge, event.getX() - this.mLastX, event.getY() - this.mLastY);
                        this.mLastX = event.getX();
                        this.mLastY = event.getY();
                        ensureVisible(this.mMotionHighlightView);
                        break;
                    }
                }
                recomputeFocus(event);
                break;
                break;
        }
        switch (event.getAction()) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                center(true, true);
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                center(true, true);
                break;
        }
        return true;
    }

    private void ensureVisible(HighlightView hv) {
        int panDeltaX;
        int panDeltaY;
        Rect r = hv.mDrawRect;
        int panDeltaX1 = Math.max(0, getLeft() - r.left);
        int panDeltaX2 = Math.min(0, getRight() - r.right);
        int panDeltaY1 = Math.max(0, getTop() - r.top);
        int panDeltaY2 = Math.min(0, getBottom() - r.bottom);
        if (panDeltaX1 != 0) {
            panDeltaX = panDeltaX1;
        } else {
            panDeltaX = panDeltaX2;
        }
        if (panDeltaY1 != 0) {
            panDeltaY = panDeltaY1;
        } else {
            panDeltaY = panDeltaY2;
        }
        if (panDeltaX != 0 || panDeltaY != 0) {
            panBy((float) panDeltaX, (float) panDeltaY);
        }
    }

    private void centerBasedOnHighlightView(HighlightView hv) {
        Rect drawRect = hv.mDrawRect;
        float thisWidth = (float) getWidth();
        float thisHeight = (float) getHeight();
        float zoom = Math.max(1.0f, Math.min((thisWidth / ((float) drawRect.width())) * 0.6f, (thisHeight / ((float) drawRect.height())) * 0.6f) * getScale());
        if (((double) (Math.abs(zoom - getScale()) / zoom)) > 0.1d) {
            float[] coordinates = new float[]{hv.mCropRect.centerX(), hv.mCropRect.centerY()};
            getImageMatrix().mapPoints(coordinates);
            zoomTo(zoom, coordinates[0], coordinates[1], 300.0f);
        }
        ensureVisible(hv);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < this.mHighlightViews.size(); i++) {
            ((HighlightView) this.mHighlightViews.get(i)).draw(canvas);
        }
    }

    public void add(HighlightView hv) {
        this.mHighlightViews.clear();
        this.mHighlightViews.add(hv);
        invalidate();
    }

    public void setCropImage(CropImage cropImage) {
        this.mCropImage = cropImage;
    }

    public void resetView(Bitmap b) {
        setImageBitmap(b);
        setImageBitmapResetBase(b, true);
        setImageMatrix(getImageViewMatrix());
        int width = this.mBitmapDisplayed.getWidth();
        int height = this.mBitmapDisplayed.getHeight();
        Rect imageRect = new Rect(0, 0, width, height);
        int cropWidth = (Math.min(width, height) * 4) / 5;
        int cropHeight = cropWidth;
        int x = (width - cropWidth) / 2;
        int y = (height - cropHeight) / 2;
        RectF cropRect = new RectF((float) x, (float) y, (float) (x + cropWidth), (float) (y + cropHeight));
        HighlightView hv = new HighlightView(this);
        hv.setup(getImageViewMatrix(), imageRect, cropRect, false, true);
        hv.setFocus(true);
        add(hv);
        centerBasedOnHighlightView(hv);
        hv.setMode(ModifyMode.None);
        center(true, true);
        invalidate();
    }
}

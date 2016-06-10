package org.vudroid.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import java.lang.ref.SoftReference;
import org.vudroid.core.DecodeService.DecodeCallback;

class PageTreeNode {
    private static final int SLICE_SIZE = 65535;
    private Bitmap bitmap;
    private final Paint bitmapPaint;
    private SoftReference<Bitmap> bitmapWeakReference;
    private PageTreeNode[] children;
    private boolean decodingNow;
    private DocumentView documentView;
    private boolean invalidateFlag;
    private Matrix matrix;
    private final Page page;
    private final RectF pageSliceBounds;
    private Rect targetRect;
    private RectF targetRectF;
    private final int treeNodeDepthLevel;

    /* renamed from: org.vudroid.core.PageTreeNode.1 */
    class C12211 implements DecodeCallback {

        /* renamed from: org.vudroid.core.PageTreeNode.1.1 */
        class C09961 implements Runnable {
            private final /* synthetic */ Bitmap val$bitmap;

            C09961(Bitmap bitmap) {
                this.val$bitmap = bitmap;
            }

            public void run() {
                PageTreeNode.this.setBitmap(this.val$bitmap);
                PageTreeNode.this.invalidateFlag = false;
                PageTreeNode.this.setDecodingNow(false);
                PageTreeNode.this.page.setAspectRatio(PageTreeNode.this.documentView.decodeService.getPageWidth(PageTreeNode.this.page.index), PageTreeNode.this.documentView.decodeService.getPageHeight(PageTreeNode.this.page.index));
                PageTreeNode.this.invalidateChildren();
            }
        }

        C12211() {
        }

        public void decodeComplete(Bitmap bitmap) {
            PageTreeNode.this.documentView.post(new C09961(bitmap));
        }
    }

    PageTreeNode(DocumentView documentView, RectF localPageSliceBounds, Page page, int treeNodeDepthLevel, PageTreeNode parent) {
        this.matrix = new Matrix();
        this.bitmapPaint = new Paint();
        this.documentView = documentView;
        this.pageSliceBounds = evaluatePageSliceBounds(localPageSliceBounds, parent);
        this.page = page;
        this.treeNodeDepthLevel = treeNodeDepthLevel;
    }

    public void updateVisibility() {
        invalidateChildren();
        if (this.children != null) {
            for (PageTreeNode child : this.children) {
                child.updateVisibility();
            }
        }
        if (isVisible() && !thresholdHit()) {
            if (getBitmap() == null || this.invalidateFlag) {
                decodePageTreeNode();
            } else {
                restoreBitmapReference();
            }
        }
        if (!isVisibleAndNotHiddenByChildren()) {
            stopDecodingThisNode();
            setBitmap(null);
        }
    }

    public void invalidate() {
        invalidateChildren();
        invalidateRecursive();
        updateVisibility();
    }

    private void invalidateRecursive() {
        this.invalidateFlag = true;
        if (this.children != null) {
            for (PageTreeNode child : this.children) {
                child.invalidateRecursive();
            }
        }
        stopDecodingThisNode();
    }

    void invalidateNodeBounds() {
        this.targetRect = null;
        this.targetRectF = null;
        if (this.children != null) {
            for (PageTreeNode child : this.children) {
                child.invalidateNodeBounds();
            }
        }
    }

    void draw(Canvas canvas) {
        int i = 0;
        if (getBitmap() != null) {
            canvas.drawBitmap(getBitmap(), new Rect(0, 0, getBitmap().getWidth(), getBitmap().getHeight()), getTargetRect(), this.bitmapPaint);
        }
        if (this.children != null) {
            PageTreeNode[] pageTreeNodeArr = this.children;
            int length = pageTreeNodeArr.length;
            while (i < length) {
                pageTreeNodeArr[i].draw(canvas);
                i++;
            }
        }
    }

    private boolean isVisible() {
        return RectF.intersects(this.documentView.getViewRect(), getTargetRectF());
    }

    private RectF getTargetRectF() {
        if (this.targetRectF == null) {
            this.targetRectF = new RectF(getTargetRect());
        }
        return this.targetRectF;
    }

    private void invalidateChildren() {
        if (thresholdHit() && this.children == null && isVisible()) {
            int newThreshold = this.treeNodeDepthLevel * 2;
            this.children = new PageTreeNode[]{new PageTreeNode(this.documentView, new RectF(0.0f, 0.0f, 0.5f, 0.5f), this.page, newThreshold, this), new PageTreeNode(this.documentView, new RectF(0.5f, 0.0f, 1.0f, 0.5f), this.page, newThreshold, this), new PageTreeNode(this.documentView, new RectF(0.0f, 0.5f, 0.5f, 1.0f), this.page, newThreshold, this), new PageTreeNode(this.documentView, new RectF(0.5f, 0.5f, 1.0f, 1.0f), this.page, newThreshold, this)};
        }
        if ((!thresholdHit() && getBitmap() != null) || !isVisible()) {
            recycleChildren();
        }
    }

    private boolean thresholdHit() {
        float zoom = this.documentView.zoomModel.getZoom();
        int mainWidth = this.documentView.getWidth();
        return ((((float) mainWidth) * zoom) * this.page.getPageHeight(mainWidth, zoom)) / ((float) (this.treeNodeDepthLevel * this.treeNodeDepthLevel)) > 65535.0f;
    }

    public Bitmap getBitmap() {
        return this.bitmapWeakReference != null ? (Bitmap) this.bitmapWeakReference.get() : null;
    }

    private void restoreBitmapReference() {
        setBitmap(getBitmap());
    }

    private void decodePageTreeNode() {
        if (!isDecodingNow()) {
            setDecodingNow(true);
            this.documentView.decodeService.decodePage(this, this.page.index, new C12211(), this.documentView.zoomModel.getZoom(), this.pageSliceBounds);
        }
    }

    private RectF evaluatePageSliceBounds(RectF localPageSliceBounds, PageTreeNode parent) {
        if (parent == null) {
            return localPageSliceBounds;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(parent.pageSliceBounds.width(), parent.pageSliceBounds.height());
        matrix.postTranslate(parent.pageSliceBounds.left, parent.pageSliceBounds.top);
        RectF sliceBounds = new RectF();
        matrix.mapRect(sliceBounds, localPageSliceBounds);
        return sliceBounds;
    }

    private void setBitmap(Bitmap bitmap) {
        if ((bitmap == null || bitmap.getWidth() != -1 || bitmap.getHeight() != -1) && this.bitmap != bitmap) {
            if (bitmap != null) {
                if (this.bitmap != null) {
                    this.bitmap.recycle();
                }
                this.bitmapWeakReference = new SoftReference(bitmap);
                this.documentView.postInvalidate();
            }
            this.bitmap = bitmap;
        }
    }

    private boolean isDecodingNow() {
        return this.decodingNow;
    }

    private void setDecodingNow(boolean decodingNow) {
        if (this.decodingNow != decodingNow) {
            this.decodingNow = decodingNow;
            if (decodingNow) {
                this.documentView.progressModel.increase();
            } else {
                this.documentView.progressModel.decrease();
            }
        }
    }

    private Rect getTargetRect() {
        if (this.targetRect == null) {
            this.matrix.reset();
            this.matrix.postScale(this.page.bounds.width(), this.page.bounds.height());
            this.matrix.postTranslate(this.page.bounds.left, this.page.bounds.top);
            RectF targetRectF = new RectF();
            this.matrix.mapRect(targetRectF, this.pageSliceBounds);
            this.targetRect = new Rect((int) targetRectF.left, (int) targetRectF.top, (int) targetRectF.right, (int) targetRectF.bottom);
        }
        return this.targetRect;
    }

    private void stopDecodingThisNode() {
        if (isDecodingNow()) {
            this.documentView.decodeService.stopDecoding(this);
            setDecodingNow(false);
        }
    }

    private boolean isHiddenByChildren() {
        if (this.children == null) {
            return false;
        }
        for (PageTreeNode child : this.children) {
            if (child.getBitmap() == null) {
                return false;
            }
        }
        return true;
    }

    private void recycleChildren() {
        if (this.children != null) {
            for (PageTreeNode child : this.children) {
                child.recycle();
            }
            if (!childrenContainBitmaps()) {
                this.children = null;
            }
        }
    }

    private boolean containsBitmaps() {
        return getBitmap() != null || childrenContainBitmaps();
    }

    private boolean childrenContainBitmaps() {
        if (this.children == null) {
            return false;
        }
        for (PageTreeNode child : this.children) {
            if (child.containsBitmaps()) {
                return true;
            }
        }
        return false;
    }

    private void recycle() {
        stopDecodingThisNode();
        setBitmap(null);
        if (this.children != null) {
            for (PageTreeNode child : this.children) {
                child.recycle();
            }
        }
    }

    private boolean isVisibleAndNotHiddenByChildren() {
        return isVisible() && !isHiddenByChildren();
    }
}

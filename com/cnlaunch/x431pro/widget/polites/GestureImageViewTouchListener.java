package com.cnlaunch.x431pro.widget.polites;

import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import com.googlecode.leptonica.android.Skew;

public class GestureImageViewTouchListener implements OnTouchListener {
    private float boundaryBottom;
    private float boundaryLeft;
    private float boundaryRight;
    private float boundaryTop;
    private boolean canDragX;
    private boolean canDragY;
    private int canvasHeight;
    private int canvasWidth;
    private float centerX;
    private float centerY;
    private final PointF current;
    private float currentScale;
    private int displayHeight;
    private int displayWidth;
    private float fitScaleHorizontal;
    private float fitScaleVertical;
    private FlingAnimation flingAnimation;
    private GestureDetector flingDetector;
    private FlingListener flingListener;
    private GestureImageView image;
    private int imageHeight;
    private GestureImageViewListener imageListener;
    private int imageWidth;
    private boolean inZoom;
    private float initialDistance;
    private final PointF last;
    private float lastScale;
    private float maxScale;
    private final PointF midpoint;
    private float minScale;
    private MoveAnimation moveAnimation;
    private boolean multiTouch;
    private final PointF next;
    private OnClickListener onClickListener;
    private final VectorF pinchVector;
    private final VectorF scaleVector;
    private float startingScale;
    private GestureDetector tapDetector;
    private boolean touched;
    private ZoomAnimation zoomAnimation;

    /* renamed from: com.cnlaunch.x431pro.widget.polites.GestureImageViewTouchListener.4 */
    class C01394 extends SimpleOnGestureListener {
        private final /* synthetic */ GestureImageView val$image;

        C01394(GestureImageView gestureImageView) {
            this.val$image = gestureImageView;
        }

        public boolean onDoubleTap(MotionEvent e) {
            GestureImageViewTouchListener.this.startZoom(e);
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (GestureImageViewTouchListener.this.inZoom || GestureImageViewTouchListener.this.onClickListener == null) {
                return false;
            }
            GestureImageViewTouchListener.this.onClickListener.onClick(this.val$image);
            return true;
        }
    }

    /* renamed from: com.cnlaunch.x431pro.widget.polites.GestureImageViewTouchListener.1 */
    class C10411 implements FlingAnimationListener {
        C10411() {
        }

        public void onMove(float x, float y) {
            GestureImageViewTouchListener.this.handleDrag(GestureImageViewTouchListener.this.current.x + x, GestureImageViewTouchListener.this.current.y + y);
        }

        public void onComplete() {
        }
    }

    /* renamed from: com.cnlaunch.x431pro.widget.polites.GestureImageViewTouchListener.2 */
    class C10422 implements ZoomAnimationListener {
        C10422() {
        }

        public void onZoom(float scale, float x, float y) {
            if (scale <= GestureImageViewTouchListener.this.maxScale && scale >= GestureImageViewTouchListener.this.minScale) {
                GestureImageViewTouchListener.this.handleScale(scale, x, y);
            }
        }

        public void onComplete() {
            GestureImageViewTouchListener.this.inZoom = false;
            GestureImageViewTouchListener.this.handleUp();
        }
    }

    /* renamed from: com.cnlaunch.x431pro.widget.polites.GestureImageViewTouchListener.3 */
    class C10433 implements MoveAnimationListener {
        private final /* synthetic */ GestureImageView val$image;

        C10433(GestureImageView gestureImageView) {
            this.val$image = gestureImageView;
        }

        public void onMove(float x, float y) {
            this.val$image.setPosition(x, y);
            this.val$image.redraw();
        }
    }

    public GestureImageViewTouchListener(GestureImageView image, int displayWidth, int displayHeight) {
        this.current = new PointF();
        this.last = new PointF();
        this.next = new PointF();
        this.midpoint = new PointF();
        this.scaleVector = new VectorF();
        this.pinchVector = new VectorF();
        this.touched = false;
        this.inZoom = false;
        this.lastScale = 1.0f;
        this.currentScale = 1.0f;
        this.boundaryLeft = 0.0f;
        this.boundaryTop = 0.0f;
        this.boundaryRight = 0.0f;
        this.boundaryBottom = 0.0f;
        this.maxScale = Skew.SWEEP_DELTA;
        this.minScale = 0.25f;
        this.fitScaleHorizontal = 1.0f;
        this.fitScaleVertical = 1.0f;
        this.canvasWidth = 0;
        this.canvasHeight = 0;
        this.centerX = 0.0f;
        this.centerY = 0.0f;
        this.startingScale = 0.0f;
        this.canDragX = false;
        this.canDragY = false;
        this.multiTouch = false;
        this.image = image;
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
        this.centerX = ((float) displayWidth) / 2.0f;
        this.centerY = ((float) displayHeight) / 2.0f;
        this.imageWidth = image.getImageWidth();
        this.imageHeight = image.getImageHeight();
        this.startingScale = image.getScale();
        this.currentScale = this.startingScale;
        this.lastScale = this.startingScale;
        this.boundaryRight = (float) displayWidth;
        this.boundaryBottom = (float) displayHeight;
        this.boundaryLeft = 0.0f;
        this.boundaryTop = 0.0f;
        this.next.x = image.getImageX();
        this.next.y = image.getImageY();
        this.flingListener = new FlingListener();
        this.flingAnimation = new FlingAnimation();
        this.zoomAnimation = new ZoomAnimation();
        this.moveAnimation = new MoveAnimation();
        this.flingAnimation.setListener(new C10411());
        this.zoomAnimation.setZoom(2.0f);
        this.zoomAnimation.setZoomAnimationListener(new C10422());
        this.moveAnimation.setMoveAnimationListener(new C10433(image));
        this.tapDetector = new GestureDetector(image.getContext(), new C01394(image));
        this.flingDetector = new GestureDetector(image.getContext(), this.flingListener);
        this.imageListener = image.getGestureImageViewListener();
        calculateBoundaries();
    }

    private void startFling() {
        this.flingAnimation.setVelocityX(this.flingListener.getVelocityX());
        this.flingAnimation.setVelocityY(this.flingListener.getVelocityY());
        this.image.animationStart(this.flingAnimation);
    }

    private void startZoom(MotionEvent e) {
        float zoomTo;
        this.inZoom = true;
        this.zoomAnimation.reset();
        if (this.image.isLandscape()) {
            if (this.image.getDeviceOrientation() != 1) {
                int scaledWidth = this.image.getScaledWidth();
                if (scaledWidth == this.canvasWidth) {
                    zoomTo = this.currentScale * 4.0f;
                    this.zoomAnimation.setTouchX(e.getX());
                    this.zoomAnimation.setTouchY(e.getY());
                } else if (scaledWidth < this.canvasWidth) {
                    zoomTo = this.fitScaleHorizontal / this.currentScale;
                    this.zoomAnimation.setTouchX(this.image.getCenterX());
                    this.zoomAnimation.setTouchY(e.getY());
                } else {
                    zoomTo = this.fitScaleHorizontal / this.currentScale;
                    this.zoomAnimation.setTouchX(this.image.getCenterX());
                    this.zoomAnimation.setTouchY(this.image.getCenterY());
                }
            } else if (this.image.getScaledHeight() < this.canvasHeight) {
                zoomTo = this.fitScaleVertical / this.currentScale;
                this.zoomAnimation.setTouchX(e.getX());
                this.zoomAnimation.setTouchY(this.image.getCenterY());
            } else {
                zoomTo = this.fitScaleHorizontal / this.currentScale;
                this.zoomAnimation.setTouchX(this.image.getCenterX());
                this.zoomAnimation.setTouchY(this.image.getCenterY());
            }
        } else if (this.image.getDeviceOrientation() == 1) {
            int scaledHeight = this.image.getScaledHeight();
            if (scaledHeight == this.canvasHeight) {
                zoomTo = this.currentScale * 4.0f;
                this.zoomAnimation.setTouchX(e.getX());
                this.zoomAnimation.setTouchY(e.getY());
            } else if (scaledHeight < this.canvasHeight) {
                zoomTo = this.fitScaleVertical / this.currentScale;
                this.zoomAnimation.setTouchX(e.getX());
                this.zoomAnimation.setTouchY(this.image.getCenterY());
            } else {
                zoomTo = this.fitScaleVertical / this.currentScale;
                this.zoomAnimation.setTouchX(this.image.getCenterX());
                this.zoomAnimation.setTouchY(this.image.getCenterY());
            }
        } else if (this.image.getScaledWidth() < this.canvasWidth) {
            zoomTo = this.fitScaleHorizontal / this.currentScale;
            this.zoomAnimation.setTouchX(this.image.getCenterX());
            this.zoomAnimation.setTouchY(e.getY());
        } else {
            zoomTo = this.fitScaleVertical / this.currentScale;
            this.zoomAnimation.setTouchX(this.image.getCenterX());
            this.zoomAnimation.setTouchY(this.image.getCenterY());
        }
        this.zoomAnimation.setZoom(zoomTo);
        this.image.animationStart(this.zoomAnimation);
    }

    private void stopAnimations() {
        this.image.animationStop();
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (!(this.inZoom || this.tapDetector.onTouchEvent(event))) {
            if (event.getPointerCount() == 1 && this.flingDetector.onTouchEvent(event)) {
                startFling();
            }
            if (event.getAction() == 1) {
                handleUp();
            } else if (event.getAction() == 0) {
                stopAnimations();
                this.last.x = event.getX();
                this.last.y = event.getY();
                if (this.imageListener != null) {
                    this.imageListener.onTouch(this.last.x, this.last.y);
                }
                this.touched = true;
            } else if (event.getAction() == 2) {
                if (event.getPointerCount() > 1) {
                    this.multiTouch = true;
                    VectorF vectorF;
                    if (this.initialDistance > 0.0f) {
                        this.pinchVector.set(event);
                        this.pinchVector.calculateLength();
                        float distance = this.pinchVector.length;
                        if (this.initialDistance != distance) {
                            float newScale = (distance / this.initialDistance) * this.lastScale;
                            if (newScale <= this.maxScale) {
                                vectorF = this.scaleVector;
                                vectorF.length *= newScale;
                                this.scaleVector.calculateEndPoint();
                                vectorF = this.scaleVector;
                                vectorF.length /= newScale;
                                handleScale(newScale, this.scaleVector.end.x, this.scaleVector.end.y);
                            }
                        }
                    } else {
                        this.initialDistance = MathUtils.distance(event);
                        MathUtils.midpoint(event, this.midpoint);
                        this.scaleVector.setStart(this.midpoint);
                        this.scaleVector.setEnd(this.next);
                        this.scaleVector.calculateLength();
                        this.scaleVector.calculateAngle();
                        vectorF = this.scaleVector;
                        vectorF.length /= this.lastScale;
                    }
                } else if (!this.touched) {
                    this.touched = true;
                    this.last.x = event.getX();
                    this.last.y = event.getY();
                    this.next.x = this.image.getImageX();
                    this.next.y = this.image.getImageY();
                } else if (!this.multiTouch && handleDrag(event.getX(), event.getY())) {
                    this.image.redraw();
                }
            }
        }
        return true;
    }

    protected void handleUp() {
        this.multiTouch = false;
        this.initialDistance = 0.0f;
        this.lastScale = this.currentScale;
        if (!this.canDragX) {
            this.next.x = this.centerX;
        }
        if (!this.canDragY) {
            this.next.y = this.centerY;
        }
        boundCoordinates();
        if (!(this.canDragX || this.canDragY)) {
            if (this.image.isLandscape()) {
                this.currentScale = this.fitScaleHorizontal;
                this.lastScale = this.fitScaleHorizontal;
            } else {
                this.currentScale = this.fitScaleVertical;
                this.lastScale = this.fitScaleVertical;
            }
        }
        this.image.setScale(this.currentScale);
        this.image.setPosition(this.next.x, this.next.y);
        if (this.imageListener != null) {
            this.imageListener.onScale(this.currentScale);
            this.imageListener.onPosition(this.next.x, this.next.y);
        }
        this.image.redraw();
    }

    protected void handleScale(float scale, float x, float y) {
        this.currentScale = scale;
        if (this.currentScale > this.maxScale) {
            this.currentScale = this.maxScale;
        } else if (this.currentScale < this.minScale) {
            this.currentScale = this.minScale;
        } else {
            this.next.x = x;
            this.next.y = y;
        }
        calculateBoundaries();
        this.image.setScale(this.currentScale);
        this.image.setPosition(this.next.x, this.next.y);
        if (this.imageListener != null) {
            this.imageListener.onScale(this.currentScale);
            this.imageListener.onPosition(this.next.x, this.next.y);
        }
        this.image.redraw();
    }

    protected boolean handleDrag(float x, float y) {
        this.current.x = x;
        this.current.y = y;
        float diffX = this.current.x - this.last.x;
        float diffY = this.current.y - this.last.y;
        if (!(diffX == 0.0f && diffY == 0.0f)) {
            PointF pointF;
            if (this.canDragX) {
                pointF = this.next;
                pointF.x += diffX;
            }
            if (this.canDragY) {
                pointF = this.next;
                pointF.y += diffY;
            }
            boundCoordinates();
            this.last.x = this.current.x;
            this.last.y = this.current.y;
            if (this.canDragX || this.canDragY) {
                this.image.setPosition(this.next.x, this.next.y);
                if (this.imageListener != null) {
                    this.imageListener.onPosition(this.next.x, this.next.y);
                }
                return true;
            }
        }
        return false;
    }

    public void reset() {
        this.currentScale = this.startingScale;
        this.next.x = this.centerX;
        this.next.y = this.centerY;
        calculateBoundaries();
        this.image.setScale(this.currentScale);
        this.image.setPosition(this.next.x, this.next.y);
        this.image.redraw();
    }

    public float getMaxScale() {
        return this.maxScale;
    }

    public void setMaxScale(float maxScale) {
        this.maxScale = maxScale;
    }

    public float getMinScale() {
        return this.minScale;
    }

    public void setMinScale(float minScale) {
        this.minScale = minScale;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    protected void setCanvasWidth(int canvasWidth) {
        this.canvasWidth = canvasWidth;
    }

    protected void setCanvasHeight(int canvasHeight) {
        this.canvasHeight = canvasHeight;
    }

    protected void setFitScaleHorizontal(float fitScale) {
        this.fitScaleHorizontal = fitScale;
    }

    protected void setFitScaleVertical(float fitScaleVertical) {
        this.fitScaleVertical = fitScaleVertical;
    }

    protected void boundCoordinates() {
        if (this.next.x < this.boundaryLeft) {
            this.next.x = this.boundaryLeft;
        } else if (this.next.x > this.boundaryRight) {
            this.next.x = this.boundaryRight;
        }
        if (this.next.y < this.boundaryTop) {
            this.next.y = this.boundaryTop;
        } else if (this.next.y > this.boundaryBottom) {
            this.next.y = this.boundaryBottom;
        }
    }

    protected void calculateBoundaries() {
        boolean z = true;
        int effectiveWidth = Math.round(((float) this.imageWidth) * this.currentScale);
        int effectiveHeight = Math.round(((float) this.imageHeight) * this.currentScale);
        this.canDragX = effectiveWidth > this.displayWidth;
        if (effectiveHeight <= this.displayHeight) {
            z = false;
        }
        this.canDragY = z;
        if (this.canDragX) {
            float diff = ((float) (effectiveWidth - this.displayWidth)) / 2.0f;
            this.boundaryLeft = this.centerX - diff;
            this.boundaryRight = this.centerX + diff;
        }
        if (this.canDragY) {
            diff = ((float) (effectiveHeight - this.displayHeight)) / 2.0f;
            this.boundaryTop = this.centerY - diff;
            this.boundaryBottom = this.centerY + diff;
        }
    }
}

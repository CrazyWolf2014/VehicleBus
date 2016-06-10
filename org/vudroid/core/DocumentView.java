package org.vudroid.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.expeditionphone.WelcomeActivity;
import java.util.HashMap;
import java.util.Map.Entry;
import org.vudroid.core.events.ZoomListener;
import org.vudroid.core.models.CurrentPageModel;
import org.vudroid.core.models.DecodingProgressModel;
import org.vudroid.core.models.ZoomModel;
import org.vudroid.core.multitouch.MultiTouchZoom;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.WKSRecord.Protocol;

public class DocumentView extends View implements ZoomListener {
    private static final int DOUBLE_TAP_TIME = 500;
    private final CurrentPageModel currentPageModel;
    DecodeService decodeService;
    private boolean inZoom;
    private boolean isInitialized;
    private long lastDownEventTime;
    private float lastX;
    private float lastY;
    private MultiTouchZoom multiTouchZoom;
    private int pageToGoTo;
    @SuppressLint({"UseSparseArrays"})
    private final HashMap<Integer, Page> pages;
    DecodingProgressModel progressModel;
    private final Scroller scroller;
    private VelocityTracker velocityTracker;
    private RectF viewRect;
    final ZoomModel zoomModel;

    /* renamed from: org.vudroid.core.DocumentView.1 */
    class C09931 implements Runnable {
        C09931() {
        }

        public void run() {
            DocumentView.this.currentPageModel.setCurrentPageIndex(DocumentView.this.getCurrentPage());
        }
    }

    /* renamed from: org.vudroid.core.DocumentView.2 */
    class C09942 implements Runnable {
        C09942() {
        }

        public void run() {
            DocumentView.this.updatePageVisibility();
        }
    }

    /* renamed from: org.vudroid.core.DocumentView.3 */
    class C09953 implements Runnable {
        C09953() {
        }

        public void run() {
            DocumentView.this.init();
            DocumentView.this.updatePageVisibility();
        }
    }

    public DocumentView(Context context, ZoomModel zoomModel, DecodingProgressModel progressModel, CurrentPageModel currentPageModel) {
        super(context);
        this.pages = new HashMap();
        this.isInitialized = false;
        this.zoomModel = zoomModel;
        this.progressModel = progressModel;
        this.currentPageModel = currentPageModel;
        setKeepScreenOn(true);
        this.scroller = new Scroller(getContext());
        setFocusable(true);
        setFocusableInTouchMode(true);
        initMultiTouchZoomIfAvailable(zoomModel);
    }

    private void initMultiTouchZoomIfAvailable(ZoomModel zoomModel) {
        try {
            this.multiTouchZoom = (MultiTouchZoom) Class.forName("org.vudroid.core.multitouch.MultiTouchZoomImpl").getConstructor(new Class[]{ZoomModel.class}).newInstance(new Object[]{zoomModel});
        } catch (Exception e) {
            System.out.println("Multi touch zoom is not available: " + e);
        }
    }

    public void setDecodeService(DecodeService decodeService) {
        this.decodeService = decodeService;
    }

    private void init() {
        if (!this.isInitialized) {
            int width = this.decodeService.getEffectivePagesWidth();
            int height = this.decodeService.getEffectivePagesHeight();
            for (int i = 0; i < this.decodeService.getPageCount(); i++) {
                this.pages.put(Integer.valueOf(i), new Page(this, i));
                ((Page) this.pages.get(Integer.valueOf(i))).setAspectRatio(width, height);
            }
            this.isInitialized = true;
            invalidatePageSizes();
            goToPageImpl(this.pageToGoTo);
        }
    }

    private void goToPageImpl(int toPage) {
        scrollTo(0, ((Page) this.pages.get(Integer.valueOf(toPage))).getTop());
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        post(new C09931());
        if (!this.inZoom) {
            post(new C09942());
        }
    }

    private void updatePageVisibility() {
        for (Page page : this.pages.values()) {
            page.updateVisibility();
        }
    }

    public void commitZoom() {
        for (Page page : this.pages.values()) {
            page.invalidate();
        }
        this.inZoom = false;
    }

    public void showDocument() {
        post(new C09953());
    }

    public void goToPage(int toPage) {
        if (this.isInitialized) {
            goToPageImpl(toPage);
        } else {
            this.pageToGoTo = toPage;
        }
    }

    public int getCurrentPage() {
        for (Entry<Integer, Page> entry : this.pages.entrySet()) {
            if (((Page) entry.getValue()).isVisible()) {
                return ((Integer) entry.getKey()).intValue();
            }
        }
        return 0;
    }

    public void zoomChanged(float newZoom, float oldZoom) {
        this.inZoom = true;
        stopScroller();
        float ratio = newZoom / oldZoom;
        invalidatePageSizes();
        scrollTo((int) ((((float) (getScrollX() + (getWidth() / 2))) * ratio) - ((float) (getWidth() / 2))), (int) ((((float) (getScrollY() + (getHeight() / 2))) * ratio) - ((float) (getHeight() / 2))));
        postInvalidate();
    }

    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        if (this.multiTouchZoom != null) {
            if (!this.multiTouchZoom.onTouchEvent(ev)) {
                if (this.multiTouchZoom.isResetLastPointAfterZoom()) {
                    setLastPosition(ev);
                    this.multiTouchZoom.setResetLastPointAfterZoom(false);
                }
            }
            return true;
        }
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(ev);
        switch (ev.getAction()) {
            case KEYRecord.OWNER_USER /*0*/:
                stopScroller();
                setLastPosition(ev);
                if (ev.getEventTime() - this.lastDownEventTime >= 500) {
                    this.lastDownEventTime = ev.getEventTime();
                    break;
                }
                this.zoomModel.toggleZoomControls();
                break;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                this.velocityTracker.computeCurrentVelocity(1000);
                this.scroller.fling(getScrollX(), getScrollY(), (int) (-this.velocityTracker.getXVelocity()), (int) (-this.velocityTracker.getYVelocity()), getLeftLimit(), getRightLimit(), getTopLimit(), getBottomLimit());
                this.velocityTracker.recycle();
                this.velocityTracker = null;
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                scrollBy((int) (this.lastX - ev.getX()), (int) (this.lastY - ev.getY()));
                setLastPosition(ev);
                break;
            default:
                break;
        }
        return true;
    }

    private void setLastPosition(MotionEvent ev) {
        this.lastX = ev.getX();
        this.lastY = ev.getY();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case WelcomeActivity.GPIO_IOCQDATAOUT /*19*/:
                    verticalDpadScroll(-1);
                    return true;
                case FileOptions.JAVA_GENERATE_EQUALS_AND_HASH_FIELD_NUMBER /*20*/:
                    verticalDpadScroll(1);
                    return true;
                case WelcomeActivity.GPIO_IOCSDATAHIGH /*21*/:
                    lineByLineMoveTo(-1);
                    return true;
                case Protocol.XNS_IDP /*22*/:
                    lineByLineMoveTo(1);
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void verticalDpadScroll(int direction) {
        this.scroller.startScroll(getScrollX(), getScrollY(), 0, (getHeight() * direction) / 2);
        invalidate();
    }

    private void lineByLineMoveTo(int direction) {
        if (direction != 1 ? getScrollX() == getLeftLimit() : getScrollX() == getRightLimit()) {
            this.scroller.startScroll(getScrollX(), getScrollY(), direction * (getLeftLimit() - getRightLimit()), (int) ((((Page) this.pages.get(Integer.valueOf(getCurrentPage()))).bounds.height() * ((float) direction)) / 50.0f));
        } else {
            this.scroller.startScroll(getScrollX(), getScrollY(), (getWidth() * direction) / 2, 0);
        }
        invalidate();
    }

    private int getTopLimit() {
        return 0;
    }

    private int getLeftLimit() {
        return 0;
    }

    private int getBottomLimit() {
        return ((int) ((Page) this.pages.get(Integer.valueOf(this.pages.size() - 1))).bounds.bottom) - getHeight();
    }

    private int getRightLimit() {
        return ((int) (((float) getWidth()) * this.zoomModel.getZoom())) - getWidth();
    }

    public void scrollTo(int x, int y) {
        super.scrollTo(Math.min(Math.max(x, getLeftLimit()), getRightLimit()), Math.min(Math.max(y, getTopLimit()), getBottomLimit()));
        this.viewRect = null;
    }

    RectF getViewRect() {
        if (this.viewRect == null) {
            this.viewRect = new RectF((float) getScrollX(), (float) getScrollY(), (float) (getScrollX() + getWidth()), (float) (getScrollY() + getHeight()));
        }
        return this.viewRect;
    }

    public void computeScroll() {
        if (this.scroller.computeScrollOffset()) {
            scrollTo(this.scroller.getCurrX(), this.scroller.getCurrY());
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Page page : this.pages.values()) {
            page.draw(canvas);
        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        float scrollScaleRatio = getScrollScaleRatio();
        invalidatePageSizes();
        invalidateScroll(scrollScaleRatio);
        commitZoom();
    }

    void invalidatePageSizes() {
        if (this.isInitialized) {
            float heightAccum = 0.0f;
            int width = getWidth();
            float zoom = this.zoomModel.getZoom();
            for (int i = 0; i < this.pages.size(); i++) {
                Page page = (Page) this.pages.get(Integer.valueOf(i));
                float pageHeight = page.getPageHeight(width, zoom);
                page.setBounds(new RectF(0.0f, heightAccum, ((float) width) * zoom, heightAccum + pageHeight));
                heightAccum += pageHeight;
            }
        }
    }

    private void invalidateScroll(float ratio) {
        if (this.isInitialized) {
            stopScroller();
            Page page = (Page) this.pages.get(Integer.valueOf(0));
            if (page != null && page.bounds != null) {
                scrollTo((int) (((float) getScrollX()) * ratio), (int) (((float) getScrollY()) * ratio));
            }
        }
    }

    private float getScrollScaleRatio() {
        Page page = (Page) this.pages.get(Integer.valueOf(0));
        if (page == null || page.bounds == null) {
            return 0.0f;
        }
        return (((float) getWidth()) * this.zoomModel.getZoom()) / page.bounds.width();
    }

    private void stopScroller() {
        if (!this.scroller.isFinished()) {
            this.scroller.abortAnimation();
        }
    }
}

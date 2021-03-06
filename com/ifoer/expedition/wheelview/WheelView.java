package com.ifoer.expedition.wheelview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import com.cnlaunch.x431frame.C0136R;
import com.googlecode.leptonica.android.Binarize;
import com.ifoer.mine.Contact;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.util.LinkedList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class WheelView extends View {
    private static final int ADDITIONAL_ITEMS_SPACE = 10;
    private static final int ADDITIONAL_ITEM_HEIGHT = 15;
    private static final int DEF_VISIBLE_ITEMS = 5;
    private static final int ITEMS_TEXT_COLOR = -16777216;
    private static final int LABEL_OFFSET = 8;
    private static final int MIN_DELTA_FOR_SCROLLING = 1;
    private static final int PADDING = 10;
    private static final int SCROLLING_DURATION = 400;
    private static final int[] SHADOWS_COLORS;
    private static final int VALUE_TEXT_COLOR = -268435456;
    private final int ITEM_OFFSET;
    private final int MESSAGE_JUSTIFY;
    private final int MESSAGE_SCROLL;
    public int TEXT_SIZE;
    private WheelAdapter adapter;
    private Handler animationHandler;
    private GradientDrawable bottomShadow;
    private Drawable centerDrawable;
    private List<OnWheelChangedListener> changingListeners;
    private int currentItem;
    private GestureDetector gestureDetector;
    private SimpleOnGestureListener gestureListener;
    boolean isCyclic;
    private boolean isScrollingPerformed;
    private int itemHeight;
    private StaticLayout itemsLayout;
    private TextPaint itemsPaint;
    private int itemsWidth;
    private String label;
    private StaticLayout labelLayout;
    private int labelWidth;
    private int lastScrollY;
    private Scroller scroller;
    private List<OnWheelScrollListener> scrollingListeners;
    private int scrollingOffset;
    private GradientDrawable topShadow;
    private StaticLayout valueLayout;
    private TextPaint valuePaint;
    private int visibleItems;

    /* renamed from: com.ifoer.expedition.wheelview.WheelView.1 */
    class C05021 extends SimpleOnGestureListener {
        C05021() {
        }

        public boolean onDown(MotionEvent e) {
            if (!WheelView.this.isScrollingPerformed) {
                return false;
            }
            WheelView.this.scroller.forceFinished(true);
            WheelView.this.clearMessages();
            return true;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            WheelView.this.startScrolling();
            WheelView.this.doScroll((int) (-distanceY));
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int maxY;
            int minY;
            WheelView.this.lastScrollY = (WheelView.this.currentItem * WheelView.this.getItemHeight()) + WheelView.this.scrollingOffset;
            if (WheelView.this.isCyclic) {
                maxY = Integer.MAX_VALUE;
            } else {
                maxY = WheelView.this.adapter.getItemsCount() * WheelView.this.getItemHeight();
            }
            if (WheelView.this.isCyclic) {
                minY = -maxY;
            } else {
                minY = 0;
            }
            WheelView.this.scroller.fling(0, WheelView.this.lastScrollY, 0, ((int) (-velocityY)) / 2, 0, 0, minY, maxY);
            WheelView.this.setNextMessage(0);
            return true;
        }
    }

    /* renamed from: com.ifoer.expedition.wheelview.WheelView.2 */
    class C05032 extends Handler {
        C05032() {
        }

        public void handleMessage(Message msg) {
            WheelView.this.scroller.computeScrollOffset();
            int currY = WheelView.this.scroller.getCurrY();
            int delta = WheelView.this.lastScrollY - currY;
            WheelView.this.lastScrollY = currY;
            if (delta != 0) {
                WheelView.this.doScroll(delta);
            }
            if (Math.abs(currY - WheelView.this.scroller.getFinalY()) < WheelView.MIN_DELTA_FOR_SCROLLING) {
                currY = WheelView.this.scroller.getFinalY();
                WheelView.this.scroller.forceFinished(true);
            }
            if (!WheelView.this.scroller.isFinished()) {
                WheelView.this.animationHandler.sendEmptyMessage(msg.what);
            } else if (msg.what == 0) {
                WheelView.this.justify();
            } else {
                WheelView.this.finishScrolling();
            }
        }
    }

    static {
        SHADOWS_COLORS = new int[]{-15658735, 11184810, 11184810};
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.ITEM_OFFSET = this.TEXT_SIZE / DEF_VISIBLE_ITEMS;
        this.adapter = null;
        this.currentItem = 0;
        this.itemsWidth = 0;
        this.labelWidth = 0;
        this.visibleItems = DEF_VISIBLE_ITEMS;
        this.itemHeight = 0;
        this.isCyclic = false;
        this.changingListeners = new LinkedList();
        this.scrollingListeners = new LinkedList();
        this.gestureListener = new C05021();
        this.MESSAGE_SCROLL = 0;
        this.MESSAGE_JUSTIFY = MIN_DELTA_FOR_SCROLLING;
        this.animationHandler = new C05032();
        initData(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ITEM_OFFSET = this.TEXT_SIZE / DEF_VISIBLE_ITEMS;
        this.adapter = null;
        this.currentItem = 0;
        this.itemsWidth = 0;
        this.labelWidth = 0;
        this.visibleItems = DEF_VISIBLE_ITEMS;
        this.itemHeight = 0;
        this.isCyclic = false;
        this.changingListeners = new LinkedList();
        this.scrollingListeners = new LinkedList();
        this.gestureListener = new C05021();
        this.MESSAGE_SCROLL = 0;
        this.MESSAGE_JUSTIFY = MIN_DELTA_FOR_SCROLLING;
        this.animationHandler = new C05032();
        initData(context);
    }

    public WheelView(Context context) {
        super(context);
        this.ITEM_OFFSET = this.TEXT_SIZE / DEF_VISIBLE_ITEMS;
        this.adapter = null;
        this.currentItem = 0;
        this.itemsWidth = 0;
        this.labelWidth = 0;
        this.visibleItems = DEF_VISIBLE_ITEMS;
        this.itemHeight = 0;
        this.isCyclic = false;
        this.changingListeners = new LinkedList();
        this.scrollingListeners = new LinkedList();
        this.gestureListener = new C05021();
        this.MESSAGE_SCROLL = 0;
        this.MESSAGE_JUSTIFY = MIN_DELTA_FOR_SCROLLING;
        this.animationHandler = new C05032();
        initData(context);
    }

    private void initData(Context context) {
        this.gestureDetector = new GestureDetector(context, this.gestureListener);
        this.gestureDetector.setIsLongpressEnabled(false);
        this.scroller = new Scroller(context);
    }

    public WheelAdapter getAdapter() {
        return this.adapter;
    }

    public void setAdapter(WheelAdapter adapter) {
        this.adapter = adapter;
        invalidateLayouts();
        invalidate();
    }

    public void setInterpolator(Interpolator interpolator) {
        this.scroller.forceFinished(true);
        this.scroller = new Scroller(getContext(), interpolator);
    }

    public int getVisibleItems() {
        return this.visibleItems;
    }

    public void setVisibleItems(int count) {
        this.visibleItems = count;
        invalidate();
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String newLabel) {
        if (this.label == null || !this.label.equals(newLabel)) {
            this.label = newLabel;
            this.labelLayout = null;
            invalidate();
        }
    }

    public void addChangingListener(OnWheelChangedListener listener) {
        this.changingListeners.add(listener);
    }

    public void removeChangingListener(OnWheelChangedListener listener) {
        this.changingListeners.remove(listener);
    }

    protected void notifyChangingListeners(int oldValue, int newValue) {
        for (OnWheelChangedListener listener : this.changingListeners) {
            listener.onChanged(this, oldValue, newValue);
        }
    }

    public void addScrollingListener(OnWheelScrollListener listener) {
        this.scrollingListeners.add(listener);
    }

    public void removeScrollingListener(OnWheelScrollListener listener) {
        this.scrollingListeners.remove(listener);
    }

    protected void notifyScrollingListenersAboutStart() {
        for (OnWheelScrollListener listener : this.scrollingListeners) {
            listener.onScrollingStarted(this);
        }
    }

    protected void notifyScrollingListenersAboutEnd() {
        for (OnWheelScrollListener listener : this.scrollingListeners) {
            listener.onScrollingFinished(this);
        }
    }

    public int getCurrentItem() {
        return this.currentItem;
    }

    public void setCurrentItem(int index, boolean animated) {
        if (this.adapter != null && this.adapter.getItemsCount() != 0) {
            if (index < 0 || index >= this.adapter.getItemsCount()) {
                if (this.isCyclic) {
                    while (index < 0) {
                        index += this.adapter.getItemsCount();
                    }
                    index %= this.adapter.getItemsCount();
                } else {
                    return;
                }
            }
            if (index == this.currentItem) {
                return;
            }
            if (animated) {
                scroll(index - this.currentItem, SCROLLING_DURATION);
                return;
            }
            invalidateLayouts();
            int old = this.currentItem;
            this.currentItem = index;
            notifyChangingListeners(old, this.currentItem);
            invalidate();
        }
    }

    public void setCurrentItem(int index) {
        setCurrentItem(index, false);
    }

    public boolean isCyclic() {
        return this.isCyclic;
    }

    public void setCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;
        invalidate();
        invalidateLayouts();
    }

    private void invalidateLayouts() {
        this.itemsLayout = null;
        this.valueLayout = null;
        this.scrollingOffset = 0;
    }

    private void initResourcesIfNecessary() {
        if (this.itemsPaint == null) {
            this.itemsPaint = new TextPaint(33);
            this.itemsPaint.setTextSize((float) this.TEXT_SIZE);
        }
        if (this.valuePaint == null) {
            this.valuePaint = new TextPaint(37);
            this.valuePaint.setTextSize((float) this.TEXT_SIZE);
            this.valuePaint.setShadowLayer(Binarize.OTSU_SCORE_FRACTION, 0.0f, Binarize.OTSU_SCORE_FRACTION, -4144960);
        }
        if (this.centerDrawable == null) {
            this.centerDrawable = getContext().getResources().getDrawable(C0136R.drawable.wheel_val);
        }
        if (this.topShadow == null) {
            this.topShadow = new GradientDrawable(Orientation.TOP_BOTTOM, SHADOWS_COLORS);
        }
        if (this.bottomShadow == null) {
            this.bottomShadow = new GradientDrawable(Orientation.BOTTOM_TOP, SHADOWS_COLORS);
        }
        setBackgroundResource(C0136R.drawable.wheel_bg);
    }

    private int getDesiredHeight(Layout layout) {
        if (layout == null) {
            return 0;
        }
        return Math.max(((getItemHeight() * this.visibleItems) - (this.ITEM_OFFSET * 2)) - 15, getSuggestedMinimumHeight());
    }

    private String getTextItem(int index) {
        if (this.adapter == null || this.adapter.getItemsCount() == 0) {
            return null;
        }
        int count = this.adapter.getItemsCount();
        if ((index < 0 || index >= count) && !this.isCyclic) {
            return null;
        }
        while (index < 0) {
            index += count;
        }
        return this.adapter.getItem(index % count);
    }

    private String buildText(boolean useCurrentValue) {
        StringBuilder itemsText = new StringBuilder();
        int addItems = (this.visibleItems / 2) + MIN_DELTA_FOR_SCROLLING;
        int i = this.currentItem - addItems;
        while (i <= this.currentItem + addItems) {
            if (useCurrentValue || i != this.currentItem) {
                String text = getTextItem(i);
                if (text != null) {
                    itemsText.append(text);
                }
            }
            if (i < this.currentItem + addItems) {
                itemsText.append(SpecilApiUtil.LINE_SEP);
            }
            i += MIN_DELTA_FOR_SCROLLING;
        }
        return itemsText.toString();
    }

    private int getMaxTextLength() {
        WheelAdapter adapter = getAdapter();
        if (adapter == null) {
            return 0;
        }
        int adapterLength = adapter.getMaximumLength();
        if (adapterLength > 0) {
            return adapterLength;
        }
        String maxText = null;
        for (int i = Math.max(this.currentItem - (this.visibleItems / 2), 0); i < Math.min(this.currentItem + this.visibleItems, adapter.getItemsCount()); i += MIN_DELTA_FOR_SCROLLING) {
            String text = adapter.getItem(i);
            if (text != null && (maxText == null || maxText.length() < text.length())) {
                maxText = text;
            }
        }
        if (maxText != null) {
            return maxText.length();
        }
        return 0;
    }

    private int getItemHeight() {
        if (this.itemHeight != 0) {
            return this.itemHeight;
        }
        if (this.itemsLayout == null || this.itemsLayout.getLineCount() <= 2) {
            return getHeight() / this.visibleItems;
        }
        this.itemHeight = this.itemsLayout.getLineTop(2) - this.itemsLayout.getLineTop(MIN_DELTA_FOR_SCROLLING);
        return this.itemHeight;
    }

    private int calculateLayoutWidth(int widthSize, int mode) {
        initResourcesIfNecessary();
        int width = widthSize;
        int maxLength = getMaxTextLength();
        if (maxLength > 0) {
            this.itemsWidth = (int) (((float) maxLength) * FloatMath.ceil(Layout.getDesiredWidth(Contact.RELATION_ASK, this.itemsPaint)));
        } else {
            this.itemsWidth = 0;
        }
        this.itemsWidth += PADDING;
        this.labelWidth = 0;
        if (this.label != null && this.label.length() > 0) {
            this.labelWidth = (int) FloatMath.ceil(Layout.getDesiredWidth(this.label, this.valuePaint));
        }
        boolean recalculate = false;
        if (mode == 1073741824) {
            width = widthSize;
            recalculate = true;
        } else {
            width = (this.itemsWidth + this.labelWidth) + 20;
            if (this.labelWidth > 0) {
                width += LABEL_OFFSET;
            }
            width = Math.max(width, getSuggestedMinimumWidth());
            if (mode == Integer.MIN_VALUE && widthSize < width) {
                width = widthSize;
                recalculate = true;
            }
        }
        if (recalculate) {
            int pureWidth = (width - 8) - 20;
            if (pureWidth <= 0) {
                this.labelWidth = 0;
                this.itemsWidth = 0;
            }
            if (this.labelWidth > 0) {
                this.itemsWidth = (int) ((((double) this.itemsWidth) * ((double) pureWidth)) / ((double) (this.itemsWidth + this.labelWidth)));
                this.labelWidth = pureWidth - this.itemsWidth;
            } else {
                this.itemsWidth = pureWidth + LABEL_OFFSET;
            }
        }
        if (this.itemsWidth > 0) {
            createLayouts(this.itemsWidth, this.labelWidth);
        }
        return width;
    }

    private void createLayouts(int widthItems, int widthLabel) {
        Alignment alignment;
        String text = null;
        if (this.itemsLayout == null || this.itemsLayout.getWidth() > widthItems) {
            CharSequence buildText = buildText(this.isScrollingPerformed);
            TextPaint textPaint = this.itemsPaint;
            if (widthLabel > 0) {
                alignment = Alignment.ALIGN_OPPOSITE;
            } else {
                alignment = Alignment.ALIGN_CENTER;
            }
            this.itemsLayout = new StaticLayout(buildText, textPaint, widthItems, alignment, 1.0f, 15.0f, false);
        } else {
            this.itemsLayout.increaseWidthTo(widthItems);
        }
        if (!this.isScrollingPerformed && (this.valueLayout == null || this.valueLayout.getWidth() > widthItems)) {
            if (getAdapter() != null) {
                text = getAdapter().getItem(this.currentItem);
            }
            buildText = text != null ? text : XmlPullParser.NO_NAMESPACE;
            textPaint = this.valuePaint;
            if (widthLabel > 0) {
                alignment = Alignment.ALIGN_OPPOSITE;
            } else {
                alignment = Alignment.ALIGN_CENTER;
            }
            this.valueLayout = new StaticLayout(buildText, textPaint, widthItems, alignment, 1.0f, 15.0f, false);
        } else if (this.isScrollingPerformed) {
            this.valueLayout = null;
        } else {
            this.valueLayout.increaseWidthTo(widthItems);
        }
        if (widthLabel <= 0) {
            return;
        }
        if (this.labelLayout == null || this.labelLayout.getWidth() > widthLabel) {
            this.labelLayout = new StaticLayout(this.label, this.valuePaint, widthLabel, Alignment.ALIGN_NORMAL, 1.0f, 15.0f, false);
        } else {
            this.labelLayout.increaseWidthTo(widthLabel);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = calculateLayoutWidth(widthSize, widthMode);
        if (heightMode == 1073741824) {
            height = heightSize;
        } else {
            height = getDesiredHeight(this.itemsLayout);
            if (heightMode == Integer.MIN_VALUE) {
                height = Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.itemsLayout == null) {
            if (this.itemsWidth == 0) {
                calculateLayoutWidth(getWidth(), 1073741824);
            } else {
                createLayouts(this.itemsWidth, this.labelWidth);
            }
        }
        if (this.itemsWidth > 0) {
            canvas.save();
            canvas.translate(10.0f, (float) (-this.ITEM_OFFSET));
            drawItems(canvas);
            drawValue(canvas);
            canvas.restore();
        }
        drawCenterRect(canvas);
        drawShadows(canvas);
    }

    private void drawShadows(Canvas canvas) {
        this.topShadow.setBounds(0, 0, getWidth(), getHeight() / this.visibleItems);
        this.topShadow.draw(canvas);
        this.bottomShadow.setBounds(0, getHeight() - (getHeight() / this.visibleItems), getWidth(), getHeight());
        this.bottomShadow.draw(canvas);
    }

    private void drawValue(Canvas canvas) {
        this.valuePaint.setColor(VALUE_TEXT_COLOR);
        this.valuePaint.drawableState = getDrawableState();
        Rect bounds = new Rect();
        this.itemsLayout.getLineBounds(this.visibleItems / 2, bounds);
        if (this.labelLayout != null) {
            canvas.save();
            canvas.translate((float) (this.itemsLayout.getWidth() + LABEL_OFFSET), (float) bounds.top);
            this.labelLayout.draw(canvas);
            canvas.restore();
        }
        if (this.valueLayout != null) {
            canvas.save();
            canvas.translate(0.0f, (float) (bounds.top + this.scrollingOffset));
            this.valueLayout.draw(canvas);
            canvas.restore();
        }
    }

    private void drawItems(Canvas canvas) {
        canvas.save();
        canvas.translate(0.0f, (float) ((-this.itemsLayout.getLineTop(MIN_DELTA_FOR_SCROLLING)) + this.scrollingOffset));
        this.itemsPaint.setColor(ITEMS_TEXT_COLOR);
        this.itemsPaint.drawableState = getDrawableState();
        this.itemsLayout.draw(canvas);
        canvas.restore();
    }

    private void drawCenterRect(Canvas canvas) {
        int center = getHeight() / 2;
        int offset = getItemHeight() / 2;
        this.centerDrawable.setBounds(0, center - offset, getWidth(), center + offset);
        this.centerDrawable.draw(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!(getAdapter() == null || this.gestureDetector.onTouchEvent(event) || event.getAction() != MIN_DELTA_FOR_SCROLLING)) {
            justify();
        }
        return true;
    }

    private void doScroll(int delta) {
        this.scrollingOffset += delta;
        int count = this.scrollingOffset / getItemHeight();
        int pos = this.currentItem - count;
        if (this.isCyclic && this.adapter.getItemsCount() > 0) {
            while (pos < 0) {
                pos += this.adapter.getItemsCount();
            }
            pos %= this.adapter.getItemsCount();
        } else if (!this.isScrollingPerformed) {
            pos = Math.min(Math.max(pos, 0), this.adapter.getItemsCount() - 1);
        } else if (pos < 0) {
            count = this.currentItem;
            pos = 0;
        } else if (pos >= this.adapter.getItemsCount()) {
            count = (this.currentItem - this.adapter.getItemsCount()) + MIN_DELTA_FOR_SCROLLING;
            pos = this.adapter.getItemsCount() - 1;
        }
        int offset = this.scrollingOffset;
        if (pos != this.currentItem) {
            setCurrentItem(pos, false);
        } else {
            invalidate();
        }
        this.scrollingOffset = offset - (getItemHeight() * count);
        if (this.scrollingOffset > getHeight()) {
            this.scrollingOffset = (this.scrollingOffset % getHeight()) + getHeight();
        }
    }

    private void setNextMessage(int message) {
        clearMessages();
        this.animationHandler.sendEmptyMessage(message);
    }

    private void clearMessages() {
        this.animationHandler.removeMessages(0);
        this.animationHandler.removeMessages(MIN_DELTA_FOR_SCROLLING);
    }

    private void justify() {
        if (this.adapter != null) {
            this.lastScrollY = 0;
            int offset = this.scrollingOffset;
            int itemHeight = getItemHeight();
            boolean needToIncrease = offset > 0 ? this.currentItem < this.adapter.getItemsCount() : this.currentItem > 0;
            if ((this.isCyclic || needToIncrease) && Math.abs((float) offset) > ((float) itemHeight) / 2.0f) {
                offset = offset < 0 ? offset + (itemHeight + MIN_DELTA_FOR_SCROLLING) : offset - (itemHeight + MIN_DELTA_FOR_SCROLLING);
            }
            if (Math.abs(offset) > MIN_DELTA_FOR_SCROLLING) {
                this.scroller.startScroll(0, 0, 0, offset, SCROLLING_DURATION);
                setNextMessage(MIN_DELTA_FOR_SCROLLING);
                return;
            }
            finishScrolling();
        }
    }

    private void startScrolling() {
        if (!this.isScrollingPerformed) {
            this.isScrollingPerformed = true;
            notifyScrollingListenersAboutStart();
        }
    }

    void finishScrolling() {
        if (this.isScrollingPerformed) {
            notifyScrollingListenersAboutEnd();
            this.isScrollingPerformed = false;
        }
        invalidateLayouts();
        invalidate();
    }

    public void scroll(int itemsToScroll, int time) {
        this.scroller.forceFinished(true);
        this.lastScrollY = this.scrollingOffset;
        int offset = itemsToScroll * getItemHeight();
        this.scroller.startScroll(0, this.lastScrollY, 0, offset - this.lastScrollY, time);
        setNextMessage(0);
        startScrolling();
    }
}

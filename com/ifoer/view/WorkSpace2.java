package com.ifoer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.event.OnScrollCompleteListener;
import com.ifoer.event.ScrollEvent;
import com.ifoer.event.ScrollEventAdapter;
import com.ifoer.ui.MainMenuActivity;

public class WorkSpace2 extends ViewGroup {
    private static final int SNAP_VELOCITY = 600;
    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SCROLLING = 1;
    private MainMenuActivity activity;
    Context context;
    private int mCurScreen;
    private int mDefaultScreen;
    private float mLastMotionX;
    private Scroller mScroller;
    private int mTouchSlop;
    private int mTouchState;
    private VelocityTracker mVelocityTracker;
    private ScrollEventAdapter scrEventAdapter;

    public int getmCurScreen() {
        return this.mCurScreen;
    }

    public void setmCurScreen(int mCurScreen) {
        this.mCurScreen = mCurScreen;
    }

    public WorkSpace2(Context context, AttributeSet attrs) {
        this(context, attrs, TOUCH_STATE_REST);
    }

    public WorkSpace2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        this.mDefaultScreen = TOUCH_STATE_REST;
        this.mTouchState = TOUCH_STATE_REST;
        this.scrEventAdapter = new ScrollEventAdapter();
        this.mScroller = new Scroller(context);
        this.context = context;
        this.mCurScreen = this.mDefaultScreen;
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.activity = (MainMenuActivity) context;
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = TOUCH_STATE_REST;
        int childCount = getChildCount();
        for (int i = TOUCH_STATE_REST; i < childCount; i += TOUCH_STATE_SCROLLING) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != 8) {
                int childWidth = childView.getMeasuredWidth();
                childView.layout(childLeft, TOUCH_STATE_REST, childLeft + childWidth, childView.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int count = getChildCount();
        for (int i = TOUCH_STATE_REST; i < count; i += TOUCH_STATE_SCROLLING) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
        scrollTo(this.mCurScreen * width, TOUCH_STATE_REST);
    }

    public void snapToDestination() {
        int screenWidth = getWidth();
        int destScreen = (getScrollX() + (screenWidth / 2)) / screenWidth;
        if (destScreen == this.mCurScreen) {
            if (destScreen == 0) {
                snapToScreen(TOUCH_STATE_REST);
            }
            if (destScreen == 2) {
                snapToScreen(2);
            }
            if (destScreen == TOUCH_STATE_SCROLLING) {
                snapToScreen(TOUCH_STATE_SCROLLING);
            }
        } else if (destScreen == 3) {
            snapToScreen(TOUCH_STATE_REST);
        } else {
            snapToScreen(destScreen);
        }
    }

    public void snapToScreen(int whichScreen) {
        if (whichScreen == 0) {
            this.activity.page1.setImageDrawable(getResources().getDrawable(C0136R.drawable.page_indicator_focused));
            this.activity.page2.setImageDrawable(getResources().getDrawable(C0136R.drawable.page_indicator_unfocused));
        } else if (whichScreen == TOUCH_STATE_SCROLLING) {
            this.activity.page2.setImageDrawable(getResources().getDrawable(C0136R.drawable.page_indicator_focused));
            this.activity.page1.setImageDrawable(getResources().getDrawable(C0136R.drawable.page_indicator_unfocused));
        }
        whichScreen = Math.max(TOUCH_STATE_REST, Math.min(whichScreen, getChildCount() - 1));
        if (getScrollX() != getWidth() * whichScreen) {
            int delta = (getWidth() * whichScreen) - getScrollX();
            this.mScroller.startScroll(getScrollX(), TOUCH_STATE_REST, delta, TOUCH_STATE_REST, Math.abs(delta) * 2);
            if (this.mCurScreen != whichScreen) {
                this.scrEventAdapter.notifyEvent(new ScrollEvent(whichScreen));
            }
            this.mCurScreen = whichScreen;
            invalidate();
        }
    }

    public void setToScreen(int whichScreen) {
        whichScreen = Math.max(TOUCH_STATE_REST, Math.min(whichScreen, getChildCount() - 1));
        this.mCurScreen = whichScreen;
        scrollTo(getWidth() * whichScreen, TOUCH_STATE_REST);
        this.scrEventAdapter.notifyEvent(new ScrollEvent(whichScreen));
        invalidate();
    }

    public int getCurScreen() {
        return this.mCurScreen;
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            postInvalidate();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(event);
        int action = event.getAction();
        float x = event.getX();
        switch (action) {
            case TOUCH_STATE_REST /*0*/:
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                this.mLastMotionX = x;
                break;
            case TOUCH_STATE_SCROLLING /*1*/:
                VelocityTracker velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int velocityX = (int) velocityTracker.getXVelocity();
                if (velocityX > SNAP_VELOCITY && this.mCurScreen > 0) {
                    snapToScreen(this.mCurScreen - 1);
                } else if (velocityX >= -600 || this.mCurScreen >= getChildCount() - 1) {
                    snapToDestination();
                } else {
                    snapToScreen(this.mCurScreen + TOUCH_STATE_SCROLLING);
                }
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                }
                this.mTouchState = TOUCH_STATE_REST;
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                int deltaX = (int) (this.mLastMotionX - x);
                this.mLastMotionX = x;
                scrollBy(deltaX, TOUCH_STATE_REST);
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                this.mTouchState = TOUCH_STATE_REST;
                break;
        }
        return true;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == 2 && this.mTouchState != 0) {
            return true;
        }
        float x = ev.getX();
        switch (action) {
            case TOUCH_STATE_REST /*0*/:
                postInvalidate();
                this.mLastMotionX = x;
                this.mTouchState = this.mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
                break;
            case TOUCH_STATE_SCROLLING /*1*/:
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                postInvalidate();
                this.mTouchState = TOUCH_STATE_REST;
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                if (((int) Math.abs(this.mLastMotionX - x)) > this.mTouchSlop) {
                    this.mTouchState = TOUCH_STATE_SCROLLING;
                    break;
                }
                break;
        }
        if (this.mTouchState == 0) {
            return false;
        }
        return true;
    }

    public void setOnScrollCompleteLinstenner(OnScrollCompleteListener listener) {
        this.scrEventAdapter.addListener(listener);
    }
}

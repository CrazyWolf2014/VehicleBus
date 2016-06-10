package com.ifoer.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;
import android.widget.Scroller;
import java.util.LinkedList;
import java.util.Queue;

public class HorizontialListView extends AdapterView<ListAdapter> {
    protected ListAdapter mAdapter;
    public boolean mAlwaysOverrideTouch;
    protected int mCurrentX;
    private boolean mDataChanged;
    private DataSetObserver mDataObserver;
    private int mDisplayOffset;
    private GestureDetector mGesture;
    private int mLeftViewIndex;
    private int mMaxX;
    protected int mNextX;
    private OnGestureListener mOnGesture;
    private OnItemClickListener mOnItemClicked;
    private OnItemSelectedListener mOnItemSelected;
    private Queue<View> mRemovedViewQueue;
    private int mRightViewIndex;
    protected Scroller mScroller;

    /* renamed from: com.ifoer.view.HorizontialListView.1 */
    class C07671 extends DataSetObserver {
        C07671() {
        }

        public void onChanged() {
            synchronized (HorizontialListView.this) {
                HorizontialListView.this.mDataChanged = true;
            }
            HorizontialListView.this.invalidate();
            HorizontialListView.this.requestLayout();
        }

        public void onInvalidated() {
            HorizontialListView.this.reset();
            HorizontialListView.this.invalidate();
            HorizontialListView.this.requestLayout();
        }
    }

    /* renamed from: com.ifoer.view.HorizontialListView.2 */
    class C07682 extends SimpleOnGestureListener {
        C07682() {
        }

        public boolean onDown(MotionEvent e) {
            return HorizontialListView.this.onDown(e);
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return HorizontialListView.this.onFling(e1, e2, velocityX, velocityY);
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            synchronized (HorizontialListView.this) {
                HorizontialListView horizontialListView = HorizontialListView.this;
                horizontialListView.mNextX += (int) distanceX;
            }
            HorizontialListView.this.requestLayout();
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            Rect viewRect = new Rect();
            for (int i = 0; i < HorizontialListView.this.getChildCount(); i++) {
                View child = HorizontialListView.this.getChildAt(i);
                viewRect.set(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
                if (viewRect.contains((int) e.getX(), (int) e.getY())) {
                    if (HorizontialListView.this.mOnItemClicked != null) {
                        HorizontialListView.this.mOnItemClicked.onItemClick(HorizontialListView.this, child, (HorizontialListView.this.mLeftViewIndex + 1) + i, HorizontialListView.this.mAdapter.getItemId((HorizontialListView.this.mLeftViewIndex + 1) + i));
                    }
                    if (HorizontialListView.this.mOnItemSelected != null) {
                        HorizontialListView.this.mOnItemSelected.onItemSelected(HorizontialListView.this, child, (HorizontialListView.this.mLeftViewIndex + 1) + i, HorizontialListView.this.mAdapter.getItemId((HorizontialListView.this.mLeftViewIndex + 1) + i));
                    }
                    return true;
                }
            }
            return true;
        }
    }

    /* renamed from: com.ifoer.view.HorizontialListView.3 */
    class C07693 implements Runnable {
        C07693() {
        }

        public void run() {
            HorizontialListView.this.requestLayout();
        }
    }

    public HorizontialListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mAlwaysOverrideTouch = true;
        this.mLeftViewIndex = -1;
        this.mRightViewIndex = 0;
        this.mMaxX = Integer.MAX_VALUE;
        this.mDisplayOffset = 0;
        this.mRemovedViewQueue = new LinkedList();
        this.mDataChanged = false;
        this.mDataObserver = new C07671();
        this.mOnGesture = new C07682();
        initView();
    }

    private synchronized void initView() {
        this.mLeftViewIndex = -1;
        this.mRightViewIndex = 0;
        this.mDisplayOffset = 0;
        this.mCurrentX = 0;
        this.mNextX = 0;
        this.mMaxX = Integer.MAX_VALUE;
        this.mScroller = new Scroller(getContext());
        this.mGesture = new GestureDetector(getContext(), this.mOnGesture);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.mOnItemSelected = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClicked = listener;
    }

    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    public View getSelectedView() {
        return null;
    }

    public void setAdapter(ListAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataObserver);
        }
        this.mAdapter = adapter;
        this.mAdapter.registerDataSetObserver(this.mDataObserver);
        reset();
    }

    private synchronized void reset() {
        initView();
        removeAllViewsInLayout();
        requestLayout();
    }

    public void setSelection(int position) {
    }

    private void addAndMeasureChild(View child, int viewPos) {
        LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(-1, -1);
        }
        addViewInLayout(child, viewPos, params, true);
        child.measure(MeasureSpec.makeMeasureSpec(getWidth(), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(getHeight(), Integer.MIN_VALUE));
    }

    protected synchronized void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mAdapter != null) {
            if (this.mDataChanged) {
                int oldCurrentX = this.mCurrentX;
                initView();
                removeAllViewsInLayout();
                this.mNextX = oldCurrentX;
                this.mDataChanged = false;
            }
            if (this.mScroller.computeScrollOffset()) {
                this.mNextX = this.mScroller.getCurrX();
            }
            if (this.mNextX < 0) {
                this.mNextX = 0;
                this.mScroller.forceFinished(true);
            }
            if (this.mNextX > this.mMaxX) {
                this.mNextX = this.mMaxX;
                this.mScroller.forceFinished(true);
            }
            int dx = this.mCurrentX - this.mNextX;
            removeNonVisibleItems(dx);
            fillList(dx);
            positionItems(dx);
            this.mCurrentX = this.mNextX;
            if (!this.mScroller.isFinished()) {
                post(new C07693());
            }
        }
    }

    private void fillList(int dx) {
        int edge = 0;
        View child = getChildAt(getChildCount() - 1);
        if (child != null) {
            edge = child.getRight();
        }
        fillListRight(edge, dx);
        edge = 0;
        child = getChildAt(0);
        if (child != null) {
            edge = child.getLeft();
        }
        fillListLeft(edge, dx);
    }

    private void fillListRight(int rightEdge, int dx) {
        while (rightEdge + dx < getWidth() && this.mRightViewIndex < this.mAdapter.getCount()) {
            View child = this.mAdapter.getView(this.mRightViewIndex, (View) this.mRemovedViewQueue.poll(), this);
            addAndMeasureChild(child, -1);
            rightEdge += child.getMeasuredWidth();
            if (this.mRightViewIndex == this.mAdapter.getCount() - 1) {
                this.mMaxX = (this.mCurrentX + rightEdge) - getWidth();
            }
            this.mRightViewIndex++;
        }
    }

    private void fillListLeft(int leftEdge, int dx) {
        while (leftEdge + dx > 0 && this.mLeftViewIndex >= 0) {
            View child = this.mAdapter.getView(this.mLeftViewIndex, (View) this.mRemovedViewQueue.poll(), this);
            addAndMeasureChild(child, 0);
            leftEdge -= child.getMeasuredWidth();
            this.mLeftViewIndex--;
            this.mDisplayOffset -= child.getMeasuredWidth();
        }
    }

    private void removeNonVisibleItems(int dx) {
        View child = getChildAt(0);
        while (child != null && child.getRight() + dx <= 0) {
            this.mDisplayOffset += child.getMeasuredWidth();
            this.mRemovedViewQueue.offer(child);
            removeViewInLayout(child);
            this.mLeftViewIndex++;
            child = getChildAt(0);
        }
        child = getChildAt(getChildCount() - 1);
        while (child != null && child.getLeft() + dx >= getWidth()) {
            this.mRemovedViewQueue.offer(child);
            removeViewInLayout(child);
            this.mRightViewIndex--;
            child = getChildAt(getChildCount() - 1);
        }
    }

    private void positionItems(int dx) {
        if (getChildCount() > 0) {
            this.mDisplayOffset += dx;
            int left = this.mDisplayOffset;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int childWidth = child.getMeasuredWidth();
                child.layout(left, 0, left + childWidth, child.getMeasuredHeight());
                left += childWidth;
            }
        }
    }

    public synchronized void scrollTo(int x) {
        this.mScroller.startScroll(this.mNextX, 0, x - this.mNextX, 0);
        requestLayout();
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return this.mGesture.onTouchEvent(ev);
    }

    protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        synchronized (this) {
            this.mScroller.fling(this.mNextX, 0, (int) (-velocityX), 0, 0, this.mMaxX, 0, 0);
        }
        requestLayout();
        return true;
    }

    protected boolean onDown(MotionEvent e) {
        this.mScroller.forceFinished(true);
        return true;
    }
}

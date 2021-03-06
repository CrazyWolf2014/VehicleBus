package android.support.v4.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.expeditionphone.WelcomeActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.ksoap2.transport.ServiceConnection;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;

public class ViewPager extends ViewGroup {
    private static final int CLOSE_ENOUGH = 2;
    private static final Comparator<ItemInfo> COMPARATOR;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int INVALID_POINTER = -1;
    private static final int[] LAYOUT_ATTRS;
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "ViewPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator;
    private int mActivePointerId;
    private PagerAdapter mAdapter;
    private OnAdapterChangeListener mAdapterChangeListener;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCloseEnough;
    private int mCurItem;
    private int mDecorChildCount;
    private int mDefaultGutterSize;
    private long mFakeDragBeginTime;
    private boolean mFakeDragging;
    private boolean mFirstLayout;
    private float mFirstOffset;
    private int mFlingDistance;
    private int mGutterSize;
    private boolean mIgnoreGutter;
    private boolean mInLayout;
    private float mInitialMotionX;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private final ArrayList<ItemInfo> mItems;
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset;
    private EdgeEffectCompat mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets;
    private PagerObserver mObserver;
    private int mOffscreenPageLimit;
    private OnPageChangeListener mOnPageChangeListener;
    private int mPageMargin;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState;
    private ClassLoader mRestoredClassLoader;
    private int mRestoredCurItem;
    private EdgeEffectCompat mRightEdge;
    private int mScrollState;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private final ItemInfo mTempItem;
    private final Rect mTempRect;
    private int mTopPageBounds;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    /* renamed from: android.support.v4.view.ViewPager.1 */
    static class C00161 implements Comparator<ItemInfo> {
        C00161() {
        }

        public int compare(ItemInfo lhs, ItemInfo rhs) {
            return lhs.position - rhs.position;
        }
    }

    /* renamed from: android.support.v4.view.ViewPager.2 */
    static class C00172 implements Interpolator {
        C00172() {
        }

        public float getInterpolation(float t) {
            t -= 1.0f;
            return ((((t * t) * t) * t) * t) + 1.0f;
        }
    }

    interface Decor {
    }

    static class ItemInfo {
        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;

        ItemInfo() {
        }
    }

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        public int gravity;
        public boolean isDecor;
        public boolean needsMeasure;
        public float widthFactor;

        public LayoutParams() {
            super(ViewPager.INVALID_POINTER, ViewPager.INVALID_POINTER);
            this.widthFactor = 0.0f;
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.widthFactor = 0.0f;
            TypedArray a = context.obtainStyledAttributes(attrs, ViewPager.LAYOUT_ATTRS);
            this.gravity = a.getInteger(ViewPager.SCROLL_STATE_IDLE, 48);
            a.recycle();
        }
    }

    interface OnAdapterChangeListener {
        void onAdapterChanged(PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2);
    }

    public interface OnPageChangeListener {
        void onPageScrollStateChanged(int i);

        void onPageScrolled(int i, float f, int i2);

        void onPageSelected(int i);
    }

    private class PagerObserver extends DataSetObserver {
        private PagerObserver() {
        }

        public void onChanged() {
            ViewPager.this.dataSetChanged();
        }

        public void onInvalidated() {
            ViewPager.this.dataSetChanged();
        }
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR;
        Parcelable adapterState;
        ClassLoader loader;
        int position;

        /* renamed from: android.support.v4.view.ViewPager.SavedState.1 */
        static class C10051 implements ParcelableCompatCreatorCallbacks<SavedState> {
            C10051() {
            }

            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.position);
            out.writeParcelable(this.adapterState, flags);
        }

        public String toString() {
            return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
        }

        static {
            CREATOR = ParcelableCompat.newCreator(new C10051());
        }

        SavedState(Parcel in, ClassLoader loader) {
            super(in);
            if (loader == null) {
                loader = getClass().getClassLoader();
            }
            this.position = in.readInt();
            this.adapterState = in.readParcelable(loader);
            this.loader = loader;
        }
    }

    class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
        MyAccessibilityDelegate() {
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(ViewPager.class.getName());
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            boolean z = true;
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.setClassName(ViewPager.class.getName());
            if (ViewPager.this.mAdapter == null || ViewPager.this.mAdapter.getCount() <= ViewPager.SCROLL_STATE_DRAGGING) {
                z = ViewPager.DEBUG;
            }
            info.setScrollable(z);
            if (ViewPager.this.mAdapter != null && ViewPager.this.mCurItem >= 0 && ViewPager.this.mCurItem < ViewPager.this.mAdapter.getCount() + ViewPager.INVALID_POINTER) {
                info.addAction(Flags.EXTEND);
            }
            if (ViewPager.this.mAdapter != null && ViewPager.this.mCurItem > 0 && ViewPager.this.mCurItem < ViewPager.this.mAdapter.getCount()) {
                info.addAction(Flags.FLAG2);
            }
        }

        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            if (super.performAccessibilityAction(host, action, args)) {
                return true;
            }
            switch (action) {
                case Flags.EXTEND /*4096*/:
                    if (ViewPager.this.mAdapter == null || ViewPager.this.mCurItem < 0 || ViewPager.this.mCurItem >= ViewPager.this.mAdapter.getCount() + ViewPager.INVALID_POINTER) {
                        return ViewPager.DEBUG;
                    }
                    ViewPager.this.setCurrentItem(ViewPager.this.mCurItem + ViewPager.SCROLL_STATE_DRAGGING);
                    return true;
                case Flags.FLAG2 /*8192*/:
                    if (ViewPager.this.mAdapter == null || ViewPager.this.mCurItem <= 0 || ViewPager.this.mCurItem >= ViewPager.this.mAdapter.getCount()) {
                        return ViewPager.DEBUG;
                    }
                    ViewPager.this.setCurrentItem(ViewPager.this.mCurItem + ViewPager.INVALID_POINTER);
                    return true;
                default:
                    return ViewPager.DEBUG;
            }
        }
    }

    public static class SimpleOnPageChangeListener implements OnPageChangeListener {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    static {
        int[] iArr = new int[SCROLL_STATE_DRAGGING];
        iArr[SCROLL_STATE_IDLE] = 16842931;
        LAYOUT_ATTRS = iArr;
        COMPARATOR = new C00161();
        sInterpolator = new C00172();
    }

    public ViewPager(Context context) {
        super(context);
        this.mItems = new ArrayList();
        this.mTempItem = new ItemInfo();
        this.mTempRect = new Rect();
        this.mRestoredCurItem = INVALID_POINTER;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
        this.mFirstOffset = -3.4028235E38f;
        this.mLastOffset = Float.MAX_VALUE;
        this.mOffscreenPageLimit = SCROLL_STATE_DRAGGING;
        this.mActivePointerId = INVALID_POINTER;
        this.mFirstLayout = true;
        this.mNeedCalculatePageOffsets = DEBUG;
        this.mScrollState = SCROLL_STATE_IDLE;
        initViewPager();
    }

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mItems = new ArrayList();
        this.mTempItem = new ItemInfo();
        this.mTempRect = new Rect();
        this.mRestoredCurItem = INVALID_POINTER;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
        this.mFirstOffset = -3.4028235E38f;
        this.mLastOffset = Float.MAX_VALUE;
        this.mOffscreenPageLimit = SCROLL_STATE_DRAGGING;
        this.mActivePointerId = INVALID_POINTER;
        this.mFirstLayout = true;
        this.mNeedCalculatePageOffsets = DEBUG;
        this.mScrollState = SCROLL_STATE_IDLE;
        initViewPager();
    }

    void initViewPager() {
        setWillNotDraw(DEBUG);
        setDescendantFocusability(ServiceConnection.DEFAULT_BUFFER_SIZE);
        setFocusable(true);
        Context context = getContext();
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffectCompat(context);
        this.mRightEdge = new EdgeEffectCompat(context);
        float density = context.getResources().getDisplayMetrics().density;
        this.mFlingDistance = (int) (25.0f * density);
        this.mCloseEnough = (int) (2.0f * density);
        this.mDefaultGutterSize = (int) (16.0f * density);
        ViewCompat.setAccessibilityDelegate(this, new MyAccessibilityDelegate());
        if (ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, SCROLL_STATE_DRAGGING);
        }
    }

    private void setScrollState(int newState) {
        if (this.mScrollState != newState) {
            this.mScrollState = newState;
            if (this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageScrollStateChanged(newState);
            }
        }
    }

    public void setAdapter(PagerAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
            this.mAdapter.startUpdate((ViewGroup) this);
            for (int i = SCROLL_STATE_IDLE; i < this.mItems.size(); i += SCROLL_STATE_DRAGGING) {
                ItemInfo ii = (ItemInfo) this.mItems.get(i);
                this.mAdapter.destroyItem((ViewGroup) this, ii.position, ii.object);
            }
            this.mAdapter.finishUpdate((ViewGroup) this);
            this.mItems.clear();
            removeNonDecorViews();
            this.mCurItem = SCROLL_STATE_IDLE;
            scrollTo(SCROLL_STATE_IDLE, SCROLL_STATE_IDLE);
        }
        PagerAdapter oldAdapter = this.mAdapter;
        this.mAdapter = adapter;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new PagerObserver();
            }
            this.mAdapter.registerDataSetObserver(this.mObserver);
            this.mPopulatePending = DEBUG;
            this.mFirstLayout = true;
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                setCurrentItemInternal(this.mRestoredCurItem, DEBUG, true);
                this.mRestoredCurItem = INVALID_POINTER;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            } else {
                populate();
            }
        }
        if (this.mAdapterChangeListener != null && oldAdapter != adapter) {
            this.mAdapterChangeListener.onAdapterChanged(oldAdapter, adapter);
        }
    }

    private void removeNonDecorViews() {
        int i = SCROLL_STATE_IDLE;
        while (i < getChildCount()) {
            if (!((LayoutParams) getChildAt(i).getLayoutParams()).isDecor) {
                removeViewAt(i);
                i += INVALID_POINTER;
            }
            i += SCROLL_STATE_DRAGGING;
        }
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    void setOnAdapterChangeListener(OnAdapterChangeListener listener) {
        this.mAdapterChangeListener = listener;
    }

    public void setCurrentItem(int item) {
        boolean z;
        this.mPopulatePending = DEBUG;
        if (this.mFirstLayout) {
            z = DEBUG;
        } else {
            z = true;
        }
        setCurrentItemInternal(item, z, DEBUG);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        this.mPopulatePending = DEBUG;
        setCurrentItemInternal(item, smoothScroll, DEBUG);
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
        setCurrentItemInternal(item, smoothScroll, always, SCROLL_STATE_IDLE);
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
        boolean dispatchSelected = true;
        if (this.mAdapter == null || this.mAdapter.getCount() <= 0) {
            setScrollingCacheEnabled(DEBUG);
        } else if (always || this.mCurItem != item || this.mItems.size() == 0) {
            if (item < 0) {
                item = SCROLL_STATE_IDLE;
            } else if (item >= this.mAdapter.getCount()) {
                item = this.mAdapter.getCount() + INVALID_POINTER;
            }
            int pageLimit = this.mOffscreenPageLimit;
            if (item > this.mCurItem + pageLimit || item < this.mCurItem - pageLimit) {
                for (int i = SCROLL_STATE_IDLE; i < this.mItems.size(); i += SCROLL_STATE_DRAGGING) {
                    ((ItemInfo) this.mItems.get(i)).scrolling = true;
                }
            }
            if (this.mCurItem == item) {
                dispatchSelected = DEBUG;
            }
            populate(item);
            ItemInfo curInfo = infoForPosition(item);
            int destX = SCROLL_STATE_IDLE;
            if (curInfo != null) {
                destX = (int) (((float) getWidth()) * Math.max(this.mFirstOffset, Math.min(curInfo.offset, this.mLastOffset)));
            }
            if (smoothScroll) {
                smoothScrollTo(destX, SCROLL_STATE_IDLE, velocity);
                if (dispatchSelected && this.mOnPageChangeListener != null) {
                    this.mOnPageChangeListener.onPageSelected(item);
                }
                if (dispatchSelected && this.mInternalPageChangeListener != null) {
                    this.mInternalPageChangeListener.onPageSelected(item);
                    return;
                }
                return;
            }
            if (dispatchSelected && this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageSelected(item);
            }
            if (dispatchSelected && this.mInternalPageChangeListener != null) {
                this.mInternalPageChangeListener.onPageSelected(item);
            }
            completeScroll();
            scrollTo(destX, SCROLL_STATE_IDLE);
        } else {
            setScrollingCacheEnabled(DEBUG);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener listener) {
        OnPageChangeListener oldListener = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = listener;
        return oldListener;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public void setOffscreenPageLimit(int limit) {
        if (limit < SCROLL_STATE_DRAGGING) {
            Log.w(TAG, "Requested offscreen page limit " + limit + " too small; defaulting to " + SCROLL_STATE_DRAGGING);
            limit = SCROLL_STATE_DRAGGING;
        }
        if (limit != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = limit;
            populate();
        }
    }

    public void setPageMargin(int marginPixels) {
        int oldMargin = this.mPageMargin;
        this.mPageMargin = marginPixels;
        int width = getWidth();
        recomputeScrollPosition(width, width, marginPixels, oldMargin);
        requestLayout();
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    public void setPageMarginDrawable(Drawable d) {
        this.mMarginDrawable = d;
        if (d != null) {
            refreshDrawableState();
        }
        setWillNotDraw(d == null ? true : DEBUG);
        invalidate();
    }

    public void setPageMarginDrawable(int resId) {
        setPageMarginDrawable(getContext().getResources().getDrawable(resId));
    }

    protected boolean verifyDrawable(Drawable who) {
        return (super.verifyDrawable(who) || who == this.mMarginDrawable) ? true : DEBUG;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable d = this.mMarginDrawable;
        if (d != null && d.isStateful()) {
            d.setState(getDrawableState());
        }
    }

    float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((double) ((float) (((double) (f - 0.5f)) * 0.4712389167638204d)));
    }

    void smoothScrollTo(int x, int y) {
        smoothScrollTo(x, y, SCROLL_STATE_IDLE);
    }

    void smoothScrollTo(int x, int y, int velocity) {
        if (getChildCount() == 0) {
            setScrollingCacheEnabled(DEBUG);
            return;
        }
        int sx = getScrollX();
        int sy = getScrollY();
        int dx = x - sx;
        int dy = y - sy;
        if (dx == 0 && dy == 0) {
            completeScroll();
            populate();
            setScrollState(SCROLL_STATE_IDLE);
            return;
        }
        int duration;
        setScrollingCacheEnabled(true);
        setScrollState(SCROLL_STATE_SETTLING);
        int width = getWidth();
        int halfWidth = width / SCROLL_STATE_SETTLING;
        float distance = ((float) halfWidth) + (((float) halfWidth) * distanceInfluenceForSnapDuration(Math.min(1.0f, (1.0f * ((float) Math.abs(dx))) / ((float) width))));
        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration = Math.round(1000.0f * Math.abs(distance / ((float) velocity))) * 4;
        } else {
            duration = (int) ((1.0f + (((float) Math.abs(dx)) / (((float) this.mPageMargin) + (((float) width) * this.mAdapter.getPageWidth(this.mCurItem))))) * 100.0f);
        }
        this.mScroller.startScroll(sx, sy, dx, dy, Math.min(duration, MAX_SETTLE_DURATION));
        ViewCompat.postInvalidateOnAnimation(this);
    }

    ItemInfo addNewItem(int position, int index) {
        ItemInfo ii = new ItemInfo();
        ii.position = position;
        ii.object = this.mAdapter.instantiateItem((ViewGroup) this, position);
        ii.widthFactor = this.mAdapter.getPageWidth(position);
        if (index < 0 || index >= this.mItems.size()) {
            this.mItems.add(ii);
        } else {
            this.mItems.add(index, ii);
        }
        return ii;
    }

    void dataSetChanged() {
        boolean needPopulate;
        if (this.mItems.size() >= (this.mOffscreenPageLimit * SCROLL_STATE_SETTLING) + SCROLL_STATE_DRAGGING || this.mItems.size() >= this.mAdapter.getCount()) {
            needPopulate = DEBUG;
        } else {
            needPopulate = true;
        }
        int newCurrItem = this.mCurItem;
        boolean isUpdating = DEBUG;
        int i = SCROLL_STATE_IDLE;
        while (i < this.mItems.size()) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            int newPos = this.mAdapter.getItemPosition(ii.object);
            if (newPos != INVALID_POINTER) {
                if (newPos == -2) {
                    this.mItems.remove(i);
                    i += INVALID_POINTER;
                    if (!isUpdating) {
                        this.mAdapter.startUpdate((ViewGroup) this);
                        isUpdating = true;
                    }
                    this.mAdapter.destroyItem((ViewGroup) this, ii.position, ii.object);
                    needPopulate = true;
                    if (this.mCurItem == ii.position) {
                        newCurrItem = Math.max(SCROLL_STATE_IDLE, Math.min(this.mCurItem, this.mAdapter.getCount() + INVALID_POINTER));
                        needPopulate = true;
                    }
                } else if (ii.position != newPos) {
                    if (ii.position == this.mCurItem) {
                        newCurrItem = newPos;
                    }
                    ii.position = newPos;
                    needPopulate = true;
                }
            }
            i += SCROLL_STATE_DRAGGING;
        }
        if (isUpdating) {
            this.mAdapter.finishUpdate((ViewGroup) this);
        }
        Collections.sort(this.mItems, COMPARATOR);
        if (needPopulate) {
            int childCount = getChildCount();
            for (i = SCROLL_STATE_IDLE; i < childCount; i += SCROLL_STATE_DRAGGING) {
                LayoutParams lp = (LayoutParams) getChildAt(i).getLayoutParams();
                if (!lp.isDecor) {
                    lp.widthFactor = 0.0f;
                }
            }
            setCurrentItemInternal(newCurrItem, DEBUG, true);
            requestLayout();
        }
    }

    void populate() {
        populate(this.mCurItem);
    }

    void populate(int newCurrentItem) {
        ItemInfo oldCurInfo = null;
        int i = this.mCurItem;
        if (r0 != newCurrentItem) {
            oldCurInfo = infoForPosition(this.mCurItem);
            this.mCurItem = newCurrentItem;
        }
        if (this.mAdapter != null && !this.mPopulatePending && getWindowToken() != null) {
            ItemInfo ii;
            int i2;
            View child;
            this.mAdapter.startUpdate((ViewGroup) this);
            int pageLimit = this.mOffscreenPageLimit;
            int startPos = Math.max(SCROLL_STATE_IDLE, this.mCurItem - pageLimit);
            int N = this.mAdapter.getCount();
            int endPos = Math.min(N + INVALID_POINTER, this.mCurItem + pageLimit);
            ItemInfo curItem = null;
            int curIndex = SCROLL_STATE_IDLE;
            while (true) {
                if (curIndex >= this.mItems.size()) {
                    break;
                }
                ii = (ItemInfo) this.mItems.get(curIndex);
                if (ii.position >= this.mCurItem) {
                    break;
                }
                curIndex += SCROLL_STATE_DRAGGING;
            }
            if (ii.position == this.mCurItem) {
                curItem = ii;
            }
            if (curItem == null && N > 0) {
                curItem = addNewItem(this.mCurItem, curIndex);
            }
            if (curItem != null) {
                float extraWidthLeft = 0.0f;
                int itemIndex = curIndex + INVALID_POINTER;
                ii = itemIndex >= 0 ? (ItemInfo) this.mItems.get(itemIndex) : null;
                float leftWidthNeeded = 2.0f - curItem.widthFactor;
                int pos = this.mCurItem + INVALID_POINTER;
                while (pos >= 0) {
                    if (extraWidthLeft < leftWidthNeeded || pos >= startPos) {
                        if (ii == null || pos != ii.position) {
                            extraWidthLeft += addNewItem(pos, itemIndex + SCROLL_STATE_DRAGGING).widthFactor;
                            curIndex += SCROLL_STATE_DRAGGING;
                            ii = itemIndex >= 0 ? (ItemInfo) this.mItems.get(itemIndex) : null;
                        } else {
                            extraWidthLeft += ii.widthFactor;
                            itemIndex += INVALID_POINTER;
                            ii = itemIndex >= 0 ? (ItemInfo) this.mItems.get(itemIndex) : null;
                        }
                    } else if (ii == null) {
                        break;
                    } else {
                        if (pos == ii.position && !ii.scrolling) {
                            this.mItems.remove(itemIndex);
                            this.mAdapter.destroyItem((ViewGroup) this, pos, ii.object);
                            itemIndex += INVALID_POINTER;
                            curIndex += INVALID_POINTER;
                            if (itemIndex >= 0) {
                                ii = (ItemInfo) this.mItems.get(itemIndex);
                            } else {
                                ii = null;
                            }
                        }
                    }
                    pos += INVALID_POINTER;
                }
                float extraWidthRight = curItem.widthFactor;
                itemIndex = curIndex + SCROLL_STATE_DRAGGING;
                if (extraWidthRight < 2.0f) {
                    ii = itemIndex < this.mItems.size() ? (ItemInfo) this.mItems.get(itemIndex) : null;
                    pos = this.mCurItem + SCROLL_STATE_DRAGGING;
                    while (pos < N) {
                        if (extraWidthRight < 2.0f || pos <= endPos) {
                            if (ii == null || pos != ii.position) {
                                ii = addNewItem(pos, itemIndex);
                                itemIndex += SCROLL_STATE_DRAGGING;
                                extraWidthRight += ii.widthFactor;
                                ii = itemIndex < this.mItems.size() ? (ItemInfo) this.mItems.get(itemIndex) : null;
                            } else {
                                extraWidthRight += ii.widthFactor;
                                itemIndex += SCROLL_STATE_DRAGGING;
                                ii = itemIndex < this.mItems.size() ? (ItemInfo) this.mItems.get(itemIndex) : null;
                            }
                        } else if (ii == null) {
                            break;
                        } else {
                            if (pos == ii.position && !ii.scrolling) {
                                this.mItems.remove(itemIndex);
                                this.mAdapter.destroyItem((ViewGroup) this, pos, ii.object);
                                if (itemIndex < this.mItems.size()) {
                                    ii = (ItemInfo) this.mItems.get(itemIndex);
                                } else {
                                    ii = null;
                                }
                            }
                        }
                        pos += SCROLL_STATE_DRAGGING;
                    }
                }
                calculatePageOffsets(curItem, curIndex, oldCurInfo);
            }
            this.mAdapter.setPrimaryItem((ViewGroup) this, this.mCurItem, curItem != null ? curItem.object : null);
            this.mAdapter.finishUpdate((ViewGroup) this);
            int childCount = getChildCount();
            for (i2 = SCROLL_STATE_IDLE; i2 < childCount; i2 += SCROLL_STATE_DRAGGING) {
                child = getChildAt(i2);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (!lp.isDecor) {
                    if (lp.widthFactor == 0.0f) {
                        ii = infoForChild(child);
                        if (ii != null) {
                            lp.widthFactor = ii.widthFactor;
                        }
                    }
                }
            }
            if (hasFocus()) {
                View currentFocused = findFocus();
                ii = currentFocused != null ? infoForAnyChild(currentFocused) : null;
                if (ii == null || ii.position != this.mCurItem) {
                    i2 = SCROLL_STATE_IDLE;
                    while (i2 < getChildCount()) {
                        child = getChildAt(i2);
                        ii = infoForChild(child);
                        if (ii == null || ii.position != this.mCurItem || !child.requestFocus(SCROLL_STATE_SETTLING)) {
                            i2 += SCROLL_STATE_DRAGGING;
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }

    private void calculatePageOffsets(ItemInfo curItem, int curIndex, ItemInfo oldCurInfo) {
        float offset;
        int pos;
        ItemInfo ii;
        int N = this.mAdapter.getCount();
        int width = getWidth();
        float marginOffset = width > 0 ? ((float) this.mPageMargin) / ((float) width) : 0.0f;
        if (oldCurInfo != null) {
            int oldCurPosition = oldCurInfo.position;
            int itemIndex;
            if (oldCurPosition < curItem.position) {
                itemIndex = SCROLL_STATE_IDLE;
                offset = (oldCurInfo.offset + oldCurInfo.widthFactor) + marginOffset;
                pos = oldCurPosition + SCROLL_STATE_DRAGGING;
                while (pos <= curItem.position && itemIndex < this.mItems.size()) {
                    ii = (ItemInfo) this.mItems.get(itemIndex);
                    while (pos > ii.position && itemIndex < this.mItems.size() + INVALID_POINTER) {
                        itemIndex += SCROLL_STATE_DRAGGING;
                        ii = (ItemInfo) this.mItems.get(itemIndex);
                    }
                    while (pos < ii.position) {
                        offset += this.mAdapter.getPageWidth(pos) + marginOffset;
                        pos += SCROLL_STATE_DRAGGING;
                    }
                    ii.offset = offset;
                    offset += ii.widthFactor + marginOffset;
                    pos += SCROLL_STATE_DRAGGING;
                }
            } else if (oldCurPosition > curItem.position) {
                itemIndex = this.mItems.size() + INVALID_POINTER;
                offset = oldCurInfo.offset;
                pos = oldCurPosition + INVALID_POINTER;
                while (pos >= curItem.position && itemIndex >= 0) {
                    ii = (ItemInfo) this.mItems.get(itemIndex);
                    while (pos < ii.position && itemIndex > 0) {
                        itemIndex += INVALID_POINTER;
                        ii = (ItemInfo) this.mItems.get(itemIndex);
                    }
                    while (pos > ii.position) {
                        offset -= this.mAdapter.getPageWidth(pos) + marginOffset;
                        pos += INVALID_POINTER;
                    }
                    offset -= ii.widthFactor + marginOffset;
                    ii.offset = offset;
                    pos += INVALID_POINTER;
                }
            }
        }
        int itemCount = this.mItems.size();
        offset = curItem.offset;
        pos = curItem.position + INVALID_POINTER;
        this.mFirstOffset = curItem.position == 0 ? curItem.offset : -3.4028235E38f;
        this.mLastOffset = curItem.position == N + INVALID_POINTER ? (curItem.offset + curItem.widthFactor) - 1.0f : Float.MAX_VALUE;
        int i = curIndex + INVALID_POINTER;
        while (i >= 0) {
            ii = (ItemInfo) this.mItems.get(i);
            while (pos > ii.position) {
                offset -= this.mAdapter.getPageWidth(pos) + marginOffset;
                pos += INVALID_POINTER;
            }
            offset -= ii.widthFactor + marginOffset;
            ii.offset = offset;
            if (ii.position == 0) {
                this.mFirstOffset = offset;
            }
            i += INVALID_POINTER;
            pos += INVALID_POINTER;
        }
        offset = (curItem.offset + curItem.widthFactor) + marginOffset;
        pos = curItem.position + SCROLL_STATE_DRAGGING;
        i = curIndex + SCROLL_STATE_DRAGGING;
        while (i < itemCount) {
            ii = (ItemInfo) this.mItems.get(i);
            while (pos < ii.position) {
                offset += this.mAdapter.getPageWidth(pos) + marginOffset;
                pos += SCROLL_STATE_DRAGGING;
            }
            if (ii.position == N + INVALID_POINTER) {
                this.mLastOffset = (ii.widthFactor + offset) - 1.0f;
            }
            ii.offset = offset;
            offset += ii.widthFactor + marginOffset;
            i += SCROLL_STATE_DRAGGING;
            pos += SCROLL_STATE_DRAGGING;
        }
        this.mNeedCalculatePageOffsets = DEBUG;
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.position = this.mCurItem;
        if (this.mAdapter != null) {
            ss.adapterState = this.mAdapter.saveState();
        }
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            if (this.mAdapter != null) {
                this.mAdapter.restoreState(ss.adapterState, ss.loader);
                setCurrentItemInternal(ss.position, DEBUG, true);
                return;
            }
            this.mRestoredCurItem = ss.position;
            this.mRestoredAdapterState = ss.adapterState;
            this.mRestoredClassLoader = ss.loader;
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        if (!checkLayoutParams(params)) {
            params = generateLayoutParams(params);
        }
        LayoutParams lp = (LayoutParams) params;
        lp.isDecor |= child instanceof Decor;
        if (!this.mInLayout) {
            super.addView(child, index, params);
        } else if (lp == null || !lp.isDecor) {
            lp.needsMeasure = true;
            addViewInLayout(child, index, params);
        } else {
            throw new IllegalStateException("Cannot add pager decor view during layout");
        }
    }

    ItemInfo infoForChild(View child) {
        for (int i = SCROLL_STATE_IDLE; i < this.mItems.size(); i += SCROLL_STATE_DRAGGING) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(child, ii.object)) {
                return ii;
            }
        }
        return null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    android.support.v4.view.ViewPager.ItemInfo infoForAnyChild(android.view.View r3) {
        /*
        r2 = this;
    L_0x0000:
        r0 = r3.getParent();
        if (r0 == r2) goto L_0x0012;
    L_0x0006:
        if (r0 == 0) goto L_0x000c;
    L_0x0008:
        r1 = r0 instanceof android.view.View;
        if (r1 != 0) goto L_0x000e;
    L_0x000c:
        r1 = 0;
    L_0x000d:
        return r1;
    L_0x000e:
        r3 = r0;
        r3 = (android.view.View) r3;
        goto L_0x0000;
    L_0x0012:
        r1 = r2.infoForChild(r3);
        goto L_0x000d;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.view.ViewPager.infoForAnyChild(android.view.View):android.support.v4.view.ViewPager$ItemInfo");
    }

    ItemInfo infoForPosition(int position) {
        for (int i = SCROLL_STATE_IDLE; i < this.mItems.size(); i += SCROLL_STATE_DRAGGING) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (ii.position == position) {
                return ii;
            }
        }
        return null;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        LayoutParams lp;
        setMeasuredDimension(getDefaultSize(SCROLL_STATE_IDLE, widthMeasureSpec), getDefaultSize(SCROLL_STATE_IDLE, heightMeasureSpec));
        int measuredWidth = getMeasuredWidth();
        this.mGutterSize = Math.min(measuredWidth / 10, this.mDefaultGutterSize);
        int childWidthSize = (measuredWidth - getPaddingLeft()) - getPaddingRight();
        int childHeightSize = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        int size = getChildCount();
        for (i = SCROLL_STATE_IDLE; i < size; i += SCROLL_STATE_DRAGGING) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                lp = (LayoutParams) child.getLayoutParams();
                if (lp != null && lp.isDecor) {
                    int hgrav = lp.gravity & 7;
                    int vgrav = lp.gravity & Opcodes.IREM;
                    int widthMode = Integer.MIN_VALUE;
                    int heightMode = Integer.MIN_VALUE;
                    boolean consumeVertical = (vgrav == 48 || vgrav == 80) ? true : DEBUG;
                    boolean consumeHorizontal = (hgrav == 3 || hgrav == 5) ? true : DEBUG;
                    if (consumeVertical) {
                        widthMode = 1073741824;
                    } else if (consumeHorizontal) {
                        heightMode = 1073741824;
                    }
                    int widthSize = childWidthSize;
                    int heightSize = childHeightSize;
                    int i2 = lp.width;
                    if (r0 != -2) {
                        widthMode = 1073741824;
                        i2 = lp.width;
                        if (r0 != INVALID_POINTER) {
                            widthSize = lp.width;
                        }
                    }
                    i2 = lp.height;
                    if (r0 != -2) {
                        heightMode = 1073741824;
                        i2 = lp.height;
                        if (r0 != INVALID_POINTER) {
                            heightSize = lp.height;
                        }
                    }
                    child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(heightSize, heightMode));
                    if (consumeVertical) {
                        childHeightSize -= child.getMeasuredHeight();
                    } else if (consumeHorizontal) {
                        childWidthSize -= child.getMeasuredWidth();
                    }
                }
            }
        }
        this.mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, 1073741824);
        this.mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, 1073741824);
        this.mInLayout = true;
        populate();
        this.mInLayout = DEBUG;
        size = getChildCount();
        for (i = SCROLL_STATE_IDLE; i < size; i += SCROLL_STATE_DRAGGING) {
            child = getChildAt(i);
            if (child.getVisibility() != 8) {
                lp = (LayoutParams) child.getLayoutParams();
                if (lp == null || !lp.isDecor) {
                    child.measure(MeasureSpec.makeMeasureSpec((int) (((float) childWidthSize) * lp.widthFactor), 1073741824), this.mChildHeightMeasureSpec);
                }
            }
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw) {
            recomputeScrollPosition(w, oldw, this.mPageMargin, this.mPageMargin);
        }
    }

    private void recomputeScrollPosition(int width, int oldWidth, int margin, int oldMargin) {
        if (oldWidth <= 0 || this.mItems.isEmpty()) {
            ItemInfo ii = infoForPosition(this.mCurItem);
            int scrollPos = (int) (((float) width) * (ii != null ? Math.min(ii.offset, this.mLastOffset) : 0.0f));
            if (scrollPos != getScrollX()) {
                completeScroll();
                scrollTo(scrollPos, getScrollY());
                return;
            }
            return;
        }
        int newOffsetPixels = (int) (((float) (width + margin)) * (((float) getScrollX()) / ((float) (oldWidth + oldMargin))));
        scrollTo(newOffsetPixels, getScrollY());
        if (!this.mScroller.isFinished()) {
            this.mScroller.startScroll(newOffsetPixels, SCROLL_STATE_IDLE, (int) (infoForPosition(this.mCurItem).offset * ((float) width)), SCROLL_STATE_IDLE, this.mScroller.getDuration() - this.mScroller.timePassed());
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int i;
        int childLeft;
        int childTop;
        this.mInLayout = true;
        populate();
        this.mInLayout = DEBUG;
        int count = getChildCount();
        int width = r - l;
        int height = b - t;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int scrollX = getScrollX();
        int decorCount = SCROLL_STATE_IDLE;
        for (i = SCROLL_STATE_IDLE; i < count; i += SCROLL_STATE_DRAGGING) {
            LayoutParams lp;
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                lp = (LayoutParams) child.getLayoutParams();
                if (lp.isDecor) {
                    int vgrav = lp.gravity & Opcodes.IREM;
                    switch (lp.gravity & 7) {
                        case SCROLL_STATE_DRAGGING /*1*/:
                            childLeft = Math.max((width - child.getMeasuredWidth()) / SCROLL_STATE_SETTLING, paddingLeft);
                            break;
                        case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                            childLeft = paddingLeft;
                            paddingLeft += child.getMeasuredWidth();
                            break;
                        case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                            childLeft = (width - paddingRight) - child.getMeasuredWidth();
                            paddingRight += child.getMeasuredWidth();
                            break;
                        default:
                            childLeft = paddingLeft;
                            break;
                    }
                    switch (vgrav) {
                        case DEFAULT_GUTTER_SIZE /*16*/:
                            childTop = Math.max((height - child.getMeasuredHeight()) / SCROLL_STATE_SETTLING, paddingTop);
                            break;
                        case Type.DNSKEY /*48*/:
                            childTop = paddingTop;
                            paddingTop += child.getMeasuredHeight();
                            break;
                        case Opcodes.LASTORE /*80*/:
                            childTop = (height - paddingBottom) - child.getMeasuredHeight();
                            paddingBottom += child.getMeasuredHeight();
                            break;
                        default:
                            childTop = paddingTop;
                            break;
                    }
                    childLeft += scrollX;
                    child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
                    decorCount += SCROLL_STATE_DRAGGING;
                }
            }
        }
        for (i = SCROLL_STATE_IDLE; i < count; i += SCROLL_STATE_DRAGGING) {
            child = getChildAt(i);
            if (child.getVisibility() != 8) {
                lp = (LayoutParams) child.getLayoutParams();
                if (!lp.isDecor) {
                    ItemInfo ii = infoForChild(child);
                    if (ii != null) {
                        childLeft = paddingLeft + ((int) (((float) width) * ii.offset));
                        childTop = paddingTop;
                        if (lp.needsMeasure) {
                            lp.needsMeasure = DEBUG;
                            int makeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (((float) ((width - paddingLeft) - paddingRight)) * lp.widthFactor), 1073741824);
                            child.measure(widthSpec, MeasureSpec.makeMeasureSpec((height - paddingTop) - paddingBottom, 1073741824));
                        }
                        child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
                    }
                }
            }
        }
        this.mTopPageBounds = paddingTop;
        this.mBottomPageBounds = height - paddingBottom;
        this.mDecorChildCount = decorCount;
        this.mFirstLayout = DEBUG;
    }

    public void computeScroll() {
        if (this.mScroller.isFinished() || !this.mScroller.computeScrollOffset()) {
            completeScroll();
            return;
        }
        int oldX = getScrollX();
        int oldY = getScrollY();
        int x = this.mScroller.getCurrX();
        int y = this.mScroller.getCurrY();
        if (!(oldX == x && oldY == y)) {
            scrollTo(x, y);
            if (!pageScrolled(x)) {
                this.mScroller.abortAnimation();
                scrollTo(SCROLL_STATE_IDLE, y);
            }
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private boolean pageScrolled(int xpos) {
        if (this.mItems.size() == 0) {
            this.mCalledSuper = DEBUG;
            onPageScrolled(SCROLL_STATE_IDLE, 0.0f, SCROLL_STATE_IDLE);
            if (this.mCalledSuper) {
                return DEBUG;
            }
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        }
        ItemInfo ii = infoForCurrentScrollPosition();
        int width = getWidth();
        int widthWithMargin = width + this.mPageMargin;
        float marginOffset = ((float) this.mPageMargin) / ((float) width);
        int currentPage = ii.position;
        float pageOffset = ((((float) xpos) / ((float) width)) - ii.offset) / (ii.widthFactor + marginOffset);
        int offsetPixels = (int) (((float) widthWithMargin) * pageOffset);
        this.mCalledSuper = DEBUG;
        onPageScrolled(currentPage, pageOffset, offsetPixels);
        if (this.mCalledSuper) {
            return true;
        }
        throw new IllegalStateException("onPageScrolled did not call superclass implementation");
    }

    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        if (this.mDecorChildCount > 0) {
            int scrollX = getScrollX();
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            int width = getWidth();
            int childCount = getChildCount();
            for (int i = SCROLL_STATE_IDLE; i < childCount; i += SCROLL_STATE_DRAGGING) {
                View child = getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.isDecor) {
                    int childLeft;
                    switch (lp.gravity & 7) {
                        case SCROLL_STATE_DRAGGING /*1*/:
                            childLeft = Math.max((width - child.getMeasuredWidth()) / SCROLL_STATE_SETTLING, paddingLeft);
                            break;
                        case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                            childLeft = paddingLeft;
                            paddingLeft += child.getWidth();
                            break;
                        case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                            childLeft = (width - paddingRight) - child.getMeasuredWidth();
                            paddingRight += child.getMeasuredWidth();
                            break;
                        default:
                            childLeft = paddingLeft;
                            break;
                    }
                    int childOffset = (childLeft + scrollX) - child.getLeft();
                    if (childOffset != 0) {
                        child.offsetLeftAndRight(childOffset);
                    }
                }
            }
        }
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }
        this.mCalledSuper = true;
    }

    private void completeScroll() {
        boolean needPopulate;
        if (this.mScrollState == SCROLL_STATE_SETTLING) {
            needPopulate = true;
        } else {
            needPopulate = DEBUG;
        }
        if (needPopulate) {
            setScrollingCacheEnabled(DEBUG);
            this.mScroller.abortAnimation();
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (!(oldX == x && oldY == y)) {
                scrollTo(x, y);
            }
            setScrollState(SCROLL_STATE_IDLE);
        }
        this.mPopulatePending = DEBUG;
        for (int i = SCROLL_STATE_IDLE; i < this.mItems.size(); i += SCROLL_STATE_DRAGGING) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (ii.scrolling) {
                needPopulate = true;
                ii.scrolling = DEBUG;
            }
        }
        if (needPopulate) {
            populate();
        }
    }

    private boolean isGutterDrag(float x, float dx) {
        return ((x >= ((float) this.mGutterSize) || dx <= 0.0f) && (x <= ((float) (getWidth() - this.mGutterSize)) || dx >= 0.0f)) ? DEBUG : true;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction() & KEYRecord.PROTOCOL_ANY;
        if (action == 3 || action == SCROLL_STATE_DRAGGING) {
            this.mIsBeingDragged = DEBUG;
            this.mIsUnableToDrag = DEBUG;
            this.mActivePointerId = INVALID_POINTER;
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
            }
            return DEBUG;
        }
        if (action != 0) {
            if (this.mIsBeingDragged) {
                return true;
            }
            if (this.mIsUnableToDrag) {
                return DEBUG;
            }
        }
        switch (action) {
            case SCROLL_STATE_IDLE /*0*/:
                float x = ev.getX();
                this.mInitialMotionX = x;
                this.mLastMotionX = x;
                this.mLastMotionY = ev.getY();
                this.mActivePointerId = MotionEventCompat.getPointerId(ev, SCROLL_STATE_IDLE);
                this.mIsUnableToDrag = DEBUG;
                this.mScroller.computeScrollOffset();
                if (this.mScrollState == SCROLL_STATE_SETTLING && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
                    this.mScroller.abortAnimation();
                    this.mPopulatePending = DEBUG;
                    populate();
                    this.mIsBeingDragged = true;
                    setScrollState(SCROLL_STATE_DRAGGING);
                    break;
                }
                completeScroll();
                this.mIsBeingDragged = DEBUG;
                break;
            case SCROLL_STATE_SETTLING /*2*/:
                int activePointerId = this.mActivePointerId;
                if (activePointerId != INVALID_POINTER) {
                    int pointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
                    float x2 = MotionEventCompat.getX(ev, pointerIndex);
                    float dx = x2 - this.mLastMotionX;
                    float xDiff = Math.abs(dx);
                    float y = MotionEventCompat.getY(ev, pointerIndex);
                    float yDiff = Math.abs(y - this.mLastMotionY);
                    if (dx == 0.0f || isGutterDrag(this.mLastMotionX, dx) || !canScroll(this, DEBUG, (int) dx, (int) x2, (int) y)) {
                        if (xDiff > ((float) this.mTouchSlop) && xDiff > yDiff) {
                            this.mIsBeingDragged = true;
                            setScrollState(SCROLL_STATE_DRAGGING);
                            this.mLastMotionX = dx > 0.0f ? this.mInitialMotionX + ((float) this.mTouchSlop) : this.mInitialMotionX - ((float) this.mTouchSlop);
                            setScrollingCacheEnabled(true);
                        } else if (yDiff > ((float) this.mTouchSlop)) {
                            this.mIsUnableToDrag = true;
                        }
                        if (this.mIsBeingDragged && performDrag(x2)) {
                            ViewCompat.postInvalidateOnAnimation(this);
                            break;
                        }
                    }
                    this.mLastMotionX = x2;
                    this.mInitialMotionX = x2;
                    this.mLastMotionY = y;
                    this.mIsUnableToDrag = true;
                    return DEBUG;
                }
                break;
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                onSecondaryPointerUp(ev);
                break;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(ev);
        return this.mIsBeingDragged;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mFakeDragging) {
            return true;
        }
        if (ev.getAction() == 0 && ev.getEdgeFlags() != 0) {
            return DEBUG;
        }
        if (this.mAdapter != null) {
            if (this.mAdapter.getCount() != 0) {
                if (this.mVelocityTracker == null) {
                    this.mVelocityTracker = VelocityTracker.obtain();
                }
                this.mVelocityTracker.addMovement(ev);
                int action = ev.getAction();
                boolean needsInvalidate = DEBUG;
                float x;
                switch (action & KEYRecord.PROTOCOL_ANY) {
                    case SCROLL_STATE_IDLE /*0*/:
                        this.mScroller.abortAnimation();
                        this.mPopulatePending = DEBUG;
                        populate();
                        this.mIsBeingDragged = true;
                        setScrollState(SCROLL_STATE_DRAGGING);
                        x = ev.getX();
                        this.mInitialMotionX = x;
                        this.mLastMotionX = x;
                        this.mActivePointerId = MotionEventCompat.getPointerId(ev, SCROLL_STATE_IDLE);
                        break;
                    case SCROLL_STATE_DRAGGING /*1*/:
                        if (this.mIsBeingDragged) {
                            VelocityTracker velocityTracker = this.mVelocityTracker;
                            velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                            int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
                            this.mPopulatePending = true;
                            int width = getWidth();
                            int scrollX = getScrollX();
                            ItemInfo ii = infoForCurrentScrollPosition();
                            int currentPage = ii.position;
                            x = (float) scrollX;
                            float f = (float) width;
                            f = ii.offset;
                            float pageOffset = ((r0 / r0) - r0) / ii.widthFactor;
                            int activePointerIndex = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
                            setCurrentItemInternal(determineTargetPage(currentPage, pageOffset, initialVelocity, (int) (MotionEventCompat.getX(ev, activePointerIndex) - this.mInitialMotionX)), true, true, initialVelocity);
                            this.mActivePointerId = INVALID_POINTER;
                            endDrag();
                            needsInvalidate = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
                            break;
                        }
                        break;
                    case SCROLL_STATE_SETTLING /*2*/:
                        if (!this.mIsBeingDragged) {
                            int pointerIndex = MotionEventCompat.findPointerIndex(ev, this.mActivePointerId);
                            float x2 = MotionEventCompat.getX(ev, pointerIndex);
                            float xDiff = Math.abs(x2 - this.mLastMotionX);
                            float yDiff = Math.abs(MotionEventCompat.getY(ev, pointerIndex) - this.mLastMotionY);
                            if (xDiff > ((float) this.mTouchSlop) && xDiff > yDiff) {
                                this.mIsBeingDragged = true;
                                if (x2 - this.mInitialMotionX > 0.0f) {
                                    x = this.mInitialMotionX + ((float) this.mTouchSlop);
                                } else {
                                    x = this.mInitialMotionX - ((float) this.mTouchSlop);
                                }
                                this.mLastMotionX = x;
                                setScrollState(SCROLL_STATE_DRAGGING);
                                setScrollingCacheEnabled(true);
                            }
                        }
                        if (this.mIsBeingDragged) {
                            needsInvalidate = DEBUG | performDrag(MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, this.mActivePointerId)));
                            break;
                        }
                        break;
                    case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                        if (this.mIsBeingDragged) {
                            setCurrentItemInternal(this.mCurItem, true, true);
                            this.mActivePointerId = INVALID_POINTER;
                            endDrag();
                            needsInvalidate = this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
                            break;
                        }
                        break;
                    case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                        int index = MotionEventCompat.getActionIndex(ev);
                        this.mLastMotionX = MotionEventCompat.getX(ev, index);
                        this.mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                        break;
                    case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                        onSecondaryPointerUp(ev);
                        this.mLastMotionX = MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, this.mActivePointerId));
                        break;
                }
                if (needsInvalidate) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                return true;
            }
        }
        return DEBUG;
    }

    private boolean performDrag(float x) {
        boolean needsInvalidate = DEBUG;
        float deltaX = this.mLastMotionX - x;
        this.mLastMotionX = x;
        float scrollX = ((float) getScrollX()) + deltaX;
        int width = getWidth();
        float leftBound = ((float) width) * this.mFirstOffset;
        float rightBound = ((float) width) * this.mLastOffset;
        boolean leftAbsolute = true;
        boolean rightAbsolute = true;
        ItemInfo firstItem = (ItemInfo) this.mItems.get(SCROLL_STATE_IDLE);
        ItemInfo lastItem = (ItemInfo) this.mItems.get(this.mItems.size() + INVALID_POINTER);
        if (firstItem.position != 0) {
            leftAbsolute = DEBUG;
            leftBound = firstItem.offset * ((float) width);
        }
        if (lastItem.position != this.mAdapter.getCount() + INVALID_POINTER) {
            rightAbsolute = DEBUG;
            rightBound = lastItem.offset * ((float) width);
        }
        float f;
        if (scrollX < leftBound) {
            if (leftAbsolute) {
                f = (float) width;
                needsInvalidate = this.mLeftEdge.onPull(Math.abs(leftBound - scrollX) / r0);
            }
            scrollX = leftBound;
        } else if (scrollX > rightBound) {
            if (rightAbsolute) {
                f = (float) width;
                needsInvalidate = this.mRightEdge.onPull(Math.abs(scrollX - rightBound) / r0);
            }
            scrollX = rightBound;
        }
        this.mLastMotionX += scrollX - ((float) ((int) scrollX));
        scrollTo((int) scrollX, getScrollY());
        pageScrolled((int) scrollX);
        return needsInvalidate;
    }

    private ItemInfo infoForCurrentScrollPosition() {
        float scrollOffset;
        float marginOffset = 0.0f;
        int width = getWidth();
        if (width > 0) {
            scrollOffset = ((float) getScrollX()) / ((float) width);
        } else {
            scrollOffset = 0.0f;
        }
        if (width > 0) {
            marginOffset = ((float) this.mPageMargin) / ((float) width);
        }
        int lastPos = INVALID_POINTER;
        float lastOffset = 0.0f;
        float lastWidth = 0.0f;
        boolean first = true;
        ItemInfo lastItem = null;
        int i = SCROLL_STATE_IDLE;
        while (i < this.mItems.size()) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (!(first || ii.position == lastPos + SCROLL_STATE_DRAGGING)) {
                ii = this.mTempItem;
                ii.offset = (lastOffset + lastWidth) + marginOffset;
                ii.position = lastPos + SCROLL_STATE_DRAGGING;
                ii.widthFactor = this.mAdapter.getPageWidth(ii.position);
                i += INVALID_POINTER;
            }
            float offset = ii.offset;
            float leftBound = offset;
            float rightBound = (ii.widthFactor + offset) + marginOffset;
            if (!first && scrollOffset < leftBound) {
                return lastItem;
            }
            if (scrollOffset < rightBound || i == this.mItems.size() + INVALID_POINTER) {
                return ii;
            }
            first = DEBUG;
            lastPos = ii.position;
            lastOffset = offset;
            lastWidth = ii.widthFactor;
            lastItem = ii;
            i += SCROLL_STATE_DRAGGING;
        }
        return lastItem;
    }

    private int determineTargetPage(int currentPage, float pageOffset, int velocity, int deltaX) {
        int targetPage = (Math.abs(deltaX) <= this.mFlingDistance || Math.abs(velocity) <= this.mMinimumVelocity) ? (int) ((((float) currentPage) + pageOffset) + 0.5f) : velocity > 0 ? currentPage : currentPage + SCROLL_STATE_DRAGGING;
        if (this.mItems.size() <= 0) {
            return targetPage;
        }
        return Math.max(((ItemInfo) this.mItems.get(SCROLL_STATE_IDLE)).position, Math.min(targetPage, ((ItemInfo) this.mItems.get(this.mItems.size() + INVALID_POINTER)).position));
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        boolean needsInvalidate = DEBUG;
        int overScrollMode = ViewCompat.getOverScrollMode(this);
        if (overScrollMode == 0 || (overScrollMode == SCROLL_STATE_DRAGGING && this.mAdapter != null && this.mAdapter.getCount() > SCROLL_STATE_DRAGGING)) {
            int restoreCount;
            int height;
            int width;
            if (!this.mLeftEdge.isFinished()) {
                restoreCount = canvas.save();
                height = (getHeight() - getPaddingTop()) - getPaddingBottom();
                width = getWidth();
                canvas.rotate(270.0f);
                canvas.translate((float) ((-height) + getPaddingTop()), this.mFirstOffset * ((float) width));
                this.mLeftEdge.setSize(height, width);
                needsInvalidate = DEBUG | this.mLeftEdge.draw(canvas);
                canvas.restoreToCount(restoreCount);
            }
            if (!this.mRightEdge.isFinished()) {
                restoreCount = canvas.save();
                width = getWidth();
                height = (getHeight() - getPaddingTop()) - getPaddingBottom();
                canvas.rotate(90.0f);
                canvas.translate((float) (-getPaddingTop()), (-(this.mLastOffset + 1.0f)) * ((float) width));
                this.mRightEdge.setSize(height, width);
                needsInvalidate |= this.mRightEdge.draw(canvas);
                canvas.restoreToCount(restoreCount);
            }
        } else {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            int scrollX = getScrollX();
            int width = getWidth();
            float marginOffset = ((float) this.mPageMargin) / ((float) width);
            int itemIndex = SCROLL_STATE_IDLE;
            ItemInfo ii = (ItemInfo) this.mItems.get(SCROLL_STATE_IDLE);
            float offset = ii.offset;
            int itemCount = this.mItems.size();
            int firstPos = ii.position;
            int lastPos = ((ItemInfo) this.mItems.get(itemCount + INVALID_POINTER)).position;
            int pos = firstPos;
            while (pos < lastPos) {
                float drawAt;
                while (pos > ii.position && itemIndex < itemCount) {
                    itemIndex += SCROLL_STATE_DRAGGING;
                    ii = (ItemInfo) this.mItems.get(itemIndex);
                }
                if (pos == ii.position) {
                    drawAt = (ii.offset + ii.widthFactor) * ((float) width);
                    offset = (ii.offset + ii.widthFactor) + marginOffset;
                } else {
                    float widthFactor = this.mAdapter.getPageWidth(pos);
                    drawAt = (offset + widthFactor) * ((float) width);
                    offset += widthFactor + marginOffset;
                }
                if (((float) this.mPageMargin) + drawAt > ((float) scrollX)) {
                    this.mMarginDrawable.setBounds((int) drawAt, this.mTopPageBounds, (int) ((((float) this.mPageMargin) + drawAt) + 0.5f), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas);
                }
                if (drawAt <= ((float) (scrollX + width))) {
                    pos += SCROLL_STATE_DRAGGING;
                } else {
                    return;
                }
            }
        }
    }

    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return DEBUG;
        }
        this.mFakeDragging = true;
        setScrollState(SCROLL_STATE_DRAGGING);
        this.mLastMotionX = 0.0f;
        this.mInitialMotionX = 0.0f;
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            this.mVelocityTracker.clear();
        }
        long time = SystemClock.uptimeMillis();
        MotionEvent ev = MotionEvent.obtain(time, time, SCROLL_STATE_IDLE, 0.0f, 0.0f, SCROLL_STATE_IDLE);
        this.mVelocityTracker.addMovement(ev);
        ev.recycle();
        this.mFakeDragBeginTime = time;
        return true;
    }

    public void endFakeDrag() {
        if (this.mFakeDragging) {
            VelocityTracker velocityTracker = this.mVelocityTracker;
            velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
            int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
            this.mPopulatePending = true;
            int width = getWidth();
            int scrollX = getScrollX();
            ItemInfo ii = infoForCurrentScrollPosition();
            setCurrentItemInternal(determineTargetPage(ii.position, ((((float) scrollX) / ((float) width)) - ii.offset) / ii.widthFactor, initialVelocity, (int) (this.mLastMotionX - this.mInitialMotionX)), true, true, initialVelocity);
            endDrag();
            this.mFakeDragging = DEBUG;
            return;
        }
        throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }

    public void fakeDragBy(float xOffset) {
        if (this.mFakeDragging) {
            this.mLastMotionX += xOffset;
            float scrollX = ((float) getScrollX()) - xOffset;
            int width = getWidth();
            float leftBound = ((float) width) * this.mFirstOffset;
            float rightBound = ((float) width) * this.mLastOffset;
            ItemInfo firstItem = (ItemInfo) this.mItems.get(SCROLL_STATE_IDLE);
            ItemInfo lastItem = (ItemInfo) this.mItems.get(this.mItems.size() + INVALID_POINTER);
            if (firstItem.position != 0) {
                leftBound = firstItem.offset * ((float) width);
            }
            if (lastItem.position != this.mAdapter.getCount() + INVALID_POINTER) {
                rightBound = lastItem.offset * ((float) width);
            }
            if (scrollX < leftBound) {
                scrollX = leftBound;
            } else if (scrollX > rightBound) {
                scrollX = rightBound;
            }
            this.mLastMotionX += scrollX - ((float) ((int) scrollX));
            scrollTo((int) scrollX, getScrollY());
            pageScrolled((int) scrollX);
            MotionEvent ev = MotionEvent.obtain(this.mFakeDragBeginTime, SystemClock.uptimeMillis(), SCROLL_STATE_SETTLING, this.mLastMotionX, 0.0f, SCROLL_STATE_IDLE);
            this.mVelocityTracker.addMovement(ev);
            ev.recycle();
            return;
        }
        throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = MotionEventCompat.getActionIndex(ev);
        if (MotionEventCompat.getPointerId(ev, pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? SCROLL_STATE_DRAGGING : SCROLL_STATE_IDLE;
            this.mLastMotionX = MotionEventCompat.getX(ev, newPointerIndex);
            this.mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = DEBUG;
        this.mIsUnableToDrag = DEBUG;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void setScrollingCacheEnabled(boolean enabled) {
        if (this.mScrollingCacheEnabled != enabled) {
            this.mScrollingCacheEnabled = enabled;
        }
    }

    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            for (int i = group.getChildCount() + INVALID_POINTER; i >= 0; i += INVALID_POINTER) {
                View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() && y + scrollY >= child.getTop() && y + scrollY < child.getBottom()) {
                    if (canScroll(child, true, dx, (x + scrollX) - child.getLeft(), (y + scrollY) - child.getTop())) {
                        return true;
                    }
                }
            }
        }
        return (checkV && ViewCompat.canScrollHorizontally(v, -dx)) ? true : DEBUG;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return (super.dispatchKeyEvent(event) || executeKeyEvent(event)) ? true : DEBUG;
    }

    public boolean executeKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return DEBUG;
        }
        switch (event.getKeyCode()) {
            case WelcomeActivity.GPIO_IOCSDATAHIGH /*21*/:
                return arrowScroll(17);
            case Protocol.XNS_IDP /*22*/:
                return arrowScroll(66);
            case Service.NI_MAIL /*61*/:
                if (VERSION.SDK_INT < 11) {
                    return DEBUG;
                }
                if (KeyEventCompat.hasNoModifiers(event)) {
                    return arrowScroll(SCROLL_STATE_SETTLING);
                }
                if (KeyEventCompat.hasModifiers(event, SCROLL_STATE_DRAGGING)) {
                    return arrowScroll(SCROLL_STATE_DRAGGING);
                }
                return DEBUG;
            default:
                return DEBUG;
        }
    }

    public boolean arrowScroll(int direction) {
        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        }
        boolean handled = DEBUG;
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        if (nextFocused == null || nextFocused == currentFocused) {
            if (direction == 17 || direction == SCROLL_STATE_DRAGGING) {
                handled = pageLeft();
            } else if (direction == 66 || direction == SCROLL_STATE_SETTLING) {
                handled = pageRight();
            }
        } else if (direction == 17) {
            handled = (currentFocused == null || getChildRectInPagerCoordinates(this.mTempRect, nextFocused).left < getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left) ? nextFocused.requestFocus() : pageLeft();
        } else if (direction == 66) {
            handled = (currentFocused == null || getChildRectInPagerCoordinates(this.mTempRect, nextFocused).left > getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left) ? nextFocused.requestFocus() : pageRight();
        }
        if (handled) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
        }
        return handled;
    }

    private Rect getChildRectInPagerCoordinates(Rect outRect, View child) {
        if (outRect == null) {
            outRect = new Rect();
        }
        if (child == null) {
            outRect.set(SCROLL_STATE_IDLE, SCROLL_STATE_IDLE, SCROLL_STATE_IDLE, SCROLL_STATE_IDLE);
        } else {
            outRect.left = child.getLeft();
            outRect.right = child.getRight();
            outRect.top = child.getTop();
            outRect.bottom = child.getBottom();
            ViewGroup parent = child.getParent();
            while ((parent instanceof ViewGroup) && parent != this) {
                ViewGroup group = parent;
                outRect.left += group.getLeft();
                outRect.right += group.getRight();
                outRect.top += group.getTop();
                outRect.bottom += group.getBottom();
                parent = group.getParent();
            }
        }
        return outRect;
    }

    boolean pageLeft() {
        if (this.mCurItem <= 0) {
            return DEBUG;
        }
        setCurrentItem(this.mCurItem + INVALID_POINTER, true);
        return true;
    }

    boolean pageRight() {
        if (this.mAdapter == null || this.mCurItem >= this.mAdapter.getCount() + INVALID_POINTER) {
            return DEBUG;
        }
        setCurrentItem(this.mCurItem + SCROLL_STATE_DRAGGING, true);
        return true;
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        int focusableCount = views.size();
        int descendantFocusability = getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for (int i = SCROLL_STATE_IDLE; i < getChildCount(); i += SCROLL_STATE_DRAGGING) {
                View child = getChildAt(i);
                if (child.getVisibility() == 0) {
                    ItemInfo ii = infoForChild(child);
                    if (ii != null && ii.position == this.mCurItem) {
                        child.addFocusables(views, direction, focusableMode);
                    }
                }
            }
        }
        if ((descendantFocusability == ServiceConnection.DEFAULT_BUFFER_SIZE && focusableCount != views.size()) || !isFocusable()) {
            return;
        }
        if (((focusableMode & SCROLL_STATE_DRAGGING) != SCROLL_STATE_DRAGGING || !isInTouchMode() || isFocusableInTouchMode()) && views != null) {
            views.add(this);
        }
    }

    public void addTouchables(ArrayList<View> views) {
        for (int i = SCROLL_STATE_IDLE; i < getChildCount(); i += SCROLL_STATE_DRAGGING) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if (ii != null && ii.position == this.mCurItem) {
                    child.addTouchables(views);
                }
            }
        }
    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int index;
        int increment;
        int end;
        int count = getChildCount();
        if ((direction & SCROLL_STATE_SETTLING) != 0) {
            index = SCROLL_STATE_IDLE;
            increment = SCROLL_STATE_DRAGGING;
            end = count;
        } else {
            index = count + INVALID_POINTER;
            increment = INVALID_POINTER;
            end = INVALID_POINTER;
        }
        for (int i = index; i != end; i += increment) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if (ii != null && ii.position == this.mCurItem && child.requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }
        }
        return DEBUG;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        int childCount = getChildCount();
        for (int i = SCROLL_STATE_IDLE; i < childCount; i += SCROLL_STATE_DRAGGING) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if (ii != null && ii.position == this.mCurItem && child.dispatchPopulateAccessibilityEvent(event)) {
                    return true;
                }
            }
        }
        return DEBUG;
    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return generateDefaultLayoutParams();
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return ((p instanceof LayoutParams) && super.checkLayoutParams(p)) ? true : DEBUG;
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }
}

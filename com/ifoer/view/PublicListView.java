package com.ifoer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import java.util.Date;

public class PublicListView extends ListView implements OnScrollListener {
    private static final int DONE = 3;
    private static final int LOADING = 4;
    private static final int PULL_To_REFRESH = 1;
    private static final int RATIO = 3;
    private static final int REFRESHING = 2;
    private static final int RELEASE_To_REFRESH = 0;
    private static final String TAG = "System.out";
    private int PULL_BOTTOM_TO_TOP;
    private OnAddMoreeRefreshListener addMoreRefreshListener;
    private RotateAnimation animation;
    private ImageView arrowImageView;
    private int firstItemIndex;
    public View footView;
    private int headContentHeight;
    private int headContentWidth;
    private LinearLayout headView;
    private LayoutInflater inflater;
    private boolean isBack;
    private boolean isRecored;
    private boolean isRefreshable;
    private TextView lastUpdatedTextView;
    private GestureDetector mGestureDetector;
    private OnTouchListener mGestureListener;
    private View more_text;
    private ProgressBar moreprogressBar;
    private ProgressBar progressBar;
    private OnRefreshListener refreshListener;
    private RotateAnimation reverseAnimation;
    private int startY;
    private int state;
    private TextView tipsTextview;

    /* renamed from: com.ifoer.view.PublicListView.1 */
    class C07751 implements OnClickListener {
        C07751() {
        }

        public void onClick(View v) {
            PublicListView.this.changFootViewByScolltoFoot(true);
            PublicListView.this.addMoreRefreshListener.onAddMoreRefresh();
        }
    }

    public interface OnAddMoreeRefreshListener {
        void onAddMoreRefresh();
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    class YScrollDetector extends SimpleOnGestureListener {
        YScrollDetector() {
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceY) >= Math.abs(distanceX)) {
                return true;
            }
            return false;
        }
    }

    public PublicListView(Context context) {
        super(context);
        this.PULL_BOTTOM_TO_TOP = 5;
        init(context);
    }

    public PublicListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.PULL_BOTTOM_TO_TOP = 5;
        this.mGestureDetector = new GestureDetector(new YScrollDetector());
        init(context);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);
        return this.mGestureDetector.onTouchEvent(ev);
    }

    private void init(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.headView = (LinearLayout) this.inflater.inflate(C0136R.layout.refresh_head, null);
        this.footView = this.inflater.inflate(C0136R.layout.foot, null);
        this.arrowImageView = (ImageView) this.headView.findViewById(C0136R.id.head_arrowImageView);
        this.arrowImageView.setMinimumWidth(70);
        this.arrowImageView.setMinimumHeight(50);
        this.more_text = this.footView.findViewById(C0136R.id.add_moredata_tv);
        this.moreprogressBar = (ProgressBar) this.footView.findViewById(C0136R.id.add_mores_pb);
        this.progressBar = (ProgressBar) this.headView.findViewById(C0136R.id.head_progressBar);
        this.tipsTextview = (TextView) this.headView.findViewById(C0136R.id.head_tipsTextView);
        this.lastUpdatedTextView = (TextView) this.headView.findViewById(C0136R.id.head_lastUpdatedTextView);
        measureView(this.headView);
        this.headContentHeight = this.headView.getMeasuredHeight();
        this.headContentWidth = this.headView.getMeasuredWidth();
        this.headView.setPadding(RELEASE_To_REFRESH, this.headContentHeight * -1, RELEASE_To_REFRESH, RELEASE_To_REFRESH);
        this.headView.invalidate();
        Log.v("size", "width:" + this.headContentWidth + " height:" + this.headContentHeight);
        addHeaderView(this.headView, null, false);
        addFooterView(this.footView);
        setOnScrollListener(this);
        this.animation = new RotateAnimation(0.0f, -180.0f, PULL_To_REFRESH, 0.5f, PULL_To_REFRESH, 0.5f);
        this.animation.setInterpolator(new LinearInterpolator());
        this.animation.setDuration(250);
        this.animation.setFillAfter(true);
        this.reverseAnimation = new RotateAnimation(-180.0f, 0.0f, PULL_To_REFRESH, 0.5f, PULL_To_REFRESH, 0.5f);
        this.reverseAnimation.setInterpolator(new LinearInterpolator());
        this.reverseAnimation.setDuration(200);
        this.reverseAnimation.setFillAfter(true);
        this.state = RATIO;
        this.isRefreshable = false;
        this.footView.setOnClickListener(new C07751());
    }

    public void setMoreText(String text) {
        ((TextView) this.more_text).setText(text);
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstItemIndex = firstVisibleItem;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.isRefreshable) {
            switch (event.getAction()) {
                case RELEASE_To_REFRESH /*0*/:
                    if (this.firstItemIndex == 0 && !this.isRecored) {
                        this.isRecored = true;
                        this.startY = (int) event.getY();
                        Log.v(TAG, "\u5728down\u65f6\u5019\u8bb0\u5f55\u5f53\u524d\u4f4d\u7f6e\u2018");
                        break;
                    }
                case PULL_To_REFRESH /*1*/:
                    if (!(this.state == REFRESHING || this.state == LOADING)) {
                        if (this.state == PULL_To_REFRESH) {
                            this.state = RATIO;
                            changeHeaderViewByState();
                            Log.v(TAG, "\u7531\u4e0b\u62c9\u5237\u65b0\u72b6\u6001\uff0c\u5230done\u72b6\u6001");
                        }
                        if (this.state == 0) {
                            this.state = REFRESHING;
                            changeHeaderViewByState();
                            onRefresh();
                            Log.v(TAG, "\u7531\u677e\u5f00\u5237\u65b0\u72b6\u6001\uff0c\u5230done\u72b6\u6001");
                        }
                        if (this.state == this.PULL_BOTTOM_TO_TOP) {
                            this.state = RATIO;
                            Log.v(TAG, "\u7531\u4e0b\u62c9\u5237\u65b0\u72b6\u6001\uff0c\u5230done\u72b6\u6001");
                        }
                    }
                    this.isRecored = false;
                    this.isBack = false;
                    break;
                case REFRESHING /*2*/:
                    int tempY = (int) event.getY();
                    if (!this.isRecored && this.firstItemIndex == 0) {
                        Log.v(TAG, "\u5728move\u65f6\u5019\u8bb0\u5f55\u4e0b\u4f4d\u7f6e");
                        this.isRecored = true;
                        this.startY = tempY;
                    }
                    if (!(this.state == REFRESHING || !this.isRecored || this.state == LOADING)) {
                        if (this.state == 0) {
                            setSelection(RELEASE_To_REFRESH);
                            if ((tempY - this.startY) / RATIO < this.headContentHeight && tempY - this.startY > 0) {
                                this.state = PULL_To_REFRESH;
                                changeHeaderViewByState();
                                Log.v(TAG, "\u7531\u677e\u5f00\u5237\u65b0\u72b6\u6001\u8f6c\u53d8\u5230\u4e0b\u62c9\u5237\u65b0\u72b6\u6001");
                            } else if (tempY - this.startY <= 0) {
                                this.state = RATIO;
                                changeHeaderViewByState();
                                Log.v(TAG, "\u7531\u677e\u5f00\u5237\u65b0\u72b6\u6001\u8f6c\u53d8\u5230done\u72b6\u6001");
                            }
                        }
                        if (this.state == PULL_To_REFRESH) {
                            setSelection(RELEASE_To_REFRESH);
                            if ((tempY - this.startY) / RATIO >= this.headContentHeight) {
                                this.state = RELEASE_To_REFRESH;
                                this.isBack = true;
                                changeHeaderViewByState();
                                Log.v(TAG, "\u7531done\u6216\u8005\u4e0b\u62c9\u5237\u65b0\u72b6\u6001\u8f6c\u53d8\u5230\u677e\u5f00\u5237\u65b0");
                            } else if (tempY - this.startY <= 0) {
                                this.state = RATIO;
                                changeHeaderViewByState();
                                Log.v(TAG, "\u7531DOne\u6216\u8005\u4e0b\u62c9\u5237\u65b0\u72b6\u6001\u8f6c\u53d8\u5230done\u72b6\u6001");
                            }
                        }
                        if (this.state == RATIO && tempY - this.startY > 0) {
                            this.state = PULL_To_REFRESH;
                            changeHeaderViewByState();
                        }
                        if (this.state == PULL_To_REFRESH) {
                            this.headView.setPadding(RELEASE_To_REFRESH, (this.headContentHeight * -1) + ((tempY - this.startY) / RATIO), RELEASE_To_REFRESH, RELEASE_To_REFRESH);
                        }
                        if (this.state == 0) {
                            this.headView.setPadding(RELEASE_To_REFRESH, ((tempY - this.startY) / RATIO) - this.headContentHeight, RELEASE_To_REFRESH, RELEASE_To_REFRESH);
                            break;
                        }
                    }
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    private void changFootViewByScolltoFoot(boolean b) {
        System.out.println("changFootViewByScolltoFoot: " + b);
        if (b) {
            this.moreprogressBar.setVisibility(RELEASE_To_REFRESH);
            this.footView.setClickable(false);
            return;
        }
        this.moreprogressBar.setVisibility(8);
        this.footView.setClickable(true);
    }

    private void changeHeaderViewByState() {
        switch (this.state) {
            case RELEASE_To_REFRESH /*0*/:
                this.arrowImageView.setVisibility(RELEASE_To_REFRESH);
                this.progressBar.setVisibility(8);
                this.tipsTextview.setVisibility(RELEASE_To_REFRESH);
                this.lastUpdatedTextView.setVisibility(RELEASE_To_REFRESH);
                this.arrowImageView.clearAnimation();
                this.arrowImageView.startAnimation(this.animation);
                this.tipsTextview.setText(getResources().getText(C0136R.string.pull_to_refresh));
                Log.v(TAG, "\u5f53\u524d\u72b6\u6001\uff0c\u677e\u5f00\u5237\u65b0");
            case PULL_To_REFRESH /*1*/:
                this.progressBar.setVisibility(8);
                this.tipsTextview.setVisibility(RELEASE_To_REFRESH);
                this.lastUpdatedTextView.setVisibility(RELEASE_To_REFRESH);
                this.arrowImageView.clearAnimation();
                this.arrowImageView.setVisibility(RELEASE_To_REFRESH);
                if (this.isBack) {
                    this.isBack = false;
                    this.arrowImageView.clearAnimation();
                    this.arrowImageView.startAnimation(this.reverseAnimation);
                    this.tipsTextview.setText("\u4e0b\u62c9\u5237\u65b0");
                } else {
                    this.tipsTextview.setText("\u4e0b\u62c9\u5237\u65b0");
                }
                Log.v(TAG, "\u5f53\u524d\u72b6\u6001\uff0c\u4e0b\u62c9\u5237\u65b0");
            case REFRESHING /*2*/:
                this.headView.setPadding(RELEASE_To_REFRESH, RELEASE_To_REFRESH, RELEASE_To_REFRESH, RELEASE_To_REFRESH);
                this.progressBar.setVisibility(RELEASE_To_REFRESH);
                this.arrowImageView.clearAnimation();
                this.arrowImageView.setVisibility(8);
                this.tipsTextview.setText("\u6b63\u5728\u5237\u65b0...");
                this.lastUpdatedTextView.setVisibility(RELEASE_To_REFRESH);
                Log.v(TAG, "\u5f53\u524d\u72b6\u6001,\u6b63\u5728\u5237\u65b0...");
            case RATIO /*3*/:
                this.headView.setPadding(RELEASE_To_REFRESH, this.headContentHeight * -1, RELEASE_To_REFRESH, RELEASE_To_REFRESH);
                this.progressBar.setVisibility(8);
                this.arrowImageView.clearAnimation();
                this.arrowImageView.setImageResource(C0136R.drawable.arrow);
                this.tipsTextview.setText("\u4e0b\u62c9\u5237\u65b0");
                this.lastUpdatedTextView.setVisibility(RELEASE_To_REFRESH);
                Log.v(TAG, "\u5f53\u524d\u72b6\u6001\uff0cdone");
            default:
        }
    }

    public void setonRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
        this.isRefreshable = true;
    }

    public void setonAddMoreeRefreshListener(OnAddMoreeRefreshListener addMoreeRefreshListener) {
        this.addMoreRefreshListener = addMoreeRefreshListener;
    }

    public void onAddMoreRefresh() {
    }

    public void onAddMoreRefreshComplete() {
        changFootViewByScolltoFoot(false);
    }

    public void onRefreshComplete() {
        this.state = RATIO;
        this.lastUpdatedTextView.setText(new StringBuilder(String.valueOf(getResources().getText(C0136R.string.recent_updates).toString())).append(new Date().toLocaleString()).toString());
        changeHeaderViewByState();
    }

    private void onRefresh() {
        if (this.refreshListener != null) {
            this.refreshListener.onRefresh();
        }
    }

    private void measureView(View child) {
        int childHeightSpec;
        LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new LayoutParams(-1, -2);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(RELEASE_To_REFRESH, RELEASE_To_REFRESH, p.width);
        int lpHeight = p.height;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, 1073741824);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(RELEASE_To_REFRESH, RELEASE_To_REFRESH);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void setAdapter(BaseAdapter adapter) {
        this.lastUpdatedTextView.setText("\u6700\u8fd1\u66f4\u65b0:" + new Date().toLocaleString());
        super.setAdapter(adapter);
    }
}

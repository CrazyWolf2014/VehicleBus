package com.ifoer.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View.MeasureSpec;
import android.widget.RelativeLayout;
import com.cnlaunch.x431frame.C0136R;

public class MostChartPlayGridView extends RelativeLayout {
    private Context mContext;
    private boolean mIfshowOneChart;
    private int mRowNum;

    public MostChartPlayGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mRowNum = 1;
        this.mIfshowOneChart = false;
        this.mContext = context;
    }

    public MostChartPlayGridView(Context context) {
        super(context);
        this.mRowNum = 1;
        this.mIfshowOneChart = false;
        this.mContext = context;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int screenHeight;
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        Activity mActivity = this.mContext;
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        Rect frame = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        if (this.mIfshowOneChart) {
            screenHeight = ((metric.heightPixels - (((int) this.mContext.getResources().getDimension(C0136R.dimen.top_title_height)) * 2)) - statusBarHeight) / this.mRowNum;
        } else {
            screenHeight = ((metric.heightPixels - ((int) this.mContext.getResources().getDimension(C0136R.dimen.top_title_height))) - statusBarHeight) / this.mRowNum;
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(screenHeight - 18, 1073741824));
    }

    public void SetRowNum(int rowNum) {
        this.mRowNum = rowNum;
    }

    public void SetIfshowOneChart(boolean ifshowOneChart) {
        this.mIfshowOneChart = ifshowOneChart;
    }
}

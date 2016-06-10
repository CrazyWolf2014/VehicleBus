package com.cnlaunch.x431pro.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ListView;

public class NoScrollerListView extends ListView {
    public NoScrollerListView(Context context) {
        super(context);
    }

    public NoScrollerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(536870911, Integer.MIN_VALUE));
    }
}

package com.cnlaunch.x431pro.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import org.xbill.DNS.KEYRecord;

public class CompaScrollerListView extends ListView {
    private ScrollView scrollView;

    public CompaScrollerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CompaScrollerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CompaScrollerListView(Context context) {
        super(context);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case KEYRecord.OWNER_USER /*0*/:
                if (this.scrollView != null) {
                    this.scrollView.requestDisallowInterceptTouchEvent(true);
                    break;
                }
                break;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                if (this.scrollView != null) {
                    this.scrollView.requestDisallowInterceptTouchEvent(false);
                    break;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public ScrollView getScrollView() {
        return this.scrollView;
    }

    public void setScrollView(ScrollView scrollView) {
        this.scrollView = scrollView;
    }
}

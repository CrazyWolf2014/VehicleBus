package com.ifoer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MTextView extends TextView {
    public MTextView(Context context) {
        super(context);
    }

    public MTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isFocused() {
        return true;
    }
}

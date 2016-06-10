package com.cnlaunch.x431pro.widget.button;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.RadioButton;

public class MequeeRadioButton extends RadioButton {
    public MequeeRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MequeeRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MequeeRadioButton(Context context) {
        super(context);
    }

    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (focused) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);
        }
    }

    @ExportedProperty(category = "focus")
    public boolean isFocused() {
        return true;
    }
}

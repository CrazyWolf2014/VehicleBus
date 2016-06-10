package com.cnlaunch.x431pro.widget.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;
import java.lang.reflect.Field;

public class NoScrollGallery extends Gallery {
    private IGalleryEvent mListener;

    public NoScrollGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public NoScrollGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollGallery(Context context) {
        super(context);
    }

    public void setGalleryEventListener(IGalleryEvent listener) {
        this.mListener = listener;
    }

    public boolean onSingleTapUp(MotionEvent e) {
        try {
            Field f = NoScrollGallery.class.getSuperclass().getDeclaredField("mDownTouchPosition");
            f.setAccessible(true);
            int position = f.getInt(this);
            if (this.mListener != null && position >= 0) {
                this.mListener.OnItemClick(position);
            }
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (NoSuchFieldException e12) {
            e12.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
        return false;
    }
}

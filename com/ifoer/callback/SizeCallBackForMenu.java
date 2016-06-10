package com.ifoer.callback;

import android.widget.RelativeLayout;
import com.ifoer.expeditionphone.MainActivity;

public class SizeCallBackForMenu implements SizeCallBack {
    private RelativeLayout menu;
    private int menuWidth;

    public SizeCallBackForMenu(RelativeLayout menu) {
        this.menu = menu;
    }

    public void onGlobalLayout() {
        this.menuWidth = MainActivity.ENLARGE_WIDTH;
    }

    public void getViewSize(int idx, int width, int height, int[] dims) {
        dims[0] = width;
        dims[1] = height;
        if (idx != 1) {
            dims[0] = MainActivity.ENLARGE_WIDTH;
        }
    }
}

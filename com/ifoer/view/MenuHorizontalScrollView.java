package com.ifoer.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.ifoer.callback.SizeCallBack;
import com.ifoer.expeditionphone.MainActivity;

public class MenuHorizontalScrollView extends HorizontalScrollView {
    private int current;
    private MenuHorizontalScrollView me;
    private RelativeLayout menu;
    private LinearLayout menuBtn;
    private boolean menuOut;
    private int menuWidth;
    private int scrollToViewPos;

    public class MenuOnGlobalLayoutListener implements OnGlobalLayoutListener {
        private View[] children;
        private ViewGroup parent;
        private SizeCallBack sizeCallBack;

        /* renamed from: com.ifoer.view.MenuHorizontalScrollView.MenuOnGlobalLayoutListener.1 */
        class C07701 implements Runnable {
            C07701() {
            }

            public void run() {
                MenuHorizontalScrollView.this.me.scrollBy(MenuHorizontalScrollView.this.scrollToViewPos, 0);
                MenuHorizontalScrollView.this.me.setVisibility(0);
                MenuHorizontalScrollView.this.menu.setVisibility(0);
            }
        }

        public MenuOnGlobalLayoutListener(ViewGroup parent, View[] children, SizeCallBack sizeCallBack) {
            this.parent = parent;
            this.children = children;
            this.sizeCallBack = sizeCallBack;
        }

        public void onGlobalLayout() {
            MenuHorizontalScrollView.this.me.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            this.sizeCallBack.onGlobalLayout();
            this.parent.removeViewsInLayout(0, this.children.length);
            int width = MenuHorizontalScrollView.this.me.getMeasuredWidth();
            int height = MenuHorizontalScrollView.this.me.getMeasuredHeight();
            int[] dims = new int[2];
            MenuHorizontalScrollView.this.scrollToViewPos = 0;
            for (int i = 0; i < this.children.length; i++) {
                this.sizeCallBack.getViewSize(i, width, height, dims);
                this.children[i].setVisibility(0);
                this.parent.addView(this.children[i], dims[0], dims[1]);
                if (i == 0) {
                    MenuHorizontalScrollView menuHorizontalScrollView = MenuHorizontalScrollView.this;
                    menuHorizontalScrollView.scrollToViewPos = menuHorizontalScrollView.scrollToViewPos + dims[0];
                }
            }
            new Handler().post(new C07701());
        }
    }

    public MenuHorizontalScrollView(Context context) {
        super(context);
        init();
    }

    public MenuHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MenuHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setHorizontalFadingEdgeEnabled(false);
        setVerticalFadingEdgeEnabled(false);
        this.me = this;
        this.me.setVisibility(4);
        this.menuOut = false;
    }

    public void initViews(View[] children, SizeCallBack sizeCallBack, RelativeLayout menu) {
        this.menu = menu;
        ViewGroup parent = (ViewGroup) getChildAt(0);
        for (int i = 0; i < children.length; i++) {
            children[i].setVisibility(4);
            parent.addView(children[i]);
        }
        getViewTreeObserver().addOnGlobalLayoutListener(new MenuOnGlobalLayoutListener(parent, children, sizeCallBack));
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    public void setMenuBtn(LinearLayout btn) {
        this.menuBtn = btn;
    }

    public void clickMenuBtn() {
        if (this.menuOut) {
            this.menuWidth = MainActivity.ENLARGE_WIDTH;
        } else {
            this.menuWidth = 0;
        }
        menuSlide();
    }

    private void menuSlide() {
        if (this.menuWidth == 0) {
            this.menuOut = true;
        } else {
            this.menuOut = false;
        }
        this.me.scrollTo(this.menuWidth, 0);
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (l < MainActivity.ENLARGE_WIDTH) {
            this.menuWidth = 0;
        } else {
            this.menuWidth = MainActivity.ENLARGE_WIDTH;
        }
        this.current = l;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        if (this.current == 0 && x < this.scrollToViewPos) {
            return false;
        }
        if (this.current == this.scrollToViewPos * 2 && x > MainActivity.ENLARGE_WIDTH) {
            return false;
        }
        if (ev.getAction() != 1) {
            return super.onTouchEvent(ev);
        }
        menuSlide();
        return false;
    }
}

package com.amap.mapapi.map;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import com.amap.mapapi.map.MapView.C0096g;

public class ZoomButtonsController implements OnTouchListener {
    private C0096g f511a;
    private ViewGroup f512b;
    private OnZoomListener f513c;

    public interface OnZoomListener {
        void onVisibilityChanged(boolean z);

        void onZoom(boolean z);
    }

    public ZoomButtonsController(View view) {
        this.f512b = (MapView) view;
        this.f511a = ((MapView) view).getZoomMgr();
    }

    public ViewGroup getContainer() {
        return this.f512b;
    }

    public View getZoomControls() {
        return null;
    }

    public void setZoomSpeed(long j) {
    }

    public void setFocusable(boolean z) {
    }

    public boolean isAutoDismissed() {
        return false;
    }

    public void setAutoDismissed(boolean z) {
    }

    public void setZoomInEnabled(boolean z) {
        this.f511a.m624f().setEnabled(z);
    }

    public boolean isVisible() {
        return this.f511a.m619b();
    }

    public void setVisible(boolean z) {
        this.f511a.m617a(z);
    }

    public void setOnZoomListener(OnZoomListener onZoomListener) {
        this.f513c = onZoomListener;
    }

    public OnZoomListener getOnZoomListener() {
        return this.f513c;
    }

    public void setZoomOutEnabled(boolean z) {
        this.f511a.m625g().setEnabled(z);
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}

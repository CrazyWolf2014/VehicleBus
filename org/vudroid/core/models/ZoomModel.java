package org.vudroid.core.models;

import org.vudroid.core.events.BringUpZoomControlsEvent;
import org.vudroid.core.events.EventDispatcher;
import org.vudroid.core.events.ZoomChangedEvent;
import org.vudroid.core.events.ZoomListener.CommitZoomEvent;

public class ZoomModel extends EventDispatcher {
    private static final float INCREMENT_DELTA = 0.05f;
    private boolean horizontalScrollEnabled;
    private boolean isCommited;
    private float zoom;

    public ZoomModel() {
        this.zoom = 1.0f;
    }

    public void setZoom(float zoom) {
        zoom = Math.max(zoom, 1.0f);
        if (this.zoom != zoom) {
            float oldZoom = this.zoom;
            this.zoom = zoom;
            this.isCommited = false;
            dispatch(new ZoomChangedEvent(zoom, oldZoom));
        }
    }

    public float getZoom() {
        return this.zoom;
    }

    public void increaseZoom() {
        setZoom(getZoom() + INCREMENT_DELTA);
    }

    public void decreaseZoom() {
        setZoom(getZoom() - INCREMENT_DELTA);
    }

    public void toggleZoomControls() {
        dispatch(new BringUpZoomControlsEvent());
    }

    public void setHorizontalScrollEnabled(boolean horizontalScrollEnabled) {
        this.horizontalScrollEnabled = horizontalScrollEnabled;
    }

    public boolean isHorizontalScrollEnabled() {
        return this.horizontalScrollEnabled;
    }

    public boolean canDecrement() {
        return this.zoom > 1.0f;
    }

    public void commit() {
        if (!this.isCommited) {
            this.isCommited = true;
            dispatch(new CommitZoomEvent());
        }
    }
}

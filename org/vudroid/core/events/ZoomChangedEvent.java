package org.vudroid.core.events;

public class ZoomChangedEvent extends SafeEvent<ZoomListener> {
    private final float newZoom;
    private final float oldZoom;

    public ZoomChangedEvent(float newZoom, float oldZoom) {
        this.newZoom = newZoom;
        this.oldZoom = oldZoom;
    }

    public void dispatchSafely(ZoomListener listener) {
        listener.zoomChanged(this.newZoom, this.oldZoom);
    }
}

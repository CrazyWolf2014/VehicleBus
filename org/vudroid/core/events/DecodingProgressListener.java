package org.vudroid.core.events;

public interface DecodingProgressListener {

    public static class DecodingProgressEvent extends SafeEvent<DecodingProgressListener> {
        private final int currentlyDecoding;

        public DecodingProgressEvent(int currentlyDecoding) {
            this.currentlyDecoding = currentlyDecoding;
        }

        public void dispatchSafely(DecodingProgressListener listener) {
            listener.decodingProgressChanged(this.currentlyDecoding);
        }
    }

    void decodingProgressChanged(int i);
}

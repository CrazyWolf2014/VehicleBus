package com.hp.hpl.sparta;

import android.support.v4.view.accessibility.AccessibilityEventCompat;

class CharCircBuffer {
    private final int[] buf_;
    private boolean enabled_;
    private int next_;
    private int total_;

    CharCircBuffer(int i) {
        this.next_ = 0;
        this.total_ = 0;
        this.enabled_ = true;
        this.buf_ = new int[i];
    }

    private void addRaw(int i) {
        if (this.enabled_) {
            this.buf_[this.next_] = i;
            this.next_ = (this.next_ + 1) % this.buf_.length;
            this.total_++;
        }
    }

    void addChar(char c) {
        addRaw(c);
    }

    void addInt(int i) {
        addRaw(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED + i);
    }

    void addString(String str) {
        for (char addChar : str.toCharArray()) {
            addChar(addChar);
        }
    }

    void disable() {
        this.enabled_ = false;
    }

    void enable() {
        this.enabled_ = true;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer((this.buf_.length * 11) / 10);
        int length = this.total_ < this.buf_.length ? this.buf_.length - this.total_ : 0;
        while (length < this.buf_.length) {
            int i = this.buf_[(this.next_ + length) % this.buf_.length];
            if (i < AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED) {
                stringBuffer.append((char) i);
            } else {
                stringBuffer.append(Integer.toString(i - AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED));
            }
            length++;
        }
        return stringBuffer.toString();
    }
}

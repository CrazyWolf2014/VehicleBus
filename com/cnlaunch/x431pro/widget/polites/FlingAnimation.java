package com.cnlaunch.x431pro.widget.polites;

public class FlingAnimation implements Animation {
    private float factor;
    private FlingAnimationListener listener;
    private float threshold;
    private float velocityX;
    private float velocityY;

    public FlingAnimation() {
        this.factor = 0.95f;
        this.threshold = 10.0f;
    }

    public boolean update(GestureImageView view, long time) {
        float seconds = ((float) time) / 1000.0f;
        float dx = this.velocityX * seconds;
        float dy = this.velocityY * seconds;
        this.velocityX *= this.factor;
        this.velocityY *= this.factor;
        boolean active = Math.abs(this.velocityX) > this.threshold && Math.abs(this.velocityY) > this.threshold;
        if (this.listener != null) {
            this.listener.onMove(dx, dy);
            if (!active) {
                this.listener.onComplete();
            }
        }
        return active;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }

    public void setListener(FlingAnimationListener listener) {
        this.listener = listener;
    }
}

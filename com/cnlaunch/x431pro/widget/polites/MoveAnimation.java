package com.cnlaunch.x431pro.widget.polites;

public class MoveAnimation implements Animation {
    private long animationTimeMS;
    private boolean firstFrame;
    private MoveAnimationListener moveAnimationListener;
    private float startX;
    private float startY;
    private float targetX;
    private float targetY;
    private long totalTime;

    public MoveAnimation() {
        this.firstFrame = true;
        this.animationTimeMS = 100;
        this.totalTime = 0;
    }

    public boolean update(GestureImageView view, long time) {
        this.totalTime += time;
        if (this.firstFrame) {
            this.firstFrame = false;
            this.startX = view.getImageX();
            this.startY = view.getImageY();
        }
        if (this.totalTime < this.animationTimeMS) {
            float ratio = ((float) this.totalTime) / ((float) this.animationTimeMS);
            float newX = ((this.targetX - this.startX) * ratio) + this.startX;
            float newY = ((this.targetY - this.startY) * ratio) + this.startY;
            if (this.moveAnimationListener != null) {
                this.moveAnimationListener.onMove(newX, newY);
            }
            return true;
        } else if (this.moveAnimationListener == null) {
            return false;
        } else {
            this.moveAnimationListener.onMove(this.targetX, this.targetY);
            return false;
        }
    }

    public void reset() {
        this.firstFrame = true;
        this.totalTime = 0;
    }

    public float getTargetX() {
        return this.targetX;
    }

    public void setTargetX(float targetX) {
        this.targetX = targetX;
    }

    public float getTargetY() {
        return this.targetY;
    }

    public void setTargetY(float targetY) {
        this.targetY = targetY;
    }

    public long getAnimationTimeMS() {
        return this.animationTimeMS;
    }

    public void setAnimationTimeMS(long animationTimeMS) {
        this.animationTimeMS = animationTimeMS;
    }

    public void setMoveAnimationListener(MoveAnimationListener moveAnimationListener) {
        this.moveAnimationListener = moveAnimationListener;
    }
}

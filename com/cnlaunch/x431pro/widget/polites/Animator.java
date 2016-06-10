package com.cnlaunch.x431pro.widget.polites;

public class Animator extends Thread {
    private boolean active;
    private Animation animation;
    private long lastTime;
    private boolean running;
    private GestureImageView view;

    public Animator(GestureImageView view, String threadName) {
        super(threadName);
        this.running = false;
        this.active = false;
        this.lastTime = -1;
        this.view = view;
    }

    public void run() {
        this.running = true;
        while (this.running) {
            while (this.active && this.animation != null) {
                long time = System.currentTimeMillis();
                this.active = this.animation.update(this.view, time - this.lastTime);
                this.view.redraw();
                this.lastTime = time;
                while (this.active) {
                    try {
                        if (this.view.waitForDraw(32)) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        this.active = false;
                    }
                }
            }
            synchronized (this) {
                if (this.running) {
                    try {
                        wait();
                    } catch (InterruptedException e2) {
                    }
                }
            }
        }
    }

    public synchronized void finish() {
        this.running = false;
        this.active = false;
        notifyAll();
    }

    public void play(Animation transformer) {
        if (this.active) {
            cancel();
        }
        this.animation = transformer;
        activate();
    }

    public synchronized void activate() {
        this.lastTime = System.currentTimeMillis();
        this.active = true;
        notifyAll();
    }

    public void cancel() {
        this.active = false;
    }
}

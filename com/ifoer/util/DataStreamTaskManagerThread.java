package com.ifoer.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataStreamTaskManagerThread implements Runnable {
    private final int POOL_SIZE;
    private final int SLEEP_TIME;
    private DataStreamTaskManager downloadTaskManager;
    private boolean isStop;
    private ExecutorService pool;

    public DataStreamTaskManagerThread() {
        this.POOL_SIZE = 1;
        this.SLEEP_TIME = 1000;
        this.isStop = false;
        this.downloadTaskManager = DataStreamTaskManager.getInstance();
        this.pool = Executors.newFixedThreadPool(1);
    }

    public void run() {
        while (!this.isStop) {
            DataStreamTask dataStreamTask = this.downloadTaskManager.getDownloadTask();
            if (dataStreamTask != null) {
                this.pool.execute(dataStreamTask);
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (this.isStop) {
            this.pool.shutdown();
        }
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }
}

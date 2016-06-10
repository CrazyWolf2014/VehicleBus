package com.ifoer.download;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadBinManagerThread implements Runnable {
    private final int POOL_SIZE;
    private final int SLEEP_TIME;
    private DownloadBinManager downloadTaskManager;
    private boolean isStop;
    private ExecutorService pool;

    public DownloadBinManagerThread() {
        this.POOL_SIZE = 2;
        this.SLEEP_TIME = 1000;
        this.isStop = false;
        this.downloadTaskManager = DownloadBinManager.getInstance();
        this.pool = Executors.newFixedThreadPool(2);
    }

    public void run() {
        while (!this.isStop) {
            DownloadBinNewVersion downloadTask = this.downloadTaskManager.getDownloadTask();
            if (downloadTask != null) {
                this.pool.execute(downloadTask);
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

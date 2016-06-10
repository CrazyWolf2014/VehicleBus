package com.ifoer.download;

import com.ifoer.entity.Constant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadTaskManagerThread implements Runnable {
    private final int POOL_SIZE;
    private final int SLEEP_TIME;
    private DownloadTaskManager downloadTaskManager;
    private boolean isStop;
    private ExecutorService pool;

    public DownloadTaskManagerThread() {
        this.POOL_SIZE = 4;
        this.SLEEP_TIME = 1000;
        this.isStop = false;
        this.downloadTaskManager = DownloadTaskManager.getInstance();
        this.pool = Executors.newFixedThreadPool(4);
    }

    public void run() {
        while (!this.isStop && !Constant.needExistDownLoad) {
            DownloadTask downloadTask = this.downloadTaskManager.getDownloadTask();
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

package com.ifoer.download;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class DownloadBinManager {
    private static final String TAG = "DownloadTaskManager";
    private static DownloadBinManager downloadTaskMananger;
    private boolean f1226D;
    private LinkedList<DownloadBinNewVersion> downloadTasks;
    public Set<String> taskIdSet;

    private DownloadBinManager() {
        this.f1226D = false;
        this.downloadTasks = new LinkedList();
        this.taskIdSet = new HashSet();
    }

    public static synchronized DownloadBinManager getInstance() {
        DownloadBinManager downloadBinManager;
        synchronized (DownloadBinManager.class) {
            if (downloadTaskMananger == null) {
                downloadTaskMananger = new DownloadBinManager();
            }
            downloadBinManager = downloadTaskMananger;
        }
        return downloadBinManager;
    }

    public void addDownloadTask(DownloadBinNewVersion downloadTask) {
        synchronized (this.downloadTasks) {
            if (!isTaskRepeat(downloadTask.getFileId())) {
                this.downloadTasks.addLast(downloadTask);
            }
        }
    }

    public boolean isTaskRepeat(String fileId) {
        synchronized (this.taskIdSet) {
            if (this.taskIdSet.contains(fileId)) {
                return true;
            }
            if (this.f1226D) {
                System.out.println("\u4e0b\u8f7d\u7ba1\u7406\u5668\u589e\u52a0\u4e0b\u8f7d\u4efb\u52a1\uff1a" + fileId);
            }
            this.taskIdSet.add(fileId);
            return false;
        }
    }

    public DownloadBinNewVersion getDownloadTask() {
        synchronized (this.downloadTasks) {
            if (this.downloadTasks.size() > 0) {
                if (this.f1226D) {
                    System.out.println("\u4e0b\u8f7d\u7ba1\u7406\u5668\u589e\u52a0\u4e0b\u8f7d\u4efb\u52a1\uff1a\u53d6\u51fa\u4efb\u52a1");
                }
                DownloadBinNewVersion downloadTask = (DownloadBinNewVersion) this.downloadTasks.removeFirst();
                return downloadTask;
            }
            return null;
        }
    }
}

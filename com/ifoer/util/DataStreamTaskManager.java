package com.ifoer.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class DataStreamTaskManager {
    private static final String TAG = "DownloadTaskManager";
    private static DataStreamTaskManager downloadTaskMananger;
    public Set<String> taskIdSet;
    private LinkedList<DataStreamTask> tasks;

    private DataStreamTaskManager() {
        this.tasks = new LinkedList();
        this.taskIdSet = new HashSet();
    }

    public static synchronized DataStreamTaskManager getInstance() {
        DataStreamTaskManager dataStreamTaskManager;
        synchronized (DataStreamTaskManager.class) {
            if (downloadTaskMananger == null) {
                downloadTaskMananger = new DataStreamTaskManager();
            }
            dataStreamTaskManager = downloadTaskMananger;
        }
        return dataStreamTaskManager;
    }

    public void addDownloadTask(DataStreamTask dataStreamTask) {
        synchronized (this.tasks) {
            this.tasks.addLast(dataStreamTask);
        }
    }

    public boolean isTaskRepeat(String fileId) {
        synchronized (this.taskIdSet) {
            if (this.taskIdSet.contains(fileId)) {
                return true;
            }
            this.taskIdSet.add(fileId);
            return false;
        }
    }

    public DataStreamTask getDownloadTask() {
        synchronized (this.tasks) {
            if (this.tasks.size() > 0) {
                DataStreamTask dataStreamTask = (DataStreamTask) this.tasks.removeFirst();
                return dataStreamTask;
            }
            return null;
        }
    }
}

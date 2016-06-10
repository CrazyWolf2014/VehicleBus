package com.ifoer.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class DataStreamChartTaskManager {
    private static final String TAG = "DataStreamChartTaskManager";
    private static DataStreamChartTaskManager downloadTaskMananger;
    public Set<String> taskIdSet;
    private LinkedList<DataStreamChartTask> tasks;

    private DataStreamChartTaskManager() {
        this.tasks = new LinkedList();
        this.taskIdSet = new HashSet();
    }

    public static synchronized DataStreamChartTaskManager getInstance() {
        DataStreamChartTaskManager dataStreamChartTaskManager;
        synchronized (DataStreamChartTaskManager.class) {
            if (downloadTaskMananger == null) {
                downloadTaskMananger = new DataStreamChartTaskManager();
            }
            dataStreamChartTaskManager = downloadTaskMananger;
        }
        return dataStreamChartTaskManager;
    }

    public void addDownloadTask(DataStreamChartTask dataStreamTask) {
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

    public DataStreamChartTask getDownloadTask() {
        synchronized (this.tasks) {
            if (this.tasks.size() > 0) {
                DataStreamChartTask dataStreamTask = (DataStreamChartTask) this.tasks.removeFirst();
                return dataStreamTask;
            }
            return null;
        }
    }
}

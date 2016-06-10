package com.ifoer.util;

import android.os.Handler;
import com.ifoer.entity.Constant;
import java.util.ArrayList;
import java.util.List;

public class DataStreamChartTask implements Runnable {
    private Handler handler;
    private List<ArrayList<?>> lists;
    private ArrayList<?> sptList;

    public DataStreamChartTask(ArrayList<?> sptList, List<ArrayList<?>> lists, Handler handler) {
        this.sptList = sptList;
        this.lists = lists;
        this.handler = handler;
    }

    public void run() {
        if (this.lists.size() >= Constant.GRAPHIC_X) {
            this.lists.remove(0);
            this.lists.add(this.sptList);
        } else {
            this.lists.add(this.sptList);
        }
        this.handler.obtainMessage(18).sendToTarget();
    }
}

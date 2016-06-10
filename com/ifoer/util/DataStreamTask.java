package com.ifoer.util;

import android.content.Context;
import android.os.Handler;
import com.cnlaunch.mycar.jni.JniX431FileTest;
import com.ifoer.entity.Constant;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptVwDataStreamIdItem;
import java.util.ArrayList;
import java.util.List;

public class DataStreamTask implements Runnable {
    private boolean f1306D;
    private boolean basicFlag;
    private Context context;
    private ArrayList<SptExDataStreamIdItem> exDataStreamIdlist;
    private int grp;
    private Handler handler;
    private boolean isReceiver;
    private boolean isRecord;
    private JniX431FileTest jnitest;
    private List<ArrayList<?>> lists;
    private boolean openFlag;
    private ArrayList<?> sptList;
    private List<Double> temp;
    private ArrayList<SptVwDataStreamIdItem> vwDataStreamIdItem;

    public DataStreamTask(Context context, ArrayList<?> sptList, List<ArrayList<?>> lists, JniX431FileTest jnitest, int grp, boolean openFlag, boolean basicFlag, boolean isReceiver, boolean isRecord, Handler handler) {
        this.temp = new ArrayList();
        this.f1306D = false;
        this.context = context;
        this.sptList = sptList;
        this.lists = lists;
        this.jnitest = jnitest;
        this.grp = grp;
        this.openFlag = openFlag;
        this.basicFlag = basicFlag;
        this.isReceiver = isReceiver;
        this.isRecord = isRecord;
        this.handler = handler;
    }

    public void run() {
        if (this.lists.size() >= Constant.GRAPHIC_X) {
            this.lists.remove(0);
            this.lists.add(this.sptList);
        } else {
            this.lists.add(this.sptList);
        }
        if (this.isReceiver && this.isRecord) {
            if (this.sptList.get(0) instanceof SptExDataStreamIdItem) {
                this.exDataStreamIdlist = this.sptList;
                if (!Constant.needWait) {
                    if (!(Constant.bBasicFlag || !Constant.bOpenFlag || Constant.hasExucet)) {
                        Constant.hasExucet = true;
                        if (this.jnitest.writeDsBasics(this.grp, this.exDataStreamIdlist)) {
                            Constant.needWait = true;
                            this.handler.obtainMessage(17).sendToTarget();
                        } else {
                            this.handler.obtainMessage(16).sendToTarget();
                        }
                    }
                    if (Constant.bOpenFlag && Constant.bBasicFlag) {
                        if (Constant.hasExucet) {
                            Constant.hasExucet = false;
                        }
                        this.jnitest.writeDSDate(this.grp, this.exDataStreamIdlist);
                    }
                }
            } else {
                this.vwDataStreamIdItem = this.sptList;
                if (!Constant.bBasicFlag && Constant.bOpenFlag) {
                    if (this.jnitest.writeVWDsBasics(this.grp, this.vwDataStreamIdItem)) {
                        this.handler.obtainMessage(17).sendToTarget();
                    } else {
                        this.handler.obtainMessage(16).sendToTarget();
                    }
                }
                if (Constant.bOpenFlag && Constant.bBasicFlag) {
                    this.jnitest.writeVWDSDate(this.grp, this.vwDataStreamIdItem);
                }
            }
        }
        this.handler.obtainMessage(18).sendToTarget();
    }
}

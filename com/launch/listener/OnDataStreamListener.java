package com.launch.listener;

import com.ifoer.entity.SptExDataStreamIdItem;
import java.util.ArrayList;

public interface OnDataStreamListener {
    void onHandleData(ArrayList<SptExDataStreamIdItem> arrayList);
}

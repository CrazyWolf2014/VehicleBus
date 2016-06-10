package com.launch.listener;

import com.ifoer.entity.SptVwDataStreamIdItem;
import java.util.ArrayList;

public interface OnVWDataStreamListener {
    void onHandleData(ArrayList<SptVwDataStreamIdItem> arrayList);
}

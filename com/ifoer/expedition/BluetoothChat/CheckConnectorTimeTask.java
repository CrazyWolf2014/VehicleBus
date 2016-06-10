package com.ifoer.expedition.BluetoothChat;

import com.cnlaunch.x431pro.common.Constants;
import com.example.gpiomanager.MainActivity;
import com.ifoer.entity.Constant;
import com.ifoer.util.MySharedPreferences;
import java.util.TimerTask;

public class CheckConnectorTimeTask extends TimerTask {
    public void run() {
        if (!Constant.isDemo && !Constant.isReset && !Constant.isLANDROVER_DEMO) {
            if (MySharedPreferences.getBooleanValue(BluetoothChatService.context, Constants.isSanMu, false)) {
                if (MainActivity.SanmuGetV(BluetoothChatService.context) != 1 && BluetoothChatService.mHandler != null) {
                    BluetoothChatService.mHandler.sendEmptyMessage(10101010);
                }
            } else if (BluetoothChatService.HardFlag && MainActivity.ODB_ISOK(BluetoothChatService.HardFlag) != 1 && BluetoothChatService.mHandler != null) {
                BluetoothChatService.mHandler.sendEmptyMessage(10101010);
            }
        }
    }
}

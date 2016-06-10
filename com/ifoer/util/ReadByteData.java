package com.ifoer.util;

import com.ifoer.expedition.BluetoothChat.BluetoothChatService;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class ReadByteData implements Runnable {
    private boolean f1312D;
    private String TAG;
    private Bridge bridge;
    private byte[] buffer;
    private OutputStream outputStream;

    class MyTimerTasks extends TimerTask {
        Bridge bridge;

        public MyTimerTasks(Bridge bridge) {
            this.bridge = bridge;
        }

        public void run() {
            BluetoothChatService.readEnd = true;
            BluetoothChatService.totalLength = 7;
            BluetoothChatService.length = 0;
            BluetoothChatService.flag = 0;
            ReadByteData.this.buffer = new byte[Flags.FLAG5];
            this.bridge.putData();
        }
    }

    public ReadByteData(Bridge bridge) {
        this.f1312D = false;
        this.TAG = "WriteByteData";
        this.bridge = bridge;
    }

    public void run() {
        String reads = XmlPullParser.NO_NAMESPACE;
        try {
            this.outputStream = BluetoothChatService.remoteDevice.getOutputStream();
            BluetoothChatService.readData = XmlPullParser.NO_NAMESPACE;
            BluetoothChatService.bridge = this.bridge;
            if (BluetoothChatService.timer != null) {
                BluetoothChatService.timer.cancel();
            }
            BluetoothChatService.timer = new Timer();
            BluetoothChatService.timer.schedule(new MyTimerTasks(this.bridge), BluetoothChatService.DELAY);
            this.bridge.getData();
        } catch (Exception e) {
        }
    }
}

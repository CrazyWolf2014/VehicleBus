package com.ifoer.util;

import com.ifoer.expedition.BluetoothChat.BluetoothChatService;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class WriteByteData implements Runnable {
    private boolean f1314D;
    private String TAG;
    private Bridge bridge;
    private byte[] buffer;
    private int count;
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
            WriteByteData.this.buffer = new byte[Flags.FLAG5];
            this.bridge.putData();
        }
    }

    public WriteByteData(Bridge bridge, byte[] buffer) {
        this.f1314D = true;
        this.TAG = "WriteByteData";
        this.count = 0;
        this.bridge = bridge;
        this.buffer = buffer;
    }

    public void run() {
        String reads = XmlPullParser.NO_NAMESPACE;
        try {
            this.outputStream = BluetoothChatService.remoteDevice.getOutputStream();
            BluetoothChatService.send = null;
            BluetoothChatService.send = this.buffer;
            BluetoothChatService.readData = XmlPullParser.NO_NAMESPACE;
            BluetoothChatService.bridge = this.bridge;
            this.outputStream.write(this.buffer);
            if (BluetoothChatService.timer != null) {
                try {
                    BluetoothChatService.timer.cancel();
                    BluetoothChatService.timer = new Timer();
                    this.count++;
                    BluetoothChatService.timer.schedule(new MyTimerTasks(this.bridge), BluetoothChatService.DELAY);
                } catch (Exception e) {
                }
            }
            this.bridge.getData();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}

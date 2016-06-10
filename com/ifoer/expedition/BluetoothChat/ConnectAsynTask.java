package com.ifoer.expedition.BluetoothChat;

import android.os.AsyncTask;
import com.ifoer.serialport.SerialPort;

/* compiled from: BluetoothChatService */
class ConnectAsynTask extends AsyncTask {
    private boolean isLoadLib;
    private final BluetoothChatService mBluetoothChatService;
    private boolean mConnectState;
    private final SerialPort mDevice;

    public ConnectAsynTask(SerialPort device, BluetoothChatService bluetoothChatService, boolean isLoadLib) {
        this.mConnectState = true;
        this.isLoadLib = false;
        this.mDevice = device;
        this.mBluetoothChatService = bluetoothChatService;
        this.isLoadLib = isLoadLib;
    }

    protected Object doInBackground(Object... params) {
        return null;
    }

    protected void onPostExecute(Object result) {
        BluetoothChatService.remoteDevice = this.mDevice;
        if (this.mConnectState) {
            this.mBluetoothChatService.connected(this.mDevice, this.isLoadLib);
        } else {
            if (this.mBluetoothChatService.mIsFirtConnect) {
                this.mBluetoothChatService.firstConnectionFailed();
            } else {
                this.mBluetoothChatService.connectionFailed();
            }
            try {
                this.mDevice.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            this.mBluetoothChatService.start();
        }
        super.onPostExecute(result);
    }
}

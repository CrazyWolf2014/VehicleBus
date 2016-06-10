package com.ifoer.expedition.BluetoothChat;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BluetoothConnectActivityReceiver extends BroadcastReceiver {
    String strPsw;

    public BluetoothConnectActivityReceiver() {
        this.strPsw = "0000";
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
            BluetoothDevice btDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            try {
                PairingUtils.setPin(btDevice.getClass(), btDevice, this.strPsw);
                PairingUtils.createBond(btDevice.getClass(), btDevice);
                PairingUtils.cancelPairingUserInput(btDevice.getClass(), btDevice);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

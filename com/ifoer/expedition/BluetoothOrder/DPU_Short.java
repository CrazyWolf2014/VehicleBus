package com.ifoer.expedition.BluetoothOrder;

import org.xbill.DNS.KEYRecord;

public class DPU_Short {
    public static final int TYPE_LEN = 2;
    short value;

    public DPU_Short(short i) {
        this.value = i;
    }

    public byte[] toBytes() {
        byte[] ret = new byte[TYPE_LEN];
        ret[0] = (byte) (this.value >> 8);
        ret[1] = (byte) this.value;
        return ret;
    }

    public int getLength() {
        return TYPE_LEN;
    }

    public static short bytesToDPUShort(byte[] data) {
        return (short) (((data[0] & KEYRecord.PROTOCOL_ANY) << 8) | (data[1] & KEYRecord.PROTOCOL_ANY));
    }

    public static void main(String[] args) {
        System.out.println(bytesToDPUShort(new DPU_Short((short) 123).toBytes()));
    }
}

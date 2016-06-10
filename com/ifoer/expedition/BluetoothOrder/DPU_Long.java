package com.ifoer.expedition.BluetoothOrder;

import java.util.Formatter;
import org.xbill.DNS.KEYRecord;

public class DPU_Long {
    public static final int TYPE_LEN = 4;
    long value;

    public DPU_Long(long value) {
        this.value = value;
    }

    public byte[] toBytes() {
        byte[] ret = new byte[TYPE_LEN];
        ret[0] = (byte) ((int) (this.value >> 24));
        ret[1] = (byte) ((int) (this.value >> 16));
        ret[2] = (byte) ((int) (this.value >> 8));
        ret[3] = (byte) ((int) this.value);
        return ret;
    }

    public int getLength() {
        return TYPE_LEN;
    }

    public static long bytesToDPULong(byte[] data) {
        return (long) (((((data[0] & KEYRecord.PROTOCOL_ANY) << 24) | ((data[1] & KEYRecord.PROTOCOL_ANY) << 16)) | ((data[2] & KEYRecord.PROTOCOL_ANY) << 8)) | (data[3] & KEYRecord.PROTOCOL_ANY));
    }

    public static String bytesToHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(new Formatter().format("%02x-", new Object[]{Byte.valueOf(b)}));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        DPU_Long lon = new DPU_Long(123456);
        System.out.println(bytesToHex(lon.toBytes()));
        System.out.println(bytesToDPULong(lon.toBytes()));
    }
}

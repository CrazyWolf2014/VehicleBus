package com.ifoer.expedition.BluetoothOrder;

import java.io.File;
import org.xbill.DNS.KEYRecord;

public class DpuOrderUtils {
    public static byte[] filterReturnDataPackage(byte[] data) {
        if (data.length > 0 && isValidPackageHeader(data)) {
            int total_len = (((data[4] & KEYRecord.PROTOCOL_ANY) << 8) | (data[5] & KEYRecord.PROTOCOL_ANY)) + 7;
            if (total_len < data.length) {
                byte[] Package = new byte[total_len];
                for (int i = 0; i < total_len; i++) {
                    Package[i] = data[i];
                }
                return Package;
            }
        }
        return data;
    }

    public static byte[] filterOutCmdParameters(byte[] data) {
        byte[] pkg = filterReturnDataPackage(data);
        int param_total_len = ((pkg.length - 2) - 0) + 1;
        byte[] parameters = new byte[param_total_len];
        int index = 0;
        int i = 0;
        while (i < param_total_len) {
            int i2 = i + 1;
            int index2 = index + 1;
            parameters[i] = pkg[index];
            index = index2;
            i = i2;
        }
        return parameters;
    }

    public static int filterOutPackageLen(byte[] data) {
        if (!isValidPackageHeader(data)) {
            return 0;
        }
        return ((data[4] & KEYRecord.PROTOCOL_ANY) << 8) | (data[5] & KEYRecord.PROTOCOL_ANY);
    }

    public static boolean isValidPackageHeader(byte[] data) {
        if (data[0] == 85 && data[1] == -86) {
            return true;
        }
        return false;
    }

    public static byte[] fileNameAndLength(String name, File file) {
        if (name == null || file == null) {
            throw new NullPointerException("file name and file obj should not be null!");
        }
        return ByteHexHelper.appendByteArray(new DPU_String(name.toUpperCase()).toBytes(), new DPU_Long(file.length()).toBytes());
    }

    public static byte[] dataChunkParams(int writePos, byte[] dataChunk, int dataLen) {
        return ByteHexHelper.appendByteArray(ByteHexHelper.appendByteArray(new DPU_Long((long) writePos).toBytes(), new DPU_Short((short) dataLen).toBytes()), dataChunk);
    }
}

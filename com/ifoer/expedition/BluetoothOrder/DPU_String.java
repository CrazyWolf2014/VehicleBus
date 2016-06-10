package com.ifoer.expedition.BluetoothOrder;

import java.util.ArrayList;
import java.util.Formatter;

public class DPU_String {
    public static int BASE_POS;
    public static int MD5_LENGTH;
    public static int PARA_LENGTH_BYTE_COUNT;
    public static String TAG;
    int len;
    String str;
    int str_len;

    static {
        TAG = "DPU_String";
        BASE_POS = 9;
        PARA_LENGTH_BYTE_COUNT = 2;
        MD5_LENGTH = 32;
    }

    public DPU_String(String str) {
        this.str = str;
        this.str_len = str.length() + 1;
        this.len = str.length() + 3;
    }

    public byte[] toBytes() {
        return append(append(new byte[]{(byte) (this.str_len >> 8), (byte) this.str_len}, this.str.getBytes()), "\u0000".getBytes());
    }

    public static String asString(byte[] data) {
        if (data == null || data.length < 3) {
            return null;
        }
        int len = (data[0] << 8) | data[1];
        byte[] ret = new byte[(len - 1)];
        System.arraycopy(data, 2, ret, 0, len - 1);
        return new String(ret);
    }

    public static ArrayList<String> toStringArray(byte[] data) {
        if (data != null) {
            int total_bytes = data.length;
            if (total_bytes >= 3) {
                int walkthrough = 0;
                ArrayList<String> result_strings = new ArrayList();
                while (walkthrough < total_bytes - 1) {
                    int temp_len = (data[walkthrough] << 8) | data[walkthrough + 1];
                    byte[] str_bytes = new byte[(temp_len - 1)];
                    System.arraycopy(data, walkthrough + 2, str_bytes, 0, temp_len - 1);
                    result_strings.add(new String(str_bytes));
                    walkthrough += temp_len + 2;
                }
                return result_strings;
            }
        }
        return null;
    }

    public int getLength() {
        return this.len;
    }

    public static byte[] append(byte[] src, byte[] data) {
        if (src.length <= 0 || data.length <= 0) {
            throw new IllegalArgumentException("byte arguments error");
        }
        byte[] ret = new byte[(src.length + data.length)];
        System.arraycopy(src, 0, ret, 0, src.length);
        System.arraycopy(data, 0, ret, src.length, data.length);
        return ret;
    }

    public static String bytesToHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(new Formatter().format("%02x-", new Object[]{Byte.valueOf(b)}));
        }
        return sb.toString();
    }

    public String toString() {
        return "DPU_String [len=" + this.len + ", str=" + this.str + "]";
    }

    public static void main(String[] args) {
        int i;
        System.out.println("-------------------------- test DPUbytesToString()---------------------");
        System.out.println(asString(new DPU_String("Hello").toBytes()));
        System.out.println("=====================test DPUStringArrayToStringArray(byte[] data)==========================");
        DPU_String[] da = new DPU_String[]{new DPU_String("this"), new DPU_String("may"), new DPU_String("fun"), new DPU_String("Gooo!")};
        byte[] dpu_da = da[0].toBytes();
        for (i = 1; i < da.length; i++) {
            dpu_da = append(dpu_da, da[i].toBytes());
        }
        System.out.println(bytesToHex(dpu_da));
        ArrayList<String> sa = toStringArray(dpu_da);
        System.out.println(sa.size());
        for (i = 0; i < sa.size(); i++) {
            System.out.println(":" + ((String) sa.get(i)));
        }
    }
}

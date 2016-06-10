package com.ifoer.expedition.ndk;

public class VINDynamicDepot {
    public native String AutoSearchVehByVIN(String str, int i);

    public native int OBDReadVIN();

    public native int VINDiagnoseMain();
}

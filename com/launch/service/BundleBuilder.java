package com.launch.service;

import android.os.Bundle;

public class BundleBuilder {
    public static final String AskFrom = "AskFrom";
    public static final String AskFromConsumer = "consumer";
    public static final String AskFromMaster = "master";
    public static final String AskFromMessage = "message";
    public static final String IdentityType = "IdentityType";
    public static final String SNKey = "SNKey";

    public static Bundle createRemoteDiagBundle(String identityType, String SNKey, String AskFrom) {
        Bundle remoteDiagBundle = new Bundle();
        remoteDiagBundle.putString(IdentityType, identityType);
        remoteDiagBundle.putString(SNKey, SNKey);
        remoteDiagBundle.putString(AskFrom, AskFrom);
        return remoteDiagBundle;
    }
}

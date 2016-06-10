package com.paypal.android.MEP;

import java.util.Vector;

public interface PaymentAdjuster {
    MEPAmounts adjustAmount(MEPAddress mEPAddress, String str, String str2, String str3, String str4);

    Vector<MEPReceiverAmounts> adjustAmountsAdvanced(MEPAddress mEPAddress, String str, Vector<MEPReceiverAmounts> vector);
}

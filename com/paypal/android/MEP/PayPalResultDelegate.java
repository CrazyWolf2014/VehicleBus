package com.paypal.android.MEP;

public interface PayPalResultDelegate {
    void onPaymentCanceled(String str);

    void onPaymentFailed(String str, String str2, String str3, String str4, String str5);

    void onPaymentSucceeded(String str, String str2);
}

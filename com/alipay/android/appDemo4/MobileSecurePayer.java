package com.alipay.android.appDemo4;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import com.alipay.android.app.IAlixPay;
import com.alipay.android.app.IAlixPay.Stub;
import com.alipay.android.app.IRemoteServiceCallback;

public class MobileSecurePayer {
    static String TAG;
    Integer lock;
    Activity mActivity;
    IAlixPay mAlixPay;
    private ServiceConnection mAlixPayConnection;
    private IRemoteServiceCallback mCallback;
    boolean mbPaying;

    /* renamed from: com.alipay.android.appDemo4.MobileSecurePayer.1 */
    class C00821 implements ServiceConnection {
        C00821() {
        }

        public void onServiceConnected(ComponentName className, IBinder service) {
            synchronized (MobileSecurePayer.this.lock) {
                MobileSecurePayer.this.mAlixPay = Stub.asInterface(service);
                MobileSecurePayer.this.lock.notify();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            MobileSecurePayer.this.mAlixPay = null;
        }
    }

    /* renamed from: com.alipay.android.appDemo4.MobileSecurePayer.3 */
    class C00833 implements Runnable {
        private final /* synthetic */ Handler val$callback;
        private final /* synthetic */ int val$myWhat;
        private final /* synthetic */ String val$strOrderInfo;

        C00833(String str, int i, Handler handler) {
            this.val$strOrderInfo = str;
            this.val$myWhat = i;
            this.val$callback = handler;
        }

        public void run() {
            Message msg;
            try {
                synchronized (MobileSecurePayer.this.lock) {
                    if (MobileSecurePayer.this.mAlixPay == null) {
                        MobileSecurePayer.this.lock.wait();
                    }
                }
                MobileSecurePayer.this.mAlixPay.registerCallback(MobileSecurePayer.this.mCallback);
                String strRet = MobileSecurePayer.this.mAlixPay.Pay(this.val$strOrderInfo);
                BaseHelper.log(MobileSecurePayer.TAG, "After Pay: " + strRet);
                MobileSecurePayer.this.mbPaying = false;
                MobileSecurePayer.this.mAlixPay.unregisterCallback(MobileSecurePayer.this.mCallback);
                MobileSecurePayer.this.mActivity.getApplicationContext().unbindService(MobileSecurePayer.this.mAlixPayConnection);
                msg = new Message();
                msg.what = this.val$myWhat;
                msg.obj = strRet;
                this.val$callback.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
                msg = new Message();
                msg.what = this.val$myWhat;
                msg.obj = e.toString();
                this.val$callback.sendMessage(msg);
            }
        }
    }

    /* renamed from: com.alipay.android.appDemo4.MobileSecurePayer.2 */
    class C12252 extends IRemoteServiceCallback.Stub {
        C12252() {
        }

        public void startActivity(String packageName, String className, int iCallingPid, Bundle bundle) throws RemoteException {
            Intent intent = new Intent("android.intent.action.MAIN", null);
            if (bundle == null) {
                bundle = new Bundle();
            }
            try {
                bundle.putInt("CallingPid", iCallingPid);
                intent.putExtras(bundle);
            } catch (Exception e) {
                e.printStackTrace();
            }
            intent.setClassName(packageName, className);
            MobileSecurePayer.this.mActivity.startActivity(intent);
        }

        public boolean isHideLoadingScreen() throws RemoteException {
            return false;
        }

        public void payEnd(boolean arg0, String arg1) throws RemoteException {
        }
    }

    public MobileSecurePayer() {
        this.lock = Integer.valueOf(0);
        this.mAlixPay = null;
        this.mbPaying = false;
        this.mActivity = null;
        this.mAlixPayConnection = new C00821();
        this.mCallback = new C12252();
    }

    static {
        TAG = "MobileSecurePayer";
    }

    public boolean pay(String strOrderInfo, Handler callback, int myWhat, Activity activity) {
        if (this.mbPaying) {
            return false;
        }
        this.mbPaying = true;
        this.mActivity = activity;
        if (this.mAlixPay == null) {
            this.mActivity.getApplicationContext().bindService(new Intent(IAlixPay.class.getName()), this.mAlixPayConnection, 1);
        }
        new Thread(new C00833(strOrderInfo, myWhat, callback)).start();
        return true;
    }
}

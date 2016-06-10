package com.alipay.android.app;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IRemoteServiceCallback extends IInterface {

    public static abstract class Stub extends Binder implements IRemoteServiceCallback {
        private static final String DESCRIPTOR = "com.alipay.android.app.IRemoteServiceCallback";
        static final int TRANSACTION_isHideLoadingScreen = 3;
        static final int TRANSACTION_payEnd = 2;
        static final int TRANSACTION_startActivity = 1;

        private static class Proxy implements IRemoteServiceCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void startActivity(String packageName, String className, int iCallingPid, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(className);
                    _data.writeInt(iCallingPid);
                    if (bundle != null) {
                        _data.writeInt(Stub.TRANSACTION_startActivity);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_startActivity, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void payEnd(boolean isPayOk, String resultStatus) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (isPayOk) {
                        i = Stub.TRANSACTION_startActivity;
                    }
                    _data.writeInt(i);
                    _data.writeString(resultStatus);
                    this.mRemote.transact(Stub.TRANSACTION_payEnd, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isHideLoadingScreen() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isHideLoadingScreen, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRemoteServiceCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRemoteServiceCallback)) {
                return new Proxy(obj);
            }
            return (IRemoteServiceCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _arg0 = 0;
            switch (code) {
                case TRANSACTION_startActivity /*1*/:
                    Bundle _arg3;
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    String _arg1 = data.readString();
                    int _arg2 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    startActivity(_arg02, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_payEnd /*2*/:
                    boolean _arg03;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = true;
                    }
                    payEnd(_arg03, data.readString());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_isHideLoadingScreen /*3*/:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result = isHideLoadingScreen();
                    reply.writeNoException();
                    if (_result) {
                        _arg0 = TRANSACTION_startActivity;
                    }
                    reply.writeInt(_arg0);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    boolean isHideLoadingScreen() throws RemoteException;

    void payEnd(boolean z, String str) throws RemoteException;

    void startActivity(String str, String str2, int i, Bundle bundle) throws RemoteException;
}

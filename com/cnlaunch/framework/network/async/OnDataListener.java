package com.cnlaunch.framework.network.async;

import com.cnlaunch.framework.network.http.HttpException;

public interface OnDataListener {
    Object doInBackground(int i) throws HttpException;

    void onFailure(int i, int i2, Object obj);

    void onSuccess(int i, Object obj);
}

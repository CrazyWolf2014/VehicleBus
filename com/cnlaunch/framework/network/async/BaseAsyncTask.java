package com.cnlaunch.framework.network.async;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import com.cnlaunch.framework.network.http.HttpException;

public class BaseAsyncTask extends AsyncTask<Object, Integer, Object> {
    private DownLoad bean;
    private Context mContext;
    private final String tag;

    public BaseAsyncTask(DownLoad bean, Context context) {
        this.tag = BaseAsyncTask.class.getSimpleName();
        this.bean = null;
        this.bean = bean;
        this.mContext = context;
    }

    public boolean isNetworkConnected(Context context, boolean isCheckNetwork) {
        if (!isCheckNetwork) {
            return true;
        }
        NetworkInfo ni = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (ni == null || !ni.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }

    protected Object doInBackground(Object... params) {
        try {
            if (this.bean.getListener() == null) {
                throw new HttpException("BaseAsyncTask listener is not null.");
            }
            if (isNetworkConnected(this.mContext, this.bean.isCheckNetwork)) {
                Object result = this.bean.getListener().doInBackground(this.bean.getRequestCode());
                this.bean.setState(AsyncTaskManager.REQUEST_SUCCESS_CODE);
                this.bean.setResult(result);
            } else {
                this.bean.setState(AsyncTaskManager.HTTP_NULL_CODE);
            }
            return this.bean;
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof HttpException) {
                this.bean.setState(AsyncTaskManager.HTTP_ERROR_CODE);
            } else if (String.valueOf(AsyncTaskManager.JSONMAPPING_ERROR_CODE).equals(e.getMessage())) {
                this.bean.setState(AsyncTaskManager.JSONMAPPING_ERROR_CODE);
            } else {
                this.bean.setState(AsyncTaskManager.REQUEST_ERROR_CODE);
            }
            this.bean.setResult(e);
            return this.bean;
        }
    }

    protected void onPostExecute(Object result) {
        DownLoad bean = (DownLoad) result;
        switch (bean.getState()) {
            case AsyncTaskManager.REQUEST_ERROR_CODE /*-999*/:
            case AsyncTaskManager.HTTP_NULL_CODE /*-400*/:
            case AsyncTaskManager.HTTP_ERROR_CODE /*-200*/:
                bean.getListener().onFailure(bean.getRequestCode(), bean.getState(), bean.getResult());
            case AsyncTaskManager.REQUEST_SUCCESS_CODE /*200*/:
                bean.getListener().onSuccess(bean.getRequestCode(), bean.getResult());
            default:
                bean.getListener().onFailure(bean.getRequestCode(), bean.getState(), bean.getResult());
        }
    }
}

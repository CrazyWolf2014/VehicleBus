package com.cnlaunch.framework.utils.location;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import com.amap.mapapi.location.LocationManagerProxy;
import com.cnlaunch.framework.common.parse.JsonMananger;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.cnlaunch.framework.network.async.OnDataListener;
import com.cnlaunch.framework.network.http.AsyncHttpClient;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.framework.network.http.HttpException;
import com.cnlaunch.framework.network.http.RequestParams;
import com.cnlaunch.framework.utils.NLog;
import com.cnlaunch.framework.utils.lang.LangManager;
import com.ifoer.entity.Constant;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocationManager implements LocationListener, OnDataListener {
    private static MyLocationManager instance;
    private final int REQ_GEOCODER_CODE;
    private AsyncHttpClient asyncHttpClient;
    private AsyncTaskManager asyncTaskManager;
    private double latitude;
    private MyLocationListener locationListener;
    private LocationManager locationMgr;
    private double longitude;
    private Context mContext;
    private final String tag;

    /* renamed from: com.cnlaunch.framework.utils.location.MyLocationManager.1 */
    class C12691 extends AsyncHttpResponseHandler {
        C12691() {
        }

        @Deprecated
        public void onSuccess(String result) {
            try {
                if (MyLocationManager.this.locationListener == null) {
                    NLog.m917e(MyLocationManager.this.tag, "LocateManager MyLocationListener is not null.");
                } else if (!TextUtils.isEmpty(result)) {
                    LocationResponse res = (LocationResponse) JsonMananger.getInstance().jsonToBean(result, LocationResponse.class);
                    if (res != null && res.getStatus().equalsIgnoreCase("ok")) {
                        List<LocationResults> list = res.getResults();
                        if (list != null && list.size() > 0) {
                            String address = ((LocationResults) list.get(0)).getFormatted_address();
                            NLog.m917e(MyLocationManager.this.tag, "network latitude: " + MyLocationManager.this.latitude + " longitude: " + MyLocationManager.this.longitude + " address: " + address);
                            MyLocationManager.this.locationListener.onLocation(MyLocationManager.this.latitude, MyLocationManager.this.longitude, address);
                        }
                    }
                }
            } catch (HttpException e) {
                e.printStackTrace();
            }
        }

        @Deprecated
        public void onFailure(Throwable error, String content) {
            MyLocationManager.this.asyncTaskManager.request(10021, MyLocationManager.this);
        }
    }

    public static MyLocationManager getInstance(Context context) {
        if (instance == null) {
            synchronized (MyLocationManager.class) {
                if (instance == null) {
                    instance = new MyLocationManager(context);
                }
            }
        }
        return instance;
    }

    private MyLocationManager(Context context) {
        this.tag = MyLocationManager.class.getSimpleName();
        this.REQ_GEOCODER_CODE = 10021;
        this.mContext = context;
        this.asyncHttpClient = AsyncHttpClient.getInstance(context);
        this.asyncTaskManager = AsyncTaskManager.getInstance(this.mContext);
        this.locationMgr = (LocationManager) context.getSystemService(LocationManagerProxy.KEY_LOCATION_CHANGED);
    }

    public boolean isGPSEnabled() {
        return this.locationMgr.isProviderEnabled(LocationManagerProxy.GPS_PROVIDER);
    }

    public void settingGPS(Activity activity) {
        if (!isGPSEnabled()) {
            activity.startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), -1);
        }
    }

    public void startLocation() {
        startLocation(LocationManagerProxy.NETWORK_PROVIDER);
    }

    public void startLocation(String provider) {
        sendLocation(this.locationMgr.getLastKnownLocation(provider));
        this.locationMgr.requestLocationUpdates(provider, 1000, 0.0f, this);
    }

    public void stopLocation() {
        if (this.locationMgr != null) {
            this.locationMgr.removeUpdates(this);
        }
    }

    public void sendLocation(Location location) {
        if (location != null) {
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
            RequestParams params = new RequestParams();
            params.put("latlng", this.latitude + "," + this.longitude);
            params.put("sensor", "true");
            params.put("language", LangManager.getLanguage());
            this.asyncHttpClient.get(Constant.GOOGLE_GEOCODE_PATH, params, new C12691());
        }
    }

    public void onLocationChanged(Location location) {
        sendLocation(location);
    }

    public void onProviderDisabled(String arg0) {
    }

    public void onProviderEnabled(String arg0) {
    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }

    public MyLocationListener getLocationListener() {
        return this.locationListener;
    }

    public void setLocationListener(MyLocationListener locationListener) {
        this.locationListener = locationListener;
    }

    public Object doInBackground(int requsetCode) throws HttpException {
        try {
            List<Address> list = new Geocoder(this.mContext, Locale.getDefault()).getFromLocation(this.latitude, this.longitude, 1);
            StringBuilder addressBuilder = new StringBuilder();
            for (Address address : list) {
                addressBuilder.append(address.getCountryName());
                addressBuilder.append(address.getLocality());
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressBuilder.append(address.getAddressLine(i));
                }
            }
            return addressBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void onSuccess(int requestCode, Object result) {
        if (result == null) {
            return;
        }
        if (this.locationListener != null) {
            NLog.m917e(this.tag, "Geocoder latitude: " + this.latitude + " longitude: " + this.longitude + " address: " + result);
            this.locationListener.onLocation(this.latitude, this.longitude, String.valueOf(result));
            return;
        }
        NLog.m917e(this.tag, "LocateManager MyLocationListener is not null.");
    }

    public void onFailure(int requestCode, int state, Object result) {
    }
}

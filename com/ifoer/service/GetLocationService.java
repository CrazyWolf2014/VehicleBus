package com.ifoer.service;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.geocoder.Geocoder;
import com.amap.mapapi.location.LocationManagerProxy;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.ifoer.entity.Constant;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

public class GetLocationService implements LocationListener {
    private static String LocationName;
    private static Context context;
    private LocationManagerProxy locationManager;

    static {
        LocationName = null;
    }

    public GetLocationService(Context context) {
        context = context;
    }

    public void onDestroy() {
        if (this.locationManager != null) {
            this.locationManager.removeUpdates((LocationListener) this);
            this.locationManager.destory();
        }
        this.locationManager = null;
    }

    public void onStart() {
        System.out.println("onStart");
        enableMyLocation();
    }

    public boolean enableMyLocation() {
        if (this.locationManager != null) {
            this.locationManager.removeUpdates((LocationListener) this);
        }
        System.out.println("enableMyLocation");
        this.locationManager = LocationManagerProxy.getInstance(context);
        Criteria cri = new Criteria();
        cri.setAccuracy(2);
        cri.setAltitudeRequired(false);
        cri.setBearingRequired(false);
        cri.setCostAllowed(false);
        String bestProvider = this.locationManager.getBestProvider(cri, true);
        if (bestProvider != null) {
            this.locationManager.requestLocationUpdates(bestProvider, 100, 1.0f, (LocationListener) this);
        }
        return true;
    }

    public void onLocationChanged(Location location) {
        System.out.println("onLocationChanged");
        if (location != null) {
            Double geoLat = Double.valueOf(location.getLatitude());
            Double geoLng = Double.valueOf(location.getLongitude());
            Constant.geoLat = geoLat.doubleValue();
            Constant.geoLng = geoLng.doubleValue();
        }
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public String GetLocationName(double lat, double lon) {
        LocationName = getLocation(lat, lon);
        return LocationName;
    }

    public static String getLocation(double mlat, double mlon) {
        GeoPoint geo = new GeoPoint((int) (1000000.0d * mlat), (int) (1000000.0d * mlon));
        Geocoder coder = new Geocoder((Activity) context);
        try {
            if (geo.toString() != XmlPullParser.NO_NAMESPACE) {
                List<Address> lstAddress = coder.getFromLocation(((double) geo.getLatitudeE6()) / 1000000.0d, ((double) geo.getLongitudeE6()) / 1000000.0d, 3);
                if (lstAddress.size() != 0) {
                    for (int i = 0; i < lstAddress.size(); i++) {
                        Address adsLocation = (Address) lstAddress.get(i);
                        Log.i("GetLocation", "i" + i + adsLocation.getCountryCode() + adsLocation.getCountryCode());
                        LocationName = adsLocation.getCountryName() + adsLocation.getAddressLine(1) + adsLocation.getFeatureName().toString();
                    }
                } else {
                    Log.i("GetLocation", "Address GeoPoint NOT Found.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LocationName;
    }

    public static String geocodeAddr(String latitude, String longitude) {
        String addr = XmlPullParser.NO_NAMESPACE;
        String url = String.format("http://maps.google.com/maps/api/geocode/json?latlng=%s,%s&sensor=true&language=%s", new Object[]{latitude, longitude, Constant.language});
        System.out.println(url);
        try {
            URL myURL = new URL(url);
            URL url2;
            try {
                URLConnection httpsConn = myURL.openConnection();
                if (httpsConn != null) {
                    InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream(), AsyncHttpResponseHandler.DEFAULT_CHARSET);
                    BufferedReader br = new BufferedReader(insr);
                    StringBuffer data = new StringBuffer();
                    while (true) {
                        String str = br.readLine();
                        if (str == null) {
                            break;
                        }
                        data.append(str);
                    }
                    if (data != null) {
                        if (data.toString().length() > 0) {
                            try {
                                JSONObject json = new JSONObject(data.toString());
                                if (json.getString(LocationManagerProxy.KEY_STATUS_CHANGED).equalsIgnoreCase("OK")) {
                                    String results = json.getString("results");
                                    if (results != null && results.toString().length() > 0 && results.subSequence(0, 1).equals("[")) {
                                        JSONArray jsonArray = new JSONArray(results);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject item = jsonArray.getJSONObject(i);
                                            if (item.getString("types").equalsIgnoreCase("[\"street_address\"]")) {
                                                addr = item.getString("formatted_address");
                                                break;
                                            }
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    insr.close();
                }
                url2 = myURL;
                return addr;
            } catch (IOException e2) {
                e2.printStackTrace();
                url2 = myURL;
                return XmlPullParser.NO_NAMESPACE;
            }
        } catch (MalformedURLException e3) {
            e3.printStackTrace();
            return null;
        }
    }
}

package com.amap.mapapi.busline;

import android.app.Activity;
import android.content.Context;
import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.ClientInfoUtil;
import com.amap.mapapi.core.CoreUtil;
import java.util.ArrayList;

public class BusSearch {
    private Context f264a;
    private BusQuery f265b;
    private int f266c;

    public BusSearch(Activity activity, BusQuery busQuery) {
        this.f266c = 10;
        ClientInfoUtil.m471a(activity);
        this.f264a = activity;
        this.f265b = busQuery;
    }

    public BusSearch(Context context, String str, BusQuery busQuery) {
        this.f266c = 10;
        ClientInfoUtil.m471a(context);
        this.f264a = context;
        this.f265b = busQuery;
    }

    public BusPagedResult searchBusLine() throws AMapException {
        BusSearchServerHandler busSearchServerHandler = new BusSearchServerHandler(this.f265b, CoreUtil.m491b(this.f264a), CoreUtil.m485a(this.f264a), null);
        busSearchServerHandler.m2600a(1);
        busSearchServerHandler.m2604b(this.f266c);
        return BusPagedResult.m430a(busSearchServerHandler, (ArrayList) busSearchServerHandler.m531j());
    }

    public void setPageSize(int i) {
        this.f266c = i;
    }

    public void setQuery(BusQuery busQuery) {
        this.f265b = busQuery;
    }

    public BusQuery getQuery() {
        return this.f265b;
    }
}

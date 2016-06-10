package com.amap.mapapi.map;

import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident.Type;
import com.google.protobuf.Internal.EnumLite;
import com.google.protobuf.Internal.EnumLiteMap;

/* compiled from: TrafficProtos */
class az implements EnumLiteMap<Type> {
    az() {
    }

    public /* synthetic */ EnumLite findValueByNumber(int i) {
        return m1961a(i);
    }

    public Type m1961a(int i) {
        return Type.valueOf(i);
    }
}

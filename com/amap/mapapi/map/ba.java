package com.amap.mapapi.map;

import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficSegment.TrafficSpeed;
import com.google.protobuf.Internal.EnumLite;
import com.google.protobuf.Internal.EnumLiteMap;

/* compiled from: TrafficProtos */
class ba implements EnumLiteMap<TrafficSpeed> {
    ba() {
    }

    public /* synthetic */ EnumLite findValueByNumber(int i) {
        return m1962a(i);
    }

    public TrafficSpeed m1962a(int i) {
        return TrafficSpeed.valueOf(i);
    }
}

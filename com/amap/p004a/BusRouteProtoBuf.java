package com.amap.p004a;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessage.FieldAccessorTable;

/* renamed from: com.amap.a.d */
class BusRouteProtoBuf implements InternalDescriptorAssigner {
    BusRouteProtoBuf() {
    }

    public ExtensionRegistry assignDescriptors(FileDescriptor fileDescriptor) {
        BusRouteProtoBuf.f162o = fileDescriptor;
        BusRouteProtoBuf.f148a = (Descriptor) BusRouteProtoBuf.m269a().getMessageTypes().get(0);
        BusRouteProtoBuf.f149b = new FieldAccessorTable(BusRouteProtoBuf.f148a, new String[]{"Common", "CityCode", "X1", "Y1", "X2", "Y2", "Per", "RouteType", "Ver"}, BusRouteProtoBuf.class, BusRouteProtoBuf.class);
        BusRouteProtoBuf.f150c = (Descriptor) BusRouteProtoBuf.m269a().getMessageTypes().get(1);
        BusRouteProtoBuf.f151d = new FieldAccessorTable(BusRouteProtoBuf.f150c, new String[]{"Count", "Searchtime", "Cache", "Busv1", "Busv2"}, BusRouteProtoBuf.class, BusRouteProtoBuf.class);
        BusRouteProtoBuf.f152e = (Descriptor) BusRouteProtoBuf.m269a().getMessageTypes().get(2);
        BusRouteProtoBuf.f153f = new FieldAccessorTable(BusRouteProtoBuf.f152e, new String[]{"BusItem"}, BusRouteProtoBuf.class, BusRouteProtoBuf.class);
        BusRouteProtoBuf.f154g = (Descriptor) BusRouteProtoBuf.m269a().getMessageTypes().get(3);
        BusRouteProtoBuf.f155h = new FieldAccessorTable(BusRouteProtoBuf.f154g, new String[]{"Text", "Coor", "Bounds"}, BusRouteProtoBuf.class, BusRouteProtoBuf.class);
        BusRouteProtoBuf.f156i = (Descriptor) BusRouteProtoBuf.m269a().getMessageTypes().get(4);
        BusRouteProtoBuf.f157j = new FieldAccessorTable(BusRouteProtoBuf.f156i, new String[]{"Bus"}, BusRouteProtoBuf.class, BusRouteProtoBuf.class);
        BusRouteProtoBuf.f158k = (Descriptor) BusRouteProtoBuf.m269a().getMessageTypes().get(5);
        BusRouteProtoBuf.f159l = new FieldAccessorTable(BusRouteProtoBuf.f158k, new String[]{"Segment", "FootEndLength", "Bounds", "Expense"}, BusRouteProtoBuf.class, BusRouteProtoBuf.class);
        BusRouteProtoBuf.f160m = (Descriptor) BusRouteProtoBuf.m269a().getMessageTypes().get(6);
        BusRouteProtoBuf.f161n = new FieldAccessorTable(BusRouteProtoBuf.f160m, new String[]{"StartName", "EndName", "BusName", "PassDepotName", "DriverLength", "FootLength", "PassDepotCount", "CoordinateList", "PassDepotCoordinate"}, BusRouteProtoBuf.class, BusRouteProtoBuf.class);
        return null;
    }
}

package com.amap.p004a;

import com.cnlaunch.x431pro.common.Constants;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessage.FieldAccessorTable;

/* renamed from: com.amap.a.n */
class RouteProtoBuf implements InternalDescriptorAssigner {
    RouteProtoBuf() {
    }

    public ExtensionRegistry assignDescriptors(FileDescriptor fileDescriptor) {
        RouteProtoBuf.f227q = fileDescriptor;
        RouteProtoBuf.f211a = (Descriptor) RouteProtoBuf.m395a().getMessageTypes().get(0);
        RouteProtoBuf.f212b = new FieldAccessorTable(RouteProtoBuf.f211a, new String[]{"Common", "X1", "Y1", "X2", "Y2", "Per", "Xs", "Ys", "RouteType", "AvoidanceType", "Region", Constants.VEHICLE_INI_SECTION_NAME, "Ext", "Ver"}, RouteProtoBuf.class, RouteProtoBuf.class);
        RouteProtoBuf.f213c = (Descriptor) RouteProtoBuf.m395a().getMessageTypes().get(1);
        RouteProtoBuf.f214d = new FieldAccessorTable(RouteProtoBuf.f213c, new String[]{"Count", "Bounds", "Searchtime", "Coors", "Routev1", "Routev2", "Length", "RouteCityList"}, RouteProtoBuf.class, RouteProtoBuf.class);
        RouteProtoBuf.f215e = (Descriptor) RouteProtoBuf.m395a().getMessageTypes().get(2);
        RouteProtoBuf.f216f = new FieldAccessorTable(RouteProtoBuf.f215e, new String[]{"RouteItem"}, RouteProtoBuf.class, RouteProtoBuf.class);
        RouteProtoBuf.f217g = (Descriptor) RouteProtoBuf.m395a().getMessageTypes().get(3);
        RouteProtoBuf.f218h = new FieldAccessorTable(RouteProtoBuf.f217g, new String[]{"Turn", "Road", "Dire", "Dist", "Coor"}, RouteProtoBuf.class, RouteProtoBuf.class);
        RouteProtoBuf.f219i = (Descriptor) RouteProtoBuf.m395a().getMessageTypes().get(4);
        RouteProtoBuf.f220j = new FieldAccessorTable(RouteProtoBuf.f219i, new String[]{"Route"}, RouteProtoBuf.class, RouteProtoBuf.class);
        RouteProtoBuf.f221k = (Descriptor) RouteProtoBuf.m395a().getMessageTypes().get(5);
        RouteProtoBuf.f222l = new FieldAccessorTable(RouteProtoBuf.f221k, new String[]{"RoadName", "Direction", "RoadLength", "Action", "AccessorialInfo", "DriveTime", "Grade", "Form", "TextInfo", "Coor", "SoundID", "VideoID"}, RouteProtoBuf.class, RouteProtoBuf.class);
        RouteProtoBuf.f223m = (Descriptor) RouteProtoBuf.m395a().getMessageTypes().get(6);
        RouteProtoBuf.f224n = new FieldAccessorTable(RouteProtoBuf.f223m, new String[]{"RouteCity"}, RouteProtoBuf.class, RouteProtoBuf.class);
        RouteProtoBuf.f225o = (Descriptor) RouteProtoBuf.m395a().getMessageTypes().get(7);
        RouteProtoBuf.f226p = new FieldAccessorTable(RouteProtoBuf.f225o, new String[]{Constants.VEHICLE_INI_SECTION_NAME, "Ename", "Code", "Telnum", "Citycode"}, RouteProtoBuf.class, RouteProtoBuf.class);
        return null;
    }
}

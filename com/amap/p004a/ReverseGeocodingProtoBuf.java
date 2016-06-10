package com.amap.p004a;

import com.amap.mapapi.geocoder.Geocoder;
import com.cnlaunch.x431pro.common.Constants;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessage.FieldAccessorTable;

/* renamed from: com.amap.a.l */
class ReverseGeocodingProtoBuf implements InternalDescriptorAssigner {
    ReverseGeocodingProtoBuf() {
    }

    public ExtensionRegistry assignDescriptors(FileDescriptor fileDescriptor) {
        ReverseGeocodingProtoBuf.f210u = fileDescriptor;
        ReverseGeocodingProtoBuf.f190a = (Descriptor) ReverseGeocodingProtoBuf.m353a().getMessageTypes().get(0);
        ReverseGeocodingProtoBuf.f191b = new FieldAccessorTable(ReverseGeocodingProtoBuf.f190a, new String[]{"Common", "SpatialXml"}, ReverseGeocodingProtoBuf.class, ReverseGeocodingProtoBuf.class);
        ReverseGeocodingProtoBuf.f192c = (Descriptor) ReverseGeocodingProtoBuf.m353a().getMessageTypes().get(1);
        ReverseGeocodingProtoBuf.f193d = new FieldAccessorTable(ReverseGeocodingProtoBuf.f192c, new String[]{"Spatial"}, ReverseGeocodingProtoBuf.class, ReverseGeocodingProtoBuf.class);
        ReverseGeocodingProtoBuf.f194e = (Descriptor) ReverseGeocodingProtoBuf.m353a().getMessageTypes().get(2);
        ReverseGeocodingProtoBuf.f195f = new FieldAccessorTable(ReverseGeocodingProtoBuf.f194e, new String[]{"Province", "City", "District", "Roads", "Pois", Geocoder.Cross}, ReverseGeocodingProtoBuf.class, ReverseGeocodingProtoBuf.class);
        ReverseGeocodingProtoBuf.f196g = (Descriptor) ReverseGeocodingProtoBuf.m353a().getMessageTypes().get(3);
        ReverseGeocodingProtoBuf.f197h = new FieldAccessorTable(ReverseGeocodingProtoBuf.f196g, new String[]{Constants.VEHICLE_INI_SECTION_NAME, "Code"}, ReverseGeocodingProtoBuf.class, ReverseGeocodingProtoBuf.class);
        ReverseGeocodingProtoBuf.f198i = (Descriptor) ReverseGeocodingProtoBuf.m353a().getMessageTypes().get(4);
        ReverseGeocodingProtoBuf.f199j = new FieldAccessorTable(ReverseGeocodingProtoBuf.f198i, new String[]{Constants.VEHICLE_INI_SECTION_NAME, "Code", "Tel"}, ReverseGeocodingProtoBuf.class, ReverseGeocodingProtoBuf.class);
        ReverseGeocodingProtoBuf.f200k = (Descriptor) ReverseGeocodingProtoBuf.m353a().getMessageTypes().get(5);
        ReverseGeocodingProtoBuf.f201l = new FieldAccessorTable(ReverseGeocodingProtoBuf.f200k, new String[]{Constants.VEHICLE_INI_SECTION_NAME, "Code", "X", "Y", "Bounds"}, ReverseGeocodingProtoBuf.class, ReverseGeocodingProtoBuf.class);
        ReverseGeocodingProtoBuf.f202m = (Descriptor) ReverseGeocodingProtoBuf.m353a().getMessageTypes().get(6);
        ReverseGeocodingProtoBuf.f203n = new FieldAccessorTable(ReverseGeocodingProtoBuf.f202m, new String[]{"Road"}, ReverseGeocodingProtoBuf.class, ReverseGeocodingProtoBuf.class);
        ReverseGeocodingProtoBuf.f204o = (Descriptor) ReverseGeocodingProtoBuf.m353a().getMessageTypes().get(7);
        ReverseGeocodingProtoBuf.f205p = new FieldAccessorTable(ReverseGeocodingProtoBuf.f204o, new String[]{"Id", Constants.VEHICLE_INI_SECTION_NAME, "Ename", "Width", "Level", "Distance", "Direction"}, ReverseGeocodingProtoBuf.class, ReverseGeocodingProtoBuf.class);
        ReverseGeocodingProtoBuf.f206q = (Descriptor) ReverseGeocodingProtoBuf.m353a().getMessageTypes().get(8);
        ReverseGeocodingProtoBuf.f207r = new FieldAccessorTable(ReverseGeocodingProtoBuf.f206q, new String[]{"Poi"}, ReverseGeocodingProtoBuf.class, ReverseGeocodingProtoBuf.class);
        ReverseGeocodingProtoBuf.f208s = (Descriptor) ReverseGeocodingProtoBuf.m353a().getMessageTypes().get(9);
        ReverseGeocodingProtoBuf.f209t = new FieldAccessorTable(ReverseGeocodingProtoBuf.f208s, new String[]{Constants.VEHICLE_INI_SECTION_NAME, "X", "Y"}, ReverseGeocodingProtoBuf.class, ReverseGeocodingProtoBuf.class);
        return null;
    }
}

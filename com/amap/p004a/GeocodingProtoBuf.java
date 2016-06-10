package com.amap.p004a;

import com.cnlaunch.x431pro.common.Constants;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessage.FieldAccessorTable;

/* renamed from: com.amap.a.h */
class GeocodingProtoBuf implements InternalDescriptorAssigner {
    GeocodingProtoBuf() {
    }

    public ExtensionRegistry assignDescriptors(FileDescriptor fileDescriptor) {
        GeocodingProtoBuf.f182m = fileDescriptor;
        GeocodingProtoBuf.f170a = (Descriptor) GeocodingProtoBuf.m313a().getMessageTypes().get(0);
        GeocodingProtoBuf.f171b = new FieldAccessorTable(GeocodingProtoBuf.f170a, new String[]{"Common", "Address", "PoiNumber", "Ver"}, GeocodingProtoBuf.class, GeocodingProtoBuf.class);
        GeocodingProtoBuf.f172c = (Descriptor) GeocodingProtoBuf.m313a().getMessageTypes().get(1);
        GeocodingProtoBuf.f173d = new FieldAccessorTable(GeocodingProtoBuf.f172c, new String[]{"Count", "Geocoding", "Spellcorrect"}, GeocodingProtoBuf.class, GeocodingProtoBuf.class);
        GeocodingProtoBuf.f174e = (Descriptor) GeocodingProtoBuf.m313a().getMessageTypes().get(2);
        GeocodingProtoBuf.f175f = new FieldAccessorTable(GeocodingProtoBuf.f174e, new String[]{"Poi"}, GeocodingProtoBuf.class, GeocodingProtoBuf.class);
        GeocodingProtoBuf.f176g = (Descriptor) GeocodingProtoBuf.m313a().getMessageTypes().get(3);
        GeocodingProtoBuf.f177h = new FieldAccessorTable(GeocodingProtoBuf.f176g, new String[]{Constants.VEHICLE_INI_SECTION_NAME, "Level", "X", "Y", "Address", "Province", "City", "Ename", "District", "Range", "Num", "Inum", "Prox", "Score", "Eprovince", "Ecity", "Edistrict", "Eaddress", "Roadpts", "Subpois"}, GeocodingProtoBuf.class, GeocodingProtoBuf.class);
        GeocodingProtoBuf.f178i = (Descriptor) GeocodingProtoBuf.m313a().getMessageTypes().get(4);
        GeocodingProtoBuf.f179j = new FieldAccessorTable(GeocodingProtoBuf.f178i, new String[]{"Subpoi"}, GeocodingProtoBuf.class, GeocodingProtoBuf.class);
        GeocodingProtoBuf.f180k = (Descriptor) GeocodingProtoBuf.m313a().getMessageTypes().get(5);
        GeocodingProtoBuf.f181l = new FieldAccessorTable(GeocodingProtoBuf.f180k, new String[]{Constants.VEHICLE_INI_SECTION_NAME, "Ename", "X", "Y"}, GeocodingProtoBuf.class, GeocodingProtoBuf.class);
        return null;
    }
}

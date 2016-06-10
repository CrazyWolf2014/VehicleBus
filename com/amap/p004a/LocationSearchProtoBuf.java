package com.amap.p004a;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessage.FieldAccessorTable;

/* renamed from: com.amap.a.j */
class LocationSearchProtoBuf implements InternalDescriptorAssigner {
    LocationSearchProtoBuf() {
    }

    public ExtensionRegistry assignDescriptors(FileDescriptor fileDescriptor) {
        LocationSearchProtoBuf.f189g = fileDescriptor;
        LocationSearchProtoBuf.f183a = (Descriptor) LocationSearchProtoBuf.m339a().getMessageTypes().get(0);
        LocationSearchProtoBuf.f184b = new FieldAccessorTable(LocationSearchProtoBuf.f183a, new String[]{"Common", "SearchName", "SrcType", "CityCode", "SearchType", "CenName", "CenX", "CenY", "Range", "Naviflag", "Sr", "Number", "Batch"}, LocationSearchProtoBuf.class, LocationSearchProtoBuf.class);
        LocationSearchProtoBuf.f185c = (Descriptor) LocationSearchProtoBuf.m339a().getMessageTypes().get(1);
        LocationSearchProtoBuf.f186d = new FieldAccessorTable(LocationSearchProtoBuf.f185c, new String[]{"Searchtime", "Cache", "Count", "Record", "Bounds", "Total", "Cenpoi", "SearchResult", "Spellcorrect"}, LocationSearchProtoBuf.class, LocationSearchProtoBuf.class);
        LocationSearchProtoBuf.f187e = (Descriptor) LocationSearchProtoBuf.m339a().getMessageTypes().get(2);
        LocationSearchProtoBuf.f188f = new FieldAccessorTable(LocationSearchProtoBuf.f187e, new String[]{"Poi"}, LocationSearchProtoBuf.class, LocationSearchProtoBuf.class);
        return null;
    }
}

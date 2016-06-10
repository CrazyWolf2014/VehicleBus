package com.amap.p004a;

import com.cnlaunch.x431pro.common.Constants;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessage.FieldAccessorTable;

/* renamed from: com.amap.a.b */
class BusLineProtoBuf implements InternalDescriptorAssigner {
    BusLineProtoBuf() {
    }

    public ExtensionRegistry assignDescriptors(FileDescriptor fileDescriptor) {
        BusLineProtoBuf.f147g = fileDescriptor;
        BusLineProtoBuf.f141a = (Descriptor) BusLineProtoBuf.m255a().getMessageTypes().get(0);
        BusLineProtoBuf.f142b = new FieldAccessorTable(BusLineProtoBuf.f141a, new String[]{"Common", "CityCode", "Ids", "BusName", "StationName", "Number", "Batch", "ResData"}, BusLineProtoBuf.class, BusLineProtoBuf.class);
        BusLineProtoBuf.f143c = (Descriptor) BusLineProtoBuf.m255a().getMessageTypes().get(1);
        BusLineProtoBuf.f144d = new FieldAccessorTable(BusLineProtoBuf.f143c, new String[]{"BusLine", "Count", "Searchtime", "Record", "Total", "Cache"}, BusLineProtoBuf.class, BusLineProtoBuf.class);
        BusLineProtoBuf.f145e = (Descriptor) BusLineProtoBuf.m255a().getMessageTypes().get(2);
        BusLineProtoBuf.f146f = new FieldAccessorTable(BusLineProtoBuf.f145e, new String[]{"Length", Constants.VEHICLE_INI_SECTION_NAME, "Type", "Status", "LineId", "KeyName", "FrontName", "TerminalName", "StartTime", "EndTime", "Company", "BasicPrice", "TotalPrice", "CommutationTicket", "AutoTicket", "IcCard", "Loop", "DoubleDeck", "DataSource", "Air", "Description", "Speed", "FrontSpell", "TerminalSpell", "ServicePeriod", "TimeInterval1", "Interval1", "TimeInterval2", "Interval2", "TimeInterval3", "Interval3", "TimeInterval4", "Interval4", "TimeInterval5", "Interval5", "TimeInterval6", "Interval6", "TimeInterval7", "Interval7", "TimeInterval8", "Interval8", "TimeDesc", "ExpressWay", "GpsfileId", "PhotoId", "PhotoFolder", "PaperTableId", "Stationdes", "Xys"}, BusLineProtoBuf.class, BusLineProtoBuf.class);
        return null;
    }
}

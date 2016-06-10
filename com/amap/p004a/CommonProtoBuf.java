package com.amap.p004a;

import com.cnlaunch.x431pro.common.Constants;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessage.FieldAccessorTable;

/* renamed from: com.amap.a.f */
class CommonProtoBuf implements InternalDescriptorAssigner {
    CommonProtoBuf() {
    }

    public ExtensionRegistry assignDescriptors(FileDescriptor fileDescriptor) {
        CommonProtoBuf.f169g = fileDescriptor;
        CommonProtoBuf.f163a = (Descriptor) CommonProtoBuf.m299a().getMessageTypes().get(0);
        CommonProtoBuf.f164b = new FieldAccessorTable(CommonProtoBuf.f163a, new String[]{"Config", "AK", "ResType", "Enc"}, CommonProtoBuf.class, CommonProtoBuf.class);
        CommonProtoBuf.f165c = (Descriptor) CommonProtoBuf.m299a().getMessageTypes().get(1);
        CommonProtoBuf.f166d = new FieldAccessorTable(CommonProtoBuf.f165c, new String[]{"Pguid", Constants.VEHICLE_INI_SECTION_NAME, "Type", "X", "Y", "Srctype", "Address", "Timestamp", "Match", "Code", "Newtype", "Citycode", "Typecode", "Gridcode", "Buscode", "Url", "Xml", "Imageid", "Tel", "Linkid", "Distance", "Drivedistance"}, CommonProtoBuf.class, CommonProtoBuf.class);
        CommonProtoBuf.f167e = (Descriptor) CommonProtoBuf.m299a().getMessageTypes().get(2);
        CommonProtoBuf.f168f = new FieldAccessorTable(CommonProtoBuf.f167e, new String[]{"Data"}, CommonProtoBuf.class, CommonProtoBuf.class);
        return null;
    }
}

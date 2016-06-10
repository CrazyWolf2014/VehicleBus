package com.amap.mapapi.map;

import com.amap.mapapi.map.TrafficProtos.TrafficTile;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.Builder;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficSegment;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessage.FieldAccessorTable;

/* compiled from: TrafficProtos */
class ay implements InternalDescriptorAssigner {
    ay() {
    }

    public ExtensionRegistry assignDescriptors(FileDescriptor fileDescriptor) {
        TrafficProtos.descriptor = fileDescriptor;
        TrafficProtos.internal_static_traffic_TrafficTile_descriptor = (Descriptor) TrafficProtos.getDescriptor().getMessageTypes().get(0);
        TrafficProtos.internal_static_traffic_TrafficTile_fieldAccessorTable = new FieldAccessorTable(TrafficProtos.internal_static_traffic_TrafficTile_descriptor, new String[]{"Vertices", "TrafficSegment", "TrafficIncident"}, TrafficTile.class, Builder.class);
        TrafficProtos.internal_static_traffic_TrafficTile_TrafficSegment_descriptor = (Descriptor) TrafficProtos.internal_static_traffic_TrafficTile_descriptor.getNestedTypes().get(0);
        TrafficProtos.f510xbb770691 = new FieldAccessorTable(TrafficProtos.internal_static_traffic_TrafficTile_TrafficSegment_descriptor, new String[]{"VertexOffset", "VertexCount", "Speed", "Width"}, TrafficSegment.class, TrafficSegment.Builder.class);
        TrafficProtos.internal_static_traffic_TrafficTile_TrafficIncident_descriptor = (Descriptor) TrafficProtos.internal_static_traffic_TrafficTile_descriptor.getNestedTypes().get(1);
        TrafficProtos.f509x10786b62 = new FieldAccessorTable(TrafficProtos.internal_static_traffic_TrafficTile_TrafficIncident_descriptor, new String[]{"UID", "Title", "Description", "Location", "VertexOffset", "IncidentVertex", "StartTime", "EndTime", "LastUpdated", "Type"}, TrafficIncident.class, TrafficIncident.Builder.class);
        return null;
    }
}

package org.codehaus.jackson.org.objectweb.asm;

import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;

final class Item {
    int f1699a;
    int f1700b;
    int f1701c;
    long f1702d;
    String f1703g;
    String f1704h;
    String f1705i;
    int f1706j;
    Item f1707k;

    Item() {
    }

    Item(int i) {
        this.f1699a = i;
    }

    Item(int i, Item item) {
        this.f1699a = i;
        this.f1700b = item.f1700b;
        this.f1701c = item.f1701c;
        this.f1702d = item.f1702d;
        this.f1703g = item.f1703g;
        this.f1704h = item.f1704h;
        this.f1705i = item.f1705i;
        this.f1706j = item.f1706j;
    }

    void m1740a(double d) {
        this.f1700b = 6;
        this.f1702d = Double.doubleToRawLongBits(d);
        this.f1706j = Integer.MAX_VALUE & (this.f1700b + ((int) d));
    }

    void m1741a(float f) {
        this.f1700b = 4;
        this.f1701c = Float.floatToRawIntBits(f);
        this.f1706j = Integer.MAX_VALUE & (this.f1700b + ((int) f));
    }

    void m1742a(int i) {
        this.f1700b = 3;
        this.f1701c = i;
        this.f1706j = Integer.MAX_VALUE & (this.f1700b + i);
    }

    void m1743a(int i, String str, String str2, String str3) {
        this.f1700b = i;
        this.f1703g = str;
        this.f1704h = str2;
        this.f1705i = str3;
        switch (i) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
            case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                this.f1706j = (str.hashCode() + i) & Integer.MAX_VALUE;
            case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                this.f1706j = ((str.hashCode() * str2.hashCode()) + i) & Integer.MAX_VALUE;
            default:
                this.f1706j = (((str.hashCode() * str2.hashCode()) * str3.hashCode()) + i) & Integer.MAX_VALUE;
        }
    }

    void m1744a(long j) {
        this.f1700b = 5;
        this.f1702d = j;
        this.f1706j = Integer.MAX_VALUE & (this.f1700b + ((int) j));
    }

    boolean m1745a(Item item) {
        switch (this.f1700b) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
            case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                return item.f1703g.equals(this.f1703g);
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                return item.f1701c == this.f1701c;
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
            case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                return item.f1702d == this.f1702d;
            case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                return item.f1703g.equals(this.f1703g) && item.f1704h.equals(this.f1704h);
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                return item.f1701c == this.f1701c && item.f1703g.equals(this.f1703g);
            default:
                return item.f1703g.equals(this.f1703g) && item.f1704h.equals(this.f1704h) && item.f1705i.equals(this.f1705i);
        }
    }
}

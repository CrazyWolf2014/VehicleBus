package com.ifoer.entity;

import com.google.protobuf.DescriptorProtos.MessageOptions;
import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.xbill.DNS.KEYRecord;

public class QueryAlipayRSATrade implements KvmSerializable {
    protected String basePath;
    protected int orderId;

    public int getOrderId() {
        return this.orderId;
    }

    public void setOrderId(int value) {
        this.orderId = value;
    }

    public String getBasePath() {
        return this.basePath;
    }

    public void setBasePath(String value) {
        this.basePath = value;
    }

    public Object getProperty(int index) {
        switch (index) {
            case KEYRecord.OWNER_USER /*0*/:
                return Integer.valueOf(this.orderId);
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return this.basePath;
            default:
                return null;
        }
    }

    public int getPropertyCount() {
        return 2;
    }

    public void setProperty(int index, Object obj) {
        switch (index) {
            case KEYRecord.OWNER_USER /*0*/:
                this.orderId = Integer.valueOf(obj.toString()).intValue();
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                this.basePath = obj.toString();
            default:
        }
    }

    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {
        switch (index) {
            case KEYRecord.OWNER_USER /*0*/:
                info.name = "orderId";
                info.type = PropertyInfo.INTEGER_CLASS;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                info.name = "basePath";
                info.type = PropertyInfo.STRING_CLASS;
            default:
        }
    }
}

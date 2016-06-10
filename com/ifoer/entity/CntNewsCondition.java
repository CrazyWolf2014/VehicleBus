package com.ifoer.entity;

import com.google.protobuf.DescriptorProtos.MessageOptions;
import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.xbill.DNS.KEYRecord;

public class CntNewsCondition implements KvmSerializable {
    private boolean f2117D;
    private Integer lanId;
    private Integer newType;

    public CntNewsCondition() {
        this.f2117D = false;
    }

    public Integer getLanId() {
        return this.lanId;
    }

    public void setLanId(Integer value) {
        this.lanId = value;
    }

    public Integer getNewType() {
        return this.newType;
    }

    public void setNewType(Integer value) {
        this.newType = value;
    }

    public Object getProperty(int index) {
        switch (index) {
            case KEYRecord.OWNER_USER /*0*/:
                return this.lanId;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return this.newType;
            default:
                return null;
        }
    }

    public int getPropertyCount() {
        return 2;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch (index) {
            case KEYRecord.OWNER_USER /*0*/:
                info.name = "lanId";
                info.type = PropertyInfo.INTEGER_CLASS;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                info.name = "newType";
                info.type = PropertyInfo.INTEGER_CLASS;
            default:
        }
    }

    public void setProperty(int index, Object obj) {
        switch (index) {
            case KEYRecord.OWNER_USER /*0*/:
                this.lanId = Integer.valueOf(Integer.parseInt(obj.toString()));
                break;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                this.newType = Integer.valueOf(Integer.parseInt(obj.toString()));
                break;
        }
        if (this.f2117D) {
            System.out.print(this.lanId + "==" + this.newType);
        }
    }
}

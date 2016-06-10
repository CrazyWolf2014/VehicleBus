package com.ifoer.entity;

import com.cnlaunch.x431pro.common.Constants;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import java.util.Hashtable;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.xbill.DNS.KEYRecord;

public class MaintenanceInfo implements KvmSerializable {
    protected String adress;
    protected int cc;
    protected String companyName;
    protected int currentMileage;
    protected String maintenanceNote;
    protected String maintenanceTime;
    protected String nextMaintenanceTime;
    protected int nextMileage;

    public String getAdress() {
        return this.adress;
    }

    public void setAdress(String value) {
        this.adress = value;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public int getCc() {
        return this.cc;
    }

    public void setCc(int cc) {
        this.cc = cc;
    }

    public void setCompanyName(String value) {
        this.companyName = value;
    }

    public String getMaintenanceNote() {
        return this.maintenanceNote;
    }

    public int getCurrentMileage() {
        return this.currentMileage;
    }

    public void setCurrentMileage(int currentMileage) {
        this.currentMileage = currentMileage;
    }

    public void setMaintenanceNote(String value) {
        this.maintenanceNote = value;
    }

    public String getMaintenanceTime() {
        return this.maintenanceTime;
    }

    public void setMaintenanceTime(String maintenanceTime) {
        this.maintenanceTime = maintenanceTime;
    }

    public String getNextMaintenanceTime() {
        return this.nextMaintenanceTime;
    }

    public void setNextMaintenanceTime(String nextMaintenanceTime) {
        this.nextMaintenanceTime = nextMaintenanceTime;
    }

    public int getNextMileage() {
        return this.nextMileage;
    }

    public void setNextMileage(int nextMileage) {
        this.nextMileage = nextMileage;
    }

    public Object getProperty(int index) {
        switch (index) {
            case KEYRecord.OWNER_USER /*0*/:
                return this.adress;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return Integer.valueOf(this.cc);
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return this.companyName;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return Integer.valueOf(this.currentMileage);
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                return this.maintenanceNote;
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                return this.maintenanceTime;
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                return this.nextMaintenanceTime;
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                return Integer.valueOf(this.nextMileage);
            default:
                return null;
        }
    }

    public int getPropertyCount() {
        return 8;
    }

    public void setProperty(int index, Object value) {
        switch (index) {
            case KEYRecord.OWNER_USER /*0*/:
                this.adress = value.toString();
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                this.cc = Integer.valueOf(value.toString()).intValue();
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                this.companyName = value.toString();
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                this.currentMileage = Integer.valueOf(value.toString()).intValue();
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                this.maintenanceNote = value.toString();
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                this.maintenanceTime = value.toString();
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                this.nextMaintenanceTime = value.toString();
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                this.nextMileage = Integer.valueOf(value.toString()).intValue();
            default:
        }
    }

    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {
        switch (index) {
            case KEYRecord.OWNER_USER /*0*/:
                info.name = "adress";
                info.type = PropertyInfo.STRING_CLASS;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                info.name = MultipleAddresses.CC;
                info.type = PropertyInfo.INTEGER_CLASS;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                info.name = Constants.COMPANYNAME;
                info.type = PropertyInfo.STRING_CLASS;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                info.name = "currentMileage";
                info.type = PropertyInfo.INTEGER_CLASS;
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                info.name = "maintenanceNote";
                info.type = PropertyInfo.STRING_CLASS;
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                info.name = "maintenanceTime";
                info.type = PropertyInfo.STRING_CLASS;
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                info.name = "nextMaintenanceTime";
                info.type = PropertyInfo.STRING_CLASS;
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                info.name = "nextMileage";
                info.type = PropertyInfo.INTEGER_CLASS;
            default:
        }
    }
}

package com.ifoer.entity;

import com.google.protobuf.DescriptorProtos.MessageOptions;
import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.xbill.DNS.KEYRecord;

public class SecurityAnswerDTO implements KvmSerializable {
    private String answer;
    private Integer questionId;

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String value) {
        this.answer = value;
    }

    public Integer getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(Integer value) {
        this.questionId = value;
    }

    public Object getProperty(int index) {
        switch (index) {
            case KEYRecord.OWNER_USER /*0*/:
                return this.questionId;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return this.answer;
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
                info.name = "questionId";
                info.type = PropertyInfo.INTEGER_CLASS;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                info.name = "answer";
                info.type = PropertyInfo.STRING_CLASS;
            default:
        }
    }

    public void setProperty(int index, Object obj) {
        switch (index) {
            case KEYRecord.OWNER_USER /*0*/:
                this.questionId = Integer.valueOf(Integer.parseInt(obj.toString()));
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                this.answer = obj.toString();
            default:
        }
    }
}

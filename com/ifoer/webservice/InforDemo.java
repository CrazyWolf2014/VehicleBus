package com.ifoer.webservice;

import com.ifoer.entity.CntNewsCondition;
import com.thoughtworks.xstream.XStream;

public class InforDemo {
    public static void main(String[] args) {
        CntNewsCondition cn = new CntNewsCondition();
        cn.setLanId(Integer.valueOf(XStream.ID_REFERENCES));
        cn.setNewType(Integer.valueOf(1));
        InforServiceClient client = new InforServiceClient();
    }
}

package com.car.result;

import com.ifoer.entity.DiagSoftOrderInfo;
import java.util.ArrayList;
import java.util.List;

public class DiagSoftOrderResult extends WSResult {
    protected List<DiagSoftOrderInfo> orderInfo;
    protected String orderSn;

    public DiagSoftOrderResult() {
        this.orderInfo = new ArrayList();
    }

    public List<DiagSoftOrderInfo> getOrderInfo() {
        return this.orderInfo;
    }

    public void setOrderInfo(List<DiagSoftOrderInfo> orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getOrderSn() {
        return this.orderSn;
    }

    public void setOrderSn(String value) {
        this.orderSn = value;
    }
}

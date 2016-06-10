package com.car.result;

import com.ifoer.entity.OrderDetail;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailInfoResult extends WSResult {
    protected List<OrderDetail> orderDetailList;

    public OrderDetailInfoResult() {
        this.orderDetailList = new ArrayList();
    }

    public List<OrderDetail> getOrderDetailList() {
        return this.orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }
}

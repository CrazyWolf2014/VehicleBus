package com.car.result;

import com.ifoer.entity.UserOrder;
import java.util.ArrayList;
import java.util.List;

public class UserOrderListResult extends WSResult {
    protected List<UserOrder> userOrder;

    public UserOrderListResult() {
        this.userOrder = new ArrayList();
    }

    public List<UserOrder> getUserOrder() {
        return this.userOrder;
    }

    public void setUserOrder(List<UserOrder> userOrder) {
        this.userOrder = userOrder;
    }
}

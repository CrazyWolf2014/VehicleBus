package com.ifoer.entity;

import com.car.result.WSResult;
import java.io.Serializable;

public class PushMessageListResult extends WSResult implements Serializable {
    private static final long serialVersionUID = 1;
    private PagingHelper pagingHelper;

    public PagingHelper getPagingHelper() {
        return this.pagingHelper;
    }

    public void setPagingHelper(PagingHelper pagingHelper) {
        this.pagingHelper = pagingHelper;
    }
}

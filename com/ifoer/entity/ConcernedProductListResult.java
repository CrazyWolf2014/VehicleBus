package com.ifoer.entity;

import com.car.result.WSResult;
import java.util.List;

public class ConcernedProductListResult extends WSResult {
    private List<ConcernedProductDTO> list;

    public List<ConcernedProductDTO> getList() {
        return this.list;
    }

    public void setList(List<ConcernedProductDTO> list) {
        this.list = list;
    }
}

package com.car.result;

import com.ifoer.entity.X431PadSoftDTO;
import java.util.ArrayList;
import java.util.List;

public class X431PadSoftListResult {
    private int code;
    private List<X431PadSoftDTO> dtoList;

    public X431PadSoftListResult() {
        this.dtoList = new ArrayList();
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<X431PadSoftDTO> getDtoList() {
        return this.dtoList;
    }

    public void setDtoList(List<X431PadSoftDTO> dtoList) {
        this.dtoList = dtoList;
    }
}

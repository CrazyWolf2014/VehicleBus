package com.car.result;

import com.ifoer.entity.DiagSoftPrice;
import java.util.ArrayList;
import java.util.List;

public class DiagSoftPriceResult extends WSResult {
    private int code;
    protected List<DiagSoftPrice> diagSoftPriceList;

    public DiagSoftPriceResult() {
        this.diagSoftPriceList = new ArrayList();
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DiagSoftPrice> getDiagSoftPriceList() {
        return this.diagSoftPriceList;
    }

    public void setDiagSoftPriceList(List<DiagSoftPrice> diagSoftPriceList) {
        this.diagSoftPriceList = diagSoftPriceList;
    }
}

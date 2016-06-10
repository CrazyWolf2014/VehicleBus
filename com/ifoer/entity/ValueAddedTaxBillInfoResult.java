package com.ifoer.entity;

import java.io.Serializable;
import java.util.List;

public class ValueAddedTaxBillInfoResult implements Serializable {
    private int code;
    private List<ValueAddedTaxBillInfoDTO> valueAddedTaxBillInfoDTO;

    public List<ValueAddedTaxBillInfoDTO> getValueAddedTaxBillInfoDTO() {
        return this.valueAddedTaxBillInfoDTO;
    }

    public void setValueAddedTaxBillInfoDTO(List<ValueAddedTaxBillInfoDTO> valueAddedTaxBillInfoDTO) {
        this.valueAddedTaxBillInfoDTO = valueAddedTaxBillInfoDTO;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

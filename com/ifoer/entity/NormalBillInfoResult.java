package com.ifoer.entity;

import java.util.List;

public class NormalBillInfoResult {
    private int code;
    private List<NormalBillInfoDTO> normalBillInfoDTO;

    public List<NormalBillInfoDTO> getNormalBillInfoDTO() {
        return this.normalBillInfoDTO;
    }

    public void setNormalBillInfoDTO(List<NormalBillInfoDTO> normalBillInfoDTO) {
        this.normalBillInfoDTO = normalBillInfoDTO;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

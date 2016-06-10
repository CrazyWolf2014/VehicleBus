package com.ifoer.entity;

import com.car.result.WSResult;
import java.util.List;

public class DiagSoftLanListResult extends WSResult {
    private List<DiagSoftLanDTO> lanList;

    public List<DiagSoftLanDTO> getLanList() {
        return this.lanList;
    }

    public void setLanList(List<DiagSoftLanDTO> lanList) {
        this.lanList = lanList;
    }
}

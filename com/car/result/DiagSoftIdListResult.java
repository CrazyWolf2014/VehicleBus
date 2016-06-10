package com.car.result;

import com.ifoer.entity.DiagSoftIdDTO;
import java.util.ArrayList;
import java.util.List;

public class DiagSoftIdListResult extends WSResult {
    protected List<DiagSoftIdDTO> diagSoftIdList;

    public DiagSoftIdListResult() {
        this.diagSoftIdList = new ArrayList();
    }

    public List<DiagSoftIdDTO> getDiagSoftIdList() {
        return this.diagSoftIdList;
    }

    public void setDiagSoftIdList(List<DiagSoftIdDTO> diagSoftIdList) {
        this.diagSoftIdList = diagSoftIdList;
    }
}

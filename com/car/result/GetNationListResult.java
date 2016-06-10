package com.car.result;

import com.ifoer.entity.NationDTO;
import java.util.ArrayList;
import java.util.List;

public class GetNationListResult extends WSResult {
    protected List<NationDTO> nationList;

    public List<NationDTO> getNationList() {
        if (this.nationList == null) {
            this.nationList = new ArrayList();
        }
        return this.nationList;
    }
}

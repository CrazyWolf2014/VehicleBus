package com.car.result;

import com.ifoer.entity.CntSynNews;
import java.util.ArrayList;
import java.util.List;

public class CntNewsDescResult extends WSResult {
    protected CntSynNews cntNewsDesc;
    private List<String> imagPaths;

    public CntNewsDescResult() {
        this.imagPaths = new ArrayList();
    }

    public List<String> getImagPaths() {
        return this.imagPaths;
    }

    public void setImagPaths(List<String> imagPaths) {
        this.imagPaths = imagPaths;
    }

    public CntSynNews getCntNewsDesc() {
        return this.cntNewsDesc;
    }

    public void setCntNewsDesc(CntSynNews value) {
        this.cntNewsDesc = value;
    }
}

package com.car.result;

import com.ifoer.entity.SoftPackageDto;
import java.util.ArrayList;
import java.util.List;

public class SoftPackageInfoResult extends WSResult {
    protected List<SoftPackageDto> softPackageList;

    public SoftPackageInfoResult() {
        this.softPackageList = new ArrayList();
    }

    public List<SoftPackageDto> getSoftPackageList() {
        return this.softPackageList;
    }

    public void setSoftPackageList(List<SoftPackageDto> softPackageList) {
        this.softPackageList = softPackageList;
    }
}

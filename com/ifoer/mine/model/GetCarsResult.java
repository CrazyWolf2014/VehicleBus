package com.ifoer.mine.model;

import java.util.List;

public class GetCarsResult extends BaseCode {
    private static final long serialVersionUID = -7396083812415844321L;
    private List<CarInfo> data;

    public List<CarInfo> getData() {
        return this.data;
    }

    public void setData(List<CarInfo> data) {
        this.data = data;
    }
}

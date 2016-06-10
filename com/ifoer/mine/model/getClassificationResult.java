package com.ifoer.mine.model;

import java.util.List;

public class getClassificationResult extends BaseCode {
    private static final long serialVersionUID = 5959509462471891904L;
    private List<SeriesInfo> data;

    public List<SeriesInfo> getData() {
        return this.data;
    }

    public void setData(List<SeriesInfo> data) {
        this.data = data;
    }
}

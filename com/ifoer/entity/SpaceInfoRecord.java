package com.ifoer.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpaceInfoRecord implements Serializable {
    private ArrayList<SptExDataStreamIdItem> filterLists;
    private int id;
    private String path;
    private List<Integer> seletedId;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Integer> getSeletedId() {
        return this.seletedId;
    }

    public void setSeletedId(List<Integer> seletedId) {
        this.seletedId = seletedId;
    }

    public ArrayList<SptExDataStreamIdItem> getFilterLists() {
        return this.filterLists;
    }

    public void setFilterLists(ArrayList<SptExDataStreamIdItem> filterLists) {
        this.filterLists = filterLists;
    }
}

package com.cnlaunch.framework.utils.location;

import java.util.List;

public class LocationResponse {
    private List<LocationResults> results;
    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LocationResults> getResults() {
        return this.results;
    }

    public void setResults(List<LocationResults> results) {
        this.results = results;
    }
}

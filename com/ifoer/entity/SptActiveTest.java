package com.ifoer.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class SptActiveTest implements Serializable {
    private static final long serialVersionUID = 1;
    private ArrayList<SptActiveTestButton> activeTestButtons;
    private ArrayList<SptActiveTestStream> activeTestStreams;

    public SptActiveTest() {
        this.activeTestButtons = new ArrayList();
        this.activeTestStreams = new ArrayList();
    }

    public ArrayList<SptActiveTestButton> getActiveTestButtons() {
        return this.activeTestButtons;
    }

    public void setActiveTestButtons(ArrayList<SptActiveTestButton> activeTestButtons) {
        this.activeTestButtons = activeTestButtons;
    }

    public ArrayList<SptActiveTestStream> getActiveTestStreams() {
        return this.activeTestStreams;
    }

    public void setActiveTestStreams(ArrayList<SptActiveTestStream> activeTestStreams) {
        this.activeTestStreams = activeTestStreams;
    }
}

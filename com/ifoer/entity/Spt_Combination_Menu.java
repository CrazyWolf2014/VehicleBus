package com.ifoer.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Spt_Combination_Menu implements Serializable {
    private static final long serialVersionUID = 1;
    private int btnState;
    private ArrayList<String> data;
    private int firstNum;
    private int intTotle;

    public Spt_Combination_Menu() {
        this.data = new ArrayList();
    }

    public int getFirstNum() {
        return this.firstNum;
    }

    public void setFirstNum(int firstNum) {
        this.firstNum = firstNum;
    }

    public int getIntTotle() {
        return this.intTotle;
    }

    public void setIntTotle(int intTotle) {
        this.intTotle = intTotle;
    }

    public int getBtnState() {
        return this.btnState;
    }

    public void setBtnState(int btnState) {
        this.btnState = btnState;
    }

    public ArrayList<String> getData() {
        return this.data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}

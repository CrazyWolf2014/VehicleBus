package com.ifoer.entity;

import java.io.Serializable;

public class IntData implements Serializable {
    private static final long serialVersionUID = 1;
    private int item;
    private boolean itemCheckedState;

    public IntData() {
        this.itemCheckedState = false;
    }

    public int getItem() {
        return this.item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public boolean getItemCheckedState() {
        return this.itemCheckedState;
    }

    public void setItemCheckedState(boolean itemCheckedState) {
        this.itemCheckedState = itemCheckedState;
    }
}

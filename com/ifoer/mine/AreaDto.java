package com.ifoer.mine;

public class AreaDto {
    String ccode;
    String display;

    public AreaDto(String ccode, String display) {
        this.ccode = ccode;
        this.display = display;
    }

    public String getCcode() {
        return this.ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
    }

    public String getDisplay() {
        return this.display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}

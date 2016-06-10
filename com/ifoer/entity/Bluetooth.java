package com.ifoer.entity;

public class Bluetooth {
    private String bluetoothaddress;
    private String bluetoothname;
    private String bluetoothpeidui;
    private String bluetootzhuantai;

    public String getBluetoothname() {
        return this.bluetoothname;
    }

    public void setBluetoothname(String bluetoothname) {
        this.bluetoothname = bluetoothname;
    }

    public String getBluetootzhuantai() {
        return this.bluetootzhuantai;
    }

    public void setBluetootzhuantai(String bluetootzhuantai) {
        this.bluetootzhuantai = bluetootzhuantai;
    }

    public String getBluetoothaddress() {
        return this.bluetoothaddress;
    }

    public void setBluetoothaddress(String bluetoothaddress) {
        this.bluetoothaddress = bluetoothaddress;
    }

    public String getBluetoothpeidui() {
        return this.bluetoothpeidui;
    }

    public void setBluetoothpeidui(String bluetoothpeidui) {
        this.bluetoothpeidui = bluetoothpeidui;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        Bluetooth other = (Bluetooth) o;
        if (getBluetoothaddress() == other.getBluetoothaddress() && getBluetoothname() == other.getBluetoothname()) {
            return true;
        }
        return false;
    }
}

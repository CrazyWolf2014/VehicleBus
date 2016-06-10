package com.ifoer.util;

import com.ifoer.entity.Constant;

public class CarDiagnose implements Runnable {
    private CarDiagnoseBridge bridge;

    public CarDiagnose(CarDiagnoseBridge bridge) {
        this.bridge = bridge;
    }

    public void run() {
        Constant.bridge = this.bridge;
        this.bridge.getData();
    }
}

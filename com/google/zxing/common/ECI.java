package com.google.zxing.common;

public abstract class ECI {
    private final int value;

    ECI(int i) {
        this.value = i;
    }

    public static ECI getECIByValue(int i) {
        if (i >= 0 && i <= 999999) {
            return i < 900 ? CharacterSetECI.getCharacterSetECIByValue(i) : null;
        } else {
            throw new IllegalArgumentException(new StringBuffer().append("Bad ECI value: ").append(i).toString());
        }
    }

    public int getValue() {
        return this.value;
    }
}

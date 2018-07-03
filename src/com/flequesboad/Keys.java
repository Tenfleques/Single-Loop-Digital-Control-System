package com.flequesboad;

public enum Keys {
    META(0),
    Y(1),
    Q_OPTIMUM(2),
    NORM_GRADIENT(0);
    private final int value;

    Keys(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

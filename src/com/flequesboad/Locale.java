package com.flequesboad;

public enum Locale {
    DYNAMIC_CHARACTER("Динамическая характеристика"),
    DISTURBED_DYNAMIC_CHARACTER("Динамическая характеристика по возмущению"),
    OPTIMUM_SETTINGS("Оптимальные настройки"),
    PROVIDED_Y("y заданое");
    private final String value;

    Locale(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

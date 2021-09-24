package ru.t1.asavin.techSupportAutomation.entity;

public enum Stage {

    OPEN("Открыт"),
    CLOSED("Закрыт"),
    PENDING("Ожидание информации");

    private final String name;

    Stage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

package com.example.ifarm.dto;

public class Pesticide {
    private final String id;
    private final String name;
    private final String unitType;

    public Pesticide(String id, String name, String unitType) {
        this.id = id;
        this.name = name;
        this.unitType = unitType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnitType() {
        return unitType;
    }
}

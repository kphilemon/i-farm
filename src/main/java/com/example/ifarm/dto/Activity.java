package com.example.ifarm.dto;

public class Activity {
    private final String id;
    private final String farmId;
    private final String userId;
    private final String date; // YYYY-MM-DD
    private final String action; // sowing, fertilizers, pesticides, harvest, or sales
    private final String type; // sowing/harvest/sales -> plant’s name, fertilizers -> fertilizer’s name, pesticides -> pesticide’s name.
    private final String unit; // mass -> kg/g, pack -> pack (500g)/ pack (1000g), volume -> l/m l
    private final Double quantity;
    private final Integer field;
    private final Integer row;

    public Activity(String id, String farmId, String userId, String date, String action, String type, String unit, Double quantity, Integer field, Integer row) {
        this.id = id;
        this.farmId = farmId;
        this.userId = userId;
        this.date = date;
        this.action = action;
        this.type = type;
        this.unit = unit;
        this.quantity = quantity;
        this.field = field;
        this.row = row;
    }

    public String getId() {
        return id;
    }

    public String getFarmId() {
        return farmId;
    }

    public String getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public String getAction() {
        return action;
    }

    public String getType() {
        return type;
    }

    public String getUnit() {
        return unit;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Integer getField() {
        return field;
    }

    public Integer getRow() {
        return row;
    }
}

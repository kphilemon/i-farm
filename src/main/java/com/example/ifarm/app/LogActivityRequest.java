package com.example.ifarm.app;

public class LogActivityRequest {
    private final String farmId;
    private final String userId;
    private final String date;
    private final String action;
    private final String targetId; // id of plant/fertilizer/pesticide depending on action
    private final int unit; // 0 or 1
    private final double quantity;
    private final int field;
    private final int row;

    public LogActivityRequest(String farmId, String userId, String date, String action, String targetId, int unit, double quantity, int field, int row) {
        this.farmId = farmId;
        this.userId = userId;
        this.date = date;
        this.action = action;
        this.targetId = targetId;
        this.unit = unit;
        this.quantity = quantity;
        this.field = field;
        this.row = row;
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

    public String getTargetId() {
        return targetId;
    }

    public int getUnit() {
        return unit;
    }

    public double getQuantity() {
        return quantity;
    }

    public int getField() {
        return field;
    }

    public int getRow() {
        return row;
    }

    @Override
    public String toString() {
        return String.format("{farmId=%s, userId=%s, date=%s, action=%s, targetId=%s, unit=%d, quantity=%f, field=%d, row=%d}",
                farmId,
                userId,
                date,
                action,
                targetId,
                unit,
                quantity,
                field,
                row);
    }
}

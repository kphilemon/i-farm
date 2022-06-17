package com.example.ifarm.app;

public class LogActivityResponse {
    private final int status;
    private final String message;

    public LogActivityResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("{status=%d, message=%s}", status, message);
    }
}

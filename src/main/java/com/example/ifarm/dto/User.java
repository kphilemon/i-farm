package com.example.ifarm.dto;

import java.util.List;

public class User {
    private final String id;
    private final String name;
    private final String email;
    private final String password;
    private final String phoneNumber;
    private final List<String> farms; // list of farm ids

    public User(String id, String name, String email, String password, String phoneNumber, List<String> farms) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.farms = farms;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<String> getFarms() {
        return farms;
    }
}

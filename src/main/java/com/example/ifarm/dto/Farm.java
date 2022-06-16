package com.example.ifarm.dto;

import java.util.List;

public class Farm {
    private final String id;
    private final String name;
    private final String address;
    private final List<String> plants; // list of plant ids
    private final List<String> fertilizers; // list of fertilizer ids
    private final List<String> pesticides; // list of pesticide ids

    public Farm(String id, String name, String address, List<String> plants, List<String> fertilizers, List<String> pesticides) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.plants = plants;
        this.fertilizers = fertilizers;
        this.pesticides = pesticides;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<String> getPlants() {
        return plants;
    }

    public List<String> getFertilizers() {
        return fertilizers;
    }

    public List<String> getPesticides() {
        return pesticides;
    }
}

package com.example.ifarm.farmersimulator;

import com.example.ifarm.dto.Farm;
import com.example.ifarm.util.Constants;
import com.example.ifarm.util.DateUtils;

import java.util.List;
import java.util.Random;

public class Farmer extends Thread {
    private final int activitiesPerFarm;
    private final String userId;
    private final List<Farm> farms;

    public Farmer(int activitiesPerFarm, String userId, List<Farm> farms) {
        this.activitiesPerFarm = activitiesPerFarm;
        this.userId = userId;
        this.farms = farms;
    }

    @Override
    public void run() {
        System.out.println("farmer: " + userId);
        for (Farm farm : farms) {
            for (int i = 0; i < activitiesPerFarm; i++) {
                generateRandomActivity(farm);
            }
        }
    }

    // generate random activity for a certain farm
    private void generateRandomActivity(Farm farm) {
        List<String> plantIds = farm.getPlants();
        List<String> fertilizerIds = farm.getFertilizers();
        List<String> pesticideIds = farm.getPesticides();
        Random random = new Random();

        String farmId = farm.getId();
        String date = DateUtils.getCurrentDateString();

        // Get a random action from action list
        String action = Constants.ACTION_LIST[random.nextInt(Constants.ACTION_LIST.length)];

        // decide target id based on action
        String targetId = "";
        switch (action) {
            case Constants.ACTION_SOWING, Constants.ACTION_HARVEST, Constants.ACTION_SALES ->
                    targetId = plantIds.get(random.nextInt(plantIds.size()));
            case Constants.ACTION_FERTILIZERS -> targetId = fertilizerIds.get(random.nextInt(fertilizerIds.size()));
            case Constants.ACTION_PESTICIDES -> targetId = pesticideIds.get(random.nextInt(pesticideIds.size()));
        }

        // unit is either 0 or 1 because there are only two types for all scenarios
        int unit = random.nextInt(2);

        // random values
        double quantity = random.nextDouble(500) + 1;
        int field = random.nextInt(5) + 1;
        int row = random.nextInt(5) + 1;

        // Temporarily print out
        System.out.println(farmId);
        System.out.println(date);
        System.out.println(action);
        System.out.println(targetId);
        System.out.println(unit);
        System.out.println(quantity);
        System.out.println(field);
        System.out.println(row);
    }
}

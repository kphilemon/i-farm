package com.example.ifarm.farmersimulator;

import com.example.ifarm.app.IFarmService;
import com.example.ifarm.app.LogActivityRequest;
import com.example.ifarm.app.LogActivityResponse;
import com.example.ifarm.dto.Farm;
import com.example.ifarm.util.Constants;
import com.example.ifarm.util.DateUtils;

import java.util.List;
import java.util.Random;

public class Farmer extends Thread {
    private final IFarmService iFarmService;
    private final int activitiesPerFarm;
    private final String userId;
    private final List<Farm> farms;

    public Farmer(IFarmService iFarmService, int activitiesPerFarm, String userId, List<Farm> farms) {
        this.iFarmService = iFarmService;
        this.activitiesPerFarm = activitiesPerFarm;
        this.userId = userId;
        this.farms = farms;
    }

    @Override
    public void run() {
        System.out.println("farmer " + userId + " started!");
        for (Farm farm : farms) {
            for (int i = 0; i < activitiesPerFarm; i++) {
                LogActivityRequest request = generateRandomActivity(farm);

                int status = 0;
                do {
                    LogActivityResponse response = iFarmService.logActivity(request);
                    status = response.getStatus();
                } while (status != Constants.STATUS_OK && status != Constants.STATUS_INVALID_REQUEST);
            }
        }
    }

    // generate random activity for a certain farm
    private LogActivityRequest generateRandomActivity(Farm farm) {
        List<String> plantIds = farm.getPlants();
        List<String> fertilizerIds = farm.getFertilizers();
        List<String> pesticideIds = farm.getPesticides();
        Random random = new Random();

        String farmId = farm.getId();
        String date = DateUtils.getCurrentDateString();

        // Get a random action from action list
        String action = Constants.ACTION_LIST.get(random.nextInt(Constants.ACTION_LIST.size()));

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

        // generate random double from 1 to 500 with 1 decimal place
        double quantity = random.nextDouble(500) + 1;
        quantity = Math.round(quantity * 10.0) / 10.0;

        // generate random int from 1 to 5 for field and row
        int field = random.nextInt(5) + 1;
        int row = random.nextInt(5) + 1;

        return new LogActivityRequest(farmId, userId, date, action, targetId, unit, quantity, field, row);
    }
}

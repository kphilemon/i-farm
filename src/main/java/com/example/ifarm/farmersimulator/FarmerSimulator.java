package com.example.ifarm.farmersimulator;

import com.example.ifarm.app.IFarmService;
import com.example.ifarm.dao.FarmDAO;
import com.example.ifarm.dao.UserDAO;
import com.example.ifarm.database.ConnectionPool;
import com.example.ifarm.dto.Farm;
import com.example.ifarm.dto.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FarmerSimulator implements FarmerSimulatorInterface {
    private final IFarmService iFarmService;
    private final int activitiesPerFarm;
    private final ConnectionPool connectionPool;

    public FarmerSimulator(IFarmService iFarmService, int activitiesPerFarm, ConnectionPool connectionPool) {
        this.iFarmService = iFarmService;
        this.activitiesPerFarm = activitiesPerFarm;
        this.connectionPool = connectionPool;
    }

    @Override
    public Farmer[] generateFarmers(int numberOfFarmers) {
        UserDAO userDAO = new UserDAO(connectionPool);
        FarmDAO farmDAO = new FarmDAO(connectionPool);

        Farmer[] farmers = new Farmer[numberOfFarmers];
        for (int i = 0; i < farmers.length; i++) {
            // generate farmer (user) details
            String id = Integer.toString(i + 1);
            String name = "farmer" + id;
            String email = name + "@gmail.com";
            String password = "pw_" + name;
            String phoneNumber = "011 - 111111" + id;
            List<String> farmIds = generateRandomFarmIds();
            User user = new User(id, name, email, password, phoneNumber, farmIds);

            try {
                userDAO.create(user);
            } catch (SQLException | InterruptedException e) {
                System.out.println("failed to create farmer with id " + id);
            }

            farmers[i] = new Farmer(iFarmService, activitiesPerFarm, id, getFarms(farmDAO, farmIds));
        }

        return farmers;
    }

    private List<String> generateRandomFarmIds() {
        // shuffle ids from 1 to 10 because we only have 10 farms
        List<Integer> ids = IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList());
        Collections.shuffle(ids);

        // generate random number of farms from 1 to 3
        Random random = new Random();
        int numOfFarms = random.nextInt(3) + 1;

        List<String> randomIds = new ArrayList<>();
        for (int i = 0; i < numOfFarms; i++) {
            randomIds.add(ids.get(i).toString());
        }
        return randomIds;
    }

    private List<Farm> getFarms(FarmDAO dao, List<String> farmIds) {
        List<Farm> farms = new ArrayList<>();
        for (String farmId : farmIds) {
            try {
                Farm farm = dao.get(farmId);
                if (farm == null) {
                    System.out.println("farm id " + farmId + " does not exist!");
                    continue;
                }
                farms.add(farm);
            } catch (SQLException | InterruptedException e) {
                System.out.println("failed to query farm " + farmId);
            }
        }
        return farms;
    }
}



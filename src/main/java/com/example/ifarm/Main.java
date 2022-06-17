package com.example.ifarm;

import com.example.ifarm.farmersimulator.Farmer;
import com.example.ifarm.farmersimulator.FarmerSimulator;
import com.example.ifarm.util.DatabaseUtils;
import com.example.ifarm.util.DummyDataGenerator;

import java.sql.SQLException;

public class Main {
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/ifarm";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final int NUMBER_OF_FARMERS = 100;
    private static final int ACTIVITIES_PER_FARM = 1000;

    public static void main(String[] args) throws SQLException, InterruptedException {
        // Reset database
        DatabaseUtils.resetDatabase(CONNECTION_URL, USER, PASSWORD);

        // Generate dummy data
        DummyDataGenerator dummyDataGenerator = new DummyDataGenerator(CONNECTION_URL, USER, PASSWORD);
        dummyDataGenerator.generate();

        // Simulate farmer threads
        FarmerSimulator farmerSimulator = new FarmerSimulator(ACTIVITIES_PER_FARM, CONNECTION_URL, USER, PASSWORD);
        Farmer[] farmers = farmerSimulator.generateFarmers(NUMBER_OF_FARMERS);

        for (Farmer farmer : farmers) {
            farmer.start();
        }
        for (Farmer farmer : farmers) {
            farmer.join();
        }
    }
}
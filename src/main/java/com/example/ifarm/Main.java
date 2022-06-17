package com.example.ifarm;

import com.example.ifarm.app.IFarm;
import com.example.ifarm.app.IFarmService;
import com.example.ifarm.farmersimulator.Farmer;
import com.example.ifarm.farmersimulator.FarmerSimulator;
import com.example.ifarm.timer.Timer;
import com.example.ifarm.util.DatabaseUtils;
import com.example.ifarm.util.DummyDataGenerator;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/ifarm";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final int NUMBER_OF_FARMERS = 100;
    private static final int ACTIVITIES_PER_FARM = 1000;

    public static void main(String[] args) throws SQLException, InterruptedException, IOException {
        // Reset database
        DatabaseUtils.resetDatabase(CONNECTION_URL, USER, PASSWORD);

        // Generate dummy data
        DummyDataGenerator dummyDataGenerator = new DummyDataGenerator(CONNECTION_URL, USER, PASSWORD);
        dummyDataGenerator.generate();
        System.out.println("Dummy data ready.");

        // Instantiate ifarm service
        IFarmService iFarmService = new IFarm(CONNECTION_URL, USER, PASSWORD);
        System.out.println("iFarm service ready.");

        // Simulate farmer threads
        FarmerSimulator farmerSimulator = new FarmerSimulator(iFarmService, ACTIVITIES_PER_FARM, CONNECTION_URL, USER, PASSWORD);
        Farmer[] farmers = farmerSimulator.generateFarmers(NUMBER_OF_FARMERS);

        Timer timer = new Timer();
        timer.start();

        // Start all farmer threads
        for (Farmer farmer : farmers) {
            farmer.start();
        }
        // wait for all farmer threads to be complete
        for (Farmer farmer : farmers) {
            farmer.join();
        }
        // shuts the service down
        iFarmService.shutdown();

        timer.stop();
        System.out.println("Total time taken to process all activities concurrently: " + timer.getMillisecondsElapsed() + "ms");
        System.out.println("==================================================\n");
    }
}
package com.example.ifarm.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DatabaseSetup {
    public static void run() {
        readAndInsert("data/fertilizers.csv", "fertilizer");
        readAndInsert("data/pesticides.csv", "pesticide");
        readAndInsert("data/plants.csv", "plant");
        insertFarmData("data/farms.csv");
    }

    private static void readAndInsert(String dataFile, String tableName) {
        try (Scanner sc = new Scanner(new File(DatabaseSetup.class.getClassLoader().getResource(dataFile).getFile()))) {
            sc.nextLine(); // skip table headers
            while (sc.hasNext()) {
                String[] columns = sc.nextLine().split(",");
                String id = columns[0];
                String name = columns[1];
                String unitType = columns[2];

                String query = String.format("INSERT INTO %s (`id`, `name` , `unit_type`) VALUES('%s', '%s', '%s');", tableName, id, name, unitType);
                System.out.println(query);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void insertFarmData(String dataFile) {
        try (Scanner sc = new Scanner(new File(DatabaseSetup.class.getClassLoader().getResource(dataFile).getFile()))) {
            sc.nextLine(); // skip table headers
            while (sc.hasNext()) {
                String[] columns = sc.nextLine().split(",");
                String farmId = columns[0];
                String farmName = columns[1];
                String farmAddress = columns[2];
                int numOfPlants = Integer.parseInt(columns[3]);
                int numOfFertilizers = Integer.parseInt(columns[4]);
                int numOfPesticides = Integer.parseInt(columns[5]);

                String query = String.format("INSERT INTO farm (`id`, `name` , `address`) VALUES('%s', '%s', '%s');", farmId, farmName, farmAddress);
                System.out.println(query);

                insertBridgeData("farm_plant", "farm_id", "plant_id", farmId, numOfPlants);
                insertBridgeData("farm_fertilizer", "farm_id", "fertilizer_id", farmId, numOfFertilizers);
                insertBridgeData("farm_pesticide", "farm_id", "pesticide_id", farmId, numOfPesticides);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void insertBridgeData(String tableName, String fk1, String fk2, String farmId, int count) {
        // shuffle ids from 1 to 100 because we only have max 100 entries
        ArrayList<Integer> randomIds = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            randomIds.add(i);
        }
        Collections.shuffle(randomIds);

        for (int i = 0; i < count; i++) {
            String query = String.format("INSERT INTO %s (`%s`, `%s`) VALUES('%s', '%s');", tableName, fk1, fk2, farmId, randomIds.get(i));
            System.out.println(query);
        }
    }
}

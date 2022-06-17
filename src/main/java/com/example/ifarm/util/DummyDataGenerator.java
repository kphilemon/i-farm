package com.example.ifarm.util;

import com.example.ifarm.dao.FarmDAO;
import com.example.ifarm.dao.FertilizerDAO;
import com.example.ifarm.dao.PesticideDAO;
import com.example.ifarm.dao.PlantDAO;
import com.example.ifarm.database.ConnectionPool;
import com.example.ifarm.dto.Farm;
import com.example.ifarm.dto.Fertilizer;
import com.example.ifarm.dto.Pesticide;
import com.example.ifarm.dto.Plant;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DummyDataGenerator {
    private final ConnectionPool connectionPool;

    public DummyDataGenerator(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void generate() {
        generatePlantsData(connectionPool);
        generateFertilizersData(connectionPool);
        generatePesticidesData(connectionPool);
        generateFarmsData(connectionPool);
    }

    private void generatePlantsData(ConnectionPool connectionPool) {
        PlantDAO dao = new PlantDAO(connectionPool);
        try (Scanner sc = new Scanner(new File(DummyDataGenerator.class.getClassLoader().getResource("data/plants.csv").getFile()))) {
            sc.nextLine(); // skip table headers
            while (sc.hasNext()) {
                String[] columns = sc.nextLine().split(",");
                String id = columns[0];
                String name = columns[1];
                String unitType = columns[2];
                dao.create(new Plant(id, name, unitType));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Plants data generated successfully.");
    }

    private void generateFertilizersData(ConnectionPool connectionPool) {
        FertilizerDAO dao = new FertilizerDAO(connectionPool);
        try (Scanner sc = new Scanner(new File(DummyDataGenerator.class.getClassLoader().getResource("data/fertilizers.csv").getFile()))) {
            sc.nextLine(); // skip table headers
            while (sc.hasNext()) {
                String[] columns = sc.nextLine().split(",");
                String id = columns[0];
                String name = columns[1];
                String unitType = columns[2];
                dao.create(new Fertilizer(id, name, unitType));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Fertilizers data generated successfully.");
    }

    private void generatePesticidesData(ConnectionPool connectionPool) {
        PesticideDAO dao = new PesticideDAO(connectionPool);
        try (Scanner sc = new Scanner(new File(DummyDataGenerator.class.getClassLoader().getResource("data/pesticides.csv").getFile()))) {
            sc.nextLine(); // skip table headers
            while (sc.hasNext()) {
                String[] columns = sc.nextLine().split(",");
                String id = columns[0];
                String name = columns[1];
                String unitType = columns[2];
                dao.create(new Pesticide(id, name, unitType));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Pesticides data generated successfully.");
    }

    private void generateFarmsData(ConnectionPool connectionPool) {
        FarmDAO dao = new FarmDAO(connectionPool);
        try (Scanner sc = new Scanner(new File(DummyDataGenerator.class.getClassLoader().getResource("data/farms.csv").getFile()))) {
            sc.nextLine(); // skip table headers
            while (sc.hasNext()) {
                String[] columns = sc.nextLine().split(",");
                String id = columns[0];
                String name = columns[1];
                String address = columns[2];
                int numOfPlants = Integer.parseInt(columns[3]);
                int numOfFertilizers = Integer.parseInt(columns[4]);
                int numOfPesticides = Integer.parseInt(columns[5]);

                List<String> plants = generateRandomIds(numOfPlants);
                List<String> fertilizers = generateRandomIds(numOfFertilizers);
                List<String> pesticides = generateRandomIds(numOfPesticides);
                dao.create(new Farm(id, name, address, plants, fertilizers, pesticides));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Farms data generated successfully.");
    }

    private List<String> generateRandomIds(int length) {
        // shuffle ids from 1 to 100 because we only have max 100 entries
        List<Integer> ids = IntStream.rangeClosed(1, 100).boxed().collect(Collectors.toList());
        Collections.shuffle(ids);

        List<String> randomIds = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            randomIds.add(ids.get(i).toString());
        }
        return randomIds;
    }
}

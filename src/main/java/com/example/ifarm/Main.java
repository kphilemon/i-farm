package com.example.ifarm;

import com.example.ifarm.util.DatabaseUtils;
import com.example.ifarm.util.DummyDataGenerator;

import java.sql.SQLException;

public class Main {
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/ifarm";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) throws SQLException {
        // Reset database
        DatabaseUtils.resetDatabase(CONNECTION_URL, USER, PASSWORD);

        // Generate dummy data
        DummyDataGenerator dummyDataGenerator = new DummyDataGenerator(CONNECTION_URL, USER, PASSWORD);
        dummyDataGenerator.generate();
    }
}
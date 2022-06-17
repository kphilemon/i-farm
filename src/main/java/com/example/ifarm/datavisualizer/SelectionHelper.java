package com.example.ifarm.datavisualizer;

import java.sql.*;
import java.util.ArrayList;

public class SelectionHelper {
    public static ArrayList<String> displayFarmSelection(String connectionUrl, String user, String password) {
        ArrayList<String> listOfFarmId = new ArrayList<>();
        String sqlQuery = "SELECT id, name FROM farm ORDER BY length(id),id";
        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password);
             PreparedStatement ps = conn.prepareStatement(sqlQuery);

             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                listOfFarmId.add(id);

                System.out.printf("%s.\t %s \n", id, name);
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return listOfFarmId;
    }

    public static ArrayList<String> displayFarmerSelection(String connectionUrl, String user, String password) {
        ArrayList<String> listOfFarmerId = new ArrayList<>();
        String sqlQuery = "SELECT id, name FROM user ORDER BY length(id),id";
        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password);
             PreparedStatement ps = conn.prepareStatement(sqlQuery);

             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                listOfFarmerId.add(id);

                System.out.printf("%s.\t %s \n", id, name);
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return listOfFarmerId;
    }

    public static ArrayList<String> displayTargetSelection(String target, String farmId, String connectionUrl, String user, String password) {
        ArrayList<String> listOfTargetId = new ArrayList<>();
        String sqlQuery = "SELECT id, name FROM " + target + " WHERE id IN " +
                "(SELECT " + target + "_id FROM farm_" + target + " WHERE farm_id = " + farmId + " ) " +
                "ORDER BY length(id),id";

        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password);
             PreparedStatement ps = conn.prepareStatement(sqlQuery);

             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                listOfTargetId.add(id);

                System.out.printf("%s.\t %s \n", id, name);
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return listOfTargetId;
    }
}
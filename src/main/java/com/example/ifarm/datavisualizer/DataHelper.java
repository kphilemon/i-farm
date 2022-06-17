package com.example.ifarm.datavisualizer;

import java.sql.*;

public class DataHelper {
    public static void getRecord(String targetField, String targetId, String connectionUrl, String user, String password) {
        String whereClause = targetField + " = '" + targetId + "'";
        String sqlQuery = "SELECT action, type, field, `row`, quantity, unit, date FROM activity WHERE " + whereClause;
        displayActivityLog(sqlQuery, connectionUrl, user, password, false);
    }

    // farm and plant / fertilizer / pesticide
    public static void getRecord(String targetField, String targetId, String table2, String targetId2, String connectionUrl, String user, String password) {
        String getName = "(SELECT name FROM " + table2 + " WHERE id = '" + targetId2 + "')";
        String whereClause = targetField + " = '" + targetId + "' AND type = " + getName;
        String sqlQuery = "SELECT action, type, field, `row`, quantity, unit, date FROM activity WHERE " + whereClause;
        displayActivityLog(sqlQuery, connectionUrl, user, password, false);
    }

    // farm and plant / fertilizer / pesticide between date
    public static void getRecord(String targetField, String targetId, String table2, String targetId2, String startDate, String endDate, String connectionUrl, String user, String password) {
        String getName = "(SELECT name FROM " + table2 + " WHERE id = '" + targetId2 + "')";
        String whereClause = targetField + " = '" + targetId + "' AND type = " + getName + "" +
                " AND date BETWEEN '" + startDate + "' AND '" + endDate + "'";
        String sqlQuery = "SELECT action, type, field, `row`, quantity, unit, date FROM activity WHERE " + whereClause;
        displayActivityLog(sqlQuery, connectionUrl, user, password, false);
    }

    // Summary
    public static void getSummary(String targetId, String startDate, String endDate, String field, String row, String connectionUrl, String user, String password) {
        String whereClause = "farm_id = '" + targetId + "' " +
                "AND field = " + field + " " +
                "AND `row` = " + row + " " +
                "AND date BETWEEN '" + startDate + "' AND '" + endDate + "' " +
                "GROUP BY action, type, field, `row`,unit";
        String sqlQuery = "SELECT action, type, field, `row`, SUM(quantity) AS quantity, unit FROM activity WHERE " + whereClause;
        displayActivityLog(sqlQuery, connectionUrl, user, password, true);
    }


    public static void displayActivityLog(String sqlQuery, String connectionUrl, String user, String password, Boolean getSummary) {
        int numberOfRow = 0;
        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password);
             PreparedStatement ps = conn.prepareStatement(sqlQuery);
             ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                if (numberOfRow == 0)
                    System.out.println("Activity logs: ");
                numberOfRow++;
                String action = rs.getString("action");
                String type = rs.getString("type");
                Integer field = rs.getInt("field");
                Integer row = rs.getInt("row");
                Double quantity = rs.getDouble("quantity");
                String unit = rs.getString("unit");
                if (!getSummary) {
                    String date = rs.getString("date");
                    if (quantity % 1 == 0) {
                        System.out.printf("%s %s Field %s Row %s %.0f %s %s \n", action, type, field, row, quantity, unit, date);
                    } else {
                        System.out.printf("%s %s Field %s Row %s %s %s %s \n", action, type, field, row, quantity, unit, date);
                    }
                } else { //summary, without date
                    if (quantity % 1 == 0) {
                        System.out.printf("%s %s Field %s Row %s %.0f %s \n", action, type, field, row, quantity, unit);
                    } else {
                        System.out.printf("%s %s Field %s Row %s %s %s \n", action, type, field, row, quantity, unit);
                    }
                }
            }
            if (numberOfRow == 0) {
                System.out.println("No data found!");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }
}
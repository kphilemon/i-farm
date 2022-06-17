package com.example.ifarm.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseUtils {
    public static void resetDatabase(String connectionUrl, String user, String password) throws SQLException {
        clearTable(connectionUrl, user, password, "farm_plant");
        clearTable(connectionUrl, user, password, "farm_fertilizer");
        clearTable(connectionUrl, user, password, "farm_pesticide");
        clearTable(connectionUrl, user, password, "user_farm");
        clearTable(connectionUrl, user, password, "activity");
        clearTable(connectionUrl, user, password, "farm");
        clearTable(connectionUrl, user, password, "plant");
        clearTable(connectionUrl, user, password, "fertilizer");
        clearTable(connectionUrl, user, password, "pesticide");
        clearTable(connectionUrl, user, password, "user");
    }

    public static void clearTable(String connectionUrl, String user, String password, String tableName) throws SQLException {
        String query = "DELETE FROM  " + tableName;
        try (Connection conn = DriverManager.getConnection(connectionUrl, user, password);
             PreparedStatement st = conn.prepareStatement(query)) {
            st.execute();
        }
    }
}

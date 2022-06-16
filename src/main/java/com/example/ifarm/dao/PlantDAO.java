package com.example.ifarm.dao;

import com.example.ifarm.database.ConnectionPool;
import com.example.ifarm.dto.Plant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class PlantDAO {
    private final ConnectionPool connectionPool;
    private final ConcurrentHashMap<String, Plant> cache;

    public PlantDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.cache = new ConcurrentHashMap<>();
    }

    public void create(Plant plant) throws SQLException, InterruptedException {
        String query = "INSERT INTO plant (`id`, `name` , `unit_type`) VALUES(?, ?, ?);";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, plant.getId());
            st.setString(2, plant.getName());
            st.setString(3, plant.getUnitType());
            st.execute();
            st.close();
        } catch (SQLException | InterruptedException e) {
            throw e;
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    public Plant get(String id) throws SQLException, InterruptedException {
        Plant plant = cache.get(id);
        // cache hit
        if (plant != null) {
            return plant;
        }

        // cache miss
        String query = String.format("SELECT `name`, `unit_type` FROM plant WHERE `id` = '%s';", id);
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String unitType = rs.getString("unit_type");
                plant = new Plant(id, name, unitType);

                // write to cache
                cache.putIfAbsent(id, plant);
            }
            rs.close();
            st.close();
        } catch (SQLException | InterruptedException e) {
            throw e;
        } finally {
            connectionPool.releaseConnection(conn);
        }

        return plant;
    }
}

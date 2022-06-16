package com.example.ifarm.dao;

import com.example.ifarm.database.ConnectionPool;
import com.example.ifarm.dto.Pesticide;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class PesticideDAO {
    private final ConnectionPool connectionPool;
    private final ConcurrentHashMap<String, Pesticide> cache;

    public PesticideDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.cache = new ConcurrentHashMap<>();
    }

    public void create(Pesticide pesticide) throws SQLException, InterruptedException {
        String query = "INSERT INTO pesticide (`id`, `name` , `unit_type`) VALUES(?, ?, ?);";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, pesticide.getId());
            st.setString(2, pesticide.getName());
            st.setString(3, pesticide.getUnitType());
            st.execute();
            st.close();
        } catch (SQLException | InterruptedException e) {
            throw e;
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    public Pesticide get(String id) throws SQLException, InterruptedException {
        Pesticide pesticide = cache.get(id);
        // cache hit
        if (pesticide != null) {
            return pesticide;
        }

        // cache miss
        String query = String.format("SELECT `name`, `unit_type` FROM pesticide WHERE `id` = '%s';", id);
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String unitType = rs.getString("unit_type");
                pesticide = new Pesticide(id, name, unitType);

                // write to cache
                cache.putIfAbsent(id, pesticide);
            }
            rs.close();
            st.close();
        } catch (SQLException | InterruptedException e) {
            throw e;
        } finally {
            connectionPool.releaseConnection(conn);
        }

        return pesticide;
    }
}

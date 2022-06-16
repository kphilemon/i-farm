package com.example.ifarm.dao;

import com.example.ifarm.database.ConnectionPool;
import com.example.ifarm.dto.Farm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class FarmDAO {
    private final ConnectionPool connectionPool;
    private final ConcurrentHashMap<String, Farm> cache;

    public FarmDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.cache = new ConcurrentHashMap<>();
    }

    public void create(Farm farm) throws SQLException, InterruptedException {
        String query = "INSERT INTO farm (`id`, `name` , `address`) VALUES(?, ?, ?);";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, farm.getId());
            st.setString(2, farm.getName());
            st.setString(3, farm.getAddress());
            st.execute();
            st.close();
        } catch (SQLException | InterruptedException e) {
            throw e;
        } finally {
            connectionPool.releaseConnection(conn);
        }

        insertBridgeRecords("plant", farm.getId(), farm.getPlants());
        insertBridgeRecords("fertilizer", farm.getId(), farm.getFertilizers());
        insertBridgeRecords("pesticide", farm.getId(), farm.getPesticides());
    }

    public Farm get(String id) throws SQLException, InterruptedException {
        Farm farm = cache.get(id);
        // cache hit
        if (farm != null) {
            return farm;
        }

        // cache miss
        String query = String.format("SELECT `name`, `address` FROM farm WHERE `id` = '%s';", id);
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String address = rs.getString("address");
                List<String> plants = queryBridgeRecords("plant", id);
                List<String> fertilizers = queryBridgeRecords("fertilizer", id);
                List<String> pesticides = queryBridgeRecords("pesticide", id);
                farm = new Farm(id, name, address, plants, fertilizers, pesticides);

                // write to cache
                cache.putIfAbsent(id, farm);
            }
            rs.close();
            st.close();
        } catch (SQLException | InterruptedException e) {
            throw e;
        } finally {
            connectionPool.releaseConnection(conn);
        }

        return farm;
    }

    private List<String> queryBridgeRecords(String target, String farmId) throws SQLException, InterruptedException {
        List<String> ids = new ArrayList<>();
        String query = String.format("SELECT %s_id FROM farm_%s WHERE farm_id = '%s';", target, target, farmId);
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                ids.add(rs.getString(target + "_id"));
            }
            rs.close();
            st.close();
        } catch (SQLException | InterruptedException e) {
            throw e;
        } finally {
            connectionPool.releaseConnection(conn);
        }
        return ids;
    }

    private void insertBridgeRecords(String target, String farmId, List<String> targetIds) throws SQLException, InterruptedException {
        for (String targetId : targetIds) {
            insertBridgeRecord(target, farmId, targetId);
        }
    }

    private void insertBridgeRecord(String target, String farmId, String targetId) throws SQLException, InterruptedException {
        String query = String.format("INSERT INTO farm_%s (`farm_id`, `%s_id`) VALUES(?, ?);", target, target);
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, farmId);
            st.setString(2, targetId);
            st.execute();
            st.close();
        } catch (SQLException | InterruptedException e) {
            throw e;
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }
}

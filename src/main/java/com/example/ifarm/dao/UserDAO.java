package com.example.ifarm.dao;

import com.example.ifarm.database.ConnectionPool;
import com.example.ifarm.dto.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class UserDAO {
    private final ConnectionPool connectionPool;
    private final ConcurrentHashMap<String, User> cache;

    public UserDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.cache = new ConcurrentHashMap<>();
    }

    public void create(User user) throws SQLException, InterruptedException {
        String query = "INSERT INTO user (`id`, `name` , `email`, `password`, `phone_number`) VALUES(?, ?, ?, ?, ?);";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, user.getId());
            st.setString(2, user.getName());
            st.setString(3, user.getEmail());
            st.setString(4, user.getPassword());
            st.setString(5, user.getPhoneNumber());
            st.execute();
            st.close();
        } catch (SQLException | InterruptedException e) {
            throw e;
        } finally {
            connectionPool.releaseConnection(conn);
        }

        insertFarmIds(user.getId(), user.getFarms());
    }

    public User get(String id) throws SQLException, InterruptedException {
        User user = cache.get(id);
        // cache hit
        if (user != null) {
            return user;
        }

        // cache miss
        String query = String.format("SELECT `name` , `email`, `password`, `phone_number` FROM user WHERE `id` = '%s';", id);
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String phoneNumber = rs.getString("phone_number");
                List<String> farmIds = queryFarmIds(id);
                user = new User(id, name, email, password, phoneNumber, farmIds);

                // write to cache
                cache.putIfAbsent(id, user);
            }
            rs.close();
            st.close();
        } catch (SQLException | InterruptedException e) {
            throw e;
        } finally {
            connectionPool.releaseConnection(conn);
        }

        return user;
    }

    private List<String> queryFarmIds(String userId) throws SQLException, InterruptedException {
        List<String> ids = new ArrayList<>();
        String query = String.format("SELECT farm_id FROM user_farm WHERE user_id = '%s';", userId);
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                ids.add(rs.getString("farm_id"));
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

    private void insertFarmIds(String userId, List<String> farmIds) throws SQLException, InterruptedException {
        for (String farmId : farmIds) {
            insertFarmId(userId, farmId);
        }
    }

    private void insertFarmId(String userId, String farmId) throws SQLException, InterruptedException {
        String query = "INSERT INTO user_farm (`user_id`, `farm_id`) VALUES(?, ?);";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, userId);
            st.setString(2, farmId);
            st.execute();
            st.close();
        } catch (SQLException | InterruptedException e) {
            throw e;
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }
}

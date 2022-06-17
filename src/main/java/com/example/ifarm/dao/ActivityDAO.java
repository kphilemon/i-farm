package com.example.ifarm.dao;

import com.example.ifarm.database.ConnectionPool;
import com.example.ifarm.dto.Activity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ActivityDAO {
    private final ConnectionPool connectionPool;

    public ActivityDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void create(Activity activity) throws SQLException, InterruptedException {
        String query = "INSERT INTO activity (`id`, `farm_id`, `user_id`, `date`, `action`, `type`, `unit`, `quantity`, `field`, `row`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, activity.getId());
            st.setString(2, activity.getFarmId());
            st.setString(3, activity.getUserId());
            st.setString(4, activity.getDate());
            st.setString(5, activity.getAction());
            st.setString(6, activity.getType());
            st.setString(7, activity.getUnit());
            st.setDouble(8, activity.getQuantity());
            st.setInt(9, activity.getField());
            st.setInt(10, activity.getRow());
            st.execute();
            st.close();
        } catch (SQLException | InterruptedException e) {
            throw e;
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }
}

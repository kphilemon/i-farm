package com.example.ifarm.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

// ConnectionPool stores a pool of fixed number connections
// Adapted from https://leetcode.com/discuss/general-discussion/1050178/design-a-thread-safe-connection-pool-java
public class ConnectionPool {
    // The default max connections is set to 151 to match the default max connections of MySQL database.
    public static final int DEFAULT_MAX_CONNECTIONS = 151;
    private final String connectionUrl;
    private final String user;
    private final String password;
    private final int maxConnections;
    private int numOfConnectionsCreated;
    private final Queue<Connection> pool;

    // Creates a connection pool with the specified maximum connections
    public ConnectionPool(String connectionUrl, String user, String password, int maxConnections) {
        this.connectionUrl = connectionUrl;
        this.user = user;
        this.password = password;

        if (maxConnections <= 0) {
            this.maxConnections = DEFAULT_MAX_CONNECTIONS;
        } else {
            this.maxConnections = maxConnections;
        }
        this.numOfConnectionsCreated = 0;
        this.pool = new LinkedList<>();
    }

    // Creates a connection pool with default max connection
    public ConnectionPool(String connectionUrl, String user, String password) {
        this(connectionUrl, user, password, DEFAULT_MAX_CONNECTIONS);
    }

    public Connection getConnection() throws SQLException, InterruptedException {
        boolean shouldCreateNewConnection = false;

        synchronized (this) {
            while (pool.isEmpty()) {
                if (numOfConnectionsCreated < maxConnections) {
                    numOfConnectionsCreated++;
                    shouldCreateNewConnection = true;
                    break;
                } else {
                    this.wait();
                }
            }

            if (!shouldCreateNewConnection) {
                return pool.poll();
            }
        }

        return createConnection();
    }

    public void releaseConnection(Connection conn) {
        synchronized (this) {
            pool.offer(conn);
            this.notifyAll();
        }
    }

    // Only call this method when you no longer need this connection pool and have released all connections
    public void shutdown() throws SQLException {
        synchronized (this) {
            for (Connection connection : pool) {
                connection.close();
            }
        }
    }

    private Connection createConnection() throws SQLException {
        try {
            return DriverManager.getConnection(connectionUrl, user, password);
        } catch (SQLException e) {
            synchronized (this) {
                numOfConnectionsCreated--;
                this.notifyAll();
            }
            throw e;
        }
    }
}

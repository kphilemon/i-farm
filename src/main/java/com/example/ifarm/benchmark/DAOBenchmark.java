package com.example.ifarm.benchmark;

import com.example.ifarm.dao.PlantDAO;
import com.example.ifarm.database.ConnectionPool;
import com.example.ifarm.dto.Plant;
import com.example.ifarm.timer.Timer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DAOBenchmark {
    private static final int N_THREADS = 100;
    private static final int N_COUNTS = 100000;
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/ifarm";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static final String TEST_ID = "testid";

    public static void main(String[] args) throws InterruptedException, SQLException {
        // NOTE: Run the benchmarking methods below ONE at a time for accurate results!
        // On average, query with cache performs better than query without cache

//        benchmarkQueryWithCache();
        benchmarkQueryWithoutCache();
    }

    public static void benchmarkQueryWithCache() throws SQLException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);
        List<Callable<Void>> tasks = new ArrayList<>();
        ConnectionPool pool = new ConnectionPool(CONNECTION_URL, USER, PASSWORD);
        PlantDAO plantDAO = new PlantDAO(pool);
        setup(pool);

        for (int i = 0; i < N_COUNTS; i++) {
            tasks.add(() -> {
                System.out.println(plantDAO.get(TEST_ID).getName());
                return null;
            });
        }

        Timer timer = new Timer();
        timer.start();
        executor.invokeAll(tasks);
        timer.stop();

        executor.shutdown();
        System.out.println("Total nanoseconds: " + timer.getNanosecondsElapsed());
        teardown(pool);
    }

    public static void benchmarkQueryWithoutCache() throws SQLException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);
        List<Callable<Void>> tasks = new ArrayList<>();
        ConnectionPool pool = new ConnectionPool(CONNECTION_URL, USER, PASSWORD);
        setup(pool);

        for (int i = 0; i < N_COUNTS; i++) {
            tasks.add(() -> {
                String query = String.format("SELECT `name`, `unit_type` FROM plant WHERE `id` = '%s';", TEST_ID);
                Connection conn = null;
                try {
                    conn = pool.getConnection();
                    PreparedStatement st = conn.prepareStatement(query);
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        System.out.println(rs.getString("name"));
                    }
                    rs.close();
                    st.close();
                } catch (SQLException | InterruptedException e) {
                    throw e;
                } finally {
                    pool.releaseConnection(conn);
                }
                return null;
            });
        }

        Timer timer = new Timer();
        timer.start();
        executor.invokeAll(tasks);
        timer.stop();

        executor.shutdown();
        System.out.println("Total nanoseconds: " + timer.getNanosecondsElapsed());
        teardown(pool);
    }


    // insert one plant record for testing
    private static void setup(ConnectionPool connectionPool) throws SQLException, InterruptedException {
        String query = "INSERT INTO plant (`id`, `name` , `unit_type`) VALUES(?, ?, ?);";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, TEST_ID);
            st.setString(2, "testname");
            st.setString(3, "testtype");
            st.execute();
            st.close();
        } catch (SQLException | InterruptedException e) {
            throw e;
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    // remove the plant record after testing
    private static void teardown(ConnectionPool connectionPool) throws SQLException, InterruptedException {
        String query = String.format("DELETE FROM plant WHERE `id` = '%s';", TEST_ID);
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            st.close();
        } catch (SQLException | InterruptedException e) {
            throw e;
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }
}

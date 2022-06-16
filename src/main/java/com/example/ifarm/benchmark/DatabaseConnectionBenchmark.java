package com.example.ifarm.benchmark;

import com.example.ifarm.database.ConnectionPool;
import com.example.ifarm.timer.Timer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseConnectionBenchmark {
    private static final int N_THREADS = 100;
    private static final int N_COUNTS = 100000;
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/ifarm";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String QUERY = "SELECT VERSION()";

    // using a max connection smaller than N_THREADS to test the reuse of connections
    private static final int MAX_CONNECTIONS = 50;

    public static void main(String[] args) throws InterruptedException {
        // NOTE: Run the benchmarking methods below ONE at a time for accurate results!
        // benchmarkConnectionPool is fast with 0 connection errors.
        // benchmarkConnectionWithoutPool is slow and raises a lot of connection errors.

        benchmarkConnectionPool();
//        benchmarkConnectionWithoutPool();
    }

    public static void benchmarkConnectionPool() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);
        List<Callable<Void>> tasks = new ArrayList<>();
        ConnectionPool pool = new ConnectionPool(CONNECTION_URL, USER, PASSWORD, MAX_CONNECTIONS);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < N_COUNTS; i++) {
            tasks.add(() -> {
                Connection conn = pool.getConnection();
                try (Statement st = conn.createStatement();
                     ResultSet rs = st.executeQuery(QUERY)) {
                    if (rs.next()) {
                        System.out.println(rs.getString(1));
                    }
                } catch (SQLException e) {
                    failureCount.incrementAndGet();
                    System.out.println(e);
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
        System.out.println("Total connection errors for " + N_COUNTS + " queries: " + failureCount.get());
        System.out.println("Total nanoseconds: " + timer.getNanosecondsElapsed());
    }

    public static void benchmarkConnectionWithoutPool() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);
        List<Callable<Void>> tasks = new ArrayList<>();
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < N_COUNTS; i++) {
            tasks.add(() -> {
                // Create database connection each time
                try (Connection conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
                     Statement st = conn.createStatement();
                     ResultSet rs = st.executeQuery(QUERY)) {
                    if (rs.next()) {
                        System.out.println(rs.getString(1));
                    }
                } catch (SQLException e) {
                    failureCount.incrementAndGet();
                    System.out.println(e);
                }
                return null;
            });
        }

        Timer timer = new Timer();
        timer.start();
        executor.invokeAll(tasks);
        timer.stop();

        executor.shutdown();
        System.out.println("Total connection errors for " + N_COUNTS + " queries: " + failureCount.get());
        System.out.println("Total nanoseconds: " + timer.getNanosecondsElapsed());
    }
}

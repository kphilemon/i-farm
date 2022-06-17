package com.example.ifarm.app;

import com.example.ifarm.counter.AtomicCounter;
import com.example.ifarm.counter.Counter;
import com.example.ifarm.dao.DAO;
import com.example.ifarm.database.ConnectionPool;
import com.example.ifarm.logger.Logger;
import com.example.ifarm.logger.QueueLogger;
import com.example.ifarm.util.Constants;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.*;

public class IFarmSequential implements IFarmService {
    private final DAO dao;
    private final Logger requestLogger;
    private final Logger activityLogger;
    private final Counter activityCounter;
    private final ExecutorService executor;

    public IFarmSequential(ConnectionPool connectionPool) throws IOException {
        dao = new DAO(connectionPool);
        requestLogger = new QueueLogger("logs/ifarm-requests.log");
        activityLogger = new QueueLogger("logs/ifarm-activities.log");
        activityCounter = new AtomicCounter();
        executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public LogActivityResponse logActivity(LogActivityRequest request) {
        LogActivityTask task = new LogActivityTask(request, dao, activityLogger, activityCounter);
        Future<LogActivityResponse> result = executor.submit(task);
        try {
            LogActivityResponse response = result.get();
            requestLogger.log(response + " " + request);
            return response;
        } catch (InterruptedException | ExecutionException e) {
            LogActivityResponse response = new LogActivityResponse(Constants.STATUS_INTERNAL_ERROR, "Internal server error: " + e.getMessage());
            requestLogger.log(response + " " + request);
            return response;
        }
    }

    @Override
    public void shutdown() throws IOException, InterruptedException, SQLException {
        // wait "forever" until all tasks are completed before shutting down other components
        // https://stackoverflow.com/questions/1250643/how-to-wait-for-all-threads-to-finish-using-executorservice/1250655#comment52439407_1250655
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        requestLogger.shutdown();
        activityLogger.shutdown();
    }
}

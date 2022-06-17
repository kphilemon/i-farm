package com.example.ifarm.app;

import java.io.IOException;
import java.sql.SQLException;

public interface IFarmService {
    // log activity
    LogActivityResponse logActivity(LogActivityRequest request);

    // shuts the service down
    void shutdown() throws IOException, InterruptedException, SQLException;
}

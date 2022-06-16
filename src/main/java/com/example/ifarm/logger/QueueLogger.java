package com.example.ifarm.logger;

import java.io.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// QueueLogger implements Logger with an internal thread pool that queues all requests for logging within its unbounded queue
// The thread pool is created with Executors.newSingleThreadExecutor and consists of only one single thread to prevent concurrent file writes.
//
// Note if this single thread terminates due to a failure during execution prior to shutdown, a new one will take its
// place if needed to execute subsequent tasks. (According to documentation)
public class QueueLogger implements Logger {
    private final Writer out;
    private final ExecutorService executor;

    public QueueLogger(String fileName) throws IOException {
        File logFile = new File(fileName);
        // mkdirs and createNewFile are both no-op if exists
        if (logFile.getParentFile() != null) {
            logFile.getParentFile().mkdirs();
        }
        logFile.createNewFile();

        out = new BufferedWriter(new FileWriter(logFile, true));
        executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void log(String msg) {
        executor.submit(() -> {
            try {
                out.write(format(msg));
            } catch (IOException e) {
                // log method should be fire and forget.
                // In case of IOException, just log the msg to stdout.
                System.out.println(format(msg));
            }
        });
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void shutdown() throws IOException, InterruptedException {
        // wait "forever" until all tasks are completed before closing the writer
        // https://stackoverflow.com/questions/1250643/how-to-wait-for-all-threads-to-finish-using-executorservice/1250655#comment52439407_1250655
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        out.close();
    }

    private String format(String msg) {
        return Timestamp.from(Instant.now()) + " " + msg + "\n";
    }
}

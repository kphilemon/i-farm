package com.example.ifarm.logger;

import java.io.*;
import java.sql.Timestamp;
import java.time.Instant;

// SynchronizedLogger implements Logger with synchronization
// Internally, it uses a BufferedWriter to write to the log file.
public class SynchronizedLogger implements Logger {
    private final Writer out;

    public SynchronizedLogger(String fileName) throws IOException {
        File logFile = new File(fileName);
        // mkdirs and createNewFile are both no-op if exists
        if (logFile.getParentFile() != null) {
            logFile.getParentFile().mkdirs();
        }
        logFile.createNewFile();

        out = new BufferedWriter(new FileWriter(logFile, true));
    }

    @Override
    synchronized public void log(String msg) {
        try {
            out.write(format(msg));
        } catch (IOException e) {
            // log method should be fire and forget.
            // In case of IOException, just log the msg to stdout.
            System.out.println(format(msg));
        }
    }

    @Override
    synchronized public void flush() throws IOException {
        out.flush();
    }

    @Override
    synchronized public void shutdown() throws IOException {
        out.close();
    }

    private String format(String msg) {
        return Timestamp.from(Instant.now()) + " " + msg + "\n";
    }
}

package com.example.ifarm.logger;

import java.io.IOException;

public interface Logger {
    // logs the msg to log file
    void log(String msg);

    // flush the internal buffer, if any
    void flush() throws IOException;

    // shuts the logger and frees all resources
    void shutdown() throws IOException, InterruptedException;
}


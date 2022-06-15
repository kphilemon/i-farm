package com.example.ifarm.timer;

import java.time.Duration;
import java.time.Instant;

public class Timer {
    private Instant start;
    private Instant end;

    public void start() {
        start = Instant.now();
    }

    public void stop() {
        end = Instant.now();
    }

    public long getNanosecondsElapsed() {
        return Duration.between(start, end).toNanos();
    }

    public long getMillisecondsElapsed() {
        return Duration.between(start, end).toMillis();
    }
}

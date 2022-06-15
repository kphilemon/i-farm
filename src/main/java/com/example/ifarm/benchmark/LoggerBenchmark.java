package com.example.ifarm.benchmark;

import com.example.ifarm.logger.Logger;
import com.example.ifarm.logger.QueueLogger;
import com.example.ifarm.logger.SynchronizedLogger;
import com.example.ifarm.timer.Timer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoggerBenchmark {
    private static final int N_THREADS = 100;
    private static final int N_COUNTS = 100000;
    private static final String LOG_FILE = "logs/benchmark.log";
    private static final String LOG_MSG = "this is a sample log message. this is a sample log message. this is a sample log message.";

    public static void main(String[] args) throws IOException, InterruptedException {
        // NOTE: Run the benchmarking methods below ONE at a time for accurate results!
        // On average, QueueLogger performs better than SynchronizedLogger
//        benchmark(new QueueLogger(LOG_FILE));
        benchmark(new SynchronizedLogger(LOG_FILE));
    }

    public static void benchmark(Logger logger) throws IOException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);
        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < N_COUNTS; i++) {
            tasks.add(() -> {
                logger.log(LOG_MSG);
                return null;
            });
        }

        Timer timer = new Timer();
        timer.start();
        executor.invokeAll(tasks);
        timer.stop();

        executor.shutdown();
        logger.shutdown();
        System.out.println("Total nanoseconds: " + timer.getNanosecondsElapsed());
    }
}
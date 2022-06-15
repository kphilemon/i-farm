package com.example.ifarm.benchmark;

import com.example.ifarm.counter.AtomicCounter;
import com.example.ifarm.counter.Counter;
import com.example.ifarm.counter.SynchronizedCounter;
import com.example.ifarm.timer.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CounterBenchmark {
    public static final int N_THREADS = 100;
    public static final int N_COUNTS = 100000;

    public static void main(String[] args) throws InterruptedException {
        // NOTE: Run the benchmarking methods below ONE at a time for accurate results!
        // On average, AtomicCounter performs better than SynchronizedCounter
//        benchmark(new AtomicCounter());
        benchmark(new SynchronizedCounter());
    }

    public static void benchmark(Counter counter) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);
        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < N_COUNTS; i++) {
            tasks.add(() -> {
                counter.incrementAndGet();
                return null;
            });
        }

        Timer timer = new Timer();
        timer.start();
        executor.invokeAll(tasks);
        timer.stop();

        executor.shutdown();
        System.out.println("Total nanoseconds: " + timer.getNanosecondsElapsed());
    }
}

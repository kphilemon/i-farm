package com.example.ifarm.counter;

// SynchronizedCounter implements Counter with synchronization
public class SynchronizedCounter implements Counter {
    private int c;

    public SynchronizedCounter() {
        c = 0;
    }

    public SynchronizedCounter(int initialValue) {
        c = initialValue;
    }

    @Override
    synchronized public int incrementAndGet() {
        c++;
        return c;
    }
}

package com.example.ifarm.counter;

import java.util.concurrent.atomic.AtomicInteger;

// AtomicCounter implements Counter with AtomicInteger
public class AtomicCounter implements Counter {
    private final AtomicInteger c;

    public AtomicCounter() {
        c = new AtomicInteger(0);
    }

    public AtomicCounter(int initialValue) {
        c = new AtomicInteger(initialValue);
    }

    public int incrementAndGet() {
        return c.incrementAndGet();
    }
}

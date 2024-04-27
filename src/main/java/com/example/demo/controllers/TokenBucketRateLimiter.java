package com.example.demo.controllers;
import org.springframework.stereotype.Component;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TokenBucketRateLimiter {
    private static final long BUCKET_CAPACITY = 10; // Maximum capacity of the bucket
    private static final long REFILL_RATE = 2; // Tokens refill rate per second
    private static final long REFILL_INTERVAL = 60000; // Refill interval in milliseconds
    private final AtomicLong tokens; // Current number of tokens in the bucket

    public TokenBucketRateLimiter() {
        this.tokens = new AtomicLong(BUCKET_CAPACITY); // Initially fill the bucket to its capacity
        startRefillTask();
    }

    public boolean allowRequest(long tokensRequired) {
        long currentTokens;
        long newTokens;
        do {
            currentTokens = tokens.get();
            if (currentTokens < tokensRequired) {
                return false; // Not enough tokens
            }
            newTokens = currentTokens - tokensRequired;
        } while (!tokens.compareAndSet(currentTokens, newTokens));
        return true; // Request allowed
    }
    
    private void startRefillTask() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::refillTokens, 0, REFILL_INTERVAL, TimeUnit.MILLISECONDS);
    }

    private void refillTokens() {
        long currentTokens = tokens.get();
        if(currentTokens>=BUCKET_CAPACITY){
            return;
        }
        tokens.set(currentTokens + REFILL_RATE);
    }
}

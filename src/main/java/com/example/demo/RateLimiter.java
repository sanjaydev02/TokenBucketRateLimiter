package com.example.demo;
import com.example.demo.controllers.TokenBucketRateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimiter {

    private final TokenBucketRateLimiter rateLimiter;

    public RateLimiter(TokenBucketRateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @GetMapping("/limiter")
    public ResponseEntity<String> testRateLimit() {
        if (rateLimiter.allowRequest(1)) {
            return ResponseEntity.status(HttpStatus.OK).body("Request Allowed");

        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }
    }
}

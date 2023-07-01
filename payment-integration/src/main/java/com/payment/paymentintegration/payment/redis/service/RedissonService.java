package com.payment.paymentintegration.payment.redis.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedissonService {

    private final RedissonClient redissonClient;

    public boolean isAccessLock(Long key) {
        RLock lock = redissonClient.getLock(key.toString());

        try {
            return lock.tryLock(5, 1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}

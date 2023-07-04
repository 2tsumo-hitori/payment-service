package com.payment.paymentintegration.payment.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;



@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AccessLockAspect {

    private final RedissonClient redissonClient;

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    @Around("@annotation(accessLock))")
    public Object execute(ProceedingJoinPoint joinPoint, AccessLock accessLock) throws Throwable {
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

        String key = REDISSON_LOCK_PREFIX +
                accessLock.prefix() +
                CustomSpringElParser.getDynamicValue(parameterNames, joinPoint.getArgs(), accessLock.key());

        RLock lock = redissonClient.getLock(key);

        try {
            boolean access = lock.tryLock(accessLock.waitTime(), accessLock.leaseTime(), accessLock.timeUnit());

            if (!access)
                return false;

            return joinPoint.proceed();
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            lock.unlock();
        }
    }
}

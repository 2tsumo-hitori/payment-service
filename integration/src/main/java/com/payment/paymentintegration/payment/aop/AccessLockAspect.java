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

    @Pointcut("@annotation(com.payment.paymentintegration.payment.aop.AccessLock))")
    private void enableStockAccessLock(){};

    @Around("enableStockAccessLock()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        AccessLock annotation = method.getAnnotation(AccessLock.class);

        String key = REDISSON_LOCK_PREFIX +
                annotation.prefix() +
                CustomSpringElParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), annotation.key());

        RLock lock = redissonClient.getLock(key);

        try {
            boolean accessLock = lock.tryLock(annotation.waitTime(), annotation.leaseTime(), annotation.timeUnit());

            if (!accessLock)
                return false;

            return joinPoint.proceed();
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            lock.unlock();
        }
    }
}

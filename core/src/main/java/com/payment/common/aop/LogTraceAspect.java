package com.payment.common.aop;

import com.payment.common.aop.logtrace.LogTrace;
import com.payment.common.aop.logtrace.TraceStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class LogTraceAspect {

    private final LogTrace logTrace;

    @Pointcut("@within(com.payment.common.aop.pointcut.Logger))")
    private void enableLogger(){};

    @Around("enableLogger()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {

        TraceStatus status = null;
        try {
            status = logTrace.start(joinPoint.getSignature().toShortString(), getIpAddr(joinPoint).orElse("내부 서비스 사용"));

            Object proceed = joinPoint.proceed();

            logTrace.end(status);

            return proceed;
        } catch (Throwable e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    private Optional<String> getIpAddr(ProceedingJoinPoint joinPoint) {

        return Arrays.stream(joinPoint.getArgs())
                .filter(arg -> arg instanceof HttpServletRequest)
                .map(arg -> ((HttpServletRequest) arg).getRemoteAddr())
                .findAny();
    }
}

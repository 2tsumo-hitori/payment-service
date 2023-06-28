package com.payment.api.aop;

import com.payment.api.aop.logtrace.TraceStatus;
import com.payment.api.aop.logtrace.LogTrace;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Optional;

@Aspect
@Slf4j
@RequiredArgsConstructor
public class LogTraceAspect {

    private final LogTrace logTrace;

    @Around("execution(* com.payment.api.controller..*(..)) || execution(* com.payment.api.service..*(..)) || execution(* com.payment.common.repository..*(..))")
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
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                return Optional.of(((HttpServletRequest) arg).getRemoteAddr());
            }
        }
        return Optional.empty();
    }
}

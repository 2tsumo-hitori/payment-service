package payment.example.common.support.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import payment.example.common.support.logtrace.LogTrace;
import payment.example.common.support.logtrace.TraceStatus;

import java.util.Optional;

@Aspect
@RequiredArgsConstructor
public class LogTraceAspect {

    private final LogTrace logTrace;
    @Around("execution(* payment.example.app..*(..))")
        public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {

        TraceStatus status = logTrace.start(joinPoint.getSignature().toShortString(), getIpAddr(joinPoint).orElse("내부 서비스 사용"));

        Object proceed = joinPoint.proceed();

        logTrace.end(status);

        return proceed;
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

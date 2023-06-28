package com.payment.api.aop.logtrace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ThreadLocalLogTrace implements LogTrace {

    private ThreadLocal<LogId> traceIdHolder = new ThreadLocal<>();
    @Override
    public TraceStatus start(String message, String ipAddr) {
        LogId logId = traceIdHolder.get();

        if (logId == null) {
            logId = new LogId(ipAddr);
            traceIdHolder.set(logId);
        } else {
            traceIdHolder.set(logId.createNextLevel(1));
        }

        log.info("[{}], [{}], [{}], [{}]", logId.getId(), logId.getIpAddr(), "REQUEST", message);

        return new TraceStatus(logId, System.currentTimeMillis(), message);
    }

    @Override
    public void end(TraceStatus traceStatus) {
        LogId logId = traceIdHolder.get();

        if (logId.getLevel() == 0 ) {
            traceIdHolder.remove();
        } else {
            traceIdHolder.set(logId.createNextLevel(-1));
        }

        long endTime = System.currentTimeMillis();

        log.info("[{}], [{}], [{}], [{}], [{}ms]", traceStatus.getLogId().getId(), logId.getIpAddr(), "RESPONSE", traceStatus.getMessage(), endTime - traceStatus.getStartTime());
    }

    @Override
    public void exception(TraceStatus traceStatus, Throwable e) {
        LogId logId = traceIdHolder.get();

        if (logId.getLevel() == 0 ) {
            traceIdHolder.remove();
        } else {
            traceIdHolder.set(logId.createNextLevel(-1));
        }

        long endTime = System.currentTimeMillis();

        log.info("[{}], [{}], [{}], [{}], [{}ms] ex={}", traceStatus.getLogId().getId(), logId.getIpAddr(), "RESPONSE", traceStatus.getMessage(), endTime - traceStatus.getStartTime(), e.toString());

    }
}

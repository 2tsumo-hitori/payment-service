package com.example.common.support.aop.logtrace;

public interface LogTrace {
    TraceStatus start(String message, String ipAddr);

    void end(TraceStatus traceStatus);
}

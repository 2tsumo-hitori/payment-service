package com.payment.common.aop.logtrace;

public interface LogTrace {
    TraceStatus start(String message, String ipAddr);
    void end(TraceStatus traceStatus);
    void exception(TraceStatus traceStatus, Throwable e);
}

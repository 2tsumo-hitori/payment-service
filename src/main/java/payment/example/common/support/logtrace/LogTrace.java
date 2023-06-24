package payment.example.common.support.logtrace;

public interface LogTrace {
    TraceStatus start(String message, String ipAddr);

    void end(TraceStatus traceStatus);
}

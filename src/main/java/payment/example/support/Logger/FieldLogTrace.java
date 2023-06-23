package payment.example.support.Logger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldLogTrace implements LogTrace {

    private ThreadLocal<LogStatus> logStatusThreadLocal = new ThreadLocal<>();

    @Override
    public long start(LogStatus status) {
        syncLogStatus(status.getTransaction(), status.getIpAddress());
        long startTime = System.currentTimeMillis();
        log.info("[{}] [{}] [{}] [{}]", logStatusThreadLocal.get().getId(), logStatusThreadLocal.get().getTime(), logStatusThreadLocal.get().getTransaction(), logStatusThreadLocal.get().getIpAddress());

        return startTime;
    }

    @Override
    public void end(LogStatus status) {

    }

    @Override
    public void exception() {

    }

    private void syncLogStatus(HttpTransaction transaction, String ipAddress) {
        LogStatus logStatus = logStatusThreadLocal.get();

        if (logStatus == null) {
            logStatusThreadLocal.set(LogStatus.create(transaction, ipAddress));
        } else {
            logStatusThreadLocal.set(logStatus.callTransAction());
        }
    }
}

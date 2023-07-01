package com.payment.common.aop.logtrace;

import lombok.Getter;

@Getter
public class TraceStatus {

    private LogId logId;

    private Long startTime;

    private String message;

    public TraceStatus(LogId logId, Long startTime, String message) {
        this.logId = logId;
        this.startTime = startTime;
        this.message = message;
    }
}

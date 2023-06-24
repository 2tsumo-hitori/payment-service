package payment.example.common.support.logtrace;

import lombok.Getter;

import java.util.UUID;

@Getter
public class LogId {
    private String id;

    private String ipAddr;

    private int level;

    public LogId(String ipAddr) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.ipAddr = ipAddr;
        this.level = 0;
    }

    private LogId callExistLogId(int levelControl) {
        this.level = level + levelControl;

        return this;
    }

    public LogId createNextLevel(int levelControl) {
        return callExistLogId(levelControl);
    }
}

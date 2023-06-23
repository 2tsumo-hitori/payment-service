package payment.example.support.Logger;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Getter
public class LogStatus {

    private String id;

    private long time = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();

    private HttpTransaction transaction;

    private String ipAddress;

    private LogStatus(HttpTransaction transaction, String ipAddress) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.transaction = transaction;
        this.ipAddress = ipAddress;
    }

    public static LogStatus create(HttpTransaction transaction, String ipAddress) {
        return new LogStatus(transaction, ipAddress);
    }

    public LogStatus callTransAction() {
        return this;
    }
}

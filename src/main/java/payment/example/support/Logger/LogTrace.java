package payment.example.support.Logger;

public interface LogTrace {
    long start(LogStatus status);

    void end(LogStatus status);

    void exception();
}

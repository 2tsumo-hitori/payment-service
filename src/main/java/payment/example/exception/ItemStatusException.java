package payment.example.exception;

public class ItemStatusException extends RuntimeException {
    public ItemStatusException() {
    }

    public ItemStatusException(String message) {
        super(message);
    }

    public ItemStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemStatusException(Throwable cause) {
        super(cause);
    }
}

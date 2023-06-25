package payment.example.app.controller.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class PaymentResponse<T> {
    private int status;
    private String message;
    private T data;

    public PaymentResponse(int status, T data) {
        this.status = status;
        this.message = "정상 호출";
        this.data = data;
    }

    public PaymentResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}

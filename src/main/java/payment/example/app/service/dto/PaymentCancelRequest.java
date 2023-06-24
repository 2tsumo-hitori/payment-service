package payment.example.app.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PaymentCancelRequest {
    private String reason;
    private String impUid;
    private String amount;
}
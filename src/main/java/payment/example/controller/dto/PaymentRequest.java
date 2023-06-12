package payment.example.controller.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private int amount;
    private String impUid;
    private String itemName;
    private Long memberId;
}

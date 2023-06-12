package payment.example.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentRequest {
    private int amount;
    private String impUid;
    private String itemName;
    private Long memberId;
}

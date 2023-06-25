package payment.example.app.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetOrderDto {
    private Long orderId;

    private Long memberId;

    private String memberName;

    private Long itemId;

    private String itemName;
}

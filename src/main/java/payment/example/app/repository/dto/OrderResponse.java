package payment.example.app.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import payment.example.common.domain.OrderStatus;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;

    private OrderStatus status;

    private Long memberId;

    private String memberName;

    private Long itemId;

    private String itemName;
}

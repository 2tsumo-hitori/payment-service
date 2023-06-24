package payment.example.app.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import payment.example.common.domain.OrderStatus;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;

    private OrderStatus status;

    private Long memberId;

    private String memberName;

    private Long itemId;

    private String itemName;

    private Long itemStock;
}

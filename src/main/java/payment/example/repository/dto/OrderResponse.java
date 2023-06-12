package payment.example.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import payment.example.domain.OrderStatus;

@Data
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

package payment.example.app.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import payment.example.app.repository.dto.GetOrderDto;

@Data
public class OrderResponse {

    private Long orderId;

    private GetOrderResponseMemberDto member;

    private GetOrderResponseItemDto item;

    private OrderResponse(GetOrderDto dto) {
        this.orderId = dto.getOrderId();
        this.member = new GetOrderResponseMemberDto(dto.getMemberId(), dto.getMemberName());
        this.item = new GetOrderResponseItemDto(dto.getItemId(), dto.getItemName());
    }

    public static OrderResponse create(GetOrderDto dto) {
        return new OrderResponse(dto);
    }


    @Data
    @AllArgsConstructor
    class GetOrderResponseMemberDto {
        private Long memberId;

        private String memberName;
    }

    @Data
    @AllArgsConstructor
    class GetOrderResponseItemDto {
        private Long itemId;

        private String itemName;
    }
}

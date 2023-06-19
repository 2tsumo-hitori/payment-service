package payment.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payment.example.domain.Item;
import payment.example.domain.Member;
import payment.example.domain.Order;
import payment.example.domain.OrderStatus;
import payment.example.exception.OutOfStockException;
import payment.example.repository.MemberRepository;
import payment.example.repository.OrderRepository;
import payment.example.repository.dto.OrderResponse;

import static payment.example.validate.PreCondition.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final static int MINIMUM_SIZE = 0;
    @Transactional(noRollbackFor = OutOfStockException.class)
    public OrderResponse makeOrder(Item item, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();

        Order order = getOrder(item, member);

        return orderRepository.findOrder(order.getId());
    }

    private Order getOrder(Item item, Member member) {
        try {
            itemStockValidate(item.getStock() > MINIMUM_SIZE);

            item.decreaseItemStock();

            return orderRepository.save(Order
                    .builder()
                    .member(member)
                    .item(item)
                    .build());
        } catch(OutOfStockException e) {
            return orderRepository.save(Order
                    .builder()
                    .member(member)
                    .item(item)
                    .status(OrderStatus.주문_대기)
                    .build());
        }
    }
}

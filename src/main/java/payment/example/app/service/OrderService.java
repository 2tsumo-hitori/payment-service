package payment.example.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payment.example.common.domain.Item;
import payment.example.common.domain.Member;
import payment.example.common.domain.Order;
import payment.example.app.repository.MemberRepository;
import payment.example.app.repository.dto.OrderResponse;
import payment.example.common.support.validate.PreCondition;
import payment.example.app.repository.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final static int MINIMUM_SIZE = 0;
    @Transactional
    public OrderResponse makeOrder(Item item, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();

        PreCondition.itemStockValidate(item.getStock() > MINIMUM_SIZE);

        item.decreaseItemStock();

        Order order = orderRepository.save(Order
                .builder()
                .member(member)
                .item(item)
                .build());

        return orderRepository.findOrder(order.getId());
    }
}

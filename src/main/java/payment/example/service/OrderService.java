package payment.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payment.example.domain.Item;
import payment.example.domain.Member;
import payment.example.domain.Order;
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
    @Transactional
    public OrderResponse makeOrder(Item item, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();

        itemStockValidate(item.getStock() > MINIMUM_SIZE);

        item.decreaseItemStock();

        Order order = orderRepository.save(Order
                .builder()
                .member(member)
                .item(item)
                .build());

        return orderRepository.findOrder(order.getId());
    }
}

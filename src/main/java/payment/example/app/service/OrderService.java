package payment.example.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payment.example.app.repository.MemberRepository;
import payment.example.app.repository.OrderRepository;
import payment.example.app.repository.dto.GetOrderDto;
import payment.example.common.domain.Item;
import payment.example.common.domain.Member;
import payment.example.common.domain.Order;



@Service
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;

    private final OrderRepository orderRepository;

    private final StockService stockService;
    @Transactional
    public GetOrderDto makeOrder(Item item, Long memberId, long quantity) {
        Member member = memberRepository.findById(memberId).orElseThrow();

        stockService.decrease(item.getStock().getId(), quantity);

        Order order = orderRepository.save(Order
                .builder()
                .member(member)
                .item(item)
                .build());

        return orderRepository.findOrder(order.getId());
    }
}

package com.payment.api.service;

import com.payment.common.repository.MemberRepository;
import com.payment.common.repository.OrderRepository;
import com.payment.common.repository.dto.GetOrderDto;
import com.payment.common.domain.Item;
import com.payment.common.domain.Member;
import com.payment.common.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

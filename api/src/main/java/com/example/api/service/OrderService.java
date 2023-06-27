package com.example.api.service;

import com.example.common.repository.MemberRepository;
import com.example.common.repository.OrderRepository;
import com.example.common.repository.dto.GetOrderDto;
import com.example.common.domain.Item;
import com.example.common.domain.Member;
import com.example.common.domain.Order;
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

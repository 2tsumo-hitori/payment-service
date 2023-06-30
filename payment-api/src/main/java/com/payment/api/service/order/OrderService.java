package com.payment.api.service.order;

import com.payment.api.service.stock.StockService;
import com.payment.common.aop.pointcut.Logger;
import com.payment.common.repository.MemberRepository;
import com.payment.common.repository.OrderRepository;
import com.payment.common.repository.dto.GetOrderDto;
import com.payment.common.domain.Item;
import com.payment.common.domain.Member;
import com.payment.common.domain.Order;
import com.payment.paymentintegration.payment.kafka.producer.CreateOrderProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Logger
public class OrderService {
    private final MemberRepository memberRepository;

    private final OrderRepository orderRepository;

    private final StockService stockService;

    private final CreateOrderProducer createOrderProducer;

    @Transactional
    public GetOrderDto makeOrder(Item item, Long memberId, long quantity) {
        Member member = memberRepository.findById(memberId).orElseThrow();

        stockService.decrease(item.getStock(), quantity);

        createOrderProducer.create(memberId, item.getId());

//        Order order = orderRepository.save(Order
//                .builder()
//                .member(member)
//                .item(item)
//                .build());

        return null;
    }
}

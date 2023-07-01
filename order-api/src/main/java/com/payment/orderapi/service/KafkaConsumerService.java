package com.payment.orderapi.service;


import com.payment.common.domain.Item;
import com.payment.common.domain.Member;
import com.payment.common.domain.Order;
import com.payment.common.repository.ItemRepository;
import com.payment.common.repository.MemberRepository;
import com.payment.common.repository.OrderRepository;
import com.payment.paymentintegration.payment.kafka.dto.OrderMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumerService implements MessageQueueService<OrderMessage> {

    private final OrderRepository orderRepository;

    private final MemberRepository memberRepository;

    private final ItemRepository itemRepository;
    @Override
    @KafkaListener(topics = "order_topic", groupId = "order_group")
    public void orderListener(OrderMessage listener) {
        Item item = itemRepository.findById(listener.getItemId()).orElseThrow();
        Member member = memberRepository.findById(listener.getMemberId()).orElseThrow();

        orderRepository.save(Order
                .builder()
                .member(member)
                .item(item)
                .build());
    }
}

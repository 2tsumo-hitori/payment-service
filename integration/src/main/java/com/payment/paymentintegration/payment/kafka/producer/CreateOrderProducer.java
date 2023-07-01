package com.payment.paymentintegration.payment.kafka.producer;

import com.payment.paymentintegration.payment.kafka.dto.OrderMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOrderProducer {
    private final KafkaTemplate<String, OrderMessage> kafkaTemplate;

    public void create(Long userId, Long itemId) {
        kafkaTemplate.send("order_topic", new OrderMessage(userId, itemId));
    }
}

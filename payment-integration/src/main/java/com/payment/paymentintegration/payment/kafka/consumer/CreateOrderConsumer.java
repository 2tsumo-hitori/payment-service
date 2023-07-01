package com.payment.paymentintegration.payment.kafka.consumer;

import com.payment.paymentintegration.payment.kafka.dto.OrderMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderConsumer {
    @KafkaListener(topics = "order_topic", groupId = "order_group")
    public void listener(OrderMessage message) {

    }
}

package com.payment.orderapi.service;

import com.payment.common.aop.pointcut.Logger;
import com.payment.paymentintegration.payment.kafka.dto.OrderMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Logger
public class OrderService {

    private final MessageQueueService<OrderMessage> messageQueueService;

    public void orderCreate(OrderMessage listener) {
        messageQueueService.orderListener(listener);
    }
}

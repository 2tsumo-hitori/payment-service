package com.payment.api.service.stock;


import com.payment.common.aop.pointcut.Logger;
import com.payment.common.domain.Item;
import com.payment.paymentintegration.payment.aop.AccessLock;
import com.payment.paymentintegration.payment.kafka.producer.CreateOrderProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
@Logger
public class RedisAsyncOrderService implements AsyncOrderService {

    private final CreateOrderProducer createOrderProducer;

    private static final String PAYMENT_SUCCESS = "결제 완료";

    @Override
    @AccessLock(key = "#item.getStock().getId().toString()", prefix = "item")
    public String decrease(Item item, Long memberId, Long quantity) {
        item.getStock().decrease(quantity);

        createOrderProducer.create(memberId, item.getId());

        return PAYMENT_SUCCESS;
    }
}

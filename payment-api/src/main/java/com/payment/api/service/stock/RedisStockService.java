package com.payment.api.service.stock;


import com.payment.api.controller.dto.PaymentRequest;
import com.payment.common.aop.pointcut.Logger;
import com.payment.common.domain.Item;
import com.payment.common.domain.Stock;
import com.payment.paymentintegration.payment.kafka.producer.CreateOrderProducer;
import com.payment.paymentintegration.payment.redis.service.RedissonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Logger
public class RedisStockService implements StockService {

    private final RedissonService redissonService;

    private final CreateOrderProducer createOrderProducer;

    private static final String PAYMENT_SUCCESS = "결제 완료";

    private static final String PAYMENT_FAILURE = "결제 실패";

    @Override
    public String decrease(Item item, Long memberId, Long quantity) {
        if(redissonService.isAccessLock(item.getStock().getId())) {
            item.getStock().decrease(quantity);

            createOrderProducer.create(memberId, quantity);

            return PAYMENT_SUCCESS;
        }

        return PAYMENT_FAILURE;
    }
}
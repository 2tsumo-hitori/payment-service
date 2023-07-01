package com.payment.api.service.stock;


import com.payment.common.aop.pointcut.Logger;
import com.payment.common.domain.Stock;
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

    @Override
    public void decrease(Stock stock, Long quantity) {
        if(redissonService.isAccessLock(stock.getId())) {
            stock.decrease(quantity);
        }
    }
}

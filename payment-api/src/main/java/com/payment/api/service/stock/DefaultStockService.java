package com.payment.api.service.stock;

import com.payment.common.repository.StockRepository;
import com.payment.common.domain.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class DefaultStockService {

    private final StockRepository stockRepository;

    @Transactional
    public void decrease(final Long id, final Long quantity) {
        Stock stock = stockRepository.findByWithPessimisticLock(id);
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
    }
}

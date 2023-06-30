package com.payment.api.service.stock;

import com.payment.common.domain.Stock;

public interface StockService {
    void decrease(Stock stock, Long quantity);
}

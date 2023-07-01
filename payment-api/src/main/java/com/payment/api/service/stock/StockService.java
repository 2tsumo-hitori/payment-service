package com.payment.api.service.stock;

import com.payment.common.domain.Item;

public interface StockService {
    String decrease(Item item, Long memberId, Long quantity);
}

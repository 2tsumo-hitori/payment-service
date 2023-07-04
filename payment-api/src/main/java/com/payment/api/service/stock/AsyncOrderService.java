package com.payment.api.service.stock;

import com.payment.common.domain.Item;

public interface AsyncOrderService {
    String order(Item item, Long memberId, Long quantity);
}

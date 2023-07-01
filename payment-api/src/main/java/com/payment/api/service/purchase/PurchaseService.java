package com.payment.api.service.purchase;

import com.payment.api.service.stock.StockService;
import com.payment.common.aop.pointcut.Logger;
import com.payment.paymentintegration.payment.iamport.PaymentTemplate;
import com.payment.paymentintegration.payment.iamport.ValidatePayment;
import com.payment.api.controller.dto.PaymentRequest;
import com.payment.common.repository.ItemRepository;
import com.payment.common.domain.Item;
import com.payment.common.exception.ItemStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.payment.common.support.validate.PreCondition.itemPriceValidate;


@Service
@RequiredArgsConstructor
@Logger
public class PurchaseService {

    private final ItemRepository itemRepository;

    private final PaymentTemplate paymentTemplate;

    private final StockService stockService;

    @Transactional
    public String purchase(PaymentRequest request) {
        Item item = itemRepository.findByName(request.getItemName()).orElseThrow(ItemStatusException::new);

        itemPriceValidate(item.getQuantity() > request.getQuantity());

        return paymentTemplate.execute(
                new ValidatePayment(item, request.getImpUid(), request.getAmount()),
                () -> stockService.decrease(item, request.getMemberId(), request.getQuantity()));
    }
}

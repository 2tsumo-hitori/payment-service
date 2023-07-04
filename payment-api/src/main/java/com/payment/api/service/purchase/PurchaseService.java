package com.payment.api.service.purchase;

import com.payment.api.service.stock.AsyncOrderService;
import com.payment.common.aop.pointcut.LogTracer;
import com.payment.paymentintegration.payment.iamport.callback.PaymentTemplate;
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
@LogTracer
public class PurchaseService {

    private final ItemRepository itemRepository;

    private final PaymentTemplate paymentTemplate;

    private final AsyncOrderService asyncOrderService;

    @Transactional
    public String purchase(PaymentRequest request) {
        Item item = itemRepository.findByName(request.getItemName()).orElseThrow(ItemStatusException::new);

        itemPriceValidate(item.getQuantity() > request.getQuantity());

        return paymentTemplate.purchase(
                new ValidatePayment(item, request.getImpUid(), request.getAmount()),
                () -> asyncOrderService.order(item, request.getMemberId(), request.getQuantity()));
    }
}

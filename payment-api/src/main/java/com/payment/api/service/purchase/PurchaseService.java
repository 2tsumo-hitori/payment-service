package com.payment.api.service.purchase;

import com.payment.api.service.order.OrderService;
import com.payment.common.aop.pointcut.Logger;
import com.payment.paymentintegration.payment.iamport.PaymentTemplate;
import com.payment.paymentintegration.payment.iamport.ValidatePayment;
import com.payment.api.controller.dto.PaymentRequest;
import com.payment.common.repository.ItemRepository;
import com.payment.common.repository.dto.GetOrderDto;
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

    private final OrderService orderService;

    private final PaymentTemplate paymentTemplate;

    @Transactional
    public GetOrderDto purchase(PaymentRequest request) {
        Item item = itemRepository.findByName(request.getItemName()).orElseThrow(PurchaseService::getExceptionMessage);

        itemPriceValidate(item.getQuantity() > request.getQuantity());

        return paymentTemplate.execute(
                new ValidatePayment(item, request.getImpUid(), request.getAmount()),
                () -> orderService.makeOrder(item, request.getMemberId(), request.getQuantity()));
    }

    private static ItemStatusException getExceptionMessage() {
        return new ItemStatusException("존재하지 않는 상품입니다.");
    }
}

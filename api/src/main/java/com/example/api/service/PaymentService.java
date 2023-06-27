package com.example.api.service;

import com.example.api.controller.dto.PaymentRequest;
import com.example.common.repository.ItemRepository;
import com.example.common.repository.dto.GetOrderDto;
import com.example.api.service.appservice.callback.PaymentTemplate;
import com.example.api.service.appservice.callback.ValidatePayment;
import com.example.common.domain.Item;
import com.example.common.exception.ItemStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.common.support.validate.PreCondition.itemPriceValidate;


@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ItemRepository itemRepository;

    private final OrderService orderService;

    private final PaymentTemplate paymentTemplate;

    @Transactional
    public GetOrderDto purchase(PaymentRequest request) {
        Item item = itemRepository.findByName(request.getItemName()).orElseThrow(PaymentService::getExceptionMessage);

        itemPriceValidate(item.getQuantity() > request.getQuantity());

        return paymentTemplate.execute(
                new ValidatePayment(item, request),
                () -> orderService.makeOrder(item, request.getMemberId(), request.getQuantity()));
    }

    private static ItemStatusException getExceptionMessage() {
        return new ItemStatusException("존재하지 않는 상품입니다.");
    }
}

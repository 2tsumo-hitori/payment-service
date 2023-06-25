package payment.example.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payment.example.app.controller.dto.PaymentRequest;
import payment.example.app.repository.ItemRepository;
import payment.example.app.repository.dto.GetOrderDto;
import payment.example.app.service.appservice.callback.PaymentTemplate;
import payment.example.app.service.appservice.callback.ValidatePayment;
import payment.example.common.domain.Item;
import payment.example.common.exception.ItemStatusException;

import static payment.example.common.support.validate.PreCondition.*;


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

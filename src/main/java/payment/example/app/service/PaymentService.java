package payment.example.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payment.example.app.controller.dto.PaymentRequest;
import payment.example.app.repository.ItemRepository;
import payment.example.app.repository.dto.GetOrderDto;
import payment.example.app.service.appservice.callback.IamPortTemplate;
import payment.example.app.service.appservice.callback.ValidatePayment;
import payment.example.common.domain.Item;
import payment.example.common.exception.ItemStatusException;

import static payment.example.common.support.validate.PreCondition.*;


@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ItemRepository itemRepository;

    private final OrderService orderService;

    private final IamPortTemplate iamPortTemplate;

    @Transactional
    public GetOrderDto purchase(PaymentRequest request) {
        Item item = itemRepository.findByName(request.getItemName()).orElseThrow(ItemStatusException::new);

        itemPriceValidate(item.getQuantity() > request.getQuantity());

        return iamPortTemplate.execute(
                new ValidatePayment(item, request.getImpUid()),
                () -> orderService.makeOrder(item, request.getMemberId(), request.getQuantity()));
    }
}

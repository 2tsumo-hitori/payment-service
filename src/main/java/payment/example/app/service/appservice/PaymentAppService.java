package payment.example.app.service.appservice;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payment.example.app.controller.dto.PaymentRequest;
import payment.example.app.repository.ItemRepository;
import payment.example.app.repository.dto.OrderResponse;
import payment.example.app.service.OrderService;
import payment.example.common.domain.Item;


import java.io.IOException;
import java.util.Optional;

import static payment.example.common.support.validate.PreCondition.*;


@Service
@RequiredArgsConstructor
public class PaymentAppService {

    private final IamportClient iamportClient;

    private final ItemRepository itemRepository;

    private final OrderService orderService;

    @Transactional
    public OrderResponse paymentValidate(PaymentRequest request) throws IamportResponseException, IOException {
        Payment payment = iamportClient.paymentByImpUid(request.getImpUid()).getResponse();

        Optional<Item> item = itemRepository.findByName(request.getItemName());

        validation(payment, item, request.getQuantity());

        return orderService.makeOrder(item.get(), request.getMemberId(), request.getQuantity());
    }

    private static void validation(Payment payment, Optional<Item> item, long quantity) {
        itemExistValidate(item.isPresent());
        itemNameValidate(item.get().getName().equals(payment.getName()));
        itemPriceValidate(item.get().getPrice() == payment.getAmount().intValue());
        itemPriceValidate(item.get().getQuantity() > quantity);
    }
}

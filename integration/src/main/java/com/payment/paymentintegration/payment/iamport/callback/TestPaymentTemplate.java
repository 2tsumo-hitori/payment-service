package com.payment.paymentintegration.payment.iamport.callback;

import com.payment.common.domain.Item;
import com.payment.common.repository.ItemRepository;
import com.payment.paymentintegration.payment.iamport.ValidatePayment;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.payment.common.support.validate.PreCondition.itemPriceValidate;

@Component
@Profile("test")
@Transactional
@RequiredArgsConstructor
public class TestPaymentTemplate implements PaymentTemplate {

    private final ItemRepository itemRepository;
    @Override
    public <T> T purchase(ValidatePayment validatePayment, IamPortCallBack<T> T) {
        Item item = itemRepository.findByName(validatePayment.getItemName()).orElseThrow();

        itemPriceValidate(validatePayment.amount() == item.getPrice());

        return T.call();
    }
}

package com.payment.paymentintegration.payment.iamport;

import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.payment.common.support.validate.PreCondition.itemNameValidate;
import static com.payment.common.support.validate.PreCondition.itemPriceValidate;

@Component
@RequiredArgsConstructor
@Transactional
public class IamPortTemplate implements PaymentTemplate {

    private final PaymentClient paymentClient;

    @Override
    public <T> T execute(ValidatePayment validatePayment, IamPortCallBack<T> T) {
        Payment payment = (Payment) paymentClient.validate(validatePayment.impUid());

        itemNameValidate(validatePayment.getItemName().equals(payment.getName()));
        itemPriceValidate(validatePayment.amount() == payment.getAmount().intValue());

        return T.call();
    }
}
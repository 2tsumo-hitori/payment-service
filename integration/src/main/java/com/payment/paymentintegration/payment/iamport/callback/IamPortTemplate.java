package com.payment.paymentintegration.payment.iamport.callback;

import com.payment.common.aop.pointcut.LogTracer;
import com.payment.paymentintegration.payment.iamport.PaymentClient;
import com.payment.paymentintegration.payment.iamport.ValidatePayment;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.payment.common.support.validate.PreCondition.itemNameValidate;
import static com.payment.common.support.validate.PreCondition.itemPriceValidate;

@Component
@Profile("local")
@RequiredArgsConstructor
@Transactional
@LogTracer
public class IamPortTemplate implements PaymentTemplate {

    private final PaymentClient paymentClient;

    @Override
    public <T> T purchase(ValidatePayment validatePayment, IamPortCallBack<T> T) {
        Payment payment = (Payment) paymentClient.validate(validatePayment.impUid());

        itemNameValidate(validatePayment.getItemName().equals(payment.getName()));
        itemPriceValidate(validatePayment.amount() == payment.getAmount().intValue());

        return T.call();
    }
}

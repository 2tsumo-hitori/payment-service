package com.payment.paymentintegration.payment.iamport;

import com.payment.common.aop.pointcut.Logger;
import com.payment.paymentintegration.payment.exception.IamPortException;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Logger
public class DefaultPaymentClient implements PaymentClient {

    private final IamportClient iamportClient;
    @Override
    public Payment validate(String id) {
        try {
            return iamportClient.paymentByImpUid(id).getResponse();
        } catch (IamportResponseException e) {
            throw new IamPortException.IamPortRunTimeException(e);
        } catch (IOException e) {
            throw new IamPortException.IamPortRunTimeIoException(e);
        }
    }
}

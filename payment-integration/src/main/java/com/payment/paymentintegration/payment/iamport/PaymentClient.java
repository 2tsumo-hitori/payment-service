package com.payment.paymentintegration.payment.iamport;

public interface PaymentClient<T> {
    T validate(String id);
}

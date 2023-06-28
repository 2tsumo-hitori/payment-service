package com.payment.paymentintegration.paymentmodule;

public interface PaymentClient<T> {
    T validate(String id);
}

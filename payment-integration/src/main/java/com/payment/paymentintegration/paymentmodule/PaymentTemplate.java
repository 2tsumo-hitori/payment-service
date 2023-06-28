package com.payment.paymentintegration.paymentmodule;

public interface PaymentTemplate {
    <T> T execute(ValidatePayment validatePayment, IamPortCallBack<T> T);
}

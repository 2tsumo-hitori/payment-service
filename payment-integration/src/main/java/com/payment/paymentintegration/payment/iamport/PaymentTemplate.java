package com.payment.paymentintegration.payment.iamport;

public interface PaymentTemplate {
    <T> T execute(ValidatePayment validatePayment, IamPortCallBack<T> T);
}

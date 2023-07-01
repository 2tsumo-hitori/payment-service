package com.payment.paymentintegration.payment.iamport;

public interface PaymentTemplate {
    <T> T purchase(ValidatePayment validatePayment, IamPortCallBack<T> T);
}

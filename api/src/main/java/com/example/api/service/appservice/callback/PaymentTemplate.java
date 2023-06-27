package com.example.api.service.appservice.callback;

public interface PaymentTemplate {
    <T> T execute(ValidatePayment validatePayment, IamPortCallBack<T> T);
}

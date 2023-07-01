package com.payment.paymentintegration.payment.iamport.callback;


@FunctionalInterface
public interface IamPortCallBack<T> {
    T call();
}

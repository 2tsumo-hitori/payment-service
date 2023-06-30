package com.payment.paymentintegration.payment.iamport;


@FunctionalInterface
public interface IamPortCallBack<T> {
    T call();
}

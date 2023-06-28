package com.payment.paymentintegration.paymentmodule;


@FunctionalInterface
public interface IamPortCallBack<T> {
    T call();
}

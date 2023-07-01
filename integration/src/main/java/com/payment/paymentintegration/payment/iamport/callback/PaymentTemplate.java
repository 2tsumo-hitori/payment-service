package com.payment.paymentintegration.payment.iamport.callback;

import com.payment.paymentintegration.payment.iamport.ValidatePayment;
import com.payment.paymentintegration.payment.iamport.callback.IamPortCallBack;

public interface PaymentTemplate {
    <T> T purchase(ValidatePayment validatePayment, IamPortCallBack<T> T);
}

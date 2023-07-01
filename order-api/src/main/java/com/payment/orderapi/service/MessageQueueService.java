package com.payment.orderapi.service;

public interface MessageQueueService<T> {
    void orderListener(T t);
}

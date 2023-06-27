package com.example.api.service.appservice.callback;


import com.example.api.controller.dto.PaymentRequest;
import com.example.common.domain.Item;

public record ValidatePayment(
        Item serverItem, PaymentRequest request
) {

    public String getItemName() {
        return serverItem.getName();
    }

    public int getItemPrice() {
        return serverItem.getPrice();
    }
}

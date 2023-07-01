package com.payment.paymentintegration.payment.iamport;

import com.payment.common.domain.Item;

public record ValidatePayment(
        Item serverItem, String impUid, int amount
) {

    public String getItemName() {
        return serverItem.getName();
    }
}

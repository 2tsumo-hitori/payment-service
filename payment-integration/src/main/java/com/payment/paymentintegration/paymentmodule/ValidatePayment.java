package com.payment.paymentintegration.paymentmodule;

import com.payment.common.domain.Item;

public record ValidatePayment(
        Item serverItem, String impUid, int amount
) {

    public String getItemName() {
        return serverItem.getName();
    }
}

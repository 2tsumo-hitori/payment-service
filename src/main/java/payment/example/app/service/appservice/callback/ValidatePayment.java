package payment.example.app.service.appservice.callback;

import payment.example.app.controller.dto.PaymentRequest;
import payment.example.common.domain.Item;

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

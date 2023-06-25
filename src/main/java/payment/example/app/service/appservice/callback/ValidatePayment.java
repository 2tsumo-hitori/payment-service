package payment.example.app.service.appservice.callback;

import payment.example.common.domain.Item;

public record ValidatePayment(
        Item serverItem, String impUid
) {

    public String getItemName() {
        return serverItem.getName();
    }

    public int getItemPrice() {
        return serverItem.getPrice();
    }
}

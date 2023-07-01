package com.payment.api.service.appservice.dto;

import lombok.Data;

@Data
public class PaymentCancelRequest {
    private String reason;
    private String impUid;
    private String amount;
}

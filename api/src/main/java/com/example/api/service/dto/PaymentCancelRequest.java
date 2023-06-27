package com.example.api.service.dto;

import lombok.Data;

@Data
public class PaymentCancelRequest {
    private String reason;
    private String impUid;
    private String amount;
}

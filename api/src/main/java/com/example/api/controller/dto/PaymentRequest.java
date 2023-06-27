package com.example.api.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private int amount;
    private String impUid;
    private String itemName;
    private Long memberId;
    private long quantity;
}

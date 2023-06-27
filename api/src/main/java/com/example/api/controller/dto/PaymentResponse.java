package com.example.api.controller.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class PaymentResponse<T> {
    private int status = HttpStatus.OK.value();
    private String message;
    private T data;

    public PaymentResponse(T data) {
        this.message = "정상 호출";
        this.data = data;
    }

    public PaymentResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}

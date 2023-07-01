package com.payment.api.controller.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Response<T> {
    private int status = HttpStatus.OK.value();
    private String message;
    private T data;

    public Response(T data) {
        this.message = "정상 호출";
        this.data = data;
    }

    public Response(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}

package com.payment.api.controller;

import com.payment.api.controller.dto.PaymentResponse;
import com.payment.paymentintegration.paymentmodule.exception.IamPortException.IamPortRunTimeException;
import com.payment.paymentintegration.paymentmodule.exception.IamPortException.IamPortRunTimeIoException;
import com.payment.common.exception.ItemStatusException;
import com.payment.common.exception.OutOfStockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApiAdvice {
    @ExceptionHandler(ItemStatusException.class)
    public PaymentResponse exception(ItemStatusException ex) {
        log.info(ex.getMessage(), ex);

        return new PaymentResponse(BAD_REQUEST.value(), ex.getMessage(), "결제 실패");
    }

    @ExceptionHandler(OutOfStockException.class)
    public PaymentResponse exception(OutOfStockException ex) {
        log.info(ex.getMessage(), ex);

        return new PaymentResponse(BAD_REQUEST.value(), ex.getMessage(), "결제 실패");
    }

    @ExceptionHandler(IamPortRunTimeException.class)
    public PaymentResponse exception(IamPortRunTimeException ex) {
        log.info(ex.getMessage(), ex);

        return new PaymentResponse(BAD_REQUEST.value(), ex.getMessage(), "결제 중 오류 발생");
    }

    @ExceptionHandler(IamPortRunTimeIoException.class)
    public PaymentResponse exception(IamPortRunTimeIoException ex) {
        log.info(ex.getMessage(), ex);

        return new PaymentResponse(BAD_REQUEST.value(), ex.getMessage(), "결제 중 오류 발생");
    }
}

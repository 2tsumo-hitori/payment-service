package payment.example.app.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import payment.example.common.exception.OutOfStockException;
import payment.example.app.controller.dto.PaymentResponse;
import payment.example.common.exception.ItemStatusException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApiAdvice {
    @ExceptionHandler(ItemStatusException.class)
    public PaymentResponse exception(ItemStatusException ex) {
        log.info(ex.getMessage(), ex);

        return new PaymentResponse(BAD_REQUEST, ex.getMessage(), "결제 실패");
    }

    @ExceptionHandler(OutOfStockException.class)
    public PaymentResponse exception(OutOfStockException ex) {
        log.info(ex.getMessage(), ex);

        return new PaymentResponse(BAD_REQUEST, ex.getMessage(), "결제 실패");
    }

    @Data
    @AllArgsConstructor
    static class ErrorResponse {
        private Object errorResponse;
    }
}
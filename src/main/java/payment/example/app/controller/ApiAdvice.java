package payment.example.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import payment.example.common.exception.IamPortException.*;
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

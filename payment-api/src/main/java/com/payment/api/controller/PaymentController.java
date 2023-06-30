package com.payment.api.controller;

import com.payment.api.controller.dto.OrderResponse;
import com.payment.api.controller.dto.PaymentRequest;
import com.payment.api.controller.dto.PaymentResponse;
import com.payment.common.aop.pointcut.Logger;
import com.payment.common.repository.dto.GetOrderDto;
import com.payment.api.service.purchase.PurchaseService;
import com.payment.api.service.appservice.PortOneAppService;
import com.payment.api.service.dto.PaymentCancelRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.payment.api.controller.dto.OrderResponse.*;


@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@Logger
public class PaymentController {

    private final PurchaseService paymentAppService;

    private final PortOneAppService portOneAppService;

    @GetMapping("page")
    public String index(HttpServletRequest request) {
        return "api/page";
    }

    @ResponseBody
    @PostMapping("/purchase")
    public PaymentResponse<OrderResponse> purchase(
            @RequestBody PaymentRequest request,
            HttpServletRequest req
    ) {
        GetOrderDto getOrderDto = paymentAppService.purchase(request);

        return new PaymentResponse<>(create(getOrderDto));
    }

    @ResponseBody
    @PostMapping("/payment/cancel")
    public PaymentResponse<String> paymentCancel(
            @RequestBody PaymentCancelRequest request
    ) {
        portOneAppService.paymentCancel(request);

        return new PaymentResponse<>("결제 취소 완료");
    }
}


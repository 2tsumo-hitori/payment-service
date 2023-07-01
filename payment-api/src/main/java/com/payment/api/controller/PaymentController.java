package com.payment.api.controller;

import com.payment.api.controller.dto.PaymentRequest;
import com.payment.api.controller.dto.Response;
import com.payment.common.aop.pointcut.Logger;
import com.payment.api.service.purchase.PurchaseService;
import com.payment.api.service.appservice.PortOneAppService;
import com.payment.api.service.appservice.dto.PaymentCancelRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



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
    public Response<?> purchase(
            @RequestBody PaymentRequest request,
            HttpServletRequest req
    ) {
        return new Response<>(paymentAppService.purchase(request));
    }

    @ResponseBody
    @PostMapping("/cancel")
    public Response<?> paymentCancel(
            @RequestBody PaymentCancelRequest request
    ) {
        portOneAppService.paymentCancel(request);

        return new Response<>("결제 취소 완료");
    }
}


package com.example.api.controller;

import com.example.api.controller.dto.OrderResponse;
import com.example.api.controller.dto.PaymentRequest;
import com.example.api.controller.dto.PaymentResponse;
import com.example.common.repository.dto.GetOrderDto;
import com.example.api.service.PaymentService;
import com.example.api.service.appservice.PortOneAppService;
import com.example.api.service.dto.PaymentCancelRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.example.api.controller.dto.OrderResponse.*;


@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentAppService;

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


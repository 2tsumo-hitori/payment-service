package payment.example.app.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import payment.example.app.controller.dto.OrderResponse;
import payment.example.app.repository.dto.GetOrderDto;
import payment.example.app.service.PaymentService;
import payment.example.app.service.dto.PaymentCancelRequest;
import payment.example.app.controller.dto.PaymentRequest;
import payment.example.app.controller.dto.PaymentResponse;
import payment.example.app.service.appservice.PortOneAppService;

import static payment.example.app.controller.dto.OrderResponse.create;

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

        return new PaymentResponse<>(HttpStatus.OK.value(), create(getOrderDto));
    }

    @ResponseBody
    @PostMapping("/payment/cancel")
    public PaymentResponse<String> paymentCancel(
            @RequestBody PaymentCancelRequest request
    ) {
        portOneAppService.paymentCancel(request);

        return new PaymentResponse<>(HttpStatus.OK.value(), "결제 취소 완료");
    }
}


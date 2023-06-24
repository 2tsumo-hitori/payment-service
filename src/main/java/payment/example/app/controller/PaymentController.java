package payment.example.app.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import payment.example.app.service.appservice.PaymentAppService;
import payment.example.app.service.dto.PaymentCancelRequest;
import payment.example.app.controller.dto.PaymentRequest;
import payment.example.app.controller.dto.PaymentResponse;
import payment.example.app.repository.dto.OrderResponse;
import payment.example.app.service.appservice.PortOneAppService;

import java.io.IOException;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentAppService paymentAppService;

    private final PortOneAppService portOneAppService;

    @GetMapping("page")
    public String index(HttpServletRequest request) {
        return "api/page";
    }

    @ResponseBody
    @PostMapping("/payment-validate")
    public PaymentResponse<OrderResponse> paymentByImpUid(
            @RequestBody PaymentRequest request,
            HttpServletRequest req
    ) throws IamportResponseException, IOException {
        OrderResponse orderResponse = paymentAppService.paymentValidate(request);

        return new PaymentResponse<>(HttpStatus.OK, orderResponse);
    }

    @ResponseBody
    @PostMapping("/payment/cancel")
    public PaymentResponse<String> paymentCancel(
            @RequestBody PaymentCancelRequest request
    ) {
        portOneAppService.paymentCancel(request);

        return new PaymentResponse<>(HttpStatus.OK, "결제 취소 완료");
    }
}


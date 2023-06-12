package payment.example.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import payment.example.controller.dto.PaymentRequest;
import payment.example.controller.dto.PaymentResponse;
import payment.example.repository.dto.OrderResponse;
import payment.example.service.appservice.PaymentAppService;
import payment.example.service.dto.PaymentCancelRequest;
import payment.example.service.appservice.PortOneAppService;

import java.io.IOException;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentAppService paymentAppService;
    private final PortOneAppService portOneAppService;

    @GetMapping("page")
    public String index() {
        return "api/page";
    }

    @ResponseBody
    @PostMapping("/payment-validate")
    public PaymentResponse<OrderResponse> paymentByImpUid(
            @RequestBody PaymentRequest request
    ) throws IamportResponseException, IOException {
        return new PaymentResponse<>(HttpStatus.OK, paymentAppService.paymentValidate(request));
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


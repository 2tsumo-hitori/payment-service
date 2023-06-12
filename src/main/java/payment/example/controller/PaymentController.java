package payment.example.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import payment.example.controller.dto.PaymentRequest;
import payment.example.controller.dto.PaymentResponse;
import payment.example.domain.Order;
import payment.example.repository.dto.OrderResponse;
import payment.example.service.PaymentService;
import payment.example.service.appservice.PaymentCancelRequest;
import payment.example.service.appservice.RestTemplateService;

import java.io.IOException;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final RestTemplateService restTemplateService;

    @GetMapping("page")
    public String index() {
        return "api/page";
    }

    @ResponseBody
    @PostMapping("/payment-validate")
    public PaymentResponse<OrderResponse> paymentByImpUid(
            @RequestBody PaymentRequest request
    ) throws IamportResponseException, IOException
    {
        return new PaymentResponse<>(HttpStatus.OK, paymentService.paymentValidate(request));
    }

    @ResponseBody
    @PostMapping("/payment/cancel")
    public PaymentResponse<String> paymentCancel(@RequestBody PaymentCancelRequest request) {
        restTemplateService.paymentCancel(request);

        return new PaymentResponse<>(HttpStatus.OK, "결제 취소 완료");
    }
}


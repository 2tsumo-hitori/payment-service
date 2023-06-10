package payment.example.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import payment.example.service.PaymentService;

import java.io.IOException;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("page")
    public String index() {
        return "api/page";
    }

    @ResponseBody
    @PostMapping(value="/paymentValidate/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(
            @PathVariable(value= "imp_uid") String imp_uid) throws IamportResponseException, IOException
    {
        return paymentService.paymentValidate(imp_uid);
    }

    // 장바구니 구매

    // 구매페이지에서 직접 구매
}


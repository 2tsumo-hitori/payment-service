package com.example.api.service.appservice;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNull;

class PortOneAppServiceTest {

    IamportClient iamportClient;

    final String test_imp_uid = "imp_448280090638";

    @BeforeEach
    public void setup() {
        String test_api_key = "imp_apikey";
        String test_api_secret = "ekKoeW8RyKuT0zgaZsUtXXTLQ4AhPFW3ZGseDA6bkA5lamv9OqDMnxyeB9wqOsuO9W3Mx9YSJ4dTqJ3f";
        iamportClient = new IamportClient(test_api_key, test_api_secret);
    }

    @Test
    public void 결제_취소_성공() throws IamportResponseException, IOException {
        CancelData cancel_data = new CancelData(test_imp_uid, true);
        cancel_data.setChecksum(BigDecimal.valueOf(500));

        IamportResponse<Payment> payment_response = iamportClient.cancelPaymentByImpUid(cancel_data);

        assertNull(payment_response.getResponse());
    }
}
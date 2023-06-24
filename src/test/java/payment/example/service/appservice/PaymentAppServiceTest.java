package payment.example.service.appservice;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import payment.example.app.controller.dto.PaymentRequest;
import payment.example.app.repository.ItemRepository;
import payment.example.app.repository.dto.OrderResponse;
import payment.example.app.service.OrderService;
import payment.example.app.service.appservice.PaymentAppService;
import payment.example.common.domain.Item;
import payment.example.common.domain.Stock;
import payment.example.common.exception.ItemStatusException;


import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class PaymentAppServiceTest {
    IamportClient iamportClient;
    PaymentAppService paymentAppService;

    final String test_imp_uid = "imp_448280090638";

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderService orderService;

    @BeforeEach
    public void setup() {
        String test_api_key = "imp_apikey";
        String test_api_secret = "ekKoeW8RyKuT0zgaZsUtXXTLQ4AhPFW3ZGseDA6bkA5lamv9OqDMnxyeB9wqOsuO9W3Mx9YSJ4dTqJ3f";
        iamportClient = new IamportClient(test_api_key, test_api_secret);

        paymentAppService = new PaymentAppService(iamportClient, itemRepository, orderService);
    }

    @Test
    void 포트원_결제_검증_성공() throws IamportResponseException, IOException {
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(test_imp_uid);

        System.out.println(response.getResponse().getName());
        System.out.println(response.getResponse().getAmount());
        System.out.println(response.getResponse());

        assertNotNull(response.getResponse());
        assertEquals(test_imp_uid, response.getResponse().getImpUid());
    }

    @Test
    void PaymentAppService_결제_검증_성공() throws IamportResponseException, IOException {
        itemRepository.save(Item.builder()
                .name("결제테스트")
                .price(1004)
                .stock(Stock.builder().remain(100).build())
                .build());

        PaymentRequest request = new PaymentRequest(1004, test_imp_uid, "결제테스트", 1L, 5);

        OrderResponse orderResponse = paymentAppService.paymentValidate(request);

        assertThat(orderResponse).isNotNull();
    }

    @Test
    void PaymentAppService_결제_검증_실패__상품이_존재하지_않음() {
        PaymentRequest request = new PaymentRequest(1004, test_imp_uid, "결제테스트", 1L, 5);

        assertThatThrownBy(() -> paymentAppService.paymentValidate(request)).isInstanceOf(ItemStatusException.class);
    }

    @Test
    void PaymentAppService_결제_검증_실패__상품_이름이_일치하지_않음() {
        itemRepository.save(Item.builder()
                .name("test")
                .price(1004)
                .stock(Stock.builder().remain(100).build())
                .build());

        PaymentRequest request = new PaymentRequest(1004, test_imp_uid, "fakeTest", 1L, 5);

        assertThatThrownBy(() -> paymentAppService.paymentValidate(request)).isInstanceOf(ItemStatusException.class);
    }

    @Test
    void PaymentAppService_결제_검증_실패__상품_가격이_일치하지_않음() {
        itemRepository.save(Item.builder()
                .name("test")
                .price(1004)
                .stock(Stock.builder().remain(100).build())
                .build());

        PaymentRequest request = new PaymentRequest(1003, test_imp_uid, "test", 1L, 5);

        assertThatThrownBy(() -> paymentAppService.paymentValidate(request)).isInstanceOf(ItemStatusException.class);
    }
}
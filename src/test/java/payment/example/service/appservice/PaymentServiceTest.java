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
import payment.example.app.repository.dto.GetOrderDto;
import payment.example.app.service.appservice.callback.IamPortTemplate;
import payment.example.app.service.OrderService;
import payment.example.app.service.PaymentService;
import payment.example.common.domain.Item;
import payment.example.common.domain.Stock;
import payment.example.common.exception.ItemStatusException;


import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class PaymentServiceTest {
    IamportClient iamportClient;
    PaymentService paymentAppService;
    IamPortTemplate iamPortTemplate;
    final String TEST_IMP_UID = "imp_448280090638";

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderService orderService;

    @BeforeEach
    public void setup() {
        String test_api_key = "imp_apikey";
        String test_api_secret = "ekKoeW8RyKuT0zgaZsUtXXTLQ4AhPFW3ZGseDA6bkA5lamv9OqDMnxyeB9wqOsuO9W3Mx9YSJ4dTqJ3f";
        iamportClient = new IamportClient(test_api_key, test_api_secret);

        iamPortTemplate = new IamPortTemplate(iamportClient);

        paymentAppService = new PaymentService(itemRepository, orderService, iamPortTemplate);
    }

    @Test
    void 포트원_결제_검증_성공() throws IamportResponseException, IOException {
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(TEST_IMP_UID);

        System.out.println(response.getResponse().getName());
        System.out.println(response.getResponse().getAmount());
        System.out.println(response.getResponse());

        assertNotNull(response.getResponse());
        assertEquals(TEST_IMP_UID, response.getResponse().getImpUid());
    }

    @Test
    void PaymentAppService_결제_검증_성공() {
        itemRepository.save(Item.builder()
                .name("결제테스트")
                .price(1004)
                .stock(Stock.builder().remain(100).build())
                .build());

        PaymentRequest request = new PaymentRequest(1004, TEST_IMP_UID, "결제테스트", 1L, 5);

        GetOrderDto orderResponse = paymentAppService.purchase(request);

        assertThat(orderResponse).isNotNull();
    }

    @Test
    void PaymentAppService_결제_검증_실패__상품이_존재하지_않음() {
        PaymentRequest request = new PaymentRequest(1004, TEST_IMP_UID, "결제테스트", 1L, 5);

        assertThatThrownBy(() -> paymentAppService.purchase(request)).isInstanceOf(ItemStatusException.class);
    }

    @Test
    void PaymentAppService_결제_검증_실패__상품_이름이_일치하지_않음() {
        itemRepository.save(Item.builder()
                .name("test")
                .price(1004)
                .stock(Stock.builder().remain(100).build())
                .build());

        PaymentRequest request = new PaymentRequest(1004, TEST_IMP_UID, "fakeTest", 1L, 5);

        assertThatThrownBy(() -> paymentAppService.purchase(request)).isInstanceOf(ItemStatusException.class);
    }

    @Test
    void PaymentAppService_결제_검증_실패__상품_가격이_일치하지_않음() {
        itemRepository.save(Item.builder()
                .name("test")
                .price(1004)
                .stock(Stock.builder().remain(100).build())
                .build());

        PaymentRequest request = new PaymentRequest(1003, TEST_IMP_UID, "test", 1L, 5);

        assertThatThrownBy(() -> paymentAppService.purchase(request)).isInstanceOf(ItemStatusException.class);
    }
}
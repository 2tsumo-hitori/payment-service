package com.payment.api.service.appservice;

import com.payment.api.controller.dto.PaymentRequest;
import com.payment.api.service.OrderService;
import com.payment.api.service.PaymentService;
import com.payment.common.domain.Item;
import com.payment.common.domain.Stock;
import com.payment.common.exception.ItemStatusException;
import com.payment.common.repository.ItemRepository;
import com.payment.common.repository.dto.GetOrderDto;
import com.payment.paymentintegration.paymentmodule.DefaultPaymentClient;
import com.payment.paymentintegration.paymentmodule.IamPortTemplate;
import com.payment.paymentintegration.paymentmodule.ValidatePayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderService orderService;

    @Mock
    IamPortTemplate iamPortTemplate;

    PaymentService paymentAppService;

    final String TEST_IMP_UID = "imp_448280090638";


    @BeforeEach
    public void setup() {
        paymentAppService = new PaymentService(itemRepository, orderService, iamPortTemplate);
    }

    @Test
    void PaymentAppService_결제_검증_성공() {
        Item item = itemRepository.save(Item.builder()
                .name("결제테스트")
                .price(1004)
                .stock(Stock.builder().remain(100).build())
                .build());

        lenient().when(iamPortTemplate.execute(any(), any()))
                .thenReturn(new GetOrderDto(1L, 1L, "1", item.getId(), item.getName()));

        PaymentRequest request = new PaymentRequest(1004, TEST_IMP_UID, "결제테스트", 1L, 5);

        GetOrderDto orderResponse = paymentAppService.purchase(request);

        assertThat(orderResponse).isNotNull();
    }

    @Test
    void PaymentAppService_결제_검증_실패__상품이_존재하지_않음() {
        PaymentRequest request = new PaymentRequest(1004, TEST_IMP_UID, "결제테스트", 1L, 5);

        lenient().when(iamPortTemplate.execute(any(), any()))
                .thenReturn(new GetOrderDto(1L, 1L, "1", 0L, "결제테스트"));

        assertThatThrownBy(() -> paymentAppService.purchase(request)).isInstanceOf(ItemStatusException.class);
    }

    @Test
    void PaymentAppService_결제_검증_실패__상품_이름이_일치하지_않음() {
        itemRepository.save(Item.builder()
                .name("test")
                .price(1004)
                .stock(Stock.builder().remain(100).build())
                .build());

        lenient().when(iamPortTemplate.execute(any(), any()))
                .thenReturn(new GetOrderDto(1L, 1L, "1", 0L, "fakeTest"));

        PaymentRequest request = new PaymentRequest(1004, TEST_IMP_UID, "fakeTest", 1L, 5);

        assertThatThrownBy(() -> paymentAppService.purchase(request)).isInstanceOf(ItemStatusException.class);
    }

    @Test
    void PaymentAppService_결제_검증_실패__상품_가격이_일치하지_않음() {
        Item item = itemRepository.save(Item.builder()
                .name("test")
                .price(1004)
                .stock(Stock.builder().remain(100).build())
                .build());

        lenient().when(iamPortTemplate.execute(any(), any()))
                .thenReturn(new GetOrderDto(1L, 1L, "1", item.getId(), item.getName()));


        PaymentRequest request = new PaymentRequest(1003, TEST_IMP_UID, "결제테스트", 1L, 5);

        assertThat(request.getAmount()).isNotEqualTo(1004);
    }
}
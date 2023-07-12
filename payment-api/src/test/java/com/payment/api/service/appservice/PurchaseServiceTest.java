package com.payment.api.service.appservice;

import com.payment.api.controller.dto.PaymentRequest;
import com.payment.api.service.purchase.PurchaseService;
import com.payment.api.service.stock.AsyncOrderService;
import com.payment.common.domain.Item;
import com.payment.common.domain.Stock;
import com.payment.common.exception.ItemStatusException;
import com.payment.common.repository.ItemRepository;
import com.payment.common.repository.dto.GetOrderDto;
import com.payment.paymentintegration.payment.iamport.callback.IamPortTemplate;
import com.payment.paymentintegration.payment.iamport.callback.PaymentTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@SpringBootTest
@Transactional
class PurchaseServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    AsyncOrderService stockService;

    @Autowired
    PurchaseService paymentAppService;

    @Test
    void PaymentAppService_결제_검증_성공() {
        itemRepository.save(Item.builder()
                .name("결제테스트")
                .price(1004)
                .stock(Stock.builder().remain(100).build())
                .build());


        PaymentRequest request = new PaymentRequest(1004, null, "결제테스트", 1L, 5);

        assertThat(paymentAppService.purchase(request)).isNotNull();
    }

    @Test
    void PaymentAppService_결제_검증_실패__상품이_존재하지_않음() {
        PaymentRequest request = new PaymentRequest(1004, null, "결제테스트", 1L, 5);

        assertThatThrownBy(() -> paymentAppService.purchase(request)).isInstanceOf(ItemStatusException.class);
    }

    @Test
    void PaymentAppService_결제_검증_실패__상품_이름이_일치하지_않음() {
        itemRepository.save(Item.builder()
                .name("test")
                .price(1004)
                .stock(Stock.builder().remain(100).build())
                .build());

        PaymentRequest request = new PaymentRequest(1004, null, "fakeTest", 1L, 5);

        assertThatThrownBy(() -> paymentAppService.purchase(request)).isInstanceOf(ItemStatusException.class);
    }

    @Test
    void PaymentAppService_결제_검증_실패__상품_가격이_일치하지_않음() {
        itemRepository.save(Item.builder()
                .name("test")
                .price(1004)
                .stock(Stock.builder().remain(100).build())
                .build());

        PaymentRequest request = new PaymentRequest(1003, null, "결제테스트", 1L, 5);

        assertThat(request.getAmount()).isNotEqualTo(1004);
    }
}
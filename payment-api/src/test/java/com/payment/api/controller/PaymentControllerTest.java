package com.payment.api.controller;

import com.payment.api.controller.dto.PaymentRequest;
import com.payment.api.support.ApiTest;
import com.payment.common.domain.*;
import com.payment.common.repository.ItemRepository;
import com.payment.common.repository.MemberRepository;
import com.payment.common.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
class PaymentControllerTest extends ApiTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderRepository orderRepository;

    Member member;

    Item item;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(Member.builder()
                .name("고객")
                .point(100)
                .address(Address.builder()
                        .city("대구시")
                        .street("달서구")
                        .zipcode("신당동")
                        .build())
                .build());

        item = itemRepository.save(Item.builder()
                .name("상품1")
                .price(100)
                .stock(Stock.builder().remain(100).build())
                .build());
    }

    @Test
    void 구매_성공() throws Exception {
        apiTestHelper(POST,
                new PaymentRequest(100, null, "상품1", 1L, 5),
                "/api/purchase");
    }
}
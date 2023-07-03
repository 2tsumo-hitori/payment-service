package com.payment.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.api.controller.dto.PaymentRequest;
import com.payment.common.domain.*;
import com.payment.common.repository.ItemRepository;
import com.payment.common.repository.MemberRepository;
import com.payment.common.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
class PaymentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderRepository orderRepository;

    Member member;

    Item item;

    final String TEST_IMP_UID = "imp_130043161733";

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();

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
        PaymentRequest paymentRequest = new PaymentRequest(100, TEST_IMP_UID, "상품1", 1L, 5);

        mockMvc.perform(post("/api/purchase")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("정상 호출"))
                .andExpect(jsonPath("$.data").value("결제 완료"));
    }
}
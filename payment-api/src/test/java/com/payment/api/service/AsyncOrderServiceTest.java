package com.payment.api.service;

import com.payment.api.service.stock.AsyncOrderService;
import com.payment.common.domain.Address;
import com.payment.common.domain.Item;
import com.payment.common.domain.Member;
import com.payment.common.domain.Stock;
import com.payment.common.repository.ItemRepository;
import com.payment.common.repository.MemberRepository;
import com.payment.common.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class AsyncOrderServiceTest {

    @Autowired
    AsyncOrderService stockService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderRepository orderRepository;

    Member member;
    Item item;

    private static final String PAYMENT_SUCCESS = "결제 완료";

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
                .name("상품2")
                .price(100)
                .stock(Stock.builder().remain(1000).build())
                .build());

    }

    @Test
    void 구매_성공() {
        String success = stockService.decrease(item, member.getId(), item.getStock().getRemain());

        Assertions.assertThat(success).isEqualTo(PAYMENT_SUCCESS);
    }

    @Test
    void 구매_실패__재고_부족() {
        Assertions.assertThatThrownBy(() ->
                stockService.decrease(item, member.getId(), item.getStock().getRemain() + 1L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 주문_생성_동시성_테스트_성공() throws InterruptedException {
        long beforeStock = itemRepository.findById(item.getId()).orElseThrow().getQuantity();

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                    try {
                        stockService.decrease(item, member.getId(), 1L);
                    }
                    finally {
                        latch.countDown();
                    }
                }
            );
        }

        latch.await();

        Assertions.assertThat(beforeStock - threadCount).isZero();
    }

    @Test
    void 주문_생성_동시성_테스트_실패__수량보다_많은_요청() throws InterruptedException {
        int threadCount = 1001;
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                    try {
                        stockService.decrease(item, member.getId(), 1L);
                    } catch (Exception e) {
                        Assertions.assertThat(e.getMessage()).isEqualTo("재고 부족");
                    }
                    finally {
                        latch.countDown();
                    }
                }
            );
        }

        latch.await();
    }
}
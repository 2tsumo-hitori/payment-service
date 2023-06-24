package payment.example.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import payment.example.domain.*;
import payment.example.repository.ItemRepository;
import payment.example.repository.MemberRepository;
import payment.example.repository.OrderRepository;
import payment.example.repository.dto.OrderResponse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

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
                .name("상품2")
                .price(100)
                .stock(Stock.builder().remain(1000).build())
                .build());
    }

    @AfterEach
    public void after() {
        orderRepository.deleteAll();
        itemRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    void 주문_생성_성공() {
        OrderResponse orderResponse = orderService.makeOrder(item, member.getId(), 5);

        Assertions.assertThat(orderResponse).isNotNull();
    }

    @Test
    void 주문_생성_동시성_테스트_성공() throws InterruptedException {

        long beforeStock = itemRepository.findById(item.getId()).orElseThrow().getQuantity();

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(1000);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                    try {
                        orderService.makeOrder(item, member.getId(), 1);
                    }
                    finally {
                        latch.countDown();
                    }
                }
            );
        }

        latch.await();

        Assertions.assertThat(beforeStock - threadCount).isZero();
        Assertions.assertThat(orderRepository.findAll().size()).isEqualTo(threadCount);
    }
}
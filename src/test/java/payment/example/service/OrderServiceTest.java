package payment.example.service;

import org.assertj.core.api.Assertions;
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


@SpringBootTest
@Transactional
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
                .stock(100L)
                .build());
    }

    @Test
    void 주문_생성_성공() {
        OrderResponse orderResponse = orderService.makeOrder(item, member.getId());

        Assertions.assertThat(orderResponse).isNotNull();
    }

    @Test
    void 주문_생성_성공__주문_대기_상태() {
        Item zeroStockItem = itemRepository.save(Item.builder()
                .name("상품3")
                .price(100)
                .stock(0L)
                .build());

        OrderResponse orderResponse = orderService.makeOrder(zeroStockItem, member.getId());

        Assertions.assertThat(orderResponse.getStatus()).isEqualTo(OrderStatus.주문_대기);
    }
}
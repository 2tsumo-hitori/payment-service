package payment.example.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import payment.example.common.domain.Address;
import payment.example.common.domain.Item;
import payment.example.common.domain.Member;
import payment.example.common.domain.Order;

import static org.assertj.core.api.Assertions.*;


class OrderTest {
    Member member;

    Item item;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .name("고객")
                .point(100)
                .address(Address.builder()
                        .city("대구시")
                        .street("달서구")
                        .zipcode("신당동")
                        .build())
                .build();

        item = Item.builder()
                .name("상품1")
                .price(100)
                .stock(100L)
                .build();
    }

    @Test
    void 주문_생성_성공() {
        Order order = Order
                .builder()
                .member(member)
                .item(item)
                .build();

        assertThat(order.getMember()).isNotNull();
        assertThat(order.getItem()).isNotNull();
    }

    @Test
    void 주문_생성_실패__회원이_널() {
        assertThatThrownBy(() -> Order
                .builder()
                .member(null)
                .item(item)
                .build()).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성_실패__상품이_널() {
        assertThatThrownBy(() -> Order
                .builder()
                .member(member)
                .item(null)
                .build()).isInstanceOf(IllegalArgumentException.class);
    }
}
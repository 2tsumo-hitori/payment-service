package payment.example.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import payment.example.common.domain.Item;

import static org.assertj.core.api.Assertions.*;

class ItemTest {
    @Test
    void 상품_생성_성공() {
        Item item = Item.builder()
                .name("상품1")
                .price(100)
                .stock(100L)
                .build();

        assertThat(item.getName()).isEqualTo("상품1");
        assertThat(item.getPrice()).isEqualTo(100);
        assertThat(item.getStock()).isEqualTo(100L);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 상품_생성_실패__이름이_공백_혹은_널(String name) {
        assertThatThrownBy(() -> Item.builder()
                .name(name)
                .price(100)
                .stock(100L)
                .build()).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_생성_실패__가격이_0원_보다_작음() {
        assertThatThrownBy(() -> Item.builder()
                .name("상품")
                .price(-1)
                .stock(100L)
                .build()).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_생성_실패__재고가_0_보다_작음() {
        assertThatThrownBy(() -> Item.builder()
                .name("상품")
                .price(100)
                .stock(-1L)
                .build()).isInstanceOf(IllegalArgumentException.class);
    }
}
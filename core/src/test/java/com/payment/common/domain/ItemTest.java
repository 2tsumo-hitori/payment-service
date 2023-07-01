package com.payment.common.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ItemTest {
    @Test
    void 상품_생성_성공() {
        Item item = Item.builder()
                .name("상품1")
                .price(100)
                .stock(Stock.builder().remain(100).build())
                .build();

        assertThat(item.getName()).isEqualTo("상품1");
        assertThat(item.getPrice()).isEqualTo(100);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 상품_생성_실패__이름이_공백_혹은_널(String name) {
        assertThatThrownBy(() -> Item.builder()
                .name(name)
                .price(100)
                .stock(Stock.builder().remain(100).build())
                .build()).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_생성_실패__가격이_0원_보다_작음() {
        assertThatThrownBy(() -> Item.builder()
                .name("상품")
                .price(-1)
                .stock(Stock.builder().remain(100).build())
                .build()).isInstanceOf(IllegalArgumentException.class);
    }
}